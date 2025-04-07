package org.musinsa.stylist.application.product.dto;

import java.util.List;

public record CategoryPricingResult(
    List<CategoryPricingDetail> details,
    int totalAmount
) {
}
