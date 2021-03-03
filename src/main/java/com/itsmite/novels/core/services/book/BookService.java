package com.itsmite.novels.core.services.book;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.errors.exceptions.ResourceNotFoundException;
import com.itsmite.novels.core.errors.exceptions.UnauthorizedResourceAction;
import com.itsmite.novels.core.graphql.resolvers.base.inputs.NumericPaginationInput;
import com.itsmite.novels.core.graphql.resolvers.book.inputs.BooksFilter;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.book.BookStatus;
import com.itsmite.novels.core.models.book.Chapter;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.repositories.book.BookRepository;
import com.itsmite.novels.core.repositories.user.WritingSpaceRepository;
import com.itsmite.novels.core.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class BookService {

    private static final String RESOURCE_TYPE = "BOOK";

    private BookRepository         bookRepository;
    private WritingSpaceRepository writingSpaceRepository;
    private RequestContext         requestContext;

    @Autowired
    public void autowireBeans(BookRepository bookRepository, WritingSpaceRepository writingSpaceRepository, RequestContext requestContext) {
        this.bookRepository = bookRepository;
        this.writingSpaceRepository = writingSpaceRepository;
        this.requestContext = requestContext;
    }

    public List<Book> findAll(int page, int size) {
        Page<Book> pageBooks = bookRepository.findAll(PageRequest.of(page, size));
        return pageBooks.getContent();
    }

    public List<Book> findAllByOwnerId(String ownerId, int page, int size) {
        Page<Book> pageBooks = bookRepository.findAllByOwnerId(ownerId, PageRequest.of(page, size));
        return pageBooks.getContent();
    }

    public List<Book> findAllByFilter(BooksFilter booksFilter, int page, int size) {
        if (booksFilter == null) {
            return findAll(page, size);
        }
        Query query = booksFilter.buildQuery();
        Page<Book> pageBooks = bookRepository.findAll(query, PageRequest.of(page, size));
        return pageBooks.getContent();
    }

    public Book getEditableBook(String bookId) {
        String userId = (String)requestContext.get(RequestContext.USER_ID);
        Set<ERole> roles = (Set<ERole>)requestContext.get(RequestContext.ROLES);
        Book book = findBookByIdOrElseThrowException(bookId);
        if (!book.getOwnerId().equals(userId)) {
            if (CollectionUtil.isNullOrEmpty(roles) || !roles.contains(ERole.ADMIN)) {
                throw new UnauthorizedResourceAction(RESOURCE_TYPE, bookId);
            }
        }
        return book;
    }

    public Book findBookByIdOrElseThrowException(String bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book of id " + bookId));
    }

    public Book findBookBy(String bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public Book createBook(String title, String description, String coverPhoto, String ownerId, WritingSpace writingSpace) {
        Book book = new Book();
        book.setTitle(title);
        book.setDescription(description);
        book.setCoverPhoto(coverPhoto);
        book.setOwnerId(ownerId);
        book = bookRepository.save(book);
        writingSpace.getBooks().add(book);
        writingSpaceRepository.save(writingSpace);
        return book;
    }

    public Book updateBook(Book book, String title, String description, String coverPhoto, BookStatus status) {
        book.setTitle(title);
        book.setDescription(description);
        book.setCoverPhoto(coverPhoto);
        book.setStatus(status);
        return bookRepository.save(book);
    }

    public Book addChapter(Book book, Chapter chapter) {
        book.getChapters().add(chapter);
        return bookRepository.save(book);
    }

    public Book removeChapter(Book book, Chapter chapter) {
        book.getChapters().remove(chapter);
        return bookRepository.save(book);
    }
}
