package org.egov.infra.config.persistence.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;

@SuppressWarnings("all")
public class MultiTenantDatabaseConnectionProvider extends TenantDatasourceProvider implements
		MultiTenantConnectionProvider {

	private static final long serialVersionUID = -4318080892788400229L;

	@Override
	public Connection getAnyConnection() throws SQLException {
		return getAnyDataSource().getConnection();
	}

	@Override
	public void releaseAnyConnection(final Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(final String tenantId) throws SQLException {
		return getTenantDataSource(tenantId).getConnection();
	}

	@Override
	public void releaseConnection(final String tenantId, final Connection connection) throws SQLException {
		connection.close();
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

}
