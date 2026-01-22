package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.ArticleAiTags;
import com.cxx.bigevent.pojo.TagCount;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleAiTagsMapper {

    @Insert("INSERT INTO tb_article_ai_tags(article_id, tag, confidence, source, created_at) " +
            "VALUES(#{articleId}, #{tag}, #{confidence}, #{source}, NOW())")
    int insert(ArticleAiTags tag);

    @Select("SELECT * FROM tb_article_ai_tags WHERE article_id = #{articleId} ORDER BY confidence DESC")
    List<ArticleAiTags> findByArticleId(Long articleId);

    @Select("SELECT * FROM tb_article_ai_tags WHERE article_id = #{articleId} AND source = 'ai' ORDER BY confidence DESC")
    List<ArticleAiTags> findAiTagsByArticleId(Long articleId);

    @Delete("DELETE FROM tb_article_ai_tags WHERE article_id = #{articleId}")
    int deleteByArticleId(Long articleId);

    @Delete("DELETE FROM tb_article_ai_tags WHERE article_id = #{articleId} AND source = 'ai'")
    int deleteAiTagsByArticleId(Long articleId);

    @Select("SELECT DISTINCT tag FROM tb_article_ai_tags ORDER BY tag")
    List<String> findAllTags();

    @Select("SELECT tag, COUNT(*) as count FROM tb_article_ai_tags GROUP BY tag ORDER BY count DESC LIMIT #{limit}")
    List<TagCount> findPopularTags(@Param("limit") Integer limit);

    @Select("SELECT COUNT(*) FROM tb_article_ai_tags WHERE article_id = #{articleId}")
    Long countByArticleId(Long articleId);
}
