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
package com.exilant.exility.pagemanager;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.common.XMLGenerator;
import com.exilant.exility.service.DataService;
import com.exilant.exility.service.DescriptionService;
import com.exilant.exility.service.ListService;
import com.exilant.exility.service.TreeService;
import com.exilant.exility.service.UpdateService;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * @author raghu.bhandi, Exilant Consulting
 *
 * Common class to locate and execute a service. We can configure this to either look-up for EJB, or use local class Idea is that
 * the JSP's need not assume remote. They will work either in a full J2EE architecture, or just with Tomcat JVM.
 *
 * This is the only class that needs to be configured to adapt Exility between remote/local systems
 *
 */
public class ServiceAgent {
    private static final Logger LOGGER = Logger.getLogger(ServiceAgent.class);
    private static ServiceAgent agent;
    private HashMap services;

    /**
     *
     */
    public static ServiceAgent getAgent() {
        if (agent == null) {
            agent = new ServiceAgent();
            agent.getInitialHashMap();
        }
        return agent;
    }

    private ServiceAgent() {
    }

    public void deliverService(final String serviceName, final DataCollection dc) {
        final Object service = services.get(serviceName);
        if (service == null)
            dc.addMessage("exilNoServiceName", serviceName);
        else
            try {
                ((ExilServiceInterface) service).doService(dc);

            } catch (final Exception e) {
                LOGGER.error("exilServerError" + e.getMessage());
                dc.addMessage("exilServerError", e.getMessage());
            }
    }

    private void getInitialHashMap() {
        final HashMap classes = new HashMap();
        classes.put("DataService", DataService.getService());
        classes.put("UpdateService", UpdateService.getService());
        classes.put("DescriptionService", DescriptionService.getService());
        classes.put("ListService", ListService.getService());
        classes.put("TreeService", TreeService.getService());
        services = classes;
    }

    public static void main(final String[] args) {
        final DataCollection dc = new DataCollection();
        dc.addValue("serviceID", "getCustomerData");
        dc.addValue("nameStartingWith", "startOfName");
        dc.addValue("codeFrom", "codeStart");
        dc.addValue("codeTo", "codeEnd");
        final ServiceAgent anAgent = ServiceAgent.getAgent();
        anAgent.deliverService("ListService", dc);
        final XMLGenerator generator = XMLGenerator.getInstance();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(generator.toXML(dc, "", ""));

    }
}
