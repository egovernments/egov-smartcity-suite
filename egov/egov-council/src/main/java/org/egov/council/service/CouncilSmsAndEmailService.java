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

import static org.egov.council.utils.constants.CouncilConstants.SENDEMAILFORCOUNCIL;
import static org.egov.council.utils.constants.CouncilConstants.SENDSMSFORCOUNCIL;
import static org.egov.council.utils.constants.CouncilConstants.SMSEMAILTYPEFORCOUNCILMEETING;

import java.util.List;

import org.egov.council.entity.CommitteeMembers;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouncilSmsAndEmailService {

   private static String AT = "at ";
   private static String COUNCIL_MEETING_SCHEDULED_ON = ",Council meeting scheduled on ";
   private static  String DEAR = "Dear ";
   private static String AGENDA_SENTTO_YOUR_MAIL = ".Agenda sent to your email.";
    @Autowired
    private MessagingService messagingService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    /**
     * @return this method will send SMS and Email is isSmsEnabled is true
     * @param CouncilMeeting
     * @param workFlowAction
     */

    public void sendSmsAndEmail(CouncilMeeting councilMeeting,String customMessage) {
        String email_id = StringUtils.EMPTY;
        String mobileno = StringUtils.EMPTY;

        List<CommitteeMembers> committeeMembersList = councilMeeting.getCommitteeType().getCommiteemembers();
        for (CommitteeMembers committeeMembers : committeeMembersList) {
            email_id = committeeMembers.getCouncilMember().getEmailId();
            mobileno = committeeMembers.getCouncilMember().getMobileNumber();
            if (email_id != null || mobileno != null) {
                getSmsAndEmailForMeeting(email_id, mobileno, councilMeeting,customMessage);
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
            final CouncilMeeting councilMeeting,final String customMessage) {
        String smsMsg = StringUtils.EMPTY;
        String body = StringUtils.EMPTY;
        String subject = StringUtils.EMPTY;

        if (isSmsEnabled()) {
            smsMsg = SmsBodyByCodeAndArgsWithType("msg.meeting.sms", councilMeeting,
                    SMSEMAILTYPEFORCOUNCILMEETING,customMessage);
            if (mobileNumber != null && smsMsg != null)
                sendSMSOnSewerageForMeeting(mobileNumber, smsMsg);
        }
        if (isEmailEnabled()) {
            body = EmailBodyByCodeAndArgsWithType("email.meeting.subject", councilMeeting,
                    SMSEMAILTYPEFORCOUNCILMEETING,customMessage);
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
            final String type,final String customMessage) {
        StringBuilder emailBody  = new StringBuilder();

        if (type.equalsIgnoreCase(CouncilConstants.SMSEMAILTYPEFORCOUNCILMEETING)) {
                    emailBody
                    .append(DEAR)
                    .append(councilMeeting.getCommitteeType().getName())
                    .append(COUNCIL_MEETING_SCHEDULED_ON)
                    .append(councilMeeting.getMeetingDate())
                    .append(AT)
                    .append(councilMeeting.getMeetingTime())
                    .append(AT)
                    .append(councilMeeting.getMeetingLocation())
                    .append(customMessage);
        }

        return emailBody.toString();
    }

    /**
     * @param code
     * @param CouncilMeeting
     * @param applicantName
     * @param type
     */
    public String SmsBodyByCodeAndArgsWithType(final String code, final CouncilMeeting councilMeeting,
            final String type,final String customMessage) {
        StringBuilder smsMsg  = new StringBuilder();

        if (type.equalsIgnoreCase(CouncilConstants.SMSEMAILTYPEFORCOUNCILMEETING)) {
            
            smsMsg
                .append(DEAR)
                .append(councilMeeting.getCommitteeType().getName())
                .append(COUNCIL_MEETING_SCHEDULED_ON)
                .append(councilMeeting.getMeetingDate())
                .append(AT)
                .append(councilMeeting.getMeetingTime())
                .append(AT)
                .append(councilMeeting.getMeetingLocation())
                .append(AGENDA_SENTTO_YOUR_MAIL)
                .append(customMessage);
            }
        return smsMsg.toString();
    }

    public Boolean isSmsEnabled() {

        return getAppConfigValueByPassingModuleAndType(CouncilConstants.MODULE_NAME, SENDSMSFORCOUNCIL);
    }

    private Boolean getAppConfigValueByPassingModuleAndType(String moduleName, String sendsmsoremail) {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName,
                sendsmsoremail);

        return "YES".equalsIgnoreCase(
                appConfigValue != null && appConfigValue.size() > 0 ? appConfigValue.get(0).getValue() : "NO");
    }

    public Boolean isEmailEnabled() {

        return getAppConfigValueByPassingModuleAndType(CouncilConstants.MODULE_NAME, SENDEMAILFORCOUNCIL);

    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final CouncilMeeting councilMeeting) {
        StringBuilder emailSubject = new StringBuilder();

        emailSubject.append(COUNCIL_MEETING_SCHEDULED_ON).append(councilMeeting.getMeetingDate()).append(AT)
                .append(councilMeeting.getMeetingTime()).append(AT).append(councilMeeting.getMeetingLocation());
        return emailSubject.toString();
    }

    public void sendSMSOnSewerageForMeeting(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnSewerageForMeeting(final String email, final String emailBody, final String emailSubject) {
        messagingService.sendEmail(email, emailSubject, emailBody);
    }

}