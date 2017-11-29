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

package org.egov.infra.config.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.EMPTY;


@Component
public class EnvironmentSettings {

    private static final String DEV_MODE = "dev.mode";
    private static final String MAIL_ENABLED = "mail.enabled";
    private static final String SMS_ENABLED = "sms.enabled";
    private static final String USER_PASWRD_EXPIRY_DAYS = "user.pwd.expiry.days";
    private static final String HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String STATE_WIDE_SCHEMA_NAME = "statewide.schema.name";
    private static final String DEFAULT_SCHEMA_NAME = "default.schema.name";

    @Autowired
    private Environment environment;

    public String getProperty(String propKey) {
        return this.environment.getProperty(propKey, EMPTY);
    }

    public <T> T getProperty(String propKey, Class<T> type) {
        return this.environment.getProperty(propKey, type);
    }

    public boolean devMode() {
        return this.environment.getProperty(DEV_MODE, Boolean.class);
    }

    public boolean emailEnabled() {
        return this.environment.getProperty(MAIL_ENABLED, Boolean.class);
    }

    public boolean smsEnabled() {
        return this.environment.getProperty(SMS_ENABLED, Boolean.class);
    }

    public Integer userPasswordExpiryInDays() {
        return this.environment.getProperty(USER_PASWRD_EXPIRY_DAYS, Integer.class);
    }

    public Integer getBatchUpdateSize() {
        return this.environment.getProperty(HIBERNATE_JDBC_BATCH_SIZE, Integer.class);
    }

    public String statewideSchemaName() {
        return environment.getProperty(STATE_WIDE_SCHEMA_NAME);
    }

    public String defaultSchemaName() {
        return environment.getProperty(DEFAULT_SCHEMA_NAME);
    }

    public String schemaName(String domainName) {
        return environment.getProperty("tenant." + domainName, defaultSchemaName());
    }
}