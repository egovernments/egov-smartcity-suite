package org.egov.api.adapter;

import static org.egov.eventnotification.constants.Constants.CATEGORY_ID;
import static org.egov.eventnotification.constants.Constants.CATEGORY_NAME;

import java.lang.reflect.Type;

import org.egov.eventnotification.entity.ModuleCategory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class ModuleCategoryAdapter extends DataAdapter<ModuleCategory> {
    @Override
    public JsonElement serialize(ModuleCategory category, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CATEGORY_ID, category.getId());
        jsonObject.addProperty(CATEGORY_NAME, category.getName());
        return jsonObject;
    }
}
