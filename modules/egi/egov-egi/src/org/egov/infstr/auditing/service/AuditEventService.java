/*
 * @(#)AuditEventService.java 3.0, 21 Jun, 2013 6:08:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.auditing.service;

import java.util.Date;

import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.user.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Services correspond to Audit Trail Framework
 * Example for How to create an AuditEvent
 * <pre>
 * Inject AuditEventService where you want to do auditing as follows
 * 
 * private AuditEventService auditEventService;
 * 
 * public void setAuditEventService(final AuditEventService auditEventService) {
 * 		this.auditEventService = auditEventService;
 * }
 * 
 * Suppose you want to audit PropertyTax Create then in the respective create method
 *  
 * public String createPropertyTax() {
 * 		// Your Property tax related business logic goes here
 * 		BasicProperty basicProperty = .....;
 *  	......
 *  	
 *  	//Creating AuditEvent
 *   	//The can be extracted to a method so that you can reuse  
 * 		AuditEvent auditEvent = new AuditEvent(AuditModule.PROPERTYTAX,AuditEntity.PROPERTYTAX_PROPERTY, "Create Property", "001-001-001-001", "Property Created with....")
 * 		auditEvent.setDetails2("More details"); //Optional
 * 		auditEvent.setPkId(basicProperty.getId()); //Optional since may not be available.
 * 		//This will persist the AuditEntity
 * 		this.auditEventService.createAuditEvent(auditEvent,BasicProperty.class);
 * }
 * 
 * </pre>
 */
public final class AuditEventService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditEventService.class);
	
	private PersistenceService<AuditEvent, Long> auditEventPersistenceService;
	
	public void setAuditEventPersistenceService(PersistenceService<AuditEvent, Long> auditEventPersistenceService) {
		this.auditEventPersistenceService = auditEventPersistenceService;
	}
	
	/**
	 * Persist the given AuditEvent object for the given auditable entity class.
	 * @param AuditEvent auditEvent
	 * @param Class auditableEntityClass
	 **/
	public AuditEvent createAuditEvent(final AuditEvent auditEvent, final Class<?> auditableEntityClass) {
		auditEvent.setEventDate(new Date());
		auditEvent.setFqcn(auditableEntityClass.getName());
		final UserImpl user = (UserImpl)auditEventPersistenceService.getSession().load(UserImpl.class, Integer.valueOf(EGOVThreadLocals.getUserId()));
		auditEvent.setUserName(user.getUserName());
		LOGGER.debug("Saving Audit Trail Record : Details [{}] ",auditEvent.toString());
		auditEventPersistenceService.persist(auditEvent);
		return auditEvent;
	}
	
}
