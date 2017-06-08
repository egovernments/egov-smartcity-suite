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

package org.egov.infra.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.egov.infra.config.core.GlobalSettings.DEFAULT_DATE_PATTERN_KEY;

@Configuration
@PropertySource(value = {
        "classpath:config/application-config.properties",
        "classpath:config/egov-erp-${user.name}.properties",
        "classpath:config/application-config-${client.id}.properties",
        "classpath:config/egov-erp-override.properties"}, ignoreResourceNotFound = true)
public class ApplicationProperties {

    private static final String FILESTORE_BASE_DIR = "filestore.base.dir";
    private static final String FILESTORESERVICE_BEANNAME = "filestoreservice.beanname";
    private static final String MAIL_PORT = "mail.port";
    private static final String MAIL_HOST = "mail.host";
    private static final String MAIL_PROTOCOL = "mail.protocol";
    private static final String MAIL_SENDER_USERNAME = "mail.sender.username";
    private static final String MAIL_SENDER_PASWRD = "mail.sender.password";
    private static final String MAIL_SMTPS_AUTH = "mail.smtps.auth";
    private static final String MAIL_SMTPS_STARTTLS_ENABLE = "mail.smtps.starttls.enable";
    private static final String MAIL_SMTPS_DEBUG = "mail.smtps.debug";
    private static final String SMS_PROVIDER_URL = "sms.provider.url";
    private static final String SMS_SENDER_USERNAME = "sms.sender.username";
    private static final String SMS_SENDER_PASWRD = "sms.sender.password";
    private static final String SMS_SENDER = "sms.sender";
    private static final String SMS_PRIORITY_ENABLED = "sms.priority.enabled";
    private static final String COMMON_PROPERTIES_FILES = "common.properties.files";
    private static final String DEV_MODE = "dev.mode";
    private static final String MAIL_ENABLED = "mail.enabled";
    private static final String SMS_ENABLED = "sms.enabled";
    private static final String USER_PASWRD_EXPIRY_DAYS = "user.pwd.expiry.days";
    private static final String SMS_ERROR_CODES = "sms.error.codes";
    private static final String MULTITENANCY_ENABLED = "multitenancy.enabled";
    private static final String APP_VERSION = "app.version";
    private static final String APP_BUILD_NO = "app.build.no";
    private static final String APP_RELEASE_NO = "%s_%s";
    private static final String APP_CORE_BUILD_NO = "app.core.build.no";
    private static final String ISSUE_REPORT_URL = "issue.report.url";
    private static final String PORTAL_FEATURE_ENABLED = "portal.feature.enabled";
    private static final String STATEWIDE_MIGRATION_REQUIRED = "statewide.migration.required";
    private static final String HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String USER_PASWRD_STRENGTH = "user.pwd.strength";
    private static final String REDIS_ENABLE_SENTINEL = "redis.enable.sentinel";
    private static final String REDIS_SENTINEL_HOSTS = "redis.sentinel.hosts";
    private static final String REDIS_SENTINEL_MASTER_NAME = "redis.sentinel.master.name";
    private static final String REDIS_ENABLE_EMBEDDED = "redis.enable.embedded";
    private static final String REDIS_HOST_NAME = "redis.host.name";
    private static final String REDIS_HOST_PORT = "redis.host.port";
    private static final String MASTER_SERVER = "master.server";
    private static final String DB_MIGRATION_ENABLED = "db.migration.enabled";
    private static final String FLYWAY_VALIDATEON_MIGRATE = "db.flyway.validateon.migrate";
    private static final String FLYWAY_MIGRATION_REPAIR = "db.flyway.migration.repair";
    private static final String SEARCH_HOSTS = "elasticsearch.hosts";
    private static final String SEARCH_PORT = "elasticsearch.port";
    private static final String SEARCH_CLUSTER_NAME = "elasticsearch.cluster.name";
    private static final String CDN_URL = "cdn.domain.url";
    private static final String SERVICES_USER_CREATE_URL = "egov.services.user.create.url";
    private static final String STATE_WIDE_SCHEMA_NAME = "statewide.schema.name";
    private static final String SERVICES_WORKFLOW_URL = "egov.services.workflow.url";
    private static final String EMPLOYEE_PORTAL_ACCESS_ROLE = "employee.portal.access.role";

    @Autowired
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer applicationPropertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public String fileStoreBaseDir() {
        return this.environment.getProperty(FILESTORE_BASE_DIR);
    }

    public String filestoreServiceBeanName() {
        return this.environment.getProperty(FILESTORESERVICE_BEANNAME);
    }

    public String defaultDatePattern() {
        return this.environment.getProperty(DEFAULT_DATE_PATTERN_KEY);
    }

    public Integer mailPort() {
        return this.environment.getProperty(MAIL_PORT, Integer.class);
    }

    public String mailHost() {
        return this.environment.getProperty(MAIL_HOST);
    }

    public String mailProtocol() {
        return this.environment.getProperty(MAIL_PROTOCOL);
    }

    public String mailSenderUsername() {
        return this.environment.getProperty(MAIL_SENDER_USERNAME);
    }

    public String mailSenderPassword() {
        return this.environment.getProperty(MAIL_SENDER_PASWRD);
    }

