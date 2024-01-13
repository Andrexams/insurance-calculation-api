package br.com.martins.insurancecalculationapi.product_taxes.enumeration;

import lombok.Getter;

@Getter
public enum ProductTaxesEnum {


    VIDA(1.0,2.2, 0.0),
    AUTO(5.5,4.0,1.0),
    VIAGEM(2.0,2.0,1.0),
    RESIDENCIAL(4.0, 0.0, 3.0),
    PATRIMONIAL(5.0, 3.0, 0.0);

    private final Double iofTax;
    private final Double pisTax;
    private final Double cofinsTax;

    ProductTaxesEnum(Double iofTax, Double pisTax, Double cofinsTax) {
        this.iofTax = iofTax;
        this.pisTax = pisTax;
        this.cofinsTax = cofinsTax;
    }

}
