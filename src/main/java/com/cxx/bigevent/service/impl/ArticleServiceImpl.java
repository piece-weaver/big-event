package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.event.ArticleCreateEvent;
import com.cxx.bigevent.event.ArticleDeleteEvent;
import com.cxx.bigevent.event.ArticleUpdateEvent;
import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.service.ArticleService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void add(Article article) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);
        articleMapper.add(article);
        eventPublisher.publishEvent(new ArticleCreateEvent(this, article));
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, Integer state) {
        PageBean<Article> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Article> as = articleMapper.list(categoryId, state, userId);
        Page<Article> p = (Page<Article>) as;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public Article findById(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return articleMapper.findById(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Article article, Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Article existingArticle = articleMapper.findById(id, userId);
        if (existingArticle == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        article.setCreateUser(userId);
        int affectedRows = articleMapper.update(article, id);

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        eventPublisher.publishEvent(new ArticleUpdateEvent(this, article, id.longValue()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Article article = articleMapper.findById(id, userId);
        if (article == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        int affectedRows = articleMapper.delete(id, userId);

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        eventPublisher.publishEvent(new ArticleDeleteEvent(this, id.longValue(), article.getTitle()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Integer> ids) {
        return articleMapper.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateState(List<Integer> ids, Integer state) {
        return articleMapper.batchUpdateState(ids, state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateCategory(List<Integer> ids, Integer categoryId) {
        return articleMapper.batchUpdateCategory(ids, categoryId);
    }

    @Override
    public List<Article> findByIds(List<Long> ids) {
        return articleMapper.findByIds(ids);
    }
}
