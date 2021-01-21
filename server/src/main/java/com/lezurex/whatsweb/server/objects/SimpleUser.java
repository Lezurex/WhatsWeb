package com.lezurex.whatsweb.server.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.UUID;

@Getter @Setter
public class SimpleUser {

    private UUID uuid;
    private String username;
    private double lastSeen;

    public SimpleUser(UUID uuid, String username, double lastSeen) {
        this.uuid = uuid;
        this.username = username;
        this.lastSeen = lastSeen;
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("uuid", uuid.toString()).put("username", this.username).put("lastSeen", this.lastSeen);
    }
}
