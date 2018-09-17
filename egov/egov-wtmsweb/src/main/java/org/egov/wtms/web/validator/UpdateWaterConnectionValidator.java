/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.wtms.web.validator;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEPUTY_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FORWARDWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERINTENDING_ENGINEER_DESIGNATION;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateWaterConnectionValidator implements Validator {

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private AssignmentService assignmentService;

    @Override
    public boolean supports(Class<?> clazz) {
        return WaterConnectionDetails.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        WaterConnectionDetails connectionDetails = (WaterConnectionDetails) target;
        if (isBlank(connectionDetails.getApprovalNumber()))
            errors.rejectValue("approvalNumber", "approvalNumber.required");
        if (connectionDetails.getApprovalDate() == null)
            errors.rejectValue("approvalDate", "approvalDate.required");
    }

    public boolean validateRegularizationAmount(WaterConnectionDetails waterConnectionDetails) {
        return connectionDemandService.getTotalDemandAmountDue(
                waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand()).compareTo(BigDecimal.ZERO) > 0 ? true : false;
    }

    public boolean applicationInProgress(WaterConnectionDetails waterConnectionDetails, String stateValue,
            String statusCode, String ownerPosition, String workFlowAction) {
        if (waterConnectionDetails != null
                && !REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
            WaterConnectionDetails connectionDetails = waterConnectionDetailsService
                    .findByApplicationNumber(waterConnectionDetails.getApplicationNumber());
            String currentUserDesignation = null;
            if (isNotBlank(ownerPosition))
                currentUserDesignation = waterTaxUtils.currentUserDesignation(Long.valueOf(ownerPosition));
            if (FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction)
                    && currentUserDesignation != null
                    && (DEPUTY_ENGINEER_DESIGN.equalsIgnoreCase(currentUserDesignation)
                            || EXECUTIVE_ENGINEER_DESIGN.equalsIgnoreCase(currentUserDesignation)
                            || SUPERINTENDING_ENGINEER_DESIGNATION.equalsIgnoreCase(currentUserDesignation)
                            || MUNICIPAL_ENGINEER_DESIGN.equalsIgnoreCase(currentUserDesignation)))
                return !statusCode.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()) 
                        || compareDesignationWithCurrentUser(waterConnectionDetails, currentUserDesignation);
            else
                return connectionDetails != null
                        && (!statusCode.equals(waterConnectionDetails.getStatus().getCode())
                                || !stateValue.equals(waterConnectionDetails.getState().getValue()));
        }
        return false;
    }

    public boolean compareDesignationWithCurrentUser(WaterConnectionDetails waterConnectionDetails,
            String currentUserDesignation) {
        List<Assignment> assignmentList = assignmentService
                .getAssignmentsForPosition(waterConnectionDetails.getState().getOwnerPosition().getId(), new Date());
        for (Assignment assignment : assignmentList)
            if (currentUserDesignation.equalsIgnoreCase(assignment.getDesignation().getName()))
                return false;
        return true;
    }
}
