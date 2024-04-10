package com.tritonkor.persistence.entity;

import com.tritonkor.persistence.exception.RequiredFieldsException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a client entity in the system with a unique identifier, username, and hashed password.
 */
public class Client extends Entity {

    /** The username of the client. */
    private String username;

    /** The hashed password of the client. */
    private String hashPassword;

    public Client(ClientBuilder clientBuilder) {
        super(clientBuilder.id);
        this.username = clientBuilder.username;
        this.hashPassword = clientBuilder.hash_password;
    }

    /**
     * Gets the username of the client.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the hashed password of the client.
     *
     * @return The hashed password.
     */
    public String getHashPassword() {
        return hashPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public static class ClientBuilder {

        private int id;
        private String username;
        private String hash_password;

        public ClientBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ClientBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ClientBuilder password(String password) {
            this.hash_password = password;
            return this;
        }

        /**
         * Creates an instance of {@link Client}
         */
        public Client build() {
            if (Stream.of(username, hash_password).anyMatch(s -> Objects.isNull(s) || s.isBlank())) {
                throw new RequiredFieldsException();
            }
            return new Client(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Client client = (Client) o;
        return Objects.equals(username, client.username) && Objects.equals(
                hashPassword, client.hashPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hashPassword);
    }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + username + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                '}';
    }
}
