package org.musinsa.stylist.infrastructure;

import java.util.List;
import org.musinsa.stylist.domain.model.Category;
import org.musinsa.stylist.domain.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryRepositoryImpl(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }
}
