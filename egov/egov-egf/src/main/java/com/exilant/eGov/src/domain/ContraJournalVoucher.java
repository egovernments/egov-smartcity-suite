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
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
/**
 * @author vijaykumar.b
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

@Transactional(readOnly=true)
public class ContraJournalVoucher
{
	private String id = null;
	private String voucherHeaderID = null;
	private String type = "";
	private String fromBankId = null;
	private String fromBankBranchId = null;
	private String fromBankAccountId = null;
	private String fromChequeNumber = "";
	private String fromChequeDate = "1-Jan-1900";
	private String toBankId = null;
	private String toBankBranchId = null;
	private String toBankAccountId = null;
	private String narration = "";
	private String toCashNameId = null;
	private String toChequeNameId = null;
	private String fromCashNameId = null;
	private String fromFundID = null;
	private String toFundID = null;
	private String isReversed = "0";
	private String updateQuery="UPDATE ContraJournalVoucher SET";
	
	private static final Logger LOGGER=Logger.getLogger(ContraJournalVoucher.class);
	private boolean isId=false, isField=false;

	private Query statement;

	public void setId(String aID){ id = aID; isId=true;}
	public void setVoucherHeaderID(String aVoucherHeaderID){ voucherHeaderID = aVoucherHeaderID;updateQuery = updateQuery + " voucherHeaderID='" + voucherHeaderID + "',"; isField = true;}
	public void setType(String aType){ type = aType; updateQuery = updateQuery + " type='" + type + "',"; isField = true;}
	public void setFromBankId(String aFromBankId){	fromBankId = aFromBankId; updateQuery = updateQuery + " fromBankId='" + fromBankId + "',"; isField = true;}
	public void setFromBankBranchId(String aFromBankBranchId){ fromBankBranchId = aFromBankBranchId; updateQuery = updateQuery + " fromBankBranchId='" + fromBankBranchId + "',"; isField = true;}
	public void setFromBankAccountId(String aFromBankAccountId){ fromBankAccountId = aFromBankAccountId; updateQuery = updateQuery + " fromBankAccountId='" + fromBankAccountId + "',"; isField = true;}
	public void setFromChequeNumber(String aFromChequeNumber){ fromChequeNumber = aFromChequeNumber; updateQuery = updateQuery + " fromChequeNumber='" + fromChequeNumber + "',"; isField = true;}
	public void setFromChequeDate(String aFromChequeDate){ fromChequeDate = aFromChequeDate; updateQuery = updateQuery + " fromChequeDate='" + fromChequeDate + "',"; isField = true;}
	public void setToBankId(String aToBankId){ toBankId = aToBankId; updateQuery = updateQuery + " toBankId='" + toBankId + "',"; isField = true;}
	public void setToBankBranchId(String aToBankBranchId){ toBankBranchId = aToBankBranchId; updateQuery = updateQuery + " toBankBranchId='" + toBankBranchId + "',"; isField = true;}
	public void setToBankAccountId(String aToBankAccountId){ toBankAccountId = aToBankAccountId; updateQuery = updateQuery + " toBankAccountId='" + toBankAccountId + "',"; isField = true;}
	public void setNarration(String aNarration){ narration = aNarration; updateQuery = updateQuery + " narration='" + narration + "',"; isField = true;}
	public void setToCashNameId(String aToCashNameId){ toCashNameId = aToCashNameId; updateQuery = updateQuery + " toCashNameId='" + toCashNameId + "',"; isField = true;}
	public void setFromCashNameId(String aFromCashNameId){ fromCashNameId = aFromCashNameId; updateQuery = updateQuery + " fromCashNameId='" + fromCashNameId + "',"; isField = true;}
	public void setFromFundID(String aFromFundID){ fromFundID = aFromFundID; updateQuery = updateQuery + " fromFundID='" + fromFundID + "',"; isField = true;}
	public void setToFundID(String aToFundID){ toFundID = aToFundID; updateQuery = updateQuery + " toFundID='" + toFundID + "',"; isField = true;}
	public void setToChequeNameId(String aToChequeNameId){ toChequeNameId = aToChequeNameId; updateQuery = updateQuery + " toChequeNameId='" + toChequeNameId + "',"; isField = true;}
	public void setIsReversed(String aIsReversed){ isReversed = aIsReversed; updateQuery = updateQuery + " isReversed='" + isReversed + "',"; isField = true;}
	@Transactional
	public void insert() throws SQLException
	{
		EGovernCommon commommethods = new EGovernCommon();
		narration = commommethods.formatString(narration);

		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("ContraJournalVoucher")) );
		String insertQuery = "INSERT INTO ContraJournalVoucher (Id, VoucherHeaderID, Type, FromBankId, " +
								"FromBankBranchId, FromBankAccountId, FromChequeNumber, FromChequeDate, " +
								"ToBankId, ToBankBranchId, ToBankAccountId, Narration, ToCashNameId, " +
								"FromCashNameId, FromFundID, ToFundID,tochequenameid,isReversed) " +
								"VALUES (" + id + ", " + voucherHeaderID + ", '" + type + "', " + fromBankId + ", " +
								fromBankBranchId + ", " + fromBankAccountId + ", '" + fromChequeNumber + "', '" +
								fromChequeDate + "', " + toBankId + ", " + toBankBranchId + ", " +
								toBankAccountId + ", '" + narration + "', " + toCashNameId + ", " +
								fromCashNameId + ", " + fromFundID + ", " + toFundID +","+ toChequeNameId+","+isReversed+")";

		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		statement.executeUpdate();
	}
	@Transactional
	public void reverse()throws SQLException
	{
		//	EGovernCommon commommethods=new EGovernCommon();
			Query statement;
			String reverseQuery="UPDATE contraJournalvoucher SET IsReversed=1 WHERE id="+id;
			if(LOGGER.isInfoEnabled())     LOGGER.info(reverseQuery);
			statement = HibernateUtil.getCurrentSession().createSQLQuery(reverseQuery);
			statement.executeUpdate();
	}
	@Transactional
	//added by rashmi
	public void update () throws SQLException
	{
		if(isId && isField)
		{
		//	EGovernCommon commommethods = new EGovernCommon();
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			updateQuery="UPDATE ContraJournalVoucher SET";
		}
	}


}

