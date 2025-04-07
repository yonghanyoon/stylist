package org.musinsa.stylist.interfaces.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.musinsa.stylist.application.product.dto.BrandPriceResult;

public record BrandPriceResponseDTO(
    String brandName,
    List<BrandPriceDetailDTO> details,
    int totalAmount
) {
    public static BrandPriceResponseDTO from(BrandPriceResult result) {
        List<BrandPriceDetailDTO> details = result.details().stream()
                                                    .map(detail -> new BrandPriceDetailDTO(detail.categoryName(), detail.price()))
                                                    .collect(Collectors.toList());
        return new BrandPriceResponseDTO(result.brandName(), details, result.totalAmount());
    }
}