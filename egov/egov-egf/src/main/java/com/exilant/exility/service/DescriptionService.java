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

public class DescriptionService implements ExilServiceInterface
{
    // Static instance of the class to follow Singleton pattern
    static DescriptionService singletonInstance;
    private static final Logger LOGGER = Logger.getLogger(DescriptionService.class);
    private static final String SERVICE_ID = "serviceID";
    private static final String KEY_VALUE = "keyValue";

    public static DescriptionService getService() {
        if (singletonInstance == null)
            singletonInstance = new DescriptionService();
        return singletonInstance;
    }

    // Singleton pattern
    private DescriptionService()
    {
        super();
    }

    @Override
    public void doService(final DataCollection dc)
    {
        String[] serviceID = dc.getValueList("serviceID");
        String[] keyValue = dc.getValueList("keyValue");
        if (dc.hasList(SERVICE_ID))
            serviceID = dc.getValueList(SERVICE_ID);
        else if (dc.hasName(SERVICE_ID))
        {
            serviceID = new String[1];
            serviceID[0] = dc.getValue(SERVICE_ID);
        }
        else
        {
            dc.addMessage("exilNoServiceID");
            return;
        }

        if (dc.hasList(KEY_VALUE))
            keyValue = dc.getValueList(KEY_VALUE);
        else if (dc.hasName(KEY_VALUE))
        {
            keyValue = new String[1];
            keyValue[0] = dc.getValue(KEY_VALUE);
        }
        else
        {
            dc.addMessage("exilNoKeyValue");
            return;
        }
        final int kount = serviceID.length;
        if (keyValue.length != kount)
        {
            dc.addMessage("exilKeyValueMismatch", " " + kount, " " + keyValue.length);
            return;
        }
        Connection con = null;
        con = null;// This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
        // con.setReadOnly(true);
        final SQLTask task = SQLTask.getTask();
        try
        {
            for (int i = 0; i < kount; i++)
            {
                dc.addValue(KEY_VALUE, keyValue[i]);
                task.execute(serviceID[i], serviceID[i] + "_" + keyValue[i], dc, con, false, false, "");
            }

        } catch (final Exception e)
        {
            LOGGER.error("SQLTask failed " + e.getMessage(), e);
            dc.addMessage("exilDBError", e.getMessage());
        }
    }
    /*
     * public static void main(String[] args) { // String[] arr = {"id1"}; // String[] a = {"p1001"}; // DescriptionService ds =
     * DescriptionService.getInstance(); // if(LOGGER.isDebugEnabled()) LOGGER.debug(ds.get(arr,a)); }
     */
}
