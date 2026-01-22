package com.cxx.bigevent.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordUpdateRequest {

    @NotEmpty(message = "原密码不能为空")
    private String oldPwd;

    @NotEmpty(message = "新密码不能为空")
    private String newPwd;

    @NotEmpty(message = "确认密码不能为空")
    private String rePwd;
}
