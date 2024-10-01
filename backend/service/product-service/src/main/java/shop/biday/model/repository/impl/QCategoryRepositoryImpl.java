package shop.biday.product.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.biday.product.model.domain.CategoryModel;
import shop.biday.product.model.entity.QCategoryEntity;
import shop.biday.product.model.repository.QCategoryRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QCategoryRepositoryImpl implements QCategoryRepository {
    private final JPAQueryFactory queryFactory;

    private final QCategoryEntity qCategory = QCategoryEntity.categoryEntity;

    @Override
    public List<CategoryModel> findAllCategory() {
        return queryFactory
                .select(Projections.constructor(CategoryModel.class,
                        qCategory.id,
                        qCategory.name,
                        qCategory.createdAt,
                        qCategory.updatedAt))
                .from(qCategory)
                .orderBy(qCategory.id.asc())
                .fetch();
    }
}
