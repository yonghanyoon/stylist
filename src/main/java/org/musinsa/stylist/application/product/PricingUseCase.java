package org.musinsa.stylist.application.product;

import org.musinsa.stylist.application.product.dto.CategoryPricingResult;

public interface PricingUseCase {
    CategoryPricingResult getCategoryMinPrices();
}
