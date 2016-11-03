package org.egov.mrs.web.adaptor;

import java.lang.reflect.Type;

import org.egov.infra.utils.StringUtils;
import org.egov.mrs.domain.entity.RegistrationCertificatesResultForReport;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MarriageRegistrationCertificateReportJsonAdaptor implements JsonSerializer<RegistrationCertificatesResultForReport> {
	@Override
	public JsonElement serialize(final RegistrationCertificatesResultForReport registration, final Type type,
			final JsonSerializationContext jsc) {
		final JsonObject jsonObject = new JsonObject();
		if (registration != null) {
			if (registration.getRegistrationNo() != null)
				jsonObject.addProperty("registrationNo", registration.getRegistrationNo());
			else
				jsonObject.addProperty("registrationNo", StringUtils.EMPTY);
			if (registration.getZone() != null)
				jsonObject.addProperty("zone", registration.getZone());
			else
				jsonObject.addProperty("zone", StringUtils.EMPTY);
			if (registration.getRegistrationDate() != null)
				jsonObject.addProperty("registrationDate", registration.getRegistrationDate().toString());
			else
				jsonObject.addProperty("registrationDate", StringUtils.EMPTY);

			if (registration.getDateOfMarriage() != null)
				jsonObject.addProperty("dateOfMarriage", registration.getDateOfMarriage().toString());
			else
				jsonObject.addProperty("dateOfMarriage", StringUtils.EMPTY);

			if (registration.getHusbandName() != null)
				jsonObject.addProperty("husbandName", registration.getHusbandName());
			else
				jsonObject.addProperty("husbandName", StringUtils.EMPTY);
			
			if (registration.getWifeName() != null)
				jsonObject.addProperty("wifeName", registration.getWifeName());
			else
				jsonObject.addProperty("wifeName", StringUtils.EMPTY);
			
			if (registration.getRejectReason() != null)
				jsonObject.addProperty("remarks", registration.getRejectReason());
			else
				jsonObject.addProperty("remarks", StringUtils.EMPTY);
			
			if (registration.getCertificateNo() != null)
				jsonObject.addProperty("certificateNo", registration.getCertificateNo());
			else
				jsonObject.addProperty("certificateNo", StringUtils.EMPTY);
			
			if (registration.getCertificateType() != null)
				jsonObject.addProperty("certificateType", registration.getCertificateType());
			else
				jsonObject.addProperty("certificateType", StringUtils.EMPTY);
			
			if (registration.getCertificateDate() != null)
				jsonObject.addProperty("certificateDate", registration.getCertificateDate());
			else
				jsonObject.addProperty("certificateDate", StringUtils.EMPTY);
			
			
			jsonObject.addProperty("id", registration.getId());
		}
		return jsonObject;
	}
}