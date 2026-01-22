package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.ArticleCollect;
import com.cxx.bigevent.pojo.ArticleLike;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleLikeMapper {

    @Select("SELECT * FROM tb_article_like WHERE article_id = #{articleId} AND user_id = #{userId}")
    ArticleLike findByArticleAndUser(@Param("articleId") Long articleId, @Param("userId") Long userId);

    @Insert("INSERT INTO tb_article_like(article_id, user_id, created_at) VALUES (#{articleId}, #{userId}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ArticleLike like);

    @Delete("DELETE FROM tb_article_like WHERE article_id = #{articleId} AND user_id = #{userId}")
    int delete(@Param("articleId") Long articleId, @Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM tb_article_like WHERE article_id = #{articleId}")
    int countByArticleId(Long articleId);

    @Select("SELECT COUNT(*) FROM tb_article_like WHERE user_id = #{userId}")
    int countByUserId(Long userId);
}
