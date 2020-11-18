package com.lezurex.whatsweb.server.connectivity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WebSocket {

    public ArrayList<ClientSession> sessions;

    private ServerSocket server;

    public WebSocket(int port) throws IOException {
        server = new ServerSocket(port);
        while (true) {

        }
    }

    public void startConnectionListener() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = server.accept();
                    System.out.println("[+] Client " + socket.getInetAddress() + " has conncted!");
                    new ClientSession(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
