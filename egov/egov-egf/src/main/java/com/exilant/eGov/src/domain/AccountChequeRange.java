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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

public class AccountChequeRange {
	private String id = "";
	private String bankAccountID = "";
	private String fromChequeNumber = "";
	private String toChequeNumber = "";
	private String receivedDate = "1-Jan-1900";
	private String isExhausted = "0";
	private String nextChqNo = "";
	private String isAllottedTo = "1";
	private String createdBy;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedDate;
	private TaskFailedException taskExc;
	private static final Logger LOGGER = Logger
			.getLogger(AccountChequeRange.class);
	private String updateQuery = "UPDATE bankbranch SET";

	public void insert(final Connection connection) throws SQLException,
			TaskFailedException {
		PreparedStatement pst = null;
		try {
			setId(String.valueOf(PrimaryKeyGenerator
					.getNextKey("EGF_ACCOUNT_CHEQUES")));

			String insertQuery = "INSERT INTO EGF_ACCOUNT_CHEQUES (Id, BankAccountID, FromChequeNumber, ToChequeNumber, ReceivedDate,isExhausted,nextChqNo,AllotedTo,createdBy,createdDate,lastModifiedBy,lastModifiedDate) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
			pst = connection.prepareStatement(insertQuery);
			pst.setString(1, id);
			pst.setString(2, bankAccountID);
			pst.setString(3, fromChequeNumber);
			pst.setString(4, toChequeNumber);
			pst.setString(5, receivedDate);
			pst.setString(6, isExhausted);
			pst.setString(7, nextChqNo);
			pst.setString(8, isAllottedTo);
			pst.setString(9, createdBy);
			pst.setString(10, createdDate);
			pst.setString(11, lastModifiedBy);
			pst.setString(12, lastModifiedDate);
			pst.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exception in insert:" + e.getMessage());
			throw taskExc;
		} finally {
			try {
				pst.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block" + e.getMessage());
			}
		}

	}
	public void update(Connection con) throws TaskFailedException,
	SQLException {
SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
EGovernCommon commommethods = new EGovernCommon();
createdDate = commommethods.getCurrentDate(con);
PreparedStatement pstmt = null;
try {
	createdDate = formatter.format(sdf.parse(createdDate));
} catch (ParseException parseExp) {
	if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(), parseExp);
}
setCreatedDate(createdDate);
setLastModifiedDate(createdDate);
StringBuilder query = new StringBuilder(500);
query.append("update EGF_ACCOUNT_CHEQUES set ");
if (bankAccountID != null)
	query.append("bankAccountID=?,");
if (fromChequeNumber != null)
	query.append("fromChequeNumber=?,");
if (toChequeNumber != null)
	query.append("toChequeNumber=?,");
if (receivedDate != null)
	query.append("ReceivedDate=?,");
if (isExhausted != null)
	query.append("isExhausted=?,");
if (nextChqNo != null)
	query.append("nextChqNo=?,");
if (isAllottedTo != null)
	query.append("isAllottedTo=?,");
if (createdBy != null)
	query.append("createdBy=?,");
if (createdDate != null)
	query.append("createdDate=?,");
if (lastModifiedBy != null)
	query.append("lastModifiedBy=?,");
if (lastModifiedDate != null)
	query.append("lastModifiedDate=?,");
int lastIndexOfComma = query.lastIndexOf(",");
query.deleteCharAt(lastIndexOfComma);
query.append(" where id=?");
try {
	int i = 1;
	pstmt = con.prepareStatement(query.toString());
	if (bankAccountID != null)
		pstmt.setString(i++, bankAccountID);
	if (fromChequeNumber != null)
		pstmt.setString(i++, fromChequeNumber);
	if (toChequeNumber != null)
		pstmt.setString(i++, toChequeNumber);
	if (receivedDate != null)
		pstmt.setString(i++, receivedDate);
	if (isExhausted != null)
		pstmt.setString(i++, isExhausted);
	if (nextChqNo != null)
		pstmt.setString(i++, nextChqNo);
	if (isAllottedTo != null)
		pstmt.setString(i++, isAllottedTo);
	if (createdBy != null)
		pstmt.setString(i++, createdBy);
	if (createdDate != null)
		pstmt.setString(i++, createdDate);
	if (lastModifiedBy != null)
		pstmt.setString(i++, lastModifiedBy);
	if (lastModifiedDate != null)
		pstmt.setString(i++, lastModifiedDate);
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


	public String getCreatedBy() {
		return createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setId(final String aId) {
		id = aId;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void setBankAccountID(final String aBankAccountID) {
		bankAccountID = aBankAccountID;
		
	}

	public void setFromChequeNumber(final String aFromChequeNumber) {
		fromChequeNumber = aFromChequeNumber;
		
	}

	public void setToChequeNumber(final String aToChequeNumber) {
		toChequeNumber = aToChequeNumber;
		
	}

	public void setReceivedDate(final String aReceivedDate) {
		receivedDate = aReceivedDate;
		
	}

	public void setIsExhausted(final String aisExhausted) {
		isExhausted = aisExhausted;
		
	}

	public void setNextChqNo(final String anextChqNo) {
		nextChqNo = anextChqNo;
	
	}

	public void setIsAllotteTo(final String aIsAllottedTo) {
		isAllottedTo = aIsAllottedTo;
		
	}

	public void setCreatedBy(String acreatedBy) {
		createdBy = acreatedBy;
		
	}

	public void setCreatedDate(String acreatedDate) {
		createdDate = acreatedDate;
		
	}

	public void setLastModifiedBy(String alastModifiedBy) {
		lastModifiedBy = alastModifiedBy;
		
	}

	public void setLastModifiedDate(String alastModifiedDate) {
		lastModifiedDate = alastModifiedDate;
		
	}

	
}
