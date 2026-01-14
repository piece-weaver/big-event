package com.cxx.bigevent.service;

import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.PageBean;

public interface ArticleService {

    //新增文章
    void add(Article article);

    //分页查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, Integer state);

    //id查询
    Article findById(Integer id);

    //更新文章
    void update(Article article, Integer id);

    //删除文章
    void delete(Integer id);
}
