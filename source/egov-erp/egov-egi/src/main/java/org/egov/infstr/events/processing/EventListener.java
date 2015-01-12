/*
 * @(#)EventListener.java 3.0, 17 Jun, 2013 12:00:59 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.events.processing;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.events.domain.entity.Event;

/**
 * Message driven pojo that receives an Event object from the queue Delegates processing 
 * to EventProcessor.
 * @author sunil
 */
public class EventListener implements MessageListener {
	private static final Logger LOG = LoggerFactory.getLogger(EventListener.class);

	protected EventProcessor eventProcessor;

	@Override
	public void onMessage(final Message message) {
		final ObjectMessage obj = (ObjectMessage) message;
		Event event = null;
		try {
			event = (Event) obj.getObject();
			this.eventProcessor.process(event);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Event Received" + event);
			}
		} catch (final JMSException e) {
			throw new EGOVRuntimeException("Exception in EventListener message ::::", e);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception Occurred in EventListener" + e);
		}
	}

	public EventProcessor getEventProcessor() {
		return this.eventProcessor;
	}

	public void setEventProcessor(final EventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
	}
}
