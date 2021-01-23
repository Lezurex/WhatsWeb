package com.lezurex.whatsweb.server.objects;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.database.DatabaseAdapter;
import com.lezurex.whatsweb.server.database.objects.Insert;
import com.lezurex.whatsweb.server.database.objects.Key;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

@Getter
public class Chat {

    public static Map<UUID, Chat> loadedChats = new HashMap<>();

    private List<ChatElement> chatElements;
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
        return new Chat(UUID.randomUUID());
    }

    private Chat(UUID uuid) {
        DatabaseAdapter databaseAdapter = Main.databaseAdapter;
        boolean result = databaseAdapter.containsEntry("chats", new Key("uuid", uuid.toString()));
        if (!result)
            databaseAdapter.insertIntoTable("chats", new Insert("uuid", uuid.toString()), new Insert("history", "[]"));
        loadedChats.put(uuid, this);
        this.uuid = uuid;
    }

    public List<ChatElement> getChatElements() {
        if (chatElements == null) {
            chatElements = Lists.newCopyOnWriteArrayList();
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
    public List<ChatElement> getChatElements(UUID lastUUID, int range) {
        List<ChatElement> chatElements = this.getChatElements();
        List<ChatElement> returnElements = Lists.newCopyOnWriteArrayList();
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

    public JSONArray getChatElementsAsJSONArray(List<ChatElement> chatElements) {
        JSONArray jsonArray = new JSONArray();
        chatElements.forEach(chatElement -> jsonArray.put(chatElement.toJSONObject()));
        return jsonArray;
    }

}