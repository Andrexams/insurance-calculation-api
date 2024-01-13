package br.com.martins.insurancecalculationapi.product.exception;

import br.com.martins.insurancecalculationapi.commom.exception.DataConstraintViolationException;

public class ProductNameExistsException extends DataConstraintViolationException {
    public ProductNameExistsException(String message, Throwable cause, String constraintName) {
        super(message, cause, constraintName);
    }
}
