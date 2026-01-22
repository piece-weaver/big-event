package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.CollectDTO;
import com.cxx.bigevent.pojo.ArticleCollect;
import com.cxx.bigevent.pojo.ArticleCollectVO;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.ArticleInteractionService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
@Validated
@Tag(name = "文章互动", description = "文章点赞、收藏等互动功能接口")
public class ArticleInteractionController {

    @Autowired
    private ArticleInteractionService articleInteractionService;

    @PostMapping("/{id}/like")
    @Operation(summary = "点赞文章", description = "对指定文章点赞")
    public Result<Map<String, Object>> like(@PathVariable Long id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        articleInteractionService.likeArticle(id, userId);

        int likeCount = articleInteractionService.getLikeCount(id);
        boolean isLiked = articleInteractionService.isLiked(id, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeCount);
        result.put("isLiked", isLiked);
        return Result.success(result);
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "取消点赞", description = "取消对指定文章的点赞")
    public Result<Map<String, Object>> unlike(@PathVariable Long id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        articleInteractionService.unlikeArticle(id, userId);

        int likeCount = articleInteractionService.getLikeCount(id);
        boolean isLiked = articleInteractionService.isLiked(id, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeCount);
        result.put("isLiked", isLiked);
        return Result.success(result);
    }

    @GetMapping("/{id}/like/status")
    @Operation(summary = "获取点赞状态", description = "获取指定文章的点赞数和当前用户的点赞状态")
    public Result<Map<String, Object>> getLikeStatus(@PathVariable Long id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = map != null ? ((Number) map.get("id")).longValue() : null;

        int likeCount = articleInteractionService.getLikeCount(id);
        boolean isLiked = userId != null && articleInteractionService.isLiked(id, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeCount);
        result.put("isLiked", isLiked);
        return Result.success(result);
    }

    @PostMapping("/{id}/collect")
    @Operation(summary = "收藏文章", description = "将指定文章收藏到个人收藏夹")
    public Result<Map<String, Object>> collect(@PathVariable Long id, @RequestBody @Valid CollectDTO dto) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        ArticleCollect collect = new ArticleCollect();
        collect.setArticleId(id);
        collect.setUserId(userId);
        collect.setFolderName(dto.getFolderName());

        articleInteractionService.collectArticle(collect);

        boolean isCollected = articleInteractionService.isCollected(id, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("isCollected", isCollected);
        return Result.success(result);
    }

    @DeleteMapping("/{id}/collect")
    @Operation(summary = "取消收藏", description = "取消对指定文章的收藏")
    public Result<Map<String, Object>> uncollect(@PathVariable Long id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        articleInteractionService.uncollectArticle(id, userId);

        boolean isCollected = articleInteractionService.isCollected(id, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("isCollected", isCollected);
        return Result.success(result);
    }

    @GetMapping("/{id}/collect/status")
    @Operation(summary = "获取收藏状态", description = "获取指定文章的收藏状态")
    public Result<Map<String, Object>> getCollectStatus(@PathVariable Long id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = map != null ? ((Number) map.get("id")).longValue() : null;

        boolean isCollected = userId != null && articleInteractionService.isCollected(id, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("isCollected", isCollected);
        return Result.success(result);
    }

    @GetMapping("/collect/my")
    @Operation(summary = "我的收藏", description = "获取当前用户的所有收藏文章")
    public Result<List<ArticleCollectVO>> getMyCollections() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        List<ArticleCollectVO> collections = articleInteractionService.getMyCollections(userId);
        return Result.success(collections);
    }
}
