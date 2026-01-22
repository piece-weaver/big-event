package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.UserBehavior;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserBehaviorMapper {

    @Insert("INSERT INTO tb_user_behavior(user_id, article_id, behavior_type, duration, created_at) " +
            "VALUES (#{userId}, #{articleId}, #{behaviorType}, #{duration}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserBehavior behavior);

    @Select("SELECT DISTINCT article_id FROM tb_user_behavior WHERE user_id = #{userId} AND behavior_type IN ('view', 'like', 'collect', 'comment')")
    List<Long> findViewedArticleIds(Long userId);

    @Select("SELECT article_id FROM tb_user_behavior WHERE user_id = #{userId} AND behavior_type = 'view' ORDER BY created_at DESC LIMIT #{limit}")
    List<Long> findRecentViewedArticleIds(@Param("userId") Long userId, @Param("limit") int limit);

    @Select("SELECT category_id FROM tb_article WHERE id = #{articleId}")
    Integer findCategoryIdByArticleId(Long articleId);

    @Select("""
        <script>
        SELECT id FROM tb_article
        WHERE state = 1
        <if test="excludeIds != null and !excludeIds.isEmpty()">
            AND id NOT IN
            <foreach item="id" open="(" separator="," close=")" collection="excludeIds">
                #{id}
            </foreach>
        </if>
        ORDER BY RAND() LIMIT #{limit}
        </script>
        """)
    List<Long> findRandomArticleIds(@Param("excludeIds") List<Long> excludeIds, @Param("limit") int limit);
}
