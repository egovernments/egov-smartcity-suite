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
 * Created on Apr 11, 2005
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
import org.egov.infstr.utils.EgovMasterDataCaching;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class FinancialYear {
	private String id = null;
	private String financialYear = null;
	private String startingDate = "1-Jan-1900";
	private String endingDate = "1-Jan-1900";
	private String isActive = "1";
	private String created = "1-Jan-1900";
	private String lastModified = "1-Jan-1900";
	private String modifiedBy = null;
	private String isActiveForPosting = "0";
	private String isClosed = "0";
	private String TransferClosingBalance = "0";
	private static final Logger LOGGER = Logger.getLogger(FinancialYear.class);
	private TaskFailedException taskExc;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
			.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
			Locale.getDefault());

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void insert(Connection connection) throws SQLException,
			TaskFailedException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse(created));
			EgovMasterDataCaching.getInstance().removeFromCache(
					"egi-activeFinYr");
		} catch (Exception e) {
			LOGGER
					.error("Exp in insert to financialyear: " + e.getMessage(),
							e);
			throw new TaskFailedException();
		}
		setCreated(created);
		setLastModified(created);
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("FinancialYear")));

		String insertQuery = "INSERT INTO FinancialYear (id, financialyear, startingdate, endingdate, "
				+ "isactive, created, lastmodified, MODIFIEDBY, isActiveForPosting, isClosed, TransferClosingBalance) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement pst = connection.prepareStatement(insertQuery);
		pst.setString(1, id);
		pst.setString(2, financialYear);
		pst.setString(3, startingDate);
		pst.setString(4, endingDate);
		pst.setString(5, isActive);
		pst.setString(6, created);
		pst.setString(7, lastModified);
		pst.setString(8, modifiedBy);
		pst.setString(9, isActiveForPosting);
		pst.setString(10, isClosed);
		pst.setString(11, TransferClosingBalance);
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		pst.executeUpdate();
		pst.close();

	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
		newUpdate(connection);
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	public String getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(String endingDate) {
		this.endingDate = endingDate;
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

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getIsActiveForPosting() {
		return isActiveForPosting;
	}

	public void setIsActiveForPosting(String isActiveForPosting) {
		this.isActiveForPosting = isActiveForPosting;
	}

	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}

	public String getTransferClosingBalance() {
		return TransferClosingBalance;
	}

	public void setTransferClosingBalance(String transferClosingBalance) {
		TransferClosingBalance = transferClosingBalance;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate();
		PreparedStatement pstmt = null;
		try {
			created = formatter.format(sdf.parse(created));
		} catch (ParseException parseExp) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(), parseExp);
		}
		setCreated(created);
		setLastModified(created);
		StringBuilder query = new StringBuilder(500);
		query.append("update financialyear set ");
		if (financialYear != null)
			query.append("financialYear=?,");
		if (startingDate != null)
			query.append("startingDate=?,");
		if (endingDate != null)
			query.append("endingDate=?,");
		if (isActive != null)
			query.append("isActive=?,");
		if (created != null)
			query.append("created=?,");
		if (lastModified != null)
			query.append("lastModified=?,");
		if (modifiedBy != null)
			query.append("modifiedBy=?,");
		if (isActiveForPosting != null)
			query.append("isActiveForPosting=?,");
		if (isClosed != null)
			query.append("isClosed=?,");
		if (TransferClosingBalance != null)
			query.append("TransferClosingBalance=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (financialYear != null)
				pstmt.setString(i++, financialYear);
			if (startingDate != null)
				pstmt.setString(i++, startingDate);
			if (endingDate != null)
				pstmt.setString(i++, endingDate);
			if (isActive != null)
				pstmt.setString(i++, isActive);
			if (created != null)
				pstmt.setString(i++, created);
			if (lastModified != null)
				pstmt.setString(i++, lastModified);
			if (modifiedBy != null)
				pstmt.setString(i++, modifiedBy);
			if (isActiveForPosting != null)
				pstmt.setString(i++, isActiveForPosting);
			if (isClosed != null)
				pstmt.setString(i++, isClosed);
			if (TransferClosingBalance != null)
				pstmt.setString(i++, TransferClosingBalance);
			pstmt.setString(i++, id);

			pstmt.executeQuery();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage());
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
