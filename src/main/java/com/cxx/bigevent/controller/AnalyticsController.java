package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.AnalyticsResponse;
import com.cxx.bigevent.dto.AnalyticsResponse.TrendData;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.AnalyticsService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "数据分析", description = "数据统计和分析相关接口")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/overview")
    @Operation(summary = "数据概览", description = "获取当前用户的数据统计概览")
    public Result<AnalyticsResponse> getOverview() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        AnalyticsResponse response = analyticsService.getOverviewByUser(userId);
        return Result.success(response);
    }

    @GetMapping("/articles")
    @Operation(summary = "文章分析", description = "获取当前用户的文章数据分析")
    public Result<AnalyticsResponse> getArticleAnalytics() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        AnalyticsResponse response = analyticsService.getOverviewByUser(userId);
        return Result.success(response);
    }

    @GetMapping("/categories")
    @Operation(summary = "分类统计", description = "获取当前用户的分类统计数据")
    public Result<AnalyticsResponse.CategoryStat> getCategoryStats() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        AnalyticsResponse.CategoryStat stat = analyticsService.getCategoryStats(userId);
        return Result.success(stat);
    }

    @GetMapping("/trend")
    @Operation(summary = "趋势分析", description = "获取近期数据变化趋势")
    public Result<List<TrendData>> getRecentTrend(@RequestParam(defaultValue = "7") Integer days) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();

        List<TrendData> trend = analyticsService.getRecentTrend(userId, days);
        return Result.success(trend);
    }

    @GetMapping("/activity")
    @Operation(summary = "用户活动", description = "获取用户活动数据统计")
    public Result<AnalyticsResponse.UserActivity> getUserActivity(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.now().minusDays(7);
        LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.now();

        AnalyticsResponse.UserActivity activity = analyticsService.getUserActivity(start, end);
        return Result.success(activity);
    }
}
