package com.cxx.bigevent.service;

import com.cxx.bigevent.dto.SearchRequest;
import com.cxx.bigevent.dto.SearchResponse;
import com.cxx.bigevent.pojo.Article;

import java.util.List;

public interface SearchService {

    SearchResponse search(SearchRequest request);

    List<String> suggest(String prefix);

    void syncArticleToEs(Article article);

    void removeArticleFromEs(Long articleId);

    void syncAllArticles();
}
