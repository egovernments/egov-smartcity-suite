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
