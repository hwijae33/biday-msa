package shop.biday.product.service;

import shop.biday.product.model.domain.CategoryModel;
import shop.biday.product.model.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> findAll();

    CategoryModel findById(Long id);

    CategoryEntity save(String role, CategoryModel category);

    CategoryEntity update(String role, CategoryModel category);

    String deleteById(String role, Long id);
}
