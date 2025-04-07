package org.musinsa.stylist.application.product.dto;

import java.util.List;

public record CategoryPriceRangeResult(
    String category,
    List<PriceRangeDetail> minPrice,
    List<PriceRangeDetail> maxPrice
) {

}