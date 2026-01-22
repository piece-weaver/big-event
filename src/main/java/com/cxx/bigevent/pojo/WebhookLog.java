package com.cxx.bigevent.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebhookLog {
    private Long id;
    private Long webhookId;
    private String eventType;
    private String payload;
    private Integer responseStatus;
    private String responseBody;
    private Integer duration;
    private String status;
    private String errorMessage;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public interface WebhookStatus {
        String SUCCESS = "success";
        String FAILED = "failed";
        String PENDING = "pending";
    }
}
