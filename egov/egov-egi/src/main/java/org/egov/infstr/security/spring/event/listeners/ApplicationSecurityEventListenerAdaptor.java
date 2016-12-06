/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infstr.security.spring.event.listeners;

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

import java.util.Date;
import java.util.List;
import java.util.Map;

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