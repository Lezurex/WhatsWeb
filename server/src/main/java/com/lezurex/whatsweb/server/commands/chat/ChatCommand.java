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
        }
    }

    private void getFullChat(Client client) {

    }
}
