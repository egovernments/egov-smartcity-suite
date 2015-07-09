/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.egov.exceptions.EGOVRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class Email {
	private static final Logger LOGGER = LoggerFactory.getLogger(Email.class);
	private final List<InternetAddress> toList;
	private final List<InternetAddress> ccList;
	private final List<InternetAddress> bccList;
	private String username;
	private String password;
	private final String subject;
	private final String body;
	private final InputStream attachment;
	private final String name;
	private final String description;

	private Email(final Builder builder) {
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


		final MultiPartEmail email = new MultiPartEmail();
		ByteArrayDataSource attachment = null;

		if (this.username == null && this.password == null) {
			this.username = "";
			this.password = "";
		}

		try {
			if (this.attachment != null) {
				LOGGER.debug("Adding attachment to the email !!");
				attachment = new ByteArrayDataSource(this.attachment, "application/octet-stream");
				email.attach(attachment, this.name, this.description, EmailAttachment.ATTACHMENT);
				LOGGER.debug("attachment to the email is done !!");
			}

			email.setHostName("");
			email.setSmtpPort(Integer.valueOf(0));
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

		public Email build() {
			return new Email(this);
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
