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
}
