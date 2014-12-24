/*
 * Created on Dec 21, 2005
 * @author Sumit
 */
package com.exilant.eGov.src.reports;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.ExilPrecision;
import com.exilant.exility.common.TaskFailedException;

class OpBal{
	public double dr;
	public double cr;
}
public class GeneralLedgerReportList {

	Statement statement=null;
	ResultSet resultset=null;
	ResultSet resultset1=null;
	TaskFailedException taskExc;
	String startDate, endDate, effTime,rType="gl";
	private static final Logger LOGGER = Logger.getLogger(GeneralLedgerReportList.class);
//	private final static String datasource = "java:/ezgovOraclePool";
	public GeneralLedgerReportList(){}

	public LinkedList getGeneralLedgerList(final GeneralLedgerReportBean reportBean)
	throws TaskFailedException{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<LinkedList>() {

			@Override
			public LinkedList execute(Connection connection) throws SQLException {

				LinkedList dataList = new LinkedList();
				
				try	{ statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);}
				catch(Exception e){
					LOGGER.error("Exception in creating statement:"+statement);
					
				}
				String isconfirmed="";
				String glCode1="";
				String glCode2="";
				taskExc = new TaskFailedException();
				EGovernCommon egc=new EGovernCommon();

					NumberFormat formatter = new DecimalFormat();
					formatter = new DecimalFormat("###############.00");

					glCode1 = reportBean.getGlCode1();
					glCode2 = reportBean.getGlCode2();
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
				catch(Exception ex){
					//dc.addMessage("eGovFailure","Snapshoterror");
					
				}

				//showRev=reportBean.getForRevEntry();
				//LOGGER.debug("showRev "+showRev);
				String fundId = reportBean.getFund_id();
				String fundSourceId = reportBean.getFundSource_id();

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
		   		}

				catch(Exception e){
					LOGGER.error("inside the try-startdate"+e);
				}

				if(startDate.equalsIgnoreCase("null")){
				String finId=getFYID(formendDate);
				startDate=EGovernCommon.getStartDate(finId);
				}
		   		else{
		   			startDate = formstartDate;
		   		}
				//LOGGER.debug("startDate22 "+startDate);
				endDate = formendDate;
				String startDateformat = startDate;
				String startDateformat1 = "";
				try{
				dt = formatter1.parse(startDateformat);
				startDateformat1 = sdf.format(dt);
				}
				catch(Exception e){
					LOGGER.error("Parse Exception"+e);
				}
				setDates(startDate,endDate);
				String fyId = getFYID(endDate);
				if(fyId.equalsIgnoreCase("")){
					LOGGER.error("Financial Year Not Valid");
					throw new ValidationException(
							Arrays.asList(new ValidationError("FinancialYear","Financial Year Not Valid")));
				}


				/*if(!isValidCode(glCode1))
				{
					LOGGER.info(glCode1+" Not Valid");
					EgovDatabaseManager.releaseConnection(connection,statement);
					throw taskExc;
				}
				if(!isValidCode(glCode2))
				{
					LOGGER.info(glCode2+" Not Valid");
					EgovDatabaseManager.releaseConnection(connection,statement);
					throw taskExc;
				}*/


				double txnDrSum=0, txnCrSum=0, openingBalance=0, closingBalance = 0;
				String query = getQuery(glCode1,glCode2,fundId, fundSourceId, startDate, endDate);
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
					//statement.close();
					ArrayList data = new ArrayList();
					String accCode="", vcNum="", vcDate="", type="", narration="";
					StringBuffer detail = new StringBuffer();
					StringBuffer amount = new StringBuffer();
					int vhId = 0, curVHID = 0 , cout=0 , cout1=0,VhidPrevious=0,lenAfterAppend=0,lenBeforeAppend=0,lenDetailBefore=0,lenDetailAfter=0;
					double txnDebit=0, txnCredit=0,previousDebit=0,previousCredit=0;
					String code="",currCode="",accCodePrevious="";
					/**
					 * When using ResultSet.TYPE_SCROLL_INSENSITIVE in createStatement
					 * if no records are there, rs.next() will return true
					 * but when trying to access (rs.getXXX()), it will throw an error
					 **/
					//	nameOfGrid = "gridBookDetail";
						//int cnt = 0;
						int totalCount=0, isConfirmedCount=0;
						String vn2="";
						String glType="";
						while(resultset1.next())  
						{
							//LOGGER.debug(" inside resultset");
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
								/**
								 * GLCODES changing here.Paint the previous GLCODE particular txn and the clsoing balance.
								 * Dnt take the cout1 = 1 record.
								 */
								if(!currCode.trim().equals("") && (!code.equals(currCode)) && cout1==0)
								{
									cout=1;
									String arrl[] = new String[13]; //12th item in array is used for chequeinhand report to display cheque-payeename list
									arrl[0] = "&nbsp;"+vcDate;
									arrl[1] = vcNum;
									arrl[2] = detail.toString();
									arrl[3] = "&nbsp;"+type;
									arrl[4] = numberToString(((Double)txnDebit).toString())+"";
									arrl[5] = numberToString(((Double)txnCredit).toString())+"";
									if(narration == null)
										arrl[6] ="&nbsp;";
									else							
										arrl[6] = "&nbsp;"+narration;
									txnDrSum = txnDrSum + txnDebit;
									txnCrSum = txnCrSum + txnCredit;
									arrl[8]=arrl[9]="&nbsp;";
									arrl[4] = arrl[4].equalsIgnoreCase(".00") ? "&nbsp;" : arrl[4];
									arrl[5] = arrl[5].equalsIgnoreCase(".00") ? "&nbsp;" : arrl[5];
									if(amount.length()>2)
									arrl[7] = amount.toString().substring(0,amount.length()-4);
									else
									arrl[7]="&nbsp;";

									// To show running Balance
									if(reportBean.getReportType().equalsIgnoreCase("generalledger") && reportBean.getForRunningBalance().equalsIgnoreCase("on") && (glType.equalsIgnoreCase("A") || glType.equalsIgnoreCase("E")) )
									{
										//LOGGER.debug(" getReportType().equalsIgnoreCase(generalledger) ");

										double runningBal=openingBalance+txnDrSum-txnCrSum;
										arrl[10]=formatter.format(runningBal)+"";
										//LOGGER.debug(" arrl[10] "+arrl[10]);
									}
									if(reportBean.getReportType().equalsIgnoreCase("generalledger") && reportBean.getForRunningBalance().equalsIgnoreCase("on") && (glType.equalsIgnoreCase("I") || glType.equalsIgnoreCase("L")) )
									{
										//LOGGER.debug(" getReportType().equalsIgnoreCase(generalledger) ");

										double runningBal=(-openingBalance)+txnCrSum-txnDrSum;
										arrl[10]=formatter.format(runningBal)+"";
										//LOGGER.debug(" arrl[10] "+arrl[10]);
									}
									arrl[11]="&nbsp;";
									arrl[12]="&nbsp;";
								// End

									data.add(arrl);//**
									detail.delete(0, detail.length());
									amount.delete(0,amount.length());
									//cnt = 0;
									vcDate = vcNum = accCode = type = narration = "";
									String arr[] = new String[13];
									if(openingBalance > 0)
									txnDrSum = txnDrSum + Math.abs(openingBalance);
									else
									txnCrSum = txnCrSum + Math.abs(openingBalance);
									closingBalance = txnDrSum - txnCrSum;
									//LOGGER.debug("Opening Balance Dr-Cr: " + formatter.format(openingBalance));
									//LOGGER.debug("Closing Balance Dr-Cr: " + formatter.format(closingBalance));
									if(closingBalance > 0){
									txnCrSum = txnCrSum + Math.abs(closingBalance);
									arr[4]="&nbsp;";
									}else if(closingBalance < 0){
									txnDrSum = txnDrSum + Math.abs(closingBalance);
									arr[4]=numberToString(((Double)Math.abs(closingBalance)).toString()).toString();
									arr[5]="&nbsp;";
									}else{
									arr[4]="&nbsp;";
									arr[5]="&nbsp;";
									}
									arr[2]="Closing Balance";
		                            arr[1]="";
									arr[0]=arr[3]=arr[6]=arr[7]=arr[8]=arr[9]=arr[10]=arr[11]=arr[12]="&nbsp;";
									data.add(arr);

								}
								if(!currCode.trim().equals("")  && (!code.equals(currCode)) && cout1==0)
								{
								  	String arr[] = new String[13];
									if(txnDrSum > 0)
									arr[4]="<b>"+numberToString(((Double)txnDrSum).toString())+"</b>";
									else
									arr[4]="&nbsp;";
									if(txnCrSum > 0)
									arr[5]="<b>"+numberToString(((Double)txnDrSum).toString())+"</b>";
									else
									arr[5]="&nbsp;";
									arr[2]="<b>Total</b>";
		                            arr[1]="";
									arr[0]=arr[3]=arr[6]=arr[7]=arr[8]=arr[9]=arr[10]=arr[11]=arr[12]="&nbsp;";
									data.add(arr);
									txnDrSum=0;txnCrSum=0;

								}
								cout1=0;
								vhId = resultset1.getInt("vhid");
								/**
								 * When the main GLCODES are changing.We need to get the opening balance first.
								 */
								if(!code.equals(currCode))
								{
									glType=resultset1.getString("glType");
									//LOGGER.debug(" inside code.equals(currCode)");
									String arr[] = new String[13];
										OpBal opbal = getOpeningBalance(code, fundId,fundSourceId, fyId, startDate);
										openingBalance = opbal.dr - opbal.cr;
										String fundName="";
										if (resultset1.getString(14) != null )
										{
											fundName=resultset1.getString(14);
										}
										statement=connection.createStatement();
										ResultSet res=statement.executeQuery("select name as \"glname\" from chartofaccounts where glcode='"+code+"'");
										String aName="";
										if(res.next())
										{
											if(res.getString("glname") !=null)
											{
											aName=res.getString("glname");
											}
										}
										else
											LOGGER.debug("In else block of getting glname");
										res.close();
										statement.close();
		                                arr[1]="";
										arr[0]=arr[2]=arr[3]=arr[6]=arr[7]=arr[10]=arr[11]=arr[12]="&nbsp;";
										if(vhId == 0)
										arr[8]="&nbsp;";
										else
										arr[8]=fundName;
										arr[9]=code+"-"+aName;
										if(openingBalance > 0){
										arr[4]=numberToString(((Double)Math.abs(openingBalance)).toString()).toString();
										arr[5]="&nbsp;";
										}
										else if(openingBalance < 0){
											arr[4]="&nbsp;";
											arr[5]=numberToString(((Double)Math.abs(openingBalance)).toString()).toString();
											}else{
										arr[4]="&nbsp;";
										arr[5]="&nbsp;";
										}
										arr[2]="Opening Balance";
										if(vhId == 0 && !(openingBalance > 0 || openingBalance < 0))
										{LOGGER.debug("Inside if..."); }
										else
										data.add(arr);
										/**
										 * Those glcodes for which there is no transaction in the
										 * general ledger for the date range.We are getting the closing balance.
										 * cout1 is set to 1.
										 */
										if(vhId == 0)
										{
											String arr1[] = new String[13];
											if(openingBalance > 0)
											{
											arr1[4]="&nbsp;";
											arr1[5]=numberToString(((Double)Math.abs(openingBalance)).toString()).toString();
											}
											else if(openingBalance < 0){
											arr1[4]=numberToString(((Double)Math.abs(openingBalance)).toString()).toString();
											arr1[5]="&nbsp;";
											}else{
											arr1[4]="0";
											arr1[5]="0";
											}
											arr1[2]="Closing Balance";
		                                    arr1[1]="";
											arr1[0]=arr1[3]=arr1[6]=arr1[7]=arr1[8]=arr1[9]=arr1[10]=arr1[11]=arr1[12]="&nbsp;";
											if(openingBalance > 0 || openingBalance < 0)
											data.add(arr1);

											String arr2[] = new String[13];
											if(openingBalance > 0)
											{
											arr2[4]=numberToString(((Double)Math.abs(openingBalance)).toString()).toString();
											arr2[5]=numberToString(((Double)Math.abs(openingBalance)).toString()).toString();
											}
											else if(openingBalance < 0)
											{
											arr2[4]=numberToString(((Double)Math.abs(openingBalance)).toString()).toString();
											arr2[5]=numberToString(((Double)Math.abs(openingBalance)).toString()).toString();
											}else{
											arr2[4]="0";
											arr2[5]="0";
											}
											arr2[2]="<b>Total</b>";
		                                    arr2[1]="";
											arr2[0]=arr2[3]=arr2[6]=arr2[7]=arr2[8]=arr2[9]=arr2[10]=arr2[11]=arr2[12]="&nbsp;";
											if(openingBalance > 0 || openingBalance < 0)
											data.add(arr2);
											cout1=1;
											continue;
										}
										//assign to temp varable,
										currCode=code;
									}//End If glcodes changing

							}
							catch(SQLException ex){
								LOGGER.error("ERROR (not an error): ResultSet is Empty");
							}
							//Vouchers are changing
							if(curVHID >0 && vhId != curVHID && cout==0 && vhId != 0)
							{
								//LOGGER.debug("  reportBean.getReportType()  "+reportBean.getReportType());
								previousDebit=0;
								previousCredit=0;
								lenAfterAppend=0;lenBeforeAppend=0;lenDetailBefore=0;lenDetailAfter=0;
								String arr9[] = new String[13];
								arr9[0] = vcDate;
								arr9[1] = vcNum;
								String cheList="";
							/*		if(narration!=null && !narration.equalsIgnoreCase("") &&  narration.trim().length()>0)
									{
										//LOGGER.debug("   @@@@@@@@@@  cheList &&");
										cheList=" - ";
									}
							*/	if(reportBean.getReportType().equalsIgnoreCase("ChequeInHand"))
								{
									//LOGGER.debug("   @@@@@@@@@@  inside");
									Statement s1=null;
									ResultSet r1=null;
								try{

								s1=connection.createStatement();
								r1 = s1.executeQuery("select distinct chequenumber,payto from chequedetail c,voucherheader v where v.id=c.voucherheaderid and v.status<>4 and v.vouchernumber='"+vcNum+"'  and v.voucherdate='"+vcDate+"'  ");
								
//								String temp="";
								while(r1.next())
								{
									//LOGGER.debug("   @@@@@@@@@@  inside while");
									cheList=cheList+"<br>"+r1.getString(1)+((r1.getString(2)==null || r1.getString(2).equals(""))?", ":"-"+r1.getString(2)+", ");

								}
								//if(temp.length()>0)
									//cheList = cheList+"Cheque# :"+temp;
								LOGGER.info("   @@@@@@@@@@  cheList  "+cheList);
								}

								catch(Exception e)
								{
									LOGGER.debug("Exp="+e.getMessage());

								}
								finally
								{

									r1.close();
									s1.close();
								}
								if(!cheList.trim().equals("")) cheList=cheList.trim().substring(0,cheList.length()-1);
								}
								arr9[2] = detail.toString();
								arr9[3] = type;
								arr9[4] = numberToString(((Double)txnDebit).toString())+"";
								arr9[5] = numberToString(((Double)txnCredit).toString())+"";
								if(reportBean.getReportType().equalsIgnoreCase("ChequeInHand")){
									arr9[6] ="&nbsp;"+narration;
									arr9[12] = "&nbsp;"+cheList;
								}else{
									if(narration != null)
										arr9[6] ="&nbsp;"+narration+" - Cheque#:"+cheList;
									else
										arr9[6] ="&nbsp;";
									arr9[12] = "&nbsp;";
								}
								if(amount.length()>2)
								arr9[7] = amount.toString().substring(0,amount.length()-4);
								else
								arr9[7]="&nbsp;";

								txnDrSum = txnDrSum + txnDebit;
								txnCrSum = txnCrSum + txnCredit;

								// To show running Balance
									if(reportBean.getReportType().equalsIgnoreCase("generalledger") && reportBean.getForRunningBalance().equalsIgnoreCase("on") && (glType.equalsIgnoreCase("A") || glType.equalsIgnoreCase("E")) )
									{
										//LOGGER.debug(" getReportType().equalsIgnoreCase(generalledger) ");

										double runningBal=openingBalance+txnDrSum-txnCrSum;
										arr9[10]=numberToString(((Double)runningBal).toString())+"";
									//	LOGGER.debug(" arr9[10] "+arr9[10]);
									}
									if(reportBean.getReportType().equalsIgnoreCase("generalledger") && reportBean.getForRunningBalance().equalsIgnoreCase("on") && (glType.equalsIgnoreCase("I") || glType.equalsIgnoreCase("L")) )
									{
										//LOGGER.debug(" getReportType().equalsIgnoreCase(generalledger) ");

										double runningBal=(-openingBalance)+txnCrSum-txnDrSum;
										arr9[10]=numberToString(((Double)runningBal).toString())+"";
										//LOGGER.debug(" arr9[10] "+arr9[10]);
									}
									arr9[11]="&nbsp;";

								// End


								arr9[8]=arr9[9]="&nbsp;";
								data.add(arr9);
								arr9[4] = arr9[4].equalsIgnoreCase(".00") ? "&nbsp;" : arr9[4];
								arr9[5] = arr9[5].equalsIgnoreCase(".00") ? "&nbsp;" : arr9[5];

								detail.delete(0, detail.length());
								amount.delete(0, amount.length());
								//cnt = 0;
								vcDate = vcNum = accCode = type = narration = "";
								//LOGGER.debug("  ENDING %%%%");

							}//End If
							curVHID = vhId;
							cout=0;
							accCode = resultset1.getString("glcode");
							if(vhId != 0)
							{
								//LOGGER.debug(" inside vhId != 0");
								//get the details other than patriculars
								if(accCode.equalsIgnoreCase(code))
								{
									double currentDebit=0,currentCredit=0,debit=0,credit=0;
									if(vhId==VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious)){
										vcDate = resultset1.getString("voucherdate");
										vcNum = resultset1.getString("vouchernumber");
										type = resultset1.getString("type");
										currentDebit=resultset1.getDouble("debitamount");
										currentCredit=resultset1.getDouble("creditamount");
										debit=(previousDebit+currentDebit)-(previousCredit+currentCredit);
										if(debit>0) txnDebit=debit;
										else txnDebit=0;
										credit=(previousCredit+currentCredit)-(previousDebit+currentDebit);
										if(credit>0) txnCredit=credit;
										else txnCredit=0;
										narration = resultset1.getString("narration");
										previousDebit=currentDebit;
										previousCredit=currentCredit;
									}
									else{
									vcDate = resultset1.getString("voucherdate");
									vcNum = resultset1.getString("vouchernumber");
									type = resultset1.getString("type");
									txnDebit = resultset1.getDouble("debitamount");
									previousDebit=txnDebit;
									txnCredit = resultset1.getDouble("creditamount");
									previousCredit=txnCredit;
									narration = resultset1.getString("narration");
									}
								}else{
									if(vhId==VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious)){
										double currentDebit=0,currentCredit=0,debit=0,credit=0;
										String debitAmount="",creditAmount="";
										String bLine="<br><br>";
										String lengthName=resultset1.getString("name");
										int len=lengthName.length();
										for(int i=30;i<len;i=i+30)
										{
											bLine=bLine+"<br>";
										}
											amount.delete(lenBeforeAppend,lenAfterAppend);
											detail.delete(lenDetailBefore,lenDetailAfter);
										detail = detail.append(" " + resultset1.getString("name") + "<br><br>");
										currentDebit=resultset1.getDouble("debitamount");
										currentCredit=resultset1.getDouble("creditamount");
										debit=(previousDebit+currentDebit)-(previousCredit+currentCredit);
										if(debit>0){
											debitAmount="Dr."+ExilPrecision.convertToString(debit,2)+"0";
											amount	=amount.append(" " + debitAmount + bLine);
										}
										credit=(previousCredit+currentCredit)-(previousDebit+currentDebit);
										if(credit>0){
											creditAmount="Cr."+ExilPrecision.convertToString(credit,2)+"0";
											amount	=amount.append(" " + creditAmount + bLine);
										}

									}else{
									String bLine="<br><br>";
									String lengthName=resultset1.getString("name");
									int len=lengthName.length();
									for(int i=30;i<len;i=i+30)
										{
										bLine=bLine+"<br>";
										}
									lenDetailBefore=detail.length();
									detail = detail.append(" " + resultset1.getString("name") + "<br><br>");
									lenDetailAfter=detail.length();
									lenBeforeAppend=amount.length();
									amount	=amount.append(" " + resultset1.getString("amount") + bLine);
									lenAfterAppend=amount.length();
									previousDebit=resultset1.getDouble("debitamount");
									previousCredit=resultset1.getDouble("creditamount");
									}
								}
							}

							accCodePrevious=accCode;
							VhidPrevious=vhId;
							if(resultset1.isLast())
							{

									String arr[] = new String[13];
									arr[0] = vcDate;
									arr[1] = vcNum;
									String cheList="";
						/*		if(narration!=null && !narration.equalsIgnoreCase("") && narration.trim().length()>0)
								{
									cheList=" - ";
								}
							*/	if(reportBean.getReportType().equalsIgnoreCase("ChequeInHand"))
								{

									Statement s2=null;
									ResultSet r2=null;
								try
								{

									s2=connection.createStatement();
									r2= s2.executeQuery("select distinct chequenumber,payto from chequedetail c,voucherheader v where v.id=c.voucherheaderid and v.vouchernumber='"+vcNum+"' and v.voucherdate='"+vcDate+"' ");
//									String temp="";
									while(r2.next())
									{
										cheList=cheList+"<br>"+r2.getString(1)+((r2.getString(2)==null||r2.getString(2).equals(""))?", ":"-"+r2.getString(2)+", ");
									}
									LOGGER.info("chequelist:"+cheList);
									//if(temp.length()>0)
									//	cheList = cheList+"Cheque# :"+temp;

								}
								catch(Exception e)
								{
									LOGGER.debug("Exp="+e.getMessage());

								}
								finally
								{

									r2.close();
									s2.close();
								}
								if(!cheList.trim().equals("")) cheList=cheList.trim().substring(0,cheList.length()-1);
								}
									arr[2] = detail.toString();
									arr[3] = type;
									arr[4] = numberToString(((Double)txnDebit).toString())+"";
									arr[5] = numberToString(((Double)txnCredit).toString())+"";
									if(reportBean.getReportType().equalsIgnoreCase("ChequeInHand")){
										arr[6] ="&nbsp;"+narration;
										arr[12] = "&nbsp;"+cheList;
									}else{
										if(narration != null)
											arr[6] ="&nbsp;"+narration+" - Cheque#:"+cheList;
										else
											arr[6] ="&nbsp;";
										arr[12] = "&nbsp;";
									}

									txnDrSum = txnDrSum + txnDebit;
									txnCrSum = txnCrSum + txnCredit;
									arr[8]=arr[9]="&nbsp;";
									arr[4] = arr[4].equalsIgnoreCase(".00") ? "&nbsp;" : arr[4];
									arr[5] = arr[5].equalsIgnoreCase(".00") ? "&nbsp;" : arr[5];
									if(amount.length()>2)
									arr[7] = amount.toString().substring(0,amount.length()-4);
									else
									arr[7]="&nbsp;";

									// To show running Balance
									if(reportBean.getReportType().equalsIgnoreCase("generalledger") && reportBean.getForRunningBalance().equalsIgnoreCase("on") && (glType.equalsIgnoreCase("A") || glType.equalsIgnoreCase("E")) )
									{
										double runningBal=openingBalance+txnDrSum-txnCrSum;
										arr[10]=numberToString(((Double)runningBal).toString())+"";
										//LOGGER.debug(" arr[10] "+arr[10]);
									}
									if(reportBean.getReportType().equalsIgnoreCase("generalledger") && reportBean.getForRunningBalance().equalsIgnoreCase("on") && (glType.equalsIgnoreCase("I") || glType.equalsIgnoreCase("L")) )
									{
										double runningBal=(-openingBalance)+txnCrSum-txnDrSum;
										arr[10]=numberToString(((Double)runningBal).toString())+"";
										//LOGGER.debug(" arr[10] "+arr[10]);
									}
									arr[11]="&nbsp;";

								// End
									data.add(arr);
									detail.delete(0, detail.length());
									amount.delete(0,amount.length());
									//cnt = 0;
									vcDate = vcNum = accCode = type = narration = "&nbsp;";

								String arr2[] = new String[13];
								if(openingBalance > 0)
								txnDrSum = txnDrSum + Math.abs(openingBalance);
								else
								txnCrSum = txnCrSum + Math.abs(openingBalance);
								closingBalance = txnDrSum - txnCrSum;
								if(closingBalance > 0){
								txnCrSum = txnCrSum + Math.abs(closingBalance);
								arr2[4]="&nbsp;";
								arr2[5]=numberToString(((Double)Math.abs(closingBalance)).toString()).toString();
								}else if(closingBalance < 0){
								txnDrSum = txnDrSum + Math.abs(closingBalance);
								arr2[4]=numberToString(((Double)Math.abs(closingBalance)).toString()).toString();
								arr2[5]="&nbsp;";
								}else{
								arr2[4]="&nbsp;";
								arr2[5]="&nbsp;";
								}
								arr2[2]="Closing Balance";
		                        arr2[1]="";
								arr2[0]=arr2[3]=arr2[6]=arr2[7]=arr2[8]=arr2[9]=arr2[10]=arr2[11]=arr2[12]="&nbsp;";
								data.add(arr2);


								String arr1[] = new String[13];
								if(txnDrSum > 0)
								arr1[4]="<hr><b>"+numberToString(((Double)txnDrSum).toString())+"</b><hr>";
								else
								arr1[4]="<hr>&nbsp;<hr>";
								if(txnCrSum > 0)
								arr1[5]="<hr><b>"+numberToString(((Double)txnDrSum).toString())+"</b><hr>";
								else
								arr1[5]="<hr>&nbsp;<hr>";
								arr1[2]="<hr><b>Total</b><hr>";
								arr1[0]=arr1[1]=arr1[3]=arr1[6]=arr1[7]=arr1[8]=arr1[9]=arr1[10]=arr1[11]=arr1[12]="<hr>&nbsp;<hr>";
		                       	data.add(arr1);
								txnDrSum=0;txnCrSum=0;



							}//End If last
						}//End While


					//Adding data to 2 dimension array to pass to dc
					String gridData[][] = new String[data.size()+1][13];
					gridData[0][0] = "voucherdate";
					gridData[0][1] = "vouchernumber";
					//gridData[0][2] = "glcode";
					gridData[0][2] = "name";
					gridData[0][3] = "type";
					gridData[0][4] = "debitamount";
					gridData[0][5] = "creditamount";
					gridData[0][6] = "narration";
					gridData[0][7] = "amount";
					gridData[0][8] = "fund";
					gridData[0][9] = "glcode";
					gridData[0][10] = "debitamountR";
					gridData[0][11] = "creditamountR";
					gridData[0][12] = "chequeslist-payeename";//for chequeinhand report
					for(int i=1; i<=data.size(); i++){
						gridData[i] = (String[])data.get(i-1);
					}


					//dc.addGrid(nameOfGrid, gridData);
					//LOGGER.debug("\n\nGRID DATA:");
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
					//	String testTempSubstring = testTemp.substring(0,counter);
						generalLedgerBean.setName(gridData[i][2]);
						String test = gridData[i][7];
						char testArray[] = test.toCharArray();

