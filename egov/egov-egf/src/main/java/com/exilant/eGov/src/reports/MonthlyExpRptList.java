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
 * Created on March 20, 2006
 * @author Prabhu
 */

package com.exilant.eGov.src.reports;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;



public class MonthlyExpRptList {
	private static final Logger LOGGER = Logger.getLogger(MonthlyExpRptList.class);

	//private final static String datasource = "java:/ezgovOraclePool";
	private final static String errConnOpenString = "Unable to get a connection from Pool Please make sure that the connection pool is set up properly";

	List<Object[]> resultset1;
	TaskFailedException taskExc;

	public MonthlyExpRptList() {}

	//	 This function returns the exact format of financial year date (dd/mm/yy)
	private String convertDate(String changeDate)
	{
		String dateChanged ="";
		String dd="";
		String mm="";
		String yy="";
	//	int dateLenght = changeDate.length( );
			dd=changeDate.substring(8,10);
			mm=changeDate.substring(5,7);
			yy=changeDate.substring(0,4);

		dateChanged = mm+"/"+dd+"/"+yy;
		return dateChanged;
	}
	// This function returns the entered date in the report
	private String enteredDate(String fiscalStartDate, String enterMonth)
	{
		String dateEntered ="";
		String dd="";
		String mm="";
		String mmm="";
		String yy="";
	//	MonthlyExpRptBean reportBean= new MonthlyExpRptBean();
		//	int dateLenght = fiscalStartDate.length( );
			int monthLength = enterMonth.length( );
			dd="01";
			mm=enterMonth.substring(0,enterMonth.length());

			// ycompare is the int value of the current entered month
			int ycompare = Integer.parseInt(mm);
			//if(LOGGER.isDebugEnabled())     LOGGER.debug(ycompare);
			yy=fiscalStartDate.substring(6,10);
			//if(LOGGER.isDebugEnabled())     LOGGER.debug(yy);

			// Variable yyy increments the year by 1
			int yyy = Integer.parseInt(yy) + 1;
			mmm=fiscalStartDate.substring(0,2);
			//if(LOGGER.isDebugEnabled())     LOGGER.debug(mmm);

			//  xcompare is the int value of the fiscal year start date month
			int mcompare = Integer.parseInt(mmm);
			//if(LOGGER.isDebugEnabled())     LOGGER.debug(mcompare);

			// it compares the month of financial year start date with the current month entered.
			/*	suppose if you enter may as a month, it compares the month with april(if fiscalyear is from april 2005 to march 2006)
			 * if(4<=5) show it will give you the current date as 05-01-2005 or if you enter january,
			 * if(4<=1) --> false, so it will give you the date as 01-01-2006
			 */
		if(mcompare <= ycompare)
		{
		// if it is true it will show you the same year as entered.
		dateEntered = mm+"/"+dd+"/"+yy;
		return dateEntered;
		}
		else
		{
		// else it shows the month along the next year (increment the year by 1)
		dateEntered = mm+"/"+dd+"/"+yyy;
		return dateEntered;
		}
	}

	//	 This functions returns the end of the month of entered date
	private String enteredDateEnd(String enterDateEnd)
	{
		String dateChanged ="";
		String dd="";
		String mm="";
		String yy="";
		//int dateLenght = enterDateEnd.length( );
			if(enterDateEnd.length( )==9){
			dd=enterDateEnd.substring(3,4);
			mm=enterDateEnd.substring(0,1);
			yy=enterDateEnd.substring(5,9);
			}
			else {
			//	dd=enterDateEnd.substring(4,5);
				mm=enterDateEnd.substring(0,2);
				yy=enterDateEnd.substring(6,10);
			}
		// Finds out the end day of the month(whether it is 31,30,29 or 28)
			int leapyear = Integer.parseInt(yy);
			int monval = Integer.parseInt(mm);
			if((leapyear % 4 == 0) && ((!(leapyear % 100 == 0)) || (leapyear % 400 == 0)))
			{
					switch(monval)
					{
						case 1: monval = 31; break;
						case 2: monval = 29; break;
						case 3: monval = 31; break;
						case 4: monval = 30; break;
						case 5: monval = 31; break;
						case 6: monval = 30; break;
						case 7: monval = 31; break;
						case 8: monval = 31; break;
						case 9: monval = 30; break;
						case 10: monval = 31; break;
						case 11: monval = 30; break;
						case 12: monval = 31; break;

					 }
			}
			else
			{
				switch(monval)
				{
					case 1: monval = 31; break;
					case 2: monval = 28; break;
					case 3: monval = 31; break;
					case 4: monval = 30; break;
					case 5: monval = 31; break;
					case 6: monval = 30; break;
					case 7: monval = 31; break;
					case 8: monval = 31; break;
					case 9: monval = 30; break;
					case 10: monval = 31; break;
					case 11: monval = 30; break;
					case 12: monval = 31; break;

				 }
			}
			dateChanged = mm+"/"+monval+"/"+yy;
		return dateChanged;
	}

