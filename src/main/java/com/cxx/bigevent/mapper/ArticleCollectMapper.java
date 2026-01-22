package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.ArticleCollect;
import com.cxx.bigevent.pojo.ArticleCollectVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleCollectMapper {

    @Insert("INSERT INTO tb_article_collect(article_id, user_id, folder_name, created_at) VALUES (#{articleId}, #{userId}, #{folderName}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ArticleCollect collect);

    @Delete("DELETE FROM tb_article_collect WHERE article_id = #{articleId} AND user_id = #{userId}")
    int delete(@Param("articleId") Long articleId, @Param("userId") Long userId);

    @Select("SELECT * FROM tb_article_collect WHERE article_id = #{articleId} AND user_id = #{userId}")
    ArticleCollect findByArticleAndUser(@Param("articleId") Long articleId, @Param("userId") Long userId);

    List<ArticleCollectVO> findByUserId(Long userId);

    @Select("SELECT DISTINCT folder_name FROM tb_article_collect WHERE user_id = #{userId}")
    List<String> findFolderNamesByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM tb_article_collect WHERE article_id = #{articleId}")
    int countByArticleId(Long articleId);

    @Select("SELECT COUNT(*) FROM tb_article_collect WHERE user_id = #{userId}")
    int countByUserId(Long userId);
}
