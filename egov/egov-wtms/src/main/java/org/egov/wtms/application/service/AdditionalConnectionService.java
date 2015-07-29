/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 *
 * Copyright (C) <2015> eGovernments Foundation
 *
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 *
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 *
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 *
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 *
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 *
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.application.service;

import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdditionalConnectionService {

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private PropertyExternalService propertyExternalService;

    public String validateAdditionalConnection(final WaterConnectionDetails parentWaterConnectionDetail) {
        String validationMessage = "";
        final String propertyID = parentWaterConnectionDetail.getConnection().getPropertyIdentifier();
        final WaterConnectionDetails inWorkflow = waterConnectionDetailsRepository
                .getAdditionalConnectionDetailsInWorkflow(propertyID, ConnectionStatus.INPROGRESS);
        final AssessmentDetails assessmentDetails = propertyExternalService.loadAssessmentDetails(propertyID,
                PropertyExternalService.FLAG_FULL_DETAILS);
        if (parentWaterConnectionDetail.getConnectionStatus().equals(ConnectionStatus.HOLDING))
            validationMessage = messageSource.getMessage("err.validate.primary.connection.holding", new String[] {
                    parentWaterConnectionDetail.getConnection().getConsumerCode(), propertyID }, null);
        else if (parentWaterConnectionDetail.getConnectionStatus().equals(ConnectionStatus.DISCONNECTED))
            validationMessage = messageSource.getMessage("err.validate.primary.connection.disconnected", new String[] {
                    parentWaterConnectionDetail.getConnection().getConsumerCode(), propertyID }, null);
        else if (parentWaterConnectionDetail.getDemand().getBaseDemand().doubleValue() > 0)
            validationMessage = messageSource.getMessage("err.validate.primary.connection.watertax.due", null, null);
        else if (assessmentDetails.getErrorDetails() != null
                && assessmentDetails.getErrorDetails().getErrorCode() != null)
            validationMessage = assessmentDetails.getErrorDetails().getErrorMessage();
        else if (assessmentDetails.getPropertyDetails() != null
                && assessmentDetails.getPropertyDetails().getTaxDue() != null
                && assessmentDetails.getPropertyDetails().getTaxDue().doubleValue() > 0)
            validationMessage = messageSource.getMessage("err.validate.property.taxdue", new String[] {
                    assessmentDetails.getPropertyDetails().getTaxDue().toString(),
                    parentWaterConnectionDetail.getConnection().getPropertyIdentifier() }, null);
        else if (null != inWorkflow)
            validationMessage = messageSource.getMessage(
                    "err.validate.addconnection.application.inprocess",
                    new String[] { parentWaterConnectionDetail.getConnection().getConsumerCode(),
                            inWorkflow.getApplicationNumber() }, null);
        return validationMessage;
    }

}
