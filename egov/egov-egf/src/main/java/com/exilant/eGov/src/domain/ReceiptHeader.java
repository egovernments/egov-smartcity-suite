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

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
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

	
	@Transactional
	public void insert() throws SQLException {
		EGovernCommon commonMethods = new EGovernCommon();
		narration = commonMethods.formatString(narration);
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("ReceiptHeader")));
		insertQuery = "INSERT INTO receiptheader (Id, VoucherHeaderId, Type, WardId, BillCollectorId, "
				+ "CounterId, BankId, BankBranchId, BankAccountNumberId, ModeOfCollection, ChequeId, "
				+ "CashAmount, Narration, RevenueSource,isReversed,CASHIER,receiptno, manualReceiptNumber) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		Query pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
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

	}
	@Transactional
	public void reverse( String cgNum)
			throws SQLException {
		String updateQuery = "update receiptheader set isreversed=1 where voucherheaderid in(select id from voucherheader where cgn= ?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
		Query pst = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
		pst.executeUpdate();
	}
	@Transactional
	public void update() throws SQLException {
		if (isId && isField) {
			try{
			newUpdate();
			}catch(Exception e){
				LOGGER.error("Exception in update method"+e.getMessage());
			}
		}
	}

	public void newUpdate() throws TaskFailedException,
			SQLException {
		EGovernCommon commonMethods = new EGovernCommon();
		narration = commonMethods.formatString(narration);
		Query pstmt=null;
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
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
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

			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(),e);
			throw taskExc;
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
