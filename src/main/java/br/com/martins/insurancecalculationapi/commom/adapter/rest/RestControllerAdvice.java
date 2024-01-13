package br.com.martins.insurancecalculationapi.commom.adapter.rest;

import br.com.martins.insurancecalculationapi.commom.exception.DataConstraintViolationException;
import br.com.martins.insurancecalculationapi.commom.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@Slf4j
@ControllerAdvice
public class RestControllerAdvice {
    
    @ExceptionHandler(DataNotFoundException.class)
    ResponseEntity<CustomErrorResponse> entityNotFound(DataNotFoundException ex) {
        log.warn(ex.getClass().getSimpleName(), ex);
        return new ResponseEntity<>(getCustomErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataConstraintViolationException.class)
    ResponseEntity<CustomErrorResponse> constraintViolation(DataConstraintViolationException ex) {
        log.error(ex.getClass().getSimpleName(), ex);
        return new ResponseEntity<>(getCustomErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<CustomErrorResponse> exception(Exception ex) {
        log.error(ex.getClass().getSimpleName(), ex);
        return new ResponseEntity<>(getCustomErrorResponse(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static CustomErrorResponse getCustomErrorResponse(Exception ex) {
        return new CustomErrorResponse(
                ex.getClass().getSimpleName(), ex.getMessage(),
                LocalDateTime.now());
    }

}
