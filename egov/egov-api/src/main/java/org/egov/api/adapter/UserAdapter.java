package org.egov.api.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.egov.portal.entity.Citizen;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class UserAdapter extends DataAdapter<Citizen> {

    @Override
    public JsonElement serialize(Citizen citizen, Type type, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        if (citizen.getName() != null)
            jo.addProperty("name", citizen.getName());
        jo.addProperty("emailId", citizen.getEmailId());
        jo.addProperty("mobileNumber", citizen.getMobileNumber());
        jo.addProperty("userName", citizen.getUsername());
        if (citizen.getAltContactNumber() != null)
            jo.addProperty("altContactNumber", citizen.getAltContactNumber());
        if (citizen.getGender() != null)
            jo.addProperty("gender", citizen.getGender().name());
        if (citizen.getPan() != null)
            jo.addProperty("panCard", citizen.getPan());
        if (citizen.getDob() != null) {
            DateTimeFormatter ft = DateTimeFormat.forPattern("yyyy-MM-dd");
            jo.addProperty("dob", ft.print(citizen.getDob().getTime()));
        }
        if (citizen.getAadhaarNumber() != null)
            jo.addProperty("aadhaarCard", citizen.getAadhaarNumber());
        if (citizen.getLocale() != null)
            jo.addProperty("preferredLanguage", citizen.getLocale());
        return jo;
    }

}