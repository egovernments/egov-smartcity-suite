/*
 * @(#)EventProcessorSpecService.java 3.0, 17 Jun, 2013 12:02:32 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.events.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.events.domain.entity.EventProcessorSpec;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class EventProcessorSpecService {

	private static final Logger LOG = LoggerFactory.getLogger(EventProcessorSpecService.class);
	protected PersistenceService persistenceService;

	public EventProcessorSpec getEventProcessingSpecByModAndCode(final String module, final String eventCode) {
		EventProcessorSpec eventSpec = null;
		try {
			this.persistenceService.setType(EventProcessorSpec.class);
			final Query qry = this.persistenceService.getSession().getNamedQuery("event_specByModuleAndCode");
			qry.setString("module", module);
			qry.setString("eventCode", eventCode);
			eventSpec = (EventProcessorSpec) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOG.error("Exception in EventProcessorSpecService::" + e);
			throw new EGOVRuntimeException("Exception in EventProcessorSpecService::::", e);
		}
		return eventSpec;
	}

	public PersistenceService getPersistenceService() {
		return this.persistenceService;
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
}
