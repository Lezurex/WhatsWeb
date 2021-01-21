package com.lezurex.whatsweb.server.utils;

import com.lezurex.whatsweb.server.enums.ResponseType;
import lombok.Getter;
import org.json.JSONObject;

@Getter
public class ResponseBuilder {

    private final ResponseType responseType;

    //Error
    private String title;
    private String description;
    private String errorCode;

    //Response
    private JSONObject data;
    private String command;

    public ResponseBuilder(ResponseType responseType) {
        this.responseType = responseType;
    }

    public ResponseBuilder setErrorTitle(String title) {
        this.title = title;
        return this;
    }

    public ResponseBuilder setErrorDescription(String description) {
        this.description = description;
        return this;
    }

    public ResponseBuilder setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ResponseBuilder setResponseData(JSONObject data) {
        this.data = data;
        return this;
    }

    public ResponseBuilder setResponseCommand(String command) {
        this.command = command;
        return this;
    }

    public String build() {
        JSONObject jsonObject = new JSONObject();
        if(this.responseType == ResponseType.RESPONSE) {
            data.put("command", command);
            jsonObject.put("data", data);
        } else {
            JSONObject innerJsonObject = new JSONObject();
            innerJsonObject.put("title", title);
            innerJsonObject.put("description", description);
            innerJsonObject.put("code", errorCode);
            jsonObject.put("error", innerJsonObject);
        }
        return jsonObject.toString();
    }

}
