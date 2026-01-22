package com.cxx.bigevent.service;

import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Category;

import java.util.List;

public interface CategoryService {

    //添加分类
    void add(Category category);

    //分类列表
    List<Category> list();

    //根据分类ID查询分类
    Category findCategoryById(Integer id);
    
    //根据分类ID查询文章
    List<Article> findArticlesByCategoryId(Integer id);

    //更新分类
    void update(Category category, Integer id);

    //删除分类
    void delete(Integer id);

    //删除分类（包含关联文章）
    void deleteWithArticles(Integer id);
}
