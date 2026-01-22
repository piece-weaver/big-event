package com.cxx.bigevent.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebhookRetry {
    private Long id;
    private Long webhookId;
    private String eventType;
    private String payload;
    private Integer attemptCount;
    private Integer maxAttempts;
    private String lastError;
    private LocalDateTime nextRetryAt;
    private Integer status;
    private LocalDateTime createdAt;

    public interface RetryStatus {
        int PENDING = 0;
        int RETRYING = 1;
        int COMPLETED = 2;
        int FAILED = 3;
    }
}
