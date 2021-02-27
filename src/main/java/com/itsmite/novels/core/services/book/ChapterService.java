package com.itsmite.novels.core.services.book;

import com.itsmite.novels.core.errors.exceptions.ResourceNotFoundException;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.book.Chapter;
import com.itsmite.novels.core.repositories.book.ChapterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class ChapterService {

    private ChapterRepository chapterRepository;

    @Autowired
    public void autowireBeans(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public Chapter createChapter(String bookId, String title, String content, String authorNotes, boolean draft) {
        Chapter chapter = new Chapter();
        chapter.setBookId(bookId);
        chapter.setTitle(title);
        chapter.setContent(content);
        chapter.setAuthorNotes(authorNotes);
        chapter.setDraft(draft);
        return chapterRepository.save(chapter);
    }

    public Chapter updateChapter(Chapter chapter, String title, String content, String authorNotes, boolean draft) {
        chapter.setTitle(title);
        chapter.setContent(content);
        chapter.setAuthorNotes(authorNotes);
        chapter.setDraft(draft);
        return chapterRepository.save(chapter);
    }

    public Chapter findChapterById(String chapterId) {
        return chapterRepository.findChapterByIdAndDeletedAt(chapterId, null)
                                .orElseThrow(() -> new ResourceNotFoundException("Chapter of id " + chapterId));
    }

    public Chapter removeChapter(Chapter chapter) {
        chapter.setDeletedAt(LocalDateTime.now());
        return chapterRepository.save(chapter);
    }

    public boolean doesChapterBelongToBook(Chapter chapter, Book book) {
        if (chapter.getDeletedAt() != null) {
            return false;
        }
        return Objects.equals(chapter.getBookId(), book.getId());
    }
}
