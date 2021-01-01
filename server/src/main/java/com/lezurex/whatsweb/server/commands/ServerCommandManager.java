package com.lezurex.whatsweb.server.commands;

import com.lezurex.whatsweb.server.commands.login.LoginCommand;

import java.util.HashMap;
import java.util.Map;

public class ServerCommandManager {

    public static ServerCommandManager instance;
    public Map<String, ServerCommand> commandMap;

    //Singleton
    public static ServerCommandManager getInstance() {
        if (instance == null) {
            instance = new ServerCommandManager();
        }
        return instance;
    }

    private ServerCommandManager() {
        this.commandMap = new HashMap<>();
        this.commandMap.put("login", new LoginCommand());
    }
}
