package com.tritonkor.domain.handler.ReviewHandler;

import com.tritonkor.domain.dto.ReviewAddDto;
import com.tritonkor.domain.handler.Handler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class CreateTimeHandler implements Handler<ReviewAddDto> {

    private Handler nextHandler;

    @Override
    public void validate(ReviewAddDto review) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDate = LocalDateTime.parse(review.getCreatedAt().format(formatter),
                formatter);

        LocalDateTime currentDate = LocalDateTime.parse(
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(formatter), formatter);
        if (Objects.isNull(review.getCreatedAt())) {
            review.getValidationMessages()
                    .add(ReviewValidationMessages.EMPTY.getTemplate().formatted("create_time"));
        } else if (parsedDate.isAfter(currentDate)) {
            review.getValidationMessages()
                    .add(ReviewValidationMessages.DATE.getTemplate().formatted("create_time"));
        }
    }

    @Override
    public void setNextHandler(Handler handler) {
        this.nextHandler = handler;
    }
}
