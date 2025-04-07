package org.musinsa.stylist.infrastructure;

import java.util.List;
import java.util.Optional;
import org.musinsa.stylist.domain.model.Brand;
import org.musinsa.stylist.domain.repository.BrandRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandJpaRepository brandJpaRepository;

    public BrandRepositoryImpl(BrandJpaRepository brandJpaRepository) {
        this.brandJpaRepository = brandJpaRepository;
    }

    @Override
    public Optional<Brand> findById(Long brandId) {
        return brandJpaRepository.findById(brandId);
    }

    @Override
    public List<Brand> findAll() {
        return brandJpaRepository.findAll();
    }

    @Override
    public Brand save(Brand brand) {
        return brandJpaRepository.save(brand);
    }

    @Override
    public boolean existsByBrandName(String brandName) {
        return brandJpaRepository.existsByBrandName(brandName);
    }

    @Override
    public boolean existsById(Long brandId) {
        return brandJpaRepository.existsById(brandId);
    }

    @Override
    public void deleteById(Long brandId) {
        brandJpaRepository.deleteById(brandId);
    }
}
