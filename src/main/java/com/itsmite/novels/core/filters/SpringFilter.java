package com.itsmite.novels.core.filters;

import com.itsmite.novels.core.RequestContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class SpringFilter implements Filter {

    protected RequestContext requestContext;

    public SpringFilter(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        preFilter(request, response);
        chain.doFilter(request, response);
        postFilter(request, response);
    }

    protected abstract void preFilter(HttpServletRequest request, HttpServletResponse response);

    protected abstract void postFilter(HttpServletRequest request, HttpServletResponse response);
}
