/*
 * DishonoredEntriesDelegate.java  Created on June 1, 2007
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.FinancialConstants;
import org.hibernate.SQLQuery;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.CommonMethodsI;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Iliyaraja
 *
 * @Version 1.00
 */
public class DishonoredEntriesDelegate {

	private static  final Logger LOGGER = Logger.getLogger(DishonoredEntriesDelegate.class);
	EGovernCommon ecm = new EGovernCommon();
	CommonMethodsI cm=new CommonMethodsImpl();
	String bankGlCode="";


		/**
		 * This function will generate the voucher number for the core product
		 * @param type
		 * @param con
		 * @param fundid
		 * @return voucher number
		 */
		public String getVoucherNumber(String type,Connection con,String fundid,int fieldid,String vdate)throws Exception{
			String vno=null;
			LOGGER.info("Inside getVoucherNumber-->DishonoredEntriesDelegate");
			vno=ecm.maxVoucherNumber(type,con,fundid);
			return vno;
		}

		/**
		 * This function will generate  the cgvn number for the core product
		 * @param voucherNum
		 * @param fiscalPeriodId
		 * @param con
		 * @return cgvn number
		 */
		public String getcgvn(String voucherNum,String fiscalPeriodId,Connection con)throws Exception{
			LOGGER.info("Inside getcgvn-->DishonoredEntriesDelegate");
			String cgvn=null;

			String vType=voucherNum.substring(0,Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
			cgvn=ecm.getEg_Voucher(vType,fiscalPeriodId,con);
			for(int i=cgvn.length();i<5;i++){
				cgvn="0"+cgvn;
			}
			cgvn=vType+cgvn;
			return cgvn;
		}
		/**
		 * This function will return the cgnumber for voucherheader
		 * @param type
		 * @return
		 * @throws Exception
		 */
		public String getCgNumber(String type,String fpId,Connection con,String vdate)throws Exception{
			LOGGER.info("Inside getCgNumber-->DishonoredEntriesDelegate");
			String cgn=null;
			cgn=type+ecm.getCGNumber();
			return cgn;
		}

// This Query For Both reversal cheque and bank charges -- based on the param--vHId
//get Dishonored cheque-Reversal Voucher Account Code Details for view and print purpose

	public ArrayList getDishonoredRevVoucherAccCodeDetails(Long vHId,Connection con) throws Exception{
		LOGGER.info(" INSIDE getDishonoredRevVoucherAccCodeDetails()>>>>>>>> ");

		StringBuffer basicquery1 = new StringBuffer("SELECT vd.glcode AS \"accCode\",vd.accountname AS \"accDesc\",vd.debitamount AS \"debAmt\","
		+" vd.creditamount AS \"creAmt\" FROM generalledger gl,voucherDetail vd,voucherheader vh");

		StringBuffer wherequery1 = new StringBuffer(" WHERE vd.voucherheaderId=vh.id AND gl.VOUCHERHEADERID=vh.ID "
		+" AND vd.id=gl.VOUCHERLINEID ");

		ArrayList al=new ArrayList();
		Statement statement=null;
		ResultSet rs=null;

		try{
		statement = con.createStatement();

		//LOGGER.info(" bankAccId value is----->"+bankAccId);
		if (vHId !=null && vHId !=0){
			wherequery1 = wherequery1.append(" AND vh.ID=").append(vHId);
		}

		// debit entry should show in the view screen
	String query = new StringBuffer().append(basicquery1).append(wherequery1).append(" order by \"debAmt\" desc").toString();

	LOGGER.info("  DishonoredCheque getDishonoredRevVoucherAccCodeDetails Query is  "+query);
			rs=statement.executeQuery(query);
			DishonoredViewEntries dve;
			
			while (rs.next()){
				dve=new DishonoredViewEntries();
				dve.setReversalAccCode(rs.getString("accCode"));
				dve.setReversalDescn(rs.getString("accDesc"));
				dve.setReversalDebitAmount(rs.getString("debAmt"));
				dve.setReversalCreditAmount(rs.getString("creAmt"));
				al.add(dve);
			}
		}
		catch(Exception e){
			LOGGER.error("Exp="+e.getMessage());
			throw new TaskFailedException();
		}
		finally{
			statement.close();
			rs.close();
		}
		return al;
}


// This Query For Both reversal cheque and bank charges -- based on the param--vHId
//get Dishonored cheque-Reversal Voucher Header Information for view and print purpose

	public ArrayList getDishonoredRevVoucherHeaderDetails(Long vHId,Connection con) throws Exception{
		LOGGER.info(" INSIDE getDishonoredRevVoucherHeaderDetails()>>>>>>>> ");
		
		StringBuffer basicquery1 = new StringBuffer("SELECT vh.NAME as \"vhName\",vh.DESCRIPTION as \"reason\",vh.VOUCHERNUMBER as \"vouNo\",TO_CHAR(vh.VOUCHERDATE,'dd/mm/yyyy') as \"vouDate\","
		+" fund.NAME as \"fundName\",ih.instrumentnumber AS \"refNo\",TO_CHAR(ih.instrumentdate,'dd/mm/yyyy') AS \"refDate\" "
		+" FROM  voucherheader vh,fund fund,EGF_INSTRUMENTHEADER ih,EGF_INSTRUMENTVOUCHER iv ");
		StringBuffer wherequery1 = new StringBuffer(" WHERE vh.FUNDID=fund.ID and vh.id=iv.VOUCHERHEADERID and iv.INSTRUMENTHEADERID=ih.id ");
		
		ArrayList al=new ArrayList();
		Statement statement=null;
		ResultSet rs=null;
	
		try{
			statement = con.createStatement();
			if (vHId !=null && vHId !=0){
				wherequery1 = wherequery1.append(" AND vh.ID=").append(vHId);
			}
		
			String query = new StringBuffer().append(basicquery1).append(wherequery1).toString();
		
			LOGGER.info("  DishonoredCheque getDishonoredRevVoucherHeaderDetails Query is  "+query);
			rs=statement.executeQuery(query);
			DishonoredViewEntries dve;
	
			while (rs.next()){
				dve=new DishonoredViewEntries();
				
				dve.setVouHName(rs.getString("vhName"));
				dve.setReason(rs.getString("reason"));
				dve.setVoucherNumber(rs.getString("vouNo"));
				dve.setVouDate(rs.getString("vouDate"));
				dve.setFund(rs.getString("fundName"));
				dve.setRefNo(rs.getString("refNo"));
				dve.setRefDate(rs.getString("refDate"));
				al.add(dve);
			}
		}catch(Exception e){
			LOGGER.error("Exp="+e.getMessage());
			throw new TaskFailedException();
		}
		finally{
			statement.close();
			rs.close();
		}
		return al;
	}

	public void updateInstrumentHeader(int instrumentHeaderId,PersistenceService persistenceService) throws Exception{
		try{
			String updateQuery="update egf_instrumentheader set id_status=(select id from egw_status where moduletype='Instrument' and " +
					"description='Dishonored') where id="+instrumentHeaderId;
			SQLQuery query = persistenceService.getSession().createSQLQuery(updateQuery);
			query.executeUpdate();
		}catch(Exception e){
			throw e;
		}
	}
}
