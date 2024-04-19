package com.tritonkor.domain.impl;

import com.tritonkor.domain.contract.ClientService;
import com.tritonkor.domain.dto.ClientAddDto;
import com.tritonkor.domain.exception.ValidationException;
import com.tritonkor.domain.exception.SignUpException;
import com.tritonkor.domain.handler.ClientHandler.UsernameHandler;
import com.tritonkor.domain.handler.HandlerFactory;
import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.exception.persistence.EntityNotFoundException;
import com.tritonkor.persistence.impl.ClientDao;

public class ClientServiceImpl extends GenericService<Client> implements ClientService {

    private ClientDao clientDao;
    private UsernameHandler usernameHandler;

    public ClientServiceImpl(ClientDao clientDao) {
        super(clientDao);
        this.clientDao = clientDao;
        this.usernameHandler = HandlerFactory.getInstance().getUsernameHandler();
    }

    @Override
    public Client findOneByUsername(String username) {
        return clientDao.findOneByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("This client does not exist."));
    }

    @Override
    public Client save(ClientAddDto clientAddDto) {
        usernameHandler.validate(clientAddDto);
        if (!clientAddDto.getValidationMessages().isEmpty()) {
            System.out.println(clientAddDto.getValidationMessages());
            throw new ValidationException(clientAddDto.getValidationMessages());
        }

        try {
            var client = Client.builder().username(clientAddDto.getUsername()).password(
                    clientAddDto.getPassword()).build();
            clientDao.save(client);
            return client;
        } catch (RuntimeException e) {
            throw new SignUpException("Error when saving client: %s"
                    .formatted(e.getMessage()));
        }
    }
}
