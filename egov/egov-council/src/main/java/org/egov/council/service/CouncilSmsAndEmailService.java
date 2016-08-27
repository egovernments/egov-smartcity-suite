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
package org.egov.council.service;

import java.util.List;
import java.util.Locale;

import org.egov.council.entity.CommitteeMembers;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.messaging.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CouncilSmsAndEmailService {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    /**
     * @return this method will send SMS and Email is isSmsEnabled is true
     * @param CouncilMeeting
     * @param workFlowAction
     */

    public void sendSmsAndEmail(CouncilMeeting councilMeeting) {
        String email_id = null;
        String mobileno = null;

        List<CommitteeMembers> committeeMembersList = councilMeeting.getCommitteeType().getCommiteemembers();
        for (CommitteeMembers committeeMembers : committeeMembersList) {
            email_id = committeeMembers.getCouncilMember().getEmailId();
            mobileno = committeeMembers.getCouncilMember().getMobileNumber();
            if (email_id != null || mobileno != null) {
                getSmsAndEmailForMeeting(email_id, mobileno, councilMeeting);
            }
        }

    }

    /**
     * @return SMS AND EMAIL body and subject For New Connection
     * @param CouncilMeeting
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */
    public void getSmsAndEmailForMeeting(final String email, final String mobileNumber,
            final CouncilMeeting councilMeeting) {
        String smsMsg = null;
        String body = "";
        String subject = "";

        if (isSmsEnabled()) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.meeting.sms", councilMeeting,
                    CouncilConstants.SMSEMAILTYPEFORCOUNCILMEETING);
            if (mobileNumber != null && smsMsg != null)
                sendSMSOnSewerageForMeeting(mobileNumber, smsMsg);
        }
        if (isEmailEnabled()) {
            body = EmailBodyByCodeAndArgsWithType("email.meeting.subject", councilMeeting,
                    CouncilConstants.SMSEMAILTYPEFORCOUNCILMEETING);
            subject = emailSubjectforEmailByCodeAndArgs("email.meeting.body", councilMeeting);

            if (email != null && body != null)
                sendEmailOnSewerageForMeeting(email, body, subject);
        }
    }

    /**
     * .
     * 
     * @param code
     * @param CouncilMeeting
     * @param applicantName
     * @param type
     * @return EmailBody for All Connection based on Type
     */
    public String EmailBodyByCodeAndArgsWithType(final String code, final CouncilMeeting councilMeeting,
            final String type) {
        String emailBody = "";

        if (type.equalsIgnoreCase(CouncilConstants.SMSEMAILTYPEFORCOUNCILMEETING)) {
            emailBody = messageSource.getMessage(code, new String[] { String.valueOf(councilMeeting.getMeetingDate()),
                    String.valueOf(councilMeeting.getMeetingTime()) }, null);
        }

        return emailBody;
    }

    /**
     * @param code
     * @param CouncilMeeting
     * @param applicantName
     * @param type
     */
    public String SmsBodyByCodeAndArgsWithType(final String code, final CouncilMeeting councilMeeting,
            final String type) {
        String smsMsg = "";

        if (type.equalsIgnoreCase(CouncilConstants.SMSEMAILTYPEFORCOUNCILMEETING)) {
            smsMsg = messageSource.getMessage(code,
                    new String[] { councilMeeting.getCommitteeType().getName(),
                            String.valueOf(councilMeeting.getMeetingDate()),
                            String.valueOf(councilMeeting.getMeetingTime()) },
                    null);
        }
        return smsMsg;
    }

    public Boolean isSmsEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService
                .getConfigValuesByModuleAndKey(CouncilConstants.MODULE_NAME, CouncilConstants.SENDSMSFORCOUNCIL).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isEmailEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService
                .getConfigValuesByModuleAndKey(CouncilConstants.MODULE_NAME, CouncilConstants.SENDEMAILFORCOUNCIL)
                .get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(final String code, final String applicantName) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String smsMsg = messageSource.getMessage(code, new String[] {}, locale);
        return smsMsg;
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final CouncilMeeting councilMeeting) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String emailSubject = messageSource.getMessage(code, new String[] {}, locale);
        return emailSubject;
    }

    public void sendSMSOnSewerageForMeeting(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnSewerageForMeeting(final String email, final String emailBody, final String emailSubject) {
        messagingService.sendEmail(email, emailSubject, emailBody);
    }

}