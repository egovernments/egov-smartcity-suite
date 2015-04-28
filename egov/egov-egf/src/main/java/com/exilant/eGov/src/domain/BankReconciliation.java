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
 * Created on Mar 24, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.SQLQuery;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.brs.BrsDetails;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class BankReconciliation {
	private String id = null;
	private String bankAccountId = null;
	private String voucherHeaderId =null ;
	private String isReconciled = "0";
	private String chequeDate = "";
	private String chequeNumber ="";
	private String amount = "0";
	private String reconciliationDate = "";
	private String recChequeDate = "";
	private String transactionDate = "";
	private String transactionBalance = null;
	private String transactionType = null;
	private String isReversed = "0";
	private String type =null;
	private String updateQuery="UPDATE bankReconciliation SET";
	private boolean isId=false, isField=false;
	private String isDishonored = null; // for Dishonored chq details
	private static final Logger LOGGER=Logger.getLogger(BankReconciliation.class);
	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	private SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",Locale.getDefault());
	public GenericHibernateDaoFactory genericDao;
	
	 
	private TaskFailedException taskExc;
	
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setBankAccountId(String aBankAccountId){ bankAccountId = aBankAccountId; updateQuery = updateQuery + " bankAccountId=" + bankAccountId + ","; isField = true;}
	public void setVoucherHeaderId(String aVoucherHeaderId){ voucherHeaderId = aVoucherHeaderId; updateQuery = updateQuery + " VoucherHeaderId=" + voucherHeaderId + ","; isField = true;}
	public void setIsReconciled(String aIsReconciled){ isReconciled = aIsReconciled; updateQuery = updateQuery + " isReconciled=" + isReconciled + ","; isField = true;}
	public void setChequeDate(String aChequeDate){ chequeDate = aChequeDate; updateQuery = updateQuery + " chequeDate='" + chequeDate + "',"; isField = true;}
	public void setChequeNumber(String aChequeNumber){ chequeNumber = aChequeNumber; updateQuery = updateQuery + " chequeNumber='" + chequeNumber + "',"; isField = true;}
	public void setAmount(String aAmount){ amount = aAmount; updateQuery = updateQuery + " amount=" + amount + ","; isField = true;}
	public void setReconciliationDate(String aReconciliationDate){ reconciliationDate = aReconciliationDate; updateQuery = updateQuery + " reconciliationDate='" + reconciliationDate + "',"; isField = true;}
	public void setRecChequeDate(String aRecChequeDate){ recChequeDate = aRecChequeDate; updateQuery = updateQuery + " recChequeDate='" + recChequeDate + "',"; isField = true;}
	public void setTransactionDate(String aTransactionDate){ transactionDate = aTransactionDate; updateQuery = updateQuery + " transactionDate='" + transactionDate + "',"; isField = true;}
	public void setTransactionBalance(String aTransactionBalance){ transactionBalance = aTransactionBalance; updateQuery = updateQuery + " transactionBalance=" + transactionBalance + ","; isField = true;}
	public void setTransactionType(String aTransactionType){ transactionType = aTransactionType; updateQuery = updateQuery + " transactionType='" + transactionType + "',"; isField = true;}
	public void setIsReversed(String aIsReversed){ isReversed = aIsReversed; updateQuery = updateQuery + " isReversed=" + isReversed + ","; isField = true;}
	public void setType(String aType){ type = aType; updateQuery = updateQuery + " type='" + type + "',"; isField = true;}
	public void setIsDishonored(String aIsDishonored){ isDishonored = aIsDishonored; updateQuery = updateQuery + " isReconciled=" + isDishonored + ","; isField = true;}


	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();
		transactionDate = commommethods.getCurrentDateTime(connection);
		PreparedStatement pst =null;
		try
   		{
			transactionDate = formatter.format(sdf.parse(transactionDate));
  			setTransactionDate(transactionDate);
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("bankReconciliation")));
			String insertQuery = "INSERT INTO bankReconciliation (Id, BankAccountId, VoucherHeaderId, IsReconciled, " +
							"ChequeDate, ChequeNumber, Amount, ReconciliationDate, TransactionDate, TransactionBalance, TransactionType,isReversed,type,recChequeDate,IsDishonored) " +
							"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, to_date(?, 'DD-MON-YYYY HH24:MI:SS" + "'), ?, ?, ?, ?, ?, ?)";
			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			pst = connection.prepareStatement(insertQuery);
			pst.setString(1, id);
			pst.setString(2, bankAccountId);
			pst.setString(3, voucherHeaderId);
			pst.setString(4, isReconciled);
			pst.setString(5, chequeDate);
			pst.setString(6, chequeNumber);
			pst.setString(7, amount);
			pst.setString(8, reconciliationDate);
			pst.setString(9, transactionDate);
			pst.setString(10, transactionBalance);
			pst.setString(11, transactionType);
			pst.setString(12, isReversed);
			pst.setString(13, type);
			pst.setString(14, recChequeDate);
			pst.setString(15, isDishonored);
			pst.executeUpdate();
   		}
   		catch(Exception e){
   			throw taskExc;
   		}finally{
   			pst.close();   				
   		}
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			PreparedStatement pst =null;
			try{
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = ?";
				if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
				pst = connection.prepareStatement(updateQuery);
				pst.setString(1, id);
				pst.executeUpdate();
			}catch(Exception e){
	   			throw taskExc;
	   		}finally{
				pst.close();   				
	   		}
				updateQuery="UPDATE bankReconciliation SET";
		}
	}


	public void reverse(Connection connection,String cgNum)throws SQLException,TaskFailedException{
			String updateQuery = "update bankreconciliation  set isreversed=1 where voucherheaderid in(select id from voucherheader where cgn='"+cgNum+"')";
			PreparedStatement pst =null;
			try{
				pst = connection.prepareStatement(updateQuery);
				pst.executeUpdate();
			}catch(Exception e){
				throw taskExc;
			}finally{
				pst.close();   				
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
		}

	public ArrayList getRecordsToReconcile(String bankAccId,String recFromDate,String asOnDate,Connection con) throws TaskFailedException,SQLException
	{
		String FromDateCondition="";
		if(StringUtils.isNotEmpty(recFromDate)){
			FromDateCondition=FromDateCondition+" AND IH.INSTRUMENTDATE>= TO_DATE('#fromDate','DD-MON-YYYY') ";
			FromDateCondition=FromDateCondition.replaceAll("#fromDate",recFromDate);
		}
		String voucherExcludeStatuses=getExcludeStatuses();
		String query= "SELECT  ih.instrumentNumber  as \"chequeNumber\","
						+"  rec.id as \"recid\",ih.id as \"instrumentHeaderId\",  decode(ih.ispaycheque, '0', 'Receipt',decode(ih.ispaycheque, '1', 'Payment')  ) as \"type\"," 
						+"  ih.instrumentDate as \"chequedate\",ih.instrumentAmount as \"chequeamount\"  FROM BANKRECONCILIATION rec, BANKACCOUNT BANK,"
						+"VOUCHERHEADER v ,egf_instrumentheader ih, egf_instrumentotherdetails io, egf_instrumentVoucher iv	WHERE   ih.bankAccountId = BANK.ID AND bank.id =?"
						+"  "+ FromDateCondition+" AND IH. INSTRUMENTDATE <= TO_DATE(? || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
						+"AND v.ID= iv.voucherheaderid and v.STATUS not in ("+voucherExcludeStatuses+") AND( (ih.ispaycheque=0 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
						+" AND rec.instrumentHeaderId=ih.id	 and iv.instrumentHeaderid=ih.id  and io.instrumentHeaderId=ih.id and ih.instrumentnumber is  not null UNION ALL "
												
						+"SELECT  decode(ih.transactionnumber, null, 'Direct', ih.transactionnumber) as \"chequeNumber\",  rec.id as \"recid\",ih.id as \"instrumentHeaderId\", "
						+" decode(ih.ispaycheque, '0', 'Receipt',decode(ih.ispaycheque, '1', 'Payment') ) as \"type\", "
						+"  "
						+" ih.transactiondate as \"chequedate\",ih.instrumentAmount as \"chequeamount\" "
						+" FROM BANKRECONCILIATION rec, BANKACCOUNT BANK,VOUCHERHEADER v ,egf_instrumentheader ih, egf_instrumentotherdetails io, egf_instrumentVoucher iv	WHERE   ih.bankAccountId = BANK.ID AND bank.id =?"
						+"  "+ FromDateCondition+" AND IH.INSTRUMENTDATE <= TO_DATE(? || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
						+"AND v.ID= iv.voucherheaderid and v.STATUS not in  ("+voucherExcludeStatuses+") AND ( (ih.ispaycheque=0 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
						+" AND rec.instrumentHeaderId=ih.id	 and iv.instrumentHeaderid=ih.id and io.instrumentHeaderId=ih.id and ih.transactionnumber is not null   "						
						+" ORDER BY \"chequedate\" ,\"chequeNumber\" ,\"type\"" ;

			if(LOGGER.isInfoEnabled())     LOGGER.info("  query  inside getRecordsToReconcile: "+query);
			String vouchersQuery="select v.vouchernumber as \"voucherNumber\",v.id as \"id\" from egf_instrumentvoucher iv ,voucherheader v "
				+"where v.id=iv.voucherheaderid and iv.instrumentHeaderId=?";
			
			ArrayList al=new ArrayList();
			
			 
	 		Date dt;
			PreparedStatement pst=null;
			ResultSet rs=null;
			ResultSet vouchersRS=null;
			PreparedStatement vouchersPS=con.prepareStatement(vouchersQuery);
			try
			{
				pst = con.prepareStatement(query);
				pst.setString(1,bankAccId);
				pst.setString(2, asOnDate);
				pst.setString(3,bankAccId);
				pst.setString(4,asOnDate);
				rs=pst.executeQuery();
				BrsDetails brs;
				while (rs.next())
				{
					brs=new BrsDetails();
					brs.setChequeAmount(rs.getString("chequeamount"));
					dt=new Date();
					dt = sdf1.parse(rs.getString("chequedate"));
					brs.setChequeDate(formatter.format(dt));
					brs.setChequeNumber(rs.getString("chequeNumber"));
					brs.setRecordId(rs.getInt("recid"));
					brs.setType(rs.getString("type"));
					//dt=new Date();
					/*dt = sdf1.parse(rs.getString("voucherdate"));
					brs.setVoucherDate(formatter.format(dt));*/
					//brs.setVoucherNumber(rs.getString("vouchernumber"));
					//brs.setCgnum(rs.getString("cgnumber"));
					brs.setInstrumentHeaderId(rs.getString("instrumentHeaderId"));
					vouchersPS.setLong(1, Long.valueOf(brs.getInstrumentHeaderId()));
					vouchersRS = vouchersPS.executeQuery();
					brs.setVoucherNumbers(new ArrayList<String>());
					brs.setVoucherHeaderIds(new ArrayList<Long>());
					while(vouchersRS.next())
					{
						brs.getVoucherNumbers().add(vouchersRS.getString("voucherNumber"));
						brs.getVoucherHeaderIds().add(vouchersRS.getLong("id"));
					}
					al.add(brs);
				}
				
			}
			
			
			
			catch(Exception e)
			{
				LOGGER.error("Exp in getRecordsToReconcile :"+e.getMessage());
				throw taskExc;
			}
			finally
			{
				pst.close();
				rs.close();
			}
			return al;
	}
//this is commented while changing to Instrument
	/*public String getUnReconciledDrCr(String bankAccId,String recDate,Connection con) throws Exception
	{

		String query="SELECT (sum(decode(rec.transactionType, 'Cr',decode(rec.type, 'C', rec.amount,0),0)))+ "
					+" (sum(decode(rec.transactionType, 'Cr',decode(rec.type, 'O', rec.amount,0),0)))+ "
					+" (sum(decode(rec.transactionType, 'Cr',decode(rec.type, 'D', rec.amount,0),0)))   AS \"brs_creditTotal\", "
					+" (sum(decode(rec.transactionType, 'Dr',decode(rec.type, 'R', rec.amount,0),0))) AS \"brs_creditTotalOthers\", "
					+" (sum(decode(rec.transactionType, 'Dr',decode(rec.type, 'C', rec.amount,0),0)) )+ "
					+" (sum(decode(rec.transactionType, 'Dr',decode(rec.type, 'O', rec.amount,0),0)))+ "
					+" (sum(decode(rec.transactionType, 'Dr',decode(rec.type, 'D', rec.amount,0),0)))  AS \"brs_debitTotal\", "
					+" (sum(decode(rec.transactionType, 'Cr',decode(rec.type, 'P', rec.amount,0),0))) AS \"brs_debitTotalOthers\" "
					+" FROM bankReconciliation rec, bankAccount bank,voucherheader vh WHERE rec.bankAccountId = bank.id AND bank.id ="+bankAccId+" "
					+" AND rec.isReversed = 0 AND (rec.reconciliationDate > to_date('"+recDate+"'  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
					+" OR (rec.isReconciled = 0)) AND vh.VOUCHERDATE <= to_date('"+recDate+"'  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS')  and vh.id=rec.VOUCHERHEADERID and vh.STATUS<>4";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("  query  in getUnReconciledDrCr: "+query);
		String unReconciledDrCr="";
		Statement statement=null;
		ResultSet rs=null;
		try
		{
			statement = con.createStatement();
			rs=statement.executeQuery(query);

			if (rs.next())
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(rs.getString(1)+"**"+rs.getString(2)+"***"+rs.getString(3)+"***"+rs.getString(4));
				unReconciledDrCr=(rs.getString("brs_creditTotal") != null ? rs.getString("brs_creditTotal") : "0" )+"/"+(rs.getString("brs_creditTotalOthers")!= null ? rs.getString("brs_creditTotalOthers") : "0") +"/"+ (rs.getString("brs_debitTotal")!= null ? rs.getString("brs_debitTotal") : "0") +"/"+( rs.getString("brs_debitTotalOthers")!= null ? rs.getString("brs_debitTotalOthers") : "0")+"";
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnReconciledDrCr"+e.getMessage());
			throw taskExc;
		}
		finally
		{
			statement.close();
			rs.close();
		}
		return unReconciledDrCr;
	}
	*/
	
	/*will fetch all instruments which are in deposited state
	 * in  payments  Bank Charges are taken as creditOthertotal and  rest all summed up for credit total
	 * in receipts   Interest Earned is taken as debitOthertotal and rest all summed up for debit total  
	 * 
	 */
	@Deprecated
	public String getUnReconciledDrCr(String bankAccId,String recDate,Connection con) throws Exception
	{

		String instrumentsForTotal="decode(iv.voucherHeaderId,null,0,ih.instrumentAmount)";
		String instrumentsForBrsEntryTotal="decode(br.voucherHeaderId,null,ih.instrumentAmount,0)";
		//String instrumentsForOtherTotal="decode(br.voucherHeaderId,null,ih.instrumentAmount,0)";
		String voucherExcludeStatuses=getExcludeStatuses();
		
		String totalQuery="SELECT (sum(decode(ih.ispaycheque, '1',ih.instrumentAmount ,0)))  AS \"brs_creditTotal\", "
			+" (sum(decode(ih.ispaycheque, '0',ih.instrumentAmount,0)) ) AS \"brs_debitTotal\" "
			+" FROM egf_instrumentheader ih 	WHERE   ih.bankAccountId =? "
			+" AND IH.INSTRUMENTDATE >= TO_DATE(?,'DD-MON-YYYY')" 
			+" AND IH.INSTRUMENTDATE <= TO_DATE(?,'DD-MON-YYYY') "
			+" AND  ( (ih.ispaycheque=0 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
			+" and ih.instrumentnumber is not null";
			
		String otherTotalQuery=" SELECT (sum(decode(ih.ispaycheque, '1',ih.instrumentAmount,0)))  AS \"brs_creditTotalOthers\", "
			+" (sum(decode(ih.ispaycheque, '0',ih.instrumentAmount,0)) ) AS \"brs_debitTotalOthers\" "
			+" FROM  egf_instrumentheader ih	WHERE   ih.bankAccountId =?"
			+" AND IH.INSTRUMENTDATE >= TO_DATE(?,'DD-MON-YYYY')"
			+" AND IH.transactiondate <= TO_DATE(? ,'DD-MON-YYYY') "
			+" AND ( (ih.ispaycheque=0 and ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
			+" AND ih.transactionnumber is not null";
		
		String brsEntryQuery=" SELECT (sum(decode(ih.ispaycheque, '1',"+instrumentsForBrsEntryTotal+",0)))  AS \"brs_creditTotalBrsEntry\", "
		+" (sum(decode(ih.ispaycheque, '0',"+instrumentsForBrsEntryTotal+",0)) ) AS \"brs_debitTotalBrsEntry\" "
		+" FROM egf_instrumentheader ih, bankentries br	WHERE   ih.bankAccountId = ?"
		+" AND IH.transactiondate >= TO_DATE(? ,'DD-MON-YYYY') "
		+" AND IH.transactiondate <= TO_DATE(? ,'DD-MON-YYYY') "
		+" AND ( (ih.ispaycheque=0 and ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
		+" AND br.instrumentHeaderid=ih.id and ih.transactionnumber is not null"	;
	
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for  total : "+totalQuery);
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for other than cheque/DD: "+otherTotalQuery);
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for bankEntries: "+brsEntryQuery);
		
		String unReconciledDrCr="";
		PreparedStatement pst=null;
		ResultSet rs=null;
		String creditTotal=null;
		String creditOthertotal=null;
		String debitTotal=null;
		String debitOtherTotal=null;
		String creditTotalBrsEntry=null;
		String debitTotalBrsEntry=null;
		
		try
		{
			pst = con.prepareStatement(totalQuery);
			pst.setString(1,bankAccId);
			pst.setString(2,recDate);
		
			
			rs=pst.executeQuery();

			if (rs.next())
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(rs.getString(1)+"**"+rs.getString(2));
				creditTotal=rs.getString("brs_creditTotal");
				debitTotal=rs.getString("brs_debitTotal");
				
				
			}
			pst = con.prepareStatement(otherTotalQuery);
			pst.setString(1,bankAccId);
			pst.setString(2,recDate);
			rs=pst.executeQuery();
			if (rs.next())
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(rs.getString(1)+"**"+rs.getString(2));
				creditOthertotal= rs.getString("brs_creditTotalOthers");
				debitOtherTotal=rs.getString("brs_debitTotalOthers");
		
			}
			
			pst = con.prepareStatement(brsEntryQuery);
			pst.setString(1,bankAccId);
			pst.setString(2,recDate);
			rs=pst.executeQuery();
			if (rs.next())
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(rs.getString(1)+"**"+rs.getString(2));
				creditTotalBrsEntry= rs.getString("brs_creditTotalBrsEntry");
				debitTotalBrsEntry=rs.getString("brs_debitTotalBrsEntry");
		
			}
			
		unReconciledDrCr=(creditTotal != null ? creditTotal : "0" )+"/"+(creditOthertotal!= null ? creditOthertotal : "0")
		+"/"+(debitTotal!= null ? debitTotal : "0") +"/"+( debitOtherTotal!= null ? debitOtherTotal : "0")+""+
		"/"+(creditTotalBrsEntry!= null ? creditTotalBrsEntry : "0") +"/"+( debitTotalBrsEntry!= null ? debitTotalBrsEntry : "0")+"";
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnReconciledDrCr"+e.getMessage());
			throw taskExc;
		}
		finally
		{
			pst.close();
			rs.close();
		}
		return unReconciledDrCr;
	}
	/*will fetch all instruments which are in deposited state
	 * in  payments  Bank Charges are taken as creditOthertotal and  rest all summed up for credit total
	 * in receipts   Interest Earned is taken as debitOthertotal and rest all summed up for debit total  
	 * 
	 */	
	public String getUnReconciledDrCr(Integer bankAccId,Date fromDate,Date toDate) throws Exception
	{

		String instrumentsForTotal="decode(iv.voucherHeaderId,null,0,ih.instrumentAmount)";
		String instrumentsForBrsEntryTotal="decode(br.voucherHeaderId,null,ih.instrumentAmount,0)";
		//String instrumentsForOtherTotal="decode(br.voucherHeaderId,null,ih.instrumentAmount,0)";
		String voucherExcludeStatuses=getExcludeStatuses();
		
		String totalQuery="SELECT (sum(decode(ih.ispaycheque, '1',ih.instrumentAmount ,0)))  AS \"brs_creditTotal\", "
			+" (sum(decode(ih.ispaycheque, '0',ih.instrumentAmount,0)) ) AS \"brs_debitTotal\" "
			+" FROM egf_instrumentheader ih 	WHERE   ih.bankAccountId =:bankAccountId "
			+" AND IH.INSTRUMENTDATE >= :fromDate" 
			+" AND IH.INSTRUMENTDATE <= :toDate"
			+" AND  ( (ih.ispaycheque=0 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
			+" and ih.instrumentnumber is not null";
	//see u might need to exclude brs entries here 
		
		String otherTotalQuery=" SELECT (sum(decode(ih.ispaycheque, '1',ih.instrumentAmount,0)))  AS \"brs_creditTotalOthers\", "
			+" (sum(decode(ih.ispaycheque, '0',ih.instrumentAmount,0)) ) AS \"brs_debitTotalOthers\" "
			+" FROM  egf_instrumentheader ih	WHERE   ih.bankAccountId =:bankAccountId"
			+" AND IH.transactiondate >= :fromDate"
			+" AND IH.transactiondate <= :toDate  "
			+" AND ( (ih.ispaycheque=0 and ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
			+" AND ih.transactionnumber is not null";
		
		String brsEntryQuery=" SELECT (sum(decode(ih.ispaycheque, '1',"+instrumentsForBrsEntryTotal+",0)))  AS \"brs_creditTotalBrsEntry\", "
		+" (sum(decode(ih.ispaycheque, '0',"+instrumentsForBrsEntryTotal+",0)) ) AS \"brs_debitTotalBrsEntry\" "
		+" FROM egf_instrumentheader ih, bankentries br	WHERE   ih.bankAccountId = :bankAccountId"
		+" AND IH.transactiondate >= :fromDate  "
		+" AND IH.transactiondate <= :toDate "
		+" AND ( (ih.ispaycheque=0 and ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
		+" AND br.instrumentHeaderid=ih.id and ih.transactionnumber is not null"	;
	
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for  total : "+totalQuery);
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for other than cheque/DD: "+otherTotalQuery);
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for bankEntries: "+brsEntryQuery);
		
		String unReconciledDrCr="";
		
		
		String creditTotal=null;
		String creditOthertotal=null;
		String debitTotal=null;
		String debitOtherTotal=null;
		String creditTotalBrsEntry=null;
		String debitTotalBrsEntry=null;
		
		try
		{
			SQLQuery totalSQLQuery =HibernateUtil.getCurrentSession().createSQLQuery(totalQuery);
			totalSQLQuery.setInteger("bankAccountId",bankAccId);
			totalSQLQuery.setDate("fromDate",fromDate);
			totalSQLQuery.setDate("toDate",toDate);
			
			List list = totalSQLQuery.list();
			if (list.size()>0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(list.get(0));
				Object [] my = (Object[])list.get(0);
				creditTotal=my[0]!=null?my[0].toString():null;
				debitTotal=my[1]!=null?my[1].toString():null;
			}

			totalSQLQuery =HibernateUtil.getCurrentSession().createSQLQuery(otherTotalQuery);
			totalSQLQuery.setInteger("bankAccountId",bankAccId);
			totalSQLQuery.setDate("fromDate",fromDate);
			totalSQLQuery.setDate("toDate",toDate);
			list = totalSQLQuery.list();
			if (list.size()>0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(list.get(0));
				Object [] my = (Object[])list.get(0);
				creditOthertotal=my[0]!=null?my[0].toString():null;
				debitOtherTotal=my[1]!=null?my[1].toString():null;
			}

			totalSQLQuery =HibernateUtil.getCurrentSession().createSQLQuery(brsEntryQuery);
			totalSQLQuery.setInteger("bankAccountId",bankAccId);
			totalSQLQuery.setDate("fromDate",fromDate);
			totalSQLQuery.setDate("toDate",toDate);
			list = totalSQLQuery.list();
			if (list.size()>0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(list.get(0));
				Object [] my = (Object[])list.get(0);
				creditTotalBrsEntry=my[0]!=null?my[0].toString():null;
				debitTotalBrsEntry=my[1]!=null?my[1].toString():null;
			}


		unReconciledDrCr=(creditTotal != null ? creditTotal : "0" )+"/"+(creditOthertotal!= null ? creditOthertotal : "0")
		+"/"+(debitTotal!= null ? debitTotal : "0") +"/"+( debitOtherTotal!= null ? debitOtherTotal : "0")+""+
		"/"+(creditTotalBrsEntry!= null ? creditTotalBrsEntry : "0") +"/"+( debitTotalBrsEntry!= null ? debitTotalBrsEntry : "0")+"";
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnReconciledDrCr"+e.getMessage());
			throw taskExc;
		}
		return unReconciledDrCr;
	}
	
	
