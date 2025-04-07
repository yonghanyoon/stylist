package org.musinsa.stylist.application.product.dto;

import java.util.List;

public record BrandPriceResult(
    String brandName,
    List<BrandPriceDetail> details,
    int totalAmount
) {

}