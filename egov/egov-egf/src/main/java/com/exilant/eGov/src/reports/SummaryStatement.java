/*
 * Created on Aug 27, 2008
 * @author Iliyaraja S
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;


import com.exilant.exility.common.TaskFailedException;

public class SummaryStatement {
	Connection con, con1;
	PreparedStatement pstmt;
	PreparedStatement pst;
	ResultSet resultset;
	ResultSet resultset1;
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

		try {
			con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
			con1 = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();

		} catch (Exception exception) {
			LOGGER.error("Could Not Get Connection", exception);
			throw taskExp;
		}

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
			con.close();
			con1.close();
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
		pstmt = con.prepareStatement(query);
		pstmt.setString(j++, FinancialYear);
		pstmt.setString(j++, sDate);
		if (!wonumber.equals(""))
			pstmt.setString(j++, wonumber);
		if (!workname.equals(""))
			pstmt.setString(j++, workname);
		if (!woDate.equals(""))
			pstmt.setString(j++, woDate);
		resultset = pstmt.executeQuery();
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
			while (resultset.next()) {

				iutBean = new SummaryStatementBean();

				headOfAccount = resultset.getString("Head Of Account");
				WORKNUMBER = resultset.getString("WORKNUMBER");
				wdPassedAmount = resultset.getString("worksPassedAmount");
				glcodeIDParam = resultset.getString("glcodeId");
				nameOfProject = resultset.getString("Project Name");
				valueOfWorkAmount = resultset
						.getString("Value ofWork/ContractorAmount");
				expenditureBillAdmittedAmount = resultset
						.getString("Expenditure(During this Month)");

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

				pst = con1.prepareStatement(query1);
				pst.setString(1, FinancialYear);
				pst.setString(2, WORKNUMBER);
				pst.setString(3, glcodeIDParam);
				pst.setString(4, sDate);
				resultset1 = pst.executeQuery();
				// if(LOGGER.isInfoEnabled())     LOGGER.info("Query2 for Expenses for Beginning of the month---------->"+query1);
				if (resultset1.next()) {
					expenditureAmount = (resultset1.getString("ExpB") == null ? "0.00"
							: resultset1.getString("ExpB"));
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
				} else {
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
				resultset1.close();
				pst.close();

				list.add(iutBean);
			} // main while

			resultset.close();
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Error in getReport" + e.getMessage(), e);
			throw taskExp;
		}

	}

}
