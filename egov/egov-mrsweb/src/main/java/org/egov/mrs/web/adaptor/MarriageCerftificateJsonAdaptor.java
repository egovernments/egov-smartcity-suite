package org.egov.mrs.web.adaptor;

import java.lang.reflect.Type;

import org.egov.infra.utils.StringUtils;
import org.egov.mrs.domain.entity.MarriageCertificate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MarriageCerftificateJsonAdaptor implements JsonSerializer<MarriageCertificate> {
    @Override
    public JsonElement serialize(final MarriageCertificate marriageCertificate, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (marriageCertificate != null) {
            if (marriageCertificate.getCertificateNo() != null)
                jsonObject.addProperty("certificateNo", marriageCertificate.getCertificateNo());
            else
                jsonObject.addProperty("certificateNo", StringUtils.EMPTY);
            if (marriageCertificate.getRegistration() != null)
                jsonObject.addProperty("registrationNo", marriageCertificate.getRegistration().getRegistrationNo());
            else
                jsonObject.addProperty("certificateNo", StringUtils.EMPTY);
            if (marriageCertificate.getCertificateDate() != null)
                jsonObject.addProperty("certificateDate", marriageCertificate.getCertificateDate().toString());
            else
                jsonObject.addProperty("certificateDate", StringUtils.EMPTY);
            
            if (marriageCertificate.getCertificateType() != null)
                jsonObject.addProperty("certificateType",marriageCertificate.getCertificateType());
            else
                jsonObject.addProperty("certificateType", StringUtils.EMPTY);
            
            jsonObject.addProperty("id", marriageCertificate.getId());
        }
        return jsonObject;
    }
}