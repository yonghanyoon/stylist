package org.musinsa.stylist.infrastructure;

import org.musinsa.stylist.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

}
