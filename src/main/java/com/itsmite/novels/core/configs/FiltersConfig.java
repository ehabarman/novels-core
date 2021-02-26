package com.itsmite.novels.core.configs;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.filters.pre.ExtractUserDetailsFilter;
import com.itsmite.novels.core.filters.pre.RequestLoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO: The filters details shouldn't be hardcoded here, it is redundant and really readable as the filters grow.
 *       The correct way is load them from a configuration file such as .xml then build the beans accordingly.
 */
@Configuration
public class FiltersConfig {

    @Autowired
    private RequestContext requestContext;

    @Bean
    public FilterRegistrationBean<ExtractUserDetailsFilter> extractUserDetailsFilterBean() {
        FilterRegistrationBean<ExtractUserDetailsFilter> registrationBean = new FilterRegistrationBean<>();
        ExtractUserDetailsFilter filter = new ExtractUserDetailsFilter(requestContext);
        registrationBean.setFilter(filter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilterBean() {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        RequestLoggingFilter filter = new RequestLoggingFilter(requestContext);
        registrationBean.setFilter(filter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
