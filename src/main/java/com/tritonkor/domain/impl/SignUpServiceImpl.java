package com.tritonkor.domain.impl;

import com.tritonkor.domain.contract.SignUpService;
import com.tritonkor.domain.dto.ClientAddDto;

public class SignUpServiceImpl implements SignUpService {

    private final ClientServiceImpl clientService;

    public SignUpServiceImpl(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @Override
    public void signUp(ClientAddDto clientAddDto) {
        clientService.save(clientAddDto);
    }
}
