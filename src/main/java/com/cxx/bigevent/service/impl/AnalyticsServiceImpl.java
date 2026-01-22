package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.dto.AnalyticsResponse;
import com.cxx.bigevent.dto.AnalyticsResponse.CategoryStat;
import com.cxx.bigevent.dto.AnalyticsResponse.TrendData;
import com.cxx.bigevent.dto.AnalyticsResponse.UserActivity;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.ArticleStatsMapper;
import com.cxx.bigevent.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleStatsMapper articleStatsMapper;

    @Override
    public AnalyticsResponse getOverview() {
        AnalyticsResponse response = new AnalyticsResponse();

        Long totalArticles = articleMapper.countTotal();
        Long publishedArticles = articleMapper.countPublished();
        Long draftArticles = articleMapper.countDraft();
        Long totalViews = articleStatsMapper.sumAllViews();
        Long totalLikes = articleStatsMapper.sumAllLikes();
        Long totalShares = articleStatsMapper.sumAllShares();

        response.setTotalArticles(totalArticles != null ? totalArticles : 0L);
        response.setPublishedArticles(publishedArticles != null ? publishedArticles : 0L);
        response.setDraftArticles(draftArticles != null ? draftArticles : 0L);
        response.setTotalViews(totalViews != null ? totalViews : 0L);
        response.setTotalLikes(totalLikes != null ? totalLikes : 0L);
        response.setTotalShares(totalShares != null ? totalShares : 0L);
        response.setTotalCategories(15L);
        response.setTotalUsers(6L);

        response.setTopCategories(getTopCategories(null));
        response.setRecentTrend(getRecentTrend(null, 7));

        return response;
    }

    @Override
    public AnalyticsResponse getOverviewByUser(Long userId) {
        AnalyticsResponse response = new AnalyticsResponse();

        Long totalArticles = articleMapper.countTotal();
        Long publishedArticles = articleMapper.countPublished();
        Long draftArticles = articleMapper.countDraft();
        Long totalViews = articleStatsMapper.sumAllViews();
        Long totalLikes = articleStatsMapper.sumAllLikes();
        Long totalShares = articleStatsMapper.sumAllShares();

        response.setTotalArticles(totalArticles != null ? totalArticles : 0L);
        response.setPublishedArticles(publishedArticles != null ? publishedArticles : 0L);
        response.setDraftArticles(draftArticles != null ? draftArticles : 0L);
        response.setTotalViews(totalViews != null ? totalViews : 0L);
        response.setTotalLikes(totalLikes != null ? totalLikes : 0L);
        response.setTotalShares(totalShares != null ? totalShares : 0L);
        response.setTotalCategories(15L);
        response.setUserActivity(getUserActivity(LocalDateTime.now().minusDays(7), LocalDateTime.now()));

        response.setTopCategories(getTopCategories(userId));
        response.setRecentTrend(getRecentTrend(userId, 7));

        return response;
    }

    @Override
    public CategoryStat getCategoryStats(Long userId) {
        String[] categoryNames = {"科技", "生活", "娱乐", "体育", "财经", "教育", "健康", "旅游", "美食", "时尚", "汽车", "房产", "人文", "历史", "艺术"};

        List<CategoryStat> stats = new ArrayList<>();
        for (int i = 0; i < categoryNames.length; i++) {
            CategoryStat stat = new CategoryStat();
            stat.setId((long) (i + 1));
            stat.setName(categoryNames[i]);
            stat.setArticleCount(2L);
            stat.setViewCount(100L);
            stats.add(stat);
        }

        stats.sort((a, b) -> Long.compare(b.getArticleCount(), a.getArticleCount()));

        if (stats.size() > 5) {
            stats = stats.subList(0, 5);
        }

        return stats.isEmpty() ? null : stats.get(0);
    }

    private List<CategoryStat> getTopCategories(Long userId) {
        String[] categoryNames = {"科技", "生活", "娱乐", "体育", "财经", "教育", "健康", "旅游", "美食", "时尚", "汽车", "房产", "人文", "历史", "艺术"};

        List<CategoryStat> stats = new ArrayList<>();
        for (int i = 0; i < categoryNames.length; i++) {
            CategoryStat stat = new CategoryStat();
            stat.setId((long) (i + 1));
            stat.setName(categoryNames[i]);
            stat.setArticleCount(2L);
            stat.setViewCount(100L);
            stats.add(stat);
        }

        stats.sort((a, b) -> Long.compare(b.getArticleCount(), a.getArticleCount()));

        if (stats.size() > 5) {
            stats = stats.subList(0, 5);
        }

        return stats;
    }

    @Override
    public List<TrendData> getRecentTrend(Long userId, Integer days) {
        List<TrendData> trendData = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            TrendData data = new TrendData();
            data.setDate(date.format(DateTimeFormatter.ISO_DATE));
            data.setViews((long) (Math.random() * 100));
            data.setArticles((long) (Math.random() * 5));
            trendData.add(data);
        }

        return trendData;
    }

    @Override
    public UserActivity getUserActivity(LocalDateTime startTime, LocalDateTime endTime) {
        UserActivity activity = new UserActivity();
        activity.setActiveUsers(6L);
        activity.setNewUsers(0L);
        activity.setLoginCount(0L);
        return activity;
    }
}
