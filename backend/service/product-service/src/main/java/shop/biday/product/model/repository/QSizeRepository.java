package shop.biday.product.model.repository;

import shop.biday.product.model.domain.SizeModel;

import java.util.List;

public interface QSizeRepository {
    List<SizeModel> findAllByProductId(Long productId);
}
