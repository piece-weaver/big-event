package com.cxx.bigevent.controller;

import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        log.info("文章添加成功：{}", article.getTitle());
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer state) {
        PageBean<Article> pb = articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pb);
    }

    @GetMapping("/{id}")
    public Result<Article> findById(@PathVariable Integer id) {
        Article article = articleService.findById(id);
        if (article == null) {
            log.warn("文章查询失败：文章不存在，ID：{}", id);
            return Result.error(ErrorCode.ARTICLE_NOT_FOUND);
        }
        return Result.success(article);
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody @Validated Article article, @PathVariable Integer id) {
        articleService.update(article, id);
        log.info("文章更新成功：{}", article.getTitle());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        articleService.delete(id);
        log.info("文章删除成功：ID：{}", id);
        return Result.success();
    }
}
