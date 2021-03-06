package com.lezurex.whatsweb.server.objects;

import com.google.common.collect.Lists;
import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.database.DatabaseAdapter;
import com.lezurex.whatsweb.server.database.objects.Key;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Getter
public class User {

    private static final Map<UUID, User> loadedUsers = new HashMap<>();

    private final UUID uuid;
    private final String username;
    private final String email;
    private List<Group> groups;
    private Map<User, Chat> friends;
    private double lastSeen;

    public static User loadUser(UUID uuid) {
        if (loadedUsers.containsKey(uuid)) {
            return loadedUsers.get(uuid);
        } else {
            return new User(uuid);
        }
    }

    private User(UUID uuid) {
        DatabaseAdapter db = Main.databaseAdapter;
        loadedUsers.put(uuid, this);

        this.uuid = uuid;
        this.username = db.getStringFromTable("users", "username", new Key("uuid", uuid.toString()));
        this.email = db.getStringFromTable("users", "email", new Key("uuid", uuid.toString()));
        new Thread(this::loadGroups).start();
    }

    public Map<User, Chat> getFriends() {
        if (friends == null) {
            DatabaseAdapter databaseAdapter = Main.databaseAdapter;

            friends = new HashMap<User, Chat>();
            String result = databaseAdapter.getStringFromTable("users", "friends", new Key("uuid", uuid.toString()));
            if (result != null) {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject friend = jsonArray.getJSONObject(i);
                    UUID friendUUID = UUID.fromString(friend.getString("uuid"));
                    UUID chatUUID = UUID.fromString(friend.getString("chatuuid"));
                    friends.put(User.loadUser(friendUUID), Chat.loadChat(chatUUID));
                }
            }
            return friends;

        } else
            return friends;
    }

    private void loadGroups() {
        if (this.groups != null) {
            return;
        }
        DatabaseAdapter databaseAdapter = Main.databaseAdapter;
        ResultSet resultSet = databaseAdapter.getResultSet("SELECT * FROM groups WHERE members LIKE '%" + uuid +"%'");
        groups = Lists.newCopyOnWriteArrayList();
        List<String> uuids = Lists.newCopyOnWriteArrayList();

        try {
            while (resultSet.next()) {
                uuids.add(resultSet.getString("uuid"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (String groupUUID : uuids) {
            groups.add(Group.loadGroup(UUID.fromString(groupUUID)));
        }

    }

    public SimpleUser toSimpleUser() {
        return new SimpleUser(this.uuid, this.username, this.lastSeen);
    }

    public static Map<UUID, User> getLoadedUsers() {
        return loadedUsers;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", groups=" + groups +
                ", friends=" + friends +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
