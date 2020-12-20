package com.lezurex.whatsweb.server;

import com.lezurex.whatsweb.server.objects.Client;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server extends WebSocketServer {

    public static Map<WebSocket, Client> clients;

    public Server(int port) {
        super(new InetSocketAddress(port));
        clients = new HashMap<WebSocket, Client>();
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
        if (clients.containsKey(webSocket)) {
            clients.get(webSocket).handleInput(s);
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started on port " + getPort());
    }
}