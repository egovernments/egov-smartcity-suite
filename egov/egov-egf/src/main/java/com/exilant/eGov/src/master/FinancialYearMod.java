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

import org.apache.log4j.Logger;

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
public class FinancialYearMod extends AbstractTask {
    private final static Logger LOGGER = Logger.getLogger(FinancialYearMod.class);
    private DataCollection dc;
    private int fid;
    private String status;

    @Override
    public void execute(final String taskName,
            final String gridName,
            final DataCollection dataCollection,
            final Connection conn,
            final boolean errorOnNoData,
            final boolean gridHasColumnHeading, final String prefix) throws TaskFailedException {

        dc = dataCollection;

        try {
            postInFinancialYear();
            postInFiscalPeriod();
            dc.addMessage("eGovSuccess", "Fiscal Year");
        } catch (final Exception ex) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Error : " + ex.toString());
            dc.addMessage("userFailure", " Insertion Failure");
            throw new TaskFailedException(ex);
        }
    }

    private void postInFinancialYear() throws SQLException, TaskFailedException {
        final FinancialYear FY = new FinancialYear();
        FY.setId(dc.getValue("financialYear_ID"));
        FY.setFinancialYear(dc.getValue("financialYear_financialYear"));
        FY.setStartingDate(dc.getValue("financialYear_startingDate"));
        FY.setEndingDate(dc.getValue("financialYear_endingDate"));
        FY.setModifiedBy(dc.getValue("egUser_id"));
        FY.update();
        fid = FY.getId();
    }

    private void postInFiscalPeriod() throws SQLException, TaskFailedException {
        final FiscalPeriod FP = new FiscalPeriod();
        status = dc.getValue("fiscalPeriod");
        FP.setFinancialYearId(fid + "");
        final String fGrid[][] = dc.getGrid("fiscalPeriodGrid");

        for (final String[] element : fGrid) {
            if (element[1].equalsIgnoreCase(""))
                continue;
            FP.setName(element[1]);
            FP.setStartingDate(element[2]);
            FP.setEndingDate(element[3]);
            FP.setModifiedBy(dc.getValue("egUser_id"));

            if (element[0].equals(""))
                FP.insert();
            else {
                FP.setId(element[0]);
                FP.update();
            }

        }

    }		// end of post
}// end of class
