package com.itsmite.novels.core.graphql.resolvers.auth.mutations;

import com.itsmite.novels.core.errors.exceptions.InvalidCredentialsException;
import com.itsmite.novels.core.graphql.resolvers.auth.inputs.RegisterInput;
import com.itsmite.novels.core.graphql.resolvers.auth.types.JwtTokenType;
import com.itsmite.novels.core.graphql.resolvers.user.types.UserType;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.services.security.UserDetailsImpl;
import com.itsmite.novels.core.services.security.UserDetailsServiceImpl;
import com.itsmite.novels.core.services.user.UserService;
import com.itsmite.novels.core.util.JwtUtils;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Component
@Validated
public class AuthMutation implements GraphQLMutationResolver {

    @Value("${security.jwt.expirationMs}")
    private int jwtExpirationMs;

    private UserService            userService;
    private AuthenticationManager  authenticationManager;
    private JwtUtils               jwtUtils;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void autowireBeans(UserService userService,
                              AuthenticationManager authenticationManager,
                              JwtUtils jwtUtils,
                              UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @SuppressWarnings("Used by graphql reflection")
    @PreAuthorize("isAnonymous()")
    public JwtTokenType login(@NotEmpty String username, @NotEmpty String password) {
        authenticate(username, password);
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        Date jwtExpirationDate = new Date((new Date()).getTime() + jwtExpirationMs);
        String jwt = jwtUtils.generateJwtToken(userDetails, jwtExpirationDate);
        return new JwtTokenType(jwt, jwtExpirationDate.toString());
    }

    @SuppressWarnings("Used by graphql reflection")
    @PreAuthorize("isAnonymous()")
    public UserType register(@Valid RegisterInput registerInput) {
        registerInput.setEmail(registerInput.getEmail().toLowerCase());
        User user = userService.createUser(registerInput.getEmail(), registerInput.getUsername(), registerInput.getPassword());
        return UserType.toType(user);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}
