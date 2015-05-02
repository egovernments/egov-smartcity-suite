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

public class BankAccount {
	private String id = null;
	private String branchId = null;
	private String accountNumber = null;
	private String accountType = null;
	private String narration = null;
	private String isActive = "1";
	private String created = "1-Jan-1900";
	private String modifiedBy = null;
	private String lastModified = "1-Jan-1900";
	private String glcodeID = null;
	private String fundID = null;
	private String payTo = "";
	private TaskFailedException taskExc;
	private String currentBalance = "0";
	private String type;
	private String updateQuery = "UPDATE bankaccount SET";
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger.getLogger(BankAccount.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
			.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
			Locale.getDefault());

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	/*
	 * public void setBranchId(String aBranchId){ branchId = aBranchId;
	 * updateQuery = updateQuery + " branchId=" + branchId + ","; isField =
	 * true;} public void setAccountNumber(String aAccountNumber){ accountNumber
	 * = aAccountNumber; updateQuery = updateQuery + " accountNumber='" +
	 * accountNumber + "',"; isField = true;} public void setAccountType(String
	 * aAccountType){ accountType = aAccountType; updateQuery = updateQuery +
	 * " accountType='" + accountType + "',"; isField = true;} public void
	 * setNarration(String aNarration){ narration = aNarration; updateQuery =
	 * updateQuery + " narration='" + narration + "',"; isField = true;} public
	 * void setPayTo(String apayTo){ payTo = apayTo; updateQuery = updateQuery +
	 * " payTo='" + payTo + "',"; isField = true;} public void
	 * setIsActive(String aIsActive){ isActive = aIsActive; updateQuery =
	 * updateQuery + " isActive=" + isActive + ","; isField = true;} public void
	 * setCreated(String aCreated){ created = aCreated; updateQuery =
	 * updateQuery + " created='" + created + "',"; isField = true;} public void
	 * setModifiedBy(String aModifiedBy){ modifiedBy = aModifiedBy; updateQuery
	 * = updateQuery + " modifiedBy=" + modifiedBy + ","; isField = true;}
	 * public void setLastModified(String aLastModified){ lastModified =
	 * aLastModified; updateQuery = updateQuery + " lastModified='" +
	 * lastModified + "',"; isField = true;} public void setGlcodeId(String
	 * aGlcodeID){ glcodeID = aGlcodeID; updateQuery = updateQuery +
	 * " glcodeid=" + glcodeID + ","; isField = true;} public void
	 * setCurrentBalance(String aCurrentBalance){ currentBalance =
	 * aCurrentBalance; updateQuery = updateQuery + " currentBalance=" +
	 * currentBalance + ","; isField = true;} public void setFundID(String
	 * aFundID){ fundID = aFundID; updateQuery = updateQuery + "fundid=" +
	 * fundID + ","; isField = true;} public void setType(String aType){ type =
	 * aType; updateQuery = updateQuery + "type='" + aType + "',"; isField =
	 * true;}
	 */

	public void insert(Connection connection) throws TaskFailedException,
			SQLException {
		EGovernCommon commonmethods = new EGovernCommon();
		created = commonmethods.getCurrentDate();
		PreparedStatement pst = null;
		try {
			created = formatter.format(sdf.parse(created));
			setCreated(created);
			setLastModified(created);
			narration = commonmethods.formatString(narration);
			setId(String.valueOf(PrimaryKeyGenerator.getNextKey("BankAccount")));
			String insertQuery = "INSERT INTO bankaccount (Id, BranchId, AccountNumber, AccountType, Narration,PayTo, IsActive, "
					+ "Created, ModifiedBy, LastModified,glcodeid, CurrentBalance,fundid,type) "
					+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			pst = connection.prepareStatement(insertQuery);
			pst.setString(1, id);
			pst.setString(2, branchId);
			pst.setString(3, accountNumber);
			pst.setString(4, accountType);
			pst.setString(5, narration);
			pst.setString(6, payTo);
			pst.setString(7, isActive);
			pst.setString(8, created);
			pst.setString(9, modifiedBy);
			pst.setString(10, lastModified);
			pst.setString(11, glcodeID);
			pst.setString(12, currentBalance);
			pst.setString(13, fundID);
			pst.setString(14, type);
			pst.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in insert" + e.getMessage(),e);
			throw taskExc;
		} finally {
			try {
				pst.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block");
			}
		}

	}

	public void update(Connection connection) throws TaskFailedException,
			SQLException {
		try {
			newUpdate(connection);
		} catch (Exception e) {
			LOGGER.error("Error inside uodating" + e.getMessage(), e);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate();
		PreparedStatement pstmt = null;
		try {
			created = formatter.format(sdf.parse(created));
			narration = commommethods.formatString(narration);
		} catch (ParseException parseExp) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(), parseExp);
		}
		setCreated(created);
		setLastModified(created);
		StringBuilder query = new StringBuilder(500);
		query.append("update bankaccount set ");
		if (branchId != null)
			query.append("BRANCHID=?,");
		if (accountNumber != null)
			query.append("ACCOUNTNUMBER=?,");
		if (accountType != null)
			query.append("ACCOUNTTYPE=?,");
		if (narration != null)
			query.append("NARRATION=?,");
		if (isActive != null)
			query.append("ISACTIVE=?,");
		if (created != null)
			query.append("CREATED=?,");
		if (modifiedBy != null)
			query.append("MODIFIEDBY=?,");
		if (lastModified != null)
			query.append("LASTMODIFIED=?,");
		if (glcodeID != null)
			query.append("GLCODEID=?,");
		if (fundID != null)
			query.append("FUNDID=?,");
		if (payTo != null)
			query.append("PAYTO=?,");
		if (currentBalance != null)
			query.append("CURRENTBALANCE=?,");
		if (type != null)
			query.append("TYPE=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (branchId != null)
				pstmt.setString(i++, branchId);
			if (accountNumber != null)
				pstmt.setString(i++, accountNumber);
			if (accountType != null)
				pstmt.setString(i++, accountType);
			if (narration != null)
				pstmt.setString(i++, narration);
			if (isActive != null)
				pstmt.setString(i++, isActive);
			if (created != null)
				pstmt.setString(i++, created);
			if (modifiedBy != null)
				pstmt.setString(i++, modifiedBy);
			if (lastModified != null)
				pstmt.setString(i++, lastModified);
			if (glcodeID != null)
				pstmt.setString(i++, glcodeID);
			if (fundID != null)
				pstmt.setString(i++, fundID);
			if (payTo != null)
				pstmt.setString(i++, payTo);
			if (currentBalance != null)
				pstmt.setString(i++, currentBalance);
			if (type != null)
				pstmt.setString(i++, type);
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

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getGlcodeID() {
		return glcodeID;
	}

	public void setGlcodeID(String glcodeID) {
		this.glcodeID = glcodeID;
	}

	public String getFundID() {
		return fundID;
	}

	public void setFundID(String fundID) {
		this.fundID = fundID;
	}

	public String getPayTo() {
		return payTo;
	}

	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
