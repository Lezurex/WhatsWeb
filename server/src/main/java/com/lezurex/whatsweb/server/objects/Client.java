package com.lezurex.whatsweb.server.objects;

import org.java_websocket.WebSocket;

public class Client {

    private User user;
    private WebSocket socket;

    public Client(WebSocket socket) {
        user = new User();
        this.socket = socket;
    }

    public void handleInput(String s) {
        switch (s) {
            case "a":
                socket.send("You sent a");
                break;
            case "b":
                socket.send("You sent b");
                break;
        }
    }

}
