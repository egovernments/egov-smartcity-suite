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
 * Created on Jan 13, 2005
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
	@Transactional
	public void insert() throws SQLException,
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
		Query pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
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
	}
	@Transactional
	public void update() throws SQLException,
			TaskFailedException {
		try {
			newUpdate();
		} catch (Exception e) {
			LOGGER.error("error inside update " + e.getMessage(), e);
		}
	}

	public void newUpdate() throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		Query pstmt = null;
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
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
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

			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(),e);
			throw taskExc;
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
