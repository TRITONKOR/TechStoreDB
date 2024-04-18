package com.tritonkor.domain.impl;

import com.tritonkor.domain.contract.TechniqueService;
import com.tritonkor.persistence.Dao;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.exception.persistence.EntityNotFoundException;
import com.tritonkor.persistence.impl.TechniqueDao;
import java.util.List;

public class TechniqueServiceImpl extends GenericService<Technique> implements TechniqueService {

    private TechniqueDao techniqueDao;

    public TechniqueServiceImpl(TechniqueDao techniqueDao) {
        super(techniqueDao);
        this.techniqueDao = techniqueDao;
    }

    @Override
    public List<Technique> findByCompany(String company) {
        return techniqueDao.findByCompany(company);
    }

    @Override
    public Technique findOneByModel(String model) {
        return techniqueDao.findOneByModel(model).orElseThrow(() -> new EntityNotFoundException("This model does not exist."));
    }

    @Override
    public List<Review> findAllReviews(Technique technique) {
        return techniqueDao.findAllReviews(technique.getId());
    }
}
