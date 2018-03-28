package com.example.group16.journaloo;

/**
 * Created by s169096 on 14-3-2018.
 */

public class User {
    public String userId;
    public String userName;
    public String email;
    public String password;

    User () {

    }

    User (String username, String password) {
        this.userName = username;
        this.password = password;
    }

    User (String userId, String username, String email) {
        this.userId = userId;
        this.userName = username;
        this.email = email;
    }
}
