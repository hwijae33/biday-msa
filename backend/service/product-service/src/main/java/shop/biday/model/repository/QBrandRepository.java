package shop.biday.product.model.repository;

import shop.biday.product.model.domain.BrandModel;

import java.util.List;

public interface QBrandRepository {
    List<BrandModel> findAllBrand();
}
