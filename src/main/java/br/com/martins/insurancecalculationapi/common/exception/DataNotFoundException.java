package br.com.martins.insurancecalculationapi.common.exception;

import lombok.Getter;

@Getter
public abstract class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String message) {
        super(message);
    }
}
