package com.cxx.bigevent.controller;

import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Category;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.security.rbac.annotation.RequirePermission;
import com.cxx.bigevent.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "分类管理", description = "文章分类的增删改查接口")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(summary = "创建分类", description = "创建一个新的文章分类")
    public Result add(@RequestBody @Validated(Category.Add.class) Category category) {
        categoryService.add(category);
        log.info("分类添加成功：{}", category.getCategoryName());
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "分类列表", description = "获取所有分类列表")
    public Result<List<Category>> list() {
        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类", description = "根据ID获取分类详情")
    public Result<Category> findById(@PathVariable Integer id) {
        Category category = categoryService.findCategoryById(id);
        return Result.success(category);
    }

    @GetMapping("/{id}/articles")
    @Operation(summary = "获取分类文章", description = "获取指定分类下的所有文章")
    public Result<List<Article>> getArticlesByCategoryId(@PathVariable Integer id) {
        List<Article> articles = categoryService.findArticlesByCategoryId(id);
        return Result.success(articles);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "根据ID更新分类信息")
    public Result update(@RequestBody @Validated(Category.Update.class) Category category, @PathVariable Integer id) {
        categoryService.update(category, id);
        log.info("分类更新成功：{}", category.getCategoryName());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "根据ID删除分类（仅当分类下无文章时）")
    public Result delete(@PathVariable Integer id) {
        categoryService.delete(id);
        log.info("分类删除成功：ID：{}", id);
        return Result.success();
    }

    @DeleteMapping("/{id}/force")
    @RequirePermission("category:delete:force")
    @Operation(summary = "强制删除分类", description = "强制删除分类及其所有关联文章")
    public Result deleteWithArticles(@PathVariable Integer id) {
        categoryService.deleteWithArticles(id);
        log.info("分类及其关联文章删除成功：ID：{}", id);
        return Result.success();
    }
}
