package com.lezurex.whatsweb.server.objects;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.commands.ServerCommandManager;
import lombok.Getter;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

@Getter
public class Client {

    private User user;
    private WebSocket socket;

    public Client(WebSocket socket) {
        user = null;
        this.socket = socket;
    }

    public void handleInput(String s) {
        JSONObject jsonObject = new JSONObject(s);
        JSONObject data = jsonObject.getJSONObject("data");
        String command = data.getString("command");
        ServerCommand serverCommand = ServerCommandManager.getInstance().commandMap.get(command);
        serverCommand.performCommand(data, this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WebSocket getSocket() {
        return socket;
    }
}
