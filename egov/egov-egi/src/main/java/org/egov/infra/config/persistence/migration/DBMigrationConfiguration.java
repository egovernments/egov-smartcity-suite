/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.infra.config.persistence.migration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Configuration
public class DBMigrationConfiguration {

    @Value("${dev.mode}")
    private boolean devMode;

    @Value("${db.migration.enabled}")
    private boolean dbMigrationEnabled;

    @Value("${db.flyway.validateon.migrate}")
    private boolean validateOnMigrate;

    @Value("${db.flyway.migration.repair}")
    private boolean repairMigration;

    @Value("${statewide.migration.required}")
    private boolean statewideMigrationRequired;

    @Value("${db.flyway.main.migration.file.path}")
    private String mainMigrationFilePath;

    @Value("${db.flyway.sample.migration.file.path}")
    private String sampleMigrationFilePath;

    @Value("${db.flyway.tenant.migration.file.path}")
    private String tenantMigrationFilePath;

    @Value("${db.flyway.statewide.migration.file.path}")
    private String statewideMigrationFilePath;

    @Value("${statewide.schema.name}")
    private String statewideSchemaName;

    @Autowired
    private ConfigurableEnvironment environment;

    @Bean
    @DependsOn("dataSource")
    public Flyway flyway(DataSource dataSource, @Qualifier("cities") List<String> cities) {
        if (dbMigrationEnabled) {
            cities.stream().forEach(schema -> {
                if (devMode)
                    migrateDatabase(dataSource, schema,
                            mainMigrationFilePath, sampleMigrationFilePath, format(tenantMigrationFilePath, schema));
                else
                    migrateDatabase(dataSource, schema,
                            mainMigrationFilePath, format(tenantMigrationFilePath, schema));
            });

            if (statewideMigrationRequired && !devMode) {
                migrateDatabase(dataSource, statewideSchemaName, mainMigrationFilePath, statewideMigrationFilePath);
            } else if (!devMode) {
                migrateDatabase(dataSource, statewideSchemaName, mainMigrationFilePath);
            }
        }

        return new Flyway();
    }

    private void migrateDatabase(DataSource dataSource, String schema, String... locations) {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setValidateOnMigrate(validateOnMigrate);
        flyway.setOutOfOrder(true);
        flyway.setLocations(locations);
        flyway.setDataSource(dataSource);
        flyway.setSchemas(schema);
        if (repairMigration)
            flyway.repair();
        flyway.migrate();
    }

    @Bean(name = "tenants", autowire = Autowire.BY_NAME)
    public List<String> tenants() {
        List<String> tenants = new ArrayList<>();
        environment.getPropertySources().iterator().forEachRemaining(propertySource -> {
            if (propertySource instanceof MapPropertySource)
                ((MapPropertySource) propertySource).getSource().forEach((key, value) -> {
                    if (key.startsWith("tenant."))
                        tenants.add(value.toString());
                });
        });
        return tenants;
    }

}
