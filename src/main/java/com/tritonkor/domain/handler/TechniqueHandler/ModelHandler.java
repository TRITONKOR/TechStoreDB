package com.tritonkor.domain.handler.TechniqueHandler;

import com.tritonkor.domain.dto.TechniqueAddDto;
import com.tritonkor.domain.handler.Handler;
import java.util.Objects;
import java.util.regex.Pattern;

public class ModelHandler implements Handler<TechniqueAddDto> {
    private Handler nextHandler;

    @Override
    public void validate(TechniqueAddDto technique) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_.#$%^:№\\s\\p{P}]+$");

        if (Objects.isNull(technique.getModel()) || technique.getModel().isEmpty()) {
            technique.getValidationMessages()
                    .add(TechniqueValidationMessages.EMPTY.getTemplate().formatted("model"));
        } else if (technique.getModel().length() > 50) {
            technique.getValidationMessages()
                    .add(TechniqueValidationMessages.MAX_LENGTH.getTemplate().formatted("model"));
        } else if (!pattern.matcher(technique.getCompany()).matches()) {
            technique.getValidationMessages()
                    .add(TechniqueValidationMessages.ONLY_LATIN.getTemplate().formatted("model"));
        }
    }

    @Override
    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }
}
