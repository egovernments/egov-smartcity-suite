/*
 * Created on Apr 24,2008
 * @author Iliyaraja S
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
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class AdvanceRegister
{

	ResultSet resultset;
	Statement statement;
	TaskFailedException taskExc;
	String endDate, startDate;
	String paramdetailtypeid="",paramdetailkeyid="";
	String arMajorGLCode="";
	public String reqSubLedgerId[];
	public String reqSubLedgerName[];
	public String reqSubLedgerOB[];
	boolean subLedgerDetails=false;


	BigDecimal colTotalPrevCB = new BigDecimal("0.00");
	BigDecimal colTotalPrevPaymentValue=new BigDecimal("0.00");
	BigDecimal colTotalPrevRecoveryValue=new BigDecimal("0.00");
	BigDecimal colTotalCBValue = new BigDecimal("0.00");

	ArrayList arList=new ArrayList();
	java.util.Date dt=new java.util.Date();
	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	EGovernCommon egc=new EGovernCommon();
	CommnFunctions cf=new CommnFunctions();
	private static final Logger LOGGER = Logger.getLogger(AdvanceRegister.class);
	public AdvanceRegister(){}

	public ArrayList getAdvanceRegisterReport(AdvanceRegisterBean arBean)throws TaskFailedException
	{
		LOGGER.info("Inside getAdvanceRegisterReport()");
		

		if(!arBean.getEndDate().equals(""))
			EGovernCommon.isCurDate(arBean.getEndDate());
		try
		{


			LOGGER.info("From date---->"+arBean.getStartDate());
			LOGGER.info("To date------>"+arBean.getEndDate());
			LOGGER.info("party detail type id--->"+arBean.getPartytype_id());
			LOGGER.info("party detail key id---->"+arBean.getAccEntityKey());

			if(!arBean.getStartDate().equals(""))
			{
				dt = sdf.parse(arBean.getStartDate());
				startDate = formatter.format(dt);
				LOGGER.info("After convert From date is--->"+startDate);
			}


			if(!arBean.getEndDate().equals(""))
			{
				dt = sdf.parse(arBean.getEndDate());
				endDate = formatter.format(dt);

				LOGGER.info("After convert To date is--->"+endDate);
			}
			if(!arBean.getPartytype_id().equals(""))
				paramdetailtypeid=arBean.getPartytype_id();

			if(!arBean.getAccEntityKey().equals(""))
				paramdetailkeyid=arBean.getAccEntityKey();

			arMajorGLCode=EGovConfig.getProperty("egf_config.xml","ARGLCode","","AdvanceRegisterCode");
			LOGGER.info("AdvanceRegister arMajorGLCode--->"+arMajorGLCode);
			getOpeningBalanceForAdvanceRegister();
			getSubLedgerListForAdvRegister();
		}
		catch(Exception exception)
		{
			LOGGER.error("EXP in getAdvanceRegisterReport"+exception.getMessage());
			throw taskExc;
		}

		return arList;

	}

	/**
	 * This function executes the Query-get the Opening Balance For Advance Register.

	 */

	private void getOpeningBalanceForAdvanceRegister() throws Exception
	{
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {

				try{
					StringBuffer basicquery1 = new StringBuffer(" SELECT slid AS \"slid\", slname AS \"slname\",opgdebitbal - opgcreditbal + prevdebit- prevcredit AS \"OpeningBalance\""
					+" FROM (SELECT DISTINCT adk.detailkey slid,NVL((SELECT SUM(ts.openingcreditbalance)"
					+" FROM chartofaccounts coa,financialyear fy,transactionsummary ts,accountdetailkey adk"
					+" WHERE ts.glcodeid=coa.id AND ts.financialyearid=fy.id AND ts.accountdetailkey=adk.detailkey "
					+" AND ts.accountdetailtypeid=adk.detailtypeid AND adk.detailtypeid="+paramdetailtypeid+" AND adk.detailkey="+paramdetailkeyid+" AND fy.startingdate <= TO_DATE ('"+startDate+"') AND fy.endingdate >= TO_DATE ('"+startDate+"')),0) OpgCreditBal,"
					+" NVL((SELECT SUM(ts.openingdebitbalance) FROM chartofaccounts coa,financialyear fy,transactionsummary ts,"
					+" accountdetailkey adk WHERE ts.glcodeid=coa.id AND ts.financialyearid=fy.id AND ts.accountdetailkey=adk.detailkey "
					+" AND ts.accountdetailtypeid=adk.detailtypeid AND adk.detailtypeid="+paramdetailtypeid+" AND adk.detailkey="+paramdetailkeyid+" AND fy.startingdate <= TO_DATE ('"+startDate+"') AND fy.endingdate >= TO_DATE ('"+startDate+"')),0)OpgDebitBal,"
					+" NVL((SELECT SUM (gld.amount)FROM generalledgerdetail gld,generalledger gl,voucherheader vh"
					+" WHERE gld.generalledgerid = gl.ID AND gl.voucherheaderid = vh.ID AND gld.detailkeyid = adk.detailkey"
					+" AND gld.detailtypeid = adk.detailtypeid AND vh.voucherdate >= fy.startingdate"
					+" AND vh.voucherdate <=TO_DATE (TO_DATE ('"+startDate+"') - 1)AND vh.status NOT IN (4)AND gl.debitamount > 0),0) prevdebit,"
					+" NVL((SELECT SUM (gld.amount)FROM generalledgerdetail gld,generalledger gl,voucherheader vh"
					+" WHERE gld.generalledgerid = gl.ID AND gl.voucherheaderid = vh.ID AND gld.detailkeyid = adk.detailkey"
					+" AND gld.detailtypeid = adk.detailtypeid AND vh.voucherdate >= fy.startingdate "
					+" AND vh.voucherdate <=TO_DATE (TO_DATE ('"+startDate+"') - 1)AND vh.status NOT IN (4)AND gl.creditamount > 0),0) prevcredit,"
					+" (SELECT DISTINCT DECODE (adt.tablename ,'accountEntityMaster', (SELECT name FROM accountentitymaster"
					+" WHERE id= adk.detailkey ),'relation',(SELECT name FROM relation WHERE id = adk.detailkey ), 'eg_employee',"
					+" (SELECT emp_firstname FROM eg_employee WHERE id= adk.detailkey )) FROM accountdetailkey adk,accountdetailtype adt"
					+" WHERE adt.id = adk.detailtypeid AND adk.detailtypeid="+paramdetailtypeid+" AND adk.detailkey="+paramdetailkeyid+") slname"
					+" FROM chartofaccounts coa,financialyear fy,accountdetailkey adk WHERE fy.isactiveforposting = 1"
					+" AND fy.startingdate <= TO_DATE ('"+startDate+"') AND fy.endingdate >= TO_DATE ('"+startDate+"')"
					+" AND adk.detailtypeid="+paramdetailtypeid+" AND adk.detailkey="+paramdetailkeyid+") ");


					String query = new StringBuffer().append(basicquery1).toString();
					LOGGER.debug("Query for getOpeningBalanceForAdvanceRegister---->"+query);

				//	AdvanceRegisterBean arBean=null;

					statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					resultset = statement.executeQuery(query);

					int resSize = 0, i = 0;
					if (resultset.last())
						resSize = resultset.getRow();

					reqSubLedgerId = new String[resSize];
					reqSubLedgerName = new String[resSize];
					reqSubLedgerOB = new String[resSize];

					resultset.beforeFirst();

					while(resultset.next())
					{
						reqSubLedgerId[i] = resultset.getString("slid");
						reqSubLedgerName[i] = resultset.getString("slname").toString();
						//reqSubLedgerOB[i] = cf.numberToString(resultset.getString("OpeningBalance")).toString();
						reqSubLedgerOB[i] = resultset.getString("OpeningBalance").toString();
						i += 1;
					}
					HibernateUtil.release(statement, resultset);
				}
				catch(SQLException sqlE){
					LOGGER.debug("Exception in getOpeningBalanceForAdvanceRegister "+sqlE);
					throw sqlE;
				}
				
			}
		});
	}

	/**
	 * This function executes the Query-get all the subledger type related with Advance Register,retrieves the values from
	 * the Query into local variables.
	 */
	private void getSubLedgerListForAdvRegister() throws Exception
	{
		
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {

				LOGGER.info("Inside getSubLedgerListForAdvRegister");
				try{
					StringBuffer wherequery3 = new StringBuffer();
					StringBuffer wherequery4 = new StringBuffer();
					StringBuffer wherequery5 = new StringBuffer();

					if (endDate !=null && !endDate.equals(""))
					{

						wherequery3 = wherequery3.append(" AND bvh.voucherdate <=").append("TO_DATE('"+endDate+"')");
					}
					if ((paramdetailtypeid !=null && !paramdetailtypeid.equals("")) && (paramdetailkeyid !=null && !paramdetailkeyid.equals("")))
					{
						wherequery4 = wherequery4.append(" AND adk.detailtypeid=").append(paramdetailtypeid);
						wherequery4 = wherequery4.append(" AND adk.detailkey=").append(paramdetailkeyid);
					}
					wherequery5 = wherequery5.append(" order by sltypename,sltype,slid,slname,vhid,Particulars DESC");


					StringBuffer basicquery1 = new StringBuffer("SELECT coa.NAME sltypename,bvh.id AS vhid,adk.detailtypeid AS sltype,adk.detailkey AS slid,bvh.description as \"remarks\","
							+" TO_CHAR(bvh.voucherdate) vDate,DECODE (adt.tablename ,'accountEntityMaster',"
							+" (SELECT name FROM accountentitymaster WHERE id= adk.detailkey ),'relation',(SELECT name FROM relation WHERE id = adk.detailkey ), "
							+" 'eg_employee',(SELECT emp_firstname FROM eg_employee WHERE id= adk.detailkey ))slname,"
							+" DECODE(gl.debitamount,0,NULL,CONCAT(bvh.vouchernumber,CONCAT('-',bvh.voucherdate))) AS \"PaymentOrderNumberDate\",null as \"BPVNumberDate\","
							+" concat(gl.glcode,concat('-',coa.NAME)) Particulars,DECODE (gl.debitamount,0,NULL,gld.amount) as \"PaymentAmount\","
							+" DECODE(gl.creditamount,0,NULL,CONCAT(bvh.vouchernumber,CONCAT('-',bvh.voucherdate))) AS \"RecoveryNumberdate\","
							+" DECODE (gl.creditamount,0,NULL,gld.amount) Recoveryamount FROM generalledgerdetail gld,generalledger gl,"
							+" voucherheader bvh left join paymentheader ph ON   ph.voucherheaderid = bvh.ID,"
							+" voucherheader bvh1 left join otherbilldetail obd ON obd.payvhid = bvh1.ID,chartofaccounts coa,"
							+" accountdetailkey adk,chartofaccountdetail cod ,accountdetailtype adt WHERE gld.detailtypeid = adk.detailtypeid AND gld.detailkeyid = adk.detailkey"
							+" AND bvh.ID = bvh1.ID AND gld.generalledgerid = gl.ID AND gl.glcodeid = coa.ID AND gl.voucherheaderid = bvh.ID"
							+" AND bvh.voucherdate >=TO_DATE ('"+startDate+"') AND adt.id = adk.detailtypeid"
							+" and bvh.status <> 4 AND coa.ID = cod .glcodeid AND cod.detailtypeid = adk .detailtypeid AND coa.ID IN (SELECT ID FROM chartofaccounts WHERE glcode like '"+arMajorGLCode+"%"+"')");


					String query = new StringBuffer().append(basicquery1).append(wherequery3).append(wherequery4).append(wherequery5).toString();
					LOGGER.debug("Query for getSubLedgerListForAdvRegister---->"+query);
					statement = con.createStatement();
					resultset = statement.executeQuery(query);
					LOGGER.info("After Execute Query");
					int i=1;

					String slDetailKeyId="";
					while(resultset.next())
					{
						subLedgerDetails=true;
						String sltype="", slid="",remarks="",vdate="",slname="",payordernumberdate="",bankpayvnumberdate="";
						String particulars="",payamount="",recovnumberdate="",recovamount="";
						
						if(slDetailKeyId.equals(""))
						{
							slDetailKeyId=resultset.getString("slid");
							// for Opening Balance Header for each subledger name
							HashMap data = new HashMap();

							if(resultset.getString("slname")!= null)
								slname=resultset.getString("slname");
							else
								slname="&nbsp;";

							data.put("serialNo","&nbsp;" );
							data.put("sltype","&nbsp;" );
							data.put("slid", "&nbsp;");
							data.put("vdate","&nbsp;" );

							data.put("slname", slname);
							data.put("payordernumberdate","&nbsp;" );
							data.put("bankpayvnumberdate", "&nbsp;");
							data.put("particulars","Opening Balance" );


							for(int k=0;k<reqSubLedgerId.length;k++)
							{
								if(reqSubLedgerId[k].equals(slDetailKeyId))
								{
									data.put("payamount",formatAmtTwoDecimal(reqSubLedgerOB[k]).toString() );
									data.put("closingBal", formatAmtTwoDecimal(reqSubLedgerOB[k]).toString());


									if(!reqSubLedgerOB[k].equals("0") && !reqSubLedgerOB[k].equals(""))
									{
										LOGGER.info("Inside If 1");
										colTotalCBValue = new BigDecimal(reqSubLedgerOB[k]);
									}
									else
									{
										LOGGER.info("Inside else 1");
										colTotalCBValue=new BigDecimal("0.00");
									}
								}

							}

							data.put("recovnumberdate","&nbsp;" );
							data.put("recovamount","&nbsp;" );
							data.put("remarks","&nbsp;");

							data.put("serialNo", i+"");
							i++;

							arList.add(data);


							//first record start for particular subledger name

							data = new HashMap();

							if(resultset.getString("sltype")!= null)
								sltype=resultset.getString("sltype");
							else
								sltype="&nbsp;";

							if(resultset.getString("slid")!= null)
								slid=resultset.getString("slid");
							else
								slid="&nbsp;";

							//LOGGER.info("remarks value is---"+resultset.getString("remarks"));

							if(resultset.getString("remarks")!= null)
							{
								if(resultset.getString("remarks").trim().length()>0)
									remarks=resultset.getString("remarks");
								else
									remarks="&nbsp;";
							}
							else
								remarks="&nbsp;";

							if(resultset.getString("vdate")!= null)
								vdate=resultset.getString("vdate");
							else
								vdate="&nbsp;";

							if(resultset.getString("slname")!= null)
								slname=resultset.getString("slname");
							else
								slname="&nbsp;";
							if(resultset.getString("paymentordernumberdate")!= null)
								payordernumberdate=resultset.getString("paymentordernumberdate");
							else
								payordernumberdate="&nbsp;";

							if(resultset.getString("bpvnumberdate")!= null)
								bankpayvnumberdate=resultset.getString("bpvnumberdate");
							else
								bankpayvnumberdate="&nbsp;";

							if(resultset.getString("particulars")!= null)
								particulars=resultset.getString("particulars");
							else
								particulars="&nbsp;";

							if(resultset.getString("paymentamount")!= null)
							{
								//LOGGER.info("Inside if 1");
								payamount=formatAmtTwoDecimal(resultset.getString("paymentamount")).toString();
								colTotalPrevPaymentValue = new BigDecimal(resultset.getString("paymentamount"));

							}
							else
							{
								//LOGGER.info("Inside else 1");
								payamount="&nbsp;";
								colTotalPrevPaymentValue=new BigDecimal("0.00");
							}

							if(resultset.getString("recoverynumberdate")!= null)
								recovnumberdate=resultset.getString("recoverynumberdate");
							else
								recovnumberdate="&nbsp;";

							if(resultset.getString("recoveryamount")!= null)
							{
								//LOGGER.info("Inside if 2");
								recovamount=formatAmtTwoDecimal(resultset.getString("recoveryamount")).toString();
								colTotalPrevRecoveryValue = new BigDecimal(resultset.getString("recoveryamount"));

							}
							else
							{
								//LOGGER.info("Inside else 2");
								recovamount="&nbsp;";
								colTotalPrevRecoveryValue=new BigDecimal("0.00");
							}
							colTotalPrevCB=colTotalPrevCB.add(colTotalCBValue);
							colTotalPrevCB=colTotalPrevCB.add(colTotalPrevPaymentValue);
							colTotalPrevCB=colTotalPrevCB.subtract(colTotalPrevRecoveryValue);
							data.put("serialNo","&nbsp;" );
							data.put("sltype",sltype );
							data.put("slid",slid);
							data.put("vdate",vdate );
							data.put("slname", "&nbsp;");
							data.put("payordernumberdate",payordernumberdate);
							data.put("bankpayvnumberdate",bankpayvnumberdate);
							data.put("particulars",particulars);
							data.put("payamount",payamount );
							data.put("recovnumberdate",recovnumberdate );
							data.put("recovamount",recovamount);
							data.put("remarks",remarks);
							data.put("closingBal",formatAmtTwoDecimal(colTotalPrevCB.toString()));

							arList.add(data);
						}
						// for categorize same subledger name
						else if(slDetailKeyId.equalsIgnoreCase(resultset.getString("slid")))
						{


							HashMap data = new HashMap();

							if(resultset.getString("sltype")!= null)
								sltype=resultset.getString("sltype");
							else
								sltype="&nbsp;";

							if(resultset.getString("slid")!= null)
								slid=resultset.getString("slid");
							else
								slid="&nbsp;";


							//LOGGER.info("remarks value is---"+resultset.getString("remarks"));
							if(resultset.getString("remarks")!= null)
							{
								if(resultset.getString("remarks").trim().length()>0)
									remarks=resultset.getString("remarks");
								else
									remarks="&nbsp;";
							}
							else
								remarks="&nbsp;";

							if(resultset.getString("vdate")!= null)
								vdate=resultset.getString("vdate");
							else
								vdate="&nbsp;";

							if(resultset.getString("slname")!= null)
								slname=resultset.getString("slname");
							else
								slname="&nbsp;";
							if(resultset.getString("paymentordernumberdate")!= null)
								payordernumberdate=resultset.getString("paymentordernumberdate");
							else
								payordernumberdate="&nbsp;";

							if(resultset.getString("bpvnumberdate")!= null)
								bankpayvnumberdate=resultset.getString("bpvnumberdate");
							else
								bankpayvnumberdate="&nbsp;";

							if(resultset.getString("particulars")!= null)
								particulars=resultset.getString("particulars");
							else
								particulars="&nbsp;";

							if(resultset.getString("paymentamount")!= null)
							{
								payamount=formatAmtTwoDecimal(resultset.getString("paymentamount")).toString();
								colTotalPrevPaymentValue = new BigDecimal(resultset.getString("paymentamount"));
							}
							else
							{
								payamount="&nbsp;";
								colTotalPrevPaymentValue=new BigDecimal("0.00");
							}

							if(resultset.getString("recoverynumberdate")!= null)
								recovnumberdate=resultset.getString("recoverynumberdate");
							else
								recovnumberdate="&nbsp;";

							if(resultset.getString("recoveryamount")!= null)
							{
								recovamount=formatAmtTwoDecimal(resultset.getString("recoveryamount")).toString();
								colTotalPrevRecoveryValue = new BigDecimal(resultset.getString("recoveryamount"));

							}
							else
							{
								recovamount="&nbsp;";
								colTotalPrevRecoveryValue=new BigDecimal("0.00");
							}


							colTotalPrevCB=colTotalPrevCB.add(colTotalPrevPaymentValue);
							colTotalPrevCB=colTotalPrevCB.subtract(colTotalPrevRecoveryValue);


							data.put("serialNo","&nbsp;" );
							data.put("sltype",sltype );
							data.put("slid", slid);
							data.put("vdate",vdate );

							data.put("slname", "&nbsp;");
							data.put("payordernumberdate",payordernumberdate);
							data.put("bankpayvnumberdate", bankpayvnumberdate);
							data.put("particulars",particulars);

							data.put("payamount",payamount );
							data.put("recovnumberdate",recovnumberdate );
							data.put("recovamount",recovamount);
							data.put("remarks",remarks);
							data.put("closingBal", formatAmtTwoDecimal(colTotalPrevCB.toString()));

							arList.add(data);

						}
						//Next record-different subledger name start
						else
						{
							slDetailKeyId=resultset.getString("slid");
							colTotalPrevCB = new BigDecimal("0.00");
							colTotalPrevPaymentValue=new BigDecimal("0.00");
							colTotalPrevRecoveryValue=new BigDecimal("0.00");


							// for Opening Balance Header for each subledger name
							HashMap data = new HashMap();

							if(resultset.getString("slname")!= null)
								slname=resultset.getString("slname");
							else
								slname="&nbsp;";

							data.put("serialNo","&nbsp;" );
							data.put("sltype","&nbsp;" );
							data.put("slid", "&nbsp;");
							data.put("vdate","&nbsp;" );

							data.put("slname", slname);
							data.put("payordernumberdate","&nbsp;" );
							data.put("bankpayvnumberdate", "&nbsp;");
							data.put("particulars","Opening Balance" );

							for(int k=0;k<reqSubLedgerId.length;k++)
							{
								if(reqSubLedgerId[k].equals(slDetailKeyId))
								{
									data.put("payamount",formatAmtTwoDecimal(reqSubLedgerOB[k]).toString() );
									data.put("closingBal", formatAmtTwoDecimal(reqSubLedgerOB[k]).toString());


									if(!reqSubLedgerOB[k].equals("0") && !reqSubLedgerOB[k].equals(""))
									{
										LOGGER.info("Inside If 2");
										colTotalCBValue = new BigDecimal(reqSubLedgerOB[k]);
									}
									else
									{
										LOGGER.info("Inside else 2");
										colTotalCBValue=new BigDecimal("0.00");
									}
								}

							}

							data.put("recovnumberdate","&nbsp;" );
							data.put("recovamount","&nbsp;" );
							data.put("remarks","&nbsp;");


							data.put("serialNo", i+"");
							i++;

							arList.add(data);


							//first record start for particular subledger name

							data = new HashMap();

							if(resultset.getString("sltype")!= null)
								sltype=resultset.getString("sltype");
							else
								sltype="&nbsp;";

							if(resultset.getString("slid")!= null)
								slid=resultset.getString("slid");
							else
								slid="&nbsp;";


							//LOGGER.info("remarks value is---"+resultset.getString("remarks"));

							if(resultset.getString("remarks")!= null)
							{
								if(resultset.getString("remarks").trim().length()>0)
									remarks=resultset.getString("remarks");
								else
									remarks="&nbsp;";
							}
							else
								remarks="&nbsp;";

							if(resultset.getString("vdate")!= null)
								vdate=resultset.getString("vdate");
							else
								vdate="&nbsp;";

							if(resultset.getString("slname")!= null)
								slname=resultset.getString("slname");
							else
								slname="&nbsp;";
							if(resultset.getString("paymentordernumberdate")!= null)
								payordernumberdate=resultset.getString("paymentordernumberdate");
							else
								payordernumberdate="&nbsp;";

							if(resultset.getString("bpvnumberdate")!= null)
								bankpayvnumberdate=resultset.getString("bpvnumberdate");
							else
								bankpayvnumberdate="&nbsp;";

							if(resultset.getString("particulars")!= null)
								particulars=resultset.getString("particulars");
							else
								particulars="&nbsp;";

							if(resultset.getString("paymentamount")!= null)
							{
								payamount=formatAmtTwoDecimal(resultset.getString("paymentamount")).toString();
								colTotalPrevPaymentValue = new BigDecimal(resultset.getString("paymentamount"));
							}
							else
							{
								payamount="&nbsp;";
								colTotalPrevPaymentValue=new BigDecimal("0.00");
							}

							if(resultset.getString("recoverynumberdate")!= null)
								recovnumberdate=resultset.getString("recoverynumberdate");
							else
								recovnumberdate="&nbsp;";

							if(resultset.getString("recoveryamount")!= null)
							{
								recovamount=formatAmtTwoDecimal(resultset.getString("recoveryamount")).toString();
								colTotalPrevRecoveryValue = new BigDecimal(resultset.getString("recoveryamount"));

							}
							else
							{
								recovamount="&nbsp;";
								colTotalPrevRecoveryValue=new BigDecimal("0.00");
							}

							colTotalPrevCB=colTotalPrevCB.add(colTotalCBValue);

							colTotalPrevCB=colTotalPrevCB.add(colTotalPrevPaymentValue);
							colTotalPrevCB=colTotalPrevCB.subtract(colTotalPrevRecoveryValue);


							data.put("serialNo","&nbsp;" );
							data.put("sltype",sltype );
							data.put("slid", slid);
							data.put("vdate",vdate );

							data.put("slname", "&nbsp;");
							data.put("payordernumberdate",payordernumberdate);
							data.put("bankpayvnumberdate", bankpayvnumberdate);
							data.put("particulars",particulars);

							data.put("payamount",payamount );
							data.put("recovnumberdate",recovnumberdate );
							data.put("recovamount",recovamount);
							data.put("remarks", remarks);
							data.put("closingBal", formatAmtTwoDecimal(colTotalPrevCB.toString()));

							arList.add(data);

						}
					}//Main while


					//This is for those subledger having only opening balance details --Starts
					if(!subLedgerDetails)
					{

							LOGGER.info("This is for those subledger having only opening balance details");
							HashMap data = new HashMap();

								data.put("serialNo","1" );
								data.put("sltype","&nbsp;" );
								data.put("slid", "&nbsp;");
								data.put("vdate","&nbsp;" );

								data.put("payordernumberdate","&nbsp;" );
								data.put("bankpayvnumberdate", "&nbsp;");
								data.put("particulars","Opening Balance" );

								for(int k=0;k<reqSubLedgerId.length;k++)
								{
										data.put("slname",(reqSubLedgerName[k]).toString());
										data.put("payamount",formatAmtTwoDecimal(reqSubLedgerOB[k]).toString());
										data.put("closingBal", formatAmtTwoDecimal(reqSubLedgerOB[k]).toString());
								}
								data.put("recovnumberdate","&nbsp;" );
								data.put("recovamount","&nbsp;" );
								data.put("remarks","&nbsp;");
								arList.add(data);

						}//This is for those subledger having only opening balance details ----Ends
					HibernateUtil.release(statement, resultset);

				}
				catch(SQLException sqlE){
					LOGGER.error("Exception in getSubLedgerListForAdvRegister :"+sqlE);
					throw sqlE;
				}
				
			}
		});
	}
	
	public String formatAmtTwoDecimal(String amt)
	{
		NumberFormat formatter;
		formatter = new DecimalFormat("##############0.00");
		return formatter.format(new BigDecimal(amt));
	}

}
