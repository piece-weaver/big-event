package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.AuditLog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AuditLogMapper {

    @Insert("INSERT INTO tb_audit_log(user_id, username, action, resource_type, resource_id, resource_name, " +
            "ip_address, user_agent, request_params, response_status, error_message, created_at) " +
            "VALUES(#{userId}, #{username}, #{action}, #{resourceType}, #{resourceId}, #{resourceName}, " +
            "#{ipAddress}, #{userAgent}, #{requestParams}, #{responseStatus}, #{errorMessage}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AuditLog log);

    @Select("SELECT * FROM tb_audit_log ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<AuditLog> findRecent(@Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("SELECT * FROM tb_audit_log WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<AuditLog> findByUserId(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("SELECT * FROM tb_audit_log WHERE action = #{action} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<AuditLog> findByAction(@Param("action") String action, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("SELECT * FROM tb_audit_log WHERE resource_type = #{resourceType} AND resource_id = #{resourceId} " +
            "ORDER BY created_at DESC")
    List<AuditLog> findByResource(@Param("resourceType") String resourceType, @Param("resourceId") Long resourceId);

    @Select("SELECT * FROM tb_audit_log WHERE created_at BETWEEN #{startTime} AND #{endTime} " +
            "ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<AuditLog> findByTimeRange(@Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime,
                                    @Param("offset") Integer offset,
                                    @Param("limit") Integer limit);

    @Select("SELECT COUNT(*) FROM tb_audit_log")
    Long countTotal();

    @Select("SELECT COUNT(*) FROM tb_audit_log WHERE user_id = #{userId}")
    Long countByUserId(Long userId);

    @Delete("DELETE FROM tb_audit_log WHERE created_at < #{beforeTime}")
    int deleteBeforeTime(@Param("beforeTime") LocalDateTime beforeTime);
}
