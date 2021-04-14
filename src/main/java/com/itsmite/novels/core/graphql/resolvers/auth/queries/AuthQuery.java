package com.itsmite.novels.core.graphql.resolvers.auth.queries;

import com.itsmite.novels.core.RequestContext;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthQuery implements GraphQLQueryResolver {

    private RequestContext requestContext;

    @Autowired
    public void autowireBeans(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @SuppressWarnings("Used by graphql reflection")
    public Boolean loggedIn() {
        return requestContext.get(RequestContext.USER_ID) != null;
    }
}
