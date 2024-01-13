package br.com.martins.insurancecalculationapi.product_taxes.port;

import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import br.com.martins.insurancecalculationapi.product_taxes.enumeration.ProductTaxesEnum;

public interface ProductTaxesOutputPort {


    ProductTaxesEnum findByCategory(ProductCategoryEnum productCategoryEnum);
}
