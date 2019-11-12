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
package org.egov.council.service;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.council.utils.constants.CouncilConstants.DESIGNATIONSTOSENDEMAILFORCOUNCILMOM;
import static org.egov.council.utils.constants.CouncilConstants.MODULE_FULLNAME;
import static org.egov.council.utils.constants.CouncilConstants.MOM_FINALISED;
import static org.egov.council.utils.constants.CouncilConstants.ROLE_COUNCIL_CLERK;
import static org.egov.council.utils.constants.CouncilConstants.SENDEMAILFORCOUNCIL;
import static org.egov.council.utils.constants.CouncilConstants.SENDSMSFORCOUNCIL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.council.entity.CommitteeMembers;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.CouncilSmsDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CouncilSmsAndEmailService {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource councilMessageSource;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private CouncilCommitteeMemberService committeeMemberService;

    @Autowired
    private CouncilMeetingService councilMeetingService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private DesignationService designationService;
    
    @Autowired
    private UserService userService;

    /**
     * @return this method will send SMS and Email is isSmsEnabled is true
     * @param CouncilMeeting
     * @param workFlowAction
     */
    public void sendSms(CouncilMeeting councilMeeting, String customMessage) {
        String mobileNo;
        Boolean smsEnabled = isSmsEmailEnabled(SENDSMSFORCOUNCIL);

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

    public void sendEmail(CouncilMeeting councilMeeting, String customMessage, final byte[] attachment, boolean isForMOM) {
        String emailId;
        Boolean emailEnabled = isSmsEmailEnabled(SENDEMAILFORCOUNCIL);
        if (emailEnabled) {
            for (CommitteeMembers committeeMembers : committeeMemberService
                    .findAllByCommitteTypeMemberIsActive(councilMeeting.getCommitteeType())) {
                emailId = committeeMembers.getCouncilMember().getEmailId();
                if (StringUtils.isNotBlank(emailId))
                    buildEmailForMeetingOrMOM(emailId, councilMeeting.getCommitteeType().getName().concat(" Members"),
                            councilMeeting, customMessage,
                            attachment);
            }
            if (isForMOM)
                sendEmailToDesignatedUsers(councilMeeting, customMessage, attachment);
            else {
                List<User> listOfUsers = councilMeetingService.getUserListForMeeting(councilMeeting);
                for (User user : listOfUsers) {
                    if (StringUtils.isNotBlank(user.getEmailId()))
                        buildEmailForMeetingOrMOM(user.getEmailId(), user.getUsername(), councilMeeting, customMessage,
                                attachment);
                }
            }
        }
    }

    private void sendEmailToDesignatedUsers(CouncilMeeting councilMeeting, String customMessage, final byte[] attachment) {
        Long desigId;
        List<Assignment> assignments;
        List<AppConfigValues> appConfigValues = getAppConfigValueByPassingModuleAndType(MODULE_FULLNAME,
                DESIGNATIONSTOSENDEMAILFORCOUNCILMOM);
        if (appConfigValues != null && !appConfigValues.isEmpty()) {
            for (AppConfigValues value : appConfigValues) {
                desigId = designationService.getDesignationByName(value.getValue()).getId();
                assignments = assignmentService.getAllPositionsByDepartmentAndDesignationForGivenRange(null, desigId, new Date());
                if (assignments != null && !assignments.isEmpty()) {
                    for (Assignment assignment : assignments) {
                        if (StringUtils.isNotBlank(assignment.getEmployee().getEmailId()))
                            buildEmailForMeetingOrMOM(assignment.getEmployee().getEmailId(), EMPTY, councilMeeting, customMessage,
                                    attachment);
                    }
                }
            }
        }
        Set<User> councilClerks = userService.getUsersByRoleName(ROLE_COUNCIL_CLERK);
        if (councilClerks != null && !councilClerks.isEmpty()) {
            for (User user : councilClerks)
                if (user.isActive() && StringUtils.isNotBlank(user.getEmailId()))
                    buildEmailForMeetingOrMOM(user.getEmailId(), EMPTY, councilMeeting, customMessage,
                            attachment);
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
            sendSMSForMeeting(mobileNumber, smsMsg);
    }

    public void buildEmailForMeetingOrMOM(final String email, final String name, final CouncilMeeting councilMeeting,
            final String customMessage, final byte[] attachment) {
        String body;
        String subject;
        String fileName;
        if (MOM_FINALISED.equals(councilMeeting.getStatus().getCode())) {
            body = emailBodyByCodeAndArgsWithType("email.resolution.body", EMPTY, councilMeeting, customMessage);
            subject = emailSubjectforEmailByCodeAndArgs("email.resolution.subject", EMPTY, councilMeeting);
            fileName = "MeetingResolution.pdf";
        } else {
            body = emailBodyByCodeAndArgsWithType("email.meeting.body", name, councilMeeting, customMessage);
            subject = emailSubjectforEmailByCodeAndArgs("email.meeting.subject", name, councilMeeting);
            fileName = "AgendaDetails.pdf";
        }
        if (email != null && body != null)
            sendEmailForMeetingWithAttachment(email, body, subject, attachment, fileName);
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
            sendSMSForMeeting(mobileNumber, smsMsg);
    }

    /**
     * @param code
     * @param CouncilMeeting
     * @param applicantName
     * @param type
     * @return EmailBody 
     */
    public String emailBodyByCodeAndArgsWithType(final String code, final String name, final CouncilMeeting councilMeeting,
            final String customMessage) {
        final SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String emailBody;
        if (StringUtils.isBlank(name))
            emailBody = councilMessageSource.getMessage(code,
                    new String[] {
                            councilMeeting.getMeetingType().getName(),
                            sf.format(councilMeeting.getMeetingDate()) },
                    LocaleContextHolder.getLocale());
        else
            emailBody = councilMessageSource.getMessage(code,
                    new String[] { name,
                            sf.format(councilMeeting.getMeetingDate()),
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
        final SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        return councilMessageSource.getMessage(code,
                new String[] { name,
                        sf.format(councilMeeting.getMeetingDate()),
                        String.valueOf(councilMeeting.getMeetingTime()),
                        String.valueOf(councilMeeting.getMeetingLocation()), customMessage != null ? customMessage : " " },
                LocaleContextHolder.getLocale());
    }

    public Boolean isSmsEmailEnabled(String keyname) {
        List<AppConfigValues> appConfigValue = getAppConfigValueByPassingModuleAndType(MODULE_FULLNAME, keyname);
        return "YES".equalsIgnoreCase(
                appConfigValue != null && !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : "NO");
    }

    private List<AppConfigValues> getAppConfigValueByPassingModuleAndType(String moduleName, String keyname) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(moduleName,
                keyname);
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String name, final CouncilMeeting councilMeeting) {
        final SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String emailSubject;
        if (StringUtils.isBlank(name))
            emailSubject = councilMessageSource.getMessage(code,
                    new String[] {
                            councilMeeting.getMeetingType().getName(),
                            sf.format(councilMeeting.getMeetingDate()) },
                    LocaleContextHolder.getLocale());
        else
            emailSubject = councilMessageSource.getMessage(code,
                    new String[] { name,
                            councilMeeting.getMeetingType().getName(),
                            sf.format(councilMeeting.getMeetingDate()),
                            String.valueOf(councilMeeting.getMeetingTime()),
                            String.valueOf(councilMeeting.getMeetingLocation()) },
                    LocaleContextHolder.getLocale());
        return emailSubject;
    }

    public void sendSMSForMeeting(final String mobileNumber, final String smsBody) {
        notificationService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailForMeetingWithAttachment(final String email, final String emailBody,
            final String emailSubject, final byte[] attachment, String fileName) {
        notificationService.sendEmailWithAttachment(email, emailSubject, emailBody, "application/pdf", fileName,
                attachment);
    }

}