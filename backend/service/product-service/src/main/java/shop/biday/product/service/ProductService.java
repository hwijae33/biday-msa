package shop.biday.product.service;

import shop.biday.product.model.domain.ProductModel;
import shop.biday.product.model.dto.ProductDto;
import shop.biday.product.model.entity.ProductEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Map<Long, ProductModel> findAll();

    List<Map.Entry<Long, ProductModel>> findByProductId(Long id);

    List<ProductDto> findByFilter(Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId);

    ProductEntity save(String token, ProductModel product);

    ProductEntity update(String token, ProductModel product);

    String deleteById(String token, Long id);
}
