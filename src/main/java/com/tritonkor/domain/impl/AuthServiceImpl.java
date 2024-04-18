package com.tritonkor.domain.impl;

import com.tritonkor.domain.contract.AuthService;
import com.tritonkor.domain.exception.AuthException;
import com.tritonkor.domain.exception.ClientNotAuthException;
import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.impl.ClientDao;

public class AuthServiceImpl implements AuthService {

    private final ClientDao clientDao;
    private Client currentClient;

    /**
     * Constructs a new AuthServiceImpl with the specified ClientDao.
     *
     * @param clientDao the client list used for client-related operations.
     */
    public AuthServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username the username of the client.
     * @param password the password of the client.
     * @return true if authentication is successful.
     * @throws AuthException if authentication fails.
     */
    @Override
    public boolean authenticate(String username, String password) {
        Client foundedClient = clientDao.findOneByUsername(username)
                .orElseThrow(AuthException::new);

        if (!foundedClient.getPassword().equals(password))
            throw new AuthException();

        currentClient = foundedClient;
        return true;
    }

    /**
     * Checks if a client is currently authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    @Override
    public boolean isAuthenticated() {
        return currentClient != null;
    }

    /**
     * Gets the authenticated client.
     *
     * @return the authenticated client.
     */
    @Override
    public Client getClient() {
        return currentClient;
    }

    /**
     * Logs out the authenticated client.
     *
     * @throws ClientNotAuthException if the client is not authenticated.
     */
    @Override
    public void logout() {
        if (currentClient == null) {
            throw new ClientNotAuthException("You are not yet authenticated.");
        }
        currentClient = null;
    }
}
