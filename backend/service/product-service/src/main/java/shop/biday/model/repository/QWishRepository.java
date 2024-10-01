package shop.biday.product.model.repository;

import shop.biday.product.model.entity.WishEntity;

import java.util.List;

public interface QWishRepository {

    void deleteWish(String userId, Long productId);

    WishEntity findByEmailAndProductId(String userId, Long productId);

    List<?> findByUserId(String userId);

}
