package com.itsmite.novels.core.graphql.resolvers.book.queries;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
import com.itsmite.novels.core.graphql.resolvers.user.types.UserType;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.services.book.BookService;
import com.itsmite.novels.core.services.user.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookQuery implements GraphQLQueryResolver {

    private BookService    bookService;
    private UserService    userService;
    private RequestContext requestContext;

    @Autowired
    public void autowireBeans(BookService bookService, UserService userService, RequestContext requestContext) {
        this.bookService = bookService;
        this.userService = userService;
        this.requestContext = requestContext;
    }

    public BookType getBook(String bookId) {
        Book book = bookService.findBookBy(bookId);
        return BookType.fromType(book);
    }



}
