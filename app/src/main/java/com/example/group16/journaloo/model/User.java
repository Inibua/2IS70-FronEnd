package com.example.group16.journaloo.model;

/**
 * Created by s169096 on 14-3-2018.
 */

public class User {
    public int id;
    public String username;
    public String email;
    public String password;

    public User () {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User (int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
