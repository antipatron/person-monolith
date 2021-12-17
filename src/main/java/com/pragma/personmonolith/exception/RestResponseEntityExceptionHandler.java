package com.pragma.personmonolith.exception;

import com.google.common.base.Joiner;
import com.pragma.personmonolith.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

    @ExceptionHandler(DataNotFoundException.class)
    public final ResponseEntity<StandardResponse> handleDataNotFound(HttpServletRequest request, DataNotFoundException ex){
        logger.error(request.getRequestURL().toString(), ex);
        return new ResponseEntity<>(new StandardResponse(
                StandardResponse.StatusStandardResponse.ERROR,
                ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageNotComeBodyException.class)
    public final ResponseEntity<StandardResponse> handleImageNotComeBody(HttpServletRequest request, ImageNotComeBodyException ex){
        logger.error(request.getRequestURL().toString(), ex);
        return new ResponseEntity<>(new StandardResponse(
                StandardResponse.StatusStandardResponse.ERROR,
                ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataConstraintViolationException.class)
    public final ResponseEntity<StandardResponse> handleDataIntegrityViolation(HttpServletRequest request, DataConstraintViolationException ex){
        logger.error(request.getRequestURL().toString(), ex);
        return new ResponseEntity<>(new StandardResponse(
                StandardResponse.StatusStandardResponse.ERROR,
                ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        String erorresMapeadosString = Joiner.on(",").withKeyValueSeparator("=").join(errors);

        return new ResponseEntity<Object>(
                new StandardResponse(StandardResponse.StatusStandardResponse.ERROR, erorresMapeadosString),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ResponseEntity<>(
                new StandardResponse(StandardResponse.StatusStandardResponse.ERROR, ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public final ResponseEntity<StandardResponse> handleConstraintViolationException(Exception ex) {
        logger.error(ex.toString());

        return new ResponseEntity<>(
                new StandardResponse(StandardResponse.StatusStandardResponse.ERROR, ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<StandardResponse> handleDataIntegrityViolation(HttpServletRequest request, DataIntegrityViolationException ex){

        logger.error(request.getRequestURL().toString(), ex);
        return new ResponseEntity<>(new StandardResponse(
                StandardResponse.StatusStandardResponse.ERROR,
                ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNoEncontradoException.class)
    public final ResponseEntity<StandardResponse> handleObjectNoEncontrado(HttpServletRequest request, ObjectNoEncontradoException ex){
        logger.error(request.getRequestURL().toString(), ex);
        return new ResponseEntity<>(new StandardResponse(
                StandardResponse.StatusStandardResponse.ERROR,
                ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

}
