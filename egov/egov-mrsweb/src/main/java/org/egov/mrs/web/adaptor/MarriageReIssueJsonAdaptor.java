package org.egov.mrs.web.adaptor;

import java.lang.reflect.Type;

import org.egov.infra.utils.StringUtils;
import org.egov.mrs.domain.entity.ReIssue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MarriageReIssueJsonAdaptor implements JsonSerializer<ReIssue> {
        @Override
        public JsonElement serialize(final ReIssue reIssue, final Type type,
                        final JsonSerializationContext jsc) {
                JsonObject jsonObject =  new JsonObject();;
                if (reIssue != null) {
                        if (reIssue.getRegistration().getRegistrationNo() != null)
                                jsonObject.addProperty("registrationNo", reIssue.getRegistration().getRegistrationNo());
                        else
                                jsonObject.addProperty("registrationNo", StringUtils.EMPTY);
                        if (reIssue.getApplicationDate() != null)
                                jsonObject.addProperty("registrationDate", reIssue.getApplicationDate().toString());
                        else
                                jsonObject.addProperty("registrationDate", StringUtils.EMPTY);

                        if (reIssue.getRegistration().getDateOfMarriage() != null)
                                jsonObject.addProperty("dateOfMarriage", reIssue.getRegistration().getDateOfMarriage().toString());
                        else
                                jsonObject.addProperty("dateOfMarriage", StringUtils.EMPTY);

                        if (reIssue.getRegistration().getHusband().getFullName() != null)
                                jsonObject.addProperty("husbandName", reIssue.getRegistration().getHusband().getFullName());
                        else
                                jsonObject.addProperty("husbandName", StringUtils.EMPTY);
                        if (reIssue.getRegistration().getWife().getFullName() != null)
                                jsonObject.addProperty("wifeName", reIssue.getRegistration().getWife().getFullName());
                        else
                                jsonObject.addProperty("wifeName", StringUtils.EMPTY);

                         if(reIssue.getMarriageCertificate().isEmpty())
                             jsonObject.addProperty("certificateIssued","No");
                          else if(reIssue.getMarriageCertificate().get(0).isCertificateIssued())
                              jsonObject.addProperty("certificateIssued","Yes");

                        if (reIssue.getStatus() != null)
                                jsonObject.addProperty("status", reIssue.getStatus().getDescription());
                        else
                                jsonObject.addProperty("status", StringUtils.EMPTY);
                        if (reIssue.getFeePaid() != null)
                                jsonObject.addProperty("feePaid", reIssue.getFeePaid());
                        else
                                jsonObject.addProperty("feePaid", StringUtils.EMPTY);

                        if (!reIssue.isFeeCollected()){
                                if (reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.APPROVED.toString())
                                                && reIssue.getCurrentState().getNextAction().equalsIgnoreCase("Certificate Print Pending")) {
                                        jsonObject.addProperty("feeCollected","No");
                                }
                        }
                        else
                            jsonObject.addProperty("feeCollected", "Yes");
                        jsonObject.addProperty("id", reIssue.getId());
                }
                return jsonObject;
        }
}