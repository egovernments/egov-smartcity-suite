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

package org.egov.infra.config.persistence.migration;

import org.egov.infra.config.properties.ApplicationProperties;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public static final String MAIN_MIGRATION_FILE_PATH = "classpath:/db/migration/main/";
    public static final String SAMPLE_MIGRATION_FILE_PATH = "classpath:/db/migration/sample/";
    public static final String TENANT_MIGRATION_FILE_PATH = "classpath:/db/migration/%s/";
    public static final String STATEWIDE_MIGRATION_FILE_PATH = "classpath:/db/migration/statewide/";
    public static final String PUBLIC_SCHEMA = "public";


    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ConfigurableEnvironment environment;

    @Bean
    @DependsOn("dataSource")
    public Flyway flyway(DataSource dataSource, @Qualifier("cities") List<String> cities) {
        if (applicationProperties.dbMigrationEnabled()) {
            boolean devMode = applicationProperties.devMode();
            cities.parallelStream().forEach(schema -> {
                if (devMode)
                    migrateDatabase(dataSource, schema, MAIN_MIGRATION_FILE_PATH, SAMPLE_MIGRATION_FILE_PATH,
                            format(TENANT_MIGRATION_FILE_PATH, schema));
                else
                    migrateDatabase(dataSource, schema, MAIN_MIGRATION_FILE_PATH,
                            format(TENANT_MIGRATION_FILE_PATH, schema));
            });

            if (applicationProperties.statewideMigrationRequired() && !devMode)
                migrateDatabase(dataSource, PUBLIC_SCHEMA, MAIN_MIGRATION_FILE_PATH, STATEWIDE_MIGRATION_FILE_PATH,
                        format(TENANT_MIGRATION_FILE_PATH, PUBLIC_SCHEMA));
            else if (!devMode)
                migrateDatabase(dataSource, PUBLIC_SCHEMA, MAIN_MIGRATION_FILE_PATH,
                        format(TENANT_MIGRATION_FILE_PATH, PUBLIC_SCHEMA));
        }

        return new Flyway();
    }

    private void migrateDatabase(DataSource dataSource, String schema, String... locations) {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setValidateOnMigrate(applicationProperties.flywayValidateonMigrate());
        flyway.setOutOfOrder(true);
        flyway.setLocations(locations);
        flyway.setDataSource(dataSource);
        flyway.setSchemas(schema);
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
