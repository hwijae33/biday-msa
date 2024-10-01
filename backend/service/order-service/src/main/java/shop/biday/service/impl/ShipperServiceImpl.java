package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.entity.PaymentEntity;
import shop.biday.model.entity.ShipperEntity;
import shop.biday.model.repository.ShipperRepository;
import shop.biday.service.PaymentService;
import shop.biday.service.ShipperService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipperServiceImpl implements ShipperService {

    // TODO: validate 메소드 전체 삭제
    private final PaymentService paymentService;
    private final ShipperRepository shipperRepository;
//    private final JWTUtil jwtUtil;
//    private final UserRepository userRepository;

    @Override
    public List<ShipperModel> findAll() {
        log.info("Find all shippers");
        return shipperRepository.findAll()
                .stream()
                .map(ShipperModel::of)
                .toList();
    }

    @Override
    public ShipperModel findById(Long id) {
        log.info("Find shipper by id: {}", id);
        return ShipperModel.of(shipperRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다.")));
    }

    @Override
    public ShipperEntity save(String token, ShipperModel shipper) {
        log.info("Save shipper started");
        PaymentEntity payment = paymentService.findById(shipper.getPaymentId());

        return validateUser(token)
                .map(t -> shipperRepository.save(ShipperEntity.builder()
                        .payment(payment)
                        .carrier(shipper.getCarrier())
                        .trackingNumber(shipper.getTrackingNumber())
                        .shipmentDate(shipper.getShipmentDate())
                        .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
                        .deliveryAddress(shipper.getDeliveryAddress())
                        .status("준비중")
                        .deliveryAddress(shipper.getDeliveryAddress())
                        .createdAt(LocalDateTime.now())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Save shipper failed"));
    }

    @Override
    public ShipperEntity update(String token, ShipperModel shipper) {
        log.info("Update shipper started");
        PaymentEntity payment = paymentService.findById(shipper.getPaymentId());

        return validateUser(token)
                .filter(t -> {
                    boolean exists = shipperRepository.existsById(shipper.getId());
                    if (!exists) {
                        log.error("Not found shipper: {}", shipper.getId());
                    }
                    return exists;
                })
                .map(t -> shipperRepository.save(ShipperEntity.builder()
                        .id(shipper.getId())
                        .payment(payment)
                        .carrier(shipper.getCarrier())
                        .trackingNumber(shipper.getTrackingNumber())
                        .shipmentDate(shipper.getShipmentDate())
                        .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
                        .deliveryAddress(shipper.getDeliveryAddress())
                        .status(shipper.getStatus())
                        .deliveryAddress(shipper.getDeliveryAddress())
                        .createdAt(shipper.getCreatedAt())
                        .updatedAt(LocalDateTime.now())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Update shipper failed: shipper not found"));
    }

    @Override
    public String deleteById(String token, Long id) {
        log.info("Delete shipper started for id: {}", id);

        return validateUser(token).map(t -> {
            if (!shipperRepository.existsById(id)) {
                log.error("Not found shipper: {}", id);
                return "배송지 삭제 실패";
            }

            shipperRepository.deleteById(id);
            log.info("Shipper deleted: {}", id);
            return "배송지 삭제 성공";
        }).orElseGet(() -> {
            log.error("User does not have role SELLER or does not exist");
            return "유효하지 않은 사용자";
        });
    }


    private Optional<String> validateUser(String token) {
        log.info("Validate User started");
//        return Optional.of(token)
//                .filter(t -> jwtUtil.getRole(t).equalsIgnoreCase("ROLE_SELLER"))
//                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
//                .or(() -> {
//                    log.error("User does not have role SELLER or does not exist");
//                    return Optional.empty();
//                });
        return null;
    }
}
