package com.tritonkor.domain.impl;

import com.tritonkor.domain.contract.ReviewService;
import com.tritonkor.domain.dto.ReviewAddDto;
import com.tritonkor.domain.exception.ValidationException;
import com.tritonkor.domain.handler.HandlerFactory;
import com.tritonkor.domain.handler.ReviewHandler.OwnerIdHandler;
import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.impl.ReviewDao;
import java.util.List;

public class ReviewServiceImpl extends GenericService<Review> implements ReviewService {

    private final ReviewDao reviewDao;
    private OwnerIdHandler ownerIdHandler;

    public ReviewServiceImpl(ReviewDao reviewDao) {
        super(reviewDao);
        this.reviewDao = reviewDao;
        this.ownerIdHandler = HandlerFactory.getInstance().getOwnerIdHandler();
    }

    @Override
    public List<Review> findByOwner(Client client) {
        return reviewDao.findByOwner(client);
    }

    @Override
    public List<Review> findByTechnique(Technique technique) {
        return reviewDao.findByTechnique(technique);
    }

    @Override
    public Review save(ReviewAddDto reviewAddDto) {
        ownerIdHandler.validate(reviewAddDto);
        if (!reviewAddDto.getValidationMessages().isEmpty()) {
            System.out.println(reviewAddDto.getValidationMessages());
            throw new ValidationException(reviewAddDto.getValidationMessages());
        }

        try {
            var review = Review.builder().owner(reviewAddDto.getOwner())
                    .technique(reviewAddDto.getTechnique()).text(reviewAddDto.getText())
                    .grade(reviewAddDto.getGrade().getGrade())
                    .createdAt(reviewAddDto.getCreatedAt()).build();
            reviewDao.save(review);
            return review;
        } catch (RuntimeException e) {
            return null;
            //TODO write exception
        }
    }
}
