/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.stms.transactions.service;

import java.math.BigDecimal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.messaging.MessagingService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SewerageConnectionSmsAndEmailService {

    private String muncipalityName;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    /**
     * @return this method will send SMS and Email is isSmsEnabled is true
     * @param SewerageApplicationDetails
     * @param workFlowAction
     */

    public void sendSmsAndEmail(final SewerageApplicationDetails sewerageApplicationDetails,
            final HttpServletRequest request) {
        final AssessmentDetails assessmentDetails = sewerageThirdPartyServices.getPropertyDetails(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier(), request);
        final String email = assessmentDetails.getPrimaryEmail();
        final String mobileNumber = assessmentDetails.getPrimaryMobileNo();
        
        
        muncipalityName = sewerageTaxUtils.getMunicipalityName();
        if (sewerageApplicationDetails != null && sewerageApplicationDetails.getApplicationType() != null
                && sewerageApplicationDetails.getApplicationType().getCode() != null
                && sewerageApplicationDetails.getStatus() != null
                && sewerageApplicationDetails.getStatus().getCode() != null) {

            for (OwnerName owner : assessmentDetails.getOwnerNames()) {
                String applicantName = owner.getOwnerName();
                if (assessmentDetails.getPrimaryEmail() != null || assessmentDetails.getPrimaryMobileNo() != null) {

                    if (SewerageTaxConstants.NEWSEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails
                            .getApplicationType().getCode())) {
                        getSmsAndEmailForNewConnection(sewerageApplicationDetails, email, mobileNumber, applicantName);
                    } else if (SewerageTaxConstants.CHANGEINCLOSETS.equalsIgnoreCase(sewerageApplicationDetails
                            .getApplicationType().getCode())) {
                        getSmsAndEmailForChangeInClosets(sewerageApplicationDetails, email, mobileNumber, applicantName);
                    }

                } else {
                    String email_id = owner.getEmailId();
                    String mobileno = owner.getMobileNumber();
                    
                    
                    if (email_id != null || mobileno != null) {
                        if (SewerageTaxConstants.NEWSEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails
                                .getApplicationType().getCode())) {
                            getSmsAndEmailForNewConnection(sewerageApplicationDetails, email_id, mobileno,
                                    applicantName);
                        } else if (SewerageTaxConstants.CHANGEINCLOSETS.equalsIgnoreCase(sewerageApplicationDetails
                                .getApplicationType().getCode())) {
                            getSmsAndEmailForChangeInClosets(sewerageApplicationDetails, email_id, mobileno,
                                    applicantName);
                        }

                    }
                }

            }
        }
    }

    /**
     * @return SMS AND EMAIL body and subject For New Connection
     * @param SewerageApplicationDetails
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */
    public void getSmsAndEmailForNewConnection(final SewerageApplicationDetails SewerageApplicationDetails,
            final String email, final String mobileNumber, final String applicantName) {
        String smsMsg = null;
        String body = "";
        String subject = "";

        if (SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())
                || SewerageTaxConstants.APPLICATION_STATUS_CREATED.equalsIgnoreCase(SewerageApplicationDetails
                        .getStatus().getCode())) {
            if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectioncreate.sms", SewerageApplicationDetails,
                        applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATE);
                body = EmailBodyByCodeAndArgsWithType("msg.newconnectioncreate.email.body", SewerageApplicationDetails,
                        applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectioncreate.email.subject",
                        SewerageApplicationDetails.getApplicationNumber());
            } else {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectioncreateForNoInsFee.sms",
                        SewerageApplicationDetails, applicantName,
                        SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATEFORNOINSFEE);
                body = EmailBodyByCodeAndArgsWithType("msg.newconnectioncreateForNoInsFee.email.body",
                        SewerageApplicationDetails, applicantName,
                        SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATEFORNOINSFEE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectioncreateForNoInsFee.email.subject",
                        SewerageApplicationDetails.getApplicationNumber());
            }
        }
        if (SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(SewerageApplicationDetails.getStatus()
                .getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectiondeeapproval.sms", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNDEEAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectiondeeapproval.email.body",
                    SewerageApplicationDetails, applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNDEEAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectiondeeapproval.email.subject",
                    SewerageApplicationDetails.getApplicationNumber());
        } else if (SewerageTaxConstants.APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectionOnDemandAndDonation.sms",
                    SewerageApplicationDetails, applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNFEEPAID);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectionOnDemandAndDonation.email.body",
                    SewerageApplicationDetails, applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNFEEPAID);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectionOnDemandAndDonation.email.subject",
                    SewerageApplicationDetails.getApplicationNumber());

        } else if (SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectionapproval.sms", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNFINALAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.newconncetionapproval.email.body", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNFINALAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconncetionapprove.email.subject",
                    SewerageApplicationDetails.getApplicationNumber());
        } else if (SewerageTaxConstants.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectionRejection.sms", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNREJECT);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectionrejection.email.body", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPENEWCONNREJECT);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectionrejection.email.subject",
                    SewerageApplicationDetails.getApplicationNumber());
        }

        if (mobileNumber != null && smsMsg != null)
            sendSMSOnSewerageConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            sendEmailOnSewerageConnection(email, body, subject);
    }

    /**
     * @return SMS AND EMAIL body and subject For Change In Closets
     * @param SewerageApplicationDetails
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */
    public void getSmsAndEmailForChangeInClosets(final SewerageApplicationDetails SewerageApplicationDetails,
            final String email, final String mobileNumber, final String applicantName) {
        String smsMsg = null;
        String body = "";
        String subject = "";

        if (SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())
                || SewerageTaxConstants.APPLICATION_STATUS_CREATED.equalsIgnoreCase(SewerageApplicationDetails
                        .getStatus().getCode())) {
            if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeincloset.sms", SewerageApplicationDetails,
                        applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN);
                body = EmailBodyByCodeAndArgsWithType("msg.changeincloset.email.body", SewerageApplicationDetails,
                        applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN);
                subject = emailSubjectforEmailByCodeAndArgs("msg.changeincloset.email.subject",
                        SewerageApplicationDetails.getConnection().getShscNumber());
            } else {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetForNoInsFee.sms", SewerageApplicationDetails,
                        applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE);
                body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetForNoInsFee.email.body",
                        SewerageApplicationDetails, applicantName,
                        SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetForNoInsFee.email.subject",
                        SewerageApplicationDetails.getConnection().getShscNumber());
            }
        }
        if (SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(SewerageApplicationDetails.getStatus()
                .getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetdeeapproval.sms", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetdeeapproval.email.body",
                    SewerageApplicationDetails, applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetdeeapproval.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());
        } else if (SewerageTaxConstants.APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetOnDemandAndDonation.sms",
                    SewerageApplicationDetails, applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetOnDemandAndDonation.email.body",
                    SewerageApplicationDetails, applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetOnDemandAndDonation.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());

        } else if (SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetapproval.sms", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetapproval.email.body", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetapproval.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());
        } else if (SewerageTaxConstants.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetRejection.sms", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetrejection.email.body", SewerageApplicationDetails,
                    applicantName, SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetrejection.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());
        }

        if (mobileNumber != null && smsMsg != null)
            sendSMSOnSewerageConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            sendEmailOnSewerageConnection(email, body, subject);
    }

    public String smsAndEmailBodyByCodeAndArgs(final String code,
            final SewerageApplicationDetails SewerageApplicationDetails, final String applicantName) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { applicantName, SewerageApplicationDetails.getApplicationNumber(), muncipalityName },
                null);
        return smsMsg;
    }

    /**
     * .
     * 
     * @param code
     * @param SewerageApplicationDetails
     * @param applicantName
     * @param type
     * @return EmailBody for All Connection based on Type
     */
    public String EmailBodyByCodeAndArgsWithType(final String code,
            final SewerageApplicationDetails sewerageApplicationDetails, final String applicantName, final String type) {
        String emailBody = "";
        if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            String.valueOf(getInspectionFeeForSewerage(sewerageApplicationDetails)),
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATEFORNOINSFEE)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNDEEAPPROVE)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE)) {
            emailBody = messageSource.getMessage(
                    code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        } else if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID)) {
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName,
                            String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)),
                            sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        } else if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNFINALAPPROVE)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE)) {
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            sewerageApplicationDetails.getConnection().getShscNumber(), muncipalityName }, null);
        } else if (SewerageTaxConstants.SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName },
                    null);
        else if (SewerageTaxConstants.SMSEMAILTYPECLOSINGCONNSANCTIONED.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_SANCTIONED))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (SewerageTaxConstants.SMSEMAILTYPENEWCONNREJECT.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT))
            emailBody = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApprovalComent(), muncipalityName,
                            sewerageApplicationDetails.getApplicationNumber(),
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        return emailBody;
    }

    /**
     * @param SewerageApplicationDetails
     * @param type
     * @return Sum Of Sewerage Application charges
     */

    public BigDecimal sumOfSewerageApplnCharges(final SewerageApplicationDetails sewerageApplicationDetails) {
        BigDecimal totalAmt = BigDecimal.ZERO;
        for (SewerageConnectionFee sewerageConnFee : sewerageApplicationDetails.getConnectionFees()) {
            if (!(sewerageConnFee.getFeesDetail().getDescription()).equals(SewerageTaxConstants.INSPECTIONCHARGE)) {
                totalAmt = totalAmt.add(BigDecimal.valueOf(sewerageConnFee.getAmount()));
            }
        }
        return totalAmt;
    }

    /**
     * @param SewerageApplicationDetails
     * @param type
     * @return Sewerage Inspection Charges
     */
    public BigDecimal getInspectionFeeForSewerage(final SewerageApplicationDetails sewerageApplicationDetails) {
        BigDecimal totalAmt = BigDecimal.ZERO;
        for (SewerageConnectionFee sewerageConnFee : sewerageApplicationDetails.getConnectionFees()) {
            if ((sewerageConnFee.getFeesDetail().getDescription()).equals(SewerageTaxConstants.INSPECTIONCHARGE)) {
                totalAmt = totalAmt.add(BigDecimal.valueOf(sewerageConnFee.getAmount()));
            }
        }
        return totalAmt;
    }

    /**
     * @param code
     * @param SewerageApplicationDetails
     * @param applicantName
     * @param type
     */
    public String SmsBodyByCodeAndArgsWithType(final String code,
            final SewerageApplicationDetails sewerageApplicationDetails, final String applicantName, final String type) {
        String smsMsg = "";
        if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            String.valueOf(getInspectionFeeForSewerage(sewerageApplicationDetails)),
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        else if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATEFORNOINSFEE)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        else if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNDEEAPPROVE)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE)) {
            smsMsg = messageSource.getMessage(
                    code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        } else if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID)) {
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName,
                            String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)),
                            sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        }

        else if (SewerageTaxConstants.SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPENEWCONNFINALAPPROVE)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            sewerageApplicationDetails.getConnection().getShscNumber(), muncipalityName }, null);
        else if (SewerageTaxConstants.SMSEMAILTYPECLOSINGCONNSANCTIONED.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_SANCTIONED))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (SewerageTaxConstants.SMSEMAILTYPENEWCONNREJECT.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT))
            smsMsg = messageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApprovalComent(), muncipalityName,
                            sewerageApplicationDetails.getApplicationNumber(),
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        return smsMsg;
    }

    public Boolean isSmsEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.SENDSMSFORSEWERAGETAX).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isEmailEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.SENDEMAILFORSEWERAGETAX).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public String smsAndEmailBodyByCodeAndArgsForRejection(final String code, final String approvalComment,
            final String applicantName) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String smsMsg = messageSource.getMessage(code, new String[] { applicantName, approvalComment,
                muncipalityName }, locale);
        return smsMsg;
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(final String code,
            final SewerageApplicationDetails sewerageApplicationDetails, final String applicantName) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String smsMsg = messageSource.getMessage(code,
                new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                        sewerageApplicationDetails.getConnection().getShscNumber(), muncipalityName }, locale);
        return smsMsg;
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String applicationNumber) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String emailSubject = messageSource.getMessage(code, new String[] { applicationNumber }, locale);
        return emailSubject;
    }

    public void sendSMSOnSewerageConnection(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnSewerageConnection(final String email, final String emailBody, final String emailSubject) {
        messagingService.sendEmail(email, emailSubject, emailBody);
    }

}