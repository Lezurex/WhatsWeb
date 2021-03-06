package com.lezurex.whatsweb.server.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.UUID;

@Getter @Setter
public class ChatElement {

    private User author;
    private String content;
    private double timestamp;
    private UUID uuid;

    public ChatElement(User author, String content, double timestamp, UUID uuid) {
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
        this.uuid = uuid;
    }

    public ChatElement(JSONObject object) {
        this.author = User.loadUser(UUID.fromString(object.getString("author")));
        this.uuid = UUID.fromString(object.getString("uuid"));
        this.content = object.getString("content");
        this.timestamp = object.getDouble("timestamp");
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("author", this.author.getUuid().toString())
                .put("content", this.content)
                .put("timestamp", this.timestamp)
                .put("uuid", this.uuid);
    }

    @Override
    public String toString() {
        return "ChatElement{" +
                "author=" + author +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", uuid=" + uuid +
                '}';
    }
}
