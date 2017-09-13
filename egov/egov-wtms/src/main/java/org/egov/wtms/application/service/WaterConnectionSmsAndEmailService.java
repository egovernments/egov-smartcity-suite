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

import static org.egov.wtms.utils.constants.WaterTaxConstants.APPCONFIGVALUEOFENABLED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_APPROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ENABLEDIGITALSIGNATURE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPEADDITONALCONNDIGISIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECONNDIGISIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPENEWCONNDIGISIGN;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.service.AppConfigService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WaterConnectionSmsAndEmailService {

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private AppConfigService appConfigService;

    private String applicantName;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource wcmsMessageSource;

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    /**
     * @param waterConnectionDetails
     * @return applicantName from Assessment
     */
    public String getApplicantNameBYAssessmentDetail(final WaterConnectionDetails waterConnectionDetails) {
        final AssessmentDetails assessmentDetailsfullFlag = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);

        Iterator<OwnerName> ownerNameItr = null;

        if (null != assessmentDetailsfullFlag.getOwnerNames())
            ownerNameItr = assessmentDetailsfullFlag.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        if (null != ownerNameItr && ownerNameItr.hasNext()) {
            consumerName.append(ownerNameItr.next().getOwnerName());
            while (ownerNameItr.hasNext())
                consumerName.append(", ".concat(ownerNameItr.next().getOwnerName()));
        }
        applicantName = consumerName.toString();
        return applicantName;
    }

    /**
     * @return this method will send SMS and Email is isSmsEnabled is true
     * @param waterConnectionDetails
     * @param workFlowAction
     */
    public void sendSmsAndEmail(final WaterConnectionDetails waterConnectionDetails, final String workFlowAction) {
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_MOBILE_EMAIL, BasicPropertyStatus.ALL);
        final String email = assessmentDetails.getPrimaryEmail();
        final String mobileNumber = assessmentDetails.getPrimaryMobileNo();
        if (waterConnectionDetails != null && waterConnectionDetails.getApplicationType() != null
                && waterConnectionDetails.getApplicationType().getCode() != null
                && waterConnectionDetails.getStatus() != null && waterConnectionDetails.getStatus().getCode() != null) {

            getApplicantNameBYAssessmentDetail(waterConnectionDetails);

            // SMS and Email for new connection
            if (WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForNewConnection(waterConnectionDetails, email, mobileNumber);
            else if (WaterTaxConstants.ADDNLCONNECTION
                    .equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForAdditionalConnection(waterConnectionDetails, email, mobileNumber);
            else if (WaterTaxConstants.CHANGEOFUSE
                    .equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForChangeOfUsageConnection(waterConnectionDetails, email, mobileNumber);
            else if (WaterTaxConstants.CLOSINGCONNECTION
                    .equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForClosingConnection(waterConnectionDetails, email, mobileNumber);
            else if (WaterTaxConstants.RECONNECTIONCONNECTION
                    .equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForReConnection(waterConnectionDetails, email, mobileNumber);

        }
    }

    /**
     * @return SMS AND EMAIL body and subject For Change of Usage Connection
     * @param waterConnectionDetails
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */
    private void getSmsAndEmailForChangeOfUsageConnection(final WaterConnectionDetails waterConnectionDetails,
            final String email, final String mobileNumber) {
        String smsMsg = null;
        String body = "";
        String subject = "";
        Boolean isDigitalSignatureEnabled = false;
        final AppConfig appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(MODULE_NAME, ENABLEDIGITALSIGNATURE);
        if (APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfig.getConfValues().get(0).getValue()))
            isDigitalSignatureEnabled = true;
        if (waterConnectionDetails.getState().getHistory().isEmpty() && WaterTaxConstants.APPLICATION_STATUS_CREATED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.changeofuseconncetioncreate.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE);
            body = emailBodyByCodeAndArgsWithType("msg.changeofuseconncetioncreate.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.changeofuseconncetioncreate.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (!isDigitalSignatureEnabled && APPLICATION_STATUS_DIGITALSIGNPENDING
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.changeofuseconnection.approval.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.changeofuseconnection.approval.email.body",
                    waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.changeofuseconnection.approval.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (isDigitalSignatureEnabled
                && APPLICATION_STATUS_APPROVED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.changeofusedigitalsigned.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECHANGEOFUSECONNDIGISIGN);
            body = emailBodyByCodeAndArgsWithType("msg.changeofusedigitalsigned.email.body",
                    waterConnectionDetails, applicantName, SMSEMAILTYPECHANGEOFUSECONNDIGISIGN);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.changeofusedigitalsigned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.changeofuseconnection.notice.gen", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.changeofuseconnection.notice.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                        "msg.changeofuseconnection.notice.email.subject",
                        waterConnectionDetails.getApplicationNumber());
            } else {
                body = emailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.noticegen.for.bpl.email.subject",
                        waterConnectionDetails.getApplicationNumber());

                smsMsg = smsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE);
            }

        } else if (WaterTaxConstants.APPLICATION_STATUS_FEEPAID
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnFeesPaid.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID);
            body = emailBodyByCodeAndArgsWithType("msg.addconncetionOnfeespaid.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID);
            final StringBuilder emailSubject = new StringBuilder(
                    " Demand and donation amount received for water tax application ");
            emailSubject.append(waterConnectionDetails.getApplicationNumber());
            subject = emailSubject.toString();

        } else if (WaterTaxConstants.APPLICATION_STATUS_SANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {

            if (!WaterTaxConstants.METERED.toUpperCase()
                    .equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION);
                body = emailBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION);
            } else {
                body = emailBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION);
                smsMsg = smsBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION);
            }
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetionOnExecutionDate.email.subject",
                    waterConnectionDetails.getConnection().getConsumerCode());
        }
        if (mobileNumber != null && smsMsg != null)
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);

    }

    /**
     * @return SMS AND EMAIL body and subject For New Connection
     * @param waterConnectionDetails
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */
    private void getSmsAndEmailForAdditionalConnection(final WaterConnectionDetails waterConnectionDetails,
            final String email, final String mobileNumber) {
        String smsMsg = null;
        String body = "";
        String subject = "";
        Boolean isDigitalSignatureEnabled = false;
        final AppConfig appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(MODULE_NAME, ENABLEDIGITALSIGNATURE);
        if (APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfig.getConfValues().get(0).getValue()))
            isDigitalSignatureEnabled = true;
        if (waterConnectionDetails.getState().getHistory().isEmpty() && WaterTaxConstants.APPLICATION_STATUS_CREATED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.additionalconncetioncreate.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE);
            body = emailBodyByCodeAndArgsWithType("msg.additionalconnectioncreate.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.additionalconnectioncreate.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (!isDigitalSignatureEnabled && APPLICATION_STATUS_DIGITALSIGNPENDING
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.additionalconncetionapproval.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.additionalconncetionapproval.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.additionalconncetionapproval.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (isDigitalSignatureEnabled
                && APPLICATION_STATUS_APPROVED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.additionalconnectiondigitalsigned.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNDIGISIGN);
            body = emailBodyByCodeAndArgsWithType("msg.additionalconnectiondigitalsigned.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPEADDITONALCONNDIGISIGN);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.additionalconnectiondigitalsigned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.addconncetionOnGenerateNotice.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.addconncetionOnGenerateNotice.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                        "msg.conncetionOnGenerateNotice.email.subject", waterConnectionDetails.getApplicationNumber());
            } else {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.noticegen.for.bpl.email.subject",
                        waterConnectionDetails.getApplicationNumber());
            }

        } else if (WaterTaxConstants.APPLICATION_STATUS_FEEPAID
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnFeesPaid.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID);
            body = emailBodyByCodeAndArgsWithType("msg.addconncetionOnfeespaid.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID);
            final StringBuilder emailSubject = new StringBuilder(
                    " Demand and donation amount received for water tax application ");
            emailSubject.append(waterConnectionDetails.getApplicationNumber());
            subject = emailSubject.toString();
        } else if (WaterTaxConstants.APPLICATION_STATUS_SANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (!WaterTaxConstants.METERED.toUpperCase()
                    .equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION);
                body = emailBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION);
            } else {
                body = emailBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION);

                smsMsg = smsBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION);
            }
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetionOnExecutionDate.email.subject",
                    waterConnectionDetails.getConnection().getConsumerCode());
        }
        if (mobileNumber != null && smsMsg != null)
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
    }

    /**
     * @return SMS AND EMAIL body and subject For New Connection
     * @param waterConnectionDetails
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */
    public void getSmsAndEmailForNewConnection(final WaterConnectionDetails waterConnectionDetails, final String email,
            final String mobileNumber) {
        String smsMsg = null;
        String body = "";
        String subject = "";
        Boolean isDigitalSignatureEnabled = false;
        final AppConfig appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(MODULE_NAME, ENABLEDIGITALSIGNATURE);
        if (APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfig.getConfValues().get(0).getValue()))
            isDigitalSignatureEnabled = true;

        if (waterConnectionDetails.getState().getHistory().isEmpty() && WaterTaxConstants.APPLICATION_STATUS_CREATED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetioncreate.sms", waterConnectionDetails, applicantName,
                    WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE);
            body = emailBodyByCodeAndArgsWithType("msg.newconncetioncreate.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetioncreate.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (!isDigitalSignatureEnabled && WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionapproval.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.newconncetionapproval.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetionapprove.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnGenerateNotice.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.newconncetionOnGenerateNotice.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                        "msg.conncetionOnGenerateNotice.email.subject", waterConnectionDetails.getApplicationNumber());
            } else {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.noticegen.for.bpl.email.subject",
                        waterConnectionDetails.getApplicationNumber());
            }

        } else if (isDigitalSignatureEnabled
                && APPLICATION_STATUS_APPROVED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconnectiondigitalsigned.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNDIGISIGN);
            body = emailBodyByCodeAndArgsWithType("msg.newconnectiondigitalsigned.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNDIGISIGN);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconnectiondigitalsigned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (WaterTaxConstants.APPLICATION_STATUS_FEEPAID
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnFeesPaid.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID);
            body = emailBodyByCodeAndArgsWithType("msg.addconncetionOnfeespaid.email.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID);
            final StringBuilder emailSubject = new StringBuilder(
                    " Demand and donation amount received for water tax application ");
            emailSubject.append(waterConnectionDetails.getApplicationNumber());
            subject = emailSubject.toString();

        } else if (WaterTaxConstants.APPLICATION_STATUS_SANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (!WaterTaxConstants.METERED.toUpperCase()
                    .equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION);
                body = emailBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION);
            } else {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.sms", waterConnectionDetails,
                        applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION);
                body = emailBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.email.body",
                        waterConnectionDetails, applicantName, WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION);
            }
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetionOnExecutionDate.email.subject",
                    waterConnectionDetails.getConnection().getConsumerCode());
        }
        if (mobileNumber != null && smsMsg != null)
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
    }

    /**
     * @param waterConnectionDetails
     * @param approvalComent
     * @return SMS AND EMAIL body and subject For Rejection in Cancelled status
     */
    public void sendSmsAndEmailOnRejection(final WaterConnectionDetails waterConnectionDetails,
            final String approvalComent) {
        if ((waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                || waterConnectionDetails.getApplicationType().getCode()
                        .equalsIgnoreCase(WaterTaxConstants.ADDNLCONNECTION)
                || waterConnectionDetails.getApplicationType().getCode()
                        .equalsIgnoreCase(WaterTaxConstants.CHANGEOFUSE))
                && waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_CANCELLED)) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_MOBILE_EMAIL, BasicPropertyStatus.ALL);
            final String email = assessmentDetails.getPrimaryEmail();
            final String mobileNumber = assessmentDetails.getPrimaryMobileNo();
            getApplicantNameBYAssessmentDetail(waterConnectionDetails);
            String smsMsg = "";
            String body = "";
            String subject = "";
            if (waterTaxUtils.isSmsEnabled() && mobileNumber != null) {
                if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)) {
                    final StringBuilder smsBody = new StringBuilder().append("Dear ").append(applicantName)
                            .append(",Your new water tap connection application is being rejected and ")
                            .append("the reason for rejection ").append(approvalComent)
                            .append(" Please get in touch with ULB official to raise a new application with proper information to get a water connection.")
                            .append("\nThanks,").append(waterTaxUtils.getMunicipalityName());

                    smsMsg = smsBody.toString();
                } else if (waterConnectionDetails.getApplicationType().getCode()
                        .equals(WaterTaxConstants.ADDNLCONNECTION)) {
                    final StringBuilder smsBody = new StringBuilder().append("Dear ").append(applicantName)
                            .append(",Your additional water tap connection application is being rejected and ")
                            .append("the reason for rejection ").append(approvalComent)
                            .append(" Please get in touch with ULB official to raise a new application with proper information to get a water connection.")
                            .append("\nThanks,").append(waterTaxUtils.getMunicipalityName());
                    smsMsg = smsBody.toString();

                } else if (waterConnectionDetails.getApplicationType().getCode()
                        .equals(WaterTaxConstants.CHANGEOFUSE)) {
                    final StringBuilder smsBody = new StringBuilder().append("Dear ").append(applicantName)
                            .append(",Your change of use  connection application is being rejected and ")
                            .append("the reason for rejection ").append(approvalComent)
                            .append(" Please get in touch with ULB official to raise a new application with proper information to get a water connection.")
                            .append("\nThanks,").append(waterTaxUtils.getMunicipalityName());
                    smsMsg = smsBody.toString();
                }
                waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
            }

            if (waterTaxUtils.isSmsEnabled() && email != null) {
                if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)) {

                    final StringBuilder bodyMsg = new StringBuilder().append("Dear ").append(applicantName)
                            .append(",\n\nYour new water tap connection application is being rejected and the reason for rejection ")
                            .append(approvalComent)
                            .append("\n \n Please get in touch with ULB official to raise a new application with proper information to get a water connection.")
                            .append("\n\nThis is computer generated email and does not need any signature and also please  do not reply to this email.")
                            .append("\n\nThanks ,\n").append(waterTaxUtils.getMunicipalityName());

                    final StringBuilder subjectMsg = new StringBuilder().append("Water tap connection application")
                            .append(waterConnectionDetails.getApplicationNumber()).append("rejected.");

                    body = bodyMsg.toString();

                    subject = subjectMsg.toString();

                } else if (waterConnectionDetails.getApplicationType().getCode()
                        .equals(WaterTaxConstants.ADDNLCONNECTION)) {
                    final StringBuilder bodyMsg = new StringBuilder().append("Dear ").append(applicantName)
                            .append(",\n\nYour Additional water tap connection application is being rejected and the reason for rejection ")
                            .append(approvalComent)
                            .append("\n \n Please get in touch with ULB official to raise a new application with proper information to get a water connection.")
                            .append("\n\nThis is computer generated email and does not need any signature and also please  do not reply to this email.")
                            .append("\n\nThanks ,\n").append(waterTaxUtils.getMunicipalityName());
                    body = bodyMsg.toString();

                    final StringBuilder subjectMsg = new StringBuilder().append("Water tap connection application")
                            .append(waterConnectionDetails.getApplicationNumber()).append("rejected.");
                    subject = subjectMsg.toString();

                } else if (waterConnectionDetails.getApplicationType().getCode()
                        .equals(WaterTaxConstants.CHANGEOFUSE)) {
                    final StringBuilder bodyMsg = new StringBuilder().append("Dear ").append(applicantName)
                            .append(",\n\nYour Change Of use water tap connection application is being rejected and the reason for rejection ")
                            .append(approvalComent)
                            .append("\n \n Please get in touch with ULB official to raise a new application with proper information to get a water connection.")
                            .append("\n\nThis is computer generated email and does not need any signature and also please  do not reply to this email.")
                            .append("\n\nThanks ,\n").append(waterTaxUtils.getMunicipalityName());
                    body = bodyMsg.toString();
                    final StringBuilder subjectMsg = new StringBuilder().append("Water tap connection application")
                            .append(waterConnectionDetails.getApplicationNumber()).append("rejected.");
                    subject = subjectMsg.toString();

                }
                waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
            }
        }
    }

    public String smsAndEmailBodyByCodeAndArgs(final String code, final WaterConnectionDetails waterConnectionDetails,
            final String applicantName) {
        final String smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        return smsMsg;
    }

    /**
     * @param code
     * @param waterConnectionDetails
     * @param applicantName
     * @param type
     * @return EmailBody for All Connection based on Type Removes Commented Email Body As per Code Review
     */
    public String emailBodyByCodeAndArgsWithType(final String code, final WaterConnectionDetails waterConnectionDetails,
            final String applicantName, final String type) {
        String emailBody = "";
        final BigDecimal waterTaxDue = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
        final DecimalFormat amountFormat = new DecimalFormat("#.00");
        if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE))
            emailBody = wcmsMessageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterConnectionDetails.getConnection().getConsumerCode(),
                            waterTaxUtils.getMunicipalityName() },
                    null);
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE)
                || WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE.equalsIgnoreCase(type)) {

            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                emailBody = wcmsMessageSource.getMessage(code,
                        new String[] { applicantName,
                                waterConnectionDetails.getApplicationNumber(),
                                waterConnectionDetails.getApplicationType().getName(),
                                ApplicationThreadLocals.getDomainURL(),
                                waterTaxUtils.getMunicipalityName(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(
                                        waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                        + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())) },
                        null);
            else
                emailBody = wcmsMessageSource
                        .getMessage(code,
                                new String[] { applicantName,
                                        waterConnectionDetails.getApplicationNumber(),
                                        waterConnectionDetails.getApplicationType().getName(),
                                        ApplicationThreadLocals.getDomainURL(),
                                        waterTaxUtils.getMunicipalityName(),
                                        String.valueOf(amountFormat.format(waterConnectionDetails
                                                .getFieldInspectionDetails().getEstimationCharges())) },
                                null);
        } else if (SMSEMAILTYPENEWCONNDIGISIGN.equalsIgnoreCase(type) ||
                SMSEMAILTYPEADDITONALCONNDIGISIGN.equalsIgnoreCase(type) ||
                SMSEMAILTYPECHANGEOFUSECONNDIGISIGN.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(), waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(), waterTaxUtils.getMunicipalityName() }, null);
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION)) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (!WaterTaxConstants.METERED.toUpperCase()
                    .equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString()))
                emailBody = wcmsMessageSource
                        .getMessage(code,
                                new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                        formatter.format(waterConnectionDetails.getExecutionDate()),
                                        amountFormat.format(waterTaxUtils.getCurrentDemand(waterConnectionDetails)
                                                .getDemand() != null ? waterTaxUtils.getCurrentDemand(waterConnectionDetails)
                                                        .getDemand().getBaseDemand() : BigDecimal.ZERO),
                                        waterTaxUtils.getMunicipalityName() },
                                null);
            else
                emailBody = wcmsMessageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                formatter.format(waterConnectionDetails.getExecutionDate()).toString(),
                                waterTaxUtils.getMunicipalityName() },
                        null);
        } else if ((type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID))
                && waterTaxDue.compareTo(BigDecimal.ZERO) == 0) {
            final String amountToDisplay = String
                    .valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                            + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges()));
            final StringBuilder emailBodyBuilder = new StringBuilder().append("Dear ").append(applicantName).append(",")
                    .append("\n\nWe have received Estimation and donation amount of Rs.").append(amountToDisplay)
                    .append("/- against your water connection application number ")
                    .append(waterConnectionDetails.getApplicationNumber())
                    .append(". We will be now processing your application to issue an work order.\n\nThis is computer generated email and does not need any signature and also please do not reply to this email.\n\nRegards,")
                    .append("\n").append(waterTaxUtils.getMunicipalityName());
            emailBody = emailBodyBuilder.toString();
        } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource
                    .getMessage(code,
                            new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                    waterConnectionDetails.getApplicationNumber(),
                                    waterTaxUtils.getMunicipalityName() },
                            null);
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(), waterTaxUtils.getMunicipalityName() },
                    null);
        else if (WaterTaxConstants.SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (WaterTaxConstants.SMSEMAILTYPECLOSINGCONNSANCTIONED.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (WaterTaxConstants.SMSEMAILTYPERECONNECTIONAPPROVE.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (WaterTaxConstants.SMSEMAILTYPERECONNECTIONSANCTIONED.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        return emailBody;
    }

    /**
     * @param code
     * @param waterConnectionDetails
     * @param applicantName
     * @param type Removes Commented SMS Format As per Code Review ..
     */

    public String smsBodyByCodeAndArgsWithType(final String code, final WaterConnectionDetails waterConnectionDetails,
            final String applicantName, final String type) {
        String smsMsg = "";
        final BigDecimal waterTaxDue = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
        final DecimalFormat amountFormat = new DecimalFormat("#.00");
        if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE))
            smsMsg = wcmsMessageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterConnectionDetails.getConnection().getConsumerCode(),
                            waterTaxUtils.getMunicipalityName() },
                    null);

        else if (SMSEMAILTYPEADDITONALCONNDIGISIGN.equalsIgnoreCase(type) ||
                SMSEMAILTYPENEWCONNDIGISIGN.equalsIgnoreCase(type) ||
                SMSEMAILTYPECHANGEOFUSECONNDIGISIGN.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                            ApplicationThreadLocals.getDomainURL(), waterConnectionDetails.getApplicationNumber(),
                            waterTaxUtils.getMunicipalityName() },
                    null);
        else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE)
                || WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE.equalsIgnoreCase(type)) {
            if (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                smsMsg = wcmsMessageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                                waterConnectionDetails.getApplicationType().getName(),
                                ApplicationThreadLocals.getDomainURL(),
                                waterTaxUtils.getMunicipalityName(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(
                                        waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                        + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())) },
                        null);
            else
                smsMsg = wcmsMessageSource
                        .getMessage(code,
                                new String[] { applicantName,
                                        waterConnectionDetails.getApplicationNumber(),
                                        waterConnectionDetails.getApplicationType().getName(),
                                        ApplicationThreadLocals.getDomainURL(),
                                        waterTaxUtils.getMunicipalityName(),
                                        String.valueOf(amountFormat.format(waterConnectionDetails
                                                .getFieldInspectionDetails().getEstimationCharges())) },
                                null);
        } else if (type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION)) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (!WaterTaxConstants.METERED.toUpperCase()
                    .equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString()))
                smsMsg = wcmsMessageSource
                        .getMessage(code,
                                new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                        formatter.format(waterConnectionDetails.getExecutionDate()),
                                        amountFormat
                                                .format(waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand() != null
                                                        ? waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand()
                                                                .getBaseDemand()
                                                        : BigDecimal.ZERO),
                                        waterTaxUtils.getMunicipalityName() },
                                null);
            else
                smsMsg = wcmsMessageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                formatter.format(waterConnectionDetails.getExecutionDate()),
                                waterTaxUtils.getMunicipalityName() },
                        null);
        } else if ((type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID)
                || type.equalsIgnoreCase(WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID))
                && waterTaxDue.compareTo(BigDecimal.ZERO) == 0) {
            final String amountToDisplay = String
                    .valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                            + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges()));
            final StringBuilder smsBody = new StringBuilder().append("Dear ").append(applicantName)
                    .append(",We have received Estimation and donation amount of Rs.").append(amountToDisplay)
                    .append("/- against your water connection application number ")
                    .append(waterConnectionDetails.getApplicationNumber())
                    .append(". We will be now processing your application to issue an work order.\nThanks,\n")
                    .append(waterTaxUtils.getMunicipalityName());
            smsMsg = smsBody.toString();
        } else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource
                    .getMessage(code,
                            new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                    waterConnectionDetails.getApplicationNumber(),
                                    waterTaxUtils.getMunicipalityName() },
                            null);
        else if (WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(), waterTaxUtils.getMunicipalityName() },
                    null);
        else if (WaterTaxConstants.SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (WaterTaxConstants.SMSEMAILTYPECLOSINGCONNSANCTIONED.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (WaterTaxConstants.SMSEMAILTYPERECONNECTIONAPPROVE.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (WaterTaxConstants.SMSEMAILTYPERECONNECTIONSANCTIONED.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        return smsMsg;
    }

    public void getSmsAndEmailForClosingConnection(final WaterConnectionDetails waterConnectionDetails,
            final String email, final String mobileNumber) {
        String smsMsg = null;
        String body = "";
        String subject = "";
        if (WaterTaxConstants.APPLICATION_STATUS_CLOSERAPRROVED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.closeconnection.approve.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECLOSINGCONNAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.closeconnection.approve.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECLOSINGCONNAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.closeofconnectionapprove.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {

            smsMsg = smsBodyByCodeAndArgsWithType("msg.closeconnection.sanctioned.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECLOSINGCONNSANCTIONED);
            body = emailBodyByCodeAndArgsWithType("msg.closeconnection.sanctioned.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPECLOSINGCONNSANCTIONED);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.closeofconnectionsanctioned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        }
        if (mobileNumber != null && smsMsg != null)
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
    }

    public void getSmsAndEmailForReConnection(final WaterConnectionDetails waterConnectionDetails, final String email,
            final String mobileNumber) {
        String smsMsg = null;
        String body = "";
        String subject = "";
        if (WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONAPPROVED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.reconnection.approve.sms", waterConnectionDetails, applicantName,
                    WaterTaxConstants.SMSEMAILTYPERECONNECTIONAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.reconnection.approve.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPERECONNECTIONAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.reconnectionapprove.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONSANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {

            smsMsg = smsBodyByCodeAndArgsWithType("msg.reconnection.sanctioned.sms", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPERECONNECTIONSANCTIONED);
            body = emailBodyByCodeAndArgsWithType("msg.reconnection.sanctioned.body", waterConnectionDetails,
                    applicantName, WaterTaxConstants.SMSEMAILTYPERECONNECTIONSANCTIONED);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.reconnectionsanctioned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        }
        if (mobileNumber != null && smsMsg != null)
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
    }

}