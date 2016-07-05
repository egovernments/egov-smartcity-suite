package org.egov.lcms.transactions.entity;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LegalCaseReportResultAdaptor implements JsonSerializer<LegalCaseReportResult> {

	@Override
	public JsonElement serialize(LegalCaseReportResult legalcaseresult, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("casenumber", legalcaseresult.getCaseNumber());
		jsonObject.addProperty("legalcaseno", legalcaseresult.getLcNumber());
		return jsonObject;
	}

}
