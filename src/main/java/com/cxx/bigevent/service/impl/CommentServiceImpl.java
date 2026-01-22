package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.CommentMapper;
import com.cxx.bigevent.mapper.ArticleStatsMapper;
import com.cxx.bigevent.mapper.UserMapper;
import com.cxx.bigevent.pojo.Comment;
import com.cxx.bigevent.pojo.CommentVO;
import com.cxx.bigevent.pojo.CommentWithUserVO;
import com.cxx.bigevent.pojo.UserVO;
import com.cxx.bigevent.service.CommentService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleStatsMapper articleStatsMapper;

    @Override
    public void add(Comment comment) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();
        comment.setUserId(userId);

        if (comment.getParentId() != null) {
            Comment parentComment = commentMapper.findById(comment.getParentId());
            if (parentComment == null) {
                throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
            }
        }

        commentMapper.insert(comment);

        articleStatsMapper.incrementCommentCount(comment.getArticleId());
    }

    @Override
    public List<CommentVO> findByArticleId(Long articleId) {
        List<CommentWithUserVO> commentsWithUser = commentMapper.findByArticleIdWithUser(articleId);

        List<CommentVO> result = new ArrayList<>();
        for (CommentWithUserVO cwu : commentsWithUser) {
            CommentVO vo = new CommentVO();
            vo.setId(cwu.getId());
            vo.setArticleId(cwu.getArticleId());
            vo.setContent(cwu.getContent());
            vo.setParentId(cwu.getParentId());
            vo.setLikeCount(cwu.getLikeCount());
            vo.setCreatedAt(cwu.getCreatedAt());
            vo.setUser(cwu.getUser());
            result.add(vo);
        }
        return result;
    }

    @Override
    public void update(Comment comment) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        Comment existing = commentMapper.findById(comment.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
        if (!existing.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        comment.setUserId(userId);
        commentMapper.update(comment);
    }

    @Override
    public void delete(Long id, Long userId) {
        Comment existing = commentMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
        if (!existing.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        commentMapper.delete(id, userId);
        articleStatsMapper.decrementCommentCount(existing.getArticleId());
    }

    @Override
    public void likeComment(Long id) {
        Comment comment = commentMapper.findById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
        commentMapper.incrementLikeCount(id);
    }

    @Override
    public int countByArticleId(Long articleId) {
        return commentMapper.countByArticleId(articleId);
    }

    @Override
    public void incrementCommentCount(Long articleId) {
        articleStatsMapper.incrementCommentCount(articleId);
    }

    @Override
    public void decrementCommentCount(Long articleId) {
        articleStatsMapper.decrementCommentCount(articleId);
    }
}
