package com.tritonkor.domain.contract;

import com.tritonkor.domain.dto.ClientAddDto;
import com.tritonkor.persistence.entity.Client;

public interface ClientService {

    Client findOneByUsername(String username);

    Client save(ClientAddDto clientAddDto);
}
