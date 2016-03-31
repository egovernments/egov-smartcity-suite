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
 * Created on Jan 4, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;

/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Qualifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.domain.ChartOfAccts;
import com.exilant.eGov.src.domain.ScheduleMapping;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

@Transactional(readOnly = true)
public class ScheduleMaster extends AbstractTask {
    private DataCollection dc;
    private Query pstmt = null;
    private static final Logger LOGGER = Logger.getLogger(ScheduleMaster.class);
    private static final String SCHNUMBER = "schNumber";
    private static final String REPTYPE = "repType";
    private static final String REPSUBTYPE = "repSubType";

 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired ScheduleMapping scheduleMapping;
    /*
     * Abstract method of AbstractTask Class
     */
    @Override
    public void execute(final String taskName, final String gridName, final DataCollection dc,
            final Connection conn, final boolean errorOnNoData,
            final boolean gridHasColumnHeading, final String prefix)
                    throws TaskFailedException {
        this.dc = dc;
        final String mode = dc.getValue("mode");
        if (mode.equalsIgnoreCase("insert") && addSchedule())
            dc.addMessage("userSuccess", " Schedule No: "
                    + dc.getValue(SCHNUMBER), " Inserted Successfully");
        else if (mode.equalsIgnoreCase("edit") && editSchedule())
            dc.addMessage("userSuccess", " Schedule No: "
                    + dc.getValue(SCHNUMBER), " updated Successfully");
    }

    public boolean addSchedule() throws TaskFailedException {
        int schId = 0;
        List<Object[]> rs = null;
        final String schNo = dc.getValue(SCHNUMBER);
        try {
            final String query = "select id from schedulemapping where schedule = ?";
            pstmt = persistenceService.getSession().createSQLQuery(query);
            pstmt.setString(0, schNo);
            if (LOGGER.isInfoEnabled())
                LOGGER.info(query);

            rs = pstmt.list();
            /** Checks whether given Schedule Number already Exists **/
            for (final Object[] element : rs) {
                dc.addMessage("exilError", ": Duplicate Schedule No.:" + schNo);
                throw new TaskFailedException();
            }
            /** If doesnot exist insert into scheduleMapping table and update chartofaccounts table **/
            schId = postInSchedulemapping();
            if (schId == 0) {
                dc.addMessage("exilError", " : Error in Schedule Creation");
                throw new TaskFailedException();
            }
            updateChartofaccounts(schId);
        } catch (final Exception s) {
            dc.addMessage("exilError", " : Error in Schedule Creation");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("ExilError" + s.getMessage(), s);
            throw new TaskFailedException();
        }
        return true;
    }

    public int postInSchedulemapping() throws TaskFailedException {
        int schId = 0;
        if (LOGGER.isInfoEnabled())
            LOGGER.info("inside postInSchedulemapping");
        scheduleMapping.setSchedule(dc.getValue(SCHNUMBER));
        scheduleMapping.setScheduleName(dc.getValue("schName"));
        scheduleMapping.setReportType(dc.getValue(REPTYPE));
        scheduleMapping.setCreatedBy(dc.getValue("egUser_id"));
        if ("RP".equalsIgnoreCase(dc.getValue(REPTYPE))) {
            scheduleMapping.setRepSubType(dc.getValue(REPSUBTYPE));
            if ("POP".equalsIgnoreCase(dc.getValue(REPSUBTYPE)))
                scheduleMapping.setIsRemission(dc.getValue("isRemission"));
        }

        try {
            scheduleMapping.insert();
        } catch (final SQLException s) {
            LOGGER.error("exilError" + s.getMessage(), s);
            dc.addMessage("exilError", " : Error in Schedule Creation");
            throw new TaskFailedException();
        }
        schId = scheduleMapping.getId();
        return schId;
    }

    public void updateChartofaccounts(final int schMapId) throws TaskFailedException {
        final ChartOfAccts coa = new ChartOfAccts();
        final String[][] scheduleGrid = dc.getGrid("schLineItem");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("rep Type:" + dc.getValue(REPTYPE));
        if (LOGGER.isInfoEnabled())
            LOGGER.info("schMapId:" + schMapId);
        if ("RP".equalsIgnoreCase(dc.getValue(REPTYPE)))
            for (final String[] element : scheduleGrid) {
                final String accntCode = element[0];
                String glCode = null;
                final StringTokenizer token = new StringTokenizer(accntCode, "  ~  ");
                if (token.hasMoreTokens())
                    glCode = token.nextToken();
                if ("ROP".equalsIgnoreCase(dc.getValue(REPSUBTYPE)) || "RNOP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))) {
                    coa.setReceiptscheduleid(schMapId + "");
                    coa.setReceiptoperation(element[1]);
                } else if ("POP".equalsIgnoreCase(dc.getValue(REPSUBTYPE)) || "PNOP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))) {
                    coa.setPaymentscheduleid(schMapId + "");
                    coa.setPaymentoperation(element[1]);
                }
                coa.setId(glCode);
                try {
                    coa.update();
                } catch (final SQLException s) {
                    LOGGER.error("exilError" + s.getMessage(), s);
                    throw new TaskFailedException();
                }
            }
        else
            for (final String[] element : scheduleGrid) {
                final String accntCode = element[0];
                String glCode = null;
                final StringTokenizer token = new StringTokenizer(accntCode, "  ~  ");
                if (token.hasMoreTokens())
                    glCode = token.nextToken();
                if ("FIE".equalsIgnoreCase(dc.getValue(REPTYPE)))
                {
                    coa.setFIEscheduleId(schMapId);
                    coa.setFIEoperation(element[1]);
                    coa.setId(glCode);
                } else
                {
                    coa.setScheduleId(schMapId + "");
                    coa.setOperation(element[1]);
                    coa.setId(glCode);

                }
                try {
                    coa.update();
                } catch (final SQLException s) {
                    LOGGER.error("SchldGrid" + s.getMessage(), s);
                    throw new TaskFailedException();
                }
            }
    }

