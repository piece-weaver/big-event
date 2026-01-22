package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.BehaviorDTO;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.RecommendationService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "推荐服务", description = "个性化推荐和热门文章接口")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    @Operation(summary = "个性化推荐", description = "根据用户行为获取个性化文章推荐")
    public Result<List<Article>> getRecommendations(
            @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = map != null ? ((Number) map.get("id")).longValue() : null;

        List<Article> recommendations = recommendationService.getRecommendations(userId, limit);
        return Result.success(recommendations);
    }

    @GetMapping("/hot")
    @Operation(summary = "热门文章", description = "获取热门文章排行榜")
    public Result<List<Article>> getHotArticles(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<Article> hotArticles = recommendationService.getHotArticles(limit);
        return Result.success(hotArticles);
    }

    @GetMapping("/similar/{articleId}")
    @Operation(summary = "相似文章", description = "获取与指定文章相似的内容推荐")
    public Result<List<Article>> getSimilarArticles(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "5") Integer limit) {
        List<Article> similarArticles = recommendationService.getSimilarArticles(articleId, limit);
        return Result.success(similarArticles);
    }

    @PostMapping("/behavior")
    @Operation(summary = "记录行为", description = "记录用户的浏览、点赞等行为用于推荐计算")
    public Result<Void> recordBehavior(@RequestBody @Valid BehaviorDTO dto) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        recommendationService.recordBehavior(userId, dto.getArticleId(), dto.getBehaviorType(), dto.getDuration());
        return Result.success();
    }
}
