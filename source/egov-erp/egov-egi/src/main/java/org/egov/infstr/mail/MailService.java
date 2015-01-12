/*
 * @(#)MailService.java 3.0, 17 Jun, 2013 2:42:31 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.mail;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.infstr.utils.EGovConfig;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailService implements MailServeable {

	private static final Logger LOG = LoggerFactory.getLogger(MailService.class);
	private JavaMailSenderImpl mailSender;
	private SimpleMailMessage mailMessage;
	private String mailType;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sendMail(final String message, final String subject) {
		boolean isSent = false;
		try {
			this.mailSender.setPort(Integer.valueOf(EGovConfig.getProperty("port", "", this.mailType)));
			this.mailSender.setHost(EGovConfig.getProperty("host", "", this.mailType));
			this.mailSender.setProtocol(EGovConfig.getProperty("protocol", "", this.mailType));
			this.mailSender.setUsername(EGovConfig.getProperty("username", "", this.mailType));
			this.mailSender.setPassword(EGovConfig.getProperty("password", "", this.mailType));
			final Properties mailProperties = new Properties();
			mailProperties.setProperty("mail.smtps.auth", EGovConfig.getProperty("mail_smtps_auth", "", this.mailType));
			mailProperties.setProperty("mail.smtps.starttls.enable", EGovConfig.getProperty("mail_smtps_starttls_enable", "", this.mailType));
			mailProperties.setProperty("mail.smtps.debug", EGovConfig.getProperty("mail_smtps_debug", "", this.mailType));
			this.mailSender.setJavaMailProperties(mailProperties);
			this.mailMessage.setTo(EGovConfig.getProperty("mailTo", "", this.mailType));
			this.mailMessage.setSubject(subject);
			this.mailMessage.setText(message);
			this.mailSender.send(this.mailMessage);
			isSent = true;
		} catch (final Exception e) {
			LOG.error("Error occurred while trying to send mail", e);
		}
		return isSent;
	}

	/**
	 * Sets the mail sender.
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(final JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Sets the mail message.
	 * @param mailMessage the mailMessage to set
	 */
	public void setMailMessage(final SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	/**
	 * Sets the mail type. The email configuration Tag name present in the egov-config.xml
	 * @param mailType the mailType to set
	 */
	public void setMailType(final String mailType) {
		this.mailType = mailType;
	}
}
