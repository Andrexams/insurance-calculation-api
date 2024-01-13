package br.com.martins.insurancecalculationapi.product.converter;

import br.com.martins.insurancecalculationapi.product.adapter.dto.ProductDto;
import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ProductConverterTest {

    @InjectMocks
    ProductConverter productConverter;

    @Test
    void should_convertToEntityAndToDto() {
        //prepare
        ProductDto productDto = ProductDto.builder()
                .id(UUID.randomUUID())
                .basePrice(100.0)
                .taxedPrice(103.25)
                .category("VIDA")
                .name("Seguro de Vida")
                .build();

        //call
        Product entity = productConverter.toEntity(productDto);

        //assert
        assertProduct(productDto, entity);

        //call come back to dto
        ProductDto dto = productConverter.toDto(entity);

        //assert
        assertProduct(dto, entity);
    }

    private static void assertProduct(ProductDto productDto, Product entity) {
        assertEquals(productDto.getId(), entity.getId());
        assertEquals(productDto.getName(), entity.getName());
        assertEquals(productDto.getCategory(), entity.getCategory().name());
        assertEquals(productDto.getBasePrice(), entity.getBasePrice());
        assertEquals(productDto.getTaxedPrice(), entity.getTaxedPrice());
    }

    @Test
    void should_convertToDtoList_when_notEmpty() {
        //prepare
        List<Product> productList = List.of(Product.builder()
                        .id(UUID.randomUUID())
                        .basePrice(100.0)
                        .taxedPrice(103.25)
                        .category(ProductCategoryEnum.VIAGEM)
                        .name("Seguro de Vida")
                        .build(),
                Product.builder()
                        .id(UUID.randomUUID())
                        .basePrice(1000.00)
                        .taxedPrice(1230.00)
                        .category(ProductCategoryEnum.AUTO)
                        .name("Seguro auto")
                        .build()
        );

        //call
        List<ProductDto> dtoList = productConverter.toDtoList(productList);

        //assert
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());

        for (int i = 0; i < dtoList.size(); i++) {
            Product product = productList.get(i);
            ProductDto productDto = dtoList.get(i);
            assertProduct(productDto, product);
        }
    }

    @Test
    void should_convertToDtoList_when_Empty() {
        //prepare
        List<Product> productList = List.of();

        //call
        List<ProductDto> dtoList = productConverter.toDtoList(productList);

        //assert
        assertNotNull(dtoList);
        assertEquals(0, dtoList.size());
    }

}
