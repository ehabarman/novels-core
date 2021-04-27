package com.itsmite.novels.core.configs;

import com.itsmite.novels.core.errors.security.AccessDeniedExceptionHandler;
import com.itsmite.novels.core.errors.security.AuthEntryPointJwt;
import com.itsmite.novels.core.filters.security.AuthTokenFilter;
import com.itsmite.novels.core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.itsmite.novels.core.constants.EndpointConstants.API_AUTH_V1_ENDPOINT;
import static com.itsmite.novels.core.constants.EndpointConstants.API_HEALTH_V1_ENDPOINT;
import static com.itsmite.novels.core.constants.EndpointConstants.API_ROLES_V1_ENDPOINT;

/**
 * TODO: Authentication paths, rules and everything else should be loaded from configuration file xml or from database
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    // {@link https://docs.spring.io/spring-security/site/docs/current/reference/html5/#method-security-expressions}
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService           userService;
    private AuthEntryPointJwt     unauthorizedHandler;

    @Autowired
    public void autowireBeans(BCryptPasswordEncoder bCryptPasswordEncoder,
                              UserService userService,
                              AuthEntryPointJwt unauthorizedHandler) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
            .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        configureApiAccess(http);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private void configureApiAccess(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                    .antMatchers(API_AUTH_V1_ENDPOINT + "/**").permitAll()
                    .antMatchers("/v3/api-docs/**").permitAll()
                    .antMatchers("/v2/api-docs/**").permitAll()
                    .antMatchers("/graphql/**").permitAll()
                    .antMatchers("/graphiql/**").permitAll()
                    .antMatchers("/vendor/graphiql/**").permitAll()
                    .antMatchers(API_ROLES_V1_ENDPOINT + "/**").permitAll()
                    .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
           .antMatchers("/v3/api-docs/**")
           .antMatchers("/swagger-resources/**")
           .antMatchers("/swagger-ui/**")
           .antMatchers(API_HEALTH_V1_ENDPOINT + "*/**");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedExceptionHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
