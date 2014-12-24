/*
 * Created on Dec 21, 2005
 * @author Sumit
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.ExilPrecision;
import com.exilant.eGov.src.transactions.OpBal;
import com.exilant.exility.common.TaskFailedException;


public class GeneralLedgerReport {
	
	Statement statement=null;
	ResultSet resultset=null;
	ResultSet resultset1=null;
	String accEntityId= null;
	 String accEntityKey= null;
	BigDecimal slDrAmount = new BigDecimal("0.00");
	BigDecimal slCrAmount = new BigDecimal("0.00");
	String engineQry=null;
	 private static TaskFailedException taskExc;
	String startDate, endDate, effTime,rType="gl";
    private static final Logger LOGGER = Logger.getLogger(GeneralLedgerReport.class);
    com.exilant.eGov.src.transactions.OpBal OpBal= new com.exilant.eGov.src.transactions.OpBal();
    DecimalFormat dft = new DecimalFormat("##############0.00");
    EGovernCommon egc=new EGovernCommon();
    CommnFunctions cmnFun=new CommnFunctions();
    
    
	public GeneralLedgerReport(){}

	public LinkedList getGeneralLedgerList(final GeneralLedgerReportBean reportBean)throws TaskFailedException{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<LinkedList>() {

			@Override
			public LinkedList execute(Connection connection) throws SQLException {
				
			    
				LinkedList dataList = new LinkedList();
				LOGGER.info("Indise the loop..........");
				
				CashBook cashbook=new CashBook();
				
				try	{ statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				}catch(Exception e)
				{
					LOGGER.error("Exception in creating statement:"+statement);
					
				}
				
				String isconfirmed="";
				String glCode1="";
				glCode1 = reportBean.getGlCode1();
				try {
					
				
				try
				{
					String snapShotDateTime=reportBean.getSnapShotDateTime();
					if(snapShotDateTime.equalsIgnoreCase(""))
					effTime="";
					else
					effTime=egc.getEffectiveDateFilter(connection,snapShotDateTime);
				}
				catch(Exception ex){
					LOGGER.error("Exception Occured"+ex.getMessage());
				}
				String fundId = reportBean.getFund_id();
				String fundSourceId = reportBean.getFundSource_id();
		        reportBean.setFundName(getFundName(fundId));
		        reportBean.setAccountCode(getAccountName(glCode1));
		 		String formstartDate="";
				String formendDate="";
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				Date dt=new Date();
				String endDate1=(String)reportBean.getEndDate();
				EGovernCommon.isCurDate(endDate1);
				try
		   		{
			   		endDate=(String)reportBean.getEndDate();
			   		dt = sdf.parse(endDate );
					formendDate = formatter1.format(dt);
				}
		   		catch(Exception e){
		   			LOGGER.error("inside the try-startdate"+e);
		   			
		   		}
		   		try
		   		{	startDate=(String)reportBean.getStartDate();
					if(!startDate.equalsIgnoreCase("null")){
						dt = sdf.parse(startDate);
						formstartDate = formatter1.format(dt);
					}
					
					if(startDate.equalsIgnoreCase("null")){
						String finId=cmnFun.getFYID(formendDate);
						startDate=cmnFun.getStartDate(Integer.parseInt(finId));
					    // SETTING START DATE IN reportBean 
						reportBean.setStartDate(startDate);
						Date dtOBj=sdf.parse(startDate);
						startDate=formatter1.format(dtOBj);
					}
			   		else{
			   			startDate = formstartDate;
			   		}
		   		}
				catch(Exception e){
					LOGGER.error("inside the try-startdate"+e);
					
				}

				
				accEntityId = reportBean.getAccEntityId();
			    accEntityKey = reportBean.getAccEntityKey();
				endDate = formendDate;
				String startDateformat = startDate;
				String startDateformat1 = "";
				try
				{
					dt = formatter1.parse(startDateformat);
					startDateformat1 = sdf.format(dt);
				}
				catch(Exception e){     
					LOGGER.error("Parse Exception"+e);
					
				}
				setDates(startDate,endDate);
				String fyId = cmnFun.getFYID(endDate);
				if(fyId.equalsIgnoreCase("")){
					LOGGER.info("Financial Year Not Valid");
					
				}
				//CodeValidator cv=CodeValidator.getInstance();
				/*if(!cv.isValidCode(glCode1))
				{
					LOGGER.error(glCode1 +" Not Valid");
					throw taskExc;
				}*/
				double txnDrSum=0, txnCrSum=0, openingBalance=0, closingBalance = 0;
				
				//String query = getQuery(glCode1,fundId, fundSourceId, startDate, endDate);

				ReportEngine engine=new ReportEngine();
				ReportEngineBean reBean=engine.populateReportEngineBean(reportBean);
				engineQry=	engine.getVouchersListQuery(reBean);
				
				String query = getQuery(glCode1,startDate, endDate,accEntityId,accEntityKey,reportBean.getFieldId(),reBean.getFunctionId());
				String functionId = reBean.getFunctionId();
				LOGGER.info("**************QUERY: " + query); 
				try{

					try
					{
						statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					}
					catch(Exception e)
					{
						LOGGER.error("Exception in creating statement:"+statement);
						
					}

					resultset1 = statement.executeQuery(query);
					ArrayList data = new ArrayList();
					String accCode="", vcNum="", vcDate="", narration="",vcTypeName="";
					StringBuffer detail = new StringBuffer();
					StringBuffer amount = new StringBuffer();
					int vhId = 0, curVHID = 0 , cout=0 , VhidPrevious=0,lenAfterAppend=0,lenBeforeAppend=0,lenDetailBefore=0,lenDetailAfter=0;
					double txnDebit=0, txnCredit=0,previousDebit=0,previousCredit=0;
					String code="",currCode="",accCodePrevious="", cgn="";
					/**
					 * When using ResultSet.TYPE_SCROLL_INSENSITIVE in createStatement
					 * if no records are there, rs.next() will return true
					 * but when trying to access (rs.getXXX()), it will throw an error
					 **/
						int totalCount=0, isConfirmedCount=0;
						String vn2="";
						LOGGER.debug("resultset1---------------------------->"+resultset1);
						if(!resultset1.next())
						{
								 OpBal opbal = getOpeningBalance(glCode1, fundId, fundSourceId, fyId, accEntityId, accEntityKey, startDate,functionId);
			                     String arr[] = new String[14];
		                         openingBalance = opbal.dr - opbal.cr;
		                         LOGGER.info("openingBalance--------------->"+openingBalance);
		                                                
		                         statement=connection.createStatement();
		                         ResultSet res=statement.executeQuery("select name as \"glname\" from chartofaccounts where glcode='"+glCode1+"'");
		                         String aName="";
		                         if(res.next() && res.getString("glname") !=null)
		                         {
		                             aName=res.getString("glname");
		                         }
		                         res.close();
		                         statement.close();
		                         arr[1]="";
		                         arr[2]=arr[3]=arr[6]=arr[7]=arr[10]=arr[11]=arr[12]=arr[13]="&nbsp;";
		                         if(vhId == 0)
		                         arr[8]="&nbsp;";                        
		                         arr[9]=glCode1+"-"+aName;
		                         if(openingBalance > 0){
		                         arr[4]="<b>"+numberToString(((Double)Math.abs(openingBalance)).toString()).toString()+"</b>";
		                         arr[5]="&nbsp;";
		                         }
		                         else if(openingBalance < 0){
		                             arr[4]="&nbsp;";
		                             arr[5]="<b>"+numberToString(((Double)Math.abs(openingBalance)).toString()).toString()+"</b>";
		                             }else{
		                         arr[4]="&nbsp;";
		                         arr[5]="&nbsp;";
		                         }
		                         arr[0]="<b>Opening Balance</b>";
		                         if(vhId == 0 && !(openingBalance > 0 || openingBalance < 0))
		                         { LOGGER.debug("Inside if condition");}
		                         else
		                         data.add(arr);
		                         
		                         String arr2[] = new String[14];                       
		                         closingBalance = openingBalance;
		                         if(closingBalance > 0){
		                         arr2[4]="&nbsp;";
		                         arr2[5]="<b>"+numberToString(((Double)Math.abs(closingBalance)).toString()).toString()+"</b>";
		                         }else if(closingBalance < 0){                        
		                         arr2[4]="<b>"+numberToString(((Double)Math.abs(closingBalance)).toString()).toString()+"</b>";
		                         arr2[5]="&nbsp;";
		                         }else{
		                         arr2[4]="&nbsp;";
		                         arr2[5]="&nbsp;";
		                         }
		                         arr2[2]="&nbsp;";
		                         arr2[0]="<b>Closing Balance</b>";
		                         arr2[1]="";
		                         arr2[3]=arr2[6]=arr2[7]=arr2[8]=arr2[9]=arr2[10]=arr2[11]=arr[12]=arr[13]="&nbsp;";
		                         data.add(arr2);					
						}
						resultset1.beforeFirst();
						while(resultset1.next())
						{
							 LOGGER.info(" inside resultset");
							try{
								code= resultset1.getString("code");
								isconfirmed= resultset1.getString("isconfirmed");
								//9 is the dummy value used in the query
								// To display X in Y are unconfirmed
								if(isconfirmed!=null && !isconfirmed.equalsIgnoreCase("")&& !isconfirmed.equalsIgnoreCase("9"))
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

		                        //cout1=0;
		                        vhId = resultset1.getInt("vhid");

		                        /**
		                         * When the main GLCODES are changing.We need to get the opening balance first.
		                         */
		                        if(!code.equals(currCode))
		                        {
			                            //glType=resultset1.getString("glType");
			                            String arr[] = new String[14];
			                            OpBal opbal = getOpeningBalance(code, fundId, fundSourceId, fyId, accEntityId, accEntityKey, startDate,functionId);
		                                openingBalance = opbal.dr - opbal.cr;
		                                String fundName="";
		                                if (resultset1.getString(14) != null )
		                                {
		                                    fundName=resultset1.getString(14);
		                                }
		                                statement=connection.createStatement();
		                                ResultSet res=statement.executeQuery("select name as \"glname\" from chartofaccounts where glcode='"+code+"'");
		                                String aName="";
		                                if(res.next() && res.getString("glname") !=null)
		                                {
		                                    aName=res.getString("glname");
		                                }
		                                
		                                res.close();
		                                statement.close();
		                                arr[1]="";
		                                arr[2]=arr[3]=arr[6]=arr[7]=arr[10]=arr[11]=arr[12]=arr[13]="&nbsp;";
		                                if(vhId == 0)
		                                arr[8]="&nbsp;";
		                                else
		                                arr[8]=fundName;
		                                arr[9]=code+"-"+aName;
		                                if(openingBalance > 0){
		                                arr[4]="<b>"+numberToString(((Double)Math.abs(openingBalance)).toString()).toString()+"</b>";
		                                arr[5]="&nbsp;";
		                                }
		                                else if(openingBalance < 0){
		                                    arr[4]="&nbsp;";
		                                    arr[5]="<b>"+numberToString(((Double)Math.abs(openingBalance)).toString()).toString()+"</b>";
		                                    }else{
		                                arr[4]="&nbsp;";
		                                arr[5]="&nbsp;";
		                                }
		                                arr[0]="<b>Opening Balance</b>";
		                                if(vhId == 0 && !(openingBalance > 0 || openingBalance < 0))
		                                { 
		                                	LOGGER.debug("Inside if");
		                                }
		                                else
		                                data.add(arr);

		                                currCode=code;
		                            }//End If glcodes changing
		                    }
		                    catch(SQLException ex){
		                        LOGGER.error("ERROR (not an error): ResultSet is Empty");
		                       
		                    }
		                    //Vouchers are changing
		                   if(curVHID >0 && vhId != curVHID && cout==0 && vhId != 0)
		                    {
		                	   	
		                       if(txnDebit>0)
		                       {
		                        previousDebit=0;
		                        previousCredit=0;
		                        String arr9[] = new String[14];
		                        arr9[0] = vcDate;
		                        arr9[1] = vcNum;

		                        arr9[2] = detail.toString();
		                        arr9[3] = "&nbsp;";
		                        arr9[4] = numberToString(((Double)txnDebit).toString())+"";
		                        arr9[5] = "&nbsp;";
		                        if(narration != null)
		                        arr9[6] ="&nbsp;"+narration;
		                        else
		                        arr9[6] ="&nbsp;";
		                        arr9[7]=cgn;
		                        txnDrSum = txnDrSum + txnDebit;
		                        txnCrSum = txnCrSum + txnCredit;

		                        arr9[10]="&nbsp;";
		                        arr9[11]="&nbsp;";

		                        // End
		                        arr9[8]=arr9[9]="&nbsp;";
		                        arr9[12]=vcTypeName;
		                        arr9[13]="&nbsp;";
		                        data.add(arr9);
		                       }
		                       else if(txnCredit>0)
		                       {
		                            previousDebit=0;
		                            previousCredit=0;
		                            String arr9[] = new String[14];
		                            arr9[0] = "&nbsp;";
		                            arr9[1] = "&nbsp;";
		                            arr9[2] = "&nbsp;";
		                            arr9[3] = detail.toString();
		                            arr9[5] = numberToString(((Double)txnCredit).toString())+"";
		                            arr9[4] = "&nbsp;";
		                            if(narration != null)
		                            arr9[6] ="&nbsp;"+narration;
		                            else
		                            arr9[6] ="&nbsp;";
		                            arr9[7]=cgn;
		                            txnDrSum = txnDrSum + txnDebit;
		                            txnCrSum = txnCrSum + txnCredit;
		                            arr9[10]=vcDate;
		                            arr9[11]=vcNum;
		                            arr9[12]="&nbsp;";
		                            arr9[13]=vcTypeName;
		                            // End
		                            arr9[8]=arr9[9]="&nbsp;";
		                            data.add(arr9);
		                           }
		                        detail.delete(0, detail.length());
		                        amount.delete(0, amount.length());
		                        //cnt = 0;
		                        vcDate = vcNum = accCode = narration = vcTypeName="";
		                    }//End If
		                    curVHID = vhId;
		                    cout=0;
		                    accCode = resultset1.getString("glcode");
		                    String detailId=null;
		                    if(!accEntityKey.equals(""))
		                    	detailId=resultset1.getString("DetailKeyId");
		                    LOGGER.debug("accEntityKey---->"+accEntityKey);
		                    if(!accCode.equalsIgnoreCase(accCodePrevious))
		                    {
		                    	previousDebit=0;
		                        previousCredit=0;
		                    }
		                    
		                    if(accCode.equalsIgnoreCase(code))
		                    {	
		                    	if(detailId != null && !detailId.equals(accEntityKey))
		                        {
			                        slDrAmount=slDrAmount.add(resultset1.getBigDecimal("debitamount"));
			                        slCrAmount=slCrAmount.add(resultset1.getBigDecimal("creditamount"));
			                    }
		                    }
		                    else if(!accEntityKey.equals(""))
		                    {
		                    	/*if(slCrAmount.compareTo(BigDecimal.ZERO)!=0) 
		                    	{
		                    		detail= detail.append(" " + glCode1+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
		                    		slCrAmount=new BigDecimal("0.00");
		                    	}
		                    	else if(slDrAmount.compareTo(BigDecimal.ZERO)!=0)
		                    	{
		                    		detail= detail.append(" " + glCode1+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
		                    		slDrAmount=new BigDecimal("0.00");
		                    	}*/
		                    	//detail= detail.append(" " + glCode1+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
		                		slCrAmount=new BigDecimal("0.00");
		                		slDrAmount=new BigDecimal("0.00");
		                    } 	
		                    
		                    if(vhId != 0 && (detailId == null || detailId.equals(accEntityKey)) && !accEntityKey.equals(""))
		                    {
		                        //get the details other than patriculars
		                        LOGGER.debug("detailId-->" +detailId+"accCode-->" +accCode+"::code:"+code);
		                        if(accCode.equalsIgnoreCase(code))
		                        {
		                        	LOGGER.debug("accCode...................."+accCode);
		                            double currentDebit=0,currentCredit=0,debit=0,credit=0;	
		                            if(vhId==VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious))
		                            {
		                            	LOGGER.debug("vhId:::::::::::::::::"+vhId);
		                                vcDate = resultset1.getString("voucherdate");
		                                vcNum = resultset1.getString("vouchernumber");
		                                vcTypeName = resultset1.getString("vouchertypename");
		                                String vhId1=resultset1.getString("vhid");
		                                LOGGER.info("vhId1:::"+vhId1);
		                                cgn =cashbook.getCGN(vhId1);
		                                //type = resultset1.getString("type");
		                                if(detailId != null)
		                                {
			                                currentDebit=resultset1.getDouble("debitamount");
			                                currentCredit=resultset1.getDouble("creditamount");
			                                debit=(previousDebit+currentDebit)-(previousCredit+currentCredit);
			                                if(debit>0) txnDebit=debit;
			                                else txnDebit=0;
			                                credit=(previousCredit+currentCredit)-(previousDebit+currentDebit);
			                                if(credit>0) txnCredit=credit;
			                                else txnCredit=0;	                                
			                                previousDebit=previousDebit+currentDebit;
			                                previousCredit=previousCredit+currentCredit;
		                                }
		                                narration = resultset1.getString("narration");
		                            }
		                            else
		                            {
			                            vcDate = resultset1.getString("voucherdate");
			                            vcNum = resultset1.getString("vouchernumber");
			                            vcTypeName = resultset1.getString("vouchertypename");
			                            String vhId1=resultset1.getString("vhid");
										LOGGER.info("vhId1:::"+vhId1);
			                            cgn =cashbook.getCGN(vhId1);
			                          //  type = resultset1.getString("type");
			                            if(detailId != null)
			                            {
				                            txnDebit = resultset1.getDouble("debitamount");
				                            previousDebit=txnDebit;
				                            txnCredit = resultset1.getDouble("creditamount");
				                            previousCredit=txnCredit;
			                            }
			                            narration = resultset1.getString("narration");
		                            }
		                        }else{
		                            if(vhId==VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious))
		                            {
		                                double currentDebit=0,currentCredit=0,debit=0,credit=0;
		                                String debitAmount="",creditAmount="";
		                                amount.delete(lenBeforeAppend,lenAfterAppend);
		                                detail.delete(lenDetailBefore,lenDetailAfter);
		                          
		                                detail = detail.append(" " + resultset1.getString("glcode")+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
		                                currentDebit=resultset1.getDouble("debitamount");
		                                currentCredit=resultset1.getDouble("creditamount");
		                                debit=(previousDebit+currentDebit)-(previousCredit+currentCredit);
		                                if(debit>0)
		                                {
		                                    debitAmount="Dr."+ExilPrecision.convertToString(debit,2)+"0";
		                                    amount  =amount.append(" " + debitAmount );
		                                 }
		                                credit=(previousCredit+currentCredit)-(previousDebit+currentDebit);
		                                if(credit>0)
		                                {
		                                    creditAmount="Cr."+ExilPrecision.convertToString(credit,2)+"0";
		                                    amount  =amount.append(" " + creditAmount );
		                                }

		                            }
		                            else
		                            {
		                            	detail= detail.append(" " + resultset1.getString("glcode")+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
		                            	previousDebit=resultset1.getDouble("debitamount");
		                            	previousCredit=resultset1.getDouble("creditamount");
		                            }
		                        }
		                    }
		                    else if(vhId != 0 && accEntityKey.equals(""))
		                    {
		                        //LOGGER.debug(" inside vhId != 0");
		                        //get the details other than patriculars
		                        if(accCode.equalsIgnoreCase(code) )
		                        {
		                            double currentDebit=0,currentCredit=0,debit=0,credit=0;
		                            if(vhId==VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious) //&& (StringUtils.isEmpty(reBean.getFunctionId()) || reBean.getFunctionId().equals(resultset1.getString("functionid"))) 
		                            		) 
		                    		{
		                                vcDate = resultset1.getString("voucherdate");
		                                vcNum = resultset1.getString("vouchernumber");
		                                vcTypeName = resultset1.getString("vouchertypename");
		                                String vhId1=resultset1.getString("vhid");
										LOGGER.info("vhId1:::"+vhId1);
		                                cgn =cashbook.getCGN(vhId1);
		                               // type = resultset1.getString("type");
		                                currentDebit=resultset1.getDouble("debitamount");
		                                currentCredit=resultset1.getDouble("creditamount");
		                                debit=(previousDebit+currentDebit)-(previousCredit+currentCredit);
		                                if(debit>0) txnDebit=debit;
		                                else txnDebit=0;
		                                credit=(previousCredit+currentCredit)-(previousDebit+currentDebit);
		                                if(credit>0) txnCredit=credit;
		                                else txnCredit=0;
		                                narration = resultset1.getString("narration");
		                               /* previousDebit=currentDebit;
		                                previousCredit=currentCredit;*/
		                                previousDebit = txnDebit;
		                                previousCredit=txnCredit;
		                            }
		                            else //if (StringUtils.isEmpty(reBean.getFunctionId()) || reBean.getFunctionId().equals(resultset1.getString("functionid")))
		                            {
			                            vcDate = resultset1.getString("voucherdate");
			                            vcNum = resultset1.getString("vouchernumber");
			                            vcTypeName = resultset1.getString("vouchertypename");
			                            String vhId1=resultset1.getString("vhid");
										LOGGER.info("vhId1:::"+vhId1);
			                            cgn =cashbook.getCGN(vhId1);
			                           // type = resultset1.getString("type");
			                            txnDebit = resultset1.getDouble("debitamount");
			                            previousDebit=txnDebit;
			                            txnCredit = resultset1.getDouble("creditamount");
			                            previousCredit=txnCredit;
			                            narration = resultset1.getString("narration");
		                            }
		                            /*else
		                            {
			                            detail= detail.append(" " + resultset1.getString("glcode")+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
		                            }*/
		                        }
		                        else
		                        {
		                            if(vhId==VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious) //&& (StringUtils.isEmpty(reBean.getFunctionId()) || reBean.getFunctionId().equals(resultset1.getString("functionid")))
		                            		)
		                            {
		                                double currentDebit=0,currentCredit=0,debit=0,credit=0;
		                                String debitAmount="",creditAmount="";

		                                    amount.delete(lenBeforeAppend,lenAfterAppend);
		                                    detail.delete(lenDetailBefore,lenDetailAfter);
		                          
				                                detail = detail.append(" " + resultset1.getString("glcode")+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
				                                currentDebit=resultset1.getDouble("debitamount");
				                                currentCredit=resultset1.getDouble("creditamount");
				                                debit=(previousDebit+currentDebit)-(previousCredit+currentCredit);
		                                if(debit>0)
		                                {
		                                    debitAmount="Dr."+ExilPrecision.convertToString(debit,2)+"0";
		                                    amount  =amount.append(" " + debitAmount );
		                                 }
		                                credit=(previousCredit+currentCredit)-(previousDebit+currentDebit);
		                                if(credit>0)
		                                {
		                                    creditAmount="Cr."+ExilPrecision.convertToString(credit,2)+"0";
		                                    amount  =amount.append(" " + creditAmount );
		                                }

		                            }
		                            else
		                            {
			                            detail= detail.append(" " + resultset1.getString("glcode")+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
			                            previousDebit=resultset1.getDouble("debitamount");
			                            previousCredit=resultset1.getDouble("creditamount");
		                            }
		                        } // else
		                    }
		                    else if(vhId != 0 && !accEntityKey.equals(""))
		                    {
		                    	detail= detail.append(" " + resultset1.getString("glcode")+"&nbsp;&nbsp;&nbsp;"+ resultset1.getString("amount"));
		                    }

		                    accCodePrevious=accCode;
		                    VhidPrevious=vhId;
		                    if(resultset1.isLast())
		                    {

		                        if(txnDebit>0)
		                        {
		                            String arr[] = new String[14];
		                            arr[0] = vcDate;
		                            arr[1] = vcNum;

		                            arr[2] = detail.toString();
		                            arr[3] = "&nbsp;";
		                            arr[4] = numberToString(((Double)txnDebit).toString())+"";
		                            arr[5] = "&nbsp;";
		                            if(narration != null)
		                            arr[6] = "&nbsp;"+narration;
		                            else
		                            arr[6] ="&nbsp;";
		                            txnDrSum = txnDrSum + txnDebit;
		                            txnCrSum = txnCrSum + txnCredit;
		                            arr[8]=arr[9]="&nbsp;";
		                            arr[4] = arr[4].equalsIgnoreCase(".00") ? "&nbsp;" : arr[4];
		                            arr[7]=cgn;
		                            arr[10]="&nbsp;";
		                            arr[11]="&nbsp;";
		                            arr[12]=vcTypeName;
		                            arr[13]="&nbsp;";
		                            data.add(arr);
		                        }
		                        else if(txnCredit>0)
		                        {
		                            String arr[] = new String[14];
		                            arr[0] = "&nbsp;";
		                            arr[1] = "&nbsp;";
		                            arr[2] = "&nbsp;";
		                            arr[3] = detail.toString();
		                            arr[4] = "&nbsp;";
		                            arr[5] = numberToString(((Double)txnCredit).toString())+"";
		                            if(narration != null)
		                            arr[6] = "&nbsp;"+narration;
		                            else
		                            arr[6] ="&nbsp;";
		                            txnDrSum = txnDrSum + txnDebit;
		                            txnCrSum = txnCrSum + txnCredit;
		                            arr[8]=arr[9]="&nbsp;";
		                            arr[5] = arr[5].equalsIgnoreCase(".00") ? "&nbsp;" : arr[5];
		                            arr[7]=cgn;
		                            arr[10]=vcDate;
		                            arr[11]=vcNum;
		                            arr[12]="&nbsp;";
		                            arr[13]=vcTypeName;
		                            data.add(arr);
		                        }
		                            detail.delete(0, detail.length());
		                            amount.delete(0,amount.length());
		                           // cnt = 0;
		                            vcDate = vcNum = accCode =  narration = "&nbsp;";
		                        String arr2[] = new String[14];
		                        if(openingBalance > 0)
		                        txnDrSum = txnDrSum + Math.abs(openingBalance);
		                        else
		                        txnCrSum = txnCrSum + Math.abs(openingBalance);
		                        closingBalance = txnDrSum - txnCrSum;
		                        if(closingBalance > 0){
		                        txnCrSum = txnCrSum + Math.abs(closingBalance);
		                        arr2[4]="&nbsp;";
		                        arr2[5]="<b>"+numberToString(((Double)Math.abs(closingBalance)).toString()).toString()+"</b>";
		                        }else if(closingBalance < 0){
		                        txnDrSum = txnDrSum + Math.abs(closingBalance);
		                        arr2[4]="<b>"+numberToString(((Double)Math.abs(closingBalance)).toString()).toString()+"</b>";
		                        arr2[5]="&nbsp;";
		                        }else{
		                        arr2[4]="&nbsp;";
		                        arr2[5]="&nbsp;";
		                        }
		                        arr2[2]="&nbsp;";
		                        arr2[0]="<b>Closing Balance</b>";
		                        arr2[1]="";
		                        arr2[3]=arr2[6]=arr2[7]=arr2[8]=arr2[9]=arr2[10]=arr2[11]=arr2[12]=arr2[13]="&nbsp;";
		                        data.add(arr2);
		                        String arr1[] = new String[14];
		                        if(txnDrSum > 0)
		                        arr1[4]="<hr><b>"+numberToString(((Double)txnDrSum).toString())+"</b><hr>";
		                        else
		                        arr1[4]="<hr>&nbsp;<hr>";
		                        if(txnCrSum > 0)
		                        arr1[5]="<hr><b>"+numberToString(((Double)txnDrSum).toString())+"</b><hr>";
		                        else
		                        arr1[5]="<hr>&nbsp;<hr>";
		                        arr1[2]="<hr>&nbsp;<hr>";
		                        arr1[0]="<hr><b>Total</b><hr>";
		                        arr1[1]="<hr>&nbsp;<hr>";
		                        arr1[3]=arr1[6]=arr1[7]=arr1[8]=arr1[9]=arr1[10]=arr1[11]=arr1[12]=arr1[13]="<hr>&nbsp;<hr>";
		                        data.add(arr1);
		                        txnDrSum=0;txnCrSum=0;
		                    }//End If last
		                }//End While


//		              Adding data to 2 dimension array to pass to Linkedlist
		                String gridData[][] = new String[data.size()+1][14]; 
		                gridData[0][0] = "voucherdate";
		                gridData[0][1] = "vouchernumber";
		                gridData[0][2] = "debitparticular";
		                gridData[0][3] = "creditparticular";
		                gridData[0][4] = "debitamount";
		                gridData[0][5] = "creditamount";
		                gridData[0][6] = "narration";
		                gridData[0][7] = "cgn";
		                gridData[0][8] = "fund";
		                gridData[0][9] = "glcode";
		                gridData[0][10] = "creditdate";
		                gridData[0][11] = "creditvouchernumber";
		                gridData[0][12] = "debitVoucherTypeName";
		                gridData[0][13] = "creditVoucherTypeName";
		                for(int i=1; i<=data.size(); i++)
		                    gridData[i] = (String[])data.get(i-1);

		                for(int i=1; i<=data.size(); i++)
		                {
		                    GeneralLedgerBean generalLedgerBean = new GeneralLedgerBean();
		                    generalLedgerBean.setGlcode(gridData[i][9]);
		                    generalLedgerBean.setVoucherdate(gridData[i][0]);
		                    generalLedgerBean.setVouchernumber(gridData[i][1]);
		                    int counter = 0;

		                    String testTemp = gridData[i][2];
		                    char testArrayTemp[] = testTemp.toCharArray();

		                    for(counter=0;counter<testArrayTemp.length;counter++)
		                    {

		                        if (testArrayTemp[counter]== '<' && (testArrayTemp[counter+1]== 'b' ||  testArrayTemp[counter+1]== 'B') )
		                            break;
		                    }
		                    generalLedgerBean.setDebitparticular(gridData[i][2]);
		                    String test = gridData[i][7];
		                    char testArray[] = test.toCharArray();

		                    for(counter=0;counter<testArray.length;counter++)
		                    {
		                        if (testArray[counter]== 'r')
		                        {
		                           break;
		                        }
		                    }

		                    generalLedgerBean.setNarration(gridData[i][6]);
		                    generalLedgerBean.setCreditparticular(gridData[i][3]);
		                    generalLedgerBean.setDebitamount(gridData[i][4]);
		                    generalLedgerBean.setCreditamount(gridData[i][5]);
		                    generalLedgerBean.setFund(gridData[i][8]);
		                    if(i==data.size())
		                        generalLedgerBean.setCGN("");
		                    else
		                      generalLedgerBean.setCGN(gridData[i][7]);
		                    generalLedgerBean.setCreditdate(gridData[i][10]);
		                    generalLedgerBean.setCreditvouchernumber(gridData[i][11]);
		                    generalLedgerBean.setDebitVoucherTypeName(gridData[i][12]);
		                    generalLedgerBean.setCreditVoucherTypeName(gridData[i][13]);
		                    reportBean.setStartDate(startDateformat1);
		                    reportBean.setTotalCount(Integer.toString(totalCount));
		                    reportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));
		                    dataList.add(generalLedgerBean);
					}


				}catch(SQLException ex){
					LOGGER.error("ERROR in getGeneralLedgerList " + ex.toString());
					
				}
				
			
				
			}
			 catch (TaskFailedException e) {
				 throw new ValidationException(
							Arrays.asList(new ValidationError("getReport", e.getMessage())));
			}
				HibernateUtil.release(statement, resultset);
				return dataList;
			}
		});
		
	}

	

	@SuppressWarnings("unchecked")
	private String getQuery(String glCode1,String startDate,String endDate,String accEntityId,String accEntityKey,String fieldId,String functionId)
																			throws TaskFailedException{
	    String addTableToQuery="";
	    String entityCondition="";
	    String functionCondition="";
	    
		if(!accEntityId.equalsIgnoreCase("") && !accEntityKey.equalsIgnoreCase("") ) 
		{
			entityCondition=" AND gl1.id=gldet.generalledgerid  AND gldet1.detailtypeid=" + accEntityId + " AND gldet1.detailkeyid=" + accEntityKey + "";
		}
		if(addTableToQuery.trim().equals("")&& null!= fieldId && !fieldId.trim().equals("")){
			addTableToQuery=", vouchermis vmis ";
		}
		if(!StringUtils.isEmpty(functionId))
			functionCondition = " and gl.functionid="+functionId+" and gl1.functionid="+functionId;
		if(!accEntityKey.equals("")) 
		{
			return	"SELECT distinct gl1.glcode as \"code\",(select ca.type from chartofaccounts ca where glcode=gl1.glcode) as \"glType\" ,"
				+" vh.id AS \"vhid\",vh.voucherDate AS \"vDate\",to_char(vh.voucherDate ,'dd-Mon-yyyy') " 
				+" AS \"voucherdate\",vh.voucherNumber AS \"vouchernumber\",gl.glCode AS \"glcode\",coa.name||" 
				+" (CASE WHEN (GLDET.GENERALLEDGERID=GL.ID) THEN '-['||(decode(gldet.detailtypeid,(select id from accountdetailtype where name='Creditor')," 
				+" (select name from relation where id=gldet.detailkeyid ),(select id from accountdetailtype where name='Employee'),(select emp_firstname from eg_employee" 
				+" where id=gldet.detailkeyid),(select name from accountentitymaster where id=gldet.detailkeyid)))||']'" 
				+" ELSE NULL END) as \"Name\",decode(gl.glcode,'"+glCode1+"',(decode( gl.DEBITAMOUNT,0,(gldet.amount||'.00cr')," 
				+" (gldet.amount||'.00dr'))),(decode(gl.DEBITAMOUNT,0,(gl.creditamount||'.00cr')," 
				+" (gl.debitamount||'.00dr')))) as \"amount\",gl.description as \"narration\",vh .type || '-' || vh.name||DECODE" 
				+" (status,1,'(Reversed)',decode(status,2 ,'(Reversal)','')) AS \"type\"," 
				+" decode(gl.glcode,'"+glCode1+"',(decode(gl.debitAMOUNT,0,0,gldet.amount ))," 
				+" (decode(gl.debitAMOUNT,0,0,gl.debitamount)))as \"debitamount\",decode(gl.glcode,'"+glCode1+ "'," 
				+" (decode(gl.creditAMOUNT,0,0,gldet.amount)),(decode(gl.debitAMOUNT,0,0,gl.creditamount)))as \"creditamount\"," 
				+" f.name as \"fundName\",vh.isconfirmed as \"isconfirmed\",case when (gldet.generalledgerid=gl.id) " 
				+" then gldet.detailkeyid else null end as \"DetailKeyId\",vh.type||'-'||vh.name as \"vouchertypename\" FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa," 
				+" generalLedger gl1,generalledgerdetail gldet , generalledgerdetail gldet1,chartofaccountdetail cdet ," 
				+" fund f WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id AND" 
				+" gl.voucherHeaderId = vh.id AND gl.voucherHeaderId = gl1.voucherHeaderId" 
				+" AND f.id= vh.fundId AND gl1.glcode ='"+glCode1+"'" + entityCondition +"" 					
				+" AND gldet1.generalledgerid=gldet.generalledgerid"  
				+" and vh.id in ("+engineQry+" )"
				+" AND (gl .debitamount>0 OR gl .creditamount>0) "
				+" order by vh.id asc ,gl.glCode";
		}
		else
		{
			
			StringBuffer sql = new StringBuffer("SELECT distinct gl1.glcode as \"code\",(select ca.type from chartofaccounts ca where glcode=gl1.glcode) as \"glType\",vh.id AS \"vhid\", vh.voucherDate AS \"vDate\", ").
			append(" to_char(vh.voucherDate, 'dd-Mon-yyyy') AS \"voucherdate\", ").
			append(" vh.voucherNumber AS \"vouchernumber\", gl.glCode AS \"glcode\", " ).
			append(" coa.name AS \"name\",decode(gl.debitAmount,0,(case (gl.creditamount) when 0 then gl.creditAmount||'.00cr' when floor(gl.creditamount)    then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end ) , (case (gl.debitamount) when 0 then gl.debitamount||'.00dr' when floor(gl.debitamount)    then gl.debitamount||'.00dr' else  gl.debitamount||'dr' 	 end )) AS \"amount\", " ).
			append(" gl.description AS \"narration\", vh.type || '-' || vh.name||DECODE(status,1,'(Reversed)',decode(status,2,'(Reversal)','')) AS \"type\", " ).
			append(" gl.debitamount  AS \"debitamount\", gl.creditamount  AS \"creditamount\",f.name as \"fundName\",  vh.isconfirmed as \"isconfirmed\",gl.functionid as \"functionid\",vh.type||'-'||vh.name as \"vouchertypename\" " ).
			append(" FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa, generalLedger gl1 , fund f " +addTableToQuery+"" ).
			append(" WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id AND gl1.voucherHeaderId = vh.id " ).
			append(" AND gl.voucherHeaderId = gl1.voucherHeaderId 	AND	f.id=vh.fundid " ).
			append(" AND gl1.glcode ='"+glCode1+"'" ).
			append(" AND (gl.debitamount>0 OR gl.creditamount>0) " ).append(functionCondition).
			append(" and vh.id in ("+engineQry+" )" ).
			append(" order by \"code\",\"vDate\" ");
			LOGGER.debug("____________________________________________________________"+sql.toString());
			return sql.toString();  
		}
	}

	private OpBal getOpeningBalance(final String glCode,final String fundId,final String fundSourceId,final String fyId,
										final String accEntityId,final String accEntityKey,final String tillDate,final String functionId)throws TaskFailedException{
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<OpBal>() {

			@Override
			public com.exilant.eGov.src.transactions.OpBal execute(
					Connection connection) throws SQLException {

				String fundCondition="";
				String fundSourceCondition="";
				String accEntityCondition="";
				String functionCondition="";
				double opDebit=0, opCredit=0;

				/** opening balance of the Year **/
				if(!fundId.equalsIgnoreCase("")) fundCondition="fundId = " + fundId + " AND ";
				if(!fundSourceId.equalsIgnoreCase("")) fundSourceCondition="fundSourceId = " + fundSourceId + " AND ";
				if(!accEntityId.equalsIgnoreCase("")){
					accEntityCondition = "accountDetailTypeid="+accEntityId+" AND accountDetailKey="+accEntityKey +" AND ";
				}
				if(!StringUtils.isEmpty(functionId))
					functionCondition = " functionid="+functionId+" AND ";
				String queryYearOpBal = "SELECT decode(sum(openingDebitBalance),null,0,sum(openingDebitBalance)) AS \"openingDebitBalance\", " +
										"decode(sum(openingCreditBalance),null,0,sum(openingCreditBalance)) AS \"openingCreditBalance\" " +
										"FROM transactionSummary WHERE "+fundCondition + fundSourceCondition + functionCondition + accEntityCondition +" financialYearId="+fyId+" " +
										"AND glCodeId = (SELECT id FROM chartOfAccounts WHERE glCode in('"+ glCode +"'))";
				LOGGER.info("**********************: OPBAL: " + queryYearOpBal);
				try{
					statement=connection.createStatement();
					resultset = null;
					resultset = statement.executeQuery(queryYearOpBal);
					
					if(resultset.next())
					{
						opDebit = resultset.getDouble("openingDebitBalance");
						opCredit = resultset.getDouble("openingCreditBalance");
					}
									
					statement.close();
				   }catch(SQLException ex){
					   LOGGER.error("Error GeneralLedger->getOpeningBalance() For the year: " + ex.toString());
					  
				    }
				   
				/** opening balance till the date from the start of the Year **/
					String startDate=cmnFun.getStartDate(Integer.parseInt(fyId));
					if(!fundId.equalsIgnoreCase("")) fundCondition="AND vh.fundId = " + fundId + " ";
					if(!fundSourceId.equalsIgnoreCase("")) fundSourceCondition="AND vh.fundId = " + fundSourceId + " ";
					if(!StringUtils.isEmpty(functionId))
						functionCondition = " and gl.functionid="+functionId+" ";
					String queryTillDateOpBal="";
					String defaultStatusExclude=null;
					List<AppConfigValues> listAppConfVal=new AppConfigValuesHibernateDAO(AppConfigValues.class, HibernateUtil.getCurrentSession()).
					getConfigValuesByModuleAndKey("finance","statusexcludeReport");
					if(null!= listAppConfVal)
					{
						defaultStatusExclude = ((AppConfigValues)listAppConfVal.get(0)).getValue();
					}
					else
					{
						throw new EGOVRuntimeException("Exlcude statusses not  are not defined for Reports");
					}
					
					if(!accEntityId.equalsIgnoreCase("") && !accEntityKey.equalsIgnoreCase("") ) 
					{
						//addGldtlTableToQuery=", generalledgerdetail gldet ";
						accEntityCondition=" AND gl.id=gldet.generalledgerid  AND gldet.detailtypeid=" + accEntityId + " AND gldet.detailkeyid=" + accEntityKey +" ";
						
						queryTillDateOpBal = "SELECT coa.glcode,(SELECT SUM(gldet.amount) FROM generalLedger gl, voucherHeader vh , generalledgerdetail gldet " 
							+" WHERE vh.id = gl.voucherHeaderId AND gl.glcodeid IN (coa.id) " +fundCondition + fundSourceCondition + functionCondition + accEntityCondition 
							+" AND vh.voucherDate >= to_date('"+startDate+"','dd/mm/yyyy')  AND vh.voucherDate < to_date('"+tillDate+"','dd/mm/yyyy') AND vh.status<>4 AND "
							+" gl.DEBITamount>0) AS \"debitAmount\",(SELECT SUM(gldet.amount) FROM generalLedger gl, voucherHeader vh , " 
							+" generalledgerdetail gldet WHERE vh.id = gl.voucherHeaderId AND "
							+" gl.glcodeid IN (coa.id) "+fundCondition + fundSourceCondition + functionCondition + accEntityCondition +" AND vh.voucherDate >= to_date('"+startDate+"','dd/mm/yyyy') AND vh.voucherDate < to_date('"+tillDate+"','dd/mm/yyyy') AND vh.status not in ("+defaultStatusExclude+") AND"
							+" gl.CREDITamount>0) AS \"creditAmount\" FROM chartofaccounts coa WHERE 	coa.glcode IN ('"+ glCode +"')"; 

					}
					else
					{
					//	if(showRev.equalsIgnoreCase("on")){
			
							queryTillDateOpBal = "SELECT decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) AS \"debitAmount\", "  
												+" decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount)) AS \"creditAmount\" "
												+" FROM generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCode IN ('"+ glCode +"')"
												+" "+fundCondition + fundSourceCondition + functionCondition +" AND vh.voucherDate >= to_date('"+startDate+"','dd/mm/yyyy') AND vh.voucherDate < to_date('"+tillDate+"','dd/mm/yyyy') AND vh.status not in ("+defaultStatusExclude+")";
					}
					LOGGER.info("***********: OPBAL: " + queryTillDateOpBal);
					try{
						 statement=connection.createStatement();
						resultset = null;
						resultset = statement.executeQuery(queryTillDateOpBal);
						if(resultset.next()){
							if(resultset.getString("debitAmount")!=null)
								opDebit = opDebit + resultset.getDouble("debitAmount");
							if(resultset.getString("creditAmount")!=null)
							opCredit = opCredit + resultset.getDouble("creditAmount");
						}
						statement.close();
					}catch(SQLException ex){
						LOGGER.error("Error GeneralLedger->getOpeningBalance() till the date: " + ex.toString());
						
					}
				OpBal opBal = new OpBal();
				opBal.dr = opDebit;
				opBal.cr = opCredit;
				resultset = null;
				return opBal;
			
				
			}
		});
		
		
	}


	
	
	private void setDates(final String startDate1, final String endDate1 ) throws ValidationException{
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				

				ResultSet rs = null;
				ResultSet rs1 = null;
				String formstartDate="";
				String formendDate="";
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				
				try
		   		{
					formstartDate = sdf.format(formatter1.parse(startDate1));
		   		}
		   		catch(Exception e){	}

		   		try{
		   			formendDate = sdf.format(formatter1.parse(endDate1 ));
		   		}
		   		catch(Exception e){}
		   		startDate = formstartDate;
				endDate = formendDate;
				if((startDate == null || startDate.equalsIgnoreCase("")) && (endDate == null || endDate.equalsIgnoreCase("")))
				{
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
				if((startDate == null || startDate.equalsIgnoreCase("")) && (endDate != null &&  !endDate.equalsIgnoreCase("")))
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
							throw new ValidationException(
									Arrays.asList(new ValidationError("accountName", ex.getMessage())));
						}
				}

				if((endDate == null || endDate.equalsIgnoreCase("")) && (startDate != null && !startDate.equalsIgnoreCase("")))
				{
						statement = connection.createStatement();
						String query = "SELECT TO_CHAR(endingDate, 'dd-Mon-yyyy') AS \"endingDate\" " +
						"FROM financialYear WHERE startingDate <= '"+startDate+"' AND endingDate >= '"+startDate+"'";
						rs = statement.executeQuery(query);
			//			if(rs.next()) endDate = rs.getString("endingDate");
						rs.close();
						statement.close();
						statement = null;
				}
				
			}
		});
		
	}

    private String getAccountName(final String glCode)throws ValidationException
    {
    	
    	return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {

		        String accountName="";
		       
		        Statement statement=null;
		        ResultSet resultset=null;
		         try
		         {
		            
		             statement=connection.createStatement();
		             String query="select glcode as \"glcode\" ,name as \"name\" from  CHARTOFACCOUNTS where GLCODE='"+glCode+"'";
		             resultset=statement.executeQuery(query);
		             if (resultset!=null && resultset.next())
		             {
		                accountName=resultset.getString("glcode")+" : "+resultset.getString("name");
		             }
		             resultset.close();
		             statement.close();
		         }
		         catch(Exception e)
		         {
		            LOGGER.error("Exp in getAccountName:"+e.getMessage());
		            throw new ValidationException(
							Arrays.asList(new ValidationError("accountName", e.getMessage())));
		         }
		        
		       return accountName;
		    
			}
		});
    	
    }
    private String getFundName(final String fundId)throws ValidationException
    {
       return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

		@Override
		public String execute(Connection connection) throws SQLException {
			
			 String fundName="";
		       
		        Statement statement=null;
		        ResultSet resultset=null;
		         try
		         {
		             
		             statement=connection.createStatement();
		             String query="select name as \"name\" from fund where id='"+fundId+"'";
		             resultset=statement.executeQuery(query);
		             if (resultset!=null && resultset.next())
		             {
		                fundName=resultset.getString("name");
		             }
		             statement.close();
		         }
		         catch(Exception e)
		         {
		            LOGGER.error("Exp in getFundName"+e.getMessage());
		            throw new ValidationException(
							Arrays.asList(new ValidationError("fundName", e.getMessage())));
		         }
		        return fundName;
			
		}
	});
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
	 public void isCurDate(Connection conn,String VDate) throws TaskFailedException
     {

			EGovernCommon egc=new EGovernCommon();
			try{
				String today=egc.getCurrentDate(conn);
				String[] dt2 = today.split("/");
				String[] dt1= VDate.split("/");

				int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
				if(ret==-1 ){
					throw taskExc;
				}

			}catch(Exception ex){
				LOGGER.error("Exception in isCurDate():"+ex);
				throw new TaskFailedException("Date Should be within the today's date");
			}

		}
}
