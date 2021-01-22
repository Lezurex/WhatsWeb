package com.lezurex.whatsweb.server.commands.group;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.Group;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

public class GroupCommand implements ServerCommand {

    @Override
    public void performCommand(JSONObject data, Client client) {
        String subcommand = data.getString("subcommand");
        switch (subcommand) {
            case "getByID":
                this.sendGroupInfo(client, UUID.fromString(data.getString("uuid")));
                break;
            case "getChat":
                this.sendChat(client, UUID.fromString(data.getString("uuid")), null, -1);
                break;
            case "getChatWithRange":
                this.sendChat(client, UUID.fromString(data.getString("uuid")), UUID.fromString(data.getString("lastUUID")), data.getInt("range"));
                break;
        }
    }

    private void sendGroupInfo(Client client, UUID uuid) {
        Group group = Group.loadGroup(uuid);
        if(group == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Group not found").
                    setErrorDescription("The provided id isn't assigned to a group").
                    setErrorCode("404").build());
            return;
        }

        JSONArray members = new JSONArray();
        group.getSimpleMembers().forEach(simpleUser -> members.put(simpleUser.toJSONObject()));

        JSONObject response = new JSONObject();
        response.put("uuid", group.getUuid()).
                put("name", group.getName()).
                put("admin", group.getAdmin().toSimpleUser().toJSONObject()).
                put("members", members);
        // TODO add chat uuid of chat object

        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(response).build());
    }

    private void sendChat(Client client, UUID uuid, UUID lastUUID, int range) {
        Group group = Group.loadGroup(uuid);
        if(group == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Group not found").
                    setErrorDescription("The provided id isn't assigned to a group").
                    setErrorCode("404").build());
            return;
        }

        JSONObject response = new JSONObject();

        if(range == -1) // -1 = get all messages
            response.put("messages", group.getChat().getChatElementsAsJSONArray(group.getChat().getChatElements()));
        else {
            //TODO invalid range check
            response.put("messages", group.getChat().getChatElementsAsJSONArray(group.getChat().getChatElements(lastUUID, range)));
        }
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(response).build());
    }

}
