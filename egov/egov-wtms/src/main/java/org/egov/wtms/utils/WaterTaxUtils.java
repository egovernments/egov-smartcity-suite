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
package org.egov.wtms.utils;

import java.util.Date;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.messaging.email.EmailService;
import org.egov.infra.messaging.sms.SMSService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
public class WaterTaxUtils {

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private CityService cityService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private PersistenceService persistenceService;
    
    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public Boolean isSmsEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "SENDSMSFORWATERTAX").get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }
    
    public String getDepartmentForWorkFlow() {
        String department="";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "WATERTAXWORKFLOWDEPARTEMENT");
        if(null!=appConfigValue && !appConfigValue.isEmpty())
            department= appConfigValue.get(0).getValue();
        return department;
    }
    
    public String getDesignationForThirdPartyUser() {
        String designation="";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "CLERKDESIGNATIONFORCSCOPERATOR");
        if(null!=appConfigValue && !appConfigValue.isEmpty())
            designation= appConfigValue.get(0).getValue();
        return designation;
    }

    public Boolean isEmailEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "SENDEMAILFORWATERTAX").get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isNewConnectionAllowedIfPTDuePresent() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "NEWCONNECTIONALLOWEDIFPTDUE").get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isMultipleNewConnectionAllowedForPID() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "MULTIPLENEWCONNECTIONFORPID").get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }
    
    public Boolean isConnectionAllowedIfWTDuePresent(final String connectionType) {
        Boolean isAllowed = false; 
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, connectionType);
        if(null!=appConfigValue && !appConfigValue.isEmpty())
            return "YES".equalsIgnoreCase(appConfigValue.get(0).getValue());
        
        return isAllowed;
    }

    public String documentRequiredForBPLCategory() {
        String documentName = null;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "DOCUMENTREQUIREDFORBPL");
        if (appConfigValue != null && !appConfigValue.isEmpty())
            documentName = appConfigValue.get(0).getValue();
        return documentName;
    }

    public String getCityName() {
        return cityService.getCityByURL(EgovThreadLocals.getDomainName()).getName();
    }

    public String getCityCode() {
        return cityService.getCityByURL(EgovThreadLocals.getDomainName()).getCode();
    }

    public String smsAndEmailBodyByCodeAndArgs(final String code, final WaterConnectionDetails waterConnectionDetails,
            final String applicantName) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { applicantName, waterConnectionDetails.getApplicationNumber(), getCityName() }, null);
        return smsMsg;
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(final String code,
            final WaterConnectionDetails waterConnectionDetails, final String applicantName) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                waterConnectionDetails.getConnection().getConsumerCode(), getCityName() }, null);
        return smsMsg;
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String applicationNumber) {
        final String emailSubject = messageSource.getMessage(code, new String[] { applicationNumber }, null);
        return emailSubject;
    }

    public void sendSMSOnWaterConnection(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(smsBody, "91" + mobileNumber);
    }

    public void sendEmailOnWaterConnection(final String email, final String emailBody, final String emailSubject) {
        messagingService.sendEmail(email, emailBody, emailSubject);
    }

    public Long getCityLevelCommissionerPosition(String commissionerDesgn) {
        // TODO: assuming only one Commissioner at ULB level
     if (!assignmentService.findPrimaryAssignmentForDesignationName(commissionerDesgn).isEmpty()) {
            final Assignment assgn = assignmentService.findPrimaryAssignmentForDesignationName(commissionerDesgn).get(0);
            return assgn.getPosition().getId();
        }
        return null;
    }

    public String getApprovalName(final Long approvalPosition) {
        Assignment assignment = null;
        String approverUser = "";
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        if (assignment != null && !assignment.equals("") && assignment.getEmployee() != null)
            approverUser = assignment.getEmployee().getUsername();
        return approverUser;
    }
    
    public EgwStatus getstatusbyCodeAndModuleType(String code,String moduleName) {
        return (EgwStatus) persistenceService.find("from EgwStatus where moduleType=? and lower(code)=?", moduleName,code.toLowerCase());
    }
    public Long getApproverPosition(final String designationname, final WaterConnectionDetails waterConnectionDetails) {

        final List<StateHistory> state = waterConnectionDetails.getState().getHistory();
        Long approvalPosition = 0l;
        if (state != null && !state.isEmpty()) {
            for (final StateHistory stateHistory : state) {
                final List<Assignment> assignmentList2 = assignmentService.getAssignmentsForPosition(stateHistory
                        .getOwnerPosition().getId(), new Date());
                for (final Assignment assgn : assignmentList2) {
                   if (assgn.getDesignation().getName().equals(designationname)) {
                        approvalPosition = stateHistory.getOwnerPosition().getId();
                        break;
                    }
                }
            }
            // TODO: just incase entry is not present in workflow histrory table
            // then checkinh in workflow state table;
            if (approvalPosition == 0) {
                final State stateObj = waterConnectionDetails.getState();
                final List<Assignment> assignmentList2 = assignmentService.getAssignmentsForPosition(stateObj
                        .getOwnerPosition().getId(), new Date());
                for (final Assignment assgn : assignmentList2)
                    if (assgn.getDesignation().getName().equals(designationname)) {
                        approvalPosition = stateObj.getOwnerPosition().getId();
                        break;
                    }
            }
        } else {
            // First after AE->CLERK need previous clerk User so by passing
            // workflow Initiator name
            final Position posObjToClerk = positionMasterService.getCurrentPositionForUser(waterConnectionDetails.
                    getCreatedBy().getId());
            approvalPosition = posObjToClerk.getId();
        }

        return approvalPosition;

    }
}
