package com.cxx.bigevent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledTaskRequest {

    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    @NotBlank(message = "资源类型不能为空")
    private String resourceType;

    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @NotNull(message = "定时时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime scheduledTime;

    private String changeSummary;
}
