package com.cxx.bigevent.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollectDTO {
    @Size(max = 50, message = "收藏夹名称不能超过50字符")
    private String folderName;
}
