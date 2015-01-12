package org.egov.infstr.auditing.service;

import static junit.framework.Assert.assertNotNull;

import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * The class <code>AuditEventServiceTest</code> contains tests for the class
 * {@link <code>AuditEventService</code>}
 */
public class AuditEventServiceTest {
	PersistenceService<AuditEvent, Long> persistenceService = new PersistenceService<AuditEvent, Long>();
	Session session = Mockito.mock(Session.class);
	User user = Mockito.mock(UserImpl.class);
	AuditEventService fixture = new AuditEventService();

	@Before
	public void beforeTest() {
		this.persistenceService = Mockito.spy(this.persistenceService);
		this.persistenceService.setType(AuditEvent.class);
		EGOVThreadLocals.setUserId("1");
		Mockito.doReturn("Egov").when(this.user).getUserName();
		Mockito.doReturn(this.user).when(this.session).load(UserImpl.class, 1);
		Mockito.doReturn(this.session).when(this.persistenceService)
				.getSession();
		this.fixture.setAuditEventPersistenceService(this.persistenceService);
	}

	/**
	 * Run the AuditEvent createAuditEvent(AuditEvent, Class<?>) method test
	 */
	@Test
	public void testCreateAuditEvent() {
		final AuditEvent auditEvent = new AuditEvent(AuditModule.PROPERTYTAX,
				AuditEntity.PROPERTYTAX_PROPERTY, "SAVE", "BIZID", "DETAILS");
		Mockito.doNothing().when(this.session).persist(auditEvent);
		final AuditEvent result = this.fixture.createAuditEvent(auditEvent,
				Object.class);
		assertNotNull(result);
	}
}