/**
 * 
 */
package org.egov.ptis.wtms;

import org.egov.ptis.domain.model.AssessmentDetails;

/**
 * Interface to access Property Information
 * @author rishi
 *
 */
public interface PropertyIntegrationService {
    
    public AssessmentDetails getAssessmentDetailsForFlag(final String asessmentNumber, final Integer flagDetail);

}
