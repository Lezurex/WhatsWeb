package com.lezurex.whatsweb.server.objects;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class SimpleGroup {

    /*
     * The SimpleGroup should only be used for the chat selector
     * column in the webinterface
     */

    private final String name;
    private final String lastMessage;

    public SimpleGroup(String name, String lastMessage) {
        this.name = name;
        this.lastMessage = lastMessage;
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("name", this.name).put("lastMessage", this.lastMessage);
    }

}
