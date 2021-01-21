package com.lezurex.whatsweb.server.commands.chat;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
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
                getInfo(client, data);
                break;
        }
    }

    private void getInfo(Client client, JSONObject data) {
        //TODO group
        JSONObject response = new JSONObject();


        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("chat").setResponseData(response).build());
    }

    private void getFullChat(Client client) {

    }
}
