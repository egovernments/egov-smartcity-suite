/*
 * Created on March 28, 2007
 * @author Girish
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

class OpBalance{
	public double dr;
	public double cr;
}
public class CashBook {
	Connection connection;
	Statement statement=null;
	ResultSet resultset=null;
	ResultSet resultset1=null;
	TaskFailedException taskExc;
	String startDate, endDate, effTime,rType="gl";
	NumberFormat numberformatter=new DecimalFormat("##############0.00") ;
	private CommnFunctions commonFun=new CommnFunctions(); 
	private static final Logger LOGGER = Logger.getLogger(CashBook.class);

	
	
	public CashBook(){
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
				connection=con;
				
			}
		});
		
	}

	public LinkedList getGeneralLedgerList(GeneralLedgerReportBean reportBean)throws TaskFailedException{
		LOGGER.debug("Inside getGeneralLedgerList");
		LinkedList dataList = new LinkedList();
		try{
		
		try	{ 
			statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); }
			catch(Exception e){
				LOGGER.info("Exception in creating statement:"+statement);
				throw taskExc;
		}
		String isconfirmed="";
		String glCode1="";
		String glCode2="";
		taskExc = new TaskFailedException();
		EGovernCommon egc=new EGovernCommon();
		String cashPId=EGovConfig.getProperty("egf_config.xml","PURPOSEID","","CashInHand");
		
		String boundryId=reportBean.getBoundary();
		String ulbname=getUlbDetails(connection);
		LOGGER.debug("ulbname:"+ulbname+"boundryId "+boundryId);
		reportBean.setUlbName(ulbname);
		String glcodes[]=getGlcode(connection,boundryId);
		glCode1 = glcodes[0];
		glCode2 = glcodes[1];

		String maxlength=EGovConfig.getProperty("egf_config.xml","glcodeMaxLength","","AccountCode");
		if(glCode1.length()!= Integer.parseInt(maxlength) &&  glCode2.length()!= Integer.parseInt(maxlength) ){
				glCode1= getMinCode(glCode1);
				glCode2= getMaxCode(glCode2);
		}

		try
		{
			String snapShotDateTime=reportBean.getSnapShotDateTime();
			if(snapShotDateTime.equalsIgnoreCase(""))
				effTime="";
			else
				effTime=egc.getEffectiveDateFilter(connection,snapShotDateTime);
		}
		catch(Exception ex){throw taskExc;}
		
		String formstartDate="",formendDate="";
		Date dt=new Date();
		String fundId = reportBean.getFund_id();
		String fundSourceId ="";
		String endDate1=(String)reportBean.getEndDate();
		LOGGER.info(" fundId:"+fundId+" fundSourceId:"+fundSourceId);
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
		isCurDate(connection,endDate1);
		try
   		{
			endDate=(String)reportBean.getEndDate();
			dt = sdf.parse(endDate );
			formendDate = formatter1.format(dt);
		}
   		catch(Exception e){
   			LOGGER.error("inside the try-startdate"+e);
   			throw taskExc;
   		}
   		try
   		{	startDate=(String)reportBean.getStartDate();
			if(!startDate.equalsIgnoreCase("null")){
				dt = sdf.parse(startDate);
			formstartDate = formatter1.format(dt);
			}
   		}
		catch(Exception e){
			LOGGER.error("inside the try-startdate"+e); 
			throw taskExc;
		}

		if(startDate.equalsIgnoreCase("null")){
			String finId=commonFun.getFYID(formendDate,connection);
			startDate=commonFun.getStartDate(connection,Integer.parseInt(finId));
		}
   		else{
   			startDate = formstartDate;
   		}
		//LOGGER.info("startDate22 "+startDate);
		endDate = formendDate;
		String startDateformat = startDate;
		String startDateformat1 = "";
		try{
		dt = formatter1.parse(startDateformat);
		startDateformat1 = sdf.format(dt);
		}
		catch(Exception e){
			LOGGER.error("Parse Exception"+e);
			throw taskExc;
		}
		
		
		setDates(startDate,endDate);
		String fyId = commonFun.getFYID(endDate,connection);
		if(fyId.equalsIgnoreCase("")){
			LOGGER.info("Financial Year Not Valid");
			throw taskExc;
		}
		LOGGER.debug("before query");
		//double txnDrSum=0, txnCrSum=0, closingBalance = 0;
		BigDecimal cashOpeningBalance=new BigDecimal("0.00");
		BigDecimal chequeOpeningBalance=new BigDecimal("0.00");
		
		ReportEngine engine=new ReportEngine();
		ReportEngineBean reBean=engine.populateReportEngineBean(reportBean);
		String engineQry=	engine.getVouchersListQuery(reBean);
		
		String query = getQuery(engineQry);
		LOGGER.debug("**************QUERY: " + query);

		//try{

			try
			{
				statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			}
			catch(Exception e)
			{
				LOGGER.error("Exception in creating statement:"+statement);
				throw taskExc;	
			}
			resultset1 = statement.executeQuery(query);
			String accCode="", vcNum="", vcDate="";
			StringBuffer detail = new StringBuffer();
			StringBuffer amount = new StringBuffer();
			StringBuffer accCodebuffer = new StringBuffer();
			int vhId = 0,VhidPrevious=0;
			BigDecimal currentDebit=new BigDecimal("0.00");
			BigDecimal currentCredit=new BigDecimal("0.00");
			BigDecimal cashdebitTotal=new BigDecimal("0.00");
			BigDecimal chequedebitTotal=new BigDecimal("0.00");
			BigDecimal cashcreditTotal=new BigDecimal("0.00");
			BigDecimal chequecreditTotal=new BigDecimal("0.00");
			String code="",vhType="",funcCode="",bgtCode="",srcOfFinance="",cgn="",currVhDate,purposeid="";
			/**
			 * When using ResultSet.TYPE_SCROLL_INSENSITIVE in createStatement
			 * if no records are there, rs.next() will return true
			 * but when trying to access (rs.getXXX()), it will throw an error
			 **/
				int totalCount=0, isConfirmedCount=0;
				String vn2="";
				OpBalance opbal = getOpeningBalance(glCode1, fundId,fundSourceId, fyId, startDate);
				cashOpeningBalance = BigDecimal.valueOf(opbal.dr - opbal.cr);
				LOGGER.info("cashOpeningBalance:"+cashOpeningBalance);
				opbal = getOpeningBalance(glCode2, fundId,fundSourceId, fyId, startDate);
				chequeOpeningBalance = BigDecimal.valueOf(opbal.dr - opbal.cr);
				/*
				 * left side of the report is receipts so we are assigning (opbal.dr - opbal.cr) to creditTotal
				 * of cash and cheque respectively and displaying opening balance
				 */
				cashcreditTotal=cashOpeningBalance;
				chequecreditTotal=chequeOpeningBalance;
				GeneralLedgerReportBean glbeanOpBal=new GeneralLedgerReportBean();
				glbeanOpBal.setRcptParticulars("<B>To Opening Balance</B>");
				glbeanOpBal.setRcptcashInHandAmt("<B>"+numberToString(cashOpeningBalance.toString())+"</B>");
				glbeanOpBal.setRcptChqInHandAmt("<B>"+numberToString(chequeOpeningBalance.toString())+"</B>");
				dataList.add(glbeanOpBal);
								
				int count2skip1stRow=0;
				while(resultset1.next())
				{
					//LOGGER.info(" inside resultset");
					try{
						
						code= resultset1.getString("code");
						isconfirmed= resultset1.getString("isconfirmed");
						//9 is the dummy value used in the query
						// To display X in Y are unconfirmed
						if(!isconfirmed.equalsIgnoreCase("")&& !isconfirmed.equalsIgnoreCase("9"))
						{
							String vn1=resultset1.getString("vouchernumber");
						 if(!vn1.equalsIgnoreCase(vn2))
						 {
							 vn2=vn1;
							totalCount=totalCount + 1;
							if(isconfirmed.equalsIgnoreCase("0"))
							{
								isConfirmedCount=isConfirmedCount+1;
							}
						 }
						}
						
						vhId = resultset1.getInt("vhid");
						LOGGER.info("check1>>vhId:"+vhId+" VhidPrevious:"+VhidPrevious+" code:"+code+" accCode:"+accCode);
						
						if(vhId!=VhidPrevious)
						{
							LOGGER.info("inside vhId!=VhidPrevious & vhType="+vhType);
							GeneralLedgerReportBean glbean=new GeneralLedgerReportBean("&nbsp;");
						
							if(currentCredit.doubleValue()>0)
							{
								LOGGER.info("inside Receipt>>>>>>"+accCode);
								glbean.setRcptVchrNo(vcNum);
								if(vcDate!=null && !vcDate.equalsIgnoreCase(""))glbean.setRcptVchrDate(vcDate);
								if(bgtCode!=null && !bgtCode.equalsIgnoreCase(""))glbean.setRcptBgtCode(bgtCode);
								if(funcCode!=null && !funcCode.equalsIgnoreCase(""))glbean.setRcptFuncCode(funcCode);
								if(purposeid.equalsIgnoreCase(cashPId))
									{if(amount!=null && !amount.equals("")) glbean.setRcptcashInHandAmt(amount.toString());}
								else
									{if(amount!=null && !amount.equals("")) glbean.setRcptChqInHandAmt(amount.toString());}
								if(detail!=null && !detail.equals("")) glbean.setRcptParticulars(detail.toString());
								if(srcOfFinance!=null && !srcOfFinance.equals("")) glbean.setRcptSrcOfFinance(srcOfFinance); 
								//if(accCode!=null && !accCode.equals("")) glbean.setRcptAccCode(accCode); 
								if(accCodebuffer!=null && !accCodebuffer.equals("")) glbean.setRcptAccCode(accCodebuffer.toString());
								
							}
							else
							{
								LOGGER.info("inside Payment>>>>>>"+accCode);
								glbean.setPmtVchrNo(vcNum);
								if(vcDate!=null && !vcDate.equalsIgnoreCase(""))glbean.setpmtVchrDate(vcDate);
								if(bgtCode!=null && !bgtCode.equalsIgnoreCase(""))glbean.setPmtBgtCode(bgtCode); 
								if(funcCode!=null && !funcCode.equalsIgnoreCase(""))glbean.setPmtFuncCode(funcCode);
								if(purposeid.equalsIgnoreCase(cashPId))
									{if(amount!=null && !amount.equals("")) glbean.setPmtCashInHandAmt(amount.toString());}
								else
									{if(amount!=null && !amount.equals("")) glbean.setPmtChqInHandAmt(amount.toString());}
								if(detail!=null && !detail.equals("")) glbean.setPmtParticulars(detail.toString());
								if(srcOfFinance!=null && !srcOfFinance.equals("")) glbean.setPmtSrcOfFinance(srcOfFinance);
								//if(accCode!=null && !accCode.equals("")) glbean.setPmtAccCode(accCode); 
								if(accCodebuffer!=null && !accCodebuffer.equals("")) glbean.setPmtAccCode(accCodebuffer.toString());
								
							}
							LOGGER.info("cgn before adding: "+cgn);
							glbean.setCGN(cgn);
							reportBean.setStartDate(startDateformat1);
							reportBean.setTotalCount(Integer.toString(totalCount));
							reportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));
							if(count2skip1stRow!=0) dataList.add(glbean);//skip to insert blank row at the top
							count2skip1stRow++;
							currVhDate= resultset1.getString("voucherdate");
							LOGGER.info("vcDate:"+vcDate+" currVhDate:"+currVhDate);
							if(!vcDate.equalsIgnoreCase(currVhDate) && !vcDate.equalsIgnoreCase("")  )
							{
								
								GeneralLedgerReportBean glbeanCb=new GeneralLedgerReportBean("&nbsp;");
								glbeanCb.setPmtParticulars("<B>Closing: By balance c/d</B>");
								glbeanCb.setPmtCashInHandAmt("<B>"+(numberToString(cashcreditTotal.subtract(cashdebitTotal).toString()))+"</B>");
								glbeanCb.setPmtChqInHandAmt("<B>"+(numberToString(chequecreditTotal.subtract(chequedebitTotal).toString()))+"</B>");
								dataList.add(glbeanCb);
								GeneralLedgerReportBean glbean1=new GeneralLedgerReportBean("<hr>&nbsp;</hr>");
								glbean1.setRcptVchrDate("<hr><B>Total</B></hr>");
								glbean1.setRcptcashInHandAmt("<hr><B>"+numberToString(cashcreditTotal.toString())+"</B></hr>");
								glbean1.setPmtCashInHandAmt("<hr><B>"+numberToString(cashdebitTotal.add(cashcreditTotal.subtract(cashdebitTotal)).toString())+"</B></hr>");
								glbean1.setRcptChqInHandAmt("<hr><B>"+numberToString(chequecreditTotal.toString())+"</B></hr>");
								glbean1.setPmtChqInHandAmt("<hr><B>"+numberToString(chequedebitTotal.add (chequecreditTotal.subtract(chequedebitTotal)).toString())+"</B></hr>");
								dataList.add(glbean1);
								LOGGER.info(cashcreditTotal+":crDr: "+cashdebitTotal);
								GeneralLedgerReportBean glbeanOb=new GeneralLedgerReportBean("<hr>&nbsp;</hr>");
								glbeanOb.setRcptParticulars("<hr><B>Opening: To balance b/d</B></hr>");
								//glbeanOb.setRcptcashInHandAmt("<hr><B>"+(numberformatter.format(cashcreditTotal.subtract(cashdebitTotal).doubleValue()))+"</B></hr>");
								//glbeanOb.setRcptChqInHandAmt("<hr><B>"+(numberformatter.format(chequecreditTotal.subtract(chequedebitTotal).doubleValue()))+"</B></hr>");
								glbeanOb.setRcptcashInHandAmt("<hr><B>"+(numberToString(cashcreditTotal.subtract(cashdebitTotal).toString()))+"</B></hr>");
								glbeanOb.setRcptChqInHandAmt("<hr><B>"+(numberToString(chequecreditTotal.subtract(chequedebitTotal).toString()))+"</B></hr>");
								dataList.add(glbeanOb);
								cashcreditTotal=cashcreditTotal.subtract(cashdebitTotal);
								chequecreditTotal=chequecreditTotal.subtract(chequedebitTotal);
								cashdebitTotal=new BigDecimal("0.00");
								chequedebitTotal=new BigDecimal("0.00");
							}
							vcNum=vcDate=bgtCode=funcCode=srcOfFinance=accCode=cgn=vhType="";
							amount.delete(0,amount.length());
							detail.delete(0,detail.length());
							accCodebuffer.delete(0,accCodebuffer.length());
						}
												
						accCode = resultset1.getString("glcode");
						LOGGER.info("check2>>vhId:"+vhId+" VhidPrevious:"+VhidPrevious+" code:"+code+" accCode:"+accCode);
						if(vhId==VhidPrevious && !code.equalsIgnoreCase(accCode))
						{
							LOGGER.info("inside vhId==VhidPrevious ");
							vhType=resultset1.getString("vhType");
						//	vhName=resultset1.getString("vhname");
							String bLine="<Br>";
							currentDebit=new BigDecimal("0.00"); 
							currentCredit=new BigDecimal("0.00");
							cgn=resultset1.getString("CGN");
							vcDate = resultset1.getString("voucherdate");
							vcNum = resultset1.getString("vouchernumber");
							funcCode=resultset1.getString("function");
							//bgtCode=resultset1.getString("BGCODE");
							srcOfFinance=resultset1.getString("fundsource");
							String name[]=resultset1.getString("name").split(" ");
							int wordLength=0;
							String formatedName="";
							//String formatedAccCode="";
							for(int i=0;i<name.length;i++)
							{
								wordLength=name[i].length();
								if((formatedName.length()-formatedName.lastIndexOf("<Br>")+wordLength)<25) formatedName=formatedName+" "+name[i];
								else
									{ formatedName=formatedName.concat("<Br>"+name[i]);
									  bLine=bLine.concat("<Br>");
									} 
							}		
							detail = detail.append(" " + formatedName + "<br>");
							accCodebuffer=accCodebuffer.append(" "+accCode+bLine);
							currentDebit=resultset1.getBigDecimal("debitamount");
							currentCredit=resultset1.getBigDecimal("creditamount");	
							LOGGER.info("currentCredit:"+currentCredit+" currentDebit:"+currentDebit+" chequedebitTotal:"+chequedebitTotal+"chequecreditTotal:"+chequecreditTotal);
							LOGGER.info(" BEFORE>>>>cashdebitTotal:"+cashdebitTotal+"cashcreditTotal:"+cashcreditTotal);
							if(currentDebit.doubleValue()>0)
							{
									LOGGER.info("if purposeid:"+purposeid+">>cashPId:"+cashPId);
									//amount=amount.append(" " + numberformatter.format(currentDebit.doubleValue()) + bLine);
									amount=amount.append(" " + numberToString(currentDebit.toString()) + bLine);
									if(purposeid.equalsIgnoreCase(cashPId))
										cashdebitTotal=cashdebitTotal.add(currentDebit);
									else
										chequedebitTotal=chequedebitTotal.add(currentDebit);
							}
							else
							{
								LOGGER.info("else purposeid:"+purposeid+">>cashPId:"+cashPId);
								//amount=amount.append(" " + numberformatter.format(currentCredit.doubleValue()) + bLine);
								amount=amount.append(" " + numberToString(currentCredit.toString()) + bLine);
								if(purposeid.equalsIgnoreCase(cashPId))
										cashcreditTotal=cashcreditTotal.add(currentCredit);
								else
										chequecreditTotal=chequecreditTotal.add(currentCredit);
							}
							LOGGER.info("after adding currentCredit:"+currentCredit+" currentDebit:"+currentDebit+" chequedebitTotal:"+chequedebitTotal+"chequecreditTotal:"+chequecreditTotal);
							LOGGER.info(" AFTER>>>>cashdebitTotal:"+cashdebitTotal+"cashcreditTotal:"+cashcreditTotal);
							cgn=resultset1.getString("CGN");
							//LOGGER.info("cgn: "+cgn);
												
						}
						else
							purposeid=resultset1.getString("purposeid");
						VhidPrevious=vhId;
						if(resultset1.isLast())
						{
							GeneralLedgerReportBean glbean=new GeneralLedgerReportBean("&nbsp;");
							
							if(currentCredit.doubleValue()>0)
							{
								LOGGER.info("inside Receipt>>>>>>"+accCode);
								glbean.setRcptVchrNo(vcNum);
								if(vcDate!=null && !vcDate.equalsIgnoreCase(""))glbean.setRcptVchrDate(vcDate);
								if(bgtCode!=null && !bgtCode.equalsIgnoreCase(""))glbean.setRcptBgtCode(bgtCode); 
								if(funcCode!=null && !funcCode.equalsIgnoreCase(""))glbean.setRcptFuncCode(funcCode); 
								if(purposeid.equalsIgnoreCase(cashPId))
									{if(amount!=null && !amount.equals("")) glbean.setRcptcashInHandAmt(amount.toString());}
								else
									{if(amount!=null && !amount.equals("")) glbean.setRcptChqInHandAmt(amount.toString());}
								if(detail!=null && !detail.equals("")) glbean.setRcptParticulars(detail.toString()); 
								if(srcOfFinance!=null && !srcOfFinance.equals("")) glbean.setRcptSrcOfFinance(srcOfFinance);
								if(accCodebuffer!=null && !accCodebuffer.equals("")) glbean.setRcptAccCode(accCodebuffer.toString());
								//if(accCode!=null && !accCode.equals("")) glbean.setRcptAccCode(accCode); 
								
							}
							else
							{
								LOGGER.info("inside Payment>>>>>>"+accCode);
								glbean.setPmtVchrNo(vcNum);
								if(vcDate!=null && !vcDate.equalsIgnoreCase(""))glbean.setpmtVchrDate(vcDate);
								if(bgtCode!=null && !bgtCode.equalsIgnoreCase(""))glbean.setPmtBgtCode(bgtCode);
								if(funcCode!=null && !funcCode.equalsIgnoreCase(""))glbean.setPmtFuncCode(funcCode); 
								if(purposeid.equalsIgnoreCase(cashPId))
									{if(amount!=null && !amount.equals("")) glbean.setPmtCashInHandAmt(amount.toString());}
								else
									{if(amount!=null && !amount.equals("")) glbean.setPmtChqInHandAmt(amount.toString());}
								if(detail!=null && !detail.equals("")) glbean.setPmtParticulars(detail.toString());
								if(srcOfFinance!=null && !srcOfFinance.equals("")) glbean.setPmtSrcOfFinance(srcOfFinance);
								//if(accCode!=null && !accCode.equals("")) glbean.setPmtAccCode(accCode);
								if(accCodebuffer!=null && !accCodebuffer.equals("")) glbean.setPmtAccCode(accCodebuffer.toString());
								
							}
							glbean.setCGN(cgn);
							reportBean.setStartDate(startDateformat1);
							reportBean.setTotalCount(Integer.toString(totalCount));
							reportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));
							dataList.add(glbean);
							currVhDate= resultset1.getString("voucherdate");
							{
								GeneralLedgerReportBean glbeanCb=new GeneralLedgerReportBean("&nbsp;");
								glbeanCb.setPmtParticulars("<B>Closing: By balance c/d</B>");
								glbeanCb.setPmtCashInHandAmt("<B>"+(numberToString(cashcreditTotal.subtract(cashdebitTotal).toString()))+"</B>");
								glbeanCb.setPmtChqInHandAmt("<B>"+(numberToString(chequecreditTotal.subtract(chequedebitTotal).toString()))+"</B>");
								dataList.add(glbeanCb);
								
								GeneralLedgerReportBean glbean1=new GeneralLedgerReportBean("<hr>&nbsp;</hr>");
								glbean1.setRcptVchrDate("<hr><B>Total</B></hr>");
								glbean1.setRcptcashInHandAmt("<hr><B>"+numberToString(cashcreditTotal.toString())+"</B></hr>");
								glbean1.setPmtCashInHandAmt("<hr><B>"+numberToString(cashdebitTotal.add(cashcreditTotal.subtract(cashdebitTotal)).toString())+"</B></hr>");
								glbean1.setRcptChqInHandAmt("<hr><B>"+numberToString(chequecreditTotal.toString())+"</B></hr>");
								glbean1.setPmtChqInHandAmt("<hr><B>"+numberToString(chequedebitTotal.add (chequecreditTotal.subtract(chequedebitTotal)).toString())+"</B></hr>");
								dataList.add(glbean1);
								GeneralLedgerReportBean glbeanOb=new GeneralLedgerReportBean("<hr>&nbsp;</hr>");
								glbeanOb.setRcptParticulars("<hr><B>Opening: To balance b/d</B></hr>");
								glbeanOb.setRcptcashInHandAmt("<hr><B>"+(numberToString(cashcreditTotal.subtract(cashdebitTotal).toString()))+"</B></hr>");
								glbeanOb.setRcptcashInHandAmt("<hr><B>"+(numberToString(cashcreditTotal.subtract(cashdebitTotal).toString()))+"</B></hr>");
								glbeanOb.setRcptChqInHandAmt("<hr><B>"+(numberToString(chequecreditTotal.subtract(chequedebitTotal).toString()))+"</B></hr>");
								glbeanOb.setRcptChqInHandAmt("<hr><B>"+(numberToString(chequecreditTotal.subtract(chequedebitTotal).toString()))+"</B></hr>");
								dataList.add(glbeanOb);
							}
						}
					}catch(Exception e)
					{
						LOGGER.error("error in resultset processing"+e.getMessage());
						throw taskExc;
					}								
					
				}//End While



		}catch(SQLException ex){
			LOGGER.error("ERROR in  getGeneralLedgerList " + ex.toString());
			throw taskExc;
		}
		LOGGER.info("returning list");
		return dataList;
	}

	private String getQuery(String engineQry)
	{
		return	"SELECT distinct gl1.glcode as \"code\",vh.type as \"vhType\",vh.cgn as \"CGN\",coa.purposeid as \"purposeid\",decode(coa.purposeid,4,1,5,1,0) as \"order\"," +
					"(select ca.type from chartofaccounts ca where glcode=gl1.glcode) as \"glType\"," +
					" (select name from fundsource where id=vh.FUNDSOURCEID) as \"fundsource\",(select name from function where id=gl.FUNCTIONID) as \"function\"," +
					" vh.id AS \"vhid\", vh.voucherDate AS \"vDate\", " +
					"to_char(vh.voucherDate, 'dd-Mon-yyyy') AS \"voucherdate\", " +
					"vh.voucherNumber AS \"vouchernumber\", gl.glCode AS \"glcode\", " +
					"coa.name||decode(vh.STATUS,1,'(Reversed)',2,'(Reversal)') AS \"name\",decode(gl.debitAmount,0,(case (gl.creditamount) when 0 then gl.creditAmount||'.00cr' when floor(gl.creditamount)    then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end ) , (case (gl.debitamount) when 0 then gl.debitamount||'.00dr' when floor(gl.debitamount)    then gl.debitamount||'.00dr' else  gl.debitamount||'dr' 	 end )) AS \"amount\", " +
					"gl.description AS \"narration\", vh.name AS \"vhname\", " +
					"gl.debitamount  AS \"debitamount\", gl.creditamount  AS \"creditamount\",f.name as \"fundName\",  vh.isconfirmed as \"isconfirmed\"  " +
					"FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa, generalLedger gl1, fund f " +
					"WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id AND gl.voucherHeaderId = vh.id " +
					" AND gl.voucherHeaderId = gl1.voucherHeaderId AND f.id=vh.fundId " + effTime +
					" AND gl1.glcode in (SELECT GLCODE FROM CHARTOFACCOUNTS WHERE PURPOSEID=4 or purposeid=5) " + 
					" AND vh.id in ("+engineQry+" )" +
					" AND (gl.debitamount>0 OR gl.creditamount>0)  " +
					" order by \"vDate\",\"vhid\",\"order\" desc ";
			}

	private OpBalance getOpeningBalance(String glCode,String fundId,String fundSourceId,String fyId,String tillDate)
	throws TaskFailedException,SQLException
	{
		String fundCondition="";
		String fundSourceCondition="";
		double opDebit=0, opCredit=0;

		/** opening balance of the Year **/
		if(!fundId.equalsIgnoreCase("")) fundCondition="fundId = " + fundId + " AND ";
		if(!fundSourceId.equalsIgnoreCase("")) fundSourceCondition="fundSourceId = " + fundSourceId + " AND ";
		String queryYearOpBal = "SELECT decode(sum(openingDebitBalance),null,0,sum(openingDebitBalance)) AS \"openingDebitBalance\", " +
								"decode(sum(openingCreditBalance),null,0,sum(openingCreditBalance)) AS \"openingCreditBalance\" " +
								"FROM transactionSummary WHERE "+fundCondition + fundSourceCondition +" financialYearId="+fyId+" " +
								"AND glCodeId = (SELECT id FROM chartOfAccounts WHERE glCode in('"+ glCode +"'))";
		LOGGER.debug("***********: OPBAL for glcode -->" +glCode+ " is-->: " + queryYearOpBal);

		statement=connection.createStatement();
		resultset = null;
		resultset = statement.executeQuery(queryYearOpBal);

		if(resultset.next()){
			opDebit = resultset.getDouble("openingDebitBalance");
			opCredit = resultset.getDouble("openingCreditBalance");
		}
		statement.close();

		/** opening balance till the date from the start of the Year **/
		if(rType.equalsIgnoreCase("gl"))
		{
			String startDate=commonFun.getStartDate(connection,Integer.parseInt(fyId));
			if(!fundId.equalsIgnoreCase("")) fundCondition="AND vh.fundId = " + fundId + " ";
			if(!fundSourceId.equalsIgnoreCase("")) fundSourceCondition="AND vh.fundId = " + fundSourceId + " ";
			String queryTillDateOpBal="";
			//if(showRev.equalsIgnoreCase("on")){

			queryTillDateOpBal = "SELECT decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) AS \"debitAmount\", " +
			"decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount)) AS \"creditAmount\" " +
			"FROM generalLedger gl, voucherHeader vh " +
			"WHERE vh.id = gl.voucherHeaderId " +
			"AND gl.glCode in('"+ glCode +"') " + fundCondition + fundSourceCondition + effTime +
			"AND vh.voucherDate >= '"+startDate+"' AND vh.voucherDate < '"+tillDate+"' AND vh.status<>4";

			LOGGER.info("***********: tilldate OPBAL for glcode -->" +glCode+ " is-->: " + queryTillDateOpBal);

		    statement=connection.createStatement();
			resultset = null;
			resultset = statement.executeQuery(queryTillDateOpBal);
			if(resultset.next()){
				opDebit = opDebit + resultset.getDouble("debitAmount");
				opCredit = opCredit + resultset.getDouble("creditAmount");
			}
			statement.close();
		}
		OpBalance opBal = new OpBalance();
		opBal.dr = opDebit;
		opBal.cr = opCredit;
		LOGGER.info("opBal.dr:"+opBal.dr+" opBal.cr:"+opBal.cr);
		resultset = null;
		return opBal;
	}

	private void setDates(String startDate, String endDate ) throws TaskFailedException{
		ResultSet rs = null;
		ResultSet rs1 = null;
		Statement st=null;
		String formstartDate="";
		String formendDate="";
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
		String isclosed="";
		try
   		{
			st = connection.createStatement();
			String query = "select id as \"id\",isclosed as \"isclosed\" from financialYear where startingDate <= '"+startDate+"' AND endingDate >= '"+endDate+"'";
			rs = st.executeQuery(query);
			if(!rs.next())
				throw new TaskFailedException();
			else
				{isclosed=rs.getString("isclosed");
				if(isclosed.equals("1")) throw new TaskFailedException();
				}
		}
		catch(Exception  ex)
		{
			//dc.addMessage("eGovFailure","Dates are not within financial year");
			if(isclosed.equals("1")) 
				throw new TaskFailedException("Choosen Financial year is closed");
			else			
				throw new TaskFailedException("Dates are not within same financial year or This financial year does not Exist");
		}
		finally
		{
			try{
				rs.close();
				st.close();
				st=null;
			}
			catch(Exception e)
			{
				LOGGER.error("Exp in finally:"+e.getMessage());
			}
		}
   		try{
   		formstartDate = sdf.format(formatter1.parse(startDate));
		formendDate = sdf.format(formatter1.parse(endDate ));
   		}
   		catch(Exception e){throw taskExc;}
   		startDate = formstartDate;
		endDate = formendDate;
		if((startDate == null || startDate.equalsIgnoreCase("")) && (endDate == null || endDate.equalsIgnoreCase("")))
		{
		   try{
				statement = connection.createStatement();
				String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" " +
								"FROM financialYear WHERE startingDate <= SYSDATE AND endingDate >= SYSDATE";
				rs = statement.executeQuery(query);
				if(rs.next()) startDate = rs.getString("startingDate");

				rs.close();
				String query1 = "SELECT TO_CHAR(sysdate, 'dd-Mon-yyyy') AS \"endingDate\" FROM dual";
				rs1 = statement.executeQuery(query1);
				if(rs1.next()) endDate = rs1.getString("endingDate");
				rs1.close();
				statement.close();
			}
			catch(SQLException  ex)
			{//dc.addMessage("eGovFailure","setDates");
			throw new TaskFailedException();}
		}
		if((startDate == null || startDate.equalsIgnoreCase("")) && (endDate != null && !endDate.equalsIgnoreCase("")))
		{
			try{
				statement = connection.createStatement();
				String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE startingDate <= '"+endDate+"' AND endingDate >= '"+endDate+"'";
				rs = statement.executeQuery(query);
				if(rs.next()) startDate = rs.getString("startingDate");
				rs.close();
				statement.close();
				}
				catch(SQLException  ex)
				{
					throw taskExc;
				}
		}

		if((endDate == null || endDate.equalsIgnoreCase("")) && (startDate != null && !startDate.equalsIgnoreCase("")))
		{
			try{
				statement = connection.createStatement();
				String query = "SELECT TO_CHAR(endingDate, 'dd-Mon-yyyy') AS \"endingDate\" " +
				"FROM financialYear WHERE startingDate <= '"+startDate+"' AND endingDate >= '"+startDate+"'";
				rs = statement.executeQuery(query);
			//	if(rs.next()) endDate = rs.getString("endingDate");
				rs.close();
				statement.close();
				statement = null;
			}
			catch(Exception  ex)
			{
				throw taskExc;
			}
		}
	}

	public String getULBName(){
		String ulbName="";

		try{
			 statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("select name from companydetail");
			rset.next();
			ulbName = rset.getString(1);
			rset.close();
			statement.close();
		}catch(Exception sqlex){
			return null;
		}
		return ulbName;
	}
	public String getMinCode(String minGlCode)throws TaskFailedException
	{//LOGGER.info("coming");
		String minCode = "";
		try{
			 statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("select glcode from chartofaccounts where glcode like '"+minGlCode+"'|| '%' and classification = 4 order by glcode asc");
			rset.next();
			minCode = rset.getString(1);
			rset.close();
			statement.close();
		}catch(Exception sqlex){
			LOGGER.error("Exception while getting minGlCode"+sqlex);
			throw taskExc;
		}
		return minCode;
	}
	public String getMaxCode(String maxGlCode)throws TaskFailedException
	{
		String maxCode = "";
		try{
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("  select glcode from chartofaccounts where glcode like '"+maxGlCode+"'|| '%' and classification = 4 order by glcode desc");
			rset.next();
			maxCode = rset.getString(1);
			rset.close();
			statement.close();
		}catch(Exception sqlex){
			LOGGER.error("Exception while getting maxGlCode"+sqlex);
			throw taskExc;
		}
		return maxCode;
	}
	public String getCGN(final String id) throws TaskFailedException
	{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
		String cgn="";
		statement=null;
		ResultSet rsCgn=null;
		if(!id.equals(""))
		{
			try
			{
				statement = connection.createStatement();
			    String queryCgn="select CGN from VOUCHERHEADER where id='"+id+"'";
			    rsCgn = statement.executeQuery(queryCgn);
			    rsCgn.next();
			    cgn = rsCgn.getString("CGN");

			}
			catch(Exception sqlex)
			{
				LOGGER.error("cgnCatch#"+sqlex);
				try {
					throw taskExc;
				} catch (TaskFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			finally
			{
				try
				{
					rsCgn.close();
					statement.close();
				}
				catch(Exception e){LOGGER.error("Exp in finally"+e.getMessage());}
			}
		}

		return cgn;
			} 
			});  
	}
	 public void isCurDate(Connection conn,String VDate) throws TaskFailedException
     {

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
				LOGGER.error("Exception in isCurDate():"+ex);
				throw new TaskFailedException("Date Should be within the today's date");
			}

		}
	 private String[] getGlcode(Connection con,String bId)throws TaskFailedException{
	 	String glcode[]=new String[2];
	 	ResultSet rs=null;
	 	try{
	 		Statement st=con.createStatement();
	 		String query="select glcode as \"glcode\" from chartofaccounts where id in (select cashinhand from codemapping where eg_boundaryid="+bId+")";
	 		LOGGER.info(query);
	 		rs=st.executeQuery(query);
	 		if(rs.next())glcode[0]=rs.getString("glcode");
	 		rs=st.executeQuery("select glcode from chartofaccounts where id in (select chequeinHand from codemapping where eg_boundaryid="+bId+")");
	 		if(rs.next())glcode[1]=rs.getString("glcode");
	 		st.close();
	 	}catch(SQLException e){throw taskExc;}	 	
	 	return glcode;
	 }
	 private String getUlbDetails(Connection con)throws TaskFailedException{
	 	
	 	ResultSet rs=null;
	 	String ulbName="";
	 	try{
	 		Statement st=con.createStatement();
	 		String query="select name as \"name\" from companydetail";
	 		LOGGER.info(query);
	 		rs=st.executeQuery(query);
	 		if(rs.next())ulbName=rs.getString("name");
	 		st.close();
	 	}catch(SQLException e){throw taskExc;}	 	
	 	return ulbName;
	 }
	
	 public static StringBuffer numberToString(final String strNumberToConvert)
     {
         String strNumber="",signBit="";
         if(strNumberToConvert.startsWith("-"))
         {
         	strNumber=""+strNumberToConvert.substring(1,strNumberToConvert.length());
         	signBit="-";
         }
         else strNumber=""+strNumberToConvert;
         DecimalFormat dft = new DecimalFormat("##############0.00");
         String strtemp=""+dft.format(Double.parseDouble(strNumber));            
         StringBuffer strbNumber=new StringBuffer(strtemp);                       
         int intLen=strbNumber.length();
            
         for(int i=intLen-6;i>0;i=i-2)                      
         {            
             strbNumber.insert(i,',');               
         }                
        if(signBit.equals("-"))strbNumber=strbNumber.insert(0,"-");
         return strbNumber;
     }
	 
}
