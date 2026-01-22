package com.cxx.bigevent.controller;

import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
@Tag(name = "文章管理", description = "文章的增删改查接口")
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @PostMapping
    @Operation(summary = "创建文章", description = "创建一篇新文章")
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        log.info("文章添加成功：{}", article.getTitle());
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "文章列表", description = "分页查询文章列表，支持按分类和状态筛选")
    public Result<PageBean<Article>> list(
            @RequestParam(defaultValue = "1") @org.springframework.validation.annotation.Validated @Min(1) Integer pageNum,
            @RequestParam(defaultValue = "10") @org.springframework.validation.annotation.Validated @Min(1) @Max(100) Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer state) {
        PageBean<Article> pb = articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pb);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章", description = "根据ID获取文章详情")
    public Result<Article> findById(@PathVariable Integer id) {
        Article article = articleService.findById(id);
        if (article == null) {
            log.warn("文章查询失败：文章不存在，ID：{}", id);
            return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
        }
        return Result.success(article);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章", description = "根据ID更新文章内容")
    public Result update(@RequestBody @Validated Article article, @PathVariable Integer id) {
        articleService.update(article, id);
        log.info("文章更新成功：{}", article.getTitle());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章", description = "根据ID删除文章")
    public Result delete(@PathVariable Integer id) {
        articleService.delete(id);
        log.info("文章删除成功：ID：{}", id);
        return Result.success();
    }
}
