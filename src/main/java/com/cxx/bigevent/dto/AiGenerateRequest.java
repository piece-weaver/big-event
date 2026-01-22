package com.cxx.bigevent.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiGenerateRequest {
    @NotBlank(message = "内容不能为空")
    private String content;
    
    private String type;
    
    private String context;
}
