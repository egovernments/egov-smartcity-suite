package org.egov.api.adapter;

import static org.egov.eventnotification.constants.Constants.CATEGORY;
import static org.egov.eventnotification.constants.Constants.DRAFT_ID;
import static org.egov.eventnotification.constants.Constants.DRAFT_NAME;
import static org.egov.eventnotification.constants.Constants.DRAFT_NOTIFICATION_TYPE;
import static org.egov.eventnotification.constants.Constants.EVENT_ID;
import static org.egov.eventnotification.constants.Constants.EVENT_NAME;
import static org.egov.eventnotification.constants.Constants.MODULE;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_MESSAGE;

import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.egov.eventnotification.entity.NotificationDrafts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class NotificationDraftsAdapter extends DataAdapter<NotificationDrafts> {

    @Override
    public JsonElement serialize(NotificationDrafts draft, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObjectDraft = new JsonObject();
        jsonObjectDraft.addProperty(DRAFT_ID, draft.getId());
        jsonObjectDraft.addProperty(DRAFT_NAME, draft.getName());
        jsonObjectDraft.addProperty(DRAFT_NOTIFICATION_TYPE, draft.getType());

        if (draft.getModule() != null) {
            JsonObject jsonObjectModule = new JsonObject();
            if (StringUtils.isNotBlank(draft.getModule().getName()))
                jsonObjectModule.addProperty(EVENT_NAME, draft.getModule().getName());
            if (null != draft.getModule().getId())
                jsonObjectModule.addProperty(EVENT_ID, draft.getModule().getId());
            jsonObjectDraft.add(MODULE, jsonObjectModule);
        }

        if (draft.getCategory() != null) {
            JsonObject jsonObjectCategory = new JsonObject();
            if (StringUtils.isNotBlank(draft.getCategory().getName()))
                jsonObjectCategory.addProperty(EVENT_NAME, draft.getCategory().getName());
            if (draft.getCategory().getId() != null)
                jsonObjectCategory.addProperty(EVENT_ID, draft.getCategory().getId());
            jsonObjectDraft.add(CATEGORY, jsonObjectCategory);
        }
        jsonObjectDraft.addProperty(NOTIFICATION_MESSAGE, draft.getMessage());
        return jsonObjectDraft;
    }
}
