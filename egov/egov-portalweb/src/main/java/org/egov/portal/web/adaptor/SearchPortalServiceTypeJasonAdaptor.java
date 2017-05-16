package org.egov.portal.web.adaptor;

import java.lang.reflect.Type;

import org.egov.portal.entity.PortalServiceType;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchPortalServiceTypeJasonAdaptor implements JsonSerializer<PortalServiceType> {

    @Override
    public JsonElement serialize(final PortalServiceType portalServiceType, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject searchResult = new JsonObject();
        if (portalServiceType != null) {
            searchResult.addProperty("id", portalServiceType.getId());
            searchResult.addProperty("module", portalServiceType.getModule().getName());
            searchResult.addProperty("code", portalServiceType.getCode());
            searchResult.addProperty("name", portalServiceType.getName());
            searchResult.addProperty("sla", portalServiceType.getSla());
            searchResult.addProperty("url", portalServiceType.getUrl());
            searchResult.addProperty("isActive", portalServiceType.getIsActive());
            searchResult.addProperty("userService", portalServiceType.getUserService());
            searchResult.addProperty("businessUserService", portalServiceType.getBusinessUserService());
            searchResult.addProperty("helpDocLink", portalServiceType.getHelpDocLink());
        }

        return searchResult;
    }
}