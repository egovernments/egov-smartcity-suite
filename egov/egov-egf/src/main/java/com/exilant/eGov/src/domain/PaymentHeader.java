/*
 * Created on Jan 19, 2005
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

public class PaymentHeader {
	private String id = null;
	private String voucherHeaderId = null;
	private String type = null;
	private String bankId = null;
	private String bankBranchId = null;
	private String bankAccountNumberId = null;
	private String chequeId = null;
	private String miscBillDetailId = null;
	private String cashName = null;
	private String narration = "";
	private TaskFailedException taskExc;
	private static final Logger LOGGER = Logger.getLogger(PaymentHeader.class);

	private String updateQuery = "UPDATE paymentheader SET";
	private boolean isId = false, isField = false;

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void setVoucherHeaderID(String aVoucherHeaderID) {
		voucherHeaderId = aVoucherHeaderID;
		isField = true;
	}

	public void setType(String aType) {
		type = aType;
		isField = true;
	}

	public void setBankId(String aBankId) {
		bankId = aBankId;
		isField = true;
	}

	public void setBankBranchId(String aBankBranchId) {
		bankBranchId = aBankBranchId;
		isField = true;
	}

	public void setBankAccountNumberId(String aBankAccountNumberId) {
		bankAccountNumberId = aBankAccountNumberId;
		isField = true;
	}

	public void setChequeId(String aChequeId) {
		chequeId = aChequeId;
		isField = true;
	}

	public void setMiscBillDetailId(String aMiscBillDetailId) {
		miscBillDetailId = aMiscBillDetailId;
		isField = true;
	}

	public void setCashName(String aCashName) {
		cashName = aCashName;
		isField = true;
	}

	public void setNarration(String aNarration) {
		narration = aNarration;
		isField = true;
	}

	public void insert(Connection connection) throws SQLException {
		EGovernCommon commonMethods = new EGovernCommon();
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside PaymentHeader Narration is" + narration);

		if (narration != null && narration.length() != 0)
			narration = commonMethods.formatString(narration);

		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("PaymentHeader")));

		String insertQuery = "INSERT INTO paymentheader (Id, VoucherHeaderID, Type,BankAccountNumberId) VALUES ( ?, ?, ?, ?)";

		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		PreparedStatement pst = connection.prepareStatement(insertQuery);
		pst.setString(1, id);
		pst.setString(2, voucherHeaderId);
		pst.setString(3, type);
		pst.setString(4, bankAccountNumberId);
		pst.executeUpdate();
		pst.close();
	}

	public void reverse(Connection connection, String cgNum)
			throws SQLException {
		String updateQuery = "update paymentheader  set isreversed=1 where voucherheaderid in(select id from voucherheader where cgn= ?)";
		PreparedStatement pst = connection.prepareStatement(updateQuery);
		pst.setString(1, cgNum);
		if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
		pst.executeUpdate();
		pst.close();
	}

	/**
	 * Fucntion for update paymentheader
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public void update(Connection connection) throws SQLException {
		if (isId && isField) {
			try {
				newUpdate(connection);
			} catch (Exception e) {
				LOGGER.error("Updation Failed" + e.getMessage());
			}
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update paymentheader set ");
		if (voucherHeaderId != null)
			query.append("voucherHeaderId=?,");
		if (type != null)
			query.append("type=?,");
		if (bankId != null)
			query.append("bankId=?,");
		if (bankBranchId != null)
			query.append("bankBranchId=?,");
		if (bankAccountNumberId != null)
			query.append("bankAccountNumberId=?,");
		if (chequeId != null)
			query.append("chequeId=?,");
		if (miscBillDetailId != null)
			query.append("miscBillDetailId=?,");
		if (cashName != null)
			query.append("cashName=?,");
		if (narration != null)
			query.append("narration=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (voucherHeaderId != null)
				pstmt.setString(i++, voucherHeaderId);
			if (type != null)
				pstmt.setString(i++, type);
			if (bankId != null)
				pstmt.setString(i++, bankId);
			if (bankBranchId != null)
				pstmt.setString(i++, bankBranchId);
			if (bankAccountNumberId != null)
				pstmt.setString(i++, bankAccountNumberId);
			if (chequeId != null)
				pstmt.setString(i++, chequeId);
			if (miscBillDetailId != null)
				pstmt.setString(i++, miscBillDetailId);
			if (cashName != null)
				pstmt.setString(i++, cashName);
			if (narration != null)
				pstmt.setString(i++, narration);
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
