package br.com.martins.insurancecalculationapi.product.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {


    private UUID id;

    @NotBlank
    @Size(max = 50)
    private String nome;

    @NotBlank
    private String categoria;

    @NotNull
    private Double preco_base;


    private Double preco_tarifado;
}
