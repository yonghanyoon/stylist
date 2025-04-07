package org.musinsa.stylist.domain.repository;

import java.util.List;
import java.util.Optional;
import org.musinsa.stylist.domain.model.Brand;

public interface BrandRepository {

    Optional<Brand> findById(Long brandId);
    List<Brand> findAll();
}
