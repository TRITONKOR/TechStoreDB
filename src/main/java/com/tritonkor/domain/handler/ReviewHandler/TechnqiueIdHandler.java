package com.tritonkor.domain.handler.ReviewHandler;

import com.tritonkor.domain.dto.ReviewAddDto;
import com.tritonkor.domain.handler.Handler;
import java.util.Objects;

public class TechnqiueIdHandler implements Handler<ReviewAddDto> {

    private Handler nextHandler;

    @Override
    public void validate(ReviewAddDto review) {
        if (Objects.isNull(review.getTechnique())) {
            review.getValidationMessages().add(ReviewValidationMessages.EMPTY.getTemplate().formatted("technique_id"));
        }

        nextHandler.validate(review);
    }

    @Override
    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }
}
