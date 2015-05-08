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
/**
 * @author Nandini Ramesh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */ 
package com.exilant.eGov.src.reports;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
public class BankTransaction 
{
	Connection con;
	List<Object[]> resultset;
	Query pstmt;
	TaskFailedException taskExc;	
	String endDate, startDate;
    String accNum,transactionType,transactionTypeQry="",transactionTypeQry1="";
    ArrayList bt=new ArrayList();
    java.util.Date dt=new java.util.Date();
	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
   	EGovernCommon egc=new EGovernCommon();
   	CommnFunctions cf=new CommnFunctions();
    private static final Logger LOGGER = Logger.getLogger(BankTransaction.class);
	public BankTransaction(){}  
	
	public ArrayList getbankTranReport(BankTransactionReportBean btBean)throws TaskFailedException
		{
			isCurDate(btBean.getEndDate());
			try
			{
				accNum=btBean.getBankAccount_id();
				transactionType = btBean.getTransactioType();
				if(LOGGER.isInfoEnabled())     LOGGER.info(" vtransactionType "+ transactionType );
				if(transactionType != null && !transactionType.equals("") )
				{
					transactionTypeQry = " and cd.ispaycheque = "+transactionType ;
					
					if(transactionType.trim().equals("0"))
						transactionTypeQry1 = " and br.transactiontype='Dr' ";
					else if(transactionType.trim().equals("1"))
						transactionTypeQry1 = " and br.transactiontype='Cr' ";
				}
									
				if(LOGGER.isInfoEnabled())     LOGGER.info(" transactionTypeQry = "+ transactionTypeQry +",transactionTypeQry1="+transactionTypeQry1);
				dt = sdf.parse(btBean.getStartDate());
				startDate = formatter.format(dt);
				dt = sdf.parse(btBean.getEndDate());
				endDate = formatter.format(dt);			
				getBTReport();
				
			}
			catch(Exception exception)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("EXP="+exception.getMessage(),exception);
			}
		return bt;
	}
	 public void isCurDate(String VDate) throws TaskFailedException{
			
			EGovernCommon egc=new EGovernCommon();
			try{
				String today=egc.getCurrentDate();
				String[] dt2 = today.split("/");
				String[] dt1= VDate.split("/");
				int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
				if(ret==-1 ){
					throw new Exception();
				}
			}catch(Exception ex){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception ",ex);
				throw new TaskFailedException("Date Should be within the today's date");
			}
		}
	 
/**
 * This function executes the Bank Transaction Query,retrieves the values from 
 * the Query into local variables and adds to the bean 
 */	
	 
