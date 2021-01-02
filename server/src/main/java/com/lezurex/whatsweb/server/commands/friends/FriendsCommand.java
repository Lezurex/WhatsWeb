package com.lezurex.whatsweb.server.commands.friends;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.objects.Chat;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.User;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

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
        Map<User, Chat> friends = client.getUser().getFriends();
        JSONObject responseData = new JSONObject();
        responseData.put("subcommand", "get");
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<User, Chat> entry : friends.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", entry.getKey().getUsername());
            jsonObject.put("uuid", entry.getKey().getUuid().toString());
            jsonObject.put("lastSeen", entry.getKey().getLastSeen());
            jsonArray.put(jsonObject);
        }
        responseData.put("friends", jsonArray);
        client.getSocket().send(ResponseBuilder.buildResponse(responseData, "friends"));
    }
}
