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
