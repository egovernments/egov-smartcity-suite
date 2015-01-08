/*
 * @(#)EventResultService.java 3.0, 17 Jun, 2013 12:01:45 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.events.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.events.domain.entity.EventResult;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.HibernateException;

public class EventResultService {

	private static final Logger LOG = LoggerFactory.getLogger(EventProcessorSpecService.class);
	protected PersistenceService persistenceService;

	public void persistEventResult(final EventResult eventResult) {
		try {
			this.persistenceService.setType(EventResult.class);
			this.persistenceService.persist(eventResult);
		} catch (final HibernateException e) {
			LOG.error("Exception in EventResultService" + e);
			throw new EGOVRuntimeException("Exception in EventResultService::::", e);
		}
	}

	public PersistenceService getPersistenceService() {
		return this.persistenceService;
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

}
