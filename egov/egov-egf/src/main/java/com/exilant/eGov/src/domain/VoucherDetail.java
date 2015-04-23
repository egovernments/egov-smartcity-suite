/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class VoucherDetail {
	private String id = null;
	private String lineId = null;
	private String voucherHeaderId = null;
	private String glCode = null;
	private String accountName = null;
	private String debitAmount = "0";
	private String creditAmount = "0";
	private String narration = "";
	private static final Logger LOGGER = Logger.getLogger(VoucherDetail.class);
	private String updateQuery = "UPDATE voucherdetail SET";
	private boolean isId = false, isField = false;
	private TaskFailedException taskExc;

	public VoucherDetail() {
	}

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	/* inserts the Value in VoucherDetail Table */
	public void insert(Connection connection) throws SQLException,
			TaskFailedException {
		EGovernCommon commonmethods = new EGovernCommon();
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside VoucherDetail Narration is" + narration);
		if (narration != null && narration.length() != 0)
			narration = commonmethods.formatString(narration);
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("VoucherDetail")));

		String insertQuery = "INSERT INTO voucherdetail (ID, LineID, VoucherHeaderID, GLCode, AccountName, "
				+ "DebitAmount, CreditAmount, Narration) "
				+ "VALUES( ?, ?, ?, ?, ?, ?, ?, ?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		PreparedStatement pst = connection.prepareStatement(insertQuery);
		pst.setString(1, id);
		pst.setString(2, lineId);
		pst.setString(3, voucherHeaderId);
		pst.setString(4, glCode);
		pst.setString(5, accountName);
		pst.setString(6, debitAmount);
		pst.setString(7, creditAmount);
		pst.setString(8, narration);
		pst.executeUpdate();
		updateQuery = "UPDATE voucherdetail SET";
		pst.close();
	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
		try {
			newUpdate(connection);
		} catch (Exception e) {
			LOGGER.error("error inside update " + e.getMessage(), e);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		PreparedStatement pstmt = null;
		if (narration != null && narration.length() != 0)
			narration = commommethods.formatString(narration);
		StringBuilder query = new StringBuilder(500);
		query.append("update voucherdetail set ");
		if (lineId != null)
			query.append("LINEID=?,");
		if (voucherHeaderId != null)
			query.append("VOUCHERHEADERID=?,");
		if (glCode != null)
			query.append("GLCODE=?,");
		if (accountName != null)
			query.append("ACCOUNTNAME=?,");
		if (debitAmount != null)
			query.append("DEBITAMOUNT=?,");
		if (creditAmount != null)
			query.append("CREDITAMOUNT=?,");
		if (narration != null)
			query.append("NARRATION=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (lineId != null)
				pstmt.setString(i++, lineId);
			if (voucherHeaderId != null)
				pstmt.setString(i++, voucherHeaderId);
			if (glCode != null)
				pstmt.setString(i++, glCode);
			if (accountName != null)
				pstmt.setString(i++, accountName);
			if (debitAmount != null)
				pstmt.setString(i++, debitAmount);
			if (creditAmount != null)
				pstmt.setString(i++, creditAmount);
			if (narration != null)
				pstmt.setString(i++, narration);
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

	public String getLineID() {
		return lineId;
	}

	public void setLineID(String lineId) {
		this.lineId = lineId;
	}

	public String getVoucherHeaderID() {
		return voucherHeaderId;
	}

	public void setVoucherHeaderID(String voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}

	public String getGLCode() {
		return glCode;
	}

	public void setGLCode(String glCode) {
		this.glCode = glCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}
}
