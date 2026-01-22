package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.ArticleVersion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleVersionMapper {

    @Insert("INSERT INTO tb_article_version(article_id, title, content, cover_img, state, category_id, " +
            "version_number, change_summary, created_by, created_at) " +
            "VALUES(#{articleId}, #{title}, #{content}, #{coverImg}, #{state}, #{categoryId}, " +
            "#{versionNumber}, #{changeSummary}, #{createdBy}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ArticleVersion version);

    @Select("SELECT * FROM tb_article_version WHERE article_id = #{articleId} ORDER BY version_number DESC")
    List<ArticleVersion> findByArticleId(Long articleId);

    @Select("SELECT * FROM tb_article_version WHERE article_id = #{articleId} ORDER BY version_number DESC LIMIT 1")
    ArticleVersion findLatestVersion(Long articleId);

    @Select("SELECT * FROM tb_article_version WHERE article_id = #{articleId} AND version_number = #{versionNumber}")
    ArticleVersion findByArticleIdAndVersion(Long articleId, Integer versionNumber);

    @Select("SELECT IFNULL(MAX(version_number), 0) FROM tb_article_version WHERE article_id = #{articleId}")
    Integer findMaxVersionNumber(Long articleId);

    @Delete("DELETE FROM tb_article_version WHERE article_id = #{articleId}")
    int deleteByArticleId(Long articleId);

    @Select("SELECT COUNT(*) FROM tb_article_version WHERE article_id = #{articleId}")
    Long countByArticleId(Long articleId);
}
