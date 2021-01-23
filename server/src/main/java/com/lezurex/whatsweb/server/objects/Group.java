package com.lezurex.whatsweb.server.objects;

import com.google.common.collect.Lists;
import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.database.DatabaseAdapter;
import com.lezurex.whatsweb.server.database.objects.Insert;
import com.lezurex.whatsweb.server.database.objects.Key;
import lombok.Getter;
import org.json.JSONArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class Group {

    public static Map<UUID, Group> loadedGroups = new HashMap<>();

    private Map<UUID, User> members;
    private final UUID uuid;
    private Chat chat;
    private User admin;
    private String name;

    public static Group loadGroup(UUID uuid) {
        if (loadedGroups.containsKey(uuid)) {
            return loadedGroups.get(uuid);
        } else {
            Group newGroup = new Group(uuid);
            loadedGroups.put(uuid, newGroup);
            return newGroup;
        }
    }

    public static Group createGroup(String name, UUID adminUUID) {
        DatabaseAdapter databaseAdapter = Main.databaseAdapter;
        UUID uuid = UUID.randomUUID();

        boolean result = databaseAdapter.containsEntry("groups", new Key("uuid", uuid.toString()));
        if (!result) {
            databaseAdapter.insertIntoTable("chats", new Insert("uuid", uuid.toString()), new Insert("history", "[]"));
            databaseAdapter.insertIntoTable("groups", new Insert("uuid", uuid.toString()),
                    new Insert("members", "[\"" + adminUUID.toString() + "\"]"),
                    new Insert("admin", adminUUID.toString()),
                    new Insert("name", name));
        }
        return new Group(uuid);
    }

    public Group(UUID uuid) {
        DatabaseAdapter databaseAdapter = Main.databaseAdapter;
        ResultSet resultSet = databaseAdapter.getResultSet("groups", new Key("uuid", uuid.toString()));

        this.uuid = uuid;
        this.members = new HashMap<>();
        String membersString = null;

        try {
            while (resultSet.next()) {
                this.chat = Chat.loadChat(uuid);
                this.admin = User.loadUser(UUID.fromString(resultSet.getString("admin")));
                this.name = resultSet.getString("name");
                membersString = resultSet.getString("members");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final JSONArray jsonArray = new JSONArray(membersString);
        for(int i = 0; i<jsonArray.length(); i++) {
            final UUID memberUUID = (UUID.fromString(jsonArray.getString(i)));
            members.put(memberUUID, User.loadUser(memberUUID));
        }

        loadedGroups.put(uuid, this);
    }

    public List<SimpleUser> getSimpleMembers() {
        List<SimpleUser> list = Lists.newCopyOnWriteArrayList();
        System.out.println(members.toString());
        members.forEach((uuids, user) -> list.add(user.toSimpleUser()));
        return list;
    }

    public SimpleGroup toSimpleGroup() {
        String lastMessage = null;
        if(this.getChat().getChatElements().size() != 0)
            lastMessage = this.getChat().getChatElements().get((this.getChat().getChatElements().size() - 1)).getContent();
        return new SimpleGroup(this.name, lastMessage, uuid);
    }

    public void addUser(UUID userUUID) {

    }

    public void removeUser(UUID userUUID) {

    }

    @Override
    public String toString() {
        return "Group{" +
                "members=" + members +
                ", uuid=" + uuid +
                ", chat=" + chat +
                ", admin=" + admin +
                ", name='" + name + '\'' +
                '}';
    }
}
