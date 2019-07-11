package org.itrunner.heroes.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice(basePackages = {"org.itrunner.heroes.controller"})
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            DuplicateKeyException.class,
            DataIntegrityViolationException.class,
            DataAccessException.class,
            Exception.class
    })
    public ResponseEntity<Object> handleException(Exception e) {
        logger.error(e.getMessage(), e);

        if (e instanceof DuplicateKeyException) {
            return badRequest(getExceptionName(e), e.getMessage());
        }

        if (e instanceof DataIntegrityViolationException) {
            return badRequest(getExceptionName(e), e.getMessage());
        }

        if (e instanceof DataAccessException) {
            return badRequest(getExceptionName(e), e.getMessage());
        }

        return badRequest(getExceptionName(e), e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(getExceptionName(ex), "Validation Failed");
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(error -> errorMessage.addDetailMessage(error.getField() + " " + error.getDefaultMessage()));
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        globalErrors.forEach(error -> errorMessage.addDetailMessage(error.getDefaultMessage()));
        return badRequest(errorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(getExceptionName(ex), ex.getMessage()), headers, status);
    }

    private ResponseEntity<Object> badRequest(ErrorMessage errorMessage) {
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> badRequest(String type, String message) {
        return badRequest(new ErrorMessage(type, message));
    }

    private String getExceptionName(Exception e) {
        return e.getClass().getSimpleName();
    }
}