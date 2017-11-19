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
package com.exilant.exility.updateservice;

import com.exilant.exility.common.XMLLoader;
import org.egov.infstr.utils.EGovConfig;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class Tables
{
    private static HashMap tableDefinitions;

    public static final String SURROGATE_KEY_NAME = "ID";
    public static final String MODIFIED_TIMESTAMP_NAME = "lastModified";
    public static final String MODIFIED_USER_NAME = "modifiedBy";
    public static final String CREATED_USER_NAME = "";
    public static final String CRAETED_TIMESTAMP_NAME = "created";
    public static final String ACTIVE_NAME = "isActive";

    // it first checks in cache. Else loads into the cache before returning the iinstance
    public static TableDefinition getTable(final String tableName) {

        if (tableDefinitions == null)
            tableDefinitions = new HashMap();
        TableDefinition definition;
        Object obj = null;

        obj = tableDefinitions.get(tableName);
        if (obj == null) {
            definition = new TableDefinition();
            final XMLLoader xl = new XMLLoader();
            String fileName = "config/resource/table/" + tableName + ".xml";
            final URL url = EGovConfig.class.getClassLoader().getResource("config/resource/table/" + tableName + ".xml");
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("url in tables=================="+url);
            if (url != null)
                fileName = url.getFile();
            final File file = new File(fileName);
            if (file.isFile()) {
                xl.load(fileName, definition);
                definition.optimize();
                tableDefinitions.put(tableName, definition);
            }
        } else
            definition = (TableDefinition) obj;

        return definition;

    }

    private Tables()
    {
        super();
    }
}
