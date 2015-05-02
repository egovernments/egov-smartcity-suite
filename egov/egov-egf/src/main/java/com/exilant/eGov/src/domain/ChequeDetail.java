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
import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentType;
import org.hibernate.SQLQuery;

import com.exilant.exility.common.TaskFailedException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ChequeDetail
{
	private String id = null;
	private String chequeDate = "1-Jan-1900";
	private String chequeNumber ="";
	private String receiptDate = "1-Jan-1900";
	private String amount = "0";
	private String regionId = null;
	private String isDeposited = "0";
	private String payInSlipNumber = "";
	private String payInSlipDate = "";
	private String narration = "";
	private String accountNumberId = null;
	private String bankId = null;
	private String branchId = null;
	private String payTo = "";
	private String paidById = null;
	private String approvedBy = null;
	private String created = "";
	private String modifiedBy = null;
	private String lastModified = "";
	private String isPayCheque="0";
	private String isReversed="0";
	private String chequeType="";
	private String voucherHeaderId =null;
	private String bankName="";
	private String updateQuery="UPDATE chequedetail SET";
	private String type="";
	private String status=null;
	private String detailKeyId = null;
	private String detailTypeId = null;	

	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	private SimpleDateFormat sdf1 =new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());
	EGovernCommon cm = new EGovernCommon();
	private TaskFailedException taskExc;
	private boolean isId=false, isField=false;
	private final static Logger LOGGER=Logger.getLogger(ChequeDetail.class);
	private Statement statement;
	private String updateChqQuery = "update chequedetail  set isreversed=1 where id=";
	
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue();}
	public void setChequeDate(String aChequeDate){ chequeDate = aChequeDate; updateQuery = updateQuery + " ChequeDate='" + chequeDate + "',"; isField = true;}
	public void setChequeNumber(String aChequeNumber){ chequeNumber = aChequeNumber; updateQuery = updateQuery + " ChequeNumber='" + chequeNumber + "',"; isField = true;}
	public void setReceiptDate(String aReceiptDate){ receiptDate = aReceiptDate; updateQuery = updateQuery + " ReceiptDate='" + receiptDate + "',"; isField = true;}
	public void setAmount(String aAmount){ amount = aAmount; updateQuery = updateQuery + " Amount=" + amount + ","; isField = true;}
	public void setRegionId(String aRegionId){ regionId = aRegionId; updateQuery = updateQuery + " RegionId=" + regionId + ","; isField = true;}
	public void setIsDeposited(String aIsDeposited){ isDeposited = aIsDeposited; updateQuery = updateQuery + " IsDeposited=" + isDeposited + ","; isField = true;}
	public void setPayInSlipNumber(String aPayInSlipNumber){ payInSlipNumber = aPayInSlipNumber; updateQuery = updateQuery + " PayInSlipNumber='" + payInSlipNumber.toUpperCase() + "',"; isField = true;}
	public void setPayInSlipDate(String aPayInSlipDate){ payInSlipDate = aPayInSlipDate; updateQuery = updateQuery + " PayInSlipDate='" + payInSlipDate + "',"; isField = true;}
	public void setNarration(String aNarration){ narration = aNarration; updateQuery = updateQuery + " Narration='" + narration + "',"; isField = true;}
	public void setAccountNumberId(String aAccountNumberId){ accountNumberId = aAccountNumberId; updateQuery = updateQuery + " AccountNumberId=" + accountNumberId + ","; isField = true;}
	public void setBankId(String aBankId){ bankId = aBankId; updateQuery = updateQuery + " BankId=" + bankId + ","; isField = true;}
	public void setBranchId(String aBranchId){ branchId = aBranchId; updateQuery = updateQuery + " BranchId=" + branchId + ","; isField = true;}
	public void setPayTo(String aPayTo){ payTo = aPayTo; updateQuery = updateQuery + " PayTo='" + payTo + "',"; isField = true;}
	public void setPaidById(String aPaidById){ paidById = aPaidById; updateQuery = updateQuery + " PaidById=" + paidById + ","; isField = true;}
	public void setApprovedBy(String aApprovedBy){ approvedBy = cm.assignValue(aApprovedBy,approvedBy); updateQuery = updateQuery + " ApprovedBy=" + approvedBy + ","; isField = true;}
	public void setCreated(String aCreated){ created = aCreated; /* not said for updation */}
	public void setModifiedBy(String aModifiedBy){ modifiedBy = aModifiedBy; updateQuery = updateQuery + " ModifiedBy=" + modifiedBy + ","; isField = true;}
	public void setLastModified(String aLastModified){ lastModified = aLastModified; updateQuery = updateQuery + " LastModified='" + lastModified + "',"; isField = true;}
	public void setIsPayCheque(String aIsPayCheque){ isPayCheque = aIsPayCheque; updateQuery = updateQuery + " IsPayCheque=" + isPayCheque + ","; isField = true;}
	public void setIsReversed(String aIsReversed){ isReversed = aIsReversed; updateQuery = updateQuery + " isReversed=" + isReversed + ","; isField = true;}
	public void setChequeType(String aChequeType){ chequeType = aChequeType; updateQuery = updateQuery + " chequeType='" + chequeType + "',"; isField = true;}
	public void setType(String aType){ type = aType; }
	public void setVoucherHeaderId(String avoucherHeaderId){ voucherHeaderId = avoucherHeaderId;  updateQuery = updateQuery + " voucherHeaderId='" + voucherHeaderId + "',"; isField = true;}
	public void setBankName(String bName) { bankName=bName; updateQuery = updateQuery + " bankName='" + bankName + "',"; isField = true;}
	public void setDetailKeyId(String aDetailKeyId) { detailKeyId=aDetailKeyId; updateQuery = updateQuery + " detailKeyId='" + detailKeyId + "',"; isField = true;}
	public void setDetailTypeId(String aDetailTypeId) { detailTypeId=aDetailTypeId; updateQuery = updateQuery + " detailTypeId='" + detailTypeId + "',"; isField = true;}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status1) {	status=status1; updateQuery = updateQuery + " chqstatus=" + status + ","; isField = true;}

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate();
		Statement statement = null;
		try
   		{
			created = formatter.format(sdf.parse(created));
   			setCreated(created);
			setLastModified(created);
			narration = commommethods.formatString(narration);
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("ChequeDetail")) );
			String insertQuery = "INSERT INTO chequedetail (Id, ChequeDate, ChequeNumber, ReceiptDate, Amount, RegionId,IsDeposited, PayInSlipNumber, PayInSlipDate, Narration, AccountNumberId, " +
								"BankId, BranchId, PayTo, PaidById, ApprovedBy, Created, ModifiedBy, LastModified,IsPayCheque,chequeType,voucherheaderid,isReversed,bankname,type,chqstatus,detailKeyId,detailTypeId) " +
								"VALUES (" + id + ", '" + chequeDate + "', '" + chequeNumber + "', '" + receiptDate + "', "
								+ amount + ", " + regionId + ", " + isDeposited + ", '"
								+ payInSlipNumber.toUpperCase() + "', '" + payInSlipDate + "', '" + narration + "', "
								+ accountNumberId + ", " + bankId + ", " + branchId + ", '" + payTo + "', "
								+ paidById + ", " + approvedBy + ", '" + created + "', " + modifiedBy + ", '"
								+ lastModified +"',"+isPayCheque+ ",'"+chequeType+ "','"+voucherHeaderId+"', " + isReversed +",'"+bankName+"','"+type+"',"+status+","+detailKeyId+","+detailTypeId+")";
	
			statement = connection.createStatement();
	        if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			statement.executeUpdate(insertQuery);
   		}
		catch(Exception e){
			LOGGER.error("Exp in insert"+e.getMessage());
			throw taskExc;
		}
		finally{
			statement.close();
		}

	}
	
	
	public void reverse(Connection connection,String cgNum)throws Exception
	{
		ResultSet resultset=null;
		try{
			statement = connection.createStatement();
			String str="select chequedetail.id from chequedetail,paymentheader where "
	            +" chequedetail.id=paymentheader.chequeid and PAYMENTHEADER.voucherheaderid in(select id from voucherheader where cgn='"+cgNum+"')";
			resultset= statement.executeQuery(str);
			if(LOGGER.isInfoEnabled())     LOGGER.info(str);
			if(resultset.next()){
				int chequeid = Integer.parseInt(resultset.getString(1));
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside reverse:"+updateChqQuery+chequeid);
				statement.executeUpdate(updateChqQuery+chequeid);				
			}else{
				throw new TaskFailedException(" No Record found");
			}
		}catch(Exception e){
			LOGGER.error("Exp in reverse"+e.getMessage());
			throw taskExc;
		}
		finally{
			resultset.close();
			statement.close();
		}
	}

	
	public void reverseContra(Connection connection,String cgNum)throws Exception
	{
		ResultSet resultset=null;
		try{
			 statement = connection.createStatement();
			 resultset= statement.executeQuery("select chequedetail.id from chequedetail ,voucherheader  where "
		                        +" chequedetail.voucherheaderid=voucherheader.id and chequedetail.voucherheaderid in(select id from voucherheader where cgn='"+cgNum+"')");
			if(LOGGER.isInfoEnabled())     LOGGER.info("select chequedetail.id from chequedetail ,voucherheader  where "
		                        +" chequedetail.voucherheaderid=voucherheader.id and chequedetail.voucherheaderid in(select id from voucherheader where cgn='"+cgNum+"')");
			 while(resultset.next()){
				int chequeid = Integer.parseInt(resultset.getString(1));
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside reverse  contra:"+updateChqQuery+chequeid);
				statement.executeUpdate(updateChqQuery+chequeid);
			 }
		}catch(Exception e) {
			LOGGER.error("Exp IN REVERSE CONTRA	"+e.getMessage());
			throw taskExc;
		}finally{
			resultset.close();
			statement.close();
		}
	}

	public void reverse(Connection connection,String cgNum,String sub)throws Exception
	{
		Statement statement = null;
		ResultSet resultset= null;
		try{
			
			statement=connection.createStatement();
			if(sub.equals("receipt"))
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("select chequedetail.id from chequedetail,receiptheader where "
	                    +" chequedetail.id=receiptheader.chequeid and receiptheader.voucherheaderid in(select id from voucherheader where cgn='"+cgNum+"')"
	                    +"and receiptheader.voucherheaderid=chequedetail.voucherheaderid ");
	
				resultset= statement.executeQuery("select chequedetail.id from chequedetail,receiptheader where "
				                        +" chequedetail.id=receiptheader.chequeid and receiptheader.voucherheaderid in(select id from voucherheader where cgn='"+cgNum+"')"
				                        +"and receiptheader.voucherheaderid=chequedetail.voucherheaderid ");
				while(resultset.next())
				{
					int chequeid = Integer.parseInt(resultset.getString(1));
					if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside reverse receipt:"+updateChqQuery+chequeid);
					statement.executeUpdate(updateChqQuery+chequeid);
				}
			}
			if(sub.equals("subledger"))
			{
				resultset= statement.executeQuery("select chequedetail.id from chequedetail,subledgerpaymentheader where "
		                        +" chequedetail.id=subledgerpaymentheader.chequeid and subledgerpaymentheader.voucherheaderid in(select id from voucherheader where cgn='"+cgNum+"')");
				while(resultset.next())
				{
					int chequeid = Integer.parseInt(resultset.getString(1));
					if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside reverse subledger:"+updateChqQuery+chequeid);
					statement.executeUpdate(updateChqQuery+chequeid);
				}
			}
		}catch(Exception e){
			LOGGER.error("Exception in reverse"+e.getMessage());
			throw taskExc;
		}finally{
			resultset.close();
			statement.close();
		}
		
	}

	public void cancel(Connection connection,String vchrNo,String vchrDate)throws Exception
	{
		EGovernCommon commommethods = new EGovernCommon();
		lastModified = commommethods.getCurrentDate();
		try
   		{
			lastModified = formatter.format(sdf.parse(lastModified));
			vchrDate=formatter.format(sdf1.parse(vchrDate));
			if(LOGGER.isDebugEnabled())     LOGGER.debug("lastModified:2"+lastModified+" vchrDate:"+vchrDate);   		
			String updateQuery ="update chequedetail set IsDeposited=0,LastModified='"+lastModified+"' where PayInSlipDate='"+vchrDate+"' and PayInSlipNumber='"+vchrNo+"'";
			if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
			statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
   		}
		catch(Exception e){
			LOGGER.error("lastemodified and voucherdate date parse error");
			throw taskExc;
		}finally{
			statement.close();
		}
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate();
			try
	   		{
				created = formatter.format(sdf.parse(created));
				setLastModified(created);
				updateQuery = updateQuery.substring(0,updateQuery.length()-1)+ " WHERE id = " + id;
				if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
				statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
	   		}catch(Exception e){	   			
	   			throw taskExc;
	   		}finally{
	   			statement.close();
	   		}			
			updateQuery="UPDATE chequedetail SET";
		}
	}

	public void reverseUsingVid(Connection connection)throws SQLException,TaskFailedException
	{
		try{
			statement=connection.createStatement();
			String reverseQuery="UPDATE chequedetail SET IsReversed=1 WHERE id="+id;
			if(LOGGER.isDebugEnabled())     LOGGER.debug(reverseQuery);
			statement.executeQuery(reverseQuery);
		}catch(Exception e){
			throw taskExc;
		}finally{
			statement.close();
		}
	}
	
	public void reverseUsingVhid(Connection connection, String vhId)throws SQLException,TaskFailedException
	{		
		try{
			statement=connection.createStatement();
			String reverseQuery="UPDATE chequedetail SET IsReversed=1 WHERE voucherheaderid="+vhId;
			if(LOGGER.isDebugEnabled())     LOGGER.debug("reverseUsingVhid is "+reverseQuery);
			statement.executeQuery(reverseQuery);
		}catch(Exception e){
			throw taskExc;
		}finally{
			statement.close();
		}
	}
}
