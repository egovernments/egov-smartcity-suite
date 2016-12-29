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

import static org.egov.council.utils.constants.CouncilConstants.MODULE_FULLNAME;
import static org.egov.council.utils.constants.CouncilConstants.MOM_FINALISED;
import static org.egov.council.utils.constants.CouncilConstants.SENDEMAILFORCOUNCIL;
import static org.egov.council.utils.constants.CouncilConstants.SENDSMSFORCOUNCIL;

import java.util.Date;
import java.util.List;

import org.egov.council.entity.CommitteeMembers;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.CouncilSmsDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.messaging.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CouncilSmsAndEmailService {

    private static final String AGENDAATTACHFILENAME = "agendadetails";

    @Autowired
    private MessagingService messagingService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource councilMessageSource;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private CouncilCommitteeMemberService committeeMemberService;

    @Autowired
    private CouncilMeetingService councilMeetingService;

    /**
     * @return this method will send SMS and Email is isSmsEnabled is true
     * @param CouncilMeeting
     * @param workFlowAction
     */
    public void sendSms(CouncilMeeting councilMeeting, String customMessage) {
        String mobileNo;
        Boolean smsEnabled = isSmsEnabled();

        if (smsEnabled) {
            for (CommitteeMembers committeeMembers : committeeMemberService
                    .findAllByCommitteTypeMemberIsActive(councilMeeting.getCommitteeType())) {
                mobileNo = committeeMembers.getCouncilMember().getMobileNumber();
                if (mobileNo != null) {
                    buildSmsForMeeting(mobileNo, councilMeeting.getCommitteeType().getName(), councilMeeting, customMessage);
                }
            }
            List<User> listOfUsers = councilMeetingService.getUserListForMeeting(councilMeeting);
            for (User user : listOfUsers) {
                if (user.getMobileNumber() != null) {
                    buildSmsForMeetingCouncilRoles(user.getUsername(), user.getMobileNumber(), councilMeeting, customMessage);
                }
            }
            buildCouncilSmsDetails(customMessage, councilMeeting);
        }
    }

    public void sendEmail(CouncilMeeting councilMeeting, String customMessage, final byte[] attachment) {
        String emailId;
        Boolean emailEnabled = isEmailEnabled();
        if (emailEnabled) {
            for (CommitteeMembers committeeMembers : committeeMemberService
                    .findAllByCommitteTypeMemberIsActive(councilMeeting.getCommitteeType())) {
                emailId = committeeMembers.getCouncilMember().getEmailId();
                if (emailId != null) {
                    buildEmailForMeeting(emailId, councilMeeting.getCommitteeType().getName(), councilMeeting, customMessage,
                            attachment);
                }
            }
            List<User> listOfUsers = councilMeetingService.getUserListForMeeting(councilMeeting);
            for (User user : listOfUsers) {
                if (user.getMobileNumber() != null) {
                    buildEmailForMeetingForCouncilRoles(user.getUsername(), user.getEmailId(), councilMeeting, customMessage,
                            attachment);
                }
            }
        }
    }

    private CouncilSmsDetails buildCouncilSmsDetails(String message,
            CouncilMeeting councilMeeting) {
        CouncilSmsDetails councilSmsDetails = new CouncilSmsDetails();
        councilSmsDetails.setSmsSentDate(new Date());
        councilSmsDetails.setSmsContent(message);
        councilSmsDetails.setMeeting(councilMeeting);
        councilMeeting.addSmsDetails(councilSmsDetails);
        return councilSmsDetails;
    }

    /**
     * @return SMS AND EMAIL body and subject For Committee Members
     * @param CouncilMeeting
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */

    public void buildSmsForMeeting(final String mobileNumber, final String name, final CouncilMeeting councilMeeting,
            final String customMessage) {
        String smsMsg;
        if (MOM_FINALISED.equals(councilMeeting.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.resolution.sms", name, councilMeeting, customMessage);
        } else {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.meeting.sms", name, councilMeeting, customMessage);
        }
        if (mobileNumber != null && smsMsg != null)
            sendSMSOnSewerageForMeeting(mobileNumber, smsMsg);
    }

    public void buildEmailForMeeting(final String email, final String name, final CouncilMeeting councilMeeting,
            final String customMessage, final byte[] attachment) {
        String body;
        String subject;
        if (MOM_FINALISED.equals(councilMeeting.getStatus().getCode())) {
            body = emailBodyByCodeAndArgsWithType("email.resolution.body", name, councilMeeting, customMessage);
            subject = emailSubjectforEmailByCodeAndArgs("email.resolution.subject", name, councilMeeting);
        } else {
            body = emailBodyByCodeAndArgsWithType("email.meeting.body", name, councilMeeting, customMessage);
            subject = emailSubjectforEmailByCodeAndArgs("email.meeting.subject", name, councilMeeting);
        }
        if (email != null && body != null)
            sendEmailOnSewerageForMeetingWithAttachment(email, body, subject, attachment);
    }

    /**
     * @return SMS AND EMAIL body and subject For Council Roles
     * @param CouncilMeeting
     * @param email
     * @param mobileNumber
     * @param smsMsg
     * @param body
     * @param subject
     */

    public void buildSmsForMeetingCouncilRoles(final String userName, final String mobileNumber,
            final CouncilMeeting councilMeeting,
            final String customMessage) {
        String smsMsg;
        if (MOM_FINALISED.equals(councilMeeting.getStatus().getCode())) {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.council.roles.resolution.sms", userName, councilMeeting,
                    customMessage);
        } else {
            smsMsg = smsBodyByCodeAndArgsWithType("msg.council.roles.meeting.sms", userName, councilMeeting,
                    customMessage);
        }
        if (mobileNumber != null && smsMsg != null)
            sendSMSOnSewerageForMeeting(mobileNumber, smsMsg);
    }

    public void buildEmailForMeetingForCouncilRoles(final String userName, final String email,
            final CouncilMeeting councilMeeting,
            final String customMessage, final byte[] attachment) {
        String body;
        String subject;
        if (MOM_FINALISED.equals(councilMeeting.getStatus().getCode())) {
            subject = emailSubjectforEmailByCodeAndArgs("email.council.roles.resolution.subject", userName, councilMeeting);
            body = emailBodyByCodeAndArgsWithType("email.council.roles.resolution.body", userName, councilMeeting,
                    customMessage);
        } else {
            subject = emailSubjectforEmailByCodeAndArgs("email.council.roles.meeting.subject", userName, councilMeeting);
            body = emailBodyByCodeAndArgsWithType("email.council.roles.meeting.body", userName, councilMeeting,
                    customMessage);
        }
        if (email != null && body != null)
            sendEmailOnSewerageForMeetingWithAttachment(email, body, subject, attachment);
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
    public String emailBodyByCodeAndArgsWithType(final String code, final String name, final CouncilMeeting councilMeeting,
            final String customMessage) {
        String emailBody;

        emailBody = councilMessageSource.getMessage(code,
                new String[] { name,
                        String.valueOf(councilMeeting.getMeetingDate()),
                        String.valueOf(councilMeeting.getMeetingTime()),
                        String.valueOf(councilMeeting.getMeetingLocation()), customMessage != null ? customMessage : " " },
                LocaleContextHolder.getLocale());

        return emailBody;
    }

    /**
     * @param code
     * @param CouncilMeeting
     * @param applicantName
     * @param type
     */
    public String smsBodyByCodeAndArgsWithType(final String code, final String name, final CouncilMeeting councilMeeting,
            final String customMessage) {
        String smsMsg;
        smsMsg = councilMessageSource.getMessage(code,
                new String[] { name,
                        String.valueOf(councilMeeting.getMeetingDate()),
                        String.valueOf(councilMeeting.getMeetingTime()),
                        String.valueOf(councilMeeting.getMeetingLocation()), customMessage != null ? customMessage : " " },
                LocaleContextHolder.getLocale());

        return smsMsg;
    }

    public Boolean isSmsEnabled() {

        return getAppConfigValueByPassingModuleAndType(MODULE_FULLNAME, SENDSMSFORCOUNCIL);
    }

    private Boolean getAppConfigValueByPassingModuleAndType(String moduleName, String sendsmsoremail) {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName,
                sendsmsoremail);

        return "YES".equalsIgnoreCase(
                appConfigValue != null && !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : "NO");
    }

    public Boolean isEmailEnabled() {

        return getAppConfigValueByPassingModuleAndType(MODULE_FULLNAME, SENDEMAILFORCOUNCIL);

    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String name, final CouncilMeeting councilMeeting) {
        return councilMessageSource.getMessage(code,
                new String[] { name,
                        String.valueOf(councilMeeting.getMeetingDate()),
                        String.valueOf(councilMeeting.getMeetingTime()),
                        String.valueOf(councilMeeting.getMeetingLocation()) },
                LocaleContextHolder.getLocale());
    }

    public void sendSMSOnSewerageForMeeting(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnSewerageForMeetingWithAttachment(final String email, final String emailBody,
            final String emailSubject, final byte[] attachment) {
        messagingService.sendEmailWithAttachment(email, emailSubject, emailBody, "application/pdf", AGENDAATTACHFILENAME,
                attachment);
    }

}