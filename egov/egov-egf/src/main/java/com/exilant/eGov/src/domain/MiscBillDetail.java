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
 * Created on Jan 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class MiscBillDetail {
	private String id = null;
	private String billNumber = "";
	private String billDate = "";
	private String amount = "0";
	private String passedAmount = "0";
	private String paidTo = "";
	private String approvedBy = null;
	private String paidById = null;
	private String created = "";
	private String modifiedBy = "";
	private String lastModified = "";
	private String BillVHId = null;
	private String payVHID = null;

	private static final Logger LOGGER = Logger.getLogger(MiscBillDetail.class);
	private String updateQuery = "UPDATE miscBillDetail SET";
	private boolean isId = false, isField = false;
	Query pst;
	private static TaskFailedException taskExc;

	public void setId(String aId) {
		id = aId;
		isId = true;
		isField = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void setBillNumber(String aBillNumber) {
		billNumber = aBillNumber;
		updateQuery = updateQuery + " billNumber='" + billNumber + "',";
		isField = true;
	}

	public void setBillDate(String aBillDate) {
		billDate = aBillDate;
		updateQuery = updateQuery + " billDate='" + billDate + "',";
		isField = true;
	}

	public void setAmount(String aAmount) {
		amount = aAmount;
		updateQuery = updateQuery + " amount='" + amount + "',";
		isField = true;
	}

	public void setPassedAmount(String aPassedAmount) {
		passedAmount = aPassedAmount;
		updateQuery = updateQuery + " passedAmount='" + passedAmount + "',";
		isField = true;
	}

	public void setPaidTo(String aPaidTo) {
		paidTo = aPaidTo;
		updateQuery = updateQuery + " paidTo='" + paidTo + "',";
		isField = true;
	}

	public void setApprovedBy(String aApprovedBy) {
		approvedBy = aApprovedBy;
		updateQuery = updateQuery + " approvedBy='" + approvedBy + "',";
		isField = true;
	}

	public void setPaidById(String aPaidById) {
		paidById = aPaidById;
		updateQuery = updateQuery + " approvedBy='" + approvedBy + "',";
		isField = true;
	}

	public void setCreated(String aCreated) {
		created = aCreated;
		updateQuery = updateQuery + " created='" + created + "',";
		isField = true;
	}

	public void setModifiedBy(String aModifiedBy) {
		modifiedBy = aModifiedBy;
		updateQuery = updateQuery + " modifiedBy='" + modifiedBy + "',";
		isField = true;
	}

	// public void setLastModified(String aLastModified){ lastModified =
	// aLastModified; updateQuery = updateQuery + " lastModified='" +
	// lastModified + "',"; isField = true; }
	public void setLastModified(String aLastModified) {
		lastModified = aLastModified;
		updateQuery = updateQuery + " lastModified=to_date('" + lastModified
				+ "','dd-Mon-yyyy HH24:MI:SS')" + ",";
		isField = true;
	}

	public void setBillVHId(String aBillVHId) {
		BillVHId = aBillVHId;
		updateQuery = updateQuery + " BILLVHID=" + BillVHId + ",";
		isField = true;
	}

	public void setPayVHID(String apayVHID) {
		payVHID = apayVHID;
		updateQuery = updateQuery + " PAYVHID=" + payVHID + ",";
		isField = true;
	}
	@Transactional
	public void insert() throws SQLException,
			TaskFailedException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse(created));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw taskExc;
		}
		setCreated(created);
		setLastModified(created);
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("MiscBillDetail")));

		String insertQuery = "INSERT INTO miscbilldetail (Id, BillNumber, BillDate, Amount, PassedAmount, PaidTo,  "
				+ "PaidById,BILLVHID,PAYVHID) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		Query pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		pst.setString(1, id);
		pst.setString(2, billNumber);
		pst.setString(3, billDate);
		pst.setString(4, amount);
		pst.setString(5, passedAmount);
		pst.setString(6, paidTo);
		pst.setString(7, paidById);
		pst.setString(8, BillVHId);
		pst.setString(9, payVHID);
		pst.executeUpdate();
	}

	public void reverse( String cgNum)
			throws SQLException {
		String query = "select miscbilldetail.id from miscbilldetail,paymentheader where"
				+ " miscbilldetail.id=paymentheader.miscbilldetailid and voucherheaderid in(select id from voucherheader where cgn= ?)";
		pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
		pst.setString(1, cgNum);
		List<Object[]> resultset = pst.list();
		int miscbilldetailid = 0;
		for(Object[] element : resultset){
			 miscbilldetailid = Integer.parseInt(element[0].toString());
		}
		String updateQuery = "update miscbilldetail  set isreversed=1 where id="
				+ miscbilldetailid;
		if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
		pst = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
		pst.executeUpdate();
	}
	@Transactional
	public void reverse() throws SQLException {
		String updateQuery = "update miscbilldetail  set isreversed=1 where id= ?";
		pst = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
		pst.setString(1, id);
		if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
		pst.executeUpdate();
	}
	@Transactional
	public void reversePositive( double passedAmount)
			throws SQLException {
		// EGovernCommon commommethods=new EGovernCommon();

		String reversePositive = "UPDATE miscBillDetail SET  passedAmount=passedAmount+"
				+ passedAmount + "WHERE id= ?";
		pst = HibernateUtil.getCurrentSession().createSQLQuery(reversePositive);
		pst.setString(1, id);
		if(LOGGER.isInfoEnabled())     LOGGER.info(reversePositive);
		pst.executeUpdate();
	}
	@Transactional
	public void reverseNegative( double paidAmount,
			double adjAmount, double passedAmount) throws SQLException {
		String reverseNegative = "UPDATE miscBillDetail SET  passedAmount=passedAmount-"
				+ passedAmount + "WHERE id= ?";
		Query pst = HibernateUtil.getCurrentSession().createSQLQuery(reverseNegative);
		pst.setString(1, id);
		if(LOGGER.isInfoEnabled())     LOGGER.info(reverseNegative);
		pst.executeUpdate();
	}

	/**
	 * Fucntion for update generalledger
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	@Transactional
	public void update() throws SQLException,
			TaskFailedException {
		if (isId && isField) {
			updateQuery = updateQuery.substring(0, updateQuery.length() - 1);
			updateQuery = updateQuery + " WHERE id = ?";
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Query pst = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			pst.setString(1, id);
			pst.executeUpdate();
			updateQuery = "UPDATE miscBillDetail SET";
		}
	}
}
