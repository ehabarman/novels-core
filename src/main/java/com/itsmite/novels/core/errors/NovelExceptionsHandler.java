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
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.util.Set;

import static java.util.Optional.ofNullable;

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
        String message = ofNullable(exception.getMessage())
            .map(msg -> msg.replaceAll("[a-zA-Z0-9.]*\\.(?=[A-Z])", ""))
            .orElse("Request body is not a valid json");
        return buildResponse(message, HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> exception(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = ofNullable(fieldError)
            .map(error -> String.format("Field {name=%s: value=%s}: %s", error.getField(), error.getRejectedValue(), error.getDefaultMessage()))
            .orElse("Method arguments are not valid ");
        return buildResponse(message, HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    ResponseEntity<Object> exception(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        ApiError apiError = includeDebugMessage
                            ? new ApiError(HttpStatus.BAD_REQUEST, "Constraints are not met", exception)
                            : new ApiError(HttpStatus.BAD_REQUEST, "Constraints are not met", null);
        apiError.addValidationErrors(constraintViolations);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    ResponseEntity<Object> exception(MethodArgumentTypeMismatchException exception) {
        String fieldName = exception.getName();
        Object fieldValue = exception.getValue();
        String expectedType = ofNullable(exception.getRequiredType()).map(Class::getName).orElse("String");
        String message = String.format("Field {name=%s: value=%s}: expected field type '%s'", fieldName, fieldValue, expectedType);
        return buildResponse(message, HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception exception) {
        return buildResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<Object> exception(InvalidCredentialsException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(value = UnexpectedTypeException.class)
    public ResponseEntity<Object> exception(UnexpectedTypeException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, exception);
    }

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status, Exception e) {
        log.error("Error exception {}: {}", e.getClass().getName(), message);
        log.error("Error details: {}", e.getMessage());
        ApiError apiError = includeDebugMessage ? new ApiError(status, message, e) : new ApiError(status, message, null);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
