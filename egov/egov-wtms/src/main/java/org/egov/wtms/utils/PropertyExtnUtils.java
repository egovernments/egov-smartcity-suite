package org.egov.wtms.utils;

import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyExtnUtils {

    @Autowired
    private PropertyExternalService propertyExternalService;

    public AssessmentDetails getAssessmentDetailsForFlag(final String asessmentNumber, final Integer flagDetail) {
        final AssessmentDetails assessmentDetails = propertyExternalService.loadAssessmentDetails(asessmentNumber,
                flagDetail);
        return assessmentDetails;
    }

}
