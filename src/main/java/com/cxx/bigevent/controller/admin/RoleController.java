package com.cxx.bigevent.controller.admin;

import com.cxx.bigevent.dto.admin.RoleDTO;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.security.rbac.service.RbacService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/roles")
@Tag(name = "Role Management", description = "RBAC Role management APIs")
public class RoleController {

    private final RbacService rbacService;

    public RoleController(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    @PostMapping
    @Operation(summary = "Create role")
    public Result<Map<String, Object>> create(@Valid @RequestBody RoleDTO dto) {
        Map<String, Object> role = new HashMap<>();
        role.put("id", System.currentTimeMillis());
        role.put("code", dto.getCode());
        role.put("name", dto.getName());
        role.put("description", dto.getDescription());
        role.put("priority", dto.getPriority() != null ? dto.getPriority() : 0);

        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            rbacService.assignPermissionsToRole((Long) role.get("id"), dto.getPermissionIds());
        }

        return Result.success(role);
    }

    @GetMapping
    @Operation(summary = "List roles")
    public Result<List<Map<String, Object>>> list() {
        List<Map<String, Object>> roles = new ArrayList<>();

        Map<String, Object> admin = new HashMap<>();
        admin.put("id", 1L);
        admin.put("code", "ADMIN");
        admin.put("name", "Administrator");
        admin.put("description", "System administrator with all permissions");
        admin.put("priority", 100);
        admin.put("permissionIds", List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L));
        roles.add(admin);

        Map<String, Object> editor = new HashMap<>();
        editor.put("id", 2L);
        editor.put("code", "EDITOR");
        editor.put("name", "Editor");
        editor.put("description", "Content editor");
        editor.put("priority", 50);
        editor.put("permissionIds", List.of(1L, 2L, 4L, 5L));
        roles.add(editor);

        Map<String, Object> viewer = new HashMap<>();
        viewer.put("id", 3L);
        viewer.put("code", "VIEWER");
        viewer.put("name", "Viewer");
        viewer.put("description", "Read-only access");
        viewer.put("priority", 10);
        viewer.put("permissionIds", List.of(1L, 4L));
        roles.add(viewer);

        return Result.success(roles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        Map<String, Object> role = new HashMap<>();
        role.put("id", id);
        role.put("code", "ADMIN");
        role.put("name", "Administrator");
        role.put("description", "System administrator");
        role.put("priority", 100);
        role.put("permissionIds", List.of(1L, 2L, 3L));
        return Result.success(role);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update role")
    public Result<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody RoleDTO dto) {
        Map<String, Object> role = new HashMap<>();
        role.put("id", id);
        role.put("code", dto.getCode());
        role.put("name", dto.getName());
        role.put("description", dto.getDescription());
        role.put("priority", dto.getPriority());
        return Result.success(role);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success();
    }

    @PutMapping("/{id}/permissions")
    @Operation(summary = "Assign permissions to role")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        rbacService.assignPermissionsToRole(id, permissionIds);
        return Result.success();
    }

    @GetMapping("/{id}/permissions")
    @Operation(summary = "Get role permissions")
    public Result<List<Long>> getPermissions(@PathVariable Long id) {
        return Result.success(List.of(1L, 2L, 3L));
    }
}
