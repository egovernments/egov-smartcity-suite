/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
/**
 *
 */
package org.egov.egf.commons;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBillregister;
import org.egov.model.budget.BudgetUsage;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.services.report.FundFlowService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author msahoo
 *
 */
@Transactional(readOnly = true)
public class EgovCommon {

    private static final Logger LOGGER = Logger.getLogger(EgovCommon.class);
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private ChartOfAccountsDAO chartOfAccountsDAO;
    @Autowired
    private FundHibernateDAO fundDAO;

    protected UserService userManager;
    private FundFlowService fundFlowService;
    
    @Autowired
    private  FinancialYearHibernateDAO financialYearDAO;

    @Autowired
    private ApplicationContext context;
    
    public FundFlowService getFundFlowService() {
        return fundFlowService;
    }

    public void setFundFlowService(final FundFlowService fundFlowService) {
        this.fundFlowService = fundFlowService;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(final AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public ChartOfAccountsDAO getChartOfAccountsDAO() {
        return chartOfAccountsDAO;
    }

    public void setChartOfAccountsDAO(final ChartOfAccountsDAO chartOfAccountsDAO) {
        this.chartOfAccountsDAO = chartOfAccountsDAO;
    }

    public FundHibernateDAO getFundDAO() {
        return fundDAO;
    }

    public void setFundDAO(final FundHibernateDAO fundDAO) {
        this.fundDAO = fundDAO;
    }

   

    public EgovCommon() {

    }

    public Boundary getBoundaryForUser(final User user) {
        /*
         * Set<JurisdictionValues> s = userManager.getJurisdictionsForUser(user.getId(), new Date()); if (!s.isEmpty() &&
         * s.iterator().hasNext()) return s.iterator().next().getBoundary();
         */
        return null;
    }

    public Department getDepartmentForUser(final User user, final EisCommonService eisCommonService,
            final EmployeeServiceOld employeeService, final PersistenceService persistenceService) {
        try {
            final Query qry1 =
                    persistenceService.getSession()
                    .createSQLQuery(
                            " select is_primary, dept_id from EG_EIS_EMPLOYEEINFO employeevi0_ where upper(trim(employeevi0_.CODE))='"
                                    + employeeService.getEmpForUserId(user.getId())
                                    .getCode()
                                    + "' and ((employeevi0_.TO_DATE is null) and employeevi0_.FROM_DATE<=CURRENT_DATE or employeevi0_.FROM_DATE<=CURRENT_DATE and employeevi0_.TO_DATE>CURRENT_DATE or employeevi0_.FROM_DATE in (select MAX(employeevi1_.FROM_DATE) from EG_EIS_EMPLOYEEINFO employeevi1_ where employeevi1_.ID=employeevi0_.ID and  not (exists (select employeevi2_.ID from EG_EIS_EMPLOYEEINFO employeevi2_ where employeevi2_.ID=employeevi0_.ID and ((employeevi2_.TO_DATE is null) and employeevi2_.FROM_DATE<=CURRENT_DATE or employeevi2_.FROM_DATE<=CURRENT_DATE and employeevi2_.TO_DATE>CURRENT_DATE))))) ");
            final List<Object[]> employeeViewList = qry1.list();
            if (!employeeViewList.isEmpty())
                if (employeeViewList.size() == 1)
                    return (Department) persistenceService.getSession().load(Department.class,
                            Integer.valueOf(employeeViewList.get(0)[1].toString()));
                else
                    for (final Object[] object : employeeViewList)
                        if (object[0].toString().equals("N"))
                            return (Department) persistenceService.getSession().load(Department.class,
                                    Integer.valueOf(employeeViewList.get(0)[1].toString()));
        } catch (final Exception e) {
            LOGGER.error("Could not get list of assignments", e);
            throw new HibernateException(e);
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
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getCashBalance");
        BigDecimal opeAvailable1 = BigDecimal.ZERO;
        BigDecimal opeAvailable2 = BigDecimal.ZERO;
        try {
            final StringBuffer opBalncQuery1 = new StringBuffer(300);
            opBalncQuery1
            .append(
                    "SELECT case when sum(openingdebitbalance) is null then  0  else sum(openingdebitbalance) end  -")
                    .append(
                            "  case when sum(openingcreditbalance) is null then 0 else sum(openingcreditbalance) end as openingBalance from TransactionSummary")
                            .append(
                                    " where financialyear.id = ( select id from CFinancialYear where startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                    .append("' AND endingDate >='").append(
                                            Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                            .append("') and glcodeid.glcode=? and fund.id=?");
            final List<Object> tsummarylist = getPersistenceService()
                    .findAllBy(opBalncQuery1.toString(), cashInHandCode, fundId);
            opeAvailable1 = BigDecimal.valueOf((Double) tsummarylist.get(0));

            final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(
                    FinancialConstants.MODULE_NAME_APPCONFIG, "cancelledstatus");
            final String statusExclude = appList.get(0).getValue();

            final StringBuffer opBalncQuery2 = new StringBuffer(300);
            opBalncQuery2
            .append(
                    "SELECT (case when sum(gl.debitAmount) is null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount)  is null then 0 else sum(gl.creditAmount) end)")
                    .append(
                            " as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE gl.voucherHeaderId.id=vh.id and gl.glcode='")
                            .append(cashInHandCode)
                            .append(
                                    "' and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                    .append("' AND endingDate >='").append(
                                            Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                            .append("') and vh.voucherDate <='").append(
                                                    Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                                    .append(" 'and vh.status not in (").append(statusExclude)
                                                    .append(") and vh.fundId.id=?");

            final List<Object> list = getPersistenceService()
                    .findAllBy(opBalncQuery2.toString(), fundId);
            opeAvailable2 = BigDecimal.valueOf((Double) list.get(0));
        } catch (final HibernateException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("exception occuered while geeting cash balance", e);
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
            final Long bankId) {
        return getAccountBalance(VoucherDate, bankId, null, null, null);
    }

    /**
     * This method will return the amount that are available to make further payments.
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
                bankaccountId.longValue(), null, null, null);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Bank balance as per Bank book:"
                    + bankBalanceasofBankBookReport);
        amountApprovedForPayment = getAmountApprovedForPaymentAndVoucherNotCreated(
                VoucherDate, bankaccountId);
        LOGGER
        .debug("Amount that are approved but voucher creation in progress:"
                + amountApprovedForPayment);
        TotalbankBalance = bankBalanceasofBankBookReport
                .subtract(amountApprovedForPayment);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Total amount available for payment :" + TotalbankBalance);
        return TotalbankBalance;
    }

    /**
     * This function will return the bank amount that are blocked for payment. There are voucher that are in approval process for
     * which some amount will be approved. This method will return the total amount that are blocked.
     *
     * @param VoucherDate
     * @param bankaccountId
     * @return
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getAmountApprovedForPaymentAndVoucherNotCreated(
            final Date VoucherDate, final Integer bankaccountId) {
        LOGGER
        .debug("EgovCommon | getAmountApprovedForPaymentAndVoucherNotCreated");
        BigDecimal bankBalance = BigDecimal.ZERO;
        try {
            String paymentWFStatus = "";
            List<Object> list = getPersistenceService()
                    .findAllBy(
                            "select chartofaccounts.id from Bankaccount where id=?",
                            bankaccountId);
            final Integer glcodeid = Integer.valueOf(list.get(0).toString());
            final CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find(
                    "from CChartOfAccounts where id=?", Long.valueOf(glcodeid));
            final List<AppConfigValues> paymentStatusList = appConfigValuesService.getConfigValuesByModuleAndKey(
                    FinancialConstants.MODULE_NAME_APPCONFIG, "PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK");
            for (final AppConfigValues values : paymentStatusList)
                paymentWFStatus = paymentWFStatus + "'" + values.getValue()
                + "',";
            if (!paymentWFStatus.equals(""))
                paymentWFStatus = paymentWFStatus.substring(0, paymentWFStatus
                        .length() - 1);

            final List<AppConfigValues> preAppList = appConfigValuesService.getConfigValuesByModuleAndKey(
                    FinancialConstants.MODULE_NAME_APPCONFIG, "PREAPPROVEDVOUCHERSTATUS");
            final String preApprovedStatus = preAppList.get(0).getValue();

            final StringBuffer paymentQuery = new StringBuffer(400);
            paymentQuery
            .append(
                    "SELECT (case when sum(gl.debitAmount) is null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount) is null then 0 else sum(gl.creditAmount) end  )")
                    .append(
                            " as amount FROM  CGeneralLedger gl , CVoucherHeader vh,Paymentheader ph WHERE gl.voucherHeaderId.id=vh.id and ph.voucherheader.id=vh.id and gl.glcodeId=? ")
                            .append(
                                    " and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                    .append("' AND endingDate >='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                    .append("') and vh.voucherDate <='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                    .append("'and vh.status in (")
                                    .append(preApprovedStatus)
                                    .append(")")
                                    .append(
                                            " and ph.state in (from org.egov.infra.workflow.entity.State where type='Paymentheader' and value in (")
                                            .append(paymentWFStatus).append(") )");
            list = getPersistenceService().findAllBy(
                    paymentQuery.toString(), coa);
            bankBalance = BigDecimal.valueOf(Math.abs((Double) list.get(0)));

            LOGGER
            .debug("Total payment amount that are approved by FM Unit but voucher not yet created :"
                    + bankBalance);
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("exception occuered while geeting cash balance"
                        + e.getMessage(), e);
            throw new HibernateException(e);
        }
        return bankBalance;
    }

    /**
     * This method will return the total amount for the payment that are approved and cheques not assigned.
     *
     * @param VoucherDate
     * @param bankaccountId
     * @return
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getAmountForApprovedPaymentAndChequeNotAssigned(
            final Date voucherDate, final Integer bankaccountId) {
        LOGGER
        .debug("EgovCommon | getAmountForApprovedPaymentAndChequeNotAssigned");
        BigDecimal bankBalance = BigDecimal.ZERO;
        try {
            final Bankaccount bankAccount = (Bankaccount) getPersistenceService()
                    .find("from Bankaccount where id=?", bankaccountId);
            StringBuffer paymentQuery = new StringBuffer();
            // query to fetch vouchers for which no cheque has been assigned
            paymentQuery = paymentQuery
                    .append(
                            "SELECT (case when sum(gl.debitAmount) is null then 0 else sum(gl.debitAmount) end   -  case when sum(gl.creditAmount) is "
                                    + " null then 0 else sum(gl.creditAmount) ) as amount FROM  GeneralLedger gl ,voucherheader vh, "
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
                                    .append(
                                            "SELECT (case when sum(gl.debitAmount) is null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount) is "
                                                    + "null then 0 else sum(gl.creditAmount) ) as amount FROM  GeneralLedger gl ,voucherheader vh, "
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
            final List<Object> list = persistenceService.getSession().createSQLQuery(paymentQuery.toString())
                    .setDate("date", voucherDate).list();
            final BigDecimal amount = (BigDecimal) list.get(0);
            bankBalance = amount == null ? BigDecimal.ZERO : amount;
            LOGGER
            .debug("Total payment amount that are approved by FM Unit but cheque not yet assigned:"
                    + bankBalance);
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("exception occuered while getting cash balance"
                        + e.getMessage(), e);
            throw new HibernateException(e);
        }
        return bankBalance.abs();
    }

    /**
     * This method will return the instrument(not cancelled and not dishonored ) details of the vouchers for a given combination
     * of AccountdetailTypeid and AccountdetailKeyid for which the subledger amount is on the CREDIT SIDE
     *
     * @param accountdetailType - detail type ID - cannot be null
     * @param accountdetailKey - detail key ID - cannot be null
     * @param voucherToDate - the upper limit of the voucherdates of the associated vouchers - current date is taken if null is
     * passed
     *
     * @return IMPORTANT - IF THERE ARE NO INSTRUMENTS ASSOCIATED WITH VOUCHERS FOR SUBLEDGER THEN NULL IS RETURNED List<Map> is
     * returned since there can be multiple instruments associated Note - The keys for the map are type, number, date, amount
     * @throws ApplicationRuntimeException accountdetailType or accountdetailkey parameter is null ApplicationRuntimeException if
     * any other exception
     * @author julian.prabhakar
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getInstrumentsDetailsForSubledgerTypeAndKey(final Integer accountdetailType,
            final Integer accountdetailKey, Date voucherToDate)
            {
        final StringBuffer query = new StringBuffer(500);
        if (accountdetailType == null)
            throw new ApplicationRuntimeException("AccountDetailType cannot be null");
        if (accountdetailKey == null)
            throw new ApplicationRuntimeException("AccountDetailKey cannot be null");
        if (voucherToDate == null)
            voucherToDate = new Date();
        List<Map<String, Object>> resultList = null;

        try {

            query.append("select iv.instrumentHeaderId FROM CGeneralLedgerDetail gld, CGeneralLedger gl , CVoucherHeader vh, ")
            .append(" InstrumentVoucher iv WHERE gld.generalLedgerId.id=gl.id AND gl.voucherHeaderId.id=vh.id")
            .append(" AND iv.voucherHeaderId.id=vh.id AND gld.detailTypeId.id =? AND gld.detailKeyId=? AND gl.creditAmount >0")
            .append(" AND vh.status=0 ")
            .append(" AND vh.voucherDate<='")
            .append(Constants.DDMMYYYYFORMAT1.format(voucherToDate))
            .append("' AND upper(iv.instrumentHeaderId.statusId.description) not in ('CANCELLED' , 'DISHONORED' ) ");
            final List<InstrumentHeader> instrumentHeaderList = getPersistenceService()
                    .findAllBy(query.toString(), accountdetailType, accountdetailKey);
            resultList = new ArrayList<Map<String, Object>>();
            Map<String, Object> instrumentMap = null;
            if (instrumentHeaderList != null)
                for (final InstrumentHeader ih : instrumentHeaderList)
                {
                    instrumentMap = new HashMap<String, Object>();
                    instrumentMap.put("type", ih.getInstrumentType().getType());
                    if (ih.getInstrumentNumber() == null)
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
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting Instrument details-" + e.getMessage(), e);
            throw new ApplicationRuntimeException("Exception occured while getting Instrument details-" + e.getMessage());
        }

        return resultList == null || resultList.isEmpty() ? null : resultList;
            }

    @SuppressWarnings("unchecked")
    public BigDecimal getAccountBalance(final Date VoucherDate,
            final Long bankId, final BigDecimal amount, final Long paymentId, final Long accGlcodeId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getCashBalance");
        LOGGER
        .info("--------------------------------------------------------------------------------getAccountBalance-----------------");

        LOGGER
        .info("-------------------------------------------------------------------------------------------------");

        BigDecimal bankBalance = BigDecimal.ZERO;

        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                "Balance Check Based on Fund Flow Report");
        final String balanceChequeBasedOnFundFlowReport = appList.get(0).getValue();

        try {
            if (balanceChequeBasedOnFundFlowReport.equalsIgnoreCase("Y"))
                bankBalance = fundFlowService.getBankBalance(Long.valueOf(bankId), VoucherDate, accGlcodeId);
            else
                bankBalance = getAccountBalanceFromLedger(VoucherDate, bankId.intValue(),
                        amount, paymentId);
            LOGGER
            .info("-------------------------------------------------------------------------------------bankBalance"
                    + bankBalance);
        } catch (final ValidationException e) {
            LOGGER.error("Balance Check Failed" + e.getMessage(), e);
            throw e;
        }
        return bankBalance;
    }

    /**
     * This method will return sum of bill amount for the given combination of AccountdetailTypeid and AccountdetailKeyid for
     * which the AccountdetailTypeid amount is on the DEBIT SIDE .
     *
     * Important Bills created from Financials module will only be considered
     *
     * @param Chartofaccounts - glcode - cannot be null
     * @param Subledger Type- cannot be null
     * @param accountdetailKey - detail key ID - cannot be null
     * @param ToDate - the upper limit of the voucherdates of the associated vouchers - current date is taken if null is passed
     *
     * @return BigDecimal value, if there are no voucher created for the zero is returned
     *
     * @throws ApplicationRuntimeException glcode, subledger or accountdetailkey or ToDate parameter is null
     * ApplicationRuntimeException if chartofaccounts or accountdetailkey doesnot exist in system
     * @author shamili.gupta
     */

    public BigDecimal getSumOfBillAmount(final String glcode, final String subledgerType, final Long accountdetailkeyId,
            final Date toBillDate)
            throws ApplicationRuntimeException, ValidationException {
        final StringBuffer query = new StringBuffer(500);
        final Session session = persistenceService.getSession();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Inside getSumOfBillCreated -Glcode :" + glcode + " subledgerType: " + subledgerType
                    + " accountdetailkeyId: " + accountdetailkeyId + " toBillDate: " + toBillDate);
        if (glcode == null)
            throw new ApplicationRuntimeException("Glcode cannot be null");
        if (subledgerType == null)
            throw new ApplicationRuntimeException("SubledgerType cannot be null");
        if (accountdetailkeyId == null)
            throw new ApplicationRuntimeException("AccountdetailkeyId cannot be null");
        if (toBillDate == null)
            throw new ApplicationRuntimeException("To Date cannot be null");

        final Query qry = session.createQuery("from CChartOfAccounts c where c.glcode=:glcode and c.classification=4 ");
        qry.setString("glcode", glcode);
        final List<Object> coaRes = qry.list();

        if (null == coaRes || coaRes.size() == 0)
            throw new ValidationException(Arrays.asList(new ValidationError(glcode, "Account code " + glcode
                    + " does not exists ")));

        final Query actQry = session
                .createQuery("from Accountdetailkey adk where adk.accountdetailtype.name=:subledgerType and adk.detailkey=:detailkey");
        actQry.setString("subledgerType", subledgerType);
        actQry.setInteger("detailkey", accountdetailkeyId.intValue());

        final List<Object> actRes = actQry.list();
        if (null == actRes || actRes.size() == 0)
            throw new ValidationException(Arrays.asList(new ValidationError("Accountdetailkey", "The accountdetailkey  "
                    + accountdetailkeyId + " for the accountdetailType : " +
                    subledgerType + " does not exist ")));

        query.append(
                "select sum(epayee.debitAmount) FROM EgBillPayeedetails epayee,  EgwStatus estatus, CChartOfAccounts coa,Accountdetailtype act  ")
                .append(" WHERE  act.name=:subledger and act.id=epayee.accountDetailTypeId and epayee.accountDetailKeyId=:accountdetailkey and  ")
                .append(" coa.glcode=:glcode")
                .append(" and  epayee.egBilldetailsId.egBillregister.status=estatus  and epayee.egBilldetailsId.egBillregister.egBillregistermis.voucherHeader.status=0 ")
                .append(" and coa.id=epayee.egBilldetailsId.glcodeid and epayee.egBilldetailsId.egBillregister.billdate<=:billdate  ")
                .append(" and epayee.egBilldetailsId.egBillregister.egBillregistermis.voucherHeader.moduleId is NULL ")
                .append(" and epayee.egBilldetailsId.egBillregister.state is null and estatus.code ='APPROVED' ")
                .append(" and epayee.egBilldetailsId.egBillregister.expendituretype='Works'  group by epayee.accountDetailKeyId");

        final Query amountQry = session.createQuery(query.toString());
        amountQry.setString("subledger", subledgerType);
        amountQry.setInteger("accountdetailkey", accountdetailkeyId.intValue());
        amountQry.setString("glcode", glcode);
        amountQry.setDate("billdate", toBillDate);

        // if(LOGGER.isInfoEnabled()) LOGGER.info("----------------:"+amountQry.list());
        BigDecimal result = BigDecimal.ZERO;
        if (!amountQry.list().isEmpty())
            result = (BigDecimal) amountQry.list().get(0);
        else
            result = BigDecimal.ZERO;
        // amountQry.list().isEmpty()
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Total bill amount generated for the " + subledgerType + "is :" + result);

        return result;

    }

    @SuppressWarnings("unchecked")
    public BigDecimal getAccountBalanceFromLedger(final Date VoucherDate,
            final Integer bankId, final BigDecimal amount, final Long paymentId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getCashBalance");
        BigDecimal opeAvailable = BigDecimal.ZERO;
        BigDecimal bankBalance = BigDecimal.ZERO;
        try {
            final StringBuffer opBalncQuery1 = new StringBuffer(300);
            opBalncQuery1
            .append(
                    "SELECT CASE WHEN sum(openingdebitbalance) is null THEN 0 ELSE sum(openingdebitbalance) END -")
                    .append(
                            " CASE WHEN sum(openingcreditbalance) is null THEN 0 ELSE sum(openingcreditbalance) END  as openingBalance from TransactionSummary")
                            .append(
                                    " where financialyear.id = ( select id from CFinancialYear where startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                    .append("' AND endingDate >='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                    .append(
                                            "') and glcodeid.id=(select chartofaccounts.id from Bankaccount where id=? )");
            final List<Object> tsummarylist = getPersistenceService()
                    .findAllBy(opBalncQuery1.toString(), bankId.longValue());
            opeAvailable = BigDecimal.valueOf(Double.parseDouble(tsummarylist.get(0).toString()));

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("opeAvailable :" + opeAvailable);

            final StringBuffer opBalncQuery2 = new StringBuffer(300);
            List<Object> list = getPersistenceService()
                    .findAllBy(
                            "select chartofaccounts.id from Bankaccount where id=?",
                            bankId.longValue());
            final Integer glcodeid = Integer.valueOf(list.get(0).toString());

            final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(
                    FinancialConstants.MODULE_NAME_APPCONFIG, "statusexcludeReport");
            final String statusExclude = appList.get(0).getValue();

            opBalncQuery2
            .append(
                    "SELECT (CASE WHEN sum(gl.debitAmount) is null THEN 0 ELSE sum(gl.debitAmount) END - CASE WHEN sum(gl.creditAmount) is null THEN 0 ELSE sum(gl.creditAmount) END)")
                    .append(
                            " as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE gl.voucherHeaderId.id=vh.id and gl.glcodeId=? ")
                            .append(
                                    " and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                    .append("' AND endingDate >='").append(
                                            Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                            .append("') and vh.voucherDate <='").append(
                                                    Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                                    .append("'and vh.status not in (").append(statusExclude)
                                                    .append(")");

            final CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find(
                    "from CChartOfAccounts where id=?", Long.valueOf(glcodeid));
            list = getPersistenceService().findAllBy(
                    opBalncQuery2.toString(), coa);
            bankBalance = BigDecimal.valueOf(Double.parseDouble(list.get(0).toString()));
            bankBalance = opeAvailable.add(bankBalance);

            // get the preapproved voucher amount also, if payment workflow
            // status in FMU level.... and subtract the amount from the balance
            // .

            boolean amountTobeInclude = false;

            if (paymentId != null) {
                // get the payment wf status
                final State s = (State) persistenceService
                        .find(
                                " from org.egov.infra.workflow.entity.State where id in (select state.id from Paymentheader where id=?) ",
                                paymentId);
                String paymentWFStatus = "";
                final List<AppConfigValues> paymentStatusList = appConfigValuesService.getConfigValuesByModuleAndKey(
                        FinancialConstants.MODULE_NAME_APPCONFIG,
                        "PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK");
                for (final AppConfigValues values : paymentStatusList) {
                    if (s.getValue().equals(values.getValue()))
                        amountTobeInclude = true;
                    paymentWFStatus = paymentWFStatus + "'" + values.getValue()
                            + "',";
                }
                if (!paymentWFStatus.equals(""))
                    paymentWFStatus = paymentWFStatus.substring(0,
                            paymentWFStatus.length() - 1);

                final List<AppConfigValues> preAppList = appConfigValuesService.getConfigValuesByModuleAndKey(
                        FinancialConstants.MODULE_NAME_APPCONFIG, "PREAPPROVEDVOUCHERSTATUS");
                final String preApprovedStatus = preAppList.get(0).getValue();

                final StringBuffer paymentQuery = new StringBuffer(400);
                paymentQuery
                .append(
                        "SELECT (CASE WHEN sum(gl.debitAmount) is null THEN 0 ELSE sum(gl.debitAmount) END  - CASE WHEN sum(gl.creditAmount) is null THEN 0 ELSE sum(gl.creditAmount) END )")
                        .append(
                                " as amount FROM  CGeneralLedger gl , CVoucherHeader vh,Paymentheader ph WHERE gl.voucherHeaderId.id=vh.id and ph.voucherheader.id=vh.id and gl.glcodeId=? ")
                                .append(
                                        " and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                        .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                        .append("' AND endingDate >='")
                                        .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                        .append("') and vh.voucherDate <='")
                                        .append(Constants.DDMMYYYYFORMAT1.format(VoucherDate))
                                        .append("'and vh.status in (")
                                        .append(preApprovedStatus)
                                        .append(")")
                                        .append(
                                                " and ph.state in (from org.egov.infra.workflow.entity.State where type='Paymentheader' and value in (")
                                                .append(paymentWFStatus).append(") )");
                list = getPersistenceService().findAllBy(
                        paymentQuery.toString(), coa);
                bankBalance = bankBalance.subtract(BigDecimal.valueOf(Math
                        .abs((Double) list.get(0))));
                final Integer voucherStatus = (Integer) persistenceService
                        .find(
                                "select status from CVoucherHeader where id in (select voucherheader.id from Paymentheader where id=?)",
                                paymentId);
                // if voucher is not preapproved and status is 0 then it is
                // modify so add the amount
                if (voucherStatus == 0)
                    amountTobeInclude = true;
                // if payment workflow status in FMU level.... and add the
                // transaction amount to it.
                if (amountTobeInclude)
                    bankBalance = bankBalance.add(amount);

            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("bankBalance :" + bankBalance);
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("exception occuered while geeting cash balance"
                        + e.getMessage(), e);
            throw new HibernateException(e);
        }
        return bankBalance;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public EntityType getEntityType(final Accountdetailtype accountdetailtype,
            final Serializable detailkey) throws ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getEntityType| Start");
        EntityType entity = null;
        try {
            final Class aClass = Class.forName(accountdetailtype
                    .getFullQualifiedName());
            final java.lang.reflect.Method method = aClass.getMethod("getId");
            final String dataType = method.getReturnType().getSimpleName();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("data Type = " + dataType);
            if (dataType.equals("Long"))
                entity = (EntityType) persistenceService.getSession().load(aClass, Long.valueOf(detailkey.toString()));
            else
                entity = (EntityType) persistenceService.getSession().load(aClass, detailkey);

        } catch (final ClassCastException e) {
            LOGGER.error(e);
            throw new ApplicationException(e.getMessage());
        } catch (final Exception e) {
            LOGGER.error("Exception to get EntityType=" + e.getMessage(), e);
            throw new ApplicationException(e.getMessage());
        }
        return entity;
    }

    /**
     * This method will return the Map of cheque in hand and cash in hand code information for the boundary at which the books of
     * accounts are maintained.
     *
     * @return
     * @throws ValidationException
     */
    public Map<String, Object> getCashChequeInfoForBoundary()
            throws ValidationException {
        String chequeInHand = null;
        Long chequeInHandId = null;
        String cashInHand = null;
        Long cashInHandId = null;
        // String
        // boundaryTypeval=EGovConfig.getProperty("egf_config.xml","city","","BoundaryType");
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                "boundaryforaccounts");
        final String boundaryTypeval = appList.get(0).getValue();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Boundary Type Level  = " + boundaryTypeval);
        if (null == boundaryTypeval || boundaryTypeval.trim().equals(""))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "configuration.parameter.missing",
                    "boundaryforaccounts is missing in appconfig master")));

        final List<BoundaryType> listBoundType = persistenceService
                .findAllBy(
                        "from BoundaryType where lower(name)=? and lower(hierarchyType.name)='administration'",
                        boundaryTypeval.toLowerCase());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("listBoundType size   = " + listBoundType.size());
        final Long boundaryTypeId = listBoundType.get(0).getId();
        final List<Boundary> listBndryLvl = persistenceService.findAllBy("from Boundary where boundaryType.id=?",
                boundaryTypeId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("listBndryLvl size   = " + listBndryLvl.size());
        if (null != listBndryLvl && !listBndryLvl.isEmpty()) {
            final Boundary boundary = listBndryLvl.get(0);
            final Long boundaryId = boundary.getId();
            try {
                final Connection connection = null;

                final String bndQry = "SELECT glcode AS chequeinhand,id FROM CHARTOFACCOUNTS where id = (SELECT chequeinhand FROM CODEMAPPING WHERE EG_BOUNDARYID=?)";
                final PreparedStatement pstmt = connection.prepareStatement(bndQry);
                pstmt.setLong(0, boundaryId);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Cheque In hand account code query =" + bndQry);
                ResultSet resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    chequeInHand = resultSet.getString("chequeinhand");
                    chequeInHandId = resultSet.getLong("id");
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("chequeInHand is " + chequeInHand
                            + " chequeInHandId is " + chequeInHandId);
                final String sqlQuery2 = "SELECT glcode AS cashinhand,id FROM CHARTOFACCOUNTS where id = (SELECT cashinhand FROM CODEMAPPING WHERE EG_BOUNDARYID=?)";
                final PreparedStatement pstmt1 = connection
                        .prepareStatement(sqlQuery2);
                pstmt1.setLong(0, boundaryId);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Cheque In hand account code query =" + sqlQuery2);
                resultSet = pstmt1.executeQuery();
                if (resultSet.next()) {
                    cashInHand = resultSet.getString("cashinhand");
                    cashInHandId = resultSet.getLong("id");
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("cashInHand is " + cashInHand
                            + " cashInHandId is " + cashInHandId);
            } catch (final Exception e) {
                LOGGER.error("Exception occuerd while getting  "
                        + e.getMessage(), e);
                throw new ApplicationRuntimeException(e.getMessage());
            }

        } else {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("listBndryLvl is either null or blank");
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "boundary.value.missing", "Boundary value missing for"
                            + boundaryTypeval)));
        }
        final Map<String, Object> boundaryMap = new HashMap<String, Object>();
        boundaryMap.put("listBndryLvl", listBndryLvl);
        boundaryMap.put("chequeInHand", chequeInHand);
        boundaryMap.put("cashInHand", cashInHand);
        boundaryMap.put("chequeInHandID", chequeInHandId);
        boundaryMap.put("cashInHandID", cashInHandId);
        return boundaryMap;

    }

    public boolean isShowChequeNumber() {
        final String value = appConfigValuesService
                .getConfigValuesByModuleAndKey(Constants.EGF, Constants.CHEQUE_NO_GENERATION_APPCONFIG_KEY).get(0)
                .getValue();
        if ("Y".equalsIgnoreCase(value))
            return false;
        return true;
    }

    /**
     * @author manoranjan
     * @description - Get the account code balance (excluding the day for which the date is passed)for any glcode and the
     * subledger balance,If the accountdetail details are provided then the account balance for the subledger needs to be
     * calculated, else the account code balance needs to be provided.If the balance is positive that means it debit balance , if
     * it is a credit balance then the API will return a -ve balance.
     * @param asondate - Mandatory
     * @param glcode - - Mandatory (validate the master data)-to get the balance for this supplied account code.
     * @param fundcode -Mandatory (Fund code from fund master)
     * @param accountdetailType - optional (if supplied validate the master data)
     * @param accountdetailkey - optional (if supplied validate the master data)
     * @param deptId TODO
     * @return accCodebalance - returns the account code balance for a glcode and subledger type.
     * @throws ValidationException -
     */
    public BigDecimal getAccountBalanceforDate(final Date asondate, final String glcode, final String fundcode,
            final Integer accountdetailType,
            final Integer accountdetailkey, final Integer deptId)
                    throws ValidationException {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getAccountBalanceforDate | Start");
        validateParameterData(asondate, glcode, fundcode, accountdetailType, accountdetailkey);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validation of data is sucessfull");
        final BigDecimal opBalAsonDate = getOpeningBalAsonDate(asondate, glcode, fundcode, accountdetailType, accountdetailkey,
                deptId);
        final BigDecimal glBalAsonDate = getGlcodeBalBeforeDate(asondate, glcode, fundcode, accountdetailType, accountdetailkey,
                deptId);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getAccountBalanceforDate | Start");
        return opBalAsonDate.add(glBalAsonDate);
    }

    /**
     * @author manoranjan
     * @description - Get the account code balance for any glcode and the subledger balance,If the accountdetail details are
     * provided then the account balance for the subledger needs to be calculated, else the account code balance needs to be
     * provided.If the balance is positive that means it debit balance , if it is a credit balance then the API will return a -ve
     * balance.
     * @param asondate - Mandatory
     * @param glcode - - Mandatory (validate the master data)-to get the balance for this supplied account code.
     * @param fundcode -Mandatory (Fund code from fund master)
     * @param accountdetailType - optional (if supplied validate the master data)
     * @param accountdetailkey - optional (if supplied validate the master data)
     * @param deptId TODO
     * @return accCodebalance - returns the account code balance for a glcode and subledger type.
     * @throws ValidationException -
     */
    public BigDecimal getAccountBalanceTillDate(final Date asondate, final String glcode, final String fundcode,
            final Integer accountdetailType,
            final Integer accountdetailkey, final Integer deptId)
                    throws ValidationException {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getAccountBalanceTillDate | Start");
        validateParameterData(asondate, glcode, fundcode, accountdetailType, accountdetailkey);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validation of data is sucessfull");
        final BigDecimal opBalAsonDate = getOpeningBalAsonDate(asondate, glcode, fundcode, accountdetailType, accountdetailkey,
                deptId);
        final BigDecimal glBalAsonDate = getGlcodeBalTillDate(asondate, glcode, fundcode, accountdetailType, accountdetailkey,
                deptId);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getAccountBalanceTillDate | Opening Balance :" + opBalAsonDate + " Txn Balance  :"
                    + glBalAsonDate);
        return opBalAsonDate.add(glBalAsonDate);
    }

    private void validateParameterData(final Date asondate, final String glcode,
            final String fundcode, final Integer accountdetailType, final Integer accountdetailkey) {

        if (null == asondate)
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "asondate", "asondate supplied is null")));

        if (null == glcode || StringUtils.isEmpty(glcode))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "glcode", "glcode supplied is either null or empty")));
        else if (null == chartOfAccountsDAO.getCChartOfAccountsByGlCode(glcode))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "glcode", "not a valid glcode :" + glcode)));

        if (null == fundcode || StringUtils.isEmpty(fundcode))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "fundcode", "Fundcode supplied is either null or empty")));
        else if (null == fundDAO.fundByCode(fundcode))
            throw new ValidationException(Arrays
                    .asList(new ValidationError("fundcode",
                            "The Fundcode supplied : " + fundcode
                            + " is not present in the system.")));

        if (null != accountdetailType) {
            final Session session = persistenceService.getSession();
            final Query qry = session
                    .createQuery("from CChartOfAccountDetail cd,CChartOfAccounts c where "
                            + "cd.glCodeId = c.id and c.glcode=:glcode and cd.detailTypeId=:detailTypeId");
            qry.setString(VoucherConstant.GLCODE, glcode);
            qry.setString("detailTypeId", accountdetailType.toString());

            if (null == qry.list() || qry.list().size() == 0)
                throw new ValidationException(
                        Arrays
                        .asList(new ValidationError(
                                "accountdetailType",
                                "Glcode "
                                        + glcode
                                        + " is not a control code for the supplied detailed type.")));

        }
        if (null != accountdetailkey) {
            final Session session = persistenceService.getSession();
            final Query qry = session
                    .createQuery("from Accountdetailkey adk where adk.accountdetailtype=:detailtypeid and adk.detailkey=:detailkey");
            qry.setString(VoucherConstant.DETAILTYPEID, accountdetailType
                    .toString());
            qry.setString("detailkey", accountdetailkey.toString());

            if (null == qry.list() || qry.list().size() == 0)
                throw new ValidationException(
                        Arrays
                        .asList(new ValidationError(
                                "accountdetailkey",
                                "The accountdetailkey supplied : "
                                        + accountdetailkey
                                        + " for the accountdetailType : "
                                        + accountdetailType
                                        + " is not correct")));
        }
    }

    @SuppressWarnings("unchecked")
    public BigDecimal getOpeningBalAsonDate(final Date asondate, final String glcode,
            final String fundCode, final Integer accountdetailType, final Integer accountdetailkey, final Integer deptId)
                    throws ValidationException {
        BigDecimal opBalAsonDate = BigDecimal.ZERO;
        final StringBuffer opBalncQuery = new StringBuffer(300);
        String deptCondition = "";
        String fundConidtion = "";
        if (fundCode != null)
            fundConidtion = " and fund.code='" + fundCode + "'";
        if (deptId != null)
            deptCondition = " and departmentid.id=" + deptId;

        opBalncQuery
        .append(
                "SELECT case when sum(openingdebitbalance) is null then  0  else sum(openingdebitbalance) end -")
                .append(
                        "  case when sum(openingcreditbalance) is null then 0 else sum(openingcreditbalance) end  as openingBalance from TransactionSummary")
                        .append(
                                " where financialyear.id = ( select id from CFinancialYear where startingDate <= '")
                                .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                        "' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                                "') and glcodeid.glcode=? ").append(fundConidtion + deptCondition);
        if (null != accountdetailType)
            opBalncQuery.append(" and accountdetailtype.id=").append(
                    accountdetailType);
        if (null != accountdetailkey)
            opBalncQuery.append(" and accountdetailkey=").append(
                    accountdetailkey);
        final List<Object> tsummarylist = getPersistenceService().findAllBy(opBalncQuery.toString(), glcode);
        opBalAsonDate = BigDecimal.valueOf((Integer) tsummarylist.get(0));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Opening balance :" + opBalAsonDate);

        return opBalAsonDate;
    }

    /**
     * This API will return the sum total of credit opening balances for a given account code and sub ledger details.
     *
     * @param asondate
     * @param glcode
     * @param fundCode
     * @param accountdetailType
     * @param accountdetailkey
     * @return opening balance if exits, else returns zero.
     * @throws ValidationException
     */
    private BigDecimal getCreditOpeningBalAsonDate(final Date asondate,
            final String glcode, final String fundCode, final Integer accountdetailType,
            final Integer accountdetailkey) throws ValidationException {
        BigDecimal opBalAsonDate = BigDecimal.ZERO;
        final StringBuffer opBalncQuery = new StringBuffer(300);
        // Opening balance query when sublegder info are there
        if (null != accountdetailkey && null != accountdetailType)
            opBalncQuery
            .append(
                    " Select sum(txns.openingcreditbalance) as openingBalance ")
                    .append(
                            "From transactionsummary txns,fund fd, chartofaccounts coa,accountdetailtype adt,accountdetailkey adk")
                            .append(
                                    " where coa.id=txns.glcodeid and fd.id=txns.fundid  and adt.id=txns.accountdetailtypeid and adk.detailkey=txns.accountdetailkey ")
                                    .append(" and coa.glcode='")
                                    .append(glcode)
                                    .append("' and fd.code='")
                                    .append(fundCode)
                                    .append(
                                            "'and txns.financialyearid in(select id from financialyear where startingdate<='")
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
                                            .append(
                                                    " Group by txns.GLCODEID,txns.fundid,txns.FINANCIALYEARID,txns.accountdetailtypeid,txns.accountdetailkey ");
        else
            // Opening balance query when subledger data is not there
            opBalncQuery
            .append(
                    " Select sum(txns.openingcreditbalance) as openingBalance From transactionsummary txns,fund fd, chartofaccounts coa")
                    .append(
                            " where coa.id=txns.glcodeid and fd.id=txns.fundid ")
                            .append(" and coa.glcode='")
                            .append(glcode)
                            .append("' and fd.code='")
                            .append(fundCode)
                            .append(
                                    "'and txns.financialyearid in(select id from financialyear where startingdate<='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("' and endingdate>='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("')")
                                    .append(
                                            " Group by txns.GLCODEID,txns.fundid,txns.FINANCIALYEARID ");

        final List<Object> list = persistenceService.getSession()
                .createSQLQuery(opBalncQuery.toString()).list();
        if (list != null && list.size() > 0)
            opBalAsonDate = (BigDecimal) list.get(0);
        opBalAsonDate = opBalAsonDate == null ? BigDecimal.ZERO : opBalAsonDate;
        return opBalAsonDate;
    }

    protected BigDecimal getGlcodeBalBeforeDate(final Date asondate, final String glcode,
            final String fundcode, final Integer accountdetailType, final Integer accountdetailkey, final Integer deptId)
                    throws ValidationException {
        final StringBuffer glCodeBalQry = new StringBuffer(400);
        final StringBuffer glCodeDbtBalQry = new StringBuffer(400);
        final StringBuffer glCodeCrdBalQry = new StringBuffer(400);
        BigDecimal glCodeBalance = BigDecimal.ZERO;
        BigDecimal glCodeDbtBalance = BigDecimal.ZERO;
        BigDecimal glCodeCrdBalance = BigDecimal.ZERO;
        String deptCond = "";
        String misTab = "";
        String fundCond = "";
        if (fundcode != null)
            fundCond = " and vh.fundId.code='" + fundcode + "'";
        if (deptId != null) {
            misTab = ",Vouchermis mis";
            deptCond = " and mis.voucherheaderid.id=vh.id and mis.departmentid.id=" + deptId;
        }

        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                "statusexcludeReport");
        final String statusExclude = appList.get(0).getValue();
        if (null == accountdetailType && null == accountdetailkey) {
            glCodeBalQry
            .append("SELECT (case when sum(gl.debitAmount) is null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount)  is null then 0 else sum(gl.creditAmount) end)")
            .append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh  ").append(misTab)
            .append(" WHERE gl.voucherHeaderId.id=vh.id and gl.glcodeId.glcode=?")
            .append(fundCond + deptCond)
            .append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
            .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append("' AND endingDate >='").append(
                    Constants.DDMMYYYYFORMAT1.format(asondate)).append("') and vh.voucherDate <'").append(
                            Constants.DDMMYYYYFORMAT1.format(asondate)).append("'and vh.status not in (").append(statusExclude)
                            .append(")");

            final List<Object> list = getPersistenceService().findAllBy(glCodeBalQry.toString(), glcode);
            glCodeBalance = BigDecimal.valueOf((Integer) list.get(0));
        } else {
            // Getting the debit balance.
            glCodeDbtBalQry
            .append("SELECT sum(gld.amount)  as debitamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld ")
            .append(misTab)
            .append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=? ")
            .append(fundCond)
            .append(deptCond)
            .append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
            .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append("' AND endingDate >='").append(
                    Constants.DDMMYYYYFORMAT1.format(asondate)).append("') and vh.voucherDate <'").append(
                            Constants.DDMMYYYYFORMAT1.format(asondate)).append("'and vh.status not in (").append(statusExclude)
                            .append(")").append(" and gld.detailTypeId.id =").append(accountdetailType);

            if (null != accountdetailkey)
                glCodeDbtBalQry.append(" and gld.detailKeyId =").append(
                        accountdetailkey);
            glCodeDbtBalQry.append(" and gl.debitAmount >0");

            final List<Object> listDbt = getPersistenceService().findAllBy(glCodeDbtBalQry.toString(), glcode);
            glCodeDbtBalance = (BigDecimal) listDbt.get(0) == null ? BigDecimal.ZERO
                    : (BigDecimal) listDbt.get(0);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total debit amount :  " + glCodeDbtBalance);

            // get the credit balance

            glCodeCrdBalQry
            .append("SELECT sum(gld.amount) as creditamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
            .append(misTab)
            .append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=? ")
            .append(fundCond).append(deptCond)
            .append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
            .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append("' AND endingDate >='").append(
                    Constants.DDMMYYYYFORMAT1.format(asondate)).append("') and vh.voucherDate <'").append(
                            Constants.DDMMYYYYFORMAT1.format(asondate)).append("'and vh.status not in (").append(statusExclude)
                            .append(")").append(" and gld.detailTypeId.id =").append(accountdetailType);

            if (null != accountdetailkey)
                glCodeCrdBalQry.append(" and gld.detailKeyId =").append(
                        accountdetailkey);
            glCodeCrdBalQry.append(" and gl.creditAmount >0");

            final List<Object> listCrd = getPersistenceService().findAllBy(glCodeCrdBalQry.toString(), glcode);
            glCodeCrdBalance = (BigDecimal) listCrd.get(0) == null ? BigDecimal.ZERO : (BigDecimal) listCrd.get(0);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total credit amount :  " + glCodeCrdBalance);
            glCodeBalance = glCodeDbtBalance.subtract(glCodeCrdBalance);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total balance amount :  " + glCodeBalance);

        }

        return glCodeBalance;
    }

    protected BigDecimal getGlcodeBalTillDate(final Date asondate, final String glcode,
            final String fundcode, final Integer accountdetailType, final Integer accountdetailkey, final Integer deptId)
                    throws ValidationException {
        final StringBuffer glCodeBalQry = new StringBuffer(400);
        final StringBuffer glCodeDbtBalQry = new StringBuffer(400);
        final StringBuffer glCodeCrdBalQry = new StringBuffer(400);
        BigDecimal glCodeBalance = BigDecimal.ZERO;
        BigDecimal glCodeDbtBalance = BigDecimal.ZERO;
        BigDecimal glCodeCrdBalance = BigDecimal.ZERO;
        String deptCond = "";
        String misTab = "";
        String fundCond = "";
        if (fundcode != null)
            fundCond = " and vh.fundId.code='" + fundcode + "'";
        if (deptId != null) {
            misTab = ",Vouchermis mis";
            deptCond = " and mis.voucherheaderid.id=vh.id and mis.departmentid.id=" + deptId;
        }

        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                "statusexcludeReport");
        final String statusExclude = appList.get(0).getValue();
        if (null == accountdetailType && null == accountdetailkey) {
            glCodeBalQry
            .append("SELECT (case when sum(gl.debitAmount)=null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount)  = null then 0 else sum(gl.creditAmount) end)")
            .append(" as amount FROM  CGeneralLedger gl , CVoucherHeader vh  ").append(misTab)
            .append(" WHERE gl.voucherHeaderId.id=vh.id and gl.glcodeId.glcode=?")
            .append(fundCond + deptCond)
            .append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
            .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append("' AND endingDate >='").append(
                    Constants.DDMMYYYYFORMAT1.format(asondate)).append("') and vh.voucherDate <='").append(
                            Constants.DDMMYYYYFORMAT1.format(asondate)).append("'and vh.status not in (").append(statusExclude)
                            .append(")");

            final List<Object> list = getPersistenceService().findAllBy(glCodeBalQry.toString(), glcode);
            glCodeBalance = BigDecimal.valueOf((Double) list.get(0));
        } else {
            // Getting the debit balance.
            glCodeDbtBalQry
            .append("SELECT sum(gld.amount)  as debitamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld ")
            .append(misTab)
            .append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=? ")
            .append(fundCond)
            .append(deptCond)
            .append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
            .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append("' AND endingDate >='").append(
                    Constants.DDMMYYYYFORMAT1.format(asondate)).append("') and vh.voucherDate <='").append(
                            Constants.DDMMYYYYFORMAT1.format(asondate)).append("'and vh.status not in (").append(statusExclude)
                            .append(")").append(" and gld.detailTypeId.id =").append(accountdetailType);

            if (null != accountdetailkey)
                glCodeDbtBalQry.append(" and gld.detailKeyId =").append(
                        accountdetailkey);
            glCodeDbtBalQry.append(" and gl.debitAmount >0");

            final List<Object> listDbt = getPersistenceService().findAllBy(glCodeDbtBalQry.toString(), glcode);
            glCodeDbtBalance = (BigDecimal) listDbt.get(0) == null ? BigDecimal.ZERO
                    : (BigDecimal) listDbt.get(0);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total debit amount :  " + glCodeDbtBalance);

            // get the credit balance

            glCodeCrdBalQry
            .append("SELECT sum(gld.amount) as creditamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
            .append(misTab)
            .append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=? ")
            .append(fundCond).append(deptCond)
            .append(" and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
            .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append("' AND endingDate >='").append(
                    Constants.DDMMYYYYFORMAT1.format(asondate)).append("') and vh.voucherDate <='").append(
                            Constants.DDMMYYYYFORMAT1.format(asondate)).append("'and vh.status not in (").append(statusExclude)
                            .append(")").append(" and gld.detailTypeId.id =").append(accountdetailType);

            if (null != accountdetailkey)
                glCodeCrdBalQry.append(" and gld.detailKeyId =").append(
                        accountdetailkey);
            glCodeCrdBalQry.append(" and gl.creditAmount >0");

            final List<Object> listCrd = getPersistenceService().findAllBy(glCodeCrdBalQry.toString(), glcode);
            glCodeCrdBalance = (BigDecimal) listCrd.get(0) == null ? BigDecimal.ZERO : (BigDecimal) listCrd.get(0);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total credit amount :  " + glCodeCrdBalance);
            glCodeBalance = glCodeDbtBalance.subtract(glCodeCrdBalance);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total balance amount :  " + glCodeBalance);

        }

        return glCodeBalance;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBankBranchForActiveBanks() { // This??
        final List<Object[]> unorderedBankBranch = persistenceService
                .findAllBy(
                        "select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname "
                                + " FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount "
                                + " where  bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id"
                                + " and bankaccount.isactive=? ", true);
        // Ordering Starts
        final List<String> bankBranchStrings = new ArrayList<String>();
        int i, j;
        final int len = unorderedBankBranch.size();
        for (i = 0; i < len; i++)
            bankBranchStrings.add(unorderedBankBranch.get(i)[1].toString());
        Collections.sort(bankBranchStrings);
        final List<Object[]> bankBranch = new ArrayList();
        for (i = 0; i < len; i++)
            for (j = 0; j < len; j++)
                if (bankBranchStrings.get(i).equalsIgnoreCase(
                        unorderedBankBranch.get(j)[1].toString()))
                    bankBranch.add(unorderedBankBranch.get(j));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Bank list size is " + bankBranch.size());
        final List<Map<String, Object>> bankBranchList = new ArrayList<Map<String, Object>>();
        Map<String, Object> bankBrmap;
        for (final Object[] element : bankBranch) {
            bankBrmap = new HashMap<String, Object>();
            bankBrmap.put("bankBranchId", element[0].toString());
            bankBrmap.put("bankBranchName", element[1].toString());
            bankBranchList.add(bankBrmap);
        }
        LOGGER.info("data" + bankBranchList);
        return bankBranchList;
    }

    @SuppressWarnings("unchecked")
    public List<Bankbranch> getActiveBankBranchForActiveBanks() {
        return persistenceService.findAllBy("from Bankbranch bankBranch where  bank.isactive=true  and isactive=true");
    }

    @SuppressWarnings("unchecked")
    public List<CChartOfAccounts> getSubledgerAccountCodesForAccountDetailTypeAndNonSubledgers(
            final Integer accountDetailTypeId) {
        if (accountDetailTypeId == 0 || accountDetailTypeId == -1)
            return persistenceService
                    .findAllBy("from CChartOfAccounts a where a.isActiveForPosting=true and a.classification=4 and size(a.chartOfAccountDetails) = 0  order by a.id");
        else
            return persistenceService
                    .findAllBy(
                            "from CChartOfAccounts  a LEFT OUTER JOIN  fetch a.chartOfAccountDetails  b where (size(a.chartOfAccountDetails) = 0 or b.detailTypeId.id=?)and a.isActiveForPosting=true and a.classification=4 order by a.id",
                            accountDetailTypeId);
    }

    /**
     * changed while 2ndlevel caching fix
     * @param accountDetailTypeId
     * @return
     */
    public List<CChartOfAccounts> getAllAccountCodesForAccountDetailType(
            final Integer accountDetailTypeId) {
        LOGGER
        .debug("Initiating getAllAccountCodesForAccountDetailType for detailtypeId "
                + accountDetailTypeId + "...");
        final List<CChartOfAccounts> subledgerCodes = getSubledgerAccountCodesForAccountDetailTypeAndNonSubledgers(accountDetailTypeId);
        // List<CChartOfAccounts> accountCodesForDetailType = new ArrayList<CChartOfAccounts>();
        // accountCodesForDetailType.addAll(subledgerCodes);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("finished getAllAccountCodesForAccountDetailType for detailtypeId "
                    + accountDetailTypeId
                    + ".size:"
                    + subledgerCodes.size() + ".");
        return subledgerCodes;
    }

    public BigDecimal getOpeningBalAsonDate(final Date asondate, final String glcode,
            final String fundCode) throws ValidationException {
        BigDecimal opBalAsonDate = BigDecimal.ZERO;
        final StringBuffer opBalncQuery = new StringBuffer(300);
        opBalncQuery
        .append(
                "SELECT case when sum(openingdebitbalance) = null then  0  else sum(openingdebitbalance) end -")
                .append(
                        "  case when sum(openingcreditbalance) = null then 0 else sum(openingcreditbalance) end  as openingBalance from TransactionSummary")
                        .append(
                                " where financialyear.id = ( select id from CFinancialYear where startingDate <= '")
                                .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                        "' AND endingDate >='").append(
                                                Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                                        "') and glcodeid.glcode=? and fund.code=?");
        final List<Object> tsummarylist = getPersistenceService()
                .findAllBy(opBalncQuery.toString(), glcode, fundCode);
        opBalAsonDate = BigDecimal.valueOf((Double) tsummarylist.get(0));
        return opBalAsonDate;
    }

    /**
     * @description - get the list of BudgetUsage based on various parameters
     * @param queryParamMap - HashMap<String, Object> queryParamMap will have data required for the query Query Parameter Map keys
     * are - fundId,ExecutionDepartmentId ,functionId,moduleId,financialYearId ,budgetgroupId,fromDate,toDate and Order By
     * @return
     */

    @SuppressWarnings("unchecked")
    public List<BudgetUsage> getListBudgetUsage(
            final Map<String, Object> queryParamMap) {

        final StringBuffer query = new StringBuffer();
        List<BudgetUsage> listBudgetUsage = null;
        query
        .append("select bu from BudgetUsage bu,BudgetDetail bd where  bu.budgetDetail.id=bd.id");
        final Map<String, String> mandatoryFields = new HashMap<String, String>();
        final List<AppConfigValues> appConfigList = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,"DEFAULTTXNMISATTRRIBUTES");
            for (final AppConfigValues appConfigVal : appConfigList) {
                final String value = appConfigVal.getValue();
                final String header = value.substring(0, value.indexOf("|"));
                final String mandate = value.substring(value.indexOf("|") + 1);
                if (mandate.equalsIgnoreCase("M"))
                    mandatoryFields.put(header, "M");
            }
        if (isNotNull(mandatoryFields.get("fund"))
                && !isNotNull(queryParamMap.get("fundId")))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "fund", "fund cannot be null")));
        else if (isNotNull(queryParamMap.get("fundId")))
            query.append(" and bd.fund.id=").append(
                    Integer.valueOf(queryParamMap.get("fundId").toString()));
        if (isNotNull(mandatoryFields.get("department"))
                && !isNotNull(queryParamMap.get("ExecutionDepartmentId")))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "department", "department cannot be null")));
        else if (isNotNull(queryParamMap.get("ExecutionDepartmentId")))
            query.append(" and bd.executingDepartment.id=").append(
                    Integer.valueOf(queryParamMap.get("ExecutionDepartmentId")
                            .toString()));
        if (isNotNull(mandatoryFields.get("function"))
                && !isNotNull(queryParamMap.get("functionId")))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "function", "function cannot be null")));
        else if (isNotNull(queryParamMap.get("functionId")))
            query.append(" and bd.function.id=").append(
                    Long.valueOf(queryParamMap.get("functionId").toString()));

        if (isNotNull(queryParamMap.get("moduleId")))
            query.append(" and bu.moduleId=").append(
                    Integer.valueOf(queryParamMap.get("moduleId").toString()));
        if (isNotNull(queryParamMap.get("financialYearId")))
            query.append(" and bu.financialYearId=").append(
                    Integer.valueOf(queryParamMap.get("financialYearId")
                            .toString()));
        if (isNotNull(queryParamMap.get("budgetgroupId")))
            query.append(" and bd.budgetGroup.id=")
            .append(
                    Long.valueOf(queryParamMap.get("budgetgroupId")
                            .toString()));
        if (isNotNull(queryParamMap.get("fromDate")))
            query.append(" and bu.updatedTime >=:from");
        if (isNotNull(queryParamMap.get("toDate")))
            query.append(" and bu.updatedTime <=:to");
        if (isNotNull(queryParamMap.get("Order By")))
            query.append(" Order By ").append(queryParamMap.get("Order By"));
        else
            query.append(" Order By bu.updatedTime");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Budget Usage Query >>>>>>>> " + query.toString());
        final Query query1 = persistenceService.getSession().createQuery(
                query.toString());
        if (isNotNull(queryParamMap.get("fromDate")))
            query1.setTimestamp("from", (Date) queryParamMap.get("fromDate"));
        if (isNotNull(queryParamMap.get("toDate"))) {
            final Date date = (Date) queryParamMap.get("toDate");
            date.setMinutes(59);
            date.setHours(23);
            date.setSeconds(59);
            query1.setTimestamp("to", date);
        }

        listBudgetUsage = query1.list();
        return listBudgetUsage;

    }

    private boolean isNotNull(final Object ob) {
        if (ob != null)
            return true;
        else
            return false;
    }

    public List<EntityType> loadEntitesFor(final Accountdetailtype detailType)
            throws ClassNotFoundException {
        final String table = detailType.getFullQualifiedName();
        final Class<?> service = Class.forName(table);
        String simpleName = service.getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase()
                + simpleName.substring(1) + "Service";
        final EntityTypeService entityService = (EntityTypeService) context.getBean(simpleName);
        return (List<EntityType>) entityService.getAllActiveEntities(detailType
                .getId());
    }

    /**
     * @author manoranjan
     * @description - API to get the net balance for a glcode from bills only
     * @param asondate - Mandatory
     * @param glcode - Mandatory (validate the master data)-to get the balance for this supplied account code.
     * @param fundcode -Mandatory (Fund code from fund master)
     * @param accountdetailType - optional (if supplied validate the master data)
     * @param accountdetailkey - optional (if supplied validate the master data)
     * @return billAccbalance - returns the account code balance for a glcode and subledger type.
     * @throws ValidationException
     */
    public BigDecimal getBillAccountBalanceforDate(final Date asondate,
            final String glcode, final String fundcode, final Integer accountdetailType,
            final Integer accountdetailkey) throws ValidationException {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getBillAccountBalanceforDate | Start");
        LOGGER
        .debug("Data Received asondate = " + asondate + " glcode = "
                + glcode + " fundcode = " + fundcode
                + " accountdetailType = " + accountdetailType
                + " accountdetailkey = " + accountdetailkey);
        validateParameterData(asondate, glcode, fundcode, accountdetailType,
                accountdetailkey);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validation of data is sucessfull");
        final BigDecimal billBalAsonDate = getBillAccBalAsonDate(asondate, glcode,
                fundcode, accountdetailType, accountdetailkey);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getBillAccountBalanceforDate | End");
        return billBalAsonDate;
    }

    private BigDecimal getBillAccBalAsonDate(final Date asondate, final String glcode,
            final String fundcode, final Integer accountdetailType, final Integer accountdetailkey)
                    throws ValidationException {

        final StringBuffer query = new StringBuffer(400);
        BigDecimal billAccCodeBalance = BigDecimal.ZERO;
        if (null == accountdetailType && null == accountdetailkey) {

            query
            .append(
                    "SELECT (case when sum(egd.debitamount) = null then 0 else sum(egd.debitamount) end - case when sum(egd.creditamount) = null THEN 0 else sum(egd.creditamount) end)")
                    .append(
                            "as amount FROM EgBillregister egb, EgBilldetails egd,EgBillregistermis egmis ");
            query
            .append(
                    " Where egb.id = egmis.egBillregister.id and egd.egBillregister.id = egb.id and egmis.voucherHeader is null ")
                    .append(
                            " and egd.glcodeid=(select id from CChartOfAccounts where glcode=?) and egmis.fund.code=?")
                            .append(" and egb.billdate <='").append(
                                    Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                            "' and egb.status IN (select id from ").append(
                                                    " EgwStatus where UPPER(code)!='CANCELLED')");

        } else {
            query
            .append(
                    "SELECT (case when sum(egp.debitAmount) = null then 0 else sum(egp.debitAmount) - case when sum(egp.creditAmount) = null then 0 else sum(egp.creditAmount))")
                    .append(
                            "as amount FROM EgBillregister egb, EgBilldetails egd,EgBillregistermis egmis,EgBillPayeedetails egp");
            query
            .append(
                    " Where egb.id = egmis.egBillregister.id and egd.egBillregister.id = egb.id and egmis.voucherHeader is null ")
                    .append(
                            " and egp.egBilldetailsId.id=egd.id and egd.glcodeid=(select id from CChartOfAccounts where glcode=?) and egmis.fund.code=?")
                            .append(" and egb.billdate <='").append(
                                    Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                            "' and egb.status IN (select id from ").append(
                                                    " EgwStatus where UPPER(code)!='CANCELLED')")
                                                    .append(" and egp.accountDetailTypeId=").append(
                                                            accountdetailType);
            if (null != accountdetailkey)
                query.append(" and egp.accountDetailKeyId=").append(
                        accountdetailkey);

        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getBillAccBalAsonDate query = " + query.toString());
        final List<Object> listAmt = getPersistenceService()
                .findAllBy(query.toString(), glcode, fundcode);
        listAmt.get(0);
        listAmt.get(0);
        billAccCodeBalance = BigDecimal.valueOf(listAmt.get(0) == null ? 0
                : (Double) listAmt.get(0));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getBillAccBalAsonDate | Bill Account Balance = "
                    + billAccCodeBalance);
        return billAccCodeBalance;

    }

    /**
     * @author manoranjan
     * @description - API to get the credit balance for a glcode and subledger
     * @param asondate - Mandatory
     * @param glcode - Mandatory (validate the master data)-to get the balance for this supplied account code.
     * @param fundcode -Mandatory (Fund code from fund master)
     * @param accountdetailType - optional (if supplied validate the master data)
     * @param accountdetailkey - optional (if supplied validate the master data)
     * @return creditBalance - returns the credit balance for a glcode and subledger type including the opening balance for the
     * year.
     * @throws ValidationException
     */
    public BigDecimal getCreditBalanceforDate(final Date asondate, final String glcode,
            final String fundcode, final Integer accountdetailType, final Integer accountdetailkey)
                    throws ValidationException {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getCreditBalanceforDate | Start");
        LOGGER
        .debug("Data Received asondate = " + asondate + " glcode = "
                + glcode + " fundcode = " + fundcode
                + " accountdetailType = " + accountdetailType
                + " accountdetailkey = " + accountdetailkey);
        validateParameterData(asondate, glcode, fundcode, accountdetailType,
                accountdetailkey);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validation of data is sucessfull");
        // Get the credit opening balance for the year
        BigDecimal creditOpeningBalance = getCreditOpeningBalAsonDate(asondate,
                glcode, fundcode, accountdetailType, accountdetailkey);
        BigDecimal creditBalance = null;
        final StringBuffer query = new StringBuffer(400);
        if (null == accountdetailType && null == accountdetailkey) {
            query
            .append("SELECT  sum(gl.creditAmount)")
            .append(
                    " as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE ")
                    .append(
                            " gl.voucherHeaderId.id=vh.id and gl.glcodeId.glcode=? and vh.fundId.code=? ")
                            .append(
                                    " and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                            "' AND endingDate >='").append(
                                                    Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                                            "') and vh.voucherDate <='").append(
                                                                    Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                                                            "'and vh.status=0");

            final List<Object> list = getPersistenceService()
                    .findAllBy(query.toString(), glcode, fundcode);
            final Double amount = (Double) list.get(0) == null ? 0 : (Double) list
                    .get(0);
            creditBalance = BigDecimal.valueOf(amount);

        } else {
            query
            .append(
                    "SELECT sum(gld.amount) as creditamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
                    .append(
                            " WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=? and vh.fundId.code=? ")
                            .append(
                                    " and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                            "' AND endingDate >='").append(
                                                    Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                                            "') and vh.voucherDate <='").append(
                                                                    Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                                                            "'and vh.status = 0").append(
                                                                                    " and gld.detailTypeId.id =")
                                                                                    .append(accountdetailType);
            if (null != accountdetailkey)
                query.append(" and gld.detailKeyId =").append(accountdetailkey);
            query.append(" and gl.creditAmount >0");
            final List<Object> listCrd = getPersistenceService()
                    .findAllBy(query.toString(), glcode, fundcode);
            creditBalance = (BigDecimal) listCrd.get(0) == null ? BigDecimal.ZERO
                    : (BigDecimal) listCrd.get(0);
        }
        creditOpeningBalance = creditOpeningBalance == null ? BigDecimal.ZERO
                : creditOpeningBalance;
        creditBalance = creditBalance.add(creditOpeningBalance);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getCreditBalanceforDate | End");
        return creditBalance;
    }

    /**
     *
     */
    public BigDecimal getDepositAmountForDepositCode(final Date asondate,
            final String glcode, final String fundcode, final Integer accountdetailType,
            final Integer accountdetailkey) throws ValidationException {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getCreditBalanceforDate | Start");
        LOGGER
        .debug("Data Received asondate = " + asondate + " glcode = "
                + glcode + " fundcode = " + fundcode
                + " accountdetailType = " + accountdetailType
                + " accountdetailkey = " + accountdetailkey);
        validateParameterData(asondate, glcode, fundcode, accountdetailType,
                accountdetailkey);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validation of data is sucessfull");

        final StringBuffer queryString = new StringBuffer(400);

        queryString
        .append("SELECT MIN(vh.voucherDate) as vhDate from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
        .append(" WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=? and vh.fundId.code=? ")
        .append(" and vh.voucherDate <= '").append(Constants.DDMMYYYYFORMAT1.format(asondate))
        .append("' and vh.status = 0")
        .append(" and gld.detailTypeId.id =").append(accountdetailType);
        queryString.append(" and gld.detailKeyId =").append(accountdetailkey);
        queryString.append(" and gl.creditAmount >0");

        Date minVouDate = (Date) getPersistenceService().findAllBy(
                queryString.toString(), glcode, fundcode).get(0);

        if (minVouDate == null)
            minVouDate = new Date();

        BigDecimal creditOpeningBalance = getFirstCreditOpeningBalForDepositCodeAsonDate(minVouDate, glcode, fundcode,
                accountdetailkey);
        BigDecimal creditBalance = null;
        final StringBuffer query = new StringBuffer(400);

        query
        .append(
                "SELECT sum(gld.amount) as creditamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
                .append(
                        " WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=? and vh.fundId.code=? ")
                        .append(" and vh.voucherDate <= '").append(
                                Constants.DDMMYYYYFORMAT1.format(asondate)).append(
                                        "' and vh.status = 0")
                                        .append(" and gld.detailTypeId.id =").append(accountdetailType);
        if (null != accountdetailkey)
            query.append(" and gld.detailKeyId =").append(accountdetailkey);
        query.append(" and gl.creditAmount >0");
        final List<Object> listCrd = getPersistenceService()
                .findAllBy(query.toString(), glcode, fundcode);
        creditBalance = (BigDecimal) listCrd.get(0) == null ? BigDecimal.ZERO
                : (BigDecimal) listCrd.get(0);

        creditOpeningBalance = creditOpeningBalance == null ? BigDecimal.ZERO
                : creditOpeningBalance;
        creditBalance = creditBalance.add(creditOpeningBalance);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getCreditBalanceforDate | End");
        return creditBalance;
    }

    /**
     * This API will return the sum total of credit opening balances for a given account code and deposit code details.
     *
     * @param asondate
     * @param glcode
     * @param fundCode
     * @param accountdetailkey
     * @return opening balance if exits, else returns zero.
     * @throws ValidationException
     */
    private BigDecimal getFirstCreditOpeningBalForDepositCodeAsonDate(final Date asondate, final String glcode,
            final String fundCode,
            final Integer accountdetailkey) throws ValidationException {
        BigDecimal opBalAsonDate = BigDecimal.ZERO;
        final StringBuffer opBalncQuery = new StringBuffer(300);

        if (null != accountdetailkey)
            opBalncQuery
            .append("SELECT SUM(txns.openingcreditbalance) FROM transactionsummary txns, chartofaccounts coa, fund fd, accountdetailtype adt, financialyear fy "
                    +
                    "WHERE txns.fundid           = fd.id " +
                    "AND fd.code                 = '").append(fundCode).append("' " +
                            "AND txns.accountdetailkey   =").append(accountdetailkey)
                            .append(" AND txns.accountdetailtypeid= adt.id " +
                                    "AND upper(adt.name)         = 'DEPOSITCODE' " +
                                    "AND txns.glcodeid           = coa.id " +
                                    "AND coa.glcode              = '").append(glcode).append("' " +
                                            "AND txns.financialyearid    = fy.id " +
                                            "AND fy.startingdate        <='").append(Constants.DDMMYYYYFORMAT1.format(asondate)).append("' " +
                                                    "GROUP BY fy.startingdate ORDER BY fy.startingdate");

        final List<Object> list = persistenceService.getSession().createSQLQuery(opBalncQuery.toString()).list();
        if (list != null && list.size() > 0)
            opBalAsonDate = (BigDecimal) list.get(0);
        opBalAsonDate = opBalAsonDate == null ? BigDecimal.ZERO : opBalAsonDate;
        return opBalAsonDate;
    }

    public BigDecimal getAccCodeBalanceForIndirectExpense(final Date asondate,
            final String glcode, final Integer accountdetailType, final String accountdetailkey)
                    throws ValidationException, Exception {
        LOGGER
        .debug("EgovCommon | getAccCodeBalanceForIndirectExpense | Start");
        validateParameterData(asondate, glcode, accountdetailType,
                accountdetailkey);
        final StringBuffer glCodeBalQry = new StringBuffer(400);
        final StringBuffer glCodeDbtBalQry = new StringBuffer(400);
        final StringBuffer glCodeCrdBalQry = new StringBuffer(400);
        BigDecimal glCodeBalance = BigDecimal.ZERO;
        BigDecimal subledgerDbtBalance = BigDecimal.ZERO;
        BigDecimal subledgerCrdBalance = BigDecimal.ZERO;

        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(
                FinancialConstants.MODULE_NAME_APPCONFIG, "statusexcludeReport");
        final String statusExclude = appList.get(0).getValue();
        if (null == accountdetailType && null == accountdetailkey) {
            glCodeBalQry
            .append(
                    "SELECT (case when sum(gl.debitAmount)=null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount)  = null then 0 else sum(gl.creditAmount) end)")
                    .append(
                            " as amount FROM  CGeneralLedger gl , CVoucherHeader vh WHERE  gl.voucherHeaderId.id=vh.id and gl.glcodeId.glcode=?")
                            .append(
                                    " and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("' AND endingDate >='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("') and vh.voucherDate <='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("'and vh.status not in (")
                                    .append(statusExclude)
                                    .append(
                                            ") and ((vh.name='Contractor Journal' and state_id is null) or(vh.name !='Contractor Journal' and vh.name !='CapitalisedAsset' ) )");

            final List<Object> list = getPersistenceService()
                    .findAllBy(glCodeBalQry.toString(), glcode);
            glCodeBalance = BigDecimal.valueOf((Double) list.get(0));
        } else {
            // Getting the debit balance.
            glCodeDbtBalQry
            .append(
                    "SELECT sum(gld.amount)  as debitamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
                    .append(
                            " WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=?  ")
                            .append(
                                    " and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("' AND endingDate >='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("') and vh.voucherDate <='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("'and vh.status not in (")
                                    .append(statusExclude)
                                    .append(
                                            ")and ((vh.name='Contractor Journal' and state_id is null) or(vh.name !='Contractor Journal' and vh.name !='CapitalisedAsset') ) ")
                                            .append(" and gld.detailTypeId.id =")
                                            .append(accountdetailType);
            if (null != accountdetailkey)
                glCodeDbtBalQry.append(" and gld.detailKeyId in (").append(
                        accountdetailkey).append(")");
            glCodeDbtBalQry.append(" and gl.debitAmount >0");
            final List<Object> listDbt = getPersistenceService()
                    .findAllBy(glCodeDbtBalQry.toString(), glcode);
            subledgerDbtBalance = (BigDecimal) listDbt.get(0) == null ? BigDecimal.ZERO
                    : (BigDecimal) listDbt.get(0);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total debit amount :  " + subledgerDbtBalance);

            // get the credit balance

            glCodeCrdBalQry
            .append(
                    "SELECT sum(gld.amount) as creditamount from CVoucherHeader vh , CGeneralLedger gl,CGeneralLedgerDetail gld")
                    .append(
                            " WHERE gl.voucherHeaderId.id=vh.id and gl.id = gld.generalLedgerId.id and gl.glcodeId.glcode=?  ")
                            .append(
                                    " and vh.voucherDate >= (select startingDate from CFinancialYear where  startingDate <= '")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("' AND endingDate >='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("') and vh.voucherDate <='")
                                    .append(Constants.DDMMYYYYFORMAT1.format(asondate))
                                    .append("'and vh.status not in (")
                                    .append(statusExclude)
                                    .append(
                                            ")and ((vh.name='Contractor Journal' and state_id is null) or(vh.name !='Contractor Journal' and vh.name !='CapitalisedAsset' ) )")
                                            .append(" and gld.detailTypeId.id =")
                                            .append(accountdetailType);
            if (null != accountdetailkey)
                glCodeCrdBalQry.append(" and gld.detailKeyId in(").append(
                        accountdetailkey).append(")");
            glCodeCrdBalQry.append(" and gl.creditAmount >0");
            final List<Object> listCrd = getPersistenceService()
                    .findAllBy(glCodeCrdBalQry.toString(), glcode);
            subledgerCrdBalance = (BigDecimal) listCrd.get(0) == null ? BigDecimal.ZERO
                    : (BigDecimal) listCrd.get(0);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total credit amount :  " + subledgerCrdBalance);
            glCodeBalance = subledgerDbtBalance.subtract(subledgerCrdBalance);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" total balance amount :  " + glCodeBalance);

        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | getAccCodeBalanceForIndirectExpense | End");
        glCodeBalance = glCodeBalance.setScale(2);
        return glCodeBalance;
    }

    private void validateParameterData(final Date asondate, final String glcode,
            final Integer accountdetailType, final String accountdetailkey) {

        if (null == asondate)
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "asondate", "asondate supplied is null")));

        if (null == glcode || StringUtils.isEmpty(glcode))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "glcode", "glcode supplied is either null or empty")));
        else if (null == chartOfAccountsDAO.getCChartOfAccountsByGlCode(glcode))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "glcode", "not a valid glcode :" + glcode)));

        if (null != accountdetailType) {
            final Session session = persistenceService.getSession();
            final Query qry = session
                    .createQuery("from CChartOfAccountDetail cd,CChartOfAccounts c where "
                            + "cd.glCodeId = c.id and c.glcode=:glcode and cd.detailTypeId=:detailTypeId");
            qry.setString(VoucherConstant.GLCODE, glcode);
            qry.setString("detailTypeId", accountdetailType.toString());

            if (null == qry.list() || qry.list().size() == 0)
                throw new ValidationException(
                        Arrays
                        .asList(new ValidationError(
                                "accountdetailType",
                                "Glcode "
                                        + glcode
                                        + " is not a control code for the supplied detailed type.")));

        }
        if (null != accountdetailkey) {
            final Session session = persistenceService.getSession();
            final Query qry = session
                    .createQuery("from Accountdetailkey adk where adk.accountdetailtype=:detailtypeid and adk.detailkey=:detailkey");
            qry.setString(VoucherConstant.DETAILTYPEID, accountdetailType
                    .toString());
            qry.setString("detailkey", accountdetailkey.toString());

            if (null == qry.list() || qry.list().size() == 0)
                throw new ValidationException(
                        Arrays
                        .asList(new ValidationError(
                                "accountdetailkey",
                                "The accountdetailkey supplied : "
                                        + accountdetailkey
                                        + " for the accountdetailType : "
                                        + accountdetailType
                                        + " is not correct")));
        }
    }

    /**
     * return the AccountCodePurpose object if name matches else returns null throws ApplicationRuntimeException if name is null
     * or empty
     *
     * @param name
     * @return AccountCodePurpose
     */
    public AccountCodePurpose getAccountCodePurposeByName(final String name) {
        if (name == null || name.isEmpty())
            throw new ApplicationRuntimeException("Name is Null Or Empty");

        return (AccountCodePurpose) persistenceService.find(
                "from AccountCodePurpose where upper(name)=upper(?)", name);
    }

    /**
     * @description -This method returns the number of payments, the total payment amount made as on a particular date for a list
     * of ProjectCode ids that is passed. NOTE - ASSUMPTION IS EJVs don't have partial payments and CJVs have only 1 project code
     * on debit side.
     * @param entityList - Integer list containing ProjectCode ids.
     * @param asOnDate - The payments are considered from the beginning to asOnDate (including asOnDate)
     * @return -A Map containing the total count and total amount. keys are 'count' , 'amount'
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode ids list passed is empty. - If any id
     * passed is wrong.
     */
    public Map<String, BigDecimal> getPaymentInfoforProjectCode(final List<Long> projectCodeIdList, final Date asOnDate)
            throws ApplicationException {
        if (projectCodeIdList == null || projectCodeIdList.size() == 0)
            throw new ApplicationException("ProjectCode Id list is null or empty");
        if (asOnDate == null)
            throw new ApplicationException("asOnDate is null");
        final String strAsOnDate = Constants.DDMMYYYYFORMAT1.format(asOnDate);
        final Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        final List<String> commaSeperatedEntitiesList = new ArrayList<String>();
        final List<List<Long>> limitedEntityList = new ArrayList<List<Long>>();
        String commaSeperatedEntities = "";
        List<Long> tempEntityIdList = new ArrayList<Long>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Size of entityIdList-" + projectCodeIdList.size()
                    + " asOnDate - " + asOnDate);
        Long entityId;
        // In sql query, if in list contains more than 1000 elements, it may
        // fail.
        // Hence, we start splitting the list passed into smaller lists of sizes
        // less than 1000.
        for (int i = 0; i < projectCodeIdList.size(); i++) {
            entityId = projectCodeIdList.get(i);
            commaSeperatedEntities = commaSeperatedEntities + entityId + ",";
            tempEntityIdList.add(entityId);
            if (i != 0 && i % 998 == 0 || i == projectCodeIdList.size() - 1) {
                commaSeperatedEntitiesList.add(commaSeperatedEntities.substring(0, commaSeperatedEntities.length() - 1));
                limitedEntityList.add(tempEntityIdList);
                commaSeperatedEntities = "";
                tempEntityIdList = new ArrayList<Long>();
            }
        }

        final String validationQuery = "SELECT detailkey FROM accountdetailkey WHERE detailtypeid= (SELECT id FROM accountdetailtype "
                + "WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and detailkey in (";
        List<BigDecimal> dbEntIdList = new ArrayList<BigDecimal>();
        boolean isPresent;
        final List<Long> incorrectEntityIds = new ArrayList<Long>();
        String dbEntIdQuery;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Validation Starts ");
        for (int i = 0; i < commaSeperatedEntitiesList.size(); i++) {
            isPresent = false;
            dbEntIdQuery = validationQuery + commaSeperatedEntitiesList.get(i) + " ) order by detailkey ";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(i + ":dbEntIdQuery- " + dbEntIdQuery);
            dbEntIdList = persistenceService.getSession().createSQLQuery(dbEntIdQuery).list();
            if (dbEntIdList != null && dbEntIdList.size() != limitedEntityList.get(i).size())
                for (final Long entId : limitedEntityList.get(i)) {
                    isPresent = false;
                    for (final BigDecimal dbEntId : dbEntIdList)
                        if (dbEntId.longValue() == entId.longValue()) {
                            isPresent = true;
                            break;
                        }
                    if (!isPresent)
                        incorrectEntityIds.add(entId);
                }
        }
        if (incorrectEntityIds.size() != 0)
            throw new ApplicationException("Incorrect detail key Ids - " + incorrectEntityIds);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Validation Succeded ");
        String qryForExpense = "";
        String qryForNonExpense = "";
        BigDecimal totalExpensePaymentAmount = BigDecimal.ZERO;
        BigDecimal totalExpensePaymentCount = BigDecimal.ZERO;
        BigDecimal totalNonExpensePaymentAmount = BigDecimal.ZERO;
        BigDecimal totalNonExpensePaymentCount = BigDecimal.ZERO;
        List<Object[]> objForExpense;
        List<Object[]> objForNonExpense;
        BigDecimal tempAmountObj = BigDecimal.ZERO;
        BigDecimal tempCountObj = BigDecimal.ZERO;
        for (int i = 0; i < commaSeperatedEntitiesList.size(); i++) {
            qryForExpense = getPaymentInfoQuery(commaSeperatedEntitiesList.get(i), strAsOnDate, true);
            qryForNonExpense = getPaymentInfoQuery(commaSeperatedEntitiesList.get(i), strAsOnDate, false);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(i + ": qryForExpense- " + qryForExpense);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(i + ": qryForNonExpense- " + qryForNonExpense);
            objForExpense = persistenceService.getSession().createSQLQuery(qryForExpense).list();
            objForNonExpense = persistenceService.getSession().createSQLQuery(qryForNonExpense).list();
            if (objForExpense != null && objForExpense.size() != 0) {
                tempAmountObj = new BigDecimal(objForExpense.get(0)[0].toString());
                tempCountObj = new BigDecimal(objForExpense.get(0)[1].toString());
                totalExpensePaymentAmount = totalExpensePaymentAmount.add(tempAmountObj);
                totalExpensePaymentCount = totalExpensePaymentCount
                        .add(tempCountObj);
            }
            if (objForNonExpense != null && objForNonExpense.size() != 0) {
                tempAmountObj = new BigDecimal(objForNonExpense.get(0)[0]
                        .toString());
                tempCountObj = new BigDecimal(objForNonExpense.get(0)[1]
                        .toString());
                totalNonExpensePaymentAmount = totalNonExpensePaymentAmount
                        .add(tempAmountObj);
                totalNonExpensePaymentCount = totalNonExpensePaymentCount
                        .add(tempCountObj);
            }
        }
        result.put("count", totalExpensePaymentCount
                .add(totalNonExpensePaymentCount));
        result.put("amount", totalExpensePaymentAmount
                .add(totalNonExpensePaymentAmount));
        return result;
    }

    /**
     * @description -This method returns the total payment amount and payment count made till date for a list of Project codes
     * that is passed.
     * @param entityList - Integer list containing ProjectCode ids.
     * @return - Map of total amount of approved payments and count made for all the bills made against the project codes send.
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public Map<String, BigDecimal> getTotalPaymentforProjectCode(final List<Long> projectCodeIdList) throws ApplicationException {
        if (projectCodeIdList == null || projectCodeIdList.size() == 0)
            throw new ApplicationException("ProjectCode Id list is null or empty");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Size of entityIdList-" + projectCodeIdList.size());

        final Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        final List<String> commaSeperatedEntitiesList = new ArrayList<String>();
        final List<List<Long>> limitedEntityList = new ArrayList<List<Long>>();
        String commaSeperatedEntities = "";
        List<Long> tempEntityIdList = new ArrayList<Long>();
        Long entityId;
        String projectCodeListCondition = "";
        List<Object[]> objForExpense;
        BigDecimal totalPaymentAmount = BigDecimal.ZERO;
        BigDecimal totalCount = BigDecimal.ZERO;
        // In sql query, if in list contains more than 1000 elements, it may fail.
        // Hence, we start splitting the list passed into smaller lists of sizes
        // less than 1000.
        for (int i = 0; i < projectCodeIdList.size(); i++) {
            entityId = projectCodeIdList.get(i);
            commaSeperatedEntities = commaSeperatedEntities + entityId + ",";
            tempEntityIdList.add(entityId);
            if (i != 0 && i % 998 == 0 || i == projectCodeIdList.size() - 1) {
                commaSeperatedEntitiesList.add(commaSeperatedEntities.substring(0, commaSeperatedEntities.length() - 1));
                limitedEntityList.add(tempEntityIdList);
                commaSeperatedEntities = "";
                tempEntityIdList = new ArrayList<Long>();
            }
        }

        // Framing the query condition for the project code list
        for (int i = 0; i < commaSeperatedEntitiesList.size(); i++) {
            final String stringIdsList = commaSeperatedEntitiesList.get(i);
            if (i != 0)
                projectCodeListCondition = projectCodeListCondition + ") or bp.ACCOUNTDETAILKEYID in (" + stringIdsList;
            else
                projectCodeListCondition = stringIdsList;
        }
        final String payQuery = "SELECT nvl(sum(mb.paidamount),0),count(vh1.id) FROM    miscbilldetail mb,voucherheader vh1 "
                + "WHERE vh1.id=mb.PAYVHID and vh1.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and mb.BILLVHID in( select vh.id FROM "
                + "eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms "
                + "WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id "
                + "and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + "moduletype in('SALBILL','EXPENSEBILL','SBILL','CONTRACTORBILL','CBILL')) "
                + "and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + "WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in("
                + projectCodeListCondition + ")))";

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Final payQuery - " + payQuery);

        objForExpense = persistenceService.getSession().createSQLQuery(payQuery).list();
        if (objForExpense != null && objForExpense.size() != 0) {
            totalPaymentAmount = new BigDecimal(objForExpense.get(0)[0].toString());
            totalCount = new BigDecimal(objForExpense.get(0)[1].toString());
        }

        result.put("count", totalCount);
        result.put("amount", totalPaymentAmount);
        return result;
    }

    /**
     * @description -This method returns the number of payments, the total payment amount made, department wise as on a particular
     * date for a list of ProjectCode ids that is passed. NOTE - ASSUMPTION IS EJVs don't have partial payments and CJVs have only
     * 1 project code on debit side.
     * @param entityList - Integer list containing ProjectCode ids.
     * @param asOnDate - The payments are considered from the beginning to asOnDate. Only fully approved payments are considered.
     * (including asOnDate)
     * @return -A Map containing the total count and total amount department wise. keys are 'count' , 'amount', 'department'
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode ids list passed is empty. - If any id
     * passed is wrong.
     */
    public Map<String, String> getPaymentInfoforProjectCodeByDepartment(final List<Long> projectCodeIdList, final Date asOnDate)
            throws ApplicationException {
        if (projectCodeIdList == null || projectCodeIdList.size() == 0)
            throw new ApplicationException("ProjectCode Id list is null or empty");
        if (asOnDate == null)
            throw new ApplicationException("asOnDate is null");
        final String strAsOnDate = Constants.DDMMYYYYFORMAT1.format(asOnDate);
        final Map<String, String> result = new HashMap<String, String>();

        final List<String> commaSeperatedEntitiesList = new ArrayList<String>();
        final List<List<Long>> limitedEntityList = new ArrayList<List<Long>>();
        String commaSeperatedEntities = "";
        List<Long> tempEntityIdList = new ArrayList<Long>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Size of entityIdList-" + projectCodeIdList.size()
                    + " asOnDate - " + asOnDate);
        Long entityId;
        // In sql query, if in list contains more than 1000 elements, it may
        // fail.Hence, we start splitting the list passed into smaller lists of sizes less than 1000.
        for (int i = 0; i < projectCodeIdList.size(); i++) {
            entityId = projectCodeIdList.get(i);
            commaSeperatedEntities = commaSeperatedEntities + entityId + ",";
            tempEntityIdList.add(entityId);
            if (i != 0 && i % 998 == 0 || i == projectCodeIdList.size() - 1) {
                commaSeperatedEntitiesList.add(commaSeperatedEntities.substring(0, commaSeperatedEntities.length() - 1));
                limitedEntityList.add(tempEntityIdList);
                commaSeperatedEntities = "";
                tempEntityIdList = new ArrayList<Long>();
            }
        }

        /*
         * String validationQuery =
         * "SELECT detailkey FROM accountdetailkey WHERE detailtypeid= (SELECT id FROM accountdetailtype " +
         * "WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and ( detailkey in ("; List<BigDecimal> dbEntIdList = new
         * ArrayList<BigDecimal>(); boolean isPresent; List<Long> incorrectEntityIds = new ArrayList<Long>(); String
         * dbEntIdQuery=""; if(LOGGER.isDebugEnabled()) LOGGER.debug(" Validation Starts "); for (int i = 0; i <
         * commaSeperatedEntitiesList.size(); i++) { isPresent = false; dbEntIdQuery = validationQuery +
         * commaSeperatedEntitiesList.get(i); if(i!=0) dbEntIdQuery=dbEntIdQuery+ ") or detailkey in(";
         * if(LOGGER.isDebugEnabled()) LOGGER.debug(i + ":dbEntIdQuery- " + dbEntIdQuery);
         * if(i==commaSeperatedEntitiesList.size()-1) dbEntIdQuery=dbEntIdQuery+ ")) order by detailkey "; }
         * if(LOGGER.isDebugEnabled()) LOGGER.debug("Final Query- " + dbEntIdQuery); dbEntIdList = (List<BigDecimal>)
         * persistenceService.getSession().createSQLQuery(dbEntIdQuery).list(); if (dbEntIdList != null && dbEntIdList.size() !=
         * limitedEntityList.size()) { for (int i = 0; i < commaSeperatedEntitiesList.size(); i++) { for (Long entId :
         * limitedEntityList.get(i)) { isPresent = false; for (BigDecimal dbEntId : dbEntIdList) { if (dbEntId.longValue() ==
         * entId.longValue()) { isPresent = true; break; } } if (!isPresent) { incorrectEntityIds.add(entId); } } } } if
         * (incorrectEntityIds.size() != 0) throw new ApplicationException("Incorrect detail key Ids - "+ incorrectEntityIds);
         * if(LOGGER.isDebugEnabled()) LOGGER.debug(" Validation Succeded in method..");
         */
        String qryForExpense = "";
        String qryForNonExpense = "";
        final BigDecimal totalExpensePaymentAmount = BigDecimal.ZERO;
        String deptName = null;
        List<Object[]> objForExpense;
        List<Object[]> objForNonExpense;
        BigDecimal tempAmountObj = BigDecimal.ZERO;

        boolean ifDeptExist = false;
        for (int i = 0; i < commaSeperatedEntitiesList.size(); i++) {
            qryForExpense = getPaymentAmountByDept(commaSeperatedEntitiesList.get(i), strAsOnDate, true);
            qryForNonExpense = getPaymentAmountByDept(commaSeperatedEntitiesList.get(i), strAsOnDate, false);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(i + ": qryForExpense- " + qryForExpense);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(i + ": qryForNonExpense- " + qryForNonExpense);
            objForExpense = persistenceService.getSession().createSQLQuery(qryForExpense).list();
            objForNonExpense = persistenceService.getSession().createSQLQuery(qryForNonExpense).list();
            if (objForExpense != null && objForExpense.size() != 0) {
                tempAmountObj = new BigDecimal(objForExpense.get(0)[0].toString());
                deptName = objForExpense.get(0)[1].toString();
                ifDeptExist = result.containsValue(deptName);
                if (ifDeptExist) {
                    result.put(deptName, totalExpensePaymentAmount.add(tempAmountObj).toString());
                    result.put("departmentname", deptName);
                }
                else {
                    result.put(deptName, tempAmountObj.toString());
                    result.put("departmentname", deptName);
                }
            }
            ifDeptExist = false;
            if (objForNonExpense != null && objForNonExpense.size() != 0) {
                tempAmountObj = new BigDecimal(objForNonExpense.get(0)[0]
                        .toString());
                deptName = objForNonExpense.get(0)[1].toString();
                ifDeptExist = result.containsValue(deptName);
                if (ifDeptExist) {
                    result.put(deptName, totalExpensePaymentAmount.add(tempAmountObj).toString());
                    result.put("departmentname", deptName);
                }
                else {
                    result.put(deptName, tempAmountObj.toString());
                    result.put("departmentname", deptName);
                }

            }
        }
        return result;
    }

    /**
     * This method returns the total payment amount of all uncancelled payments for a particular billId.
     *
     * @param billId - this is the EgBillregister id.
     * @return - 0 is returned if no payments are made for the bill.
     * @throws ApplicationException - If parameter passed is null. - billId passed is incorrect. - Bill is cancelled.
     */
    public BigDecimal getPaymentAmount(final Long billId) throws ApplicationException {
        if (billId == null)
            throw new ApplicationException("Parameter passed is null.");
        final EgBillregister billRegister = (EgBillregister) persistenceService.getSession().load(EgBillregister.class, billId);
        if (billRegister == null)
            throw new ApplicationException("Incorrect billId - " + billId);
        final EgwStatus billStatus = billRegister.getStatus();
        if (billStatus.getDescription().equalsIgnoreCase("Cancelled"))
            throw new ApplicationException("Bill with id - " + billId
                    + " is cancelled.");
        final String sqlQuery = "SELECT COALESCE(sum(misc.paidamount),0) FROM eg_billregister br, eg_billregistermis bmis, voucherheader bvh, "
                + " miscbilldetail misc, voucherheader pvh WHERE br.id="
                + billRegister.getId()
                + " and br.id=bmis.billid "
                + " AND bmis.voucherheaderid=bvh.id AND bvh.id= misc.billvhid and pvh.id= misc.payvhid "
                + " and pvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " ";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("sqlQuery- " + sqlQuery);
        final List<BigDecimal> paymentAmount = persistenceService.getSession().createSQLQuery(sqlQuery).list();
        return paymentAmount.get(0) == null ? BigDecimal.ZERO : paymentAmount
                .get(0);
    }

    private String getPaymentInfoQuery(final String stringIdsList, final String strDate,
            final boolean isExpenseType) {
        final String qryForExpense = "select nvl(sum(amt),0), count(*) from ( select sum("
                + (isExpenseType ? "gd.amount" : "m.paidamount")
                + ") as amt,"
                + " count(*), pvh.id as pvh_id from generalledger g, generalledgerdetail gd, voucherheader bvh, "
                + " miscbilldetail m, voucherheader pvh where g.id= gd.generalledgerid and g.voucherheaderid=bvh.id and m.billvhid= bvh.id"
                + " and m.payvhid= pvh.id and gd.detailtypeid=(SELECT id FROM accountdetailtype WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) "
                + " and pvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and gd.detailkeyid in ("
                + stringIdsList
                + ")  "
                + " and pvh.voucherdate<='"
                + strDate
                + "' "
                + " and bvh.name"
                + (isExpenseType ? "=" : "!=")
                + "'"
                + FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL
                + "' group by pvh.id) ";
        return qryForExpense;
    }

    private String getPaymentAmountByDept(final String stringIdsList, final String strDate,
            final boolean isExpenseType) {
        final String qryForExpense = "select sum("
                + (isExpenseType ? "gd.amount" : "m.paidamount")
                + ") as amt,"
                + " dept.dept_name as dept_name from generalledger g, generalledgerdetail gd,"
                + "voucherheader bvh,vouchermis mis,eg_department dept,  "
                + " miscbilldetail m, voucherheader pvh where g.id= gd.generalledgerid and g.voucherheaderid=bvh.id "
                + " and m.billvhid= bvh.id and mis.voucherheaderid=pvh.id and dept.id_dept=mis.departmentid "
                + " and m.payvhid= pvh.id and gd.detailtypeid=(SELECT id FROM accountdetailtype WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) "
                + " and pvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and gd.detailkeyid in ("
                + stringIdsList
                + ")  "
                + " and pvh.voucherdate<='"
                + strDate
                + "' "
                + " and bvh.name"
                + (isExpenseType ? "=" : "!=")
                + "'"
                + FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL
                + "' group by dept.dept_name ";
        return qryForExpense;
    }

    /**
     * This API returns List of Map, Map containing Amount, VoucherNumber, VoucherDate, BillNumber & BillId of passed projectcode
     * and till passed date. Vouchers selected accross funds and financial year.
     *
     * @author chetan
     * @param projectCodeId
     * @param asOnDate
     * @return List<Map<String, String>>
     * @throws ApplicationRuntimeException
     */
    public List<Map<String, String>> getExpenditureDetailsforProject(
            final Long projectCodeId, final Date asOnDate) throws ApplicationRuntimeException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getExpenditureDetailsforProject .....");
        if (projectCodeId.equals(Long.valueOf(0)))
            throw new ApplicationRuntimeException("ProjectCode is null or empty");
        if (asOnDate == null || asOnDate.equals(null))
            throw new ApplicationRuntimeException("asOnDate is null");
        final Accountdetailkey adk = (Accountdetailkey) persistenceService
                .find(
                        "FROM Accountdetailkey where accountdetailtype.name='PROJECTCODE' and detailkey=?",
                        projectCodeId.intValue());
        if (adk == null || adk.equals(null))
            throw new ApplicationRuntimeException("There is no project code");

        final List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        final String queryForGLList = "SELECT gld.amount, vh.id, vh.voucherNumber, vh.voucherDate, egmis.billid, egbill.billnumber "
                + "FROM generalledger gl, generalledgerdetail gld, accountdetailkey adk, accountdetailtype adt, voucherheader vh left outer join eg_billregistermis egmis on vh.id=egmis.voucherheaderid left outer join eg_billregister egbill on egmis.billid=egbill.id "
                + "WHERE gl.id           = gld.generalledgerid AND gl.voucherheaderid= vh.id AND gld.detailtypeid  = adt.id "
                + "AND gld.detailkeyid   = adk.detailkey AND adt.name          ='PROJECTCODE' AND adk.detailtypeid  = adt.id "
                + "AND adk.detailkey     ="
                + projectCodeId
                + " AND gl.debitamount<>0 AND vh.voucherdate<='"
                + Constants.DDMMYYYYFORMAT1.format(asOnDate)
                + "' AND vh.status=0";

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("queryForGLList >> " + queryForGLList);
        final List<Object[]> generalLedgerList = persistenceService.getSession()
                .createSQLQuery(queryForGLList).list();
        for (final Object[] objects : generalLedgerList) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Project code has vouchers.");
            final Map<String, String> mp = new HashMap<String, String>();
            if (objects[0].toString() != null)
                mp.put("Amount", objects[0].toString());
            if (objects[2] != null && objects[3] != null) {
                mp.put("VoucherNumber", objects[2].toString());
                mp.put("VoucherDate", Constants.DDMMYYYYFORMAT2
                        .format(objects[3]));
            }
            if (objects[4] != null && objects[5] != null) {
                mp.put("BillNumber", objects[5].toString());
                mp.put("BillId", objects[4].toString());
            }
            result.add(mp);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getExpenditureDetailsforProject.");
        return result;
    }

    /**
     * This API returns List of Map, Map containing Amount, VoucherNumber, VoucherDate, BillNumber & BillId of passed Depositcode
     * and till passed date. Vouchers selected accross funds and financial year.
     *
     * @author chetan
     * @param depositCodeId
     * @param asOnDate
     * @return List<Map<String, String>>
     * @throws ApplicationRuntimeException
     */
    public List<Map<String, String>> getExpenditureDetailsforDepositCode(
            final Long depositCodeId, final Date asOnDate) throws ApplicationRuntimeException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getExpenditureDetailsforDepositCode .....");
        if (depositCodeId.equals(Long.valueOf(0)))
            throw new ApplicationRuntimeException("DepositCode is null or empty");
        if (asOnDate == null || asOnDate.equals(null))
            throw new ApplicationRuntimeException("asOnDate is null");
        final Accountdetailkey adk = (Accountdetailkey) persistenceService
                .find(
                        "FROM Accountdetailkey where accountdetailtype.name='DEPOSITCODE' and detailkey=?",
                        depositCodeId.intValue());
        if (adk == null || adk.equals(null))
            throw new ApplicationRuntimeException("There is no such Deposit code");

        final List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        final String queryForGLList = "SELECT gld.amount, vh.id, vh.voucherNumber, vh.voucherDate, egmis.billid, egbill.billnumber "
                + "FROM generalledger gl, generalledgerdetail gld, accountdetailkey adk, accountdetailtype adt, voucherheader vh left outer join eg_billregistermis egmis on vh.id=egmis.voucherheaderid left outer join eg_billregister egbill on egmis.billid=egbill.id "
                + "WHERE gl.id           = gld.generalledgerid AND gl.voucherheaderid= vh.id AND gld.detailtypeid  = adt.id "
                + "AND gld.detailkeyid   = adk.detailkey AND adt.name          ='DEPOSITCODE' AND adk.detailtypeid  = adt.id "
                + "AND adk.detailkey     ="
                + depositCodeId
                + " AND gl.debitamount<>0 AND vh.voucherdate<='"
                + Constants.DDMMYYYYFORMAT1.format(asOnDate)
                + "' AND vh.status=0";

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("queryForGLList >> " + queryForGLList);
        final List<Object[]> generalLedgerList = persistenceService.getSession()
                .createSQLQuery(queryForGLList).list();
        for (final Object[] objects : generalLedgerList) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Deposit code has vouchers.");
            final Map<String, String> mp = new HashMap<String, String>();
            if (objects[0].toString() != null)
                mp.put("Amount", objects[0].toString());
            if (objects[2] != null && objects[3] != null) {
                mp.put("VoucherNumber", objects[2].toString());
                mp.put("VoucherDate", Constants.DDMMYYYYFORMAT2
                        .format(objects[3]));
            }
            if (objects[4] != null && objects[5] != null) {
                mp.put("BillNumber", objects[5].toString());
                mp.put("BillId", objects[4].toString());
            }
            result.add(mp);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getExpenditureDetailsforDepositCode.");
        return result;
    }

    /**
     * This API returns List of Map, Map containing Amount, VoucherNumber, VoucherDate, BillNumber & BillId of passed projectcode
     * and. Vouchers selected accross funds. Vouchers selected within asOnDate Financial Year.
     *
     * @author chetan
     * @param projectCodeId
     * @param asOnDate
     * @return
     * @throws ApplicationRuntimeException
     */
    public List<Map<String, String>> getExpenditureDetailsforProjectforFinYear(
            final Long projectCodeId, final Date asOnDate) throws ApplicationRuntimeException {
        LOGGER
        .debug("Starting getExpenditureDetailsforProjectforFinYear .....");
        if (projectCodeId.equals(Long.valueOf(0)))
            throw new ApplicationRuntimeException("ProjectCode is null or empty");
        if (asOnDate == null || asOnDate.equals(null))
            throw new ApplicationRuntimeException("asOnDate is null");
        final Accountdetailkey adk = (Accountdetailkey) persistenceService
                .find(
                        "FROM Accountdetailkey where accountdetailtype.name='PROJECTCODE' and detailkey=?",
                        projectCodeId.intValue());
        if (adk == null || adk.equals(null))
            throw new ApplicationRuntimeException("There is no project code");

        final CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(asOnDate);
        final Date startDate = finYear.getStartingDate();

        final List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        final String queryForGLList = "SELECT gld.amount, vh.id, vh.voucherNumber, vh.voucherDate, egmis.billid, egbill.billnumber "
                + "FROM generalledger gl, generalledgerdetail gld, accountdetailkey adk, accountdetailtype adt, voucherheader vh left outer join eg_billregistermis egmis on vh.id=egmis.voucherheaderid left outer join eg_billregister egbill on egmis.billid=egbill.id "
                + "WHERE gl.id           = gld.generalledgerid AND gl.voucherheaderid= vh.id AND gld.detailtypeid  = adt.id "
                + "AND gld.detailkeyid   = adk.detailkey AND adt.name          ='PROJECTCODE' AND adk.detailtypeid  = adt.id "
                + "AND adk.detailkey     ="
                + projectCodeId
                + " AND gl.debitamount<>0 AND vh.voucherdate>='"
                + Constants.DDMMYYYYFORMAT1.format(startDate)
                + "' AND vh.voucherdate<='"
                + Constants.DDMMYYYYFORMAT1.format(asOnDate)
                + "' AND vh.status=0";

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("queryForGLList >> " + queryForGLList);
        final List<Object[]> generalLedgerList = persistenceService.getSession()
                .createSQLQuery(queryForGLList).list();
        for (final Object[] objects : generalLedgerList) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Project code has vouchers.");
            final Map<String, String> mp = new HashMap<String, String>();
            if (objects[0].toString() != null)
                mp.put("Amount", objects[0].toString());
            if (objects[2] != null && objects[3] != null) {
                mp.put("VoucherNumber", objects[2].toString());
                mp.put("VoucherDate", Constants.DDMMYYYYFORMAT2
                        .format(objects[3]));
            }
            if (objects[4] != null && objects[5] != null) {
                mp.put("BillNumber", objects[5].toString());
                mp.put("BillId", objects[4].toString());
            }
            result.add(mp);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getExpenditureDetailsforProjectforFinYear.");
        return result;
    }

    /**
     * finds the sum of debit amount of all active Journal vouchers ie is VoucherType is 'Journal Voucher' for the provided list
     * of detailkeyIds. Vouchers selected across funds and financial year.
     *
     * @author mani
     * @param detailTypeId
     * @param entityIdList having list of id is in Integer type(AccountKey List)
     * @return voucherSum
     *
     */

    public BigDecimal getVoucherExpenditureByEntities(final Integer detailTypeId, final List<Integer> entityIdList)
    {
        BigDecimal voucherSum = BigDecimal.ZERO;
        if (detailTypeId == null || entityIdList == null || entityIdList.size() == 0)
            throw new ValidationException("DetailTypeId or EntityIdList not provided",
                    "DetailTypeId or EntityIdList not provided");
        final String query = "select sum(gld.amount) from CGeneralLedger gl, CGeneralLedgerDetail gld, CVoucherHeader vh "
                + " WHERE gl.voucherHeaderId= vh and gl.id = gld.generalLedgerId.id and  gld.detailTypeId.id  in ( :detailTypeId ) and"
                + " gld.detailKeyId   in ( :entityIdList ) and gl.debitAmount>0 and vh.status!=4 and vh.type = 'Journal Voucher'";

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query For getVoucherExpenditureByEntities >> " + query);

        final Query expenditureQuery = persistenceService.getSession().createQuery(query);

        expenditureQuery.setInteger("detailTypeId", detailTypeId);
        expenditureQuery.setParameterList("entityIdList", entityIdList);
        final List<Object> result = expenditureQuery.list();
        if (result != null)
            voucherSum = getBigDecimalValue(result.get(0));
        return voucherSum;
    }

    /**
     * Finds the sum of debit amount of all approved Direct Bank Payments vouchers ie is VoucherType is 'Payment' and voucher name
     * is 'Direct Bank Payment' for the provided list of detailkeyIds. Vouchers selected across funds and financial year.
     *
     * @author mani
     * @param detailTypeId
     * @param entityIdList having list of id is in Integer type(AccountKey List)
     * @return voucherSum
     *
     */

    public BigDecimal getDirectBankPaymentExpenditureByEntities(final Integer detailTypeId, final List<Integer> entityIdList)
    {
        if (detailTypeId == null || entityIdList == null || entityIdList.size() == 0)
            throw new ValidationException("DetailTypeId or EntityIdList not provided",
                    "DetailTypeId or EntityIdList not provided");
        BigDecimal dbpSum = BigDecimal.ZERO;
        final String query = "select sum(gld.amount) from CGeneralLedger gl, CGeneralLedgerDetail gld, CVoucherHeader vh "
                + " WHERE gl.voucherHeaderId= vh and gl.id = gld.generalLedgerId.id and  gld.detailTypeId.id  in ( :detailTypeId ) and"
                + " gld.detailKeyId   in ( :entityIdList ) and gl.debitAmount>0 and vh.status!=4 and vh.name = 'Direct Bank Payment'";

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query For getDirectBankPaymentExpenditureByEntities >> " + query);

        final Query expenditureQuery = persistenceService.getSession().createQuery(query);

        expenditureQuery.setInteger("detailTypeId", detailTypeId);
        expenditureQuery.setParameterList("entityIdList", entityIdList);
        final List<Object> result = expenditureQuery.list();
        if (result != null)
            dbpSum = getBigDecimalValue(result.get(0));
        return dbpSum;
    }

    private BigDecimal getBigDecimalValue(final Object object) {
        return object != null ? new BigDecimal(object.toString()) : BigDecimal.ZERO;
    }
}