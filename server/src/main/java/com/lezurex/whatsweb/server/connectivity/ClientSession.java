package com.lezurex.whatsweb.server.connectivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSession {

    private Socket socket;
    private OutputStream out;
    private InputStreamReader in;

    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        out = socket.getOutputStream();
        in = new InputStreamReader(socket.getInputStream());
        startListener();
    }

    public void startListener() {
        while (socket.isConnected()) {

        }
    }

}
