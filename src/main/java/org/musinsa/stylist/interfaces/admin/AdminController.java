package org.musinsa.stylist.interfaces.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.musinsa.stylist.application.admin.AdminUseCase;
import org.musinsa.stylist.interfaces.dto.BrandRegistrationRequestDTO;
import org.musinsa.stylist.interfaces.dto.BrandResponseDTO;
import org.musinsa.stylist.interfaces.dto.BrandUpdateRequestDTO;
import org.musinsa.stylist.interfaces.dto.ProductRegistrationRequestDTO;
import org.musinsa.stylist.interfaces.dto.ProductResponseDTO;
import org.musinsa.stylist.interfaces.dto.ProductUpdateRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AdminController")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminUseCase adminUseCase;

    public AdminController(AdminUseCase adminUseCase) {
        this.adminUseCase = adminUseCase;
    }

    // 브랜드 등록 API
    @Operation(description = "브랜드 등록 API")
    @PostMapping("/brands")
    public ResponseEntity<BrandResponseDTO> createBrand(@RequestBody BrandRegistrationRequestDTO requestDTO) {
        return ResponseEntity.ok(BrandResponseDTO.from(adminUseCase.createBrand(BrandRegistrationRequestDTO.toCommand(requestDTO))));
    }

    // 브랜드 수정 API
    @Operation(description = "브랜드 수정 API")
    @PutMapping("/brands/{brandId}")
    public ResponseEntity<BrandResponseDTO> updateBrand(@PathVariable Long brandId, @RequestBody BrandUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(BrandResponseDTO.from(adminUseCase.updateBrand(brandId, BrandUpdateRequestDTO.toCommand(requestDTO))));
    }

    // 브랜드 삭제 API
    @Operation(description = "브랜드 삭제 API")
    @DeleteMapping("/brands/{brandId}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long brandId) {
        adminUseCase.deleteBrand(brandId);
        return ResponseEntity.ok().build();
    }

    // 상품 등록 API
    @Operation(description = "상품 등록 API")
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRegistrationRequestDTO requestDTO,
        @RequestParam Long brandId) {
        return ResponseEntity.ok(ProductResponseDTO.from(adminUseCase.createProduct(ProductRegistrationRequestDTO.toCommand(requestDTO), brandId)));
    }

    // 상품 수정 API
    @Operation(description = "상품 수정 API")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequestDTO requestDTO,
        @RequestParam Long brandId) {
        return ResponseEntity.ok(ProductResponseDTO.from(adminUseCase.updateProduct(productId, ProductUpdateRequestDTO.toCommand(requestDTO), brandId)));
    }

    // 상품 삭제 API
    @Operation(description = "상품 삭제 API")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        adminUseCase.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
