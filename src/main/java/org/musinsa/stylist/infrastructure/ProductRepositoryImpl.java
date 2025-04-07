package org.musinsa.stylist.infrastructure;

import java.util.List;
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

    @Override
    public Optional<Product> findFirstByBrandIdAndCategoryIdOrderByPriceAsc(Long brandId,
        Long categoryId) {
        return productJpaRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(brandId, categoryId);
    }

    @Override
    public List<Product> findAllByCategoryId(Long categoryId) {
        return productJpaRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public int countByBrandIdAndCategoryId(Long brandId, Long categoryId) {
        return productJpaRepository.countByBrandIdAndCategoryId(brandId, categoryId);
    }

    @Override
    public void deleteById(Long productId) {
        productJpaRepository.deleteById(productId);
    }

    @Override
    public void deleteAllByBrandId(Long brandId) {
        productJpaRepository.deleteAllByBrandId(brandId);
    }
}
