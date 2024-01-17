package br.com.martins.insurancecalculationapi.common.adapter.rest;

import br.com.martins.insurancecalculationapi.common.exception.DataConstraintViolationException;
import br.com.martins.insurancecalculationapi.common.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<CustomErrorResponse> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error(ex.getClass().getSimpleName(), ex);

        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        return new ResponseEntity<>(getCustomErrorResponse(ex,messages), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    ResponseEntity<CustomErrorResponse> exception(Exception ex) {
        log.error(ex.getClass().getSimpleName(), ex);
        return new ResponseEntity<>(getCustomErrorResponse(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static CustomErrorResponse getCustomErrorResponse(Exception ex) {
        return new CustomErrorResponse(
                ex.getClass().getSimpleName(),
                List.of(ex.getMessage()),
                LocalDateTime.now());
    }

    private static CustomErrorResponse getCustomErrorResponse(Exception ex, List<String> messages) {
        return new CustomErrorResponse(
                ex.getClass().getSimpleName(),
                messages,
                LocalDateTime.now());
    }

}
