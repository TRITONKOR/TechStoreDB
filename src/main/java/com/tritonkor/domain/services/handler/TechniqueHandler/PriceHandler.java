package com.tritonkor.domain.services.handler.TechniqueHandler;

import com.tritonkor.domain.services.dto.TechniqueAddDto;
import com.tritonkor.domain.services.handler.Handler;

public class PriceHandler implements Handler<TechniqueAddDto> {
    private Handler nextHandler;

    @Override
    public void validate(TechniqueAddDto technique) {
        if (technique.getPrice() <= 0.00) {
            technique.getValidationMessages()
                    .add(TechniqueValidationMessages.MIN_PRICE.getTemplate().formatted("price"));
        }

        nextHandler.validate(technique);
    }

    @Override
    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }
}
