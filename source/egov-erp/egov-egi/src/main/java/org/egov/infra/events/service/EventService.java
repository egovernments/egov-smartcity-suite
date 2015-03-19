package org.egov.infra.events.service;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.events.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventService {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventService.class);
	private static ThreadLocal<Event> registeredEvent = new ThreadLocal<Event>();

	public static void registerEvent(final Event e) {
		try {
			registeredEvent.set(e);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Event Registered:::::::" + e);
			}
		} catch (final Exception ex) {
			LOG.error("Exception in registerEvent" + e);
			throw new EGOVRuntimeException(
					"Exception occurred in EventService register Event>>>", ex);
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