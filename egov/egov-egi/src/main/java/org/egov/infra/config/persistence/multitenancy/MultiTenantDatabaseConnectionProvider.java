/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.config.persistence.multitenancy;

import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;

import java.sql.Connection;
import java.sql.SQLException;

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
