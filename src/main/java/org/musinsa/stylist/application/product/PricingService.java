package org.musinsa.stylist.application.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.musinsa.stylist.application.product.dto.BrandPriceDetail;
import org.musinsa.stylist.application.product.dto.BrandPriceResult;
import org.musinsa.stylist.application.product.dto.CategoryPriceRangeResult;
import org.musinsa.stylist.application.product.dto.CategoryPricingDetail;
import org.musinsa.stylist.application.product.dto.CategoryPricingResult;
import org.musinsa.stylist.application.product.dto.PriceRangeDetail;
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
                                                                                               .orElseThrow(() -> new CustomNotFoundException("해당 카테고리의 상품을 찾을 수 없습니다. category: " + category.getCategoryName()));
                                                            Brand brand = brandRepository.findById(product.getBrandId())
                                                                                         .orElseThrow(() -> new CustomNotFoundException("브랜드를 찾을 수 없습니다. BrandId: " + product.getBrandId()));

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

    @Override
    @Transactional(readOnly = true)
    public BrandPriceResult getLowestSingleBrandPrice() {
        List<Category> categories = categoryRepository.findAll();

        return brandRepository.findAll().stream()
                              .map(brand -> getBrandPriceResponseForBrand(brand, categories))
                              .filter(Optional::isPresent)
                              .map(Optional::get)
                              .min(Comparator.comparingInt(BrandPriceResult::totalAmount))
                              .orElseThrow(() -> new CustomNotFoundException("모든 카테고리에 상품을 제공하는 브랜드를 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryPriceRangeResult getCategoryPriceRange(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName)
                                              .orElseThrow(() -> new CustomNotFoundException("카테고리를 찾을 수 없습니다. categoryName: " + categoryName));

        List<Product> products = productRepository.findAllByCategoryId(category.getCategoryId());
        if (products.isEmpty()) {
            throw new CustomNotFoundException("해당 카테고리의 상품을 찾을 수 없습니다. category: " + category.getCategoryName());
        }

        int minPrice = products.stream()
                               .mapToInt(Product::getPrice)
                               .min()
                               .orElseThrow(() -> new CustomNotFoundException("최저 가격을 계산할 상품이 없습니다."));
        int maxPrice = products.stream()
                               .mapToInt(Product::getPrice)
                               .max()
                               .orElseThrow(() -> new CustomNotFoundException("최고 가격을 계산할 상품이 없습니다."));

        List<PriceRangeDetail> minDetails = getPriceRangeDetails(products, minPrice);
        List<PriceRangeDetail> maxDetails = getPriceRangeDetails(products, maxPrice);

        return new CategoryPriceRangeResult(category.getCategoryName(), minDetails, maxDetails);
    }

    private Optional<BrandPriceResult> getBrandPriceResponseForBrand(Brand brand, List<Category> categories) {
        List<BrandPriceDetail> details = new ArrayList<>();
        int sum = 0;
        for (Category category : categories) {
            Optional<Product> productOpt = productRepository
                .findFirstByBrandIdAndCategoryIdOrderByPriceAsc(brand.getBrandId(), category.getCategoryId());
            if (productOpt.isEmpty()) {
                return Optional.empty();
            }
            Product product = productOpt.get();
            details.add(new BrandPriceDetail(category.getCategoryName(), product.getPrice()));
            sum += product.getPrice();
        }
        return Optional.of(new BrandPriceResult(brand.getBrandName(), details, sum));
    }

    private List<PriceRangeDetail> getPriceRangeDetails(List<Product> products, int targetPrice) {
        return products.stream()
                       .filter(product -> product.getPrice() == targetPrice)
                       .map(this::mapProductToPriceRangeDetail)
                       .distinct()
                       .toList();
    }

    private PriceRangeDetail mapProductToPriceRangeDetail(Product product) {
        Brand brand = brandRepository.findById(product.getBrandId())
                                     .orElseThrow(() -> new CustomNotFoundException("브랜드를 찾을 수 없습니다. brandId: " + product.getBrandId()));
        return new PriceRangeDetail(brand.getBrandName(), product.getPrice());
    }
}
