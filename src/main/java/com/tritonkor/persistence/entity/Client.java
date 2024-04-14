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

    /** The password of the client. */
    private String password;

    public Client(ClientBuilder clientBuilder) {
        super(clientBuilder.id);
        this.username = clientBuilder.username;
        this.password = clientBuilder.password;
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
     * Gets the password of the client.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public static class ClientBuilder {

        private int id;
        private String username;
        private String password;

        public ClientBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ClientBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ClientBuilder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Creates an instance of {@link Client}
         */
        public Client build() {
            if (Stream.of(username, password).anyMatch(s -> Objects.isNull(s) || s.isBlank())) {
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
                password, client.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + username + '\'' +
                ", hashPassword='" + password + '\'' +
                '}';
    }
}
