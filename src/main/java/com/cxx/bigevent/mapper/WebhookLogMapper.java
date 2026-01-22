package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.WebhookLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WebhookLogMapper {

    @Insert("insert into tb_webhook_log(webhook_id, event_type, payload, response_status, " +
            "response_body, duration, status, error_message, created_at) " +
            "values (#{webhookId}, #{eventType}, #{payload}, #{responseStatus}, " +
            "#{responseBody}, #{duration}, #{status}, #{errorMessage}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(WebhookLog webhookLog);

    @Select("select * from tb_webhook_log where webhook_id = #{webhookId} order by created_at desc")
    List<WebhookLog> findByWebhookId(Long webhookId);

    @Select("select * from tb_webhook_log where webhook_id = #{webhookId} order by created_at desc " +
            "limit #{offset}, #{limit}")
    List<WebhookLog> findByWebhookIdPaged(@Param("webhookId") Long webhookId,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    @Select("select count(*) from tb_webhook_log where webhook_id = #{webhookId}")
    Long countByWebhookId(Long webhookId);

    @Delete("delete from tb_webhook_log where webhook_id = #{webhookId}")
    void deleteByWebhookId(Long webhookId);
}
