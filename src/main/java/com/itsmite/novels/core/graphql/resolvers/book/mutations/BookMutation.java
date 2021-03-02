package com.itsmite.novels.core.graphql.resolvers.book.mutations;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.graphql.resolvers.book.inputs.CreateBookInput;
import com.itsmite.novels.core.graphql.resolvers.book.inputs.UpdateBookInput;
import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.services.book.BookService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Validated
public class BookMutation implements GraphQLMutationResolver {

    private BookService    bookService;
    private RequestContext requestContext;

    @Autowired
    public void autowireBeans(BookService bookService, RequestContext requestContext) {
        this.bookService = bookService;
        this.requestContext = requestContext;
    }

    public BookType createBook(@Valid CreateBookInput input) {
        Book book = bookService.createBook(
            input.getTitle(),
            input.getDescription(),
            input.getCoverPhoto(),
            (String)requestContext.get(RequestContext.USER_ID),
            (WritingSpace)requestContext.get(RequestContext.WRITING_SPACE)
        );
        return BookType.fromType(book);
    }

    public BookType updateBook(@Valid UpdateBookInput input) {
        Book book = bookService.getEditableBook(input.getBookId());
        book = bookService.updateBook(book, input.getTitle(), input.getDescription(), input.getCoverPhoto(), input.getStatus());
        return BookType.fromType(book);
    }
}
