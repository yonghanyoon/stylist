package org.musinsa.stylist.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.musinsa.stylist.application.admin.AdminService;
import org.musinsa.stylist.application.admin.dto.BrandRegistrationCommand;
import org.musinsa.stylist.application.admin.dto.BrandRegistrationResult;
import org.musinsa.stylist.application.admin.dto.BrandUpdateCommand;
import org.musinsa.stylist.application.admin.dto.ProductRegistrationCommand;
import org.musinsa.stylist.common.exception.list.CustomBadRequestException;
import org.musinsa.stylist.common.exception.list.CustomNotFoundException;
import org.musinsa.stylist.domain.model.Brand;
import org.musinsa.stylist.domain.model.Category;
import org.musinsa.stylist.domain.model.Product;
import org.musinsa.stylist.domain.repository.BrandRepository;
import org.musinsa.stylist.domain.repository.CategoryRepository;
import org.musinsa.stylist.domain.repository.ProductRepository;

public class AdminServiceUnitTest {

    private ProductRepository productRepository;
    private BrandRepository brandRepository;
    private CategoryRepository categoryRepository;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        brandRepository = Mockito.mock(BrandRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        adminService = new AdminService(brandRepository, productRepository, categoryRepository);
    }

    @DisplayName("성공 케이스: 모든 카테고리에 대한 상품 정보를 제공하여 브랜드 등록")
    @Test
    void createBrand_success() {
        // given
        List<ProductRegistrationCommand> productCommands = Arrays.asList(
            new ProductRegistrationCommand(10000L, 10000),
            new ProductRegistrationCommand(20000L, 5000)
        );
        BrandRegistrationCommand command = new BrandRegistrationCommand("NewBrand", productCommands);

        when(brandRepository.existsByBrandName("NewBrand")).thenReturn(false);
        Category cat1 = Category.builder().categoryId(10000L).categoryName("상의").build();
        Category cat2 = Category.builder().categoryId(20000L).categoryName("아우터").build();
        List<Category> categories = Arrays.asList(cat1, cat2);
        when(categoryRepository.findAll()).thenReturn(categories);
        Brand savedBrand = Brand.builder().brandId(30000L).brandName("NewBrand").build();
        when(brandRepository.save(any(Brand.class))).thenReturn(savedBrand);
        when(productRepository.save(any(
            Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        BrandRegistrationResult result = adminService.createBrand(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.brandName()).isEqualTo("NewBrand");
        assertThat(result.brandId()).isEqualTo(30000L);
    }

    @DisplayName("실패 케이스: 이미 존재하는 브랜드 이름인 경우")
    @Test
    void createBrand_failure_duplicateBrand() {
        // given
        BrandRegistrationCommand command = new BrandRegistrationCommand("ExistingBrand", Arrays.asList(
            new ProductRegistrationCommand(10000L, 10000),
            new ProductRegistrationCommand(20000L, 5000)
        ));
        when(brandRepository.existsByBrandName("ExistingBrand")).thenReturn(true);

        // when then
        CustomBadRequestException ex = assertThrows(CustomBadRequestException.class,
                                                             () -> adminService.createBrand(command));
        assertThat(ex.getMessage()).contains("이미 존재하는 브랜드 이름입니다.");
    }

    @DisplayName("실패 케이스: 제공된 상품 정보에 누락된 카테고리가 있는 경우")
    @Test
    void createBrand_failure_missingCategory() {
        // given
        Category cat1 = Category.builder().categoryId(10000L).categoryName("상의").build();
        Category cat2 = Category.builder().categoryId(20000L).categoryName("아우터").build();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        List<ProductRegistrationCommand> productCommands = Arrays.asList(
            new ProductRegistrationCommand(10000L, 10000)
        );
        BrandRegistrationCommand command = new BrandRegistrationCommand("NewBrand", productCommands);
        when(brandRepository.existsByBrandName("NewBrand")).thenReturn(false);

        // when then
        CustomBadRequestException ex = assertThrows(CustomBadRequestException.class,
                                                    () -> adminService.createBrand(command));
        assertThat(ex.getMessage()).contains("카테고리의 수에 맞는 상품 정보를 제공해야 합니다.");
    }

    @DisplayName("정상 케이스: 브랜드 업데이트 성공")
    @Test
    void updateBrand_success() {
        // given
        Long brandId = 30000L;
        Brand existingBrand = Brand.builder().brandId(brandId).brandName("OldBrand").build();
        BrandUpdateCommand command = new BrandUpdateCommand("NewBrand");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.existsByBrandName("NewBrand")).thenReturn(false);
        when(brandRepository.save(any(Brand.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        BrandRegistrationResult result = adminService.updateBrand(brandId, command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.brandId()).isEqualTo(brandId);
        assertThat(result.brandName()).isEqualTo("NewBrand");
        assertThat(existingBrand.getBrandName()).isEqualTo("NewBrand");
    }

    @DisplayName("실패 케이스: 브랜드 미존재")
    @Test
    void updateBrand_brandNotFound_throwsException() {
        // given
        Long brandId = 30000L;
        BrandUpdateCommand command = new BrandUpdateCommand("NewBrand");
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // when then
        CustomNotFoundException ex = assertThrows(CustomNotFoundException.class,
                                                  () -> adminService.updateBrand(brandId, command));
        assertThat(ex.getMessage()).contains("브랜드를 찾을 수 없습니다. BrandId: " + brandId);
    }

    @DisplayName("실패 케이스: 중복된 브랜드명으로 업데이트 시도")
    @Test
    void updateBrand_duplicateName_throwsException() {
        // given
        Long brandId = 30000L;
        Brand existingBrand = Brand.builder().brandId(brandId).brandName("OldBrand").build();
        BrandUpdateCommand command = new BrandUpdateCommand("ExistingBrand");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.existsByBrandName("ExistingBrand")).thenReturn(true);

        // when then
        CustomBadRequestException ex = assertThrows(CustomBadRequestException.class,
                                                    () -> adminService.updateBrand(brandId, command));
        assertThat(ex.getMessage()).contains("이미 존재하는 브랜드 이름입니다.");
    }
}
