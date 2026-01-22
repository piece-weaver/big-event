package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.Comment;
import com.cxx.bigevent.pojo.CommentWithUserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into tb_comment(article_id, user_id, content, parent_id, like_count, status, created_at, updated_at) " +
            "values (#{articleId}, #{userId}, #{content}, #{parentId}, 0, 1, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Comment comment);

    @Select("select * from tb_comment where id = #{id} and status = 1")
    Comment findById(Long id);

    List<Comment> findByArticleId(@Param("articleId") Long articleId);

    @Select("""
        SELECT c.id, c.article_id, c.content, c.parent_id, c.like_count, c.created_at,
               u.id as user_id, u.nickname, u.user_pic
        FROM tb_comment c
        LEFT JOIN tb_user u ON c.user_id = u.id
        WHERE c.article_id = #{articleId} AND c.status = 1
        ORDER BY c.created_at DESC
        """)
    List<CommentWithUserVO> findByArticleIdWithUser(@Param("articleId") Long articleId);

    @Update("update tb_comment set content = #{content}, updated_at = now() where id = #{id} and user_id = #{userId}")
    int update(Comment comment);

    @Update("update tb_comment set status = 0, updated_at = now() where id = #{id} and user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId);

    @Update("update tb_comment set like_count = like_count + 1 where id = #{id}")
    int incrementLikeCount(Long id);

    @Update("update tb_comment set like_count = like_count - 1 where id = #{id} and like_count > 0")
    int decrementLikeCount(Long id);

    @Select("select count(*) from tb_comment where article_id = #{articleId} and status = 1")
    int countByArticleId(Long articleId);
}
