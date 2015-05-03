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
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.common;

//import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class LoadSubLedgerSalaryData extends AbstractTask{
	private final static Logger LOGGER=Logger.getLogger(LoadSubLedgerSalaryData.class);
	private static TaskFailedException taskExc;
	public void execute (String taskName,
			String gridName,DataCollection dc,
			Connection con,	boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException
			{
				//
				int noOfRec=0;
				ResultSet rset=null;
				PreparedStatement pst=null;
				String cgn=dc.getValue("drillDownCgn");
				try{
					//String mmonth="",fundid="",fundSourceid="",chequeId="";
					String mmonth="",chequeId="";

					String sql="select sbd.mmonth as \"salaryBillDetail_mmonth\" ,vh.fundid as \"fund_id\",vh.fundSourceid as \"fundSource_id\",sph.chequeid from salarybilldetail sbd,voucherheader  vh ,subledgerpaymentheader sph "+
							   " where  sph.salarybillid=sbd.id and sph.voucherheaderid=vh.id and vh.cgn= ?";
					if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst =con.prepareStatement(sql);
					pst.setString(1, cgn);
					rset=pst.executeQuery();
					ResultSetMetaData rmeta=rset.getMetaData();
					if(rset.next()){
						 mmonth=rset.getString(1);
						 //fundid=rset.getString(2);
						 //fundSourceid=rset.getString(3);
						 chequeId=rset.getString(4);
					}
					//rset.close();
					if(chequeId==null || chequeId.equals("0")){
						dc.addValue("subLedgerPaymentHeader_typeOfPayment","Cash");
					}else{
						dc.addValue("subLedgerPaymentHeader_typeOfPayment","Cheque");
					}
					sql="select  paidby as \"subLedgerPaymentHeader_paidBy\",bankaccountid as \"accId\", " +
					" f.id as \"fund_id\", " +
					" fs.id as \"fundSource_id\" ," +
					" paidto as \"chequeDetail_payTo\" ," + 
					" from subledgerpaymentheader"+
					" sph,voucherheader  vh ,fund f,fundSource fs where "+
					" sph.voucherheaderid=vh.id  and f.id=vh.fundid and fs.id=vh.fundSourceid and vh.cgn= ?";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, cgn);
					rset=pst.executeQuery();
					rmeta=rset.getMetaData();
					if(rset.next()){
						 for(int i=1;i<=rmeta.getColumnCount();i++){
						 	dc.addValue(rmeta.getColumnName(i),rset.getString(i));
						 }
					}
					
					sql="select a.name as \"subLedgerPaymentHeader_paidBy\",c.glcode as \"billCollector_chequeInHandDesc\",b.glcode as \"billCollector_cashInHandDesc\" from billcollector a,chartofaccounts b,chartofaccounts c where "+
						" a.cashinhand=b.id and a.chequeinhand=c.id and b.id!=c.id and a.id= ?";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, dc.getValue("subLedgerPaymentHeader_paidBy"));
					rset=pst.executeQuery();
					rmeta=rset.getMetaData();
					if(rset.next()){
						 for(int i=1;i<=rmeta.getColumnCount();i++){
						 	dc.addValue(rmeta.getColumnName(i),rset.getString(i));
						 }
					}
					
					dc.addValue("salaryBillDetail_mmonth",mmonth);
					
					sql="select  b.id as \"subLedgerPaymentHeader_bankId\" from bank a ,bankbranch b, bankaccount c  where"+
						" a.id=b.bankid and b.id=c.branchid and c.id= ?";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, dc.getValue("accId"));
					rset=pst.executeQuery();
					rmeta=rset.getMetaData();
					if(rset.next()){
						 for(int i=1;i<=rmeta.getColumnCount();i++){
						 	dc.addValue(rmeta.getColumnName(i),rset.getString(i));
						 }
					}
					dc.addValue("subLedgerPaymentHeader_branchAccountId",dc.getValue("accId"));
					
					sql=" select count(*) from salaryBillDetail s,voucherHeader v, subledgerpaymentheader sph " +
							" where   v.id=s.voucherHeaderId AND "+
							" sph.salarybillid=s.id and sph.voucherheaderid in(select id from voucherheader where cgn= ?)";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, cgn);
					rset=pst.executeQuery();
					if(rset.next()){
						 noOfRec=rset.getInt(1);
					}
					
					if(noOfRec>0){
						String[][] grid=new String[noOfRec+1][7];
						sql="select s.id as \"salaryBillDetail_id\","+
							" v.id as \"voucherHeader_id\",v.voucherNumber as \"voucherHeader_voucherNumber1\","+
							" to_char(v.voucherdate,'dd-mon-yyyy') as \"voucherHeader_voucherDate1\",s.grossPay as \"salaryBillDetail_grossPay\","+
							" s.totalDeductions as \"salaryBillDetail_totalDed\",s.netPay as \"salaryBillDetail_netPay\" "+
							" from salaryBillDetail s,voucherHeader v, subledgerpaymentheader sph where    v.id=s.voucherHeaderId AND"+
							" sph.salarybillid=s.id and sph.voucherheaderid in(select id from voucherheader where cgn= ?) ";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
						pst = con.prepareStatement(sql);
						pst.setString(1, cgn);
						rset=pst.executeQuery();
						rmeta=rset.getMetaData();
					
						for(int i=1;i<=rmeta.getColumnCount();i++){
						 	grid[0][i-1]=rmeta.getColumnName(i);
						}
						int idx=1;//grid[from 1][x] we start filling data
						while(rset.next()){
							for(int i=1;i<=rmeta.getColumnCount();i++){
							 	grid[idx][i-1]=rset.getString(i);
							}
							idx++;
						}
						//rset.close();
						dc.addGrid(gridName,grid);
					}
					sql="select cgn as \"voucherHeader_cgn\",vouchernumber as \"voucherHeader_voucherNumber\",to_char(voucherdate,'dd-mon-yyyy') as \"voucherHeader_voucherDate\","+
					" chequenumber as \"chequeDetail_chequeNumber\" ,to_char(chequedate,'dd-mon-yyyy')  as \"chequeDetail_chequeDate\",vh.description as \"narration\" from voucherheader vh,subledgerpaymentheader sph,chequedetail cq where"+
					" sph.voucherheaderid=vh.id  and cq.id=sph.chequeid"+
					" and chequeid is not null and chequeid>0 and vh.cgn= ?"+
					" union "+
					" select cgn as \"voucherHeader_cgn\",vouchernumber as \"voucherHeader_voucherNumber\",to_char(voucherdate,'dd-mon-yyyy') as \"voucherHeader_voucherDate\",'','',vh.description as \"narration\" from voucherheader vh,subledgerpaymentheader sph  where"+
					" sph.voucherheaderid=vh.id "+
					" and (chequeid is  null or chequeid =0 )and vh.cgn= ?";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, cgn);
					pst.setString(2, cgn);
					rset=pst.executeQuery();
					rmeta=rset.getMetaData();
					String columnName="";
					if(rset.next()){
						 for(int i=1;i<=rmeta.getColumnCount();i++){
						 	columnName=rmeta.getColumnName(i);
						 	if(columnName.equalsIgnoreCase("narration")){
						 		columnName="subLedgerPaymentHeader_"+columnName;
						 	}
						 	dc.addValue(columnName,rset.getString(i));
						 }
					}
					
				}catch(Exception e){
					LOGGER.error("exilError"+e.getMessage());
					throw taskExc;
			}
			finally{
				try{
					rset.close();
					pst.close();
				}catch(Exception e){LOGGER.error("Inside finally");}
			}			}
}
