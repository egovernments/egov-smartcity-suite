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
 * Created on Aug 27, 2008
 * @author Iliyaraja S
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.exility.common.TaskFailedException;

public class SummaryStatement {
	Query pstmt;
	Query pst;
	List<Object[]> resultset;
	List<Object[]> resultset1;
	String workname, wonumber, sDate;
	String headOfAccount = "";
	String workOrderNo = "";
	String nameOfProject = "";
	String valueOfWorkAmount = "";
	String expenditureAmount = "";
	String expenditureBillAdmittedAmount = "";
	String totalExpenditure = "";
	String amountOfContractUnexecute = "";
	String projectCompleted = "";
	String FinancialYear = "";
	ArrayList list;
	String woDate = "";
	private TaskFailedException taskExp;
	private static final Logger LOGGER = Logger
			.getLogger(SummaryStatement.class);

	// This method is called by the SummaryStatement.jsp
	public ArrayList getSummaryStatementReport(String wonumber,
			String workname, String monthNm, String workDate,
			String FinancialYear) throws TaskFailedException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date dt;

		if(LOGGER.isDebugEnabled())     LOGGER.debug("Work Number:" + wonumber);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Work Name:" + workname);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Date:" + workDate);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Mode:" + monthNm);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("FinancialYear From JSP Page : : " + FinancialYear);

		try {
			this.workname = workname;
			this.wonumber = wonumber;
			this.sDate = monthNm;
			this.FinancialYear = FinancialYear;

			if (!workDate.trim().equals("")) {
				dt = sdf.parse(workDate);
				woDate = formatter.format(dt);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("AFTER CONVERT WORK ORDER DATE IS---->" + woDate);
			}

			getReport();
		} catch (Exception exception) {
			LOGGER.error("EXP=" + exception.getMessage(), exception);
			throw taskExp;
		}

		return list;
	}

	void getReport() throws Exception {

		if(LOGGER.isDebugEnabled())     LOGGER.debug("Month Name Is :" + sDate);
		// Here STATUSID taking from egw_status es table "id" where
		// upper(es.MODULETYPE)=upper('WORKSBILL') and
		// upper(es.DESCRIPTION)=upper('Passed')
		// Here STATUSID is compare with eg_billregister table STATUSID column
		// value.

		StringBuffer basicquery = new StringBuffer(
				" SELECT gl.GLCODEID AS \"glcodeId\",w.CODE AS \"WORKNUMBER\" ,w.PASSEDAMOUNT AS \"worksPassedAmount\","
						+ " c.GLCODE ||'/' || C.NAME AS \"Head Of Account\" , "
						+ " w.NAME AS \"Project Name\",w.TOTALVALUE AS \"Value ofWork/ContractorAmount\" ,"
						+ " SUM(cbd.PASSEDAMOUNT) AS \"Expenditure(During this Month)\" "
						// +" DECODE(w.ISACTIVE ,'0','YES',DECODE(w.isactive,1,'NO')) AS \"Whether Project completed\" "
						+ " FROM eg_Billregister b,worksdetail w ,contractorbilldetail cbd,chartofaccounts c,"
						+ " generalledger gl, voucherheader v,FISCALPERIOD f , financialyear fy , egw_status es ");

		StringBuffer wherequery = new StringBuffer(
				" WHERE fy.FINANCIALYEAR= ?).append( AND b.WORKSDETAILID=w.ID "
						+ " AND b.STATUSID=(select id from egw_status es where upper(es.MODULETYPE)=upper('WORKSBILL') and upper(es.DESCRIPTION)=upper('Passed'))"
						+ " AND cbd.BILLID=b.ID AND cbd.VOUCHERHEADERID=v.ID AND gl.GLCODEID=c.ID"
						+ " AND gl.VOUCHERHEADERID=cbd.VOUCHERHEADERID AND gl.DEBITAMOUNT > 0"
						+ " AND es.id=b.statusid AND extract(month from v.VOUCHERDATE) = ?"
						+ " AND fy.id=f.FINANCIALYEARID AND F.ID=V.FISCALPERIODID AND b.PASSEDAMOUNT>0 ");

		StringBuffer orderbyquery = new StringBuffer(
				" GROUP BY gl.GLCODEID,c.GLCODE,C.NAME,w.CODE,w.NAME,w.PASSEDAMOUNT,w.TOTALVALUE  ORDER BY \"WORKNUMBER\",\"Head Of Account\" ASC ");

		if (!wonumber.equals(""))
			wherequery = wherequery.append(" AND w.CODE=trim(?)");
		if (!workname.equals(""))
			wherequery = wherequery.append(" AND w.NAME=trim(?)");
		if (!woDate.equals(""))
			wherequery = wherequery.append(" AND w.ORDERDATE = ?");

		String query = new StringBuffer().append(basicquery).append(wherequery)
				.append(orderbyquery).toString();

		int j = 1;
		pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
		pstmt.setString(j++, FinancialYear);
		pstmt.setString(j++, sDate);
		if (!wonumber.equals(""))
			pstmt.setString(j++, wonumber);
		if (!workname.equals(""))
			pstmt.setString(j++, workname);
		if (!woDate.equals(""))
			pstmt.setString(j++, woDate);
		resultset = pstmt.list();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Main Query---------->" + query);

		try {
			String WORKNUMBER = "";
			String glcodeIDParam = "";
			String headOfAccount = "";
			String nameOfProject = "";
			String valueOfWorkAmount = "";
			String expenditureAmount = "";
			String expenditureBillAdmittedAmount = "";
			String remainingUnExecutedAmt = "";
			String wdPassedAmount = "";

			SummaryStatementBean iutBean = null;
			list = new ArrayList();
			for(Object[] element : resultset){

				iutBean = new SummaryStatementBean();

				headOfAccount =element[3].toString();
				WORKNUMBER = element[1].toString();
				wdPassedAmount = element[2].toString();
				glcodeIDParam = element[0].toString();
				nameOfProject = element[4].toString();
				valueOfWorkAmount = element[5].toString();
				expenditureBillAdmittedAmount = element[6].toString();

				if (headOfAccount != null)
					iutBean.setHeadOfAccount(headOfAccount);
				else
					iutBean.setHeadOfAccount("&nbsp;");

				if (WORKNUMBER != null)
					iutBean.setWorkOrderNo(WORKNUMBER);
				else
					iutBean.setWorkOrderNo("&nbsp;");
				if (nameOfProject != null)

					iutBean.setNameOfProject(nameOfProject);
				else
					iutBean.setNameOfProject("&nbsp;");

				if (valueOfWorkAmount != null)
					iutBean.setValueOfWorkAmount(""
							+ new BigDecimal(Double
									.parseDouble(valueOfWorkAmount)).setScale(
									2, BigDecimal.ROUND_HALF_UP));
				else
					iutBean.setValueOfWorkAmount("0.00");

				if (expenditureBillAdmittedAmount != null)
					iutBean
							.setExpenditureBillAdmittedAmount(""
									+ new BigDecimal(
											Double
													.parseDouble(expenditureBillAdmittedAmount))
											.setScale(2,
													BigDecimal.ROUND_HALF_UP));
				else
					iutBean.setExpenditureBillAdmittedAmount("0.00");

				if (wdPassedAmount != null) {
					remainingUnExecutedAmt = (new BigDecimal(Double
							.parseDouble(valueOfWorkAmount)
							- (Double.parseDouble(wdPassedAmount))).setScale(2,
							BigDecimal.ROUND_HALF_UP)).toString();
					iutBean.setAmountOfContractUnexecute(""
							+ remainingUnExecutedAmt);
				} else {
					if (valueOfWorkAmount != null)
						remainingUnExecutedAmt = (new BigDecimal(Double
								.parseDouble(valueOfWorkAmount)).setScale(2,
								BigDecimal.ROUND_HALF_UP)).toString();
					iutBean.setAmountOfContractUnexecute(""
							+ remainingUnExecutedAmt);
				}

				if (remainingUnExecutedAmt.equalsIgnoreCase("0.00"))
					iutBean.setProjectCompleted("YES");
				else
					iutBean.setProjectCompleted("NO");

				if(LOGGER.isInfoEnabled())     LOGGER.info("WorkOrderNo param------------->" + WORKNUMBER);
				if(LOGGER.isInfoEnabled())     LOGGER.info("glcodeIDParam----------->" + glcodeIDParam);

				// Here STATUSID taking from egw_status es table "id" where
				// upper(es.MODULETYPE)=upper('WORKSBILL') and
				// upper(es.DESCRIPTION)=upper('Passed')
				// Here STATUSID is compare with eg_billregister table STATUSID
				// column value.

				String query1 = " SELECT w.CODE AS \"WORKNUMBER\","
						+ " c.GLCODE ||'/' || C.NAME AS \"Head Of Account\" ,sum(cbd.PASSEDAMOUNT) as \"ExpB\" "
						+ " FROM eg_Billregister b,worksdetail w ,contractorbilldetail cbd,chartofaccounts c,"
						+ " generalledger gl, voucherheader v,FISCALPERIOD f , financialyear fy , egw_status es "

						+ " WHERE fy.FINANCIALYEAR=? AND b.WORKSDETAILID=w.ID AND w.CODE=? "
						+ " AND b.STATUSID=(select id from egw_status es where upper(es.MODULETYPE)=upper('WORKSBILL') and upper(es.DESCRIPTION)=upper('Passed'))"
						+ " AND cbd.BILLID=b.ID AND cbd.VOUCHERHEADERID=v.ID AND gl.GLCODEID=? AND gl.GLCODEID=c.ID"
						+ " AND gl.VOUCHERHEADERID=cbd.VOUCHERHEADERID AND gl.DEBITAMOUNT > 0"
						+ " AND es.id=b.statusid AND extract(month from v.VOUCHERDATE) <?"
						+ " AND fy.id=f.FINANCIALYEARID AND F.ID=V.FISCALPERIODID AND b.PASSEDAMOUNT>0 "

						+ " GROUP BY gl.GLCODEID,c.GLCODE,C.NAME,w.CODE,w.NAME,w.TOTALVALUE"
						+ " ORDER BY \"WORKNUMBER\",\"Head Of Account\" ASC ";

				pst = HibernateUtil.getCurrentSession().createSQLQuery(query1);
				pst.setString(1, FinancialYear);
				pst.setString(2, WORKNUMBER);
				pst.setString(3, glcodeIDParam);
				pst.setString(4, sDate);
				resultset1 = pst.list();
				// if(LOGGER.isInfoEnabled())     LOGGER.info("Query2 for Expenses for Beginning of the month---------->"+query1);
				for(Object[] element1 : resultset1){
					expenditureAmount = (element[2].toString() == null ? "0.00"
							: element[2].toString());
					iutBean.setExpenditureAmount(""
							+ new BigDecimal(Double
									.parseDouble(expenditureAmount)).setScale(
									2, BigDecimal.ROUND_HALF_UP));
					iutBean
							.setTotalExpenditure(""
									+ new BigDecimal(
											Double
													.parseDouble(expenditureAmount)
													+ Double
															.parseDouble(expenditureBillAdmittedAmount))
											.setScale(2,
													BigDecimal.ROUND_HALF_UP));
				} if(resultset1 == null || resultset1.size() == 0)  {
					expenditureAmount = "0.00";
					iutBean.setExpenditureAmount("0.00");
					if (expenditureBillAdmittedAmount != null)
						iutBean
								.setTotalExpenditure(""
										+ new BigDecimal(
												Double
														.parseDouble(expenditureBillAdmittedAmount))
												.setScale(
														2,
														BigDecimal.ROUND_HALF_UP));

				}
				// For Inside sub query

				list.add(iutBean);
			} // main while

		} catch (Exception e) {
			LOGGER.error("Error in getReport" + e.getMessage(), e);
			throw taskExp;
		}

	}

}
