package com.cxx.bigevent.scheduler;

import com.cxx.bigevent.service.ScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("scheduledTaskScheduler")
@EnableScheduling
public class TaskScheduler {

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Scheduled(fixedRate = 60000)
    public void checkScheduledTasks() {
        scheduledTaskService.executeDueTasks();
    }
}
