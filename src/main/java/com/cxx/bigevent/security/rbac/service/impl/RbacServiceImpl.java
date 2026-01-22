package com.cxx.bigevent.security.rbac.service.impl;

import com.cxx.bigevent.security.rbac.service.RbacService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RbacServiceImpl implements RbacService {

    @Override
    public Set<String> getUserPermissions(Integer userId) {
        Set<String> permissions = new HashSet<>();
        permissions.add("article:read");
        permissions.add("article:write");
        permissions.add("article:delete");
        permissions.add("category:read");
        permissions.add("category:write");
        permissions.add("user:read");
        return permissions;
    }

    @Override
    public Set<String> getUserRoles(Integer userId) {
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        return roles;
    }

    @Override
    public boolean hasPermission(Integer userId, String permission) {
        return getUserPermissions(userId).contains(permission);
    }

    @Override
    public boolean hasRole(Integer userId, String role) {
        return getUserRoles(userId).contains(role);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Integer userId) {
        return List.of(1L);
    }

    @Override
    public void assignRolesToUser(Integer userId, List<Long> roleIds) {
    }

    @Override
    public void removeRolesFromUser(Integer userId, List<Long> roleIds) {
    }

    @Override
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
    }

    @Override
    public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
    }
}
