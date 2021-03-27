package com.itsmite.novels.core.services.user;

import com.itsmite.novels.core.boot.NAssert;
import com.itsmite.novels.core.boot.SpringRunnerWithDataProvider;
import com.itsmite.novels.core.models.user.ReadingSpace;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.repositories.user.ReadingSpaceRepository;
import com.itsmite.novels.core.repositories.user.UserRepository;
import com.itsmite.novels.core.repositories.user.WritingSpaceRepository;
import com.itsmite.novels.core.services.security.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@RunWith(SpringRunnerWithDataProvider.class)
public class UserSpaceServiceTest {

    @TestConfiguration
    static class UserSpaceServiceTestConfiguration {
        @Bean
        public UserSpaceService userSpaceService() {
            return new UserSpaceService();
        }
    }

    @Autowired
    private UserSpaceService userSpaceService;

    @MockBean
    private WritingSpaceRepository writingSpaceRepository;

    @MockBean
    private ReadingSpaceRepository readingSpaceRepository;

    @Test
    public void createReadingSpaceTest() {
        Mockito.when(readingSpaceRepository.save(any())).thenReturn( new ReadingSpace());
        ReadingSpace readingSpace = userSpaceService.createReadingSpace();
        NAssert.assertNotNull(readingSpace);
        NAssert.isEmpty(readingSpace.getBooks());
    }

    @Test
    public void createWritingSpaceTest() {
        Mockito.when(writingSpaceRepository.save(any())).thenReturn( new WritingSpace());
        WritingSpace writingSpace = userSpaceService.createWritingSpace();
        NAssert.assertNotNull(writingSpace);
        NAssert.isEmpty(writingSpace.getBooks());
    }
}
