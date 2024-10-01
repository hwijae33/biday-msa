package shop.biday.product.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.product.model.domain.CategoryModel;
import shop.biday.product.model.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, QCategoryRepository {
    List<CategoryEntity> findAll();

    Optional<CategoryEntity> findById(Long id);

    CategoryEntity findByName(String name);

    boolean existsById(Long id);

    CategoryEntity save(CategoryModel category);

    void deleteById(Long id);
}
