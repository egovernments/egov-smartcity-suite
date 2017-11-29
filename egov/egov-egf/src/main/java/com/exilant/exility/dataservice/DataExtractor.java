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
package com.exilant.exility.dataservice;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilityParameters;
import com.exilant.exility.common.ObjectGetSetter;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * TODO : 1. RS shpuld not go forward and back. We should use ArrayList and convert to Array. 2. set the right property of rs/con
 * for optimized read-forward-only operation 3. Date format: How is it taken care of? Is that taken care of in writing SQLs?
 *
 * Executes a SQL statement and extracs the results into a dataCollection This is a singleton
 */

public class DataExtractor {
    private static final Logger LOGGER = Logger.getLogger(DataExtractor.class);
    private static DataExtractor singletonInstance;

    // private static Connection con;

    public static DataExtractor getExtractor() {
        if (singletonInstance == null)
            singletonInstance = new DataExtractor();
        // con = DBHandler.getConnection();
        return singletonInstance;
    }

    private DataExtractor() {
        super();
    }

    public void extract(final String sql,
            final String gridName,
            final DataCollection dc,
            final Connection con,
            final boolean errorOnNoData,
            final boolean addColumnHeading,
            final String prefix) throws TaskFailedException {
        if (LOGGER.isInfoEnabled())
            LOGGER.info(sql + "sqllll");
        if (sql == null || sql.length() == 0)
            return;

        // temp code to test when database is not available
        if (ExilityParameters.logSQLs)
        {
            int i = dc.getInt("sqlCount");
            i++;
            dc.addValue("sqlCount", i);
            /*
             * changed in egf dc.addValue("sql" + i, sql);
             */
        }
        if (null == con)
            return;

        int rowCount = 0;
        int columnCount = 0;
        ResultSetMetaData metaData = null;
        String[][] dataValues = null;

        try
        {

            final PreparedStatement pststement = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = null;
            String[] columnNames = null;
            try {
                rs = pststement.executeQuery();
                metaData = rs.getMetaData();
                columnCount = metaData.getColumnCount();// Gets the Column Count in the ResultSet
                columnNames = new String[columnCount];
            } catch (final Exception e) {
                LOGGER.error("Exception while analysing Result set in extract", e);
                return;
            }
            for (int m = 0; m < columnCount; m++)
                columnNames[m] = prefix + metaData.getColumnName(m + 1);
            if (null == gridName || gridName.length() == 0)
                try {
                    if (rs.next())
                        for (int m = 0; m < columnCount; m++) {
                            String mText = rs.getString(m + 1);

                            if (mText == null)
                                mText = "";
                            if (mText.indexOf("\r") > 0) {
                                mText = mText.replaceAll("\r", " ");
                                mText = mText.replaceAll("\n", " ");
                                mText = mText.replaceAll("\f", " ");
                            }
                            dc.addValue(columnNames[m], mText);
                        }
                    else
                        return;
                    /*
                     * if(errorOnNoData){ dc.addMessage("exilNoData"); throw new TaskFailedException(); }
                     */
                } catch (final Exception e) {
                    LOGGER.error("Exception while analysing Result set in extract", e);
                    return;
                }
            else { // result to be put into a grid
                rowCount = -1;
                try {
                    rs.next();
                    rs.getString(1);
                } catch (final Exception eee) {
                    LOGGER.error("Error while analysing Result set in extract", eee);
                    rowCount = 0;
                }
                try {
                    if (rowCount == -1)
                    {
                        rs.last();
                        rowCount = rs.getRow();
                        rs.beforeFirst();				// brings back the cursor to the start in the ResultSet
                    }
                } catch (final Exception e) {
                    LOGGER.error("Error while analysing Result set in extract", e);
                    rowCount = 0;
                }
                /*
                 * if (rowCount == 0 && errorOnNoData) { dc.addMessage("exilNoData"); throw new TaskFailedException(); }
                 */
                int rowid;
                if (addColumnHeading) {
                    dataValues = new String[rowCount + 1][columnCount]; // additional row for column headings
                    for (int k = 0; k < columnCount; k++)
                        dataValues[0][k] = columnNames[k];
                    rowid = 1;
                } else {
                    dataValues = new String[rowCount][columnCount];
                    rowid = 0;
                }
                try {
                    while (rs.next()) {
                        for (int j = 0; j < columnCount; j++) {// executes number of column times
                            String mText = rs.getString(j + 1);

                            if (mText == null)
                                mText = "";
                            dataValues[rowid][j] = mText;
                        }
                        rowid++; // Row wise increment;
                    }
                } catch (final Exception e) {
                    LOGGER.error("Inside extract", e);
                    try {
                        if (gridName != null && dataValues != null && addColumnHeading)
                            dc.addGrid(gridName, dataValues);

                    } catch (final Exception ee) {
                        LOGGER.error("Error while adding grid in extract", ee);
                    }
                    return;
                }
                dc.addGrid(gridName, dataValues);
            }
            rs.close();
            pststement.close();
        } catch (final SQLException e) {
            dc.addMessage("exilSQLException", e.getMessage());
            LOGGER.error(e.getMessage(), e);
            throw new TaskFailedException();
        }
        return;
    }

    public HashMap extractIntoMap(final String sql,
            final String keyName,
            final Class collectionMemberClass) throws TaskFailedException {
        // DBHandler handler = DBHandler.getHandler();
        /* Connection con = handler.getConnection(); */
        final Connection con = null;// This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
        final HashMap map = extractIntoMap(sql, con, keyName, collectionMemberClass);
        // This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(con,null);
        // handler.returnConnection(con);
        return map;
    }

    public HashMap extractIntoMap(final String sql,
            final Connection con,
            final String keyName,
            final Class collectionMemberClass) throws TaskFailedException {

        final HashMap map = new HashMap();
        int columnCount = 0;
        ResultSetMetaData metaData = null;

        try
        {
            final PreparedStatement pststement = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            final ResultSet rs = pststement.executeQuery();
            metaData = rs.getMetaData();
            columnCount = metaData.getColumnCount();// Gets the Column Count in the ResultSet
            // int keyIndex = rs.findColumn(keyName);
            String val = null;
            while (rs.next())
            {
                final Object object = collectionMemberClass.newInstance();
                for (int m = 0; m < columnCount; m++) {
                    String mText = rs.getString(m + 1);
                    if (mText == null)
                        mText = "";
                    if (metaData.getColumnName(m + 1).equalsIgnoreCase(keyName))
                        val = mText;
                    ObjectGetSetter.set(object, metaData.getColumnName(m + 1), mText);
                }
                map.put(val, object);
            }

            // rs.close();
            // ststement.close();
        } catch (final SQLException e) {
            LOGGER.error("Inside extractIntoMap", e);
            // throw new TaskFailedException("sql field " + sql);
            return new HashMap();
        } catch (final InstantiationException e) {
            LOGGER.error("Inside extractIntoMap", e);
            throw new TaskFailedException("Object could not be instantiated for " + collectionMemberClass.getName());
        } catch (final IllegalAccessException e) {
            LOGGER.error("Inside extractIntoMap", e);
            throw new TaskFailedException("Object could not be instantiated due to access issues "
                    + collectionMemberClass.getName());
        }
        return map;
    }
}
