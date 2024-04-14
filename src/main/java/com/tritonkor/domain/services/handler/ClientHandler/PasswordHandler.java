package com.tritonkor.domain.services.handler.ClientHandler;

import com.tritonkor.domain.services.dto.ClientAddDto;
import com.tritonkor.domain.services.handler.Handler;
import java.util.regex.Pattern;

public class PasswordHandler implements Handler<ClientAddDto> {

    private Handler nextHandler;

    @Override
    public void validate(ClientAddDto client) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_.#$%^:№\\s\\p{P}]+$");

        if (client.getPassword().equals(null)) {
            client.getValidationMessages()
                    .add(ClientValidationMessages.EMPTY.getTemplate().formatted("password"));
        } else if (client.getPassword().length() < 6) {
            client.getValidationMessages()
                    .add(ClientValidationMessages.MIN_LENGTH.getTemplate().formatted("password"));
        } else if (client.getPassword().length() > 32) {
            client.getValidationMessages()
                    .add(ClientValidationMessages.PASSWORD_MAX_LENGTH.getTemplate()
                            .formatted("password"));
        } else if (!pattern.matcher(client.getUsername()).matches()) {
            client.getValidationMessages()
                    .add(ClientValidationMessages.ONLY_LATIN.getTemplate().formatted("password"));
        }
    }

    @Override
    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }
}
