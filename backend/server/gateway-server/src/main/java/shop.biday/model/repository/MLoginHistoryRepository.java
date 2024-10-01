package shop.biday.model.repository;//package shop.biday.users.webflux.model.repository;
//
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Mono;
//import shop.biday.users.webflux.model.document.LoginHistoryDocument;
//import java.time.LocalDateTime;
//
//@Repository
//public interface MLoginHistoryRepository extends ReactiveMongoRepository<LoginHistoryDocument, Long> {
//    Mono<LoginHistoryDocument> findFirstByUserIdAndCreatedAtBetween(String userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
//}