    public String mailSMTPSAuth() {
        return this.environment.getProperty(MAIL_SMTPS_AUTH);
    }

    public String mailStartTLSEnabled() {
        return this.environment.getProperty(MAIL_SMTPS_STARTTLS_ENABLE);
    }

    public String mailSMTPSDebug() {
        return this.environment.getProperty(MAIL_SMTPS_DEBUG);
    }

    public String smsProviderURL() {
        return this.environment.getProperty(SMS_PROVIDER_URL);
    }

    public String smsSenderUsername() {
        return this.environment.getProperty(SMS_SENDER_USERNAME);
    }

    public String smsSenderPassword() {
        return this.environment.getProperty(SMS_SENDER_PASWRD);
    }

    public String smsSender() {
        return this.environment.getProperty(SMS_SENDER);
    }

    public boolean smsPriorityEnabled() {
        return this.environment.getProperty(SMS_PRIORITY_ENABLED, Boolean.class);
    }

    public String[] commonMessageFiles() {
        return this.environment.getProperty(COMMON_PROPERTIES_FILES).split(",");
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

    public List<String> smsErrorCodes() {
        return Arrays.asList(this.environment.getProperty(SMS_ERROR_CODES).split(","));
    }

    public String smsResponseMessageForCode(String errorCode) {
        return this.environment.getProperty(errorCode, "No Message");
    }

    public boolean multiTenancyEnabled() {
        return this.environment.getProperty(MULTITENANCY_ENABLED, Boolean.class);
    }

    public String getProperty(String propKey) {
        return this.environment.getProperty(propKey, EMPTY);
    }

    public <T> T getProperty(String propKey, Class<T> type) {
        return this.environment.getProperty(propKey, type);
    }

    public String appVersion() {
        return this.environment.getProperty(APP_VERSION, EMPTY);
    }

    public String appBuildNo() {
        return this.environment.getProperty(APP_BUILD_NO, EMPTY);
    }

    public String appCoreBuildNo() {
        return this.environment.getProperty(APP_CORE_BUILD_NO, EMPTY);
    }

    public String issueReportingUrl() {
        return this.environment.getProperty(ISSUE_REPORT_URL, EMPTY);
    }

    public List<String> portalEnabledFeatures() {
        return Arrays.asList(this.environment.getProperty(PORTAL_FEATURE_ENABLED).split(","));
    }

    public boolean statewideMigrationRequired() {
        return this.environment.getProperty(STATEWIDE_MIGRATION_REQUIRED, Boolean.class, Boolean.FALSE);
    }

    public Integer getBatchUpdateSize() {
        return this.environment.getProperty(HIBERNATE_JDBC_BATCH_SIZE, Integer.class);
    }

    public String passwordStrength() {
        return this.environment.getProperty(USER_PASWRD_STRENGTH);
    }

    public boolean sentinelEnabled() {
        return this.environment.getProperty(REDIS_ENABLE_SENTINEL, Boolean.class);
    }

    public List<String> sentinelHosts() {
        return Arrays.asList(this.environment.getProperty(REDIS_SENTINEL_HOSTS).split(","));
    }

    public String sentinelMasterName() {
        return this.environment.getProperty(REDIS_SENTINEL_MASTER_NAME);
    }

    public boolean usingEmbeddedRedis() {
        return this.environment.getProperty(REDIS_ENABLE_EMBEDDED, Boolean.class);
    }

    public String redisHost() {
        return this.environment.getProperty(REDIS_HOST_NAME);
    }

    public int redisPort() {
        return this.environment.getProperty(REDIS_HOST_PORT, Integer.class);
    }

    public boolean isMasterServer() {
        return this.environment.getProperty(MASTER_SERVER, Boolean.class);
    }

    public boolean dbMigrationEnabled() {
        return this.environment.getProperty(DB_MIGRATION_ENABLED, Boolean.class);
    }

    public boolean flywayValidateonMigrate() {
        return this.environment.getProperty(FLYWAY_VALIDATEON_MIGRATE, Boolean.class);
    }

    public boolean flywayRepair() {
        return this.environment.getProperty(FLYWAY_MIGRATION_REPAIR, Boolean.class);
    }

    public List<String> searchHosts() {
        return Arrays.asList(environment.getProperty(SEARCH_HOSTS).split(","));
    }

    public int searchPort() {
        return Integer.parseInt(environment.getProperty(SEARCH_PORT));
    }

    public String searchClusterName() {
        return environment.getProperty(SEARCH_CLUSTER_NAME);
    }

    public String applicationReleaseNo() {
        return format(APP_RELEASE_NO, appVersion(), appBuildNo());
    }

    public String cdnURL() {
        return environment.getProperty(CDN_URL, EMPTY);
    }

    public String getCreateUserServiceUrl() {
        return environment.getProperty(SERVICES_USER_CREATE_URL);
    }

    public String statewideSchemaName() {
        return environment.getProperty(STATE_WIDE_SCHEMA_NAME);
    }

    public String getServicesWorkflowUrl() {
        return environment.getProperty(SERVICES_WORKFLOW_URL);
    }

    public String portalAccessibleRole() {
        return environment.getProperty(EMPLOYEE_PORTAL_ACCESS_ROLE, EMPTY);
    }
}