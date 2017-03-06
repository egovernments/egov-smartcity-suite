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

import java.math.BigDecimal;
import java.util.Locale;

import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.messaging.MessagingService;
import org.egov.tl.entity.License;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class TradeLicenseSmsAndEmailService {
    @Autowired
    private MessagingService messagingService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource licenseMessageSource;

    public void sendSMSOnLicense(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnLicense(final String email, final String emailBody, final String emailSubject) {
        messagingService.sendEmail(email, emailSubject, emailBody);
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
        if (license.getState().getHistory().isEmpty() && license.isAcknowledged()) {

            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecreator.sms",
                    new String[] { license.getLicensee().getApplicantName(), license.getLicenseAppType().getName(),
                            license.getApplicationNumber(), getMunicipalityName() },
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecreate.email.body",
                    new String[] { license.getLicensee().getApplicantName(), license.getLicenseAppType().getName(),
                            license.getNameOfEstablishment(), license.getApplicationNumber(),
                            getMunicipalityName() },
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecreate.email.subject",
                    new String[] { license.getApplicationNumber() }, locale);
        } else if (workFlowAction.equals(Constants.BUTTONAPPROVE)
                && Constants.STATUS_UNDERWORKFLOW.equalsIgnoreCase(license.getStatus()
                        .getStatusCode())) {
            BigDecimal demAmt = BigDecimal.ZERO;
            for (final EgDemandDetails dmdDtls : license.getCurrentDemand().getEgDemandDetails())
                demAmt = demAmt.add(dmdDtls.getAmount().subtract(dmdDtls.getAmtCollected()));

            if (demAmt.compareTo(BigDecimal.ZERO) == 0)
                emailCode = "msg.newTradeLicenseapproval.email.body";
            else

                emailCode = "msg.newTradeLicenseapprovalAmt.email.body";

            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseapproval.sms",
                    new String[] { license.getLicensee().getApplicantName(),
                            license.getApplicationNumber(),
                            license.getNameOfEstablishment(), license.getLicenseNumber(), getMunicipalityName() },
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    emailCode,
                    new String[] { license.getLicensee().getApplicantName(), license.getLicenseAppType().getName(),
                            license.getApplicationNumber(),
                            license.getNameOfEstablishment(),
                            license.getLicenseNumber(), getMunicipalityName() },
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseApproval.email.subject",
                    new String[] { license.getNameOfEstablishment() }, locale);
        } else if (Constants.STATUS_CANCELLED.equalsIgnoreCase(license.getStatus()
                .getStatusCode())) {
            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecancelled.sms",
                    new String[] { license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                            cityname, getMunicipalityName() },
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecancelled.email.body",
                    new String[] { license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                            license.getNameOfEstablishment(),
                            getMunicipalityName() },
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecancelled.email.subject",
                    new String[] { license.getNameOfEstablishment() }, locale);
        }
        sendSMSOnLicense(license.getLicensee().getMobilePhoneNumber(), smsMsg);
        sendEmailOnLicense(license.getLicensee().getEmailId(), emailBody, emailSubject);
    }

    public void sendSMsAndEmailOnCollection(final License license, final BigDecimal demandAmount) {
        String smsMsg;
        String emailBody;
        String emailSubject;
        final Locale locale = Locale.getDefault();

        if (Constants.APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE.equals(license.getEgwStatus().getCode())) {

            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseFirstcollection.sms",
                    new String[] { license.getLicensee().getApplicantName(), demandAmount.toString(),
                            license.getLicenseAppType().getName(),
                            license.getNameOfEstablishment(),
                            license.getApplicationNumber(), ApplicationThreadLocals.getMunicipalityName() },
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseFirstcollection.email.subject",
                    new String[] { license.getNameOfEstablishment() }, locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseFirstcollection.email.body",
                    new String[] { license.getLicensee().getApplicantName(), demandAmount.toString(),
                            license.getLicenseAppType().getName(),
                            license.getNameOfEstablishment(),
                            license.getApplicationNumber(), ApplicationThreadLocals.getMunicipalityName() },
                    locale);
        } else {
            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecollection.sms",
                    new String[] { license.getLicensee().getApplicantName(), demandAmount.toString(),
                            license.getLicenseAppType().getName(),
                            license.getNameOfEstablishment(),
                            license.getLicenseNumber(), ApplicationThreadLocals.getMunicipalityName() },
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicensecollection.email.subject",
                    new String[] { license.getLicenseNumber() }, locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicensecollection.email.body",
                    new String[] { license.getLicensee().getApplicantName(), demandAmount.toString(),
                            license.getLicenseAppType().getName(),
                            license.getNameOfEstablishment(),
                            license.getLicenseNumber(), ApplicationThreadLocals.getMunicipalityName() },
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

        if (license.getLicenseNumber() != null && Constants.BUTTONFORWARD.equals(workflowAction)) {

            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseclosure.sms",
                    new String[] { license.getLicensee().getApplicantName(), license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName() },
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseclosure.email.body",
                    new String[] { license.getLicensee().getApplicantName(), license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName() },
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseclosure.email.subject",
                    new String[] { license.getNameOfEstablishment() }, locale);

        } else if (license.getLicenseNumber() != null && Constants.BUTTONAPPROVE.equals(workflowAction)) {
            smsMsg = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseclosureapproval.sms",
                    new String[] { license.getLicensee().getApplicantName(), license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName() },
                    locale);
            emailBody = licenseMessageSource.getMessage(
                    "msg.newTradeLicenseclosureapproval.email.body",
                    new String[] { license.getLicensee().getApplicantName(), license.getNameOfEstablishment(),
                            license.getLicenseNumber(),
                            getMunicipalityName() },
                    locale);
            emailSubject = licenseMessageSource.getMessage("msg.newTradeLicenseclosureapproval.email.subject",
                    new String[] { license.getNameOfEstablishment() }, locale);

        }

        sendSMSOnLicense(license.getLicensee().getMobilePhoneNumber(), smsMsg);
        sendEmailOnLicense(license.getLicensee().getEmailId(), emailBody, emailSubject);

    }

}
