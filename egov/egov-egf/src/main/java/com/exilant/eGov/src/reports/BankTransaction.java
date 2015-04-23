/**
 * @author Nandini Ramesh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */ 
package com.exilant.eGov.src.reports;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;


import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
public class BankTransaction 
{
	Connection con;
	ResultSet resultset;
	PreparedStatement pstmt;
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
			try
			{
				con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection(); 
			}
			catch(Exception exception)
			{
				LOGGER.error("Could Not Get Connection",exception);
			}
			isCurDate(con,btBean.getEndDate());
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
			finally{
						try{
							//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(con,null);
						}catch(Exception e){
							LOGGER.error(e.getMessage(),e);
						}
			}		
		return bt;
	}
	 public void isCurDate(Connection conn,String VDate) throws TaskFailedException{
			
			EGovernCommon egc=new EGovernCommon();
			try{
				String today=egc.getCurrentDate(conn);
				String[] dt2 = today.split("/");
				String[] dt1= VDate.split("/");
				int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
				if(ret==-1 ){
					throw new Exception();
				}
			}catch(Exception ex){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception ",ex);
				//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(con,null);
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
			 pstmt = con.prepareStatement(query);
			 pstmt.setString(1, startDate);
			 pstmt.setString(2, endDate);
			 pstmt.setString(3, startDate);
			 pstmt.setString(4, endDate);
			 resultset = pstmt.executeQuery();
		 	 int i=1;
		 		while(resultset.next())
		 		{	 			
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
		 				
		 				if(resultset.getString("Voucher Date")!= null)
		 					vhDate=resultset.getString("Voucher Date");
			 			else
			 				vhDate="&nbsp;";	 				
		 				
		 				if(resultset.getString("Cheque Date")!= null)
		 					chqDate=resultset.getString("Cheque Date");
			 			else
			 				chqDate="&nbsp;";	 				
		 				
		 				if(resultset.getString("Payment Amount")!=null)
		 					paymentAmt=cf.numberToString(resultset.getString("Payment Amount")).toString();
		 				else
		 					paymentAmt="&nbsp;";
		 				if(resultset.getString("Receipt Amount")!=null)
		 					receiptAmt=cf.numberToString(resultset.getString("Receipt Amount")).toString();
		 				else
		 					receiptAmt="&nbsp;";		 				
		 				if(resultset.getString("Voucher No")!= null)
		 					vhNo=resultset.getString("Voucher No");
			 			else
			 				vhNo="&nbsp;";
		 				
		 				if(resultset.getString("Cheque No")!= null)
		 					chqNo=resultset.getString("Cheque No");
			 			else
			 				chqNo="&nbsp;";			 				
		 				
		 				type=resultset.getString("Type");
		 				cgn=resultset.getString("cgn");
		 				
		 				//If transaction type is Receipt show Payinslip no & date 
		 				if(type.equals("Receipt"))
		 				{
		 				if(resultset.getString("Payinslip No")!= null )
		 					payinslipNo=resultset.getString("Payinslip No");
			 			else
			 				payinslipNo="&nbsp;";	
		 				if(resultset.getString("Payinslip Date")!= null)
		 					payinslipDate=resultset.getString("Payinslip Date");
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
			catch(SQLException sqlE){
				if(LOGGER.isInfoEnabled())     LOGGER.info("Exception occured in getBTReport() "+ sqlE.getMessage(),sqlE);
			}
		 }	 		
 }