package org.musinsa.stylist.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
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
import org.musinsa.stylist.application.product.dto.BrandPriceResult;
import org.musinsa.stylist.application.product.dto.CategoryPriceRangeResult;
import org.musinsa.stylist.application.product.dto.CategoryPricingDetail;
import org.musinsa.stylist.application.product.dto.CategoryPricingResult;
import org.musinsa.stylist.application.product.dto.PriceRangeDetail;
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
        assertTrue(exception.getMessage().contains("해당 카테고리의 상품을 찾을 수 없습니다. category: " + category.getCategoryName()));
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
        assertTrue(exception.getMessage().contains("브랜드를 찾을 수 없습니다. BrandId: " + product.getBrandId()));
    }


    @DisplayName("정상 케이스: 모든 카테고리의 상품이 존재하는 브랜드 중 최저 총액 브랜드 반환")
    @Test
    void getLowestSingleBrandPrice_normalCase_returnsCorrectResult() {
        // given
        Category category1 = new Category(1L, "상의");
        Category category2 = new Category(2L, "아우터");

        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        Brand brand1 = new Brand(1L, "Brand1");
        Brand brand2 = new Brand(2L, "Brand2");

        List<Brand> brands = Arrays.asList(brand1, brand2);
        when(brandRepository.findAll()).thenReturn(brands);

        Product productA1 = new Product(1L, 1L, 1L, 10000);
        when(productRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(1L, 1L))
            .thenReturn(Optional.of(productA1));
        Product productA2 = new Product(2L, 1L, 2L, 5000);
        when(productRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(1L, 2L))
            .thenReturn(Optional.of(productA2));

        Product productB1 = new Product(3L, 2L, 1L, 12000);
        when(productRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(2L, 1L))
            .thenReturn(Optional.of(productB1));
        Product productB2 = new Product(4L, 2L, 2L, 4000);
        when(productRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(2L, 2L))
            .thenReturn(Optional.of(productB2));

        // when
        BrandPriceResult result = pricingService.getLowestSingleBrandPrice();

        // then
        assertNotNull(result);
        assertEquals("Brand1", result.brandName());
        assertEquals(15000, result.totalAmount());
        assertEquals(2, result.details().size());
    }

    @DisplayName("실패 케이스: 브랜드의 카테고리에 상품이 존재하지 않는 경우 CustomNotFoundException 발생")
    @Test
    void getLowestSingleBrandPrice_noBrandFullCoverage_throwsException() {
        // given
        Category category1 = new Category(1L, "상의");
        Category category2 = new Category(2L, "아우터");

        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        Brand brand1 = new Brand(1L, "Brand1");
        Brand brand2 = new Brand(2L, "Brand2");

        List<Brand> brands = Arrays.asList(brand1, brand2);
        when(brandRepository.findAll()).thenReturn(brands);

        Product productA1 = new Product(1L, 1L, 1L, 10000);
        when(productRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(1L, 1L))
            .thenReturn(Optional.of(productA1));
        when(productRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(1L, 2L))
            .thenReturn(Optional.empty());

        when(productRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(2L, 1L))
            .thenReturn(Optional.empty());
        when(productRepository.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(2L, 2L))
            .thenReturn(Optional.empty());

        // when then
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                                                         () -> pricingService.getLowestSingleBrandPrice());
        assertEquals("모든 카테고리에 상품을 제공하는 브랜드를 찾을 수 없습니다.", exception.getMessage());
    }

    @DisplayName("정상 케이스: 특정 카테고리의 최저, 최고 가격 정보를 반환한다")
    @Test
    void getCategoryPriceRange_normalCase_returnsCorrectResult() {
        // given
        String categoryName = "상의";
        Category category = new Category(1L, categoryName);
        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.of(category));

        Product product1 = new Product(1L, 1L, 1L, 10000);
        Product product2 = new Product(2L, 2L, 1L, 12000);
        Product product3 = new Product(3L, 3L, 1L, 8000);
        List<Product> products = Arrays.asList(product1, product2, product3);
        when(productRepository.findAllByCategoryId(1L)).thenReturn(products);

        Brand brand1 = new Brand(1L, "Brand1");
        Brand brand2 = new Brand(2L, "Brand2");
        Brand brand3 = new Brand(3L, "Brand3");

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand1));
        when(brandRepository.findById(2L)).thenReturn(Optional.of(brand2));
        when(brandRepository.findById(3L)).thenReturn(Optional.of(brand3));

        // when
        CategoryPriceRangeResult result = pricingService.getCategoryPriceRange(categoryName);

        // then
        assertThat(result).isNotNull();
        assertThat(result.category()).isEqualTo(categoryName);
        assertThat(result.minPrice())
            .extracting(PriceRangeDetail::brand, PriceRangeDetail::price)
            .containsExactlyInAnyOrder(tuple("Brand3", 8000));
        assertThat(result.maxPrice())
            .extracting(PriceRangeDetail::brand, PriceRangeDetail::price)
            .containsExactlyInAnyOrder(tuple("Brand2", 12000));
    }

    @DisplayName("실패 케이스: 해당 카테고리가 존재하지 않으면 CustomNotFoundException 발생")
    @Test
    void getCategoryPriceRange_categoryNotFound_throwsException() {
        // given
        String categoryName = "무신사";
        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.empty());

        // when then
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                                                         () -> pricingService.getCategoryPriceRange(categoryName));
        assertThat(exception.getMessage()).isEqualTo("카테고리를 찾을 수 없습니다. categoryName: " + categoryName);
    }

    @DisplayName("실패 케이스: 해당 카테고리에 상품이 없으면 CustomNotFoundException 발생")
    @Test
    void getCategoryPriceRange_noProducts_throwsException() {
        // given
        String categoryName = "상의";
        Category category = new Category(1L, categoryName);
        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.of(category));
        when(productRepository.findAllByCategoryId(1L)).thenReturn(List.of());

        // when then
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                                                         () -> pricingService.getCategoryPriceRange(categoryName));
        assertThat(exception.getMessage()).isEqualTo("해당 카테고리의 상품을 찾을 수 없습니다. category: " + categoryName);
    }
}
