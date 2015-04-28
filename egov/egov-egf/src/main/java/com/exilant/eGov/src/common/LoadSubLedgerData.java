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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.apache.log4j.Logger;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoadSubLedgerData extends AbstractTask{
	private final static Logger LOGGER=Logger.getLogger(LoadSubLedgerData.class);
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
					String relationType="";
					String relationTypeID="";
					String relationBillTable="";String relationBillID="";
					String chequeId="";
					String sql="select sph.type,sph.chequeid from subledgerpaymentheader sph,voucherheader  vh  where "+
					" sph.voucherheaderid=vh.id and vh.cgn= ?";
					pst =con.prepareStatement(sql);
					pst.setString(1, cgn);
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					rset=pst.executeQuery();
					ResultSetMetaData rmeta=rset.getMetaData();
					if(rset.next()){
						 relationType=rset.getString(1);
						 chequeId=rset.getString(2);
					}
					rset.close();
					dc.addValue("pay_hide",relationType);
					if(chequeId==null || chequeId.equals("0")){
						dc.addValue("subLedgerPaymentHeader_typeOfPayment","Cash");
					}else{
						dc.addValue("subLedgerPaymentHeader_typeOfPayment","Cheque");
					}
					relationTypeID=relationType+"id";
					relationBillTable=relationType+"billdetail";
					relationBillID=relationType+"billid";
					sql="select sph.type as \"pay_type\","+relationTypeID+" as \"payToid\","+
					" paidby as \"paidByid\",bankaccountid as \"accId\",worksdetailid as \"worksDetailid\", " +
					"f.name as \"fund_name\",f.id as \"fund_id\",fsrc.name as \"fundSource_id\",fsrc.name as \"fundSource_name\" " +
					" from subledgerpaymentheader"+
					" sph,voucherheader  vh ,fund f ,fundsource fsrc where "+
					" sph.voucherheaderid=vh.id  and f.id=vh.fundid and fsrc.id=vh.fundsourceid" +
					" and vh.cgn= ?";
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
					//rset.close();
					//billcollector
					sql="select a.name as \"paidBy\",b.glcode as \"billCollector_cashInHandDesc\" from billcollector a,chartofaccounts b where "+
						" a.cashinhand=b.id and a.id= ?";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, dc.getValue("paidByid"));
					rset=pst.executeQuery();
					rmeta=rset.getMetaData();
					if(rset.next()){
						 for(int i=1;i<=rmeta.getColumnCount();i++){
						 	dc.addValue(rmeta.getColumnName(i),rset.getString(i));
						 }
					}
					//rset.close();
					//supplier/contractor name
					sql="select name  as \"payTo\" from relation where id= ?";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, dc.getValue("payToid"));
					rset=pst.executeQuery();
					rmeta=rset.getMetaData();
					if(rset.next()){
						 for(int i=1;i<=rmeta.getColumnCount();i++){
						 	dc.addValue(rmeta.getColumnName(i),rset.getString(i));
						 }
					}
					//rset.close();
					//	workorder
					sql="select name  as \"worksDetail_id\" ,advanceamount as \"worksDetail_advanceAmount\" from worksDetail where id= ?";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, dc.getValue("worksDetailid"));
					rset=pst.executeQuery();
					rmeta=rset.getMetaData();
					if(rset.next()){
						 for(int i=1;i<=rmeta.getColumnCount();i++){
						 	dc.addValue(rmeta.getColumnName(i),rset.getString(i));
						 }
					}
					//rset.close();
					//bank name
					sql="select a.name||' '||b.branchname as \"subLedgerPaymentHeader_bankId\" from bank a ,bankbranch b, bankaccount c  where"+
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
					//rset.close();
					//acount number
					sql="select accountnumber as \"branchAccountId\" from bankaccount where id= ?";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, dc.getValue("accId"));
					rset=pst.executeQuery();
					rmeta=rset.getMetaData();
					if(rset.next()){
						 	dc.addValue("subLedgerPaymentHeader_branchAccountId",rset.getString(1));
					}
					//rset.close();
					sql="select count(*)"+
					" from "+relationBillTable+" a ,"+
					" voucherheader b ,subledgerpaymentheader sph  "+
					" where b.id=a.voucherheaderid  and "+
					" sph."+relationBillID+"=a.id and "+
					" sph.voucherheaderid =(select id from voucherheader where cgn= ?)"+
					" and passedamount>(a.paidamount+tdsamount+advadjamt)-sph.paidamount "+
					" and a."+relationTypeID+"= ? and b.fundid="+
					" and a.worksdetailid= ?"+" order by a.billDate";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
					pst = con.prepareStatement(sql);
					pst.setString(1, cgn);
					pst.setString(2, dc.getValue("payToid"));
					pst.setString(3, dc.getValue("fund_id"));
					pst.setString(4, dc.getValue("worksDetailid"));
					rset=pst.executeQuery();
					if(rset.next()){
						 noOfRec=rset.getInt(1);
					}
					//rset.close();
					if(noOfRec>0){
						String[][] grid=new String[noOfRec+1][13];
						sql="select a.id as \"billNoId\",billNumber as\"billNo\",vouchernumber as \"d_voucherNo\" ,"+
						"to_char(billdate,'dd-Mon-yyyy') as \"billDate\",a.PassedAmount as \"passedAmount\","+
						" advadjamt as \"advance\",TDSamount as \"tds\",OtherRecoveries as \"otherRecoveries\","+
						" a.passedAmount-(advadjamt+tdsamount+otherrecoveries) as \"net\","+
						" a.PaidAmount-sph.paidamount as \"earlierPayment\" ,"+
						" sph.paidamount as \"slph_paidAmount\","+//((a.passedAmount-(advadjamt+tdsamount+otherrecoveries))-a.paidamount)
						" rownum as \"slNo\" ,'1' as \"billSelect\" from "+relationBillTable+" a ,"+
						" voucherheader b ,subledgerpaymentheader sph  "+
						" where b.id=a.voucherheaderid  and "+
						" sph."+relationBillID+"=a.id and "+
						" sph.voucherheaderid =(select id from voucherheader where cgn= ?)"+
						" and passedamount>(a.paidamount+tdsamount+advadjamt)-sph.paidamount "+
						" and a."+relationTypeID+"= ? and b.fundid= ?"+
						" and a.worksdetailid= ? order by a.billDate";
if(LOGGER.isDebugEnabled())     LOGGER.debug(sql);
						pst = con.prepareStatement(sql);
						pst.setString(1, cgn);
						pst.setString(2, dc.getValue("payToid"));
						pst.setString(3, dc.getValue("fund_id"));
						pst.setString(4, dc.getValue("worksDetailid"));
						rset=pst.executeQuery();
						rmeta=rset.getMetaData();
						//grid[0][x] we filled control name
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
						rset.close();
						dc.addGrid(gridName,grid);
					}
					sql="select cgn as \"voucherHeader_cgn\",vouchernumber as \"voucherHeader_voucherNumber\",to_char(voucherdate,'dd-Mon-yyyy') as \"voucherHeader_voucherDate\","+
					" chequenumber as \"chequeDetail_chequeNumber\" ,to_char(chequedate,'dd-Mon-yyyy')  as \"chequeDetail_chequeDate\",vh.description as \"narration\",vh.fundsourceid as \"fundsource_id\" from voucherheader vh,subledgerpaymentheader sph,chequedetail cq where"+
					" sph.voucherheaderid=vh.id  and cq.id=sph.chequeid"+
					" and chequeid >0 and chequeid is not null  and vh.cgn= ?"+
					" union "+
					" select cgn as \"voucherHeader_cgn\",vouchernumber as \"voucherHeader_voucherNumber\",to_char(voucherdate,'dd-Mon-yyyy') as \"voucherHeader_voucherDate\",'','',vh.description as \"narration\",vh.fundsourceid as \"fundsource_id\" from voucherheader vh,subledgerpaymentheader sph  where"+
					" sph.voucherheaderid=vh.id "+
					" and (chequeid is  null or chequeid=0) and vh.cgn= ?";
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
					//rset.close();
					//st.close();
				}catch(Exception e){
					LOGGER.error("Error in executing query");
					throw taskExc;
			}
			finally{
				try{
					rset.close();
					pst.close();
				}catch(Exception e){LOGGER.error("Inside finally");}
			}
			}
}
