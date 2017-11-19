/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.egf.web.actions.payment;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.service.FunctionService;
import org.egov.commons.utils.BankAccountType;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.payment.PaymentBean;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.WorkflowBean;
import org.egov.payment.services.PaymentActionHelper;
import org.egov.services.payment.PaymentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")
@Validation
@Results({ @Result(name = "search", location = "payment-search.jsp"),
        @Result(name = "searchbills", location = "payment-searchbills.jsp"),
        @Result(name = "tnebSearch", location = "payment-tnebSearch.jsp"),
        @Result(name = "balance", location = "payment-balance.jsp"),
        @Result(name = "modify", location = "payment-modify.jsp"),
        @Result(name = "form", location = "payment-form.jsp"), @Result(name = "view", location = "payment-view.jsp"),
        @Result(name = "list", location = "payment-list.jsp") })
public class PaymentAction extends BasePaymentAction {
    private final static String FORWARD = "Forward";
    private static final long serialVersionUID = 1L;
    private String expType, fromDate, toDate, mode, voucherdate, paymentMode, contractorIds = "", supplierIds = "",
            vouchernumber, voucherNumberPrefix = "", voucherNumberSuffix = "";
    private Long functionSel;
    private String contingentIds = "";
    private String salaryIds = "";
    private String pensionIds = "";
    private int miscount = 0;
    private boolean isDepartmentDefault;
    private BigDecimal balance;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    private Paymentheader paymentheader = new Paymentheader();
    @Qualifier("paymentService")
    private @Autowired PaymentService paymentService;
    @Autowired
    private VoucherTypeForULB voucherTypeForULB;
    @Autowired
    @Qualifier("voucherService")
    private VoucherService voucherService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private FunctionService functionService;

    private Integer bankaccount, bankbranch;
    private Integer departmentId;
    private Integer defaultDept;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
    private static final Logger LOGGER = Logger.getLogger(PaymentAction.class);
    private static final String PAYMENTID = "paymentid";
    private static final String VIEW = "view";
    private static final String LIST = "list";
    private static final String MODIFY = "modify";
    private String wfitemstate;
    private String type;
    private String billNumber;
    private String typeOfAccount;
    private List<EgBillregister> contractorBillList = null;
    private List<EgBillregister> supplierBillList = null;
    private List<EgBillregister> contingentBillList = null;
    private List<EgBillregister> salaryBillList = new ArrayList<EgBillregister>();
    private List<EgBillregister> pensionBillList = new ArrayList<EgBillregister>();
    private List<EgBillregister> totalBillList = new ArrayList<EgBillregister>();
    private List<Bankaccount> bankaccountList = null;
    private List<Miscbilldetail> miscBillList = null;
    private List<PaymentBean> billList;
    private List<PaymentBean> contractorList = null;
    private List<PaymentBean> supplierList = null;
    private List<PaymentBean> contingentList = null;
    private List<PaymentBean> salaryList = new ArrayList<PaymentBean>();
    private List<PaymentBean> pensionList = new ArrayList<PaymentBean>();
    private List<InstrumentHeader> instrumentHeaderList;
    private List<Paymentheader> paymentheaderList;
    private EgBillregister billregister;
    private boolean disableExpenditureType = false;
    private boolean enablePensionType = false;
    private boolean changePartyName;
    private String newPartyname;
    private String chk = "";
    private String fundNameStr = "";
    private String functionNameStr = "";
    private String deptNameStr = "";
    private String fundSourceNameStr = "";
    private String schemeStr = "";
    private String subSchemeStr = "";
    private Map<String, String> payeeMap = new HashMap<String, String>();
    private Map<Long, BigDecimal> deductionAmtMap = new HashMap<Long, BigDecimal>();
    private Map<Long, BigDecimal> paidAmtMap = new HashMap<Long, BigDecimal>();
    private List<EgAdvanceRequisition> advanceRequisitionList = new ArrayList<EgAdvanceRequisition>();
    private CFunction cFunctionobj;
    private String rtgsDefaultMode;
    private Date rtgsModeRestrictionDateForCJV;
    // private String paymentRestrictionDateForCJV;
    private String billSubType;
    private String region;
    private String month;
    private String year;
    private String bank_branch;
    private String bank_account;
    private ScriptService scriptService;
    private Map<Integer, String> monthMap = new LinkedHashMap<Integer, String>();
    private String attributes = "";
    private FinancialYearHibernateDAO financialYearDAO;
    @Autowired
    private PaymentActionHelper paymentActionHelper;
    private String cutOffDate;
    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date;

