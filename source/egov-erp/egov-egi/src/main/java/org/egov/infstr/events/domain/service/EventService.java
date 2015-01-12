/*
 * @(#)EventService.java 3.0, 17 Jun, 2013 12:01:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.events.domain.service;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.events.domain.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventService {

	private static final Logger LOG = LoggerFactory.getLogger(EventService.class);
	private static ThreadLocal<Event> registeredEvent = new ThreadLocal<Event>();

	public static void registerEvent(final Event e) {
		try {
			registeredEvent.set(e);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Event Registered:::::::" + e);
			}
		} catch (final Exception ex) {
			LOG.error("Exception in registerEvent" + e);
			throw new EGOVRuntimeException("Exception occurred in EventService register Event>>>", ex);
		}
	}

	public static Event getRegisteredEvent() {
		return registeredEvent.get();
	}

	public static boolean isEventRegistered() {
		return (registeredEvent.get() != null);
	}

	public static void removeRegisteredEvent() {
		registeredEvent.remove();
	}
}
