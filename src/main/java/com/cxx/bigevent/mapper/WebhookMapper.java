package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.Webhook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WebhookMapper {

    @Insert("insert into tb_webhook(name, url, secret, events, is_active, created_by, created_at, updated_at) " +
            "values (#{name}, #{url}, #{secret}, #{events}, #{isActive}, #{createdBy}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Webhook webhook);

    @Select("select * from tb_webhook where id = #{id}")
    Webhook findById(Long id);

    @Select("select * from tb_webhook where is_active = 1")
    List<Webhook> findActiveWebhooks();

    @Select("select * from tb_webhook where created_by = #{userId} order by created_at desc")
    List<Webhook> findByUserId(Long userId);

    @Select("select * from tb_webhook order by created_at desc")
    List<Webhook> list();

    @Update("update tb_webhook set name = #{name}, url = #{url}, secret = #{secret}, " +
            "events = #{events}, is_active = #{isActive}, updated_at = now() where id = #{id}")
    int update(Webhook webhook);

    @Delete("delete from tb_webhook where id = #{id}")
    int delete(Long id);

    @Update("update tb_webhook set last_triggered_at = now(), success_count = success_count + #{success}, " +
            "failed_count = failed_count + #{failed} where id = #{id}")
    void updateStats(@Param("id") Long id, @Param("success") int success, @Param("failed") int failed);
}
