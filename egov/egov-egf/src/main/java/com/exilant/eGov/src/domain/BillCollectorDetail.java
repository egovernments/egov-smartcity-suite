/*
 * Created on Jan 18, 2005
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

public class BillCollectorDetail {
	private String id = null;
	private String billCollectorId = null;
	private String wardId = null;
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger
			.getLogger(BillCollectorDetail.class);
	private TaskFailedException taskExc;

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void setBillCollectorID(String aBillCollectorID) {
		billCollectorId = aBillCollectorID;
		isField = true;
	}

	public void setWardId(String aWardId) {
		wardId = aWardId;
		isField = true;
	}

	public void insert(Connection connection) throws SQLException,
			TaskFailedException {

		PreparedStatement ps = null;
		try {
			setId(String.valueOf(PrimaryKeyGenerator
					.getNextKey("BillCollectorDetail")));

			String insertQuery = "INSERT INTO billcollectorDetail (Id, BillCollectorID, WardId) "
					+ " values (?,?,?)";
			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, id);
			ps.setString(2, billCollectorId);
			ps.setString(3, wardId);
			ps.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in insert :" + e.getMessage(),e);
			throw taskExc;
		} finally {
			ps.close();
		}
	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
		if (isId && isField) {
			newUpdate(connection);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		StringBuilder query = new StringBuilder(500);
		PreparedStatement pstmt = null;
		query.append("update billcollectorDetail set ");
		if (billCollectorId != null)
			query.append("billCollectorId=?,");
		if (wardId != null)
			query.append("wardId=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (billCollectorId != null)
				pstmt.setString(i++, billCollectorId);
			if (wardId != null)
				pstmt.setString(i++, wardId);
			pstmt.setString(i++, id);

			pstmt.executeQuery();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(),e);
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
