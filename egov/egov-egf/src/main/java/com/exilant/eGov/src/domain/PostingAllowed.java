/*
 * Created on Apr 20, 2005 
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.exility.common.TaskFailedException;

public class PostingAllowed {
	private final static Logger LOGGER = Logger.getLogger(PostingAllowed.class);
	private static TaskFailedException taskExc;
	private String postingAllowed = "0";
	private String fiscalPeriodId = "0";
	private String glCodeId = "0";

	private String updateQuery = "UPDATE PostingAllowed SET";
	private boolean isField = false;

	public void setPostingAllowed(String aPostingAllowed) {
		postingAllowed = aPostingAllowed;
		updateQuery = updateQuery + " postingAllowed = " + postingAllowed + ",";
		isField = true;
	}

	public void setFiscalPeriodId(String aFiscalPeriodId) {
		fiscalPeriodId = aFiscalPeriodId;
		updateQuery = updateQuery + " fiscalPeriodId = " + fiscalPeriodId + ",";
		isField = true;
	}

	public void setGLCodeId(String aGLCodeId) {
		glCodeId = aGLCodeId;
		updateQuery = updateQuery + " glCodeId = " + glCodeId + ",";
		isField = true;
	}

	public void insert(Connection connection) throws SQLException {
		String insertQuery = "INSERT INTO PostingAllowed (postingallowed, fiscalperiodid, GLCODEID) "
				+ "VALUES (?, ?, ?)";

		PreparedStatement pstmt = connection.prepareStatement(insertQuery);
		pstmt.setString(1, postingAllowed);
		pstmt.setString(1, fiscalPeriodId);
		pstmt.setString(1, glCodeId);
		pstmt.executeUpdate();
		pstmt.close();
		if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
	}

	public void update(Connection connection) throws SQLException {
		if (isField) {

			try {
				newUpdate(connection);
			} catch (TaskFailedException e) {
				LOGGER.error("Error Inside update" + e.getMessage(), e);
			}
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update vouchermis set ");
		if (postingAllowed != null)
			query.append("voucherheaderid=?,");
		if (fiscalPeriodId != null)
			query.append("divisionId=?,");
		if (glCodeId != null)
			query.append("createTimeStamp=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");

		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (postingAllowed != null)
				pstmt.setString(i++, postingAllowed);
			if (fiscalPeriodId != null)
				pstmt.setString(i++, fiscalPeriodId);
			if (glCodeId != null)
				pstmt.setString(i++, glCodeId);
			// pstmt.setString(i++, id);

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
}
