package org.musinsa.stylist.integration;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.musinsa.stylist.application.admin.AdminService;
import org.musinsa.stylist.application.admin.dto.BrandRegistrationCommand;
import org.musinsa.stylist.application.admin.dto.BrandRegistrationResult;
import org.musinsa.stylist.application.admin.dto.BrandUpdateCommand;
import org.musinsa.stylist.application.admin.dto.ProductRegistrationCommand;
import org.musinsa.stylist.domain.model.Brand;
import org.musinsa.stylist.domain.model.Category;
import org.musinsa.stylist.domain.repository.BrandRepository;
import org.musinsa.stylist.domain.repository.CategoryRepository;
import org.musinsa.stylist.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
public class AdminServiceIntegrationTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("정상 케이스: 모든 카테고리에 대한 상품 정보를 제공하여 브랜드 등록")
    @Test
    void createBrandIntegration_success() {
        // given
        List<Category> categories = categoryRepository.findAll();
        int categoryCount = categories.size();
        List<ProductRegistrationCommand> productCommands = categories.stream()
                                                                     .map(cat -> new ProductRegistrationCommand(cat.getCategoryId(), 10000))
                                                                     .toList();
        BrandRegistrationCommand command = new BrandRegistrationCommand("newBrand", productCommands);

        // when
        BrandRegistrationResult result = adminService.createBrand(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.brandName()).isEqualTo("newBrand");
    }

    @Test
    @DisplayName("정상 케이스: 존재하는 브랜드의 이름을 업데이트한다")
    void updateBrandIntegration_success() {
        // given
        Brand brand = Brand.builder().brandName("OldBrand").build();
        brand = brandRepository.save(brand);
        Long brandId = brand.getBrandId();
        BrandUpdateCommand command = new BrandUpdateCommand("NewBrand");

        // when
        var result = adminService.updateBrand(brandId, command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.brandId()).isEqualTo(brandId);
        assertThat(result.brandName()).isEqualTo("NewBrand");
    }
}
