/*
 * Created on Aug 27, 2008
 * @author Iliyaraja S
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

import com.exilant.exility.common.TaskFailedException;

public class SummaryStatement
{
	Statement statement;
	Statement st2;
	ResultSet resultset;
	ResultSet resultset1;
	String workname, wonumber,sDate;
	String headOfAccount="";
	String workOrderNo="";
	String nameOfProject="";
	String valueOfWorkAmount="";
	String expenditureAmount="";
	String expenditureBillAdmittedAmount="";
	String totalExpenditure="";
	String amountOfContractUnexecute="";
	String projectCompleted="";
	String FinancialYear="";
	ArrayList list;
	String woDate ="";
	private TaskFailedException taskExp;
	private static  final Logger LOGGER = Logger.getLogger(SummaryStatement.class);
	
	//	This method is called by the SummaryStatement.jsp
	public ArrayList getSummaryStatementReport(String wonumber,String workname,String monthNm,String workDate,String FinancialYear)throws TaskFailedException
	{
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date dt;
		
	
		
		LOGGER.debug("Work Number:"+ wonumber);
		LOGGER.debug("Work Name:"+ workname);
		LOGGER.debug("Date:"+ workDate);
		LOGGER.debug("Mode:"+ monthNm);
		LOGGER.debug("FinancialYear From JSP Page : : "+FinancialYear);
		
		try
		{
			this.workname = workname;
			this.wonumber = wonumber;
			this.sDate = monthNm;
			this.FinancialYear=FinancialYear;
			
			
			if(!workDate.trim().equals(""))
			{
				dt = sdf.parse(workDate);
				woDate = formatter.format(dt);
				LOGGER.debug("AFTER CONVERT WORK ORDER DATE IS---->"+woDate);
			}
			
			getReport();
			
		}
		catch(Exception exception)
		{
			LOGGER.error("EXP="+exception.getMessage());
			throw taskExp;
		}
		
		
		return list;
	}
	
	
	void getReport() throws Exception
	{
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
				
				LOGGER.debug("Month Name Is :"+sDate);
				//Here STATUSID taking from egw_status es table "id" where upper(es.MODULETYPE)=upper('WORKSBILL') and upper(es.DESCRIPTION)=upper('Passed')
				//Here STATUSID is compare with eg_billregister table STATUSID column value.
				
				StringBuffer basicquery = new StringBuffer(" SELECT gl.GLCODEID AS \"glcodeId\",w.CODE AS \"WORKNUMBER\" ,w.PASSEDAMOUNT AS \"worksPassedAmount\","
						+" c.GLCODE ||'/' || C.NAME AS \"Head Of Account\" , "
						+" w.NAME AS \"Project Name\",w.TOTALVALUE AS \"Value ofWork/ContractorAmount\" ,"
						+" SUM(cbd.PASSEDAMOUNT) AS \"Expenditure(During this Month)\" "
						//+" DECODE(w.ISACTIVE ,'0','YES',DECODE(w.isactive,1,'NO')) AS \"Whether Project completed\" "
						+" FROM eg_Billregister b,worksdetail w ,contractorbilldetail cbd,chartofaccounts c,"
						+" generalledger gl, voucherheader v,FISCALPERIOD f , financialyear fy , egw_status es ");
				
				StringBuffer wherequery = new StringBuffer(" WHERE fy.FINANCIALYEAR='").append(FinancialYear).append("' AND b.WORKSDETAILID=w.ID "
						+" AND b.STATUSID=(select id from egw_status es where upper(es.MODULETYPE)=upper('WORKSBILL') and upper(es.DESCRIPTION)=upper('Passed'))"
						+" AND cbd.BILLID=b.ID AND cbd.VOUCHERHEADERID=v.ID AND gl.GLCODEID=c.ID"
						+" AND gl.VOUCHERHEADERID=cbd.VOUCHERHEADERID AND gl.DEBITAMOUNT > 0"
						+" AND es.id=b.statusid AND extract(month from v.VOUCHERDATE) ='"+sDate+"'"
						+" AND fy.id=f.FINANCIALYEARID AND F.ID=V.FISCALPERIODID AND b.PASSEDAMOUNT>0 ");
				
				StringBuffer orderbyquery = new StringBuffer(" GROUP BY gl.GLCODEID,c.GLCODE,C.NAME,w.CODE,w.NAME,w.PASSEDAMOUNT,w.TOTALVALUE  ORDER BY \"WORKNUMBER\",\"Head Of Account\" ASC ");
				
				statement=con.createStatement();
				
				if (!wonumber.equals(""))
					wherequery = wherequery.append(" AND w.CODE=trim('").append(wonumber).append("')");
				if (!workname.equals(""))
					wherequery = wherequery.append(" AND w.NAME=trim('").append(workname).append("')");
				if (!woDate.equals(""))
					wherequery = wherequery.append(" AND w.ORDERDATE ='").append(woDate).append("'");
				
				String query = new StringBuffer().append(basicquery).append(wherequery).append(orderbyquery).toString();
				
				resultset = statement.executeQuery(query);
				LOGGER.debug("Main Query---------->"+query);
				
				try
				{
					String WORKNUMBER="";
					String glcodeIDParam="";
					String headOfAccount="";
					String nameOfProject="";
					String valueOfWorkAmount="";
					String expenditureAmount="";
					String expenditureBillAdmittedAmount="";
					String remainingUnExecutedAmt="";
					String wdPassedAmount="";
					
					SummaryStatementBean iutBean = null;
					list = new ArrayList();
					while(resultset.next())
					{
						
						iutBean = new SummaryStatementBean();
						
						headOfAccount=resultset.getString("Head Of Account");
						WORKNUMBER=resultset.getString("WORKNUMBER");
						wdPassedAmount=resultset.getString("worksPassedAmount");
						glcodeIDParam=resultset.getString("glcodeId");
						nameOfProject=resultset.getString("Project Name");
						valueOfWorkAmount=resultset.getString("Value ofWork/ContractorAmount");
						expenditureBillAdmittedAmount=resultset.getString("Expenditure(During this Month)");
						
						
						if(headOfAccount!=null)
							iutBean.setHeadOfAccount(headOfAccount);
						else
							iutBean.setHeadOfAccount("&nbsp;");
						
						if(WORKNUMBER!=null)
							iutBean.setWorkOrderNo(WORKNUMBER);
						else
							iutBean.setWorkOrderNo("&nbsp;");
						if(nameOfProject!= null)
							
							iutBean.setNameOfProject(nameOfProject);
						else
							iutBean.setNameOfProject("&nbsp;");
						
						if(valueOfWorkAmount!= null)
							iutBean.setValueOfWorkAmount( ""+new BigDecimal(Double.parseDouble(valueOfWorkAmount)).setScale(2, BigDecimal.ROUND_HALF_UP));
						else
							iutBean.setValueOfWorkAmount("0.00");
						
						if(expenditureBillAdmittedAmount!= null)
							iutBean.setExpenditureBillAdmittedAmount( ""+new BigDecimal(Double.parseDouble(expenditureBillAdmittedAmount)).setScale(2, BigDecimal.ROUND_HALF_UP));
						else
							iutBean.setExpenditureBillAdmittedAmount("0.00");
						
						if(wdPassedAmount!= null)
						{
							remainingUnExecutedAmt=(new BigDecimal(Double.parseDouble(valueOfWorkAmount)- (Double.parseDouble(wdPassedAmount))).setScale(2, BigDecimal.ROUND_HALF_UP)).toString();
							iutBean.setAmountOfContractUnexecute(""+remainingUnExecutedAmt);
						}
						else
						{
							if(valueOfWorkAmount!=null)
							remainingUnExecutedAmt=(new BigDecimal(Double.parseDouble(valueOfWorkAmount)).setScale(2, BigDecimal.ROUND_HALF_UP)).toString();
							iutBean.setAmountOfContractUnexecute(""+remainingUnExecutedAmt);
						}
						
						if(remainingUnExecutedAmt.equalsIgnoreCase("0.00"))
							iutBean.setProjectCompleted("YES");
						else
							iutBean.setProjectCompleted("NO");
						
						
						LOGGER.info("WorkOrderNo param------------->"+WORKNUMBER);
						LOGGER.info("glcodeIDParam----------->"+glcodeIDParam);
						
						//Here STATUSID taking from egw_status es table "id" where upper(es.MODULETYPE)=upper('WORKSBILL') and upper(es.DESCRIPTION)=upper('Passed')
						//Here STATUSID is compare with eg_billregister table STATUSID column value.
						
						String query1=	" SELECT w.CODE AS \"WORKNUMBER\","
							+" c.GLCODE ||'/' || C.NAME AS \"Head Of Account\" ,sum(cbd.PASSEDAMOUNT) as \"ExpB\" "
							+" FROM eg_Billregister b,worksdetail w ,contractorbilldetail cbd,chartofaccounts c,"
							+" generalledger gl, voucherheader v,FISCALPERIOD f , financialyear fy , egw_status es "
							
							+" WHERE fy.FINANCIALYEAR='"+FinancialYear+"' AND b.WORKSDETAILID=w.ID AND w.CODE='"+WORKNUMBER+"' "
							+" AND b.STATUSID=(select id from egw_status es where upper(es.MODULETYPE)=upper('WORKSBILL') and upper(es.DESCRIPTION)=upper('Passed'))"
							+" AND cbd.BILLID=b.ID AND cbd.VOUCHERHEADERID=v.ID AND gl.GLCODEID="+glcodeIDParam+" AND gl.GLCODEID=c.ID"
							+" AND gl.VOUCHERHEADERID=cbd.VOUCHERHEADERID AND gl.DEBITAMOUNT > 0"
							+" AND es.id=b.statusid AND extract(month from v.VOUCHERDATE) <'"+sDate+"'"
							+" AND fy.id=f.FINANCIALYEARID AND F.ID=V.FISCALPERIODID AND b.PASSEDAMOUNT>0 "
							
							+" GROUP BY gl.GLCODEID,c.GLCODE,C.NAME,w.CODE,w.NAME,w.TOTALVALUE"
							+" ORDER BY \"WORKNUMBER\",\"Head Of Account\" ASC ";
						
						
						st2=con.createStatement();
						resultset1 = st2.executeQuery(query1);
						//LOGGER.info("Query2 for Expenses for Beginning of the month---------->"+query1);
						
						if(resultset1.next())
						{
							expenditureAmount=(resultset1.getString("ExpB")==null?"0.00":resultset1.getString("ExpB"));
							iutBean.setExpenditureAmount( ""+new BigDecimal(Double.parseDouble(expenditureAmount)).setScale(2, BigDecimal.ROUND_HALF_UP));
							iutBean.setTotalExpenditure(""+new BigDecimal(Double.parseDouble(expenditureAmount) + Double.parseDouble(expenditureBillAdmittedAmount) ).setScale(2, BigDecimal.ROUND_HALF_UP));
						}
						else
						{
							expenditureAmount="0.00";
							iutBean.setExpenditureAmount("0.00");
							if(expenditureBillAdmittedAmount!=null)
							iutBean.setTotalExpenditure( ""+new BigDecimal(Double.parseDouble(expenditureBillAdmittedAmount)).setScale(2, BigDecimal.ROUND_HALF_UP));
							
						}
						// For Inside sub query
						resultset1.close();
						st2.close();
						
						list.add(iutBean);
					} //main while
					
						resultset.close();
						statement.close();
				}
				catch(Exception e)
				{
					LOGGER.error("Error in getReport"+e.getMessage());
					
				}
			}
		});
		
	}
	
}
