package shop.biday.product.model.repository;

import shop.biday.product.model.domain.CategoryModel;

import java.util.List;

public interface QCategoryRepository {
    List<CategoryModel> findAllCategory();
}
