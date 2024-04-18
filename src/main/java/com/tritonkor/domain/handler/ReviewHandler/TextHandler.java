package com.tritonkor.domain.handler.ReviewHandler;

import com.tritonkor.domain.dto.ReviewAddDto;
import com.tritonkor.domain.handler.Handler;
import java.util.Objects;
import java.util.regex.Pattern;

public class TextHandler implements Handler<ReviewAddDto> {

    private Handler nextHandler;

    @Override
    public void validate(ReviewAddDto review) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_.#$%^:№\\s\\p{P}]+$");

        if (Objects.isNull(review.getText())) {
            review.getValidationMessages()
                    .add(ReviewValidationMessages.EMPTY.getTemplate().formatted("text"));
        } else if (review.getText().isEmpty()) {
            review.getValidationMessages()
                    .add(ReviewValidationMessages.EMPTY.getTemplate().formatted("text"));
        } else if (review.getText().length() > 256) {
            review.getValidationMessages().add(ReviewValidationMessages.TEXT_MAX_LENGTH.getTemplate().formatted("text"));
        } else if (!pattern.matcher(review.getText()).matches()) {
            review.getValidationMessages()
                    .add(ReviewValidationMessages.ONLY_LATIN.getTemplate().formatted("text"));
        }

        nextHandler.validate(review);
    }

    @Override
    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }
}
