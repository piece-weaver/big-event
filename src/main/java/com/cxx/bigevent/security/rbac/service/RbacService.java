package com.cxx.bigevent.security.rbac.service;

import java.util.List;
import java.util.Set;

public interface RbacService {
    Set<String> getUserRoles(Integer userId);
    Set<String> getUserPermissions(Integer userId);
    boolean hasPermission(Integer userId, String permission);
    boolean hasRole(Integer userId, String role);
    List<Long> getRoleIdsByUserId(Integer userId);
    void assignRolesToUser(Integer userId, List<Long> roleIds);
    void removeRolesFromUser(Integer userId, List<Long> roleIds);
    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    void removePermissionsFromRole(Long roleId, List<Long> permissionIds);
}
