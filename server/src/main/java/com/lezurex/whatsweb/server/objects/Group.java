package com.lezurex.whatsweb.server.objects;

import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Insert;
import com.lezurex.whatsweb.server.utils.Key;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Group {

    public static Map<UUID, Group> loadedGroups = new HashMap<>();

    private Map<UUID, User> members;
    private UUID uuid;
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

    private Group(UUID uuid) {
        DatabaseAdapter databaseAdapter = Main.databaseAdapter;
        boolean result = databaseAdapter.containsEntry("groups", new Key("uuid", uuid.toString()));
        if (!result)
            databaseAdapter.insertIntoTable("chats", new Insert("uuid", uuid.toString()), new Insert("history", "[]"));
        loadedGroups.put(uuid, this);
        this.uuid = uuid;
    }

}
