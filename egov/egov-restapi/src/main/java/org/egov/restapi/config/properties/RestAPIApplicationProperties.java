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
package org.egov.restapi.config.properties;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = {
        "classpath:config/restapi-config.properties",
        "classpath:config/egov-erp-${user.name}.properties",
        "classpath:config/application-config-${client.id}.properties",
        "classpath:config/egov-erp-override.properties" }, ignoreResourceNotFound = true)
public class RestAPIApplicationProperties {

    @Autowired
    private Environment environment;

    public List<String> aponlineIPAddress() {
        return environment.getProperty("aponline.ipaddress") == null ? emptyList()
                : Arrays.asList(environment.getProperty("aponline.ipaddress").split(","));
    }

    public List<String> esevaIPAddress() {
        return environment.getProperty("eseva.ipaddress") == null ? emptyList()
                : Arrays.asList(environment.getProperty("eseva.ipaddress").split(","));
    }

    public List<String> softtechIPAddress() {
        return environment.getProperty("softtech.ipaddress") == null ? emptyList()
                : Arrays.asList(environment.getProperty("softtech.ipaddress").split(","));
    }

    public List<String> cardIPAddress() {
        return environment.getProperty("card.ipaddress") == null
                ? emptyList() : Arrays.asList(environment.getProperty("card.ipaddress").split(","));
    }

    public List<String> leadwinnerIPAddress() {
        return environment.getProperty("leadwinner.ipaddress") == null ? emptyList()
                : Arrays.asList(environment.getProperty("leadwinner.ipaddress").split(","));
    }
}
