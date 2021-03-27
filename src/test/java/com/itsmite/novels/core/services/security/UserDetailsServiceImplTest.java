package com.itsmite.novels.core.services.security;

import com.itsmite.novels.core.boot.NAssert;
import com.itsmite.novels.core.boot.SpringRunnerWithDataProvider;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.security.Role;
import com.itsmite.novels.core.models.user.ReadingSpace;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.repositories.user.UserRepository;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyString;


@RunWith(SpringRunnerWithDataProvider.class)
public class UserDetailsServiceImplTest {

    @TestConfiguration
    static class UserDetailsServiceImplTestConfiguration {
        @Bean
        public UserDetailsServiceImpl userDetailsServiceImpl() {
            return new UserDetailsServiceImpl();
        }
    }

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void loadUserByUsernameTestSuccess() {
        User user = getUser();
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(user.getUsername());
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
    public void loadUserByUsernameTestFailure() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        userDetailsService.loadUserByUsername("test");
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
