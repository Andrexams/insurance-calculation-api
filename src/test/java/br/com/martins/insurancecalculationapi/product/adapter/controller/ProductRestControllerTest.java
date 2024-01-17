package br.com.martins.insurancecalculationapi.product.adapter.controller;

import br.com.martins.insurancecalculationapi.common.adapter.rest.CustomErrorResponse;
import br.com.martins.insurancecalculationapi.product.adapter.dto.ProductDto;
import br.com.martins.insurancecalculationapi.product.adapter.persitence.ProductRepository;
import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
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

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void deleteProducts() {
        productRepository.deleteAll();
    }

    @Test
    void should_CreateAndUpdateProductWithTaxedPrice() {
        //prepare
        ProductDto productDtoRequest = ProductDto.builder()
                .preco_base(100.0)
                .categoria(ProductCategoryEnum.VIDA.name())
                .nome("Seguro de Vida")
                .build();
        HttpEntity<ProductDto> request = getRequest(productDtoRequest);

        //call create product
        ResponseEntity<ProductDto> productDtoRespEntity = this.restTemplate.postForEntity(getUrl(), request,
                ProductDto.class);
        ProductDto productDtoResponse = productDtoRespEntity.getBody();

        //assert response
        assertEquals(productDtoRespEntity.getStatusCode(), HttpStatus.OK);
        assertProduct(productDtoRequest, productDtoResponse);

        //do update
        should_UpdateProductWithTaxedPrice(productDtoResponse.getId());
    }

    private void should_UpdateProductWithTaxedPrice(UUID productId) {
        //prepare
        ProductDto productDtoRequest = ProductDto.builder()
                .id(productId)
                .preco_base(200.0)
                .categoria(ProductCategoryEnum.VIDA.name())
                .nome("Seguro de Vida Especial")
                .build();
        HttpEntity<ProductDto> request = getRequest(productDtoRequest);

        //call create product
        ResponseEntity<ProductDto> productDtoRespEntity = this.restTemplate.exchange(getUrl(), HttpMethod.PUT, request,
                ProductDto.class);
        ProductDto productDtoResponse = productDtoRespEntity.getBody();

        //assert response
        assertEquals(productDtoRespEntity.getStatusCode(), HttpStatus.OK);
        assertProduct(productDtoRequest, productDtoResponse);
    }

    @Test
    void should_ReturnErrorMessageAndNotFoundStatus_whenTryUpdateUnknownProduct() {
        //prepare
        ProductDto productDtoRequest = ProductDto.builder()
                .id(UUID.randomUUID())
                .preco_base(200.0)
                .categoria(ProductCategoryEnum.RESIDENCIAL.name())
                .nome("Seguro Residencial")
                .build();
        HttpEntity<ProductDto> request = getRequest(productDtoRequest);

        //call
        ResponseEntity<CustomErrorResponse> responseResponseEntity =
                this.restTemplate.exchange(getUrl(), HttpMethod.PUT, request, CustomErrorResponse.class);

        //assert
        assertEquals(responseResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        assertCustomErrorResponse(responseResponseEntity);
    }

    @Test
    void should_ReturnErrorMessageAndBadRequest_whenTryInsertSameProductName() {
        //prepare
        ProductDto productDto = ProductDto.builder()
                .preco_base(100.0)
                .categoria(ProductCategoryEnum.RESIDENCIAL.name())
                .nome("Seguro Residencial")
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

    @Test
    void should_ReturnAllProducts() {
        //prepare
        ProductDto productDto = ProductDto.builder()
                .preco_base(500.0)
                .categoria(ProductCategoryEnum.RESIDENCIAL.name())
                .nome("Seguro Residencial Especial")
                .build();

        HttpEntity<ProductDto> request = getRequest(productDto);
        this.restTemplate.postForEntity(getUrl(), request, ProductDto.class);

        ProductDto productDto2 = ProductDto.builder()
                .preco_base(350.0)
                .categoria(ProductCategoryEnum.VIAGEM.name())
                .nome("Seguro Viagem Internacional")
                .build();

        HttpEntity<ProductDto> request2 = getRequest(productDto2);
        this.restTemplate.postForEntity(getUrl(), request2, ProductDto.class);

        //call get all
        ResponseEntity<ProductDto[]> productDtoRespEntity = this.restTemplate.getForEntity(getUrl(), ProductDto[].class);

        //assert response
        assertEquals(productDtoRespEntity.getStatusCode(), HttpStatus.OK);

        ProductDto[] productDtos = productDtoRespEntity.getBody();
        assertNotNull(productDtos);
        assertEquals(2, productDtos.length);

        ProductDto productDtoByName = Arrays.stream(productDtos)
                .filter(p -> p.getNome().equals(productDto.getNome()))
                .findFirst()
                .orElseThrow();

        assertProduct(productDto, productDtoByName);
    }

    @Test
    void should_ThrowException() {
        //prepare
        ProductDto productDtoRequest = ProductDto.builder()
                .id(UUID.randomUUID())
                .preco_base(200.0)
                .categoria("CASA")
                .nome("Seguro Residencial")
                .build();
        HttpEntity<ProductDto> request = getRequest(productDtoRequest);

        //call
        ResponseEntity<CustomErrorResponse> customErrorResponseEntity = this.restTemplate.postForEntity(getUrl(), request,
                CustomErrorResponse.class);

        //assert
        assertEquals(customErrorResponseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertCustomErrorResponse(customErrorResponseEntity);
    }


    private static void assertCustomErrorResponse(ResponseEntity<CustomErrorResponse> responseResponseEntity) {
        CustomErrorResponse body = responseResponseEntity.getBody();
        Assertions.assertThat(body.getMessage()).isNotEmpty();
        Assertions.assertThat(body.getType()).isNotEmpty();
        assertNotNull(body.getTimestamp());
    }

    private static void assertProduct(ProductDto productDtoRequest, ProductDto productDtoResponse) {
        assertNotNull(productDtoResponse.getId());
        assertEquals(productDtoRequest.getCategoria(), productDtoResponse.getCategoria());
        assertEquals(productDtoRequest.getNome(), productDtoResponse.getNome());
        assertEquals(productDtoRequest.getPreco_base(), productDtoResponse.getPreco_base());
        assertNotNull(productDtoResponse.getPreco_tarifado());
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
