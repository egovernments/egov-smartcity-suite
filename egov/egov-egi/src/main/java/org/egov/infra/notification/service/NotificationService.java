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

package org.egov.infra.notification.service;

import org.egov.infra.admin.common.service.MessageTemplateService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.notification.entity.CalendarInviteInfo;
import org.egov.infra.notification.entity.NotificationPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.egov.infra.notification.NotificationConstants.ATTACHMENT;
import static org.egov.infra.notification.NotificationConstants.EMAIL;
import static org.egov.infra.notification.NotificationConstants.FILENAME;
import static org.egov.infra.notification.NotificationConstants.FILETYPE;
import static org.egov.infra.notification.NotificationConstants.MESSAGE;
import static org.egov.infra.notification.NotificationConstants.MOBILE;
import static org.egov.infra.notification.NotificationConstants.PRIORITY;
import static org.egov.infra.notification.NotificationConstants.SUBJECT;
import static org.egov.infra.notification.entity.NotificationPriority.HIGH;
import static org.egov.infra.notification.entity.NotificationPriority.MEDIUM;
import static org.egov.infra.notification.NotificationConstants.TEMPLATEID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("emailQueue")
    private Destination emailQueue;

    @Autowired
    @Qualifier("smsQueue")
    private Destination smsQueue;

    @Autowired
    @Qualifier("flashQueue")
    private Destination flashQueue;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Value("${mail.enabled}")
    private boolean mailEnabled;

    @Value("${sms.enabled}")
    private boolean smsEnabled;

    public void sendEmail(User user, String subject, String templateName,
            Object... messageValues) {
        sendEmail(user.getEmailId(), subject, messageTemplateService.realizeMessage(
                messageTemplateService.getByTemplateName(templateName), messageValues));
    }

    public void sendEmail(String email, String subject, String message) {
        if (mailEnabled && isNoneBlank(email, subject, message))
            jmsTemplate.send(emailQueue, session -> {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString(EMAIL, email);
                mapMessage.setString(MESSAGE, message);
                mapMessage.setString(SUBJECT, subject);
                return mapMessage;
            });
    }

    public void sendEmailWithAttachment(String email, String subject, String message,
            String fileType, String fileName, byte[] attachment) {
        if (mailEnabled && isNoneBlank(email, subject, message))
            jmsTemplate.send(emailQueue, session -> {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString(EMAIL, email);
                mapMessage.setString(MESSAGE, message);
                mapMessage.setString(SUBJECT, subject);
                mapMessage.setString(FILETYPE, fileType);
                mapMessage.setString(FILENAME, fileName);
                mapMessage.setBytes(ATTACHMENT, attachment);
                return mapMessage;
            });
    }

    /* @deprecated 
     * As part of new SMS API integration.
     * <p> Use {@link NotificationService#sendSMS(String mobileNo, String message, String templateId)}
     */
    @Deprecated
    public void sendSMS(String mobileNo, String message) {
        //sendSMS(mobileNo, message, MEDIUM);
    	sendSMS(mobileNo, getMessage(message), getTemplateIdFromMessage(message), MEDIUM);
    }

    public void sendSMS(User user, String templateName, Object... messageValues) {
        sendSMS(user.getMobileNumber(), messageTemplateService.realizeMessage(
                messageTemplateService.getByTemplateName(templateName), messageValues), MEDIUM);
    }

    public void sendSMS(String mobileNo, String message, NotificationPriority priority) {
        if (smsEnabled && isNoneBlank(mobileNo, message))
            jmsTemplate.send(HIGH.equals(priority) ? flashQueue : smsQueue, session -> {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString(MOBILE, mobileNo);
                mapMessage.setString(MESSAGE, message);
                mapMessage.setString(PRIORITY, priority.name());
                return mapMessage;
            });
    }
    
    public void sendSMS(String mobileNo, String message, String templateId) {
        sendSMS(mobileNo, message, templateId, MEDIUM);
    }
    
    public void sendSMS(String mobileNo, String message, String templateId, NotificationPriority priority) {
        if (smsEnabled && isNoneBlank(mobileNo, message))
            jmsTemplate.send(HIGH.equals(priority) ? flashQueue : smsQueue, session -> {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString(MOBILE, mobileNo);
                mapMessage.setString(MESSAGE, message);
                mapMessage.setString(TEMPLATEID, templateId);
                mapMessage.setString(PRIORITY, priority.name());
                return mapMessage;
            });
    }

    public void sendCalendarInvite(String email, String subject, CalendarInviteInfo calendarInfo) {
        if (mailEnabled && calendarInfo != null && calendarInfo.getStartDateTime() != null
                && calendarInfo.getEndDateTime() != null
                && isNoneBlank(email, subject, calendarInfo.getLocation(), calendarInfo.getSummary(),
                        calendarInfo.getDescription(), calendarInfo.getSummary()))
            jmsTemplate.send(emailQueue, session -> {
                ObjectMessage mapMessage = session.createObjectMessage();
                mapMessage.setStringProperty(EMAIL, email);
                mapMessage.setStringProperty(SUBJECT, subject);
                mapMessage.setObject(calendarInfo);
                return mapMessage;
            });
    }
    
    public static String getTemplateIdFromMessage(String message) {
    	Pattern ptn = Pattern.compile("\\$\\$(.*?)\\$\\$");
    	Matcher m = ptn.matcher(message);
        while (m.find()) {
        	return m.group(1);
        }
    	return null;
    }
    
    public static String getMessage(String message) {
    	Pattern ptn = Pattern.compile("\\$\\$(.*?)\\$\\$");
    	String[] parts = ptn.split(message);
    	StringBuilder builder = new StringBuilder(""); 
        for(String p:parts){
        	builder.append(p);
        	builder.append(" ");
        }
    	return builder.toString().trim();
    }
}
