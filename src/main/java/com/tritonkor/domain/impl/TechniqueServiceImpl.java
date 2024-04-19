package com.tritonkor.domain.impl;

import com.tritonkor.domain.contract.TechniqueService;
import com.tritonkor.domain.dto.TechniqueAddDto;
import com.tritonkor.domain.exception.ValidationException;
import com.tritonkor.domain.handler.HandlerFactory;
import com.tritonkor.domain.handler.TechniqueHandler.PriceHandler;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.exception.persistence.EntityNotFoundException;
import com.tritonkor.persistence.impl.TechniqueDao;
import java.util.List;

public class TechniqueServiceImpl extends GenericService<Technique> implements TechniqueService {

    private TechniqueDao techniqueDao;
    private PriceHandler priceHandler;

    public TechniqueServiceImpl(TechniqueDao techniqueDao) {
        super(techniqueDao);
        this.techniqueDao = techniqueDao;

        this.priceHandler = HandlerFactory.getInstance().getPriceHandler();
    }

    @Override
    public List<Technique> findByCompany(String company) {
        return techniqueDao.findByCompany(company);
    }

    @Override
    public Technique findOneByModel(String model) {
        return techniqueDao.findOneByModel(model)
                .orElseThrow(() -> new EntityNotFoundException("This model does not exist."));
    }

    @Override
    public List<Review> findAllReviews(Technique technique) {
        return techniqueDao.findAllReviews(technique.getId());
    }

    @Override
    public Technique save(TechniqueAddDto techniqueAddDto) {
        priceHandler.validate(techniqueAddDto);
        if (!techniqueAddDto.getValidationMessages().isEmpty()) {
            System.out.println(techniqueAddDto.getValidationMessages());
            throw new ValidationException(techniqueAddDto.getValidationMessages());
        }

        try {
            var technique = Technique.builder().price(techniqueAddDto.getPrice())
                    .company(techniqueAddDto.getCompany()).model(techniqueAddDto.getModel())
                    .build();
            techniqueDao.save(technique);
            return technique;
        } catch (RuntimeException e) {
            return null;
            //TODO write exception
        }
    }
}
