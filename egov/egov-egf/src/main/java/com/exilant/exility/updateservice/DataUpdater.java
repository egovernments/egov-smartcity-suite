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

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author raghu.bhandi, Exilant Consulting
 *
 * Executes a SQL statement and extracs the results into a dataCollection This is a singleton
 */

public class DataUpdater {
    private static final Logger LOGGER = Logger.getLogger(DataUpdater.class);
    private static DataUpdater singletonInstance;

    public static DataUpdater getUpdater() {
        if (singletonInstance == null)
            singletonInstance = new DataUpdater();
        return singletonInstance;
    }

    private DataUpdater() {
        super();
    }

    public int update(final String sql, final Connection con, final DataCollection dc,
            final boolean errorOnNoUpdate) throws TaskFailedException {
        if (sql == null || sql.length() == 0)
            return 0;
        // temp code to test when database is not available
        int i = dc.getInt("sqlCount");
        i++;
        dc.addValue("sqlCount", i);
        /*
         * commented in egf dc.addValue("sql" + i, sql);
         */
        if (null == con)
            return 0;
        // end of temp patch. You should remove this patch when going to
        // production
        int noOfRowsEffected = 0;
        try {
            final Statement statement = con.createStatement();
            final PreparedStatement pstmt = con.prepareStatement(sql);
            noOfRowsEffected = pstmt.executeUpdate();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("SSSSSSSS>>>>>>>>>>>>>>>>>>>>>>>>" + sql);
            if (noOfRowsEffected == 0 && errorOnNoUpdate)
                throw new TaskFailedException();
            else
                dc.addMessage("masterInsertUpdate");
            statement.close();
        } catch (final SQLException e) {
            LOGGER.error("err Message  " + e.getErrorCode(), e);
            if (e.getErrorCode() == 1)
                dc.addMessage("exilDuplicate");
            else
                dc.addMessage("exilSQLException", e.getMessage());
            throw new TaskFailedException();
        }
        return noOfRowsEffected;
    }
}
