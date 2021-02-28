package com.itsmite.novels.core.services.user;

import com.itsmite.novels.core.errors.exceptions.AlreadyUsedException;
import com.itsmite.novels.core.errors.exceptions.ResourceNotFoundException;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.security.Role;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.repositories.security.RoleRepository;
import com.itsmite.novels.core.repositories.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.InternalServerErrorException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FOUND_TEMPLATE = "Username %S not found";

    private UserRepository        userRepository;
    private RoleRepository        roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserSpaceService      userSpaceService;

    @Autowired
    public void autowireBeans(UserRepository userRepository,
                              RoleRepository roleRepository,
                              @Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
                              UserSpaceService userSpaceService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userSpaceService = userSpaceService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.findByUsername(email);
        return user.map(User::getRoles)
                   .map(this::getUserAuthority)
                   .map(authorities -> buildUserForAuthentication(user.get(), authorities))
                   .orElseThrow(
                       () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, email))
                   );
    }

    public User createUser(String email, String username, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyUsedException("email", email);
        }
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyUsedException("username", username);
        }
        User user = createVerifiedEnabledUser(email, username, bCryptPasswordEncoder.encode(password));
        try {
            Role role = roleRepository.findByRole(ERole.BASIC)
                                      .orElseThrow(InternalServerErrorException::new);
            user.setRoles(Set.of(role));
            return userRepository.save(user);
        } catch (Exception ex) {
            log.error("Failed to create user: {}", ex.getMessage());
            throw new InternalServerErrorException("Something went wrong");
        }
    }

    private User createVerifiedEnabledUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);
        user.setVerified(true);
        user.setWritingSpace(userSpaceService.createWritingSpace());
        user.setReadingSpace(userSpaceService.createReadingSpace());
        return user;
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> useRoles) {
        return useRoles.stream()
                       .map(role -> role.getRole().name())
                       .map(SimpleGrantedAuthority::new)
                       .collect(Collectors.toList());
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public User findById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User of id " + userId));
    }
}
