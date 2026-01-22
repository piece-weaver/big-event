package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.dto.ScheduledTaskRequest;
import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.ScheduledTaskMapper;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.pojo.ScheduledTask;
import com.cxx.bigevent.service.ScheduledTaskService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskServiceImpl.class);

    @Autowired
    private ScheduledTaskMapper taskMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(ScheduledTaskRequest request) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        if (request.getScheduledTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(4003, "定时时间必须晚于当前时间");
        }

        ScheduledTask task = new ScheduledTask();
        task.setTaskType(request.getTaskType());
        task.setResourceType(request.getResourceType());
        task.setResourceId(request.getResourceId());
        task.setScheduledTime(request.getScheduledTime());
        task.setStatus(ScheduledTask.TaskStatus.PENDING);
        task.setCreatedBy(userId.longValue());

        taskMapper.add(task);
        log.info("定时任务创建成功：ID={}，类型={}，资源ID={}，执行时间={}",
                task.getId(), task.getTaskType(), task.getResourceId(), task.getScheduledTime());

        return task.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskTime(Long id, LocalDateTime scheduledTime) {
        if (scheduledTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException(4003, "定时时间必须晚于当前时间");
        }

        ScheduledTask task = taskMapper.findById(id);
        if (task == null) {
            throw new BusinessException(ErrorCode.SCHEDULED_TASK_NOT_FOUND);
        }

        if (task.getStatus() != ScheduledTask.TaskStatus.PENDING) {
            throw new BusinessException(4003, "只能修改待执行任务的时间");
        }

        taskMapper.updateTime(id, scheduledTime);
        log.info("定时任务时间更新：ID={}，新时间={}", id, scheduledTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTask(Long id) {
        ScheduledTask task = taskMapper.findById(id);
        if (task == null) {
            throw new BusinessException(ErrorCode.SCHEDULED_TASK_NOT_FOUND);
        }

        if (task.getStatus() != ScheduledTask.TaskStatus.PENDING) {
            throw new BusinessException(4003, "只能取消待执行的任务");
        }

        taskMapper.cancel(id);
        log.info("定时任务已取消：ID={}", id);
    }

    @Override
    public ScheduledTask getTask(Long id) {
        ScheduledTask task = taskMapper.findById(id);
        if (task == null) {
            throw new BusinessException(ErrorCode.SCHEDULED_TASK_NOT_FOUND);
        }
        return task;
    }

    @Override
    public PageBean<ScheduledTask> listTasks(Integer pageNum, Integer pageSize) {
        PageBean<ScheduledTask> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ScheduledTask> tasks = taskMapper.list();
        Page<ScheduledTask> p = (Page<ScheduledTask>) tasks;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public void executeDueTasks() {
        List<ScheduledTask> tasks = taskMapper.findDueTasks(LocalDateTime.now());

        for (ScheduledTask task : tasks) {
            try {
                executeTask(task.getId());
            } catch (Exception e) {
                log.error("定时任务执行失败：ID={}，错误={}", task.getId(), e.getMessage());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeTask(Long taskId) {
        ScheduledTask task = taskMapper.findById(taskId);
        if (task == null || task.getStatus() != ScheduledTask.TaskStatus.PENDING) {
            return;
        }

        taskMapper.updateStatus(taskId, ScheduledTask.TaskStatus.EXECUTING, null);

        try {
            String resultMessage;
            switch (task.getTaskType()) {
                case ScheduledTask.TaskType.PUBLISH:
                    if ("article".equals(task.getResourceType())) {
                        ArticleMapper articleMapper = this.articleMapper;
                        articleMapper.updateState(task.getResourceId(), 1);
                        resultMessage = "文章已发布";
                    } else {
                        resultMessage = "不支持的资源类型";
                    }
                    break;
                case ScheduledTask.TaskType.DELETE:
                    if ("article".equals(task.getResourceType())) {
                        articleMapper.deleteById(task.getResourceId().intValue());
                        resultMessage = "文章已删除";
                    } else {
                        resultMessage = "不支持的资源类型";
                    }
                    break;
                default:
                    resultMessage = "不支持的任务类型";
            }

            taskMapper.updateStatus(taskId, ScheduledTask.TaskStatus.COMPLETED, resultMessage);
            log.info("定时任务执行成功：ID={}，结果={}", taskId, resultMessage);

        } catch (Exception e) {
            taskMapper.updateStatus(taskId, ScheduledTask.TaskStatus.FAILED, e.getMessage());
            log.error("定时任务执行失败：ID={}，错误={}", taskId, e.getMessage());
            throw new BusinessException(ErrorCode.SCHEDULED_TASK_EXECUTION_FAILED);
        }
    }
}
