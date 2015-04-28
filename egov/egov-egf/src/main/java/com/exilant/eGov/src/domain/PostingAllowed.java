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
