package com.lezurex.whatsweb.server.commands.chat;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.objects.Chat;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.Group;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.json.JSONObject;

import java.util.UUID;

public class ChatCommand implements ServerCommand {

    @Override
    public void performCommand(JSONObject data, Client client) {
        String subcommand = data.getString("subcommand");
        switch (subcommand) {
            case "getInfo":
                getInfo(client, data);
                break;
            case "getChat":
                this.sendChat(client, UUID.fromString(data.getString("uuid")), null, -1);
                break;
            case "getChatWithRange":
                this.sendChat(client, UUID.fromString(data.getString("uuid")), UUID.fromString(data.getString("lastUUID")), data.getInt("range"));
                break;
        }
    }

    private void getInfo(Client client, JSONObject data) {
        //TODO group
        JSONObject response = new JSONObject();


        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("chat").setResponseData(response).build());
    }

    private void sendChat(Client client, UUID uuid, UUID lastUUID, int range) {
        Chat chat = Chat.loadChat(uuid);
        if(chat == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Chat not found").
                    setErrorDescription("The provided id isn't assigned to a chat").
                    setErrorCode("404").build());
            return;
        }

        JSONObject response = new JSONObject();

        if(range == -1) // -1 = get all messages
            response.put("messages", chat.getChatElementsAsJSONArray(chat.getChatElements()));
        else {
            //TODO invalid range check
            response.put("messages", chat.getChatElementsAsJSONArray(chat.getChatElements(lastUUID, range)));
        }
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("chat").setResponseData(response).build());
    }
}
