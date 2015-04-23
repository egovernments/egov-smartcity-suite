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

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

public class ReceiptHeader {
	private String id = null;
	private String voucherHeaderId = null;
	private String type = null;
	private String wardId = null;
	private String billCollectorId = null;
	private String counterId = null;
	private String bankId = null;
	private String bankBranchId = null;
	private String bankAccountNumberId = null;
	private String modeOfCollection = "";
	private String chequeId = null;
	private String cashAmount = "0";
	private String narration = "";
	private String revenueSource = null;
	private String isReversed = "0";
	private String agent = "";
	private String receiptNo = "";
	private String manualReceiptNo = "";
	private boolean isId = false, isField = false;
	private TaskFailedException taskExc;
	private String updateQuery = "UPDATE receiptheader SET";

	// private Connection connection;
	// private Statement statement;

	private String insertQuery;
	private static final Logger LOGGER = Logger.getLogger(ReceiptHeader.class);

	

	public void insert(Connection connection) throws SQLException {
		EGovernCommon commonMethods = new EGovernCommon();
		narration = commonMethods.formatString(narration);
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("ReceiptHeader")));
		insertQuery = "INSERT INTO receiptheader (Id, VoucherHeaderId, Type, WardId, BillCollectorId, "
				+ "CounterId, BankId, BankBranchId, BankAccountNumberId, ModeOfCollection, ChequeId, "
				+ "CashAmount, Narration, RevenueSource,isReversed,CASHIER,receiptno, manualReceiptNumber) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		PreparedStatement pst = connection.prepareStatement(insertQuery);
		pst.setString(1, id);
		pst.setString(2, voucherHeaderId);
		pst.setString(3, type);
		pst.setString(4, wardId);
		pst.setString(5, billCollectorId);
		pst.setString(6, counterId);
		pst.setString(7, bankId);
		pst.setString(8, bankBranchId);
		pst.setString(9, bankAccountNumberId);
		pst.setString(10, modeOfCollection);
		pst.setString(11, chequeId);
		pst.setString(12, cashAmount);
		pst.setString(13, narration);
		pst.setString(14, revenueSource);
		pst.setString(15, isReversed);
		pst.setString(16, agent);
		pst.setString(17, receiptNo);
		pst.setString(18, manualReceiptNo);
		pst.executeUpdate();
		pst.close();

	}

	public void reverse(Connection connection, String cgNum)
			throws SQLException {
		String updateQuery = "update receiptheader set isreversed=1 where voucherheaderid in(select id from voucherheader where cgn= ?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
		PreparedStatement pst = connection.prepareStatement(updateQuery);
		pst.executeUpdate();
		pst.close();
	}

	public void update(Connection connection) throws SQLException {
		if (isId && isField) {
			try{
			newUpdate(connection);
			}catch(Exception e){
				LOGGER.error("Exception in update method"+e.getMessage());
			}
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commonMethods = new EGovernCommon();
		narration = commonMethods.formatString(narration);
		PreparedStatement pstmt=null;
		StringBuilder query = new StringBuilder(500);
		query.append("update receiptheader set ");
		if (voucherHeaderId != null)
			query.append("voucherHeaderId=?,");
		if (type != null)
			query.append("type=?,");
		if (wardId != null)
			query.append("wardId=?,");
		if (billCollectorId != null)
			query.append("billCollectorId=?,");
		if (counterId != null)
			query.append("counterId=?,");
		if (bankId != null)
			query.append("bankId=?,");
		if (bankBranchId != null)
			query.append("bankBranchId=?,");
		if (bankAccountNumberId != null)
			query.append("bankAccountNumberId=?,");
		if (modeOfCollection != null)
			query.append("modeOfCollection=?,");
		if (chequeId != null)
			query.append("chequeId=?,");
		if (cashAmount != null)
			query.append("cashAmount=?,");
		if (narration != null)
			query.append("narration=?,");
		if (revenueSource != null)
			query.append("revenueSource=?,");
		if (isReversed != null)
			query.append("isReversed=?,");
		if (receiptNo != null)
			query.append("receiptNo=?,");
		if (manualReceiptNo != null)
			query.append("manualReceiptNumber=?,");
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
			if (wardId != null)
				pstmt.setString(i++, wardId);
			if (billCollectorId != null)
				pstmt.setString(i++, billCollectorId);
			if (counterId != null)
				pstmt.setString(i++, counterId);
			if (bankId != null)
				pstmt.setString(i++, bankId);
			if (bankBranchId != null)
				pstmt.setString(i++, bankBranchId);
			if (bankAccountNumberId != null)
				pstmt.setString(i++, bankAccountNumberId);
			if (modeOfCollection != null)
				pstmt.setString(i++, modeOfCollection);
			if (chequeId != null)
				pstmt.setString(i++, chequeId);
			if (cashAmount != null)
				pstmt.setString(i++, cashAmount);
			if (narration != null)
				pstmt.setString(i++, narration);
			if (revenueSource != null)
				pstmt.setString(i++, revenueSource);
			if (isReversed != null)
				pstmt.setString(i++, isReversed);
			if (receiptNo != null)
				pstmt.setString(i++, receiptNo);
			if (manualReceiptNo != null)
				pstmt.setString(i++, manualReceiptNo);
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
	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void setVoucherHeaderId(String aVoucherHeaderId) {
		voucherHeaderId = aVoucherHeaderId;
		isField = true;
	}

	public void setType(String aType) {
		type = aType;
		isField = true;
	}

	public void setWardId(String aWardId) {
		wardId = aWardId;
		isField = true;
	}

	public void setBillCollectorId(String aBillCollectorId) {
		billCollectorId = aBillCollectorId;
		
		isField = true;
	}

	public void setCounterId(String aCounterId) {
		counterId = aCounterId;
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

	public void setModeOfCollection(String aModeOfCollection) {
		modeOfCollection = aModeOfCollection;
		isField = true;
	}

	public void setChequeId(String aChequeId) {
		chequeId = aChequeId;
		isField = true;
	}

	public void setCashAmount(String aCashAmount) {
		cashAmount = aCashAmount;
		isField = true;
	}

	public void setNarration(String aNarration) {
		narration = aNarration;
		isField = true;
	}

	public void setRevenueSource(String aRevenueSource) {
		revenueSource = aRevenueSource;
		isField = true;
	}

	public void setIsReversed(String aIsReversed) {
		isReversed = aIsReversed;
		isField = true;
	}

	public void setAgent(String aAgent) {
		agent = aAgent;
	}

	public void setreceiptNo(String areceiptNo) {
		receiptNo = areceiptNo;
		isField = true;
	}

	public void setManualReceiptNo(String aManualReceiptNo) {
		manualReceiptNo = aManualReceiptNo;
		isField = true;
	}

}
