package org.itrunner.heroes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {"org.itrunner.heroes.controller"})
public class ErrorControllerAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler({
            DuplicateKeyException.class,
            DataIntegrityViolationException.class,
            DataAccessException.class,
            MethodArgumentNotValidException.class,
            Exception.class
    })
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        LOG.error(e.getMessage(), e);

        if (e instanceof DuplicateKeyException) {
            return handleMessage(getExceptionName(e), e.getMessage());
        }

        if (e instanceof DataIntegrityViolationException) {
            return handleMessage(getExceptionName(e), e.getMessage());
        }

        if (e instanceof DataAccessException) {
            return handleMessage(getExceptionName(e), e.getMessage());
        }

        if (e instanceof MethodArgumentNotValidException) {
            return handleMessage(getExceptionName(e), ((MethodArgumentNotValidException) e).getBindingResult().toString());
        }

        return handleMessage(getExceptionName(e), e.getMessage());
    }

    private ResponseEntity<ErrorMessage> handleMessage(String type, String message) {
        return ResponseEntity.badRequest().body(new ErrorMessage(type, message));
    }

    private String getExceptionName(Exception e) {
        return e.getClass().getSimpleName();
    }
}