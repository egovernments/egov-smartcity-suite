/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.wtms.application.service;

import java.math.BigDecimal;

import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdditionalConnectionService {

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource wcmsMessageSource;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    public static final String ADDCONNALLOWEDIFWTDUE = "ADDCONNECTIONALLOWEDIFWTDUE";

    public String validateAdditionalConnection(final WaterConnectionDetails parentWaterConnectionDetail) {
        String validationMessage = "";
        final String propertyID = parentWaterConnectionDetail.getConnection().getPropertyIdentifier();
        final WaterConnectionDetails inWorkflow = waterConnectionDetailsRepository
                .getConnectionDetailsInWorkflow(propertyID, ConnectionStatus.INPROGRESS);
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(propertyID,
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
        if (parentWaterConnectionDetail.getConnectionStatus().equals(ConnectionStatus.HOLDING))
            validationMessage = wcmsMessageSource.getMessage("err.validate.primary.connection.holding",
                    new String[] { parentWaterConnectionDetail.getConnection().getConsumerCode(), propertyID }, null);
        else if (parentWaterConnectionDetail.getConnectionStatus().equals(ConnectionStatus.DISCONNECTED))
            validationMessage = wcmsMessageSource.getMessage("err.validate.primary.connection.disconnected",
                    new String[] { parentWaterConnectionDetail.getConnection().getConsumerCode(), propertyID }, null);
        else if (null != assessmentDetails.getErrorDetails()
                && null != assessmentDetails.getErrorDetails().getErrorCode())
            validationMessage = assessmentDetails.getErrorDetails().getErrorMessage();
        else if (null != inWorkflow)
            validationMessage = wcmsMessageSource.getMessage("err.validate.addconnection.application.inprocess",
                    new String[] { parentWaterConnectionDetail.getConnection().getConsumerCode(),
                            inWorkflow.getApplicationNumber() },
                    null);
        else {
            if (null != assessmentDetails.getPropertyDetails()
                    && null != assessmentDetails.getPropertyDetails().getTaxDue()
                    && assessmentDetails.getPropertyDetails().getTaxDue().doubleValue() > 0)
                if (!waterTaxUtils.isNewConnectionAllowedIfPTDuePresent())
                    validationMessage = wcmsMessageSource.getMessage("err.validate.property.taxdue",
                            new String[] { assessmentDetails.getPropertyDetails().getTaxDue().toString(),
                                    parentWaterConnectionDetail.getConnection().getPropertyIdentifier(), "additional" },
                            null);
            if (!waterTaxUtils.isConnectionAllowedIfWTDuePresent(ADDCONNALLOWEDIFWTDUE)) {
                final BigDecimal waterTaxDueforParent = waterConnectionDetailsService
                        .getCurrentDue(parentWaterConnectionDetail);
                if (waterTaxDueforParent.doubleValue() > 0)
                    if (validationMessage.equalsIgnoreCase(""))
                        validationMessage = wcmsMessageSource.getMessage("err.validate.primary.connection.watertax.due",
                                null, null);
                    else
                        validationMessage = validationMessage + " and " + wcmsMessageSource
                                .getMessage("err.validate.primary.connection.watertax.due", null, null);
                if (parentWaterConnectionDetail.getConnection().getId() != null)
                    if (waterTaxUtils.waterConnectionDue(parentWaterConnectionDetail.getConnection().getId()) > 0)
                        if (validationMessage.equalsIgnoreCase(""))
                            validationMessage = wcmsMessageSource
                                    .getMessage("err.validate.additional.connection.watertax.due", null, null);
                        else
                            validationMessage = validationMessage + " and " + wcmsMessageSource
                                    .getMessage("err.validate.additional.connection.watertax.due", null, null);
            }
        }
        return validationMessage;
    }

}