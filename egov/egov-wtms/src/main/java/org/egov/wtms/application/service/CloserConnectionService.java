/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.wtms.application.service;

import java.math.BigDecimal;

import org.egov.commons.entity.Source;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.workflow.ApplicationWorkflowCustomDefaultImpl;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CloserConnectionService {

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource wcmsMessageSource;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private SecurityUtils securityUtils;

    public static final String CLOSUREALLOWEDIFWTDUE = "CLOSUREALLOWEDIFWTDUE";

    public String validateChangeOfUseConnection(final WaterConnectionDetails parentWaterConnectionDetail) {
        String validationMessage = "";
        final String propertyID = parentWaterConnectionDetail.getConnection().getPropertyIdentifier();
        final WaterConnectionDetails inWorkflow = waterConnectionDetailsRepository
                .getConnectionDetailsInWorkflow(propertyID, ConnectionStatus.INPROGRESS);
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(propertyID,
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        if (parentWaterConnectionDetail.getConnectionStatus().equals(ConnectionStatus.HOLDING))
            validationMessage = wcmsMessageSource.getMessage("err.validate.primary.connection.holding",
                    new String[] { parentWaterConnectionDetail.getConnection().getConsumerCode(), propertyID }, null);
        else if (parentWaterConnectionDetail.getConnectionStatus().equals(ConnectionStatus.DISCONNECTED))
            validationMessage = wcmsMessageSource.getMessage("err.validate.primary.connection.disconnected",
                    new String[] { parentWaterConnectionDetail.getConnection().getConsumerCode(), propertyID }, null);
        else if (null != assessmentDetails.getErrorDetails()
                && null != assessmentDetails.getErrorDetails().getErrorCode())
            validationMessage = assessmentDetails.getErrorDetails().getErrorMessage();
        else if (null != assessmentDetails.getPropertyDetails()
                && null != assessmentDetails.getPropertyDetails().getTaxDue()
                && assessmentDetails.getPropertyDetails().getTaxDue().doubleValue() > 0) {
            if (!waterTaxUtils.isConnectionAllowedIfWTDuePresent(CLOSUREALLOWEDIFWTDUE))
                validationMessage = wcmsMessageSource
                        .getMessage("err.validate.property.taxdue",
                                new String[] { assessmentDetails.getPropertyDetails().getTaxDue().toString(),
                                        parentWaterConnectionDetail.getConnection().getPropertyIdentifier(),
                                        "Closure" },
                                null);
        } else if (!waterTaxUtils.isConnectionAllowedIfWTDuePresent(CLOSUREALLOWEDIFWTDUE)) {
            final BigDecimal waterTaxDueforParent = waterConnectionDetailsService
                    .getCurrentDue(parentWaterConnectionDetail);
            if (waterTaxDueforParent.doubleValue() > 0)
                validationMessage = wcmsMessageSource.getMessage("err.closure.connection.watertaxdue", null, null);
        } else if (null != inWorkflow)
            validationMessage = wcmsMessageSource.getMessage("err.validate.closeconnection.application.inprocess",
                    new String[] { parentWaterConnectionDetail.getConnection().getConsumerCode(),
                            inWorkflow.getApplicationNumber() },
                    null);
        return validationMessage;
    }

    /**
     * @param changeOfUse
     * @param approvalPosition
     * @param approvalComent
     * @param additionalRule
     * @param workFlowAction
     * @return Update Old Connection Object And Creates New WaterConnectionDetails with INPROGRESS of ApplicationType as
     * "CHNAGEOFUSE"
     */
    @Transactional
    public WaterConnectionDetails updatecloserConnection(final WaterConnectionDetails waterConnectionDetails,
            final Long approvalPosition, final String approvalComent, String additionalRule,
            final String workFlowAction, final String sourceChannel) {

        waterConnectionDetailsService.applicationStatusChange(waterConnectionDetails, workFlowAction, "",
                sourceChannel);
        final WaterConnectionDetails savedwaterConnectionDetails = waterConnectionDetailsRepository
                .saveAndFlush(waterConnectionDetails);

        final ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = waterConnectionDetailsService
                .getInitialisedWorkFlowBean();
        additionalRule = WaterTaxConstants.WORKFLOW_CLOSUREADDITIONALRULE;
        applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(savedwaterConnectionDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        if (waterConnectionDetails.getSource() != null
                && Source.CITIZENPORTAL.toString().equalsIgnoreCase(waterConnectionDetails.getSource().toString())
                && waterConnectionDetailsService.getPortalInbox(waterConnectionDetails.getApplicationNumber()) != null)
            waterConnectionDetailsService.updatePortalMessage(waterConnectionDetails);
        else if (waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()))
            waterConnectionDetailsService.pushPortalMessage(savedwaterConnectionDetails);
        waterConnectionDetailsService.updateIndexes(savedwaterConnectionDetails, sourceChannel);
        return savedwaterConnectionDetails;
    }
}
