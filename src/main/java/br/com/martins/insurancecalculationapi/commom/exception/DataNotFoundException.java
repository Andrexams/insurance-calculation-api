package br.com.martins.insurancecalculationapi.commom.exception;

import lombok.Getter;

@Getter
public abstract class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String message) {
        super(message);
    }
}
