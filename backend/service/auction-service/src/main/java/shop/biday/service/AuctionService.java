package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;

import java.util.List;

public interface AuctionService {
    AuctionModel findById(Long id);

    Slice<AuctionDto> findBySize(Long sizeId, String order, Long cursor, Pageable pageable);

    List<AuctionDto> findAllBySize(Long sizeId, String order);

    Slice<AuctionDto> findByUser(String role, String userId, String period, Long cursor, Pageable pageable);

    AuctionEntity updateState(Long id);

    boolean existsById(Long id);

    AuctionEntity save(String role, String userId, AuctionModel auction);

    AuctionEntity update(String role, String userId, AuctionModel auction);

    String deleteById(String role, String userId, Long id);
}
