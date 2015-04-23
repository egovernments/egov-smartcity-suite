/*
 * Created on Apr 19, 2005 
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class TransactionSummary {
	private String id = null;
	private String financialYearId = null;
	private String glCodeId = null;
	private String openingDebitBalance = "";
	private String openingCreditBalance = "";
	private String debitAmount = "";
	private String creditAmount = "";
	private String accountDetailTypeId = null;
	private String accountDetailKey = null;
	private String fundId = null;
	private PreparedStatement pstmt = null;
	private static TaskFailedException taskExc;
	private ResultSet rs = null;
	private String updateQuery = "UPDATE TransactionSummary SET";
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger
			.getLogger(SupplierBillDetail.class);

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void setFinancialYearId(String aFinancialYearId) {
		financialYearId = aFinancialYearId;
		isField = true;
	}

	public void setGLCodeId(String aGLCodeId) {
		glCodeId = aGLCodeId;
		isField = true;
	}

	public void setOpeningDebitBalance(String aOpeningDebitBalance) {
		openingDebitBalance = aOpeningDebitBalance;
		isField = true;
	}

	public void setOpeningCreditBalance(String aOpeningCreditBalance) {
		openingCreditBalance = aOpeningCreditBalance;
		isField = true;
	}

	public void setDebitAmount(String aDebitAmount) {
		debitAmount = aDebitAmount;
		isField = true;
	}

	public void setCreditAmount(String aCreditAmount) {
		creditAmount = aCreditAmount;
		isField = true;
	}

	public void setAccountDetailTypeId(String aAccountDetailTypeId) {
		accountDetailTypeId = aAccountDetailTypeId;
		isField = true;
	}

	public void setAccountDetailKey(String aAccountDetailKey) {
		accountDetailKey = aAccountDetailKey;
		isField = true;
	}

	public void setFundId(String aFundId) {
		fundId = aFundId;
		isField = true;
	}

	public void insert(Connection connection) throws SQLException {
		// EGovernCommon commommethods = new EGovernCommon();

		setId(String.valueOf(PrimaryKeyGenerator
				.getNextKey("TransactionSummary")));

		String insertQuery = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid,openingdebitbalance, openingcreditbalance, debitamount,creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId) VALUES (?,?,?,?,?,?,?,?,?,?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		pstmt = connection.prepareStatement(insertQuery);
		pstmt.setString(1, id);
		pstmt.setString(2, financialYearId);
		pstmt.setString(3, glCodeId);
		pstmt.setString(4, openingDebitBalance);
		pstmt.setString(5, openingCreditBalance);
		pstmt.setString(6, debitAmount);
		pstmt.setString(7, creditAmount);
		pstmt.setString(8, accountDetailTypeId);
		pstmt.setString(9, accountDetailKey);
		pstmt.setString(10, fundId);
		rs = pstmt.executeQuery();

	}

	public void update(Connection connection) throws SQLException ,TaskFailedException{
		if (isId && isField) {
			newUpdate(connection);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		
		StringBuilder query = new StringBuilder(500);
		query.append("update TransactionSummary set ");
		if (financialYearId != null)
			query.append("financialYearId=?,");
		if (glCodeId != null)
			query.append("glCodeId=?,");
		if (openingDebitBalance != null)
			query.append("openingDebitBalance=?,");
		if (openingCreditBalance != null)
			query.append("openingCreditBalance=?,");
		if (debitAmount != null)
			query.append("debitAmount=?,");
		if (creditAmount != null)
			query.append("creditAmount=?,");
		if (accountDetailTypeId != null)
			query.append("accountDetailTypeId=?,");
		if (accountDetailKey != null)
			query.append("accountDetailKey=?,");
		if (fundId != null)
			query.append("fundId=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (financialYearId != null)
				pstmt.setString(i++, financialYearId);
			if (glCodeId != null)
				pstmt.setString(i++, glCodeId);
			if (openingDebitBalance != null)
				pstmt.setString(i++, openingDebitBalance);
			if (openingCreditBalance != null)
				pstmt.setString(i++, openingCreditBalance);
			if (debitAmount != null)
				pstmt.setString(i++, debitAmount);
			if (creditAmount != null)
				pstmt.setString(i++, creditAmount);
			if (accountDetailTypeId != null)
				pstmt.setString(i++, accountDetailTypeId);
			if (accountDetailKey != null)
				pstmt.setString(i++, accountDetailKey);
			if (fundId != null)
				pstmt.setString(i++, fundId);
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
