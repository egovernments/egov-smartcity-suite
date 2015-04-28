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

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubLedgerPaymentHeader
{
	private String id = null;
	private String voucherHeaderId = null;
	private String type =null;
	private String typeOfPayment = null;
	private String supplierId = null;
	private String bankAccountId =null;
	private String chequeId = null;
	private String supplierBillId = null;
	private String contractorId = null;
	private String contractorBillId = null;
	private String salaryBillId = null;
	private String cashName = "";
	private String paidBy = null;
	private String paidAmount = "0";
	private String narration = "";
	private String worksDetailID = null;
	private String isReversed = "0";
	private String paidTo = null;
	private String updateQuery="UPDATE subledgerpaymentheader SET";
	private static final Logger LOGGER = Logger.getLogger(SubLedgerPaymentHeader.class);
	private boolean isId=false, isField=false;

	public void setId(String aId){id=aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue();}
	public void setVoucherHeaderId(String aVoucherHeaderId){voucherHeaderId=aVoucherHeaderId; updateQuery = updateQuery + " voucherHeaderId=" + voucherHeaderId + ","; isField = true;}
	public void setType(String aType){type = aType; updateQuery = updateQuery + " type='" + type + "',"; isField = true;}
	public void setTypeOfPayment(String aTypeOfPayment){typeOfPayment=aTypeOfPayment; updateQuery = updateQuery + " typeOfPayment='" + typeOfPayment + "',"; isField = true;}
	public void setSupplierId(String aSupplierId){supplierId = aSupplierId; updateQuery = updateQuery + " supplierId=" + supplierId + ","; isField = true;}
	public void setBankAccountId(String aBankAccountId){bankAccountId=aBankAccountId;updateQuery = updateQuery + " bankAccountId=" + bankAccountId + ","; isField = true;}
	public void setChequeId(String aChequeId){chequeId=aChequeId; updateQuery = updateQuery + " chequeId=" + chequeId + ","; isField = true;}
	public void setSupplierBillId(String aSupplierBillId){supplierBillId=aSupplierBillId; updateQuery = updateQuery + " supplierBillId=" + supplierBillId + ","; isField = true;}
	public void setContractorId(String aContractorId){contractorId=aContractorId;updateQuery = updateQuery + " contractorId=" + contractorId + ","; isField = true;}
	public void setContractorBillId(String aContractorBillId){contractorBillId=aContractorBillId;updateQuery = updateQuery + " contractorBillId=" + contractorBillId + ","; isField = true;}
	public void setSalaryBillId(String aSalaryBillId){salaryBillId=aSalaryBillId;updateQuery = updateQuery + " salaryBillId=" + salaryBillId + ","; isField = true;}
	public void setCashName(String aCashName){cashName=aCashName;updateQuery = updateQuery + " cashName=" + cashName + ","; isField = true;}
	public void setPaidBy(String aPaidBy){paidBy=aPaidBy;updateQuery = updateQuery + " paidBy=" + paidBy + ","; isField = true;}
	public void setPaidAmount(String aPaidAmount){paidAmount=aPaidAmount;updateQuery = updateQuery + " paidAmount='" + paidAmount + "',"; isField = true;}
	public void setNarration(String aNarration){narration=aNarration;updateQuery = updateQuery + " narration='" + narration + "',"; isField = true;}
	public void setWorksDetailID(String aWorksDetailID){worksDetailID=aWorksDetailID;updateQuery = updateQuery + " worksDetailID=" + worksDetailID + ","; isField = true;}
	public void setIsReversed(String aIsReversed){isReversed=aIsReversed;updateQuery = updateQuery + " isReversed=" + isReversed + ","; isField = true;}
	public void setPaidTo(String aPaidTo){paidTo=aPaidTo;updateQuery = updateQuery + " paidTo=" + paidTo + ","; isField = true;}
	public void insert(Connection connection) throws SQLException
	{
		EGovernCommon commonMethods = new EGovernCommon();
		narration = commonMethods.formatString(narration);

		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("SubLedgerPaymentHeader")) );

		String insertQuery = "INSERT INTO subledgerpaymentheader (ID, VoucherHeaderID, Type, TypeOfPayment, SupplierId, " +
						" BankAccountId, ChequeId, SupplierBillId, ContractorId, ContractorBillId, " +
						"SalaryBillId, CashName, PaidBy, PaidAmount, Narration,worksDetailID,isreversed,paidTo) " +
							"VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		PreparedStatement pst = connection.prepareStatement(insertQuery);
		pst.setString(1, id);
		pst.setString(2, voucherHeaderId);
		pst.setString(3, type);
		pst.setString(4, typeOfPayment);
		pst.setString(5, supplierId);
		pst.setString(6, bankAccountId);
		pst.setString(7, chequeId);
		pst.setString(8, supplierBillId);
		pst.setString(9, contractorId);
		pst.setString(10, contractorBillId);
		pst.setString(11, salaryBillId);
		pst.setString(12, cashName);
		pst.setString(13, paidBy);
		pst.setString(14, paidAmount);
		pst.setString(15, narration);
		pst.setString(16, worksDetailID);
		pst.setString(17, isReversed);
		pst.setString(18, paidTo);
		pst.executeUpdate();
		pst.close();
	}
	public void reverse(Connection connection,String cgNum)throws SQLException{
		String updateQuery = "update subledgerpaymentheader  set isreversed=1 where voucherheaderid in(select id from voucherheader where cgn= ?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
		PreparedStatement pst = connection.prepareStatement(updateQuery);
		pst.setString(1, cgNum);
		pst.executeUpdate();
		pst.close();
	}
	
	public void reverse(Connection connection,String paidAmount,String cgNum)throws SQLException{
		String updateQuery = "update subledgerpaymentheader  set paidamount=paidamount-"+paidAmount +" where voucherheaderid in(select id from voucherheader where cgn= ?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
		PreparedStatement pst = connection.prepareStatement(updateQuery);
		pst.setString(1, cgNum);
		pst.executeUpdate();
		pst.close();
	}
	
	public void update (Connection connection) throws SQLException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			narration = commommethods.formatString(narration);
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = ?";
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			PreparedStatement pst = connection.prepareStatement(updateQuery);
			pst.setString(1, id);
			pst.executeUpdate();
			pst.close();
			updateQuery="UPDATE subledgerpaymentheader SET";
		}
	}
}
