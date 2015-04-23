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
 * @author Rashmi MN
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

public class CodeMapping {

	private String eg_BoundaryID = null;
	private String cashInHand = null;
	private String chequeInHand = null;

	private String ID = "0";

	private String insertQuery;
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger.getLogger(CodeMapping.class);
	private TaskFailedException taskExc;

	public void setID(String aID) {
		ID = aID;
		isId = true;
	}

	public void setegBoundaryID(String xegBoundaryID) {
		eg_BoundaryID = xegBoundaryID;
		insertQuery = insertQuery + " egBoundaryID=" + xegBoundaryID + ",";
		isField = true;
	}

	public void setcashInHand(String xcashInHand) {
		cashInHand = xcashInHand;
	}

	public void setchequeInHand(String xchequeInHand) {
		chequeInHand = xchequeInHand;
	}

	public void insert(Connection connection) throws SQLException {
		setID(String.valueOf(PrimaryKeyGenerator.getNextKey("CodeMapping")));
		insertQuery = "insert into CodeMapping (id,eg_BoundaryID,cashInHand,chequeInHand) values(?,?,?,?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		// Statement statement = connection.createStatement();
		PreparedStatement pstmt = connection.prepareStatement(insertQuery);
		pstmt.setString(1, ID);
		pstmt.setString(2, eg_BoundaryID);
		pstmt.setString(3, cashInHand);
		pstmt.setString(4, chequeInHand);
		pstmt.executeUpdate();

	}

	public void update(Connection connection) throws SQLException {
		try {
			newUpdate(connection);
		} catch (TaskFailedException e) {
			LOGGER.error("error inside update" + e.getMessage(),e);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update billcollector set ");
		if (cashInHand != null)
			query.append("CASHINHAND=?,");
		if (chequeInHand != null)
			query.append("CHEQUEINHAND=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where EG_BOUNDARYID=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (cashInHand != null)
				pstmt.setString(i++, cashInHand);
			if (chequeInHand != null)
				pstmt.setString(i++, chequeInHand);
			pstmt.setString(i++, eg_BoundaryID);

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
