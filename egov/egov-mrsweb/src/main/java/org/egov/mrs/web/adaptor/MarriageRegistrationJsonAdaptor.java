package org.egov.mrs.web.adaptor;

import java.lang.reflect.Type;

import org.egov.infra.utils.StringUtils;
import org.egov.mrs.domain.entity.MarriageRegistration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MarriageRegistrationJsonAdaptor implements JsonSerializer<MarriageRegistration> {
	@Override
	public JsonElement serialize(final MarriageRegistration registration, final Type type,
			final JsonSerializationContext jsc) {
		final JsonObject jsonObject = new JsonObject();
		if (registration != null) {
			if (registration.getRegistrationNo() != null)
				jsonObject.addProperty("registrationNo", registration.getRegistrationNo());
			else
				jsonObject.addProperty("registrationNo", StringUtils.EMPTY);
			if (registration.getApplicationDate() != null)
				jsonObject.addProperty("registrationDate", registration.getApplicationDate().toString());
			else
				jsonObject.addProperty("registrationDate", StringUtils.EMPTY);

			if (registration.getDateOfMarriage() != null)
				jsonObject.addProperty("dateOfMarriage", registration.getDateOfMarriage().toString());
			else
				jsonObject.addProperty("dateOfMarriage", StringUtils.EMPTY);

			if (registration.getHusband().getFullName() != null)
				jsonObject.addProperty("husbandName", registration.getHusband().getFullName());
			else
				jsonObject.addProperty("husbandName", StringUtils.EMPTY);
			if (registration.getWife().getFullName() != null)
				jsonObject.addProperty("wifeName", registration.getWife().getFullName());
			else
				jsonObject.addProperty("wifeName", StringUtils.EMPTY);

			 if(registration.getMarriageCertificate().isEmpty())
			     jsonObject.addProperty("certificateIssued","No");
		          else if(registration.getMarriageCertificate().get(0).isCertificateIssued())
		              jsonObject.addProperty("certificateIssued","Yes");

			if (registration.getStatus() != null)
				jsonObject.addProperty("status", registration.getStatus().getDescription());
			else
				jsonObject.addProperty("status", StringUtils.EMPTY);
			if (registration.getFeePaid() != null)
				jsonObject.addProperty("feePaid", registration.getFeePaid());
			else
				jsonObject.addProperty("feePaid", StringUtils.EMPTY);

			if (!registration.isFeeCollected())
				if (registration.getStatus().getCode().equalsIgnoreCase(MarriageRegistration.RegistrationStatus.APPROVED.toString())
						&& registration.getCurrentState().getNextAction().equalsIgnoreCase("Fee Collection Pending")) {
					jsonObject.addProperty("feeCollectionPending", true);
				}
			if (registration.getRemarks() != null)
				jsonObject.addProperty("remarks", registration.getRejectionReason());
			else
				jsonObject.addProperty("remarks", StringUtils.EMPTY);
			if (registration.getStateType() != null)
				jsonObject.addProperty("applicationType", registration.getStateType());
			else
				jsonObject.addProperty("remarks", StringUtils.EMPTY);
			jsonObject.addProperty("id", registration.getId());
		}
		return jsonObject;
	}
}