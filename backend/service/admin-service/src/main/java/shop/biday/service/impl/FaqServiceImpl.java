package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.FaqModel;
import shop.biday.model.entity.FaqEntity;
import shop.biday.model.repository.FaqRepository;
import shop.biday.service.FaqService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    public List<FaqModel> findAll() {
        return faqRepository.findAll()
                .stream()
                .map(FaqModel::of)
                .toList();
    }

    @Override
    public FaqModel save(FaqModel faqModel) {
        return FaqModel.of(faqRepository.save(FaqEntity.builder()
                .userId(faqModel.getUserId())
                .title(faqModel.getTitle())
                .content(faqModel.getContent())
                .build()));
    }

    @Override
    public FaqModel findById(Long id) {
        return FaqModel.of(faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다.")));
    }

    @Override
    public boolean deleteById(Long id) {
        if (!existsById(id)) {
            return false;
        }
        faqRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return faqRepository.existsById(id);
    }
}
