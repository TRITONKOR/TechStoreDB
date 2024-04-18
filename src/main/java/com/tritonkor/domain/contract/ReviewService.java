package com.tritonkor.domain.contract;

import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import java.util.List;

public interface ReviewService {

    List<Review> findByOwner(Client client);
    List<Review> findByTechnique(Technique technique);

}
