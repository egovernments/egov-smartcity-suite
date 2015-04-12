package org.egov.infra.web.support.json.adapter;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.common.constants.CommonConstants;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BoundaryAdapter implements JsonSerializer<Boundary> {
    
    DateFormat dateFormatter = new SimpleDateFormat(CommonConstants.DATE_FORMAT_DDMMYYYY);
    @Override
    public JsonElement serialize(Boundary boundary, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", boundary.getName());
        jsonObject.addProperty("boundaryNameLocal", boundary.getBoundaryNameLocal());
        jsonObject.addProperty("boundaryNum", boundary.getBoundaryNum());
        jsonObject.addProperty("fromDate", dateFormatter.format(boundary.getFromDate()));
        
        if (boundary.getToDate() == null) {
            jsonObject.addProperty("toDate", "NA");
        } else {
            jsonObject.addProperty("toDate", dateFormatter.format(boundary.getToDate()));
        }
        
        return jsonObject;      
    }
}
