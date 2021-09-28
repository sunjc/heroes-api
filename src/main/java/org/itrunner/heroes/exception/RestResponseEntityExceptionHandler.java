package org.itrunner.heroes.exception;

import org.itrunner.heroes.util.Messages;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.springframework.core.NestedExceptionUtils.getMostSpecificCause;

@ControllerAdvice(basePackages = {"org.itrunner.heroes.controller"})
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private final Messages messages;

    public RestResponseEntityExceptionHandler(Messages messages) {
        this.messages = messages;
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            BadCredentialsException.class,
            Exception.class
    })
    public final ResponseEntity<Object> handleAllException(Exception e) {
        logger.error(e.getMessage(), e);

        if (e instanceof EntityNotFoundException) {
            return notFound(getExceptionName(e), messages.getMessage("error.entityNotFound"));
        }

        if (e instanceof BadCredentialsException) {
            return badRequest(getExceptionName(e), messages.getMessage("error.badCredentials"));
        }

        return badRequest(getMostSpecificName(e), getMostSpecificMessage(e));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder messages = new StringBuilder();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        globalErrors.forEach(error -> messages.append(error.getDefaultMessage()).append(";"));
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(error -> messages.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(";"));
        return badRequest(getExceptionName(ex), messages.toString());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(getExceptionName(ex), ex.getMessage()), headers, status);
    }

    private ResponseEntity<Object> badRequest(String error, String message) {
        return new ResponseEntity<>(new ErrorMessage(error, message), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> notFound(String error, String message) {
        return new ResponseEntity(new ErrorMessage(error, message), HttpStatus.NOT_FOUND);
    }

    private String getExceptionName(Exception e) {
        return e.getClass().getSimpleName();
    }

    private String getMostSpecificName(Exception e) {
        return getMostSpecificCause(e).getClass().getSimpleName();
    }

    private String getMostSpecificMessage(Exception e) {
        return getMostSpecificCause(e).getMessage();
    }
}