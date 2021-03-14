package com.itsmite.novels.core.graphql.resolvers.user.queries;

import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
import com.itsmite.novels.core.graphql.resolvers.user.types.UserType;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.services.user.UserService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFieldsQuery implements GraphQLResolver<UserType> {

    private UserService userService;

    @Autowired
    public void autowireBeans(UserService userService) {
        this.userService = userService;
    }

    @SuppressWarnings("Used by graphql reflection")
    public List<BookType> getWrittenBooks(UserType userType) {
        User user = userService.findById(userType.getId());
        return BookType.toType(user.getWritingSpace().getBooks());
    }
}
