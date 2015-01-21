/*
 * @(#)Email.java 3.0, 17 Jun, 2013 2:41:30 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.mail;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Email {
	private static final Logger LOGGER = LoggerFactory.getLogger(Email.class);
	private final List<InternetAddress> toList;
	private final List<InternetAddress> ccList;
	private final List<InternetAddress> bccList;
	private final GenericHibernateDaoFactory genericHibernateDaoFactory;
	private String username;
	private String password;
	private final String subject;
	private final String body;
	private final InputStream attachment;
	private final String name;
	private final String description;

	private Email(final Builder builder, GenericHibernateDaoFactory genericHibernateDaoFactory) {
		this.genericHibernateDaoFactory = genericHibernateDaoFactory;
		this.body = builder.body;
		this.subject = builder.subject;
		this.username = builder.username;
		this.password = builder.password;
		this.toList = builder.toLst;
		this.ccList = builder.ccLst;
		this.bccList = builder.bccLst;
		this.attachment = builder.attachment;
		this.name = builder.name;
		this.description = builder.description;
	}

	public void send() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Started sending email !!");
			LOGGER.debug("Email parameters :" + this.toList + "||" + this.ccList + "||" + this.bccList + "||" + this.subject + "||" + this.body);
		}

		final AppConfigValuesDAO appConfValDao = genericHibernateDaoFactory.getAppConfigValuesDAO();

		final MultiPartEmail email = new MultiPartEmail();
		ByteArrayDataSource attachment = null;

		if (this.username == null && this.password == null) {
			this.username = appConfValDao.getConfigValuesByModuleAndKey("egi", "mailSenderUserName").get(0).getValue();
			this.password = appConfValDao.getConfigValuesByModuleAndKey("egi", "mailSenderPassword").get(0).getValue();
		}

		try {
			if (this.attachment != null) {
				LOGGER.debug("Adding attachment to the email !!");
				attachment = new ByteArrayDataSource(this.attachment, "application/octet-stream");
				email.attach(attachment, this.name, this.description, EmailAttachment.ATTACHMENT);
				LOGGER.debug("attachment to the email is done !!");
			}

			email.setHostName(appConfValDao.getConfigValuesByModuleAndKey("egi", "smtpHostName").get(0).getValue());
			email.setSmtpPort(Integer.valueOf(appConfValDao.getConfigValuesByModuleAndKey("egi", "smtpPort").get(0).getValue()));
			email.setAuthenticator(new DefaultAuthenticator(this.username, this.password));
			email.setDebug(false);
			email.setTo(this.toList);

			if (this.ccList != null && this.ccList.size() != 0) {
				email.setCc(this.ccList);
			}

			if (this.bccList != null && this.bccList.size() != 0) {
				email.setBcc(this.bccList);
			}

			email.setFrom(this.username);
			email.setSubject(this.subject);
			email.setMsg(this.body);

			email.setTLS(true);
			email.send();

		} catch (final EmailException emailExp) {
			throw new EGOVRuntimeException("Error occured in sending email !!", emailExp);
		} catch (final IOException ioExp) {
			throw new EGOVRuntimeException("Error occured in attachment !!", ioExp);
		}

		LOGGER.debug("Sending email is done !!");

	}

	public static class Builder {
		private final List<InternetAddress> toLst;
		private final List<InternetAddress> ccLst;
		private final List<InternetAddress> bccLst;
		private String username;
		private String password;
		private String subject;
		private final String body;
		private InputStream attachment;
		private String name;
		private String description;

		public Builder(final String to, final String body) {
			this.body = body;
			this.toLst = new ArrayList<InternetAddress>();
			try {
				this.toLst.add(new InternetAddress(to));
			} catch (final AddressException addrExp) {
				throw new EGOVRuntimeException("Invalid To Adress -" + to, addrExp);
			}
			this.ccLst = new ArrayList<InternetAddress>();
			this.bccLst = new ArrayList<InternetAddress>();
		}

		public Builder(final List<String> tos, final String body) {
			this.body = body;
			this.toLst = new ArrayList<InternetAddress>();
			try {
				for (String to : tos) {
					this.toLst.add(new InternetAddress(to));
				}
			} catch (final AddressException addrExp) {
				throw new EGOVRuntimeException("Invalid To Adress -" + tos, addrExp);
			}
			this.ccLst = new ArrayList<InternetAddress>();
			this.bccLst = new ArrayList<InternetAddress>();
		}

		public Email build(GenericHibernateDaoFactory genericHibernateDaoFactory) {
			return new Email(this, genericHibernateDaoFactory);
		}

		public Builder subject(final String subject) {
			this.subject = subject;
			return this;
		}

		public Builder addTo(final String address) {
			try {
				this.toLst.add(new InternetAddress(address));
			} catch (final AddressException addrExp) {
				throw new EGOVRuntimeException("Invalid To Adress -" + address, addrExp);
			}

			return this;
		}

		public Builder addCc(final String address) {
			try {
				this.ccLst.add(new InternetAddress(address));
			} catch (final AddressException addrExp) {
				throw new EGOVRuntimeException("Invalid CC Adress -" + address, addrExp);
			}

			return this;
		}

		public Builder addCc(final List<String> addresses) {
			try {
				for (String address : addresses)
				this.ccLst.add(new InternetAddress(address));
			} catch (final AddressException addrExp) {
				throw new EGOVRuntimeException("Invalid CC Adress -" + addresses, addrExp);
			}

			return this;
		}
		
		public Builder addBcc(final String address) {
			try {
				this.bccLst.add(new InternetAddress(address));
			} catch (final AddressException addrExp) {
				throw new EGOVRuntimeException("Invalid BCC Adress -" + address, addrExp);
			}

			return this;
		}

		public Builder addBcc(final List<String> addresses) {
			try {
				for (String address : addresses)
				this.bccLst.add(new InternetAddress(address));
			} catch (final AddressException addrExp) {
				throw new EGOVRuntimeException("Invalid BCC Adress -" + addresses, addrExp);
			}

			return this;
		}

		/**
		 * This method is for adding attachment to the email
		 * @param attachment -> input stream to be attached with the email
		 * @param name -> file name with extension of the file to be attached
		 * @param description -> description of the file to be attached
		 */

		public Builder attachment(final InputStream attachment, final String name, final String description) {
			this.attachment = attachment;
			this.name = name;
			this.description = description;
			return this;
		}

		/**
		 * This method is for passing username and password of sender
		 * @param username -> user name of sender
		 * @param password -> password for the user name
		 */

		public Builder mailServerAuth(final String username, final String password) {
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				throw new EGOVRuntimeException("Either username or password is empty : username=" + username + "password=" + password);
			} else {
				this.username = username;
				this.password = password;
			}
			return this;
		}

	}

}
