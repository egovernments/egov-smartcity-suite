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

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

import java.sql.Connection;

public class BulkTask extends AbstractTask
{
    static private BulkTask singletonInstance;

    private final String BULK_ACTION = "bulkAction";
    private final String UPDATE_ACTION = "update";
    private final String INSERT_ACTION = "insert";
    private final String DELETE_ACTION = "delete";
    private final String NONE_ACTION = "none";

    public static BulkTask getTask()
    {
        if (singletonInstance == null)
            singletonInstance = new BulkTask();
        return singletonInstance;
    }

    private BulkTask()
    {
        super();
    }

    @Override
    public void execute(final String tableName,
            final String gridName,
            final DataCollection dc,
            final Connection con,
            final boolean errorOnNoData,
            final boolean gridHasColumnHeading, final String prefix) throws TaskFailedException
    {
        final TableDefinition tableDefinition = Tables.getTable(tableName);
        final DataUpdater dataUpdater = DataUpdater.getUpdater();
        String sql = "";
        final String nameInDC = tableDefinition.name + "_" + BULK_ACTION;

        if (dc.hasName(nameInDC)) // single row insert/update case
        {
            final String action = dc.getValue(nameInDC);

            if (action == NONE_ACTION)
                return; // no work

            if (action == INSERT_ACTION)
                sql = tableDefinition.getInsertSQL(dc);
            else if (action == UPDATE_ACTION)
                sql = tableDefinition.getUpdateSQL(dc);
            else if (action == DELETE_ACTION)
                sql = tableDefinition.getDeleteSQL(dc);
            else
            {
                dc.addMessage("exilInvalidBulkAction", tableName, action);
                throw new TaskFailedException();
            }

            dataUpdater.update(sql, con, dc, errorOnNoData);
            return;
        }

        if (dc.hasList(nameInDC)) // multiple row.. Bulk insert/update
        {
            final String[] actions = dc.getValueList(nameInDC);
            String action;

            for (int i = 0; i < actions.length; i++)
            {
                action = actions[i];
                if (action == NONE_ACTION)
                    continue; // no work

                if (action == INSERT_ACTION)
                    sql = tableDefinition.getInsertSQL(dc, i);
                else if (action == UPDATE_ACTION)
                    sql = tableDefinition.getUpdateSQL(dc, i);
                else if (action == DELETE_ACTION)
                    sql = tableDefinition.getDeleteSQL(dc, i);
                else
                {
                    dc.addMessage("exilInvalidBulkAction", tableName, action);
                    throw new TaskFailedException();
                }

                dataUpdater.update(sql, con, dc, errorOnNoData);
            }
            return;
        }
        // possibly multiple rows sent in dc as valueList

        dataUpdater.update(sql, con, dc, errorOnNoData);

    }

    public static void main(final String[] args)
    {
    }
}
