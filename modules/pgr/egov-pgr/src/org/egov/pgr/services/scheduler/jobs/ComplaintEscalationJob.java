/*
 * @(#)ComplaintEscalationJob.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.services.scheduler.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.events.domain.entity.Event;
import org.egov.infstr.events.domain.service.EventService;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.domain.services.ComplaintDetailService;
import org.egov.pgr.domain.services.EscalationService;
import org.egov.pgr.utils.PgrCommonUtils;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.BeansException;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

@DisallowConcurrentExecution
public class ComplaintEscalationJob extends AbstractQuartzJob {

	private static final long serialVersionUID = 1L;
	private ComplaintDetailService complaintService;
	private EisCommonsService eisCommonsService;
	private EscalationService escalationService;
	private AppConfigValuesDAO appConfigValuesDAO;
	private JmsTemplate jmsTemplate;

	@Override
	public void executeJob() {
		// Get the email config value from the app config
		final AppConfigValues appConfigValue = this.appConfigValuesDAO.getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, "SENDEMAILFORESCALATION").get(0);
		final Boolean isEmailNotificationSet = "YES".equalsIgnoreCase(appConfigValue.getValue());
		final List<ComplaintDetails> escalationComplaints = this.complaintService.getComplaintsEligibleForEscalation();
		for (final ComplaintDetails complaintDetails : escalationComplaints) {
			final Position superiorPosition = this.eisCommonsService.getSuperiorPositionByObjType(complaintDetails.getRedressal().getPosition(), PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
			final User superiorUser = this.eisCommonsService.getUserForPosition(superiorPosition.getId(), new Date());
			// if the previous RedressalOfficer is the same as Superior User then take no actions
			if (superiorUser.getId().intValue() != complaintDetails.getRedressal().getRedressalOfficer().getId().intValue()) {

				complaintDetails.setEscalatedTime(new Date());
				complaintDetails.setIsEscalated(1);
				complaintDetails.setExpiryDate(getExpairyDate(superiorUser, complaintDetails));

				complaintDetails.changeState(complaintDetails.getRedressal().getComplaintStatus().getName(), "", superiorPosition, "The Complaint has been Escalated ");
				complaintDetails.getRedressal().setRedressalOfficer(superiorUser);
				complaintDetails.getRedressal().setPosition(superiorPosition);
				if (isEmailNotificationSet && PgrCommonUtils.isValidEmail(superiorUser.getExtraField1())) {
					final Map<String, String> params = new HashMap<String, String>();
					params.put("to_address", superiorUser.getExtraField1());
					params.put("subject", "Complaint Escaltion");
					params.put("to_name", superiorUser.getFirstName());
					final Event event = new Event(PGRConstants.MODULE_NAME, "Complaint Escalation", params);
					EventService.registerEvent(event);
					sendEmail();
				}

			}
		}

	}

	private Date getExpairyDate(final User superiorUser, final ComplaintDetails complaintDetails) {

		Date expairyDate = null;
		for (final Role role : superiorUser.getRoles()) {

			final Integer noOfDays = this.escalationService.getNoOfDaysForEscByRoleAndCompType(complaintDetails.getComplaintType(), role);
			if (null != noOfDays) {
				expairyDate = DateUtils.add(complaintDetails.getEscalatedTime(), Calendar.DAY_OF_MONTH, noOfDays);
				break;
			} else {
				expairyDate = this.complaintService.getComplaintExpiryDate(complaintDetails.getComplaintType().getId(), complaintDetails.getEscalatedTime());
			}
		}
		return expairyDate;
	}

	private void sendEmail() {
		if (!HibernateUtil.isMarkedForRollback() && EventService.isEventRegistered()) {
			final Event event = EventService.getRegisteredEvent();
			raiseEvent(event);
			EventService.removeRegisteredEvent();
		}
	}

	private void raiseEvent(final Event event) {
		try {
			final Map<String, String> params = new HashMap<String, String>();
			event.addParam("DOMAIN_NAME", EGOVThreadLocals.getDomainName());
			event.addParam("JNDI_NAME", EGOVThreadLocals.getJndiName());
			event.addParam("FACTORY_NAME", EGOVThreadLocals.getHibFactName());
			event.addParams(params);
			event.setDateRaised(new Date());

			final MessageCreator message = new MessageCreator() {
				@Override
				public Message createMessage(final Session session) throws JMSException {
					final ObjectMessage objectMessage = session.createObjectMessage();
					objectMessage.setObject(event);
					return objectMessage;
				}
			};
			this.jmsTemplate.send(message);

		} catch (final BeansException e) {
			throw new EGOVRuntimeException("Exception in raising Event:::::", e);
		} catch (final JmsException e) {
			throw new EGOVRuntimeException("Exception in raising Event:::::", e);
		}
	}

	public void setComplaintService(final ComplaintDetailService complaintService) {
		this.complaintService = complaintService;
	}

	public void setEisCommonsService(final EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setEscalationService(final EscalationService escalationService) {
		this.escalationService = escalationService;
	}

	public void setAppConfigValuesDAO(final AppConfigValuesDAO appConfigValuesDAO) {
		this.appConfigValuesDAO = appConfigValuesDAO;
	}

	public void setJmsTemplate(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
