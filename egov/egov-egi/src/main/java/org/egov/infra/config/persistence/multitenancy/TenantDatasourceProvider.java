package org.egov.infra.config.persistence.multitenancy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.egov.exceptions.EGOVRuntimeException;
import org.hibernate.service.spi.Stoppable;

public class TenantDatasourceProvider implements Stoppable {

	private Map<String, DataSource> tenantDatasourceStore = new ConcurrentHashMap<String, DataSource>();
	private static String ANY_DS_JNDI_NAME = "ezgovDatabasePool";

	protected DataSource getAnyDataSource() {
		return getTenantDataSource(ANY_DS_JNDI_NAME);
	}

	protected DataSource getTenantDataSource(final String tenantID) {
		DataSource datasource = tenantDatasourceStore.get(tenantID);
		if (datasource == null) {
			try {
				datasource = (DataSource) InitialContext.doLookup("java:/" + tenantID);
			} catch (final NamingException e) {
				new EGOVRuntimeException("Error occurred at JNDI lookup for tenant datatsource", e);
			}
			tenantDatasourceStore.put(tenantID, datasource);
		}
		return datasource;
	}

	@Override
	public void stop() {
		tenantDatasourceStore.clear();
		tenantDatasourceStore = null;
	}
}