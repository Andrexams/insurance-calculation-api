package br.com.martins.insurancecalculationapi.product.converter;

import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import br.com.martins.insurancecalculationapi.product.adapter.dto.ProductDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductConverter {


    public Product toEntity(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        product.setCategory(ProductCategoryEnum.valueOf(productDto.getCategory()));
        return product;
    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        productDto.setCategory(product.getCategory().name());
        return productDto;
    }

    public List<ProductDto> toDtoList(List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : productList) {
            productDtoList.add(toDto(product));
        }
        return productDtoList;
    }
}