    public boolean editSchedule() throws TaskFailedException {
        final String strSchId = dc.getValue("schId");
        final int schId = Integer.parseInt(strSchId);
        try {
            /** update into scheduleMapping table and update chartofaccounts table **/
            if (schId != 0) {
                resetChartofAccounts(schId);
                updateSchedulemapping(schId);
                updateChartofaccounts(schId);
            }
        } catch (final Exception s) {
            LOGGER.error("Error in updating" + s.getMessage(), s);
            dc.addMessage("exilError", " : Error in Schedule Updation");
            throw new TaskFailedException();
        }
        return true;
    }

    public void updateSchedulemapping(final int schMapId) throws TaskFailedException {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("schMapId:" + schMapId);
        scheduleMapping.setSchedule(dc.getValue(SCHNUMBER));
        scheduleMapping.setScheduleName(dc.getValue("schName"));
        scheduleMapping.setReportType(dc.getValue(REPTYPE));
        scheduleMapping.setLastModifiedBy(dc.getValue("egUser_id"));
        if ("RP".equalsIgnoreCase(dc.getValue(REPTYPE))) {
            scheduleMapping.setRepSubType(dc.getValue(REPSUBTYPE));
            if ("POP".equalsIgnoreCase(dc.getValue(REPSUBTYPE)))
                scheduleMapping.setIsRemission(dc.getValue("isRemission"));
        }
        scheduleMapping.setId(schMapId + "");
        try {
            scheduleMapping.update();
        } catch (final SQLException s) {
            LOGGER.error("Error in Schedule Updation" + s.getMessage(), s);
            dc.addMessage("exilError", " : Error in Schedule Updation");
            throw new TaskFailedException();
        }
    }

    @Transactional
    public void resetChartofAccounts(final int schMapId) throws TaskFailedException {
        try {
            if ("RP".equalsIgnoreCase(dc.getValue(REPTYPE))) {
                if ("ROP".equalsIgnoreCase(dc.getValue(REPSUBTYPE)) || "RNOP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))) {
                    // sbuffer.append(query).append("Receiptscheduleid= ?,Receiptoperation= ? where Receiptscheduleid= ?");
                    final String query = "update chartofaccounts set Receiptscheduleid = ?,Receiptoperation = ? where Receiptscheduleid= ?";
                    // query=query+" Receiptscheduleid=null,Receiptoperation=null where Receiptscheduleid="+schMapId;
                    // query = sbuffer.toString();
                    pstmt = persistenceService.getSession().createSQLQuery(query);
                    pstmt.setString(0, null);
                    pstmt.setString(1, null);
                    pstmt.setInteger(2, schMapId);
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info(query);
                    pstmt.executeUpdate();
                }
                else if ("POP".equalsIgnoreCase(dc.getValue(REPSUBTYPE)) || "PNOP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))) {
                    final String query = "update chartofaccounts set Paymentscheduleid = ?,Paymentoperation = ? where Paymentscheduleid = ?";
                    // query=query+" Paymentscheduleid=null,Paymentoperation=null where Paymentscheduleid="+schMapId;
                    pstmt = persistenceService.getSession().createSQLQuery(query);
                    pstmt.setString(0, null);
                    pstmt.setString(1, null);
                    pstmt.setInteger(2, schMapId);
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info(query);
                    pstmt.executeUpdate();
                }
            }
            else {
                final String query = "update chartofaccounts set  ScheduleId= ?,Operation= ? where ScheduleId= ?";
                pstmt = persistenceService.getSession().createSQLQuery(query);
                pstmt.setString(0, null);
                pstmt.setString(1, null);
                pstmt.setInteger(2, schMapId);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(query);
                pstmt.executeUpdate();
            }
        } catch (final Exception s) {
            dc.addMessage("exilError", ":Error" + s.getMessage());
            LOGGER.error("Error in updating Chartofaccounts", s);
            throw new TaskFailedException();
        }
    }

}