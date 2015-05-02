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

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.EgovMasterDataCaching;

/**
 * @author pushpendra.singh
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

public class Fund {
	private String id = null;
	private String code = null;
	private String name = null;
	private String type = null;
	private String lLevel = null;
	private String parentId = null;
	private String openingDebitBalance = "0";
	private String openingCreditBalance = "0";
	private String transactionDebitAmount = "0";
	private String transactionCreditAmount = "0";
	private String isActive = "1";
	private String lastModified = "1-Jan-1900";
	private String created = "1-Jan-1900";
	private String modifiedBy = "0";
	private String payGlcodeId = null;
	private static TaskFailedException taskExc;
	private static final Logger LOGGER = Logger.getLogger(Fund.class);
	private String updateQuery = "UPDATE fund SET";
	private boolean isId = false, isField = false;

	public Fund() {
	};

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public String getPayGlcodeId() {
		return payGlcodeId;
	}

	public void setCode(String aCode) {
		code = aCode;
		isField = true;
	}

	public void setName(String aName) {
		name = aName;
		isField = true;
	}

	public void setType(String aType) {
		type = aType;
		isField = true;
	}

	public void setLlevel(String aLlevel) {
		lLevel = aLlevel;
		isField = true;
	}

	public void setParentId(String aParentId) {
		parentId = aParentId;
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

	public void setTranscDebitAmount(String aTransactionDebitAmount) {
		transactionDebitAmount = aTransactionDebitAmount;
		isField = true;
	}

	public void setTranscCreditAmount(String aTransactionCreditAmount) {
		transactionCreditAmount = aTransactionCreditAmount;
		isField = true;
	}

	public void setTransactionDebitAmount(String aTransactionDebitAmount) {
		transactionDebitAmount = aTransactionDebitAmount;
		isField = true;
	}

	public void setTransactionCreditAmount(String aTransactionCreditAmount) {
		transactionCreditAmount = aTransactionCreditAmount;
		isField = true;
	}

	public void setIsActive(String aIsActive) {
		isActive = aIsActive;
		isField = true;
	}

	public void setLastModified(String aLastModified) {
		lastModified = aLastModified;

		isField = true;
	}

	public void setCreated(String aCreated) {
		created = aCreated;
		isField = true;
	}

	public void setModifiedBy(String aModifiedBy) {
		modifiedBy = aModifiedBy;
		isField = true;
	}

	public void setPayGlcodeId(String apayGlcodeId) {
		payGlcodeId = apayGlcodeId;
		isField = true;
	}

	public String getInterfundCode(String fundid, Connection connection)
			throws SQLException, TaskFailedException {
		String retVal = null;
		String query = "Select payGlcodeId from fund where id= ?";
		PreparedStatement pst = connection.prepareStatement(query);
		pst.setString(1, fundid);
		ResultSet rs = pst.executeQuery();
		if (rs.next())
			retVal = rs.getString(1);
		return retVal;
	}

	public void insert(Connection connection) throws SQLException,
			TaskFailedException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse(created));
			EgovMasterDataCaching.getInstance().removeFromCache("egi-fund");
		} catch (Exception e) {
			LOGGER.error("Exp  in insert to fund" + e.getMessage(),e);
			throw taskExc;
		}
		setCreated(created);
		setLastModified(created);
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("Fund")));
		String insertQuery = "INSERT INTO Fund (Id, Code, Name, Type, Llevel, ParentId, OpeningDebitBalance, "
				+ "OpeningCreditBalance, TransactionDebitAmount, TransactionCreditAmount, "
				+ "IsActive, LastModified, Created, ModifiedBy) " +

				"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		PreparedStatement pst = connection.prepareStatement(insertQuery);
		pst.setString(1, id);
		pst.setString(2, code);
		pst.setString(3, name);
		pst.setString(4, type);
		pst.setString(5, lLevel);
		pst.setString(6, parentId);
		pst.setString(7, openingDebitBalance);
		pst.setString(8, openingCreditBalance);
		pst.setString(9, transactionDebitAmount);
		pst.setString(10, transactionCreditAmount);
		pst.setString(11, isActive);
		pst.setString(12, lastModified);
		pst.setString(13, created);
		pst.setString(14, modifiedBy);
		pst.executeUpdate();
		pst.close();
	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
		if (isId && isField) {
			newUpdate(connection);
		}
	}
	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		PreparedStatement pstmt = null;
		try {
			created = formatter.format(sdf.parse(created));
		} catch (ParseException parseExp) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(), parseExp);
		}
		setCreated(created);
		setLastModified(created);
		StringBuilder query = new StringBuilder(500);
		query.append("update fund set ");
		if (code != null)
			query.append("code=?,");
		if (name != null)
			query.append("name=?,");
		if (type != null)
			query.append("type=?,");
		if (lLevel != null)
			query.append("lLevel=?,");
		if (parentId != null)
			query.append("parentId=?,");
		if (openingDebitBalance != null)
			query.append("openingDebitBalance=?,");
		if (openingCreditBalance != null)
			query.append("openingCreditBalance=?,");
		if (transactionDebitAmount != null)
			query.append("transactionDebitAmount=?,");
		if (transactionCreditAmount != null)
			query.append("transactionCreditAmount=?,");
		if (isActive != null)
			query.append("isActive=?,");
		if (lastModified != null)
			query.append("lastModified=?,");
		if (created != null)
			query.append("created=?,");
		if (modifiedBy != null)
			query.append("modifiedBy=?,");
		if (payGlcodeId != null)
			query.append("payGlcodeId=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (code != null)
				pstmt.setString(i++, code);
			if (name != null)
				pstmt.setString(i++, name);
			if (type != null)
				pstmt.setString(i++, type);
			if (lLevel != null)
				pstmt.setString(i++, lLevel);
			if (parentId != null)
				pstmt.setString(i++, parentId);
			if (openingDebitBalance != null)
				pstmt.setString(i++, openingDebitBalance);
			if (openingCreditBalance != null)
				pstmt.setString(i++, openingCreditBalance);
			if (transactionDebitAmount != null)
				pstmt.setString(i++, transactionDebitAmount);
			if (transactionCreditAmount != null)
				pstmt.setString(i++, transactionCreditAmount);
			if (isActive != null)
				pstmt.setString(i++, isActive);
			if (lastModified != null)
				pstmt.setString(i++, lastModified);
			if (created != null)
				pstmt.setString(i++, created);
			if (modifiedBy != null)
				pstmt.setString(i++, modifiedBy);
			if (payGlcodeId != null)
				pstmt.setString(i++, payGlcodeId);
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

	public HashMap getFundList(Connection con) throws Exception {
		String query = "SELECT id, name FROM fund where isactive=1 and isnotleaf!=1 order by name";
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query   " + query);
		HashMap hm = new HashMap();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next())
				hm.put(rs.getString(1), rs.getString(2));

		} catch (Exception e) {
			LOGGER.error("Exp in getFundList" + e.getMessage(),e);
			throw taskExc;
		}
		return hm;
	}

	public String getFundForAcc(String accId, Connection con) throws Exception {
		String query = "SELECT fundid FROM bankaccount WHERE id= ?";
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query   " + query);
		String fundId = "";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, accId);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
				fundId = rs.getString(1);
		} catch (Exception e) {
			LOGGER.error("Exp in getFundForAcc" + e.getMessage(),e);
			throw taskExc;
		}
		return fundId;
	}

	public HashMap getFundSourceList(Connection con) throws Exception {
		String query = "SELECT id, name FROM fundsource where isactive=1 and isnotleaf!=1 order by name";
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query   " + query);
		HashMap hm = new HashMap();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next())
				hm.put(rs.getString(1), rs.getString(2));
		} catch (Exception e) {
			LOGGER.error("Exp in getFundSourceList" + e.getMessage(),e);
			throw taskExc;
		}
		return hm;
	}

}
