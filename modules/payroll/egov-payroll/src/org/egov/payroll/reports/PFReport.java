package org.egov.payroll.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.services.providentfund.PFDelegate;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.EmployeeView;
import org.hibernate.jdbc.ReturningWork;

import freemarker.core.ParseException;

/**
 * @author Ilayaraja
 * 
 */
public class PFReport {
	private static final Logger LOGGER = Logger.getLogger(PFReport.class);
	private static final String SPACE = "&nbsp;";
	private static final String DEDUCTION = "deductions";
	private static final String EARNING = "earned";
	private static final String TYPE = "type";
	PFDelegate delegate = new PFDelegate();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
			Locale.getDefault());
	SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy",
			Locale.getDefault());
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
			Locale.getDefault());
	EmployeeView employeeView = null;
	PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
			.getPayrollExterInterface();

	/**
	 * To get the PF report for a particular employee
	 * 
	 * @param empId
	 * @param empCode
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<Object> getPFReport(String empId, String empCode,
			String fromDate, String toDate) throws Exception {
		LOGGER.info("==============================EmpId=" + empId
				+ ",empCode=" + empCode + ",fromDate=" + fromDate + ",toDate="
				+ toDate);
		List<Object> returnList = new ArrayList<Object>();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);

		try {

			double balance = 0;
			HashMap<String, String> returnMap = null;
			String pfAccountCodes[] = delegate.getPFAccountCodes("pfaccount");
			String pfIntExpAccountCodes[] = delegate
					.getPFAccountCodes("pfintexpaccount");
			String detailkey[] = delegate
					.getDetailKeyForEmp(new Integer(empId));
			double opBalance = delegate.getPFBalance(pfAccountCodes[0],
					detailkey[0], detailkey[1], fromDate, "report");

			returnMap = new HashMap<String, String>();
			returnMap.put("date", SPACE);
			returnMap.put(TYPE, "Op. Balance");
			returnMap.put(DEDUCTION, SPACE);
			returnMap.put(EARNING, SPACE);
			balance = opBalance;
			returnMap.put("balance", nf.format(balance));
			returnList.add(returnMap);

			PayRollService payRollService = PayrollManagersUtill
					.getPayRollService();
			AdvanceService advanceService = PayrollManagersUtill
					.getAdvanceService();

			CChartOfAccounts coa = payrollExternalInterface
					.getCChartOfAccountsByGlCode(pfAccountCodes[0]);

			// get the loan amount
			String array[] = new String[2];
			List loanAmtList = advanceService.getListOfSanctionedAdvanceAmount(
					new Integer(empId), coa, sdf.parse(fromDate),
					sdf.parse(toDate));
			HashMap loanAmtMap = new HashMap();
			for (int i = 0; i < loanAmtList.size(); i++) {
				array = loanAmtList.get(i).toString().split("->");
				loanAmtMap.put(sdf1.parse(array[0]), array[1] + "~L");
			}
			LOGGER.info("loanAmtMap=" + loanAmtMap);

			// get the loan repayment amount
			List loanRepayAmtList = advanceService
					.getListOfRepaidAdvanceAmount(new Integer(empId), coa,
							sdf.parse(fromDate), sdf.parse(toDate));
			HashMap loanRepayAmtMap = new HashMap();
			for (int i = 0; i < loanRepayAmtList.size(); i++) {
				array = loanRepayAmtList.get(i).toString().split("->");
				loanRepayAmtMap.put(sdf1.parse(array[0]), array[1] + "~R");
			}
			LOGGER.info("loanRepayAmtMap=" + loanRepayAmtMap);

			// get the monthly deduction amount
			List deductionsList = payRollService.GetListOfDeductionAmount(
					new Integer(empId), coa, sdf.parse(fromDate),
					sdf.parse(toDate));
			HashMap deductionMap = new HashMap();
			for (int i = 0; i < deductionsList.size(); i++) {
				array = deductionsList.get(i).toString().split("->");
				deductionMap.put(sdf1.parse(array[0]), array[1] + "~D");
			}
			LOGGER.info("deductionMap=" + deductionMap);

			// get the interest expense amount
			Map pfIntExpMap = new HashMap();
			if (pfIntExpAccountCodes != null && pfIntExpAccountCodes[0] != null
					&& !pfIntExpAccountCodes[0].equals("")) {
				pfIntExpMap = getPFInterestAmt(pfAccountCodes[0],
						pfIntExpAccountCodes[0], detailkey[0], detailkey[1],
						fromDate, toDate);
				LOGGER.info("pfIntExpMap=" + pfIntExpMap);
			}
			// get the other vouchers
			Map otherVouchersMap = getOtherVouchers(pfAccountCodes[0],
					detailkey[0], detailkey[1], fromDate, toDate);
			LOGGER.info("otherVouchersMap=" + otherVouchersMap);

			// merge it with all the amounts based on the voucher date
			Object key;
			Iterator third = deductionMap.keySet().iterator();
			while (third.hasNext()) {
				key = third.next();
				if (otherVouchersMap.containsKey(key)) {
					otherVouchersMap.put(key, otherVouchersMap.get(key) + "#"
							+ deductionMap.get(key));
				} else {
					otherVouchersMap.put(key, deductionMap.get(key));
				}
			}

			Iterator first = loanAmtMap.keySet().iterator();
			while (first.hasNext()) {
				key = first.next();
				if (otherVouchersMap.containsKey(key)) {
					otherVouchersMap.put(key, otherVouchersMap.get(key) + "#"
							+ loanAmtMap.get(key));
				} else {
					otherVouchersMap.put(key, loanAmtMap.get(key));
				}
			}

			Iterator second = loanRepayAmtMap.keySet().iterator();
			while (second.hasNext()) {
				key = second.next();
				if (otherVouchersMap.containsKey(key)) {
					otherVouchersMap.put(key, otherVouchersMap.get(key) + "#"
							+ loanRepayAmtMap.get(key));
				} else {
					otherVouchersMap.put(key, loanRepayAmtMap.get(key));
				}
			}

			Iterator fourth = pfIntExpMap.keySet().iterator();
			while (fourth.hasNext()) {
				key = fourth.next();
				if (otherVouchersMap.containsKey(key)) {
					otherVouchersMap.put(key, otherVouchersMap.get(key) + "#"
							+ pfIntExpMap.get(key));
				} else {
					otherVouchersMap.put(key, pfIntExpMap.get(key));
				}
			}

			Map sortedMap = new TreeMap(otherVouchersMap);

			LOGGER.info("sortedMap=" + sortedMap);
			Iterator sorted = sortedMap.keySet().iterator();
			while (sorted.hasNext()) {
				key = sorted.next();
				String val[] = StringUtils.split(sortedMap.get(key).toString(),
						"#");
				for (int i = 0; i < val.length; i++) {
					String values[] = StringUtils.split(val[i], "~");
					returnMap = new HashMap<String, String>();
					returnMap.put("date", sdf.format(key));
					if (values[1].equals("L")) {
						returnMap.put(TYPE, "PF Loan withdrawal");
						returnMap.put(DEDUCTION,
								nf.format(Double.parseDouble(values[0])));
						returnMap.put(EARNING, SPACE);
						balance = balance - Double.parseDouble(values[0]);
					} else if (values[1].equals("R")) {
						returnMap.put(TYPE, "PF Loan Repayment");
						returnMap.put(DEDUCTION, SPACE);
						returnMap.put(EARNING,
								nf.format(Double.parseDouble(values[0])));
						balance = balance + Double.parseDouble(values[0]);
					} else if (values[1].equals("D")) {
						returnMap.put(TYPE, "PF Subscriptions");
						returnMap.put(DEDUCTION, SPACE);
						returnMap.put(EARNING,
								nf.format(Double.parseDouble(values[0])));
						balance = balance + Double.parseDouble(values[0]);
					} else if (values[1].equals("I")) {
						returnMap.put(TYPE, "PF Interest Earning");
						returnMap.put(DEDUCTION, SPACE);
						returnMap.put(EARNING,
								nf.format(Double.parseDouble(values[0])));
						balance = balance + Double.parseDouble(values[0]);
					} else if (values[1].toString().length() > 4
							&& values[1].substring(0, 4).equals("OVDb")) // other
																			// vouchers
																			// debit
					{
						returnMap.put(TYPE,
								values[1].substring(4, values[1].length()));
						returnMap.put(DEDUCTION,
								nf.format(Double.parseDouble(values[0])));
						returnMap.put(EARNING, SPACE);
						balance = balance + Double.parseDouble(values[0]);
					} else if (values[1].toString().length() > 4
							&& values[1].substring(0, 4).equals("OVCr")) // other
																			// vouchers
																			// credit
					{
						returnMap.put(TYPE,
								values[1].substring(4, values[1].length()));
						returnMap.put(DEDUCTION, SPACE);
						returnMap.put(EARNING,
								nf.format(Double.parseDouble(values[0])));
						balance = balance + Double.parseDouble(values[0]);
					}
					returnMap.put("balance", nf.format(balance));
					returnList.add(returnMap);
				}
			}
			LOGGER.info("returnList=" + returnList);
		} catch (Exception e) {
			LOGGER.error("Exception while generating the PF Report="
					+ e.getMessage());
			throw e;
		}
		return returnList;
	}

	/**
	 * To get the PF interest expense amount. first , get the voucher id's for
	 * that particular employee with PF liabaility code second, get that
	 * correspondig debit entries voucherid's, if the debit account should be PF
	 * interest expense account third, get the amount from the general ledger
	 * detail table for employee
	 * 
	 * @param pfLiabailitycode
	 * @param pfIntExpCode
	 * @param detailkey
	 * @param detailtypeid
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Map<Object, String> getPFInterestAmt(final String pfLiabailitycode,
			final String pfIntExpCode, final String detailkey,
			final String detailtypeid, final String fromDate,
			final String toDate) throws Exception {
		final HashMap intMap = new HashMap();

		return HibernateUtil.getCurrentSession().doReturningWork(
				new ReturningWork<Map<Object, String>>() {
					Statement stmt = null;
					ResultSet rs = null;
					ResultSet rs2 = null;
					List<HashMap> results = null;

					@Override
					public Map<Object, String> execute(Connection connection)
							throws SQLException {
						try {

							final String from = getDateString(fromDate);
							final String to = getDateString(toDate);

							// get the voucherid list and pass it another query
							String sql = "select distinct gl.voucherheaderid from generalledger gl,voucherheader vh,accountdetailkey adk, "
									+ "  chartofaccounts coa,generalledgerdetail gld "
									+ "  where gld.generalledgerid=gl.id and gl.voucherheaderid = vh.id "
									+ "  and vh.name='PFInterest' "
									+ " and vh.status NOT IN (4) "
									+ "  and coa.id=gl.glcodeid and coa.glcode='"
									+ pfLiabailitycode
									+ "' "
									+ "  and gld.detailkeyid=adk.detailkey and gld.detailtypeid=adk.detailtypeid "
									+ "  and vh.voucherdate>='"
									+ getDateString(fromDate)
									+ "'  and vh.voucherdate<='"
									+ getDateString(toDate)
									+ "' "
									+ " and adk.detailkey="
									+ detailkey
									+ " and adk.detailtypeid=" + detailtypeid;
							LOGGER.info("first query==============" + sql);
							stmt = connection.createStatement();
							rs = stmt.executeQuery(sql);
							List voucheridList = new ArrayList();
							while (rs.next()) {
								voucheridList.add(rs
										.getString("voucherheaderid"));
							}

							if (!voucheridList.isEmpty()) {
								// second part
								try {
									stmt.close();
								} catch (Exception e) {
									LOGGER.error(e.getMessage());
								}
								stmt = connection.createStatement();
								String appendQry1 = "";

								for (int i = 0; i < voucheridList.size(); i++) {
									appendQry1 = appendQry1
											+ voucheridList.get(i).toString()
											+ ",";
								}
								appendQry1 = appendQry1.substring(0,
										appendQry1.length() - 1);
								String sql1 = null;
								try {
									sql1 = " select distinct gl.voucherheaderid from generalledger gl,voucherheader vh, chartofaccounts coa "
											+ "   where gl.voucherheaderid = vh.id "
											+ "   and vh.name='PFInterest' and vh.status NOT IN (4)   and coa.id=gl.glcodeid and coa.glcode='"
											+ pfIntExpCode
											+ "' "
											+ "   and vh.id in("
											+ appendQry1
											+ ") and vh.voucherdate>='"
											+ getDateString(fromDate)
											+ "' and vh.voucherdate<='"
											+ getDateString(toDate) + "' ";
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								// LOGGER.info("second query==="+sql1);
								ResultSet rs1 = stmt.executeQuery(sql1);
								List tempvoucheridList = new ArrayList();
								while (rs1.next()) {
									tempvoucheridList.add(rs1
											.getString("voucherheaderid"));
								}

								if (!tempvoucheridList.isEmpty()) {
									// third part
									try {
										stmt.close();
									} catch (Exception e) {
										LOGGER.error(e.getMessage());
									}
									stmt = connection.createStatement();
									String appendQry2 = "";

									for (int i = 0; i < tempvoucheridList
											.size(); i++) {
										appendQry2 = appendQry2
												+ tempvoucheridList.get(i)
														.toString() + ",";
									}
									appendQry2 = appendQry2.substring(0,
											appendQry2.length() - 1);
									String sql2 = " select vh.voucherdate,sum(gld.amount) as amount from generalledger gl,chartofaccounts coa,voucherheader vh, "
											+ " generalledgerdetail gld "
											+ " where vh.id = gl.voucherheaderid and gld.generalledgerid=gl.id and coa.id=gl.glcodeid and coa.glcode='"
											+ pfLiabailitycode
											+ "' "
											+ "  and gl.voucherheaderid IN ("
											+ appendQry2
											+ ") "
											+ "  and gld.DETAILKEYID="
											+ detailkey
											+ " and gld.DETAILTYPEID="
											+ detailtypeid
											+ " "
											+ " group by vh.voucherdate order by vh.voucherdate";
									// LOGGER.info("third query==="+sql2);
									ResultSet rs2 = stmt.executeQuery(sql2);

									while (rs2.next()) {
										try {
											intMap.put(sdf.parse(sdf.format(rs2
													.getDate("voucherdate"))),
													rs2.getDouble("amount")
															+ "~I");
										} catch (java.text.ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								}
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new SQLException(e.getMessage());
						} catch (SQLException sqlEx) {
							// TODO Auto-generated catch block
							throw sqlEx;
						} catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} finally {
							HibernateUtil.release(stmt, rs);
							HibernateUtil.release(stmt, rs2);
						}
						return intMap;
					}

				});

	}

	/**
	 * to get any other vouchers are passed for that employee with the PF
	 * liability account
	 * 
	 * @param pfLiabailitycode
	 * @param detailkey
	 * @param detailtypeid
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Map<Object, Object> getOtherVouchers(final String pfLiabailitycode,
			final String detailkey, final String detailtypeid,
			final String fromDate, final String toDate) throws Exception {
		final HashMap otherVouchersMap = new HashMap();

		final String pfLiabailitycodeStr = pfLiabailitycode;
		final String detailkeyStr = detailkey;
		final String detailtypeidStr = detailtypeid;

		return HibernateUtil.getCurrentSession().doReturningWork(
				new ReturningWork<Map<Object, Object>>() {
					Statement stmt = null;
					ResultSet rs1;
					List<String> dateAmountList = null;

					@Override
					public Map<Object, Object> execute(Connection connection)
							throws SQLException {
						try {

							final String from = getDateString(fromDate);
							final String to = getDateString(toDate);
							stmt = connection.createStatement();

							String sql = " select vh.voucherdate,vh.name,gl.debitamount,gl.creditamount,gld.amount as dtlamount "
									+ "  from generalledger gl,chartofaccounts coa,voucherheader vh,accountdetailkey adk ,generalledgerdetail gld "
									+ "  where  gl.voucherheaderid = vh.id "
									+ "  and vh.status NOT IN (4) "
									+ "  and ( (vh.name !='PFInterest' and vh.name!='Salary Journal' and vh.name!='Advance Payment') "
									+ "  or (vh.name='Bank Payment' and vh.description!='Advance Disbursement') ) "
									+ "  and gld.generalledgerid=gl.id and gl.glcodeid=coa.id and coa.glcode='"
									+ pfLiabailitycode
									+ "' "
									+ "  and gld.detailkeyid=adk.detailkey and gld.detailtypeid=adk.detailtypeid "
									+ "  and adk.detailkey="
									+ detailkey
									+ " and adk.detailtypeid="
									+ detailtypeid
									+ " "
									+ "  and vh.voucherdate>='"
									+ getDateString(fromDate)
									+ "'  and vh.voucherdate<='"
									+ getDateString(toDate) + "' " +

									"  order by vh.voucherdate";
							LOGGER.info("getOtherVouchers query===" + sql);
							rs1 = stmt.executeQuery(sql);
							while (rs1.next()) {
								double dbAmt = rs1.getDouble("debitamount");
								double crAmt = rs1.getDouble("creditamount");
								double amt = rs1.getDouble("dtlamount");
								String abbrStr = "";

								if (amt == 0) {
									continue;
								}

								if (dbAmt == 0) // if debit amount is zero, then
												// we assume
												// that,dtlamount is for credit
												// side.
								{
									abbrStr = "OVCr";
								} else if (crAmt == 0) // // if credit amount is
														// zero, then we
														// assume that,dtlamount
														// is for debit
														// side.
								{
									abbrStr = "OVDb";
								}

								Date voucherDate = sdf.parse(sdf.format(rs1
										.getDate("voucherdate")));
								if (otherVouchersMap.containsKey(voucherDate)) {
									otherVouchersMap.put(
											voucherDate,
											otherVouchersMap.get(voucherDate)
													.toString()
													+ "#"
													+ amt
													+ "~"
													+ abbrStr
													+ rs1.getString("name"));
								} else {
									otherVouchersMap.put(voucherDate, amt + "~"
											+ abbrStr + rs1.getString("name"));
								}
							}
						} catch (Exception e) {
							LOGGER.error("Exception while getOtherVouchers="
									+ e.getMessage());
						} finally {
							HibernateUtil.release(stmt, rs1);
						}
						return otherVouchersMap;
					}
				});

	}

	/**
	 * @param dateString
	 * @return retValue
	 * @throws Exception
	 */
	private String getDateString(String dateString) throws Exception {
		String retValue = "";
		try {
			if (!"".equals(dateString)) {
				Date dt = null;
				dt = new Date();
				dt = sdf.parse(dateString);
				retValue = formatter.format(dt);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in getDateString method=" + e.getMessage());
			throw e;
		}
		return retValue;
	}
}
