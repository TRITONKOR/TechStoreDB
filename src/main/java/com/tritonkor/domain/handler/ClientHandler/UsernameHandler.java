package com.tritonkor.domain.handler.ClientHandler;

import com.tritonkor.domain.dto.ClientAddDto;
import com.tritonkor.domain.handler.Handler;
import java.util.regex.Pattern;

public class UsernameHandler implements Handler<ClientAddDto> {

    private Handler nextHandler;

    @Override
    public void validate(ClientAddDto client) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_.#$%^:№\\s\\p{P}]+$");

        if (client.getUsername().equals(null)) {
            client.getValidationMessages()
                    .add(ClientValidationMessages.EMPTY.getTemplate().formatted("username"));
        } else if (client.getUsername().length() < 6) {
            client.getValidationMessages()
                    .add(ClientValidationMessages.MIN_LENGTH.getTemplate().formatted("username"));
        } else if (client.getUsername().length() > 16) {
            client.getValidationMessages()
                    .add(ClientValidationMessages.USERNAME_MAX_LENGTH.getTemplate()
                            .formatted("username"));
        } else if (!pattern.matcher(client.getUsername()).matches()) {
            client.getValidationMessages()
                    .add(ClientValidationMessages.ONLY_LATIN.getTemplate().formatted("username"));
        }

        nextHandler.validate(client);
    }

    @Override
    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }
}
