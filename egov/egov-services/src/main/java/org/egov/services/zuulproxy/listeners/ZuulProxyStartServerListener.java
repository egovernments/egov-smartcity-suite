/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.services.zuulproxy.listeners;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.egov.services.zuulproxy.filter.ZuulProxyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.filters.FilterRegistry;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import com.netflix.zuul.monitoring.MonitoringHelper;

public class ZuulProxyStartServerListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(ZuulProxyStartServerListener.class);

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        if (logger.isInfoEnabled())
            logger.info("Starting Zuul Proxy server");

        // mocks monitoring infrastructure as we don't need it for this simple app
        MonitoringHelper.initMocks();

        // initializes groovy filesystem poller
        initGroovyFilterManager();

        // initializes a few java filter examples
        initJavaFilters();
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        if (logger.isInfoEnabled())
            logger.info("Stopping Zuul Proxy server");
    }

    private void initGroovyFilterManager() {
        FilterLoader.getInstance().setCompiler(new GroovyCompiler());
        String scriptRoot = System.getProperty("zuul.filter.root", "groovy/filters");
        /*
         * String scriptRoot = System.getProperty("zuul.filter.root",
         * getClass().getClassLoader().getResource("/groovy/filters").getPath()); if (scriptRoot.length() > 0 &&
         * !scriptRoot.endsWith("/")) scriptRoot = scriptRoot + File.separator;
         */
        if (scriptRoot.length() > 0 && !scriptRoot.endsWith("/"))
            scriptRoot = scriptRoot + File.separator;
        try {
            FilterFileManager.setFilenameFilter(new GroovyFileFilter());
            FilterFileManager.init(5, scriptRoot + "route", scriptRoot + "post");
        } catch (final Exception e) {
            logger.error("Error reading Groovy filters for zuul proxy... " + e.getMessage());
            // throw new RuntimeException(e);
        }
    }

    private void initJavaFilters() {
        final FilterRegistry r = FilterRegistry.instance();
        r.put("javaPreFilter", new ZuulProxyFilter());
    }

}