package org.musinsa.stylist.infrastructure;

import org.musinsa.stylist.domain.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<Brand, Long> {

}
