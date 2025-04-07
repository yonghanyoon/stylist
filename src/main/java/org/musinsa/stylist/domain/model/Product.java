package org.musinsa.stylist.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.musinsa.stylist.common.jpa.BaseEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private Long brandId;
    private Long categoryId;
    private int price;

    public void updateProduct(Long brandId, Long categoryId, int price) {
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.price = price;
    }
}
