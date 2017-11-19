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
package org.egov.mrs.domain.service;

import static org.egov.mrs.application.MarriageConstants.MODULE_NAME;
import static org.egov.mrs.application.MarriageConstants.SENDEMAILFROOMMARRIAGEMODULE;
import static org.egov.mrs.application.MarriageConstants.SENDSMSFROOMMARRIAGEMODULE;

import java.util.List;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.notification.service.NotificationService;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MarriageSmsAndEmailService {
    private static final String MSG_KEY_SMS_REGISTRATION_NEW = "msg.newregistration.sms";
    private static final String MSG_KEY_SMS_REGISTRATION_REJECTION = "msg.rejectregistration.sms";
    private static final String MSG_KEY_SMS_REGISTRATION_APPROVED= "msg.registration.approved.sms";
    private static final String MSG_KEY_SMS_REGISTRATION_REGISTERED = "msg.registration.registered.sms";
    private static final String MSG_KEY_SMS_REISSUE_REGISTERED  ="msg.reissue.registered.sms";
    private static final String MSG_KEY_EMAIL_REGISTRATION_NEW_EMAIL = "msg.newregistration.mail";
    private static final String MSG_KEY_EMAIL_REGISTRATION_NEW_SUBJECT = "msg.newregistration.mail.subject";
    private static final String MSG_KEY_EMAIL_REGISTRATION_REJECTION_EMAIL = "msg.rejectionregistration.mail";
    private static final String MSG_KEY_EMAIL_REGISTRATION_REJECTION_SUBJECT = "msg.rejectionregistration.mail.subject";
    private static final String MSG_KEY_EMAIL_REGISTRATION_REGISTERED = "msg.registration.registered.mail";
    private static final String MSG_KEY_EMAIL_REGISTRATION_REGISTERED_SUBJECT = "msg.registration.registered.mail.subject";

    private static final String MSG_KEY_SMS_REISSUE_NEW = "msg.reissue.sms";
    private static final String MSG_KEY_SMS_REISSUE_REJECTION = "msg.reissuerejected.sms";
    private static final String MSG_KEY_SMS_REISSUE_APPROVED = "msg.reissue.approved.sms";

    private static final String MSG_KEY_EMAIL_REISSUE_NEW_EMAIL = "msg.reissue.mail";
    private static final String MSG_KEY_EMAIL_REISSUE_NEW_SUBJECT = "msg.reissue.mail.subject";
    private static final String MSG_KEY_EMAIL_REISSUE_REJECTION_EMAIL = "msg.reissuerejected.mail";
    private static final String MSG_KEY_EMAIL_REISSUE_REJECTION_SUBJECT = "msg.reissuerejected.mail.subject";
    private static final String MSG_KEY_EMAIL_REISSUE_APPROVED_EMAIL = "msg.reissueApprove.mail";
    private static final String MSG_KEY_EMAIL_REISSUE_APPROVED_SUBJECT = "msg.reissueApprove.mail.subject";

    @Autowired
    private NotificationService notificationService;
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource mrsMessageSource;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private MarriageCertificateService marriageCertificateService;

    public void sendSMS(final MarriageRegistration registration, String status) {
        String msgKey = MSG_KEY_SMS_REGISTRATION_NEW;
        String referenceNumber;
        if (isSmsEnabled() && registration.getApplicationNo() != null) {

            referenceNumber = registration.getApplicationNo();
            if (registration.getStatus() != null && registration.getStatus().getCode()
                    .equalsIgnoreCase(MarriageRegistration.RegistrationStatus.CANCELLED.toString())) {
                msgKey = MSG_KEY_SMS_REGISTRATION_REJECTION;

            } else if (registration.getStatus() != null && (registration.getStatus().getCode()
                    .equalsIgnoreCase(MarriageRegistration.RegistrationStatus.REGISTERED.toString())
                    || registration.getStatus().getCode()
                            .equalsIgnoreCase(MarriageRegistration.RegistrationStatus.DIGITALSIGNED.toString()))) {
                msgKey = MSG_KEY_SMS_REGISTRATION_REGISTERED; 
                referenceNumber = registration.getRegistrationNo();
            } else if (registration.getStatus() != null && (registration.getStatus().getCode()
                    .equalsIgnoreCase(MarriageRegistration.RegistrationStatus.APPROVED.toString()))){
                msgKey = MSG_KEY_SMS_REGISTRATION_APPROVED;
                referenceNumber = registration.getRegistrationNo();
                
            }
                
            final String message = buildEmailMessage(registration, msgKey, referenceNumber);
            if (registration.getHusband() != null && registration.getHusband().getContactInfo() != null
                    && registration.getHusband().getContactInfo().getMobileNo() != null)
                notificationService.sendSMS(registration.getHusband().getContactInfo().getMobileNo(), message);
            if (registration.getWife() != null && registration.getWife().getContactInfo() != null
                    && registration.getWife().getContactInfo().getMobileNo() != null)
                notificationService.sendSMS(registration.getWife().getContactInfo().getMobileNo(), message);
        }
    }

    public void sendEmail(final MarriageRegistration registration, String status) {
        String msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_NEW_EMAIL;
        String msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_NEW_SUBJECT;
        String referenceNumber;
        if (isEmailEnabled() && registration.getApplicationNo() != null) {
            referenceNumber = registration.getApplicationNo();
            if (registration.getStatus() != null && registration.getStatus().getCode()
                    .equalsIgnoreCase(MarriageRegistration.RegistrationStatus.CANCELLED.toString())) {
                msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_REJECTION_EMAIL;
                msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_REJECTION_SUBJECT;
            } else if (registration.getStatus() != null && registration.getStatus().getCode()
                    .equalsIgnoreCase(MarriageRegistration.RegistrationStatus.APPROVED.toString())) {
                msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_REGISTERED;
                msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_REGISTERED_SUBJECT;
                referenceNumber = registration.getRegistrationNo();
            }
            final String message = buildEmailMessage(registration, msgKeyMail, referenceNumber);

            final String subject = mrsMessageSource.getMessage(msgKeyMailSubject, null, null);
            if (registration.getHusband() != null && registration.getHusband().getContactInfo() != null
                    && registration.getHusband().getContactInfo().getEmail() != null)
                notificationService.sendEmail(registration.getHusband().getContactInfo().getEmail(), subject, message);
            if (registration.getWife() != null && registration.getWife().getContactInfo() != null
                    && registration.getWife().getContactInfo().getEmail() != null)
                notificationService.sendEmail(registration.getWife().getContactInfo().getEmail(), subject, message);
        }
    }

    public void sendSMSForReIssueApplication(final ReIssue reIssue) {
        String msgKey = MSG_KEY_SMS_REISSUE_NEW;

        if (isSmsEnabled() && null != reIssue.getApplicationNo() && null != reIssue.getApplicant().getContactInfo().getMobileNo()) {
            if (reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.CANCELLED.toString()))
                msgKey = MSG_KEY_SMS_REISSUE_REJECTION;
            else if (reIssue.getStatus() != null
                    && reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.APPROVED.toString())) {
                msgKey = MSG_KEY_SMS_REISSUE_APPROVED;
            }
            else if (null!=reIssue.getStatus() && 
                    (reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.CERTIFICATEREISSUED.toString())||
                            reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.DIGITALSIGNED.toString()))){
                msgKey= MSG_KEY_SMS_REISSUE_REGISTERED;
            }
            final String message = buildMessageForIssueCertificate(reIssue, msgKey);
            notificationService.sendSMS(reIssue.getApplicant().getContactInfo().getMobileNo(), message);
        }
    }

    public void sendEmailForReIssueApplication(final ReIssue reIssue) {
        String msgKeyMail = MSG_KEY_EMAIL_REISSUE_NEW_EMAIL;
        String msgKeyMailSubject = MSG_KEY_EMAIL_REISSUE_NEW_SUBJECT;

        if (isEmailEnabled() && null != reIssue.getApplicationNo()
                && null != reIssue.getApplicant().getContactInfo().getEmail()) {
            if (reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.CANCELLED.toString())) {
                msgKeyMail = MSG_KEY_EMAIL_REISSUE_REJECTION_EMAIL;
                msgKeyMailSubject = MSG_KEY_EMAIL_REISSUE_REJECTION_SUBJECT;
            } else if (reIssue.getStatus() != null
                    && reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.APPROVED.toString())) {
                msgKeyMail = MSG_KEY_EMAIL_REISSUE_APPROVED_EMAIL;
                msgKeyMailSubject = MSG_KEY_EMAIL_REISSUE_APPROVED_SUBJECT;
            }
            final String message = buildMessageForIssueCertificate(reIssue, msgKeyMail);
            final String subject = mrsMessageSource.getMessage(msgKeyMailSubject, null, null);
            notificationService.sendEmail(reIssue.getApplicant().getContactInfo().getEmail(), subject, message);
        }
    }

    private String buildEmailMessage(final MarriageRegistration registration, String msgKeyMail, String number) {
        MarriageCertificate marriageCertificate = marriageCertificateService.getGeneratedCertificate(registration);
        String pdfLink = null;
        if(null!=marriageCertificate && marriageCertificate.getId()!=null)
         pdfLink = ApplicationThreadLocals.getDomainURL()+"/mrs/registration/printcertficate/"+marriageCertificate.getId();
       return mrsMessageSource.getMessage(msgKeyMail,
                new String[] { registration.getHusband().getFullName(), registration.getWife().getFullName(), registration.getApplicationNo(), pdfLink,ApplicationThreadLocals.getMunicipalityName() },
                null);
    }

    private String buildMessageForIssueCertificate(final ReIssue reIssue, String msgKeyMail) {
        MarriageCertificate marriageCertificate = marriageCertificateService.getGeneratedReIssueCertificateForPrint(reIssue);
        String pdfLink = null;
        if(null!=marriageCertificate && marriageCertificate.getId()!=null)
         pdfLink = ApplicationThreadLocals.getDomainURL()+"/mrs/registration/printcertficate/"+marriageCertificate.getId();
        return mrsMessageSource.getMessage(msgKeyMail,
                new String[] { reIssue.getApplicant().getFullName(), reIssue.getApplicationNo(), pdfLink,ApplicationThreadLocals.getMunicipalityName()}, null);
    }

    public Boolean isSmsEnabled() {
        return getAppConfigValueByPassingModuleAndType(MODULE_NAME, SENDSMSFROOMMARRIAGEMODULE);
    }

    public Boolean getAppConfigValueByPassingModuleAndType(String moduleName, String sendsmsoremail) {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName,
                sendsmsoremail);
        return "YES".equalsIgnoreCase(
                appConfigValue != null && !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : "NO");
    }

    public Boolean isEmailEnabled() {
        return getAppConfigValueByPassingModuleAndType(MODULE_NAME, SENDEMAILFROOMMARRIAGEMODULE);
    }

}