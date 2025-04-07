package org.musinsa.stylist.interfaces.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.musinsa.stylist.application.product.dto.CategoryPriceRangeResult;

public record CategoryPriceRangeResponseDTO(
    String category,
    List<PriceRangeDetailDTO> minPrice,
    List<PriceRangeDetailDTO> maxPrice
) {
    public static CategoryPriceRangeResponseDTO from(CategoryPriceRangeResult result) {
        List<PriceRangeDetailDTO> minDTO = result.minPrice().stream()
                .map(detail -> new PriceRangeDetailDTO(detail.brand(), detail.price()))
                .collect(Collectors.toList());
        List<PriceRangeDetailDTO> maxDTO = result.maxPrice().stream()
                .map(detail -> new PriceRangeDetailDTO(detail.brand(), detail.price()))
                .collect(Collectors.toList());
        return new CategoryPriceRangeResponseDTO(result.category(), minDTO, maxDTO);
    }
}