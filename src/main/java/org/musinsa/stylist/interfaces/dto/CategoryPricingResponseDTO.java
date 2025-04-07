package org.musinsa.stylist.interfaces.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.musinsa.stylist.application.product.dto.CategoryPricingResult;

public record CategoryPricingResponseDTO(
    List<CategoryPricingDetailDTO> details,
    int totalAmount
) {
    public static CategoryPricingResponseDTO from(CategoryPricingResult result) {
        List<CategoryPricingDetailDTO> detailDTOs = result.details().stream()
                                                          .map(detail -> new CategoryPricingDetailDTO(
                                                              detail.categoryName(),
                                                              detail.brandName(),
                                                              detail.price()))
                                                          .collect(Collectors.toList());
        return new CategoryPricingResponseDTO(detailDTOs, result.totalAmount());
    }
}