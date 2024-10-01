package shop.biday.auction.service;

import reactor.core.publisher.Mono;
import shop.biday.auction.model.document.BidDocument;
import shop.biday.auction.model.domain.BidModel;
import shop.biday.auction.model.dto.BidResponse;

public interface BidService {

    Mono<BidResponse> save(BidModel bid);

    Mono<BidDocument> findTopBidByAuctionId(Long auctionId);

    Mono<Boolean> updateAward(Long auctionId);

    Mono<Long> countBidByAuctionIdAndUserId(Long auctionId, String userId);
}
