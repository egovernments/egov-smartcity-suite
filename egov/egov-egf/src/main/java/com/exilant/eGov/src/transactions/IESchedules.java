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
/**
 * Created on Aug 08, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class IESchedules extends AbstractTask {
    private static final Logger LOGGER = Logger.getLogger(IESchedules.class);
    Connection conn;
    NumberFormat formatter;

    /* this method is called by Exility */
    @Override
    public void execute(final String taskName, final String gridName, final DataCollection dc,
            final Connection conn, final boolean erroOrNoData,
            final boolean gridHasColumnHeading, final String prefix)
                    throws TaskFailedException {
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("****************IESchedules");
        this.conn = conn;
        formatter = new DecimalFormat();
        formatter = new DecimalFormat("###############.00");

        printSchedules(dc);

        // dc.addMessage("eGovSuccess","IESchedules");
    }

    private void printSchedules(final DataCollection dc) throws TaskFailedException {
        final String tableTime = dc.getValue("tableToDrop");
        final String mainTable = "coaie" + tableTime;
        final String report = "SELECT scheduleglCode AS \"glcode\", case when operation = 'L' then 'Less: ' else ' ' end  || schedulename AS \"name\", 'Schedule ' || schschedule || ': ' || summaryname || '[Code No ' || summaryglcode || ']' AS \"schTitle\", case when schschedule = NULL then '-' else schschedule AS \"schedule\", curYearAmount AS \"curyearamount\", preyearamount AS \"preyearamount\", operation AS \"operation\", TYPE AS \"type\" FROM "
                + mainTable
                + " WHERE TYPE = 'I' OR TYPE = 'E' ORDER BY scheduleglCode, TYPE, operation";
        PreparedStatement pst = null;
        ResultSet rs = null;
        final ArrayList ar = new ArrayList();
        double curAmt = 0, preAmt = 0, sumCur = 0, sumPre = 0;
        String operation = "", schedule = "", preSchedule = "", title = "";
        final String schTitle[] = new String[20];
        int grids = 0, rowCount = 0, cnt = 0;
        final int rows[] = new int[20];

        final String sDate = dc.getValue("sDate") == null ? "start of FY" : dc
                .getValue("sDate");
        final String eDate = dc.getValue("eDate") == null ? "today" : dc
                .getValue("eDate");
        dc.addValue("pageTitle",
                "Income & Expenditure Schedules For the period of " + sDate
                + " to " + eDate);

        try {
            pst = conn.prepareStatement(report);
            rs = pst.executeQuery();

            while (rs.next()) {
                curAmt = rs.getDouble("curyearamount");
                preAmt = rs.getDouble("preyearamount");
                schedule = rs.getString("schedule");
                operation = rs.getString("operation");

                if (!preSchedule.equalsIgnoreCase(schedule))
                    grids++;
                if (preSchedule.equalsIgnoreCase(""))
                    preSchedule = schedule;
                if (!preSchedule.equalsIgnoreCase(schedule)) {
                    schTitle[cnt] = title;
                    // if we dont have any records for this schedule we should
                    // not show on screen
                    if (sumCur != 0 || sumPre != 0)
                        dc.addValue("showRowIESchedule" + (cnt + 1), "true");
                    else {
                        dc.addValue("showRowIESchedule" + (cnt + 1), "false");
                        schTitle[cnt] += " -No Data";
                    }
                    final String total[] = { "-", "Total", formatter.format(sumCur),
                            formatter.format(sumPre) };
                    ar.add(total);
                    rowCount++;

                    rows[cnt++] = rowCount;
                    rowCount = 0;
                    sumCur = sumPre = 0;
                }
                rowCount++;
                title = rs.getString("schTitle");

                if (operation.equalsIgnoreCase("L")
                        && preSchedule.equalsIgnoreCase(schedule)) {
                    final String total[] = { "-", "Sub Total",
                            formatter.format(sumCur), formatter.format(sumPre) };
                    ar.add(total);
                    rowCount++;
                }
                if (operation.equalsIgnoreCase("L")) {
                    sumCur = sumCur - curAmt;
                    sumPre = sumPre - preAmt;
                } else {
                    sumCur = sumCur + curAmt;
                    sumPre = sumPre + preAmt;
                }
                final String row[] = { rs.getString("glcode"), rs.getString("name"),
                        formatter.format(curAmt), formatter.format(preAmt) };
                ar.add(row);
                preSchedule = schedule;
            }
            // rows[cnt] = rowCount;
            // if(LOGGER.isDebugEnabled())
            // LOGGER.debug("Out One rowcounnt: "+cnt+"-"+rowCount+" Preschedule:"+preSchedule+" schedule:"+schedule);

        } catch (final SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
            // dc.addMessage("eGovFailure","IESchedules->printSchedules: "+ex.toString());
            throw new TaskFailedException();
        }

        // //if(LOGGER.isDebugEnabled()) LOGGER.debug("******************grids-"+grids);

        final String grid[][][] = new String[grids][][];
        int nextRow = 0;
        for (int gridNo = 0; gridNo < grids - 1; gridNo++) {
            final String gridData[][] = new String[rows[gridNo] + 1][4];
            gridData[0][0] = "glCode";
            gridData[0][1] = "name";
            gridData[0][2] = "amountCurYear";
            gridData[0][3] = "amountPreYear";
            for (int rowNo = 0; rowNo < rows[gridNo]; rowNo++)
                gridData[rowNo + 1] = (String[]) ar.get(nextRow++);
            // if(LOGGER.isDebugEnabled()) LOGGER.debug(gridData[rowNo][0] + " - " + gridData[rowNo][1]
            // + " - " + gridData[rowNo][2] + " - " + gridData[rowNo][3]);
            grid[gridNo] = gridData;
        }
        for (int i = 0; i < grids - 1; i++)
            if (grid[i] != null) {
                dc.addValue("schTitle" + (i + 1), schTitle[i]);
                dc.addGrid("gridIESchedule" + (i + 1), grid[i]);
            } else if (LOGGER.isDebugEnabled())
                LOGGER.debug("grid is null: " + i);
    }
}
