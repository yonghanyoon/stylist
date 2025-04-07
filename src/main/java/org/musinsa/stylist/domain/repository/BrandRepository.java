package org.musinsa.stylist.domain.repository;

import java.util.List;
import java.util.Optional;
import org.musinsa.stylist.domain.model.Brand;

public interface BrandRepository {

    Optional<Brand> findById(Long brandId);
    List<Brand> findAll();
    Brand save(Brand brand);
    boolean existsByBrandName(String brandName);
    boolean existsById(Long brandId);
    void deleteById(Long brandId);
}
