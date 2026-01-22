package com.cxx.bigevent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class WebhookRequest {

    @NotBlank(message = "Webhook名称不能为空")
    @Size(max = 64, message = "名称最长64字符")
    private String name;

    @NotBlank(message = "Webhook URL不能为空")
    @Size(max = 512, message = "URL最长512字符")
    private String url;

    @Size(max = 128, message = "密钥最长128字符")
    private String secret;

    @NotEmpty(message = "触发事件不能为空")
    private List<String> events;

    private Integer isActive = 1;
}
