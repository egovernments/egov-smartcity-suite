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
