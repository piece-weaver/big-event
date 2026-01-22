package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert("insert into tb_article(title, content, cover_img, state, category_id, create_user_id, create_time, update_time) " +
            "values (#{title}, #{content}, #{coverImg}, #{state}, #{categoryId}, #{createUser}, now(), now())")
    void add(Article article);

    List<Article> list(Integer categoryId, Integer state, Integer userId);

    @Select("select * from tb_article where id = #{id} and create_user_id = #{userId}")
    Article findById(Integer id, Integer userId);

    @Update("update tb_article set title = #{article.title}, content = #{article.content}, cover_img = #{article.coverImg}, state = #{article.state}, " +
            "category_id = #{article.categoryId}, update_time = now() where id = #{id} and create_user_id = #{article.createUser}")
    int update(Article article, Integer id);

    @Delete("delete from tb_article where id = #{id} and create_user_id = #{userId}")
    int delete(Integer id, Integer userId);

    @Delete("delete from tb_article where category_id = #{categoryId} and create_user_id = #{userId}")
    void deleteByCategoryId(Integer categoryId, Integer userId);

    @Select("select count(*) from tb_article where category_id = #{categoryId} and create_user_id = #{userId}")
    int countByCategoryId(Integer categoryId, Integer userId);

    @Delete("delete from tb_article where id = #{id}")
    void deleteById(Integer id);

    List<Article> findByIds(@Param("ids") List<Long> ids);

    @Select("select * from tb_article where id = #{id}")
    Article findByIdOnly(Long id);

    int batchDelete(@Param("ids") List<Integer> ids);

    int batchUpdateState(@Param("ids") List<Integer> ids, @Param("state") Integer state);

    int batchUpdateCategory(@Param("ids") List<Integer> ids, @Param("categoryId") Integer categoryId);

    @Select("select count(*) from tb_article")
    Long countTotal();

    @Select("select count(*) from tb_article where state = 1")
    Long countPublished();

    @Select("select count(*) from tb_article where state = 0")
    Long countDraft();

    @Select("select sum(view_count) from tb_article_stats")
    Long sumAllViews();

    List<Article> search(@Param("keyword") String keyword,
                         @Param("categoryId") Integer categoryId,
                         @Param("state") Integer state);

    @Update("update tb_article set state = #{state} where id = #{id}")
    int updateState(@Param("id") Long id, @Param("state") int state);

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
