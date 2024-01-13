package br.com.martins.insurancecalculationapi.product_calculation.port;

import br.com.martins.insurancecalculationapi.product.entity.Product;

public interface ProductCalculationInputPort {


    Double calculateTaxedPrice(Product product);
}
