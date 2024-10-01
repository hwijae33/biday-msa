package shop.biday.product.service;

import shop.biday.product.model.domain.BrandModel;
import shop.biday.product.model.entity.BrandEntity;

import java.util.List;

public interface BrandService {
    List<BrandModel> findAll();

    BrandModel findById(Long id);

    BrandEntity save(String role, BrandModel brand);

    BrandEntity update(String role, BrandModel brand);

    String deleteById(String role, Long id);
}
