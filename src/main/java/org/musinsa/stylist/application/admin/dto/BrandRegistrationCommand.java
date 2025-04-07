package org.musinsa.stylist.application.admin.dto;

import java.util.List;

public record BrandRegistrationCommand(
    String brandName,
    List<ProductRegistrationCommand> products
) {

}