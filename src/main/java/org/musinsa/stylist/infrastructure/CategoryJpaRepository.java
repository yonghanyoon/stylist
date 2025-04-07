package org.musinsa.stylist.infrastructure;

import java.util.Optional;
import org.musinsa.stylist.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);
}
