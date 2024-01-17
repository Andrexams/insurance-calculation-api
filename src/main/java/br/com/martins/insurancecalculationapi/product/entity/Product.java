package br.com.martins.insurancecalculationapi.product.entity;

import br.com.martins.insurancecalculationapi.product.enumeration.ProductCategoryEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = Product.UNIQUE_NAME_CONSTRAINT, columnNames = {"name"})})
public class Product {

    public static final String UNIQUE_NAME_CONSTRAINT = "PRODUCT_UNIQUE_NAME";

    @Id
    @GeneratedValue
    private UUID id;
    @Column(length = 50)
    private String name;
    private ProductCategoryEnum category;
    private Double basePrice;
    private Double taxedPrice;
}
