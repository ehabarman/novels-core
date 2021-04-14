package com.itsmite.novels.core.graphql.resolvers.book.queries;

import com.itsmite.novels.core.graphql.resolvers.base.inputs.NumericPaginationInput;
import com.itsmite.novels.core.graphql.resolvers.book.inputs.BooksFilter;
import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.services.book.BookService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Component
@Validated
public class BookQuery implements GraphQLQueryResolver {

    private BookService    bookService;

    @Autowired
    public void autowireBeans(BookService bookService) {
        this.bookService = bookService;
    }

    @SuppressWarnings("Used by graphql reflection")
    public BookType getBook(String bookId) {
        Book book = bookService.findBookBy(bookId);
        return BookType.toType(book);
    }

    @SuppressWarnings("Used by graphql reflection")
    public List<BookType> getBooks(@Valid BooksFilter booksFilter,@Valid NumericPaginationInput paginationInput) {
        List<Book> books = bookService.findAllByFilter(booksFilter, paginationInput.getOffset(), paginationInput.getSize());
        return BookType.toType(books);
    }
}
