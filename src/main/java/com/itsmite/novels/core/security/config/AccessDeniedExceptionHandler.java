package com.itsmite.novels.core.security.config;

import com.google.gson.Gson;
import com.itsmite.novels.core.errors.api.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Slf4j
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    private static final String USER_UNAUTHORIZED_MESSAGE_TEMPLATE = "User: %s attempted to access the protected URL: %s";
    private static final String UNAUTHORIZED_ACCESS_MESSAGE        = "Unauthorized action";
    private static final Gson   GSON                               = new Gson();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.warn(String.format(USER_UNAUTHORIZED_MESSAGE_TEMPLATE, auth.getName(), request.getRequestURI()));
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_ACCESS_MESSAGE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String responseBody = GSON.toJson(apiError.toMap());
        PrintWriter out = response.getWriter();
        response.setContentType(APPLICATION_JSON);
        response.setCharacterEncoding(UTF_8.name());
        out.print(responseBody);
        out.flush();
    }
}