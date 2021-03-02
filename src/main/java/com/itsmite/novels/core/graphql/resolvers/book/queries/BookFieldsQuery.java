package com.itsmite.novels.core.graphql.resolvers.book.queries;

import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
import com.itsmite.novels.core.graphql.resolvers.user.types.UserType;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.services.user.UserService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookFieldsQuery implements GraphQLResolver<BookType> {

    private UserService userService;

    @Autowired
    public void autowireBeans(UserService userService) {
        this.userService = userService;
    }

    public UserType getOwner(BookType book) {
        User user = userService.findById(book.getOwner().getId());
        return UserType.fromType(user);
    }
}
