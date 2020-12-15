package com.lezurex.whatsweb.server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Server server = new Server(2121);
        server.start();
    }
}
