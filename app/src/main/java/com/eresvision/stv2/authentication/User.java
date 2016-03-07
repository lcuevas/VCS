package com.eresvision.stv2.authentication;

public class User {

    String name, username, password, email;

    public User(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this("", "", username, password);
    }
}
