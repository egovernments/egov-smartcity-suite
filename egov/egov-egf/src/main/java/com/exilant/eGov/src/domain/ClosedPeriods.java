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
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class ClosedPeriods
{
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private String id = null;
    private String startingDate = "";
    private String endingDate = "";
    private String isClosed = null;
    Query psmt = null;
    private TaskFailedException taskExc;
    private boolean isId = false, isField = false;
    private static final Logger LOGGER = Logger.getLogger(ClosedPeriods.class);

    public void setId(final String aId) {
        id = aId;
        isId = true;
    }

    public int getId() {
        return Integer.valueOf(id).intValue();
    }

    public void setStartingDate(final String aStartingDate) {
        startingDate = aStartingDate;
        isField = true;
    }

    public void setEndingDate(final String aEndingDate) {
        endingDate = aEndingDate;
        isField = true;
    }

    public void setIsClosed(final String aIsClosed) {
        isClosed = aIsClosed;
        isField = true;
    }

    @Transactional
    public void insert() throws SQLException {
        setId(String.valueOf(PrimaryKeyGenerator.getNextKey("ClosedPeriods")));
        final String insertQuery = "INSERT INTO closedPeriods (id, startingDate, endingDate, isClosed) " +
                "VALUES(?,?,?,?)";

       // psmt = persistenceService.getSession().createSQLQuery(insertQuery);
        psmt.setString(1, id);
        psmt.setString(2, startingDate);
        psmt.setString(3, endingDate);
        psmt.setString(4, isClosed);
        if (LOGGER.isInfoEnabled())
            LOGGER.info(insertQuery);
        psmt.executeUpdate();
    }

    @Transactional
    public void update() throws SQLException
    {
        if (isId && isField)
            try {
                newUpdate();
            } catch (final Exception e) {
                LOGGER.error("Error while updating close period");
            }
    }

    public void newUpdate() throws TaskFailedException, SQLException {
        final StringBuilder query = new StringBuilder(500);
        Query pstmt = null;
        query.append("update closedPeriods set ");
        if (startingDate != null)
            query.append("startingDate=?,");
        if (endingDate != null)
            query.append("endingDate=?,");
        if (isClosed != null)
            query.append("isClosed=?,");
        final int lastIndexOfComma = query.lastIndexOf(",");
        query.deleteCharAt(lastIndexOfComma);
        query.append(" where id=?");
        try {
            int i = 1;
            pstmt = persistenceService.getSession().createSQLQuery(query.toString());
            if (startingDate != null)
                pstmt.setString(i++, startingDate);
            if (endingDate != null)
                pstmt.setString(i++, endingDate);
            if (isClosed != null)
                pstmt.setString(i++, isClosed);
            pstmt.setString(i++, id);

            pstmt.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exp in update: " + e.getMessage());
            throw taskExc;
        }

    }
@Deprecated
    static public boolean isClosedForPosting(final String date) throws TaskFailedException {
        boolean isClosed = true;
        String chkqry = null;
        Query psmt = null;
        Query psmt1 = null;
        // Date dateTyp=null;
        try {
            // SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
            // SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
            // dateTyp = (Date)formatter.parse(date);

            chkqry = "SELECT id FROM financialYear " +
                    "WHERE startingDate<='" + date + "'  AND endingDate>='" + date + "' AND isActiveForPosting=true";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Before excuting " + chkqry);
            //psmt = persistenceService.getSession().createSQLQuery(chkqry);
            List<Object[]> rs = psmt.list();

            if (rs != null && rs.size() > 0)
                isClosed = false;

            if (!isClosed) {
                rs = null;
                final String qry = "SELECT id FROM closedPeriods WHERE to_char(startingDate, 'DD-MON-YYYY')<='" + date
                        + "' AND endingDate>='" + date + "'";
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(qry);
                //psmt1 = persistenceService.getSession().createSQLQuery(qry);
                rs = psmt1.list();

                if (!(rs != null && rs.size() > 0))
                    isClosed = false;
                else
                    isClosed = true;
            }

        } catch (final HibernateException e) {
            isClosed = true;
            LOGGER.error("Exception occured while getting the data  " + e.getMessage(), new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            isClosed = true;
            LOGGER.error("Exception occured while getting the data  " + e.getMessage(), new Exception(e.getMessage()));
        }
        return isClosed;
    }

}