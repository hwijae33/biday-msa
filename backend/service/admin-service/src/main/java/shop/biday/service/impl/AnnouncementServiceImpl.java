package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AnnouncementModel;
import shop.biday.model.entity.AnnouncementEntity;
import shop.biday.model.repository.AnnouncementRepository;
import shop.biday.service.AnnouncementService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Override
    public List<AnnouncementModel> findAll() {
        return announcementRepository.findAll()
                .stream()
                .map(AnnouncementModel::of)
                .toList();
    }

    @Override
    public AnnouncementModel save(AnnouncementModel announcementModel) {
        return AnnouncementModel.of(announcementRepository.save(
                AnnouncementEntity.builder()
                        .userId(announcementModel.getUserId())
                        .title(announcementModel.getTitle())
                        .content(announcementModel.getContent())
                        .build()));
    }

    @Override
    public AnnouncementModel findById(Long id) {
        return AnnouncementModel.of(announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다.")));
    }

    @Override
    public boolean deleteById(Long id) {
        if (!existsById(id)) {
            return false;
        }
        announcementRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return announcementRepository.existsById(id);
    }
}
