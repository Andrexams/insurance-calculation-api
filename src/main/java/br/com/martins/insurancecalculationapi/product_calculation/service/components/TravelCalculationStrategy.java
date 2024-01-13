package br.com.martins.insurancecalculationapi.product_calculation.service.components;

import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product_taxes.enumeration.ProductTaxesEnum;

/**
 * Exemplo caso necessite alterar a regra da fórmula para o seguro de viagem, basta criar uma estratégia
 * de calculo exclusiva e sobrescrever o método getTaxedPrice.
 */
public class TravelCalculationStrategy extends ProductCalculationStrategy {


    @Override
    public Double getTaxedPrice(Product product, ProductTaxesEnum productTaxesEnum) {
        return super.getTaxedPrice(product, productTaxesEnum);
    }
}
