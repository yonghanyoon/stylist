package org.musinsa.stylist.application.admin;

import org.musinsa.stylist.application.admin.dto.BrandRegistrationCommand;
import org.musinsa.stylist.application.admin.dto.BrandRegistrationResult;
import org.musinsa.stylist.application.admin.dto.BrandUpdateCommand;
import org.musinsa.stylist.application.admin.dto.ProductRegistrationCommand;
import org.musinsa.stylist.application.admin.dto.ProductRegistrationResult;
import org.musinsa.stylist.application.admin.dto.ProductUpdateCommand;

public interface AdminUseCase {
    BrandRegistrationResult createBrand(BrandRegistrationCommand command);
    BrandRegistrationResult updateBrand(Long brandId, BrandUpdateCommand command);
    void deleteBrand(Long brandId);

    ProductRegistrationResult createProduct(ProductRegistrationCommand command, Long brandId);
    ProductRegistrationResult updateProduct(Long productId, ProductUpdateCommand command, Long brandId);
    void deleteProduct(Long productId);
}
