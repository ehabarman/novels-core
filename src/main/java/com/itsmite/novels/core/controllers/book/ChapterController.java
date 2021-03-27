package com.itsmite.novels.core.controllers.book;

import com.itsmite.novels.core.annotations.JsonRequestMapping;
import com.itsmite.novels.core.errors.exceptions.ResourceNotFoundException;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.book.Chapter;
import com.itsmite.novels.core.payload.book.request.CreateChapterRequest;
import com.itsmite.novels.core.payload.book.request.UpdateChapterRequest;
import com.itsmite.novels.core.payload.book.response.ChapterResponse;
import com.itsmite.novels.core.services.book.BookService;
import com.itsmite.novels.core.services.book.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.itsmite.novels.core.constants.EndpointConstants.API_CHAPTERS_ENDPOINT;
import static com.itsmite.novels.core.constants.EndpointConstants.BOOK_ID_PARAM;
import static com.itsmite.novels.core.constants.EndpointConstants.CHAPTER_ID_PARAM;
import static com.itsmite.novels.core.constants.EndpointConstants.CHAPTER_ID_PATH;

@Slf4j
@Validated
@RestController
@RequestMapping(value = API_CHAPTERS_ENDPOINT)
public class ChapterController {

    private BookService    bookService;
    private ChapterService chapterService;

    @Autowired
    public void autowireBeans(BookService bookService, ChapterService chapterService) {
        this.bookService = bookService;
        this.chapterService = chapterService;
    }

    @JsonRequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ChapterResponse createChapter(@PathVariable(BOOK_ID_PARAM) String bookId, @Valid @RequestBody CreateChapterRequest request) {
        Book book = bookService.getEditableBook(bookId);
        Chapter chapter = chapterService.createChapter(bookId, request.getTitle(), request.getContent(), request.getAuthorNotes(), request.isDraft());
        bookService.addChapter(book, chapter);
        return ChapterResponse.toResponse(chapter);
    }

    @JsonRequestMapping(path = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ChapterResponse> getBookChapters(@PathVariable(BOOK_ID_PARAM) String bookId) {
        Book book = bookService.findBookByIdOrElseThrowException(bookId);
        return book.getChapters().stream().map(ChapterResponse::toResponse).collect(Collectors.toList());
    }

    @JsonRequestMapping(path = CHAPTER_ID_PATH, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ChapterResponse getChapter(@PathVariable(BOOK_ID_PARAM) String bookId, @PathVariable(CHAPTER_ID_PARAM) String chapterId) {
        Book book = bookService.findBookByIdOrElseThrowException(bookId);
        return book.getChapters().stream()
                   .filter(chapter -> Objects.equals(chapter.getId(), chapterId))
                   .findFirst()
                   .map(ChapterResponse::toResponse)
                   .orElseThrow(() -> new ResourceNotFoundException("Chapter of id " + chapterId));
    }

    @JsonRequestMapping(path = CHAPTER_ID_PATH, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public ChapterResponse deleteChapter(@PathVariable(BOOK_ID_PARAM) String bookId, @PathVariable(CHAPTER_ID_PARAM) String chapterId) {
        Book book = bookService.getEditableBook(bookId);
        Chapter chapter = chapterService.findChapterById(chapterId);
        bookService.removeChapter(book, chapter);
        return ChapterResponse.toResponse(chapterService.removeChapter(chapter));
    }

    @JsonRequestMapping(path = CHAPTER_ID_PATH, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ChapterResponse updateChapter(@PathVariable(BOOK_ID_PARAM) String bookId, @PathVariable(CHAPTER_ID_PARAM) String chapterId,
                                         @Valid @RequestBody UpdateChapterRequest request) {
        Book book = bookService.getEditableBook(bookId);
        Chapter chapter = chapterService.findChapterById(chapterId);
        if (chapterService.chapterDoesntBelongToBook(chapter, book)) {
            throw new ResourceNotFoundException("Chapter of id " + chapterId);
        }
        chapter = chapterService.updateChapter(chapter, request.getTitle(), request.getContent(), request.getAuthorNotes(), request.isDraft());
        return ChapterResponse.toResponse(chapter);
    }
}
