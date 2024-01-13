package br.com.martins.insurancecalculationapi.product.adapter.controller;

import br.com.martins.insurancecalculationapi.commom.adapter.rest.CustomErrorResponse;
import br.com.martins.insurancecalculationapi.product.adapter.dto.ProductDto;
import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRestControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void should_CreateAndUpdateProductWithTaxedPrice() {
        //prepare
        ProductDto productDtoRequest = ProductDto.builder()
                .basePrice(100.0)
                .category(ProductCategoryEnum.VIDA.name())
                .name("Seguro de Vida")
                .build();
        HttpEntity<ProductDto> request = getRequest(productDtoRequest);

        //call create product
        ResponseEntity<ProductDto> productDtoRespEntity = this.restTemplate.postForEntity(getUrl(), request,
                ProductDto.class);
        ProductDto productDtoResponse = productDtoRespEntity.getBody();

        //assert response
        assertEquals(productDtoRespEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(productDtoResponse.getId());
        assertEquals(productDtoRequest.getCategory(), productDtoResponse.getCategory());
        assertEquals(productDtoRequest.getName(), productDtoResponse.getName());
        assertEquals(productDtoRequest.getBasePrice(), productDtoResponse.getBasePrice());
        assertEquals(103.2, productDtoResponse.getTaxedPrice());

        //do update
        should_UpdateProductWithTaxedPrice(productDtoResponse.getId());
    }

    private void should_UpdateProductWithTaxedPrice(UUID productId) {
        //prepare
        ProductDto productDtoRequest = ProductDto.builder()
                .id(productId)
                .basePrice(200.0)
                .category(ProductCategoryEnum.VIDA.name())
                .name("Seguro de Vida Especial")
                .build();
        HttpEntity<ProductDto> request = getRequest(productDtoRequest);

        //call create product
        ResponseEntity<ProductDto> productDtoRespEntity = this.restTemplate.exchange(getUrl(), HttpMethod.PUT, request,
                ProductDto.class);
        ProductDto productDtoResponse = productDtoRespEntity.getBody();

        //assert response
        assertEquals(productDtoRespEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(productDtoResponse.getId());
        assertEquals(productDtoRequest.getCategory(), productDtoResponse.getCategory());
        assertEquals(productDtoRequest.getName(), productDtoResponse.getName());
        assertEquals(productDtoRequest.getBasePrice(), productDtoResponse.getBasePrice());
        assertEquals(206.4, productDtoResponse.getTaxedPrice());
    }

    @Test
    void should_ReturnErrorMessageAndNotFoundStatus_whenTryUpdateUnknownProduct() {
        //prepare
        ProductDto productDtoRequest = ProductDto.builder()
                .id(UUID.randomUUID())
                .basePrice(200.0)
                .category(ProductCategoryEnum.RESIDENCIAL.name())
                .name("Seguro Residencial")
                .build();
        HttpEntity<ProductDto> request = getRequest(productDtoRequest);

        //call
        ResponseEntity<CustomErrorResponse> responseResponseEntity =
                this.restTemplate.exchange(getUrl(), HttpMethod.PUT, request, CustomErrorResponse.class);

        //assert
        assertEquals(responseResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        assertCustomErrorResponse(responseResponseEntity);
    }

    private static void assertCustomErrorResponse(ResponseEntity<CustomErrorResponse> responseResponseEntity) {
        CustomErrorResponse body = responseResponseEntity.getBody();
        Assertions.assertThat(body.getMessage()).isNotEmpty();
        Assertions.assertThat(body.getType()).isNotEmpty();
        assertNotNull(body.getTimestamp());
    }

    @Test
    void should_ReturnErrorMessageAndBadRequest_whenTryInsertSameProductName() {
        //prepare
        ProductDto productDto = ProductDto.builder()
                .basePrice(100.0)
                .category(ProductCategoryEnum.RESIDENCIAL.name())
                .name("Seguro Residencial")
                .build();

        HttpEntity<ProductDto> request = getRequest(productDto);

        //call create product
        ResponseEntity<ProductDto> productDtoRespEntity = this.restTemplate.postForEntity(getUrl(), request,
                ProductDto.class);
        //assert response
        assertEquals(productDtoRespEntity.getStatusCode(), HttpStatus.OK);

        //try create same product
        ResponseEntity<CustomErrorResponse> customErrorResponseEntity = this.restTemplate.postForEntity(getUrl(), request,
                CustomErrorResponse.class);

        //assert response
        assertEquals(customErrorResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertCustomErrorResponse(customErrorResponseEntity);
    }

    String getUrl() {
        return "http://localhost:" + port + "/insurance-calculation-api/products";
    }

    static HttpEntity<ProductDto> getRequest(ProductDto productDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(productDto, headers);
    }

}
