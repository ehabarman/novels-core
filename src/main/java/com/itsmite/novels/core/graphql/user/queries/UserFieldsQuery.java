package com.itsmite.novels.core.graphql.user.queries;

import com.itsmite.novels.core.graphql.base.inputs.NumericPaginationInput;
import com.itsmite.novels.core.graphql.book.types.BookType;
import com.itsmite.novels.core.graphql.user.types.UserType;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.services.user.UserService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Component
public class UserFieldsQuery implements GraphQLResolver<UserType> {

    private UserService userService;

    @Autowired
    public void autowireBeans(UserService userService) {
        this.userService = userService;
    }

    public List<BookType> getWrittenBooks(UserType userType, @Valid NumericPaginationInput pagination) {
        User user = userService.findById(userType.getId());
        return user.getWritingSpace()
                   .getBooks().stream()
                   .map(BookType::fromType)
                   .collect(Collectors.toList());
    }
}
