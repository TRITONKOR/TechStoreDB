package com.tritonkor.domain.contract;

import com.tritonkor.persistence.entity.Client;

public interface AuthService{

    /**
     * Authenticates a client based on the provided username and password.
     *
     * @param username The username of the client attempting to authenticate.
     * @param password The password of the client attempting to authenticate.
     * @return true if the authentication is successful, false otherwise.
     */
    boolean authenticate(String username, String password);

    /**
     * Checks if a user is currently authenticated.
     *
     * @return true if a user is authenticated, false otherwise.
     */
    boolean isAuthenticated();

    /**
     * Retrieves the details of the authenticated user.
     *
     * @return The Client object representing the authenticated client, or null if no client is
     * authenticated.
     */
    Client getClient();

    /**
     * Logs out the currently authenticated client. Throws an exception if no client is authenticated.
     */
    void logout();
}
