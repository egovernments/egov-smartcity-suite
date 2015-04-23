/*
 * Created on Apr 11, 2005
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

public class FiscalPeriod {
	private String id = null;
	private String type = null;
	private String name = null;
	private String startingDate = "1-Jan-1900";
	private String endingDate = "1-Jan-1900";
	private String parentId = null;
	private String isActiveForPosting = "0";
	private String isActive = "0";
	private String modifiedBy = null;
	private String lastModified = "";
	private String created = "1-Jan-1900";
	private String financialYearId = null;
	private static TaskFailedException taskExc;
	private String updateQuery = "UPDATE FiscalPeriod SET";
	private static final Logger LOGGER = Logger.getLogger(FiscalPeriod.class);
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

	public void insert(Connection connection) throws SQLException,
			TaskFailedException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(connection);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse(created));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw taskExc;
		}
		setCreated(created);
		setLastModified(created);
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("FiscalPeriod")));

		String insertQuery = "INSERT INTO FiscalPeriod (id, type, name, startingdate, endingdate, parentid, "
				+ "isactiveforposting, isactive, modifiedby, lastmodified, created, FINANCIALYEARID) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		if(LOGGER.isInfoEnabled())     LOGGER.info("before : " + insertQuery);
		PreparedStatement pst = connection.prepareStatement(insertQuery);
		pst.setString(1, id);
		pst.setString(2, type);
		pst.setString(3, name);
		pst.setString(4, startingDate);
		pst.setString(5, endingDate);
		pst.setString(6, parentId);
		pst.setString(7, isActiveForPosting);
		pst.setString(8, isActive);
		pst.setString(9, modifiedBy);
		pst.setString(10, lastModified);
		pst.setString(11, created);
		pst.setString(12, financialYearId);
		pst.executeUpdate();
		pst.close();
	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
		newUpdate(connection);
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(con);
		PreparedStatement pstmt = null;
		try {
			created = formatter.format(sdf.parse(created));
		} catch (ParseException parseExp) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(), parseExp);
		}
		setCreated(created);
		setLastModified(created);
		StringBuilder query = new StringBuilder(500);
		query.append("update FiscalPeriod set ");
		if (type != null)
			query.append("type=?,");
		if (name != null)
			query.append("name=?,");
		if (startingDate != null)
			query.append("startingDate=?,");
		if (endingDate != null)
			query.append("endingDate=?,");
		if (parentId != null)
			query.append("parentId=?,");
		if (isActiveForPosting != null)
			query.append("isActiveForPosting=?,");
		if (isActive != null)
			query.append("isActive=?,");
		if (modifiedBy != null)
			query.append("modifiedBy=?,");
		if (lastModified != null)
			query.append("lastModified=?,");
		if (created != null)
			query.append("created=?,");
		if (financialYearId != null)
			query.append("financialYearId=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (type != null)
				pstmt.setString(i++, type);
			if (name != null)
				pstmt.setString(i++, name);
			if (startingDate != null)
				pstmt.setString(i++, startingDate);
			if (endingDate != null)
				pstmt.setString(i++, endingDate);
			if (parentId != null)
				pstmt.setString(i++, parentId);
			if (isActiveForPosting != null)
				pstmt.setString(i++, isActiveForPosting);
			if (isActive != null)
				pstmt.setString(i++, isActive);
			if (modifiedBy != null)
				pstmt.setString(i++, modifiedBy);
			if (lastModified != null)
				pstmt.setString(i++, lastModified);
			if (created != null)
				pstmt.setString(i++, created);
			if (financialYearId != null)
				pstmt.setString(i++, financialYearId);
			pstmt.setString(i++, id);

			pstmt.executeQuery();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage());
			throw taskExc;
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block of update");
			}
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	public String getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(String endingDate) {
		this.endingDate = endingDate;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIsActiveForPosting() {
		return isActiveForPosting;
	}

	public void setIsActiveForPosting(String isActiveForPosting) {
		this.isActiveForPosting = isActiveForPosting;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getFinancialYearId() {
		return financialYearId;
	}

	public void setFinancialYearId(String financialYearId) {
		this.financialYearId = financialYearId;
	}

}
