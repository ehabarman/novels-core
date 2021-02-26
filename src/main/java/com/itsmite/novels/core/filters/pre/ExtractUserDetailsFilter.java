package com.itsmite.novels.core.filters.pre;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.filters.PreSpringFilter;
import com.itsmite.novels.core.filters.SpringFilter;
import com.itsmite.novels.core.services.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ExtractUserDetailsFilter extends PreSpringFilter {

    private static final String ANONYMOUS_USER = "anonymousUser";

    public ExtractUserDetailsFilter(RequestContext requestContext) {
        super(requestContext);
    }

    @Override
    protected void preFilter(HttpServletRequest request, HttpServletResponse response) {
        log.info("ExtractUserDetailsFilter says hi");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object user = authentication.getPrincipal();
        if (!ANONYMOUS_USER.equals(user)) {
            UserDetailsImpl userDetails = (UserDetailsImpl)user;
            requestContext.put(RequestContext.USER_ID, userDetails.getId());
            requestContext.put(RequestContext.USERNAME, userDetails.getUsername());
            requestContext.put(RequestContext.EMAIL, userDetails.getEmail());
            requestContext.put(RequestContext.READING_SPACE_ID, userDetails.getReadingSpaceId());
            requestContext.put(RequestContext.WRITING_SPACE_ID, userDetails.getWritingSpaceId());
            requestContext.put(RequestContext.AUTHORITIES, userDetails.getAuthorities());
        }
    }
}
