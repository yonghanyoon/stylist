package org.musinsa.stylist.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.musinsa.stylist.application.product.PricingUseCase;
import org.musinsa.stylist.interfaces.dto.BrandPriceResponseDTO;
import org.musinsa.stylist.interfaces.dto.CategoryPriceRangeResponseDTO;
import org.musinsa.stylist.interfaces.dto.CategoryPricingResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProductController")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final PricingUseCase pricingUseCase;
    public ProductController(PricingUseCase pricingUseCase) {
        this.pricingUseCase = pricingUseCase;
    }

    // 카테고리별 최저가 조회 API
    @Operation(description = "카테고리별 최저가 조회 API")
    @GetMapping("/categories")
    public ResponseEntity<CategoryPricingResponseDTO> getCategoryMinPrices() {
        return ResponseEntity.ok(CategoryPricingResponseDTO.from(
            pricingUseCase.getCategoryMinPrices()));
    }

    // 단일 브랜드 최저가 조회 API
    @Operation(description = "단일 브랜드 최저가 조회 API")
    @GetMapping("/brands/lowest")
    public ResponseEntity<BrandPriceResponseDTO> getLowestSingleBrandPrice() {
        return ResponseEntity.ok(BrandPriceResponseDTO.from(pricingUseCase.getLowestSingleBrandPrice()));
    }

    // 특정 카테고리 최저, 최고가 조회 API
    @Operation(description = "특정 카테고리 최저, 최고가 조회 API")
    @GetMapping("/categories/{categoryName}")
    public ResponseEntity<CategoryPriceRangeResponseDTO> getCategoryPriceRange(@PathVariable String categoryName) {
        return ResponseEntity.ok(CategoryPriceRangeResponseDTO.from(pricingUseCase.getCategoryPriceRange(categoryName)));
    }
}
