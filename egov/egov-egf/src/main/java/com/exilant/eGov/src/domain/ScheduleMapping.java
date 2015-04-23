/*
 * Created on Jan 4, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Lakshmi
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ScheduleMapping {
	private static final Logger LOGGER = Logger
			.getLogger(ScheduleMapping.class);
	PreparedStatement pstmt = null;
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
	private String updateQuery = "UPDATE schedulemapping SET";
	private boolean isId = false, isField = false;
	private static TaskFailedException taskExc;
	EGovernCommon cm = new EGovernCommon();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
			.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
			Locale.getDefault());

	public void setId(String aId) {
		id = aId;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void insert(Connection con) throws SQLException, TaskFailedException {
		// EGovernCommon commommethods = new EGovernCommon();

		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("schedulemapping")));
			EGovernCommon common = new EGovernCommon();
			try {
				createdDate = common.getCurrentDate(con);
				// Formatting Date
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				createdDate = formatter.format(sdf.parse(createdDate));
				setCreatedDate(createdDate);
				lastModifiedDate = null;

				setCreatedDate(createdDate);
				setLastModifiedDate(lastModifiedDate);

				// scheduleName=common.formatString(scheduleName);
				String insertQuery = "INSERT INTO schedulemapping (id, reportType,schedule, scheduleName, createdBy, createdDate, "
						+ "lastModifiedBy,lastModifiedDate,repSubType,isRemission) "
						+ "values(?,?,?,?,?,?,?,?,?,?)";
				if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
				pstmt = con.prepareStatement(insertQuery);
				pstmt.setString(1, id);
				pstmt.setString(2, reportType);
				pstmt.setString(3, schedule);
				pstmt.setString(4, scheduleName);
				pstmt.setString(5, createdBy);
				pstmt.setString(6, createdDate);
				pstmt.setString(7, lastModifiedBy);
				pstmt.setString(8, lastModifiedDate);
				pstmt.setString(9, repSubType);
				pstmt.setString(10, isRemission);
				pstmt.executeUpdate();
			} catch (Exception e) {
				LOGGER.error("ERROR" + e.getMessage(), e);
				throw taskExc;
			} finally {
				pstmt.close();
			}

	}

	public void update(Connection con) throws SQLException, TaskFailedException {
		try {
			newUpdate(con);
		} catch (Exception e) {
			LOGGER.error("Error inside update" + e.getMessage(), e);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		lastModifiedDate = commommethods.getCurrentDate(con);
		PreparedStatement pstmt = null;
		try {
			lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
		} catch (ParseException parseExp) {
			LOGGER.error("error inside newUpdate"+parseExp.getMessage(), parseExp);
		}
		setLastModifiedDate(lastModifiedDate);
		StringBuilder query = new StringBuilder(500);
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
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
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

			pstmt.executeQuery();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(), e);
			throw taskExc;
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block of update"+e.getMessage(), e);
			}
		}
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getRepSubType() {
		return repSubType;
	}

	public void setRepSubType(String repSubType) {
		this.repSubType = repSubType;
	}

	public String getIsRemission() {
		return isRemission;
	}

	public void setIsRemission(String isRemission) {
		this.isRemission = isRemission;
	}

}
