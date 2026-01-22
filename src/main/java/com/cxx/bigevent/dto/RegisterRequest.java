package com.cxx.bigevent.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @Pattern(regexp = "^\\S{5,16}$", message = "用户名必须是5-16位非空字符")
    private String username;

    @Pattern(regexp = "^\\S{5,16}$", message = "密码必须是5-16位非空字符")
    private String password;
}
