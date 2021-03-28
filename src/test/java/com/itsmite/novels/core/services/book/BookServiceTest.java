package com.itsmite.novels.core.services.book;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.boot.SpringRunnerWithDataProvider;
import com.itsmite.novels.core.repositories.book.BookRepository;
import com.itsmite.novels.core.repositories.user.WritingSpaceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

// TODO: add unit tests
@RunWith(SpringRunnerWithDataProvider.class)
public class BookServiceTest {

    @TestConfiguration
    static class BookServiceTestConfiguration {
        @Bean
        public BookService bookService() {
            return new BookService();
        }

        @Bean
        public RequestContext requestContext() {
            return new RequestContext();
        }
    }

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private WritingSpaceRepository writingSpaceRepository;

    @Test
    public void findAllTest() {

    }
}
