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

import static org.egov.infra.notification.NotificationConstants.CHARSET;
import static org.egov.infra.notification.NotificationConstants.COMPONENT;
import static org.egov.infra.notification.NotificationConstants.CONTENTCLASS_KEY;
import static org.egov.infra.notification.NotificationConstants.CONTENTCLASS_VALUE;
import static org.egov.infra.notification.NotificationConstants.CONTENTID_KEY;
import static org.egov.infra.notification.NotificationConstants.CONTENTID_VALUE;
import static org.egov.infra.notification.NotificationConstants.DATASOURCETYPE;
import static org.egov.infra.notification.NotificationConstants.DATETIME_FORMAT_YYYYMMDDTHHMMSSZ;
import static org.egov.infra.notification.NotificationConstants.MAILBODYMESSAGE_CONTENT;
import static org.egov.infra.notification.NotificationConstants.REQUESTMETHOD;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.notification.entity.CalendarInviteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    public void sendEmail(String toEmail, String subject, String mailBody) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setFrom(mailSender.getUsername());
        mailMessage.setSubject(subject);
        mailMessage.setText(mailBody);
        mailSender.send(mailMessage);
    }

    public void sendEmail(String toEmail, String subject, String mailBody, String fileType, String fileName,
            byte[] attachment) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessage = new MimeMessageHelper(message, true);
            mimeMessage.setTo(toEmail);
            mimeMessage.setFrom(mailSender.getUsername());
            mimeMessage.setSubject(subject);
            mimeMessage.setText(mailBody);
            ByteArrayDataSource source = new ByteArrayDataSource(attachment, fileType);
            mimeMessage.addAttachment(fileName, source);
        } catch (MessagingException | IllegalArgumentException e) {
            throw new ApplicationRuntimeException("Error occurred while sending email with attachment", e);
        }
        mailSender.send(message);
    }

    /**
     * 
     * @param toEmail
     * @param subject
     * @param calendarInviteInfo parameter is used to build calendar invitation.
     * 
     */

    public void sendCalendarInvite(String toEmail, String subject, CalendarInviteInfo calendarInviteInfo) {
        MimeMessage calendarMail = mailSender.createMimeMessage();
        try {
            calendarMail.addHeaderLine(REQUESTMETHOD);
            calendarMail.addHeaderLine(CHARSET);
            calendarMail.addHeaderLine(COMPONENT);
            MimeMessageHelper mailMessage = new MimeMessageHelper(calendarMail, true);
            mailMessage.setFrom(mailSender.getUsername());
            mailMessage.setSubject(subject);
            if (null == calendarInviteInfo.getMailList() || calendarInviteInfo.getMailList().isEmpty())
                mailMessage.setTo(toEmail);
            else
                mailMessage.setTo(calendarInviteInfo.getMailList().toArray(new String[calendarInviteInfo.getMailList().size()]));
            Multipart mailContent = new MimeMultipart();
            mailContent.addBodyPart(constructCalendarMail(calendarInviteInfo));
            if (StringUtils.isNotBlank(calendarInviteInfo.getMailBodyMessage()))
                mailContent.addBodyPart(constructMailMessage(calendarInviteInfo.getMailBodyMessage()));
            calendarMail.setContent(mailContent);
        } catch (MessagingException | IllegalArgumentException e) {
            throw new ApplicationRuntimeException("Error occurred while sending calendar email notification", e);
        }
        mailSender.send(calendarMail);
    }

    /**
     * 
     * @param calendarInviteInfo
     * @return Calendar invitation format mail body.
     */

    private BodyPart constructCalendarMail(CalendarInviteInfo calendarInviteInfo) {
        BodyPart calendarMail = new MimeBodyPart();
        try {
            calendarMail.setHeader(CONTENTCLASS_KEY, CONTENTCLASS_VALUE);
            calendarMail.setHeader(CONTENTID_KEY, CONTENTID_VALUE);
            calendarMail.setDataHandler(new DataHandler(
                    new ByteArrayDataSource(constructCalendarInvite(calendarInviteInfo), DATASOURCETYPE)));
        } catch (MessagingException | IOException e) {
            throw new ApplicationRuntimeException("Error occurred while preparing calendar invitation", e);
        }
        return calendarMail;
    }

    private String constructCalendarInvite(CalendarInviteInfo calendarInviteInfo) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT_YYYYMMDDTHHMMSSZ);

        return messageSource.getMessage("calendar.invite.message", new String[] { calendarInviteInfo.getParticipant(),
                calendarInviteInfo.getParticipant(),
                calendarInviteInfo.getStartDateTime(),
                calendarInviteInfo.getEndDateTime(),
                dateFormat.format(new Date()),
                calendarInviteInfo.getLocation(),
                calendarInviteInfo.getDescription(),
                calendarInviteInfo.getSummary() }, null);
    }

    /**
     * API is to add calendar invite mail details. In calendar invitation with mailbodyMessage param can provide notification
     * details.
     * @param mailbodyMessage
     * @return Calendar invitation mail message body.
     */
    private MimeBodyPart constructMailMessage(String mailbodyMessage) {
        MimeBodyPart mailMessage = new MimeBodyPart();
        try {
            mailMessage.setContent(
                    messageSource.getMessage("calendar.invite.mailbody.message", new String[] { mailbodyMessage }, null),
                    MAILBODYMESSAGE_CONTENT);
        } catch (MessagingException e) {
            throw new ApplicationRuntimeException("Error occurred while preparing calendar notification", e);
        }
        return mailMessage;
    }
}
