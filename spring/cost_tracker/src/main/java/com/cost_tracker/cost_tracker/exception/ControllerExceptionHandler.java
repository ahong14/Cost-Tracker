package com.cost_tracker.cost_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage defaultException(IllegalArgumentException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage noSuchElementException(NoSuchElementException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }
}
