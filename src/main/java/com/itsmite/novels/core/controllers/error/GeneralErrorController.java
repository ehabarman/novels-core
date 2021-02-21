package com.itsmite.novels.core.controllers.error;

import com.itsmite.novels.core.errors.api.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Errors api controller
 *
 * @author ehab
 */
@Slf4j
@Controller
@RequestMapping("/error") // Special controller, changing path must be done here and in properties file ${server.error.path}
public class GeneralErrorController extends AbstractErrorController {

    public GeneralErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public ResponseEntity<Object> error(HttpServletRequest request) {
        String status = request.getAttribute("javax.servlet.error.status_code").toString();
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(status));
        String message = request.getAttribute("javax.servlet.error.message").toString();
        ApiError apiError = new ApiError(httpStatus, message);
        log.error("Unhandled exception occurred: {}", message);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
