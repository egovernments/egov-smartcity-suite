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

package org.egov.infra.messaging;

import org.egov.infra.admin.common.service.MessageTemplateService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.MapMessage;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.egov.infra.messaging.MessageConstants.ATTACHMENT;
import static org.egov.infra.messaging.MessageConstants.EMAIL;
import static org.egov.infra.messaging.MessageConstants.FILENAME;
import static org.egov.infra.messaging.MessageConstants.FILETYPE;
import static org.egov.infra.messaging.MessageConstants.MESSAGE;
import static org.egov.infra.messaging.MessageConstants.MOBILE;
import static org.egov.infra.messaging.MessageConstants.PRIORITY;
import static org.egov.infra.messaging.MessageConstants.SUBJECT;
import static org.egov.infra.messaging.MessagePriority.MEDIUM;

@Service
public class MessagingService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("emailQueue")
    private Destination emailQueue;

    @Autowired
    @Qualifier("smsQueue")
    private Destination smsQueue;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public void sendEmail(final User user, final String subject, final String templateName,
                          final Object... messageValues) {
        sendEmail(user.getEmailId(), subject, messageTemplateService.realizeMessage(
                messageTemplateService.getByTemplateName(templateName), messageValues));
    }

    public void sendEmail(final String email, final String subject, final String message) {
        if (applicationProperties.emailEnabled() && isNoneBlank(email, subject, message))
            jmsTemplate.send(emailQueue, session -> {
                final MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString(EMAIL, email);
                mapMessage.setString(MESSAGE, message);
                mapMessage.setString(SUBJECT, subject);
                return mapMessage;
            });
    }

    public void sendEmailWithAttachment(final String email, final String subject, final String message,
                                        final String fileType, final String fileName, final byte[] attachment) {
        if (applicationProperties.emailEnabled() && isNoneBlank(email, subject, message))
            jmsTemplate.send(emailQueue, session -> {
                final MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString(EMAIL, email);
                mapMessage.setString(MESSAGE, message);
                mapMessage.setString(SUBJECT, subject);
                mapMessage.setString(FILETYPE, fileType);
                mapMessage.setString(FILENAME, fileName);
                mapMessage.setBytes(ATTACHMENT, attachment);
                return mapMessage;
            });
    }

    public void sendSMS(final String mobileNo, final String message) {
        sendSMS(mobileNo, message, MEDIUM);
    }

    public void sendSMS(final User user, final String templateName, final Object... messageValues) {
        sendSMS(user.getMobileNumber(), messageTemplateService.realizeMessage(
                messageTemplateService.getByTemplateName(templateName), messageValues), MEDIUM);
    }

    public void sendSMS(final String mobileNo, final String message, MessagePriority priority) {
        if (applicationProperties.smsEnabled() && isNoneBlank(mobileNo, message))
            jmsTemplate.send(smsQueue, session -> {
                final MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString(MOBILE, mobileNo);
                mapMessage.setString(MESSAGE, message);
                mapMessage.setString(PRIORITY, priority.name());
                return mapMessage;
            });
    }
}
