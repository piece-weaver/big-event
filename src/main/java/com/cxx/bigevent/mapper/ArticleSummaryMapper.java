package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.ArticleSummary;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleSummaryMapper {

    @Insert("INSERT INTO tb_article_summary(article_id, summary, reading_time, seo_score, seo_suggestions, model_used, created_at, updated_at) " +
            "VALUES(#{articleId}, #{summary}, #{readingTime}, #{seoScore}, #{seoSuggestions}, #{modelUsed}, NOW(), NOW()) " +
            "ON DUPLICATE KEY UPDATE summary = #{summary}, reading_time = #{readingTime}, seo_score = #{seoScore}, " +
            "seo_suggestions = #{seoSuggestions}, model_used = #{modelUsed}, updated_at = NOW()")
    int insertOrUpdate(ArticleSummary summary);

    @Select("SELECT * FROM tb_article_summary WHERE article_id = #{articleId}")
    ArticleSummary findByArticleId(Long articleId);

    @Select("SELECT * FROM tb_article_summary WHERE article_id = #{articleId} AND summary IS NOT NULL")
    ArticleSummary findExistingSummary(Long articleId);

    @Delete("DELETE FROM tb_article_summary WHERE article_id = #{articleId}")
    int deleteByArticleId(Long articleId);

    @Select("SELECT AVG(seo_score) FROM tb_article_summary WHERE seo_score IS NOT NULL")
    Double avgSeoScore();

    @Select("SELECT COUNT(*) FROM tb_article_summary WHERE summary IS NOT NULL")
    Long countWithSummary();
}
