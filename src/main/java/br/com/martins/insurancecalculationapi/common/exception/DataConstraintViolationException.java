package br.com.martins.insurancecalculationapi.common.exception;

import lombok.Getter;

@Getter
public class DataConstraintViolationException extends RuntimeException{
    private final String constraintName;

    public DataConstraintViolationException(String message, Throwable cause, String constraintName) {
        super(message, cause);
        this.constraintName = constraintName;
    }
}
