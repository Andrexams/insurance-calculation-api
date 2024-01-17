package br.com.martins.insurancecalculationapi.product.service;

import br.com.martins.insurancecalculationapi.common.exception.DataConstraintViolationException;
import br.com.martins.insurancecalculationapi.product.adapter.persitence.ProductRepository;
import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import br.com.martins.insurancecalculationapi.product.exception.ProductNameExistsException;
import br.com.martins.insurancecalculationapi.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void deleteProducts() {
        productRepository.deleteAll();
    }

    @Test
    void should_InsertAndUpdateProductWithTaxedPrice() {
        //prepare
        Product product = Product.builder()
                .basePrice(100.0)
                .category(ProductCategoryEnum.VIDA)
                .name("Seguro de Vida")
                .build();

        //call create product
        Product persistedProduct = productService.insert(product);

        //assert
        assertNotNull(persistedProduct.getId());
        assertEquals(product.getCategory(), persistedProduct.getCategory());
        assertEquals(product.getName(), persistedProduct.getName());
        assertEquals(product.getBasePrice(), persistedProduct.getBasePrice());
        assertEquals(103.2, persistedProduct.getTaxedPrice());

        //do update
        should_UpdateProductWithTaxedPrice(persistedProduct.getId());

    }

    private void should_UpdateProductWithTaxedPrice(UUID productId) {
        //prepare
        Product product = Product.builder()
                .id(productId)
                .basePrice(200.0)
                .category(ProductCategoryEnum.VIDA)
                .name("Seguro de Vida Especial")
                .build();

        //call create product
        Product persistedProduct = productService.update(product);

        //assert
        assertNotNull(persistedProduct.getId());
        assertEquals(product.getCategory(), persistedProduct.getCategory());
        assertEquals(product.getName(), persistedProduct.getName());
        assertEquals(product.getBasePrice(), persistedProduct.getBasePrice());
        assertEquals(206.4, persistedProduct.getTaxedPrice());
    }

    @Test
    void should_ThrowProductNameExistsException_whenTryInsertSameProductName() {
        //prepare
        Product product = Product.builder()
                .basePrice(100.0)
                .category(ProductCategoryEnum.RESIDENCIAL)
                .name("Seguro Residencial")
                .build();

        Product product2 = Product.builder()
                .basePrice(100.0)
                .category(ProductCategoryEnum.RESIDENCIAL)
                .name("Seguro Residencial")
                .build();

        //call and assert
        assertThrows(ProductNameExistsException.class, () -> {
            productService.insert(product);
            productService.insert(product2);
        });
    }

    @Test
    void should_FindAllProducts() {
        //prepare
        Product product = Product.builder()
                .basePrice(100.0)
                .category(ProductCategoryEnum.RESIDENCIAL)
                .name("Seguro Residencial")
                .build();
        productService.insert(product);

        Product product2 = Product.builder()
                .basePrice(150.0)
                .category(ProductCategoryEnum.RESIDENCIAL)
                .name("Seguro Residencial - APTO")
                .build();
        productService.insert(product2);

        //call
        List<Product> all = productService.findAll();

        //assert
        assertNotNull(all);
        assertEquals(2, all.size());
    }

    @Test
    void should_ThrowsProductNotFoundException_whenTryUpdateUnknownProduct() {
        //prepare
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .basePrice(200.0)
                .category(ProductCategoryEnum.RESIDENCIAL)
                .name("Seguro Residencial")
                .build();

        //call and assert
        assertThrows(ProductNotFoundException.class, () -> {
            productService.update(product);
        });
    }

    @Test
    void should_ThrowDataConstraintViolationException_whenTryInsertProductWithNameLargeThanColumn() {
        //prepare
        Product product = Product.builder()
                .basePrice(100.0)
                .category(ProductCategoryEnum.RESIDENCIAL)
                .name("Seguro Residencial Seguro Residencial Seguro Residencial")
                .build();

        //call and assert
        assertThrows(DataConstraintViolationException.class, () -> {
            productService.insert(product);
        });
    }

}
