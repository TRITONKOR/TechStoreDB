package com.tritonkor.domain.services.handler.ReviewHandler;

import com.tritonkor.domain.services.dto.ReviewAddDto;
import com.tritonkor.domain.services.handler.Handler;
import java.util.Objects;

public class GradeHandler implements Handler<ReviewAddDto> {

    private Handler nextHandler;

    @Override
    public void validate(ReviewAddDto review) {
        if (Objects.isNull(review.getGrade())) {
            review.getValidationMessages().add(ReviewValidationMessages.EMPTY.getTemplate().formatted("grade"));
        }

        nextHandler.validate(review);
    }

    @Override
    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }
}
