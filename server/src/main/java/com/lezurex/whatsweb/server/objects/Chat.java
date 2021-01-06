package com.lezurex.whatsweb.server.objects;

import com.google.gson.Gson;
import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Insert;
import com.lezurex.whatsweb.server.utils.Key;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

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

    public static Chat createChat() {
        UUID uuid = UUID.randomUUID();
        return null;
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

    /**
     * Gets a range of {@link ChatElement ChatElements} from the last UUID
     * @param lastUUID Last UUID of the list
     * @param range Range counted backwards from lastUUID
     * @return Map with UUIDs and ChatElements
     */
    public ArrayList<ChatElement> getChatElements(UUID lastUUID, int range) {
        ArrayList<ChatElement> chatElements = this.getChatElements();
        ArrayList<ChatElement> returnElements = new ArrayList<>();
        for (int i = 0; i < chatElements.size(); i++) {
            ChatElement chatElement = chatElements.get(i);
            if (chatElement.getUuid() == lastUUID) {
                int lastIndex = i;
                for (int j = lastIndex; j > (chatElements.size() - range) ; j--) {
                    returnElements.add(chatElements.get(j));
                }
                Collections.reverse(returnElements);
            }
        }
        return returnElements;
    }

    public void addMessage(ChatElement chatElement) {
        chatElements.add(chatElement);
        JSONArray jsonArray = new JSONArray();
        for (ChatElement element : chatElements) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(element);
            jsonArray.put(new JSONObject(jsonString));
        }
        DatabaseAdapter databaseAdapter = Main.databaseAdapter;
        databaseAdapter.updateValue("chats", "history", jsonArray.toString(), new Key("uuid", uuid.toString()));
    }

}