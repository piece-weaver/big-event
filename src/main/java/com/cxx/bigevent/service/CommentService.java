package com.cxx.bigevent.service;

import com.cxx.bigevent.pojo.Comment;
import com.cxx.bigevent.pojo.CommentVO;

import java.util.List;

public interface CommentService {
    void add(Comment comment);

    List<CommentVO> findByArticleId(Long articleId);

    void update(Comment comment);

    void delete(Long id, Long userId);

    void likeComment(Long id);

    int countByArticleId(Long articleId);

    void incrementCommentCount(Long articleId);

    void decrementCommentCount(Long articleId);
}
