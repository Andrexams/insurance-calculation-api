package br.com.martins.insurancecalculationapi.product_calculation.service.components;

import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product_taxes.enumeration.ProductTaxesEnum;

public abstract class ProductCalculationStrategy {


    public Double getTaxedPrice(Product product, ProductTaxesEnum productTaxesEnum) {
        Double basePrice = product.getBasePrice();
        Double iofTax = productTaxesEnum.getIofTax() / 100;
        Double pisTax = productTaxesEnum.getPisTax() / 100;
        Double cofinsTax = productTaxesEnum.getCofinsTax() / 100;

        return basePrice + (basePrice * iofTax) + (basePrice * pisTax) + (basePrice * cofinsTax);
    }

}
