package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.mapper.ArticleCollectMapper;
import com.cxx.bigevent.mapper.ArticleLikeMapper;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.ArticleStatsMapper;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.ArticleCollect;
import com.cxx.bigevent.pojo.ArticleCollectVO;
import com.cxx.bigevent.pojo.ArticleLike;
import com.cxx.bigevent.service.ArticleInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleInteractionServiceImpl implements ArticleInteractionService {

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private ArticleCollectMapper articleCollectMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleStatsMapper articleStatsMapper;

    @Override
    public void likeArticle(Long articleId, Long userId) {
        ArticleLike existing = articleLikeMapper.findByArticleAndUser(articleId, userId);
        if (existing != null) {
            throw new BusinessException(ErrorCode.ERROR, "已点赞过该文章");
        }

        Article article = articleMapper.findByIdOnly(articleId);
        if (article == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        ArticleLike like = new ArticleLike();
        like.setArticleId(articleId);
        like.setUserId(userId);
        articleLikeMapper.insert(like);

        articleStatsMapper.incrementLikeCount(articleId);
    }

    @Override
    public void unlikeArticle(Long articleId, Long userId) {
        ArticleLike existing = articleLikeMapper.findByArticleAndUser(articleId, userId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.ERROR, "尚未点赞该文章");
        }
        articleLikeMapper.delete(articleId, userId);
        articleStatsMapper.decrementLikeCount(articleId);
    }

    @Override
    public boolean isLiked(Long articleId, Long userId) {
        return articleLikeMapper.findByArticleAndUser(articleId, userId) != null;
    }

    @Override
    public int getLikeCount(Long articleId) {
        return articleLikeMapper.countByArticleId(articleId);
    }

    @Override
    public void collectArticle(ArticleCollect collect) {
        ArticleCollect existing = articleCollectMapper.findByArticleAndUser(collect.getArticleId(), collect.getUserId());
        if (existing != null) {
            throw new BusinessException(ErrorCode.ERROR, "已收藏过该文章");
        }

        Article article = articleMapper.findByIdOnly(collect.getArticleId());
        if (article == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        if (collect.getFolderName() == null || collect.getFolderName().isEmpty()) {
            collect.setFolderName("默认收藏");
        }

        articleCollectMapper.insert(collect);
        articleStatsMapper.incrementCollectCount(collect.getArticleId());
    }

    @Override
    public void uncollectArticle(Long articleId, Long userId) {
        ArticleCollect existing = articleCollectMapper.findByArticleAndUser(articleId, userId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.ERROR, "尚未收藏该文章");
        }
        articleCollectMapper.delete(articleId, userId);
        articleStatsMapper.decrementCollectCount(articleId);
    }

    @Override
    public boolean isCollected(Long articleId, Long userId) {
        return articleCollectMapper.findByArticleAndUser(articleId, userId) != null;
    }

    @Override
    public List<ArticleCollectVO> getMyCollections(Long userId) {
        return articleCollectMapper.findByUserId(userId);
    }
}
