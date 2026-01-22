package com.cxx.bigevent.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiSummaryResponse {
    private String summary;
    private Integer readingTime;
    private List<String> keywords;
    private Integer seoScore;
    private List<String> seoSuggestions;
    private String suggestedCategory;
    private String modelUsed;
    private Long processingTime;
}
