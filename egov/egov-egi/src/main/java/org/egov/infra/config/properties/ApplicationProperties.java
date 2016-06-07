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

package org.egov.infra.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Configuration
@PropertySource(value = {
        "classpath:config/application-config.properties",
        "classpath:config/egov-erp-${user.name}.properties",
        "classpath:config/application-config-${client.id}.properties",
        "classpath:config/egov-erp-override.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {

    @Autowired
    private Environment environment;

    public String fileStoreBaseDir() {
        return this.environment.getProperty("filestore.base.dir");
    }

    public String filestoreServiceBeanName() {
        return this.environment.getProperty("filestoreservice.beanname");
    }

    public String defaultDatePattern() {
        return this.environment.getProperty("default.date.pattern");
    }

    public Integer mailPort() {
        return this.environment.getProperty("mail.port", Integer.class);
    }

    public String mailHost() {
        return this.environment.getProperty("mail.host");
    }

    public String mailProtocol() {
        return this.environment.getProperty("mail.protocol");
    }

    public String mailSenderUsername() {
        return this.environment.getProperty("mail.sender.username");
    }

    public String mailSenderPassword() {
        return this.environment.getProperty("mail.sender.password");
    }

    public String mailSMTPSAuth() {
        return this.environment.getProperty("mail.smtps.auth");
    }

    public String mailStartTLSEnabled() {
        return this.environment.getProperty("mail.smtps.starttls.enable");
    }

    public String mailSMTPSDebug() {
        return this.environment.getProperty("mail.smtps.debug");
    }

    public String smsProviderURL() {
        return this.environment.getProperty("sms.provider.url");
    }

    public String smsSenderUsername() {
        return this.environment.getProperty("sms.sender.username");
    }

    public String smsSenderPassword() {
        return this.environment.getProperty("sms.sender.password");
    }

    public String smsSender() {
        return this.environment.getProperty("sms.sender");
    }

    public String[] commonMessageFiles() {
        return this.environment.getProperty("common.properties.files").split(",");
    }

    public boolean devMode() {
        return this.environment.getProperty("dev.mode", Boolean.class);
    }

    public boolean emailEnabled() {
        return this.environment.getProperty("mail.enabled", Boolean.class);
    }

    public boolean smsEnabled() {
        return this.environment.getProperty("sms.enabled", Boolean.class);
    }

    public Integer userPasswordExpiryInDays() {
        return this.environment.getProperty("user.pwd.expiry.days", Integer.class);
    }

    public List<String> smsErrorCodes() {
        return Arrays.asList(this.environment.getProperty("sms.error.codes").split(","));
    }

    public String smsResponseMessageForCode(String errorCode) {
        return this.environment.getProperty(errorCode, "No Message");
    }

    public boolean multiTenancyEnabled() {
        return this.environment.getProperty("multitenancy.enabled", Boolean.class);
    }

    public String getProperty(String propKey) {
        return this.environment.getProperty(propKey, EMPTY);
    }

    public <T> T getProperty(String propKey, Class<T> type) {
        return this.environment.getProperty(propKey, type);
    }

    public String appVersion() {
        return this.environment.getProperty("app.version", EMPTY);
    }

    public String appBuildNo() {
        return this.environment.getProperty("app.build.no", EMPTY);
    }

    public String appCoreBuildNo() {
        return this.environment.getProperty("app.core.build.no", EMPTY);
    }

    public String issueReportingUrl() {
        return this.environment.getProperty("issue.report.url", EMPTY);
    }

    public List<String> portalEnabledFeatures() {
        return Arrays.asList(this.environment.getProperty("portal.feature.enabled").split(","));
    }

    public boolean statewideMigrationRequired() {
        return this.environment.getProperty("statewide.migration.required", Boolean.class, Boolean.FALSE);
    }

    public Integer getBatchUpdateSize() {
        return this.environment.getProperty("hibernate.jdbc.batch_size", Integer.class);
    }

    public String passwordStrength() {
        return this.environment.getProperty("user.pwd.strength");
    }

    public boolean sentinelEnabled() {
        return this.environment.getProperty("redis.enable.sentinel", Boolean.class);
    }

    public List<String> sentinelHosts() {
        return Arrays.asList(this.environment.getProperty("redis.sentinel.hosts").split(","));
    }

    public String sentinelMasterName() {
        return this.environment.getProperty("redis.sentinel.master.name");
    }

    public boolean usingEmbeddedRedis() {
        return this.environment.getProperty("redis.enable.embedded", Boolean.class);
    }

    public String redisHost() {
        return this.environment.getProperty("redis.host.name");
    }

    public int redisPort() {
        return this.environment.getProperty("redis.host.port", Integer.class);
    }
}
