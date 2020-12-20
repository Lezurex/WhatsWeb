package com.lezurex.whatsweb.server.objects;

import lombok.Getter;

import java.util.List;

@Getter
public class User {

    private int userID;
    private String username;
    private String email;
    private List<Group> groups;
    private List<User> friends;

    public User() {

    }


}
