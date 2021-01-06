package com.lezurex.whatsweb.server.commands.chat;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.objects.Client;
import org.json.JSONObject;

public class ChatCommand implements ServerCommand {
    @Override
    public void performCommand(JSONObject data, Client client) {
        String subcommand = data.getString("subcommand");
        switch (subcommand) {
            case "getFull":
                getFullChat(client);
                break;
            case "getInfo":
                getInfo(client);
                break;
        }
    }

    private void getInfo(Client client) {

    }

    private void getFullChat(Client client) {

    }
}
