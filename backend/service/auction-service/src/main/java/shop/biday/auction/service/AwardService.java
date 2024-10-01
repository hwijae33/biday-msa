package shop.biday.auction.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.auction.model.domain.AwardModel;
import shop.biday.auction.model.entity.AwardEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface AwardService {

    List<AwardEntity> findAll();

    AwardEntity findById(Long id);

    AwardEntity save(AwardEntity award);

    AwardModel findByAwardId(String userId, Long awardId);

    Slice<AwardModel> findByUser(String userId, String period, LocalDateTime cursor, Pageable pageable);
}
