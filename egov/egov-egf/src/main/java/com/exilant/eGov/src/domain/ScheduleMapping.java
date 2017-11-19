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
/*
 * Created on Jan 4, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;


import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class ScheduleMapping {
    private static final Logger LOGGER = Logger
            .getLogger(ScheduleMapping.class);
    Query pstmt = null;
    private String id = null;
    private String reportType = null;
    private String schedule = "0";
    private String scheduleName = null;
    private String createdBy = null;
    private String createdDate = "";
    private String lastModifiedBy = null;
    private String lastModifiedDate = "";
    private String repSubType = null;
    private String isRemission = null;
    private static TaskFailedException taskExc;
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired EGovernCommon eGovernCommon;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
            .getDefault());
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
            Locale.getDefault());

    public void setId(final String aId) {
        id = aId;
    }

    public int getId() {
        return Integer.valueOf(id).intValue();
    }

    @Transactional
    public void insert() throws SQLException, TaskFailedException {

        setId(String.valueOf(PrimaryKeyGenerator.getNextKey("schedulemapping")));
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            createdDate = formatter.format(new Date());
            setCreatedDate(createdDate);
            lastModifiedDate = null;

            setCreatedDate(createdDate);
            setLastModifiedDate(lastModifiedDate);

            // scheduleName=common.formatString(scheduleName);
            final String insertQuery = "INSERT INTO schedulemapping (id, reportType,schedule, scheduleName, createdBy, createdDate, "
                    + "lastModifiedBy,lastModifiedDate,repSubType,isRemission) "
                    + "values(?,?,?,?,?,?,?,?,?,?)";
            if (LOGGER.isInfoEnabled())
                LOGGER.info(insertQuery);
            pstmt = persistenceService.getSession().createSQLQuery(insertQuery);
            pstmt.setString(0, id);
            pstmt.setString(1, reportType);
            pstmt.setString(2, schedule);
            pstmt.setString(3, scheduleName);
            pstmt.setString(4, createdBy);
            pstmt.setString(5, createdDate);
            pstmt.setString(6, lastModifiedBy);
            pstmt.setString(7, lastModifiedDate);
            pstmt.setString(8, repSubType);
            pstmt.setString(9, isRemission);
            pstmt.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("ERROR" + e.getMessage(), e);
            throw taskExc;
        }

    }

    @Transactional
    public void update() throws SQLException, TaskFailedException {
        try {
            newUpdate();
        } catch (final Exception e) {
            LOGGER.error("Error inside update" + e.getMessage(), e);
        }
    }

    public void newUpdate() throws TaskFailedException,
    SQLException {
        lastModifiedDate = eGovernCommon.getCurrentDate();
        Query pstmt = null;
        try {
            lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
        } catch (final ParseException parseExp) {
            LOGGER.error("error inside newUpdate" + parseExp.getMessage(), parseExp);
        }
        setLastModifiedDate(lastModifiedDate);
        final StringBuilder query = new StringBuilder(500);
        query.append("update schedulemapping set ");
        if (reportType != null)
            query.append("REPORTTYPE=?,");
        if (schedule != null)
            query.append("SCHEDULE=?,");
        if (scheduleName != null)
            query.append("SCHEDULENAME=?,");
        if (createdBy != null)
            query.append("CREATEDBY=?,");
        if (createdDate != null && !createdDate.isEmpty())
            query.append("CREATEDDATE=?,");
        if (lastModifiedBy != null)
            query.append("LASTMODIFIEDBY=?,");
        if (lastModifiedDate != null)
            query.append("LASTMODIFIEDDATE=?,");
        if (repSubType != null)
            query.append("REPSUBTYPE=?,");
        if (isRemission != null)
            query.append("ISREMISSION=?,");
        final int lastIndexOfComma = query.lastIndexOf(",");
        query.deleteCharAt(lastIndexOfComma);
        query.append(" where id=?");
        try {
            int i = 1;
            pstmt = persistenceService.getSession().createSQLQuery(query.toString());
            if (reportType != null)
                pstmt.setString(i++, reportType);
            if (schedule != null)
                pstmt.setString(i++, schedule);
            if (scheduleName != null)
                pstmt.setString(i++, scheduleName);
            if (createdBy != null)
                pstmt.setString(i++, createdBy);
            if (createdDate != null && !createdDate.isEmpty())
                pstmt.setString(i++, createdDate);
            if (lastModifiedBy != null)
                pstmt.setString(i++, lastModifiedBy);
            if (lastModifiedDate != null)
                pstmt.setString(i++, lastModifiedDate);
            if (repSubType != null)
                pstmt.setString(i++, repSubType);
            if (isRemission != null)
                pstmt.setString(i++, isRemission);
            pstmt.setString(i++, id);

            pstmt.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exp in update: " + e.getMessage(), e);
            throw taskExc;
        }
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(final String reportType) {
        this.reportType = reportType;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(final String schedule) {
        this.schedule = schedule;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(final String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getRepSubType() {
        return repSubType;
    }

    public void setRepSubType(final String repSubType) {
        this.repSubType = repSubType;
    }

    public String getIsRemission() {
        return isRemission;
    }

    public void setIsRemission(final String isRemission) {
        this.isRemission = isRemission;
    }

}