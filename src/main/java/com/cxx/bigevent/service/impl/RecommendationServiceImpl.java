package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.UserBehaviorMapper;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.UserBehavior;
import com.cxx.bigevent.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final String HOT_ARTICLES_KEY = "recommendation:hot";
    private static final int CACHE_MINUTES = 30;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Article> getRecommendations(Long userId, int limit) {
        if (userId == null) {
            return getHotArticles(limit);
        }

        List<Long> viewedIds = userBehaviorMapper.findViewedArticleIds(userId);
        List<Long> recommendedIds = articleMapper.findRandomArticleIds(viewedIds, limit);

        List<Article> result = new ArrayList<>();
        if (recommendedIds != null && !recommendedIds.isEmpty()) {
            result = articleMapper.findByIds(recommendedIds);
            result = result.stream().filter(a -> a != null).collect(Collectors.toList());
        }

        if (result.size() < limit) {
            List<Article> hotArticles = getHotArticles(limit - result.size());
            List<Long> existingIds = result.stream().map(a -> a.getId().longValue()).collect(Collectors.toList());
            for (Article article : hotArticles) {
                if (!existingIds.contains(article.getId().longValue())) {
                    result.add(article);
                }
            }
        }

        return result.size() > limit ? result.subList(0, limit) : result;
    }

    @Override
    public void recordBehavior(Long userId, Long articleId, String behaviorType, Integer duration) {
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setArticleId(articleId);
        behavior.setBehaviorType(behaviorType);
        behavior.setDuration(duration);
        userBehaviorMapper.insert(behavior);

        invalidateHotCache();
    }

    @Override
    public List<Article> getSimilarArticles(Long articleId, int limit) {
        Integer categoryId = userBehaviorMapper.findCategoryIdByArticleId(articleId);
        if (categoryId == null) {
            return getHotArticles(limit);
        }

        List<Article> articles = articleMapper.search(null, categoryId, 1);
        List<Article> result = new ArrayList<>();
        for (Article article : articles) {
            if (!article.getId().equals(articleId) && result.size() < limit) {
                result.add(article);
            }
        }
        return result;
    }

    @Override
    public List<Article> getHotArticles(int limit) {
        List<Long> cachedIds = (List<Long>) redisTemplate.opsForValue().get(HOT_ARTICLES_KEY);
        if (cachedIds != null && !cachedIds.isEmpty()) {
            List<Article> result = articleMapper.findByIds(cachedIds);
            result = result.stream().filter(a -> a != null).limit(limit).collect(Collectors.toList());
            if (!result.isEmpty()) {
                return result;
            }
        }

        List<Article> hotArticles = articleMapper.list(null, 1, 1);
        List<Long> ids = hotArticles.stream()
                .map(a -> a.getId().longValue())
                .collect(Collectors.toList());

        if (!ids.isEmpty()) {
            redisTemplate.opsForValue().set(HOT_ARTICLES_KEY, ids, CACHE_MINUTES, TimeUnit.MINUTES);
        }

        return hotArticles.size() > limit ? hotArticles.subList(0, limit) : hotArticles;
    }

    private void invalidateHotCache() {
        redisTemplate.delete(HOT_ARTICLES_KEY);
    }
}
