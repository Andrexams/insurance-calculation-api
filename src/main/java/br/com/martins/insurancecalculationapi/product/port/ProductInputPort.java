package br.com.martins.insurancecalculationapi.product.port;

import br.com.martins.insurancecalculationapi.product.entity.Product;

import java.util.List;

public interface ProductInputPort {


    Product insert(Product product);

    Product update(Product product);

    List<Product> findAll();
}
