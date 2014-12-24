/*
 * BankEntries.java  Created on Aug 25, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import com.exilant.eGov.src.transactions.brs.BrsEntries;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.util.Locale;

/**
 * @author Tilak
 *
 * @Version 1.00
 */
public class BankEntries
{
	private String id=null;
	private int bankAccountId;
	private String refNo=null;
	private String type=null;
	private String txnDate="";
	private String txnAmount=null;
	private String glcodeId=null;
	private String voucherheaderId=null;
	private String remarks=null;
	private Long instrumentHeaderId=null;
	/**
	 * @return the instrumentHeaderId
	 */
	public Long getInstrumentHeaderId() {
		return instrumentHeaderId;
	}
	/**
	 * @param instrumentHeaderId the instrumentHeaderId to set
	 */
	public void setInstrumentHeaderId(Long instrumentHeaderId) {
		this.instrumentHeaderId = instrumentHeaderId;
		updateQuery = updateQuery + " instrumentHeaderId=" + instrumentHeaderId + ","; isField = true;
	}

	private boolean isId=false, isField=false;
	private String  updateQuery="UPDATE bankentries SET";
	private SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",Locale.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	private static  final Logger LOGGER = Logger.getLogger(BankEntries.class);
	private TaskFailedException taskExc;
	
	public int getBankAccountId() {
		return bankAccountId;
	}
	/**
	 * @param bankAccountId The bankAccountId to set.
	 */
	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	/**
	 * @param glcodeId The glcodeId to set.
	 */
	public void setGlcodeId(String glcodeId) {
		this.glcodeId = glcodeId;
		updateQuery = updateQuery + " glcodeid=" + glcodeId + ","; isField = true;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
		isId=true;
	}
	/**
	 * @param refNo The refNo to set.
	 */
	public void setRefNo(String refNo) {
		this.refNo = refNo;
		updateQuery = updateQuery + " refno='" + refNo + "',"; isField = true;
	}
	/**
	 * @param txnAmount The txnAmount to set.
	 */
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
		updateQuery = updateQuery + " txnAmount=" + txnAmount + ","; isField = true;
	}
	/**
	 * @param txnDate The txnDate to set.
	 */
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
		updateQuery = updateQuery + " txndate='" + txnDate + "',"; isField = true;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
		updateQuery = updateQuery + " type='" + type + "',"; isField = true;
	}
	/**
	 * @param voucherheaderId The voucherheaderId to set.
	 */
	public void setVoucherheaderId(String voucherheaderId) {
		this.voucherheaderId = voucherheaderId;
		updateQuery = updateQuery + " voucherheaderId='" + voucherheaderId + "',"; isField = true;
	}
	/**
	 * @param remarks The remarks to set.
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
		updateQuery = updateQuery + " remarks='" + remarks + "',"; isField = true;
	}
	@Deprecated
	public void insert(Connection connection) throws TaskFailedException,SQLException
	{
		Statement statement=null;
		try
		{
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("bankEntries")));
			String insertQuery = "INSERT INTO bankEntries (Id, BankAccountId, refNo,type,txndate,txnamount,glcodeid,VoucherHeaderId,remarks,instrumentHeaderId)" +
							"VALUES (" + id + ", " + bankAccountId + ", '" + refNo + "','" + type	+ "', '" + txnDate + "', " + txnAmount + ", " + glcodeId + ", " + voucherheaderId + ",'"+ remarks +"',"+instrumentHeaderId+")";
			LOGGER.debug(insertQuery);
			statement = connection.createStatement();
			statement.executeUpdate(insertQuery);
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage(),e);
			throw taskExc;
		}
		finally
		{
			statement.close();
		}
	}
	
	
	public void insert() throws TaskFailedException,SQLException
	{
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {

				Statement statement=null;
				try
				{
					setId( String.valueOf(PrimaryKeyGenerator.getNextKey("bankEntries")));
					String insertQuery = "INSERT INTO bankEntries (Id, BankAccountId, refNo,type,txndate,txnamount,glcodeid,VoucherHeaderId,remarks,instrumentHeaderId)" +
									"VALUES (" + id + ", " + bankAccountId + ", '" + refNo + "','" + type	+ "', '" + txnDate + "', " + txnAmount + ", " + glcodeId + ", " + voucherheaderId + ",'"+ remarks +"',"+instrumentHeaderId+")";
					LOGGER.debug(insertQuery);
					statement = connection.createStatement();
					statement.executeUpdate(insertQuery);
				}
				catch(Exception e)
				{
					LOGGER.error(e.getMessage(),e);
					
				}
				finally
				{
					statement.close();
					HibernateUtil.release(statement, null);
				}
				
			}
		});
		
	}
	@Deprecated
	public void update(Connection connection) throws TaskFailedException,SQLException
	{
		Statement statement=null;
		if(isId && isField)
		{
			try	{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			LOGGER.info(updateQuery);
			statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			updateQuery="UPDATE bankbranch SET";
			}catch(Exception e)	{
				LOGGER.error(e.getMessage(),e);
				throw taskExc;
			}finally
			{
				statement.close();
			}
		}
	}
	
	public void update() throws SQLException
	{
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {

				Statement statement=null;
				if(isId && isField)
				{
					try	{
					updateQuery = updateQuery.substring(0,updateQuery.length()-1);
					updateQuery = updateQuery + " WHERE id = " + id;
					LOGGER.info(updateQuery);
					statement = connection.createStatement();
					statement.executeUpdate(updateQuery);
					updateQuery="UPDATE bankbranch SET";
					}catch(Exception e)	{
						LOGGER.error(e.getMessage(),e);
						
					}finally
					{
						statement.close();
					}
				}
			
				
			}
		});
	}
	
	public void reverse(Connection connection,String cgNum)throws SQLException,TaskFailedException
	{
		Statement statement =null;
		try{
			String updateQuery = "update bankentries  set isreversed=1 where voucherheaderid in(select id from voucherheader where cgn='"+cgNum+"')";
			LOGGER.debug(updateQuery);
			statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in reverse:"+e.getMessage(),e);
			throw taskExc;
		}
		finally
		{
			statement.close();
		}
	}
	
	public ArrayList getRecords(final String bankAccId) throws SQLException
	{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ArrayList>() {

			@Override
			public ArrayList execute(Connection con) throws SQLException {
			

				String query="SELECT be.id as \"id\", be.refNo as \"refNo\",  be.type as \"type\", "
							+" be.txnDate as \"txnDate\", "
							+" be.txnAmount as \"txnAmount\", "
							+" be.remarks as \"remarks\",be.glcodeid as \"glcodeid\",be.instrumentHeaderId as\"instrumentHeaderId\" "
							+" from bankentries be,bankaccount ba where be.bankaccountid=ba.id and ba.id="+bankAccId+" and be.voucherheaderid is null ORDER BY txnDate";
				LOGGER.info("  query   "+query);
				ArrayList al=new ArrayList();
				Statement statement=null;
				ResultSet rs=null;
				Date dt;
				try
				{
					statement = con.createStatement();
					 rs=statement.executeQuery(query);
					BrsEntries brs;
					while (rs.next())
					{
						brs=new BrsEntries();
						brs.setId(rs.getString("id"));
						brs.setRefNo(rs.getString("refNo"));
						brs.setType(rs.getString("type"));
						dt = sdf.parse(rs.getString("txnDate"));
						brs.setTxnDate(formatter.format(dt));
						brs.setTxnAmount(rs.getString("txnAmount"));
						brs.setRemarks(rs.getString("remarks"));
						brs.setGlCodeId(rs.getString("glcodeid"));
						brs.setInstrumentHeaderId(rs.getString("instrumentHeaderId"));
						al.add(brs);
					}
				}
				catch(Exception e)
				{
					LOGGER.error(e.getMessage(),e);
					
				}
				finally
				{
					rs.close();
					statement.close();				
				}
				return al;
		
			}
		});
		
	}
	@Deprecated
	public ArrayList getRecords(String bankAccId,Connection con) throws TaskFailedException,SQLException
	{
			String query="SELECT be.id as \"id\", be.refNo as \"refNo\",  be.type as \"type\", "
						+" be.txnDate as \"txnDate\", "
						+" be.txnAmount as \"txnAmount\", "
						+" be.remarks as \"remarks\",be.glcodeid as \"glcodeid\",be.instrumentHeaderId as\"instrumentHeaderId\" "
						+" from bankentries be,bankaccount ba where be.bankaccountid=ba.id and ba.id="+bankAccId+" and be.voucherheaderid is null ORDER BY txnDate";
			LOGGER.info("  query   "+query);
			ArrayList al=new ArrayList();
			Statement statement=null;
			ResultSet rs=null;
			Date dt;
			try
			{
				statement = con.createStatement();
				 rs=statement.executeQuery(query);
				BrsEntries brs;
				while (rs.next())
				{
					brs=new BrsEntries();
					brs.setId(rs.getString("id"));
					brs.setRefNo(rs.getString("refNo"));
					brs.setType(rs.getString("type"));
					dt = sdf.parse(rs.getString("txnDate"));
					brs.setTxnDate(formatter.format(dt));
					brs.setTxnAmount(rs.getString("txnAmount"));
					brs.setRemarks(rs.getString("remarks"));
					brs.setGlCodeId(rs.getString("glcodeid"));
					brs.setInstrumentHeaderId(rs.getString("instrumentHeaderId"));
					al.add(brs);
				}
			}
			catch(Exception e)
			{
				LOGGER.error(e.getMessage(),e);
				throw taskExc;
			}
			finally
			{
				rs.close();
				statement.close();				
			}
			return al;
	}

	// get all ISRECONCILED=0 Cheque details FOR CORE PRODUCT
	public List getChequeDetails(Long bankAccId,Long bankId, String chequeNo,String chqFromDate,String chqToDate,Connection con) throws TaskFailedException,SQLException
	{
		LOGGER.info(" INSIDE getChequeDetails()>>>>>>>> ");
		Statement statement=null;
		ResultSet rs=null;
		List al=new ArrayList();
		try{
			statement = con.createStatement();
			String detailsQuery = getDetailsQuery(bankAccId,bankId,chequeNo,chqFromDate,chqToDate);
			LOGGER.debug("  DishonoredCheque getChequeDetails instrument  Query is  "+detailsQuery);
			rs = executeChequeDetailsQuery(al, statement, detailsQuery);
		}catch(Exception e){
			LOGGER.error("Exp in getChequeDetails:"+e.getMessage(),e);
			throw taskExc;
		}finally{
			rs.close();
			statement.close();			
		}
		return al;
	}
	private ResultSet executeChequeDetailsQuery(List al,Statement statement, String query) throws SQLException,ParseException {
		ResultSet rs;
		Date dt;
		rs=statement.executeQuery(query);
		BrsEntries brs;
		while (rs.next()){
			brs=new BrsEntries();
			brs.setVoucherNumber(rs.getString("VOUCHERNUMBER"));
			brs.setCgnum(rs.getString("cgnumber"));
			brs.setVoucherHeaderId(rs.getString("voucherHeaderId"));
			brs.setInstrumentHeaderId(rs.getString("instrumentHeaderId"));
			brs.setPayinSlipVHeaderId(rs.getString("payinVHeaderId"));
			brs.setVoucherType(rs.getString("type"));
			brs.setFundId(rs.getString("FUNDID"));
			brs.setFundSourceId(rs.getString("FUNDSOURCEID"));
			brs.setChequeNumber(rs.getString("CHEQUENUMBER"));
			brs.setBankName(rs.getString("bank"));
			brs.setAccNumber(rs.getString("accNumber"));
			brs.setAccIdParam(rs.getString("accIdParam"));
			brs.setPayTo(rs.getString("payTo"));
			brs.setPayCheque(rs.getString("payCheque"));
			dt = sdf.parse(rs.getString("CHEQUEDATE"));
			brs.setChequeDate(formatter.format(dt));
			brs.setAmount(rs.getString("AMOUNT"));
			brs.setDepartmentId(rs.getString("departmentId"));
			brs.setFunctionaryId(rs.getString("functionaryId"));
			LOGGER.debug("BankEntries | getChequeDetails | departmentId>>>"+brs.getDepartmentId());
			LOGGER.debug("BankEntries | getChequeDetails | functionaryId>>>"+brs.getFunctionaryId());
			al.add(brs);
		}
		return rs;
	}
	
	private String getDetailsQuery(Long bankAccId,Long bankId, String chequeNo,String chqFromDate,String chqToDate) {
		StringBuffer basicquery1 = new StringBuffer("SELECT distinct vh.id as \"voucherHeaderId\",ih.id as \"instrumentHeaderId\",vh.id as \"payinVHeaderId\"," +
				"vh.cgn as \"cgnumber\",vh.VOUCHERNUMBER as \"voucherNumber\",vh.TYPE as \"type\",vh.FUNDID as \"fundId\"," +
				"vh.FUNDSOURCEID as \"fundSourceId\",ih.INSTRUMENTNUMBER as \"chequeNumber\",ih.INSTRUMENTDATE as \"chequeDate\"," +
				"ih.INSTRUMENTAMOUNT as \"amount\",bank.NAME as \"bank\",bacc.ACCOUNTNUMBER as \"accNumber\",bacc.ID as \"accIdParam\"," +
				"ih.PAYTO as \"payTo\" ,ih.ISPAYCHEQUE AS \"payCheque\",vmis.DEPARTMENTID AS \"departmentId\",vmis.FUNCTIONARYID AS \"functionaryId\""
				+" FROM VOUCHERHEADER vh,egf_instrumentheader ih,BANK bank,BANKACCOUNT bacc,VOUCHERMIS vmis,bankbranch branch," +
				"egf_instrumenttype it,EGF_INSTRUMENTVOUCHER iv");

		StringBuffer wherequery1 = new StringBuffer(" WHERE vh.status=0 AND vh.id=vmis.voucherheaderid and ih.INSTRUMENTTYPE=it.id and " +
				"it.TYPE='cheque' and iv.VOUCHERHEADERID=vh.ID and iv.INSTRUMENTHEADERID=ih.id and ((ih.ispaycheque=0 and ih.id_status=(select id from " +
				"egw_status where moduletype='Instrument' and description='Deposited')) or (ih.ispaycheque=1 and ih.id_status=(select id from " +
				"egw_status where moduletype='Instrument' and description='New'))) and branch.BANKID=bank.id AND branch.id=bacc.BRANCHID");
				
		StringBuffer orderbyquery = new StringBuffer(" ORDER BY \"voucherNumber\",\"type\",\"chequeDate\" ");
			if (bankAccId !=null && bankAccId !=0){
				wherequery1 = wherequery1.append(" AND ih.BANKACCOUNTID=").append(bankAccId);
				wherequery1 = wherequery1.append(" AND ih.BANKACCOUNTID=bacc.ID");
			}
			if ((bankAccId == null || bankAccId == 0) && bankId !=null && bankId !=0){
				wherequery1 = wherequery1.append(" AND bank.id=").append(bankId);
				wherequery1 = wherequery1.append(" AND ih.BANKACCOUNTID=bacc.id");
			}
			if ((bankAccId == null || bankAccId == 0) && (bankId ==null || bankId == 0)){
				wherequery1 = wherequery1.append(" AND ih.BANKACCOUNTID=bacc.id");
			}
			if (!("").equals(chequeNo)){
				wherequery1 = wherequery1.append(" AND ih.INSTRUMENTNUMBER=trim('").append(chequeNo).append("')");
			}
			if (!("").equals(chqFromDate)){
				wherequery1 = wherequery1.append(" AND ih.INSTRUMENTDATE >'").append(chqFromDate).append("'");
			}
			if (!("").equals(chqToDate)){
				wherequery1 = wherequery1.append(" AND ih.INSTRUMENTDATE <'").append(chqToDate).append("'");
			}
			String query = new StringBuffer().append(basicquery1).append(wherequery1).append(orderbyquery).toString();
			return query;
	}
}
