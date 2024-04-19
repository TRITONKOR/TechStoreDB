package com.tritonkor.domain.impl;


import com.tritonkor.domain.contract.ClientService;
import com.tritonkor.domain.dto.ClientAddDto;
import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.impl.ClientDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;
    @Mock
    private ClientDao clientDao;
    @Test
    void saveClientTest() {
        ClientAddDto clientAddDto = new ClientAddDto(1, "sasha", "password");
        Client client = Client.builder().id(1).username("sasha").password("password").build();

        Mockito.when(clientDao.save(client)).thenReturn()
    }
}
