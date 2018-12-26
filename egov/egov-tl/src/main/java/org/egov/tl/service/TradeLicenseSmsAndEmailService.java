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
package org.egov.tl.service;

import org.egov.commons.Installment;
import org.egov.commons.service.CFinancialYearService;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityName;
import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;
import static org.egov.tl.utils.Constants.APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONFORWARD;
import static org.egov.tl.utils.Constants.STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.STATUS_UNDERWORKFLOW;

@Service
public class TradeLicenseSmsAndEmailService {

    private static final String MSG_LICENSE_CREATE_SMS = "msg.%s.license.creator.sms";
    private static final String MSG_LICENSE_CREATE_SUBJECT = "msg.%s.license.create.email.subject";
    private static final String MSG_LICENSE_CREATE_BODY = "msg.%s.license.create.email.body";
    private static final String MSG_LICENSE_APPROVAL_BODY = "msg.%s.licenseapproval.email.body";
    private static final String MSG_LICENSE_APPROVALAMT_BODY = "msg.%s.license.approvalAmt.email.body";
    private static final String MSG_LICENSE_CANCEL_BODY = "msg.%s.license.cancelled.email.body";
    private static final String MSG_LICENSE_FIRSTLEVEL_SMS = "msg.%s.license.firstcollection.sms";
    private static final String MSG_LICENSE_FIRSTLEVEL_SUBJECT = "msg.%s.license.firstcollection.email.subject";
    private static final String MSG_LICENSE_FIRSTLEVEL_BODY = "msg.%s.license.firstcollection.email.body";
    private static final String MSG_LICENSE_SECONDLEVEL_SMS = "msg.%s.license.second.level.sms";
    private static final String MSG_LICENSE_SECONDLEVEL_BODY = "msg.%s.license.second.level.email.body";
    private static final String MSG_LICENSE_DIGI_APPROVAL_BODY = "msg.%s.licenseapproval.digienabled.email.body";
    private static final String MSG_LICENSE_DIGI_APPROVALAMT_BODY = "msg.%s.license.digienabled.approvalAmt.email.body";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource licenseMessageSource;

