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
 * Created on Apr 19, 2005 
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
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
	private Query pstmt = null;
	private static TaskFailedException taskExc;
	private ResultSet rs = null;
	private String updateQuery = "UPDATE TransactionSummary SET";
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger
			.getLogger(TransactionSummary.class);

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
	@Transactional
	public void insert() throws SQLException {
		// EGovernCommon commommethods = new EGovernCommon();

		setId(String.valueOf(PrimaryKeyGenerator
				.getNextKey("TransactionSummary")));

		String insertQuery = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid,openingdebitbalance, openingcreditbalance, debitamount,creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId) VALUES (?,?,?,?,?,?,?,?,?,?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		pstmt = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
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
		pstmt.executeUpdate();

	}
	@Transactional
	public void update() throws SQLException ,TaskFailedException{
		if (isId && isField) {
			newUpdate();
		}
	}

	public void newUpdate() throws TaskFailedException,
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
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
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

			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(),e);
			throw taskExc;
		} 
	}

}
