package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.ArticleStats;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleStatsMapper {

    @Insert("INSERT INTO tb_article_stats(article_id, view_count, like_count, share_count, collect_count, comment_count, updated_at) " +
            "VALUES(#{articleId}, 0, 0, 0, 0, 0, NOW()) ON DUPLICATE KEY UPDATE updated_at = NOW()")
    void insertOrUpdate(ArticleStats stats);

    @Select("SELECT * FROM tb_article_stats WHERE article_id = #{articleId}")
    ArticleStats findByArticleId(Long articleId);

    @Update("UPDATE tb_article_stats SET view_count = view_count + 1, last_viewed_at = NOW(), updated_at = NOW() WHERE article_id = #{articleId}")
    void incrementViewCount(Long articleId);

    @Update("UPDATE tb_article_stats SET like_count = like_count + 1, updated_at = NOW() WHERE article_id = #{articleId}")
    void incrementLikeCount(Long articleId);

    @Update("UPDATE tb_article_stats SET share_count = share_count + 1, updated_at = NOW() WHERE article_id = #{articleId}")
    void incrementShareCount(Long articleId);

    @Update("UPDATE tb_article_stats SET collect_count = collect_count + 1, updated_at = NOW() WHERE article_id = #{articleId}")
    void incrementCollectCount(Long articleId);

    @Update("UPDATE tb_article_stats SET comment_count = comment_count + 1, updated_at = NOW() WHERE article_id = #{articleId}")
    void incrementCommentCount(Long articleId);

    @Update("UPDATE tb_article_stats SET comment_count = comment_count - 1, updated_at = NOW() WHERE article_id = #{articleId} AND comment_count > 0")
    void decrementCommentCount(Long articleId);

    @Update("UPDATE tb_article_stats SET comment_count = #{count}, updated_at = NOW() WHERE article_id = #{articleId}")
    void updateCommentCount(@Param("articleId") Long articleId, @Param("count") int count);

    @Update("UPDATE tb_article_stats SET like_count = like_count - 1, updated_at = NOW() WHERE article_id = #{articleId} AND like_count > 0")
    void decrementLikeCount(Long articleId);

    @Update("UPDATE tb_article_stats SET collect_count = collect_count - 1, updated_at = NOW() WHERE article_id = #{articleId} AND collect_count > 0")
    void decrementCollectCount(Long articleId);

    @Select("SELECT SUM(view_count) FROM tb_article_stats")
    Long sumAllViews();

    @Select("SELECT SUM(like_count) FROM tb_article_stats")
    Long sumAllLikes();

    @Select("SELECT SUM(share_count) FROM tb_article_stats")
    Long sumAllShares();

    @Select("SELECT * FROM tb_article_stats ORDER BY view_count DESC LIMIT #{limit}")
    java.util.List<ArticleStats> findTopByViews(@Param("limit") Integer limit);
}
