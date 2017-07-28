package org.egov.mrs.domain.service;

import java.util.HashMap;
import java.util.Map;

import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarriageRegistrationThirdPartyService {

    private final MarriageRegistrationService marriageRegistrationService;

    @Autowired
    public MarriageRegistrationThirdPartyService(final MarriageRegistrationService marriageRegistrationService) {
        this.marriageRegistrationService = marriageRegistrationService;
    }

    public Map<String, String> validateMarriageRegistration(final String applicationNo) {
        final MarriageRegistration marriageRegsitration = marriageRegistrationService.findByApplicationNo(applicationNo);
        final Map<String, String> statusCommentsMap = new HashMap<String, String>();
        if (marriageRegsitration.getStatus().getCode().equals(MarriageConstants.CREATED)
                || marriageRegsitration.getStatus().getCode().equals(MarriageConstants.APPROVED)
                || marriageRegsitration.getStatus().getCode()
                        .equals(MarriageConstants.DIGITALSIGNED)) {
            statusCommentsMap.put("status", MarriageConstants.OPEN);
            statusCommentsMap.put("comments", marriageRegsitration.getState().getComments());
            statusCommentsMap.put("updatedBy", marriageRegsitration.getState().getLastModifiedBy().getName());
        } else if (marriageRegsitration.getStatus().getCode().equals(MarriageConstants.REGISTERED)) {
            statusCommentsMap.put("status", MarriageConstants.APPROVED);
            statusCommentsMap.put("comments", marriageRegsitration.getState().getComments());
            statusCommentsMap.put("updatedBy", marriageRegsitration.getState().getLastModifiedBy().getName());
        } else if (marriageRegsitration.getStatus().getCode().equals(MarriageConstants.CANCELLED)) {
            statusCommentsMap.put("status", MarriageConstants.REJECTED);
            statusCommentsMap.put("comments", marriageRegsitration.getState().getComments());
            statusCommentsMap.put("updatedBy", marriageRegsitration.getState().getLastModifiedBy().getName());
        }
        return statusCommentsMap;
    }

}
