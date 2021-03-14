package com.itsmite.novels.core.graphql.resolvers.book.mutations;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.errors.exceptions.ResourceNotFoundException;
import com.itsmite.novels.core.graphql.resolvers.book.inputs.CreateBookInput;
import com.itsmite.novels.core.graphql.resolvers.book.inputs.CreateChapterInput;
import com.itsmite.novels.core.graphql.resolvers.book.inputs.UpdateBookInput;
import com.itsmite.novels.core.graphql.resolvers.book.inputs.UpdateChapterInput;
import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
import com.itsmite.novels.core.graphql.resolvers.book.types.ChapterType;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.book.Chapter;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.services.book.BookService;
import com.itsmite.novels.core.services.book.ChapterService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Validated
public class BookMutation implements GraphQLMutationResolver {

    private BookService    bookService;
    private ChapterService chapterService;
    private RequestContext requestContext;

    @Autowired
    public void autowireBeans(BookService bookService, ChapterService chapterService, RequestContext requestContext) {
        this.bookService = bookService;
        this.chapterService = chapterService;
        this.requestContext = requestContext;
    }

    @SuppressWarnings("Used by graphql reflection")
    @PreAuthorize("isAuthenticated()")
    public BookType createBook(@Valid CreateBookInput input) {
        Book book = bookService.createBook(
            input.getTitle(),
            input.getDescription(),
            input.getCoverPhoto(),
            (String)requestContext.get(RequestContext.USER_ID),
            (WritingSpace)requestContext.get(RequestContext.WRITING_SPACE)
        );
        return BookType.toType(book);
    }

    @SuppressWarnings("Used by graphql reflection")
    @PreAuthorize("isAuthenticated()")
    public BookType updateBook(@Valid UpdateBookInput input) {
        Book book = bookService.getEditableBook(input.getBookId());
        book = bookService.updateBook(book, input.getTitle(), input.getDescription(), input.getCoverPhoto(), input.getStatus());
        return BookType.toType(book);
    }

    @SuppressWarnings("Used by graphql reflection")
    @PreAuthorize("isAuthenticated()")
    public ChapterType createChapter(@Valid CreateChapterInput input) {
        Book book = bookService.getEditableBook(input.getBookId());
        Chapter chapter = chapterService.createChapter(input.getBookId(), input.getTitle(), input.getContent(), input.getAuthorNotes(), input.isDraft());
        bookService.addChapter(book, chapter);
        return ChapterType.toType(chapter);
    }

    @SuppressWarnings("Used by graphql reflection")
    @PreAuthorize("isAuthenticated()")
    public ChapterType updateChapter(@Valid UpdateChapterInput input) {
        Book book = bookService.getEditableBook(input.getBookId());
        Chapter chapter = chapterService.findChapterById(input.getChapterId());
        if (!chapterService.doesChapterBelongToBook(chapter, book)) {
            throw new ResourceNotFoundException("Chapter of id " + input.getChapterId());
        }
        chapter = chapterService.updateChapter(chapter, input.getTitle(), input.getContent(), input.getAuthorNotes(), input.isDraft());
        return ChapterType.toType(chapter);
    }

    @SuppressWarnings("Used by graphql reflection")
    @PreAuthorize("isAuthenticated()")
    public ChapterType deleteChapter(String bookId, String chapterId) {
        Book book = bookService.getEditableBook(bookId);
        Chapter chapter = chapterService.findChapterById(chapterId);
        bookService.removeChapter(book, chapter);
        return ChapterType.toType(chapterService.removeChapter(chapter));
    }
}