    @Autowired
    private LicenseConfigurationService licenseConfigurationService;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    public void sendSMS(String mobileNumber, String smsBody) {
        notificationService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmail(String email, String emailBody, String emailSubject) {
        notificationService.sendEmail(email, emailSubject, emailBody);
    }

    public void sendSmsAndEmail(TradeLicense license, String workFlowAction) {
        String smsMsg = null;
        String emailBody = null;
        String emailSubject = null;
        Locale locale = Locale.getDefault();
        Boolean digitalSignEnabled = licenseConfigurationService.digitalSignEnabled();
        if (license.getState().getHistory().isEmpty() && license.isAcknowledged()) {

            smsMsg = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_CREATE_SMS, license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            license.getApplicationNumber(),
                            getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_CREATE_BODY, license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            license.getNameOfEstablishment(),
                            license.getApplicationNumber(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage(String.format(MSG_LICENSE_CREATE_SUBJECT,
                    license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{getMunicipalityName()}, locale);

        } else if (BUTTONAPPROVE.equals(workFlowAction) && license.getStatus() != null
                && STATUS_UNDERWORKFLOW.equalsIgnoreCase(license.getStatus().getStatusCode())) {
            BigDecimal demAmt = ZERO;
            for (EgDemandDetails dmdDtls : license.getCurrentDemand().getEgDemandDetails())
                demAmt = demAmt.add(dmdDtls.getAmount().subtract(dmdDtls.getAmtCollected()));

            if (digitalSignEnabled && demAmt.compareTo(ZERO) == 0) {

                String emailCode = String.format(MSG_LICENSE_DIGI_APPROVAL_BODY,
                        license.getLicenseAppType().getCode().toLowerCase());
                emailBody = licenseMessageSource.getMessage(
                        emailCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getLicenseNumber(),
                                getMunicipalityName()},
                        locale);
                String smsCode = "msg.digi.enabled.newTradeLicenseapproval.sms";
                smsMsg = licenseMessageSource.getMessage(
                        smsCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getLicenseNumber(),
                                getMunicipalityName()},
                        locale);
            } else if (!digitalSignEnabled && demAmt.compareTo(ZERO) == 0) {
                String emailCode = String.format(MSG_LICENSE_APPROVAL_BODY,
                        license.getLicenseAppType().getCode().toLowerCase());
                emailBody = licenseMessageSource.getMessage(
                        emailCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getLicenseNumber(),
                                getMunicipalityName()},
                        locale);
                String smsCode = "msg.newTradeLicenseapproval.sms";
                smsMsg = licenseMessageSource.getMessage(
                        smsCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getLicenseNumber(),
                                getMunicipalityName()},
                        locale);
            } else if (demAmt.compareTo(ZERO) > 0 && digitalSignEnabled) {
                String emailCode = String.format(MSG_LICENSE_DIGI_APPROVALAMT_BODY,
                        license.getLicenseAppType().getCode().toLowerCase());
                emailBody = licenseMessageSource.getMessage(
                        emailCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(), license.getLicenseNumber(),
                                license.getTotalBalance().toString(), getDomainURL(),
                                getMunicipalityName()}, locale);
                String smsCode = "msg.digi.enabled.newTradeLicenseapprovalAmt.sms";
                smsMsg = licenseMessageSource.getMessage(
                        smsCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(), license.getLicenseNumber(),
                                license.getTotalBalance().toString(), getDomainURL(),
                                getMunicipalityName()},
                        locale);
            } else if (demAmt.compareTo(ZERO) > 0 && !digitalSignEnabled) {
                String emailCode = String.format(MSG_LICENSE_APPROVALAMT_BODY,
                        license.getLicenseAppType().getCode().toLowerCase());
                emailBody = licenseMessageSource.getMessage(
                        emailCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(), license.getLicenseNumber(),
                                license.getTotalBalance().toString(), getDomainURL(),
                                getMunicipalityName()},
                        locale);
                String smsCode = "msg.newTradeLicenseapprovalAmt.sms";
                smsMsg = licenseMessageSource.getMessage(
                        smsCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getTotalBalance().toString(), getDomainURL(),
                                getMunicipalityName()},
                        locale);
            }
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseApproval.email.subject",
                    new String[]{license.getNameOfEstablishment()}, locale);
        } else if (STATUS_CANCELLED.equalsIgnoreCase(license.getStatus().getStatusCode())) {
            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecancelled.sms",
                    new String[]{license.getLicensee().getApplicantName(),
                            license.getApplicationNumber(),
                            getCityName(),
                            getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_CANCEL_BODY, license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            license.getApplicationNumber(),
                            license.getNameOfEstablishment(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecancelled.email.subject",
                    new String[]{license.getNameOfEstablishment()}, locale);
        }
        sendEmailAndSms(license.getLicensee(), emailSubject, emailBody, smsMsg);
    }

    public void sendSMsAndEmailOnCollection(TradeLicense license, BigDecimal demandAmount) {
        String smsMsg;
        String emailBody;
        String emailSubject;
        Locale locale = Locale.getDefault();
        if ("First Level Fee Collected".equals(license.getState().getValue()) || (license.getEgwStatus() != null
                && APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE.equals(license.getEgwStatus().getCode()))) {
            smsMsg = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_FIRSTLEVEL_SMS, license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            demandAmount.toString(),
                            license.getNameOfEstablishment(),
                            license.getApplicationNumber(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_FIRSTLEVEL_SUBJECT, license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{license.getLicenseAppType().getName()}, locale);
            emailBody = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_FIRSTLEVEL_BODY, license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            demandAmount.toString(),
                            license.getNameOfEstablishment(),
                            license.getApplicationNumber(),
                            getMunicipalityName()},
                    locale);
        } else {
            smsMsg = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_SECONDLEVEL_SMS, license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            demandAmount.toString(),
                            license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecollection.email.subject",
                    new String[]{license.getLicenseNumber()}, locale);
            emailBody = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_SECONDLEVEL_BODY, license.getLicenseAppType().getCode().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            demandAmount.toString(),
                            license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName()},
                    locale);
        }
        sendEmailAndSms(license.getLicensee(), emailSubject, emailBody, smsMsg);
    }

    public void sendLicenseClosureMessage(TradeLicense license, String workflowAction) {
        String smsMsg = null;
        String emailBody = null;
        String emailSubject = null;
        Locale locale = Locale.getDefault();

        if (license.getLicenseNumber() != null && BUTTONFORWARD.equals(workflowAction)) {

            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseclosure.sms",
                    new String[]{license.getLicensee().getApplicantName(), license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseclosure.email.body",
                    new String[]{license.getLicensee().getApplicantName(), license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseclosure.email.subject",
                    new String[]{license.getNameOfEstablishment()}, locale);

        } else if (license.getLicenseNumber() != null && BUTTONAPPROVE.equals(workflowAction)) {
            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseclosureapproval.sms",
                    new String[]{license.getLicensee().getApplicantName(), license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseclosureapproval.email.body",
                    new String[]{license.getLicensee().getApplicantName(),
                            license.getNameOfEstablishment(),
                            license.getLicenseNumber(), getDomainURL(),
                            license.getApplicationNumber(), license.getDigiSignedCertFileStoreId(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseclosureapproval.email.subject",
                    new String[]{license.getNameOfEstablishment()}, locale);

        }

        sendEmailAndSms(license.getLicensee(), emailSubject, emailBody, smsMsg);

    }

    public void sendSMsAndEmailOnDigitalSign(final TradeLicense license) {
        Locale locale = Locale.getDefault();
        String smsCode = "msg.digi.sign.no.collection";
        String smsMsg = licenseMessageSource.getMessage(
                smsCode,
                new String[]{license.getLicensee().getApplicantName(),
                        license.getApplicationNumber(),
                        license.getNameOfEstablishment(),
                        license.getLicenseNumber(), getDomainURL(), license.getDigiSignedCertFileStoreId(),
                        getMunicipalityName()},
                locale);
        String emailSubject = licenseMessageSource.getMessage("msg.Licensedigisign.email.subject",
                new String[]{license.getNameOfEstablishment()}, locale);
        String emailBody = licenseMessageSource.getMessage(
                "msg.digi.sign.no.collection",
                new String[]{license.getLicensee().getApplicantName(),
                        license.getApplicationNumber(),
                        license.getNameOfEstablishment(),
                        license.getLicenseNumber(), getDomainURL(), license.getDigiSignedCertFileStoreId(),
                        getMunicipalityName()},
                locale);
        sendEmailAndSms(license.getLicensee(), emailSubject, emailBody, smsMsg);
    }

    public void sendNotificationOnDemandGeneration(TradeLicense license, Installment installment,
                                                   ReportOutput reportOutput, Date penaltyDate) {
        Locale locale = Locale.getDefault();
        String smsBody = licenseMessageSource.getMessage("msg.demand.generation.sms.body",
                new String[]{license.getLicensee().getApplicantName(),
                        license.getNameOfEstablishment(),
                        license.getLicenseNumber(),
                        getDefaultFormattedDate(license.getDateOfExpiry()),
                        getDefaultFormattedDate(penaltyDate),
                        getDomainURL(),
                        license.getId().toString(),
                        getMunicipalityName()},
                locale);

        String emailSubject = licenseMessageSource.getMessage("msg.demand.generation.email.subject",
                new String[]{cFinancialYearService.getFinancialYearByDate(installment.getFromDate()).getFinYearRange(),
                        license.getLicenseNumber()},
                locale);

        String emailBody = licenseMessageSource.getMessage("msg.demand.generation.email.body",
                new String[]{license.getLicensee().getApplicantName(),
                        license.getNameOfEstablishment(),
                        license.getLicenseNumber(),
                        getDefaultFormattedDate(license.getDateOfExpiry()),
                        getDefaultFormattedDate(penaltyDate),
                        getDomainURL(),
                        license.getId().toString(),
                        getMunicipalityName()},
                locale);
        notificationService.sendSMS(license.getLicensee().getMobilePhoneNumber(), smsBody);
        notificationService.sendEmailWithAttachment(license.getLicensee().getEmailId(), emailSubject, emailBody,
                "application/pdf", "demand_notice", reportOutput.getReportOutputData());
    }

    private void sendEmailAndSms(Licensee licensee, String emailSubject, String emailMessage, String smsMessage) {
        if (isNotBlank(smsMessage))
            sendSMS(licensee.getMobilePhoneNumber(), smsMessage);
        if (isNotBlank(emailMessage))
            sendEmail(licensee.getEmailId(), emailMessage, emailSubject);
    }
}