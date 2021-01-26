package com.lezurex.whatsweb.server.commands.chat;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.objects.*;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatCommand implements ServerCommand {

    @Override
    public void performCommand(JSONObject data, Client client) {
        String subcommand = data.getString("subcommand");
        switch (subcommand) {
            case "getInfo":
                getInfo(client, data);
                break;
            case "getChat":
                this.sendChat(client, UUID.fromString(data.getString("uuid")), null, -1, "getChat");
                break;
            case "getChatWithRange":
                if (data.has("lastUUID"))
                    this.sendChat(client, UUID.fromString(data.getString("uuid")), UUID.fromString(data.getString("lastUUID")), data.getInt("range"), "getChatWithRange");
                else
                    this.sendChat(client, UUID.fromString(data.getString("uuid")), null, data.getInt("range"), "getChatWithRange");
                break;
            case "sendMessage":
                this.sendMessage(client, UUID.fromString(data.getString("groupUUID")), data.getString("message"));
                break;
        }
    }

    private void getInfo(Client client, JSONObject data) {
        //TODO group
        JSONObject response = new JSONObject();


        response.put("subcommand", "getInfo");
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("chat").setResponseData(response).build());
    }

    private void sendChat(Client client, UUID uuid, UUID lastUUID, int range, String subCommand) {
        Chat chat = Chat.loadChat(uuid);
        if(chat == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Chat not found").
                    setErrorDescription("The provided id isn't assigned to a chat").
                    setErrorCode("404").build());
            return;
        }

        JSONObject response = new JSONObject();

        if (range == -1) // -1 = get all messages
            response.put("messages", chat.getChatElementsAsJSONArray(chat.getChatElements()));
        else {
            if(range > 100) {
                client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                        setErrorTitle("Invalid range").
                        setErrorDescription("The provided range is too large").
                        setErrorCode("400").build());
                return;
            }
            JSONArray jsonArray = chat.getChatElementsAsJSONArray(chat.getChatElements(lastUUID, range));
            JSONArray messages = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject message = jsonArray.getJSONObject(i);
                UUID authorUUID = UUID.fromString(message.getString("author"));

                message.put("author", User.loadUser(authorUUID).toSimpleUser().toJSONObject());
                messages.put(message);
            }
            response.put("messages", messages);
        }
        response.put("subcommand", subCommand).put("uuid", uuid.toString());
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("chat").setResponseData(response).build());
    }

    private void sendMessage(Client client, UUID chatUUID, String message) {
        final Chat chat = Chat.loadChat(chatUUID);

        if (chat == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Chat not found").
                    setErrorDescription("The provided id isn't assigned to a chat").
                    setErrorCode("404").build());
            return;
        }

        AtomicBoolean isPermitted = new AtomicBoolean(false);

        client.getUser().getFriends().forEach((user, chats) -> {
            if(chat.getUuid().equals(chats.getUuid())) isPermitted.set(true);
        });

        if(!isPermitted.get()) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("No permission").
                    setErrorDescription("You are not permitted to send a message in that chat").
                    setErrorCode("403").build());
            return;
        }

        chat.addMessage(new ChatElement(client.getUser(), message, System.currentTimeMillis(), UUID.randomUUID()));
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("chat").setResponseData(new JSONObject().put("subcommand", "sendMessage").put("status", "success")).build());
    }

}
