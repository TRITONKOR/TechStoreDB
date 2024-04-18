package com.tritonkor.domain.dto;

import com.tritonkor.persistence.entity.Entity;

public class ClientAddDto extends Entity {

    /** The username of the client. */
    private String username;

    /** The password of the client. */
    private String password;

    public ClientAddDto(int id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
