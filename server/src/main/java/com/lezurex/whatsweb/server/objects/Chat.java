package com.lezurex.whatsweb.server.objects;

import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Insert;
import com.lezurex.whatsweb.server.utils.Key;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Chat {

    public static Map<UUID, Chat> loadedChats = new HashMap<>();

    public static Chat loadChat(UUID uuid) {
        if (loadedChats.containsKey(uuid)) {
            return loadedChats.get(uuid);
        } else {
            Chat newChat = new Chat(uuid);
            loadedChats.put(uuid, newChat);
            return newChat;
        }
    }

    private Chat(UUID uuid) {
        DatabaseAdapter databaseAdapter = Main.databaseAdapter;
        boolean result = databaseAdapter.containsEntry("chats", new Key("uuid", uuid.toString()));
        if (!result)
            databaseAdapter.insertIntoTable("chats", new Insert("uuid", uuid.toString()), new Insert("history", "[]"));
        loadedChats.put(uuid, this);
    }
}