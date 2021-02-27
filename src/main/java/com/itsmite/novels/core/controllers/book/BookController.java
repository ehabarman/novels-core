package com.itsmite.novels.core.controllers.book;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.annotations.JsonRequestMapping;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.payload.book.request.CreateBookRequest;
import com.itsmite.novels.core.payload.book.request.UpdateBookRequest;
import com.itsmite.novels.core.payload.book.response.BookResponse;
import com.itsmite.novels.core.services.book.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static com.itsmite.novels.core.constants.EndpointConstants.API_BOOKS_ENDPOINT;
import static com.itsmite.novels.core.constants.EndpointConstants.BOOK_ID_PARAM;
import static com.itsmite.novels.core.constants.EndpointConstants.BOOK_ID_PATH;
import static com.itsmite.novels.core.constants.EndpointConstants.OWNER_ID_PARAM;

@Slf4j
@Validated
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

    @JsonRequestMapping(path = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> getBooks(@Min(0) @RequestParam(defaultValue = "0") int page, @Min(1) @Max(100) @RequestParam(defaultValue = "10") int size) {
        return bookService.findAll(page, size).stream()
                          .map(BookResponse::toResponse)
                          .collect(Collectors.toList());
    }

    @JsonRequestMapping(path = "/owner/{ownerId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> getBooksByOwner(@PathVariable(OWNER_ID_PARAM) String ownerId,
                                              @Min(0) @RequestParam(defaultValue = "0") int page,
                                              @Min(1) @Max(100) @RequestParam(defaultValue = "10") int size) {
        return bookService.findAllByOwnerId(ownerId, page, size).stream()
                          .map(BookResponse::toResponse)
                          .collect(Collectors.toList());
    }

    @JsonRequestMapping(path = BOOK_ID_PATH, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public BookResponse updateBook(@PathVariable(BOOK_ID_PARAM) String bookId, @Valid @RequestBody UpdateBookRequest requestBody) {
        Book book = bookService.getEditableBook(bookId);
        book = bookService.updateBook(book, requestBody.getTitle(), requestBody.getDescription(), requestBody.getCoverPhoto(), requestBody.getStatus());
        return BookResponse.toResponse(book);
    }

    @JsonRequestMapping(path = BOOK_ID_PATH, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public BookResponse getBook(@PathVariable(BOOK_ID_PARAM) String bookId) {
        Book book = bookService.findBookById(bookId);
        return BookResponse.toResponse(book);
    }
}
