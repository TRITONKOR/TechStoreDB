package com.tritonkor.domain.contract;

import com.tritonkor.domain.dto.TechniqueAddDto;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import java.util.List;

public interface TechniqueService {

    List<Technique> findByCompany(String company);
    Technique findOneByModel(String model);

    List<Review> findAllReviews(Technique technique);

    Technique save(TechniqueAddDto techniqueAddDto);
}
