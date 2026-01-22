package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.WebhookRetry;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WebhookRetryMapper {

    @Insert("insert into tb_webhook_retry(webhook_id, event_type, payload, attempt_count, max_attempts, " +
            "next_retry_at, status, created_at) " +
            "values (#{webhookId}, #{eventType}, #{payload}, #{attemptCount}, #{maxAttempts}, " +
            "#{nextRetryAt}, #{status}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(WebhookRetry retry);

    @Select("select * from tb_webhook_retry where status = 0 and next_retry_at <= #{now}")
    List<WebhookRetry> findPendingRetries(@Param("now") LocalDateTime now);

    @Select("select * from tb_webhook_retry where id = #{id}")
    WebhookRetry findById(Long id);

    @Update("update tb_webhook_retry set attempt_count = attempt_count + 1, last_error = #{error}, " +
            "next_retry_at = #{nextRetryAt}, status = #{status} where id = #{id}")
    int updateRetry(@Param("id") Long id, @Param("error") String error,
                    @Param("nextRetryAt") LocalDateTime nextRetryAt, @Param("status") int status);

    @Update("update tb_webhook_retry set status = 2 where id = #{id}")
    int markCompleted(Long id);

    @Delete("delete from tb_webhook_retry where status = 2 or status = 3")
    void cleanup();
}
