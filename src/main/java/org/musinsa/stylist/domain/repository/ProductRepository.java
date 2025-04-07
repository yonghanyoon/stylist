package org.musinsa.stylist.domain.repository;

import java.util.List;
import java.util.Optional;
import org.musinsa.stylist.domain.model.Product;

public interface ProductRepository {

    Optional<Product> findFirstByCategoryIdOrderByPriceAsc(Long categoryId);
    Optional<Product> findFirstByBrandIdAndCategoryIdOrderByPriceAsc(Long brandId, Long categoryId);
    List<Product> findAllByCategoryId(Long categoryId);
    Product save(Product product);
    Optional<Product> findById(Long productId);
    int countByBrandIdAndCategoryId(Long brandId, Long categoryId);
    void deleteById(Long productId);
    void deleteAllByBrandId(Long brandId);
}
