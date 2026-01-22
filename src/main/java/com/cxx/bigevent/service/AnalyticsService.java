package com.cxx.bigevent.service;

import com.cxx.bigevent.dto.AnalyticsResponse;
import com.cxx.bigevent.dto.AnalyticsResponse.TrendData;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsService {

    AnalyticsResponse getOverview();

    AnalyticsResponse getOverviewByUser(Long userId);

    AnalyticsResponse.CategoryStat getCategoryStats(Long userId);

    List<TrendData> getRecentTrend(Long userId, Integer days);

    AnalyticsResponse.UserActivity getUserActivity(LocalDateTime startTime, LocalDateTime endTime);
}
