package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.BatchOperationRequest;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "批量操作", description = "文章批量管理接口")
public class BatchOperationController {

    private static final Logger log = LoggerFactory.getLogger(BatchOperationController.class);

    @Autowired
    private ArticleService articleService;

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除", description = "批量删除指定ID的文章")
    public Result<String> batchDelete(@Valid @RequestBody BatchOperationRequest request) {
        if (request.getIds() == null || request.getIds().isEmpty()) {
            return Result.error(ErrorCode.PARAM_NOT_EMPTY);
        }

        List<Integer> ids = new ArrayList<>();
        for (Long id : request.getIds()) {
            ids.add(id.intValue());
        }

        int deleted = articleService.batchDelete(ids);
        return Result.success("成功删除" + deleted + "篇文章");
    }

    @PutMapping("/batch/state")
    @Operation(summary = "批量更新状态", description = "批量更新文章发布状态")
    public Result<String> batchUpdateState(@Valid @RequestBody BatchOperationRequest request) {
        try {
            if (request.getIds() == null || request.getIds().isEmpty()) {
                return Result.error(ErrorCode.PARAM_NOT_EMPTY);
            }
            if (request.getState() == null) {
                return Result.error(ErrorCode.PARAM_NOT_EMPTY);
            }

            List<Integer> ids = new ArrayList<>();
            for (Long id : request.getIds()) {
                ids.add(id.intValue());
            }

            int updated = articleService.batchUpdateState(ids, request.getState());
            return Result.success("成功更新" + updated + "篇文章状态");
        } catch (Exception e) {
            return Result.error(500, "错误: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    @PutMapping("/batch/category")
    @Operation(summary = "批量移动分类", description = "批量将文章移动到指定分类")
    public Result<String> batchUpdateCategory(@Valid @RequestBody BatchOperationRequest request) {
        if (request.getIds() == null || request.getIds().isEmpty()) {
            return Result.error(ErrorCode.PARAM_NOT_EMPTY);
        }
        if (request.getCategoryId() == null) {
            return Result.error(ErrorCode.PARAM_NOT_EMPTY);
        }

        List<Integer> ids = new ArrayList<>();
        for (Long id : request.getIds()) {
            ids.add(id.intValue());
        }

        int updated = articleService.batchUpdateCategory(ids, request.getCategoryId());
        return Result.success("成功移动" + updated + "篇文章到指定分类");
    }

    @GetMapping("/batch/export")
    @Operation(summary = "批量导出", description = "根据ID列表批量导出文章")
    public Result<List<Article>> batchExport(@RequestParam List<Long> ids) {
        List<Article> articles = articleService.findByIds(ids);
        return Result.success(articles);
    }
}
