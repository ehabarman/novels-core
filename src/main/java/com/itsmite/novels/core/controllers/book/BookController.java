package com.itsmite.novels.core.controllers.book;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.annotations.JsonRequestMapping;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.payload.book.request.CreateBookRequest;
import com.itsmite.novels.core.payload.book.response.BookResponse;
import com.itsmite.novels.core.services.book.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

import static com.itsmite.novels.core.constants.EndpointConstants.API_BOOKS_ENDPOINT;

@Slf4j
@RestController
@RequestMapping(value = API_BOOKS_ENDPOINT)
public class BookController {

    private BookService    bookService;
    private RequestContext requestContext;

    @Autowired
    public void autowireBeans(BookService bookService, RequestContext requestContext) {
        this.bookService = bookService;
        this.requestContext = requestContext;
    }

    @JsonRequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest requestBody) {
        Book book = bookService.createBook(
            requestBody.getTitle(),
            requestBody.getDescription(),
            requestBody.getCoverPhoto(),
            (String)requestContext.get(RequestContext.USER_ID),
            (WritingSpace)requestContext.get(RequestContext.WRITING_SPACE)
        );
        return BookResponse.toResponse(book);
    }

    @JsonRequestMapping(path = "/{bookId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public BookResponse deleteBook(@PathVariable String bookId) {
        Book book = bookService.getEditableBook(bookId,
                                                (String)requestContext.get(RequestContext.USER_ID),
                                                (Set<ERole>)requestContext.get(RequestContext.ROLES));
        book = bookService.deleteBook(book, (WritingSpace)requestContext.get(RequestContext.WRITING_SPACE));
        return BookResponse.toResponse(book);
    }
}
