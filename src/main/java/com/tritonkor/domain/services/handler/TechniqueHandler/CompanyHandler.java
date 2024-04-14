package com.tritonkor.domain.services.handler.TechniqueHandler;

import com.tritonkor.domain.services.dto.TechniqueAddDto;
import com.tritonkor.domain.services.handler.Handler;
import java.util.Objects;
import java.util.regex.Pattern;

public class CompanyHandler implements Handler<TechniqueAddDto> {
    private Handler nextHandler;

    @Override
    public void validate(TechniqueAddDto technique) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_.#$%^:№\\s\\p{P}]+$");

        if (Objects.isNull(technique.getCompany()) || technique.getCompany().isEmpty()) {
            technique.getValidationMessages()
                    .add(TechniqueValidationMessages.EMPTY.getTemplate().formatted("company"));
        } else if (technique.getCompany().length() > 50) {
            technique.getValidationMessages()
                    .add(TechniqueValidationMessages.MAX_LENGTH.getTemplate().formatted("company"));
        } else if (!pattern.matcher(technique.getCompany()).matches()) {
            technique.getValidationMessages()
                    .add(TechniqueValidationMessages.ONLY_LATIN.getTemplate().formatted("company"));
        }

        nextHandler.validate(technique);
    }

    @Override
    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }
}
