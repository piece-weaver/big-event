package com.cxx.bigevent.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private List<ArticleSearchResult> results;

    @Data
    public static class ArticleSearchResult {
        private Long id;
        private String title;
        private String content;
        private String coverImg;
        private Integer categoryId;
        private String categoryName;
        private Long viewCount;
        private Long likeCount;
        private String createTime;
        private List<String> highlightTitle;
        private List<String> highlightContent;
    }
}
