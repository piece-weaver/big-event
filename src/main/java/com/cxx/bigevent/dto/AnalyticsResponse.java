package com.cxx.bigevent.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnalyticsResponse {
    private Long totalArticles;
    private Long publishedArticles;
    private Long draftArticles;
    private Long totalViews;
    private Long totalLikes;
    private Long totalShares;
    private Long totalUsers;
    private Long totalCategories;
    private List<CategoryStat> topCategories;
    private List<TrendData> recentTrend;
    private UserActivity userActivity;
    
    @Data
    public static class CategoryStat {
        private Long id;
        private String name;
        private Long articleCount;
        private Long viewCount;
    }
    
    @Data
    public static class TrendData {
        private String date;
        private Long views;
        private Long articles;
    }
    
    @Data
    public static class UserActivity {
        private Long activeUsers;
        private Long newUsers;
        private Long loginCount;
    }
}
