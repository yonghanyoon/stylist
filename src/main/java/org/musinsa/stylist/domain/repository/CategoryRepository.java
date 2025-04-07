package org.musinsa.stylist.domain.repository;

import java.util.List;
import org.musinsa.stylist.domain.model.Category;

public interface CategoryRepository {

    List<Category> findAll();
}
