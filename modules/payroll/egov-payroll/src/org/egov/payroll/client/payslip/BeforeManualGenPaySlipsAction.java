package org.egov.payroll.client.payslip;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.model.PayTypeMaster;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.services.payslip.PayslipFailureException;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.PayFixedInMaster;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;

/**
 * @author Lokesh, Surya,Mamatha,Soumen,Divya MODE values will be regenerate :
 *         for resolve payslips from report screen central : search screen
 *         create : create payslip ,manual modify : modify payslip view : view
 *         payslip
 */
public class BeforeManualGenPaySlipsAction extends Action {
	private static final Logger logger = Logger
			.getLogger(BeforeManualGenPaySlipsAction.class);

	private PayrollExternalInterface payrollExternalInterface;
	private PersistenceService actionService;
	private static final String REGENERATE = "reGenerate";
	private EISServeable eisService;
	private EmployeeService employeeService;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = "failure";
		javax.servlet.http.HttpSession session = request.getSession();
		SalaryPaySlipForm salPaySlipForm = (SalaryPaySlipForm) form;

		try {
			ArrayList functionList = (ArrayList) EgovMasterDataCaching
					.getInstance().get("egEmp-function");
			session.setAttribute("functionList", functionList);
			String mode = (String) request.getParameter("mode");
			List<Action> wfActionList = null;
			if (request.getAttribute("ess") != null
					|| (request.getParameter("ess") != null)) {
				PersonalInformation employee = employeeService
						.getEmpForUserId(Integer.valueOf(EGOVThreadLocals
								.getUserId()));
				if (employee == null)
					return mapping.findForward("essError");
				salPaySlipForm.setEmployeeCodeId(String.valueOf(employee
						.getId()));
				salPaySlipForm.setDepartment(null);
				request.setAttribute("employeeCode", employee.getCode());
				request.setAttribute("employeeName", employee.getName());
				request.setAttribute("ess", 1);
			}
			if ("payscale".equals(mode)) {
				wfActionList = actionService.findAllByNamedQuery("BY_TYPE",
						"PayScaleHeader");
			}
			if (mode != null
					&& ("create".equalsIgnoreCase(mode) || REGENERATE
							.equalsIgnoreCase(mode))) {
				loadDataForCreate(salPaySlipForm, request, mode);
			} else if ("view".equals(mode)) {
				List<EmpPayroll> payslips = null;
				String empStrID = salPaySlipForm.getEmployeeCodeId();
				String empDeptId = salPaySlipForm.getDepartment();
				int empId = 0;

				if (salPaySlipForm.getEmployeeCodeId() != null
						&& !salPaySlipForm.getEmployeeCodeId().equals("")) {
					empId = Integer
							.parseInt(salPaySlipForm.getEmployeeCodeId());
				}
				int month = Integer.parseInt(salPaySlipForm.getMonth());
				int year = Integer.parseInt(salPaySlipForm.getYear());
				String bill = "";
				if (!("").equalsIgnoreCase(salPaySlipForm.getBillNumberId())
						&& null != salPaySlipForm.getBillNumberId()) {
					bill = salPaySlipForm.getBillNumberId();
				}
				if (empStrID != null && !empStrID.equals("")) {
					payslips = PayrollManagersUtill.getPayRollService()
							.getAllPayslipByEmpMonthYear(empId, month, year);

				} else if (empDeptId != null && !empDeptId.equals("")) {

					Integer deptId = Integer.valueOf(empDeptId);
					payslips = PayrollManagersUtill.getPayRollService()
							.getAllPayslipByEmpMonthYearBasedOnDept(
									deptId,
									month,
									year,
									((!bill.equals("") ? Integer.valueOf(bill)
											: null)));
					logger.info("PAYSLIP COUNT............" + payslips.size());
				}
				for (EmpPayroll payslip : payslips) {
					if (payslip.getState() != null
							&& payslip.getState().getOwner() != null
							&& payslip.getState().getOwner().getId() != null) {
						if (null != eisService.getUserForPosition(payslip
								.getState().getOwner().getId(), new Date())
								&& (!payslip.getState().getValue()
										.equalsIgnoreCase("end"))) {
							payslip.setUserName(eisService.getUserForPosition(
									payslip.getState().getOwner().getId(),
									new Date()).getUserName());
						}
					}
				}

				request.setAttribute("payslips", payslips);
				return mapping.findForward("viewPayslips");
			} else if (mode == null)// for modify payslip
			{
				String payslipId = request.getParameter("payslipId");
				salPaySlipForm.setPayslipId(payslipId);
				salPaySlipForm = loadDataForModify(salPaySlipForm, request);
			}

			PayScaleHeader payheader = null;
			// this block of code for payscale search screens
			if (salPaySlipForm.getPayScaleName() != null
					&& !salPaySlipForm.getPayScaleName().trim().equals("")
					&& request.getParameter("path") != null
					&& !request.getParameter("path").equals("")
					&& !request.getParameter("path").equals("back")) {
				// FIXME: check if it's required?
				payheader = PayrollManagersUtill.getPayRollService()
						.getPayScaleHeaderById(
								new Integer(salPaySlipForm.getPayScaleName()
										.trim()));
				session.setAttribute("payHeader", payheader);
				String check = PayrollManagersUtill.getPayRollService()
						.checkExistingPayscaleInPayslip(payheader);
				logger.info(check);
				request.getSession().setAttribute("chekPayscale", check);
				request.getSession().setAttribute("payscaleModify", "");
			}

			loadDropDowns(request);

			request.setAttribute("SalaryPaySlipForm", salPaySlipForm);

			target = "success";
			if (mode != null && mode.equals(REGENERATE)) {
				request.setAttribute("salaryPaySlipForm", salPaySlipForm);
				form = salPaySlipForm;
			}
			if (salPaySlipForm.getEmployeeCodeId() != null
					&& !salPaySlipForm.getEmployeeCodeId().equals("")
					&& request.getAttribute("ess") == null
					&& request.getParameter("ess") == null) {
				List salAdvances = PayrollManagersUtill.getPayRollService()
						.getAdvancesByEmpId(
								Integer.parseInt(salPaySlipForm
										.getEmployeeCodeId()));
				request.getSession().setAttribute("salAdvances", salAdvances);
			}
			request.setAttribute("mode", mode);
			if ("payscale".equals(mode)) {
				session.setAttribute("wfActionList", wfActionList);
				List<PayFixedInMaster> payFixedList = actionService
						.findAllBy("from PayFixedInMaster pay where pay.fromDate <= sysdate and pay.toDate >= sysdate");
				session.setAttribute("payFixedList", payFixedList);
			}
			List<Department> departmentList = actionService
					.findAllBy("from DepartmentImpl order by deptName");
			List<DesignationMaster> designationList = actionService
					.findAllBy("from DesignationMaster order by designationName");
			session.setAttribute("departmentList", departmentList);
			session.setAttribute("deptList", getEisService().getDeptsForUser());
			session.setAttribute("designationList", designationList);
			return mapping.findForward(target);
		} catch (EGOVException egovExp) {
			logger.error(egovExp.getMessage());
			request.setAttribute("alertMessage", egovExp.getMessage());
			return mapping.findForward(target);
		} catch (EGOVRuntimeException egovExp) {
			logger.error(egovExp.getMessage());
			HibernateUtil.rollbackTransaction();
			request.setAttribute("alertMessage", egovExp.getMessage());
			return mapping.findForward(target);
		} catch (Exception e) {
			logger.error(e.getMessage());
			HibernateUtil.rollbackTransaction();
			request.setAttribute("alertMessage", e.getMessage());
			return mapping.findForward(target);
		}
	}

	private void loadDropDowns(HttpServletRequest request) throws Exception {
		List<SalaryCodes> salaryCodes = PayrollManagersUtill
				.getPayRollService().getOrderedSalaryCodes();
		List financialYear = (ArrayList) EgovMasterDataCaching.getInstance()
				.get("egi-activeFinYr");
		List payScaleHeaderList = PayrollManagersUtill.getPayRollService()
				.getAllPayScaleHeaders();
		List<PayTypeMaster> paytypelist = PayrollManagersUtill
				.getPayRollService().getAllPayTypes();
		request.getSession().setAttribute("payScaleHeaderList",
				payScaleHeaderList);
		request.getSession().setAttribute("financialYear", financialYear);
		request.getSession().setAttribute("salaryCodes", salaryCodes);
		request.getSession().setAttribute("paytypelist", paytypelist);
		request.getSession().setAttribute("currFinYr",
				payrollExternalInterface.getCurrYearFiscalId());
	}

	private void loadDataForCreate(SalaryPaySlipForm salPaySlipForm,
			HttpServletRequest request, String mode) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		try {

			PayTypeMaster normalPaytype = PayrollManagersUtill
					.getPayRollService().getPayTypeMasterByPaytype(
							PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
			PayTypeMaster expPaytype = PayrollManagersUtill.getPayRollService()
					.getPayTypeMasterByPaytype(
							PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION);
			PayProcessingUtil payutil = new PayProcessingUtil();
			String paytype = null;
			String flagForDedTax = "";
			String flagForDedOthr = "";
			String empid = salPaySlipForm.getEmployeeCodeId();
			paytype = salPaySlipForm.getPayType();
			String empinfo = "";

			if (empid == null || empid.equals("")) {
				empid = (String) request.getParameter("empid");
			}
			if (empid == null || empid.equals("")) {
				throw new EGOVException("Employee id not selected");
			}

			salPaySlipForm.setEmployeeCodeId(empid);
			// set user in the form if null
			// this user name is used to save the increment details
			if (salPaySlipForm.getUserName() == null) {
				salPaySlipForm.setUserName((String) request.getSession()
						.getAttribute("com.egov.user.LoginUserName"));
			}
			// we are setting the pay type as request parameter in report screen
			// and in case of view payslip,value will be set to form
			// cross check this
			if (paytype == null) {
				paytype = (String) request.getParameter("payType");
				// paytype default value is normal
				if (paytype == null) {
					paytype = normalPaytype.getId().toString();
				}
			}

			setFDateAndTDate(salPaySlipForm, request, mode);

			GregorianCalendar fromdate1 = new GregorianCalendar();
			fromdate1
					.setTime(formatter.parse(salPaySlipForm.getEffectiveFrom()));
			GregorianCalendar todate1 = new GregorianCalendar();
			todate1.setTime(formatter.parse(salPaySlipForm.getEffectiveTo()));
			PayTypeMaster payTypeMstr = PayrollManagersUtill
					.getPayRollService().getPayTypeById(new Integer(paytype));
			if (PayrollManagersUtill
					.getPayRollService()
					.checkPayslipProcessAbilityByPayType(
							Integer.parseInt(salPaySlipForm.getEmployeeCodeId()),
							fromdate1, todate1, payTypeMstr.getNarration())) {
				PayStructure payst = PayrollManagersUtill.getPayRollService()
						.getPayStructureForEmpByDate(
								Integer.parseInt(salPaySlipForm
										.getEmployeeCodeId()),
								todate1.getTime());
				request.setAttribute("payScaleName", payst.getPayHeader()
						.getName());
			}

			Map dedlist = null;
			logger.debug("form  to date  === "
					+ salPaySlipForm.getEffectiveTo());

			// we are setting empid , fromdate,todate,month,financialyear and
			// paytype for form object
			if (mode != null
					&& (REGENERATE.equalsIgnoreCase(mode) || "create"
							.equalsIgnoreCase(mode))) {
				empinfo = (String) PayrollManagersUtill.getPayRollService()
						.getEmpInfoByFDateAndTDate(new Integer(empid),
								fromdate1, todate1);
				if (paytype.equalsIgnoreCase(normalPaytype.getId().toString())) {
					Float[] wrkdays = payutil
							.getWorkingDaysForPayslip(salPaySlipForm);
					if (wrkdays == null) {
						throw new EGOVException("Attendence is not Available");
					} else { // TODO::: get numdays and working days from form
								// in all jsp's
						salPaySlipForm.setNumDays(Float.parseFloat(wrkdays[0]
								.toString()) + "");
						salPaySlipForm.setWorkingDays(wrkdays[1].toString());
					}
					dedlist = payutil
							.loadPayslipForNormalPayslip(salPaySlipForm);
				} else if (paytype.equalsIgnoreCase(expPaytype.getId()
						.toString())) {
					HashMap map = (HashMap) payutil
							.getPaidDaysDetailsForNormalAndSuppPayslips(
									Integer.parseInt(empid), fromdate1, todate1);
					if (map == null) {
						throw new EGOVException("Attendence is not Available");
					} else {
						float totaldays = (Float) map.get("SUPPnooftotaldays");
						float noofpaiddays = (Float) map
								.get("SUPPnoofpaiddays");
						salPaySlipForm.setNumDays(noofpaiddays + "");
						salPaySlipForm.setWorkingDays(totaldays + "");
					}
					dedlist = payutil.loadPayslipForExpPayslip(salPaySlipForm);
				} else // for supplementary payslips
				{
					dedlist = payutil.setEarningsAndDeductions(salPaySlipForm);
					if (empinfo.equals("")) {
						empinfo = (String) PayrollManagersUtill
								.getPayRollService()
								.getEmpInfoByLastAssignment(new Integer(empid));
					}
				}
			}

			String empcode = empinfo.split("~")[0];
			String empname = empinfo.split("~")[1];
			String desgname = empinfo.split("~")[3];
			String deptname = empinfo.split("~")[4];
			String dateofjoining = empinfo.split("~")[5];

			request.setAttribute("employeeName", empname);
			request.setAttribute("designation", desgname);
			request.setAttribute("department", deptname);
			request.setAttribute("yearOfJoining", dateofjoining);

			request.setAttribute("empcode", empcode);
			double absents = Double
					.parseDouble(salPaySlipForm.getWorkingDays())
					- Double.parseDouble(salPaySlipForm.getNumDays());
			// to dispaly values in screen
			request.setAttribute("noofdaysmonth",
					salPaySlipForm.getWorkingDays());
			request.setAttribute("noofabsents", absents);
			if (dedlist != null) {
				List dedOtherListWithAmount = (List) dedlist
						.get("dedOtherListWithAmount");
				if (dedOtherListWithAmount != null) {
					for (Iterator it = dedOtherListWithAmount.iterator(); it
							.hasNext();) {
						Deductions ded = (Deductions) it.next();
						BigDecimal tempAmnt = ded.getAmount();
						if (BigDecimal.ZERO.compareTo(tempAmnt) != 0) {
							flagForDedOthr = "valExists";
						}
					}
				}

				List dedListWithAmount = (List) dedlist
						.get("dedListWithAmount");
				if (dedListWithAmount != null) {
					for (Iterator it = dedListWithAmount.iterator(); it
							.hasNext();) {
						Deductions ded = (Deductions) it.next();
						BigDecimal tempAmnt = ded.getAmount();
						if (BigDecimal.ZERO.compareTo(tempAmnt) != 0) {
							flagForDedTax = "valExists";
						}
					}
				}

				// Collections.sort((List)dedlist.get("dedTaxlist"),
				// Deductions.SalarycodeComparator);
				request.getSession().setAttribute("dedTaxlist",
						dedlist.get("dedTaxlist"));
				request.getSession().setAttribute("dedTaxamountlist",
						dedlist.get("dedTaxamountlist"));
				request.getSession().setAttribute("dedOtheramountlist",
						dedlist.get("dedOtheramountlist"));
				request.getSession().setAttribute("dedOtherlist",
						dedlist.get("dedOtherlist"));
				request.getSession().setAttribute("otherDedList",
						dedlist.get("otherDedList"));
				request.getSession().setAttribute("deductionsAdvList",
						dedlist.get("deductionsAdvList"));
				request.getSession().setAttribute("dedListWithAmount",
						dedlist.get("dedListWithAmount"));
				request.getSession().setAttribute("dedOtherListWithAmount",
						dedlist.get("dedOtherListWithAmount"));
				request.getSession().setAttribute("flagForDedOthr",
						flagForDedOthr);
				request.getSession().setAttribute("flagForDedTax",
						flagForDedTax);
			}
		} catch (PayslipFailureException failureException) {
			request.setAttribute("exceptionStr", failureException.getMessage());
			throw failureException;
		} catch (EGOVException e) {
			logger.error(" " + e.getMessage());
			throw e;
		} catch (EGOVRuntimeException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("" + e.getMessage());
			throw e;
		}
	}

	private SalaryPaySlipForm loadDataForModify(
			SalaryPaySlipForm salPaySlipForm, HttpServletRequest request)
			throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		List<Deductions> deductionsTaxList = new ArrayList<Deductions>();
		List<Deductions> deductionOtherList = new ArrayList<Deductions>();
		List deductionsAdvList = new ArrayList();
		List deductionsotherList = new ArrayList();
		String flagForDeductionTax = "";
		String flagForDeductionOthr = "";

		try {

			PayTypeMaster normalPaytype = PayrollManagersUtill
					.getPayRollService().getPayTypeMasterByPaytype(
							PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
			PayTypeMaster expPaytype = PayrollManagersUtill.getPayRollService()
					.getPayTypeMasterByPaytype(
							PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION);
			EmpPayroll paySlip = PayrollManagersUtill.getPayRollService()
					.getPayslipByIdWithYTD(
							Long.parseLong(salPaySlipForm.getPayslipId()));
			PayStructure payStructure = PayrollManagersUtill
					.getPayRollService().getPayStructureForEmpByDate(
							paySlip.getEmployee().getIdPersonalInformation(),
							paySlip.getToDate());
			// wht if fromdate and todates are null
			logger.info(payStructure.getPayHeader().getName());
			float totaldays = paySlip.getWorkingDays().floatValue();

			logger.info("paySlip.getToDate()=====" + paySlip.getToDate()
					+ "paySlip.getFromDate()==" + paySlip.getFromDate());
			if (paySlip.getFromDate() == null) {
				salPaySlipForm.setEffectiveFrom(null);
			} else {
				salPaySlipForm.setEffectiveFrom(formatter.format(paySlip
						.getFromDate()));
			}
			if (paySlip.getToDate() == null) {
				salPaySlipForm.setEffectiveTo(null);
			} else {
				salPaySlipForm.setEffectiveTo(formatter.format(paySlip
						.getToDate()));
			}
			salPaySlipForm.setPayType(paySlip.getPayType().getId().toString());

			List<SalaryCodes> salcodesTax = PayrollManagersUtill
					.getPayheadService().getAllSalarycodesByCategoryName(
							"Deduction-Tax");
			List<SalaryCodes> salcodesDedOther = PayrollManagersUtill
					.getPayheadService().getAllSalarycodesByCategoryName(
							"Deduction-Other");
			// get all deduction-tax / deduction-others and iterate through
			// each.

			// if present in payslip, add ded obj to correspondinglist,
			// otherwise add empty object
			Deductions deduction;
			// loop for deduction other
			flagForDeductionOthr = "";
			for (SalaryCodes sal : salcodesDedOther) {
				if (paySlip.getDeductionses() != null
						&& paySlip.getDeductionses().size() != 0) {
					for (Iterator iter = paySlip.getDeductionses().iterator(); iter
							.hasNext();) {
						deduction = (Deductions) iter.next();
						if (deduction.getSalaryCodes().getCategoryMaster()
								.getId().intValue() == PayrollConstants.DEDUCTION_OTHER_TYPE
								&& sal.getId() == deduction.getSalaryCodes()
										.getId()) {
							deductionOtherList.add(deduction);
							flagForDeductionOthr = "valTaxOthr";
						}
					}
				}
			}

			for (SalaryCodes sal : salcodesTax) {
				if (paySlip.getDeductionses() != null
						&& paySlip.getDeductionses().size() != 0) {
					for (Iterator iter = paySlip.getDeductionses().iterator(); iter
							.hasNext();) {
						deduction = (Deductions) iter.next();
						if (deduction.getSalaryCodes().getCategoryMaster()
								.getId().intValue() == PayrollConstants.DEDUCTION_TAX_TYPE
								&& sal.getId().equals(
										deduction.getSalaryCodes().getId())) {
							deductionsTaxList.add(deduction);
							flagForDeductionTax = "valTaxDed";
						}
					}
				}
			}

			if (paySlip.getDeductionses() != null
					&& paySlip.getDeductionses().size() != 0) {
				for (Iterator iter = paySlip.getDeductionses().iterator(); iter
						.hasNext();) {
					deduction = (Deductions) iter.next();

					if (deduction.getSalAdvances() != null) {
						Advance salAdnces = (Advance) PayrollManagersUtill
								.getPayRollService().getAdvanceById(
										deduction.getSalAdvances().getId());
						// New Implementation:If deduction payhead type is
						// bank-loan advance,then check advance disbursed status
						// if deduction payhead type is ded-advance,then check
						// bill PAYMENT APPROVED status
						if (salAdnces != null
								&& "Deduction-BankLoan".equals(salAdnces
										.getSalaryCodes().getCategoryMaster()
										.getName())) {
							deductionsAdvList.add(deduction);
						} else if (salAdnces != null
								&& "Deduction-Advance".equals(salAdnces
										.getSalaryCodes().getCategoryMaster()
										.getName())) {
							deductionsAdvList.add(deduction);
						}
					}
					if (deduction.getChartofaccounts() != null) {
						CChartOfAccounts accounts = (CChartOfAccounts) payrollExternalInterface
								.getCChartOfAccountsById(Long
										.valueOf((deduction
												.getChartofaccounts().getId())));
						if (accounts != null) {
							deductionsotherList.add(deduction);
						}
					}

				}
			}

			GregorianCalendar todate = new GregorianCalendar();
			todate.setTime(paySlip.getToDate());

			GregorianCalendar fromdate = new GregorianCalendar();
			fromdate.setTime(paySlip.getFromDate());

			String empinfo = "";
			if (paySlip.getPayType().getId().toString()
					.equalsIgnoreCase(normalPaytype.getId().toString())
					|| paySlip.getPayType().getId().toString()
							.equalsIgnoreCase(expPaytype.getId().toString())) {
				empinfo = (String) PayrollManagersUtill.getPayRollService()
						.getEmpInfoByFDateAndTDate(
								paySlip.getEmployee()
										.getIdPersonalInformation(), fromdate,
								todate);
			} else {
				empinfo = (String) PayrollManagersUtill.getPayRollService()
						.getEmpInfoByFDateAndTDate(
								paySlip.getEmployee()
										.getIdPersonalInformation(), fromdate,
								todate);
				if (empinfo.equals("")) {
					empinfo = (String) PayrollManagersUtill.getPayRollService()
							.getEmpInfoByLastAssignment(
									new Integer(paySlip.getEmployee()
											.getIdPersonalInformation()));
				}
			}

			// String empcode=empinfo.split("~")[0];
			// String empname=empinfo.split("~")[1];
			String desgname = empinfo.split("~")[3];
			String deptname = empinfo.split("~")[4];
			// String dateofjoining=empinfo.split("~")[5];

			request.setAttribute("designation", desgname);
			request.setAttribute("department", deptname);

			Collections
					.sort(deductionsTaxList, Deductions.SalarycodeComparator);
			request.getSession().setAttribute("noofdaysmonth", totaldays);
			request.getSession().setAttribute("deductionsTaxList",
					deductionsTaxList);
			request.getSession().setAttribute("deductionsAdvList",
					deductionsAdvList);
			request.getSession().setAttribute("deductionsotherList",
					deductionsotherList);
			// request.getSession().setAttribute("empPayroll", empPayrollList);
			request.getSession().setAttribute("paySlip", paySlip);
			request.getSession().setAttribute("flagForDeductionTax",
					flagForDeductionTax);
			request.getSession().setAttribute("flagForDeductionOthr",
					flagForDeductionOthr);
			// request.getSession().setAttribute("flagDed", flagDed);
			request.getSession().setAttribute("deductionOtherList",
					deductionOtherList);
			request.setAttribute("payStructure", payStructure);
			request.setAttribute("salcodesTax", salcodesTax);
			request.setAttribute("salcodesDedOther", salcodesDedOther);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return salPaySlipForm;
	}

	/**
	 * it will set the month,financialyearid and fromdate and todate to
	 * salarypayslipform object
	 * 
	 * @param salPaySlipForm
	 * @param request
	 * @param mode
	 * @return
	 */
	public void setFDateAndTDate(SalaryPaySlipForm salPaySlipForm,
			HttpServletRequest request, String mode) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		String fromdate = "";
		String todate = "";
		GregorianCalendar fromdate1;
		GregorianCalendar todate1;
		CFinancialYear finyr;
		String month;
		try {
			// fromdate and todate values are passing as request parameters from
			// payslipreport.jsp both the cases exception payslip and normal
			// payslip
			// paytype values setted as a request parameter in payslipreport.jsp
			if (mode.equals(REGENERATE)) {
				fromdate = (String) request.getParameter("fromdate");
				todate = (String) request.getParameter("todate");
				finyr = PayrollManagersUtill.getPayRollService()
						.getFinancialYearByDate(formatter.parse(todate));
				String year = finyr.getId().toString();
				salPaySlipForm.setYear(year);
				month = todate.split("/")[1];
				logger.info("payType===" + request.getParameter("payType"));
				if (month.charAt(0) == '0') {
					salPaySlipForm.setMonth(month.charAt(1) + "");
				} else {
					salPaySlipForm.setMonth(month);
				}
				salPaySlipForm.setEffectiveFrom(fromdate);
				salPaySlipForm.setEffectiveTo(todate);
			} else {
				// this loop will set the from date and todate as startign and
				// ending dates of the payslip month
				finyr = payrollExternalInterface
						.findFinancialYearById(new Long(salPaySlipForm
								.getYear()));
				// GregorianCalendar cal=new GregorianCalendar();
				String yeartemp = "0";
				month = salPaySlipForm.getMonth();
				if (Integer.parseInt(month) < 4) {
					yeartemp = formatter.format(finyr.getEndingDate()).split(
							"/")[2];
				} else {
					yeartemp = formatter.format(finyr.getStartingDate()).split(
							"/")[2];
				}
				fromdate1 = new GregorianCalendar(Integer.parseInt(yeartemp),
						(Integer.parseInt(month) - 1), 1);
				todate1 = new GregorianCalendar(fromdate1.get(Calendar.YEAR),
						fromdate1.get(Calendar.MONTH),
						fromdate1.getActualMaximum(Calendar.DATE));
				salPaySlipForm.setEffectiveFrom(formatter.format(fromdate1
						.getTime()));
				salPaySlipForm.setEffectiveTo(formatter.format(todate1
						.getTime()));
				logger.info("yeartemp=" + yeartemp);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	public PersistenceService getActionService() {
		return actionService;
	}

	public void setActionService(PersistenceService actionService) {
		this.actionService = actionService;
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public EISServeable getEisService() {
		return eisService;
	}

	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
