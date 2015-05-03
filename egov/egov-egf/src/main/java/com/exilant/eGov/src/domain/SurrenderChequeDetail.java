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
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class SurrenderChequeDetail
{
	private String id = null;
	private String BankAccountId = null;
	private String chequeNumber ="";
	private String chequeDate = "1-Jan-1900";
	private String voucherHeaderId =null;
	private String lastModified = "1-Jan-1900";
	private String created = "1-Jan-1900";
	private static TaskFailedException taskExc;
	private String updateQuery="insert into eg_surrendered_cheques values ";
	EGovernCommon cm = new EGovernCommon();
	private final static Logger LOGGER=Logger.getLogger(SurrenderChequeDetail.class);
	Statement statement;
	public void setId(String aId){ id = aId;}
	public int getId() {return Integer.valueOf(id).intValue();}
	public void setChequeDate(String aChequeDate){ chequeDate = aChequeDate; updateQuery = updateQuery + " ChequeDate='" + chequeDate + "',";}
	public void setChequeNumber(String aChequeNumber){ chequeNumber = aChequeNumber; updateQuery = updateQuery + " ChequeNumber='" + chequeNumber + "',"; }
	public void setBankAccountId(String aBankAccountId){ BankAccountId = aBankAccountId; updateQuery = updateQuery + " BankAccountId=" + BankAccountId + ","; }
	public void setLastModified(String aLastModified){ lastModified = aLastModified; updateQuery = updateQuery + " LastModified='" + lastModified + "',"; }
	public void setCreated(String aCreated){ created = aCreated; /* not said for updation */}
	public void setVoucherHeaderId(String avoucherHeaderId){ voucherHeaderId = avoucherHeaderId;  updateQuery = updateQuery + " voucherHeaderId='" + voucherHeaderId + "',";}
	
	@Transactional
	public void insert() throws SQLException,TaskFailedException
	{
		if(LOGGER.isInfoEnabled())     LOGGER.info("insert inside");
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate();
		try
   		{
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse( created ));
   		}
		catch(Exception e){throw taskExc;}
		setCreated(created);
		setLastModified(created);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("eg_surrendered_cheques")) );

		String insertQuery = "INSERT INTO eg_surrendered_cheques (Id,BankAccountId, ChequeNumber,ChequeDate,VHID,LastModifiedDate) " +
		" VALUES(" + id + ","+ BankAccountId +", '" + chequeNumber + "', '" + chequeDate + "','"+voucherHeaderId+"', '"+ lastModified +"')";
		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
        if(LOGGER.isDebugEnabled())     LOGGER.debug("insertQuery:"+insertQuery);
		statement.executeUpdate();

	}
	
}
