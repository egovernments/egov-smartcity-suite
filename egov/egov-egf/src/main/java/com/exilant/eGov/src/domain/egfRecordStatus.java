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
/**
 * @author Raja
 * @Domain Object for egfRecordStatus table
 *
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class egfRecordStatus {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	private PreparedStatement pstmt = null;
	private static final Logger LOGGER = Logger
			.getLogger(egfRecordStatus.class);
	private String id = null;
	private String updateQuery = "UPDATE egf_record_status SET";
	private boolean isId = false, isField = false;
	private String voucherheaderId = null;
	private String record_Type = null;
	private String status = null;
	private String updateTime = null;
	private String userId = null;
	private String voucherHeader_voucherDate = null;
	private String effectiveDate = "1-Jan-1900";
	private String currentUserId = null;
	private TaskFailedException taskExc;
	public void setId(String aId) {
		id = aId;
	}

	public void setVoucherheaderId(String avoucherheaderId) {
		voucherheaderId = avoucherheaderId;
		isField = true;
		isId = true;
	}

	public void setEffectiveDate(String aeffectiveDate) {
		effectiveDate = aeffectiveDate;
		isField = true;
	}

	public void setRecord_Type(String arecord_Type) {
		record_Type = arecord_Type;
		isField = true;
	}

	public void setStatus(String astatus) {
		status = astatus;
		isField = true;
	}

	public void setUserId(String auserId) {
		userId = auserId;
		isField = true;
	}

	public void setVoucherHeader_voucherDate(String avoucherHeader_voucherDate) {
		voucherHeader_voucherDate = avoucherHeader_voucherDate;
		isField = true;
	}

	public void setCurrentUserId(String acurrentUserId) {
		currentUserId = acurrentUserId;
		isField = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public String getvoucherheaderId() {
		return voucherheaderId;
	}

	public String getrecord_Type() {
		return record_Type;
	}

	public String getstatus() {
		return status;
	}

	public String getupdateTime() {
		return updateTime;
	}

	public String getuserId() {
		return userId;
	}

	public String geteffectiveDate() {
		return effectiveDate;
	}

	public void insert(Connection connection) throws SQLException,
			TaskFailedException {
		ResultSet rs = null;
		// setId(
		// String.valueOf(PrimaryKeyGenerator.getNextKey("EGF_RECORD_STATUS"))
		// );
		// String insertQuery =
		// "Insert into EGF_RECORD_STATUS(ID,voucherheaderid,RECORD_TYPE,STATUS,updatedtime,"
		// +
		// "USERID) values("+id+","+voucherheaderId+",'"+record_Type+"',"+status+
		// ",to_date('"
		// +effectiveDate+"','dd-Mon-yyyy HH24:MI:SS'),"+userId+")";
		setId(String.valueOf(PrimaryKeyGenerator
				.getNextKey("EGF_RECORD_STATUS")));
		String insertQuery = "Insert into EGF_RECORD_STATUS(ID,voucherheaderid,RECORD_TYPE,STATUS,updatedtime,USERID) values (?,?,?,?,?,?)";
		pstmt = connection.prepareStatement(insertQuery);
		pstmt.setString(1, id);
		pstmt.setString(2, voucherheaderId);
		pstmt.setString(3, record_Type);
		pstmt.setString(4, status);
		pstmt.setString(5, effectiveDate);
		pstmt.setString(6, userId);
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		pstmt.executeUpdate();
	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
		if (isId && isField) {
			newUpdate(connection);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update egf_record_status set ");
		if (voucherheaderId != null)
			query.append("voucherheaderId=?,");
		if (record_Type != null)
			query.append("record_Type=?,");
		if (status != null)
			query.append("status=?,");
		if (updateTime != null)
			query.append("updatedTime=?,");
		if (userId != null)
			query.append("userId=?,");
				
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());

			if (voucherheaderId != null)
				pstmt.setString(i++, voucherheaderId);
			if (record_Type != null)
				pstmt.setString(i++, record_Type);
			if (status != null)
				pstmt.setString(i++, status);
			if (updateTime != null)
				pstmt.setString(i++, updateTime);
			if (userId != null)
				pstmt.setString(i++, userId);
			pstmt.setString(i++, id);

			pstmt.executeQuery();
			
		} catch (Exception e) {
			LOGGER.error("Exp in update: "+e.getMessage(),e);
			throw taskExc;
		}finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block of update");
			}
		}
	}

}
