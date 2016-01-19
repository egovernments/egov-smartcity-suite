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
/*
 * Created on Apr 19, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.PostingAllowed;
import com.exilant.eGov.src.domain.TransactionSummary;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class CloseBalance extends AbstractTask {
    private static final Logger LOGGER = Logger.getLogger(CloseBalance.class);
    private Connection connection;
    private ResultSet resultset;
    private PreparedStatement pstmt = null;

    public CloseBalance() {
    }

    @Override
    public void execute(final String taskName, final String gridName, final DataCollection dc,
            final Connection conn, final boolean erroOrNoData,
            final boolean gridHasColumnHeading, final String prefix)
                    throws TaskFailedException {
        connection = conn;
        try {
            final ArrayList transSummaryData = getData(dc);
            postInTransactionSummary(transSummaryData, dc);

            dc.addMessage("eGovSuccess", "Closing Balance");
        } catch (final SQLException sqlex) {
            dc.addMessage("exilRPError", "Closing Balance : "
                    + sqlex.toString());
            LOGGER.error("Error : " + sqlex.toString(), sqlex);
            throw new TaskFailedException();
        }
    }

    private ArrayList getData(final DataCollection dc) throws SQLException {
        final ArrayList arrList = new ArrayList();

        final String financialYearId = dc.getValue("financialYear_id");
        final String fiscalPeriodId = dc.getValue("fiscalPeriod_id");
        final String glCode = dc.getValue("chartOfAccounts_glCode");

        // if(LOGGER.isDebugEnabled()) LOGGER.debug("financialYearId : " + financialYearId);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("fiscalPeriodId : " + fiscalPeriodId);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("glCode : " + glCode);

        final String selectQuery = "SELECT case when gl.glCode = gl.glCode then gl.glCodeId end  AS glCodeId, "
                + "case when fp.name =  fp.name then fp.id end AS fiscalPeriodId,sum(gl.debitAmount) AS OpeningDebitBalance, sum(gl.creditAmount) AS OpeningCreditBalance, "
                + "0 AS debitAmount, 0 AS creditAmount,case when adt.name = adt.name  then adt.id end AS accountDetailTypeId, "
                + "adk.detailKey AS accountDetailKey FROM generalLedger gl, voucherDetail vd, voucherHeader vh, "
                + "fiscalPeriod fp, financialYear fy,	accountDetailType adt, accountDetailKey  adk "
                + "WHERE gl.glCode= ? AND fy.id= ? AND fp.id= ? AND gl.voucherLineId = vd.id AND vd.voucherHeaderId = vh.id "
                + "AND vh.fiscalPeriodId = fp.id AND fp.financialYearId = fy.id AND adk.detailTypeId = adt.id AND adk.glCodeId = gl.glCodeId	"
                + "GROUP BY case when gl.glCode = gl.glCode then gl.glCodeId end, case when fp.name = fp.name then fp.id end, case when adt.name = adt.name then adt.id, adk.detailKey";

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(selectQuery);
        pstmt = connection.prepareStatement(selectQuery);
        pstmt.setString(0, glCode);
        pstmt.setString(1, financialYearId);
        pstmt.setString(2, fiscalPeriodId);
        resultset = pstmt.executeQuery();

        /*
         * statement = connection.createStatement(); resultset = statement.executeQuery(selectQuery);
         */
        final ResultSetMetaData rsmd = resultset.getMetaData();
        final int cols = rsmd.getColumnCount();

        while (resultset.next()) {
            final String record[] = new String[cols];
            for (int i = 1; i <= cols; i++)
                record[i - 1] = resultset.getString(i);

            arrList.add(record);
        }
        resultset.close();

        return arrList;
    }

    private void postInTransactionSummary(final ArrayList arrList, final DataCollection dc)
            throws TaskFailedException, SQLException {

        // if(LOGGER.isDebugEnabled()) LOGGER.debug("postInTransactionSummary");
        final PostingAllowed pa = new PostingAllowed();
        pstmt = connection
                .prepareStatement("SELECT id FROM chartOfAccounts WHERE glCode=?");
        pstmt.setString(0, dc.getValue("chartOfAccounts_glCode"));
        resultset = pstmt.executeQuery();
        /* resultset = statement.executeQuery(); */
        resultset.next();
        final int glCodeId = resultset.getInt("id");
        resultset.close();
        final String fiscalPeriodId = dc.getValue("fiscalPeriod_id");

        // if(LOGGER.isDebugEnabled()) LOGGER.debug("check for balance already closed");
        final String sqlString = "SELECT * FROM transactionSummary WHERE glCodeId=? AND fiscalPeriodId= ?";
        pstmt = connection.prepareStatement(sqlString);
        pstmt.setInt(0, glCodeId);
        pstmt.setString(1, fiscalPeriodId);
        resultset = pstmt.executeQuery();
        /* resultset = statement.executeQuery(); */
        if (resultset.next()) {
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("yes, it is closed");
            dc.addMessage("eGovFailure",
                    "Balance already closed for Account : "
                            + dc.getValue("chartOfAccounts_glCode")
                            + " and Fiscal Period Id: " + fiscalPeriodId);
            throw new TaskFailedException();
        } else {
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("not closed, closing now");
            resultset.close();
            // updating Posting Allowed
            pa.setFiscalPeriodId(fiscalPeriodId);
            pa.setGLCodeId(glCodeId + "");
            pa.setPostingAllowed("0");
            pstmt = connection.prepareStatement("SELECT * FROM postingAllowed");
            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                resultset.close();
                pstmt.close();
                pa.update();
            } else {
                resultset.close();
                pstmt.close();
                pa.insert();
            }

            pstmt = connection
                    .prepareStatement("SELECT * FROM postingAllowed FOR UPDATE");
            resultset = pstmt.executeQuery();
            resultset.close();
            pstmt.close();

            final TransactionSummary ts = new TransactionSummary();
            final int recNum = arrList.size();
            String record[];
            for (int i = 0; i < recNum; i++) {
                record = (String[]) arrList.get(i);
                ts.setGLCodeId(record[0]);
                ts.setFinancialYearId(record[1]);
                ts.setOpeningDebitBalance(record[2]);
                ts.setOpeningCreditBalance(record[3]);
                ts.setDebitAmount(record[4]);
                ts.setCreditAmount(record[5]);
                ts.setAccountDetailTypeId(record[6]);
                ts.setAccountDetailKey(record[7]);
                ts.insert();
            }

            final PostingAllowed pa1 = new PostingAllowed();
            pa1.setPostingAllowed("1");
            pa1.update();
        }
    }
}
