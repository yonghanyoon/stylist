package org.musinsa.stylist.domain.repository;

import java.util.Optional;
import org.musinsa.stylist.domain.model.Product;

public interface ProductRepository {

    Optional<Product> findFirstByCategoryIdOrderByPriceAsc(Long categoryId);
    Optional<Product> findFirstByBrandIdAndCategoryIdOrderByPriceAsc(Long brandId, Long categoryId);

}
