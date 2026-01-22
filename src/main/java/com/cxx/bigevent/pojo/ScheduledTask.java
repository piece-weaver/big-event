package com.cxx.bigevent.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledTask {
    private Long id;
    private String taskType;
    private String resourceType;
    private Long resourceId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledTime;
    
    private Integer status;
    private Long createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime executedAt;
    
    private String resultMessage;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    public interface TaskStatus {
        int PENDING = 0;
        int EXECUTING = 1;
        int COMPLETED = 2;
        int CANCELLED = 3;
        int FAILED = 4;
    }
    
    public interface TaskType {
        String PUBLISH = "publish";
        String DELETE = "delete";
    }
}
