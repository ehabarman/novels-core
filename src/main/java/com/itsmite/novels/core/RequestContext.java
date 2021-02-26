package com.itsmite.novels.core;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Manage data passed through out the request/thread
 */

@Component
public class RequestContext {

    public static final String USER_ID       = "userId";
    public static final String USERNAME      = "username";
    public static final String EMAIL         = "email";
    public static final String ROLES         = "roles";
    public static final String AUTHORITIES   = "authorities";
    public static final String WRITING_SPACE = "writingSpace";
    public static final String READING_SPACE = "readingSpace";

    public void put(String key, Object value) {
        HttpServletRequest request = getAttributes();
        request.setAttribute(key, value);
    }

    public Object get(String key) {
        HttpServletRequest request = getAttributes();
        return request.getAttribute(key);
    }

    private HttpServletRequest getAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return ((ServletRequestAttributes)requestAttributes).getRequest();
    }
}
