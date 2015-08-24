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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public class WaterTaxUtils {

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DesignationService designationService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private UserService userService;

    public Boolean isSmsEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.SENDSMSFORWATERTAX).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public String getDepartmentForWorkFlow() {
        String department = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.WATERTAXWORKFLOWDEPARTEMENT);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return department;
    }

    public String getDesignationForThirdPartyUser() {
        String designation = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.CLERKDESIGNATIONFORCSCOPERATOR);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            designation = appConfigValue.get(0).getValue();
        return designation;
    }

    public List<AppConfigValues> getThirdPartyUserRoles() {

        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.ROLEFORNONEMPLOYEEINWATERTAX);

        return !appConfigValueList.isEmpty() ? appConfigValueList : null;

    }

    public Boolean getCurrentUserRole(final User currentUser) {
        Boolean applicationByOthers = false;
        for (final Role userrole : currentUser.getRoles())
            for (final AppConfigValues appconfig : getThirdPartyUserRoles()) {
                if (userrole != null && userrole.getName().equals(appconfig.getValue())) {
                    applicationByOthers = true;
                    break;
                }
                break;
            }
        return applicationByOthers;
    }

    public Boolean isEmailEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.SENDEMAILFORWATERTAX).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isNewConnectionAllowedIfPTDuePresent() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.NEWCONNECTIONALLOWEDIFPTDUE).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isMultipleNewConnectionAllowedForPID() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.MULTIPLENEWCONNECTIONFORPID).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isConnectionAllowedIfWTDuePresent(final String connectionType) {
        final Boolean isAllowed = false;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, connectionType);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            return "YES".equalsIgnoreCase(appConfigValue.get(0).getValue());

        return isAllowed;
    }

    public String documentRequiredForBPLCategory() {
        String documentName = null;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.DOCUMENTREQUIREDFORBPL);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            documentName = appConfigValue.get(0).getValue();
        return documentName;
    }

    public String getCityName() {
        return null != cityService.getCityByURL(EgovThreadLocals.getDomainName()).getPreferences()
                ? cityService.getCityByURL(EgovThreadLocals.getDomainName()).getPreferences().getMunicipalityName()
                : cityService.getCityByURL(EgovThreadLocals.getDomainName()).getName();
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

    public String EmailBodyByCodeAndArgsWithType(final String code,
            final WaterConnectionDetails waterConnectionDetails, final String applicantName, final String type) {
        String emailBody = "";
        final DecimalFormat amountFormat = new DecimalFormat("#.00");
        if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(), getCityName() }, null);
        // Dear {0},\n\nYour New tap connection application is accepted and the
        // acknowledgement number is {1}.\n \nPlease use this number
        // as reference in all your future transactions.\n\nThis is computer
        // generated Email and does not need any signature and also
        // please do not reply to this e-mail.\n\nThanks ,\n{2}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterConnectionDetails.getConnection().getConsumerCode(), getCityName() },
                    null);
        // Dear {0},\n\nThe water tap connection application with
        // Acknowledgement No. {1} has been approved with Consumer No. {2}
        // Monthly water tax will be generated after the tap execution.\n
        // \nPlease keep this Consumer Number for future transactions
        // on your water tap..\n\nThis is computer generated email and does not
        // need any signature and also please do not reply
        // to this e-mail.\n\nRegards,\n{3}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE)) {

            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                emailBody = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat
                                        .format(waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                        + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                getCityName() },
                        null);
            // Dear {0},\n\nWe have processed your application for new tap
            // connection with acknowledgement number {1}.and generated an
            // estimation
            // notice.\n\n Donation amount and Estimation amount for your
            // application will be Rs.{2}.00/-and Rs.{3}.00/- respectively .
            // We request you to pay the amount Rs.{4}.00/- ({2}+{3})at the ULB
            // counter. so that we can process your request for work
            // order.\n\nThis is computer generated Email and does not need any
            // signature and also please do not reply to this e-mail.\n\nThanks
            // ,\n{5}
            else
                emailBody = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails
                                        .getApplicationType().getCode()) ? "new water" : "additioanl water",
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                getCityName() },
                        null);
            // Dear {0},\n\nWe have processed your application for {1} tap
            // connection with acknowledgement number {2} and generated an
            // estimation notice.\n Estimation amount for your application will be Rs.{3}/-.
            // We request you to pay the same at the ULB counter,so that we can process
            // your request for work order.\n\nThis is computer generated email and does
            // not need any signature and also please do not reply to this email.\n\nThanks ,\n{4}
        } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION)|| type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION)) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (!WaterTaxConstants.METERED.toUpperCase()
                    .equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString()))
                emailBody = messageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                formatter.format(waterConnectionDetails.getExecutionDate()).toString(),
                                amountFormat.format(waterConnectionDetails.getDemand().getBaseDemand()).toString(),
                                getCityName() },
                        null);
            else
                emailBody = messageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                formatter.format(waterConnectionDetails.getExecutionDate()).toString(),
                                getCityName() },
                        null);
            // Dear {0},\n\nWater tap connection with H.S.C number {1} is installed at your site on {2} by our Asst engineer.
            // Please pay the tax before the due date to avail uninterrupted service.\n\nThis is computer generated email and
            // does not need any signature and also please do not reply to this email.\n\nThanks ,\n{3}
        }
        // Dear {0},\n\nWater tap connection with H.S.C number {1} is installed
        // at your site on {2} by our Asst engineer and your monthly
        // water tax demand will be Rs.{3}.00/-.Please pay the tax before the
        // due date to avail uninterrupted service.\n\nThis is computer
        // generated Email and does not need any signature and also please do
        // not reply to this e-mail.\n\nThanks ,\n{4}
        // TODO: while collectinge fees sending message not working with MessageSourse Cos Its not able to find Message.properties
        // so hardcoding sms and Email body
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID)) {
            final String amountToDisplay = String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                    + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges()));
            final StringBuffer emailBodyBuilder = new StringBuffer().append("Dear ").append(applicantName).append(",")
                    .append("\n\nWe have received Estimation and donation amount of Rs.").append(amountToDisplay)
                    .append("/- against your water connection application number ")
                    .append(waterConnectionDetails.getApplicationNumber())
                    .append(".We will be now processing your application to issue an work order.\n\nThis is computer generated email and does not need any signature and also please do not reply to this email.\n\nRegards,")
                    .append("\n").append(getCityName());
            emailBody = emailBodyBuilder.toString();
        } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                            waterConnectionDetails.getApplicationNumber(), getCityName() },
                    null);
        // Dear {0},\nYour application for change of use for the H.S.C number {1} is accepted and the acknowledgement number
        // is {2}.\n
        // Please use this number as reference in all your future transactions.\n\nThis is computer generated email and does
        // not need any
        // signature and also please do not reply to this email.\n\nThanks ,\n{3}
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE.equalsIgnoreCase(type))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(), getCityName() }, null);
        // Dear {0},\n\nYour application for change of use is accepted with acknowledgement No. {1} has been approved.
        // Your water tax will be generated after the tap execution.\n \nPlease keep this Application Number for future
        // transactions on your water tap..\n\nThis is computer generated email and does not need any signature and
        // also please do not reply to this email.\n\nRegards,\n{3}
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE.equalsIgnoreCase(type))
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                emailBody = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat
                                        .format(waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                        + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                getCityName() },
                        null);
            // Dear {0},\nWe have processed your application for change of use request with acknowledgement number {1}.and
            // generated an
            // estimation notice.\n\n Donation amount and Estimation amount for your
            // application will be Rs.{2}.00/-and Rs.{3}.00/- respectively .
            // We request you to pay the amount Rs.{4}.00/- ({2}+{3})at the ULB
            // counter. so that we can process your request for work
            // order.\n\nThis is computer generated Email and does not need any
            // signature and also please do not reply to this e-mail.\n\nThanks
            // ,\n{5}
            else
                emailBody = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                "change of",
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                getCityName() },
                        null);
        // Dear {0},\n\nWe have processed your application for {1} tap
        // connection with acknowledgement number {2} and generated an
        // estimation notice.\n Estimation amount for your application will be Rs.{3}/-.
        // We request you to pay the same at the ULB counter,so that we can process
        // your request for work order.\n\nThis is computer generated email and does
        // not need any signature and also please do not reply to this email.\n\nThanks ,\n{4}

        return emailBody;
    }

    public String SmsBodyByCodeAndArgsWithType(final String code, final WaterConnectionDetails waterConnectionDetails,
            final String applicantName, final String type) {
        String smsMsg = "";
        final DecimalFormat amountFormat = new DecimalFormat("#.00");
        if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(), getCityName() }, null);
        // Dear {0}, Your New tap connection application is accepted and the
        // acknowledgement number is {1}. Please use this number as
        // reference in all your future transactions.\nThanks, {2}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE))
            smsMsg = messageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(), getCityName() }, null);
        // Dear {0}, Your new water tap connection application processed with
        // Consumer No.{1}. Monthly water tax will be generated after
        // the tap execution..\nThanks, {2}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE)) {
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                        + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                getCityName() },
                        null);
            // --- for NON BPL --
            // Dear {0}, We have processed your application for new tap
            // connection with acknowledgement number {1} and generated an estimation
            // notice.\n Donation amount and Estimation amount for your application will
            // be Rs.{2}.00/-and Rs.{3}.00/- respectively .We request you to pay
            // the amount Rs.{4}.00/- ({2}+{3})at the ULB counter. so that we can
            // process your request for work order.\nThanks, {5}
            else
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails
                                        .getApplicationType().getCode()) ? "new water" : "additional water",
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                getCityName() },
                        null);
            // -- for BPL --
            // Dear {0}, We have processed your application for {1} tap
            // connection with acknowledgement number {2} and generated an
            // estimation notice.\n\n Estimation amount for your application
            // will be Rs.{3}/-. We request you to pay the same at the
            // ULB counter,so that we can process your request for work
            // order.\nThanks, {4}
        } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION)) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (!WaterTaxConstants.METERED.toUpperCase()
                    .equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString()))
                smsMsg = messageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                formatter.format(waterConnectionDetails.getExecutionDate()).toString(),
                                amountFormat.format(waterConnectionDetails.getDemand().getBaseDemand()).toString(),
                                getCityName() },
                        null);
            else
                smsMsg = messageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                formatter.format(waterConnectionDetails.getExecutionDate()).toString(), getCityName() },
                        null);
            // Dear {0}, Water tap connection with H.S.C number {1} is installed at your site on {2} by our Asst engineer.
            // Please pay the tax before the due date to avail uninterrupted service.\nThanks, {3}
        }
        // Dear {0}, Water tap connection with H.S.C number {1} is installed at
        // your site on {2} by our Asst engineer and your
        // demand will be Rs.{3}.00/-.Please pay the tax before the due date
        // to avail uninterrupted service.\nThanks, {4}
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID)) {
            final String amountToDisplay = String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                    + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges()));
            final StringBuffer smsBody = new StringBuffer().append("Dear ").append(applicantName)
                    .append(",We have received Estimation and donation amount of Rs.").append(amountToDisplay)
                    .append("/- against your water connection application number ")
                    .append(waterConnectionDetails.getApplicationNumber())
                    .append(".We will be now processing your application to issue an work order..\nThanks,\n")
                    .append(getCityName());
            smsMsg = smsBody.toString();
        } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                            waterConnectionDetails.getApplicationNumber(), getCityName() },
                    null);
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE.equalsIgnoreCase(type))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(), getCityName() }, null);
        // Dear {0}, Your application request for change of use is accepted with acknowledgement No.{1}.
        // Please use this number in all future communication..\nThanks, {2}
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE.equalsIgnoreCase(type))
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                        + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                getCityName() },
                        null);
            // Dear {0}, We have processed your application for change of use request with acknowledgement number {1}
            // and generated an estimation notice.\n\n Donation amount and Estimation amount for your application will
            // be Rs.{2}/- and Rs.{3}/- respectively. We request you to pay the amount Rs.{4}/- ({2}+{3}) at the ULB
            // counter, so that we can process your request for work order.\nThanks, {5}
            else
                smsMsg = messageSource.getMessage(
                        code,
                        new String[] {
                                applicantName,
                                "change of",
                                waterConnectionDetails.getApplicationNumber(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getFieldInspectionDetails()
                                        .getEstimationCharges())),
                                getCityName() },
                        null);
        // -- for BPL --
        // Dear {0}, We have processed your application for {1} tap
        // connection with acknowledgement number {2} and generated an
        // estimation notice.\n\n Estimation amount for your application
        // will be Rs.{3}/-. We request you to pay the same at the
        // ULB counter,so that we can process your request for work
        // order.\nThanks, {4}
        return smsMsg;
    }

    public String smsAndEmailBodyByCodeAndArgsForRejection(final String code, final String approvalComment,
            final String applicantName) {
        final String smsMsg = messageSource.getMessage(code, new String[] { applicantName, approvalComment,
                getCityName() }, null);
        return smsMsg;
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(final String code,
            final WaterConnectionDetails waterConnectionDetails, final String applicantName) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                        waterConnectionDetails.getConnection().getConsumerCode(), getCityName() },
                null);
        return smsMsg;
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String applicationNumber) {
        final String emailSubject = messageSource.getMessage(code, new String[] { applicationNumber }, null);
        return emailSubject;
    }

    public void sendSMSOnWaterConnection(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnWaterConnection(final String email, final String emailBody, final String emailSubject) {
        messagingService.sendEmail(email, emailSubject, emailBody);
    }

    public Position getCityLevelCommissionerPosition(final String commissionerDesgn) {
        return assignmentService.findPrimaryAssignmentForDesignationName(commissionerDesgn).get(0).getPosition();
    }

    public String getApproverUserName(final Long approvalPosition) {
        Assignment assignment = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        return assignment != null ? assignment.getEmployee().getUsername() : "";
    }

    public EgwStatus getStatusByCodeAndModuleType(final String code, final String moduleName) {
        return (EgwStatus) persistenceService.find("from EgwStatus where moduleType=? and code=?", moduleName, code);
    }

    public Long getApproverPosition(final String designationName, final WaterConnectionDetails waterConnectionDetails) {

        final List<StateHistory> stateHistoryList = waterConnectionDetails.getState().getHistory();
        Long approverPosition = 0l;
        if (stateHistoryList != null && !stateHistoryList.isEmpty()) {
            for (final StateHistory stateHistory : stateHistoryList) {
                final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(stateHistory
                        .getOwnerPosition().getId(), new Date());
                for (final Assignment assgn : assignmentList)
                    if (assgn.getDesignation().getName().equals(designationName)) {
                        approverPosition = stateHistory.getOwnerPosition().getId();
                        break;
                    }
            }
            if (approverPosition == 0) {
                final State stateObj = waterConnectionDetails.getState();
                final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(stateObj
                        .getOwnerPosition().getId(), new Date());
                for (final Assignment assgn : assignmentList)
                    if (assgn.getDesignation().getName().equals(designationName)) {
                        approverPosition = stateObj.getOwnerPosition().getId();
                        break;
                    }
            }
        } else {
            final Position posObjToClerk = positionMasterService.getCurrentPositionForUser(waterConnectionDetails
                    .getCreatedBy().getId());
            approverPosition = posObjToClerk.getId();
        }

        return approverPosition;

    }

    public Position getZonalLevelClerkForLoggedInUser(final String asessmentNumber) {
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS);
        Assignment assignmentObj = null;
        final BoundaryType boundaryTypeObj = boundaryTypeService.getBoundaryTypeByName(assessmentDetails
                .getBoundaryDetails().getZoneBoundaryType());
        final Boundary boundaryObj = boundaryService.getBoundaryByTypeAndNo(boundaryTypeObj, assessmentDetails
                .getBoundaryDetails().getZoneNumber());
        final Designation desgnObj = designationService.getDesignationByName(getDesignationForThirdPartyUser());
        final Department deptObj = departmentService.getDepartmentByName(getDepartmentForWorkFlow());
        final List<Employee> employeeList = employeeService.findByDepartmentDesignationAndBoundary(deptObj.getId(),
                desgnObj.getId(), boundaryObj.getId());
        if (!employeeList.isEmpty())
            assignmentObj = assignmentService.getPrimaryAssignmentForEmployee(employeeList.get(0).getId());
        return assignmentObj != null ? assignmentObj.getPosition() : null;
    }

    @ModelAttribute(value = "checkOperator")
    public Boolean checkCollectionOperatorRole() {
        Boolean isCSCOperator = false;
        final User userObj = userService.getUserById(EgovThreadLocals.getUserId());
        if (userObj != null)
            for (final Role role : userObj.getRoles())
                if (role != null && role.getName().contains(WaterTaxConstants.CSCOPERTAORROLE)) {
                    isCSCOperator = true;
                    break;
                }
        return isCSCOperator;
    }
}
