package com.itsmite.novels.core.errors;

import com.itsmite.novels.core.errors.api.ApiError;
import com.itsmite.novels.core.errors.exceptions.AlreadyUsedException;
import com.itsmite.novels.core.errors.exceptions.InvalidCredentialsException;
import com.itsmite.novels.core.errors.exceptions.ResourceNotFoundException;
import com.itsmite.novels.core.errors.exceptions.UnauthorizedResourceAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

/**
 * Handler for custom defined exceptions
 *
 * @author ehab
 */
@Slf4j
@ControllerAdvice
public class NovelExceptionsHandler {

    @Value("${api.error.include-debug-message}")
    private boolean includeDebugMessage;

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> exception(HttpRequestMethodNotSupportedException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, exception);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> exception(UsernameNotFoundException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(value = UnauthorizedResourceAction.class)
    public ResponseEntity<Object> exception(UnauthorizedResourceAction exception) {
        return buildResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> exception(IllegalArgumentException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(value = AlreadyUsedException.class)
    public ResponseEntity<Object> exception(AlreadyUsedException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> exception(ResourceNotFoundException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Object> exception(InternalServerErrorException exception) {
        return buildResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> exception(BadRequestException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Object> exception(HttpMessageNotReadableException exception) {
        return buildResponse("Please make sure you are using a valid request body", HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> exception(MethodArgumentNotValidException exception) {
        return buildResponse("Please make sure you are using a valid request body", HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception exception) {
        return buildResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<Object> exception(InvalidCredentialsException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED, exception);
    }

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status, Exception e) {
        log.error("Error exception {}: {}", e.getClass().getName(), message);
        log.error("Error details: {}", e.getMessage());
        ApiError apiError = includeDebugMessage ? new ApiError(status, message, e) : new ApiError(status, message, null);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
