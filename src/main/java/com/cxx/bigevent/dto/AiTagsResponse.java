package com.cxx.bigevent.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiTagsResponse {
    private List<TagItem> tags;
    private String modelUsed;
    private Long processingTime;
    
    @Data
    public static class TagItem {
        private String name;
        private Double confidence;
    }
}
