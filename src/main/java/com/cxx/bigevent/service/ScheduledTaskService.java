package com.cxx.bigevent.service;

import com.cxx.bigevent.dto.ScheduledTaskRequest;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.pojo.ScheduledTask;

import java.time.LocalDateTime;

public interface ScheduledTaskService {

    Long createTask(ScheduledTaskRequest request);

    void updateTaskTime(Long id, LocalDateTime scheduledTime);

    void cancelTask(Long id);

    ScheduledTask getTask(Long id);

    PageBean<ScheduledTask> listTasks(Integer pageNum, Integer pageSize);

    void executeDueTasks();

    void executeTask(Long taskId);
}
