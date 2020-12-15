package com.lezurex.whatsweb.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Map;

public class Server extends WebSocketServer {

    public static Map<WebSocket, Client> clients;

    public Server(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        clients.put(webSocket, new Client(webSocket));
        System.out.println("Connected " + webSocket.getRemoteSocketAddress().getAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        clients.remove(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        for (Map.Entry<WebSocket, Client> entry : clients.entrySet()) {
            if (entry.getKey() == webSocket) {
                entry.getValue().handleInput(s);
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }
}