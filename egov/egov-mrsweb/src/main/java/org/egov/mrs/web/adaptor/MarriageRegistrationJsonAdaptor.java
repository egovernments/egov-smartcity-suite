package org.egov.mrs.web.adaptor;

import java.lang.reflect.Type;

import org.egov.infra.utils.DateUtils;
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
                jsonObject.addProperty("registrationNo", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getApplicationNo() != null)
                jsonObject.addProperty("applicationNo", registration.getApplicationNo());
            else
                jsonObject.addProperty("applicationNo", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getApplicationDate() != null)
                jsonObject.addProperty("registrationDate", DateUtils.getDefaultFormattedDate(registration.getApplicationDate()));
            else
                jsonObject.addProperty("registrationDate", org.apache.commons.lang.StringUtils.EMPTY);

            if (registration.getDateOfMarriage() != null)
                jsonObject.addProperty("dateOfMarriage", DateUtils
                        .getDefaultFormattedDate(registration.getDateOfMarriage()));
            else
                jsonObject.addProperty("dateOfMarriage", org.apache.commons.lang.StringUtils.EMPTY);

            if (registration.getHusband().getFullName() != null)
                jsonObject.addProperty("husbandName", registration.getHusband().getFullName());
            else
                jsonObject.addProperty("husbandName", org.apache.commons.lang.StringUtils.EMPTY);

            if (registration.getHusband().getReligion() != null)
                jsonObject.addProperty("husbandreligion", registration.getHusband().getReligion().getName());
            else
                jsonObject.addProperty("husbandreligion", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getHusband().getMaritalStatus() != null)
                jsonObject.addProperty("husbandMaritalStatus", registration.getHusband().getMaritalStatus().toString());
            else
                jsonObject.addProperty("husbandMaritalStatus", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getWife().getFullName() != null)
                jsonObject.addProperty("wifeName", registration.getWife().getFullName());
            else
                jsonObject.addProperty("wifeName", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getWife().getReligion() != null)
                jsonObject.addProperty("wifereligion", registration.getWife().getReligion().getName());
            else
                jsonObject.addProperty("wifereligion", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getWife().getMaritalStatus() != null)
                jsonObject.addProperty("wifeMaritalStatus", registration.getWife().getMaritalStatus().toString());
            else
                jsonObject.addProperty("wifeMaritalStatus", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getMarriageCertificate().isEmpty())
                jsonObject.addProperty("certificateIssued", "No");
            else if (registration.getMarriageCertificate().get(0).isCertificateIssued())
                jsonObject.addProperty("certificateIssued", "Yes");

            if (registration.getStatus() != null)
                jsonObject.addProperty("status", registration.getStatus().getDescription());
            else
                jsonObject.addProperty("status", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getFeePaid() != null)
                jsonObject.addProperty("feePaid", registration.getFeePaid());
            else
                jsonObject.addProperty("feePaid", org.apache.commons.lang.StringUtils.EMPTY);

            if (!registration.isFeeCollected()) {
                if (registration.getStatus().getCode()
                        .equalsIgnoreCase(MarriageRegistration.RegistrationStatus.APPROVED.toString())
                        && registration.getCurrentState().getNextAction().equalsIgnoreCase("Certificate Print Pending")) {
                    jsonObject.addProperty("feeCollected", "No");
                    jsonObject.addProperty("feeCollectionPending", true);
                }
            }
            else
                jsonObject.addProperty("feeCollected", "Yes");

            if (registration.getRemarks() != null)
                jsonObject.addProperty("remarks", registration.getRejectionReason());
            else
                jsonObject.addProperty("remarks", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getZone() != null)
                jsonObject.addProperty("zone", registration.getZone().getName());
            else
                jsonObject.addProperty("zone", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getStateType() != null)
                jsonObject.addProperty("applicationType", registration.getStateType());
            else
                jsonObject.addProperty("remarks", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getMarriageRegistrationUnit() != null)
                jsonObject.addProperty("marriageRegistrationUnit", registration.getMarriageRegistrationUnit().getName());
            else
                jsonObject.addProperty("marriageRegistrationUnit", org.apache.commons.lang.StringUtils.EMPTY);
            jsonObject.addProperty("id", registration.getId());
        }
        return jsonObject;
    }
}