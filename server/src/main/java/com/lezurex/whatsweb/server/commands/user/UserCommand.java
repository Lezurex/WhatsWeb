package com.lezurex.whatsweb.server.commands.user;

import com.lezurex.whatsweb.server.commands.ServerCommand;
import com.lezurex.whatsweb.server.enums.ResponseType;
import com.lezurex.whatsweb.server.objects.Client;
import com.lezurex.whatsweb.server.objects.User;
import com.lezurex.whatsweb.server.utils.ResponseBuilder;
import org.json.JSONObject;

import java.util.UUID;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 24.01.2021
 * Time: 12:58
 * Project: whatsweb
 */

public class UserCommand implements ServerCommand {

    @Override
    public void performCommand(JSONObject data, Client client) {
        String subcommand = data.getString("subcommand");
        switch (subcommand) {
            case "getByID":
                this.sendUserInfo(client, UUID.fromString(data.getString("uuid")));
                break;
        }
    }

    private void sendUserInfo(Client client, UUID uuid) {
        final User user = User.loadUser(uuid);
        if(user == null) {
            client.getSocket().send(new ResponseBuilder(ResponseType.ERROR).
                    setErrorTitle("User not found").
                    setErrorDescription("The provided id isn't assigned to a user").
                    setErrorCode("404").build());
            return;
        }

        client.getSocket().send(new ResponseBuilder(ResponseType.RESPONSE).setResponseCommand("user").setResponseData(new JSONObject().put("subcommand", "getByID").put("user", user.toSimpleUser().toJSONObject())).build());
    }

}
