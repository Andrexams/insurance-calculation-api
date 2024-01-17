package br.com.martins.insurancecalculationapi.product.exception;

import br.com.martins.insurancecalculationapi.common.exception.DataNotFoundException;

public class ProductNotFoundException extends DataNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
