package org.musinsa.stylist.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.musinsa.stylist.application.product.PricingService;
import org.musinsa.stylist.application.product.dto.CategoryPricingDetail;
import org.musinsa.stylist.application.product.dto.CategoryPricingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PricingServiceIntegrationTest {

    @Autowired
    private PricingService pricingService;

    @DisplayName("정상 케이스: getCategoryMinPrices() 호출 시 전체 카테고리별 최저가 상품 정보를 반환한다")
    @Test
    void getCategoryMinPrices_normalCase_returnsCorrectResult() {
        // given
        // when
        CategoryPricingResult result = pricingService.getCategoryMinPrices();

        // then
        assertNotNull(result);
        List<CategoryPricingDetail> details = result.details();
        assertFalse(details.isEmpty());

        int total = details.stream().mapToInt(CategoryPricingDetail::price).sum();
        assertEquals(total, result.totalAmount());

        assertThat(details)
            .extracting(CategoryPricingDetail::categoryName,
                        CategoryPricingDetail::brandName,
                        CategoryPricingDetail::price)
            .contains(
                tuple("상의", "C", 10000),
                tuple("아우터", "E", 5000),
                tuple("바지", "D", 3000),
                tuple("스니커즈", "A", 9000),
                tuple("가방", "A", 2000),
                tuple("모자", "D", 1500),
                tuple("양말", "I", 1700),
                tuple("액세서리", "F", 1900)
            );
    }
}
