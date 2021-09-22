package edu.miu.cs.appointmentsystem.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import edu.miu.cs.appointmentsystem.controllers.responses.AppResponse;

@ControllerAdvice
public class SystemExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleWebStoreExceptions(Exception ex, WebRequest request) {
        AppResponse<Object> response = new AppResponse<>(false, null, Arrays.asList(ex.getMessage()));
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof SystemNotFoundException)
            status = HttpStatus.NOT_FOUND;
        else if (ex instanceof SystemAlreadyExistException)
            status = HttpStatus.BAD_REQUEST;
        else if (ex instanceof ClientDataIncorrectException || ex instanceof OptimisticLockException)
            status = HttpStatus.BAD_REQUEST;
        else if (ex instanceof AccessDeniedException || ex instanceof NotAuthorizedException)
            status = HttpStatus.UNAUTHORIZED;
        else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setErrors(Arrays.asList("Unknown exception occurred on the server"));
        }
        ex.printStackTrace();
        return new ResponseEntity<>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> fieldError = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            fieldError.add(error.getField() + ":" + error.getDefaultMessage());
        }
        AppResponse<Object> bankResponse = new AppResponse<>(false, null, fieldError);
        return new ResponseEntity<>(bankResponse, HttpStatus.BAD_REQUEST);
    }
}