    public PaymentAction() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("creating PaymentAction...");
        addRelatedEntity("paymentheader", Paymentheader.class);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("creating PaymentAction completed.");
    }

    @Override
    public void prepare() {

        super.prepare();
        if (fromDate == null)
            fromDate = "";
        if (toDate == null)
            toDate = "";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepare...");
        // mandatoryFields = new ArrayList<String>();

        if (parameters.containsKey("salaryType"))
            setDisableExpenditureType(true);
        if (parameters.containsKey("pensionType")) {
            setEnablePensionType(true);
            setDisableExpenditureType(true);
        }
        if (parameters.get("fundId") != null && !parameters.get("fundId")[0].equals("-1")) {
            final Fund fund = (Fund) persistenceService.find("from Fund where id=?",
                    Integer.parseInt(parameters.get("fundId")[0]));
            addDropdownData("bankbranchList",
                    persistenceService.findAllBy(
                            "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and type in ('RECEIPTS_PAYMENTS','PAYMENTS') ) and br.isactive=true order by br.bank.name asc",
                            fund));
        } else
            addDropdownData("bankbranchList", Collections.EMPTY_LIST);

        if (parameters.get("functionSel") != null && !parameters.get("functionSel")[0].equals("-1")
                && !parameters.get("functionSel")[0].equals(""))
            cFunctionobj = (CFunction) persistenceService.find("from CFunction where id=?",
                    Long.valueOf(parameters.get("functionSel")[0]));

        if (getBankbranch() != null)
            addDropdownData("bankaccountList", persistenceService
                    .findAllBy(" from Bankaccount where bankbranch.id=? and isactive=true ", getBankbranch()));
        else if (parameters.get("paymentheader.bankaccount.bankbranch.id") != null
                && !parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
            addDropdownData("bankaccountList",
                    persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=true ",
                            Integer.valueOf(parameters.get("paymentheader.bankaccount.bankbranch.id")[0])));
        else
            addDropdownData("bankaccountList", Collections.EMPTY_LIST);
        if (getBillregister() != null && getBillregister().getId() != null) {
            billregister = (EgBillregister) persistenceService.find(" from EgBillregister where id=?",
                    getBillregister().getId());
            addDropdownData("bankbranchList",
                    persistenceService.findAllBy(
                            "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive=true order by br.bank.name asc",
                            billregister.getEgBillregistermis().getFund()));
        }

        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
        addDropdownData("regionsList", VoucherHelper.TNEB_REGIONS);
        addDropdownData("financialYearsList", financialYearDAO.getAllActiveFinancialYearList());
        monthMap = DateUtils.getAllMonthsWithFullNames();
        typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS + ","
                + FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepare.");
    }

    private void loadbankBranch(final Fund fund) {
        if (typeOfAccount != null && !typeOfAccount.equals("")) {
            if (typeOfAccount.indexOf(",") != -1) {
                final String[] strArray = typeOfAccount.split(",");
                addDropdownData("bankbranchList",
                        persistenceService.findAllBy(
                                "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = true and type in (?,?) ) and br.isactive=true and br.bank.isactive = true order by br.bank.name asc",
                                fund, BankAccountType.valueOf(strArray[0]), BankAccountType.valueOf(strArray[1])));
            } else
                addDropdownData("bankbranchList",
                        persistenceService.findAllBy(
                                "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = true and type in (?) ) and br.isactive=true and br.bank.isactive = true order by br.bank.name asc",
                                fund, typeOfAccount));
        } else
            addDropdownData("bankbranchList",
                    persistenceService.findAllBy(
                            "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = true) and br.isactive=true and br.bank.isactive = true order by br.bank.name asc",
                            fund));
        String bankCode = null;

        if (billSubType != null && !billSubType.equalsIgnoreCase("")) {

            try {
                final List<AppConfigValues> configValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                        FinancialConstants.MODULE_NAME_APPCONFIG, FinancialConstants.EB_VOUCHER_PROPERTY_BANK);

                for (final AppConfigValues appConfigVal : configValues)
                    bankCode = appConfigVal.getValue();
            } catch (final Exception e) {
                throw new ApplicationRuntimeException(
                        "Appconfig value for EB Voucher propartys is not defined in the system");
            }
            addDropdownData("bankbranchList",
                    persistenceService.findAllBy(
                            "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and type in ('RECEIPTS_PAYMENTS','PAYMENTS') ) and br.isactive=true and br.bank.code = ? order by br.bank.name asc",
                            fund, bankCode));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed loadbankBranch.");
    }

    @Override
    public StateAware getModel() {
        voucherHeader = (CVoucherHeader) super.getModel();
        voucherHeader.setType("Payment");
        return voucherHeader;
    }

    @SkipValidation
    @Action(value = "/payment/payment-beforeSearch")
    public String beforeSearch() throws Exception {
        return "search";
    }

    @SkipValidation
    @Action(value = "/payment/payment-beforeTNEBSearch")
    public String beforeTNEBSearch() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeTNEBSearch...");
        setTNEBMandatoryFields();
        // if(validateUser("deptcheck"))
        voucherHeader.getVouchermis().setDepartmentid(paymentService.getAssignment().getDepartment());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed beforeSearch.");
        return "tnebSearch";
    }

    private void setTNEBMandatoryFields() {

        final List<String> propartyAppConfigKeysList = new ArrayList<String>();
        final Map<String, String> propartyAppConfigResultList = new LinkedHashMap<String, String>();
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_FUND);
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_FUNCTION);
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_DEPARTMENT);
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_BANKBRANCH);
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_BANKACCOUNT);

        for (final String key : propartyAppConfigKeysList) {
            String value = null;
            try {
                final List<AppConfigValues> configValues = appConfigValuesService
                        .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, key);

                for (final AppConfigValues appConfigVal : configValues) {
                    value = appConfigVal.getValue();
                    propartyAppConfigResultList.put(key, value);
                }
            } catch (final Exception e) {
                throw new ApplicationRuntimeException(
                        "Appconfig value for EB Voucher propartys is not defined in the system");
            }
        }
        for (final String key : propartyAppConfigResultList.keySet()) {

            if (key.equals("EB Voucher Property-Fund"))
                voucherHeader.setFundId((Fund) persistenceService.find("from Fund where code = ?",
                        propartyAppConfigResultList.get(key)));
            if (key.equals("EB Voucher Property-Function"))
                voucherHeader.getVouchermis().setFunction((CFunction) persistenceService
                        .find("from CFunction where code = ?", propartyAppConfigResultList.get(key)));
            if (key.equals("EB Voucher Property-Department"))
                voucherHeader.getVouchermis().setDepartmentid((Department) persistenceService
                        .find("from Department where deptCode = ?", propartyAppConfigResultList.get(key)));
            if (key.equals("EB Voucher Property-BankBranch"))
                bank_branch = propartyAppConfigResultList.get(key);
            if (key.equals("EB Voucher Property-BankAccount")) {
                bank_account = propartyAppConfigResultList.get(key);
                final Bankaccount ba = (Bankaccount) persistenceService.find(" from Bankaccount where accountnumber=?",
                        bank_account);
                if (ba.getId() != null)
                    bankaccount = ba.getId().intValue();
            }

        }
    }

    @SkipValidation
    @ValidationErrorPage(value = "search")
    @Action(value = "/payment/payment-search")
    public String search() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting search...");
        // Get App config value

        final StringBuffer sql = new StringBuffer();
        if (!"".equals(billNumber))
            sql.append(" and bill.billnumber = '" + billNumber + "' ");
        if (!"".equals(fromDate))
            sql.append(" and bill.billdate>='" + sdf.format(formatter.parse(fromDate)) + "' ");
        if (!"".equals(toDate))
            sql.append(" and bill.billdate<='" + sdf.format(formatter.parse(toDate)) + "'");
        if (voucherHeader.getFundId() != null)
            sql.append(" and bill.egBillregistermis.fund.id=" + voucherHeader.getFundId().getId());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            sql.append(" and bill.egBillregistermis.fundsource.id="
                    + voucherHeader.getVouchermis().getFundsource().getId());
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            sql.append(" and bill.egBillregistermis.egDepartment.id="
                    + voucherHeader.getVouchermis().getDepartmentid().getId());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and bill.egBillregistermis.scheme.id=" + voucherHeader.getVouchermis().getSchemeid().getId());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql.append(" and bill.egBillregistermis.subScheme.id="
                    + voucherHeader.getVouchermis().getSubschemeid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql.append(" and bill.egBillregistermis.functionaryid.id="
                    + voucherHeader.getVouchermis().getFunctionary().getId());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql.append(" and bill.egBillregistermis.fieldid=" + voucherHeader.getVouchermis().getDivisionid().getId());
        // function field is intruduced later as mandatory , so we getting for
        // the vocuhermis table
        if (voucherHeader.getVouchermis().getFunction() != null)
            sql.append(" and bill.egBillregistermis.function=" + voucherHeader.getVouchermis().getFunction().getId());

        EgwStatus egwStatus = null;
        /*
         * String mainquery =
         * "from EgBillregister bill where bill.egBillregistermis.voucherHeader is not null and bill.egBillregistermis.voucherHeader.status=0 "
         * +
         * " and bill.expendituretype=? and ( ( bill.passedamount >(select sum(paidamount) from Miscbilldetail where  billVoucherHeader in "
         * + " (select voucherHeader from EgBillregistermis where egBillregister.id = bill.id ) and (payVoucherHeader is null or "
         * + " payVoucherHeader.status not in (1,2,4) )    group by billVoucherHeader)) " +
         * "  or(select count(*) from Miscbilldetail where payVoucherHeader.status!=4 and billVoucherHeader in " +
         * " (select voucherHeader from EgBillregistermis where egBillregister.id = bill.id ) )=0 ) " ;
         */
        final String mainquery = "from EgBillregister bill where bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 "
                + " and bill.passedamount > (select SUM(misc.paidamount) from Miscbilldetail misc where misc.billVoucherHeader = bill.egBillregistermis.voucherHeader "
                + " and misc.payVoucherHeader.status in (0,5))";

        final String mainquery1 = "from EgBillregister bill where bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 "
                + " and bill.egBillregistermis.voucherHeader NOT IN (select misc.billVoucherHeader from Miscbilldetail misc where misc.billVoucherHeader is not null and misc.payVoucherHeader.status <> 4)";

        if (disableExpenditureType == true && enablePensionType == false
                || expType != null && !expType.equals("-1") && expType.equals("Salary"))
            return salaryBills(sql, mainquery, mainquery1);
        if (disableExpenditureType == true && enablePensionType == true
                || expType != null && !expType.equals("-1") && expType.equals("Pension"))
            return pensionBills(sql, mainquery, mainquery1);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("start purchase bill");
        if (expType == null || expType.equals("-1") || expType.equals("Purchase")) {
            egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode("SBILL", "Approved");
            final EgwStatus egwStatus1 = egwStatusHibernateDAO.getStatusByModuleAndCode("PURCHBILL", "Passed");
            String statusCheck = "";
            if (egwStatus == null)
                statusCheck = " and bill.status in (" + egwStatus1.getId() + ") ";
            else
                statusCheck = " and bill.status in (" + egwStatus.getId() + "," + egwStatus1.getId() + ") ";

            final String supplierBillSql = mainquery + statusCheck + sql.toString() + " order by bill.billdate desc";
            final String supplierBillSql1 = mainquery1 + statusCheck + sql.toString() + " order by bill.billdate desc";
            supplierBillList = getPersistenceService().findPageBy(supplierBillSql, 1, 500, "Purchase").getList();
            if (supplierBillList != null)
                supplierBillList
                        .addAll(getPersistenceService().findPageBy(supplierBillSql1, 1, 500, "Purchase").getList());
            else
                supplierBillList = getPersistenceService()
                        .findPageBy(supplierBillSql1, 1, 500, "Purchase", egwStatus, egwStatus1).getList();
            final Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(supplierBillList);
            supplierBillList.clear();
            supplierBillList.addAll(tempBillList);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("supplierBillSql  ===> " + supplierBillSql);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("end purchase bill");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("start works bill");
        if (expType == null || expType.equals("-1") || expType.equals("Works")) {
            // right not we dont know, the EGW-Status for works bill, passed
            // from external system
            egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode("WORKSBILL", "Passed");
            final EgwStatus egwStatus1 = egwStatusHibernateDAO.getStatusByModuleAndCode("CONTRACTORBILL", "APPROVED"); // for
            // external
            // systems
            String statusCheck = "";
            if (egwStatus1 == null)
                statusCheck = " and bill.status in (" + egwStatus.getId() + ") ";
            else
                statusCheck = " and bill.status in (" + egwStatus.getId() + "," + egwStatus1.getId() + ") ";

            final String contractorBillSql = mainquery + statusCheck + sql.toString() + " order by bill.billdate desc";
            final String contractorBillSql1 = mainquery1 + statusCheck + sql.toString()
                    + " order by bill.billdate desc";
            contractorBillList = getPersistenceService().findPageBy(contractorBillSql, 1, 500, "Works").getList();
            if (contractorBillList != null)
                contractorBillList
                        .addAll(getPersistenceService().findPageBy(contractorBillSql1, 1, 500, "Works").getList());
            else
                contractorBillList = getPersistenceService().findPageBy(contractorBillSql1, 1, 500, "Works").getList();
            final Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(contractorBillList);
            contractorBillList.clear();

            contractorBillList.addAll(tempBillList);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("contractorBillSql  ===> " + contractorBillSql);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("end works bill");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("start contingent bill");
        if (expType == null || expType.equals("-1")
                || expType.equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)) {

            final String cBillmainquery = "from EgBillregister bill left join fetch bill.egBillregistermis.egBillSubType egBillSubType where (egBillSubType is null or egBillSubType.name not in ('"
                    + FinancialConstants.BILLSUBTYPE_TNEBBILL
                    + "')) and bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 "
                    + " and bill.passedamount > (select SUM(misc.paidamount) from Miscbilldetail misc where misc.billVoucherHeader = bill.egBillregistermis.voucherHeader "
                    + " and misc.payVoucherHeader.status in (0,5))";

            final String cBillmainquery1 = "from EgBillregister bill left join fetch bill.egBillregistermis.egBillSubType egBillSubType where (egBillSubType is null or egBillSubType.name not in ('"
                    + FinancialConstants.BILLSUBTYPE_TNEBBILL
                    + "')) and bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 "
                    + " and bill.egBillregistermis.voucherHeader NOT IN (select misc.billVoucherHeader from Miscbilldetail misc where misc.billVoucherHeader is not null and misc.payVoucherHeader.status <> 4)";

            egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode("EXPENSEBILL", "Approved"); // for
                                                                                                   // financial
                                                                                                   // expense
                                                                                                   // bills
            final String cBillSql = cBillmainquery + " and bill.status in (?) " + sql.toString()
                    + " order by bill.billdate desc";
            final String cBillSql1 = cBillmainquery1 + " and bill.status in (?) " + sql.toString()
                    + " order by bill.billdate desc";
            contingentBillList = getPersistenceService()
                    .findPageBy(cBillSql, 1, 500, FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT, egwStatus)
                    .getList();
            if (contingentBillList != null)
                contingentBillList.addAll(getPersistenceService().findPageBy(cBillSql1, 1, 500,
                        FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT, egwStatus).getList());
            else
                contingentBillList = getPersistenceService().findPageBy(cBillSql1, 1, 500,
                        FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT, egwStatus).getList();
            final Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(contingentBillList);
            contingentBillList.clear();
            contingentBillList.addAll(tempBillList);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("cBillSql  ===> " + cBillSql);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("end contingent bill");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getting glcodeids");
        paymentService.getGlcodeIds();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("done glcodeids");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deduction works start");
        deductionAmtMap = paymentService.getDeductionAmt(contractorBillList, "Works");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deduction works end");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deduction supplier start");
        deductionAmtMap.putAll(paymentService.getDeductionAmt(supplierBillList, "Purchase"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deduction supplier end");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deduction contingent start");
        deductionAmtMap.putAll(paymentService.getDeductionAmt(contingentBillList,
                FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deduction contingent end");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("paidamt works start");
        paidAmtMap = paymentService.getEarlierPaymentAmt(contractorBillList, "Works");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("paidamt works end");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("paidamt purchase start");
        paidAmtMap.putAll(paymentService.getEarlierPaymentAmt(supplierBillList, "Purchase"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("paidamt purchase end");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("paidamt contingent start");
        paidAmtMap.putAll(paymentService.getEarlierPaymentAmt(contingentBillList,
                FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("paidamt contingent end");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getCSList all 3 start");
        contractorList = paymentService.getCSList(contractorBillList, deductionAmtMap, paidAmtMap);
        supplierList = paymentService.getCSList(supplierBillList, deductionAmtMap, paidAmtMap);

        contingentList = paymentService.getCSList(contingentBillList, deductionAmtMap, paidAmtMap);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getCSList all 3 end");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("contingentList size ===" + contingentList.size());

        setMode("search");
        paymentMode = FinancialConstants.MODEOFCOLLECTION_CHEQUE;
        loadSchemeSubscheme();
        loadFundSource();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed search.");
        return "searchbills";
    }

    private String salaryBills(final StringBuffer sql, final String mainquery, final String mainquery1) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting salaryBills...");
        final EgwStatus egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode("SALBILL", "Approved"); // for
                                                                                                           // financial
                                                                                                           // salary
        // bills
        final EgwStatus egwStatus1 = egwStatusHibernateDAO.getStatusByModuleAndCode("SBILL", "Approved"); // for
                                                                                                          // external
                                                                                                          // systems
        final String sBillSql = mainquery + " and bill.status in (?,?) " + sql.toString()
                + " order by bill.billdate desc";
        final String sBillSql1 = mainquery1 + " and bill.status in (?,?) " + sql.toString()
                + " order by bill.billdate desc";
        salaryBillList = getPersistenceService().findAllBy(sBillSql, FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY,
                egwStatus, egwStatus1);
        if (salaryBillList != null)
            salaryBillList.addAll(getPersistenceService().findAllBy(sBillSql1,
                    FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY, egwStatus, egwStatus1));
        else
            salaryBillList = getPersistenceService().findAllBy(sBillSql1,
                    FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY, egwStatus, egwStatus1);
        final Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(salaryBillList);
        salaryBillList.clear();
        salaryBillList.addAll(tempBillList);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("sBillSql  ===> " + sBillSql);
        paymentService.getGlcodeIds();
        deductionAmtMap = paymentService.getDeductionAmt(salaryBillList, "Salary");
        paidAmtMap = paymentService.getEarlierPaymentAmt(salaryBillList, "Salary");
        salaryList = paymentService.getCSList(salaryBillList, deductionAmtMap, paidAmtMap);
        setMode("search");
        paymentMode = FinancialConstants.MODEOFPAYMENT_CASH;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed salaryBills.");
        return "salaryBills";
    }

    private String pensionBills(final StringBuffer sql, final String mainquery, final String mainquery1) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting pensionBills...");
        final EgwStatus egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode("PENSIONBILL", "Approved");
        final String pBillSql = mainquery + " and bill.status in (?) " + sql.toString()
                + " order by bill.billdate desc";
        final String pBillSql1 = mainquery1 + " and bill.status in (?) " + sql.toString()
                + " order by bill.billdate desc";
        pensionBillList = getPersistenceService().findAllBy(pBillSql,
                FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION, egwStatus);
        if (pensionBillList != null)
            pensionBillList.addAll(getPersistenceService().findAllBy(pBillSql1,
                    FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION, egwStatus));
        else
            pensionBillList = getPersistenceService().findAllBy(pBillSql1,
                    FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION, egwStatus);
        final Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(pensionBillList);
        pensionBillList.clear();
        pensionBillList.addAll(tempBillList);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("pBillSql  ===> " + pBillSql);
        paymentService.getGlcodeIds();
        deductionAmtMap = paymentService.getDeductionAmt(pensionBillList,
                FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION);
        paidAmtMap = paymentService.getEarlierPaymentAmt(pensionBillList,
                FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION);
        pensionList = paymentService.getCSList(pensionBillList, deductionAmtMap, paidAmtMap);
        setMode("search");
        paymentMode = FinancialConstants.MODEOFPAYMENT_CASH;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed pensionBills.");
        return "pensionBills";
    }

    @ValidationErrorPage(value = "tnebSearch")
    public String tnebBills() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting tnebBills...");

        final StringBuffer sql = new StringBuffer();
        if (!"".equals(billNumber))
            sql.append(" and bill.billnumber = '" + billNumber + "' ");
        if (voucherHeader.getFundId() != null)
            sql.append(" and bill.egBillregistermis.fund.id=" + voucherHeader.getFundId().getId());
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            sql.append(" and bill.egBillregistermis.egDepartment.id="
                    + voucherHeader.getVouchermis().getDepartmentid().getId());
        if (voucherHeader.getVouchermis().getFunction() != null)
            sql.append(" and bill.egBillregistermis.function=" + voucherHeader.getVouchermis().getFunction().getId());

        final String tnebSqlMainquery = "select bill from EgBillregister bill , EBDetails ebd   where  bill.id = ebd.egBillregister.id and bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 "
                + " and bill.passedamount > (select SUM(misc.paidamount) from Miscbilldetail misc where misc.billVoucherHeader = bill.egBillregistermis.voucherHeader "
                + " and misc.payVoucherHeader.status in (0,5))";

        final String tnebSqlMainquery1 = "select bill from EgBillregister bill , EBDetails ebd  where  bill.id = ebd.egBillregister.id and bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 "
                + " and bill.egBillregistermis.voucherHeader NOT IN (select misc.billVoucherHeader from Miscbilldetail misc where misc.billVoucherHeader is not null and misc.payVoucherHeader.status <> 4)";
        if (billSubType != null && !billSubType.equalsIgnoreCase(""))
            sql.append(" and bill.egBillregistermis.egBillSubType.name='" + billSubType + "'");
        if (region != null && !region.equalsIgnoreCase(""))
            sql.append(" and ebd.region='" + region + "'");
        if (month != null && !month.equalsIgnoreCase(""))
            sql.append(" and ebd.month=" + month + "");
        if (year != null && !year.equalsIgnoreCase(""))
            sql.append(" and ebd.financialyear.id=" + year + "");
        final EgwStatus egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode("EXPENSEBILL", "Approved"); // for
                                                                                                               // financial
        // expense
        // bills
        final EgwStatus egwStatus1 = egwStatusHibernateDAO.getStatusByModuleAndCode("CBILL", "APPROVED"); // for
                                                                                                          // external
                                                                                                          // systems
        final String tnebBillSql = tnebSqlMainquery + " and bill.status in (?,?) " + sql.toString()
                + " order by bill.billdate desc";
        final String tnebBillSql1 = tnebSqlMainquery1 + " and bill.status in (?,?) " + sql.toString()
                + " order by bill.billdate desc";
        contingentBillList = getPersistenceService().findPageBy(tnebBillSql, 1, 500,
                FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT, egwStatus, egwStatus1).getList();
        if (contingentBillList != null)
            contingentBillList.addAll(getPersistenceService().findPageBy(tnebBillSql1, 1, 500,
                    FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT, egwStatus, egwStatus1).getList());
        else
            contingentBillList = getPersistenceService().findPageBy(tnebBillSql1, 1, 500,
                    FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT, egwStatus, egwStatus1).getList();
        final Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(contingentBillList);
        contingentBillList.clear();
        contingentBillList.addAll(tempBillList);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("tnebBillSql  ===> " + tnebBillSql);
        paymentService.getGlcodeIds();
        deductionAmtMap = paymentService.getDeductionAmt(contingentBillList,
                FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        paidAmtMap = paymentService.getEarlierPaymentAmt(contingentBillList,
                FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        contingentList = paymentService.getCSList(contingentBillList, deductionAmtMap, paidAmtMap);
        setMode("search");
        paymentMode = FinancialConstants.MODEOFPAYMENT_RTGS;
        setTNEBMandatoryFields();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed tnebBills.");
        return "tnebBills";
    }

    /**
     *
     * @return
     * @throws ValidationException this api is called from searchbills method is changed to save to enable csrf fix actaul method
     * name was generate payment. I doesnot save data but forwards to screen where for selected bill we can make payment
     */
    @SkipValidation
    @ValidationErrorPage("searchbills")
    @Action(value = "/payment/payment-save")
    public String save() throws ValidationException {
        final List<PaymentBean> paymentList = new ArrayList<PaymentBean>();
        final List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        if (cutOffDateconfigValue != null && !cutOffDateconfigValue.isEmpty())
            try {
                date = df.parse(cutOffDateconfigValue.get(0).getValue());
                cutOffDate = formatter.format(date);
            } catch (final ParseException e) {

            }
        try {
            final String paymentMd = parameters.get("paymentMode")[0];
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Starting generatePayment...");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Expenditure type is--------------------------------- " + expType);

            if (null != contractorList && !contractorList.isEmpty())
                paymentList.addAll(contractorList);
            if (null != supplierList && !supplierList.isEmpty())
                paymentList.addAll(supplierList);
            if (null != contingentList && !contingentList.isEmpty())
                paymentList.addAll(contingentList);
            if (null != salaryList && !salaryList.isEmpty())
                paymentList.addAll(salaryList);
            if (null != pensionList && !pensionList.isEmpty())
                paymentList.addAll(pensionList);

            if (rtgsDefaultMode != null && rtgsDefaultMode.equalsIgnoreCase("Y") && !paymentMd.equalsIgnoreCase("rtgs"))
                if (paymentService.CheckForContractorSubledgerCodes(paymentList, rtgsModeRestrictionDateForCJV))
                    throw new ValidationException(Arrays.asList(new ValidationError(
                            "Payment Mode of any bill having Contractor/Supplier subledger should  RTGS For Bill Date Greater than 01-Oct-2013",
                            "Payment Mode of any bill having Contractor/Supplier subledger should  RTGS For Bill Date Greater than 01-Oct-2013")));
            billList = new ArrayList<PaymentBean>();
            contractorIds = contractorIds + populateBillListFor(contractorList, contractorIds);
            supplierIds = supplierIds + populateBillListFor(supplierList, supplierIds);
            contingentIds = contingentIds + populateBillListFor(contingentList, contingentIds);
            salaryIds = salaryIds + populateBillListFor(salaryList, salaryIds);
            pensionIds = pensionIds + populateBillListFor(pensionList, pensionIds);
            // functionSel=
            if (salaryIds != null && salaryIds.length() > 0)
                disableExpenditureType = true;
            if (pensionIds != null && pensionIds.length() > 0) {
                disableExpenditureType = true;
                enablePensionType = true;
            }

            billregister = (EgBillregister) persistenceService.find(" from EgBillregister where id=?",
                    billList.get(0).getBillId());
            if (billregister.getEgBillregistermis().getFunction() != null)
                setFunctionSel(billregister.getEgBillregistermis().getFunction().getId());
            loadbankBranch(billregister.getEgBillregistermis().getFund());
            miscount = billList.size();
            if (parameters.get("paymentMode")[0].equalsIgnoreCase("RTGS")) {
                paymentService.validateForRTGSPayment(contractorList, "Contractor");
                paymentService.validateForRTGSPayment(supplierList, "Supplier");
                if (billSubType == null || billSubType.equalsIgnoreCase(""))
                    paymentService.validateForRTGSPayment(contingentList,
                            FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
            }

            if (!"Auto".equalsIgnoreCase(voucherTypeForULB.readVoucherTypes("Payment"))) {
                headerFields.add("vouchernumber");
                mandatoryFields.add("vouchernumber");
            }
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Expenditure type is--------------------------------- " + expType);
            voucherdate = formatter.format(new Date());
        } catch (final ValidationException e) {
            try {
                search();
            } catch (final Exception e1) {
                LOGGER.error(e.getMessage(), e1);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exception", e1.getMessage()));
                throw new ValidationException(errors);
            }
            LOGGER.error(e.getErrors(), e);
            throw new ValidationException(e.getErrors());
        } catch (final ApplicationException e) {
            try {
                search();
            } catch (final Exception e1) {
                LOGGER.error(e.getMessage(), e1);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exception", e1.getMessage()));
                throw new ValidationException(errors);
            }
            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exception", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed generatePayment.");
        if (getBankBalanceCheck() == null || "".equals(getBankBalanceCheck()))
            addActionMessage(getText("payment.bankbalance.controltype"));
        return "form";

    }

    private String populateBillListFor(final List<PaymentBean> list, String ids) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting populateBillListFor...");

        String tempAttributes = "";
        if (list != null) {
            for (final PaymentBean bean : list) {
                if (bean == null)
                    continue;
                // if (bean.getIsSelected()) {
                if (chk.equals("")) {
                    chk = "checked";
                    fundNameStr = bean.getFundName() == null ? "" : bean.getFundName();
                    functionNameStr = bean.getFunctionName() == null ? "" : bean.getFunctionName();
                    deptNameStr = bean.getDeptName() == null ? "" : bean.getDeptName();
                    fundSourceNameStr = bean.getFundsourceName() == null ? "" : bean.getFundsourceName();
                    schemeStr = bean.getSchemeName() == null ? "" : bean.getSchemeName();
                    subSchemeStr = bean.getSubschemeName() == null ? "" : bean.getSubschemeName();
                    region = bean.getRegion() == null ? "" : bean.getRegion();
                    /*
                     * attributes = fundNameStr + "-" + functionNameStr + "-" + deptNameStr + "-" + fundSourceNameStr + "-" +
                     * schemeStr + "-" + subSchemeStr;
                     */
                    attributes = fundNameStr + "-" + fundSourceNameStr + "-" + schemeStr + "-" + subSchemeStr;
                }
                /*
                 * tempAttributes = (bean.getFundName() == null ? "" : bean.getFundName()) + "-" + (bean.getFunctionName() == null
                 * ? "" : bean.getFunctionName()) + "-" + (bean.getDeptName() == null ? "" : bean.getDeptName()) + "-" +
                 * (bean.getFundsourceName() == null ? "" : bean.getFundsourceName()) + "-" + (bean.getSchemeName() == null ? "" :
                 * bean.getSchemeName()) + "-" + (bean.getSubschemeName() == null ? "" : bean.getSubschemeName());
                 */
                tempAttributes = (bean.getFundName() == null ? "" : bean.getFundName()) + "-"
                        + (bean.getFundsourceName() == null ? "" : bean.getFundsourceName()) + "-"
                        + (bean.getSchemeName() == null ? "" : bean.getSchemeName()) + "-"
                        + (bean.getSubschemeName() == null ? "" : bean.getSubschemeName());
                if (attributes.equalsIgnoreCase(tempAttributes)) {
                    billList.add(bean);
                    ids = ids + bean.getBillId() + ",";
                } else {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Validation Error mismatch in attributes ");
                    throw new ValidationException(
                            Arrays.asList(new ValidationError("Mismatch in attributes", "Mismatch in attributes!!")));
                }
            }
            // else
            // continue;
            if (ids.length() > 0)
                ids = ids.substring(0, ids.length() - 1);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed populateBillListFor.");
        return ids;
    }

    @ValidationErrorPage(value = "form")
    @SkipValidation
    @Action(value = "/payment/payment-create")
    public String create() {
        try {
            final String vdate = parameters.get("voucherdate")[0];
            final Date date1 = sdf1.parse(vdate);
            final String voucherDate = formatter1.format(date1);
            String cutOffDate1 = null;
            // billregister.getEgBillregistermis().setFunction(functionSel);
            paymentActionHelper.setbillRegisterFunction(billregister, cFunctionobj);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Starting createPayment...");
            populateWorkflowBean();
            if (parameters.get("department") != null)
                billregister.getEgBillregistermis().setEgDepartment(
                        departmentService.getDepartmentById(Long.valueOf(parameters.get("department")[0].toString())));
            if (parameters.get("function") != null)
                billregister.getEgBillregistermis()
                        .setFunction(functionService.findOne(Long.valueOf(parameters.get("function")[0].toString())));
            paymentheader = paymentService.createPayment(parameters, billList, billregister, workflowBean);
            miscBillList = paymentActionHelper.getPaymentBills(paymentheader);
            // sendForApproval();// this should not be called here as it is
            // public method which is called from jsp submit

            if (!cutOffDate.isEmpty() && cutOffDate != null)
                try {
                    date = sdf1.parse(cutOffDate);
                    cutOffDate1 = formatter1.format(date);
                } catch (final ParseException e) {
                    //
                }
            if (cutOffDate1 != null && voucherDate.compareTo(cutOffDate1) <= 0
                    && FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getMessage("payment.transaction.success",
                        new String[] { paymentheader.getVoucherheader().getVoucherNumber() }));
            else {
                addActionMessage(getMessage("payment.transaction.success",
                        new String[] { paymentheader.getVoucherheader().getVoucherNumber() }));
                if (FinancialConstants.BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                    addActionMessage(getMessage("payment.voucher.approved", new String[] { paymentService
                            .getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));

            }

        } catch (final ValidationException e) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exception", e.getErrors().get(0).getMessage()));
            loadbankBranch(billregister.getEgBillregistermis().getFund());
            throw new ValidationException(errors);
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage());
            loadbankBranch(billregister.getEgBillregistermis().getFund());
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exception", e.getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            loadbankBranch(billregister.getEgBillregistermis().getFund());
            errors.add(new ValidationError("exception", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed createPayment.");
        setMode("view");
        return VIEW;
    }

    @ValidationErrorPage(value = VIEW)
    @SkipValidation
    @Action(value = "/payment/payment-sendForApproval")
    public String sendForApproval() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting sendForApproval...");
        if (paymentheader.getId() == null)
            paymentheader = getPayment();
        // this is to check if is not the create mode
        populateWorkflowBean();
        paymentheader = paymentActionHelper.sendForApproval(paymentheader, workflowBean);
        paymentActionHelper.getPaymentBills(paymentheader);
        if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("payment.voucher.rejected", new String[] {
                    paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));
        if (FinancialConstants.BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getMessage("payment.voucher.approved", new String[] {
                    paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));
        if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("payment.voucher.cancelled"));
        else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getMessage("payment.voucher.final.approval"));
        if (Constants.ADVANCE_PAYMENT.equalsIgnoreCase(paymentheader.getVoucherheader().getName())) {
            advanceRequisitionList.addAll(paymentActionHelper.getAdvanceRequisitionDetails(paymentheader));
            return "advancePaymentView";
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed sendForApproval.");
        setMode("view");
        return VIEW;
    }

    public String getComments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getComments...");
        return getText("payment.comments", new String[] {
                paymentheader.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toPlainString() });
    }

    @SkipValidation
    @Action(value = "/payment/payment-view")
    public String view() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting view...");
        paymentheader = getPayment();
        /*
         * if (paymentheader.getState().getValue() != null && !paymentheader.getState().getValue().isEmpty() &&
         * paymentheader.getState().getValue().contains("Rejected")) { if (LOGGER.isDebugEnabled()) LOGGER.debug("Completed view."
         * ); return modify(); }
         */
        miscBillList = paymentActionHelper.getPaymentBills(paymentheader);
        getChequeInfo(paymentheader);
        if (null != parameters.get("showMode") && parameters.get("showMode")[0].equalsIgnoreCase("view"))
            // if user is drilling down form source , parameter showMode is
            // passed with value view, in this case we do not show
            // the
            mode = parameters.get("showMode")[0];
        if (LOGGER.isInfoEnabled())
            LOGGER.info("defaultDept in vew : " + getDefaultDept());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed view.");
        return VIEW;
    }

    @SkipValidation
    public String advanceView() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting advanceView...");
        paymentheader = getPayment();
        if (paymentheader.getState().getValue() != null && !paymentheader.getState().getValue().isEmpty()
                && paymentheader.getState().getValue().contains("Rejected")) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Completed advanceView.");
            return modifyAdvancePayment();
        }
        advanceRequisitionList.addAll(paymentActionHelper.getAdvanceRequisitionDetails(paymentheader));
        getChequeInfo(paymentheader);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed advanceView.");
        return "advancePaymentView";
    }

    public void getChequeInfo(Paymentheader paymentheader) {
        // if(LOGGER.isInfoEnabled()) LOGGER.info("Inside getChequeInfo");
        paymentheader = getPayment();
        instrumentHeaderList = getPersistenceService().findAllBy(
                " from InstrumentHeader ih where ih.id in (select iv.instrumentHeaderId.id from InstrumentVoucher iv where iv.voucherHeaderId.id=?) order by instrumentNumber",
                paymentheader.getVoucherheader().getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Retrived cheque info details for the paymentheader");
    }

    @SkipValidation
    public boolean validateUser(final String purpose) throws ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("------------------Starting validateUser...");
        if (LOGGER.isInfoEnabled())
            LOGGER.info(
                    "-------------------------------------------------------------------------------------------------");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Calling Validate User " + purpose);
        if (LOGGER.isInfoEnabled())
            LOGGER.info(
                    "-------------------------------------------------------------------------------------------------");
        final Script validScript = (Script) getPersistenceService()
                .findAllByNamedQuery(Script.BY_NAME, "Paymentheader.show.bankbalance").get(0);
        final List<String> list = (List<String>) scriptService.executeScript(validScript,
                ScriptService.createContext("persistenceService", paymentService, "purpose", purpose));
        if (list.get(0).equals("true")) {
            if (purpose.equals("balancecheck")) {
                paymentheader = getPayment();
                try {
                    getBankBalance(paymentheader.getBankaccount().getId().toString(), formatter.format(new Date()),
                            paymentheader.getPaymentAmount(), paymentheader.getId(),
                            paymentheader.getBankaccount().getChartofaccounts().getId());
                } catch (final ValidationException e) {
                    LOGGER.error("Error" + e.getMessage(), e);
                    balance = BigDecimal.valueOf(-1);
                }
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Completed validateUser.");
            return true;
        } else {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Completed validateUser.");
            return false;
        }
    }

    @SkipValidation
    public String ajaxLoadBankAccounts() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccounts...");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Bankbranch id = " + parameters.get("bankbranch")[0]);
        final Bankbranch bankbranch = (Bankbranch) persistenceService.find("from Bankbranch where id = ?",
                Integer.parseInt(parameters.get("bankbranch")[0]));
        bankaccountList = getPersistenceService().findAllBy(" FROM Bankaccount where bankbranch=? and isactive=true ",
                bankbranch);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccounts.");
        return "bankaccount";
    }

    @SkipValidation
    @Action(value = "/payment/payment-ajaxGetAccountBalance")
    public String ajaxGetAccountBalance() throws ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside ajaxGetAccountBalance.");
        getBankBalance(parameters.get("bankaccount")[0], parameters.get("voucherDate")[0], null, null, null);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetAccountBalance.");
        return "balance";
    }

    @SkipValidation
    public void getBankBalance(final String accountId, final String vdate, final BigDecimal amount,
            final Long paymentId, final Long bankGlcodeId) throws ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getBankBalance.");

        try {
            balance = paymentService.getAccountBalance(accountId, vdate, amount, paymentId, bankGlcodeId);
        } catch (final Exception e) {
            balance = BigDecimal.valueOf(-1);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getBankBalance.");
    }

    @SkipValidation
    @Action(value = "/payment/payment-beforeModify")
    public String beforeModify() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeModify.");
        // if(validateUser("deptcheck"))
        voucherHeader.getVouchermis().setDepartmentid(paymentService.getAssignment().getDepartment());
        action = "search";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed beforeModify.");
        return LIST;
    }

    @ValidationErrorPage(value = LIST)
    public String list() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting list...");
        final List<String> descriptionList = new ArrayList<String>();
        descriptionList.add("New");
        descriptionList.add("Deposited");
        descriptionList.add("Reconciled");
        final List<EgwStatus> egwStatusList = egwStatusHibernateDAO.getStatusListByModuleAndCodeList("Instrument",
                descriptionList);
        String statusId = "";
        for (final EgwStatus egwStatus : egwStatusList)
            statusId = statusId + egwStatus.getId() + ",";
        statusId = statusId.substring(0, statusId.length() - 1);

        final StringBuffer sql = new StringBuffer();
        if (!StringUtils.isBlank(fromDate))
            sql.append(" and ph.voucherheader.voucherDate>='" + sdf.format(formatter.parse(fromDate)) + "' ");
        if (!StringUtils.isBlank(toDate))
            sql.append(" and ph.voucherheader.voucherDate<='" + sdf.format(formatter.parse(toDate)) + "'");
        if (!StringUtils.isBlank(voucherHeader.getVoucherNumber()))
            sql.append(" and ph.voucherheader.voucherNumber like '%" + voucherHeader.getVoucherNumber() + "%'");
        if (voucherHeader.getFundId() != null)
            sql.append(" and ph.voucherheader.fundId.id=" + voucherHeader.getFundId().getId());
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            sql.append(" and ph.voucherheader.vouchermis.departmentid.id="
                    + voucherHeader.getVouchermis().getDepartmentid().getId());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and ph.voucherheader.vouchermis.schemeid.id="
                    + voucherHeader.getVouchermis().getSchemeid().getId());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql.append(" and ph.voucherheader.vouchermis.subschemeid.id="
                    + voucherHeader.getVouchermis().getSubschemeid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql.append(" and ph.voucherheader.vouchermis.functionary.id="
                    + voucherHeader.getVouchermis().getFunctionary().getId());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql.append(" and ph.voucherheader.vouchermis.divisionid.id="
                    + voucherHeader.getVouchermis().getDivisionid().getId());

        paymentheaderList = getPersistenceService().findAllBy(
                " from Paymentheader ph where ph.voucherheader.status=0 and (ph.voucherheader.isConfirmed=null or ph.voucherheader.isConfirmed=0) "
                        + sql.toString()
                        + "  and ph.voucherheader.id not in (select iv.voucherHeaderId.id from InstrumentVoucher iv where iv.instrumentHeaderId in (from InstrumentHeader ih where ih.statusId.id in ("
                        + statusId + ") ))");
        action = LIST;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed list...");
        return LIST;
    }

    @ValidationErrorPage(value = LIST)
    @SkipValidation
    @Action(value = "/payment/payment-modify")
    public String modify() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting modify...");
        paymentheader = getPayment();
        final String vNumGenMode = voucherTypeForULB.readVoucherTypes("Payment");
        if (!"Auto".equalsIgnoreCase(vNumGenMode)) {
            voucherNumberPrefix = paymentheader.getVoucherheader().getVoucherNumber().substring(0,
                    Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
            voucherNumberSuffix = paymentheader.getVoucherheader().getVoucherNumber()
                    .substring(Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH,
                            paymentheader.getVoucherheader().getVoucherNumber().length()));
        }
        addDropdownData("bankaccountList",
                persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=true ",
                        paymentheader.getBankaccount().getBankbranch().getId()));
        // addDropdownData("bankbranchList",
        // persistenceService.findAllBy("from Bankbranch br where br.id in
        // (select bankbranch.id from Bankaccount where fund=? ) and
        // br.isactive=true order by br.bank.name
        // asc",paymentheader.getVoucherheader().getFundId()));
        loadbankBranch(paymentheader.getVoucherheader().getFundId());
        billList = paymentService.getMiscBillList(paymentheader);
        if (FinancialConstants.PAYMENTVOUCHER_NAME_SALARY.equalsIgnoreCase(paymentheader.getVoucherheader().getName()))
            disableExpenditureType = true;
        // commented by msahoo to avoid account balance check for the bill
        // creator.
        /*
         * try { balance = paymentService.getAccountBalance(paymentheader.getBankaccount().getId ().toString(),
         * formatter.format(new Date()),paymentheader.getPaymentAmount(),paymentheader.getId()); } catch (ParseException e) {
         * throw new ValidationException(Arrays.asList(new ValidationError( "Error While formatting date",
         * "Error While formatting date"))); }
         */
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed modify.");
        return MODIFY;

    }

    @ValidationErrorPage(value = LIST)
    @SkipValidation
    public String modifyAdvancePayment() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting modifyAdvancePayment...");
        paymentheader = (Paymentheader) persistenceService.find(" from Paymentheader where id=? ",
                paymentheader.getId());
        addDropdownData("bankaccountList",
                persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=true and fund.id=?",
                        paymentheader.getBankaccount().getBankbranch().getId(),
                        paymentheader.getBankaccount().getFund().getId()));
        loadbankBranch(paymentheader.getVoucherheader().getFundId());
        advanceRequisitionList.addAll(paymentActionHelper.getAdvanceRequisitionDetails(paymentheader));
        final String vNumGenMode = voucherTypeForULB.readVoucherTypes("Payment");
        if (!"Auto".equalsIgnoreCase(vNumGenMode)) {
            voucherNumberPrefix = paymentheader.getVoucherheader().getVoucherNumber().substring(0,
                    Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
            voucherNumberSuffix = paymentheader.getVoucherheader().getVoucherNumber()
                    .substring(Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH,
                            paymentheader.getVoucherheader().getVoucherNumber().length()));
        }
        try {
            balance = paymentService.getAccountBalance(paymentheader.getBankaccount().getId().toString(),
                    formatter.format(new Date()), paymentheader.getPaymentAmount(), paymentheader.getId(),
                    paymentheader.getBankaccount().getChartofaccounts().getId());
        } catch (final ParseException e) {
            LOGGER.error("Error" + e.getMessage(), e);
            throw new ValidationException(
                    Arrays.asList(new ValidationError("Error While formatting date", "Error While formatting date")));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed modifyAdvancePayment...");
        return "advancePaymentModify";
    }

    @ValidationErrorPage(value = MODIFY)
    @SkipValidation
    @Action(value = "/payment/payment-cancel")
    public String cancelPayment() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting cancelPayment...");
        paymentheader = (Paymentheader) persistenceService.find(" from Paymentheader where id=? ",
                paymentheader.getId());
        voucherHeader = paymentheader.getVoucherheader();
        voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
        // persistenceService.setType(CVoucherHeader.class);
        paymentheader.transition().end();
        persistenceService.persist(voucherHeader);
        addActionMessage(getMessage("payment.cancel.success"));
        action = parameters.get(ACTIONNAME)[0];
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed cancelPayment...");
        return VIEW;
    }

    @ValidationErrorPage(value = MODIFY)
    @SkipValidation
    @Action(value = "/payment/payment-edit")
    public String edit() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting update...");
        try {
            validateForUpdate();
            if (getFieldErrors().isEmpty()) {
                paymentheader = paymentService.updatePayment(parameters, billList, paymentheader);
                miscBillList = paymentActionHelper.getPaymentBills(paymentheader);
                sendForApproval();
                addActionMessage(getMessage("payment.transaction.success",
                        new String[] { paymentheader.getVoucherheader().getVoucherNumber() }));
            } else {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Completed update...");
                return MODIFY;
            }
        } catch (final ValidationException e) {
            LOGGER.error("Error" + e.getMessage(), e);
            addDropdownData("bankbranchList",
                    persistenceService.findAllBy(
                            "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive=true order by br.bank.name asc",
                            paymentheader.getVoucherheader().getFundId()));
            throw new ValidationException(e.getErrors());
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exception", e.getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exception", e.getMessage()));
            throw new ValidationException(errors);
        }

        // action=MODIFY;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed update...");
        return VIEW;
    }

    @ValidationErrorPage(value = "advancePaymentModify")
    @SkipValidation
    public String updateAdvancePayment() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting updateAdvancePayment...");
        paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where id=?", paymentheader.getId());
        advanceRequisitionList.addAll(paymentActionHelper.getAdvanceRequisitionDetails(paymentheader));
        try {
            validateAdvancePayment();
            paymentheader.setBankaccount((Bankaccount) persistenceService.find("from Bankaccount where id=?",
                    Integer.valueOf(parameters.get("paymentheader.bankaccount.id")[0])));
            addDropdownData("bankaccountList",
                    persistenceService.findAllBy(
                            " from Bankaccount where bankbranch.id=? and isactive=true and fund.id=?",
                            paymentheader.getBankaccount().getBankbranch().getId(),
                            paymentheader.getBankaccount().getFund().getId()));
            loadbankBranch(paymentheader.getBankaccount().getFund());
            if (getFieldErrors().isEmpty()) {
                if (null != parameters.get("approverUserId")
                        && Integer.valueOf(parameters.get("approverUserId")[0]) != -1)
                    Integer.valueOf(parameters.get("approverUserId")[0]);
                else
                    ApplicationThreadLocals.getUserId().intValue();
                /*
                 * paymentWorkflowService.transition(getValidActions().get(0). getName() + "|" + userId, paymentheader,
                 * paymentheader.getVoucherheader().getDescription());
                 */
                addActionMessage(getMessage("payment.voucher.approved", new String[] {
                        paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));
            } else {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Completed updateAdvancePayment.");
                return "advancePaymentModify";
            }
        } catch (final ValidationException e) {
            LOGGER.error("Error" + e.getMessage(), e);
            addDropdownData("bankaccountList",
                    persistenceService.findAllBy(
                            " from Bankaccount where bankbranch.id=? and isactive=true and fund.id=?",
                            paymentheader.getBankaccount().getBankbranch().getId(),
                            paymentheader.getBankaccount().getFund().getId()));
            loadbankBranch(paymentheader.getBankaccount().getFund());
            throw new ValidationException(e.getErrors());
        } catch (final Exception e) {
            addDropdownData("bankaccountList",
                    persistenceService.findAllBy(
                            " from Bankaccount where bankbranch.id=? and isactive=true and fund.id=?",
                            paymentheader.getBankaccount().getBankbranch().getId(),
                            paymentheader.getBankaccount().getFund().getId()));
            loadbankBranch(paymentheader.getBankaccount().getFund());
            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exception", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed updateAdvancePayment.");
        return "advancePaymentView";
    }

    private void validateAdvancePayment() throws ValidationException, ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateAdvancePayment...");
        if (paymentheader.getVoucherheader().getVoucherDate() == null
                || paymentheader.getVoucherheader().getVoucherDate().equals(""))
            throw new ValidationException(
                    Arrays.asList(new ValidationError("payment.voucherdate.empty", "payment.voucherdate.empty")));
        final String vNumGenMode = voucherTypeForULB.readVoucherTypes("Payment");
        if (!"Auto".equalsIgnoreCase(vNumGenMode) && (voucherNumberSuffix == null || voucherNumberSuffix.equals("")))
            throw new ValidationException(
                    Arrays.asList(new ValidationError("payment.vouchernumber.empty", "payment.vouchernumber.empty")));
        if (parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
            throw new ValidationException(Arrays.asList(new ValidationError("bankbranch.empty", "bankbranch.empty")));
        if (parameters.get("paymentheader.bankaccount.id")[0].equals("-1"))
            throw new ValidationException(Arrays.asList(new ValidationError("bankaccount.empty", "bankaccount.empty")));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateAdvancePayment...");
    }

    private void validateForUpdate() throws ValidationException, ApplicationException, ParseException {
        List<PaymentBean> tempBillList = new ArrayList<PaymentBean>();
        final List<String> expenseTypeList = new ArrayList<String>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateForUpdate...");
        if (paymentheader.getVoucherheader().getVoucherDate() == null
                || paymentheader.getVoucherheader().getVoucherDate().equals(""))
            addFieldError("paymentheader.voucherheader.voucherDate", getMessage("payment.voucherdate.empty"));
        final String vNumGenMode = voucherTypeForULB.readVoucherTypes("Payment");
        if (!"Auto".equalsIgnoreCase(vNumGenMode) && (voucherNumberSuffix == null || voucherNumberSuffix.equals("")))
            addFieldError("paymentheader.voucherheader.voucherNumber", getMessage("payment.vouchernumber.empty"));
        if (parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
            addFieldError("paymentheader.bankaccount.bankbranch.id", getMessage("bankbranch.empty"));
        if (parameters.get("paymentheader.bankaccount.id")[0].equals("-1"))
            addFieldError("paymentheader.bankaccount.id", getMessage("bankaccount.empty"));
        if (billList == null)
            addFieldError("paymentheader.bankaccount.id", getMessage("bill.details.empty"));

        /*
         * if(!parameters.get("paymentheader.bankaccount.id")[0].equals("-1") &&
         * paymentheader.getVoucherheader().getVoucherDate()!=null) { balance = paymentService.getAccountBalance(parameters.get(
         * "paymentheader.bankaccount.id")[0], formatter.format(paymentheader.getVoucherheader
         * ().getVoucherDate()),paymentheader.getPaymentAmount(),paymentheader. getId());
         * if(BigDecimal.valueOf(Long.valueOf(parameters.get("grandTotal")[0])). compareTo(balance)==1)
         * addFieldError("balance",getMessage("insufficient.bank.balance")); }
         */
        int i = 0;

        boolean selectedContractorPay = false;
        for (final PaymentBean bean : billList) {
            tempBillList = new ArrayList<PaymentBean>();
            tempBillList.add(bean);
            if (expenseTypeList.size() != 0 && expenseTypeList.contains(bean.getExpType()))
                continue;
            else
                expenseTypeList.add(bean.getExpType());
            if (bean.getIsSelected()) {
                i++;
                if (bean.getPaymentAmt().compareTo(BigDecimal.ZERO) <= 0)
                    addFieldError("billList[" + i + "].paymentAmt", getMessage("payment.amount.null"));
                if (rtgsDefaultMode != null && rtgsDefaultMode.equalsIgnoreCase("Y")
                        && bean.getExpType().equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS))
                    if (bean.getBillDate().compareTo(rtgsModeRestrictionDateForCJV) > 0
                            && !paymentheader.getType().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)) {
                        selectedContractorPay = true;
                        break;
                    }
            }
        }

        try {
            for (int j = 0; j < expenseTypeList.size(); j++)
                if (paymentheader.getType().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                    // if(expenseTypeList.get(j).equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS))
                    paymentService.validateRTGSPaymentForModify(tempBillList);
            /*
             * if(expenseTypeList.get(j).equalsIgnoreCase(FinancialConstants. STANDARD_EXPENDITURETYPE_PURCHASE))
             * paymentService.validateRTGSPaymentForModify(tempBillList);
             * if(expenseTypeList.get(j).equalsIgnoreCase(FinancialConstants. STANDARD_EXPENDITURETYPE_CONTINGENT))
             * paymentService.validateRTGSPaymentForModify(tempBillList);
             */
        } catch (final ValidationException e) {
            addFieldError(e.getErrors().get(0).getMessage(), getMessage(e.getErrors().get(0).getMessage()));
            // addFieldError("rtgs.payment.mandatory.details.missing",getMessage("rtgs.payment.mandatory.details.missing"));
        }

        if (selectedContractorPay)
            addFieldError("contractor.bills.only.rtgs.payment", getMessage("contractor.bills.only.rtgs.payment"));
        // throw new ValidationException(Arrays.asList(new
        // ValidationError("Mode of payment for contractor bills should only be
        // RTGS For Bill Date Greater than 01-Oct-2013",
        // "Mode of payment for contractor bills should only be RTGS For Bill
        // Date Greater than 01-Oct-2013")));
        if (i == 0)
            addFieldError("paymentheader.bankaccount.id", getMessage("bill.details.empty"));

        /*
         * Commenting since Salary payable is not subledger and Party is DO-bank name can use this in future when salary payable
         * is subledger financial needs to generate advice if(paymentheader.getType().equalsIgnoreCase("RTGS"))
         * paymentService.validateRTGSPaymentForModify(billList);
         */
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateForUpdate.");
    }

    @Override
    public void validate() {
        checkMandatory("fundId", Constants.FUND, voucherHeader.getFundId(), "voucher.fund.mandatory");
        checkMandatory("vouchermis.departmentid", Constants.DEPARTMENT, voucherHeader.getVouchermis().getDepartmentid(),
                "voucher.department.mandatory");
        checkMandatory("vouchermis.function", Constants.FUNCTION, voucherHeader.getVouchermis().getFunction(),
                "voucher.function.mandatory");
        checkMandatory("vouchermis.schemeid", Constants.SCHEME, voucherHeader.getVouchermis().getSchemeid(),
                "voucher.scheme.mandatory");
        checkMandatory("vouchermis.subschemeid", Constants.SUBSCHEME, voucherHeader.getVouchermis().getSubschemeid(),
                "voucher.subscheme.mandatory");
        checkMandatory("vouchermis.functionary", Constants.FUNCTIONARY, voucherHeader.getVouchermis().getFunctionary(),
                "voucher.functionary.mandatory");
        checkMandatory("fundsourceId", Constants.FUNDSOURCE, voucherHeader.getVouchermis().getFundsource(),
                "voucher.fundsource.mandatory");
        checkMandatory("vouchermis.divisionId", Constants.FIELD, voucherHeader.getVouchermis().getDivisionid(),
                "voucher.field.mandatory");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validate.");
    }

    private void checkMandatory(final String objectName, final String fieldName, final Object value,
            final String errorKey) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside checkMandatory.");
        if (mandatoryFields.contains(fieldName) && value == null)
            addFieldError(objectName, getMessage(errorKey));
    }

    @Override
    public List<String> getValidActions() {
        List<String> validActions = Collections.emptyList();
        final List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        if (cutOffDateconfigValue != null && !cutOffDateconfigValue.isEmpty()) {
            if (null == paymentheader || null == paymentheader.getId()
                    || paymentheader.getCurrentState().getValue().endsWith("NEW"))
                validActions = Arrays.asList(FORWARD, FinancialConstants.CREATEANDAPPROVE);
            else if (paymentheader.getCurrentState() != null)
                validActions = customizedWorkFlowService.getNextValidActions(paymentheader.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        paymentheader.getCurrentState().getValue(), getPendingActions(),
                        paymentheader.getCreatedDate());
        } else if (null == paymentheader || null == paymentheader.getId()
                || paymentheader.getCurrentState().getValue().endsWith("NEW"))
            validActions = Arrays.asList(FORWARD);
        else if (paymentheader.getCurrentState() != null)
            validActions = customizedWorkFlowService.getNextValidActions(paymentheader.getStateType(),
                    getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                    paymentheader.getCurrentState().getValue(), getPendingActions(),
                    paymentheader.getCreatedDate());
        return validActions;
    }

    @Override
    public String getNextAction() {
        WorkFlowMatrix wfMatrix = null;
        if (paymentheader.getId() != null)
            if (paymentheader.getCurrentState() != null)
                wfMatrix = customizedWorkFlowService.getWfMatrix(paymentheader.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        paymentheader.getCurrentState().getValue(), getPendingActions(),
                        paymentheader.getCreatedDate());
            else
                wfMatrix = customizedWorkFlowService.getWfMatrix(paymentheader.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), paymentheader.getCreatedDate());
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }

    /**
     *
     * @return
     */
    public Paymentheader getPayment() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getPayment...");
        String paymentid = null;
        paymentid = parameters.get(PAYMENTID)[0];
        if (paymentid != null)
            paymentheader = paymentService.find("from Paymentheader where id=?", Long.valueOf(paymentid));
        if (paymentheader == null)
            paymentheader = new Paymentheader();

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getPayment.");
        return paymentheader;
    }

    protected String getMessage(final String key) {
        return getText(key);
    }

    protected String getMessage(final String key, final String[] value) {
        return getText(key, value);
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(final String expType) {
        this.expType = expType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

    public List<EgBillregister> getContractorBillList() {
        return contractorBillList;
    }

    public void setContractorBillList(final List<EgBillregister> contractorBillList) {
        this.contractorBillList = contractorBillList;
    }

    public List<EgBillregister> getSupplierBillList() {
        return supplierBillList;
    }

    public void setSupplierBillList(final List<EgBillregister> supplierBillList) {
        this.supplierBillList = supplierBillList;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Map<String, String> getPayeeMap() {
        return payeeMap;
    }

    public void setPayeeMap(final Map<String, String> payeeMap) {
        this.payeeMap = payeeMap;
    }

    public List<EgBillregister> getTotalBillList() {
        return totalBillList;
    }

    public void setTotalBillList(final List<EgBillregister> totalBillList) {
        this.totalBillList = totalBillList;
    }

    public List<Bankaccount> getBankaccountList() {
        return bankaccountList;
    }

    public void setBankaccountList(final List<Bankaccount> bankaccountList) {
        this.bankaccountList = bankaccountList;
    }

    public Paymentheader getPaymentheader() {
        return paymentheader;
    }

    public void setPaymentheader(final Paymentheader paymentheader) {
        this.paymentheader = paymentheader;
    }

    public void setPaymentService(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public Map<Long, BigDecimal> getDeductionAmtMap() {
        return deductionAmtMap;
    }

    public void setDeductionAmtMap(final Map<Long, BigDecimal> deductionAmtMap) {
        this.deductionAmtMap = deductionAmtMap;
    }

    public Map<Long, BigDecimal> getPaidAmtMap() {
        return paidAmtMap;
    }

    public void setPaidAmtMap(final Map<Long, BigDecimal> paidAmtMap) {
        this.paidAmtMap = paidAmtMap;
    }

    public String getVoucherdate() {
        return voucherdate;
    }

    public void setVoucherdate(final String voucherdate) {
        this.voucherdate = voucherdate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<Miscbilldetail> getMiscBillList() {
        return miscBillList;
    }

    public void setMiscBillList(final List<Miscbilldetail> miscBillList) {
        this.miscBillList = miscBillList;
    }

    public int getMiscount() {
        return miscount;
    }

    public void setMiscount(final int miscount) {
        this.miscount = miscount;
    }

    public Integer getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(final Integer bankaccount) {
        this.bankaccount = bankaccount;
    }

    public Integer getBankbranch() {
        return bankbranch;
    }

    public void setBankbranch(final Integer bankbranch) {
        this.bankbranch = bankbranch;
    }

    public List<PaymentBean> getBillList() {
        return billList;
    }

    public void setBillList(final List<PaymentBean> billList) {
        this.billList = billList;
    }

    public String getContractorIds() {
        return contractorIds;
    }

    public void setContractorIds(final String contractorIds) {
        this.contractorIds = contractorIds;
    }

    public String getSalaryIds() {
        return salaryIds;
    }

    public void setSalaryIds(final String salaryIds) {
        this.salaryIds = salaryIds;
    }

    public String getSupplierIds() {
        return supplierIds;
    }

    public void setSupplierIds(final String supplierIds) {
        this.supplierIds = supplierIds;
    }

    public String getVouchernumber() {
        return vouchernumber;
    }

    public void setVouchernumber(final String vouchernumber) {
        this.vouchernumber = vouchernumber;
    }

    public EgBillregister getBillregister() {
        return billregister;
    }

    public void setBillregister(final EgBillregister billregister) {
        this.billregister = billregister;
    }

    public boolean isDepartmentDefault() {
        return isDepartmentDefault;
    }

    public void setDepartmentDefault(final boolean isDepartmentDefault) {
        this.isDepartmentDefault = isDepartmentDefault;
    }

    public List<InstrumentHeader> getInstrumentHeaderList() {
        return instrumentHeaderList;
    }

    public void setInstrumentHeaderList(final List<InstrumentHeader> instrumentHeaderList) {
        this.instrumentHeaderList = instrumentHeaderList;
    }

    public List<PaymentBean> getContractorList() {
        return contractorList;
    }

    public void setContractorList(final List<PaymentBean> contractorList) {
        this.contractorList = contractorList;
    }

    public List<PaymentBean> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(final List<PaymentBean> supplierList) {
        this.supplierList = supplierList;
    }

    public List<Paymentheader> getPaymentheaderList() {
        return paymentheaderList;
    }

    public void setPaymentheaderList(final List<Paymentheader> paymentheaderList) {
        this.paymentheaderList = paymentheaderList;
    }

    public String getVoucherNumberPrefix() {
        return voucherNumberPrefix;
    }

    public void setVoucherNumberPrefix(final String voucherNumberPrefix) {
        this.voucherNumberPrefix = voucherNumberPrefix;
    }

    public String getVoucherNumberSuffix() {
        return voucherNumberSuffix;
    }

    public void setVoucherNumberSuffix(final String voucherNumberSuffix) {
        this.voucherNumberSuffix = voucherNumberSuffix;
    }

    public List<EgBillregister> getContingentBillList() {
        return contingentBillList;
    }

    public void setContingentBillList(final List<EgBillregister> contingentBillList) {
        this.contingentBillList = contingentBillList;
    }

    public List<EgBillregister> getSalaryBillList() {
        return salaryBillList;
    }

    public void setSalaryBillList(final List<EgBillregister> salaryBillList) {
        this.salaryBillList = salaryBillList;
    }

    public List<PaymentBean> getContingentList() {
        return contingentList;
    }

    public void setContingentList(final List<PaymentBean> contingentList) {
        this.contingentList = contingentList;
    }

    public List<PaymentBean> getSalaryList() {
        return salaryList;
    }

    public void setSalaryList(final List<PaymentBean> salaryList) {
        this.salaryList = salaryList;
    }

    public String getContingentIds() {
        return contingentIds;
    }

    public void setContingentIds(final String contingentIds) {
        this.contingentIds = contingentIds;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(final String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDefaultDept() {
        return defaultDept;
    }

    public void setDefaultDept(final Integer defaultDept) {
        this.defaultDept = defaultDept;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(final String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public void setAdvanceRequisition(final List<EgAdvanceRequisition> advanceRequisition) {
        advanceRequisitionList = advanceRequisition;
    }

    public List<EgAdvanceRequisition> getAdvanceRequisitionList() {
        return advanceRequisitionList;
    }

    public void setDisableExpenditureType(final boolean disableExpenditureType) {
        this.disableExpenditureType = disableExpenditureType;
    }

    public boolean isDisableExpenditureType() {
        return disableExpenditureType;
    }

    public void setVoucherHelper(final VoucherHelper voucherHelper) {
    }

    public boolean isChangePartyName() {
        return changePartyName;
    }

    public void setChangePartyName(final boolean changePartyName) {
        this.changePartyName = changePartyName;
    }

    public String getNewPartyname() {
        return newPartyname;
    }

    public void setNewPartyname(final String newPartyname) {
        this.newPartyname = newPartyname;
    }

    public boolean isEnablePensionType() {
        return enablePensionType;
    }

    public void setEnablePensionType(final boolean enablePensionType) {
        this.enablePensionType = enablePensionType;
    }

    public List<EgBillregister> getPensionBillList() {
        return pensionBillList;
    }

    public void setPensionBillList(final List<EgBillregister> pensionBillList) {
        this.pensionBillList = pensionBillList;
    }

    public List<PaymentBean> getPensionList() {
        return pensionList;
    }

    public void setPensionList(final List<PaymentBean> pensionList) {
        this.pensionList = pensionList;
    }

    public String getPensionIds() {
        return pensionIds;
    }

    public void setPensionIds(final String pensionIds) {
        this.pensionIds = pensionIds;
    }

    public Long getFunctionSel() {
        return functionSel;
    }

    public void setFunctionSel(final Long functionSel) {
        this.functionSel = functionSel;
    }

    public String getRtgsDefaultMode() {
        return rtgsDefaultMode;
    }

    public void setRtgsDefaultMode(final String rtgsDefaultMode) {
        this.rtgsDefaultMode = rtgsDefaultMode;
    }

    public Date getRtgsModeRestrictionDateForCJV() {
        return rtgsModeRestrictionDateForCJV;
    }

    public void setRtgsModeRestrictionDateForCJV(final Date rtgsModeRestrictionDateForCJV) {
        this.rtgsModeRestrictionDateForCJV = rtgsModeRestrictionDateForCJV;
    }

    public String getBillSubType() {
        return billSubType;
    }

    public void setBillSubType(final String billSubType) {
        this.billSubType = billSubType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public Map<Integer, String> getMonthMap() {
        return monthMap;
    }

    public void setMonthMap(final Map<Integer, String> monthMap) {
        this.monthMap = monthMap;
    }

    public FinancialYearHibernateDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(final String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public String getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(final String bank_branch) {
        this.bank_branch = bank_branch;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(final String bank_account) {
        this.bank_account = bank_account;
    }

    public ScriptService getScriptService() {
        return scriptService;
    }

    public void setScriptService(final ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    public EgwStatusHibernateDAO getEgwStatusHibernateDAO() {
        return egwStatusHibernateDAO;
    }

    public void setEgwStatusHibernateDAO(final EgwStatusHibernateDAO egwStatusHibernateDAO) {
        this.egwStatusHibernateDAO = egwStatusHibernateDAO;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    @Override
    public String getCurrentState() {
        return paymentheader.getState().getValue();
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(final String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

}
