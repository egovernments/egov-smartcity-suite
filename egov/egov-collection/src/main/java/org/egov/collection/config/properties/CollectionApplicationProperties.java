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
package org.egov.collection.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(name = "collectionApplicationProperties", value = {
        "classpath:config/application-config-collection.properties",
        "classpath:config/payment-gateway.properties",
        "classpath:config/egov-erp-${user.name}.properties",
        "classpath:config/application-config-${client.id}.properties",
        "classpath:config/egov-erp-override.properties",
        "classpath:config/collection-override-${env}.properties" }, ignoreResourceNotFound = true)
public class CollectionApplicationProperties {

    @Autowired
    private Environment environment;

    public String axisTransactionMessage() {
        return environment.getProperty("AXIS.transactionmessage");
    }

    public String axisVersion() {
        return environment.getProperty("axis.version", String.class);
    }

    public String axisCommand() {
        return environment.getProperty("axis.command");
    }

    public String axisCommandQuery() {
        return environment.getProperty("axis.command.query");
    }

    public String axisAccessCode() {
        return environment.getProperty("axis.access.code");
    }

    public String axisMerchant() {
        return environment.getProperty("axis.merchant");
    }

    public String axisLocale() {
        return environment.getProperty("axis.locale");
    }

    public String axisOperator() {
        return environment.getProperty("axis.operator.id");
    }

    public String axisPassword() {
        return environment.getProperty("axis.password");
    }

    public String axisReconcileUrl() {
        return environment.getProperty("axis.reconcile.url");
    }

    public String axisSecureSecret() {
        return environment.getProperty("axis.secure.secret");
    }

    public String getEmailSubject() {
        return environment.getProperty("email.subject.message");
    }

    public String getEmailBody() {
        return environment.getProperty("email.body.message");
    }

    public String getValue(final String key) {
        return environment.getProperty(key);
    }

    public String sbimopsDdocode(final String cityCode) {
        return environment.getProperty(cityCode.concat(".sbimops.ddocode"));
    }

    public String sbimopsServiceCode(final String serviceCode) {
        return environment.getProperty(serviceCode.concat(".sbimops.serviceCode"));
    }

    public String sbimopsDepartmentcode(final String key) {
        return environment.getProperty(key);
    }

    public String sbimopsHoa(final String cityCode) {
        return environment.getProperty(cityCode.concat(".sbimops.hoa"));
    }

    public String sbimopsHoa(final String cityCode, final String serviceCode) {
        return environment.getProperty(cityCode.concat(serviceCode).concat(".sbimops.hoa"));
    }

    public String sbimopsReconcileUrl() {
        return environment.getProperty("sbimops.reconcile.url");
    }

    public String sbimopsReconcileUsername() {
        return environment.getProperty("sbimops.reconcile.username");
    }

    public String sbimopsReconcilePassword() {
        return environment.getProperty("sbimops.reconcile.password");
    }

    public String sbimopsTransactionMessage() {
        return environment.getProperty("SBIMOPS.transactionmessage");
    }

    public String getUpdateDemandUrl(final String serviceCode) {
        return environment.getProperty(serviceCode.concat(".updatedemand.url"));
    }

    public String getLamsServiceUrl() {
        return environment.getProperty("egov.services.lams.hostname");
    }

    public String atomProdid() {
        return environment.getProperty("atom.prodid", String.class);
    }

    public String atomPass() {
        return environment.getProperty("atom.pass", String.class);
    }

    public String atomTtype() {
        return environment.getProperty("atom.ttype", String.class);
    }

    public String atomClientcode() {
        return environment.getProperty("atom.clientcode", String.class);
    }

    public String atomCustacc() {
        return environment.getProperty("atom.custacc", String.class);
    }

    public String atomMdd() {
        return environment.getProperty("atom.mdd", String.class);
    }

    public String atomTxncurr() {
        return environment.getProperty("atom.txncurr", String.class);
    }

    public String atomLogin() {
        return environment.getProperty("atom.login", String.class);
    }

    public String atomTxnscamt() {
        return environment.getProperty("atom.txnscamt", String.class);
    }

    public String atomTransactionMessage() {
        return environment.getProperty("ATOM.transactionmessage");
    }

    public String atomReconcileUrl() {
        return environment.getProperty("atom.reconcile.url");
    }

    public String mobilePaymentServiceCode() {
        return environment.getProperty("mobile.paymentservice.code");
    }
}