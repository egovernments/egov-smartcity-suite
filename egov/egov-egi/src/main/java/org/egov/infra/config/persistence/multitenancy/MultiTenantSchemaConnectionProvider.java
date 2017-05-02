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

package org.egov.infra.config.persistence.multitenancy;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class MultiTenantSchemaConnectionProvider implements MultiTenantConnectionProvider, ServiceRegistryAwareService {
    private static final long serialVersionUID = -6022082859572861041L;
    private static final Logger LOG = LoggerFactory.getLogger(MultiTenantSchemaConnectionProvider.class);
    private transient DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(final Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(final String tenantId) throws SQLException {
        try {
            Connection connection = getAnyConnection();
            connection.setSchema(tenantId);
            return connection;
        } catch (final SQLException e) {
            LOG.error("Error occurred while switching tenant schema upon getting connection", e);
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantId + "]", e);
        }
    }

    @Override
    public void releaseConnection(final String tenantId, final Connection connection) throws SQLException {
        try {
            connection.setSchema(tenantId);
        } catch (final SQLException e) {
            LOG.warn("Error occurred while switching schema upon release connection", e);
        }
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
        final Map<String, Object> settings = serviceRegistry.getService(ConfigurationService.class).getSettings();
        dataSource = (DataSource) settings.get(AvailableSettings.DATASOURCE);
    }
}
