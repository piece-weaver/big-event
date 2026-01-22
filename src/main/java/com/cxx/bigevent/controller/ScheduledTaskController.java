package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.ScheduledTaskRequest;
import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.pojo.ScheduledTask;
import com.cxx.bigevent.service.ScheduledTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/scheduled-tasks")
@Validated
@Tag(name = "定时任务", description = "定时发布任务管理接口")
public class ScheduledTaskController {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskController.class);

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @PostMapping
    @Operation(summary = "创建定时任务", description = "创建一个新的定时发布任务")
    public Result<Long> create(@RequestBody @Valid ScheduledTaskRequest request) {
        Long taskId = scheduledTaskService.createTask(request);
        return Result.success(taskId);
    }

    @GetMapping
    @Operation(summary = "任务列表", description = "获取所有定时任务列表")
    public Result<PageBean<ScheduledTask>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<ScheduledTask> pb = scheduledTaskService.listTasks(pageNum, pageSize);
        return Result.success(pb);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取任务", description = "根据ID获取定时任务详情")
    public Result<ScheduledTask> get(@PathVariable Long id) {
        ScheduledTask task = scheduledTaskService.getTask(id);
        return Result.success(task);
    }

    @PutMapping("/{id}/time")
    @Operation(summary = "更新时间", description = "更新定时任务的执行时间")
    public Result<Void> updateTime(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime scheduledTime) {
        scheduledTaskService.updateTaskTime(id, scheduledTime);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "取消任务", description = "取消指定的定时任务")
    public Result<Void> cancel(@PathVariable Long id) {
        scheduledTaskService.cancelTask(id);
        return Result.success();
    }

    @PostMapping("/{id}/execute")
    @Operation(summary = "立即执行", description = "立即执行指定的定时任务")
    public Result<Void> execute(@PathVariable Long id) {
        scheduledTaskService.executeTask(id);
        return Result.success();
    }
}
