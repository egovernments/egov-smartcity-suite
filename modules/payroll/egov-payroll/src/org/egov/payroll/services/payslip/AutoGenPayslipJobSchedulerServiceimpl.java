package org.egov.payroll.services.payslip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class AutoGenPayslipJobSchedulerServiceimpl implements
		AutoGenPayslipJobSchedulerService {

	public AutoGenPayslipJobSchedulerServiceimpl() {
	}

	private static final Logger logger = Logger
			.getLogger(AutoGenPayslipJobSchedulerServiceimpl.class);

	public Boolean autoGenPaySlipForEmp(Integer empId, Integer month,
			Integer finYear) {

		/*
		 * try {
		 * 
		 * logger.debug("finYear >>>>> " + finYear); PayRollManager
		 * paymgr=PayrollManagersUtill.getPayRollManager(); EmpPayroll paySlip =
		 * paymgr.getPayslipForEmpByMonthAndYear(empId, month,new
		 * Integer(finYear)); if (paySlip == null) {
		 * logger.debug("finYear >>>>> " + finYear); GregorianCalendar calendar
		 * = new GregorianCalendar(); EmpPayroll currPay = new EmpPayroll();
		 * EmpPayroll lastPay =
		 * paymgr.getPrevApprovedPayslipForEmpByMonthAndYear(empId, month,new
		 * Integer(finYear)); logger.debug("\nlast Pay Obj" + lastPay); if
		 * (lastPay != null) { List<SalaryCodes> salaryCodesList
		 * =PayrollManagersUtill.getPayRollManager().getOrderedSalaryCodes();
		 * EgwStatus egwStatus = (EgwStatus)
		 * PayrollManagersUtill.getCommonsManager
		 * ().getStatusByModuleAndDescription( PayrollConstants.PAYSLIP_MODULE,
		 * PayrollConstants.PAYSLIP_CREATED_STATUS);
		 * 
		 * currPay.setEmployee(lastPay.getEmployee());
		 * currPay.setCreatedby(lastPay.getCreatedby());
		 * currPay.setCreateddate(new Date());
		 * //PayrollManagersUtill.getCommonsManager().findFinancialYearById(new
		 * Long(finYear));
		 * currPay.setFinancialyear(PayrollManagersUtill.getCommonsManager
		 * ().findFinancialYearById(new Long(finYear)));
		 * 
		 * EmpLeaveManager emplvmgr = PayrollManagersUtill.getEmpLeaveManager();
		 * 
		 * EmployeeAttendenceReport empatcreport =
		 * emplvmgr.getEmployeeAttendenceReport
		 * (month,currPay.getEmployee(),currPay.getFinancialyear());
		 * 
		 * int noofdaysmonth=empatcreport.getDaysInMonth()==null ? 0 :
		 * empatcreport.getDaysInMonth(); int
		 * noofabsents=empatcreport.getNoOfAbsents()== null ? 0
		 * :empatcreport.getNoOfAbsents(); float noofunpaidleaves =
		 * empatcreport.getNoOfUnPaidleaves()==null?0:
		 * empatcreport.getNoOfUnPaidleaves(); SimpleDateFormat formatter = new
		 * SimpleDateFormat("dd/MM/yyyy"); String year="0";
		 * 
		 * 
		 * GregorianCalendar caltemp = new GregorianCalendar();
		 * 
		 * if((month-(currPay.getFinancialyear().getStartingDate().getMonth()+1))
		 * <0) { year =
		 * formatter.format(currPay.getFinancialyear().getEndingDate
		 * ()).split("/")[2]; } else { year =
		 * formatter.format(currPay.getFinancialyear
		 * ().getStartingDate()).split("/")[2]; }
		 * 
		 * caltemp.set(Integer.parseInt(year), (month-1), 1);
		 * logger.info("year=-=="+year+"month="+month);
		 * logger.info("noofdaysmonth ="+ noofdaysmonth + "noofabscents="+
		 * (noofabsents+noofunpaidleaves));
		 * //logger.info("empatcreport.getNoOfOverTimes()"
		 * +empatcreport.getNoOfOverTimes()+"noofpaidleaves="+noofpaidleaves);
		 * logger.info("new Date(year,month,1)=="+caltemp.getTime());
		 * //logger.info
		 * ("\n emp assaignment="+PayrollManagersUtill.getEisManager
		 * ().getAssignmentByEmpAndDate(caltemp.getTime(),empId));
		 * //currPay.setEmpAssignment
		 * (PayrollManagersUtill.getEisManager().getAssignmentByEmpAndDate
		 * (caltemp.getTime(),empId));
		 * currPay.setNumdays(Math.round(noofdaysmonth
		 * -(noofabsents+noofunpaidleaves))); currPay.setStatus(egwStatus);
		 * currPay.setMonth(new BigDecimal(month)); PayStructure payStructure =
		 * getPayStructureByEmp(lastPay.getEmployee());
		 * calendar.setTime(payStructure.getAnnualIncrement()); int increMonth =
		 * calendar.get(Calendar.MONTH); BigDecimal incrementMonth = new
		 * BigDecimal(increMonth).add(new BigDecimal(1));
		 * logger.info("month >>>>> " + incrementMonth); BigDecimal
		 * grossPayTotal = new BigDecimal(0); BigDecimal basicAmount = new
		 * BigDecimal(0); BigDecimal headAmount = new BigDecimal(0); BigDecimal
		 * pct = new BigDecimal(0); float flatBasicAmount = 0; Set earningsSet =
		 * new HashSet(); Set<Earnings> lastPayearningses = new
		 * HashSet<Earnings>(lastPay.getEarningses()); if (salaryCodesList !=
		 * null && !salaryCodesList.isEmpty()) { for (SalaryCodes sal :
		 * salaryCodesList) { for (Earnings ear: lastPayearningses) { if
		 * (sal.getId().equals(ear.getSalaryCodes().getId())) { Earnings currEar
		 * = new Earnings(); currEar.setEmpPayroll(currPay);
		 * currEar.setSalaryCodes(sal); if (sal.getHead().equals("Basic")&&
		 * sal.getCalType().equals("MonthlyFlatRate")) {
		 * 
		 * if (incrementMonth.equals(month)) { basicAmount =
		 * lastPay.getBasicPay(
		 * ).add(payStructure.getPayHeader().getIncrement()); } else basicAmount
		 * = lastPay.getBasicPay(); flatBasicAmount = basicAmount.floatValue();
		 * currPay.setBasicPay(basicAmount); logger.info("basicAmount >>>>> "+
		 * flatBasicAmount); double temp1=flatBasicAmount/ noofdaysmonth;
		 * basicAmount = new
		 * BigDecimal(temp1*(noofdaysmonth-(noofabsents+noofunpaidleaves)));
		 * 
		 * grossPayTotal = grossPayTotal.add(basicAmount);
		 * currEar.setAmount(basicAmount);
		 * 
		 * logger.info("grossPayTotal b >>>>> "+ grossPayTotal); } else if
		 * (sal.getCalType().equals("MonthlyFlatRate")) {
		 * 
		 * //basicAmount = ear.getAmount(); flatBasicAmount =
		 * Math.round(ear.getAmount().floatValue());
		 * 
		 * logger.info("basicAmount121 >>>>> "+ flatBasicAmount); grossPayTotal
		 * = grossPayTotal.add(new BigDecimal(flatBasicAmount));
		 * currEar.setAmount(new BigDecimal(flatBasicAmount));
		 * logger.info("grossPayTotal b121 >>>>> "+ grossPayTotal); } if
		 * (ear.getPct() != null && !ear.getPct().equals("") &&
		 * basicAmount.intValue() >= 0) { pct = ear.getPct().divide(new
		 * BigDecimal(100)); // logger.info("pct bb >>>>> " + pct); headAmount =
		 * pct.multiply(basicAmount); float computedAmount =
		 * Math.round(headAmount.floatValue()); //
		 * logger.info("headAmount bb >>>>> "+computedAmount);
		 * currEar.setAmount(new BigDecimal(computedAmount));
		 * currEar.setPct(ear.getPct()); BigDecimal temp3 = new
		 * BigDecimal(computedAmount); grossPayTotal = grossPayTotal.add(temp3);
		 * logger.info("grossPayTotal bb >>>>> "+ grossPayTotal); }
		 * logger.info("currEar.setAmount >>>>> "+ currEar.getAmount());
		 * 
		 * earningsSet.add(currEar); } } } if (!earningsSet.isEmpty()) {
		 * currPay.setEarningses(earningsSet); } logger.info("gross pay >>>>> "
		 * + grossPayTotal); currPay.setGrossPay(grossPayTotal); BigDecimal
		 * deductionTotal = new BigDecimal(0); BigDecimal netPay = new
		 * BigDecimal(0); Set currDeductionses = new HashSet(); Set deductionses
		 * = new HashSet(lastPay.getDeductionses()); if (deductionses != null &&
		 * deductionses.size() != 0) { for (Iterator itr1 =
		 * deductionses.iterator(); itr1.hasNext();) { Deductions lastPayDed =
		 * (Deductions) itr1.next(); Deductions currPayDed = new Deductions();
		 * currPayDed.setAmount(lastPayDed.getAmount());
		 * currPayDed.setEmpPayroll(currPay); if (lastPayDed.getSalaryCodes() !=
		 * null) { currPayDed.setSalaryCodes(lastPayDed.getSalaryCodes()); } //
		 * CR: for advances check Pending Amount //update pending amount - // if
		 * pending amount < installment amt , deduct pending amt if
		 * (lastPayDed.getSalAdvances() != null &&
		 * lastPayDed.getSalAdvances().getPendingAmt().intValue() > 0) {
		 * if(lastPayDed
		 * .getSalAdvances().getPendingAmt().floatValue()<lastPayDed
		 * .getSalAdvances().getInstAmt().floatValue()) {
		 * currPayDed.setAmount(lastPayDed.getSalAdvances().getPendingAmt()); }
		 * lastPayDed.getSalAdvances().setPendingAmt(new
		 * BigDecimal((lastPayDed.getSalAdvances
		 * ().getPendingAmt().doubleValue()-
		 * currPayDed.getAmount().doubleValue())));
		 * currPayDed.setSalAdvances(lastPayDed.getSalAdvances()); } if
		 * (lastPayDed.getChartofaccounts() != null) {
		 * currPayDed.setChartofaccounts(lastPayDed.getChartofaccounts()); }
		 * 
		 * deductionTotal = deductionTotal.add(currPayDed.getAmount());
		 * currDeductionses.add(currPayDed); } if (!currDeductionses.isEmpty())
		 * { currPay.setDeductionses(currDeductionses); } }
		 * logger.info("deductionTotal >>>>> " + deductionTotal); netPay =
		 * grossPayTotal.subtract(deductionTotal); logger.info("netPay >>>>>  "
		 * + netPay); currPay.setNetPay(netPay); } else
		 * logger.error("No Salary Codes are available");
		 * 
		 * PayRollManager pmgr=PayrollManagersUtill.getPayRollManager();
		 * pmgr.createPayslip(currPay); logger.info("Pay Slip is created"); } }
		 * else { logger.error("PaySlip Already Exists for the month"); return
		 * false; } return true; } catch (HibernateException he) {
		 * he.printStackTrace();
		 * logger.error("Hibernate Exception closing session:" +
		 * he.getMessage()); throw he;
		 * 
		 * } catch (Exception e) { logger.error("Exception closing session:" +
		 * e.getMessage()); e.printStackTrace(); return false; }
		 */
		return false;

	}

	public void autoGenPayslipJob(String cityURL, String jndi,
			String hibFactName)  {

		logger.debug("TestEJB Job inside");
		logger.debug("City URL in Job : " + cityURL);
		logger.debug("jndi nae  >>>>  " + jndi);
		logger.debug("hibernate faCTORY name >>>>> " + hibFactName);
		/*
		 * EGOVThreadLocals.setDomainName(cityURL);
		 * EGOVThreadLocals.setJndiName(jndi);
		 * EGOVThreadLocals.setHibFactName(hibFactName);
		 */
		SetDomainJndiHibFactNames.setThreadLocals(cityURL, jndi, hibFactName);
		GregorianCalendar calendar = new GregorianCalendar();

		if (HibernateUtil.getCurrentSession() != null) {
			try {
				PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
						.getPayrollExterInterface();
				String finYear = payrollExternalInterface.getCurrYearFiscalId();
				logger.debug("finYear >>>>> " + finYear);
				int month = calendar.get(Calendar.MONTH);
				BigDecimal monthPay = new BigDecimal(month).add(BigDecimal.ONE);
				logger.debug("month >>>>> " + monthPay);

				List paySlipList = new ArrayList();// PayrollManagersUtill.getPayRollManager().getPrevApprovedPayslipsByMonthAndYear(month,new
													// Integer(finYear));

				logger.debug("paySlipList >>>>> " + paySlipList.size());

				if (!paySlipList.isEmpty() && paySlipList != null) {
					for (Iterator iter = paySlipList.iterator(); iter.hasNext();) {
						EmpPayroll lastPay = (EmpPayroll) iter.next();
						autoGenPaySlipForEmp(lastPay.getEmployee()
								.getIdPersonalInformation(),
								monthPay.intValue(), new Integer(finYear));
					}
				}

				HibernateUtil.getCurrentSession().flush();

			} catch (HibernateException he) {
				logger.error("Hibernate Exception closing session:"
						+ he.getMessage());
				// he.printStackTrace();
				throw he;
			} catch (Exception e) {
				logger.error("Exception closing session:" + e.getMessage());
				// e.printStackTrace();
			}
		} else {
			logger.error("Session is null");
		}

	}

	/*
	 * this api will get call when the user manually rise the create
	 * supplimentary payslip action
	 */
	public Boolean createSuppPaySlip(Integer empId, Integer month,
			Integer finYear, EmployeeAttendenceReport empatcreport)
			throws Exception {
		/*
		 * try {
		 * 
		 * 
		 * PayRollManager paymgr=PayrollManagersUtill.getPayRollManager();
		 * logger
		 * .info("empId="+empId+":::::month="+month+":::::finYear"+finYear); //
		 * EmpPayroll paySlip = paymgr.getPaySlipForEmployeeByMonthExists(empId,
		 * month,new Integer(finYear)); // if (paySlip == null) {
		 * 
		 * EmpPayroll currPay = new EmpPayroll(); EmpPayroll lastPay =
		 * paymgr.getPrevApprovedPayslipForEmpByMonthAndYear(empId, month,new
		 * Integer(finYear)); logger.debug("\nlast Pay Obj" + lastPay); if
		 * (lastPay != null) {
		 * 
		 * EgwStatus egwStatus = (EgwStatus)
		 * PayrollManagersUtill.getCommonsManager
		 * ().getStatusByModuleAndDescription( PayrollConstants.PAYSLIP_MODULE,
		 * PayrollConstants.PAYSLIP_CREATED_STATUS);
		 * 
		 * currPay.setEmployee(lastPay.getEmployee());
		 * currPay.setCreatedby(lastPay.getCreatedby());
		 * currPay.setCreateddate(new Date());
		 * currPay.setPayType(PayrollConstants
		 * .EMP_PAYSLIP_PAYTYPE_SUPPLEMENTARY);
		 * //PayrollManagersUtill.getCommonsManager().findFinancialYearById(new
		 * Long(finYear));
		 * currPay.setFinancialyear(PayrollManagersUtill.getCommonsManager
		 * ().findFinancialYearById(new Long(finYear)));
		 * 
		 * int noofdaysmonth=empatcreport.getDaysInMonth()==null ? 0 :
		 * empatcreport.getDaysInMonth(); int
		 * noofabsents=empatcreport.getNoOfAbsents()== null ? 0
		 * :empatcreport.getNoOfAbsents(); float noofunpaidleaves =
		 * empatcreport.getNoOfUnPaidleaves()==null?0:
		 * empatcreport.getNoOfUnPaidleaves(); SimpleDateFormat formatter = new
		 * SimpleDateFormat("dd/MM/yyyy"); String year="0";
		 * 
		 * // this is to get the employee assignment for the given date
		 * GregorianCalendar caltemp = new GregorianCalendar();
		 * if((month-(currPay
		 * .getFinancialyear().getStartingDate().getMonth()+1))<0) { year =
		 * formatter
		 * .format(currPay.getFinancialyear().getEndingDate()).split("/")[2]; }
		 * else { year =
		 * formatter.format(currPay.getFinancialyear().getStartingDate
		 * ()).split("/")[2]; } caltemp.set(Integer.parseInt(year), (month-1),
		 * 1);
		 * 
		 * // currPay.setEmpAssignment(PayrollManagersUtill.getEisManager().
		 * getAssignmentByEmpAndDate(caltemp.getTime(),empId));
		 * currPay.setNumdays
		 * (Math.round(noofdaysmonth-(noofabsents+noofunpaidleaves)));
		 * currPay.setStatus(egwStatus); currPay.setMonth(new
		 * BigDecimal(month));
		 * currPay=setEarnings(lastPay,currPay,empatcreport);
		 * currPay=setDeductions(lastPay,currPay); } else
		 * logger.error("Approved PaySlip is not existing");
		 * 
		 * PayRollManager pmgr=PayrollManagersUtill.getPayRollManager();
		 * pmgr.createPayslip(currPay); logger.info("Pay Slip is created"); // }
		 * 
		 * return true; } catch (HibernateException he) { he.printStackTrace();
		 * logger.error("Hibernate Exception closing session:" +
		 * he.getMessage()); throw he;
		 * 
		 * } catch (Exception e) { logger.error("Exception closing session:" +
		 * e.getMessage()); e.printStackTrace(); return false; }
		 */
		return false;
	}

	/**
	 * this api will set the deductions to current payslip and returns currpay
	 * slip
	 * 
	 * @param lastPay
	 * @param currPay
	 * @return
	 */

	private EmpPayroll setDeductions(EmpPayroll lastPay, EmpPayroll currPay) {
		/*
		 * if (deductionses != null && deductionses.size() != 0) { for (Iterator
		 * itr1 = deductionses.iterator(); itr1.hasNext();) { Deductions
		 * lastPayDed = (Deductions) itr1.next(); Deductions currPayDed = new
		 * Deductions(); currPayDed.setAmount(lastPayDed.getAmount());
		 * currPayDed.setEmpPayroll(currPay); if (lastPayDed.getSalaryCodes() !=
		 * null) { currPayDed.setSalaryCodes(lastPayDed.getSalaryCodes()); } //
		 * CR: for advances check Pending Amount //update pending amount - // if
		 * pending amount < installment amt , deduct pending amt if
		 * (lastPayDed.getSalAdvances() != null &&
		 * lastPayDed.getSalAdvances().getPendingAmt().intValue() > 0) {
		 * if(lastPayDed
		 * .getSalAdvances().getPendingAmt().floatValue()<lastPayDed
		 * .getSalAdvances().getInstAmt().floatValue()) {
		 * currPayDed.setAmount(lastPayDed.getSalAdvances().getPendingAmt()); }
		 * lastPayDed.getSalAdvances().setPendingAmt(new
		 * BigDecimal((lastPayDed.getSalAdvances
		 * ().getPendingAmt().doubleValue()-
		 * currPayDed.getAmount().doubleValue())));
		 * currPayDed.setSalAdvances(lastPayDed.getSalAdvances()); } if
		 * (lastPayDed.getChartofaccounts() != null) {
		 * currPayDed.setChartofaccounts(lastPayDed.getChartofaccounts()); }
		 * 
		 * deductionTotal = deductionTotal.add(currPayDed.getAmount());
		 * currDeductionses.add(currPayDed); } if (!currDeductionses.isEmpty())
		 * { currPay.setDeductionses(currDeductionses); } }
		 * logger.info("deductionTotal >>>>> " + deductionTotal); netPay =
		 * currPay.getGrossPay().subtract(deductionTotal);
		 * logger.info("netPay >>>>>  " + netPay); currPay.setNetPay(netPay);
		 */
		return currPay;
	}

	/**
	 * this api will set the earnings to the current payslip and returns the
	 * current payslip
	 * 
	 * @param lastPay
	 * @param currPay
	 * @param empatcreport
	 * @return
	 */
	private EmpPayroll setEarnings(EmpPayroll lastPay, EmpPayroll currPay,
			EmployeeAttendenceReport empatcreport) {

		/*
		 * List<SalaryCodes> salaryCodesList
		 * =PayrollManagersUtill.getPayRollManager().getOrderedSalaryCodes();
		 * int noofdaysmonth=empatcreport.getDaysInMonth()==null ? 0 :
		 * empatcreport.getDaysInMonth(); int
		 * noofabsents=empatcreport.getNoOfAbsents()== null ? 0
		 * :empatcreport.getNoOfAbsents(); float noofunpaidleaves =
		 * empatcreport.getNoOfUnPaidleaves()==null?0:
		 * empatcreport.getNoOfUnPaidleaves();
		 * calendar.setTime(payStructure.getAnnualIncrement()); int increMonth =
		 * calendar.get(Calendar.MONTH); BigDecimal incrementMonth = new
		 * BigDecimal(increMonth).add(new BigDecimal(1));
		 * logger.info("month >>>>> " + incrementMonth); BigDecimal
		 * grossPayTotal = new BigDecimal(0); BigDecimal basicAmount = new
		 * BigDecimal(0); BigDecimal headAmount = new BigDecimal(0); BigDecimal
		 * pct = new BigDecimal(0); float flatBasicAmount = 0; Set earningsSet =
		 * new HashSet(); Set<Earnings> lastPayearningses = new
		 * HashSet<Earnings>(lastPay.getEarningses()); if (salaryCodesList !=
		 * null && !salaryCodesList.isEmpty()) { for (SalaryCodes sal :
		 * salaryCodesList) { for (Earnings ear: lastPayearningses) { if
		 * (sal.getId().equals(ear.getSalaryCodes().getId())) { Earnings currEar
		 * = new Earnings(); currEar.setEmpPayroll(currPay);
		 * currEar.setSalaryCodes(sal); if (sal.getHead().equals("Basic")&&
		 * sal.getCalType().equals("MonthlyFlatRate")) {
		 * 
		 * if (incrementMonth.equals(currPay.getMonth())) { basicAmount =
		 * lastPay
		 * .getBasicPay().add(payStructure.getPayHeader().getIncrement()); }
		 * else basicAmount = lastPay.getBasicPay(); flatBasicAmount =
		 * basicAmount.floatValue(); currPay.setBasicPay(basicAmount);
		 * logger.info("basicAmount >>>>> "+ flatBasicAmount); double
		 * temp1=flatBasicAmount/ noofdaysmonth; basicAmount = new
		 * BigDecimal(temp1*(noofdaysmonth-(noofabsents+noofunpaidleaves)));
		 * 
		 * grossPayTotal = grossPayTotal.add(basicAmount);
		 * currEar.setAmount(basicAmount);
		 * 
		 * logger.info("grossPayTotal b >>>>> "+ grossPayTotal); } else if
		 * (sal.getCalType().equals("MonthlyFlatRate")) {
		 * 
		 * //basicAmount = ear.getAmount(); flatBasicAmount =
		 * Math.round(ear.getAmount().floatValue());
		 * 
		 * logger.info("basicAmount121 >>>>> "+ flatBasicAmount); grossPayTotal
		 * = grossPayTotal.add(new BigDecimal(flatBasicAmount));
		 * currEar.setAmount(new BigDecimal(flatBasicAmount));
		 * logger.info("grossPayTotal b121 >>>>> "+ grossPayTotal); } if
		 * (ear.getPct() != null && !ear.getPct().equals("") &&
		 * basicAmount.intValue() >= 0) { pct = ear.getPct().divide(new
		 * BigDecimal(100)); // logger.info("pct bb >>>>> " + pct); headAmount =
		 * pct.multiply(basicAmount); float computedAmount =
		 * Math.round(headAmount.floatValue()); //
		 * logger.info("headAmount bb >>>>> "+computedAmount);
		 * currEar.setAmount(new BigDecimal(computedAmount));
		 * currEar.setPct(ear.getPct()); BigDecimal temp3 = new
		 * BigDecimal(computedAmount); grossPayTotal = grossPayTotal.add(temp3);
		 * logger.info("grossPayTotal bb >>>>> "+ grossPayTotal); }
		 * logger.info("currEar.setAmount >>>>> "+ currEar.getAmount());
		 * 
		 * earningsSet.add(currEar); } if (!earningsSet.isEmpty()) {
		 * currPay.setEarningses(earningsSet); }
		 * currPay.setGrossPay(grossPayTotal); } } }else {
		 * logger.error("PaySlip Already Exists for the month"); //return false;
		 * }
		 */
		return currPay;
	}

	/**
	 * this api will generate the supplementary payslips automatically every
	 * month (this we will mention in xml file) *
	 * 
	 * @param cityURL
	 * @param jndi
	 * @param hibFactName
	 * @throws EJBException
	 */
	public void autoGenSuppPayslipJob(String cityURL, String jndi,
			String hibFactName)  {

		logger.debug("TestEJB Job inside");
		logger.debug("City URL in Job : " + cityURL);
		logger.debug("jndi nae  >>>>  " + jndi);
		logger.debug("hibernate faCTORY name >>>>> " + hibFactName);
		/*
		 * EGOVThreadLocals.setDomainName(cityURL);
		 * EGOVThreadLocals.setJndiName(jndi);
		 * EGOVThreadLocals.setHibFactName(hibFactName);
		 */
		SetDomainJndiHibFactNames.setThreadLocals(cityURL, jndi, hibFactName);
		GregorianCalendar calendar = new GregorianCalendar();

		if (HibernateUtil.getCurrentSession() != null) {
			try {
				PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
						.getPayrollExterInterface();
				String finYear = payrollExternalInterface.getCurrYearFiscalId();
				logger.debug("finYear >>>>> " + finYear);
				int month = calendar.get(Calendar.MONTH);
				BigDecimal monthPay = new BigDecimal(month).add(BigDecimal.ONE);
				logger.debug("month >>>>> " + monthPay);

				/*
				 * List paySlipList =PayrollManagersUtill.getPayRollManager().
				 * getPrevSuppPayslipsForEmpExceptionsInMonthAndYear(month,new
				 * Integer(finYear));
				 * 
				 * logger.debug("paySlipList >>>>> " + paySlipList.size());
				 * 
				 * if (!paySlipList.isEmpty() && paySlipList != null) { for
				 * (Iterator iter = paySlipList.iterator(); iter.hasNext();) {
				 * EmpPayroll lastPay = (EmpPayroll) iter.next();
				 * autoGenSuppPaySlipForEmp(lastPay, monthPay.intValue(),new
				 * Integer(finYear)); } }
				 */

				HibernateUtil.getCurrentSession().flush();

			} catch (HibernateException he) {
				logger.error("Hibernate Exception closing session:"
						+ he.getMessage());
				// he.printStackTrace();
				throw he;
			} catch (Exception e) {
				logger.error("Exception closing session:" + e.getMessage());
				// e.printStackTrace();
			}
		} else {
			logger.error("Session is null");
		}

	}

	/**
	 * this api will save the supp payslip and returns true if it is saved
	 * successfully otherwise it will return false
	 * 
	 * @param lastPay
	 * @param month
	 * @param finYear
	 * @return
	 */
	public Boolean autoGenSuppPaySlipForEmp(EmpPayroll lastPay, Integer month,
			Integer finYear) {

		/*
		 * { if (lastPay != null) {
		 * 
		 * EgwStatus egwStatus = (EgwStatus)
		 * PayrollManagersUtill.getCommonsManager
		 * ().getStatusByModuleAndDescription( PayrollConstants.PAYSLIP_MODULE,
		 * PayrollConstants.PAYSLIP_CREATED_STATUS);
		 * 
		 * currPay.setEmployee(lastPay.getEmployee());
		 * currPay.setCreatedby(lastPay.getCreatedby());
		 * currPay.setCreateddate(new Date());
		 * currPay.setFinancialyear(PayrollManagersUtill
		 * .getCommonsManager().findFinancialYearById(new Long(finYear)));
		 * 
		 * EmpLeaveManager emplvmgr = PayrollManagersUtill.getEmpLeaveManager();
		 * EmployeeAttendenceReport empatcreport =
		 * emplvmgr.getEmployeeAttendenceReport
		 * (month,currPay.getEmployee(),currPay.getFinancialyear());
		 * 
		 * int noofdaysmonth=empatcreport.getDaysInMonth()==null ? 0 :
		 * empatcreport.getDaysInMonth(); int
		 * noofabsents=empatcreport.getNoOfAbsents()== null ? 0
		 * :empatcreport.getNoOfAbsents(); float noofunpaidleaves =
		 * empatcreport.getNoOfUnPaidleaves()==null?0:
		 * empatcreport.getNoOfUnPaidleaves(); SimpleDateFormat formatter = new
		 * SimpleDateFormat("dd/MM/yyyy"); String year="0";
		 * 
		 * GregorianCalendar caltemp = new GregorianCalendar();
		 * if((month-(currPay
		 * .getFinancialyear().getStartingDate().getMonth()+1))<0) year =
		 * formatter
		 * .format(currPay.getFinancialyear().getEndingDate()).split("/")[2];
		 * else year =
		 * formatter.format(currPay.getFinancialyear().getStartingDate
		 * ()).split("/")[2];
		 * 
		 * caltemp.set(Integer.parseInt(year), (month-1), 1);
		 * //currPay.setEmpAssignment
		 * (PayrollManagersUtill.getEisManager().getAssignmentByEmpAndDate
		 * (caltemp
		 * .getTime(),lastPay.getEmployee().getIdPersonalInformation()));
		 * currPay
		 * .setNumdays(Math.round(noofdaysmonth-(noofabsents+noofunpaidleaves
		 * ))); currPay.setStatus(egwStatus);
		 * currPay.setPayType(lastPay.getPayType()); currPay.setMonth(new
		 * BigDecimal(month)); PayStructure payStructure =
		 * getPayStructureByEmp(lastPay.getEmployee());
		 * calendar.setTime(payStructure.getAnnualIncrement()); int increMonth =
		 * calendar.get(Calendar.MONTH); BigDecimal incrementMonth = new
		 * BigDecimal(increMonth).add(new BigDecimal(1));
		 * logger.info("month >>>>> " + incrementMonth);
		 * 
		 * 
		 * List<SalaryCodes> salaryCodesList
		 * =PayrollManagersUtill.getPayRollManager().getOrderedSalaryCodes(); if
		 * (salaryCodesList != null && !salaryCodesList.isEmpty()) {
		 * 
		 * currPay.setEarningses(lastPay.getEarningses());
		 * currPay.setGrossPay(lastPay.getGrossPay());
		 * 
		 * BigDecimal deductionTotal = new BigDecimal(0); BigDecimal netPay =
		 * new BigDecimal(0); Set currDeductionses = new HashSet(); Set
		 * deductionses = new HashSet(lastPay.getDeductionses()); if
		 * (deductionses != null && deductionses.size() != 0) { for (Iterator
		 * itr1 = deductionses.iterator(); itr1.hasNext();) { Deductions
		 * lastPayDed = (Deductions) itr1.next(); Deductions currPayDed = new
		 * Deductions(); currPayDed.setAmount(lastPayDed.getAmount());
		 * currPayDed.setEmpPayroll(currPay); if (lastPayDed.getSalaryCodes() !=
		 * null) { currPayDed.setSalaryCodes(lastPayDed.getSalaryCodes()); }
		 * 
		 * if (lastPayDed.getSalAdvances() != null &&
		 * lastPayDed.getSalAdvances().getPendingAmt().intValue() > 0) {
		 * if(lastPayDed
		 * .getSalAdvances().getPendingAmt().floatValue()<lastPayDed
		 * .getSalAdvances().getInstAmt().floatValue()) {
		 * currPayDed.setAmount(lastPayDed.getSalAdvances().getPendingAmt()); }
		 * lastPayDed.getSalAdvances().setPendingAmt(new
		 * BigDecimal((lastPayDed.getSalAdvances
		 * ().getPendingAmt().doubleValue()-
		 * currPayDed.getAmount().doubleValue())));
		 * currPayDed.setSalAdvances(lastPayDed.getSalAdvances()); } if
		 * (lastPayDed.getChartofaccounts() != null) {
		 * currPayDed.setChartofaccounts(lastPayDed.getChartofaccounts()); }
		 * 
		 * deductionTotal = deductionTotal.add(currPayDed.getAmount());
		 * currDeductionses.add(currPayDed); } if (!currDeductionses.isEmpty())
		 * { currPay.setDeductionses(currDeductionses); } }
		 * logger.info("deductionTotal >>>>> " + deductionTotal); netPay =
		 * lastPay.getGrossPay().subtract(deductionTotal);
		 * logger.info("netPay >>>>>  " + netPay); currPay.setNetPay(netPay); }
		 * else logger.error("No Salary Codes are available");
		 * 
		 * PayRollManager pmgr=PayrollManagersUtill.getPayRollManager();
		 * pmgr.createPayslip(currPay); logger.info("Pay Slip is created"); }
		 * return true; } catch (HibernateException he) { he.printStackTrace();
		 * logger.error("Hibernate Exception closing session:" +
		 * he.getMessage()); throw he;
		 * 
		 * } catch (Exception e) { logger.error("Exception closing session:" +
		 * e.getMessage()); e.printStackTrace(); return false; }
		 */
		return false;
	}

	// i need to move this api to hibernate dao class
	public PayStructure getPayStructureByEmp(PersonalInformation employee) {
		Query qry = HibernateUtil.getCurrentSession().createQuery(
				"from PayStructure ps where ps.employee =:employee ");
		qry.setEntity("employee", employee);
		return (PayStructure) qry.uniqueResult();
	}


	
}
