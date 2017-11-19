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

//import com.exilant.exility.dataservice.*;

/**
 * @author raghu.bhandi
 *
 * Defines an entity in terms of its attributes, and their validations
 *
 * IMPORTANT : it is assuemd that the DC contains columns with name = tablename_columnname
 */
public class TableDefinition {
    private static final Logger LOGGER = Logger.getLogger(TableDefinition.class);
    public String name;
    boolean hasSurrogateKey = true;
    boolean hasCreatedTimestamp = true;
    boolean hasModifiedTimestamp = true;
    boolean hasCreatedUser = false;
    boolean hasModifiedUser = true;
    boolean hasActiveField = true;
    public String keyColumnName; // applicable if usesSurrogateKey is false
    public boolean keyRequiresQuote = false;
    public ColumnDefinition[] columnDefinitions;
    public boolean okToDelete = false;

    private final String USER_ID_NAME = "current_UserID";
    private final String COMMA = ",";
    private final String QUOTE = "'";
    private final char CONNECTOR = '_';
    private final char EQUAL = '=';
    private final String now = "CURRENT_DATE";

    public TableDefinition()
    {
        super();
    }

    public String getUpdateSQL(final DataCollection dc) throws TaskFailedException
    {
        return getUpdateSQL(dc, -1); // rowid = -1 implies there are no lists. look invalues and not in valueLists
    }

    public String getDeleteSQL(final DataCollection dc) throws TaskFailedException
    {
        return getDeleteSQL(dc, -1);
    }

    public String getDeleteSQL(final DataCollection dc, final int rowid) throws TaskFailedException
    {
        if (!okToDelete)
        {
            dc.addMessage("exilDeleteNotAllowed", name);
            throw new TaskFailedException();
        }

        final String nameInDC = name + CONNECTOR + keyColumnName;
        String keyValue = null;
        if (rowid == -1)
            keyValue = dc.getValue(nameInDC);
        else
        {
            final String[] keyValues = dc.getValueList(nameInDC);
            if (null != keyValues && rowid < keyValues.length)
                keyValue = keyValues[rowid];
        }

        if (null == keyValue || keyValue.length() == 0 || keyValue.equals("0"))
        {
            dc.addMessage("exilNoKeyForUpdate", name, keyColumnName, name + CONNECTOR + keyColumnName);
            throw new TaskFailedException();
        }

        final StringBuffer sbf = new StringBuffer();
        sbf.append("DELETE ").append(name).append(" WHERE ").append(keyColumnName).append("=");
        if (keyRequiresQuote)
            sbf.append(QUOTE).append(keyValue).append(QUOTE);
        else
            sbf.append(keyValue);

        return sbf.toString();
    }

    public String getUpdateSQL(final DataCollection dc, final int rowid) throws TaskFailedException
    {
        if (columnDefinitions == null)
        {
            dc.addMessage("exilNoColumns", name);
            throw new TaskFailedException();
        }

        final StringBuffer sbf = new StringBuffer("UPDATE ").append(name).append(" SET ");

        boolean valid = true; // set to false inthe for-loop if any field fails validation
        String columnValue = null;
        String nameInDC;
        ColumnDefinition column;
        int nbrColumns = 0; // actual number of columns added to the sql String in the loop
        String prefix = ""; // nothing in the beginning, but then, it will be changed to COMMA
        String[] valueList;

        for (final ColumnDefinition columnDefinition : columnDefinitions) {
            column = columnDefinition;
            nameInDC = name + CONNECTOR + column.name;
            if (rowid >= 0)
            {
                valueList = dc.getValueList(nameInDC);
                if (null != valueList && valueList.length > rowid)
                    columnValue = valueList[rowid];
            } else {
                if (!dc.hasName(nameInDC))
                    continue;
                columnValue = dc.getValue(nameInDC);
            }
            // if (null == columnValue || columnValue.length() == 0) continue; //no value? No problem... yet.
            /*
             * if (!column.isValid(columnValue)) { valid = false; dc.addMessage("exilInvalidField", nameInDC, columnValue); //
             * don't throw exception .. yet. Let us continue to validate all field values continue; }
             */
            nbrColumns++;
            sbf.append(prefix).append(column.name).append(EQUAL);
            if (null == columnValue || columnValue.length() == 0)
                sbf.append("null");
            else if (column.requiresQuote)
                sbf.append(QUOTE).append(columnValue.replaceAll(QUOTE, "''")).append(QUOTE);
            else
                sbf.append(columnValue);
            prefix = COMMA;
        } // end of loop for each column

        if (nbrColumns == 0)
        {
            valid = false;
            dc.addMessage("exilNothingToUpdate", name);
        }

        if (!valid)
            throw new TaskFailedException();

        // append modified user and timestamp
        appendStandardUpdateValues(sbf, dc, rowid);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("update sql in getupdate SQL >>>>>>>>>>>>>>" + sbf.toString());
        return sbf.toString();
    }