private void getBTReport() throws Exception 
 {	
	try{
		 String query=" SELECT DISTINCT vh.voucherdate AS \"vDate\", vh.vouchernumber AS \"Voucher No\", TO_CHAR (vh.voucherdate, 'dd/mm/yyyy') AS \"Voucher Date\", "
			 		+" cd.chequenumber AS \"Cheque No\",TO_CHAR (cd.chequedate, 'dd/mm/yyyy') AS \"Cheque Date\",vh.cgn AS \"cgn\",DECODE (ispaycheque, 0, 'Receipt', "
			 		+" 1, 'Payment') AS \"Type\",DECODE (ispaycheque, 0, cd.amount, '') AS \"Receipt Amount\",DECODE (ispaycheque, 1, cd.amount, '') AS " 
			 		+" \"Payment Amount\" ,PAYINSLIPNUMBER as \"Payinslip No\",TO_CHAR (PAYINSLIPDATE,'dd/mm/yyyy') as \"Payinslip Date\"  FROM chequedetail cd, voucherheader vh WHERE cd.voucherheaderid = vh.ID AND vh.status <> 4 and vh.status<>1 AND vh.voucherdate >=  "
			 		+" TO_DATE ('?') AND vh.voucherdate <= TO_DATE ('?') AND cd.isdeposited=1 AND cd.accountnumberid ="+accNum+transactionTypeQry+" UNION " 
			 		+" SELECT DISTINCT vh.voucherdate AS \"vDate\", vh.vouchernumber AS \"Voucher No\", TO_CHAR (vh.voucherdate, 'dd/mm/yyyy') AS \"Voucher Date\", " 
			 		+" '' AS \"Cheque No\", '' AS \"Cheque Date\", vh.cgn AS \"cgn\",DECODE (br.transactiontype,'Dr', 'Receipt','Cr', 'Payment') AS \"Type\", "
			 		+" DECODE (br.transactiontype,'Dr', br.amount,'') AS \"Receipt Amount\",DECODE (br.transactiontype,'Cr', br.amount,'') AS \"Payment Amount\" ,CHEQUENUMBER as \"Payinslip No\",TO_CHAR(CHEQUEDATE,'dd/mm/yyyy') as \"Payinslip Date\" "
			 		+" FROM bankreconciliation br, voucherheader vh WHERE br.voucherheaderid = vh.ID AND vh.status <> 4 and vh.status<>1 AND vh.voucherdate >=  "
			 		+" TO_DATE ('?') AND vh.voucherdate <= TO_DATE ('?') AND br.bankaccountid ="+accNum+transactionTypeQry1+"  AND br.chequenumber IS NULL ORDER BY \"vDate\" " ; 
			 if(LOGGER.isInfoEnabled())     LOGGER.info("*******query  "+query);
		
			 BankTransactionReportBean btBean=null; 
			 pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
			 pstmt.setString(1, startDate);
			 pstmt.setString(2, endDate);
			 pstmt.setString(3, startDate);
			 pstmt.setString(4, endDate);
			 resultset = pstmt.list();
		 	 int i=1;
		 	for(Object[] element : resultset){	 			
		 			btBean=new BankTransactionReportBean();
		 				String vhDate="";
		 				String chqDate="";
		 				String paymentAmt="";
		 				String receiptAmt="";
		 				String vhNo="";
		 				String chqNo="";	
		 				String type="";
		 				String cgn="";		 
		 				String payinslipNo="";
		 				String payinslipDate="";
		 				
		 				if(element[2].toString()!= null)
		 					vhDate=element[2].toString();
			 			else
			 				vhDate="&nbsp;";	 				
		 				
		 				if(element[4].toString()!= null)
		 					chqDate=element[4].toString();
			 			else
			 				chqDate="&nbsp;";	 				
		 				
		 				if(element[8].toString()!=null)
		 					paymentAmt=cf.numberToString(element[8].toString()).toString();
		 				else
		 					paymentAmt="&nbsp;";
		 				if(element[7].toString()!=null)
		 					receiptAmt=cf.numberToString(element[7].toString()).toString();
		 				else
		 					receiptAmt="&nbsp;";		 				
		 				if(element[1].toString()!= null)
		 					vhNo=element[1].toString();
			 			else
			 				vhNo="&nbsp;";
		 				
		 				if(element[3].toString()!= null)
		 					chqNo=element[3].toString();
			 			else
			 				chqNo="&nbsp;";			 				
		 				
		 				type=element[6].toString();
		 				cgn=element[5].toString();
		 				
		 				//If transaction type is Receipt show Payinslip no & date 
		 				if(type.equals("Receipt"))
		 				{
		 				if(element[9].toString()!= null )
		 					payinslipNo=element[9].toString();
			 			else
			 				payinslipNo="&nbsp;";	
		 				if(element[10].toString()!= null)
		 					payinslipDate=element[10].toString();
			 			else
			 				payinslipDate="&nbsp;";	
		 				}
		 				else
		 				{
		 					//If transaction type is payment dont show values in Payinslip no & date columns
		 					payinslipNo="&nbsp;";
		 					payinslipDate="&nbsp;";
		 				}
		 				btBean.setVhNo(vhNo);
		 				btBean.setChqNo(chqNo);
		 				btBean.setType(type);
		 				btBean.setVhDate(vhDate);
		 				btBean.setChqDate(chqDate); 				
		 				btBean.setPaymentAmt(paymentAmt); 				
		 				btBean.setReceiptAmt(receiptAmt);	 			
		 				btBean.setCgn(cgn); 				
		 				btBean.setSerialNo(i+"");
		 				btBean.setPayinslipNo(payinslipNo);
		 				btBean.setPayinslipDate(payinslipDate);
		 				bt.add(btBean);	
		 				i++;
		 			}
			}
			catch(Exception sqlE){
				if(LOGGER.isInfoEnabled())     LOGGER.info("Exception occured in getBTReport() "+ sqlE.getMessage(),sqlE);
			}
		 }	 		
 }
