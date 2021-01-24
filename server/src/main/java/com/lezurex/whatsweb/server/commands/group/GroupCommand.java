package com.lezurex.whatsweb.server.commands.group;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.objects.ChatElement;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.Group;
import com.lezurex.whatsweb.server.objects.User;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

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
                if (data.has("lastUUID"))
                    this.sendChat(client, UUID.fromString(data.getString("uuid")), UUID.fromString(data.getString("lastUUID")), data.getInt("range"), "getChatWithRange");
                else
                    this.sendChat(client, UUID.fromString(data.getString("uuid")), null, data.getInt("range"), "getChatWithRange");
                break;
            case "getGroups":
                this.sendGroups(client);
                break;
            case "addUser":
                this.addUser(client, UUID.fromString(data.getString("groupUUID")), UUID.fromString(data.getString("userUUID")));
                break;
            case "removeUser":
                this.removeUser(client, UUID.fromString(data.getString("groupUUID")), UUID.fromString(data.getString("userUUID")));
                break;
            case "sendMessage":
                this.sendMessage(client, UUID.fromString(data.getString("groupUUID")), data.getString("message"));
                break;
        }
    }

    private void sendGroupInfo(Client client, UUID uuid) {
        Group group = Group.loadGroup(uuid);
        if (group == null) {
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
                .put("admin", User.loadUser(group.getAdmin()).toSimpleUser().toJSONObject())
                .put("members", members)
                .put("chatuuid", group.getChat().getUuid());

        response.put("subcommand", "getByID");
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(response).build());
        ;
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
        ;
    }

    private void sendGroups(Client client) {
        ;
        final List<Group> groups = client.getUser().getGroups();
        JSONArray jsonArray = new JSONArray();

        groups.forEach(group -> {
            jsonArray.put(group.toSimpleGroup().toJSONObject());
        });

        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(new JSONObject().put("subcommand", "getGroups").put("groups", jsonArray)).build());
    }

    private void addUser(Client client, UUID groupUUID, UUID userUUID) {
        final Group group = Group.loadGroup(groupUUID);

        if(group == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Group not found").
                    setErrorDescription("The provided id isn't assigned to a group").
                    setErrorCode("404").build());
            return;
        }
        if(group.getAdmin() != client.getUser().getUuid()) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("No permission").
                    setErrorDescription("You are not permitted to add a user").
                    setErrorCode("403").build());
            return;
        }
        if(group.getMembers().containsKey(userUUID)) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("User already in group").
                    setErrorDescription("The user you are trying to add, is already in the group").
                    setErrorCode("400").build());
            return;
        }

        group.addUser(userUUID);
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(new JSONObject().put("subcommand", "addUser").put("status", "success")).build());
    }

    private void removeUser(Client client, UUID groupUUID, UUID userUUID) {
        final Group group = Group.loadGroup(groupUUID);

        if(group == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Group not found").
                    setErrorDescription("The provided id isn't assigned to a group").
                    setErrorCode("404").build());
            return;
        }
        if(group.getAdmin() != client.getUser().getUuid()) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("No permission").
                    setErrorDescription("You are not permitted to remove a user").
                    setErrorCode("403").build());
            return;
        }
        if(!group.getMembers().containsKey(userUUID)) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("User not in group").
                    setErrorDescription("The user you are trying to remove, is not in the group").
                    setErrorCode("400").build());
            return;
        }

        group.removeUser(userUUID);
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(new JSONObject().put("subcommand", "removeUser").put("status", "success")).build());
    }

    private void sendMessage(Client client, UUID groupUUID, String message) {
        final Group group = Group.loadGroup(groupUUID);

        if (group == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Group not found").
                    setErrorDescription("The provided id isn't assigned to a group").
                    setErrorCode("404").build());
            return;
        }

        AtomicBoolean isInGroup = new AtomicBoolean(false);

        group.getMembers().forEach((uuid, user) -> {
            if(uuid.equals(client.getUser().getUuid())) isInGroup.set(true);
        });

        if(!isInGroup.get()) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("No permission").
                    setErrorDescription("You are not permitted to send a message in that group").
                    setErrorCode("403").build());
            return;
        }

        group.sendMessage(new ChatElement(client.getUser(), message, System.currentTimeMillis(), UUID.randomUUID()));
        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("group").setResponseData(new JSONObject().put("subcommand", "sendMessage").put("status", "success")).build());
    }

}
