package com.lezurex.whatsweb.server.objects;

import com.google.common.collect.Lists;
import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.Server;
import com.lezurex.whatsweb.server.database.DatabaseAdapter;
import com.lezurex.whatsweb.server.database.objects.Insert;
import com.lezurex.whatsweb.server.database.objects.Key;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

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
    private UUID admin;
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

        loadedGroups.put(uuid, this);

        this.uuid = uuid;
        this.chat = Chat.loadChat(uuid);
        this.members = new HashMap<>();

        String membersString = null;
        String adminString = null;

        try {
            while (resultSet.next()) {
                adminString = resultSet.getString("admin");
                this.name = resultSet.getString("name");
                membersString = resultSet.getString("members");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assert adminString != null;
        admin = UUID.fromString(adminString);

        assert membersString != null;
        final JSONArray jsonArray = new JSONArray(membersString);

        new Thread(() -> {
            for (int i = 0; i < jsonArray.length(); i++) {
                final UUID memberUUID = (UUID.fromString(jsonArray.getString(i)));
                User user = User.loadUser(memberUUID);
                members.put(memberUUID, user);
            }
        }).start();

    }

    public List<SimpleUser> getSimpleMembers() {
        List<SimpleUser> list = Lists.newCopyOnWriteArrayList();
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
        //TODO
    }

    public void removeUser(UUID userUUID) {
        //TODO
    }

    public void sendMessage(ChatElement chatElement) {
        this.chat.addMessage(chatElement);

        final JSONObject payload = new JSONObject();
        payload.put("uuid", uuid.toString()).put("message", chatElement.toJSONObject());

        this.members.forEach((memberUUID, user) -> {
            Server.clients.forEach((webSocket, client) -> {
                if(memberUUID.equals(client.getUser().getUuid())) {
                    client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("update").setResponseData(payload).build());
                }
            });
        });
    }
}
