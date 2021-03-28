package com.itsmite.novels.core.services.book;

import com.itsmite.novels.core.boot.NAssert;
import com.itsmite.novels.core.boot.SpringRunnerWithDataProvider;
import com.itsmite.novels.core.models.book.Chapter;
import com.itsmite.novels.core.repositories.book.ChapterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import static org.mockito.ArgumentMatchers.any;

// TODO: Add unit tests
@RunWith(SpringRunnerWithDataProvider.class)
public class ChapterServiceTest {

    @TestConfiguration
    static class ChapterServiceTestConfiguration {
        @Bean
        public ChapterService chapterService() {
            return new ChapterService();
        }
    }

    @Autowired
    private ChapterService chapterService;

    @MockBean
    private ChapterRepository chapterRepository;

    @Test
    public void createChapterTest() {
        Chapter chapter = getChapter();
        Mockito.when(chapterRepository.save(any())).thenReturn(chapter);
        Chapter actual = chapterService.createChapter(chapter.getBookId(),
                                                      chapter.getTitle(),
                                                      chapter.getContent(),
                                                      chapter.getAuthorNotes(),
                                                      chapter.isDraft());
        NAssert.assertEquals(chapter, actual);
    }



    private Chapter getChapter() {
        Chapter chapter = new Chapter();
        chapter.setBookId("testBookId");
        chapter.setTitle("testTitle");
        chapter.setContent("testContent");
        chapter.setAuthorNotes("testAuthorNotes");
        chapter.setDraft(true);
        return chapter;
    }
}
