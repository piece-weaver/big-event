package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.ScheduledTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduledTaskMapper {

    @Insert("insert into tb_scheduled_task(task_type, resource_type, resource_id, scheduled_time, " +
            "status, created_by, created_at, updated_at) " +
            "values (#{taskType}, #{resourceType}, #{resourceId}, #{scheduledTime}, " +
            "#{status}, #{createdBy}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(ScheduledTask task);

    @Select("select * from tb_scheduled_task where id = #{id}")
    ScheduledTask findById(Long id);

    @Select("select * from tb_scheduled_task where status = 0 and scheduled_time <= #{now} " +
            "order by scheduled_time asc")
    List<ScheduledTask> findDueTasks(@Param("now") LocalDateTime now);

    @Select("select * from tb_scheduled_task where created_by = #{userId} order by created_at desc")
    List<ScheduledTask> findByUserId(Long userId);

    @Select("select * from tb_scheduled_task order by created_at desc")
    List<ScheduledTask> list();

    @Update("update tb_scheduled_task set scheduled_time = #{scheduledTime}, updated_at = now() where id = #{id}")
    int updateTime(Long id, LocalDateTime scheduledTime);

    @Update("update tb_scheduled_task set status = #{status}, executed_at = now(), " +
            "result_message = #{resultMessage}, updated_at = now() where id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") int status,
                     @Param("resultMessage") String resultMessage);

    @Delete("delete from tb_scheduled_task where id = #{id}")
    int delete(Long id);

    @Update("update tb_scheduled_task set status = 3, updated_at = now() where id = #{id}")
    int cancel(Long id);
}
