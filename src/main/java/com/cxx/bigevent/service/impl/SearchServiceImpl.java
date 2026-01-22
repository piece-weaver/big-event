package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.dto.SearchRequest;
import com.cxx.bigevent.dto.SearchResponse;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.CategoryMapper;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Category;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.service.SearchService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String HOT_KEYWORDS_KEY = "search:hot:keywords";

    @Override
    public SearchResponse search(SearchRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());

        List<Article> articles = articleMapper.search(
                request.getKeyword(),
                request.getCategoryId(),
                request.getState()
        );

        Page<Article> p = (Page<Article>) articles;

        SearchResponse response = new SearchResponse();
        response.setTotal(p.getTotal());
        response.setPageNum(request.getPageNum());
        response.setPageSize(request.getPageSize());

        List<SearchResponse.ArticleSearchResult> results = new ArrayList<>();
        for (Article article : p.getResult()) {
            SearchResponse.ArticleSearchResult result = new SearchResponse.ArticleSearchResult();
            result.setId(Long.valueOf(article.getId()));
            result.setTitle(article.getTitle());
            result.setContent(article.getContent());
            result.setCoverImg(article.getCoverImg());
            result.setCategoryId(article.getCategoryId());
            result.setViewCount(0L);
            result.setLikeCount(0L);
            result.setCreateTime(article.getCreateTime() != null ?
                    article.getCreateTime().toString() : null);

            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                result.setHighlightTitle(highlight(article.getTitle(), request.getKeyword()));
                result.setHighlightContent(highlight(article.getContent(), request.getKeyword()));
            }

            results.add(result);
        }
        response.setResults(results);

        incrementHotKeywords(request.getKeyword());

        return response;
    }

    private List<String> highlight(String text, String keyword) {
        List<String> highlights = new ArrayList<>();
        if (text == null || keyword == null) return highlights;

        String lowerText = text.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();

        int index = lowerText.indexOf(lowerKeyword);
        if (index >= 0) {
            int start = Math.max(0, index - 20);
            int end = Math.min(text.length(), index + keyword.length() + 20);
            String snippet = text.substring(start, end);
            if (start > 0) snippet = "..." + snippet;
            if (end < text.length()) snippet = snippet + "...";
            highlights.add(snippet.replaceAll("(?i)(" + keyword + ")", "<em>$1</em>"));
        } else {
            highlights.add(text.length() > 100 ? text.substring(0, 100) + "..." : text);
        }
        return highlights;
    }

    @Override
    public List<String> suggest(String prefix) {
        if (prefix == null || prefix.length() < 1) {
            return List.of();
        }
        List<Article> articles = articleMapper.search(prefix, null, 1);
        List<String> suggestions = new ArrayList<>();
        for (Article article : articles) {
            if (article.getTitle().toLowerCase().contains(prefix.toLowerCase())) {
                suggestions.add(article.getTitle());
            }
        }
        return suggestions.stream().limit(10).toList();
    }

    @Override
    public void syncArticleToEs(Article article) {
        // MySQL搜索不需要同步，保留接口兼容性
    }

    @Override
    public void removeArticleFromEs(Long articleId) {
        // MySQL搜索不需要同步，保留接口兼容性
    }

    @Override
    public void syncAllArticles() {
        // MySQL搜索不需要同步，保留接口兼容性
    }

    private void incrementHotKeywords(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return;
        stringRedisTemplate.opsForZSet().incrementScore(HOT_KEYWORDS_KEY, keyword.trim(), 1);
        stringRedisTemplate.expire(HOT_KEYWORDS_KEY, 7, TimeUnit.DAYS);
    }

    public List<String> getHotKeywords(int limit) {
        java.util.Set<String> keywords = stringRedisTemplate.opsForZSet().reverseRange(HOT_KEYWORDS_KEY, 0, limit - 1);
        return keywords != null ? new ArrayList<>(keywords) : List.of();
    }
}
