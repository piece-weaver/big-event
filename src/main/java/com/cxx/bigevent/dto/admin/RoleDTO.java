package com.cxx.bigevent.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Long id;

    @NotBlank(message = "Role code cannot be empty")
    @Size(max = 50, message = "Role code max 50 characters")
    private String code;

    @NotBlank(message = "Role name cannot be empty")
    @Size(max = 100, message = "Role name max 100 characters")
    private String name;

    @Size(max = 255, message = "Description max 255 characters")
    private String description;

    private Integer priority;

    private List<Long> permissionIds;
}
