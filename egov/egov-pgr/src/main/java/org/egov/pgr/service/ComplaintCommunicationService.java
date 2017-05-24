/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.service;

import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.messaging.MessagingService;
import org.egov.pgr.entity.Complaint;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class ComplaintCommunicationService {

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource complaintMessageSource;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private AssignmentService assignmentService;

    public void sendRegistrationMessage(final Complaint complaint) {

        final Locale locale = Locale.getDefault();
        if ("REGISTERED".equals(complaint.getStatus().getName())) {
            final String smsMsg = complaintMessageSource.getMessage(
                    "msg.complaint.registered.sms",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                            complaint.getComplaintType().getSlaHours().toString(), getDomainURL(), complaint.getCrn(),
                            getMunicipalityName() },
                    locale);
            final String emailBody = complaintMessageSource.getMessage(
                    "msg.complaint.registered.email.body",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                            complaint.getComplaintType().getSlaHours().toString(), getDomainURL(), complaint.getCrn(),
                            getMunicipalityName() },
                    locale);
            final String emailSubject = complaintMessageSource.getMessage("msg.complaint.registered.email.subject",
                    new String[] {}, locale);
            messagingService.sendEmail(complaint.getComplainant().getEmail(), emailSubject, emailBody);
            messagingService.sendSMS(complaint.getComplainant().getMobile(), smsMsg);
        }
        officialSmsOnRegistration(complaint);
    }

    public void officialSmsOnRegistration(final Complaint complaint) {
        final Locale locale = Locale.getDefault();
        final Position owner = complaint.getState().getOwnerPosition();
        if (owner != null && owner.getDeptDesig() != null) {
            final List<Assignment> assignments = assignmentService.getAssignmentsForPosition(owner.getId(), new Date());
            if (!assignments.isEmpty()) {
                final User user = assignments.get(0).getEmployee();
                if (user != null) {
                    final String smsMsg = complaintMessageSource.getMessage(
                            "msg.complaint.official.registered.sms",
                            new String[] { complaint.getComplaintType().getName(), complaint.getLocation().getName(),
                                    complaint.getComplainant().getName(),
                                    complaint.getComplainant().getMobile(),
                            },
                            locale);
                    messagingService.sendSMS(user.getMobileNumber(), smsMsg);
                }
            }
        }
    }

    public void sendUpdateMessage(final Complaint complaint) {
        final Locale locale = Locale.getDefault();
        if ("COMPLETED".equals(complaint.getStatus().getName())) {
            final String smsMsg = complaintMessageSource.getMessage(
                    "msg.complaint.completed.sms",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(), getDomainURL(),
                            complaint.getCrn(),
                            getMunicipalityName() },
                    locale);
            final String emailBody = complaintMessageSource.getMessage(
                    "msg.complaint.completed.email.body",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(), getDomainURL(),
                            complaint.getCrn(),
                            getMunicipalityName() },
                    locale);
            final String emailSubject = complaintMessageSource.getMessage("msg.complaint.completed.email.subject",
                    new String[] {}, locale);
            messagingService.sendEmail(complaint.getComplainant().getEmail(), emailSubject, emailBody);
            messagingService.sendSMS(complaint.getComplainant().getMobile(), smsMsg);
        }

        else if ("REJECTED".equals(complaint.getStatus().getName())) {
            final String smsMsg = complaintMessageSource.getMessage(
                    "msg.complaint.rejected.sms",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(), getDomainURL(),
                            complaint.getCrn(),
                            getMunicipalityName() },
                    locale);
            final String emailBody = complaintMessageSource.getMessage(
                    "msg.complaint.rejected.email.body",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(), getDomainURL(),
                            complaint.getCrn(),
                            getMunicipalityName() },
                    locale);
            final String emailSubject = complaintMessageSource.getMessage("msg.complaint.rejected.email.subject",
                    new String[] {}, locale);
            messagingService.sendEmail(complaint.getComplainant().getEmail(), emailSubject, emailBody);
            messagingService.sendSMS(complaint.getComplainant().getMobile(), smsMsg);
        }

        else if ("REOPENED".equals(complaint.getStatus().getName())) {
            final String smsMsg = complaintMessageSource.getMessage(
                    "msg.complaint.reopened.sms",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                            complaint.getComplaintType().getSlaHours().toString(), getDomainURL(), complaint.getCrn(),
                            getMunicipalityName() },
                    locale);
            final String emailBody = complaintMessageSource.getMessage(
                    "msg.complaint.reopened.email.body",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                            complaint.getComplaintType().getSlaHours().toString(), getDomainURL(), complaint.getCrn(),
                            getMunicipalityName() },
                    locale);
            final String emailSubject = complaintMessageSource.getMessage("msg.complaint.reopened.email.subject",
                    new String[] {}, locale);
            messagingService.sendEmail(complaint.getComplainant().getEmail(), emailSubject, emailBody);
            messagingService.sendSMS(complaint.getComplainant().getMobile(), smsMsg);
        }

        else if ("WITHDRAWN".equals(complaint.getStatus().getName())) {
            final String smsMsg = complaintMessageSource.getMessage(
                    "msg.complaint.withdrawn.sms",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                            getMunicipalityName() },
                    locale);
            final String emailBody = complaintMessageSource.getMessage(
                    "msg.complaint.withdrawn.email.body",
                    new String[] { complaint.getComplainant().getName(), complaint.getComplaintType().getName(),
                            getMunicipalityName() },
                    locale);
            final String emailSubject = complaintMessageSource.getMessage("msg.complaint.withdrawn.email.subject",
                    new String[] {}, locale);
            messagingService.sendEmail(complaint.getComplainant().getEmail(), emailSubject, emailBody);
            messagingService.sendSMS(complaint.getComplainant().getMobile(), smsMsg);
        }

    }

    public void sendEscalationMessage(final Complaint complaint, final User nextOwner, final Position previousAssignee) {
        final Locale locale = Locale.getDefault();
        final List<Assignment> prevUserAssignments = assignmentService
                .getAssignmentsForPosition(previousAssignee.getId(), new Date());
        final User previousOwner = !prevUserAssignments.isEmpty() ? prevUserAssignments.get(0).getEmployee() : null;

        final String emailSubject = complaintMessageSource.getMessage("msg.complaint.escalation.email.subject",
                new String[] { complaint.getComplaintType().getSlaHours().toString() }, locale);

        final String previousowner = previousOwner != null ? previousOwner.getName()
                : previousAssignee.getName();

        final String smsMsgnextOwner = complaintMessageSource.getMessage(
                "msg.complaint.escalation.sms.nextowner",
                new String[] { complaint.getCrn(), complaint.getComplaintType().getName(),
                        nextOwner.getName(), complaint.getAssignee().getName()
                },
                locale);

        final String smsMsgPreviousOwner = complaintMessageSource.getMessage(
                "msg.complaint.escalation.sms.previousowner",
                new String[] { complaint.getCrn(),
                        previousowner, previousAssignee.getDeptDesig().getDesignation().getName(),
                        complaint.getComplaintType().getName()
                },
                locale);
        if (previousOwner != null) {
            messagingService.sendEmail(previousOwner.getEmailId(), emailSubject, smsMsgPreviousOwner);
            messagingService.sendSMS(previousOwner.getMobileNumber(), smsMsgPreviousOwner);
        }
        messagingService.sendEmail(nextOwner.getEmailId(), emailSubject, smsMsgnextOwner);
        messagingService.sendSMS(nextOwner.getMobileNumber(), smsMsgnextOwner);
    }
}
