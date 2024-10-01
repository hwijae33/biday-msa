package shop.biday.model.repository;//package shop.biday.users.webflux.model.repository;
//
//
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import shop.biday.users.webflux.model.document.AddressDocument;
//
//
//import java.util.List;
//
//@Repository
//public interface MAddressRepository extends ReactiveMongoRepository<AddressDocument, Long> {
//    Mono<Long> countByUserId(String id);
//
//    Mono<AddressDocument> findByUserIdAndPick(String userId, Boolean pick);
//
//    Mono<AddressDocument> findByUserId(String id);
//
//    Flux<AddressDocument> findAllByUserId(String id);
//}