package com.lezurex.whatsweb.server.commands.login;

import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.User;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Key;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

import java.util.UUID;

public class LoginCommand implements ServerCommand {
    @Override
    public void performCommand(JSONObject data, Client client) {
        WebSocket socket = client.getSocket();
        DatabaseAdapter db = Main.databaseAdapter;
        if (db.getStringFromTable("users", "token", new Key("uuid", data.getString("uuid"))).equals(data.getString("token"))) {
            JSONObject response = new JSONObject();
            response.put("success", "true");
            client.setUser(User.loadUser(UUID.fromString(data.getString("uuid"))));
            client.getSocket().send(ResponseBuilder.buildResponse(response, "login"));
            client.getSocket().send("Your username is: " + client.getUser().getUsername());
        } else {
            client.getSocket().send(ResponseBuilder.buildError("Token denied", "Provided token/uuid is invalid", "403"));
        }
    }
}
