package com.cxx.bigevent.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BatchOperationRequest {
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;
    
    private Integer categoryId;
    
    private Integer state;
    
    private Boolean isForce;
}
