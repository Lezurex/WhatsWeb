package com.lezurex.whatsweb.server.commands;

import com.lezurex.whatsweb.server.commands.chat.ChatCommand;
import com.lezurex.whatsweb.server.commands.friends.FriendsCommand;
import com.lezurex.whatsweb.server.commands.group.GroupCommand;
import com.lezurex.whatsweb.server.commands.login.LoginCommand;
import com.lezurex.whatsweb.server.commands.user.UserCommand;

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
        this.commandMap.put("friends", new FriendsCommand());
        this.commandMap.put("chat", new ChatCommand());
        this.commandMap.put("group", new GroupCommand());
        this.commandMap.put("user", new UserCommand());
    }
}
