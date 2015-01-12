package org.egov.lib.security.terminal.dao;

import static org.junit.Assert.assertNotNull;

import org.egov.lib.security.terminal.model.LocationIPMap;
import org.junit.Test;

public class LocationIPMapHibernateDAOTest {

	@Test
	public void testLocationIPMapHibernateDAO() {
		final LocationIPMapHibernateDAO locationIPMapHibernateDAO = new LocationIPMapHibernateDAO(
				LocationIPMap.class, null);
		assertNotNull(locationIPMapHibernateDAO);
	}

}
