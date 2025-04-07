package org.musinsa.stylist.application.product;

import org.musinsa.stylist.application.product.dto.BrandPriceResult;
import org.musinsa.stylist.application.product.dto.CategoryPriceRangeResult;
import org.musinsa.stylist.application.product.dto.CategoryPricingResult;

public interface PricingUseCase {
    CategoryPricingResult getCategoryMinPrices();
    BrandPriceResult getLowestSingleBrandPrice();
    CategoryPriceRangeResult getCategoryPriceRange(String categoryName);
}