	public LinkedList getMonthlyExpRptList(MonthlyExpRptBean reportBean)
	throws TaskFailedException {

		Query prepStmt = null;
		List<Object[]> rs =null;
		LinkedList links = new LinkedList();



		String yearName = reportBean.getYearName();
		String monthName = reportBean.getMonthName();
		String zoneName = reportBean.getZoneName();

		EGovernCommon egc=new EGovernCommon();
		String finId = reportBean.getYearName();
		//if(LOGGER.isDebugEnabled())     LOGGER.debug(finId);

		// This function returns the financial year date(yyyy-mm-dd)
		String startDate=egc.getStartDate(finId);
		//if(LOGGER.isDebugEnabled())     LOGGER.debug(startDate);

		// This function returns the exact format of financial year date (dd/mm/yy)
		String fiscalStartDate = convertDate(startDate);
		//if(LOGGER.isDebugEnabled())     LOGGER.debug(fiscalStartDate);

		// This function returns the entered date in the report
		String enterDateStart = enteredDate(fiscalStartDate,monthName);
		//if(LOGGER.isDebugEnabled())     LOGGER.debug(enterDateStart);

		// This functions returns the end of the month of entered date
		String enterDateEnd = enteredDateEnd(enterDateStart);
		//if(LOGGER.isDebugEnabled())     LOGGER.debug(enterDateEnd);


		String queryString="";

		String queryStringOne = "SELECT DISTINCT gv.departmentid AS deptId, gl.glcode AS accId,gl.glcodeid AS accCode, coa.name AS nomenName"+
		" FROM generalvouchermis gv, generalledger gl,chartofaccounts coa,voucherheader vh "+
		" WHERE gv.voucherheaderid = vh.id AND gl.glcodeid = coa.id AND gl.voucherheaderid = vh.id and coa.type='E' "+
		" and vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY')AND gv.zoneid='"+zoneName+"' ";

		String queryStringTwo = "SELECT DISTINCT gv.departmentid AS deptId, gl.glcode AS accId,gl.glcodeid AS accCode, coa.name AS nomenName"+
		" FROM generalvouchermis gv, generalledger gl,chartofaccounts coa,voucherheader vh "+
		" WHERE gv.voucherheaderid = vh.id AND gl.glcodeid = coa.id AND gl.voucherheaderid = vh.id and coa.type='E'"+
		" AND vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY')";

		if(!zoneName.equalsIgnoreCase("")){
			queryString = queryStringOne;
			}
			else{
				queryString = queryStringTwo;
			}
		try	{
			prepStmt=HibernateUtil.getCurrentSession().createSQLQuery(queryString);
			rs=prepStmt.list();
		}
		catch(Exception e)
		{if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in prepare statement (deptId, accId, nomenclature):"+prepStmt);
		}

		try
		{
			for(Object[] element : rs){
				 reportBean= new MonthlyExpRptBean();

				 reportBean.setDeptCode(element[0].toString());
				 reportBean.setAccCode(element[1].toString());
				 reportBean.setNomenName(element[3].toString());

			//Resultset rs1 will give the budget estimate

				 Query prepStmt1=null;
				 List<Object[]> rs1=null;

				 String budEst = "SELECT SUM(bd.ORIGINALAMT)as budgetEstimate FROM egf_budgetdetail bd,egf_budget bm,egf_budgetgroup bg "+
					" WHERE bm.id=bd.BUDGETID AND bm.ISACTIVEBUDGET=1 AND bm.STATUSID=2 AND (bd.BUDGETCODEID="+element[2].toString()+" OR  bd.BUDGETGROUPID =bg.id "+
					" AND bg.MAJORCODE=(SELECT parentid FROM chartofaccounts coa WHERE coa.id="+element[2].toString()+")) AND bd.DEPARTMENTID="+element[0].toString() +" AND  "+
					" bm.FINANCIALYEARID="+yearName+"  "+
					" GROUP BY bd.DEPARTMENTID";

				 try	{
				 	prepStmt1=HibernateUtil.getCurrentSession().createSQLQuery(budEst);
				 	rs1=prepStmt1.list();
				 }
				 catch(Exception e)
					{
				 	if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in prepare statement (Budget Estimate):"+prepStmt);
					}

				 double budgetAmount= 0.0;

				 for(Object[] element1 : rs1){
				 	budgetAmount = Double.parseDouble(element1[0].toString());
				 }
			    //if(LOGGER.isDebugEnabled())     LOGGER.debug("budgetAmount is :"+budgetAmount);
			    String budgetEst = Double.toString(budgetAmount);

			    reportBean.setBudEstimate(budgetEst);

			//   Resultset rs2 will give Expenditure upto the previous month

			     Query prepStmt2=null;
			     List<Object[]> rs2=null;

				 String expPrevMonthAll=" ";
				 String expPrevMonthOne = " SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(sal.netpay)AS expPrevious FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,"+
				  "	SALARYBILLDETAIL sal WHERE vh.id =sal.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate < to_date('"+enterDateStart+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" and gv.zoneid =  "+zoneName+" "+
				  " group by gv.departmentid, gl.glcode"+

				  "	UNION "+

				  "	SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(cbd.passedamount)AS expPrevious FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv, "+
				  "	CONTRACTORBILLDETAIL cbd WHERE vh.id =cbd.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate < to_date('"+enterDateStart+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" and gv.zoneid =  "+zoneName+" "+
				  " group by gv.departmentid, gl.glcode"+

				  "	UNION "+

				  "	SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(sbd.passedamount)AS expPrevious FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,  "+
				  "	SUPPLIERBILLDETAIL sbd WHERE vh.id =sbd.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate < to_date('"+enterDateStart+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" and gv.zoneid =  "+zoneName+"  "+
				  " group by gv.departmentid, gl.glcode";

				 String expPrevMonthTwo = " SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(sal.netpay)AS expPrevious FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,"+
				  "	SALARYBILLDETAIL sal WHERE vh.id =sal.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate < to_date('"+enterDateStart+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" "+
				  " group by gv.departmentid, gl.glcode"+

				  "	UNION "+

				  "	SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(cbd.passedamount)AS expPrevious FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv, "+
				  "	CONTRACTORBILLDETAIL cbd WHERE vh.id =cbd.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate < to_date('"+enterDateStart+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" "+
				  " group by gv.departmentid, gl.glcode"+

				  "	UNION "+

				  "	SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(sbd.passedamount)AS expPrevious FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,  "+
				  "	SUPPLIERBILLDETAIL sbd WHERE vh.id =sbd.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate < to_date('"+enterDateStart+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+"  "+
				  " group by gv.departmentid, gl.glcode ";


				 if(!zoneName.equalsIgnoreCase("")){
				 	expPrevMonthAll = expPrevMonthOne;
					}
					else{
						expPrevMonthAll = expPrevMonthTwo;
					}
				try	{
					prepStmt2=HibernateUtil.getCurrentSession().createSQLQuery(expPrevMonthAll);
					rs2=prepStmt2.list();
				}
				catch(Exception e)
				{if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in prepare statement (Expenditure upto previous month):"+prepStmt);
				}

				 double expPrevMonth= 0.0;

				 for(Object[] element2 : rs2){
				 	double getexpPrevious = Double.parseDouble(element2[2].toString());
				 	expPrevMonth = expPrevMonth + getexpPrevious;
				 }

				 //if(LOGGER.isDebugEnabled())     LOGGER.debug("Expenditure upto the previous month is "+expPrevMonth);

				 String expUptoPrevMonth = Double.toString(expPrevMonth);

				 reportBean.setExpPrev(expUptoPrevMonth);

			//    Resultset rs3 will give Payment upto the previous month

				 Query prepStmt3=null;
				 List<Object[]> rs3=null;

				 String payPrevMonthAll="";

				 String payPrevMonthOne = " SELECT  gv.departmentid AS deptcode2, gl.glcode AS acccode2,SUM(sph.paidamount)AS payPrevious FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,"+
				  "	subledgerpaymentheader sph WHERE vh.id =sph.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate < to_date('"+enterDateStart+"','MM-DD-YYYY')and gv.zoneid =  "+zoneName+" "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" "+
				  " GROUP BY gv.departmentid, gl.glcode ";

				 String payPrevMonthTwo = " SELECT  gv.departmentid AS deptcode2, gl.glcode AS acccode2,SUM(sph.paidamount)AS payPrevious FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,"+
				  "	subledgerpaymentheader sph WHERE vh.id =sph.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+fiscalStartDate+"','MM-DD-YYYY') AND vh.voucherdate < to_date('"+enterDateStart+"','MM-DD-YYYY')  "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" "+
				  " GROUP BY gv.departmentid, gl.glcode ";


				 if(!zoneName.equalsIgnoreCase("")){
				 	payPrevMonthAll = payPrevMonthOne;
					}
					else{
						payPrevMonthAll = payPrevMonthTwo;
					}
				try	{
					 prepStmt3=HibernateUtil.getCurrentSession().createSQLQuery(payPrevMonthAll);
					rs3=prepStmt3.list();
				}
				catch(Exception e)
				{if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in prepare statement (Payment upto previous month):"+prepStmt);
				}


				 double payPrevMonth= 0.0;

				 for(Object[] element3 : rs3){
				 	double getPayPrevious =  Double.parseDouble(element3[2].toString());
				 	payPrevMonth = payPrevMonth + getPayPrevious;
				 }

				 //if(LOGGER.isDebugEnabled())     LOGGER.debug("Payment upto the previous month is "+payPrevMonth);

				 String payUptoPrevMonth = Double.toString(payPrevMonth);

				 reportBean.setPayPrev(payUptoPrevMonth);

			//    Resultset rs4 will give Expenditure during the month

				 Query prepStmt4=null;
				 List<Object[]> rs4=null;

				 String expCurrMonthAll=" ";
				 String expCurrMonthOne = " SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(sal.netpay)AS expCurrent FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,"+
				  "	SALARYBILLDETAIL sal WHERE vh.id =sal.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+enterDateStart+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" and gv.zoneid =  "+zoneName+" "+
				  " group by gv.departmentid, gl.glcode"+

				  "	UNION "+

				  "	SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(cbd.passedamount)AS expCurrent FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv, "+
				  "	CONTRACTORBILLDETAIL cbd WHERE vh.id =cbd.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+enterDateStart+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" and gv.zoneid =  "+zoneName+" "+
				  " group by gv.departmentid, gl.glcode"+

				  "	UNION "+

				  "	SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(sbd.passedamount)AS expCurrent FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,  "+
				  "	SUPPLIERBILLDETAIL sbd WHERE vh.id =sbd.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+enterDateStart+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" and gv.zoneid =  "+zoneName+"  "+
				  " group by gv.departmentid, gl.glcode";

				 String expCurrMonthTwo = " SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(sal.netpay)AS expCurrent FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,"+
				  "	SALARYBILLDETAIL sal WHERE vh.id =sal.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+enterDateStart+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" "+
				  " group by gv.departmentid, gl.glcode"+

				  "	UNION "+

				  "	SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(cbd.passedamount)AS expCurrent FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv, "+
				  "	CONTRACTORBILLDETAIL cbd WHERE vh.id =cbd.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+enterDateStart+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" "+
				  " group by gv.departmentid, gl.glcode"+

				  "	UNION "+

				  "	SELECT gv.departmentid AS deptcode1, gl.glcode AS acccode1,SUM(sbd.passedamount)AS expCurrent FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,  "+
				  "	SUPPLIERBILLDETAIL sbd WHERE vh.id =sbd.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+enterDateStart+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+"  "+
				  " group by gv.departmentid, gl.glcode ";

				 if(!zoneName.equalsIgnoreCase("")){
				 	expCurrMonthAll = expCurrMonthOne;
					}
					else{
						expCurrMonthAll = expCurrMonthTwo;
					}
				 try	{ prepStmt4=HibernateUtil.getCurrentSession().createSQLQuery(expCurrMonthAll);
				 		rs4=prepStmt4.list();
					}
					catch(Exception e)
					{if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in prepare statement (Payment upto previous month):"+prepStmt);
					}


				 double expCurrMonth= 0.0;

				 for(Object[] element4 : rs4){
				 	double getexpCurrMonth =  Double.parseDouble(element4[2].toString());
				 	expCurrMonth = expCurrMonth + getexpCurrMonth;
				 }

				 //if(LOGGER.isDebugEnabled())     LOGGER.debug("Expenditure during the current month is "+expCurrMonth);

				 String expCurrentMonth = Double.toString(expCurrMonth);

				 reportBean.setExpCurr(expCurrentMonth);

			//    Resultset rs5 will give Payment during the  month

				 Query prepStmt5=null;
				 List<Object[]> rs5=null;

				 String payCurrMonthAll="";
				 String payCurrMonthOne = " SELECT  gv.departmentid AS deptcode2, gl.glcode AS acccode2,SUM(sph.paidamount)AS payCurrent FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,"+
				  "	subledgerpaymentheader sph WHERE vh.id =sph.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+enterDateStart+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY') and gv.zoneid =  "+zoneName+" "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" "+
				  " GROUP BY gv.departmentid, gl.glcode ";

				 String payCurrMonthTwo = " SELECT  gv.departmentid AS deptcode2, gl.glcode AS acccode2,SUM(sph.paidamount)AS payCurrent FROM  VOUCHERHEADER vh ,GENERALLEDGER gl, generalvouchermis gv,"+
				  "	subledgerpaymentheader sph WHERE vh.id =sph.voucherheaderid AND vh.id=gl.voucherheaderid AND vh.id=gv.voucherheaderid AND "+
				  "	vh.voucherdate >= to_date('"+enterDateStart+"','MM-DD-YYYY') AND vh.voucherdate <= to_date('"+enterDateEnd+"','MM-DD-YYYY') "+
				  " and gv.departmentid = "+element[0].toString() +"  and gl.glcode = "+element[1].toString()+" "+
				  " GROUP BY gv.departmentid, gl.glcode ";

				 if(!zoneName.equalsIgnoreCase("")){
				 	payCurrMonthAll = payCurrMonthOne;
					}
					else{
						payCurrMonthAll = payCurrMonthTwo;
					}

				 try	{
				 	prepStmt5=HibernateUtil.getCurrentSession().createSQLQuery(payCurrMonthAll);
				 		rs5=prepStmt5.list();
					}
					catch(Exception e)
					{if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in prepare statement (Payment upto previous month):"+prepStmt);
					}

				 double payCurrMonth= 0.0;

				 for(Object[] element5 : rs5){
				 	double getpayCurrMonth =Double.parseDouble(element5[2].toString());
				 	payCurrMonth = payCurrMonth + getpayCurrMonth;
				 }

				 //if(LOGGER.isDebugEnabled())     LOGGER.debug("Payment during the current month is "+payCurrMonth);

				 String payCurrentMonth = Double.toString(payCurrMonth);

				 reportBean.setPayCurr(payCurrentMonth );

			// Progressive expenditure  = Expenditure upto the previous month + Expenditure during the month

				 Double progExpenditure = Double.parseDouble(expUptoPrevMonth) + Double.parseDouble(expCurrentMonth);

				 reportBean.setProgExp(Double.toString(progExpenditure) );

			// Liabilities accured = Progressive Expenditure - (Payment upto the previous month + Payment during the month)

				 Double liabAcc = Double.parseDouble(payUptoPrevMonth) + Double.parseDouble(payCurrentMonth);

				 Double liabAccured = progExpenditure - liabAcc;

				 reportBean.setLiaAccured(Double.toString(liabAccured) );

			   	links.add(reportBean);

				}

		}
			catch(Exception e){if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in main while loop:"+e);}

			return links;
		}
}

