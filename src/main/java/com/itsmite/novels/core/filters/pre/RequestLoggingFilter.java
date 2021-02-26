package com.itsmite.novels.core.filters.pre;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.filters.PreSpringFilter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequestLoggingFilter extends PreSpringFilter {

    public RequestLoggingFilter(RequestContext requestContext) {
        super(requestContext);
    }

    @Override
    protected void preFilter(HttpServletRequest request, HttpServletResponse response) {
        logClientInfo();
        logRequest(request);
    }

    private void logClientInfo() {
        if (requestContext.get(RequestContext.USER_ID) != null) {
            log.info("Request client: [ 'id': '{}', 'username': '{}']", requestContext.get(RequestContext.USER_ID),
                     requestContext.get(RequestContext.USERNAME));
        } else {
            log.info("Request client is an anonymous user");
        }
    }

    private void logRequest(HttpServletRequest request) {
        log.info("{} {}", request.getMethod(), request.getRequestURL().toString());
    }
}
