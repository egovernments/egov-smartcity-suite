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

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.messaging.MessagingService;
import org.egov.tl.entity.License;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;

@Service
public class TradeLicenseSmsAndEmailService {
    @Autowired
    private MessagingService messagingService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource licenseMessageSource;

    public void sendSMSOnLicense(String mobileNumber, String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnLicense(String email, String emailBody, String emailSubject) {
        messagingService.sendEmail(email, emailSubject, emailBody);
    }

    public String getMunicipalityName() {
        return ApplicationThreadLocals.getMunicipalityName();
    }

    public void sendSmsAndEmail(License license, String workFlowAction) {
        String mobileNumber = license.getLicensee() != null && license.getLicensee().getMobilePhoneNumber() != null
                ? license.getLicensee().getMobilePhoneNumber() : null;
        String email = license.getLicensee() != null && license.getLicensee().getEmailId() != null
                ? license.getLicensee().getEmailId() : null;
        getSmsAndEmailForNewTradeLicense(license, workFlowAction, email, mobileNumber);
    }

    public void getSmsAndEmailForNewTradeLicense(License license, String workFlowAction, String email,
                                                 String mobileNumber) {
        String smsMsg = null;
        String emailBody = "";
        String emailSubject = "";
        Locale locale = Locale.getDefault();
        String[] strarr = getMunicipalityName().split(" ");
        String cityname = strarr[0];
        String smsCode;
        String emailCode;
        if (license.getState().getHistory().isEmpty()
                && Constants.STATUS_ACKNOLEDGED.equalsIgnoreCase(license.getStatus().getStatusCode())) {
            if (license.getLicenseAppType() != null
                    && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)) {
                smsCode = "msg.renewTradeLicensecreator.sms";
                emailCode = "msg.renewTradeLicensecreate.email.body";
            } else {
                smsCode = "msg.newTradeLicensecreator.sms";
                emailCode = "msg.newTradeLicensecreate.email.body";
            }
            smsMsg = licenseMessageSource.getMessage(
                    smsCode,
                    new String[]{license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                            getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    emailCode,
                    new String[]{license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                            getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecreate.email.subject",
                    new String[]{license.getApplicationNumber()}, locale);
        } else if (workFlowAction.equals(Constants.BUTTONAPPROVE)
                && Constants.STATUS_UNDERWORKFLOW.equalsIgnoreCase(license.getStatus()
                .getStatusCode())) {
            BigDecimal demAmt = license.getCurrentLicenseFee();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (license.getLicenseAppType() != null
                    && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
                emailCode = "msg.renewTradeLicenseapproval.email.body";
            else
                emailCode = "msg.newTradeLicenseapproval.email.body";
            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseapproval.sms",
                    new String[]{license.getLicensee().getApplicantName(), license.getLicenseNumber(),
                            demAmt.toString(), formatter.format(license.getApplicationDate()), cityname, getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    emailCode,
                    new String[]{license.getLicensee().getApplicantName(), license.getLicenseNumber(),
                            demAmt.toString(), formatter.format(license.getApplicationDate()), cityname, getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseApproval.email.subject",
                    new String[]{license.getLicenseNumber()}, locale);
        } else if (Constants.STATUS_CANCELLED.equalsIgnoreCase(license.getStatus()
                .getStatusCode())) {
            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecancelled.sms",
                    new String[]{license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                            cityname, getMunicipalityName()},
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecancelled.email.body",
                    new String[]{license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                            cityname, getMunicipalityName()},
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecancelled.email.subject",
                    new String[]{license.getApplicationNumber()}, locale);
        }
        sendSMSOnLicense(mobileNumber, smsMsg);
        sendEmailOnLicense(email, emailBody, emailSubject);
    }

    public void sendSMsAndEmailOnCollection(License license, Date receiptDate, BigDecimal demandAmount) {
        String message;
        if (license.getLicenseNumber() != null) {
            message = String.format(
                    "Dear %s,%nTrade License with TIN No.%s, fee collected is at the rate of Rs.%s/- per year w.e.f %s.%nThanks,%n%s",
                    license.getLicensee().getApplicantName(), license.getLicenseNumber(),
                    demandAmount.toString(),
                    toDefaultDateFormat(receiptDate), ApplicationThreadLocals.getMunicipalityName());
        } else {
            message = String.format(
                    "Dear %s, %nYour Trade License application with number %s has been accepted, please use this number for future reference.%nThanks,%n%s",
                    license.getLicensee().getApplicantName(), license.getApplicationNumber(), ApplicationThreadLocals.getMunicipalityName());
        }
        StringBuilder subject = new StringBuilder();
        subject.append(Constants.STR_FOR_EMAILSUBJECT).append(license.getLicenseNumber());
        messagingService.sendSMS(license.getLicensee().getMobilePhoneNumber(), message);
        messagingService.sendEmail(license.getLicensee().getEmailId(), subject.toString(),
                message);
    }

}
