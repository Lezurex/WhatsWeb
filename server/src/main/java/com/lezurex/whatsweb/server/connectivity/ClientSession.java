package com.lezurex.whatsweb.server.connectivity;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSession {

    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private Scanner scanner;

    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        out = socket.getOutputStream();
        in = socket.getInputStream();
        scanner = new Scanner(in, "UTF-8");

        try {
            String data = scanner.useDelimiter("\\r\\n\\r\\n").next();
            Matcher get = Pattern.compile("^GET").matcher(data);
            if (get.find()) {
                Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                match.find();
                byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                        + "Connection: Upgrade\r\n"
                        + "Upgrade: websocket\r\n"
                        + "Sec-WebSocket-Accept: "
                        + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                        + "\r\n\r\n").getBytes("UTF-8");
                out.write(response, 0, response.length);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        startListener();

    }

    public void startListener() {
        while (socket.isConnected()) {
            if (scanner.hasNext()) {
                String message = scanner.next();
                System.out.println("[I]");
            }
        }



    }

}
