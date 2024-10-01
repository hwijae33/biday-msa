package shop.biday.auction.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.auction.model.domain.AuctionModel;
import shop.biday.auction.model.dto.AuctionDto;
import shop.biday.auction.model.entity.AuctionEntity;
import shop.biday.auction.model.repository.AuctionRepository;
import shop.biday.auction.scheduler.QuartzService;
import shop.biday.auction.service.AuctionService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final QuartzService quartzService;

    @Override
    public AuctionModel findById(Long id) {
        log.info("Find Auction by id: {}", id);
        return Optional.ofNullable(auctionRepository.findByAuctionId(id))
                .orElseGet(() -> {
                    log.warn("Auction not found for id: {}", id);
                    return null;
                });
    }

    @Override
    public boolean existsById(Long id) {
        log.info("Exists Auction by id: {}", id);
        return auctionRepository.existsById(id);
    }

    @Override
    public Slice<AuctionDto> findBySize(Long sizeId, String order, Long cursor, Pageable pageable) {
        log.info("Find All Auctions By Time: {} SizeId: {}", order, sizeId);
        return auctionRepository.findBySize(sizeId, order, cursor, pageable);
    }

    @Override
    public List<AuctionDto> findAllBySize(Long sizeId, String order) {
        log.info("Find All by Size: {} Order: {}", sizeId, order);
        return auctionRepository.findAllBySize(sizeId, order);
    }

    @Override
    public Slice<AuctionDto> findByUser(String role, String userId, String period, Long cursor, Pageable pageable) {
        log.info("Find All Auctions By User: {}", userId);
        return validateUser(role)
                .map(t -> auctionRepository.findByUser(userId, period, cursor, pageable))
                .orElseThrow(() -> new IllegalArgumentException("User does not exist or invalid role"));
    }

    @Override
    public AuctionEntity updateState(Long id) {
        log.info("Update Auction Status by id: {}", id);
        return auctionRepository.findById(id).map(auction -> {
            auction.setStatus(true);
            return auctionRepository.save(auction);
        }).orElseThrow(() -> new NoSuchElementException("Auction not found with id: " + id));
    }

    @Override
    public AuctionEntity save(String role, String userId, AuctionModel auction) {
        log.info("Save Auction started");
        AuctionEntity auctionEntity = validateUser(role)
                .map(t -> auctionRepository.save(AuctionEntity.builder()
                        .userId(userId)
                        .sizeId(auction.getSize())
                        .description(auction.getDescription())
                        .startingBid(auction.getStartingBid())
                        .currentBid(auction.getCurrentBid())
                        .startedAt(auction.getStartedAt())
                        .endedAt(auction.getEndedAt())
                        .build()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid role or not a seller"));

        quartzService.createJob(auctionEntity.getId(), auctionEntity.getEndedAt());
        return auctionEntity;
    }

    @Override
    public AuctionEntity update(String role, String userId, AuctionModel auction) {
        log.info("Update Auction started for id: {}", auction.getId());
        return validateUser(role)
                .map(t -> {
                    Long auctionId = auction.getId();
                    String auctionUserId = auctionRepository.findById(auctionId)
                            .orElseThrow(() -> new IllegalArgumentException("Auction not found"))
                            .getUserId();

                    if (!auctionUserId.equals(userId)) {
                        log.error("User with ID {} does not have Update Authority for auction id: {}", userId, auctionId);
                        throw new SecurityException("User does not have permission to update this auction.");
                    } else {
                        AuctionEntity auctionEntity = auctionRepository.save(AuctionEntity.builder()
                                .userId(auction.getUser())
                                .sizeId(auction.getSize())
                                .description(auction.getDescription())
                                .startingBid(auction.getStartingBid())
                                .currentBid(auction.getCurrentBid())
                                .startedAt(auction.getStartedAt())
                                .endedAt(auction.getEndedAt())
                                .build());
                        log.debug("Update Auction By User for id: {}", auctionId);
                        return auctionEntity;
                    }
                })
                .orElseThrow(() -> {
                    log.error("User does not have role SELLER or does not exist");
                    return new IllegalArgumentException("Invalid role or user does not exist.");
                });
    }

    @Override
    public String deleteById(String role, String userId, Long id) {
        log.info("Delete Auction started for id: {}", id);
        return validateUser(role)
                .map(t -> {
                    String auctionUserId = auctionRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Auction not found"))
                            .getUserId();

                    if (!auctionUserId.equals(userId)) {
                        log.error("User with ID {} does not have Delete Authority for auction id: {}", userId, id);
                        return "삭제 권한이 없습니다";
                    } else {
                        auctionRepository.deleteById(id);
                        log.debug("Delete Auction By User for id: {}", id);
                        return "경매 삭제 성공";
                    }
                })
                .orElseGet(() -> {
                    log.error("User does not have role SELLER or does not exist");
                    return "유효하지 않은 사용자";
                });
    }

    private Optional<String> validateUser(String role) {
        log.info("Validating user role: {}", role);
        return Optional.ofNullable(role)
                .filter(t -> t.equalsIgnoreCase("ROLE_SELLER"))
                .or(() -> {
                    log.error("User does not have role SELLER: {}", role);
                    return Optional.empty();
                });
    }
}
