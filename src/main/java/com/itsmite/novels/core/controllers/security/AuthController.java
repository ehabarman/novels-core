package com.itsmite.novels.core.controllers.security;

import com.itsmite.novels.core.annotations.JsonRequestMapping;
import com.itsmite.novels.core.errors.exceptions.InvalidCredentialsException;
import com.itsmite.novels.core.payload.security.request.LoginRequest;
import com.itsmite.novels.core.payload.security.request.SignupRequest;
import com.itsmite.novels.core.security.jwt.JwtUtils;
import com.itsmite.novels.core.services.security.UserDetailsServiceImpl;
import com.itsmite.novels.core.services.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.itsmite.novels.core.constants.EndpointConstants.API_AUTH_V1_ENDPOINT;

@Slf4j
@RestController
@RequestMapping(value = API_AUTH_V1_ENDPOINT)
public class AuthController {

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

    @JsonRequestMapping(path = "/signup", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest signupRequest) {
        userService.createUser(signupRequest.getEmail(), signupRequest.getUsername(), signupRequest.getPassword());
    }

    @JsonRequestMapping(path = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        Date jwtExpirationDate = new Date((new Date()).getTime() + jwtExpirationMs);
        String jwt = jwtUtils.generateJwtToken(userDetails, jwtExpirationDate);
        return new HashMap<>() {{
            put("token", jwt);
            put("expirationDate", jwtExpirationDate);
        }};
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}
