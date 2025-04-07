package org.musinsa.stylist.infrastructure;

import java.util.Optional;
import org.musinsa.stylist.domain.model.Product;
import org.musinsa.stylist.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Optional<Product> findFirstByCategoryIdOrderByPriceAsc(Long categoryId) {
        return productJpaRepository.findFirstByCategoryIdOrderByPriceAsc(categoryId);
    }
}
