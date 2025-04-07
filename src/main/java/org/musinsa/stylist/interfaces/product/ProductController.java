package org.musinsa.stylist.interfaces.product;

import org.musinsa.stylist.application.product.PricingUseCase;
import org.musinsa.stylist.interfaces.dto.BrandPriceResponseDTO;
import org.musinsa.stylist.interfaces.dto.CategoryPricingResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final PricingUseCase pricingUseCase;
    public ProductController(PricingUseCase pricingUseCase) {
        this.pricingUseCase = pricingUseCase;
    }

    // 카테고리별 최저가 조회 API
    @GetMapping("/categories")
    public ResponseEntity<CategoryPricingResponseDTO> getCategoryMinPrices() {
        return ResponseEntity.ok(CategoryPricingResponseDTO.from(
            pricingUseCase.getCategoryMinPrices()));
    }

    // 단일 브랜드 최저가 조회 API
    @GetMapping("/brands/lowest")
    public ResponseEntity<BrandPriceResponseDTO> getLowestSingleBrandPrice() {
        BrandPriceResponseDTO response = BrandPriceResponseDTO.from(pricingUseCase.getLowestSingleBrandPrice());
        return ResponseEntity.ok(response);
    }
}
