package com.lezurex.whatsweb.server.objects;

import com.google.gson.Gson;
import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Insert;
import com.lezurex.whatsweb.server.utils.Key;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Chat {

    public static Map<UUID, Chat> loadedChats = new HashMap<>();

    private ArrayList<ChatElement> chatElements;
    private UUID uuid;

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
        this.uuid = uuid;
    }

    public ArrayList<ChatElement> getChatElements() {
        if (chatElements == null) {
            chatElements = new ArrayList<>();
            DatabaseAdapter databaseAdapter = Main.databaseAdapter;
            String result = databaseAdapter.getStringFromTable("chats", "history", new Key("uuid", uuid.toString()));
            if (result == null || result.equals("")) {
                databaseAdapter.updateValue("chats", "history", "[]", new Key("uuid", uuid.toString()));
                result = "[]";
            }
            JSONArray items = new JSONArray(result);
            for (int i = 0; i < items.length(); i++) {
                Gson gson = new Gson();
                ChatElement chatElement = gson.fromJson(items.getJSONObject(i).toString(), ChatElement.class);
                chatElements.add(chatElement);
            }

        }
        return chatElements;
    }

}