package br.com.martins.insurancecalculationapi.product.service;

import br.com.martins.insurancecalculationapi.common.exception.DataConstraintViolationException;
import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product.exception.ProductNameExistsException;
import br.com.martins.insurancecalculationapi.product.exception.ProductNotFoundException;
import br.com.martins.insurancecalculationapi.product.port.ProductInputPort;
import br.com.martins.insurancecalculationapi.product.port.ProductOutputPort;
import br.com.martins.insurancecalculationapi.product_calculation.port.ProductCalculationInputPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService implements ProductInputPort {


    private final ProductOutputPort productOutputPort;
    private final ProductCalculationInputPort productCalculationInputPort;

    public ProductService(ProductOutputPort productOutputPort,
                          ProductCalculationInputPort productCalculationInputPort) {
        this.productOutputPort = productOutputPort;
        this.productCalculationInputPort = productCalculationInputPort;
    }

    @Override
    public Product insert(Product product) {
        try {
            product.setTaxedPrice(productCalculationInputPort.calculateTaxedPrice(product));
            return productOutputPort.save(product);
        } catch (DataConstraintViolationException dataConstraintViolationException) {
            if (Objects.equals(dataConstraintViolationException.getConstraintName(), Product.UNIQUE_NAME_CONSTRAINT)) {
                throw new ProductNameExistsException(String.format("Já existe um produto com o nome %s.", product.getName()),
                        dataConstraintViolationException,Product.UNIQUE_NAME_CONSTRAINT);
            }
            throw dataConstraintViolationException;
        }
    }

    @Override
    public Product update(Product product) {
        return productOutputPort.findById(product.getId()).map(persistedProduct -> {

            persistedProduct.setName(product.getName());
            persistedProduct.setBasePrice(product.getBasePrice());
            persistedProduct.setCategory(product.getCategory());
            persistedProduct.setTaxedPrice(productCalculationInputPort.calculateTaxedPrice(product));

            return productOutputPort.save(persistedProduct);
        }).orElseThrow(() -> new ProductNotFoundException(String.format("Produto não localizado pelo id %s.",product.getId())));
    }

    @Override
    public List<Product> findAll() {
        return productOutputPort.findAll();
    }
}
