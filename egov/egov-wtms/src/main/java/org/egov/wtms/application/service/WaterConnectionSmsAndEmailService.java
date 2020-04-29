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

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPCONFIGVALUEOFENABLED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_APPROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CANCELLED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNCTIONSANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_SANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.BPL_CATEGORY;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ENABLEDIGITALSIGNATURE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NON_METERED_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPEADDCONNESTNOTICE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPEADDCONNFEEPAID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPEADDITONALCONNAPPROVE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPEADDITONALCONNCREATE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPEADDITONALCONNDIGISIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEAPPROVE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECONNDIGISIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECHANGEOFUSECREATE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEEXECUTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECHANGEOFUSEFEEPAID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECHANGEOFUSENOTICE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECLOSINGCONNAPPROVE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPECLOSINGCONNSANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPENEWCONNAPPROVE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPENEWCONNCREATE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPENEWCONNDIGISIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPENEWCONNESTNOTICE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPENEWCONNEXECUTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPENEWCONNFEEPAID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPERECONNECTIONAPPROVE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SMSEMAILTYPERECONNECTIONSANCTIONED;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
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
        if (waterConnectionDetails.getApplicationType() != null
                && waterConnectionDetails.getApplicationType().getCode() != null
                && waterConnectionDetails.getStatus() != null && waterConnectionDetails.getStatus().getCode() != null) {

            getApplicantNameBYAssessmentDetail(waterConnectionDetails);

            // SMS and Email for new connection
            if (NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForNewConnection(waterConnectionDetails, email, mobileNumber);
            else if (ADDNLCONNECTION
                    .equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForAdditionalConnection(waterConnectionDetails, email, mobileNumber);
            else if (CHANGEOFUSE
                    .equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForChangeOfUsageConnection(waterConnectionDetails, email, mobileNumber);
            else if (CLOSINGCONNECTION
                    .equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                getSmsAndEmailForClosingConnection(waterConnectionDetails, email, mobileNumber);
            else if (RECONNECTION
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
        if (waterConnectionDetails.getState().getHistory().isEmpty() && APPLICATION_STATUS_CREATED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.changeofuseconncetioncreate.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECHANGEOFUSECREATE);
            body = emailBodyByCodeAndArgsWithType("msg.changeofuseconncetioncreate.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECHANGEOFUSECREATE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.changeofuseconncetioncreate.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (!isDigitalSignatureEnabled && APPLICATION_STATUS_DIGITALSIGNPENDING
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.changeofuseconnection.approval.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECHANGEOFUSEAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.changeofuseconnection.approval.email.body",
                    waterConnectionDetails, applicantName, SMSEMAILTYPECHANGEOFUSEAPPROVE);
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
        } else if (APPLICATION_STATUS_ESTIMATENOTICEGEN
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName())) {
                body = emailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body", waterConnectionDetails,
                        applicantName, SMSEMAILTYPECHANGEOFUSENOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.noticegen.for.bpl.email.subject",
                        waterConnectionDetails.getApplicationNumber());

                smsMsg = smsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPECHANGEOFUSENOTICE);

            } else {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.changeofuseconnection.notice.gen", waterConnectionDetails,
                        applicantName, SMSEMAILTYPECHANGEOFUSENOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.changeofuseconnection.notice.email.body",
                        waterConnectionDetails, applicantName, SMSEMAILTYPECHANGEOFUSENOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                        "msg.changeofuseconnection.notice.email.subject",
                        waterConnectionDetails.getApplicationNumber());
            }

        } else if (APPLICATION_STATUS_FEEPAID
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnFeesPaid.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECHANGEOFUSEFEEPAID);
            body = emailBodyByCodeAndArgsWithType("msg.addconncetionOnfeespaid.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECHANGEOFUSEFEEPAID);
            final StringBuilder emailSubject = new StringBuilder(
                    " Demand and donation amount received for water tax application ");
            emailSubject.append(waterConnectionDetails.getApplicationNumber());
            subject = emailSubject.toString();

        } else if (APPLICATION_STATUS_SANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (METERED.equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString())) {
                body = emailBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.email.body",
                        waterConnectionDetails, applicantName, SMSEMAILTYPECHANGEOFUSEEXECUTION);
                smsMsg = smsBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPECHANGEOFUSEEXECUTION);
            } else {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPECHANGEOFUSEEXECUTION);
                body = emailBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.email.body",
                        waterConnectionDetails, applicantName, SMSEMAILTYPECHANGEOFUSEEXECUTION);

            }
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetionOnExecutionDate.email.subject",
                    waterConnectionDetails.getConnection().getConsumerCode());
        }
        if (mobileNumber != null && isNotBlank(smsMsg))
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && isNotBlank(body))
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
        if (waterConnectionDetails.getState().getHistory().isEmpty() && APPLICATION_STATUS_CREATED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.additionalconncetioncreate.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPEADDITONALCONNCREATE);
            body = emailBodyByCodeAndArgsWithType("msg.additionalconnectioncreate.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPEADDITONALCONNCREATE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.additionalconnectioncreate.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (!isDigitalSignatureEnabled && APPLICATION_STATUS_DIGITALSIGNPENDING
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.additionalconncetionapproval.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPEADDITONALCONNAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.additionalconncetionapproval.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPEADDITONALCONNAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.additionalconncetionapproval.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (isDigitalSignatureEnabled
                && APPLICATION_STATUS_APPROVED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.additionalconnectiondigitalsigned.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPEADDITONALCONNDIGISIGN);
            body = emailBodyByCodeAndArgsWithType("msg.additionalconnectiondigitalsigned.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPEADDITONALCONNDIGISIGN);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.additionalconnectiondigitalsigned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (APPLICATION_STATUS_ESTIMATENOTICEGEN
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (!BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.addconncetionOnGenerateNotice.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPEADDCONNESTNOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.addconncetionOnGenerateNotice.email.body",
                        waterConnectionDetails, applicantName, SMSEMAILTYPEADDCONNESTNOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                        "msg.conncetionOnGenerateNotice.email.subject", waterConnectionDetails.getApplicationNumber());
            } else {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPEADDCONNESTNOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body", waterConnectionDetails,
                        applicantName, SMSEMAILTYPEADDCONNESTNOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.noticegen.for.bpl.email.subject",
                        waterConnectionDetails.getApplicationNumber());
            }

        } else if (APPLICATION_STATUS_FEEPAID
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnFeesPaid.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNFEEPAID);
            body = emailBodyByCodeAndArgsWithType("msg.addconncetionOnfeespaid.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNFEEPAID);
            final StringBuilder emailSubject = new StringBuilder(
                    " Demand and donation amount received for water tax application ");
            emailSubject.append(waterConnectionDetails.getApplicationNumber());
            subject = emailSubject.toString();
        } else if (APPLICATION_STATUS_SANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (!METERED.toUpperCase()
                    .equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPENEWCONNEXECUTION);
                body = emailBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.email.body",
                        waterConnectionDetails, applicantName, SMSEMAILTYPENEWCONNEXECUTION);
            } else {
                body = emailBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.email.body",
                        waterConnectionDetails, applicantName, SMSEMAILTYPENEWCONNEXECUTION);

                smsMsg = smsBodyByCodeAndArgsWithType("msg.conncetionexeuction.metered.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPENEWCONNEXECUTION);
            }
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetionOnExecutionDate.email.subject",
                    waterConnectionDetails.getConnection().getConsumerCode());
        }
        if (mobileNumber != null && isNotBlank(smsMsg))
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && isNotBlank(body))
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

        if (waterConnectionDetails.getState().getHistory().isEmpty() && APPLICATION_STATUS_CREATED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetioncreate.sms", waterConnectionDetails, applicantName,
                    SMSEMAILTYPENEWCONNCREATE);
            body = emailBodyByCodeAndArgsWithType("msg.newconncetioncreate.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNCREATE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetioncreate.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (!isDigitalSignatureEnabled && APPLICATION_STATUS_DIGITALSIGNPENDING
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionapproval.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.newconncetionapproval.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetionapprove.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (APPLICATION_STATUS_ESTIMATENOTICEGEN
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()) &&
                Arrays.asList(NEWCONNECTION, ADDNLCONNECTION)
                        .contains(waterConnectionDetails.getApplicationType().getCode())
                && NON_METERED_CODE.equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newandadditionalconn.nonmeter.estimationnotice.sms",
                    waterConnectionDetails, applicantName, SMSEMAILTYPENEWCONNESTNOTICE);
            body = emailBodyByCodeAndArgsWithType("msg.newandadditionalconn.nonmeter.estimationnotice.email.body",
                    waterConnectionDetails, applicantName, SMSEMAILTYPENEWCONNESTNOTICE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.conncetionOnGenerateNotice.email.subject",
                    waterConnectionDetails.getApplicationNumber());

        } else if (APPLICATION_STATUS_ESTIMATENOTICEGEN
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            if (!BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName())) {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnGenerateNotice.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPENEWCONNESTNOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.newconncetionOnGenerateNotice.email.body",
                        waterConnectionDetails, applicantName, SMSEMAILTYPENEWCONNESTNOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs(
                        "msg.conncetionOnGenerateNotice.email.subject", waterConnectionDetails.getApplicationNumber());
            } else {
                smsMsg = smsBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.sms", waterConnectionDetails,
                        applicantName, SMSEMAILTYPENEWCONNESTNOTICE);
                body = emailBodyByCodeAndArgsWithType("msg.noticegen.for.bpl.email.body", waterConnectionDetails,
                        applicantName, SMSEMAILTYPENEWCONNESTNOTICE);
                subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.noticegen.for.bpl.email.subject",
                        waterConnectionDetails.getApplicationNumber());
            }

        } else if (isDigitalSignatureEnabled
                && APPLICATION_STATUS_APPROVED
                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconnectiondigitalsigned.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNDIGISIGN);
            body = emailBodyByCodeAndArgsWithType("msg.newconnectiondigitalsigned.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNDIGISIGN);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconnectiondigitalsigned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (APPLICATION_STATUS_FEEPAID
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnFeesPaid.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNFEEPAID);
            body = emailBodyByCodeAndArgsWithType("msg.addconncetionOnfeespaid.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNFEEPAID);
            final StringBuilder emailSubject = new StringBuilder(
                    " Demand and donation amount received for water tax application ");
            emailSubject.append(waterConnectionDetails.getApplicationNumber());
            subject = emailSubject.toString();
        } else if (APPLICATION_STATUS_SANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNEXECUTION);
            body = emailBodyByCodeAndArgsWithType("msg.newconncetionOnExecutionDate.email.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPENEWCONNEXECUTION);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.newconncetionOnExecutionDate.email.subject",
                    waterConnectionDetails.getConnection().getConsumerCode());
        }

        if (mobileNumber != null && isNotBlank(smsMsg))
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && isNotBlank(body))
            waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
    }

    /**
     * @param waterConnectionDetails
     * @param approvalComent
     * @return SMS AND EMAIL body and subject For Rejection in Cancelled status
     */
    public void sendSmsAndEmailOnRejection(final WaterConnectionDetails waterConnectionDetails,
            final String approvalComent) {
        if ((waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)
                || waterConnectionDetails.getApplicationType().getCode()
                        .equalsIgnoreCase(ADDNLCONNECTION)
                || waterConnectionDetails.getApplicationType().getCode()
                        .equalsIgnoreCase(CHANGEOFUSE))
                && waterConnectionDetails.getStatus().getCode()
                        .equals(APPLICATION_STATUS_CANCELLED)) {
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
                if (waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)) {
                    final StringBuilder smsBody = new StringBuilder().append("Dear ").append(applicantName)
                            .append(",Your new water tap connection application is being rejected and ")
                            .append("the reason for rejection ").append(approvalComent)
                            .append(" Please get in touch with ULB official to raise a new application with proper information to get a water connection.")
                            .append("\nThanks,").append(waterTaxUtils.getMunicipalityName());

                    smsMsg = smsBody.toString();
                } else if (waterConnectionDetails.getApplicationType().getCode()
                        .equals(ADDNLCONNECTION)) {
                    final StringBuilder smsBody = new StringBuilder().append("Dear ").append(applicantName)
                            .append(",Your additional water tap connection application is being rejected and ")
                            .append("the reason for rejection ").append(approvalComent)
                            .append(" Please get in touch with ULB official to raise a new application with proper information to get a water connection.")
                            .append("\nThanks,").append(waterTaxUtils.getMunicipalityName());
                    smsMsg = smsBody.toString();

                } else if (waterConnectionDetails.getApplicationType().getCode()
                        .equals(CHANGEOFUSE)) {
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
                if (waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)) {

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
                        .equals(ADDNLCONNECTION)) {
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
                        .equals(CHANGEOFUSE)) {
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
        return wcmsMessageSource.getMessage(code, new String[] { applicantName,
                waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
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
        final DecimalFormat amountFormat = new DecimalFormat("#.00");
        if (Arrays.asList(SMSEMAILTYPENEWCONNCREATE, SMSEMAILTYPEADDITONALCONNCREATE)
                .contains(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (Arrays.asList(SMSEMAILTYPENEWCONNAPPROVE, SMSEMAILTYPEADDITONALCONNAPPROVE)
                .contains(type))
            emailBody = wcmsMessageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterConnectionDetails.getConnection().getConsumerCode(),
                            waterTaxUtils.getMunicipalityName() },
                    null);

        else if (Arrays.asList(SMSEMAILTYPENEWCONNESTNOTICE, SMSEMAILTYPEADDCONNESTNOTICE)
                .contains(type) && NON_METERED_CODE.equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString()))
            emailBody = wcmsMessageSource.getMessage(code,
                    new String[] { applicantName,
                            waterConnectionDetails.getApplicationNumber(),
                            waterConnectionDetails.getApplicationType().getName(),
                            ApplicationThreadLocals.getDomainURL(),
                            waterTaxUtils.getMunicipalityName() },
                    null);
        else if (Arrays.asList(SMSEMAILTYPENEWCONNESTNOTICE, SMSEMAILTYPEADDCONNESTNOTICE,
                SMSEMAILTYPECHANGEOFUSENOTICE).contains(type)) {
            if (BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
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
            else
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

        } else if (Arrays
                .asList(SMSEMAILTYPENEWCONNDIGISIGN, SMSEMAILTYPEADDITONALCONNDIGISIGN, SMSEMAILTYPECHANGEOFUSECONNDIGISIGN)
                .contains(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(), waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(), waterTaxUtils.getMunicipalityName() }, null);
        else if (Arrays.asList(SMSEMAILTYPENEWCONNEXECUTION, SMSEMAILTYPECHANGEOFUSEEXECUTION)
                .contains(type))
            emailBody = wcmsMessageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                            toDefaultDateFormat(waterConnectionDetails.getExecutionDate()),
                            waterTaxUtils.getMunicipalityName() },
                    null);
        else if (Arrays.asList(SMSEMAILTYPENEWCONNFEEPAID,
                SMSEMAILTYPEADDCONNFEEPAID,
                SMSEMAILTYPECHANGEOFUSEFEEPAID)
                .contains(type)
                && waterConnectionDetailsService.getTotalAmount(waterConnectionDetails).compareTo(BigDecimal.ZERO) == 0) {
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
        } else if (SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource
                    .getMessage(code,
                            new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                    waterConnectionDetails.getApplicationNumber(),
                                    waterTaxUtils.getMunicipalityName() },
                            null);
        else if (SMSEMAILTYPECHANGEOFUSEAPPROVE.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(), waterTaxUtils.getMunicipalityName() },
                    null);
        else if (SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(),
                    waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(),
                    waterTaxUtils.getMunicipalityName() }, null);
        else if (SMSEMAILTYPECLOSINGCONNSANCTIONED.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(),
                    waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(),
                    waterTaxUtils.getMunicipalityName() }, null);
        else if (SMSEMAILTYPERECONNECTIONAPPROVE.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(),
                    waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(),
                    waterTaxUtils.getMunicipalityName() }, null);
        else if (SMSEMAILTYPERECONNECTIONSANCTIONED.equalsIgnoreCase(type))
            emailBody = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(),
                    waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(),
                    waterTaxUtils.getMunicipalityName() }, null);
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
        final DecimalFormat amountFormat = new DecimalFormat("#.00");
        if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(SMSEMAILTYPEADDITONALCONNCREATE))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getApplicationNumber(), waterTaxUtils.getMunicipalityName() }, null);
        else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNAPPROVE)
                || type.equalsIgnoreCase(SMSEMAILTYPEADDITONALCONNAPPROVE))
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

        else if (Arrays.asList(SMSEMAILTYPENEWCONNESTNOTICE, SMSEMAILTYPEADDCONNESTNOTICE)
                .contains(type) && NON_METERED_CODE.equalsIgnoreCase(waterConnectionDetails.getConnectionType().toString()))
            smsMsg = wcmsMessageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                            waterConnectionDetails.getApplicationType().getName(),
                            ApplicationThreadLocals.getDomainURL(),
                            waterTaxUtils.getMunicipalityName()
                    },
                    null);
        else if (Arrays.asList(SMSEMAILTYPENEWCONNESTNOTICE, SMSEMAILTYPEADDCONNESTNOTICE,
                SMSEMAILTYPECHANGEOFUSENOTICE).contains(type)) {
            if (!BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
                smsMsg = wcmsMessageSource.getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                                waterConnectionDetails.getApplicationType().getName(),
                                ApplicationThreadLocals.getDomainURL(),
                                waterTaxUtils.getMunicipalityName(),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges())),
                                String.valueOf(amountFormat.format(
                                        waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges())),
                                String.valueOf(amountFormat.format(waterConnectionDetails.getDonationCharges()
                                        + waterConnectionDetails.getFieldInspectionDetails().getEstimationCharges()))
                        },
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
        } else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNEXECUTION)
                || type.equalsIgnoreCase(SMSEMAILTYPECHANGEOFUSEEXECUTION))
            smsMsg = wcmsMessageSource.getMessage(code,
                    new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                            toDefaultDateFormat(waterConnectionDetails.getExecutionDate()),
                            waterTaxUtils.getMunicipalityName() },
                    null);
        else if ((type.equalsIgnoreCase(SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(SMSEMAILTYPEADDCONNFEEPAID)
                || type.equalsIgnoreCase(SMSEMAILTYPECHANGEOFUSEFEEPAID))
                &&  waterConnectionDetailsService.getTotalAmount(waterConnectionDetails).compareTo(BigDecimal.ZERO) == 0) {
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
        } else if (SMSEMAILTYPECHANGEOFUSECREATE.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource
                    .getMessage(code,
                            new String[] { applicantName, waterConnectionDetails.getConnection().getConsumerCode(),
                                    waterConnectionDetails.getApplicationNumber(),
                                    waterTaxUtils.getMunicipalityName() },
                            null);
        else if (SMSEMAILTYPECHANGEOFUSEAPPROVE.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(), waterTaxUtils.getMunicipalityName() },
                    null);
        else if (SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(),
                    waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(),
                    waterTaxUtils.getMunicipalityName() },
                    null);
        else if (SMSEMAILTYPECLOSINGCONNSANCTIONED.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(),
                    waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(),
                    waterTaxUtils.getMunicipalityName() },
                    null);
        else if (SMSEMAILTYPERECONNECTIONAPPROVE.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(),
                    waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(),
                    waterTaxUtils.getMunicipalityName() },
                    null);
        else if (SMSEMAILTYPERECONNECTIONSANCTIONED.equalsIgnoreCase(type))
            smsMsg = wcmsMessageSource.getMessage(code, new String[] { applicantName,
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    ApplicationThreadLocals.getDomainURL(),
                    waterConnectionDetails.getApplicationNumber(),
                    waterTaxUtils.getMunicipalityName(),
                    waterTaxUtils.getMunicipalityName() },
                    null);
        return smsMsg;
    }

    public void getSmsAndEmailForClosingConnection(final WaterConnectionDetails waterConnectionDetails,
            final String email, final String mobileNumber) {
        String smsMsg = null;
        String body = "";
        String subject = "";
        Boolean isDigitalSignatureEnabled = false;
        final AppConfig appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(MODULE_NAME, ENABLEDIGITALSIGNATURE);
        if (APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfig.getConfValues().get(0).getValue()))
            isDigitalSignatureEnabled = true;
        if (!isDigitalSignatureEnabled && APPLICATION_STATUS_CLOSERDIGSIGNPENDING
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.closeconnection.approve.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECLOSINGCONNAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.closeconnection.approve.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECLOSINGCONNAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.closeofconnectionapprove.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (isDigitalSignatureEnabled && APPLICATION_STATUS_CLOSERSANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.closeconnection.sanctioned.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECLOSINGCONNSANCTIONED);
            body = emailBodyByCodeAndArgsWithType("msg.closeconnection.sanctioned.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPECLOSINGCONNSANCTIONED);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.closeofconnectionsanctioned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        }
        if (mobileNumber != null && isNotBlank(smsMsg))
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && isNotBlank(body))
            waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
    }

    public void getSmsAndEmailForReConnection(final WaterConnectionDetails waterConnectionDetails, final String email,
            final String mobileNumber) {
        String smsMsg = null;
        String body = "";
        String subject = "";
        Boolean isDigitalSignatureEnabled = false;
        final AppConfig appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(MODULE_NAME, ENABLEDIGITALSIGNATURE);
        if (APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfig.getConfValues().get(0).getValue()))
            isDigitalSignatureEnabled = true;

        if (!isDigitalSignatureEnabled && APPLICATION_STATUS_RECONNDIGSIGNPENDING
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.reconnection.approve.sms", waterConnectionDetails, applicantName,
                    SMSEMAILTYPERECONNECTIONAPPROVE);
            body = emailBodyByCodeAndArgsWithType("msg.reconnection.approve.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPERECONNECTIONAPPROVE);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.reconnectionapprove.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        } else if (isDigitalSignatureEnabled && APPLICATION_STATUS_RECONNCTIONSANCTIONED
                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {

            smsMsg = smsBodyByCodeAndArgsWithType("msg.reconnection.sanctioned.sms", waterConnectionDetails,
                    applicantName, SMSEMAILTYPERECONNECTIONSANCTIONED);
            body = emailBodyByCodeAndArgsWithType("msg.reconnection.sanctioned.body", waterConnectionDetails,
                    applicantName, SMSEMAILTYPERECONNECTIONSANCTIONED);
            subject = waterTaxUtils.emailSubjectforEmailByCodeAndArgs("msg.reconnectionsanctioned.email.subject",
                    waterConnectionDetails.getApplicationNumber());
        }
        if (mobileNumber != null && isNotBlank(smsMsg))
            waterTaxUtils.sendSMSOnWaterConnection(mobileNumber, smsMsg);
        if (email != null && isNotBlank(body))
            waterTaxUtils.sendEmailOnWaterConnection(email, body, subject);
    }

}