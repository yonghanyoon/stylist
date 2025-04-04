package org.musinsa.stylist.infrastructure;

import org.musinsa.stylist.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

}
