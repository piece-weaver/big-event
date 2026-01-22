package com.cxx.bigevent.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BehaviorDTO {
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @NotNull(message = "行为类型不能为空")
    private String behaviorType;

    private Integer duration;
}
