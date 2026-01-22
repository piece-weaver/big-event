package com.cxx.bigevent.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleVersion {
    private Long id;
    private Long articleId;
    private String title;
    private String content;
    private String coverImg;
    private Integer state;
    private Long categoryId;
    private Integer versionNumber;
    private String changeSummary;
    private Long createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
