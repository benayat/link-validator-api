package com.benaya.link_validator_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.MalformedURLException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MalformedURLException.class)
    public ProblemDetail handleIllegalArgumentException(BadUrlException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }
}
