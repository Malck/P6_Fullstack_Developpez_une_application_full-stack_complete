package com.openclassrooms.mddapi.model;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 123456789101112L;

    private String login;
    private String password;

    // Default constructor for JSON Parsing
    public JwtRequest()
    {

    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

}