/**
 * @return
 */
private String getExcludeStatuses() {
	final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("finance","statusexcludeReport");
	final String statusExclude = appList.get(0).getValue();
	return statusExclude;
	
}
	public String getUnRecDrCrAndBankEntries(String bankAccId,String recDate,Connection con) throws Exception
	{

		String query="SELECT (sum(decode(rec.transactionType, 'Cr',decode(rec.type, 'C', rec.amount,0),0)))+ "
					+" (sum(decode(rec.transactionType, 'Cr',decode(rec.type, 'O', rec.amount,0),0)))+ "
					+" (sum(decode(rec.transactionType, 'Cr',decode(rec.type, 'D', rec.amount,0),0)))   AS \"brs_creditTotal\", "
					+" (sum(decode(rec.transactionType, 'Dr',decode(rec.type, 'R', rec.amount,0),0)))+ "
					+" sum((select decode(sum(decode(be.type,'R',be.txnamount,0)),null,0,sum(decode(be.type,'R',be.txnamount,0))) from bankentries be,bankaccount bank where bank.id =?  AND "
					+" be.bankAccountId = bank.id and be.txndate<= to_date(? || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
					+" and be.voucherheaderid is null))/COUNT(*) AS \"brs_creditTotalOthers\", "
					+" (sum(decode(rec.transactionType, 'Dr',decode(rec.type, 'C', rec.amount,0),0)) )+ "
					+" (sum(decode(rec.transactionType, 'Dr',decode(rec.type, 'O', rec.amount,0),0)))+ "
					+" (sum(decode(rec.transactionType, 'Dr',decode(rec.type, 'D', rec.amount,0),0)))  AS \"brs_debitTotal\", "
					+" (sum(decode(rec.transactionType, 'Cr',decode(rec.type, 'P', rec.amount,0),0)))+ "
					+" sum((select decode(sum(decode(be.type,'P',be.txnamount,0)),null,0,sum(decode(be.type,'P',be.txnamount,0))) from bankentries be,bankaccount bank where bank.id =?  AND "
					+" be.bankAccountId = bank.id and be.txndate<= to_date(?  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
					+" and be.voucherheaderid is null))/COUNT(*) AS \"brs_debitTotalOthers\" "
					+" FROM bankReconciliation rec, bankAccount bank,voucherheader vh WHERE rec.bankAccountId = bank.id AND bank.id =? "
					+" AND rec.isReversed = 0 AND (rec.reconciliationDate > to_date(?  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
					+" OR (rec.isReconciled = 0)) AND vh.VOUCHERDATE <= to_date(?  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') and vh.id=rec.VOUCHERHEADERID and vh.STATUS not in ("+getExcludeStatuses()+")";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("  query  in getUnRecDrCrAndBankEntries: "+query);
		
		
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  in getUnRecDrCrAndBankEntries: "+query);
		
		String unReconciledDrCr="";
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst = con.prepareStatement(query);
			pst.setString(1,bankAccId);
			pst.setString(2,recDate);
			pst.setString(3,bankAccId);
			pst.setString(4,recDate);
			pst.setString(5,bankAccId);
			pst.setString(6,recDate);
			pst.setString(7,bankAccId);
			pst.setString(8,recDate);
			pst.setString(9,recDate);
			rs=pst.executeQuery();
			if (rs.next())
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(rs.getString(1)+"**"+rs.getString(2)+"***"+rs.getString(3)+"***"+rs.getString(4));
				unReconciledDrCr=(rs.getString("brs_creditTotal") != null ? rs.getString("brs_creditTotal") : "0" )+"/"+(rs.getString("brs_creditTotalOthers")!= null ? rs.getString("brs_creditTotalOthers") : "0") +"/"+ (rs.getString("brs_debitTotal")!= null ? rs.getString("brs_debitTotal") : "0") +"/"+( rs.getString("brs_debitTotalOthers")!= null ? rs.getString("brs_debitTotalOthers") : "0")+"";
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnRecDrCrAndBankEntries:"+e.getMessage());
			throw taskExc;
		}
		finally
		{
			pst.close();
			rs.close();
		}
		return unReconciledDrCr;
	}
	public ArrayList getUnReconciledCheques(String bankAccId,String recDate,Connection con) throws Exception
	{
		String voucherExcludeStatuses=getExcludeStatuses();
String query="select decode(ih.instrumentNumber,null,'Direct',ih.instrumentNumber) as \"chequeNumber\", " +
		"ih.instrumentDate as \"chequedate\" ,ih.instrumentAmount as \"chequeamount\",rec.transactiontype as \"txnType\" , decode(rec.transactionType, 'Cr', 'P','R')  as \"type\" " +
		"FROM BANKRECONCILIATION rec, BANKACCOUNT BANK,"
						+"VOUCHERHEADER v ,egf_instrumentheader ih, egf_instrumentotherdetails io, egf_instrumentVoucher iv	WHERE   ih.bankAccountId = BANK.ID AND bank.id =?"
						+"   AND IH.INSTRUMENTDATE <= TO_DATE(? || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
						+"AND v.ID= iv.voucherheaderid  and v.STATUS not in  ("+voucherExcludeStatuses+") AND ((ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
						+" AND rec.instrumentHeaderId=ih.id	 and iv.instrumentHeaderid=ih.id and io.instrumentheaderid=ih.id and ih.instrumentNumber is not null union  "
						+"select decode(ih.transactionnumber,null,'Direct',ih.transactionnumber) as \"chequeNumber\", " +
						"ih.transactiondate as \"chequedate\" ,ih.instrumentAmount as \"chequeamount\",rec.transactiontype as \"txnType\", decode(rec.transactionType, 'Cr', 'P','R')   as \"type\" " +
						"FROM BANKRECONCILIATION rec, BANKACCOUNT BANK,"
						+"VOUCHERHEADER v ,egf_instrumentheader ih, egf_instrumentotherdetails io, egf_instrumentVoucher iv	WHERE   ih.bankAccountId = BANK.ID AND bank.id =?"
						+"   AND IH.INSTRUMENTDATE <= TO_DATE(? || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
						+"AND v.ID= iv.voucherheaderid and v.STATUS not in  ("+voucherExcludeStatuses+") AND ((ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque=1 and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
						+" AND rec.instrumentHeaderId=ih.id	 and iv.instrumentHeaderid=ih.id and io.instrumentheaderid=ih.id and ih.transactionnumber is not null"
						;
		
		/*String query=" SELECT decode(rec.chequeNumber, null, 'Direct', rec.chequeNumber) as \"chequeNumber\",rec.chequedate as \"chequedate\" ,amount as \"chequeamount\",transactiontype as \"txnType\" ,rec.type as \"type\" from bankreconciliation rec, bankAccount bank, voucherheader vh "
			+" where  rec.bankAccountId = bank.id AND bank.id ="+bankAccId+" and  rec.isReversed = 0 AND (rec.reconciliationDate > to_date('"+recDate+"'  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
			+" OR (rec.isReconciled = 0)) AND vh.VOUCHERDATE <= to_date('"+recDate+"'  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') and vh.id=rec.VOUCHERHEADERID and vh.STATUS<>4"
			+" union "
			+" select refno as \"chequeNumber\", txndate as \"chequedate\", txnamount as \"chequeamount\", decode(type,'R','Dr','Cr') as \"txnType\", "
			+" type as \"type\" from bankentries be,bankAccount bank where  be.bankAccountId = bank.id and bank.id ="+bankAccId+"  "
			+" and txndate<= to_date('"+recDate+"'  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') and voucherheaderid is null ";
*/
		if(LOGGER.isInfoEnabled())     LOGGER.info("Query in getUnReconciledCheques:"+query);
		ArrayList al=new ArrayList();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst = con.prepareStatement(query);
			pst.setString(1, bankAccId);
			pst.setString(2, recDate);
			pst.setString(3, bankAccId);
			pst.setString(4, recDate);
			
			
			rs=pst.executeQuery();
			BrsDetails brs;
			while (rs.next())
			{
				brs=new BrsDetails();
				brs.setChequeAmount(rs.getString("chequeamount"));
				brs.setChequeDate(formatter.format(sdf1.parse(rs.getString("chequedate"))));
				brs.setChequeNumber(rs.getString("chequeNumber"));
				brs.setType(rs.getString("type"));
				brs.setTxnType(rs.getString("txnType"));
				al.add(brs);
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnReconciledCheques:"+e.getMessage());
			throw taskExc;
		}
		finally
		{
			pst.close();
			rs.close();
		}
		return al;
	}

	// update bankReconciliation table for Receipt Reversal -Dishonored Chques Entries
	public void updateReversalDishonorChque(int payinVHeadId,String recDate,String recChqDate,String chqNumber,Connection connection) 
										throws SQLException,TaskFailedException
	{
		PreparedStatement pst =null;
		try{
				String updateQuery="UPDATE bankReconciliation SET isReconciled=1,isDishonored=1,RECONCILIATIONDATE= ? ,RECCHEQUEDATE= ? "
				+" where VOUCHERHEADERID= ? and CHEQUENUMBER= ?";
				if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
				pst = connection.prepareStatement(updateQuery);
				pst.setString(1, recDate);
				pst.setString(2, recChqDate);
				pst.setInt(3, payinVHeadId);
				pst.setString(4, chqNumber);
				pst.executeUpdate();
		}catch(Exception e)
		{
			LOGGER.error("Exp in updateReversalDishonorChque:"+e.getMessage());
			throw taskExc;
		}
		finally
		{
			pst.close();
		}
	}

}
