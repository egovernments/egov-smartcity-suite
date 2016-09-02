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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CouncilSmsAndEmailService {
	
	private static String AGENDAATTACHFILENAME = "agendadetails";

	@Autowired
	private MessagingService messagingService;

	@Autowired
	@Qualifier("parentMessageSource")
	private MessageSource councilMessageSource;

	@Autowired
	private AppConfigValueService appConfigValuesService;

	@Autowired
	private CouncilCommitteeMemberService committeeMemberService;

	/**
	 * @return this method will send SMS and Email is isSmsEnabled is true
	 * @param CouncilMeeting
	 * @param workFlowAction
	 */
	public void sendSms(CouncilMeeting councilMeeting, String customMessage) {
		String mobileno = StringUtils.EMPTY;
		Boolean smsEnabled = isSmsEnabled();
		if (smsEnabled) {
			for (CommitteeMembers committeeMembers : committeeMemberService
					.findAllByCommitteType(councilMeeting.getCommitteeType())) {
				mobileno = committeeMembers.getCouncilMember().getMobileNumber();
				if (mobileno != null) {
					buildSmsForMeeting(mobileno, councilMeeting, customMessage);
				}
			}
		}
	}

	public void sendEmail(CouncilMeeting councilMeeting, String customMessage, final byte[] attachment) {
		String email_id = StringUtils.EMPTY;
		Boolean emailEnabled = isEmailEnabled();
		if (emailEnabled) {
			for (CommitteeMembers committeeMembers : committeeMemberService
					.findAllByCommitteType(councilMeeting.getCommitteeType())) {
				email_id = committeeMembers.getCouncilMember().getEmailId();
				if (email_id != null) {
					buildEmailForMeeting(email_id, councilMeeting, customMessage, attachment);
				}
			}
		}
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

	public void buildSmsForMeeting(final String mobileNumber, final CouncilMeeting councilMeeting,
			final String customMessage) {
		String smsMsg = StringUtils.EMPTY;
		smsMsg = SmsBodyByCodeAndArgsWithType("msg.meeting.sms", councilMeeting, SMSEMAILTYPEFORCOUNCILMEETING,
				customMessage);
		if (mobileNumber != null && smsMsg != null)
			sendSMSOnSewerageForMeeting(mobileNumber, smsMsg);
	}

	public void buildEmailForMeeting(final String email, final CouncilMeeting councilMeeting,
			final String customMessage, final byte[] attachment) {
		String body = StringUtils.EMPTY;
		String subject = StringUtils.EMPTY;
		body = EmailBodyByCodeAndArgsWithType("email.meeting.body", councilMeeting, SMSEMAILTYPEFORCOUNCILMEETING,
				customMessage);
		subject = emailSubjectforEmailByCodeAndArgs("email.meeting.subject", councilMeeting);
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
	public String EmailBodyByCodeAndArgsWithType(final String code, final CouncilMeeting councilMeeting,
			final String type, final String customMessage) {
		String emailBody = StringUtils.EMPTY;

		if (SMSEMAILTYPEFORCOUNCILMEETING.equalsIgnoreCase(type)) {
			emailBody = councilMessageSource.getMessage(code,
					new String[] { String.valueOf(councilMeeting.getCommitteeType().getName()),
							String.valueOf(councilMeeting.getMeetingDate()),
							String.valueOf(councilMeeting.getMeetingTime()),
							String.valueOf(councilMeeting.getMeetingLocation()), customMessage },
					LocaleContextHolder.getLocale());
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
			final String type, final String customMessage) {
		String smsMsg = StringUtils.EMPTY;
		if (SMSEMAILTYPEFORCOUNCILMEETING.equalsIgnoreCase(type)) {
			smsMsg = councilMessageSource.getMessage(code,
					new String[] { String.valueOf(councilMeeting.getCommitteeType().getName()),
							String.valueOf(councilMeeting.getMeetingDate()),
							String.valueOf(councilMeeting.getMeetingTime()),
							String.valueOf(councilMeeting.getMeetingLocation()), customMessage },
					LocaleContextHolder.getLocale());
		}
		return smsMsg;
	}

	public Boolean isSmsEnabled() {

		return getAppConfigValueByPassingModuleAndType(CouncilConstants.MODULE_FULLNAME, SENDSMSFORCOUNCIL);
	}

	private Boolean getAppConfigValueByPassingModuleAndType(String moduleName, String sendsmsoremail) {
		final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName,
				sendsmsoremail);

		return "YES".equalsIgnoreCase(
				appConfigValue != null && appConfigValue.size() > 0 ? appConfigValue.get(0).getValue() : "NO");
	}

	public Boolean isEmailEnabled() {

		return getAppConfigValueByPassingModuleAndType(MODULE_FULLNAME, SENDEMAILFORCOUNCIL);

	}

	public String emailSubjectforEmailByCodeAndArgs(final String code, final CouncilMeeting councilMeeting) {
		final String emailSubject = councilMessageSource.getMessage(code,
				new String[] { String.valueOf(councilMeeting.getCommitteeType().getName()),
						String.valueOf(councilMeeting.getMeetingDate()),
						String.valueOf(councilMeeting.getMeetingTime()),
						String.valueOf(councilMeeting.getMeetingLocation()) },
				LocaleContextHolder.getLocale());
		return emailSubject;
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