package com.lezurex.whatsweb.server;

import com.lezurex.whatsweb.server.connectivity.WebSocket;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new WebSocket(2020);
    }
}
