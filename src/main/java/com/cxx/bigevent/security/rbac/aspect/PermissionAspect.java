package com.cxx.bigevent.security.rbac.aspect;

import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.security.rbac.annotation.RequirePermission;
import com.cxx.bigevent.security.rbac.annotation.RequireRole;
import com.cxx.bigevent.security.rbac.service.RbacService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
public class PermissionAspect {

    private final RbacService rbacService;

    public PermissionAspect(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) {
        Integer userId = getCurrentUserId();
        Set<String> userPermissions = rbacService.getUserPermissions(userId);

        String[] requiredPermissions = requirePermission.value().split(",");
        boolean hasPermission = requirePermission.checkAll()
            ? userPermissions.containsAll(Arrays.asList(requiredPermissions))
            : Arrays.stream(requiredPermissions).anyMatch(userPermissions::contains);

        if (!hasPermission) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return proceed(joinPoint);
    }

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) {
        Integer userId = getCurrentUserId();
        Set<String> userRoles = rbacService.getUserRoles(userId);

        boolean hasRole = Arrays.stream(requireRole.value())
            .anyMatch(userRoles::contains);

        if (!hasRole) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return proceed(joinPoint);
    }

    private Integer getCurrentUserId() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Object id = map.get("id");
        if (id == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return (Integer) id;
    }

    private Object proceed(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
