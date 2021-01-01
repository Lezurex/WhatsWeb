package com.lezurex.whatsweb.server.objects;

import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Key;
import lombok.Getter;

import java.util.List;

@Getter
public class User {

    private String uuid;
    private String username;
    private String email;
    private List<Group> groups;
    private List<User> friends;
    private double lastSeen;

    public User(String uuid) {
        DatabaseAdapter db = Main.databaseAdapter;
        this.uuid = uuid;
        this.username = db.getStringFromTable("users", "username", new Key("uuid", uuid));
        this.email = db.getStringFromTable("users", "email", new Key("uuid", uuid));
        String lastSeenDB = db.getStringFromTable("users", "lastSeen", new Key("uuid", uuid));
        if (lastSeenDB != null) {
            this.lastSeen = Double.parseDouble(lastSeenDB);
        } else {
            this.lastSeen = 0;
        }
        // TODO: Initialize groups
    }

}
