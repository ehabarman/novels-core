package com.itsmite.novels.core.graphql.resolvers.book.queries;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.services.book.BookService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookQuery implements GraphQLQueryResolver {

    private BookService    bookService;
    private RequestContext requestContext;

    @Autowired
    public void autowireBeans(BookService bookService, RequestContext requestContext) {
        this.bookService = bookService;
        this.requestContext = requestContext;
    }

    public BookType getBook(String id) {
        Book book = bookService.findBookBy(id);
        return BookType.fromType(book);
    }
}
