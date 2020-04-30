package org.egov.ptis.actions.reports;

import java.lang.reflect.Type;

import org.egov.ptis.domain.entity.property.view.SearchCourtCaseWriteoffRequest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SearchWriteoffCourtCaseAdapter implements JsonSerializer<SearchCourtCaseWriteoffRequest> {
    @Override
    public JsonElement serialize(final SearchCourtCaseWriteoffRequest searchReport, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (searchReport != null) {
            jsonObject.addProperty("applicationType", searchReport.getApplicationType());
            jsonObject.addProperty("applicationNumber", searchReport.getApplicationNumber());
            jsonObject.addProperty("applicationDate", searchReport.getApplicationDate());
            jsonObject.addProperty("applicantName", searchReport.getApplicantName());
            jsonObject.addProperty("source", searchReport.getSource());
            jsonObject.addProperty("applicationAddress", searchReport.getApplicationAddress());
            jsonObject.addProperty("applicationStatus", searchReport.getApplicationStatus());
            jsonObject.addProperty("ownerName", searchReport.getOwnerName());
            jsonObject.addProperty("url", searchReport.getUrl());
            
        }
        return jsonObject;
    }
}
