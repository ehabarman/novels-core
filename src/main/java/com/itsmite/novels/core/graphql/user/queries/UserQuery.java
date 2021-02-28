package com.itsmite.novels.core.graphql.user.queries;

import com.itsmite.novels.core.graphql.user.types.UserType;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.services.user.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserQuery implements GraphQLQueryResolver {

    private UserService userService;

    @Autowired
    public void autowireBeans(UserService userService) {
        this.userService = userService;
    }

    public UserType getUser(String id) {
        User user = userService.findById(id);
        return UserType.fromType(user);
    }
}
