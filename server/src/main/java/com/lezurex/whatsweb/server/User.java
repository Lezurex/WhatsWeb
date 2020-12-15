package com.lezurex.whatsweb.server;

import java.util.List;

public class User {

    private int userID;
    private String username;
    private String email;
    private List<Group> groups;
    private List<User> friends;

    public User() {

    }

}
