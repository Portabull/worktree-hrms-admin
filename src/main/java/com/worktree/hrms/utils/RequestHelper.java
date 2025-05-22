package com.worktree.hrms.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestHelper {

    private RequestHelper() {
    }

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static boolean isEmpty(Object str) {
        return StringUtils.isEmpty(str);
    }

    public static String getAuthorizationToken() {
        String authorization = RequestHelper.getCurrentRequest().getHeader("Authorization");
        if (!isEmpty(authorization) && authorization.contains("Bearer")) {
            return authorization.replace("Bearer", "").trim();
        }
        return authorization;
    }

    public static String getHeader(String headerKey) {
        return RequestHelper.getCurrentRequest().getHeader(headerKey);
    }
}
