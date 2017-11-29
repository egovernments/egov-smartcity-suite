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

/*
 * PageMaps is the Class that programmers use in their JSP to get
 * an instance of a PageMap for the page/service they want to handle.
 *
 * It is a static class. getPageMap(mapName) returns an instance of PageMap for the page
 *
 * PageMaps caches in all the maps that is loaded. Even if a map is not found,
 *  it caches that fact, so that it need not try to load it again.
 *
 * getpageMap(mapName, refresh) would force a refresh. This may be required during testing stage
 */

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.XMLLoader;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

/**
 * @author raghu.bhandi, Exilant Consulting
 *
 */
public class PageMaps {
    private static final Logger LOGGER = Logger.getLogger(PageMaps.class);
    private static HashMap pageMaps;

    public static PageMap getPageMap(final String pageName) {
        if (pageMaps == null)
            pageMaps = new HashMap();
        PageMap pm;
        Object obj = null;

        obj = pageMaps.get(pageName);
        if (obj == null) {
            pm = new PageMap();
            final XMLLoader xl = new XMLLoader();
            String fileName = "config/resource/page/" + pageName + ".xml";
            final URL url = EGovConfig.class.getClassLoader().getResource("config/resource/page/" + pageName + ".xml");
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("url in pagemaps=================="+url);
            if (url != null)
                fileName = url.getFile();
            final File file = new File(fileName);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("file name in pagemaps === " + fileName);
            if (file.isFile()) {
                xl.load(fileName, pm);
                pageMaps.put(pageName, pm);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("added toi pagemaps === ");
            }
        } else
            pm = (PageMap) obj;

        return pm;
    }

    private PageMaps() {
        super();
    }

    public static void main(final String[] args) {

        final DataCollection dc = new DataCollection();
        dc.addValue("field1", "field1Value");
        dc.addValue("field2", "field2Value");
        dc.addValue("field3", "field3Value");
        dc.addValue("field4", "field4Value");
        dc.addValue("ServerNameForField2", "SomeValue");
        dc.addValue("entity_field1", 12345);
        dc.addValue("entity_field2", true);
        dc.addValue("entity_field3", "entity_field3Value");
        dc.addValue("entity_field4", "entity_field4Value");
        final String[][] grid1 = { { "column1", "column2", "column3", "column4" }
        , { "row1Value1", "row1Value2", "row1Value3", "row1Value4" }
        , { "row2Value1", "row2Value2", "row2Value3", "row2Value4" }
        , { "row3Value1", "row3Value2", "row3Value3", "row3Value4" }
        , { "row4Value1", "row4Value2", "row4Value3", "row4Value4" }
        };

        final String[][] grid2 = { { "row1Value1", "row1Value2", "row1Value3", "row1Value4" }
        , { "row2Value1", "row2Value2", "row2Value3", "row2Value4" }
        };
        dc.addGrid("grid1", grid1);
        dc.addGrid("grid2", grid2);
        PageMap pm = PageMaps.getPageMap("supplierDataIn");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(pm.toJavaScript(dc));
        pm = PageMaps.getPageMap("supplierDataOut");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(pm.toJavaScript(dc));
        pm = PageMaps.getPageMap("junk");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(pm.toJavaScript(dc));

    }
}
