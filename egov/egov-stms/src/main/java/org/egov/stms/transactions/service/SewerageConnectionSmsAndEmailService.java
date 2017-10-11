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

import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_REJECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSESEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_DONATIONCHARGE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ESTIMATIONCHARGES_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_SEWERAGETAX_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.INSPECTIONCHARGE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULE_NAME;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NEWSEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SENDEMAILFORSEWERAGETAX;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SENDSMSFORSEWERAGETAX;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPECLOSINGCONNAPPROVE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPECLOSINGCONNSANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPENEWCONNCREATEFORNOINSFEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPENEWCONNDEEAPPROVE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPENEWCONNFEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPENEWCONNFINALAPPROVE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPENEWCONNREJECT;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CHANGEINCLOSETS_CONN_SANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CLOSESEWERAGE_CONN_CREATE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CLOSESEWERAGE_CONN_EEAPPROVE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SMSEMAILTYPE_CLOSESEWERAGE_CONN_REJECT;

import java.math.BigDecimal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.notification.service.NotificationService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.utils.SewerageTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SewerageConnectionSmsAndEmailService {

    private String muncipalityName;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource stmsMessageSource;
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AppConfigValueService appConfigValuesService;
    
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

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

                    if (NEWSEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails
                            .getApplicationType().getCode())) {
                        getSmsAndEmailForNewConnection(sewerageApplicationDetails, email, mobileNumber, applicantName);
                    } else if (CHANGEINCLOSETS.equalsIgnoreCase(sewerageApplicationDetails
                            .getApplicationType().getCode())) {
                        getSmsAndEmailForChangeInClosets(sewerageApplicationDetails, email, mobileNumber, applicantName);
                    }else if(CLOSESEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())){
                        getSmsAndEmailForCloseConnection(sewerageApplicationDetails, email, mobileNumber, applicantName);
                    }

                } else {
                    String email_id = owner.getEmailId();
                    String mobileno = owner.getMobileNumber();
                    
                    
                    if (email_id != null || mobileno != null) {
                        if (NEWSEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails
                                .getApplicationType().getCode())) {
                            getSmsAndEmailForNewConnection(sewerageApplicationDetails, email_id, mobileno,
                                    applicantName);
                        } else if (CHANGEINCLOSETS.equalsIgnoreCase(sewerageApplicationDetails
                                .getApplicationType().getCode())) {
                            getSmsAndEmailForChangeInClosets(sewerageApplicationDetails, email_id, mobileno,
                                    applicantName);
                        }else if(CLOSESEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())){
                            getSmsAndEmailForCloseConnection(sewerageApplicationDetails, email_id, mobileno, applicantName);
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

        if (APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())
                || APPLICATION_STATUS_CREATED.equalsIgnoreCase(SewerageApplicationDetails
                        .getStatus().getCode())) {
            if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectioncreate.sms", SewerageApplicationDetails,
                        applicantName, SMSEMAILTYPENEWCONNCREATE);
                body = EmailBodyByCodeAndArgsWithType("msg.newconnectioncreate.email.body", SewerageApplicationDetails,
                        applicantName, SMSEMAILTYPENEWCONNCREATE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectioncreate.email.subject",
                        SewerageApplicationDetails.getApplicationNumber());
            } else {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectioncreateForNoInsFee.sms",
                        SewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNCREATEFORNOINSFEE);
                body = EmailBodyByCodeAndArgsWithType("msg.newconnectioncreateForNoInsFee.email.body",
                        SewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNCREATEFORNOINSFEE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectioncreateForNoInsFee.email.subject",
                        SewerageApplicationDetails.getApplicationNumber());
            }
        }
        if (APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(SewerageApplicationDetails.getStatus()
                .getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectiondeeapproval.sms", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNDEEAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectiondeeapproval.email.body",
                    SewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNDEEAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectiondeeapproval.email.subject",
                    SewerageApplicationDetails.getApplicationNumber());
        } else if (APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectionOnDemandAndDonation.sms",
                    SewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNFEEPAID);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectionOnDemandAndDonation.email.body",
                    SewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNFEEPAID);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectionOnDemandAndDonation.email.subject",
                    SewerageApplicationDetails.getApplicationNumber());

        } else if (APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newsewerageconnectionapprove.sms", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNFINALAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.newsewerageconnectionapprove.email.body", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNFINALAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newsewerageconnectionapprove.email.subject",
                    SewerageApplicationDetails.getApplicationNumber());
        } else if (APPLICATION_STATUS_REJECTED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectionRejection.sms", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNREJECT);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectionrejection.email.body", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNREJECT);
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

        if (APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())
                || APPLICATION_STATUS_CREATED.equalsIgnoreCase(SewerageApplicationDetails
                        .getStatus().getCode())) {
            if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeincloset.sms", SewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN);
                body = EmailBodyByCodeAndArgsWithType("msg.changeincloset.email.body", SewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN);
                subject = emailSubjectforEmailByCodeAndArgs("msg.changeincloset.email.subject",
                        SewerageApplicationDetails.getConnection().getShscNumber());
            } else {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetForNoInsFee.sms", SewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE);
                body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetForNoInsFee.email.body",
                        SewerageApplicationDetails, applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetForNoInsFee.email.subject",
                        SewerageApplicationDetails.getConnection().getShscNumber());
            }
        }
        if (APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(SewerageApplicationDetails.getStatus()
                .getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetdeeapproval.sms", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetdeeapproval.email.body",
                    SewerageApplicationDetails, applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetdeeapproval.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());
        } else if (APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetOnDemandAndDonation.sms",
                    SewerageApplicationDetails, applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetOnDemandAndDonation.email.body",
                    SewerageApplicationDetails, applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetOnDemandAndDonation.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());

        } else if (APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetapproval.sms", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetapproval.email.body", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetapproval.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());
        } else if (APPLICATION_STATUS_REJECTED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetRejection.sms", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetrejection.email.body", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetrejection.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());
        }

        if (mobileNumber != null && smsMsg != null)
            sendSMSOnSewerageConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            sendEmailOnSewerageConnection(email, body, subject);
    }
    
    
    public void getSmsAndEmailForCloseConnection(final SewerageApplicationDetails SewerageApplicationDetails,
            final String email, final String mobileNumber, final String applicantName) {
        String smsMsg = null;
        String body = "";
        String subject = "";

        if (APPLICATION_STATUS_CREATED.equalsIgnoreCase(SewerageApplicationDetails
                        .getStatus().getCode())) {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.closeofconnectioncreated.sms", SewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_CREATE);
                body = EmailBodyByCodeAndArgsWithType("msg.closeofconnectioncreated.email.body", SewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_CREATE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.closeofconnectioncreated.email.subject",
                        SewerageApplicationDetails.getConnection().getShscNumber());
        }
        else if (APPLICATION_STATUS_CLOSERSANCTIONED.equalsIgnoreCase(SewerageApplicationDetails.getStatus()
                .getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.closeofconnectioneeapproval.sms", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_EEAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.closeofconnectioneeapproval.email.body",
                    SewerageApplicationDetails, applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_EEAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.closeofconnectioneeapproval.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());
        } 
        else if (APPLICATION_STATUS_REJECTED.equalsIgnoreCase(SewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.closeofconnectionrejection.sms", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_REJECT);
            body = EmailBodyByCodeAndArgsWithType("msg.closeofconnectionrejection.email.body", SewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_REJECT);
            subject = emailSubjectforEmailByCodeAndArgs("msg.closeofconnectionrejection.email.subject",
                    SewerageApplicationDetails.getConnection().getShscNumber());
        }

        if (mobileNumber != null && smsMsg != null)
            sendSMSOnSewerageConnection(mobileNumber, smsMsg);
        if (email != null && body != null)
            sendEmailOnSewerageConnection(email, body, subject);
    }
    

    public String smsAndEmailBodyByCodeAndArgs(final String code,
            final SewerageApplicationDetails SewerageApplicationDetails, final String applicantName) {
        final String smsMsg = stmsMessageSource.getMessage(code,
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
        if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN) || type.equalsIgnoreCase(SMSEMAILTYPE_CLOSESEWERAGE_CONN_CREATE))
            emailBody = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            String.valueOf(getInspectionFeeForSewerage(sewerageApplicationDetails)),
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNCREATEFORNOINSFEE)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE))
            emailBody = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNDEEAPPROVE)) {
            emailBody = stmsMessageSource.getMessage(
                    code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        } 
        else if(type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE)){
            emailBody = stmsMessageSource.getMessage(
                    code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            String.valueOf(sumOfChangeClosetSewerageApplnCharges(sewerageApplicationDetails)), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        }
        else if(type.equalsIgnoreCase(SMSEMAILTYPE_CLOSESEWERAGE_CONN_EEAPPROVE)){
            emailBody = stmsMessageSource.getMessage(
                    code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        }
        else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNFEEPAID)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID)) {
            emailBody = stmsMessageSource.getMessage(code,
                    new String[] { applicantName,
                            String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)),
                            sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        } else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNFINALAPPROVE)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE)) {
            String pdfLink = StringUtils.EMPTY;
            if (null != sewerageApplicationDetails.getApplicationNumber())
                pdfLink = ApplicationThreadLocals.getDomainURL() + "/stms/transactions/workordernotice?pathVar="
                        + sewerageApplicationDetails.getApplicationNumber();
            emailBody = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            sewerageApplicationDetails.getConnection().getShscNumber(), muncipalityName, pdfLink },
                    null);
        } else if (SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type))
            emailBody = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName },
                    null);
        else if (SMSEMAILTYPECLOSINGCONNSANCTIONED.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_SANCTIONED))
            emailBody = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (SMSEMAILTYPENEWCONNREJECT.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT) || type.equalsIgnoreCase(SMSEMAILTYPE_CLOSESEWERAGE_CONN_REJECT))
            emailBody = stmsMessageSource.getMessage(code,
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
            if (!(sewerageConnFee.getFeesDetail().getDescription()).equals(INSPECTIONCHARGE)) {
                totalAmt = totalAmt.add(BigDecimal.valueOf(sewerageConnFee.getAmount()));
            }
        }
        return totalAmt;
    }
    /**
     * @param SewerageAppliationDetails
     * @param type
     * @return Sum Of Sewerage Application charges including only the difference with first application
     */
    public BigDecimal sumOfChangeClosetSewerageApplnCharges(final SewerageApplicationDetails sewerageApplicationDetails){
        BigDecimal totalAmt=BigDecimal.ZERO;
        
        BigDecimal oldDonationCharge = BigDecimal.ZERO;
        BigDecimal oldSewerageTax = BigDecimal.ZERO;
        
        BigDecimal currentDonationCharge=BigDecimal.ZERO;
        BigDecimal currentSewerageTax=BigDecimal.ZERO;
        BigDecimal currentEstimationCharges=BigDecimal.ZERO;
        
        BigDecimal totalDonationCharge=BigDecimal.ZERO;
        BigDecimal totalSewerageTax=BigDecimal.ZERO;
        
        if (sewerageApplicationDetails.getCurrentDemand() != null) {
            SewerageApplicationDetails oldApplicationDtl = sewerageApplicationDetailsService.findByConnection_ShscNumberAndIsActive(sewerageApplicationDetails.getConnection().getShscNumber());
            if(oldApplicationDtl !=null){
                for (final SewerageConnectionFee oldSewerageConnectionFee : oldApplicationDtl.getConnectionFees()) {
                    if (oldSewerageConnectionFee.getFeesDetail().getCode().equalsIgnoreCase(FEES_SEWERAGETAX_CODE))
                        oldSewerageTax=oldSewerageTax.add(BigDecimal.valueOf(oldSewerageConnectionFee.getAmount()));
                    if (oldSewerageConnectionFee.getFeesDetail().getCode().equalsIgnoreCase(FEES_DONATIONCHARGE_CODE))
                        oldDonationCharge=oldDonationCharge.add(BigDecimal.valueOf(oldSewerageConnectionFee.getAmount()));
                } 
            }
            
            for (final SewerageConnectionFee scf : sewerageApplicationDetails.getConnectionFees()) {
                if (scf.getFeesDetail().getCode().equalsIgnoreCase(FEES_SEWERAGETAX_CODE)) 
                    currentSewerageTax=currentSewerageTax.add(BigDecimal.valueOf(scf.getAmount()));
                if (scf.getFeesDetail().getCode().equalsIgnoreCase(FEES_DONATIONCHARGE_CODE))
                    currentDonationCharge=currentDonationCharge.add(BigDecimal.valueOf(scf.getAmount()));
                if (scf.getFeesDetail().getCode().equalsIgnoreCase(FEES_ESTIMATIONCHARGES_CODE))
                    currentEstimationCharges = BigDecimal.valueOf(scf.getAmount());
            }
            
            if(currentDonationCharge.compareTo(oldDonationCharge)>0){
                totalDonationCharge = currentDonationCharge.subtract(oldDonationCharge);
            }
           
            if(currentSewerageTax.compareTo(oldSewerageTax)>0){
                totalSewerageTax = currentSewerageTax.subtract(oldSewerageTax);
            }
            totalAmt = totalSewerageTax.add(totalDonationCharge).add(currentEstimationCharges);
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
            if ((sewerageConnFee.getFeesDetail().getDescription()).equals(INSPECTIONCHARGE)) {
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
        if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNCREATE)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN) || type.equalsIgnoreCase(SMSEMAILTYPE_CLOSESEWERAGE_CONN_CREATE))
            smsMsg = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            String.valueOf(getInspectionFeeForSewerage(sewerageApplicationDetails)),
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNCREATEFORNOINSFEE)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE))
            smsMsg = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

          if(type.equalsIgnoreCase(SMSEMAILTYPENEWCONNDEEAPPROVE)){
            smsMsg = stmsMessageSource.getMessage(
                    code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
          }
          else if (type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE)){
              smsMsg = stmsMessageSource.getMessage(
                      code,
                      new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                              String.valueOf(sumOfChangeClosetSewerageApplnCharges(sewerageApplicationDetails)), muncipalityName,
                              sewerageApplicationDetails.getConnection().getShscNumber() }, null);
          }
          else if(type.equalsIgnoreCase(SMSEMAILTYPE_CLOSESEWERAGE_CONN_EEAPPROVE)){
              smsMsg = stmsMessageSource.getMessage(
                      code, 
                      new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), 
                      muncipalityName, sewerageApplicationDetails.getConnection().getShscNumber() }, null);    
          }
          else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNFEEPAID)
                    || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID)) {
                smsMsg = stmsMessageSource.getMessage(code,
                        new String[] { applicantName,
                                String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)),
                                sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                                sewerageApplicationDetails.getConnection().getShscNumber() }, null);
            }

        else if (SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE))
            smsMsg = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNFINALAPPROVE)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE))
        {
            String pdfLink = StringUtils.EMPTY;
            if (null != sewerageApplicationDetails.getApplicationNumber())
                pdfLink = ApplicationThreadLocals.getDomainURL() + "/stms/transactions/workordernotice?pathVar="
                        + sewerageApplicationDetails.getApplicationNumber();
            smsMsg = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                            sewerageApplicationDetails.getConnection().getShscNumber(), muncipalityName, pdfLink },
                    null);
        }
        else if (SMSEMAILTYPECLOSINGCONNSANCTIONED.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_SANCTIONED))
            smsMsg = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (SMSEMAILTYPENEWCONNREJECT.equalsIgnoreCase(type)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT) || type.equalsIgnoreCase(SMSEMAILTYPE_CLOSESEWERAGE_CONN_REJECT))
            smsMsg = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApprovalComent(), muncipalityName,
                            sewerageApplicationDetails.getApplicationNumber(),
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);

        return smsMsg;
    }

    public Boolean isSmsEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, SENDSMSFORSEWERAGETAX).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isEmailEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, SENDEMAILFORSEWERAGETAX).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public String smsAndEmailBodyByCodeAndArgsForRejection(final String code, final String approvalComment,
            final String applicantName) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String smsMsg = stmsMessageSource.getMessage(code, new String[] { applicantName, approvalComment,
                muncipalityName }, locale);
        return smsMsg;
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(final String code,
            final SewerageApplicationDetails sewerageApplicationDetails, final String applicantName) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String smsMsg = stmsMessageSource.getMessage(code,
                new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                        sewerageApplicationDetails.getConnection().getShscNumber(), muncipalityName }, locale);
        return smsMsg;
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String applicationNumber) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String emailSubject = stmsMessageSource.getMessage(code, new String[] { applicationNumber }, locale);
        return emailSubject;
    }

    public void sendSMSOnSewerageConnection(final String mobileNumber, final String smsBody) {
        notificationService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnSewerageConnection(final String email, final String emailBody, final String emailSubject) {
        notificationService.sendEmail(email, emailSubject, emailBody);
    }

}