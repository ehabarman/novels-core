package com.itsmite.novels.core.filters;

import com.itsmite.novels.core.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class PostSpringFilter extends SpringFilter {

    public PostSpringFilter(RequestContext requestContext) {
        super(requestContext);
    }

    @Override
    protected final void preFilter(HttpServletRequest request, HttpServletResponse response) {

    }
}