package shop.biday.service;

import shop.biday.model.domain.FaqModel;

import java.util.List;

public interface FaqService {

    List<FaqModel> findAll();

    FaqModel save(FaqModel questionModel);

    FaqModel findById(Long id);

    boolean deleteById(Long id);

    boolean existsById(Long id);
}
