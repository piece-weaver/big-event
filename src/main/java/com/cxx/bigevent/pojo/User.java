package com.cxx.bigevent.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    @NotNull(message = "用户ID不能为空", groups = {User.Update.class})
    private Integer id;

    @Pattern(regexp = "^\\S{5,16}$", message = "用户名必须是5-16位非空字符")
    private String username;

    @JsonIgnore
    private String password;

    @NotEmpty(message = "昵称不能为空")
    @Pattern(regexp = "^.{1,20}$", message = "昵称必须是1-20位字符")
    private String nickname;

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private String userPic;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public interface Update {
    }
}