//						int flag = 0;
						for(counter=0;counter<testArray.length;counter++)
						{
							if (testArray[counter]== 'r')
							{
			//					flag = 1;
								break;
							}
						}

			//			String testSubstring = test.substring(0,counter);

					  generalLedgerBean.setAmount(gridData[i][7]);
						generalLedgerBean.setNarration(gridData[i][6]);
						generalLedgerBean.setType(gridData[i][3]);
						generalLedgerBean.setDebitamount(gridData[i][4]);
						generalLedgerBean.setCreditamount(gridData[i][5]);
						generalLedgerBean.setFund(gridData[i][8]);
		                if(i==data.size())
		                    generalLedgerBean.setCGN(getCGN("",""));
		                else
						  generalLedgerBean.setCGN(getCGN(gridData[i][1],gridData[i][0]));
						generalLedgerBean.setRunningDrCr(gridData[i][10]);
						//generalLedgerBean.setRunningCr(gridData[i][11]);
						//LOGGER.debug("  gridData[i][10]  "+gridData[i][10]);
						generalLedgerBean.setCheques(gridData[i][12]);
						reportBean.setStartDate(startDateformat1);
						reportBean.setTotalCount(Integer.toString(totalCount));
						reportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));
						dataList.add(generalLedgerBean);
					}

				}catch(SQLException ex){
					LOGGER.error("ERROR: " + ex.toString());
					
				}catch(Exception ex){
					LOGGER.error("EXP="+ex.getMessage());
					
				}
				HibernateUtil.release(statement, resultset1);
				return dataList;
				
			}
			
		});
	}

	private String getQuery(String glCode1,String glCode2,
								String fundId,
								String fundSourceId,
								String startDate,
								String endDate){
		String fundCondition="";
		String fundSourceCondition="";
		String addTableToQuery="";

		if(!fundId.equalsIgnoreCase("")) fundCondition="AND vh.fundId = " + fundId + " ";
		if(!fundSourceId.equalsIgnoreCase("")) 
		{
			addTableToQuery=", vouchermis vmis ";
			fundSourceCondition="AND vmis.voucherheaderid=vh.id AND vmis.fundSourceId = " + fundSourceId + " ";
		}


			return	"SELECT distinct gl1.glcode as \"code\",(select ca.type from chartofaccounts ca where glcode=gl1.glcode) as \"glType\",vh.id AS \"vhid\", vh.voucherDate AS \"vDate\", " +
					"to_char(vh.voucherDate, 'dd-Mon-yyyy') AS \"voucherdate\", " +
					"vh.voucherNumber AS \"vouchernumber\", gl.glCode AS \"glcode\", " +
					"coa.name AS \"name\",decode(gl.debitAmount,0,(case (gl.creditamount) when 0 then gl.creditAmount||'.00cr' when floor(gl.creditamount)    then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end ) , (case (gl.debitamount) when 0 then gl.debitamount||'.00dr' when floor(gl.debitamount)    then gl.debitamount||'.00dr' else  gl.debitamount||'dr' 	 end )) AS \"amount\", " +
					"gl.description AS \"narration\", vh.type || '-' || vh.name||DECODE(status,1,'(Reversed)',decode(status,2,'(Reversal)','')) AS \"type\", " +
					"gl.debitamount  AS \"debitamount\", gl.creditamount  AS \"creditamount\",f.name as \"fundName\",  vh.isconfirmed as \"isconfirmed\"  " +
					"FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa, generalLedger gl1, fund f " + addTableToQuery+
					"WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id AND gl.voucherHeaderId = vh.id " +
					"AND gl.voucherHeaderId = gl1.voucherHeaderId AND f.id=vh.fundId " + effTime +
					"AND gl1.glcode >='"+glCode1+"' AND gl1.glcode <='"+glCode2+"'" + fundCondition + fundSourceCondition +
					"AND vh.voucherDate >= '"+startDate+"' AND vh.voucherDate <= '"+endDate+"' " +
                    "AND vh.status <> 4 " +
					"AND (gl.debitamount>0 OR gl.creditamount>0)  " +
					"union "+
					"SELECT coa.glcode as \"code\",coa.type as \"glType\", 0 as  \"vhid\", to_date('') AS \"vDate\", "+
					"'' AS \"voucherdate\", '' AS \"vouchernumber\", "+
					"'' AS \"glcode\", coa.name AS \"name\",null  AS \"amount\", '' AS \"narration\", "+
					"'' AS \"type\", null   AS \"debitamount\",  null  AS \"creditamount\",'' as \"fundName\",  9 as \"isconfirmed\" FROM  chartOfAccounts coa WHERE "+
					" coa.glCode not in(select glcode from generalledger gl,voucherheader vh where gl.VOUCHERHEADERID=vh.id AND vh.status <> 4 AND vh.voucherDate >= '"+startDate+"' AND vh.voucherDate <= '"+endDate+"') and glcode >='"+glCode1+"' AND glcode <='"+glCode2+"' order by \"code\",\"vDate\" ";

		
	}
	private String getFYID(final String sDate){
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				String fyId="";
				try{
					//for accross the financial year
					statement=connection.createStatement();
					resultset = statement.executeQuery("SELECT id FROM financialYear " +
							"WHERE startingDate<='"+sDate+"' AND endingDate>='"+sDate+"'");
					if(resultset.next()) fyId = resultset.getString("id");
					resultset .close();
					statement.close();
				}catch(SQLException ex){
					fyId="";
					LOGGER.error("Error GeneralLedger->getFYID(): " + ex.toString());
				}
				//LOGGER.debug("fyId: " + fyId);
				return fyId;
			}
		});
	}
	
	private OpBal getOpeningBalance(final String glCode,
										final String fundId,
										final String fundSourceId,
										final String fyId,
										final String tillDate	)throws TaskFailedException{
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<OpBal>() {

			@Override
			public OpBal execute(Connection connection) throws SQLException {

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
				//LOGGER.debug("***********: OPBAL: " + queryYearOpBal);
				try{
					statement=connection.createStatement();
					resultset = null;
					resultset = statement.executeQuery(queryYearOpBal);

					if(resultset.next()){
						opDebit = resultset.getDouble("openingDebitBalance");
						opCredit = resultset.getDouble("openingCreditBalance");
					}
					statement.close();
				   }catch(SQLException ex){
					LOGGER.error("Error GeneralLedger->getOpeningBalance() For the year: " + ex.toString());
					
				      }



				/** opening balance till the date from the start of the Year **/  
				if(rType.equalsIgnoreCase("gl"))
				{
					String startDate=EGovernCommon.getStartDate(fyId);
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

					LOGGER.info("***********: OPBAL: " + queryTillDateOpBal);
					try{
						 statement=connection.createStatement();
						resultset = null;
						resultset = statement.executeQuery(queryTillDateOpBal);
						if(resultset.next()){
							opDebit = opDebit + resultset.getDouble("debitAmount");
							opCredit = opCredit + resultset.getDouble("creditAmount");
						}
						statement.close();
					}catch(SQLException ex){
					LOGGER.error("Error GeneralLedger->getOpeningBalance() till the date: " + ex.toString());
					
					}
				}
				/*********************************/


				OpBal opBal = new OpBal();
				opBal.dr = opDebit;
				opBal.cr = opCredit;
				HibernateUtil.release(statement, resultset);
				resultset = null;
				return opBal;
			
			}
			
			
		});
	}


	/*private String getStartDate(String fyId){

		String startDate="";
		resultset = null;
		try{
			 statement=connection.createStatement();
			resultset = statement.executeQuery("SELECT to_char(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE id = "+fyId);
			if(resultset.next())	startDate = resultset.getString("startingDate");
			else
				LOGGER.debug("Inside else ...");
			statement.close();
		}catch(SQLException ex){
			startDate = "";
			LOGGER.error("Error GeneralLedger->getStartDate(): " + ex.toString());
		}
		finally{
			try{
				resultset.close();
			}catch(Exception e)
			{
				LOGGER.error("Exp in finally..");
			}
		}
		//LOGGER.debug("startDate: " + startDate);
		return startDate;
	}*/
	/*private boolean isValidCode(String glc) throws TaskFailedException{
		CodeValidator cv=CodeValidator.getInstance();
		if(!cv.isValidCode(glc)){
						return false;
			}

		return true;
	}*/

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
				formstartDate = formatter1.format(sdf.parse(startDate1));
	   		}
	   		catch(Exception e){throw new ValidationException(
					Arrays.asList(new ValidationError("setdates",e.getMessage())));}

	   		try{

				formendDate = formatter1.format(sdf.parse(endDate1 ));
	   		}
	   		catch(Exception e){Arrays.asList(new ValidationError("setdates",e.getMessage()));}
	   		startDate = formstartDate;
			endDate = formendDate;
			if((formstartDate == null || formstartDate.equalsIgnoreCase("")) && (formendDate == null || formendDate.equalsIgnoreCase("")))
			{
			   try{
					statement = connection.createStatement();
					String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" " +
									"FROM financialYear WHERE startingDate <= SYSDATE AND endingDate >= SYSDATE";
					rs = statement.executeQuery(query);
					if(rs.next()) formstartDate = rs.getString("startingDate");

					rs.close();
					String query1 = "SELECT TO_CHAR(sysdate, 'dd-Mon-yyyy') AS \"endingDate\" FROM dual";
					rs1 = statement.executeQuery(query1);
					if(rs1.next()) formendDate = rs1.getString("endingDate");
					rs1.close();
					statement.close();
				}
				catch(SQLException  ex)
				{//dc.addMessage("eGovFailure","setDates");
					Arrays.asList(new ValidationError("setdates",ex.getMessage()));}
			}
			if((formstartDate == null || formstartDate.equalsIgnoreCase("")) && (formendDate != null || !formendDate.equalsIgnoreCase("")))
			{
				try{
					statement = connection.createStatement();
					String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE startingDate <= '"+formendDate+"' AND endingDate >= '"+formendDate+"'";
					rs = statement.executeQuery(query);
					if(rs.next()) formstartDate = rs.getString("startingDate");
					rs.close();
					statement.close();
					}
					catch(SQLException  ex)
					{
						//dc.addMessage("eGovFailure","setDates1");
						Arrays.asList(new ValidationError("setdates",ex.getMessage()));
					}
			}

			if((formendDate == null || formendDate.equalsIgnoreCase("")) && (formstartDate != null || !formstartDate.equalsIgnoreCase("")))
			{
				try{
					statement = connection.createStatement();
					String query = "SELECT TO_CHAR(endingDate, 'dd-Mon-yyyy') AS \"endingDate\" " +
					"FROM financialYear WHERE startingDate <= '"+formstartDate+"' AND endingDate >= '"+formstartDate+"'";
					rs = statement.executeQuery(query);
			//		if(rs.next()) endDate = rs.getString("endingDate");
					rs.close();
					statement.close();
					statement = null;
				}catch(SQLException  ex){
					Arrays.asList(new ValidationError("setdates",ex.getMessage()));}
			}
		}
	});
	
	
	}

	public String getULBName(){
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				String ulbName="";

				try{
					 statement = connection.createStatement();
					ResultSet rset = statement.executeQuery("select name from companydetail");
					if(rset.next())
						ulbName = rset.getString(1);
					else
						LOGGER.debug("No result for companydetails");
					rset.close();
					statement.close();
				}catch(Exception sqlex){
					return null;
				}
				return ulbName;
				
			}
		});
	}
	public String getMinCode(final String minGlCode)
	{//LOGGER.debug("coming");
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				
				String minCode = "";
				try{
					 statement = connection.createStatement();
					ResultSet rset = statement.executeQuery("select glcode from chartofaccounts where glcode like '"+minGlCode+"'|| '%' and classification = 4 order by glcode asc");
					if(rset.next())
						minCode = rset.getString(1);
					else
						LOGGER.debug("No glcodes for this mincode");
					rset.close();
					statement.close();
				}catch(Exception sqlex){
					LOGGER.error("Exception while getting minGlCode"+sqlex);
				}
				return minCode;
				
				
			}
		});
		
	}
	public String getMaxCode(final String maxGlCode)
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
			
				String maxCode = "";
				try{
					statement = connection.createStatement();
					ResultSet rset = statement.executeQuery("  select glcode from chartofaccounts where glcode like '"+maxGlCode+"'|| '%' and classification = 4 order by glcode desc");
					if(rset.next())
						maxCode = rset.getString(1);
					else
						LOGGER.debug("No glcodes for this maxcode");
					rset.close();
					statement.close();
				}catch(Exception sqlex){
					LOGGER.error("Exception while getting maxGlCode"+sqlex);
				}
				return maxCode;
			}
		});
		
	}
	public String getCGN(final String vhn,final String vdate)
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				

				String cgn="";
				statement=null;
				ResultSet rsCgn=null;
				if(!vhn.equals(""))
				{
					try
					{
						statement = connection.createStatement();  
					    String queryCgn="select CGN from VOUCHERHEADER where VOUCHERNUMBER='"+vhn+"' and voucherdate=to_date('"+vdate+"','dd-Mon-yyyy')";  
					    rsCgn = statement.executeQuery(queryCgn);
					    rsCgn.next();
					    cgn = rsCgn.getString("CGN");

					}
					catch(Exception sqlex)
					{
						LOGGER.error("Error in cgnCatch#"+sqlex);
						return null;
					}
					finally
					{
						try
						{
							rsCgn.close();
							statement.close();
						}
						catch(Exception ExCgn){LOGGER.error("Exp in finally...");}
					}
				}

				return cgn;
			
				
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
	//			Statement st = conn.createStatement();
		//		st = conn.createStatement();
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
}
