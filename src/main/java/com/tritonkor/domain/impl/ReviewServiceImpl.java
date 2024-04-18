package com.tritonkor.domain.impl;

import com.tritonkor.domain.contract.ReviewService;
import com.tritonkor.persistence.Dao;
import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.impl.ReviewDao;
import java.util.List;

public class ReviewServiceImpl extends GenericService<Review> implements ReviewService {

    private ReviewDao reviewDao;

    public ReviewServiceImpl(ReviewDao reviewDao) {
        super(reviewDao);
        this.reviewDao = reviewDao;
    }

    @Override
    public List<Review> findByOwner(Client client) {
        return reviewDao.findByOwner(client);
    }

    @Override
    public List<Review> findByTechnique(Technique technique) {
        return reviewDao.findByTechnique(technique);
    }
}
