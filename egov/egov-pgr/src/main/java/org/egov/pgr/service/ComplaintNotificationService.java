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

package org.egov.pgr.service;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.notification.service.NotificationService;
import org.egov.pgr.entity.Complaint;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;
import static org.egov.infra.utils.DateUtils.toDefaultDateTimeFormat;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_COMPLETED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REGISTERED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REJECTED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REOPENED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_WITHDRAWN;

@Service
public class ComplaintNotificationService {

    private static final String COMPLAINT_REGISTERED_SMS_MSG_KEY = "msg.complaint.registered.sms";
    private static final String COMPLAINT_REGISTERED_EMAIL_BODY_MSG_KEY = "msg.complaint.registered.email.body";
    private static final String COMPLAINT_REGISTERED_EMAIL_SUBJECT_MEG_KEY = "msg.complaint.registered.email.subject";
    private static final String COMPLAINT_OFFICIAL_REGISTERED_SMS_MSG_KEY = "msg.complaint.official.registered.sms";
    private static final String COMPLAINT_COMPLETED_SMS_MSG_KEY = "msg.complaint.completed.sms";
    private static final String COMPLAINT_COMPLETED_EMAIL_BODY_MSG_KEY = "msg.complaint.completed.email.body";
    private static final String COMPLAINT_COMPLETED_EMAIL_SUBJECT_MSG_KEY = "msg.complaint.completed.email.subject";
    private static final String COMPLAINT_REJECTED_SMS_MSG_KEY = "msg.complaint.rejected.sms";
    private static final String COMPLAINT_REJECTED_EMAIL_BODY_MSG_KEY = "msg.complaint.rejected.email.body";
    private static final String COMPLAINT_REJECTED_EMAIL_SUBJECT_MSG_KEY = "msg.complaint.rejected.email.subject";
    private static final String COMPLAINT_REOPENED_SMS_MSG_KEY = "msg.complaint.reopened.sms";
    private static final String COMPLAINT_REOPENED_EMAIL_BODY_MSG_KEY = "msg.complaint.reopened.email.body";
    private static final String COMPLAINT_REOPENED_EMAIL_SUBJECT_MSG_KEY = "msg.complaint.reopened.email.subject";
    private static final String COMPLAINT_WITHDRAWN_SMS_MSG_KEY = "msg.complaint.withdrawn.sms";
    private static final String COMPLAINT_WITHDRAWN_EMAIL_BODY_MSG_KEY = "msg.complaint.withdrawn.email.body";
    private static final String COMPLAINT_WITHDRAWN_EMAIL_SUBJECT_MSG_KEY = "msg.complaint.withdrawn.email.subject";
    private static final String COMPLAINT_ESCALATION_EMAIL_SUBJECT_MSG_KEY = "msg.complaint.escalation.email.subject";
    private static final String COMPLAINT_ESCALATION_SMS_NEXTOWNER_MSG_KEY = "msg.complaint.escalation.sms.nextowner";
    private static final String COMPLAINT_ESCALATION_SMS_PREVIOUSOWNER_MSG_KEY = "msg.complaint.escalation.sms.previousowner";

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource complaintMessageSource;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AssignmentService assignmentService;

    public void sendRegistrationMessage(Complaint complaint) {
        if (complaint.isNotifyComplainant() && COMPLAINT_REGISTERED.equals(complaint.getStatus().getName())) {
            notificationService.sendEmail(complaint.getComplainant().getEmail(),
                    getMessage(COMPLAINT_REGISTERED_EMAIL_SUBJECT_MEG_KEY),
                    getMessageForRegistration(COMPLAINT_REGISTERED_EMAIL_BODY_MSG_KEY, complaint));
            notificationService.sendSMS(complaint.getComplainant().getMobile(),
                    getMessageForRegistration(COMPLAINT_REGISTERED_SMS_MSG_KEY, complaint));
        }
        officialSmsOnRegistration(complaint);
    }

    public void officialSmsOnRegistration(Complaint complaint) {
        Position owner = complaint.getState().getOwnerPosition();
        if (owner != null) {
            List<Assignment> assignments = assignmentService.getAssignmentsForPosition(owner.getId(), new Date());
            if (!assignments.isEmpty()) {
                User user = assignments.get(0).getEmployee();
                if (user != null) {
                    String smsMsg = getMessage(
                            COMPLAINT_OFFICIAL_REGISTERED_SMS_MSG_KEY,
                            complaint.getComplaintType().getName(), complaint.getLocation().getName(),
                            complaint.getComplainant().getName(),
                            complaint.getComplainant().getMobile()
                    );
                    notificationService.sendSMS(user.getMobileNumber(), smsMsg);
                }
            }
        }
    }

