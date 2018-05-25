package org.egov.api.adapter;

import java.lang.reflect.Type;

import org.egov.pushbox.application.entity.UserDevice;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class UserDeviceAdapter extends DataAdapter<UserDevice> {
    public static final String USER_ID = "userId";
    public static final String USER_TOKEN_ID = "userToken";
    public static final String SEND_ALL = "sendAll";
    public static final String USER_DEVICE_ID = "deviceId";

    @Override
    public JsonElement serialize(UserDevice userDevice, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObjectDraft = new JsonObject();
        jsonObjectDraft.addProperty(USER_TOKEN_ID, userDevice.getUserDeviceToken());
        jsonObjectDraft.addProperty(USER_ID, userDevice.getUserId());
        jsonObjectDraft.addProperty(USER_DEVICE_ID, userDevice.getDeviceId());
        return jsonObjectDraft;
    }
}
