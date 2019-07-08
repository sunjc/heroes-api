package org.itrunner.heroes.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice(basePackages = {"org.itrunner.heroes.controller"})
@Slf4j
public class ErrorControllerAdvice {

    @ExceptionHandler({
            DuplicateKeyException.class,
            DataIntegrityViolationException.class,
            DataAccessException.class,
            MethodArgumentNotValidException.class,
            Exception.class
    })
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        log.error(e.getMessage(), e);

        if (e instanceof DuplicateKeyException) {
            return badRequest(getExceptionName(e), e.getMessage());
        }

        if (e instanceof DataIntegrityViolationException) {
            return badRequest(getExceptionName(e), e.getMessage());
        }

        if (e instanceof DataAccessException) {
            return badRequest(getExceptionName(e), e.getMessage());
        }

        if (e instanceof MethodArgumentNotValidException) {
            return handleMethodArgumentNotValid((MethodArgumentNotValidException) e);
        }

        return badRequest(getExceptionName(e), e.getMessage());
    }

    private ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ErrorMessage errorMessage = new ErrorMessage(getExceptionName(e), "Validation Failed");
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(error -> errorMessage.addDetailMessage(error.getField() + " " + error.getDefaultMessage()));
        List<ObjectError> globalErrors = e.getBindingResult().getGlobalErrors();
        globalErrors.forEach(error -> errorMessage.addDetailMessage(error.getDefaultMessage()));
        return badRequest(errorMessage);
    }

    private ResponseEntity<ErrorMessage> badRequest(ErrorMessage errorMessage) {
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorMessage> badRequest(String type, String message) {
        return badRequest(new ErrorMessage(type, message));
    }

    private String getExceptionName(Exception e) {
        return e.getClass().getSimpleName();
    }
}