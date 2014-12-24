package org.egov.payroll.services.providentfund;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.recoveries.Recovery;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.pims.model.EmployeeView;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import freemarker.core.ParseException;

/**
 * @author eGov
 * 
 */
public class PFDelegate {
	private static final Logger LOGGER = Logger.getLogger(PFDelegate.class);
	private static final String FUNCTIONSTR = "function";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
			Locale.getDefault());
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
			Locale.getDefault());
	private PayrollExternalInterface payrollExternalInterface;
	private boolean isTestMode = false;

	public boolean isTestMode() {
		return isTestMode;
	}

	public void setTestMode(boolean isTestMode) {
		this.isTestMode = isTestMode;
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public void PFInterestCalculation(int userId, Date currentDate,
			List<String> mandatoryFields) throws ServletException, Exception {
		LOGGER.info("=======================PF Interest calculation method calling at="
				+ currentDate);
		double interestRate = 0;
		BigDecimal pfInterest = BigDecimal.ZERO;
		String detail[] = null;
		try {
			if (!isTestMode) {
				HibernateUtil.beginTransaction();
			}
			LinkedList<String> groupingByOrderList = new LinkedList<String>();
			EmployeeView employeeView = null;
			EmployeeView tmpEmployeeView = null;

			// to get all the active employees
			List empList = new ArrayList();

			if (mandatoryFields.contains("fund")) {
				groupingByOrderList.add("FundCode");
			}
			if (mandatoryFields.contains("department")) {
				groupingByOrderList.add("DeptCode");
			}
			if (mandatoryFields.contains(FUNCTIONSTR)) {
				groupingByOrderList.add("FunctionCode");
			}

			empList = payrollExternalInterface
					.searchEmployeeByGrouping(groupingByOrderList);

			String pfAccountCode[] = getPFAccountCodes("pfaccount");
			String pfIntExpAccountCode[] = getPFAccountCodes("pfintexpaccount");
			String tdsIdOfPfPayableAccCode = getTdsIdOfPFPayableAccountCode(pfAccountCode[0]);
			String frequency = getFrequency();
			interestRate = getPFInterestRate(sdf.format(currentDate));

			String detailKey = "", detailTypeId = "", fundCode = "", functionCode = "", deptCode = "", empId = "";
			LOGGER.info("Employee list size=" + empList.size());
			HashMap tmpEmpMap = null;
			String groupByCode = "";
			int noOfJV = 0;
			int noOfEmp = 0;
			String tempGroupByCode = "";
			double empPFBalAmt = 0;
			BigDecimal totEmpIntAmt = BigDecimal.ZERO;
			List tempFundwiseList = new ArrayList();
			for (int i = 0; i <= empList.size(); i++) {
				if (i < empList.size()) {
					employeeView = (EmployeeView) empList.get(i);

					// Try to skip the employee if below details not there.
					if (employeeView.getAssignment().getFundId() == null
							|| employeeView.getAssignment().getFunctionId() == null
							|| employeeView.getDeptId() == null) {
						LOGGER.error("PF calculation not considered due to Fund/Function/Department not defined for employeeCode="
								+ employeeView.getEmployeeCode());
						continue;
					}

					fundCode = ""
							+ employeeView.getAssignment().getFundId()
									.getCode();
					functionCode = ""
							+ employeeView.getAssignment().getFunctionId()
									.getCode();
					deptCode = "" + employeeView.getDeptId().getDeptCode();
					groupByCode = "";

					if (mandatoryFields.contains("fund")) {
						groupByCode += "" + fundCode;
					}
					if (mandatoryFields.contains("department")) {
						groupByCode += "" + deptCode;
					}
					if (mandatoryFields.contains(FUNCTIONSTR)) {
						groupByCode += "" + functionCode;
					}

					LOGGER.info("groupByCode====" + groupByCode);
					empId = "" + employeeView.getId();

					if (!"".equals(tempGroupByCode)
							&& !tempGroupByCode.equals(groupByCode)) {
						if (totEmpIntAmt.compareTo(BigDecimal.ZERO) != 0) {
							setParamValuesForCreateVoucher(userId,
									totEmpIntAmt, pfIntExpAccountCode[0],
									pfAccountCode[0], tdsIdOfPfPayableAccCode,
									tempFundwiseList, currentDate,
									tmpEmployeeView, mandatoryFields);
							noOfJV++;
						}
						tempFundwiseList = new ArrayList();
						totEmpIntAmt = BigDecimal.ZERO;
					}
					tmpEmployeeView = employeeView;
					LOGGER.info("empId=" + empId + "==fundCode==" + fundCode
							+ "==functionCode==" + functionCode
							+ "==deptCode==" + deptCode);

					// Get the emp Detail Key and detail type id.
					detail = getDetailKeyForEmp(Integer.parseInt(empId));
					detailKey = detail[0];
					detailTypeId = detail[1];
					// if detailkey is null for the employee, skip it
					LOGGER.info("##################### detailKey=" + detailKey);
					if (detailKey == null) {
						continue;
					}

					empPFBalAmt = getPFBalance(pfAccountCode[0], detailKey,
							detailTypeId, sdf.format(currentDate),
							"PFInterestCalculation");
					if (empPFBalAmt <= 0) {
						continue;
					}
					LOGGER.info("##################### pfBalance="
							+ empPFBalAmt);

					if ("Annual".equals(frequency)) {
						pfInterest = BigDecimal.valueOf(empPFBalAmt).multiply(
								BigDecimal.valueOf(interestRate / 100));
					} else if ("Monthly".equals(frequency)) {
						pfInterest = BigDecimal.valueOf(empPFBalAmt).multiply(
								BigDecimal.valueOf(interestRate / (12 * 100)));
						// pfInterest = empPFBalAmt * (interestRate/(12*100));
					}
					LOGGER.info("########## empId =" + Integer.parseInt(empId)
							+ "  =########### pfBalance=" + empPFBalAmt
							+ "####### pfInterestAmount" + pfInterest);

					totEmpIntAmt = totEmpIntAmt.add(pfInterest);

					noOfEmp++;
					LOGGER.info("##################### totEmpIntAmt="
							+ totEmpIntAmt + ",pfInterest=" + pfInterest
							+ ",detailKey=" + detailKey + ",groupcode=" + ""
							+ fundCode + "" + functionCode + "" + deptCode);
					tmpEmpMap = new HashMap();
					tmpEmpMap.put(VoucherConstant.DETAILKEYID, detailKey);
					tmpEmpMap.put(VoucherConstant.DETAILTYPEID, detailTypeId);
					tmpEmpMap.put(PayrollConstants.DETAIL_AMOUNT, pfInterest);
					tempFundwiseList.add(tmpEmpMap);

					tempGroupByCode = groupByCode;
				} else // to execute after last employee record.
				{
					if (totEmpIntAmt.compareTo(BigDecimal.ZERO) != 0) {
						setParamValuesForCreateVoucher(userId, totEmpIntAmt,
								pfIntExpAccountCode[0], pfAccountCode[0],
								tdsIdOfPfPayableAccCode, tempFundwiseList,
								currentDate, tmpEmployeeView, mandatoryFields);
						noOfJV++;
					}
				}
			}
			LOGGER.info("before committing the transaction in PF interest calculation");
			if (!isTestMode) {
				HibernateUtil.commitTransaction();
			}
			LOGGER.info("After committing the transaction in PF interest calculation");
			LOGGER.info("##################### noOfJV=" + noOfJV + ",noOfEmp="
					+ noOfEmp);
		} catch (EGOVRuntimeException ex) {
			LOGGER.error("EGOVRuntimeException Encountered!!!"
					+ ex.getMessage());
			HibernateUtil.rollbackTransaction();
			throw ex;
		} catch (Exception e) {
			LOGGER.error("Exception in PF interest calculation="
					+ e.getMessage());
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			if (!isTestMode) {
				HibernateUtil.closeSession();
			}
		}
	}

	private String getDateString(String dateString) throws Exception {
		String retValue = "";
		try {
			if (!"".equals(dateString)) {
				Date dt = new Date();
				dt = sdf.parse(dateString);
				retValue = formatter.format(dt);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in getDateString method=" + e.getMessage());
		}
		return retValue;
	}

	/**
	 * To get the employee detail key
	 * 
	 * @param empId
	 * @return
	 */
	public String[] getDetailKeyForEmp(final Integer empId) throws Exception {
		final String emp[] = new String[2];

		return HibernateUtil.getCurrentSession().doReturningWork(
				new ReturningWork<String[]>() {

					Statement stmt = null;
					ResultSet rs = null;

					String query = " select adk.id,adk.detailkey,adk.detailtypeid from accountdetailkey adk "
							+ " where detailtypeid=(select id from accountdetailtype where name='Employee') "
							+ " and adk.detailkey = " + empId;

					@Override
					public String[] execute(Connection connection)
							throws SQLException {

						try {
							stmt = connection.createStatement();
							rs = stmt.executeQuery(query);

							if (rs.next()) {
								emp[0] = rs.getString("detailkey");
								emp[1] = rs.getString("detailtypeid");
							}
						} catch (SQLException e) {
							throw e;
						} finally {
							HibernateUtil.release(stmt, rs);
						}
						return emp;
					}
				});

	}

	/**
	 * To get the glcode
	 * 
	 * @param id
	 *            -> glcode id
	 * @param whichId
	 *            --> which account is going to match
	 * @return glcode from chartofaccounts
	 */
	public String[] getPFAccountCodes(String whichId) throws Exception {
		final String glcode[] = new String[3];
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		String query = "";
		if (whichId.equalsIgnoreCase("pfaccount")) {
			query = "select coa.glcode as glcode,coa.name as name,coa.id as id from chartofaccounts coa, egpay_pfheader pfh "
					+ " where pfh.pfaccountid= coa.id and pfh.pf_type='GPF' and coa.isactiveforposting=1 ";
		} else if (whichId.equalsIgnoreCase("pfintexpaccount")) {
			query = " select coa.glcode as glcode,coa.name as name,coa.id as id from chartofaccounts coa, egpay_pfheader pfh "
					+ " where pfh.pfintexpaccountid= coa.id and pfh.pf_type='GPF' and coa.isactiveforposting=1 ";
		}
		final String queryStr = query;

		return HibernateUtil.getCurrentSession().doReturningWork(
				new ReturningWork<String[]>() {
					Statement stmt = null;
					ResultSet rs = null;

					@Override
					public String[] execute(Connection connection)
							throws SQLException {
						try {
							stmt = connection.createStatement();
							rs = stmt.executeQuery(queryStr);

							if (rs.next()) {
								glcode[0] = rs.getString("glcode");
								glcode[1] = rs.getString("name");
								glcode[2] = rs.getString("id");
							}
						} catch (SQLException sqle) {
							throw sqle;
						} finally {
							HibernateUtil.release(stmt, rs);

						}
						return glcode;
					}
				});
	}

	/**
	 * To get the tds id
	 * 
	 * @param id
	 *            -> glcode id
	 * @param glcode
	 *            --> for which account is going to match
	 * @return tds id from tds
	 */
	public String getTdsIdOfPFPayableAccountCode(String glcode)
			throws Exception {
		String tdsId = "";
		Query querytds = HibernateUtil
				.getCurrentSession()
				.createQuery(
						"select T from Recovery T where T.chartofaccounts.glcode=:glcode ");
		querytds.setString("glcode", glcode);
		Recovery tds = (Recovery) querytds.uniqueResult();
		if (tds != null && tds.getId() != null) {
			tdsId = tds.getId().toString();
		}
		return tdsId;
	}

	/**
	 * To get the PF balance of the PF account
	 * 
	 * @param glCode
	 * @param detailKey
	 * @param detailTypeId
	 * @param fromDate
	 * @return
	 */
	public double getPFBalance(final String glCode, final String detailKey,
			final String detailTypeId, final String fromDate,
			final String fromWhere) throws Exception {
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Double>() {
			Statement stmt = null;
			ResultSet rs = null;
			double openingBalance  =0;
			public Double execute(Connection connection) throws SQLException
			{
		try
		{
			
			
			stmt = connection.createStatement(); 
			Date tmpDt = new Date();
			try {
				tmpDt = sdf.parse(fromDate);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Calendar cal = GregorianCalendar.getInstance(); 
			cal.setTime(tmpDt);
			if(fromWhere.equalsIgnoreCase("report")){  // if report means, get the op.bal for previous date
				cal.add(Calendar.DATE, -1);
			}
			String tempFromDate = sdf.format(cal.getTime()); 
			
			String query = null;
			try {
				query = " select slID,opgcreditbal-opgdebitbal+prevcredit-prevdebit as OpeningBalance from " +
						" ( SELECT distinct adk.detailkey SLID, " +
						" NVL((SELECT SUM(ts.openingcreditbalance) FROM transactionsummary ts,generalledger gl WHERE ts.glcodeid=coa.id " +
						" AND ts.financialyearid=fy.id and ts.accountdetailkey=adk.detailkey and ts.accountdetailtypeid=adk.detailtypeid and gl.glcodeid=ts.glcodeid and gl.glcode='"+glCode+"'),0) OpgCreditBal, " +
						" NVL((SELECT SUM(ts.openingdebitbalance) FROM transactionsummary ts,generalledger gl WHERE ts.glcodeid=coa.id AND " +
						" ts.financialyearid=fy.id and ts.accountdetailkey=adk.detailkey and ts.accountdetailtypeid=adk.detailtypeid and gl.glcodeid=ts.glcodeid and gl.glcode='"+glCode+"'),0) OpgDebitBal, " +
						" NVL((SELECT SUM(gld.amount) FROM generalledgerdetail gld,generalledger gl,voucherheader vh WHERE gld.generalledgerid=gl.id and " +
						" gl.VOUCHERHEADERID=vh.id and gld.detailkeyid=adk.detailkey and gld.detailtypeid=adk.detailtypeid " +
						" AND vh.voucherdate>= fy.startingdate AND vh.voucherdate<= '"+getDateString(tempFromDate)+"' " +
						" AND vh.status NOT IN (4) AND gl.glcode='"+glCode+"' AND gl.debitamount>0),0) PrevDebit, " +
						" NVL((SELECT SUM(gld.amount) FROM generalledgerdetail gld,generalledger gl,voucherheader vh WHERE gld.generalledgerid=gl.id and " +
						" gl.VOUCHERHEADERID=vh.id AND gld.detailkeyid=adk.detailkey and gld.detailtypeid=adk.detailtypeid " +
						" and vh.voucherdate>= fy.startingdate AND vh.voucherdate<= '"+getDateString(tempFromDate)+"' " +
						" AND vh.status NOT IN (4) AND gl.glcode='"+glCode+"' AND gl.creditamount>0),0) PrevCredit " +
						" FROM chartofaccounts coa, financialyear fy,accountdetailkey adk WHERE fy.isactiveforposting=1 and fy.startingdate<='"+getDateString(fromDate)+"' AND " +
						" fy.endingdate>='"+getDateString(fromDate)+"' AND adk.detailkey="+detailKey+" and adk.detailtypeid="+detailTypeId+" and coa.glcode='"+glCode+"' )";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LOGGER.info("PFBalance query===="+query);
			rs = stmt.executeQuery(query);
			if(rs.next())
			{
				openingBalance = rs.getDouble("OpeningBalance");
			}
			
		}
		catch(SQLException e)
		{
			LOGGER.error("Exception while getting the PF Balance="+e.getMessage());
			throw e;
		}		
		
		finally{
			HibernateUtil.release(stmt, rs);
		}
		return openingBalance;
			}
		});


	}

	/**
	 * To get the frequency for the PF account id
	 * 
	 * @param pfAccountId
	 * @return
	 */
	public String getFrequency() throws Exception {
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			Statement stmt = null;
			ResultSet rs = null;
			
			public String execute(Connection connection) throws SQLException
			{
				String frequency="";
					
				try
				{
					stmt = connection.createStatement();
					String query = "select distinct frequency as freq from egpay_pfheader where pf_type='GPF' ";
					rs = stmt.executeQuery(query);
					if(rs.next()){
						frequency = rs.getString("freq");
					}
					
				}
				catch(SQLException e)
				{
					LOGGER.error("Exception while getting the frequency="+e.getMessage());
				}
				finally{
					HibernateUtil.release(stmt, rs);
				}
			return frequency;
			}
		});

	}

	/**
	 * To get the interest rate for the particular date
	 * 
	 * @param fromDate
	 * @param pfAccountId
	 * @return
	 */
	public double getPFInterestRate(final String fromDate) throws Exception {
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Double>() {
			Statement stmt = null;
			ResultSet rs = null;
			double interestRate = 0;
			public Double execute(Connection connection) throws SQLException
			{
				try
				{
					stmt = connection.createStatement();
					String query = null;
					try {
						query = " select distinct pfd.annualrateofinterest as interest from egpay_pfdetails pfd "+
								" where pfd.fromdate<='"+getDateString(fromDate)+"' and ( pfd.todate>='"+getDateString(fromDate)+"' or  pfd.todate is null) ";
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rs= stmt.executeQuery(query);
					if(rs.next()){
						interestRate = rs.getDouble("interest");
					}
				}
				catch(SQLException e)
				{
					LOGGER.error("Exception while getting the PF Interest rate="+e.getMessage());
					throw e;
				}
				finally
				{
					HibernateUtil.release(stmt, rs);
				}
				return interestRate;
			}
		});

	}

	/**
	 * To get the financial year end date
	 * 
	 * @param currentDate
	 * @return
	 */
	public Date getFinancialYearEndDate(Date currentDate) throws Exception {
		final Date currentDateFinal=currentDate;
		return	HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Date>() {
				Date enddate=null;
				Statement stmt = null;
				ResultSet rs = null;
				public Date execute(Connection connection) throws SQLException
				{
					try
					{
						stmt = connection.createStatement();
						String query = " select endingdate from financialyear where startingdate<='"+
						formatter.format(currentDateFinal)+"' and endingdate>='"+formatter.format(currentDateFinal)+"' ";
						rs= stmt.executeQuery(query);
						if(rs.next()){
							enddate = rs.getDate("endingdate");
						}
					}
					catch(SQLException e)
					{
						LOGGER.error("Exception while getting the getFinancialYearEndDate="+e.getMessage());
						throw e;
					}
					finally{
						HibernateUtil.release(stmt, rs);
					}
					return enddate;
					
				}
			});

	}

	public void setParamValuesForCreateVoucher(int userid, BigDecimal totalAmt,
			String pfExpenseAccount, String pfPayableAccount, String tdsId,
			List tempList, Date currentDate, EmployeeView employeeView,
			List<String> mandatoryFields) throws Exception {
		Connection con = null;

		try {
			con = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Connection>() {
				public Connection execute(Connection connection)
				{
					return connection;
				}
			});

			// Setting voucher header detials
			HashMap<String, Object> headerdetails = new HashMap<String, Object>();
			headerdetails.put(VoucherConstant.VOUCHERNAME, "PFInterest");
			headerdetails.put(VoucherConstant.VOUCHERTYPE, "Journal Voucher");
			headerdetails.put(VoucherConstant.DESCRIPTION, "");
			headerdetails.put(VoucherConstant.VOUCHERDATE, currentDate);
			String fundcode = employeeView.getAssignment().getFundId()
					.getCode();
			String functioncode = employeeView.getAssignment().getFunctionId()
					.getCode();
			String deptcode = employeeView.getAssignment().getDeptId()
					.getDeptCode();

			if (mandatoryFields.contains("fund")) {
				headerdetails.put(VoucherConstant.FUNDCODE, fundcode);
			}
			if (mandatoryFields.contains("department")) {
				headerdetails.put(VoucherConstant.DEPARTMENTCODE, deptcode);
			}

			Module egModule = (Module) GenericDaoFactory.getDAOFactory()
					.getModuleDao().getModuleByName("Provident Fund");
			if (egModule != null) {
				headerdetails.put(VoucherConstant.MODULEID, egModule.getId()
						.toString());
			}
			// Setting account details
			List<HashMap<String, Object>> accountcodedetails = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> accountMap = new HashMap<String, Object>();
			accountMap.put(VoucherConstant.GLCODE, pfExpenseAccount);
			accountMap.put(VoucherConstant.DEBITAMOUNT, totalAmt);
			accountMap.put(VoucherConstant.CREDITAMOUNT, 0);
			if (mandatoryFields.contains(FUNCTIONSTR)) {
				accountMap.put(VoucherConstant.FUNCTIONCODE, "" + functioncode);
			}
			accountcodedetails.add(accountMap);

			accountMap = new HashMap<String, Object>();
			accountMap.put(VoucherConstant.GLCODE, pfPayableAccount);
			accountMap.put(VoucherConstant.DEBITAMOUNT, 0);
			accountMap.put(VoucherConstant.CREDITAMOUNT, totalAmt);
			if (mandatoryFields.contains(FUNCTIONSTR)) {
				accountMap.put(VoucherConstant.FUNCTIONCODE, "" + functioncode);
			}
			accountcodedetails.add(accountMap);

			// Setting subledger details
			List<HashMap<String, Object>> subledgerdetails = new ArrayList<HashMap<String, Object>>();

			// debit
			if(!payrollExternalInterface.getAccountdetailtypeListByGLCode(pfExpenseAccount).isEmpty())
			{
				HashMap<String,Object> empMap = null;
				for(int i=0;i<tempList.size();i++)
				{
					empMap = new HashMap<String,Object>();
					empMap = (HashMap) tempList.get(i);
					empMap.put(VoucherConstant.GLCODE, pfExpenseAccount);
					empMap.put(VoucherConstant.DETAILKEYID, empMap.get(VoucherConstant.DETAILKEYID).toString());
					empMap.put(VoucherConstant.DETAILTYPEID, empMap.get(VoucherConstant.DETAILTYPEID).toString());
					empMap.put(VoucherConstant.DEBITAMOUNT, empMap.get(PayrollConstants.DETAIL_AMOUNT).toString());
					subledgerdetails.add(empMap);
				}
			}
			
			// credit the PF Payable Account  with individual sub-ledgers being updated.
			
			if(!payrollExternalInterface.getAccountdetailtypeListByGLCode(pfPayableAccount).isEmpty())
			{
				HashMap<String,Object> empMap = null;
				for(int i=0;i<tempList.size();i++)
				{
					empMap = new HashMap<String,Object>();
					empMap = (HashMap) tempList.get(i);
					empMap.put(VoucherConstant.GLCODE, pfPayableAccount);
					if(!tdsId.equals("")){ //Will pass the tdsid only if recover account is there for glcode.
						empMap.put(VoucherConstant.TDSID, tdsId);
					}
					empMap.put(VoucherConstant.DETAILKEYID, empMap.get(VoucherConstant.DETAILKEYID).toString());
					empMap.put(VoucherConstant.DETAILTYPEID, empMap.get(VoucherConstant.DETAILTYPEID).toString());
					empMap.put(VoucherConstant.CREDITAMOUNT, empMap.get(PayrollConstants.DETAIL_AMOUNT).toString());
					subledgerdetails.add(empMap);
				}
			}
			EGOVThreadLocals.setUserId(String.valueOf(userid));
			CreateVoucher cv = new CreateVoucher();
			headerdetails.put("budgetCheckReq", true);
			CVoucherHeader vh = cv.createVoucher(headerdetails, accountcodedetails, subledgerdetails);
			LOGGER.info("voucher================================"+vh.getId());
		
		}
		catch(EGOVRuntimeException egovE)
		{
			LOGGER.error("Exception = "+egovE.getMessage());
			throw egovE;
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in setParamValuesForCreateVoucher in PFDelegate=="+e.getMessage());
		}
		finally
		{
			try{
				con.close();
			}catch(Exception e){LOGGER.error(e);}
		}

	}

}
