package br.com.martins.insurancecalculationapi.product_calculation.service;

import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import br.com.martins.insurancecalculationapi.product_calculation.port.ProductCalculationInputPort;
import br.com.martins.insurancecalculationapi.product_calculation.service.components.TravelCalculationStrategy;
import br.com.martins.insurancecalculationapi.product_calculation.service.components.ProductCalculationStrategy;
import br.com.martins.insurancecalculationapi.product_taxes.enumeration.ProductTaxesEnum;
import br.com.martins.insurancecalculationapi.product_taxes.port.ProductTaxesOutputPort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductCalculationService implements ProductCalculationInputPort {


    private final ProductTaxesOutputPort productTaxesOutputPort;

    public ProductCalculationService(ProductTaxesOutputPort productTaxesOutputPort) {
        this.productTaxesOutputPort = productTaxesOutputPort;
    }

    public Double calculateTaxedPrice(Product product) {

        ProductTaxesEnum productTaxes = this.productTaxesOutputPort.findByCategory(product.getCategory());

        ProductCalculationStrategy productCalculationStrategy = getProductCalculationStrategy(product.getCategory());
        return productCalculationStrategy.getTaxedPrice(product, productTaxes);
    }

    ProductCalculationStrategy getProductCalculationStrategy(ProductCategoryEnum productCategoryEnum) {
        if (Objects.requireNonNull(productCategoryEnum) == ProductCategoryEnum.VIAGEM) {
            return new TravelCalculationStrategy();
        }
        return new ProductCalculationStrategy() {};
    }

}
