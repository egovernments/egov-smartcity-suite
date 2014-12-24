package org.egov.payroll.services.payslipApprove;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptContext;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgActiondetails;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.egf.bills.model.Cbill;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.recoveries.Recovery;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.Earnings;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.services.payslip.PayslipFailureException;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.pims.commons.DrawingOfficer;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.utils.FinancialConstants;
import org.hibernate.jdbc.ReturningWork;

public class SalaryPaybill {

	private static final Logger LOGGER = Logger.getLogger(SalaryPaybill.class);
	protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	// private PayrollExternalInterface
	// payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
	private PayrollExternalInterface payrollExternalInterface;
	private SequenceGenerator sequenceGenerator;
	private CommonsService commonsService;
	private ScriptService scriptExecutionService;
	private PersistenceService persistenceService;

	GenericDaoFactory genericDaoFactory;

	/**
	 * Aggregating Payslips depending uppon Fund , Function & Field
	 */
	public HashMap getSalaryBillAggregateMap(List approvedPayslips)
			throws Exception {
		try {
			List<String> mandatoryFields = new ArrayList<String>();

			List<AppConfigValues> appConfigList = GenericDaoFactory
					.getDAOFactory()
					.getAppConfigValuesDAO()
					.getConfigValuesByModuleAndKey("Payroll",
							"PAYROLLFINTRNATTRIBUTES");

			for (AppConfigValues appConfig : appConfigList) {

				String value = appConfig.getValue();
				String header = value.substring(0, value.indexOf('|'));
				String mandate = value.substring(value.indexOf('|') + 1);
				if (mandate.equalsIgnoreCase("M")) {
					mandatoryFields.add(header);
				}

			}
			if (!mandatoryFields.contains("fund")) {
				throw new Exception("Fund should be mandatory");
			}
			LOGGER.info("AGGREGATION OF PAYSLIP STARTED----------");
			HashMap hm = new HashMap();
			for (Iterator iter = approvedPayslips.iterator(); iter.hasNext();) {
				EmpPayroll empPayroll = (EmpPayroll) iter.next();
				SalaryBillAggregate salBillAgr = new SalaryBillAggregate();
				if (mandatoryFields.contains("fund")) {
					if (empPayroll.getEmpAssignment().getFundId() != null) {
						salBillAgr.setFund(empPayroll.getEmpAssignment()
								.getFundId());
					} else {
						throw new PayslipFailureException(
								"Fund is mandatory for payroll-egf transaction,but not there!!");
					}
				}
				if (mandatoryFields.contains("function")) {
					if (empPayroll.getEmpAssignment().getFunctionId() != null) {
						salBillAgr.setFunction(empPayroll.getEmpAssignment()
								.getFunctionId());
					} else {
						throw new PayslipFailureException(
								"Function is maendatory for payroll-egf transaction,but not there!!");
					}
				}
				if (mandatoryFields.contains("functionary")) {
					if (empPayroll.getEmpAssignment().getFunctionary() != null) {
						salBillAgr.setFunctionary(empPayroll.getEmpAssignment()
								.getFunctionary());
					} else {
						throw new PayslipFailureException(
								"Functionary is mandatory for payroll-egf transaction,but not there!!");
					}
				}
				if (mandatoryFields.contains("department")) {
					if (empPayroll.getEmpAssignment().getDeptId() != null) {
						salBillAgr.setDepartment(empPayroll.getEmpAssignment()
								.getDeptId());
					} else {
						throw new PayslipFailureException(
								"Department is mandatory for payroll-egf transaction,but not there!!");
					}

				}
				if (mandatoryFields.contains("drawingOfficer")) {
					Position empPosition = empPayroll.getEmpAssignment()
							.getPosition();
					Position drawingOfficerPosition = payrollExternalInterface
							.getSuperiorPositionByObjType(empPosition,
									"payslip");
					if (drawingOfficerPosition != null
							&& drawingOfficerPosition.getDrawingOfficer() != null) {
						salBillAgr
								.setDrawingOfficer((DrawingOfficer) drawingOfficerPosition
										.getDrawingOfficer());
					} else {
						throw new PayslipFailureException(
								"Drawing officer is not defined properly for - "
										+ empPayroll.getEmployee().getName()
										+ "!!");
					}
				}
				if (mandatoryFields.contains("field")) {
					throw new PayslipFailureException(
							"There is no field defined");
				}
				if (mandatoryFields.contains("fundsource")) {
					throw new PayslipFailureException(
							"There is no fundsource defined");
				}
				if (empPayroll.getPayType().getBillType() == null) {
					throw new PayslipFailureException(
							"Bill type is not defined for this pay type---"
									+ empPayroll.getPayType().getBillType());
				} else {
					salBillAgr.setBillType(empPayroll.getPayType()
							.getBillType());
				}
				/*
				 * if(mandatoryFields.contains("billType")){
				 * if("Normal PaySlip".
				 * equals(empPayroll.getPayType().getPaytype())){
				 * salBillAgr.setBillType
				 * (FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY); } else{
				 * salBillAgr.setBillType(FinancialConstants.
				 * STANDARD_EXPENDITURETYPE_CONTINGENT); } }
				 */
				salBillAgr.setEmpType(empPayroll.getEmployee()
						.getEmployeeTypeMaster());
				List payslipForAggregates = (List) hm.get(salBillAgr);
				if (payslipForAggregates == null) {
					payslipForAggregates = new ArrayList();
					payslipForAggregates.add(empPayroll);
					hm.put(salBillAgr, payslipForAggregates);
				} else {
					payslipForAggregates.add(empPayroll);
					hm.put(salBillAgr, payslipForAggregates);
				}
			}
			return hm;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	// Getting netamount for each aggregating bill into BILREGISTER Billamount
	public BigDecimal getNetAmountPerBill(HashMap salaryBillAggrMap,
			SalaryBillAggregate salaryBillAggregate) throws Exception {
		try {
			BigDecimal netAmount = BigDecimal.ZERO;
			List payslips = (List) salaryBillAggrMap.get(salaryBillAggregate);
			for (Iterator iter = payslips.iterator(); iter.hasNext();) {
				EmpPayroll empPayroll = (EmpPayroll) iter.next();
				netAmount = netAmount.add(empPayroll.getNetPay());
			}
			return netAmount;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	// Getting gross amount for each aggregating bill
	public BigDecimal getGrossAmountPerBill(HashMap salaryBillAggrMap,
			SalaryBillAggregate salaryBillAggregate) throws Exception {
		try {
			BigDecimal grossAmount = BigDecimal.ZERO;
			List payslips = (List) salaryBillAggrMap.get(salaryBillAggregate);
			for (Iterator iter = payslips.iterator(); iter.hasNext();) {
				EmpPayroll empPayroll = (EmpPayroll) iter.next();
				grossAmount = grossAmount.add(empPayroll.getGrossPay());
			}
			return grossAmount;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	// ////GETTING NETAMOUNT & EMPLOYEE MAP PER BILL \\\\\\\\

	public HashMap getEmployeeNetAmountMapPerBill(HashMap salaryBillAggrMap,
			SalaryBillAggregate salaryBillAggregate) throws Exception {
		try {
			HashMap enahm = new HashMap();
			List payslips = (List) salaryBillAggrMap.get(salaryBillAggregate);
			for (Iterator iter = payslips.iterator(); iter.hasNext();) {
				EmpPayroll empPayroll = (EmpPayroll) iter.next();
				PersonalInformation employee = empPayroll.getEmployee();
				BigDecimal amount = (BigDecimal) enahm.get(employee);
				if (amount == null) {
					amount = BigDecimal.ZERO;
					amount = amount.add(empPayroll.getNetPay());
					enahm.put(employee, amount);
				} else {
					amount = amount.add(empPayroll.getNetPay());
					enahm.put(employee, amount);
				}
			}
			return enahm;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	// // GETTING SET OF BILLPAYDETAILS OBJECT FOR NET AMOUNT PER EMPLOYEE PER
	// BILL \\\\
	public Set getBillPaydetailsesForNetAmount(
			HashMap employeeNetAmountMapPerBill,
			SalaryBillAggregate salaryBilAggr, EgBilldetails billDetail)
			throws Exception {
		try {
			CChartOfAccounts chartOfAccount = null;
			BigDecimal doToatalAmount = BigDecimal.ZERO;
			// Getting chartofaccount from empType master instead of getting by
			// purposeId
			if (salaryBilAggr.getEmpType() != null
					&& salaryBilAggr.getEmpType().getCoa() != null) {
				chartOfAccount = salaryBilAggr.getEmpType().getCoa();
			} else {
				throw new EGOVRuntimeException(
						"For this Bill salary payable account is not defined in employee type master");
			}
			Accountdetailtype accDetailType = getAccountDetailTypeToBeUsedInBillForGlcode(chartOfAccount);
			Set billPaydetailses = billDetail.getEgBillPaydetailes();
			if (accDetailType != null) {
				Iterator iter = employeeNetAmountMapPerBill.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry pairs = (Map.Entry) iter.next();
					// Getting accountDetailType depending on config and
					// sub-ledger configuration
					if ("Employee".equals(accDetailType.getName())) {
						EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();
						egBillPaydetail.setAccountDetailTypeId(accDetailType
								.getId());

						PersonalInformation employee = (PersonalInformation) pairs
								.getKey();
						egBillPaydetail.setAccountDetailKeyId(employee
								.getIdPersonalInformation());

						egBillPaydetail.setCreditAmount((BigDecimal) pairs
								.getValue());
						egBillPaydetail.setEgBilldetailsId(billDetail);
						billPaydetailses.add(egBillPaydetail);
					} else if ("DrawingOfficer".equals(accDetailType.getName())) {
						doToatalAmount = doToatalAmount.add((BigDecimal) pairs
								.getValue());
					}
				}
				if ("DrawingOfficer".equals(accDetailType.getName())) {
					EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();
					egBillPaydetail.setAccountDetailTypeId(accDetailType
							.getId());
					egBillPaydetail.setAccountDetailKeyId(salaryBilAggr
							.getDrawingOfficer().getId());
					egBillPaydetail.setCreditAmount(doToatalAmount);
					egBillPaydetail.setEgBilldetailsId(billDetail);
					billPaydetailses.add(egBillPaydetail);
				}
			}
			return billPaydetailses;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	// //// Getting all earnings and create map of Account--employee,amount
	// \\\\\\\\\\\\

	public HashMap getEarningAccountMapPerBill(HashMap salaryBillAggrMap,
			SalaryBillAggregate salaryBillAggregate) throws Exception {
		try {
			LOGGER.info("POPULATING EARNING ACCOUNT MAP--------------");
			HashMap eahm = new HashMap();
			List payslipsOfBill = (List) salaryBillAggrMap
					.get(salaryBillAggregate);
			for (Iterator iter = payslipsOfBill.iterator(); iter.hasNext();) {
				EmpPayroll empPayroll = (EmpPayroll) iter.next();
				PersonalInformation employee = empPayroll.getEmployee();
				for (Iterator iter1 = empPayroll.getEarningses().iterator(); iter1
						.hasNext();) {
					Earnings earning = (Earnings) iter1.next();
					if (earning.getAmount().compareTo(BigDecimal.ZERO) > 0) {
						// Aggregating depending uppon chartsofAccount not by
						// tds
						CChartOfAccounts chartOfAccount = null;
						if (earning.getSalaryCodes() != null) {
							if (earning.getSalaryCodes().getTdsId() != null) {
								chartOfAccount = earning.getSalaryCodes()
										.getTdsId().getChartofaccounts();
							} else {
								chartOfAccount = earning.getSalaryCodes()
										.getChartofaccounts();
							}
						}
						HashMap employeeTdsAmountMap = (HashMap) eahm
								.get(chartOfAccount);
						TdsEmployee tdsEmployee = new TdsEmployee();
						Accountdetailtype accDetailType = getAccountDetailTypeToBeUsedInBillForGlcode(chartOfAccount);
						if (accDetailType != null) {
							if ("Employee".equals(accDetailType.getName())) {
								tdsEmployee.setEntity(employee);
							} else if ("DrawingOfficer".equals(accDetailType
									.getName())) {
								tdsEmployee.setEntity(salaryBillAggregate
										.getDrawingOfficer());
							}
						}
						if (employeeTdsAmountMap == null) {
							employeeTdsAmountMap = new HashMap();
							BigDecimal amount = (BigDecimal) employeeTdsAmountMap
									.get(tdsEmployee);
							if (amount == null) {
								amount = BigDecimal.ZERO;
								amount = amount.add(earning.getAmount());
								employeeTdsAmountMap.put(tdsEmployee, amount);
								/*
								 * tdsAmount = new TdsAmount();
								 * tdsAmount.setAmount(earning.getAmount());
								 * employeeAmountMap.put(employee, tdsAmount);
								 */
							}
							eahm.put(chartOfAccount, employeeTdsAmountMap);
						} else {
							BigDecimal amount = (BigDecimal) employeeTdsAmountMap
									.get(tdsEmployee);
							if (amount == null) {
								amount = BigDecimal.ZERO;
								amount = amount.add(earning.getAmount());
								employeeTdsAmountMap.put(tdsEmployee, amount);
								/*
								 * tdsAmount = new TdsAmount();
								 * tdsAmount.setAmount(earning.getAmount());
								 * employeeAmountMap.put(employee, tdsAmount);
								 */
							} else {
								amount = amount.add(earning.getAmount());
								employeeTdsAmountMap.put(tdsEmployee, amount);
								/*
								 * tdsAmount.setAmount(tdsAmount.getAmount().add(
								 * earning.getAmount()));
								 * employeeAmountMap.put(employee, tdsAmount);
								 */
							}
							eahm.put(chartOfAccount, employeeTdsAmountMap);
						}
					}
				}
			}
			return eahm;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * GETTING ALL DEDUCTIONS PER BILL & CREATEING DEDUCTION ACCOUNT MAP
	 * 
	 * @param salaryBillAggrMap
	 * @param salaryBillAggregate
	 * @return
	 * @throws Exception
	 */
	public HashMap getDeductionAccountMapPerBill(HashMap salaryBillAggrMap,
			SalaryBillAggregate salaryBillAggregate) throws Exception {
		try {
			LOGGER.debug("POPULATING DEDUCTION ACCOUNT MAP--------------");
			HashMap dahm = new HashMap();
			List payslipsOfBill = (List) salaryBillAggrMap
					.get(salaryBillAggregate);
			for (Iterator iter = payslipsOfBill.iterator(); iter.hasNext();) {
				EmpPayroll empPayroll = (EmpPayroll) iter.next();
				PersonalInformation employee = empPayroll.getEmployee();
				for (Iterator iter1 = empPayroll.getDeductionses().iterator(); iter1
						.hasNext();) {
					Deductions deduction = (Deductions) iter1.next();
					if (deduction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal dedAmounts[] = new BigDecimal[2];
						CChartOfAccounts chartOfAccounts[] = new CChartOfAccounts[2];
						// check if Deduction is of Schedule type
						Recovery tds[] = new Recovery[2];

						// Spliting installment amount into two
						// accounts(principal,interest)

						if (deduction.getSalaryCodes() != null
								&& deduction.getSalaryCodes()
										.getInterestAccount() != null) {
							if (deduction.getSalAdvances() != null
									&& deduction.getSalAdvances()
											.getMaintainSchedule()
											.equalsIgnoreCase("Y")) {

								chartOfAccounts[0] = null;
								tds[0] = null;
								if (deduction.getSalaryCodes().getTdsId() != null) {
									chartOfAccounts[0] = deduction
											.getSalaryCodes().getTdsId()
											.getChartofaccounts();
									tds[0] = deduction.getSalaryCodes()
											.getTdsId();
								} else {
									chartOfAccounts[0] = deduction
											.getSalaryCodes()
											.getChartofaccounts();
								}

								chartOfAccounts[1] = deduction.getSalaryCodes()
										.getInterestAccount();
								dedAmounts[0] = deduction.getAdvanceScheduler()
										.getPrincipalAmt();
								dedAmounts[1] = deduction.getAdvanceScheduler()
										.getInterestAmt();

							} else {
								chartOfAccounts[0] = null;
								dedAmounts[0] = deduction.getAmount();
								tds[0] = null;
								if (deduction.getSalaryCodes().getTdsId() != null) {
									chartOfAccounts[0] = deduction
											.getSalaryCodes().getTdsId()
											.getChartofaccounts();
									tds[0] = deduction.getSalaryCodes()
											.getTdsId();
								} else {
									chartOfAccounts[0] = deduction
											.getSalaryCodes()
											.getChartofaccounts();
								}
								chartOfAccounts[1] = deduction.getSalaryCodes()
										.getInterestAccount();
								BigDecimal installmentAmnt = deduction
										.getAmount();
								BigDecimal noOfInstallment = deduction
										.getSalAdvances().getNumOfInst();
								BigDecimal interestRate = deduction
										.getSalAdvances().getInterestPct();
								BigDecimal temp = new BigDecimal(100)
										.multiply(new BigDecimal(12));
								BigDecimal tempDivisor = interestRate.multiply(
										noOfInstallment).divide(temp, 2,
										RoundingMode.HALF_UP);
								BigDecimal divisor = BigDecimal.ONE
										.add(tempDivisor);
								BigDecimal principalAmnt = installmentAmnt
										.divide(divisor, 2,
												RoundingMode.HALF_UP);
								BigDecimal interestAmnt = installmentAmnt
										.subtract(principalAmnt);
								dedAmounts[0] = principalAmnt;
								dedAmounts[1] = interestAmnt;
							}
						} else {
							chartOfAccounts[0] = null;
							dedAmounts[0] = deduction.getAmount();
							tds[0] = null;
							if (deduction.getSalaryCodes() != null) {
								if (deduction.getSalaryCodes().getTdsId() != null) {
									chartOfAccounts[0] = deduction
											.getSalaryCodes().getTdsId()
											.getChartofaccounts();
									tds[0] = deduction.getSalaryCodes()
											.getTdsId();
								} else {
									chartOfAccounts[0] = deduction
											.getSalaryCodes()
											.getChartofaccounts();
								}
							} else {
								chartOfAccounts[0] = deduction
										.getChartofaccounts();
							}
						}

						for (int i = 0; i <= 1; i++) {
							if (chartOfAccounts[i] != null) {
								HashMap employeeTdsAmountMap = (HashMap) dahm
										.get(chartOfAccounts[i]);
								TdsEmployee tdsEmployee = new TdsEmployee();
								Accountdetailtype accDetailType = getAccountDetailTypeToBeUsedInBillForGlcode(chartOfAccounts[i]);
								if (accDetailType != null) {
									if ("Employee".equals(accDetailType
											.getName())) {
										tdsEmployee.setEntity(employee);
									} else if ("DrawingOfficer"
											.equals(accDetailType.getName())) {
										tdsEmployee
												.setEntity(salaryBillAggregate
														.getDrawingOfficer());
									}
								}
								tdsEmployee.setTds(tds[0]);
								if (employeeTdsAmountMap == null) {
									employeeTdsAmountMap = new HashMap();
									BigDecimal amount = (BigDecimal) employeeTdsAmountMap
											.get(tdsEmployee);
									if (amount == null) {
										amount = BigDecimal.ZERO;
										amount = amount.add(dedAmounts[i]);
										employeeTdsAmountMap.put(tdsEmployee,
												amount);
									}
									dahm.put(chartOfAccounts[i],
											employeeTdsAmountMap);
								} else {
									BigDecimal amount = (BigDecimal) employeeTdsAmountMap
											.get(tdsEmployee);
									if (amount == null) {
										amount = BigDecimal.ZERO;
										amount = amount.add(dedAmounts[i]);
										employeeTdsAmountMap.put(tdsEmployee,
												amount);
									} else {
										amount = amount.add(dedAmounts[i]);
										employeeTdsAmountMap.put(tdsEmployee,
												amount);
									}
									dahm.put(chartOfAccounts[i],
											employeeTdsAmountMap);
								}
							}
						}
					}
				}
			}
			return dahm;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}

	}

	// ////// POPULATE BILDETAILS & BILLPAYDETAILS \\\\\\\\\\\\\\\\\\\\\\

	public void populateBilldetails(HashMap accountMapPerBill,
			SalaryBillAggregate salaryBillAggregate,
			EgBillregister egBillregister, String type) throws Exception {

		try {
			LOGGER.debug("POPULATING BILL DETAILS-----------------");
			Set billDetailses = egBillregister.getEgBilldetailes();
			Iterator it1 = accountMapPerBill.entrySet().iterator();
			while (it1.hasNext()) {
				EgBilldetails egBilldetail = new EgBilldetails();
				egBilldetail.setEgBillregister(egBillregister);
				if (salaryBillAggregate.getFunction() != null) {
					egBilldetail
							.setFunctionid(BigDecimal
									.valueOf(salaryBillAggregate.getFunction()
											.getId()));
				}
				BigDecimal totalAmountPerAccount = BigDecimal.ZERO;
				Map.Entry pairs1 = (Map.Entry) it1.next();
				// / PUT chartOfAccount----GLCODEID IN BILLDETAILS TABLE
				// \\\\\\\\\\\\\\\\\
				CChartOfAccounts chartOfAccount = (CChartOfAccounts) pairs1
						.getKey();
				// logger.info("Account-"+chartOfAccount.getId());
				egBilldetail.setGlcodeid(BigDecimal.valueOf(chartOfAccount
						.getId()));
				HashMap employeTdsAmountMap = (HashMap) pairs1.getValue();
				Iterator it2 = employeTdsAmountMap.entrySet().iterator();
				Set billPayDetailses = egBilldetail.getEgBillPaydetailes();
				List<ValidationError> errorList = new ArrayList<ValidationError>();
				while (it2.hasNext()) {
					Map.Entry pairs2 = (Map.Entry) it2.next();
					// POPULATING BILLPAYDETAILS TABLE \\\\\\\\\\\\
					Accountdetailtype accDetailType = getAccountDetailTypeToBeUsedInBillForGlcode(chartOfAccount);
					TdsEmployee tdsEmployee = (TdsEmployee) pairs2.getKey();
					if (accDetailType != null) {
						EgBillPayeedetails egBillpayeedetails = new EgBillPayeedetails();
						// PersonalInformation employee = (PersonalInformation)
						// pairs2.getKey();
						egBillpayeedetails.setEgBilldetailsId(egBilldetail);
						egBillpayeedetails.setAccountDetailTypeId(accDetailType
								.getId());
						// if("Employee".equals(accDetailType.getName())){
						if (null == tdsEmployee
								|| null == tdsEmployee.getEntity()) {

							String validationMsg = "Account code "
									+ chartOfAccount.getGlcode()
									+ " is incorrect to be used for paybill";
							errorList.add(new ValidationError(validationMsg,
									validationMsg));

						} else {
							egBillpayeedetails
									.setAccountDetailKeyId(tdsEmployee
											.getEntity().getEntityId());
						}
						// }
						// else
						// if("DrawingOfficer".equals(accDetailType.getName())){
						// egBillpayeedetails.setAccountDetailKeyId(tdsEmployee.getEntity().getEntityId());
						// }
						if (("E").equals(type)) {
							egBillpayeedetails
									.setDebitAmount((BigDecimal) pairs2
											.getValue());
						} else {
							egBillpayeedetails
									.setCreditAmount((BigDecimal) pairs2
											.getValue());
							egBillpayeedetails
									.setRecovery(tdsEmployee.getTds());
						}
						billPayDetailses.add(egBillpayeedetails);
					}
					totalAmountPerAccount = totalAmountPerAccount
							.add((BigDecimal) pairs2.getValue());
				}
				if (!errorList.isEmpty())
					throw new ValidationException(errorList);
				egBilldetail.setEgBillPaydetailes(billPayDetailses);
				// // PUT netAmountPerAccount-----DEBITAMOUNT/CREDITAMOUNT IN
				// BULLDETAILS TABLE \\\\\\\\\\\\\\\
				if (("E").equals(type)) {
					// logger.info("Earning:------");
					egBilldetail.setDebitamount(totalAmountPerAccount);
				}
				if (("D").equals(type)) {
					// logger.info("Deduc:------");
					egBilldetail.setCreditamount(totalAmountPerAccount);
				}
				// logger.info("Total per Account-"+totalAmountPerAccount);
				// populateBillPayDetails(accountMapPerBill,
				// salaryBillAggregate, type, egBilldetail);
				// Checking for greater than zero .Then only adding to
				// billdetailSet in billregister
				if (egBilldetail.getDebitamount() != null
						&& egBilldetail.getDebitamount().compareTo(
								BigDecimal.ZERO) > 0) {
					billDetailses.add(egBilldetail);
				}
				if (egBilldetail.getCreditamount() != null
						&& egBilldetail.getCreditamount().compareTo(
								BigDecimal.ZERO) > 0) {
					billDetailses.add(egBilldetail);
				}
			}
			egBillregister.setEgBilldetailes(billDetailses);
		} catch (Exception e) {
			LOGGER.error("Error----------------" + e.getMessage());
			throw e;
		}
	}

	// populate information for rejected payslips \\\\\\

	public void populateRejectedPayslips(List<EmpPayroll> rejectablePayslips,
			User user) throws Exception {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
					Locale.getDefault());
			String txndate = payrollExternalInterface.getCurrentDate();
			for (EmpPayroll empPayroll : rejectablePayslips) {
				EgActiondetails egActiondetails = new EgActiondetails();
				egActiondetails.setActionDoneBy((UserImpl) user);
				egActiondetails.setActionDoneOn(formatter.parse(txndate));
				egActiondetails.setModuleid(Long.valueOf(empPayroll.getId())
						.intValue());
				egActiondetails.setComments(empPayroll.getRejectComment());
				egActiondetails.setModuletype("Payroll");
				egActiondetails.setCreatedby(user.getId());
				payrollExternalInterface.createEgActiondetails(egActiondetails);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	// create list of bill(S) form approved payslips \\\\\\\

	public List<EgBillregister> createSalaryBill(
			List<EmpPayroll> approvablePayslips, User user) throws Exception {
		LOGGER.info("BILL CREATION STARTED-----------------");
		final List<EmpPayroll> approvablePayslipsFinal = approvablePayslips;
		final User userFinal = user;
		return  HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<List<EgBillregister>>() 
		{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			String txndate = payrollExternalInterface.getCurrentDate();
			int payslipCount=0;
			String empFullName=null;
			List<EgBillregister> billRegisters = new ArrayList<EgBillregister>();
			//GETTING MAP OF AGGREGATING SALARY BILL
			@SuppressWarnings("rawtypes")
			HashMap salaryBillAggrMap = getSalaryBillAggregateMap(approvablePayslipsFinal);
			
			Statement stmt = null;
			public List<EgBillregister> execute(Connection connection) throws SQLException 
			{
				try
				{
					stmt =connection.createStatement();
					Iterator iter1 = salaryBillAggrMap.entrySet().iterator();
					while(iter1.hasNext())
					{
						EmpPayroll payslip = null;
						Map.Entry pairs1 = (Map.Entry) iter1.next();
						SalaryBillAggregate salaryBillAggregate = (SalaryBillAggregate) pairs1.getKey();
						List<EmpPayroll> payslips = (List<EmpPayroll>) pairs1.getValue();
						payslipCount=payslips.size();//checking this later to set payto, when its 1  empcode else multiple
						for(EmpPayroll empPayroll : payslips)
						{
							payslip = empPayroll;
							empFullName=empPayroll.getEmployee().getEmployeeFirstName();
							break;
						}
						
						//POPULATE  EG_BILLREGISTER
						EgBillregister egBillRegister = new EgBillregister();					
						egBillRegister.setBilldate(formatter.parse(txndate));
						egBillRegister.setBillamount(getGrossAmountPerBill(salaryBillAggrMap, salaryBillAggregate));
						egBillRegister.setPassedamount(getGrossAmountPerBill(salaryBillAggrMap, salaryBillAggregate));
						egBillRegister.setBillstatus("APPROVED");			
						egBillRegister.setCreatedBy(userFinal);
						egBillRegister.setCreatedDate(formatter.parse(txndate));
						egBillRegister.setModifiedBy(userFinal);
						EgBillregistermis egBillregisterMis = new EgBillregistermis();
						egBillregisterMis.setEgBillregister(egBillRegister);
						
						if(salaryBillAggregate.getFund() != null)
						{
							egBillregisterMis.setFund(salaryBillAggregate.getFund());
						}
						if(salaryBillAggregate.getFunctionary() != null)
						{
							egBillregisterMis.setFunctionaryid(salaryBillAggregate.getFunctionary());
						}
						if(salaryBillAggregate.getDepartment() != null)
						{
							egBillregisterMis.setEgDepartment((DepartmentImpl)salaryBillAggregate.getDepartment());
						}
									
						egBillregisterMis.setMonth(payslip.getMonth());
						egBillregisterMis.setFinancialyear(payslip.getFinancialyear());
						egBillregisterMis.setBudgetCheckReq(true);			
						if(salaryBillAggregate.getDrawingOfficer() != null)
						{
							egBillregisterMis.setPayto(salaryBillAggregate.getDrawingOfficer().getCode());
						}
						else if(payslipCount==1 )
						{
							egBillregisterMis.setPayto(empFullName);
						}
						else
						{
							egBillregisterMis.setPayto("Multiple");
						}
						egBillRegister.setEgBillregistermis(egBillregisterMis);
						if(salaryBillAggregate.getBillType() != null)
						{
							if(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY.equals(salaryBillAggregate.getBillType()))
							{
								String billNumber = payrollExternalInterface.getTxnNumber("SAL", txndate, connection);	
								LOGGER.info("Bill Number..........."+billNumber);
								egBillRegister.setBillnumber(billNumber);
								egBillRegister.setExpendituretype(salaryBillAggregate.getBillType());
								EgwStatus status = payrollExternalInterface.getStatusByModuleAndDescription("SALBILL", "Approved");
								egBillRegister.setStatus(status);
							}
							else if(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equals(salaryBillAggregate.getBillType()))
							{
								String billNumber = getNextBillNumber(egBillRegister);			
								LOGGER.info("Bill Number..........."+billNumber);
								egBillRegister.setBillnumber(billNumber);
								egBillRegister.setExpendituretype(salaryBillAggregate.getBillType());
								EgwStatus status = payrollExternalInterface.getStatusByModuleAndDescription(FinancialConstants.CONTINGENCYBILL_FIN, "Approved");
								egBillRegister.setStatus(status);
							}
						}
						/*else{
							String billNumber = payrollExternalInterface.getTxnNumber("SAL", txndate, con);			
							LOGGER.info("Bill Number..........."+billNumber);
							egBillRegister.setBillnumber(billNumber);
							egBillRegister.setExpendituretype("Salary");
							EgwStatus status = payrollExternalInterface.getStatusByModuleAndDescription("SALBILL", "Approved");
							egBillRegister.setStatus(status);
						}*/
							
						//POPULATE EG_BILLREGISTERMIS
						
			
						HashMap earningAccountMapPerBill = getEarningAccountMapPerBill(salaryBillAggrMap, salaryBillAggregate);			
						LOGGER.info("MAP--"+salaryBillAggrMap+"www"+salaryBillAggregate);			
						HashMap deductionAccountMapPerBill = getDeductionAccountMapPerBill(salaryBillAggrMap, salaryBillAggregate);
						
						//POPULATE EARNINGS & DEDUCTIONS BILLDETAILS & BILLPAYDETAILS TABLE
						populateBilldetails(earningAccountMapPerBill,salaryBillAggregate,egBillRegister,"E");
						populateBilldetails(deductionAccountMapPerBill,salaryBillAggregate,egBillRegister,"D");
			
						//POPULATE  NET AMOUNT PER BILL	IN EG_BILDETAILS FOR INTERNAL CODE
						BigDecimal netAmountPerBill = getNetAmountPerBill(salaryBillAggrMap, salaryBillAggregate);
						//Getting chartofaccount from empType master instead of getting by purposeId			
						CChartOfAccounts chartOfAccount = null;
						if(salaryBillAggregate.getEmpType() != null && salaryBillAggregate.getEmpType().getCoa() != null)
						{
							chartOfAccount = salaryBillAggregate.getEmpType().getCoa();
						}
						else
						{
							throw new EGOVRuntimeException("For this Bill salary payable account is not defined in employee type master");
						}
						EgBilldetails egBilldetails = new EgBilldetails();
						egBilldetails.setCreditamount(netAmountPerBill);
						egBilldetails.setEgBillregister(egBillRegister);
						if(salaryBillAggregate.getFunction() != null)
						{
							egBilldetails.setFunctionid(BigDecimal.valueOf(salaryBillAggregate.getFunction().getId()));
						}
						egBilldetails.setGlcodeid(BigDecimal.valueOf(chartOfAccount.getId()));
			
						//POPULATE NET AMOUNT PER EMPLOYEE PER BILL IN EG_BILLPAYDETAILS TABLE
						HashMap employeeNetAmountMap = getEmployeeNetAmountMapPerBill(salaryBillAggrMap, salaryBillAggregate);
						Set billPaydetailses = getBillPaydetailsesForNetAmount(employeeNetAmountMap, salaryBillAggregate, egBilldetails);
						egBilldetails.setEgBillPaydetailes(billPaydetailses);
			
						Set billDetailses =  egBillRegister.getEgBilldetailes();
						if(egBilldetails.getCreditamount() != null && egBilldetails.getCreditamount().compareTo(BigDecimal.ZERO) > 0)
						{
							billDetailses.add(egBilldetails);
						}
						egBillRegister.setEgBilldetailes(billDetailses);						
						//Calling budget check API to check the budget before creating bill 
						Boolean isBudgetCheckSucceed = false;
						try
						{
							isBudgetCheckSucceed = payrollExternalInterface.isBudgetCheckSucceedForBill(egBillRegister);
						}
						catch(ValidationException e)
						{
							List<ValidationError> errorList = new ArrayList<ValidationError>();
							errorList.add(new ValidationError("Problem in budget check","Problem in budget check"));
							errorList.addAll(e.getErrors());
							throw new ValidationException(errorList);
						}
						//Setting bill object to the payslips if budget check is success
						if(isBudgetCheckSucceed)
						{
							for(EmpPayroll empPayroll : payslips)
							{
								empPayroll.setBillRegister(egBillRegister);
							}
							billRegisters.add(egBillRegister);
						}
					}
				}
				catch(Exception e)
				{
					LOGGER.error("Error is "+e.getMessage());
				}
				return billRegisters;
			}	
	});

	}

	/*
	 * //////// POPULATE BILLPAYDETAILS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 * 
	 * public void populateBillPayDetails(HashMap
	 * accountMapPerBill,SalaryBillAggregate salaryBillAggregate, String type,
	 * EgBilldetails egBilldetails) throws Exception{ Set billPaydetailses =
	 * egBilldetails.getEgBillPaydetailes(); Iterator it1 =
	 * accountMapPerBill.entrySet().iterator(); while(it1.hasNext()) { Map.Entry
	 * pairs1 = (Map.Entry) it1.next(); CChartOfAccounts chartOfAccount =
	 * (CChartOfAccounts) pairs1.getKey(); //
	 * System.out.println("Account-"+chartOfAccount.getId()); HashMap
	 * employeeAmountMap = (HashMap) pairs1.getValue(); Iterator it2 =
	 * employeeAmountMap.entrySet().iterator(); while(it2.hasNext()){ Connection
	 * con = HibernateUtil.getCurrentSession().connection(); int
	 * accountDetailTypeId =
	 * commonManager.getDetailTypeId(chartOfAccount.getGlcode(),con); Map.Entry
	 * pairs2 = (Map.Entry) it2.next(); PersonalInformation employee =
	 * (PersonalInformation) pairs2.getKey();
	 * 
	 * EgBillPayeedetails egBillpayeedetails = new EgBillPayeedetails();
	 * egBillpayeedetails.setEgBilldetailsId(egBilldetails);
	 * egBillpayeedetails.setAccountDetailTypeId(accountDetailTypeId);
	 * egBillpayeedetails
	 * .setAccountDetailKeyId(employee.getIdPersonalInformation());
	 * if(type.equals("E"))
	 * egBillpayeedetails.setDebitAmount((BigDecimal)pairs2.getValue()); else
	 * egBillpayeedetails.setCreditAmount((BigDecimal)pairs2.getValue());
	 * 
	 * billPaydetailses.add(egBillpayeedetails);
	 * System.out.println("Employee per Account-"
	 * +employee.getIdPersonalInformation());
	 * System.out.println("Total per Employee for Acc-"
	 * +(BigDecimal)pairs2.getValue()); } }
	 * egBilldetails.setEgBillPaydetailes(billPaydetailses); }
	 */

	protected Accountdetailtype getAccountDetailTypeToBeUsedInBillForGlcode(
			CChartOfAccounts coa) throws Exception {
		Accountdetailtype accDetailType = null;
		List<Accountdetailtype> accDetailTypeList = payrollExternalInterface
				.getAccountdetailtypeListByGLCode(coa.getGlcode());
		if (accDetailTypeList != null) {
			if (accDetailTypeList.size() > 1) {
				String doSubledger = EGovConfig.getAppConfigValue("Payroll",
						"SalaryBillSubledgerByDO", "false");
				if ("true".equalsIgnoreCase(doSubledger)) {
					for (Accountdetailtype obj : accDetailTypeList) {
						if ("DrawingOfficer".equals(obj.getName())) {
							accDetailType = obj;
						}
					}
				} else {
					for (Accountdetailtype obj : accDetailTypeList) {
						if ("Employee".equals(obj.getName())) {
							accDetailType = obj;
						}
					}
				}
				if (accDetailType == null) {
					throw new PayslipFailureException(
							"This glcode("
									+ coa.getGlcode()
									+ ") is mapped to neither employe nor drawing officer");
				}
			} else {
				accDetailType = accDetailTypeList.get(0);
			}
		}

		return accDetailType;
	}

	private String getNextBillNumber(EgBillregister bill) {
		String billNumber = null;
		CFinancialYear finYear = commonsService.getFinancialYearByDate(bill.getBilldate());
		String financialYearId = finYear.getId().toString();
		if (financialYearId == null) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"no.financial.year", "No Financial Year for bill date"
							+ sdf.format(bill.getBilldate()))));
		}
		CFinancialYear financialYear = commonsService.getFinancialYearById(Long
				.valueOf(financialYearId));
		String year = financialYear.getFinYearRange();
		Script billNumberScript = (Script) persistenceService
				.findAllByNamedQuery(Script.BY_NAME,
						"egf.bill.number.generator").get(0);
		ScriptContext scriptContext = ScriptService.createContext(
				"sequenceGenerator", sequenceGenerator, "sItem", bill, "year",
				year);
		billNumber = (String) scriptExecutionService.executeScript(
				billNumberScript.getName(), scriptContext);
		if (billNumber == null) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"unable.to.generate.bill.number",
					"No Financial Year for bill date"
							+ sdf.format(bill.getBilldate()))));
		}
		return billNumber;
	}

	public ScriptService getScriptExecutionService() {
		return scriptExecutionService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public SequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}
}
