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
package com.exilant.exility.service;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.dataservice.SQLTask;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class ListService implements ExilServiceInterface {
    private static final Logger LOGGER = Logger.getLogger(ListService.class);
    private static ListService singletonInstance;

    public static ListService getService() {
        if (singletonInstance == null)
            singletonInstance = new ListService();
        return singletonInstance;
    }

    private ListService() {
        super();
    }

    @Override
    public void doService(final DataCollection dc)
    {
        String[] services = dc.getValueList("serviceID");
        if (null == services || services.length == 0)
        {
            final String serviceID = dc.getValue("serviceID");
            if (null == serviceID || serviceID.length() == 0)
            {
                dc.addMessage("exilNoServiceID");
                return;
            }
            services = new String[1];
            services[0] = serviceID;
        }
        Connection con = null;
        final SQLTask task = SQLTask.getTask();

        try
        {
            con = null;// This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
            for (int i = 0; i < services.length; i++)
            {
                // con.setReadOnly(true);
                task.execute(services[i], services[i], dc, con, false, false, "");
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" tasks executed in " + i + " for service id = ????????  " + services[i]);
            }
        } catch (final Exception e)
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exp=" + e.getMessage());
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("DataBase error in ListService"));
            dc.addMessage("exilDBError", e.getMessage());
        }

        /*
         * try { //con.close(); // DBHandler.returnConnection(con,null); } catch (Exception e) {}
         */

    }

    public static void main(final String[] args)
    {
        final ListService ls = new ListService();
        final DataCollection dc = new DataCollection();
        dc.addValue("serviceID", "SQLDefinition1");
        // dc.addValue("empno","100");
        // dc.addValue("doj","08-dec-04");
        ls.doService(dc);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("ReSULT :"+res);

    }
}
