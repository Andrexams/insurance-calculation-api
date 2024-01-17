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
       return Product.builder()
                .id(productDto.getId())
                .name(productDto.getNome())
                .category(ProductCategoryEnum.valueOf(productDto.getCategoria()))
                .basePrice(productDto.getPreco_base())
                .build();
    }

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .nome(product.getName())
                .categoria(product.getCategory().name())
                .preco_base(product.getBasePrice())
                .preco_tarifado(product.getTaxedPrice())
                .build();
    }

    public List<ProductDto> toDtoList(List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : productList) {
            productDtoList.add(toDto(product));
        }
        return productDtoList;
    }
}
