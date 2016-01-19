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
 * Created on Apr 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.domain.FinancialYear;
import com.exilant.eGov.src.domain.FiscalPeriod;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author rashmi.mahale
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class FiscalPeriodMod extends AbstractTask {
    private static final Logger LOGGER = Logger
            .getLogger(FiscalPeriodMod.class);
    private DataCollection dc;
    int fid;
    String status;

    @Override
    public void execute(final String taskName, final String gridName,
            final DataCollection dataCollection, final Connection conn,
            final boolean errorOnNoData, final boolean gridHasColumnHeading, final String prefix)
                    throws TaskFailedException {

        dc = dataCollection;

        try {
            checkFinancialYear();
        } catch (final Exception e) {
            LOGGER.error("Exp=" + e.getMessage(), e);
            dc.addMessage("userFailure", " Modification Failure");
            throw new TaskFailedException(e);
        }
        try {
            postInFinancialYear();
            postInFiscalPeriod();
            dc.addMessage("eGovSuccess", "Fiscal Year");
        } catch (final Exception ex) {
            LOGGER.error("Error : " + ex.toString(), ex);
            dc.addMessage("userFailure", " Modification Failure");
            throw new TaskFailedException(ex);
        }

    }

    private void checkFinancialYear() throws Exception {
        // FinancialYear FY = new FinancialYear();

        List<Object[]> resultSet = null;
        final String finId = dc.getValue("financialYear_ID");
        // String fid=dc.getValue("financialYear_ID");
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("fid : " +fid);
        final String query = "select fiscalperiodid from voucherheader where fiscalperiodid in (select fp.id from fiscalperiod fp,financialyear fy where fy.id=fp.financialyearid and fy.id=?)";
        final Query pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
        pstmt.setString(0, finId);
        resultSet = pstmt.list();
        for (final Object[] element : resultSet) {
            dc.addMessage("userFailure", " Cannot Modify this financial year "
                    + dc.getValue("financialYear_financialYear")
                    + " as records are there for this year.");
            throw new Exception();
        }
    }

    private void postInFinancialYear() throws SQLException, TaskFailedException {
        final FinancialYear FY = new FinancialYear();
        final String open = dc.getValue("isActiveForPosting");
        FY.setId(dc.getValue("financialYear_ID"));
        FY.setFinancialYear(dc.getValue("financialYear_financialYear"));
        try {
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("inside try catch");
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date dt = new Date();
            String financialYear_startingDate = dc
                    .getValue("financialYear_startingDate");
            dt = sdf.parse(financialYear_startingDate);
            financialYear_startingDate = formatter.format(dt);

            String financialYear_endingDate = dc
                    .getValue("financialYear_endingDate");
            dt = sdf.parse(financialYear_endingDate);
            financialYear_endingDate = formatter.format(dt);

            FY.setStartingDate(financialYear_startingDate);
            FY.setEndingDate(financialYear_endingDate);

        } catch (final Exception e) {
            LOGGER.error("Error in postInFinancialYear: " + e.toString(), e);
            throw new TaskFailedException(e.getMessage());
        }
        // FY.setStartingDate(dc.getValue("financialYear_startingDate"));
        // FY.setEndingDate(dc.getValue("financialYear_endingDate"));
        FY.setModifiedBy(dc.getValue("egUser_id"));
        if (open != null && open.length() > 0)
            FY.setIsActiveForPosting(open);
        FY.update();
        fid = FY.getId();
    }

    @Transactional
    private void postInFiscalPeriod() throws SQLException, TaskFailedException {

        Query pstmt = null;
        final FiscalPeriod FP = new FiscalPeriod();
        status = dc.getValue("fiscalPeriod");
        FP.setFinancialYearId(fid + "");
        final String fGrid[][] = dc.getGrid("fiscalPeriodGrid");
        final String delQuery = "delete from fiscalPeriod where id= ?";
        for (final String[] element : fGrid)
            if (element[1].equalsIgnoreCase("")) {
                final String fisGrid = element[0];
                pstmt = HibernateUtil.getCurrentSession().createSQLQuery(delQuery);
                pstmt.setString(0, fisGrid);
                pstmt.executeUpdate();
            } else {
                FP.setName(element[1]);
                try {
                    // if(LOGGER.isDebugEnabled()) LOGGER.debug("inside try catch");
                    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMM-yyyy");
                    Date dt = new Date();
                    String FiscalPeriod_startingDate = element[2];
                    dt = sdf.parse(FiscalPeriod_startingDate);
                    FiscalPeriod_startingDate = formatter.format(dt);

                    String FiscalPeriod_endingDate = element[3];
                    dt = sdf.parse(FiscalPeriod_endingDate);
                    FiscalPeriod_endingDate = formatter.format(dt);

                    FP.setStartingDate(FiscalPeriod_startingDate);
                    FP.setEndingDate(FiscalPeriod_endingDate);
                } catch (final Exception e) {
                    LOGGER
                    .error("Error in postInFiscalPeriod: "
                            + e.toString());
                    throw new TaskFailedException(e.getMessage());
                }
                // FP.setStartingDate(fGrid[i][2]);
                // FP.setEndingDate(fGrid[i][3]);
                FP.setModifiedBy(dc.getValue("egUser_id"));
                if (element[0].equals(""))
                    FP.insert();
                else {
                    FP.setId(element[0]);
                    FP.update();
                }

            }
    } // end of post
}// end of class
