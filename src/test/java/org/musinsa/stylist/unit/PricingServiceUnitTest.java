package org.musinsa.stylist.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.musinsa.stylist.application.product.PricingService;
import org.musinsa.stylist.application.product.dto.CategoryPricingDetail;
import org.musinsa.stylist.application.product.dto.CategoryPricingResult;
import org.musinsa.stylist.common.exception.list.CustomNotFoundException;
import org.musinsa.stylist.domain.model.Brand;
import org.musinsa.stylist.domain.model.Category;
import org.musinsa.stylist.domain.model.Product;
import org.musinsa.stylist.domain.repository.BrandRepository;
import org.musinsa.stylist.domain.repository.CategoryRepository;
import org.musinsa.stylist.domain.repository.ProductRepository;

public class PricingServiceUnitTest {

    private ProductRepository productRepository;
    private BrandRepository brandRepository;
    private CategoryRepository categoryRepository;
    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        brandRepository = Mockito.mock(BrandRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        pricingService = new PricingService(productRepository, brandRepository, categoryRepository);
    }

    @DisplayName("정상 케이스: 카테고리별 최저가 조회 시 올바른 결과 반환")
    @Test
    void getCategoryMinPrices_normalCase_returnsResult() {
        // given
        Category category1 = new Category(1L, "상의");
        Category category2 = new Category(2L, "아우터");
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        Product product1 = new Product(1L, 1L, 1L, 10000);
        Product product2 = new Product(2L, 2L, 2L, 5000);
        when(productRepository.findFirstByCategoryIdOrderByPriceAsc(1L))
            .thenReturn(Optional.of(product1));
        when(productRepository.findFirstByCategoryIdOrderByPriceAsc(2L))
            .thenReturn(Optional.of(product2));

        Brand brand1 = new Brand(1L, "Brand1");
        Brand brand2 = new Brand(2L, "Brand2");
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand1));
        when(brandRepository.findById(2L)).thenReturn(Optional.of(brand2));

        // when
        CategoryPricingResult result = pricingService.getCategoryMinPrices();

        // then
        assertNotNull(result);

        List<CategoryPricingDetail> details = result.details();
        assertEquals(2, details.size());
        assertEquals(15000, result.totalAmount());

        CategoryPricingDetail detail1 = details.get(0);
        CategoryPricingDetail detail2 = details.get(1);

        assertEquals("상의", detail1.categoryName());
        assertEquals("Brand1", detail1.brandName());
        assertEquals(10000, detail1.price());

        assertEquals("아우터", detail2.categoryName());
        assertEquals("Brand2", detail2.brandName());
        assertEquals(5000, detail2.price());
    }

    @DisplayName("실패 케이스: 상품이 없는 경우 CustomNotFoundException 발생")
    @Test
    void getCategoryMinPrices_noProduct_throwsException() {
        // given
        Category category = new Category(1L, "상의");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(productRepository.findFirstByCategoryIdOrderByPriceAsc(1L))
            .thenReturn(Optional.empty());

        // when then
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                                                         () -> pricingService.getCategoryMinPrices());
        assertTrue(exception.getMessage().contains("No product found for category: 상의"));
    }

    @DisplayName("실패 케이스: 브랜드가 없는 경우 CustomNotFoundException 발생")
    @Test
    void getCategoryMinPrices_noBrand_throwsException() {
        // given
        Category category = new Category(1L, "상의");
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        Product product = new Product(1L, 1L, 1L, 10000);
        when(productRepository.findFirstByCategoryIdOrderByPriceAsc(1L))
            .thenReturn(Optional.of(product));

        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        // when then
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                                                         () -> pricingService.getCategoryMinPrices());
        assertTrue(exception.getMessage().contains("Brand not found with id: 1"));
    }
}
