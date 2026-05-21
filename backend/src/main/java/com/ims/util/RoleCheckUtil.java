package com.ims.util;

import com.ims.common.BusinessException;
import org.springframework.util.StringUtils;

public final class RoleCheckUtil {

    private RoleCheckUtil() {
    }

    public static String requireRole(String role, String expectedRole) {
        if (!StringUtils.hasText(role)) {
            throw new BusinessException(401, "未登录或缺少角色信息");
        }
        String normalizedRole = role.trim().toUpperCase();
        if (!expectedRole.equalsIgnoreCase(normalizedRole)) {
            throw new BusinessException(403, "无权限访问该接口");
        }
        return normalizedRole;
    }

    public static String requireRelatedId(String relatedId) {
        if (!StringUtils.hasText(relatedId)) {
            throw new BusinessException(401, "缺少用户关联编号");
        }
        return relatedId.trim();
    }
}
