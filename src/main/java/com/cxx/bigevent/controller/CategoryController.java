package com.cxx.bigevent.controller;

import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Category;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category) {
        categoryService.add(category);
        log.info("分类添加成功：{}", category.getCategoryName());
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> list() {
        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable Integer id) {
        Category category = categoryService.findCategoryById(id);
        return Result.success(category);
    }

    @GetMapping("/{id}/articles")
    public Result<List<Article>> getArticlesByCategoryId(@PathVariable Integer id) {
        List<Article> articles = categoryService.findArticlesByCategoryId(id);
        return Result.success(articles);
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody @Validated(Category.Update.class) Category category, @PathVariable Integer id) {
        categoryService.update(category, id);
        log.info("分类更新成功：{}", category.getCategoryName());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        categoryService.delete(id);
        log.info("分类删除成功：ID：{}", id);
        return Result.success();
    }

    @DeleteMapping("/{id}/force")
    public Result deleteWithArticles(@PathVariable Integer id) {
        categoryService.deleteWithArticles(id);
        log.info("分类及其关联文章删除成功：ID：{}", id);
        return Result.success();
    }
}
