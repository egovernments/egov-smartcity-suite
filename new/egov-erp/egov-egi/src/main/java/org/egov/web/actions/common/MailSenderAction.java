/*
 * @(#)MailSenderAction.java 3.0, 14 Jun, 2013 1:43:10 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.mail.MailServeable;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class MailSenderAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MailSenderAction.class);
	private MailServeable feedbackMailService;
	private MailServeable errorMailService;
	private String message;
	private String subject;

	/**
	 * Sets the message.
	 * @param message the message to set
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * Sets the subject.
	 * @param subject the subject to set
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * Send feedback.
	 * @return the view string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String sendFeedback() throws IOException {
		final HttpServletResponse res = ServletActionContext.getResponse();
		try {
			final boolean hasSent = this.feedbackMailService.sendMail(this.message, "Feedback from : " + EGOVThreadLocals.getUserId() + " : " + EGOVThreadLocals.getDomainName() + " : " + this.subject);
			if (hasSent) {
				res.getWriter().write(getText("MailSender.Feedback.Success"));
			} else {
				res.getWriter().write(getText("MailSender.Feedback.Failed"));
			}
		} catch (final RuntimeException ex) {
			LOG.error("Error occurred while sending feedback message mail", ex);
			res.getWriter().write(getText("MailSender.Feedback.Failed"));
		}
		return null;
	}

	/**
	 * Send error.
	 * @return the view string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String sendError() throws IOException {
		final HttpServletResponse res = ServletActionContext.getResponse();
		final HttpSession session = ServletActionContext.getRequest().getSession();
		try {
			final String msgKey = "message" + EGOVThreadLocals.getUserId();
			final String message = session.getAttribute(msgKey).toString();
			final boolean hasSent = this.errorMailService.sendMail(message, "Error occurred at : " + EGOVThreadLocals.getUserId() + " : " + EGOVThreadLocals.getDomainName());
			if (hasSent) {
				res.getWriter().write(getText("MailSender.Error.Success"));
				session.removeAttribute(msgKey);
			} else {
				res.getWriter().write(getText("MailSender.Error.Success"));
			}
		} catch (final RuntimeException ex) {
			LOG.error("Error occurred while sending error message mail", ex);
			res.getWriter().write(getText("MailSender.Error.Success"));
		}
		return null;
	}

	/**
	 * Sets the feedback mail service.
	 * @param feedbackMailService the new feedback mail service
	 */
	public void setFeedbackMailService(final MailServeable feedbackMailService) {
		this.feedbackMailService = feedbackMailService;
	}

	/**
	 * Sets the error mail service.
	 * @param errorMailService the new error mail service
	 */
	public void setErrorMailService(final MailServeable errorMailService) {
		this.errorMailService = errorMailService;
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public Object getModel() {
		return null;
	}
}
