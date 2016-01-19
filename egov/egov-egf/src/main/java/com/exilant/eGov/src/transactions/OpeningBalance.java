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
 * Created on Jan 16 , 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class OpeningBalance extends AbstractTask {
    private Connection connection;
    private PreparedStatement pst;
    private ResultSet resultset;
    private DataCollection dc;
    private TaskFailedException taskExc;
    private String extraMessage;
    private static final Logger LOGGER = Logger.getLogger(OpeningBalance.class);
    EGovernCommon cm = new EGovernCommon();
    String lastModifiedDate;

    @Override
    public void execute(final String taksName,
            final String gridName,
            final DataCollection dc,
            final Connection conn,
            final boolean erroOrNoData,
            final boolean gridHasColumnHeading, final String prefix) throws TaskFailedException {
        connection = conn;
        this.dc = dc;
        taskExc = new TaskFailedException();
        // String glCod = dc.getValue("chartOfAccounts_glCode");
        final String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
        String serviceType = dc.getValue("ftService_type");

        // set lastmodified date
        lastModifiedDate = cm.getCurrentDateTime();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
            lastModifiedDate = " to_date('" + lastModifiedDate + "','dd-Mon-yyyy HH24:MI:SS')";
        } catch (final Exception e) {
            LOGGER.error("ERROR in the date formate " + e);
            throw new TaskFailedException();
        }

        for (int i = 0; i < gridOpeningBalance.length; i++) {
            if (gridOpeningBalance[i][1].equalsIgnoreCase(""))
                continue;
            try {
                final String query = "select glcode as \"glcode\",classification as \"classification\",isactiveforposting as \"isactiveforposting\",type as \"type\" from chartofaccounts where glcode = ? and type= ?";
                pst = connection.prepareStatement(query);
                pst.setString(0, gridOpeningBalance[i][1]);
                pst.setString(1, serviceType);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(query);
                resultset = pst.executeQuery();
                if (resultset.next()) {
                    if (!resultset.getString("classification").equalsIgnoreCase("4"))
                    {
                        dc.addMessage("exilError", "Account code should be a Detailed Code");
                        throw new TaskFailedException();
                    }
                    else if (!resultset.getString("isactiveforposting").equalsIgnoreCase("1"))
                    {
                        dc.addMessage("exilError", "Account code is not open for Posting");
                        throw new TaskFailedException();
                    }
                } else if (dc.getValue("ftService_type").equalsIgnoreCase("L")
                        || dc.getValue("ftService_type").equalsIgnoreCase("A"))
                {
                    if (dc.getValue("ftService_type").equalsIgnoreCase("A"))
                        serviceType = "Asset Type";
                    if (dc.getValue("ftService_type").equalsIgnoreCase("L"))
                        serviceType = "Liability Type";
                    dc.addMessage("exilError", "Account Code should be of " + serviceType);
                    throw new TaskFailedException();
                }
            } catch (final Exception e) {
                LOGGER.error("Exception while validating Glcode " + e);
                throw new TaskFailedException("Invalid Account Code");
            }

            final String mode = isOpeniningBalanceSet(conn, dc, i);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("mode:" + mode);
            if (mode.equalsIgnoreCase("set")) {
                if (!setOpeningBalance(dc, i))
                    throw taskExc;
            } else if (mode.equalsIgnoreCase("update"))
                if (!updateOpeningBalance(i))
                    throw taskExc;
        }

        dc.addMessage("eGovSuccess", "Opening Balance");
        // if(extraMessage.length()>1) dc.addMessage("eGovSuccess", extraMessage);
    }

    private boolean setOpeningBalance(final DataCollection dc, final int index) throws TaskFailedException {
        boolean status = false;
        String fundSourceId = dc.getValue("fundSourceId");

        if (fundSourceId.trim().equals(""))
            fundSourceId = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("inside setOpeningBalance-------------------");
        final String fundId = dc.getValue("fund_id");
        final String financialYearId = dc.getValue("financialYear_id");
        final String deptId = dc.getValue("dept_name");
        String detailTypeId = "0", detailKey = "0";
        String id = "", query = "";
        final String dr = "0", cr = "0";
        final String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
        extraMessage = "";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("<<<<<<<<<<<<<department object>>>>>>>" + deptId);
        if (!isFYOpen(financialYearId)) {
            dc.addMessage("exilError", "Financial Year is Closed, Opening Balance can not be updated");
            throw taskExc;
        }
        try {
            String chkEntry = "";
            final int i = index;
            gridOpeningBalance[i][3] = gridOpeningBalance[i][3].equalsIgnoreCase("") ? "0" : gridOpeningBalance[i][3];
            gridOpeningBalance[i][4] = gridOpeningBalance[i][4].equalsIgnoreCase("") ? "0" : gridOpeningBalance[i][4];
            detailTypeId = gridOpeningBalance[i][5];
            detailKey = gridOpeningBalance[i][6];

            if (detailTypeId.equalsIgnoreCase(""))
                detailTypeId = null;
            if (detailKey.equalsIgnoreCase(""))
                detailKey = null;
            String fsCond = "";
            if (fundSourceId == null)
                fsCond = " AND fundSourceId is null";
            else
                fsCond = " AND fundSourceId=" + fundSourceId;
            chkEntry = "SELECT id FROM transactionSummary WHERE glCodeId= ?" +
                    "AND fundId= ? AND financialYearId= ? " +
                    "AND accountDetailTypeId= ? AND accountDetailKey= ?" + fsCond +
                    " and DEPARTMENTID=?";

            pst = connection.prepareStatement(chkEntry);
            pst.setString(0, gridOpeningBalance[i][0]);
            pst.setString(1, fundId);
            pst.setString(2, financialYearId);
            pst.setString(3, detailTypeId);
            pst.setString(4, detailKey);
            pst.setString(5, deptId);
            resultset = null;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("chkEntry  " + chkEntry);
            resultset = pst.executeQuery();
            query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, "
                    +
                    "openingdebitbalance, openingcreditbalance, debitamount, "
                    +
                    "creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,fundsourceid,DEPARTMENTID,LASTMODIFIEDBY,LASTMODIFIEDDATE,NARRATION) "
                    +
                    "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, " + lastModifiedDate + ", ?)";
            if (LOGGER.isInfoEnabled())
                LOGGER.info("query  " + query);
            final PreparedStatement pstBatch = connection.prepareStatement(query);
            if (resultset.next())
                extraMessage = extraMessage + gridOpeningBalance[i][1] + ", ";
            else {
                /**
                 * Checking the Bank account If its an asset code then only we need to check
                 * */
                if (dc.getValue("ftService_type").equals("A"))
                {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Need to check bank accounts.");
                    final String queryFundId = "select fundid from bankaccount where glcodeid= ?";
                    pst = connection.prepareStatement(queryFundId);
                    pst.setString(0, gridOpeningBalance[i][0]);
                    final ResultSet rs = pst.executeQuery();
                    if (rs.next())
                    {
                        // Check if the funds are same
                        final String bankFund = rs.getString(1);
                        if (LOGGER.isDebugEnabled())
                            LOGGER.debug("Need to check bank accounts.");
                        if (!bankFund.equalsIgnoreCase(fundId)) {
                            dc.addMessage("exilError", "Fund associated to the Bank Account is incorrect");
                            throw new TaskFailedException("Fund associated to the Bank Account is incorrect,");
                        }
                    }
                }

                id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
                pstBatch.setString(0, id);
                pstBatch.setString(1, financialYearId);
                pstBatch.setString(2, gridOpeningBalance[i][0]);
                pstBatch.setString(3, gridOpeningBalance[i][3]);
                pstBatch.setString(4, gridOpeningBalance[i][4]);
                pstBatch.setString(5, dr);
                pstBatch.setString(6, cr);
                pstBatch.setString(7, detailTypeId);
                pstBatch.setString(8, detailKey);
                pstBatch.setString(9, fundId);
                pstBatch.setString(10, fundSourceId);
                pstBatch.setString(11, deptId);
                pstBatch.setString(12, dc.getValue("current_UserID"));
                pstBatch.setString(13, gridOpeningBalance[i][8]);
                pstBatch.addBatch();
            }
            /** if executeBatch Fails throw exception **/
            final int updateCounts[] = pstBatch.executeBatch();
            for (final int updateCount : updateCounts)
                if (updateCount == 0)
                {
                    dc.addMessage("eGovFailure", "Updating Opening Balance (Execute Batch)");
                    return false;
                }
            /** *********************************** **/

            if (extraMessage.length() > 3)
                extraMessage = "Opening Balance Already set for Account Codes: " + extraMessage;

            resultset.close();
            pst.close();
            pstBatch.close();
            status = true;
        } catch (final SQLException ex) {
            LOGGER.error("ERROR OpeningBalance: " + ex.toString());
            status = false;
        }
        return status;
    }

    private boolean updateOpeningBalance(final int index) throws TaskFailedException {
        boolean status = false;
        // String fundSourceId=dc.getValue("fundSourceId");
        // String fundId = dc.getValue("fund_id");
        final String financialYearId = dc.getValue("financialYear_id");
        // String detailTypeId = "0", detailKey = "0";;
        // String id="", query="", accEntityCondition="", dr="0", cr="0";
        final String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
        extraMessage = "";
        if (!isFYOpen(financialYearId)) {
            dc.addMessage("exilError", "Financial Year is Closed, Opening Balance can not be updated");
            throw taskExc;
        }
        try {
            String updateQuery = "";
            String glCodes = "";
            final int i = index;
            gridOpeningBalance[i][3] = gridOpeningBalance[i][3].equalsIgnoreCase("") ? "0" : gridOpeningBalance[i][3];
            gridOpeningBalance[i][4] = gridOpeningBalance[i][4].equalsIgnoreCase("") ? "0" : gridOpeningBalance[i][4];
            glCodes = glCodes + "'" + gridOpeningBalance[i][1] + "',";
            /*
             * chkEntry = "SELECT id FROM transactionSummary WHERE glCodeId="+gridOpeningBalance[i][0]+" " +
             * "AND fundId="+fundId+" AND financialYearId="+financialYearId + accEntityCondition;
             */
            updateQuery = "UPDATE transactionSummary SET openingdebitbalance= ?, " +
                    "openingcreditbalance= ?, " +
                    " LASTMODIFIEDBY= ?, " +
                    " LASTMODIFIEDDATE= " + lastModifiedDate + ", " +
                    " NARRATION= ?" +
                    " WHERE id= ?";
            final PreparedStatement pstBatch = connection.prepareStatement(updateQuery);

            if (LOGGER.isInfoEnabled())
                LOGGER.info("updateQuery  " + updateQuery);
            pstBatch.setString(0, gridOpeningBalance[i][3]);
            pstBatch.setString(1, gridOpeningBalance[i][4]);
            pstBatch.setString(2, dc.getValue("current_UserID"));
            pstBatch.setString(3, gridOpeningBalance[i][8]);
            pstBatch.setString(4, gridOpeningBalance[i][7]);
            pstBatch.addBatch();

            /** if executeBatch Fails throw exception **/
            final int updateCounts[] = pstBatch.executeBatch();
            for (final int updateCount : updateCounts)
                if (updateCount == 0)
                {
                    dc.addMessage("eGovFailure", "Update Opening Balance (Execute Batch)");
                    return false;
                }
            /** *********************************** **/
            glCodes = glCodes.substring(0, glCodes.length() - 1);
            status = true;
        } catch (final SQLException ex) {
            LOGGER.error("ERROR OpeningBalance: " + ex.toString());
            dc.addMessage("eGovFailure", "Opening Balance");
            status = false;
        }
        return status;
    }

    private boolean nextYear(final String fyId) throws TaskFailedException {
        boolean nextYear = false;
        try {
            final String query = "SELECT id FROM financialYear " +
                    "WHERE startingDate > (SELECT endingDate FROM financialYear WHERE id = ?)";
            pst = connection.prepareStatement(query);
            pst.setString(0, fyId);
            resultset = pst.executeQuery();
            if (resultset.next())
                nextYear = true;
            resultset.close();
            pst.close();
        } catch (final SQLException ex) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Error SetUp->nextYear: " + ex.toString());
        }
        return nextYear;
    }

    private FY getFinancialYear(final String fyId) {
        final FY fy = new FY();
        try {
            final String query = "SELECT to_char(startingDate, 'DD-Mon-yyyy') AS \"startingDate\", to_char(endingDate, 'DD-Mon-yyyy') AS \"endingDate\" "
                    +
                    "FROM financialYear WHERE id= ?";
            pst = connection.prepareStatement(query);
            pst.setString(0, fyId);
            resultset = pst.executeQuery();
            if (resultset.next()) {
                fy.setId(fyId);
                fy.setSDate(resultset.getString("startingDate"));
                fy.setEDate(resultset.getString("endingDate"));
            }
            resultset.close();
            pst.close();
        } catch (final SQLException ex) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Error SetUp->getFinancialYear: " + ex.toString());
        }
        return fy;
    }

    private boolean isFYOpen(final String fyId) {
        boolean isOpen = false;
        try {
            final String query = "SELECT id FROM financialYear WHERE isClosed=1 AND id= ?";
            pst = connection.prepareStatement(query);
            pst.setString(0, fyId);
            resultset = pst.executeQuery();
            if (!resultset.next())
                isOpen = true;
            resultset.close();
            pst.close();
        } catch (final SQLException ex) {
            isOpen = false;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Error SetUp->isFYOpen(): " + ex.toString());
        }
        return isOpen;
    }

    private String getNextFYId(final String fyId) {
        String nextFYId = "";
        try {
            final String query = "SELECT id FROM financialYear " +
                    "WHERE startingDate = (SELECT endingDate+1 FROM financialYear WHERE id = ?)";
            pst = connection.prepareStatement(query);
            pst.setString(0, fyId);
            resultset = pst.executeQuery();
            if (resultset.next())
                nextFYId = resultset.getString("id");
            resultset.close();
            pst.close();
        } catch (final SQLException ex) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Error SetUp->getNextFYId: " + ex.toString());
        }
        return nextFYId;
    }

    private String isOpeniningBalanceSet(final Connection con, final DataCollection dc, final Integer index)
            throws TaskFailedException
    {
        try {
            final String fundId = dc.getValue("fund_id");
            final String fyId = dc.getValue("financialYear_id");
            final String deptId = dc.getValue("dept_name");
            final String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
            final String accountDetailTypeId = gridOpeningBalance[index][5];
            final String accountDetailKey = gridOpeningBalance[index][6];
            final String glCodeId = gridOpeningBalance[index][0];
            final String fundSourceId = dc.getValue("fundSourceId");
            String fundsourceCondition = "";
            if (!(fundSourceId.equalsIgnoreCase("") || fundSourceId.equalsIgnoreCase("-1") || fundSourceId.equalsIgnoreCase("0")))
                fundsourceCondition = " AND FUNDSOURCEID=" + Integer.parseInt(fundSourceId);
            else
                fundsourceCondition = " AND FUNDSOURCEID IS NULL ";

            // check if entity key exists then check OB is set or not
            if (accountDetailTypeId != null
                    && !(accountDetailTypeId.trim().equalsIgnoreCase("") || accountDetailTypeId.equalsIgnoreCase("0")))
            {
                final String query = "select glcodeid as \"glcodeId\" from transactionsummary "
                        +
                        " where glcodeid= ? AND fundId= ? AND ACCOUNTDETAILKEY= ? AND accountdetailtypeid= ? AND financialYearId= ? "
                        + fundsourceCondition +
                        " and departmentId=?";

                pst = con.prepareStatement(query);
                pst.setString(0, glCodeId);
                pst.setString(1, fundId);
                pst.setString(2, accountDetailKey);
                pst.setString(3, accountDetailTypeId);
                pst.setString(4, fyId);
                pst.setString(5, deptId);
                if (accountDetailKey != null
                        && !(accountDetailKey.trim().equalsIgnoreCase("") || accountDetailKey.equalsIgnoreCase("0")))
                    resultset = pst.executeQuery();
            } else {
                final String sql = "select glcodeid as \"glcodeId\" from transactionsummary " +
                        " where glcodeid= ? AND fundId= ? AND financialYearId= ? " + fundsourceCondition +
                        " and departmentid=?";
                pst = con.prepareStatement(sql);
                pst.setString(0, glCodeId);
                pst.setString(1, fundId);
                pst.setString(2, fyId);
                pst.setString(3, deptId);
                resultset = pst.executeQuery();
            }

            if (resultset.next())
                return "update";
            else
                return "set";
        } catch (final SQLException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exp=" + e.getMessage());
            throw taskExc;
        }
    }

}
