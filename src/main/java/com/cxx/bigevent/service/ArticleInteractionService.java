package com.cxx.bigevent.service;

import com.cxx.bigevent.pojo.ArticleCollect;
import com.cxx.bigevent.pojo.ArticleCollectVO;

import java.util.List;

public interface ArticleInteractionService {
    void likeArticle(Long articleId, Long userId);
    void unlikeArticle(Long articleId, Long userId);
    boolean isLiked(Long articleId, Long userId);
    int getLikeCount(Long articleId);

    void collectArticle(ArticleCollect collect);
    void uncollectArticle(Long articleId, Long userId);
    boolean isCollected(Long articleId, Long userId);
    List<ArticleCollectVO> getMyCollections(Long userId);
}
