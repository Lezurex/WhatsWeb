package com.lezurex.whatsweb.server.commands.group;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.Group;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
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
                this.sendChat(client, UUID.fromString(data.getString("uuid")), null, -1, "getChat");
                break;
            case "getChatWithRange":
                this.sendChat(client, UUID.fromString(data.getString("uuid")), UUID.fromString(data.getString("lastUUID")), data.getInt("range"), "getChatWithRange");
                break;
            case "getGroups":
                this.sendGroups(client);
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
        response.put("uuid", group.getUuid())
                .put("name", group.getName())
                .put("admin", group.getAdmin().toSimpleUser().toJSONObject())
                .put("members", members)
                .put("chatuuid", group.getChat().getUuid());

        response.put("subcommand", "getByID");
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(response).build());
    }

    private void sendChat(Client client, UUID uuid, UUID lastUUID, int range, String subCommand) {
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
            if(range > 100) {
                client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                        setErrorTitle("Invalid range").
                        setErrorDescription("The provided range is too large").
                        setErrorCode("400").build());
                return;
            }
            response.put("messages", group.getChat().getChatElementsAsJSONArray(group.getChat().getChatElements(lastUUID, range)));
        }
        response.put("subcommand", subCommand).put("uuid", uuid.toString());
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(response).build());
    }

    private void sendGroups(Client client) {
        final List<Group> groups = client.getUser().getGroups();
        JSONArray jsonArray = new JSONArray();

        groups.forEach(group -> {
            jsonArray.put(group.toSimpleGroup().toJSONObject());
        });

        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(new JSONObject().put("subcommand", "getGroups").put("groups", jsonArray)).build());
    }

}
