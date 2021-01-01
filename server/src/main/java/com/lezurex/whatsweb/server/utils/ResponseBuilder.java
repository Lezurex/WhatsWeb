package com.lezurex.whatsweb.server.utils;

import org.json.JSONObject;

public class ResponseBuilder {

    public static String buildError(String title, String description, String errorCode) {
        JSONObject jsonObject = new JSONObject();
        JSONObject innerJsonObject = new JSONObject();
        innerJsonObject.put("title", title);
        innerJsonObject.put("description", description);
        innerJsonObject.put("code", errorCode);
        jsonObject.put("error", innerJsonObject);
        return jsonObject.toString();
    }

    public static String buildResponse(JSONObject data, String command) {
        JSONObject jsonObject = new JSONObject();
        data.put("command", command);
        jsonObject.put("data", data);
        return jsonObject.toString();
    }

}
