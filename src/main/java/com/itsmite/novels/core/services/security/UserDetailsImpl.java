package com.itsmite.novels.core.services.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.security.Role;
import com.itsmite.novels.core.models.user.ReadingSpace;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.models.user.WritingSpace;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private ReadingSpace readingSpace;

    private WritingSpace writingSpace;

    private Set<ERole> roles;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String id, String username, String email, String password, ReadingSpace readingSpace, WritingSpace writingSpace,
                           Set<ERole> roles, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.readingSpace = readingSpace;
        this.writingSpace = writingSpace;
        this.roles = roles;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                                                 .map(role -> role.getRole().name())
                                                 .map(SimpleGrantedAuthority::new)
                                                 .collect(Collectors.toList());
        return new UserDetailsImpl(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getReadingSpace(),
            user.getWritingSpace(),
            user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()),
            authorities);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl)o;
        return Objects.equals(id, user.id);
    }
}
