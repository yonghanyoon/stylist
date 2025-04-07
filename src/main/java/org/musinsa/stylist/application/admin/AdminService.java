package org.musinsa.stylist.application.admin;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.musinsa.stylist.application.admin.dto.BrandRegistrationCommand;
import org.musinsa.stylist.application.admin.dto.BrandRegistrationResult;
import org.musinsa.stylist.application.admin.dto.BrandUpdateCommand;
import org.musinsa.stylist.application.admin.dto.ProductRegistrationCommand;
import org.musinsa.stylist.application.admin.dto.ProductRegistrationResult;
import org.musinsa.stylist.application.admin.dto.ProductUpdateCommand;
import org.musinsa.stylist.common.exception.list.CustomBadRequestException;
import org.musinsa.stylist.common.exception.list.CustomNotFoundException;
import org.musinsa.stylist.domain.model.Brand;
import org.musinsa.stylist.domain.model.Category;
import org.musinsa.stylist.domain.model.Product;
import org.musinsa.stylist.domain.repository.BrandRepository;
import org.musinsa.stylist.domain.repository.CategoryRepository;
import org.musinsa.stylist.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements AdminUseCase{

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminService(BrandRepository brandRepository,
        ProductRepository productRepository,
        CategoryRepository categoryRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public BrandRegistrationResult createBrand(BrandRegistrationCommand command) {
        if (brandRepository.existsByBrandName(command.brandName())) {
            throw new CustomBadRequestException("이미 존재하는 브랜드 이름입니다.");
        }
        Brand savedBrand = brandRepository.save(Brand.builder()
                                                     .brandName(command.brandName())
                                                     .build());

        List<Category> categories = categoryRepository.findAll();
        Set<Long> providedCategoryIds = command.products().stream()
                                               .map(ProductRegistrationCommand::categoryId)
                                               .collect(Collectors.toSet());
        if (providedCategoryIds.size() != categories.size()) {
            throw new CustomBadRequestException("카테고리의 수에 맞는 상품 정보를 제공해야 합니다.");
        }

        for (Category category : categories) {
            if (!providedCategoryIds.contains(category.getCategoryId())) {
                throw new CustomBadRequestException("모든 카테고리에 대한 상품 정보를 제공해야 합니다. 누락된 카테고리 ID: " + category.getCategoryId());
            }
        }

        for (ProductRegistrationCommand item : command.products()) {
            Product product = Product.builder()
                                     .brandId(savedBrand.getBrandId())
                                     .categoryId(item.categoryId())
                                     .price(item.price())
                                     .build();
            productRepository.save(product);
        }
        return new BrandRegistrationResult(savedBrand.getBrandId(), savedBrand.getBrandName());
    }

    @Override
    @Transactional
    public BrandRegistrationResult updateBrand(Long brandId, BrandUpdateCommand command) {
        Brand brand = brandRepository.findById(brandId)
                                             .orElseThrow(() -> new CustomNotFoundException("브랜드를 찾을 수 없습니다. BrandId: " + brandId));
        if (!brand.getBrandName().equals(command.brandName()) &&
            brandRepository.existsByBrandName(command.brandName())) {
            throw new CustomBadRequestException("이미 존재하는 브랜드 이름입니다.");
        }
        brand.updateBrandName(command.brandName());
        return new BrandRegistrationResult(brand.getBrandId(), brand.getBrandName());
    }

    @Override
    @Transactional
    public void deleteBrand(Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new CustomNotFoundException("브랜드를 찾을 수 없습니다. BrandId: " + brandId);
        }
        productRepository.deleteAllByBrandId(brandId);
        brandRepository.deleteById(brandId);
    }

    @Override
    @Transactional
    public ProductRegistrationResult createProduct(ProductRegistrationCommand command,
        Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new CustomNotFoundException("브랜드를 찾을 수 없습니다. BrandId: " + brandId);
        }

        List<Category> categories = categoryRepository.findAll();
        Set<Long> providedCategoryIds = categories.stream()
                                               .map(Category::getCategoryId)
                                               .collect(Collectors.toSet());
        if (!providedCategoryIds.contains(command.categoryId())) {
            throw new CustomBadRequestException("존재하지 않는 카테고리 입니다. CategoryId: " + command.categoryId());
        }

        Product product = Product.builder()
                                 .brandId(brandId)
                                 .categoryId(command.categoryId())
                                 .price(command.price())
                                 .build();
        Product saved = productRepository.save(product);
        return new ProductRegistrationResult(saved.getProductId(), saved.getBrandId(), saved.getCategoryId(), saved.getPrice());
    }

    @Override
    @Transactional
    public ProductRegistrationResult updateProduct(Long productId,
        ProductUpdateCommand command, Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new CustomNotFoundException("브랜드를 찾을 수 없습니다. BrandId: " + brandId);
        }
        Product product = productRepository.findById(productId)
                                            .orElseThrow(() -> new CustomNotFoundException("상품을 찾을 수 없습니다. ProductId: " + productId));
        List<Category> categories = categoryRepository.findAll();
        Set<Long> providedCategoryIds = categories.stream()
                                                  .map(Category::getCategoryId)
                                                  .collect(Collectors.toSet());
        if (!providedCategoryIds.contains(command.categoryId())) {
            throw new CustomBadRequestException("존재하지 않는 카테고리 입니다. CategoryId: " + command.categoryId());
        }
        product.updateProduct(brandId, command.categoryId(), command.price());
        return new ProductRegistrationResult(product.getProductId(), product.getBrandId(), product.getCategoryId(), product.getPrice());
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                                           .orElseThrow(() -> new CustomNotFoundException("상품을 찾을 수 없습니다. ProductId: " + productId));
        int count = productRepository.countByBrandIdAndCategoryId(product.getBrandId(), product.getCategoryId());
        if (count <= 1) {
            throw new IllegalArgumentException("해당 카테고리에는 최소 1개 이상의 상품이 있어야 합니다. 삭제할 수 없습니다. (BrandId: "
                                                   + product.getBrandId() + ", CategoryId: " + product.getCategoryId() + ")");
        }
        productRepository.deleteById(productId);
    }
}
