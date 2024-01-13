package br.com.martins.insurancecalculationapi.product.adapter.controller;

import br.com.martins.insurancecalculationapi.product.converter.ProductConverter;
import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product.adapter.dto.ProductDto;
import br.com.martins.insurancecalculationapi.product.port.ProductInputPort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductRestController {


    private final ProductInputPort productInputPort;

    private final ProductConverter productConverter;

    public ProductRestController(ProductInputPort productInputPort,
                                 ProductConverter productConverter) {
        this.productInputPort = productInputPort;
        this.productConverter = productConverter;
    }

    @PostMapping("/products")
    public ProductDto insert(@RequestBody ProductDto productDto){
        Product entity = productConverter.toEntity(productDto);
        return productConverter.toDto(productInputPort.insert(entity));
    }

    @PutMapping("/products")
    public ProductDto update(@RequestBody ProductDto productDto){
        Product entity = productConverter.toEntity(productDto);
        return productConverter.toDto(productInputPort.update(entity));
    }

    @GetMapping("/products")
    public List<ProductDto> all() {
        return productConverter.toDtoList(productInputPort.findAll());
    }

}
