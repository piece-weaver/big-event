package com.cxx.bigevent.security.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Role {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean isSystem;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
