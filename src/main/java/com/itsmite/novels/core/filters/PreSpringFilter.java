package com.itsmite.novels.core.filters;

import com.itsmite.novels.core.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class PreSpringFilter extends SpringFilter{

    public PreSpringFilter(RequestContext requestContext) {
        super(requestContext);
    }

    @Override
    protected final void postFilter(HttpServletRequest request, HttpServletResponse response) {

    }
}
