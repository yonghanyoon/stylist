package org.musinsa.stylist.infrastructure;

import java.util.List;
import java.util.Optional;
import org.musinsa.stylist.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    Optional<Product> findFirstByCategoryIdOrderByPriceAsc(Long categoryId);
    Optional<Product> findFirstByBrandIdAndCategoryIdOrderByPriceAsc(Long brandId, Long categoryId);
    List<Product> findAllByCategoryId(Long categoryId);
    int countByBrandIdAndCategoryId(Long brandId, Long categoryId);
    void deleteAllByBrandId(Long brandId);
}