    // Method which forms insert query.
    public String getInsertSQL(final DataCollection dc) throws TaskFailedException
    {
        return getInsertSQL(dc, -1); // rowid = -1 implies take from values, and not valueList
    }

    public String getInsertSQL(final DataCollection dc, final int rowid) throws TaskFailedException
    {

        if (columnDefinitions == null || columnDefinitions.length == 0)
        {
            dc.addMessage("exilNoColumns", name);
            throw new TaskFailedException();
        }
        final StringBuffer sbfNames = new StringBuffer(); // buffer for adding the column names
        sbfNames.append("INSERT INTO ").append(name).append('(');

        final StringBuffer sbfValues = new StringBuffer(); // buffer for adding the column values
        sbfValues.append(" VALUES ").append('(');

        final boolean valid = true;
        String columnValue = null;
        String nameInDC;
        ColumnDefinition column;
        int nbrColumns = 0;
        String prefix = "";
        String[] valueList;

        for (int i = 0; i < columnDefinitions.length; i++)
        {
            column = columnDefinitions[i];
            nameInDC = name + CONNECTOR + column.name;
            if (rowid >= 0)
            {
                valueList = dc.getValueList(nameInDC);
                if (null != valueList && valueList.length > rowid)
                    columnValue = valueList[rowid];
            }
            else
                columnValue = dc.getValue(nameInDC);

            if (null == columnValue || columnValue.length() == 0)
                columnValue = column.defaultValue;
            /*
             * if (!column.isValid(columnValue)) { valid = false; dc.addMessage("exilInvalidField", nameInDC, columnValue); //
             * dont throw exception... yet. Let us validate all fields before doing that continue; }
             */
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("column =========== " + i);
            nbrColumns++;
            sbfNames.append(prefix).append(column.name);
            sbfValues.append(prefix);

            if (column.requiresQuote)
                sbfValues.append(QUOTE).append(columnValue.replaceAll(QUOTE, "''")).append(QUOTE);
            else
                sbfValues.append(columnValue);
            prefix = COMMA;
        }
        if (!valid)
            throw new TaskFailedException();

        if (nbrColumns == 0)
        {
            dc.addMessage("exilNothingToInsert", name);
            throw new TaskFailedException();
        }

        appendStandardInsertNames(sbfNames);
        appendStandardInsertValues(sbfValues, dc);
        sbfNames.append(')');
        sbfValues.append(')');

        return sbfNames.append(sbfValues).toString();
    }

    private StringBuffer appendStandardInsertNames(final StringBuffer sbfNames)
    {
        if (hasSurrogateKey)
            sbfNames.append(COMMA).append(Tables.SURROGATE_KEY_NAME);

        if (hasCreatedTimestamp)
            sbfNames.append(COMMA).append(Tables.CRAETED_TIMESTAMP_NAME);

        if (hasCreatedUser)
            sbfNames.append(COMMA).append(Tables.CREATED_USER_NAME);

        if (hasModifiedTimestamp)
            sbfNames.append(COMMA).append(Tables.MODIFIED_TIMESTAMP_NAME);

        if (hasModifiedUser)
            sbfNames.append(COMMA).append(Tables.MODIFIED_USER_NAME);

        if (hasActiveField)
            sbfNames.append(COMMA).append(Tables.ACTIVE_NAME);

        return sbfNames;
    }

