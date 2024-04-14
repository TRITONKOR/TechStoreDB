package com.tritonkor.persistence.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tritonkor.persistence.filter.ClientFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.util.DbInitialization;

import java.util.List;

public class ClientDaoTest {

    ClientDao clientDao;

    @BeforeEach
    void setup() {
        DbInitialization.apply();

        clientDao = ClientDao.getInstance();
    }

    @Test
    void testGetClients() {
        List<Client> clients = clientDao.findAll();
        assertInstanceOf(List.class, clients,
                "Помилка: метод findAll повинен повертати List<>");
        assertFalse(clients.isEmpty(),
                "Помилка: повернений список не може бути пустим");
    }

    @Test
    void testAddClient() {
        Client clientToAdd = Client.builder().username("test").password("test").build();

        clientDao.save(clientToAdd);

        List<Client> clients = clientDao.findAll();

        assertTrue(clients.contains(clientToAdd), "Помилка: список не містить доданого клієнта");
    }

    @Test
    void testUpdateClient() {
        Client client = clientDao.findOneById(1).orElseThrow();

        client.setPassword("newPassword");

        clientDao.update(client);

        List<Client >clients = clientDao.findAll();
        assertTrue(clients.contains(client), "Помилка: клієнт не обновився");
    }
}
