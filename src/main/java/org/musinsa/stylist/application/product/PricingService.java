package org.musinsa.stylist.application.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.musinsa.stylist.application.product.dto.CategoryPricingDetail;
import org.musinsa.stylist.application.product.dto.CategoryPricingResult;
import org.musinsa.stylist.common.exception.list.CustomNotFoundException;
import org.musinsa.stylist.domain.model.Brand;
import org.musinsa.stylist.domain.model.Category;
import org.musinsa.stylist.domain.model.Product;
import org.musinsa.stylist.domain.repository.BrandRepository;
import org.musinsa.stylist.domain.repository.CategoryRepository;
import org.musinsa.stylist.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PricingService implements PricingUseCase{

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public PricingService(ProductRepository productRepository, BrandRepository brandRepository,
        CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryPricingResult getCategoryMinPrices() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryPricingDetail> details = categories.stream()
                                                        .map(category -> {
                                                            Product product = productRepository.findFirstByCategoryIdOrderByPriceAsc(category.getCategoryId())
                                                                                               .orElseThrow(() -> new CustomNotFoundException("No product found for category: " + category.getCategoryName()));
                                                            Brand brand = brandRepository.findById(product.getBrandId())
                                                                                         .orElseThrow(() -> new CustomNotFoundException("Brand not found with id: " + product.getBrandId()));

                                                            return new CategoryPricingDetail(
                                                                category.getCategoryName(),
                                                                brand.getBrandName(),
                                                                product.getPrice()
                                                            );
                                                        })
                                                        .collect(Collectors.toList());

        int totalAmount = details.stream()
                                 .mapToInt(CategoryPricingDetail::price)
                                 .sum();

        return new CategoryPricingResult(details, totalAmount);
    }
}
