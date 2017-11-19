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
package org.egov.stms.transactions.service;

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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.stms.utils.constants.SewerageTaxConstants.*;

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
                    String emailId = owner.getEmailId();
                    String mobileNo = owner.getMobileNumber();
                    
                    
                    if (emailId != null || mobileNo != null) {
                        if (NEWSEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails
                                .getApplicationType().getCode())) {
                            getSmsAndEmailForNewConnection(sewerageApplicationDetails, emailId, mobileNo,
                                    applicantName);
                        } else if (CHANGEINCLOSETS.equalsIgnoreCase(sewerageApplicationDetails
                                .getApplicationType().getCode())) {
                            getSmsAndEmailForChangeInClosets(sewerageApplicationDetails, emailId, mobileNo,
                                    applicantName);
                        }else if(CLOSESEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())){
                            getSmsAndEmailForCloseConnection(sewerageApplicationDetails, emailId, mobileNo, applicantName);
                        }

                    }
                }

            }
        }
    }

    /**
     * @return SMS AND EMAIL body and subject For New Connection
     * @param sewerageApplicationDetails
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */
    public void getSmsAndEmailForNewConnection(final SewerageApplicationDetails sewerageApplicationDetails,
            final String email, final String mobileNumber, final String applicantName) {
        String smsMsg = EMPTY;
        String body = EMPTY;
        String subject = EMPTY;

        if (APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())
                || APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails
                        .getStatus().getCode())) {
            if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectioncreate.sms", sewerageApplicationDetails,
                        applicantName, SMSEMAILTYPENEWCONNCREATE);
                body = EmailBodyByCodeAndArgsWithType("msg.newconnectioncreate.email.body", sewerageApplicationDetails,
                        applicantName, SMSEMAILTYPENEWCONNCREATE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectioncreate.email.subject",
                        sewerageApplicationDetails.getApplicationNumber());
            } else {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectioncreateForNoInsFee.sms",
                        sewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNCREATEFORNOINSFEE);
                body = EmailBodyByCodeAndArgsWithType("msg.newconnectioncreateForNoInsFee.email.body",
                        sewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNCREATEFORNOINSFEE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectioncreateForNoInsFee.email.subject",
                        sewerageApplicationDetails.getApplicationNumber());
            }
        }
        if (APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus()
                .getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectiondeeapproval.sms", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNDEEAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectiondeeapproval.email.body",
                    sewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNDEEAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectiondeeapproval.email.subject",
                    sewerageApplicationDetails.getApplicationNumber());
        } else if (APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectionOnDemandAndDonation.sms",
                    sewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNFEEPAID);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectionOnDemandAndDonation.email.body",
                    sewerageApplicationDetails, applicantName, SMSEMAILTYPENEWCONNFEEPAID);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectionOnDemandAndDonation.email.subject",
                    sewerageApplicationDetails.getApplicationNumber());

        } else if (APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newsewerageconnectionapprove.sms", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNFINALAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.newsewerageconnectionapprove.email.body", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNFINALAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newsewerageconnectionapprove.email.subject",
                    sewerageApplicationDetails.getApplicationNumber());
        } else if (APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.newconnectionRejection.sms", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNREJECT);
            body = EmailBodyByCodeAndArgsWithType("msg.newconnectionrejection.email.body", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPENEWCONNREJECT);
            subject = emailSubjectforEmailByCodeAndArgs("msg.newconnectionrejection.email.subject",
                    sewerageApplicationDetails.getApplicationNumber());
        }

        if (mobileNumber != null && isNotBlank(smsMsg))
            sendSMSOnSewerageConnection(mobileNumber, smsMsg);
        if (email != null && isNotBlank(body))
            sendEmailOnSewerageConnection(email, body, subject);
    }

    /**
     * @return SMS AND EMAIL body and subject For Change In Closets
     * @param sewerageApplicationDetails
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */
    public void getSmsAndEmailForChangeInClosets(final SewerageApplicationDetails sewerageApplicationDetails,
            final String email, final String mobileNumber, final String applicantName) {
        String smsMsg =  EMPTY;
        String body = EMPTY;
        String subject = EMPTY;

        if (APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())
                || APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails
                        .getStatus().getCode())) {
            if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeincloset.sms", sewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN);
                body = EmailBodyByCodeAndArgsWithType("msg.changeincloset.email.body", sewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN);
                subject = emailSubjectforEmailByCodeAndArgs("msg.changeincloset.email.subject",
                        sewerageApplicationDetails.getConnection().getShscNumber());
            } else {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetForNoInsFee.sms", sewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE);
                body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetForNoInsFee.email.body",
                        sewerageApplicationDetails, applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetForNoInsFee.email.subject",
                        sewerageApplicationDetails.getConnection().getShscNumber());
            }
        }
        if (APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus()
                .getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetdeeapproval.sms", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetdeeapproval.email.body",
                    sewerageApplicationDetails, applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetdeeapproval.email.subject",
                    sewerageApplicationDetails.getConnection().getShscNumber());
        } else if (APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetOnDemandAndDonation.sms",
                    sewerageApplicationDetails, applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetOnDemandAndDonation.email.body",
                    sewerageApplicationDetails, applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetOnDemandAndDonation.email.subject",
                    sewerageApplicationDetails.getConnection().getShscNumber());

        } else if (APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetapproval.sms", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetapproval.email.body", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetapproval.email.subject",
                    sewerageApplicationDetails.getConnection().getShscNumber());
        } else if (APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.changeinclosetRejection.sms", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT);
            body = EmailBodyByCodeAndArgsWithType("msg.changeinclosetrejection.email.body", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT);
            subject = emailSubjectforEmailByCodeAndArgs("msg.changeinclosetrejection.email.subject",
                    sewerageApplicationDetails.getConnection().getShscNumber());
        }

        if (mobileNumber != null && isNotBlank(smsMsg))
            sendSMSOnSewerageConnection(mobileNumber, smsMsg);
        if (email != null && isNotBlank(body))
            sendEmailOnSewerageConnection(email, body, subject);
    }
    
    
    public void getSmsAndEmailForCloseConnection(final SewerageApplicationDetails sewerageApplicationDetails,
            final String email, final String mobileNumber, final String applicantName) {
        String smsMsg = EMPTY;
        String body = EMPTY;
        String subject = EMPTY;

        if (APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails
                        .getStatus().getCode())) {
                smsMsg = SmsBodyByCodeAndArgsWithType("msg.closeofconnectioncreated.sms", sewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_CREATE);
                body = EmailBodyByCodeAndArgsWithType("msg.closeofconnectioncreated.email.body", sewerageApplicationDetails,
                        applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_CREATE);
                subject = emailSubjectforEmailByCodeAndArgs("msg.closeofconnectioncreated.email.subject",
                        sewerageApplicationDetails.getConnection().getShscNumber());
        }
        else if (APPLICATION_STATUS_CLOSERSANCTIONED.equalsIgnoreCase(sewerageApplicationDetails.getStatus()
                .getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.closeofconnectioneeapproval.sms", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_EEAPPROVE);
            body = EmailBodyByCodeAndArgsWithType("msg.closeofconnectioneeapproval.email.body",
                    sewerageApplicationDetails, applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_EEAPPROVE);
            subject = emailSubjectforEmailByCodeAndArgs("msg.closeofconnectioneeapproval.email.subject",
                    sewerageApplicationDetails.getConnection().getShscNumber());
        }         
        else if (APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.closeofconnectionrejection.sms", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_REJECT);
            body = EmailBodyByCodeAndArgsWithType("msg.closeofconnectionrejection.email.body", sewerageApplicationDetails,
                    applicantName, SMSEMAILTYPE_CLOSESEWERAGE_CONN_REJECT);
            subject = emailSubjectforEmailByCodeAndArgs("msg.closeofconnectionrejection.email.subject",
                    sewerageApplicationDetails.getConnection().getShscNumber());
        }

        if (mobileNumber != null && isNotBlank(smsMsg))
            sendSMSOnSewerageConnection(mobileNumber, smsMsg);
        if (email != null && isNotBlank(body))
            sendEmailOnSewerageConnection(email, body, subject);
    }
    

    public String smsAndEmailBodyByCodeAndArgs(final String code,
            final SewerageApplicationDetails sewerageApplicationDetails, final String applicantName) {
        return  stmsMessageSource.getMessage(code,
                new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName },
                null);
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
        String emailBody = EMPTY;
        String emailPdfLink = getNoticePdfLink(sewerageApplicationDetails);
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
                            sewerageApplicationDetails.getConnection().getShscNumber() ,emailPdfLink}, null);
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
            emailBody = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber(), emailPdfLink },
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

    private String getNoticePdfLink(final SewerageApplicationDetails sewerageApplicationDetails) {
        String noticePdfLink = EMPTY;
        if (null != sewerageApplicationDetails.getApplicationNumber()) {
            if (CLOSESEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode()))
                noticePdfLink = ApplicationThreadLocals.getDomainURL() + "/stms/transactions/viewcloseconnectionnotice/"
                        + sewerageApplicationDetails.getApplicationNumber() + "?closureNoticeNumber="
                        + sewerageApplicationDetails.getClosureNoticeNumber();
            else
                noticePdfLink = ApplicationThreadLocals.getDomainURL() + "/stms/transactions/workordernotice?pathVar="
                        + sewerageApplicationDetails.getApplicationNumber();
        }
        return noticePdfLink;
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
        String smsMsg = EMPTY;
        String pdfLink = getNoticePdfLink(sewerageApplicationDetails);
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
                      muncipalityName, sewerageApplicationDetails.getConnection().getShscNumber(),pdfLink }, null); 
              
          }
          else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNFEEPAID)
                    || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID)) {
                smsMsg = stmsMessageSource.getMessage(code,
                        new String[] { applicantName,
                                String.valueOf(sumOfSewerageApplnCharges(sewerageApplicationDetails)),
                                sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                                sewerageApplicationDetails.getConnection().getShscNumber() }, null);
            }

        else if (SMSEMAILTYPECLOSINGCONNAPPROVE.equalsIgnoreCase(type))
            smsMsg = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(), muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber() }, null);
        else if (type.equalsIgnoreCase(SMSEMAILTYPENEWCONNFINALAPPROVE)
                || type.equalsIgnoreCase(SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE))
        {        
            smsMsg = stmsMessageSource.getMessage(code,
                    new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),muncipalityName,
                            sewerageApplicationDetails.getConnection().getShscNumber(), pdfLink },
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
        return stmsMessageSource.getMessage(code, new String[] { applicantName, approvalComment,
                muncipalityName }, locale);
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(final String code,
            final SewerageApplicationDetails sewerageApplicationDetails, final String applicantName) {
        final Locale locale = LocaleContextHolder.getLocale();
        return stmsMessageSource.getMessage(code,
                new String[] { applicantName, sewerageApplicationDetails.getApplicationNumber(),
                        sewerageApplicationDetails.getConnection().getShscNumber(), muncipalityName }, locale);
        
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String applicationNumber) {
        final Locale locale = LocaleContextHolder.getLocale();
        return stmsMessageSource.getMessage(code, new String[] { applicationNumber }, locale);
    }

    public void sendSMSOnSewerageConnection(final String mobileNumber, final String smsBody) {
        notificationService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnSewerageConnection(final String email, final String emailBody, final String emailSubject) {
        notificationService.sendEmail(email, emailSubject, emailBody);
    }

}