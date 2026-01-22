package com.cxx.bigevent.service;

import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.PageBean;

import java.util.List;

public interface ArticleService {

    void add(Article article);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, Integer state);

    Article findById(Integer id);

    void update(Article article, Integer id);

    void delete(Integer id);

    int batchDelete(List<Integer> ids);

    int batchUpdateState(List<Integer> ids, Integer state);

    int batchUpdateCategory(List<Integer> ids, Integer categoryId);

    List<Article> findByIds(List<Long> ids);
}
