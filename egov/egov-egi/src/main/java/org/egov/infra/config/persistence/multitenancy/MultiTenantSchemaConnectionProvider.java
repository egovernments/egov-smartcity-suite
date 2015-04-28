package org.egov.infra.config.persistence.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

@SuppressWarnings("all")
public class MultiTenantSchemaConnectionProvider implements MultiTenantConnectionProvider, ServiceRegistryAwareService {
	private static final long serialVersionUID = -6022082859572861041L;
	private DataSource dataSource;
	private String databaseType;
	
	@Override
	public Connection getAnyConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void releaseAnyConnection(final Connection connection) throws SQLException {
		try {
			if (databaseType.equals("POSTGRESQL")) 
				connection.createStatement().execute("SET SCHEMA 'public'");
			else 
				connection.createStatement().execute( "USE master" );
		} catch (final SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [public]", e);
		}
		connection.close();
	}

	@Override
	public Connection getConnection(final String tenantId) throws SQLException {
		final Connection connection = getAnyConnection();
		try {
			if (databaseType.equals("POSTGRESQL")) 
				connection.createStatement().execute("SET SCHEMA '" + tenantId + "'");
			else
				connection.createStatement().execute( "USE "+tenantId );
		} catch (final SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantId + "]", e);
		}
		return connection;
	}

	@Override
	public void releaseConnection(final String tenantId, final Connection connection) throws SQLException {
		releaseAnyConnection(connection);
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return true;
	}

	@Override
	public boolean isUnwrappableAs(final Class unwrapType) {
		return MultiTenantConnectionProvider.class.equals(unwrapType)
				|| AbstractMultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
	}

	@Override
	public <T> T unwrap(final Class<T> unwrapType) {
		if (isUnwrappableAs(unwrapType))
			return (T) this;
		else
			throw new UnknownUnwrapTypeException(unwrapType);
	}

	@Override
	public void injectServices(final ServiceRegistryImplementor serviceRegistry) {
		Map<String,Object> settings = serviceRegistry.getService(ConfigurationService.class).getSettings();
		dataSource = (DataSource) settings.get(AvailableSettings.DATASOURCE);
		databaseType = (String)settings.get("hibernate.database.type");
	}

}
