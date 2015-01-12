/*
 * @(#)ApplicationSecurityEventListenerAdaptor.java 3.0, 12 Jul, 2013 6:07:50 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.event.listeners;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.infstr.security.spring.event.EventType;
import org.egov.infstr.security.spring.event.actions.ApplicationSecurityEventAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

public class ApplicationSecurityEventListenerAdaptor implements ApplicationListener<ApplicationEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationSecurityEventListenerAdaptor.class);
	private Map<EventType, List<ApplicationSecurityEventAction<ApplicationEvent>>> applicationSecurityEventActions;
	
	public void setApplicationSecurityEventActions(Map<EventType, List<ApplicationSecurityEventAction<ApplicationEvent>>> applicationSecurityEventActions) {
		this.applicationSecurityEventActions = applicationSecurityEventActions;
	}

	@Override
	public void onApplicationEvent(final ApplicationEvent event) {
		if (event instanceof AuthorizedEvent) {
			final AuthorizedEvent authorizedEvent = (AuthorizedEvent) event;
			LOG.debug("User {} authorized to access {}", authorizedEvent.getAuthentication().getName());
			doAction(authorizedEvent,EventType.AuthorizedEvent);
		} else if (event instanceof AuthorizationFailureEvent) {
			final AuthorizationFailureEvent authorizationFailureEvent = (AuthorizationFailureEvent) event;
			LOG.debug("Authorization failed for User : {}", authorizationFailureEvent.getAuthentication().getName());
			doAction(authorizationFailureEvent,EventType.AuthorizationFailureEvent);
		} else if (event instanceof AuthenticationFailureBadCredentialsEvent) {
			final AuthenticationFailureBadCredentialsEvent badCredentialsEvent = (AuthenticationFailureBadCredentialsEvent) event;
			LOG.debug("User {} has entered bad credentials", badCredentialsEvent.getAuthentication().getName());
			doAction(badCredentialsEvent,EventType.AuthenticationFailureBadCredentialsEvent);
		} else if (event instanceof InteractiveAuthenticationSuccessEvent) {
			final InteractiveAuthenticationSuccessEvent authenticationSuccessEvent = (InteractiveAuthenticationSuccessEvent) event;
			LOG.debug("User {} authenticated successfully.", authenticationSuccessEvent.getAuthentication().getName());
			doAction(authenticationSuccessEvent,EventType.InteractiveAuthenticationSuccessEvent);
		} else if (event instanceof HttpSessionDestroyedEvent){
			final HttpSessionDestroyedEvent httpSessionDestroyedEvent = (HttpSessionDestroyedEvent)event;
			LOG.debug("User has logged out or Http Session expired, Session ID : {} at {}", httpSessionDestroyedEvent.getSession().getId(),new Date(httpSessionDestroyedEvent.getTimestamp()));
			doAction(httpSessionDestroyedEvent,EventType.HttpSessionDestroyedEvent);
		}
	}

	private void doAction(final ApplicationEvent event,final EventType eventType) {
		if (applicationSecurityEventActions.containsKey(eventType)) {
			for (ApplicationSecurityEventAction<ApplicationEvent> applicationAction : applicationSecurityEventActions.get(eventType)){
				applicationAction.doAction(event);
			}
		}
	}
}