/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/**
 * Created on Jul 01, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class AccountEntity extends AbstractTask {
    private static final Logger LOGGER = Logger.getLogger(AccountEntity.class);
    private Connection connection;
    private ResultSet resultset;
    private DataCollection dc;
    private TaskFailedException taskExc;

    /* this method is called by Exility */
    @Override
    public void execute(final String taskName,
            final String gridName,
            final DataCollection dc,
            final Connection conn,
            final boolean erroOrNoData,
            final boolean gridHasColumnHeading, final String prefix) throws TaskFailedException {

        connection = conn;
        taskExc = new TaskFailedException();
        this.dc = dc;
        final String accDetailTypeId = dc.getValue("accDetailTypeId");
        if (accDetailTypeId != null && !accDetailTypeId.equalsIgnoreCase("")) {
            final String accDetailKey = dc.getValue("accDetailKey");
            final String filter = " AND accountDetailTypeId=" + accDetailTypeId + " AND accountDetailKey=" + accDetailKey;
            getData(filter);
            return;
        }
        final String accEntityId = getAccEntityID(dc.getValue("glCodeId"));
        if (dc.getValue("setOrUpdate").equalsIgnoreCase("update") || dc.getValue("setOrUpdate").equalsIgnoreCase("set"))
            if (!getData("")) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("ERROR AccountEntity: get data for update");
                dc.addMessage("eGovFailure", "get data for update");
                throw taskExc;
            }
        // }
        if (!accEntityId.equalsIgnoreCase("")) {
            final String tableName = getTableName(accEntityId);
            String query = "";
            if (tableName.equalsIgnoreCase("accountentitymaster"))
                query = "SELECT id, name FROM ? WHERE detailTypeId = ? ORDER BY name";
            else
                query = "SELECT id, name FROM ? ORDER BY name";
            dc.addValue("accEntityId", accEntityId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("query " + query);
            if (!fillDC(query, tableName, accEntityId)) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("ERROR AccountEntity->fillDC()");
                dc.addMessage("eGovFailure", "getting accEntity Failed : AccountEntity->fillDC()");
                throw taskExc;
            }
        }
    }

    private boolean getData(final String filter) {
        boolean status = false;
        final String fundId = dc.getValue("fundId");
        final String fundSourceId = dc.getValue("fundSourceId");
        final String fyId = dc.getValue("fyId");
        final String glCodeId = dc.getValue("glCodeId");
        final String departmentId = dc.getValue("deptId");
        String query = "";
        PreparedStatement pstmt = null;
        try {
            if (fundSourceId.trim().length() == 0) {
                query = "SELECT id, openingDebitBalance AS \"dr\", openingCreditBalance AS \"cr\" , accountdetailkey AS \"accDetailKey\" , narration as \"txnNarration\" "
                        +
                        "FROM transactionSummary WHERE glCodeId=? AND fundId= ? AND fundSourceId is null AND financialYearId= ? and departmentId=?";
                if (!filter.equalsIgnoreCase(""))
                    query = query + filter;
                pstmt = connection.prepareStatement(query);
                pstmt.setString(0, glCodeId);
                pstmt.setString(1, fundId);
                pstmt.setString(2, fyId);
                pstmt.setString(3, departmentId);
            }
            else {
                query = "SELECT id, openingDebitBalance AS \"dr\", openingCreditBalance AS \"cr\" , accountdetailkey AS \"accDetailKey\" , narration as \"txnNarration\" "
                        +
                        "FROM transactionSummary WHERE glCodeId= ? AND fundId=? AND fundSourceId=? AND financialYearId=? and departmentId=?";
                if (!filter.equalsIgnoreCase(""))
                    query = query + filter;
                pstmt = connection.prepareStatement(query);
                pstmt.setString(0, glCodeId);
                pstmt.setString(1, fundId);
                pstmt.setString(2, fundSourceId);
                pstmt.setString(3, fyId);
                pstmt.setString(4, departmentId);
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Execute get data ::::" + query);
            if (!filter.equalsIgnoreCase(""))
                query = query + filter;

            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                dc.addValue("txnId", resultset.getString("id"));
                dc.addValue("drAmt", resultset.getString("dr"));
                dc.addValue("crAmt", resultset.getString("cr"));
                dc.addValue("accEnt", resultset.getString("accDetailKey"));
                dc.addValue("txnNarration", resultset.getString("txnNarration"));
            }
            resultset.close();
            status = true;
        } catch (final SQLException ex) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("ERROR AccountEntity->getAccEntityID()" + ex, ex);
            dc.addMessage("eGovFailure", "getting accEntityId Failed");
        }
        return status;
    }

    private String getAccEntityID(final String glCodeId) {
        String accEntityId = "";
        PreparedStatement pstmt = null;
        final String dtlQry = "SELECT detailTypeId FROM chartOfAccountDetail WHERE glCodeId=?";
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("SELECT detailTypeId FROM chartOfAccountDetail WHERE glCodeId=" + glCodeId);
            pstmt = connection.prepareStatement(dtlQry);
            pstmt.setString(0, glCodeId);
            resultset = pstmt.executeQuery();
            if (resultset.next())
                accEntityId = resultset.getString(1);
            resultset.close();
            pstmt.close();
        } catch (final SQLException ex) {
            LOGGER.error("ERROR AccountEntity->getAccEntityID()", ex);
            dc.addMessage("eGovFailure", "getting accEntityId Failed");
        }
        return accEntityId;
    }

    private boolean fillDC(final String query, final String tablName, final String accEntityId) throws TaskFailedException {
        String data = "";
        PreparedStatement pstmt = null;
        boolean status = false;
        try {
            if (tablName.equalsIgnoreCase("accountentitymaster")) {
                pstmt = connection.prepareStatement(query);
                pstmt.setString(0, tablName);
                pstmt.setString(1, accEntityId);
                resultset = pstmt.executeQuery();
            }
            else {
                pstmt = connection.prepareStatement(query);
                pstmt.setString(0, tablName);
                resultset = pstmt.executeQuery();
            }
            while (resultset.next())
                data = data.concat(resultset.getString("id").concat(",".concat(resultset.getString("name").concat(";"))));
            resultset.close();
        } catch (final SQLException ex) {
            LOGGER.error("ERROR AccountEntity->fillDC()" + ex.toString(), ex);
            status = false;
        }
        final String rows[] = data.split(";");
        final String gridData[][] = new String[rows.length + 1][2];
        gridData[0][0] = "0";
        gridData[0][1] = "";
        for (int i = 1; i <= rows.length; i++)
            gridData[i] = rows[i - 1].split(",");
        dc.addGrid("accEntityList", gridData);
        status = true;
        return status;
    }

    private String getTableName(final String accEntityId) throws TaskFailedException {
        String tableName = "";
        PreparedStatement pstmt = null;
        final String tabQry = "SELECT tableName FROM accountdetailtype WHERE id = ?";
        try {
            pstmt = connection.prepareStatement(tabQry);
            pstmt.setString(0, accEntityId);
            resultset = pstmt.executeQuery();
            if (resultset.next())
                tableName = resultset.getString("tableName");
            resultset.close();
            pstmt.close();
        } catch (final SQLException ex) {
            LOGGER.error("ERROR AccountEntity->getTableName()", ex);
            dc.addMessage("eGovFailure", "getting accEntity Failed");
            throw taskExc;
        }
        return tableName;
    }
}
