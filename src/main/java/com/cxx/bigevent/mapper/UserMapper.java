package com.cxx.bigevent.mapper;

import com.cxx.bigevent.pojo.User;
import com.cxx.bigevent.pojo.UserVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from tb_user where username = #{username}")
    User findByUserName(String username);

    @Insert("insert into tb_user(username, password, nickname, email, create_time, update_time) " +
            "values (#{username}, #{password}, #{username}, CONCAT(#{username}, '@example.com'), now(), now())")
    void add(String username, String password);

    @Update("update tb_user set nickname = #{user.nickname},email = #{user.email}, update_time = now() where id = #{userId}")
    void update(User user, Integer userId);

    @Update("update tb_user set user_pic = #{avatarUrl}, update_time = now() where id = #{userId}")
    void updateAvatar(String avatarUrl, Integer userId);

    @Update("update tb_user set password = #{md5String},update_time = now() where id = #{userId}")
    void updatePwd(String md5String, Integer userId);

    @Select("select id, nickname, user_pic from tb_user where id = #{id}")
    UserVO findUserVOById(Long id);
}