    public void sendUpdateMessage(Complaint complaint) {
        if (complaint.isNotifyComplainant()) {
            switch (complaint.getStatus().getName()) {
                case COMPLAINT_COMPLETED:
                    notificationService.sendEmail(complaint.getComplainant().getEmail(),
                            getMessage(COMPLAINT_COMPLETED_EMAIL_SUBJECT_MSG_KEY),
                            getMessageForProcessing(COMPLAINT_COMPLETED_EMAIL_BODY_MSG_KEY, complaint));
                    notificationService.sendSMS(complaint.getComplainant().getMobile(),
                            getMessageForProcessing(COMPLAINT_COMPLETED_SMS_MSG_KEY, complaint));
                    break;
                case COMPLAINT_REJECTED:
                    notificationService.sendEmail(complaint.getComplainant().getEmail(),
                            getMessage(COMPLAINT_REJECTED_EMAIL_SUBJECT_MSG_KEY),
                            getMessageForProcessing(COMPLAINT_REJECTED_EMAIL_BODY_MSG_KEY, complaint));
                    notificationService.sendSMS(complaint.getComplainant().getMobile(),
                            getMessageForProcessing(COMPLAINT_REJECTED_SMS_MSG_KEY, complaint));
                    break;
                case COMPLAINT_REOPENED:
                    notificationService.sendEmail(complaint.getComplainant().getEmail(),
                            getMessage(COMPLAINT_REOPENED_EMAIL_SUBJECT_MSG_KEY),
                            getMessageForReopening(COMPLAINT_REOPENED_EMAIL_BODY_MSG_KEY, complaint));
                    notificationService.sendSMS(complaint.getComplainant().getMobile(),
                            getMessageForReopening(COMPLAINT_REOPENED_SMS_MSG_KEY, complaint));
                    break;
                case COMPLAINT_WITHDRAWN:
                    notificationService.sendEmail(complaint.getComplainant().getEmail(),
                            getMessage(COMPLAINT_WITHDRAWN_EMAIL_SUBJECT_MSG_KEY),
                            getMessageForWithdrawn(COMPLAINT_WITHDRAWN_EMAIL_BODY_MSG_KEY, complaint));
                    notificationService.sendSMS(complaint.getComplainant().getMobile(),
                            getMessageForWithdrawn(COMPLAINT_WITHDRAWN_SMS_MSG_KEY, complaint));
                    break;
                default:
                    break;
            }
        }
    }

    public void sendEscalationMessage(Complaint complaint, User nextOwner, Position previousAssignee) {
        List<Assignment> prevUserAssignments = assignmentService.getAssignmentsForPosition(previousAssignee.getId(), new Date());
        User previousOwner = prevUserAssignments.isEmpty() ? null : prevUserAssignments.get(0).getEmployee();
        String previousOwnerName = previousOwner != null ? previousOwner.getName() : previousAssignee.getName();

        String emailSubject = getMessage(COMPLAINT_ESCALATION_EMAIL_SUBJECT_MSG_KEY,
                complaint.getComplaintType().getSlaHours().toString());

        String smsMsgnextOwner = getMessage(COMPLAINT_ESCALATION_SMS_NEXTOWNER_MSG_KEY,
                complaint.getCrn(), complaint.getComplaintType().getName(),
                nextOwner.getName(), complaint.getAssignee().getName());

        String smsMsgPreviousOwner = getMessage(COMPLAINT_ESCALATION_SMS_PREVIOUSOWNER_MSG_KEY,
                complaint.getCrn(), previousOwnerName, previousAssignee.getDeptDesig().getDesignation().getName(),
                complaint.getComplaintType().getName());
        if (previousOwner != null) {
            notificationService.sendEmail(previousOwner.getEmailId(), emailSubject, smsMsgPreviousOwner);
            notificationService.sendSMS(previousOwner.getMobileNumber(), smsMsgPreviousOwner);
        }
        notificationService.sendEmail(nextOwner.getEmailId(), emailSubject, smsMsgnextOwner);
        notificationService.sendSMS(nextOwner.getMobileNumber(), smsMsgnextOwner);
    }

    @Transactional(readOnly = true)
    public String getEmailBody(Complaint complaint) {
        StringBuilder emailBody = new StringBuilder()
                .append(" %0D%0A Grievance Details -  %0D%0A %0D%0A CRN - ").append(complaint.getCrn())
                .append(" %0D%0A Grievance Type -").append(complaint.getComplaintType().getName())
                .append("  %0D%0A Grievance department  - ").append(complaint.getDepartment().getName())
                .append("  %0D%0A Complainant name - ").append(complaint.getComplainant().getName())
                .append("  %0D%0A Complainant mobile number - ").append(complaint.getComplainant().getMobile())
                .append("  %0D%0A Location details - ").append(complaint.getLocation().getName())
                .append(" %0D%0A Grievance details - ").append(complaint.getDetails())
                .append(" %0D%0A Grievance status -").append(complaint.getStatus().getName())
                .append(" %0D%0A Grievance Registration Date - ").append(toDefaultDateTimeFormat(complaint.getCreatedDate()));
        return emailBody.toString();
    }

    private String getMessageForRegistration(String msgKey, Complaint complaint) {
        return getMessage(msgKey,
                complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                complaint.getComplaintType().getSlaHours().toString(), getDomainURL(),
                complaint.getCrn(), getMunicipalityName());
    }

    private String getMessageForProcessing(String msgKey, Complaint complaint) {
        return getMessage(msgKey,
                complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                getDomainURL(), complaint.getCrn(), getMunicipalityName());
    }

    private String getMessageForReopening(String msgKey, Complaint complaint) {
        return getMessage(msgKey,
                complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                complaint.getComplaintType().getSlaHours().toString(), getDomainURL(),
                complaint.getCrn(), getMunicipalityName());
    }

    private String getMessageForWithdrawn(String msgKey, Complaint complaint) {
        return getMessage(msgKey,
                complaint.getComplainant().getName(), complaint.getComplaintType().getName(), getMunicipalityName());
    }

    private String getMessage(String key, String... args) {
        return complaintMessageSource.getMessage(key, args, Locale.getDefault());
    }
}
