/**
 *
 */
package org.egov.egf.commons;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.CommonsService;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryTypeImpl;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.model.budget.BudgetUsage;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.services.report.FundFlowService;
import org.egov.utils.Constants;
import org.egov.web.actions.report.BillRegisterReportBean;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.transform.Transformers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author msahoo
 * 
 */
public class EgovCommon {

	private static final Logger LOGGER = Logger.getLogger(EgovCommon.class);
	private PersistenceService persistenceService;
	private GenericHibernateDaoFactory genericDao;
	private CommonsService commonsService;
	private UserService userService;
	private FundFlowService fundFlowService;

	public FundFlowService getFundFlowService() {
		return fundFlowService;
	}

	public void setFundFlowService(FundFlowService fundFlowService) {
		this.fundFlowService = fundFlowService;
	}

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public EgovCommon() {

	}

	public Boundary getBoundaryForUser(User user) {
		Set<JurisdictionValues> s = userService.getJurisdictionsForUser(
				user.getId(), new Date());
		if (!s.isEmpty() && s.iterator().hasNext())
			return s.iterator().next().getBoundary();
		return null;
	}

	public Department getDepartmentForUser(User user,
			EmployeeService employeeService, EisUtilService eisService,
			PersistenceService persistenceService) {
		try {
			Query qry1 = persistenceService
					.getSession()
					.createSQLQuery(
							" select is_primary, dept_id from EG_EIS_EMPLOYEEINFO employeevi0_ where upper(trim(employeevi0_.CODE))="
									+ employeeService.getEmpForUserId(
											user.getId()).getCode()
									+ "and ((employeevi0_.TO_DATE is null) and employeevi0_.FROM_DATE<=SYSDATE or employeevi0_.FROM_DATE<=SYSDATE and employeevi0_.TO_DATE>SYSDATE or employeevi0_.FROM_DATE in (select MAX(employeevi1_.FROM_DATE) from EG_EIS_EMPLOYEEINFO employeevi1_ where employeevi1_.ID=employeevi0_.ID and  not (exists (select employeevi2_.ID from EG_EIS_EMPLOYEEINFO employeevi2_ where employeevi2_.ID=employeevi0_.ID and ((employeevi2_.TO_DATE is null) and employeevi2_.FROM_DATE<=SYSDATE or employeevi2_.FROM_DATE<=SYSDATE and employeevi2_.TO_DATE>SYSDATE))))) ");
			List<Object[]> employeeViewList = (List) qry1.list();
			if (!employeeViewList.isEmpty()) {
				if (employeeViewList.size() == 1) {
					return (Department) persistenceService.find(
							"from DepartmentImpl where id=?", Integer
									.valueOf((employeeViewList.get(0)[1]
											.toString())));
				} else {
					for (Object[] object : employeeViewList) {
						if (object[0].toString().equals("N")) {
							return (Department) persistenceService.find(
									"from DepartmentImpl where id=?",
									Integer.valueOf(object[1].toString()));
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Could not get list of assignments", e);
		}
		return null;
	}

	/**
	 * @author manoranjan
	 * @param VoucherDate
	 * @param cashInHandCode
	 * @param fundId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getCashBalance(final Date VoucherDate,
			final String cashInHandCode, final Integer fundId) {
		LOGGER.debug("EgovCommon | getCashBalance");
		BigDecimal opeAvailable1 = BigDecimal.ZERO;
		BigDecimal opeAvailable2 = BigDecimal.ZERO;
		try {
			StringBuffer opBalncQuery1 = new StringBuffer(300);
			opBalncQuery1
					.append("SELECT decode(sum(openingdebitbalance),null,0,sum(openingdebitbalance))-")
					.append(" decode(sum(openingcreditbalance),null,0,sum(openingcreditbalance)) as openingBalance from TransactionSummary")
					.append(" where financialyear.id = ( select id from CFinancialYear where startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("') and glcodeid.glcode=? and fund.id=?");
			List<Object> tsummarylist = (List<Object>) getPersistenceService()
					.findAllBy(opBalncQuery1.toString(), cashInHandCode, fundId);
			opeAvailable1 = BigDecimal.valueOf((Double) tsummarylist.get(0));

			final List<AppConfigValues> appList = genericDao
					.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
							"EGF", "cancelledstatus");
			final String statusExclude = appList.get(0).getValue();

			StringBuffer opBalncQuery2 = new StringBuffer(300);
			opBalncQuery2
					.append("SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount)))")
					.append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE gl.voucherHeaderId.id=vh.id and gl.glcode='")
					.append(cashInHandCode)
					.append("' and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append(" 'and vh.status not in (").append(statusExclude)
					.append(") and vh.fundId.id=?");

			List<Object> list = (List<Object>) getPersistenceService()
					.findAllBy(opBalncQuery2.toString(), fundId);
			opeAvailable2 = BigDecimal.valueOf((Double) list.get(0));
		} catch (HibernateException e) {
			LOGGER.debug("exception occuered while geeting cash balance");
			throw new HibernateException(e);
		}
		return opeAvailable1.add(opeAvailable2);

	}

	/**
	 * @author manoranjan
	 * @param VoucherDate
	 * @param bankId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getAccountBalance(final Date VoucherDate,
			final Integer bankId) {
		return getAccountBalance(VoucherDate, bankId, null, null);
	}

	/**
	 * This method will return the amount that are available to make further
	 * payments.
	 * 
	 * @param VoucherDate
	 * @param bankaccountId
	 * @return
	 * @throws ValidationException
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getBankBalanceAvailableforPayment(final Date VoucherDate,
			final Integer bankaccountId) throws ValidationException {
		// return getAccountBalance(VoucherDate, bankId,null,null);
		BigDecimal TotalbankBalance = BigDecimal.ZERO;
		BigDecimal bankBalanceasofBankBookReport = BigDecimal.ZERO;
		BigDecimal amountApprovedForPayment = BigDecimal.ZERO;
		bankBalanceasofBankBookReport = getAccountBalance(VoucherDate,
				bankaccountId, null, null);
		LOGGER.debug("Bank balance as per Bank book:"
				+ bankBalanceasofBankBookReport);
		amountApprovedForPayment = getAmountApprovedForPaymentAndVoucherNotCreated(
				VoucherDate, bankaccountId);
		LOGGER.debug("Amount that are approved but voucher creation in progress:"
				+ amountApprovedForPayment);
		TotalbankBalance = bankBalanceasofBankBookReport
				.subtract(amountApprovedForPayment);
		LOGGER.debug("Total amount available for payment :" + TotalbankBalance);
		return TotalbankBalance;
	}

	/**
	 * This function will return the bank amount that are blocked for payment.
	 * There are voucher that are in approval process for which some amount will
	 * be approved. This method will return the total amount that are blocked.
	 * 
	 * @param VoucherDate
	 * @param bankaccountId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getAmountApprovedForPaymentAndVoucherNotCreated(
			final Date VoucherDate, final Integer bankaccountId) {
		LOGGER.debug("EgovCommon | getAmountApprovedForPaymentAndVoucherNotCreated");
		BigDecimal bankBalance = BigDecimal.ZERO;
		try {
			String paymentWFStatus = "";
			List<Object> list = (List<Object>) getPersistenceService()
					.findAllBy(
							"select chartofaccounts.id from Bankaccount where id=?",
							bankaccountId);
			Integer glcodeid = Integer.valueOf(list.get(0).toString());
			CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find(
					"from CChartOfAccounts where id=?", Long.valueOf(glcodeid));
			final List<AppConfigValues> paymentStatusList = genericDao
					.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
							"EGF", "PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK");
			for (AppConfigValues values : paymentStatusList) {
				paymentWFStatus = paymentWFStatus + "'" + values.getValue()
						+ "',";
			}
			if (!paymentWFStatus.equals(""))
				paymentWFStatus = paymentWFStatus.substring(0,
						paymentWFStatus.length() - 1);

			final List<AppConfigValues> preAppList = genericDao
					.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
							"EGF", "PREAPPROVEDVOUCHERSTATUS");
			final String preApprovedStatus = preAppList.get(0).getValue();

			StringBuffer paymentQuery = new StringBuffer(400);
			paymentQuery
					.append("SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount)))")
					.append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh,Paymentheader ph WHERE gl.voucherHeaderId.id=vh.id and ph.voucherheader.id=vh.id and gl.glcodeId=? ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("'and vh.status in (")
					.append(preApprovedStatus)
					.append(")")
					.append(" and ph.state in (from org.egov.infstr.models.State where type='Paymentheader' and value in (")
					.append(paymentWFStatus).append(") )");
			list = (List<Object>) getPersistenceService().findAllBy(
					paymentQuery.toString(), coa);
			bankBalance = BigDecimal.valueOf(Math.abs((Double) list.get(0)));

			LOGGER.debug("Total payment amount that are approved by FM Unit but voucher not yet created :"
					+ bankBalance);
		} catch (Exception e) {
			LOGGER.debug("exception occuered while geeting cash balance"
					+ e.getMessage());
			throw new HibernateException(e);
		}
		return bankBalance;
	}

	/**
	 * This method will return the total amount for the payment that are
	 * approved and cheques not assigned.
	 * 
	 * @param VoucherDate
	 * @param bankaccountId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getAmountForApprovedPaymentAndChequeNotAssigned(
			final Date voucherDate, final Integer bankaccountId) {
		LOGGER.debug("EgovCommon | getAmountForApprovedPaymentAndChequeNotAssigned");
		BigDecimal bankBalance = BigDecimal.ZERO;
		try {
			Bankaccount bankAccount = (Bankaccount) getPersistenceService()
					.find("from Bankaccount where id=?", bankaccountId);
			StringBuffer paymentQuery = new StringBuffer();
			// query to fetch vouchers for which no cheque has been assigned
			paymentQuery = paymentQuery
					.append("SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),"
							+ "null,0,sum(gl.creditAmount))) as amount FROM  GeneralLedger gl ,voucherheader vh, "
							+ " Paymentheader ph ,eg_wf_states es ,egf_instrumentvoucher iv right outer join voucherheader vh1 on "
							+ "vh1.id =iv.VOUCHERHEADERID WHERE gl.voucherHeaderId=vh.id and "
							+ "ph.voucherheaderid=vh.id and gl.glcodeId="
							+ bankAccount.getChartofaccounts().getId()
							+ " and "
							+ "vh.voucherDate >= (select startingDate from FinancialYear where  startingDate <= :date AND endingDate >=:date) and "
							+ " vh.voucherDate <= :date and ph.state_id=es.id and es.value='END' and vh.status=0 and vh1.id=vh.id and iv.VOUCHERHEADERID is null ")
					.append(" union ")
					// query to fetch vouchers for which cheque has been
					// assigned and surrendered
					.append("SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),"
							+ "null,0,sum(gl.creditAmount))) as amount FROM  GeneralLedger gl ,voucherheader vh, "
							+ " Paymentheader ph ,eg_wf_states es ,egf_instrumentvoucher iv,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader "
							+ "ih1, (select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,"
							+ "bankaccountid,instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
							+ "and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih WHERE gl.voucherHeaderId=vh.id and "
							+ "ph.voucherheaderid=vh.id and gl.glcodeId="
							+ bankAccount.getChartofaccounts().getId()
							+ " and "
							+ "vh.voucherDate >= (select startingDate from FinancialYear where  startingDate <= :date AND endingDate >=:date) and"
							+ " vh.voucherDate <= :date and ph.state_id=es.id and es.value='END' and vh.status=0 and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and "
							+ "ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign')");
			List<Object> list = (List<Object>) getPersistenceService()
					.getSession().createSQLQuery(paymentQuery.toString())
					.setDate("date", voucherDate).list();
			BigDecimal amount = (BigDecimal) list.get(0);
			bankBalance = amount == null ? BigDecimal.ZERO : amount;
			LOGGER.debug("Total payment amount that are approved by FM Unit but cheque not yet assigned:"
					+ bankBalance);
		} catch (Exception e) {
			LOGGER.debug("exception occuered while getting cash balance"
					+ e.getMessage());
			throw new HibernateException(e);
		}
		return bankBalance.abs();
	}

	@SuppressWarnings("unchecked")
	public BigDecimal getAccountBalance(final Date VoucherDate,
			final Integer bankId, final BigDecimal amount, final Long paymentId) {
		LOGGER.debug("EgovCommon | getCashBalance");
		LOGGER.info("--------------------------------------------------------------------------------getAccountBalance-----------------");

		LOGGER.info("-------------------------------------------------------------------------------------------------");

		BigDecimal opeAvailable = BigDecimal.ZERO;
		BigDecimal bankBalance = BigDecimal.ZERO;

		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",
						"Balance Check Based on Fund Flow Report");
		final String balanceChequeBasedOnFundFlowReport = appList.get(0)
				.getValue();

		try {
			if (balanceChequeBasedOnFundFlowReport.equalsIgnoreCase("Y")) {
				bankBalance = fundFlowService.getBankBalance(
						Long.valueOf(bankId), VoucherDate);
			} else {
				bankBalance = getAccountBalanceFromLedger(VoucherDate, bankId,
						amount, paymentId);
			}
			LOGGER.info("-------------------------------------------------------------------------------------bankBalance"
					+ bankBalance);
		} catch (ValidationException e) {
			LOGGER.error("Balance Check Failed" + e.getMessage());
			throw e;
		}
		return bankBalance;
	}

	@SuppressWarnings("unchecked")
	public BigDecimal getAccountBalanceFromLedger(final Date VoucherDate,
			final Integer bankId, final BigDecimal amount, final Long paymentId) {
		LOGGER.debug("EgovCommon | getCashBalance");
		BigDecimal opeAvailable = BigDecimal.ZERO;
		BigDecimal bankBalance = BigDecimal.ZERO;
		try {
			StringBuffer opBalncQuery1 = new StringBuffer(300);
			opBalncQuery1
					.append("SELECT decode(sum(openingdebitbalance),null,0,sum(openingdebitbalance))-")
					.append(" decode(sum(openingcreditbalance),null,0,sum(openingcreditbalance)) as openingBalance from TransactionSummary")
					.append(" where financialyear.id = ( select id from CFinancialYear where startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("') and glcodeid.id=(select chartofaccounts.id from Bankaccount where id=? )");
			List<Object> tsummarylist = (List<Object>) getPersistenceService()
					.findAllBy(opBalncQuery1.toString(), bankId);
			opeAvailable = BigDecimal.valueOf((Double) tsummarylist.get(0));

			LOGGER.debug("opeAvailable :" + opeAvailable);

			StringBuffer opBalncQuery2 = new StringBuffer(300);
			List<Object> list = (List<Object>) getPersistenceService()
					.findAllBy(
							"select chartofaccounts.id from Bankaccount where id=?",
							bankId);
			Integer glcodeid = Integer.valueOf(list.get(0).toString());

			final List<AppConfigValues> appList = genericDao
					.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
							"finance", "statusexcludeReport");
			final String statusExclude = appList.get(0).getValue();

			opBalncQuery2
					.append("SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount)))")
					.append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE gl.voucherHeaderId.id=vh.id and gl.glcodeId=? ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
					.append("'and vh.status not in (").append(statusExclude)
					.append(")");

			CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find(
					"from CChartOfAccounts where id=?", Long.valueOf(glcodeid));
			list = (List<Object>) getPersistenceService().findAllBy(
					opBalncQuery2.toString(), coa);
			bankBalance = BigDecimal.valueOf((Double) list.get(0));
			bankBalance = opeAvailable.add(bankBalance);

			// get the preapproved voucher amount also, if payment workflow
			// status in FMU level.... and subtract the amount from the balance
			// .

			boolean amountTobeInclude = false;

			if (paymentId != null) {
				// get the payment wf status
				State s = (State) persistenceService
						.find(" from org.egov.infstr.models.State where id in (select state.id from Paymentheader where id=?) ",
								paymentId);
				String paymentWFStatus = "";
				final List<AppConfigValues> paymentStatusList = genericDao
						.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
								"EGF",
								"PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK");
				for (AppConfigValues values : paymentStatusList) {
					if (s.getValue().equals(values.getValue()))
						amountTobeInclude = true;
					paymentWFStatus = paymentWFStatus + "'" + values.getValue()
							+ "',";
				}
				if (!paymentWFStatus.equals(""))
					paymentWFStatus = paymentWFStatus.substring(0,
							paymentWFStatus.length() - 1);

				final List<AppConfigValues> preAppList = genericDao
						.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
								"EGF", "PREAPPROVEDVOUCHERSTATUS");
				final String preApprovedStatus = preAppList.get(0).getValue();

				StringBuffer paymentQuery = new StringBuffer(400);
				paymentQuery
						.append("SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount)))")
						.append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh,Paymentheader ph WHERE gl.voucherHeaderId.id=vh.id and ph.voucherheader.id=vh.id and gl.glcodeId=? ")
						.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
						.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
						.append("' AND endingDate >='")
						.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
						.append("') and vh.voucherDate <='")
						.append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
						.append("'and vh.status in (")
						.append(preApprovedStatus)
						.append(")")
						.append(" and ph.state in (from org.egov.infstr.models.State where type='Paymentheader' and value in (")
						.append(paymentWFStatus).append(") )");
				list = (List<Object>) getPersistenceService().findAllBy(
						paymentQuery.toString(), coa);
				bankBalance = bankBalance.subtract(BigDecimal.valueOf(Math
						.abs((Double) list.get(0))));
				Integer voucherStatus = (Integer) persistenceService
						.find("select status from CVoucherHeader where id in (select voucherheader.id from Paymentheader where id=?)",
								paymentId);
				// if voucher is not preapproved and status is 0 then it is
				// modify so add the amount
				if (voucherStatus == 0) {
					amountTobeInclude = true;
				}
				// if payment workflow status in FMU level.... and add the
				// transaction amount to it.
				if (amountTobeInclude) {
					bankBalance = bankBalance.add(amount);
				}

			}
			LOGGER.debug("bankBalance :" + bankBalance);
		} catch (Exception e) {
			LOGGER.debug("exception occuered while geeting cash balance"
					+ e.getMessage());
			throw new HibernateException(e);
		}
		return bankBalance;
	}

	/**
	 * This method will return the instrument(not cancelled and not dishonored ) details of the vouchers for a given combination of AccountdetailTypeid and AccountdetailKeyid
	 * for which the subledger amount is on the CREDIT SIDE 
	 * 
	 * @param accountdetailType - detail type ID - cannot be null
	 * @param accountdetailKey - detail key ID - cannot be null
	 * @param voucherToDate - the upper limit of the voucherdates of the associated vouchers - current date is taken if null is passed
	 * 
	 * @return IMPORTANT - IF THERE ARE NO INSTRUMENTS ASSOCIATED WITH VOUCHERS FOR SUBLEDGER THEN NULL IS RETURNED
	 * 		   List<Map> is returned since there can be multiple instruments associated
	 * 		   Note - The keys for the map are type, number, date, amount
	 * @throws EGOVRuntimeException  accountdetailType or accountdetailkey parameter is null
	 * 		   EGOVRuntimeException  if any other exception	
	 * @author julian.prabhakar
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getInstrumentsDetailsForSubledgerTypeAndKey(Integer accountdetailType, Integer accountdetailKey, Date voucherToDate)
	{
		StringBuffer query = new StringBuffer(500);
		if(accountdetailType==null )
			throw new EGOVRuntimeException("AccountDetailType cannot be null");
		if( accountdetailKey==null )
			throw new EGOVRuntimeException("AccountDetailKey cannot be null");
		if(voucherToDate==null)
			voucherToDate = new Date();
		List<Map<String, Object>> resultList = null;
		
		try{
		
			query.append("select iv.instrumentHeaderId FROM CGeneralLedgerDetail gld, CGeneralLedger gl , CVoucherHeader vh, ")
				.append(" InstrumentVoucher iv WHERE gld.generalLedgerId=gl.id AND gl.voucherHeaderId.id=vh.id")
				.append(" AND iv.voucherHeaderId.id=vh.id AND gld.detailTypeId =? AND gld.detailKeyId=? AND gl.creditAmount >0")
				.append(" AND vh.status=0 ")
				.append(" AND vh.voucherDate<='")
				.append(Constants.DDMMYYYYFORMAT1.format(voucherToDate))
				.append("' AND upper(iv.instrumentHeaderId.statusId.description) not in ('CANCELLED' , 'DISHONORED' ) ");
			List<InstrumentHeader> instrumentHeaderList = (List<InstrumentHeader>) getPersistenceService()
					.findAllBy(query.toString(), accountdetailType,accountdetailKey);
			resultList = new ArrayList<Map<String,Object>>();
			Map<String, Object> instrumentMap = null;
			if(instrumentHeaderList!=null)
			{
				for(InstrumentHeader ih:instrumentHeaderList)
				{
					instrumentMap = new HashMap<String, Object>();
					instrumentMap.put("type", ih.getInstrumentType().getType());
					if(ih.getInstrumentNumber()==null)
					{
						instrumentMap.put("number", ih.getTransactionNumber());
						instrumentMap.put("date", ih.getTransactionDate());
					}
					else
					{
						instrumentMap.put("number", ih.getInstrumentNumber());
						instrumentMap.put("date", ih.getInstrumentDate());
					}
					
					instrumentMap.put("amount", ih.getInstrumentAmount());
					resultList.add(instrumentMap);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while getting Instrument details-"+ e.getMessage(), e);
			throw new EGOVRuntimeException("Exception occured while getting Instrument details-"+ e.getMessage());
		}	
		
		return ((resultList==null||resultList.isEmpty())?null:resultList);
	}


	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public EntityType getEntityType(Accountdetailtype accountdetailtype,
			Serializable detailkey) throws EGOVException {
		LOGGER.debug("EgovCommon | getEntityType| Start");
		EntityType entity = null;
		try {
			Class aClass = Class.forName(accountdetailtype
					.getFullQualifiedName());
			java.lang.reflect.Method method = aClass.getMethod("getId");
			String dataType = method.getReturnType().getSimpleName();
			LOGGER.debug("data Type = " + dataType);
			if (dataType.equals("Long")) {
				entity = (EntityType) persistenceService.find(" from "
						+ accountdetailtype.getFullQualifiedName()
						+ " where id=? ", Long.valueOf(detailkey.toString()));
			} else {
				entity = (EntityType) persistenceService.find(" from "
						+ accountdetailtype.getFullQualifiedName()
						+ " where id=? ", detailkey);
			}

		} catch (ClassCastException e) {
			LOGGER.error(e);
			throw new EGOVException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Exception to get EntityType=" + e.getMessage());
			throw new EGOVException(e.getMessage());
		}
		return entity;
	}

	/**
	 * This method will return the Map of cheque in hand and cash in hand code
	 * information for the boundary at which the books of accounts are
	 * maintained.
	 * 
	 * @return
	 * @throws ValidationException
	 */
	public Map<String, Object> getCashChequeInfoForBoundary()
			throws ValidationException {
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Map<String, Object>>() {

			@Override
			public Map<String, Object> execute(Connection connection)
					throws SQLException {
				

				String chequeInHand = null;
				Long chequeInHandId = null;
				String cashInHand = null;
				Long cashInHandId = null;
				// String
				// boundaryTypeval=EGovConfig.getProperty("egf_config.xml","city","","BoundaryType");
				final List<AppConfigValues> appList = genericDao
						.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
								Constants.EGF, "boundaryforaccounts");
				final String boundaryTypeval = appList.get(0).getValue();
				LOGGER.debug("Boundary Type Level  = " + boundaryTypeval);
				if (null == boundaryTypeval || boundaryTypeval.trim().equals(""))
					throw new ValidationException(Arrays.asList(new ValidationError(
							"configuration.parameter.missing",
							"boundaryforaccounts is missing in appconfig master")));

				List<BoundaryTypeImpl> listBoundType = (List<BoundaryTypeImpl>) persistenceService
						.findAllBy(
								"from BoundaryTypeImpl where lower(name)=? and lower(heirarchyType.name)='administration'",
								boundaryTypeval.toLowerCase());
				LOGGER.debug("listBoundType size   = " + listBoundType.size());
				Integer boundaryTypeId = listBoundType.get(0).getId();
				List<BoundaryImpl> listBndryLvl = (List<BoundaryImpl>) persistenceService
						.findAllBy("from BoundaryImpl where boundaryType.id=?",
								boundaryTypeId);
				LOGGER.debug("listBndryLvl size   = " + listBndryLvl.size());
				if (null != listBndryLvl && !listBndryLvl.isEmpty()) {
					BoundaryImpl boundary = listBndryLvl.get(0);
					Integer boundaryId = boundary.getId();
					try {
						
						Statement statement = connection.createStatement();
						StringBuffer sqlQuery1 = new StringBuffer(
								"SELECT glcode AS chequeinhand,id  "
										+ " FROM CHARTOFACCOUNTS where id = (SELECT chequeinhand FROM CODEMAPPING WHERE EG_BOUNDARYID=");
						sqlQuery1.append(boundaryId).append(")");
						LOGGER.debug("Cheque In hand account code query =" + sqlQuery1);
						ResultSet resultSet = statement.executeQuery(sqlQuery1
								.toString());
						if (resultSet.next()) {
							chequeInHand = resultSet.getString("chequeinhand");
							chequeInHandId = resultSet.getLong("id");
						}
						LOGGER.debug("chequeInHand is " + chequeInHand
								+ " chequeInHandId is " + chequeInHandId);
						StringBuffer sqlQuery2 = new StringBuffer(
								"SELECT glcode AS cashinhand,id  "
										+ " FROM CHARTOFACCOUNTS where id = (SELECT cashinhand FROM CODEMAPPING WHERE EG_BOUNDARYID=");
						sqlQuery2.append(boundaryId).append(")");
						LOGGER.debug("Cheque In hand account code query =" + sqlQuery2);
						resultSet = statement.executeQuery(sqlQuery2.toString());
						if (resultSet.next()) {
							cashInHand = resultSet.getString("cashinhand");
							cashInHandId = resultSet.getLong("id");
						}
						LOGGER.debug("cashInHand is " + cashInHand
								+ " cashInHandId is " + cashInHandId);
					} catch (Exception e) {
						LOGGER.error("Exception occuerd while getting  "
								+ e.getMessage());
						throw new EGOVRuntimeException(e.getMessage());
					}

				} else {
					LOGGER.debug("listBndryLvl is either null or blank");
					throw new ValidationException(Arrays.asList(new ValidationError(
							"boundary.value.missing", "Boundary value missing for"
									+ boundaryTypeval)));
				}
				Map<String, Object> boundaryMap = new HashMap<String, Object>();
				boundaryMap.put("listBndryLvl", listBndryLvl);
				boundaryMap.put("chequeInHand", chequeInHand);
				boundaryMap.put("cashInHand", cashInHand);
				boundaryMap.put("chequeInHandID", chequeInHandId);
				boundaryMap.put("cashInHandID", cashInHandId);
				return boundaryMap;

			
				
			}
		});
		
		
	}

	public boolean isShowChequeNumber() {
		String value = genericDao
				.getAppConfigValuesDAO()
				.getConfigValuesByModuleAndKey(Constants.EGF,
						Constants.CHEQUE_NO_GENERATION_APPCONFIG_KEY).get(0)
				.getValue();
		if ("Y".equalsIgnoreCase(value))
			return false;
		return true;
	}

	/**
	 * @author manoranjan
	 * @description - Get the account code balance for any glcode and the
	 *              subledger balance,If the accountdetail details are provided
	 *              then the account balance for the subledger needs to be
	 *              calculated, else the account code balance needs to be
	 *              provided.If the balance is positive that means it debit
	 *              balance , if it is a credit balance then the API will return
	 *              a -ve balance.
	 * @param asondate
	 *            - Mandatory
	 * @param glcode
	 *            - - Mandatory (validate the master data)-to get the balance
	 *            for this supplied account code.
	 * @param fundcode
	 *            -Mandatory (Fund code from fund master)
	 * @param accountdetailType
	 *            - optional (if supplied validate the master data)
	 * @param accountdetailkey
	 *            - optional (if supplied validate the master data)
	 * @return accCodebalance - returns the account code balance for a glcode
	 *         and subledger type.
	 * @throws ValidationException
	 *             -
	 */
	public BigDecimal getAccountBalanceforDate(Date asondate, String glcode,
			String fundcode, Integer accountdetailType, Integer accountdetailkey)
			throws ValidationException {

		LOGGER.debug("EgovCommon | getAccountBalanceforDate | Start");
		validateParameterData(asondate, glcode, fundcode, accountdetailType,
				accountdetailkey);
		LOGGER.debug("validation of data is sucessfull");
		BigDecimal opBalAsonDate = getOpeningBalAsonDate(asondate, glcode,
				fundcode, accountdetailType, accountdetailkey);
		BigDecimal glBalAsonDate = getGlcodeBalAsonDate(asondate, glcode,
				fundcode, accountdetailType, accountdetailkey);
		LOGGER.debug("EgovCommon | getAccountBalanceforDate | Start");
		return opBalAsonDate.add(glBalAsonDate);
	}

	private void validateParameterData(Date asondate, String glcode,
			String fundcode, Integer accountdetailType, Integer accountdetailkey) {

		if (null == asondate) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"asondate", "asondate supplied is null")));
		}

		if (null == glcode || StringUtils.isEmpty(glcode)) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"glcode", "glcode supplied is either null or empty")));
		} else if (null == commonsService.getCChartOfAccountsByGlCode(glcode)) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"glcode", "not a valid glcode :" + glcode)));
		}

		if (null == fundcode || StringUtils.isEmpty(fundcode)) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"fundcode", "Fundcode supplied is either null or empty")));
		} else {
			if (null == commonsService.fundByCode(fundcode)) {
				throw new ValidationException(
						Arrays.asList(new ValidationError("fundcode",
								"The Fundcode supplied : " + fundcode
										+ " is not present in the system.")));
			}
		}

		if (null != accountdetailType) {
			Session session = HibernateUtil.getCurrentSession();
			Query qry = session
					.createQuery("from CChartOfAccountDetail cd,CChartOfAccounts c where "
							+ "cd.glCodeId = c.id and c.glcode=:glcode and cd.detailTypeId=:detailTypeId");
			qry.setString(VoucherConstant.GLCODE, glcode);
			qry.setString("detailTypeId", accountdetailType.toString());

			if (null == qry.list() || qry.list().size() == 0) {
				throw new ValidationException(
						Arrays.asList(new ValidationError(
								"accountdetailType",
								"Glcode "
										+ glcode
										+ " is not a control code for the supplied detailed type.")));
			}

		}
		if (null != accountdetailkey) {
			Session session = HibernateUtil.getCurrentSession();
			Query qry = session
					.createQuery("from Accountdetailkey adk where adk.accountdetailtype=:detailtypeid and adk.detailkey=:detailkey");
			qry.setString(VoucherConstant.DETAILTYPEID,
					accountdetailType.toString());
			qry.setString("detailkey", accountdetailkey.toString());

			if (null == qry.list() || qry.list().size() == 0) {
				throw new ValidationException(
						Arrays.asList(new ValidationError("accountdetailkey",
								"The accountdetailkey supplied : "
										+ accountdetailkey
										+ " for the accountdetailType : "
										+ accountdetailType + " is not correct")));
			}
		}
	}

	protected BigDecimal getOpeningBalAsonDate(Date asondate, String glcode,
			String fundCode, Integer accountdetailType, Integer accountdetailkey)
			throws ValidationException {
		BigDecimal opBalAsonDate = BigDecimal.ZERO;
		StringBuffer opBalncQuery = new StringBuffer(300);

		opBalncQuery
				.append("SELECT decode(sum(openingdebitbalance),null,0,sum(openingdebitbalance))-")
				.append(" decode(sum(openingcreditbalance),null,0,sum(openingcreditbalance)) as openingBalance from TransactionSummary")
				.append(" where financialyear.id = ( select id from CFinancialYear where startingDate <= '")
				.append(Constants.DDMMYYYYFORMAT1.format(asondate))
				.append("' AND endingDate >='")
				.append(Constants.DDMMYYYYFORMAT1.format(asondate))
				.append("') and glcodeid.glcode=? and fund.code=?");
		if (null != accountdetailType) {
			opBalncQuery.append(" and accountdetailtype.id=").append(
					accountdetailType);
		}
		if (null != accountdetailkey) {
			opBalncQuery.append(" and accountdetailkey.detailkey=").append(
					accountdetailkey);
		}
		List<Object> tsummarylist = (List<Object>) getPersistenceService()
				.findAllBy(opBalncQuery.toString(), glcode, fundCode);
		opBalAsonDate = BigDecimal.valueOf((Double) tsummarylist.get(0));

		LOGGER.debug("Opening balance :" + opBalAsonDate);

		return opBalAsonDate;
	}

	/**
	 * This API will return the sum total of credit opening balances for a given
	 * account code and sub ledger details.
	 * 
	 * @param asondate
	 * @param glcode
	 * @param fundCode
	 * @param accountdetailType
	 * @param accountdetailkey
	 * @return opening balance if exits, else returns zero.
	 * @throws ValidationException
	 */
	private BigDecimal getCreditOpeningBalAsonDate(Date asondate,
			String glcode, String fundCode, Integer accountdetailType,
			Integer accountdetailkey) throws ValidationException {
		BigDecimal opBalAsonDate = BigDecimal.ZERO;
		StringBuffer opBalncQuery = new StringBuffer(300);
		// Opening balance query when sublegder info are there
		if (null != accountdetailkey && null != accountdetailType) {
			opBalncQuery
					.append(" Select sum(txns.openingcreditbalance) as openingBalance ")
					.append("From transactionsummary txns,fund fd, chartofaccounts coa,accountdetailtype adt,accountdetailkey adk")
					.append(" where coa.id=txns.glcodeid and fd.id=txns.fundid  and adt.id=txns.accountdetailtypeid and adk.detailkey=txns.accountdetailkey ")
					.append(" and coa.glcode='")
					.append(glcode)
					.append("' and fd.code='")
					.append(fundCode)
					.append("'and txns.financialyearid in(select id from financialyear where startingdate<='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' and endingdate>='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("')")
					.append(" and txns.accountdetailtypeid=")
					.append(accountdetailType)
					.append(" and txns.accountdetailkey=")
					.append(accountdetailkey)
					.append(" and adk.detailtypeid=")
					.append(accountdetailType)
					.append(" Group by txns.GLCODEID,txns.fundid,txns.FINANCIALYEARID,txns.accountdetailtypeid,txns.accountdetailkey ");
		} else {
			// Opening balance query when subledger data is not there
			opBalncQuery
					.append(" Select sum(txns.openingcreditbalance) as openingBalance From transactionsummary txns,fund fd, chartofaccounts coa")
					.append(" where coa.id=txns.glcodeid and fd.id=txns.fundid ")
					.append(" and coa.glcode='")
					.append(glcode)
					.append("' and fd.code='")
					.append(fundCode)
					.append("'and txns.financialyearid in(select id from financialyear where startingdate<='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' and endingdate>='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("')")
					.append(" Group by txns.GLCODEID,txns.fundid,txns.FINANCIALYEARID ");
		}
		System.out.println("Opening balance query :" + opBalncQuery);

		List<Object> list = (List<Object>) getPersistenceService().getSession()
				.createSQLQuery(opBalncQuery.toString()).list();
		if (list != null && list.size() > 0)
			opBalAsonDate = (BigDecimal) list.get(0);
		opBalAsonDate = opBalAsonDate == null ? BigDecimal.ZERO : opBalAsonDate;
		System.out.println("Opening balance  :" + opBalAsonDate);
		return opBalAsonDate;
	}

	protected BigDecimal getGlcodeBalAsonDate(Date asondate, String glcode,
			String fundcode, Integer accountdetailType, Integer accountdetailkey)
			throws ValidationException {
		StringBuffer glCodeBalQry = new StringBuffer(400);
		StringBuffer glCodeDbtBalQry = new StringBuffer(400);
		StringBuffer glCodeCrdBalQry = new StringBuffer(400);
		BigDecimal glCodeBalance = BigDecimal.ZERO;
		BigDecimal glCodeDbtBalance = BigDecimal.ZERO;
		BigDecimal glCodeCrdBalance = BigDecimal.ZERO;

		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
						"finance", "statusexcludeReport");
		final String statusExclude = appList.get(0).getValue();
		if (null == accountdetailType && null == accountdetailkey) {
			glCodeBalQry
					.append("SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount)))")
					.append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE ")
					.append(" gl.voucherHeaderId.id=vh.id and gl.glcodeId.glcode=? and vh.fundId.code=? ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("'and vh.status not in (").append(statusExclude)
					.append(")");

			List<Object> list = (List<Object>) getPersistenceService()
					.findAllBy(glCodeBalQry.toString(), glcode, fundcode);
			glCodeBalance = BigDecimal.valueOf((Double) list.get(0));
		} else {
			// Getting the debit balance.
			glCodeDbtBalQry
					.append("SELECT sum(gld.amount)  as debitamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
					.append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId and gl.glcodeId.glcode=? and vh.fundId.code=? ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("'and vh.status not in (").append(statusExclude)
					.append(")").append(" and gld.detailTypeId =")
					.append(accountdetailType);
			if (null != accountdetailkey) {
				glCodeDbtBalQry.append(" and gld.detailKeyId =").append(
						accountdetailkey);
			}
			glCodeDbtBalQry.append(" and gl.debitAmount >0");
			List<Object> listDbt = (List<Object>) getPersistenceService()
					.findAllBy(glCodeDbtBalQry.toString(), glcode, fundcode);
			glCodeDbtBalance = (BigDecimal) listDbt.get(0) == null ? BigDecimal.ZERO
					: (BigDecimal) listDbt.get(0);
			LOGGER.debug(" total debit amount :  " + glCodeDbtBalance);

			// get the credit balance

			glCodeCrdBalQry
					.append("SELECT sum(gld.amount) as creditamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
					.append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId and gl.glcodeId.glcode=? and vh.fundId.code=? ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("'and vh.status not in (").append(statusExclude)
					.append(")").append(" and gld.detailTypeId =")
					.append(accountdetailType);
			if (null != accountdetailkey) {
				glCodeCrdBalQry.append(" and gld.detailKeyId =").append(
						accountdetailkey);
			}
			glCodeCrdBalQry.append(" and gl.creditAmount >0");
			List<Object> listCrd = (List<Object>) getPersistenceService()
					.findAllBy(glCodeCrdBalQry.toString(), glcode, fundcode);
			glCodeCrdBalance = (BigDecimal) listCrd.get(0) == null ? BigDecimal.ZERO
					: (BigDecimal) listCrd.get(0);
			LOGGER.debug(" total credit amount :  " + glCodeCrdBalance);
			glCodeBalance = glCodeDbtBalance.subtract(glCodeCrdBalance);
			LOGGER.debug(" total balance amount :  " + glCodeBalance);

		}

		return glCodeBalance;
	}
	/**
	 * @description- Get the total payment amount made and payment dates against the bill.In cases of multiple 
	 * 				   payment(partial payment) for the bill the payment date is comma separated string value.
	 * 				   Also in case of multiple payments on same date we show 1 date only( same date is not repeated for a bill) 
	 * @param List<Long> billId
	 * @return  Map containing billid as key and values as anotherMap which holds payment amount and payment date
	 * 			 Inner Map have key as amount and date to get payment amount and payment date
	 */
	@SuppressWarnings("unchecked")
	public Map<Long,Map<String,String>> getTotalPaymentAmountAndPaymentDates(List<Long> billIdList){
		LOGGER.debug(" EGovernCommon | getDeductionAmt| Fetching sum of payment and list of payment dates ");
		Map<Long,Map<String,String>> billPaymentDetailMap = new HashMap<Long,Map<String,String>>();
		List<BillRegisterReportBean> queryResultList=new ArrayList<BillRegisterReportBean>();
		Map<Long,BigDecimal> sumBillPaymentMap=new HashMap<Long,BigDecimal>();
		Map<Long,String> paymentDateMap=new HashMap<Long,String>();
		Map<String,String> paymentSumAndDateMap=new HashMap<String,String>();
		String paymentDateStr="";
		String commaSepratedBillIds = billIdList.toString().replace("[","").replace("]", "");
		BigDecimal sumBillPaymentAount=BigDecimal.ZERO;
		
		Query getPaymentDatenAmountQry= HibernateUtil.getCurrentSession().createSQLQuery("SELECT bill.id as billId, misbill.paidamount as paidAmount,"+
					"to_char(paymentvh.voucherdate,'DD/MM/YYYY') as billDate FROM eg_billregister bill,  eg_billregistermis mis,  voucherheader vh,"+  
					 " miscbilldetail misbill,  voucherheader paymentvh WHERE bill.id=mis.billid AND mis.voucherheaderid=vh.id "+
					" AND misbill.billvhid =vh.id AND vh.status=0 AND paymentvh.status =0 AND misbill.payvhid  =paymentvh.id  " +
					"  and bill.id in ("+commaSepratedBillIds+")  order by bill.id")
					.addScalar("billId")
					.addScalar("paidAmount")
					.addScalar("billDate")  	// here billdate is actually paymentdate
					.setResultTransformer(Transformers.aliasToBean(BillRegisterReportBean.class));
		
		queryResultList=	(List<BillRegisterReportBean>)getPaymentDatenAmountQry.list();
    	
		LOGGER.debug(" Iterating the query result list to SumUp the amount and list the dates ");
		for(int i=0;i<queryResultList.size();i++){
			if(paymentDateMap.containsKey(queryResultList.get(i).getBillId().longValue())){
				//since billids are ordered no need to get back from Map  
				sumBillPaymentAount=sumBillPaymentAount.add(queryResultList.get(i).getPaidAmount());
				sumBillPaymentMap.put(queryResultList.get(i).getBillId().longValue(),sumBillPaymentAount);
				if(!paymentDateStr.contains(queryResultList.get(i).getBillDate())){
					//since billids are ordered no need to get back from Map
					paymentDateStr=paymentDateStr+","+(queryResultList.get(i).getBillDate());
	    			paymentDateMap.put(queryResultList.get(i).getBillId().longValue(),paymentDateStr);
				}
    		}else{
    			paymentDateStr="";
    			sumBillPaymentAount=BigDecimal.ZERO;
    			sumBillPaymentAount=queryResultList.get(i).getPaidAmount();
    			sumBillPaymentMap.put(queryResultList.get(i).getBillId().longValue(),sumBillPaymentAount);
    			paymentDateStr=queryResultList.get(i).getBillDate();
    			paymentDateMap.put(queryResultList.get(i).getBillId().longValue(),queryResultList.get(i).getBillDate());
    		}
    	}
    	
		for(Long billId:billIdList){
			paymentSumAndDateMap=new HashMap<String,String>();
			if( sumBillPaymentMap.containsKey((Long) billId)){
				paymentSumAndDateMap.put("amount", sumBillPaymentMap.get(billId).toString());
			}
			if(paymentDateMap.containsKey((Long) billId)){
				paymentSumAndDateMap.put("date", paymentDateMap.get(billId));
			}
			if(paymentDateMap.containsKey((Long)billId)){
				billPaymentDetailMap.put(billId, paymentSumAndDateMap);
			}else{
				billPaymentDetailMap.put(billId, null);
			}
			
		}
		return billPaymentDetailMap;
	}
	
	/**
	 * @description- Get the deduction dates for the billid
	 *  Also in case of multiple payments on same date we show 1 date only( same date is not repeated for a bill) 	
	 * @param List<Long> billId
	 * @param Long deductionGlcodeId
	 * @return Map  It returns comma seperated remittance date incase of multiple remittance else single date 
	 * 				 for the deductionGlcodeId against the billid.If remittance is not generated for the billId 
	 * 				 then the billId contains NULL value.
	 * 
	 * @throws ValidationException     
	 * 				Throws ValidationException if the deductionGlcodeId doesnot exist or if its not a detailed code.
	 */

	@SuppressWarnings("unchecked")
	public Map<Long,String> getDeductionDateList(List<Long> billIdList,Long deductionGlcodeId) throws ValidationException{
		LOGGER.debug(" EGovernCommon|getDeductionDateList| Fetching deduction dates ");
		Map<Long,String> deductionDateListMap = new HashMap<Long,String>();
		List<BillRegisterReportBean> dateList=new ArrayList<BillRegisterReportBean>();
		Map<Long,String> paymentDateMap=new HashMap<Long,String>();
		String commaSepratedBillIds = billIdList.toString().replace("[","").replace("]", "");
		Session session = HibernateUtil.getCurrentSession();
		String remitDate="";
		
		 Query qry = session.createQuery("from CChartOfAccounts c where c.id=:glcodeId and c.classification=4 ");
		 qry.setLong("glcodeId",deductionGlcodeId);
		
		 if(null ==qry.list() || qry.list().size() ==0){
			 throw new ValidationException(Arrays.asList(new ValidationError(deductionGlcodeId.toString(),"Given deduction code Id is not detailed code or it doesnot exists in the system")));
		 }

		Query remittancePaymentDateQuery= HibernateUtil.getCurrentSession().createSQLQuery("select bill.id as billId,to_char(payvh.voucherdate,'DD/MM/YYYY') as billDate from  eg_billregister bill, eg_billregistermis mis,voucherheader vh,"+
							" generalledger gl,generalledgerdetail gld, eg_remittance_gldtl rmgld, eg_remittance_detail rmdtl, eg_remittance rm, voucherheader payvh"+
							" where bill.id=mis.billid  and mis.voucherheaderid=vh.id and gl.voucherheaderid= vh.id and vh.status=0 and gl.glcodeid="+deductionGlcodeId+
							" and gld.generalledgerid= gl.id and gld.id=rmgld.gldtlid and rmdtl.remittancegldtlid=rmgld.id and rm.id= rmdtl.remittanceid  and rm.paymentvhid= payvh.id"+
							" and payvh.status=0  and bill.id in ("+commaSepratedBillIds+") order by bill.id")
							.addScalar("billId")   
							.addScalar("billDate")  	// here billdate is actually paymentdate
							.setResultTransformer(Transformers.aliasToBean(BillRegisterReportBean.class));
		
		dateList=remittancePaymentDateQuery.list();
		
		for(int i=0;i<dateList.size();i++){
    		if(paymentDateMap.containsKey(dateList.get(i).getBillId().longValue())){
    			if(!remitDate.contains(dateList.get(i).getBillDate())){
    				//since billids are ordered no need to get back from Map
    				remitDate=remitDate+","+(dateList.get(i).getBillDate());
	    			paymentDateMap.put(dateList.get(i).getBillId().longValue(),remitDate);
				}
    		}else{
    			remitDate="";
    			remitDate=dateList.get(i).getBillDate();
    			paymentDateMap.put(dateList.get(i).getBillId().longValue(),remitDate);
    		}
    	}
		for(Long bill:billIdList){
				if(paymentDateMap.containsKey((Long)bill)){
					deductionDateListMap.put(bill,paymentDateMap.get(bill));
				}else{
					deductionDateListMap.put(bill,null);
				}
		}
		
		return deductionDateListMap;
	}
	
	/**
	 * @description- Get Total  payment amount for the given list of bill 	
	 * @param List<Long> billId
	 * @param String detailTypeName
	 * @param Long detailKeyId
	 * @return BigDecimal  It returns sum of payment made for list of billid associated 
	 * 						with the given detailtype and detail key Id. If payment doesnt exits for the given 
	 * 						details	it returns ZERO 
	 * @throws ValidationException     
	 * 				Throws ValidationException if given accountdetailName and accountdetailkeyId doesnt exists 
	 */

	@SuppressWarnings("unchecked")
	public BigDecimal getTotalPaymentAmountForBills(List<Long> billIdList,String detailName,Long detailKeyId) throws ValidationException{
		LOGGER.debug(" EGovernCommon | getTotalPaymentAmountForBills | Fetching sum of payment for given bill list");
		String query = "from AppConfigValues where key.module=:module and key.keyName=:keyName";
		String commaSepratedBillIds = billIdList.toString().replace("[","").replace("]", "");
		List<Object> detailQryResult;
		Session session=HibernateUtil.getCurrentSession();
		
		
		Query qry = session.createQuery("from Generalledgerdetail gld where " +
		 		"gld.accountdetailtype.name=:detailName  and gld.detailkeyid=:detailkeyId ");
		qry.setString("detailName",detailName);
		qry.setLong("detailkeyId",detailKeyId);
		detailQryResult=qry.list();
		
		 if(null ==detailQryResult || detailQryResult.size() ==0){
			 throw new ValidationException(Arrays.asList(new ValidationError(detailName+"-"+detailKeyId.toString()
					 ,"Given detail type or detail keyId doesnot exists in the system")));
		 }
		 
		 List<String> wBillNetPayCodeList = new ArrayList<String>();
			Query worksquery = session.createQuery(query);
			worksquery.setString("module", "EGF");
			worksquery.setString("keyName", Constants.WORKS_BILL_PURPOSE_IDS);
			List<AppConfigValues> wBillNetPurpose =(List<AppConfigValues>)worksquery.list();
			for (AppConfigValues appConfigValues : wBillNetPurpose) {
					List<CChartOfAccounts> coaList = (List<CChartOfAccounts>) session.createQuery("from CChartOfAccounts where purposeId="+appConfigValues.getValue()).list();
					for (CChartOfAccounts chartOfAccounts : coaList) {
						wBillNetPayCodeList.add(chartOfAccounts.getId().toString());
					}
			}
		 
			String worksCOAList = wBillNetPayCodeList.toString().replace("[","").replace("]", "");	
			
		Query getPaymentDatenAmountQry= HibernateUtil.getCurrentSession().createSQLQuery("SELECT SUM(gld.amount) FROM eg_billregister bill,"+
		" eg_billregistermis mis,  voucherheader vh,  generalledger gl,  generalledgerdetail gld, accountdetailtype accdtl,  miscbilldetail misbill,"+
		" chartofaccounts coa, voucherheader paymentvh WHERE bill.id =mis.billid AND  mis.voucherheaderid=vh.id AND misbill.billvhid =vh.id AND " +
		" vh.status  =0 AND paymentvh.id  = gl.voucherheaderid AND gl.id = gld.generalledgerid AND gld.detailtypeid  =accdtl.id AND " +
		" gld.detailkeyid ="+detailKeyId+" AND accdtl.name  ='"+detailName+"' AND paymentvh.status  =0 AND misbill.payvhid =paymentvh.id" +
		" and coa.id  in("+worksCOAList+") AND bill.id  IN ("+commaSepratedBillIds+") GROUP BY gld.detailtypeid, gld.detailkeyid");
	
		detailQryResult=getPaymentDatenAmountQry.list();
    	if(detailQryResult!=null && detailQryResult.size()>0)
    		return (BigDecimal)detailQryResult.get(0);
    	else
    		return BigDecimal.ZERO;
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBankBranchForActiveBanks() { // This??
		List<Object[]> unorderedBankBranch = (List<Object[]>) persistenceService
				.findAllBy(
						"select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname "
								+ " FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount "
								+ " where  bank.isactive='1'  and bankBranch.isactive='1' and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id"
								+ " and bankaccount.isactive=? ", true);
		// Ordering Starts
		List<String> bankBranchStrings = new ArrayList<String>();
		int i, j, len = unorderedBankBranch.size();
		for (i = 0; i < len; i++)
			bankBranchStrings.add(unorderedBankBranch.get(i)[1].toString());
		Collections.sort(bankBranchStrings);
		List<Object[]> bankBranch = new ArrayList();
		for (i = 0; i < len; i++) {
			for (j = 0; j < len; j++) {
				if (bankBranchStrings.get(i).equalsIgnoreCase(
						unorderedBankBranch.get(j)[1].toString()))
					bankBranch.add(unorderedBankBranch.get(j));
			}
		}

		LOGGER.debug("Bank list size is " + bankBranch.size());
		List<Map<String, Object>> bankBranchList = new ArrayList<Map<String, Object>>();
		Map<String, Object> bankBrmap;
		for (Object[] element : bankBranch) {
			bankBrmap = new HashMap<String, Object>();
			bankBrmap.put("bankBranchId", element[0].toString());
			bankBrmap.put("bankBranchName", element[1].toString());
			bankBranchList.add(bankBrmap);
		}
		return bankBranchList;
	}

	@SuppressWarnings("unchecked")
	public List<Bankbranch> getActiveBankBranchForActiveBanks() {
		return persistenceService
				.findAllBy("from Bankbranch bankBranch where  bank.isactive='1'  and isactive='1'");
	}

	@SuppressWarnings("unchecked")
	public List<CChartOfAccounts> getSubledgerAccountCodesForAccountDetailTypeAndNonSubledgers(
			Integer accountDetailTypeId) {
		
		//Query qry=persistenceService.getSession().createQuery("from CChartOfAccounts  coa LEFT JOIN ");
						
				
		
		if (accountDetailTypeId == 0 || accountDetailTypeId == -1){
			Criteria critQuery = HibernateUtil.getCurrentSession().createCriteria(CChartOfAccounts.class)
					.createAlias("chartOfAccountDetails","details",CriteriaSpecification.LEFT_JOIN)
					.setFetchMode( "chartOfAccountDetails", FetchMode.EAGER)
					.add(Restrictions.sizeEq("chartOfAccountDetails", 0))
					.add( Restrictions.eq("isActiveForPosting", Long.valueOf(1)))
					.add( Restrictions.eq("classification",Long.valueOf(4)))
					   .addOrder( Order.asc("id") );
			return critQuery.list();
		}
		else{
			Criteria crit = HibernateUtil.getCurrentSession().createCriteria(CChartOfAccounts.class)
					.createAlias("chartOfAccountDetails","details",CriteriaSpecification.LEFT_JOIN)
					.setFetchMode( "chartOfAccountDetails", FetchMode.EAGER)
					.add( Restrictions.eq("isActiveForPosting", Long.valueOf(1)))
					.add( Restrictions.eq("classification",Long.valueOf(4)))
					.add( Restrictions.disjunction()
					.add(Restrictions.sizeEq("chartOfAccountDetails", 0))
					.add(Restrictions.eq("details.detailTypeId.id", accountDetailTypeId) ))
					.addOrder( Order.asc("id") )
					;
			LOGGER.info("-----------------------------Query------------"+crit.list());
			LOGGER.info("-----------------------------Query------------");
			   
			return crit.list();  
			/*return persistenceService
					.findAllBy(
							"from CChartOfAccounts  a LEFT JOIN  fetch a.chartOfAccountDetails  b where (size(b) = 0 or b.detailTypeId.id=?)and a.isActiveForPosting=1 and a.classification=4 order by a.id",
							accountDetailTypeId);*/
		}
	}

	public List<CChartOfAccounts> getAllAccountCodesForAccountDetailType(
			Integer accountDetailTypeId) {
		LOGGER.debug("Initiating getAllAccountCodesForAccountDetailType for detailtypeId "
				+ accountDetailTypeId + "...");
		List<CChartOfAccounts> subledgerCodes = getSubledgerAccountCodesForAccountDetailTypeAndNonSubledgers(accountDetailTypeId);
		List<CChartOfAccounts> accountCodesForDetailType = new ArrayList<CChartOfAccounts>();
		accountCodesForDetailType.addAll(subledgerCodes);
		LOGGER.debug("finished getAllAccountCodesForAccountDetailType for detailtypeId "
				+ accountDetailTypeId
				+ ".size:"
				+ accountCodesForDetailType.size() + ".");
		return accountCodesForDetailType;
	}

	public BigDecimal getOpeningBalAsonDate(Date asondate, String glcode,
			String fundCode) throws ValidationException {
		BigDecimal opBalAsonDate = BigDecimal.ZERO;
		StringBuffer opBalncQuery = new StringBuffer(300);
		opBalncQuery
				.append("SELECT decode(sum(openingdebitbalance),null,0,sum(openingdebitbalance))-")
				.append(" decode(sum(openingcreditbalance),null,0,sum(openingcreditbalance)) as openingBalance from TransactionSummary")
				.append(" where financialyear.id = ( select id from CFinancialYear where startingDate <= '")
				.append(Constants.DDMMYYYYFORMAT1.format(asondate))
				.append("' AND endingDate >='")
				.append(Constants.DDMMYYYYFORMAT1.format(asondate))
				.append("') and glcodeid.glcode=? and fund.code=?");
		List<Object> tsummarylist = (List<Object>) getPersistenceService()
				.findAllBy(opBalncQuery.toString(), glcode, fundCode);
		opBalAsonDate = BigDecimal.valueOf((Double) tsummarylist.get(0));
		return opBalAsonDate;
	}

	/**
	 * @description - get the list of BudgetUsage based on various parameters
	 * @param queryParamMap
	 *            - HashMap<String, Object> queryParamMap will have data
	 *            required for the query Query Parameter Map keys are -
	 *            fundId,ExecutionDepartmentId
	 *            ,functionId,moduleId,financialYearId
	 *            ,budgetgroupId,fromDate,toDate and Order By
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public List<BudgetUsage> getListBudgetUsage(
			Map<String, Object> queryParamMap) {

		StringBuffer query = new StringBuffer();
		List<BudgetUsage> listBudgetUsage = null;
		query.append("select bu from BudgetUsage bu,BudgetDetail bd where  bu.budgetDetail.id=bd.id");
		Map<String, String> mandatoryFields = new HashMap<String, String>();
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService
				.findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) {
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				String value = appConfigVal.getValue();
				String header = value.substring(0, value.indexOf("|"));
				String mandate = value.substring(value.indexOf("|") + 1);
				if (mandate.equalsIgnoreCase("M")) {
					mandatoryFields.put(header, "M");
				}
			}
		}
		if (isNotNull(mandatoryFields.get("fund"))
				&& !isNotNull(queryParamMap.get("fundId"))) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"fund", "fund cannot be null")));
		} else if (isNotNull(queryParamMap.get("fundId"))) {
			query.append(" and bd.fund.id=").append(
					Integer.valueOf(queryParamMap.get("fundId").toString()));
		}
		if (isNotNull(mandatoryFields.get("department"))
				&& !isNotNull(queryParamMap.get("ExecutionDepartmentId"))) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"department", "department cannot be null")));
		} else if (isNotNull(queryParamMap.get("ExecutionDepartmentId"))) {
			query.append(" and bd.executingDepartment.id=").append(
					Integer.valueOf(queryParamMap.get("ExecutionDepartmentId")
							.toString()));
		}
		if (isNotNull(mandatoryFields.get("function"))
				&& !isNotNull(queryParamMap.get("functionId"))) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"function", "function cannot be null")));
		} else if (isNotNull(queryParamMap.get("functionId"))) {
			query.append(" and bd.function.id=").append(
					Long.valueOf(queryParamMap.get("functionId").toString()));
		}

		if (isNotNull(queryParamMap.get("moduleId"))) {
			query.append(" and bu.moduleId=").append(
					Integer.valueOf(queryParamMap.get("moduleId").toString()));
		}
		if (isNotNull(queryParamMap.get("financialYearId"))) {

			query.append(" and bu.financialYearId=").append(
					Integer.valueOf(queryParamMap.get("financialYearId")
							.toString()));
		}
		if (isNotNull(queryParamMap.get("budgetgroupId"))) {

			query.append(" and bd.budgetGroup.id=")
					.append(Long.valueOf(queryParamMap.get("budgetgroupId")
							.toString()));
		}
		if (isNotNull(queryParamMap.get("fromDate"))) {
			query.append(" and bu.updatedTime >=:from");
		}
		if (isNotNull(queryParamMap.get("toDate"))) {

			query.append(" and bu.updatedTime <=:to");
		}
		if (isNotNull(queryParamMap.get("Order By"))) {

			query.append(" Order By ").append(queryParamMap.get("Order By"));
		} else {
			query.append(" Order By bu.updatedTime");
		}

		LOGGER.debug("Budget Usage Query >>>>>>>> " + query.toString());
		Query query1 = HibernateUtil.getCurrentSession().createQuery(
				query.toString());
		if (isNotNull(queryParamMap.get("fromDate"))) {
			query1.setTimestamp("from", (Date) queryParamMap.get("fromDate"));
		}
		if (isNotNull(queryParamMap.get("toDate"))) {
			Date date = (Date) queryParamMap.get("toDate");
			date.setMinutes(59);
			date.setHours(23);
			date.setSeconds(59);
			query1.setTimestamp("to", date);
		}

		listBudgetUsage = (List<BudgetUsage>) query1.list();
		return listBudgetUsage;

	}

	private boolean isNotNull(Object ob) {
		if (ob != null) {
			return true;
		} else {
			return false;
		}
	}

	public List<EntityType> loadEntitesFor(Accountdetailtype detailType)
			throws ClassNotFoundException {
		String table = detailType.getFullQualifiedName();
		Class<?> service = Class.forName(table);
		String simpleName = service.getSimpleName();
		simpleName = simpleName.substring(0, 1).toLowerCase()
				+ simpleName.substring(1) + "Service";
		WebApplicationContext wac = WebApplicationContextUtils
				.getWebApplicationContext(ServletActionContext
						.getServletContext());
		EntityTypeService entityService = (EntityTypeService) wac
				.getBean(simpleName);
		return (List<EntityType>) entityService.getAllActiveEntities(detailType
				.getId());
	}

	/**
	 * @author manoranjan
	 * @description - API to get the net balance for a glcode from bills only
	 * @param asondate
	 *            - Mandatory
	 * @param glcode
	 *            - Mandatory (validate the master data)-to get the balance for
	 *            this supplied account code.
	 * @param fundcode
	 *            -Mandatory (Fund code from fund master)
	 * @param accountdetailType
	 *            - optional (if supplied validate the master data)
	 * @param accountdetailkey
	 *            - optional (if supplied validate the master data)
	 * @return billAccbalance - returns the account code balance for a glcode
	 *         and subledger type.
	 * @throws ValidationException
	 */
	public BigDecimal getBillAccountBalanceforDate(Date asondate,
			String glcode, String fundcode, Integer accountdetailType,
			Integer accountdetailkey) throws ValidationException {

		LOGGER.debug("EgovCommon | getBillAccountBalanceforDate | Start");
		LOGGER.debug("Data Received asondate = " + asondate + " glcode = "
				+ glcode + " fundcode = " + fundcode + " accountdetailType = "
				+ accountdetailType + " accountdetailkey = " + accountdetailkey);
		validateParameterData(asondate, glcode, fundcode, accountdetailType,
				accountdetailkey);
		LOGGER.debug("validation of data is sucessfull");
		BigDecimal billBalAsonDate = getBillAccBalAsonDate(asondate, glcode,
				fundcode, accountdetailType, accountdetailkey);
		LOGGER.debug("EgovCommon | getBillAccountBalanceforDate | End");
		return billBalAsonDate;
	}

	private BigDecimal getBillAccBalAsonDate(Date asondate, String glcode,
			String fundcode, Integer accountdetailType, Integer accountdetailkey)
			throws ValidationException {

		StringBuffer query = new StringBuffer(400);
		BigDecimal billAccCodeBalance = BigDecimal.ZERO;
		if (null == accountdetailType && null == accountdetailkey) {

			query.append(
					"SELECT (decode(sum(egd.debitamount),null,0,sum(egd.debitamount)) - decode(sum(egd.creditamount),null,0,sum(egd.creditamount)))")
					.append("as amount FROM EgBillregister egb, EgBilldetails egd,EgBillregistermis egmis ");
			query.append(
					" Where egb.id = egmis.egBillregister.id and egd.egBillregister.id = egb.id and egmis.voucherHeader is null ")
					.append(" and egd.glcodeid=(select id from CChartOfAccounts where glcode=?) and egmis.fund.code=?")
					.append(" and egb.billdate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' and egb.status IN (select id from ")
					.append(" EgwStatus where UPPER(code)!='CANCELLED')");

		} else {
			query.append(
					"SELECT (decode(sum(egp.debitAmount),null,0,sum(egp.debitAmount)) - decode(sum(egp.creditAmount),null,0,sum(egp.creditAmount)))")
					.append("as amount FROM EgBillregister egb, EgBilldetails egd,EgBillregistermis egmis,EgBillPayeedetails egp");
			query.append(
					" Where egb.id = egmis.egBillregister.id and egd.egBillregister.id = egb.id and egmis.voucherHeader is null ")
					.append(" and egp.egBilldetailsId.id=egd.id and egd.glcodeid=(select id from CChartOfAccounts where glcode=?) and egmis.fund.code=?")
					.append(" and egb.billdate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' and egb.status IN (select id from ")
					.append(" EgwStatus where UPPER(code)!='CANCELLED')")
					.append(" and egp.accountDetailTypeId=")
					.append(accountdetailType);
			if (null != accountdetailkey) {
				query.append(" and egp.accountDetailKeyId=").append(
						accountdetailkey);
			}

		}
		LOGGER.debug("getBillAccBalAsonDate query = " + query.toString());
		List<Object> listAmt = (List<Object>) getPersistenceService()
				.findAllBy(query.toString(), glcode, fundcode);
		Double amount = listAmt.get(0) == null ? 0 : (Double) listAmt.get(0);
		billAccCodeBalance = BigDecimal.valueOf(listAmt.get(0) == null ? 0
				: (Double) listAmt.get(0));
		LOGGER.debug("getBillAccBalAsonDate | Bill Account Balance = "
				+ billAccCodeBalance);
		return billAccCodeBalance;

	}

	/**
	 * @author manoranjan
	 * @description - API to get the credit balance for a glcode and subledger
	 * @param asondate
	 *            - Mandatory
	 * @param glcode
	 *            - Mandatory (validate the master data)-to get the balance for
	 *            this supplied account code.
	 * @param fundcode
	 *            -Mandatory (Fund code from fund master)
	 * @param accountdetailType
	 *            - optional (if supplied validate the master data)
	 * @param accountdetailkey
	 *            - optional (if supplied validate the master data)
	 * @return creditBalance - returns the credit balance for a glcode and
	 *         subledger type including the opening balance for the year.
	 * @throws ValidationException
	 */
	public BigDecimal getCreditBalanceforDate(Date asondate, String glcode,
			String fundcode, Integer accountdetailType, Integer accountdetailkey)
			throws ValidationException {

		LOGGER.debug("EgovCommon | getCreditBalanceforDate | Start");
		LOGGER.debug("Data Received asondate = " + asondate + " glcode = "
				+ glcode + " fundcode = " + fundcode + " accountdetailType = "
				+ accountdetailType + " accountdetailkey = " + accountdetailkey);
		validateParameterData(asondate, glcode, fundcode, accountdetailType,
				accountdetailkey);
		LOGGER.debug("validation of data is sucessfull");
		// Get the credit opening balance for the year
		BigDecimal creditOpeningBalance = getCreditOpeningBalAsonDate(asondate,
				glcode, fundcode, accountdetailType, accountdetailkey);
		BigDecimal creditBalance = null;
		StringBuffer query = new StringBuffer(400);
		if (null == accountdetailType && null == accountdetailkey) {
			query.append("SELECT  sum(gl.creditAmount)")
					.append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE ")
					.append(" gl.voucherHeaderId.id=vh.id and gl.glcodeId.glcode=? and vh.fundId.code=? ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("'and vh.status=0");

			List<Object> list = (List<Object>) getPersistenceService()
					.findAllBy(query.toString(), glcode, fundcode);
			Double amount = (Double) list.get(0) == null ? 0 : (Double) list
					.get(0);
			creditBalance = BigDecimal.valueOf(amount);

		} else {
			query.append(
					"SELECT sum(gld.amount) as creditamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
					.append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId and gl.glcodeId.glcode=? and vh.fundId.code=? ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("'and vh.status = 0")
					.append(" and gld.detailTypeId =")
					.append(accountdetailType);
			if (null != accountdetailkey) {
				query.append(" and gld.detailKeyId =").append(accountdetailkey);
			}
			query.append(" and gl.creditAmount >0");
			List<Object> listCrd = (List<Object>) getPersistenceService()
					.findAllBy(query.toString(), glcode, fundcode);
			creditBalance = (BigDecimal) listCrd.get(0) == null ? BigDecimal.ZERO
					: (BigDecimal) listCrd.get(0);
		}
		creditOpeningBalance = (BigDecimal) creditOpeningBalance == null ? BigDecimal.ZERO
				: creditOpeningBalance;
		creditBalance = creditBalance.add(creditOpeningBalance);
		LOGGER.debug("EgovCommon | getCreditBalanceforDate | End");
		return creditBalance;
	}

	public BigDecimal getAccCodeBalanceForIndirectExpense(Date asondate,
			String glcode, Integer accountdetailType, String accountdetailkey)
			throws ValidationException, Exception {
		LOGGER.debug("EgovCommon | getAccCodeBalanceForIndirectExpense | Start");
		validateParameterData(asondate, glcode, accountdetailType,
				accountdetailkey);
		StringBuffer glCodeBalQry = new StringBuffer(400);
		StringBuffer glCodeDbtBalQry = new StringBuffer(400);
		StringBuffer glCodeCrdBalQry = new StringBuffer(400);
		BigDecimal glCodeBalance = BigDecimal.ZERO;
		BigDecimal subledgerDbtBalance = BigDecimal.ZERO;
		BigDecimal subledgerCrdBalance = BigDecimal.ZERO;

		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
						"finance", "statusexcludeReport");
		final String statusExclude = appList.get(0).getValue();
		if (null == accountdetailType && null == accountdetailkey) {
			glCodeBalQry
					.append("SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount)))")
					.append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE  gl.voucherHeaderId.id=vh.id and gl.glcodeId.glcode=?")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("'and vh.status not in (")
					.append(statusExclude)
					.append(") and ((vh.name='Contractor Journal' and state_id is null) or(vh.name !='Contractor Journal' and vh.name !='CapitalisedAsset' ) )");

			List<Object> list = (List<Object>) getPersistenceService()
					.findAllBy(glCodeBalQry.toString(), glcode);
			glCodeBalance = BigDecimal.valueOf((Double) list.get(0));
		} else {
			// Getting the debit balance.
			glCodeDbtBalQry
					.append("SELECT sum(gld.amount)  as debitamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
					.append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId and gl.glcodeId.glcode=?  ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("'and vh.status not in (")
					.append(statusExclude)
					.append(")and ((vh.name='Contractor Journal' and state_id is null) or(vh.name !='Contractor Journal' and vh.name !='CapitalisedAsset') ) ")
					.append(" and gld.detailTypeId =")
					.append(accountdetailType);
			if (null != accountdetailkey) {
				glCodeDbtBalQry.append(" and gld.detailKeyId in (")
						.append(accountdetailkey).append(")");
			}
			glCodeDbtBalQry.append(" and gl.debitAmount >0");
			List<Object> listDbt = (List<Object>) getPersistenceService()
					.findAllBy(glCodeDbtBalQry.toString(), glcode);
			subledgerDbtBalance = (BigDecimal) listDbt.get(0) == null ? BigDecimal.ZERO
					: (BigDecimal) listDbt.get(0);
			LOGGER.debug(" total debit amount :  " + subledgerDbtBalance);

			// get the credit balance

			glCodeCrdBalQry
					.append("SELECT sum(gld.amount) as creditamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
					.append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId and gl.glcodeId.glcode=?  ")
					.append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("' AND endingDate >='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("') and vh.voucherDate <='")
					.append(Constants.DDMMYYYYFORMAT1.format(asondate))
					.append("'and vh.status not in (")
					.append(statusExclude)
					.append(")and ((vh.name='Contractor Journal' and state_id is null) or(vh.name !='Contractor Journal' and vh.name !='CapitalisedAsset' ) )")
					.append(" and gld.detailTypeId =")
					.append(accountdetailType);
			if (null != accountdetailkey) {
				glCodeCrdBalQry.append(" and gld.detailKeyId in(")
						.append(accountdetailkey).append(")");
			}
			glCodeCrdBalQry.append(" and gl.creditAmount >0");
			List<Object> listCrd = (List<Object>) getPersistenceService()
					.findAllBy(glCodeCrdBalQry.toString(), glcode);
			subledgerCrdBalance = (BigDecimal) listCrd.get(0) == null ? BigDecimal.ZERO
					: (BigDecimal) listCrd.get(0);
			LOGGER.debug(" total credit amount :  " + subledgerCrdBalance);
			glCodeBalance = subledgerDbtBalance.subtract(subledgerCrdBalance);
			LOGGER.debug(" total balance amount :  " + glCodeBalance);

		}
		LOGGER.debug("EgovCommon | getAccCodeBalanceForIndirectExpense | End");
		glCodeBalance = glCodeBalance.setScale(2);
		return glCodeBalance;
	}
	private void  validateParameterData(Date asondate,String glcode,Integer accountdetailType,
			String accountdetailkey)  {

	
		if (null == asondate) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"asondate", "asondate supplied is null")));
		}

		if (null == glcode || StringUtils.isEmpty(glcode)) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"glcode", "glcode supplied is either null or empty")));
		} else if (null == commonsService.getCChartOfAccountsByGlCode(glcode)) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"glcode", "not a valid glcode :" + glcode)));
		}

		if (null != accountdetailType) {
			Session session = HibernateUtil.getCurrentSession();
			Query qry = session
					.createQuery("from CChartOfAccountDetail cd,CChartOfAccounts c where "
							+ "cd.glCodeId = c.id and c.glcode=:glcode and cd.detailTypeId=:detailTypeId");
			qry.setString(VoucherConstant.GLCODE, glcode);
			qry.setString("detailTypeId", accountdetailType.toString());

			if (null == qry.list() || qry.list().size() == 0) {
				throw new ValidationException(
						Arrays.asList(new ValidationError(
								"accountdetailType",
								"Glcode "
										+ glcode
										+ " is not a control code for the supplied detailed type.")));
			}

		}
		if (null != accountdetailkey) {
			Session session = HibernateUtil.getCurrentSession();
			Query qry = session
					.createQuery("from Accountdetailkey adk where adk.accountdetailtype=:detailtypeid and adk.detailkey=:detailkey");
			qry.setString(VoucherConstant.DETAILTYPEID,
					accountdetailType.toString());
			qry.setString("detailkey", accountdetailkey.toString());

			if (null == qry.list() || qry.list().size() == 0) {
				throw new ValidationException(
						Arrays.asList(new ValidationError("accountdetailkey",
								"The accountdetailkey supplied : "
										+ accountdetailkey
										+ " for the accountdetailType : "
										+ accountdetailType + " is not correct")));
			}
		}
	}

	/**
	 * return the AccountCodePurpose object if name matches else returns null
	 * throws EGOVRuntimeException if name is null or empty
	 * 
	 * @param name
	 * @return AccountCodePurpose
	 */
	public AccountCodePurpose getAccountCodePurposeByName(String name) {
		if (name == null || name.isEmpty())
			throw new EGOVRuntimeException("Name is Null Or Empty");

		return (AccountCodePurpose) persistenceService.find(
				"from AccountCodePurpose where upper(name)=upper(?)", name);
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}