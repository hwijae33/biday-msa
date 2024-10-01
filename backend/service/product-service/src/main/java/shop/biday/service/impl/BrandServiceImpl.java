package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;
import shop.biday.model.repository.BrandRepository;
import shop.biday.service.BrandService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<BrandModel> findAll() {
        log.info("Find all brands");
        return brandRepository.findAllBrand();
    }

    @Override
    public BrandModel findById(Long id) {
        log.info("Find brand by id: {}", id);
        return Optional.ofNullable(id)
                .filter(t -> {
                    boolean exists = brandRepository.existsById(id);
                    if (!exists) {
                        log.error("Not found brand with id: {}", id);
                    }
                    return exists;
                })
                .flatMap(brandRepository::findById)
                .map(brandEntity -> BrandModel.builder()
                        .id(brandEntity.getId())
                        .name(brandEntity.getName())
                        .createdAt(brandEntity.getCreatedAt())
                        .updatedAt(brandEntity.getUpdatedAt())
                        .build())
                .orElse(null);
    }

    @Override
    public BrandEntity save(String role, BrandModel brand) {
        log.info("Save Brand started with role: {}", role);
        return isAdmin(role)
                .map(t -> {
                    BrandEntity savedBrand = brandRepository.save(BrandEntity.builder()
                            .name(brand.getName())
                            .build());
                    log.info("Brand saved successfully: {}", savedBrand.getId());
                    return savedBrand;
                })
                .orElseThrow(() -> new RuntimeException("Save Brand failed: User does not have permission"));
    }

    @Override
    public BrandEntity update(String role, BrandModel brand) {
        log.info("Update Brand started for id: {}", brand.getId());
        return isAdmin(role)
                .filter(t -> {
                    boolean exists = brandRepository.existsById(brand.getId());
                    if (!exists) {
                        log.error("Not found brand with id: {}", brand.getId());
                    }
                    return exists;
                })
                .map(t -> {
                    BrandEntity updatedBrand = brandRepository.save(BrandEntity.builder()
                            .id(brand.getId())
                            .name(brand.getName())
                            .build());
                    log.info("Brand updated successfully: {}", updatedBrand.getId());
                    return updatedBrand;
                })
                .orElseThrow(() -> new RuntimeException("Update Brand failed: Brand not found or user does not have permission"));
    }

    @Override
    public String deleteById(String role, Long id) {
        log.info("Delete Brand started for id: {}", id);

        return isAdmin(role).map(t -> {
            if (!brandRepository.existsById(id)) {
                log.error("Not found brand with id: {}", id);
                return "브랜드 삭제 실패: 브랜드를 찾을 수 없습니다";
            }

            brandRepository.deleteById(id);
            log.info("Brand deleted successfully: {}", id);
            return "브랜드 삭제 성공";
        }).orElseGet(() -> {
            log.error("User does not have role ADMIN or does not exist");
            return "유효하지 않은 사용자: 관리자 권한이 필요합니다";
        });
    }

    private Optional<String> isAdmin(String role) {
        log.info("Validate User role: {}", role);
        return Optional.of(role)
                .filter(t -> t.equalsIgnoreCase("ROLE_ADMIN"))
                .or(() -> {
                    log.error("User does not have role ADMIN: {}", role);
                    return Optional.empty();
                });
    }
}
