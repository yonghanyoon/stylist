package org.musinsa.stylist.application.product;

import org.musinsa.stylist.application.product.dto.BrandPriceResult;
import org.musinsa.stylist.application.product.dto.CategoryPricingResult;
import org.musinsa.stylist.domain.model.Brand;

public interface PricingUseCase {
    CategoryPricingResult getCategoryMinPrices();
    BrandPriceResult getLowestSingleBrandPrice();
}
