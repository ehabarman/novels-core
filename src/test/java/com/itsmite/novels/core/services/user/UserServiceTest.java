package com.itsmite.novels.core.services.user;

import com.itsmite.novels.core.boot.NAssert;
import com.itsmite.novels.core.boot.SpringRunnerWithDataProvider;
import com.itsmite.novels.core.errors.exceptions.AlreadyUsedException;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.security.Role;
import com.itsmite.novels.core.models.user.ReadingSpace;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.repositories.security.RoleRepository;
import com.itsmite.novels.core.repositories.user.ReadingSpaceRepository;
import com.itsmite.novels.core.repositories.user.UserRepository;
import com.itsmite.novels.core.repositories.user.WritingSpaceRepository;
import com.itsmite.novels.core.services.security.UserDetailsImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunnerWithDataProvider.class)
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceTestConfiguration {
        @Bean
        public UserService userServiceService() {
            return new UserService();
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserSpaceService userSpaceService() {
            return new UserSpaceService();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private WritingSpaceRepository writingSpaceRepository;

    @MockBean
    private ReadingSpaceRepository readingSpaceRepository;

    @Test
    public void loadUserByUsernameTest() {
        User user = getUser();
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserDetailsImpl userDetails = (UserDetailsImpl)userService.loadUserByUsername(user.getUsername());
        NAssert.assertEquals(user.getId(), userDetails.getId());
        NAssert.assertEquals(user.getUsername(), userDetails.getUsername());
        NAssert.assertEquals(user.getEmail(), userDetails.getEmail());
        NAssert.assertEquals(user.getPassword(), userDetails.getPassword());
        NAssert.assertEquals(user.getReadingSpace(), userDetails.getReadingSpace());
        NAssert.assertEquals(user.getWritingSpace(), userDetails.getWritingSpace());
        List<ERole> eRoles = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        NAssert.assertNonSortedCollectionEquals(eRoles, userDetails.getRoles());
        NAssert.assertNonSortedCollectionEquals((Collection)buildAuthorities(eRoles), userDetails.getAuthorities());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameTestError() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        userService.loadUserByUsername("test");
    }

    @Test(expected = AlreadyUsedException.class)
    public void createUserTestAlreadyUsedEmail() {
        Mockito.when(userRepository.existsByEmail(any())).thenReturn(true);
        userService.createUser("", "", "");
    }

    @Test(expected = AlreadyUsedException.class)
    public void createUserTestAlreadyUsername() {
        Mockito.when(userRepository.existsByEmail(any())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(any())).thenReturn(true);
        userService.createUser("", "", "");
    }

    @Test(expected = InternalServerErrorException.class)
    public void createUserTestInternalServerErrorException() {
        User user = getUser();
        Mockito.when(userRepository.existsByEmail(any())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(any())).thenReturn(false);
        Mockito.when(readingSpaceRepository.save(any())).thenReturn(new ReadingSpace());
        Mockito.when(writingSpaceRepository.save(any())).thenReturn(new WritingSpace());
        Mockito.when(roleRepository.findByRole(ERole.BASIC)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(user);
        User actual = userService.createUser("", "", "");
        NAssert.assertEquals(user, actual);
    }

    @Test
    public void createUserTest() {
        Mockito.when(userRepository.existsByEmail(any())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(any())).thenReturn(false);
        Mockito.when(readingSpaceRepository.save(any())).thenReturn(new ReadingSpace());
        Mockito.when(writingSpaceRepository.save(any())).thenReturn(new WritingSpace());
        Mockito.when(roleRepository.findByRole(ERole.BASIC)).thenReturn(Optional.of(new Role(ERole.BASIC)));
        userService.createUser("", "", "");
    }

    @Test
    public void findByIdTest() {
        User user = getUser();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        User actual = userRepository.findById("").orElse(null);
        NAssert.assertEquals(user, actual);
    }

    private User getUser() {
        User user = new User();
        user.setId("111111");
        user.setEmail("test@email.com");
        user.setUsername("test");
        user.setPassword("test");
        user.setReadingSpace(new ReadingSpace());
        user.setWritingSpace(new WritingSpace());
        user.setRoles(Set.of(new Role(ERole.ADMIN), new Role(ERole.BASIC)));
        return user;
    }

    private List<GrantedAuthority> buildAuthorities(List<ERole> roles) {
        return roles.stream()
                    .map(ERole::name)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }
}
