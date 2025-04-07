package org.musinsa.stylist.interfaces.dto;

import java.util.List;
import org.musinsa.stylist.application.admin.dto.BrandRegistrationCommand;
import org.musinsa.stylist.application.admin.dto.ProductRegistrationCommand;
import org.musinsa.stylist.domain.model.Brand;

public record BrandRegistrationRequestDTO(
    String brandName,
    List<ProductRegistrationRequestDTO> products
) {
    public static BrandRegistrationCommand toCommand(BrandRegistrationRequestDTO dto) {
        List<ProductRegistrationCommand> productCommands = dto.products().stream()
                                                                   .map(ProductRegistrationRequestDTO::toCommand)
                                                                   .toList();
        return new BrandRegistrationCommand(dto.brandName(), productCommands);
    }
}