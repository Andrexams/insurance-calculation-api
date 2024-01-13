package br.com.martins.insurancecalculationapi.product_taxes.adapter.persistence;

import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import br.com.martins.insurancecalculationapi.product_taxes.enumeration.ProductTaxesEnum;
import br.com.martins.insurancecalculationapi.product_taxes.port.ProductTaxesOutputPort;
import org.springframework.stereotype.Component;

@Component
public class ProductTaxesPersistenceAdapter implements ProductTaxesOutputPort {
    @Override
    public ProductTaxesEnum findByCategory(ProductCategoryEnum productCategoryEnum) {
        return ProductTaxesEnum.valueOf(productCategoryEnum.name());
    }
}
