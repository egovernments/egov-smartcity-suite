/*
 * Created on Mar 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

public class AccountDtlKey {
	private final static Logger LOGGER = Logger.getLogger(AccountDtlKey.class);
	private String id = "";
	private String groupID = "1";
	private String glCodeID = null;
	private String detailTypeId = null;
	private String detailName = "";
	private String detailKey = null;
	private TaskFailedException taskExc;

	// private String insertQuery;
	private boolean isId = false, isField = false;

	public void setID(final String aID) {
		id = aID;
		isId = true;
	}

	public void setgroupID(final String agroupID) {
		groupID = agroupID;
		isField = true;
	}

	public void setglCodeID(final String aglCodeID) {
		glCodeID = aglCodeID;
		isField = true;
	}

	public void setdetailTypeId(final String adetailTypeId) {
		detailTypeId = adetailTypeId;
		isField = true;
	}

	public void setdetailName(final String adetailName) {
		detailName = adetailName;
		isField = true;
	}

	public void setdetailKey(final String adetailKey) {
		detailKey = adetailKey;
		isField = true;
	}

	public void insert(final Connection connection) throws SQLException,
			TaskFailedException {
		PreparedStatement pst = null;
		try {

			setID(String.valueOf(PrimaryKeyGenerator
					.getNextKey("AccountDetailKey")));
			String insertQuery = "insert into AccountDetailKey (id,groupID, glCodeID, detailTypeId, detailName, detailKey)"
					+ " values ( ?, ?, ?, ?, ?, ?)";
			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			pst = connection.prepareStatement(insertQuery);
			pst.setString(1, id);
			pst.setString(2, groupID);
			pst.setString(3, glCodeID);
			pst.setString(4, detailTypeId);
			pst.setString(5, detailName);
			pst.setString(6, detailKey);
			int ret = pst.executeUpdate();
			// if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			if (ret == 1) {
				if(LOGGER.isDebugEnabled())     LOGGER.debug("SUCCESS INSERTING INTO ADK");
			}
		} catch (Exception e) {
			LOGGER.error("Exception in insert:" + e.getMessage(),e);
			throw taskExc;
		} finally {
			try {
				pst.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block");
			}
		}
	}

	public void update(final Connection connection) throws SQLException,
			TaskFailedException {
		PreparedStatement pst = null;
		try {

			if (isId && isField) {
				newUpdate(connection);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in update:" + e.getMessage(),e);
			throw taskExc;
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {

		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update accountdetailkey set ");
		if (groupID != null)
			query.append("groupID=?,");
		if (glCodeID != null)
			query.append("glCodeID=?,");
		if (detailTypeId != null)
			query.append("detailTypeId=?,");
		if (detailName != null)
			query.append("detailName=?,");
		if (detailKey != null)
			query.append("detailKey=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (groupID != null)
				pstmt.setString(i++, groupID);
			if (glCodeID != null)
				pstmt.setString(i++, glCodeID);
			if (detailTypeId != null)
				pstmt.setString(i++, detailTypeId);
			if (detailName != null)
				pstmt.setString(i++, detailName);
			if (detailKey != null)
				pstmt.setString(i++, detailKey);
			pstmt.setString(i++, id);

			pstmt.executeQuery();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(), e);
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
