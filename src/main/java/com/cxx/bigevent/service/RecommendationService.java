package com.cxx.bigevent.service;

import com.cxx.bigevent.pojo.Article;

import java.util.List;

public interface RecommendationService {
    List<Article> getRecommendations(Long userId, int limit);
    void recordBehavior(Long userId, Long articleId, String behaviorType, Integer duration);
    List<Article> getSimilarArticles(Long articleId, int limit);
    List<Article> getHotArticles(int limit);
}
