package br.com.martins.insurancecalculationapi.product.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {


    private UUID id;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("categoria")
    private String category;

    @JsonProperty("preco_base")
    private Double basePrice;

    @JsonProperty("preco_tarifado")
    private Double taxedPrice;
}
