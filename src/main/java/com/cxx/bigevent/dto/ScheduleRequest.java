package com.cxx.bigevent.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequest {
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;
    
    @NotNull(message = "计划时间不能为空")
    private LocalDateTime scheduledTime;
    
    private String changeSummary;
}
