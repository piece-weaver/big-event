package com.cxx.bigevent.dto;

import lombok.Data;

@Data
public class SearchRequest {
    private String keyword;
    private Integer categoryId;
    private Integer state;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String sortBy = "relevance";
}
