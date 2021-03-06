package com.lezurex.whatsweb.server.commands.login;

import com.lezurex.whatsweb.server.Main;
import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.User;
import com.lezurex.whatsweb.server.database.DatabaseAdapter;
import com.lezurex.whatsweb.server.database.objects.Key;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

import java.util.UUID;

public class LoginCommand implements ServerCommand {
    @Override
    public void performCommand(JSONObject data, Client client) {
        WebSocket socket = client.getSocket();
        DatabaseAdapter db = Main.databaseAdapter;
        String result = db.getStringFromTable("users", "token", new Key("uuid", data.getString("uuid")));

        if (result != null) {
            if (result.equals(data.getString("token"))){
                JSONObject response = new JSONObject();
                response.put("success", "true");
                client.setUser(User.loadUser(UUID.fromString(data.getString("uuid"))));
                client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setErrorTitle("login").setResponseData(response).setResponseCommand("login").build());

            } else
                client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                        setErrorTitle("Token denied").
                        setErrorDescription("Provided token/uuid is invalid").
                        setErrorCode("403").build());
        } else
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("Token denied").
                    setErrorDescription("Provided token/uuid is invalid").
                    setErrorCode("403").build());
    }
}
