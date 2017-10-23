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
package org.egov.tl.service;

import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.notification.service.NotificationService;
import org.egov.tl.entity.License;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Locale;

import static org.egov.tl.utils.Constants.WF_DIGI_SIGNED;
import static org.egov.tl.utils.Constants.APPLICATION_STATUS_APPROVED_CODE;
import static org.egov.tl.utils.Constants.APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.STATUS_UNDERWORKFLOW;
import static org.egov.tl.utils.Constants.STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.BUTTONFORWARD;

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
    private LicenseUtils licenseUtils;

    public void sendSMSOnLicense(final String mobileNumber, final String smsBody) {
        notificationService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnLicense(final String email, final String emailBody, final String emailSubject) {
        notificationService.sendEmail(email, emailSubject, emailBody);
    }

    public String getMunicipalityName() {
        return ApplicationThreadLocals.getMunicipalityName();
    }

    public void sendSmsAndEmail(final License license, final String workFlowAction) {
        String smsMsg = null;
        String emailBody = "";
        String emailSubject = "";
        final Locale locale = Locale.getDefault();
        final String[] strarr = getMunicipalityName().split(" ");
        final String cityname = strarr[0];
        String emailCode;
        String smsCode;
        if (license.getState().getHistory().isEmpty() && license.isAcknowledged()) {

            smsMsg = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_CREATE_SMS, license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            license.getApplicationNumber(),
                            getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_CREATE_BODY, license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            license.getNameOfEstablishment(),
                            license.getApplicationNumber(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage(String.format(MSG_LICENSE_CREATE_SUBJECT,
                    license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{getMunicipalityName()}, locale);

        } else if (workFlowAction.equals(BUTTONAPPROVE) && STATUS_UNDERWORKFLOW.equalsIgnoreCase(license.getStatus().getStatusCode())) {
            BigDecimal demAmt = BigDecimal.ZERO;
            for (final EgDemandDetails dmdDtls : license.getCurrentDemand().getEgDemandDetails())
                demAmt = demAmt.add(dmdDtls.getAmount().subtract(dmdDtls.getAmtCollected()));

            if (licenseUtils.isDigitalSignEnabled() && demAmt.compareTo(BigDecimal.ZERO) == 0) {

                emailCode = String.format(MSG_LICENSE_DIGI_APPROVAL_BODY,
                        license.getLicenseAppType().getName().toLowerCase());
                emailBody = licenseMessageSource.getMessage(
                        emailCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getLicenseNumber(),
                                getMunicipalityName()},
                        locale);
                smsCode = "msg.digi.enabled.newTradeLicenseapproval.sms";
                smsMsg = licenseMessageSource.getMessage(
                        smsCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getLicenseNumber(),
                                getMunicipalityName()},
                        locale);
            } else if (!licenseUtils.isDigitalSignEnabled() && demAmt.compareTo(BigDecimal.ZERO) == 0) {
                emailCode = String.format(MSG_LICENSE_APPROVAL_BODY,
                        license.getLicenseAppType().getName().toLowerCase());
                emailBody = licenseMessageSource.getMessage(
                        emailCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getLicenseNumber(),
                                getMunicipalityName()},
                        locale);
                smsCode = "msg.newTradeLicenseapproval.sms";
                smsMsg = licenseMessageSource.getMessage(
                        smsCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getLicenseNumber(),
                                getMunicipalityName()},
                        locale);
            } else if (demAmt.compareTo(BigDecimal.ZERO) > 0 && licenseUtils.isDigitalSignEnabled()) {
                emailCode = String.format(MSG_LICENSE_DIGI_APPROVALAMT_BODY,
                        license.getLicenseAppType().getName().toLowerCase());
                emailBody = licenseMessageSource.getMessage(
                        emailCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(), license.getLicenseNumber(), license.getTotalBalance().toString(), ApplicationThreadLocals.getDomainURL(),
                                getMunicipalityName()}, locale);
                smsCode = "msg.digi.enabled.newTradeLicenseapprovalAmt.sms";
                smsMsg = licenseMessageSource.getMessage(
                        smsCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(), license.getLicenseNumber(),
                                license.getTotalBalance().toString(), ApplicationThreadLocals.getDomainURL(),
                                getMunicipalityName()},
                        locale);
            } else if (demAmt.compareTo(BigDecimal.ZERO) > 0 && !licenseUtils.isDigitalSignEnabled()) {
                emailCode = String.format(MSG_LICENSE_APPROVALAMT_BODY,
                        license.getLicenseAppType().getName().toLowerCase());
                emailBody = licenseMessageSource.getMessage(
                        emailCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(), license.getLicenseNumber(), license.getTotalBalance().toString(), ApplicationThreadLocals.getDomainURL(),
                                getMunicipalityName()},
                        locale);
                smsCode = "msg.newTradeLicenseapprovalAmt.sms";
                smsMsg = licenseMessageSource.getMessage(
                        smsCode,
                        new String[]{license.getLicensee().getApplicantName(),
                                license.getApplicationNumber(),
                                license.getNameOfEstablishment(),
                                license.getTotalBalance().toString(), ApplicationThreadLocals.getDomainURL(),
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
                            cityname,
                            getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_CANCEL_BODY, license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            license.getApplicationNumber(),
                            license.getNameOfEstablishment(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecancelled.email.subject",
                    new String[]{license.getNameOfEstablishment()}, locale);
        }
        sendSMSOnLicense(license.getLicensee().getMobilePhoneNumber(), smsMsg);
        sendEmailOnLicense(license.getLicensee().getEmailId(), emailBody, emailSubject);
    }

    public void sendSMsAndEmailOnCollection(final License license, final BigDecimal demandAmount) {
        String smsMsg;
        String emailBody;
        String emailSubject;
        final Locale locale = Locale.getDefault();
        if ("First Level Fee Collected".equals(license.getState().getValue()) || (license.getEgwStatus() != null && APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE.equals(license.getEgwStatus().getCode()))) {
            smsMsg = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_FIRSTLEVEL_SMS, license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            demandAmount.toString(),
                            license.getNameOfEstablishment(),
                            license.getApplicationNumber(),
                            ApplicationThreadLocals.getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_FIRSTLEVEL_SUBJECT, license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{license.getLicenseAppType().getName()}, locale);
            emailBody = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_FIRSTLEVEL_BODY, license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            demandAmount.toString(),
                            license.getNameOfEstablishment(),
                            license.getApplicationNumber(),
                            ApplicationThreadLocals.getMunicipalityName()},
                    locale);
        } else {
            smsMsg = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_SECONDLEVEL_SMS, license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            demandAmount.toString(),
                            license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            ApplicationThreadLocals.getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecollection.email.subject",
                    new String[]{license.getLicenseNumber()}, locale);
            emailBody = licenseMessageSource.getMessage(
                    String.format(MSG_LICENSE_SECONDLEVEL_BODY, license.getLicenseAppType().getName().toLowerCase()),
                    new String[]{license.getLicensee().getApplicantName(),
                            demandAmount.toString(),
                            license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            ApplicationThreadLocals.getMunicipalityName()},
                    locale);
        }
        sendSMSOnLicense(license.getLicensee().getMobilePhoneNumber(), smsMsg);
        sendEmailOnLicense(license.getLicensee().getEmailId(), emailBody,
                emailSubject);
    }

    public void sendSMsAndEmailOnClosure(final License license, final String workflowAction) {
        String smsMsg = null;
        String emailBody = "";
        String emailSubject = "";
        final Locale locale = Locale.getDefault();

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
                    new String[]{license.getLicensee().getApplicantName(), license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseclosureapproval.email.subject",
                    new String[]{license.getNameOfEstablishment()}, locale);

        }

        sendSMSOnLicense(license.getLicensee().getMobilePhoneNumber(), smsMsg);
        sendEmailOnLicense(license.getLicensee().getEmailId(), emailBody, emailSubject);

    }

    public void sendSMsAndEmailOnDigitalSign(final License license) {
        final Locale locale = Locale.getDefault();
        String smsCode = "msg.digi.sign.no.collection";
        String smsMsg = licenseMessageSource.getMessage(
                smsCode,
                new String[]{license.getLicensee().getApplicantName(),
                        license.getApplicationNumber(),
                        license.getNameOfEstablishment(),
                        license.getLicenseNumber(), ApplicationThreadLocals.getDomainURL(), license.getDigiSignedCertFileStoreId(),
                        getMunicipalityName()},
                locale);
        String emailSubject = licenseMessageSource.getMessage("msg.Licensedigisign.email.subject",
                new String[]{license.getNameOfEstablishment()}, locale);
        String emailBody = licenseMessageSource.getMessage(
                "msg.digi.sign.no.collection",
                new String[]{license.getLicensee().getApplicantName(),
                        license.getApplicationNumber(),
                        license.getNameOfEstablishment(),
                        license.getLicenseNumber(), ApplicationThreadLocals.getDomainURL(), license.getDigiSignedCertFileStoreId(),
                        getMunicipalityName()},
                locale);
        sendSMSOnLicense(license.getLicensee().getMobilePhoneNumber(), smsMsg);
        sendEmailOnLicense(license.getLicensee().getEmailId(), emailBody,
                emailSubject);
    }
}
