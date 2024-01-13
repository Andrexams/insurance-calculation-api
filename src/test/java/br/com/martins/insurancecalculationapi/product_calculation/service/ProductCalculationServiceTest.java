package br.com.martins.insurancecalculationapi.product_calculation.service;

import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import br.com.martins.insurancecalculationapi.product_taxes.enumeration.ProductTaxesEnum;
import br.com.martins.insurancecalculationapi.product_taxes.port.ProductTaxesOutputPort;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductCalculationServiceTest {

    @Mock
    ProductTaxesOutputPort productTaxesOutputPort;

    @InjectMocks
    ProductCalculationService productCalculationService;

    public static Stream<Arguments> productTestCases() {
        return Stream.of(Arguments.arguments(ProductCategoryEnum.VIDA, 100.00, 103.20),
                         Arguments.arguments(ProductCategoryEnum.AUTO, 50.00, 55.25),
                         Arguments.arguments(ProductCategoryEnum.VIAGEM, 250.0, 262.5),
                         Arguments.arguments(ProductCategoryEnum.RESIDENCIAL, 300.00, 321.0),
                         Arguments.arguments(ProductCategoryEnum.PATRIMONIAL, 15000.00, 16200.0));
    }

    @ParameterizedTest
    @MethodSource("productTestCases")
    void should_calculateTaxedPriceForAllCategories(ProductCategoryEnum productCategoryEnum,
                                                    Double basePrice,
                                                    Double expectedTaxedPrice){

        //prapare
        when(productTaxesOutputPort.findByCategory(any())).thenReturn(ProductTaxesEnum.valueOf(productCategoryEnum.name()));

        Product product = Product.builder()
                .category(productCategoryEnum)
                .basePrice(basePrice)
                .build();

        //call
        Double taxedPrice = productCalculationService.calculateTaxedPrice(product);

        //assert
        assertEquals(expectedTaxedPrice, taxedPrice);

    }
}
