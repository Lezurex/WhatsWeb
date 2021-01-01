package com.lezurex.whatsweb.server.commands;

import com.lezurex.whatsweb.server.objects.Client;
import org.json.JSONObject;

public interface ServerCommand {
    public void performCommand(JSONObject data, Client client);
}
