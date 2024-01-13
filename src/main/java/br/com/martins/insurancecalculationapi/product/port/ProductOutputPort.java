package br.com.martins.insurancecalculationapi.product.port;

import br.com.martins.insurancecalculationapi.product.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductOutputPort {


    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAll();

}
