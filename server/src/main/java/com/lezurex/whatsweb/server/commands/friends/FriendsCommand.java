package com.lezurex.whatsweb.server.commands.friends;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.User;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendsCommand implements ServerCommand {

    @Override
    public void performCommand(JSONObject data, Client client) {
        String subcommand = data.getString("subcommand");
        switch (subcommand) {
            case "get":
                getFriends(client);
                break;
        }
    }

    private void getFriends(Client client) {
        ArrayList<User> friends = client.getUser().getFriends();
        JSONObject responseData = new JSONObject();
        responseData.put("subcommand", "get");
        JSONArray jsonArray = new JSONArray();
        for (User user : friends) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", user.getUsername());
            jsonObject.put("uuid", user.getUuid().toString());
            jsonObject.put("lastSeen", user.getLastSeen());
            jsonArray.put(jsonObject);
        }
        responseData.put("friends", jsonArray);
        client.getSocket().send(ResponseBuilder.buildResponse(responseData, "friends"));
    }
}
