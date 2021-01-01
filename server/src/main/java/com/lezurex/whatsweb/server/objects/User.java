package com.lezurex.whatsweb.server.objects;

import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Key;
import lombok.Getter;
import org.checkerframework.checker.units.qual.K;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

@Getter
public class User {

    private static final Map<UUID, User> loadedUsers = new HashMap<>();

    private UUID uuid;
    private String username;
    private String email;
    private List<Group> groups;
    private List<User> friends;
    private double lastSeen;

    public static User loadUser(UUID uuid) {
        if (loadedUsers.containsKey(uuid)) {
            return loadedUsers.get(uuid);
        } else {
            User newUser = new User(uuid);
            loadedUsers.put(uuid, newUser);
            return newUser;
        }
    }

    private User(UUID uuid) {
        System.out.println("new User");
        DatabaseAdapter db = Main.databaseAdapter;
        this.uuid = uuid;
        this.username = db.getStringFromTable("users", "username", new Key("uuid", uuid.toString()));
        this.email = db.getStringFromTable("users", "email", new Key("uuid", uuid.toString()));
    }

    public static Map<UUID, User> getLoadedUsers() {
        return loadedUsers;
    }
}
