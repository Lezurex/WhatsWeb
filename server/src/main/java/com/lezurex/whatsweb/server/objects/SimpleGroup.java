package com.lezurex.whatsweb.server.objects;

import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class SimpleGroup {

    /*
     * The SimpleGroup should only be used for the chat selector
     * column in the webinterface
     */

    private final String name;
    private final String lastMessage;
    private final UUID uuid;

    public SimpleGroup(String name, String lastMessage, UUID uuid) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.uuid = uuid;
        ;
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("name", this.name).put("lastMessage", this.lastMessage).put("uuid", uuid.toString());
    }

}
