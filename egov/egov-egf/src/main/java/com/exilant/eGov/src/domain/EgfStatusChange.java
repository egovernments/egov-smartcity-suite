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
 * Created on Jun 20, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Sapna
 * @version 1.0
 * 
 *          TODO To change the template for this generated type comment go to
 *          Window - Preferences - Java - Code Style - Code Templates
 */
public class EgfStatusChange {
	private static final Logger LOGGER = Logger
			.getLogger(EgfStatusChange.class);
	private String id = null;
	private String moduletype = null;
	private String moduleid = null;
	private String fromstatus = null;
	private String tostatus = null;
	private String createdby = null;
	private String lastmodifieddate;
	private boolean isModuleid = false, isField = false;
	private static TaskFailedException taskExc;

	/**
	 * @return Returns the createdby.
	 */
	public String getCreatedby() {
		return createdby;
	}

	/**
	 * @param createdby
	 *            The createdby to set.
	 */
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
		
		isField = true;
	}

	/**
	 * @return Returns the fromstatus.
	 */
	public String getFromstatus() {
		return fromstatus;
	}

	/**
	 * @param fromstatus
	 *            The fromstatus to set.
	 */
	public void setFromstatus(String fromstatus) {
		this.fromstatus = fromstatus;
		isField = true;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the lastmodifieddate.
	 */
	public String getLastmodifieddate() {
		return lastmodifieddate;
	}

	/**
	 * @param lastmodifieddate
	 *            The lastmodifieddate to set.
	 */
	public void setLastmodifieddate(String lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
		isField = true;
	}

	/**
	 * @return Returns the moduleid.
	 */
	public String getModuleid() {
		return moduleid;
	}

	/**
	 * @param moduleid
	 *            The moduleid to set.
	 */
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
		isModuleid = true;

	}

	/**
	 * @return Returns the moduletype.
	 */
	public String getModuletype() {
		return moduletype;
	}

	/**
	 * @param moduletype
	 *            The moduletype to set.
	 */
	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
		isField = true;
	}

	/**
	 * @return Returns the tostatus.
	 */
	public String getTostatus() {
		return tostatus;
	}

	/**
	 * @param tostatus
	 *            The tostatus to set.
	 */
	public void setTostatus(String tostatus) {
		this.tostatus = tostatus;
		isField = true;
	}

	/**
	 * This method inserts the record in the EGW_SATUSCHANGE table
	 * 
	 * @param connection
	 * @return
	 */
	public void insert(Connection connection) throws SQLException {
		PreparedStatement psmt = null;
		try {
			String insertQuery = "";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
					.getDefault());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
					Locale.getDefault());
			Date last_date = (Date) formatter.parse(this.lastmodifieddate);
			setId(String.valueOf(PrimaryKeyGenerator
					.getNextKey("EGW_SATUSCHANGE")));
			if(LOGGER.isInfoEnabled())     LOGGER.info("Berfore fetching details getID()---" + getId());
			insertQuery = "INSERT INTO EGW_SATUSCHANGE (ID, MODULETYPE, MODULEID, FROMSTATUS,TOSTATUS,CREATEDBY,LASTMODIFIEDDATE)"
					+ "values(?,?,?,?,?,?,to_date(?,'dd-Mon-yyyy HH24:MI:SS'))";

			if(LOGGER.isInfoEnabled())     LOGGER.info("insertQuery: " + insertQuery);
			psmt = connection.prepareStatement(insertQuery);
			psmt.setString(1, this.id);
			psmt.setString(2, this.moduletype);
			psmt.setString(3, this.moduleid);
			psmt.setString(4, this.fromstatus);
			psmt.setString(5, this.tostatus);
			psmt.setString(6, this.createdby);
			psmt.setString(7, this.lastmodifieddate);
			psmt.executeUpdate();
		}

		catch (Exception sql) {
			if(LOGGER.isInfoEnabled())     LOGGER.info("Exception in inserting query");
		}

		// if(LOGGER.isInfoEnabled())     LOGGER.info("INSERT QUERY IS:"+insertQuery);
	}

	public void update(Connection connection) throws SQLException {
		PreparedStatement psmt = null;
		try {
			if (isModuleid && isField) {
				newUpdate(connection);
			}
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(), e);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		StringBuilder query = new StringBuilder(500);
		PreparedStatement pstmt = null;
		query.append("update EGW_SATUSCHANGE set ");
		if (moduletype != null)
			query.append("moduletype=?,");
		if (moduleid != null)
			query.append("moduleid=?,");
		if (fromstatus != null)
			query.append("fromstatus=?,");
		if (tostatus != null)
			query.append("tostatus=?,");
		if (createdby != null)
			query.append("createdby=?,");
		if (lastmodifieddate != null)
			query.append("lastmodifieddate=to_date(?,'dd-Mon-yyyy HH24:MI:SS'),");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (moduletype != null)
				pstmt.setString(i++, moduletype);
			if (moduleid != null)
				pstmt.setString(i++, moduleid);
			if (fromstatus != null)
				pstmt.setString(i++, fromstatus);
			if (tostatus != null)
				pstmt.setString(i++, tostatus);
			if (createdby != null)
				pstmt.setString(i++, createdby);
			if (lastmodifieddate != null)
				pstmt.setString(i++, lastmodifieddate);

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