    private StringBuffer appendStandardInsertValues(final StringBuffer sbfValues, final DataCollection dc)
    {
        // SimpleDateFormat dateFormat = new SimpleDateFormat(FULL_DATETIME_FORMAT);

        // String now = QUOTE + dateFormat.format(new Date()) + QUOTE;

        final String user = dc.getValue(USER_ID_NAME);

        if (hasSurrogateKey)
            sbfValues.append(COMMA).append(PrimaryKeyGenerator.getNextKey(name));

        if (hasCreatedTimestamp)
            sbfValues.append(COMMA).append(now);

        if (hasCreatedUser)
            sbfValues.append(COMMA).append(user);

        if (hasModifiedTimestamp)
            sbfValues.append(COMMA).append(now);

        if (hasModifiedUser)
            sbfValues.append(COMMA).append(user);

        if (hasActiveField)
            sbfValues.append(COMMA).append(1);
        return sbfValues;
    }

    private StringBuffer appendStandardUpdateValues(final StringBuffer sbf, final DataCollection dc, final int rowid)
            throws TaskFailedException
    {
        String key = null;
        final String nameInDC = name + CONNECTOR + keyColumnName;

        if (rowid < 0)
            key = dc.getValue(nameInDC);
        else
        {
            final String[] keys = dc.getValueList(nameInDC);
            if (null != keys && keys.length > rowid)
                key = keys[rowid];
        }

        if (key == null || key.length() == 0 || key.equals("0"))
        {
            dc.addMessage("exilNoKeyForUpdate", name, keyColumnName, name + CONNECTOR + keyColumnName);
            throw new TaskFailedException();
        }

        // SimpleDateFormat dateFormat = new SimpleDateFormat(FULL_DATETIME_FORMAT);
        // String now = QUOTE + dateFormat.format(new Date()) + QUOTE;
        final String user = dc.getValue(USER_ID_NAME);

        if (hasModifiedTimestamp)
            sbf.append(COMMA).append(Tables.MODIFIED_TIMESTAMP_NAME).append(EQUAL).append(now);

        if (hasModifiedUser)
            sbf.append(COMMA).append(Tables.MODIFIED_USER_NAME).append(EQUAL).append(user);

        if (hasActiveField)
        {
            final String fieldName = name + CONNECTOR + Tables.ACTIVE_NAME;
            if (dc.hasName(fieldName))
                sbf.append(COMMA).append(Tables.ACTIVE_NAME).append(EQUAL).append(dc.getValue(fieldName));
            else
                sbf.append(COMMA).append(Tables.ACTIVE_NAME).append(EQUAL).append('0');
        }

        sbf.append(" WHERE ").append(keyColumnName).append(EQUAL);
        if (keyRequiresQuote)
            sbf.append(QUOTE).append(key).append(QUOTE);
        else
            sbf.append(key);

        if (hasModifiedTimestamp)
        {
            String stamp = null;
            final String stampName = name + CONNECTOR + Tables.MODIFIED_TIMESTAMP_NAME;
            if (rowid < 0)
                stamp = dc.getValue(stampName);
            else
            {
                final String[] stamps = dc.getValueList(stampName);
                if (null != stamps && stamps.length > rowid)
                    stamp = stamps[rowid];
            }

            if (stamp == null || stamp.length() == 0 || stamp.equals("0"))
            {
                dc.addMessage("exilNoTimeStamp", name, Tables.MODIFIED_TIMESTAMP_NAME, name + CONNECTOR
                        + Tables.MODIFIED_TIMESTAMP_NAME);
                throw new TaskFailedException();
            }
            /*
             * used when we used to connect using dsn
             * stamp="to_date("+QUOTE+stamp+QUOTE+COMMA+QUOTE+"yyyy-mm-dd HH24:MI:SS"+QUOTE+")";
             */
            stamp = "to_date(" + QUOTE + stamp + QUOTE + COMMA + QUOTE + "mm/dd/yyyy HH24:MI:SS" + QUOTE + ")";
            sbf.append(" AND ").append(Tables.MODIFIED_TIMESTAMP_NAME).append(EQUAL);
            // sbf.append(QUOTE).append(stamp).append(QUOTE);
            sbf.append(stamp);
        }
        return sbf;
    }

    // in the XML, dataType is specified as a String (like signedDecimal et..)
    // However, DataType is more effecient with the numeric code for that.
    // Let us do this translation once and for all...
    public void optimize()
    {
        final int length = columnDefinitions.length;
        for (int i = 0; i < length; i++)
            columnDefinitions[i].optimize();
    }
}
