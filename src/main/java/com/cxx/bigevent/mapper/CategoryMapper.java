package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Insert("insert into tb_category(category_name, category_alias, create_user_id, create_time, update_time) " +
            "values (#{category.categoryName}, #{category.categoryAlias}, #{userId}, now(), now())")
    void add(Category category, Integer userId);

    @Select("select * from tb_category where create_user_id = #{userId}")
    List<Category> list(Integer userId);

    @Select("select * from tb_category")
    List<Category> listAll();

    @Select("select * from tb_category where id = #{id} and create_user_id = #{userId}")
    Category findCategoryById(Integer id, Integer userId);

    @Select("select * from tb_article where category_id = #{id} and create_user_id = #{userId}")
    List<Article> findArticlesByCategoryId(Integer id, Integer userId);

    @Update("update tb_category set category_name = #{category.categoryName}, category_alias = #{category.categoryAlias}, " +
            "update_time = now() where id = #{id} and create_user_id = #{userId}")
    void update(Category category, Integer id, Integer userId);

    @Delete("delete from tb_category where id = #{id} and create_user_id = #{userId}")
    int delete(Integer id, Integer userId);
}
