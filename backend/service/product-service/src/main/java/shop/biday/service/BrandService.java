package shop.biday.service;

import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;

import java.util.List;

public interface BrandService {
    List<BrandModel> findAll();

    BrandModel findById(Long id);

    BrandEntity save(String role, BrandModel brand);

    BrandEntity update(String role, BrandModel brand);

    String deleteById(String role, Long id);
}
