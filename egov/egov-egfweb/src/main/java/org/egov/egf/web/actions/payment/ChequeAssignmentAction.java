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

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.BankAccountService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.autonumber.RtgsNumberGenerator;
import org.egov.egf.model.BankAdviceReportInfo;
import org.egov.egf.web.actions.voucher.BaseVoucherAction;
import org.egov.eis.entity.DrawingOfficer;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.ChequeAssignment;
import org.egov.model.payment.Paymentheader;
import org.egov.model.recoveries.Recovery;
import org.egov.model.service.RecoveryService;
import org.egov.payment.client.BankAdviceForm;
import org.egov.services.cheque.ChequeService;
import org.egov.services.instrument.InstrumentHeaderService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.instrument.InstrumentVoucherService;
import org.egov.services.masters.BankService;
import org.egov.services.payment.ChequeAssignmentHelper;
import org.egov.services.payment.PaymentService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@ParentPackage("egov")
@Results({
        @Result(name = "search", location = "chequeAssignment-search.jsp"),
        @Result(name = "view", location = "chequeAssignment-view.jsp"),
        @Result(name = "viewRtgs", location = "chequeAssignment-viewRtgs.jsp"),
        @Result(name = "surrenderRTGSsearch", location = "chequeAssignment-surrenderRTGSsearch.jsp"),
        @Result(name = "viewReceiptDetailsResult", location = "chequeAssignment-viewReceiptDetailsResult.jsp"),
        @Result(name = "before_pension_search", location = "chequeAssignment-before_pension_search.jsp"),
        @Result(name = "surrenderRTGS", location = "chequeAssignment-surrenderRTGS.jsp"),
        @Result(name = "viewsurrender", location = "chequeAssignment-viewsurrender.jsp"),
        @Result(name = "remittanceRtgsSearch", location = "chequeAssignment-remittanceRtgsSearch.jsp"),
        @Result(name = "before_remittance_search", location = "chequeAssignment-before_remittance_search.jsp"),
        @Result(name = "before_salary_search", location = "chequeAssignment-before_salary_search.jsp"),
        @Result(name = "searchRtgsResult", location = "chequeAssignment-searchRtgsResult.jsp"),
        @Result(name = "surrendersearch", location = "chequeAssignment-surrendersearch.jsp"),
        @Result(name = "searchremittance", location = "chequeAssignment-searchremittance.jsp"),
        @Result(name = "searchpayment", location = "chequeAssignment-searchpayment.jsp"),
        @Result(name = "surrendercheques", location = "chequeAssignment-surrendercheques.jsp"),
        @Result(name = "rtgsSearch", location = "chequeAssignment-rtgsSearch.jsp"),
        @Result(name = "tnebRtgsSearch", location = "chequeAssignment-tnebRtgsSearch.jsp"),
        @Result(name = "bankAdvice-PDF", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/pdf", Constants.CONTENT_DISPOSITION,
                "no-cache;filename=BandAdvice.pdf"}),
        @Result(name = "bankAdvice-XLS", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/xls", Constants.CONTENT_DISPOSITION,
                "no-cache;filename=${fileName}"}),
        @Result(name = "bankAdvice-HTML", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "text/html"})
})
public class ChequeAssignmentAction extends BaseVoucherAction {
    private static final long serialVersionUID = -3721873563220007939L;
    private static final String SURRENDERSEARCH = "surrendersearch";
    private static final String SURRENDERRTGSSEARCH = "surrenderRTGSsearch";
    private static final Logger LOGGER = Logger.getLogger(ChequeAssignmentAction.class);
    private static final String JASPER_PATH = "/org/egov/payment/client/bankAdviceReport.jasper";
    private static final String WORKS = "Works";
    private static final String RTGS_DATE_LESS_THAN_PAYMENT_DATE = "rtgs.date.less.than.payment.date";
    private static final String CHEQUE_NO = "chequeNo";
    private static final String IN_FAVOUR_OF = "inFavourOf";
    private static final String INSTRUMENT_VOUCHER_LIST = "instrumentVoucherList";
    private static final String INSTRUMENT_HEADER_LIST = "instrumentHeaderList";
    private static final String UNPARSABLE_DATE = "Unparsable Date";
    private static final String BANKACCOUNT_LIST = "bankaccountList";
    private static final String PAYMENT_CHEQUENO_EMPTY = "payment.chequeno.empty";
    private static final String CHEQUE_NUMBER_SUFFIX = "].chequeNumber";
    private static final String CHEQUE_ASSIGNMENT_PREFIX = "chequeAssignmentList[";
    private static final String PAYMENT_CHEQUENUMBER_INVALID = "payment.chequenumber.invalid";
    private static final String EXCEPTION_WHILE_SURRENDER_CHEQUE = "Exception while surrender Cheque ";
    private static final String BILL_PAYMENT = "BillPayment";
    private static final String BANKBRANCH_LIST = "bankbranchList";
    private static final String RTGS_TRANSACTION_SUCCESS = "rtgs.transaction.success";
    private static final String CHQ_ASSIGNMENT_TXN_SUCCESS = "chq.assignment.transaction.success";
    private static final String DEPARTMENT = "department";
    private static final String BANK_ADVICE_REPORT_PATH = "/reports/templates/bankAdviceExcelReport.jasper";
    private static final String RECOVERY_LIST = "recoveryList";
    private static final String ACCOUNT_NO_AND_RTGS_ENTRY_MAP = "accountNoAndRtgsEntryMapSession";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
    private transient Map<String, String> modeOfPaymentMap;
    private transient List<InstrumentVoucher> instVoucherList;
    private transient List<InstrumentHeader> instVoucherDisplayList;
    private transient InputStream inputStream;
    private transient ReportHelper reportHelper;
    private transient Map<String, Object> paramMap = new HashMap<>();
    private transient List<Object> adviceList = new ArrayList<>();
    private String fromDate;
    private String toDate;
    private String[] surrender;
    private String instrumentNumber;
    private Long recoveryId;
    private String[] newInstrumentNumber;
    private String[] newSerialNo;
    private String[] newInstrumentDate;
    private String[] surrendarReasons;
    private String bank_account_dept;
    private transient Map<String, String> bankBranchMap = Collections.emptyMap();
    private transient Map<String, String> billTypeMap = Collections.emptyMap();
    private String billType;
    private transient Map<String, String> bankAccountMap = Collections.emptyMap();
    private transient Map<String, String> surrendarReasonMap = Collections.emptyMap();
    private transient Map<Bankaccount, List<ChequeAssignment>> accountNoAndRtgsEntryMap = new HashMap<>();
    private transient Map<BankAccountRemittanceCOA, List<ChequeAssignment>> accountNoAndRemittanceRtgsEntryMap = new HashMap<>();
    private transient Map<String, String> rtgsdateMap = new HashMap<>();
    private transient Map<String, String> rtgsRefNoMap = new HashMap<>();
    private transient Map<String, Boolean> rtgsSeceltedAccMap = new HashMap<>();
    private transient List<ChequeAssignment> rtgsList = new LinkedList<>();
    private transient List<ChequeAssignment> viewReceiptDetailsList = new ArrayList<>();
    private String paymentMode;
    private String inFavourOf;
    private Integer bankaccount = 0;
    private Integer selectedRows = 0;
    private Integer bankbranch;
    private String bank_branch;
    private String bank_account;
    private Integer department;
    private Date chequeDt;
    private boolean chequeNoGenerationAuto;
    private boolean rtgsNoGenerationAuto;
    private String typeOfAccount;
    private String fileName;
    @Autowired
    @Qualifier("paymentService")
    private transient PaymentService paymentService;
    @Autowired
    @Qualifier("chequeAssignmentHelper")
    private transient ChequeAssignmentHelper chequeAssignmentHelper;
    @Autowired
    @Qualifier("instrumentService")
    private transient InstrumentService instrumentService;
    @Autowired
    @Qualifier("instrumentHeaderService")
    private transient InstrumentHeaderService instrumentHeaderService;
    @Autowired
    @Qualifier("bankService")
    private transient BankService bankService;
    @Autowired
    @Qualifier("bankAccountService")
    private transient BankAccountService bankAccountService;
    private transient List<ChequeAssignment> chequeAssignmentList;
    private transient List<InstrumentHeader> instHeaderList = null;
    private String rtgsDate;
    private String rtgsRefNo;
    private transient List<InstrumentVoucher> instrumentVoucherList;
    private transient List<InstrumentHeader> instrumentHeaderList;
    private transient List<InstrumentHeader> tempInstrumentHeaderList;
    @Autowired
    private transient AutonumberServiceBeanResolver beanResolver;
    private String button;
    private transient ChequeService chequeService;
    private Date currentDate;
    @Autowired
    @Qualifier("remittanceRecoveryService")
    private transient RecoveryService recoveryService;
    private boolean reassignSurrenderChq = false;
    // to overriding department Mandatory Condition only for remittance cheque assignment search
    private Boolean deptNonMandatory = false;
    private Boolean functionNonMandatory = false;
    private Boolean rtgsContractorAssignment = false;
    private String assignmentType = BILL_PAYMENT;
    private transient List<String> chequeSlNoList = new ArrayList<>();
    private transient Map chequeSlNoMap;
    private Integer drawingOfficerId;
    private String drawingOfficerCode;
    private String billSubType;
    private String region;
    private String recoveryCode;
    private String paymentId;
    private BigDecimal totalDeductedAmount;
    private Boolean nonSubledger = false;
    private transient FinancialYearDAO financialYearDAO;
    private boolean containsRTGS = false;
    private transient List<CFinancialYear> yearCodeList;
    private Long departmentId;
    private boolean chequePrintingEnabled;
    private String chequePrintAvailableAt;
    private String instrumentHeader;
    private String chequeFormat;
    private Long instHeaderId;
    private String selectedRowsId;
    @Autowired
    private transient InstrumentVoucherService instrumentVoucherService;

    public List<String> getChequeSlNoList() {
        return chequeSlNoList;
    }

    public void setChequeSlNoList(final List<String> chequeSlNoList) {
        this.chequeSlNoList = chequeSlNoList;
    }

    public String getRtgsRefNo() {
        return rtgsRefNo;
    }

    public void setRtgsRefNo(final String rtgsRefNo) {
        this.rtgsRefNo = rtgsRefNo;
    }

    public String getRtgsDate() {
        return rtgsDate;
    }

    public void setRtgsDate(final String rtgsDate) {
        this.rtgsDate = rtgsDate;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(final String assignmentType) {
        this.assignmentType = assignmentType;
    }

    public boolean getReassignSurrenderChq() {
        return reassignSurrenderChq;
    }

    public void setReassignSurrenderChq(final boolean reassignSurrenderChq) {
        this.reassignSurrenderChq = reassignSurrenderChq;
    }

    public Map<String, String> getSurrendarReasonMap() {
        return surrendarReasonMap;
    }

    public void setSurrendarReasonMap(final Map<String, String> surrendarReasonMap) {
        this.surrendarReasonMap = surrendarReasonMap;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepare...");
        addDropdownData(BANKACCOUNT_LIST, Collections.emptyList());
        addDropdownData("regionsList", VoucherHelper.TNEB_REGIONS);
        chequeNoGenerationAuto = paymentService.isChequeNoGenerationAuto();
        rtgsNoGenerationAuto = paymentService.isRtgsNoGenerationAuto();
        typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS + "," + FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
        currentDate = new Date();
        // overriding department Mandatory Condition only for remittance cheque assignment search
        if (deptNonMandatory)
            mandatoryFields.remove(DEPARTMENT);

        // overriding function Mandatory Condition only for cheque assignment search
        mandatoryFields.remove("function");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepare.");
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforeSearch")
    public String beforeSearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeSearch...");
        paymentMode = FinancialConstants.MODEOFPAYMENT_CHEQUE;
        loadBillTypeMap();
        mandatoryFields.remove(DEPARTMENT);
        mandatoryFields.remove("function");

        deptNonMandatory = true;
        functionNonMandatory = true;
        return "search";
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforeRtgsSearch")
    public String beforeRtgsSearch() {
        paymentMode = FinancialConstants.MODEOFPAYMENT_RTGS;
        rtgsContractorAssignment = true;
        return "rtgsSearch";
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforeTNEBRtgsSearch")
    public String beforeTNEBRtgsSearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeRtgsSearch...");
        paymentMode = FinancialConstants.MODEOFPAYMENT_RTGS;
        rtgsContractorAssignment = true;
        setTNEBMandatoryFields();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed beforeRtgsSearch.");
        return "tnebRtgsSearch";
    }

    private void setTNEBMandatoryFields() {
        billType = "Expense";
        final List<String> propartyAppConfigKeysList = new ArrayList<>();
        final Map<String, String> propartyAppConfigResultList = new LinkedHashMap<>();
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_FUND);
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_FUNCTION);
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_DEPARTMENT);
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_BANKBRANCH);
        propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_BANKACCOUNT);

        // Get App config value
        for (final String key : propartyAppConfigKeysList) {
            final List<AppConfigValues> configValues = appConfigValuesService
                    .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, key);
            for (final AppConfigValues appConfigVal : configValues) {
                propartyAppConfigResultList.put(key, appConfigVal.getValue());
            }
        }
        for (final Map.Entry<String, String> entry : propartyAppConfigResultList.entrySet()) {
            String appConfigKey = entry.getKey();
            String appConfigValue = entry.getValue();
            if (appConfigKey.equals("EB Voucher Property-Fund"))
                voucherHeader.setFundId((Fund) persistenceService.find("from Fund where code = ?", appConfigValue));
            else if (appConfigKey.equals("EB Voucher Property-Function"))
                voucherHeader.getVouchermis().setFunction(
                        (CFunction) persistenceService.find("from CFunction where code = ?", appConfigValue));
            else if (appConfigKey.equals("EB Voucher Property-Department"))
                voucherHeader.getVouchermis().setDepartmentid(
                        (Department) persistenceService.find("from Department where deptCode = ?", appConfigValue));
            else if (appConfigKey.equals("EB Voucher Property-BankBranch"))
                bank_branch = appConfigValue;
            else if (appConfigKey.equals("EB Voucher Property-BankAccount")) {
                bank_account = appConfigValue;
                final Bankaccount ba = (Bankaccount) persistenceService.find(" from Bankaccount where accountnumber=?",
                        bank_account);
                if (ba.getId() != null)
                    bankaccount = ba.getId().intValue();
            }
        }

    }

    public void prepareBeforeRemittanceRtgsSearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareBeforeRemittanceRtgsSearch...");
        addDropdownData("drawingofficerList", getPersistenceService().findAllBy("from DrawingOfficer where id in" +
                " (select drawingOfficer.id from DepartmentDOMapping) order by code"));
        final List<Recovery> listRecovery = recoveryService.getAllActiveAutoRemitTds();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryAction | Tds list size : " + listRecovery.size());
        addDropdownData(RECOVERY_LIST, listRecovery);
        paymentMode = FinancialConstants.MODEOFPAYMENT_RTGS;
        rtgsContractorAssignment = true;
        if (deptNonMandatory)
            mandatoryFields.remove(DEPARTMENT);
        if (mandatoryFields.contains("fund"))
            mandatoryFields.remove("fund");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareBeforeRemittanceRtgsSearch.");
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforeRemittanceRtgsSearch")
    public String beforeRemittanceRtgsSearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeRemittanceRtgsSearch...");
        paymentMode = FinancialConstants.MODEOFPAYMENT_RTGS;
        rtgsContractorAssignment = true;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed beforeRemittanceRtgsSearch.");
        return "remittanceRtgsSearch";
    }

    @ValidationErrorPage(value = "remittanceRtgsSearch")
    @SkipValidation
    public String searchRemittanceRTGS() throws ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchRemittanceRTGS...");
        List<ChequeAssignment> rtgsChequeAssignmentList;
        List<ChequeAssignment> rtgsEntry;
        rtgsContractorAssignment = true;
        boolean addList = false;
        Bankaccount bnkAcc;
        CChartOfAccounts coa;

        rtgsChequeAssignmentList = paymentService.getPaymentVoucherForRemittanceRTGSInstrument(parameters, voucherHeader);

        for (final ChequeAssignment chqAssgn : rtgsChequeAssignmentList) {
            BankAccountRemittanceCOA bnkAccCOA = new BankAccountRemittanceCOA();
            if (accountNoAndRemittanceRtgsEntryMap.isEmpty()) {
                rtgsEntry = new ArrayList<>();
                bnkAcc = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                        Long.parseLong(chqAssgn.getBankAccountId().toString()));
                coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id =?", new Long(chqAssgn
                        .getGlcodeId().toString()));
                bnkAccCOA.setBankAccount(bnkAcc);
                bnkAccCOA.setRemittanceCOA(coa);
                rtgsEntry.add(chqAssgn);
                accountNoAndRemittanceRtgsEntryMap.put(bnkAccCOA, rtgsEntry);
                rtgsdateMap.put(bnkAcc.getId().toString(), formatter.format(currentDate));
            } else {
                final Set<BankAccountRemittanceCOA> bankAccntKeySet = accountNoAndRemittanceRtgsEntryMap.keySet();
                final java.util.Iterator keySetitr = bankAccntKeySet.iterator();
                while (keySetitr.hasNext()) {
                    final BankAccountRemittanceCOA bkcoa = (BankAccountRemittanceCOA) keySetitr.next();
                    if (bkcoa.getBankAccount().getId() == chqAssgn.getBankAccountId() &&
                            bkcoa.getRemittanceCOA().getId() == chqAssgn.getGlcodeId()) {
                        bnkAccCOA = bkcoa;
                        addList = false;
                        break;
                    } else
                        addList = true;
                }
                if (!addList) {
                    accountNoAndRemittanceRtgsEntryMap.get(bnkAccCOA).add(chqAssgn);
                } else {
                    rtgsEntry = new ArrayList<>();
                    bnkAcc = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                            Long.parseLong(chqAssgn.getBankAccountId().toString()));
                    coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id =?", new Long(chqAssgn
                            .getGlcodeId().toString()));
                    bnkAccCOA.setBankAccount(bnkAcc);
                    bnkAccCOA.setRemittanceCOA(coa);
                    rtgsEntry.add(chqAssgn);
                    accountNoAndRemittanceRtgsEntryMap.put(bnkAccCOA, rtgsEntry);
                    rtgsdateMap.put(bnkAcc.getId().toString(), formatter.format(currentDate));
                }
            }
        }
        getSession().put(ACCOUNT_NO_AND_RTGS_ENTRY_MAP, accountNoAndRemittanceRtgsEntryMap);
        if (0 != drawingOfficerId) {
            final DrawingOfficer drawingOfficer = (DrawingOfficer) persistenceService.find("from DrawingOfficer where id =?",
                    drawingOfficerId);
            drawingOfficerCode = drawingOfficer.getCode();
        }
        assignmentType = BILL_PAYMENT;
        if (!EMPTY.equals(parameters.get("recoveryId")[0])) {
            final Recovery recovery = (Recovery) persistenceService.find("from Recovery where id=?",
                    new Long(parameters.get("recoveryId")[0]));
            if (recovery.getChartofaccounts().getChartOfAccountDetails().isEmpty())
                nonSubledger = true;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed searchRemittanceRTGS.");
        return "searchRemittanceRtgsResult";
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-getReceiptDetails")
    public String getReceiptDetails() {
        Query query = null;
        query = persistenceService.getSession()
                .createSQLQuery(
                        "select  vh.id as voucherid ,vh.voucherNumber as voucherNumber ," +
                                " redtl.remittedamt as receiptAmount,redtl.remittedamt as deductedAmount" +
                                " FROM voucherheader vh,eg_remittance re,eg_remittance_detail redtl,generalledger gl" +
                                " WHERE re.paymentvhid = " + paymentId
                                + " AND re.id = redtl.remittanceid AND redtl.generalledgerid = gl.id AND gl.voucherheaderid =  " +
                                "  vh.id group by vh.id,vh.voucherNumber,redtl.remittedamt order by vh.voucherNumber")
                .addScalar("voucherid").addScalar("voucherNumber")
                .addScalar("receiptAmount").addScalar("deductedAmount")
                .setResultTransformer(Transformers.aliasToBean(ChequeAssignment.class));
        viewReceiptDetailsList = query.list();
        totalDeductedAmount = BigDecimal.ZERO;
        for (final ChequeAssignment ch : viewReceiptDetailsList)
            totalDeductedAmount = totalDeductedAmount.add(ch.getDeductedAmount());
        return "viewReceiptDetailsResult";

    }

    public void prepareBeforeSearchForRemittance() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareBeforeSearchForRemittance...");
        paymentMode = FinancialConstants.MODEOFPAYMENT_CASH;

        if (getSession().get(RECOVERY_LIST) == null) {
            final List<Recovery> listRecovery = recoveryService.getAllActiveRecoverys();
            getSession().put("RecoveryList", listRecovery);
        }
        addDropdownData(RECOVERY_LIST, (List) getSession().get(RECOVERY_LIST));
        // overriding department Mandatory Condition only for remittance cheque assignment search
        deptNonMandatory = true;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareBeforeSearchForRemittance.");

    }

    public void prepareBeforeRtgsSearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareBeforeRemittanceRtgsSearch...");

        addDropdownData("drawingofficerList", getPersistenceService().findAllBy("from DrawingOfficer order by code"));

        recoveryService.getAllActiveAutoRemitTds();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareBeforeRtgsSearch...");
        paymentMode = FinancialConstants.MODEOFPAYMENT_RTGS;
        rtgsContractorAssignment = true;
        if (deptNonMandatory)
            mandatoryFields.remove(DEPARTMENT);
        if (mandatoryFields.contains("fund"))
            mandatoryFields.remove("fund");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareBeforeRtgsSearch.");

    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforeSearchForRemittance")
    public String beforeSearchForRemittance() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeSearchForRemittance...");
        paymentMode = FinancialConstants.MODEOFPAYMENT_CASH;
        modeOfPaymentMap = new LinkedHashMap<>();
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_CASH, getText("cash.consolidated.cheque"));
        final List<Recovery> listRecovery = recoveryService.getAllActiveRecoverys();
        addDropdownData(RECOVERY_LIST, listRecovery);
        return "before_remittance_search";
    }

    @ValidationErrorPage(value = "rtgsSearch")
    @SkipValidation
    @Action(value = "/payment/chequeAssignment-searchRTGS")
    public String searchRTGS() throws ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchRTGS...");
        List<ChequeAssignment> rtgsChequeAssignmentList;
        List<ChequeAssignment> dbpRtgsAssignmentList;
        List<ChequeAssignment> rtgsEntry;
        rtgsContractorAssignment = true;
        boolean addList = false;
        Bankaccount bnkAcc;
        Bankaccount selBnkAcc = new Bankaccount();
        rtgsChequeAssignmentList = paymentService.getPaymentVoucherForRTGSInstrument(parameters, voucherHeader);
        dbpRtgsAssignmentList = paymentService.getDirectBankPaymentVoucherForRTGSInstrument(parameters, voucherHeader);

        rtgsChequeAssignmentList.addAll(dbpRtgsAssignmentList);
        if (!paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
            final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                    "cheque.assignment.infavourof");
            inFavourOf = appList.get(0).getValue();
        }
        chequeDt = new Date();
        for (final ChequeAssignment chqAssgn : rtgsChequeAssignmentList)
            // to set date
            if (accountNoAndRtgsEntryMap.isEmpty()) {
                rtgsEntry = new ArrayList<>();
                bnkAcc = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                        Long.parseLong(chqAssgn.getBankAccountId().toString()));
                selBnkAcc = bnkAcc;
                rtgsEntry.add(chqAssgn);
                accountNoAndRtgsEntryMap.put(bnkAcc, rtgsEntry);
                rtgsdateMap.put(bnkAcc.getId().toString(), formatter.format(currentDate));
            } else {
                final Set<Bankaccount> bankAccntSet = accountNoAndRtgsEntryMap.keySet();
                final java.util.Iterator ir = bankAccntSet.iterator();
                while (ir.hasNext()) {
                    final Bankaccount bk = (Bankaccount) ir.next();
                    if (bk.getId().compareTo(chqAssgn.getBankAccountId()) == 0) {
                        selBnkAcc = bk;
                        addList = false;
                        break;
                    } else
                        addList = true;
                }
                if (!addList) {
                    accountNoAndRtgsEntryMap.get(selBnkAcc).add(chqAssgn);
                    selBnkAcc = null;
                } else {
                    rtgsEntry = new ArrayList<>();
                    bnkAcc = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                            Long.parseLong(chqAssgn.getBankAccountId().toString()));
                    selBnkAcc = bnkAcc;
                    rtgsEntry.add(chqAssgn);
                    accountNoAndRtgsEntryMap.put(selBnkAcc, rtgsEntry);
                    rtgsdateMap.put(bnkAcc.getId().toString(), formatter.format(currentDate));
                    selBnkAcc = null;
                }

            }
        getSession().put(ACCOUNT_NO_AND_RTGS_ENTRY_MAP, accountNoAndRtgsEntryMap);
        assignmentType = BILL_PAYMENT;
        return "searchRtgsResult";
    }

    @SkipValidation
    public String searchTNEBRTGS() throws ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchTNEBRTGS...");
        List<ChequeAssignment> rtgsChequeAssignmentList;
        List<ChequeAssignment> rtgsEntry;
        rtgsContractorAssignment = true;
        boolean addList = false;
        Bankaccount bnkAcc;
        Bankaccount selBnkAcc = new Bankaccount();
        rtgsChequeAssignmentList = paymentService.getPaymentVoucherForTNEBRTGSInstrument(parameters, voucherHeader);
        chequeDt = new Date();
        for (final ChequeAssignment chqAssgn : rtgsChequeAssignmentList)
            // to set date
            if (accountNoAndRtgsEntryMap.isEmpty()) {
                rtgsEntry = new ArrayList<>();
                bnkAcc = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                        Long.parseLong(chqAssgn.getBankAccountId().toString()));
                selBnkAcc = bnkAcc;
                rtgsEntry.add(chqAssgn);
                accountNoAndRtgsEntryMap.put(bnkAcc, rtgsEntry);
                rtgsdateMap.put(bnkAcc.getId().toString(), formatter.format(currentDate));
            } else {
                final Set<Bankaccount> bankAccntSet = accountNoAndRtgsEntryMap.keySet();
                final java.util.Iterator ir = bankAccntSet.iterator();
                while (ir.hasNext()) {
                    final Bankaccount bk = (Bankaccount) ir.next();
                    if (bk.getId() == chqAssgn.getBankAccountId()) {
                        selBnkAcc = bk;
                        addList = false;
                        break;
                    } else
                        addList = true;
                }
                if (!addList) {
                    accountNoAndRtgsEntryMap.get(selBnkAcc).add(chqAssgn);
                    selBnkAcc = null;
                } else {
                    rtgsEntry = new ArrayList<>();
                    bnkAcc = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                            Long.parseLong(chqAssgn.getBankAccountId().toString()));
                    selBnkAcc = bnkAcc;
                    rtgsEntry.add(chqAssgn);
                    accountNoAndRtgsEntryMap.put(selBnkAcc, rtgsEntry);
                    rtgsdateMap.put(bnkAcc.getId().toString(), formatter.format(currentDate));
                    selBnkAcc = null;
                }

            }
        getSession().put(ACCOUNT_NO_AND_RTGS_ENTRY_MAP, accountNoAndRtgsEntryMap);

        assignmentType = BILL_PAYMENT;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed searchRTGS.");
        return "searchRtgsResult";
    }

    @ValidationErrorPage(value = "search")
    @Action(value = "/payment/chequeAssignment-search")
    public String search() throws ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting search...");
        chequeSlNoMap = loadChequeSerialNo();
        chequeAssignmentList = paymentService.getPaymentVoucherNotInInstrument(parameters, voucherHeader);
        if (!paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
            chequeDt = new Date();
            final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                    "cheque.assignment.infavourof");
            inFavourOf = appList.get(0).getValue();
        }
        loadBankAndAccount();
        assignmentType = BILL_PAYMENT;
        return "searchpayment";
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> loadChequeSerialNo() {
        chequeSlNoMap = new LinkedHashMap<>();
        try {
            if (bankaccount != null) {
                if (department != null) {
                    final List<Object[]> yearCodes = persistenceService
                            .findAllBy(
                                    "select ac.serialNo ,fs.finYearRange from  AccountCheques ac,CFinancialYear fs,ChequeDeptMapping cd  where ac.serialNo = fs.id and  bankAccountId=?"
                                            + "and ac.id=cd.accountCheque and cd.allotedTo=(select id from Department where id = "
                                            + department + ")"
                                            + " order by serialNo desc ",
                                    bankaccount);

                    if (yearCodes != null) {
                        for (final Object[] s : yearCodes)
                            chequeSlNoMap.put(s[0], s[1]);
                    }
                } else if (departmentId != null) {
                    final List<Object[]> yearCodes = persistenceService
                            .findAllBy(
                                    "select ac.serialNo ,fs.finYearRange from  AccountCheques ac,CFinancialYear fs,ChequeDeptMapping cd  where ac.serialNo = fs.id and  bankAccountId=?"
                                            + "and ac.id=cd.accountCheque and cd.allotedTo=(select id from Department where id = "
                                            + departmentId + ")"
                                            + " order by serialNo desc ",
                                    bankaccount);

                    if (yearCodes != null) {
                        for (final Object[] s : yearCodes)
                            chequeSlNoMap.put(s[0], s[1]);
                    }
                } else {
                    final List<Object[]> yearCodes = persistenceService
                            .findAllBy(
                                    "select ac.serialNo ,fs.finYearRange from  AccountCheques ac,CFinancialYear fs,ChequeDeptMapping cd  where ac.serialNo = fs.id and  bankAccountId=?"
                                            + "and ac.id=cd.accountCheque and cd.allotedTo=(select id from Department where upper(name) = 'ACCOUNTS')"
                                            + " order by serialNo desc ",
                                    bankaccount);

                    if (yearCodes != null) {
                        for (final Object[] s : yearCodes)
                            chequeSlNoMap.put(s[0], s[1]);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception occured while getting year code ", e);
        }
        return chequeSlNoMap;
    }

    public Map getChequeSlNoMap() {
        return chequeSlNoMap;
    }

    public void setChequeSlNoMap(final Map chequeSlNoMap) {
        this.chequeSlNoMap = chequeSlNoMap;
    }

    public void prepareSearchChequesOfRemittance() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareSearchChequesOfRemittance...");
        beforeSearchForRemittance();
        assignmentType = "RemittancePayment";
        deptNonMandatory = true;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareSearchChequesOfRemittance.");
    }

    @ValidationErrorPage(value = "before_salary_search")
    public String searchForSalaryPayments() throws ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchForSalaryPayments...");
        voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_SALARY);
        loadChequeSerialNo();
        chequeAssignmentList = paymentService.getPaymentVoucherNotInInstrument(parameters, voucherHeader);
        return "searchsalpayment";
    }

    @ValidationErrorPage(value = "before_pension_search")
    public String searchForPensionPayments() throws ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchForPensionPayments...");
        voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_PENSION);
        loadChequeSerialNo();
        chequeAssignmentList = paymentService.getPaymentVoucherNotInInstrument(parameters, voucherHeader);
        return "searchpensionpayment";
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforeSalarySearch")
    public String beforeSalarySearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeSalarySearch...");
        modeOfPaymentMap = new LinkedHashMap<>();
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_CASH, getText("cash.consolidated.cheque"));
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_RTGS, getText("rtgs"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed beforeSalarySearch.");
        return "before_salary_search";
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforePensionSearch")
    public String beforePensionSearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforePensionSearch...");
        modeOfPaymentMap = new LinkedHashMap<>();
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_CASH, getText("cash.consolidated.cheque"));
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_RTGS, getText("rtgs"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed beforePensionSearch.");
        return "before_pension_search";
    }

    public void prepareBeforeSalarySearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareBeforeSalarySearch...");
        paymentMode = FinancialConstants.MODEOFPAYMENT_CASH;
        deptNonMandatory = true;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareBeforeSalarySearch.");
    }

    public void prepareBeforePensionSearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareBeforePensionSearch...");
        paymentMode = FinancialConstants.MODEOFPAYMENT_CASH;
        deptNonMandatory = true;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareBeforePensionSearch.");
    }

    public void prepareSearchForSalaryPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareSearchForSalaryPayments...");
        beforeSalarySearch();
        deptNonMandatory = true;
        assignmentType = "SalaryPayment";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareSearchForSalaryPayments.");
    }

    public void prepareSearchForPensionPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareSearchForPensionPayments...");
        beforePensionSearch();
        deptNonMandatory = true;
        assignmentType = "PensionPayment";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareSearchForPensionPayments.");
    }

    @ValidationErrorPage(value = "before_remittance_search")
    @Action(value = "/payment/chequeAssignment-searchChequesOfRemittance")
    public String searchChequesOfRemittance() throws ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchChequesOfRemittance...");
        final Recovery recovery = (Recovery) persistenceService.find("from Recovery where id=?", recoveryId);
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE);
        loadChequeSerialNo();
        chequeAssignmentList = paymentService.getPaymentVoucherNotInInstrument(parameters, voucherHeader);
        if (recovery != null && recovery.getRemitted() != null)
            inFavourOf = recovery.getRemitted();
        else
            inFavourOf = EMPTY;
        loadBankAndAccount();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed searchChequesOfRemittance.");
        return "searchremittance";
    }

    private void loadBankAndAccount() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting loadBankAndAccount...");
        if (voucherHeader.getFundId() != null) {
            setTypeOfAccount(typeOfAccount);
            List<Map<String, Object>> bankbranchList = bankService
                    .getPaymentApprovedBankAndBranchName(voucherHeader.getFundId().getId(), currentDate);
            addDropdownData(BANKBRANCH_LIST, bankbranchList);
            bankBranchMap = new LinkedHashMap<>();
            for (final Map mp : bankbranchList)
                bankBranchMap.put((String) mp.get("bankBranchId"), (String) mp.get("bankBranchName"));

        }
        if (getBankbranch() != null) {
            setTypeOfAccount(typeOfAccount);
            addDropdownData(BANKACCOUNT_LIST,
                    bankAccountService.getBankaccountsHasApprovedPayment(voucherHeader.getFundId().getId(), getBankbranch()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed loadBankAndAccount.");
    }

    @ValidationErrorPage(value = "searchRtgsResult")
    @SkipValidation
    @Action(value = "/payment/chequeAssignment-update")
    public String update() throws ApplicationException, ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Start createInstrumentForRTGS");
        Map<String, List<ChequeAssignment>> resultMap;
        instHeaderList = new ArrayList<>();
        try {
            resultMap = prepareMapForRTGS();
            if (!getFieldErrors().isEmpty()) {
                accountNoAndRtgsEntryMap = (Map<Bankaccount, List<ChequeAssignment>>) getSession().get(
                        ACCOUNT_NO_AND_RTGS_ENTRY_MAP);
                return "searchRtgsResult";
            }
            createRtgsAssignment(resultMap);
        } catch (final ValidationException e) {
            searchRTGS();
            LOGGER.error("Validation Error", e);
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(e.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Error while searching rtgs", e);
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        addActionMessage(getMessage(RTGS_TRANSACTION_SUCCESS));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed createInstrument.");

        return "viewRtgs";
    }

    @ValidationErrorPage(value = "searchpayment")
    @SkipValidation
    @Action(value = "/payment/chequeAssignment-create")
    public String create() {

        if (!paymentMode.equalsIgnoreCase("cash"))
            prepareChequeAssignmentList();
        final List<AppConfigValues> printAvailConfig = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "chequeprintavailableat");

        chequePrintingEnabled = isChequePrintEnabled();

        for (final AppConfigValues appConfigVal : printAvailConfig)
            chequePrintAvailableAt = appConfigVal.getValue();
        if (chequePrintAvailableAt == null)
            chequePrintAvailableAt = EMPTY;
        Bankaccount bankAccount;
        if (bankaccount != null && bankaccount != 0) {
            bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?", bankaccount.longValue());
            if (bankAccount.getChequeformat() != null) {
                chequeFormat = bankAccount.getChequeformat().getId().toString();
            }
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting createInstrument...");
        loadChequeSerialNo();
        try {
            validateData();
            if (reassignSurrenderChq || !isChequeNoGenerationAuto())
                validateDataForManual();

            if (getFieldErrors().isEmpty()) {
                if (reassignSurrenderChq && !paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                    instHeaderList = chequeAssignmentHelper.reassignInstrument(chequeAssignmentList, paymentMode, bankaccount,
                            parameters, voucherHeader.getVouchermis().getDepartmentid());
                else
                    instHeaderList = chequeAssignmentHelper.createInstrument(chequeAssignmentList, paymentMode, bankaccount,
                            parameters,
                            voucherHeader.getVouchermis().getDepartmentid());
                selectedRows = paymentService.selectedRows;
                if (paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                    addActionMessage(getMessage(RTGS_TRANSACTION_SUCCESS));
                else
                    addActionMessage(getMessage(CHQ_ASSIGNMENT_TXN_SUCCESS));

                instVoucherList = paymentService.getInstVoucherList();
            } else {
                loadChequeSerialNo();
                return "searchpayment";
            }
        } catch (final ValidationException e) {
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed createInstrument.");

        return "view";
    }

    public List<ChequeAssignment> prepareChequeAssignmentList() {
        chequeAssignmentList = new ArrayList<>();
        String[] selectedRowsIdArray;
        if (selectedRowsId != null)
            selectedRowsIdArray = selectedRowsId.split(";,");
        else
            selectedRowsIdArray = new String[0];
        int length = selectedRowsIdArray.length;
        for (int index = 0; index < length; index++) {
            ChequeAssignment chequeAssignment = new ChequeAssignment();
            String[] items = selectedRowsIdArray[index].split("\\~");
            chequeAssignment.setVoucherHeaderId(Long.valueOf(items[0]));
            chequeAssignment.setPaidTo(items[1]);
            chequeAssignment.setSerialNo(items[2]);
            chequeAssignment.setChequeNumber(items[3]);
            try {
                chequeAssignment.setChequeDate(formatter.parse(items[4]));
                chequeAssignment.setVoucherDate(formatter.parse(items[6]));
            } catch (ParseException e) {
                LOGGER.error("Error parsing voucher date", e);
            }
            chequeAssignment.setVoucherNumber(items[5]);
            String item = items[7];
            if (item.contains(";"))
                item = items[7].replace(";", EMPTY);
            chequeAssignment.setPaidAmount(BigDecimal.valueOf(Double.valueOf(item)));
            chequeAssignment.setIsSelected(true);
            chequeAssignmentList.add(chequeAssignment);
        }
        return chequeAssignmentList;
    }

    private void createRtgsAssignment(final Map<String, List<ChequeAssignment>> resultMap) throws Exception {
        instVoucherList = new ArrayList<>();
        instVoucherDisplayList = new ArrayList<>();
        for (final Entry<String, List<ChequeAssignment>> row : resultMap.entrySet())
            if (row.getKey() != null && getRtgsSeceltedAccMap().get(row.getKey()) != null && getRtgsSeceltedAccMap().get(row.getKey())) {
                if (isRtgsNoGenerationAuto()) {
                    final String[] dateArray = new String[]{getRtgsdateMap().get(String.valueOf(Long.valueOf(row.getKey())))};
                    Date rtgsdate = null;
                    final Date autoNoCutOffDate = FinancialConstants.RTGS_FINYEAR_WISE_ROLLING_SEQ_CUTOFF_DATE;
                    String rtgsNo;
                    if (dateArray[0] != null) {
                        final String date = dateArray[0];
                        rtgsdate = formatter.parse(date);
                    }
                    final String finYearRange = financialYearDAO.getFinancialYearByDate(rtgsdate).getFinYearRange();

                    RtgsNumberGenerator rtgsNumberGenerator = beanResolver.getAutoNumberServiceFor(RtgsNumberGenerator.class);
                    if (rtgsdate != null && rtgsdate.after(autoNoCutOffDate)) {
                        rtgsNo = rtgsNumberGenerator.getNextNumber("RTGS_RefNumber_" + finYearRange.replace('-', '_'));

                        rtgsNo = new StringBuilder().append(rtgsNo).append("/").append(finYearRange).toString();
                    } else
                        rtgsNo = rtgsNumberGenerator.getNextNumber("RTGS_RefNumber");

                    final String[] refNoArray = new String[]{rtgsNo};
                    parameters.put("rtgsRefNo", refNoArray);
                } else {
                    final String[] refNoArray = new String[]{getRtgsRefNoMap().get(row.getKey())};
                    parameters.put("rtgsRefNo", refNoArray);
                }
                final String[] dateArray = new String[]{getRtgsdateMap().get(row.getKey())};
                parameters.put("rtgsDate", dateArray);
                chequeAssignmentList = resultMap.get(row.getKey());
                bankaccount = Integer.parseInt(row.getKey());

                paymentService.createInstrument(chequeAssignmentList, paymentMode, bankaccount, parameters, voucherHeader
                        .getVouchermis().getDepartmentid());
                instVoucherList.addAll(paymentService.getInstVoucherList());
                List<InstrumentVoucher> tempInstVoucherList = new ArrayList<>();
                for (InstrumentVoucher iv : instVoucherList) {
                    Paymentheader payment = paymentService.getPaymentHeaderByVoucherHeaderId(iv.getVoucherHeaderId().getId());
                    iv.setPaymentAmount(payment.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN));

                    tempInstVoucherList.add(iv);
                }
                instVoucherList = new ArrayList<>();
                instVoucherList = tempInstVoucherList;
            }
    }

    private Map<String, List<ChequeAssignment>> prepareMapForRTGS() throws ParseException {
        ArrayList<ChequeAssignment> rtgsEntry;
        final List<ChequeAssignment> contractorbillList = new ArrayList<>();
        new ArrayList<>();
        new ArrayList<>();
        Date rtgsdate = null;

        final Map<String, List<ChequeAssignment>> resultMap = new HashMap<>();
        new ArrayList<>();
        for (final ChequeAssignment chqAssgn : rtgsList)
            if (resultMap.isEmpty()) {
                rtgsEntry = new ArrayList<>();
                rtgsEntry.add(chqAssgn);
                if (chqAssgn.getBankAccountId() != null)
                    resultMap.put(String.valueOf(chqAssgn.getBankAccountId().longValue()), rtgsEntry);
                if (chqAssgn.getIsSelected()) {
                    rtgsSeceltedAccMap.put(String.valueOf(chqAssgn.getBankAccountId().longValue()), true);
                    if (isNotBlank(chqAssgn.getExpenditureType()) &&
                            chqAssgn.getExpenditureType().equalsIgnoreCase(WORKS))
                        contractorbillList.add(chqAssgn);
                    rtgsdate = Constants.DDMMYYYYFORMAT2.parse(getRtgsdateMap().get(String.valueOf(chqAssgn.getBankAccountId().longValue())));
                    if (chqAssgn.getVoucherDate().compareTo(rtgsdate) > 0)
                        addFieldError(RTGS_DATE_LESS_THAN_PAYMENT_DATE,
                                " RTGS Date cannot be less than Payment Date." + chqAssgn.getVoucherNumber());
                }
            } else if (resultMap.containsKey(String.valueOf(chqAssgn.getBankAccountId().longValue()))) {
                resultMap.get(String.valueOf(chqAssgn.getBankAccountId().longValue())).add(chqAssgn);
                if (chqAssgn.getIsSelected()) {
                    rtgsSeceltedAccMap.put(String.valueOf(chqAssgn.getBankAccountId().longValue()), true);
                    if (isNotBlank(chqAssgn.getExpenditureType()) &&
                            chqAssgn.getExpenditureType().equalsIgnoreCase(WORKS))
                        contractorbillList.add(chqAssgn);
                    rtgsdate = Constants.DDMMYYYYFORMAT2.parse(getRtgsdateMap().get(String.valueOf(chqAssgn.getBankAccountId().longValue())));
                    if (chqAssgn.getVoucherDate().compareTo(rtgsdate) > 0)
                        addFieldError(RTGS_DATE_LESS_THAN_PAYMENT_DATE,
                                "RTGS Date cannot be less than Payment Date." + chqAssgn.getVoucherNumber());
                }
            } else {
                rtgsEntry = new ArrayList<>();
                rtgsEntry.add(chqAssgn);
                resultMap.put(String.valueOf(chqAssgn.getBankAccountId().longValue()), rtgsEntry);
                if (chqAssgn.getIsSelected()) {
                    rtgsSeceltedAccMap.put(String.valueOf(chqAssgn.getBankAccountId().longValue()), true);
                    if (isNotBlank(chqAssgn.getExpenditureType())
                            && chqAssgn.getExpenditureType().equalsIgnoreCase(WORKS))
                        contractorbillList.add(chqAssgn);
                    rtgsdate = Constants.DDMMYYYYFORMAT2.parse(getRtgsdateMap().get(String.valueOf(chqAssgn.getBankAccountId().longValue())));
                    if (chqAssgn.getVoucherDate().compareTo(rtgsdate) > 0)
                        addFieldError(RTGS_DATE_LESS_THAN_PAYMENT_DATE,
                                "RTGS Date cannot be less than Payment Date." + chqAssgn.getVoucherNumber());
                }
            }
        try {
            paymentService.validatePaymentForRTGSAssignment(contractorbillList, "Contractor");
        } catch (final ValidationException e) {
            accountNoAndRtgsEntryMap = (Map<Bankaccount, List<ChequeAssignment>>) getSession().get("accountNoAndRtgsEntryMap");
            addFieldError("rtgs.payment.mandatory.details.missing", e.getErrors().get(0).getMessage());
        } catch (final ApplicationException e) {
            accountNoAndRtgsEntryMap = (Map<Bankaccount, List<ChequeAssignment>>) getSession().get("accountNoAndRtgsEntryMap");
            addFieldError("rtgs.payment.mandatory.details.missing", e.getMessage());
        }
        return resultMap;
    }

    @ValidationErrorPage(value = "searchsalpayment")
    @SkipValidation
    public String createInstrumentForSalaryPayment() {
        loadChequeSerialNo();

        try {
            validateData();
            if (reassignSurrenderChq || !isChequeNoGenerationAuto())
                validateDataForManual();
            if (getFieldErrors().isEmpty()) {
                if (reassignSurrenderChq && !paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                    instHeaderList = paymentService.reassignInstrument(chequeAssignmentList, paymentMode, bankaccount,
                            parameters, voucherHeader.getVouchermis().getDepartmentid());
                else
                    instHeaderList = paymentService.createInstrument(chequeAssignmentList, paymentMode, bankaccount, parameters,
                            voucherHeader.getVouchermis().getDepartmentid());
                selectedRows = paymentService.selectedRows;
                if (paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                    addActionMessage(getMessage(RTGS_TRANSACTION_SUCCESS));
                else
                    addActionMessage(getMessage(CHQ_ASSIGNMENT_TXN_SUCCESS));

                instVoucherList = paymentService.getInstVoucherList();
            } else {
                loadChequeSerialNo();
                return "searchsalpayment";
            }
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Error occurred while instrumentation salary", e);
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)) {
            prepareBeforeSalarySearch();
            beforeSalarySearch();
            mandatoryFields.remove(DEPARTMENT);
            return "before_salary_search";
        } else
            return "view";
    }

    @ValidationErrorPage(value = "searchpensionpayment")
    @SkipValidation
    public String createInstrumentForPensionPayment() {
        loadChequeSerialNo();
        try {
            validateData();
            if (reassignSurrenderChq || !isChequeNoGenerationAuto())
                validateDataForManual();
            if (getFieldErrors().isEmpty()) {
                if (reassignSurrenderChq && !paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                    instHeaderList = paymentService.reassignInstrument(chequeAssignmentList, paymentMode, bankaccount,
                            parameters, voucherHeader.getVouchermis().getDepartmentid());
                else
                    instHeaderList = paymentService.createInstrument(chequeAssignmentList, paymentMode, bankaccount, parameters,
                            voucherHeader.getVouchermis().getDepartmentid());
                selectedRows = paymentService.selectedRows;
                if (paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                    addActionMessage(getMessage(RTGS_TRANSACTION_SUCCESS));
                else
                    addActionMessage(getMessage(CHQ_ASSIGNMENT_TXN_SUCCESS));
                instVoucherList = paymentService.getInstVoucherList();
            } else {
                loadChequeSerialNo();
                return "searchpensionpayment";
            }
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Error occurred while instrumentation pension", e);
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)) {
            prepareBeforePensionSearch();
            beforePensionSearch();
            mandatoryFields.remove(DEPARTMENT);
            return "before_pension_search";
        } else
            return "view";
    }

    public void generateAdvice() throws ApplicationException {
        BankAdviceForm baf;
        EntityType entity;
        String voucherno = EMPTY;
        final InstrumentHeader instrumentHdr = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?",
                Long.valueOf(parameters.get("instHeaderId")[0]));
        for (final InstrumentVoucher instrumentVoucher : instrumentHdr.getInstrumentVouchers()) {
            final Object[] obj = (Object[]) persistenceService
                    .find(" select gld.detailTypeId.id,gld.detailKeyId,gld.amount from CGeneralLedgerDetail gld,CGeneralLedger gl where gl.id=gld.generalLedgerId.id and gl.voucherHeaderId=?",
                            instrumentVoucher.getVoucherHeaderId());
            if (obj != null) {
                entity = paymentService.getEntity((Integer) obj[0], (Serializable) obj[1]);
                baf = new BankAdviceForm();
                baf.setContractorCode(entity.getCode());
                baf.setContractorName(entity.getName());
                baf.setBankName(entity.getBankname());
                baf.setBankAccountNo(entity.getBankaccount());
                baf.setIfscCode(entity.getIfsccode());
                baf.setNetAmount((BigDecimal) obj[2]);
                voucherno = new StringBuilder().append(voucherno).append(instrumentVoucher.getVoucherHeaderId()
                        .getVoucherNumber()).append(',').toString();
                adviceList.add(baf);
            }
        }

        if (!adviceList.isEmpty()) {
            voucherno = voucherno.substring(0, voucherno.length() - 1);
            paramMap.put(CHEQUE_NO, instrumentHdr.getInstrumentNumber());
            paramMap.put("chequeDate", paymentService.formatter.format(instrumentHdr.getInstrumentDate()));
            paramMap.put("pymtVhNo", voucherno);
            paramMap.put(IN_FAVOUR_OF, instrumentHdr.getPayTo());
            paramMap.put("pymtBank", instrumentHdr.getBankAccountId().getBankbranch().getBank().getName() + "-"
                    + instrumentHdr.getBankAccountId().getBankbranch().getBranchname());
            paramMap.put("pymtAcNo", instrumentHdr.getBankAccountId().getAccountnumber());
        }
    }

    public void validateData() {
        if (paymentMode.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)) {
            if (!rtgsContractorAssignment) {
                if (isBlank(rtgsRefNo))
                    addFieldError("rtgsrefno", getMessage("rtgs.refno.empty"));
                if (isBlank(rtgsDate))
                    addFieldError("rtgsdate", getMessage("rtgs.date.empty"));
            }
            return;
        }
        checkMandatory("vouchermis.departmentid", Constants.DEPARTMENT, voucherHeader.getVouchermis().getDepartmentid(),
                "voucher.department.mandatory");
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforeSearchForSurrender")
    public String beforeSearchForSurrender() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeSearchForSurrender...");
        addDropdownData(BANKACCOUNT_LIST, Collections.emptyList());
        loadBankAndAccounForSurender();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed beforeSearchForSurrender.");
        return SURRENDERSEARCH;
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-beforeSearchForRTGSSurrender")
    public String beforeSearchForRTGSSurrender() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting beforeSearchForSurrender...");
        addDropdownData(BANKACCOUNT_LIST, Collections.emptyList());
        loadBankAndAccounForRTGSSurender();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed beforeSearchForSurrender.");
        return SURRENDERRTGSSEARCH;
    }

    @SkipValidation
    @ValidationErrorPage(value = SURRENDERSEARCH)
    @Action(value = "/payment/chequeAssignment-searchChequesForSurrender")
    public String searchChequesForSurrender() {
        validateForSuurenderSearch();
        if (!getFieldErrors().isEmpty()) {
            if (bank_branch != null && !bank_branch.equals("-1"))
                addDropdownData(
                        BANKACCOUNT_LIST,
                        persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=true ",
                                Integer.valueOf(bank_branch.split("-")[1])));
            loadReasonsForSurrendaring();
            return beforeSearchForSurrender();
        }

        final StringBuilder sql = new StringBuilder();
        try {
            List<Object> params = new LinkedList<>();
            if (isNotBlank(fromDate)) {
                sql.append(" and iv.voucherHeaderId.voucherDate>=? ");
                params.add(new Date(sdf.format(formatter.parse(fromDate))));
            }
            if (isNotBlank(toDate)) {
                sql.append(" and iv.voucherHeaderId.voucherDate<=? ");
                params.add(new Date(sdf.format(formatter.parse(toDate))));
            }
            if (bankaccount != null && bankaccount != -1) {
                sql.append(" and  ih.bankAccountId.id=? ");
                params.add(Long.valueOf(bankaccount));
            }
            if (isNotBlank(instrumentNumber)) {
                sql.append(" and  ih.instrumentNumber=? ");
                params.add(instrumentNumber);
            }
            if (department != null && department != -1) {
                sql.append(" and  iv.voucherHeaderId.vouchermis.departmentid.id=? ");
                params.add(Long.valueOf(department));
            }
            if (isNotBlank(voucherHeader.getVoucherNumber())) {
                sql.append(" and  iv.voucherHeaderId.voucherNumber=? ");
                params.add(voucherHeader.getVoucherNumber());
            }
            final String mainQuery = new StringBuilder(500)
                    .append("select ih from  InstrumentVoucher iv ,InstrumentHeader ih ,InstrumentType it ")
                    .append("where iv.instrumentHeaderId.id =ih.id and ih.instrumentNumber is not null ")
                    .append("and ih.instrumentType=it.id and ( it.type = 'cheque' or it.type = 'cash' ) and ")
                    .append("iv.voucherHeaderId.status=0  and iv.voucherHeaderId.type='")
                    .append(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT).append("'  ")
                    .append(sql).append(" and ih.statusId.id in (?)  order by iv.voucherHeaderId.voucherDate").toString();
            final EgwStatus created = instrumentService.getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS);
            params.add(created.getId());
            instrumentHeaderList = persistenceService.findAllBy(mainQuery, params.toArray());
            final LinkedHashSet lhs = new LinkedHashSet();
            lhs.addAll(instrumentHeaderList);
            instrumentHeaderList.clear();
            instrumentHeaderList.addAll(lhs);
            instrumentVoucherList = new ArrayList<>();
            for (final InstrumentHeader ih : instrumentHeaderList)
                instrumentVoucherList.addAll(ih.getInstrumentVouchers());
            getSession().put(INSTRUMENT_VOUCHER_LIST, instrumentVoucherList);
            getSession().put(INSTRUMENT_HEADER_LIST, instrumentHeaderList);

            if (!instrumentVoucherList.isEmpty()) {
                loadReasonsForSurrendaring();
                loadChequeSerialNo();
            }

        } catch (final ParseException e) {
            LOGGER.error("Error occurred while parsing date", e);
            throw new ValidationException(Arrays.asList(new ValidationError(UNPARSABLE_DATE, UNPARSABLE_DATE)));
        }
        getheader();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed searchChequesForSurrender.");
        containsRTGS = false;
        return "surrendercheques";
    }

    @SkipValidation
    @ValidationErrorPage(value = SURRENDERRTGSSEARCH)
    @Action(value = "/payment/chequeAssignment-searchForRTGSSurrender")
    public String searchForRTGSSurrender() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchRTGSForSurrender...");

        validateForSuurenderSearch();
        if (getFieldErrors().size() > 0) {
            if (bank_branch != null && !bank_branch.equals("-1"))
                addDropdownData(
                        BANKACCOUNT_LIST,
                        persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=true ",
                                Integer.valueOf(bank_branch.split("-")[1])));
            loadReasonsForSurrendaring();
            return beforeSearchForRTGSSurrender();
        }

        final StringBuilder sql = new StringBuilder();
        try {
            List<Object> params = new LinkedList<>();
            if (isNotBlank(fromDate)) {
                sql.append(" and iv.voucherHeaderId.voucherDate>=? ");
                params.add(sdf.format(formatter.parse(fromDate)));
            }
            if (isNotBlank(toDate)) {
                sql.append(" and iv.voucherHeaderId.voucherDate<=? ");
                params.add(sdf.format(formatter.parse(toDate)));
            }
            if (bankaccount != null && bankaccount != -1) {
                sql.append(" and  ih.bankAccountId.id=? ");
                params.add(bankaccount);
            }
            if (isNotBlank(instrumentNumber)) {
                sql.append(" and  ih.transactionNumber=? ");
                params.add(instrumentNumber);
            }
            if (department != null && department != -1) {
                sql.append(" and  iv.voucherHeaderId.vouchermis.departmentid.id=? ");
                params.add(department);
            }
            if (isNotBlank(voucherHeader.getVoucherNumber())) {
                sql.append(" and  iv.voucherHeaderId.voucherNumber=? ");
                params.add(voucherHeader.getVoucherNumber());
            }
            final String mainQuery = new StringBuilder(500)
                    .append("select ih from  InstrumentVoucher iv,InstrumentHeader ih ,InstrumentType it ")
                    .append("where iv.instrumentHeaderId.id =ih.id and ih.transactionNumber is not null and ih.instrumentType=it.id ")
                    .append("and it.type = 'advice' and   iv.voucherHeaderId.status=0  and iv.voucherHeaderId.type='")
                    .append(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT).append("' ").append(sql)
                    .append(" and ih.statusId.id in (?)  order by iv.voucherHeaderId.voucherDate").toString();
            final EgwStatus created = instrumentService.getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS);
            params.add(created.getId());
            instrumentHeaderList = persistenceService.findAllBy(mainQuery, params.toArray());
            final LinkedHashSet lhs = new LinkedHashSet();
            lhs.addAll(instrumentHeaderList);
            instrumentHeaderList.clear();
            instrumentHeaderList.addAll(lhs);
            instrumentVoucherList = new ArrayList<>();
            for (final InstrumentHeader ih : instrumentHeaderList)
                instrumentVoucherList.addAll(ih.getInstrumentVouchers());
            getSession().put(INSTRUMENT_VOUCHER_LIST, instrumentVoucherList);
            getSession().put(INSTRUMENT_HEADER_LIST, instrumentHeaderList);

            if (!instrumentVoucherList.isEmpty())
                loadReasonsForSurrendaring();
            loadChequeSerialNo();

        } catch (final ParseException e) {
            LOGGER.error("Error occurred while parsing date", e);
            throw new ValidationException(Arrays.asList(new ValidationError(UNPARSABLE_DATE, UNPARSABLE_DATE)));
        }
        getheader();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed searchRTGSForSurrender.");
        return "surrenderRTGS";
    }

    /**
     *
     */
    private void loadReasonsForSurrendaring() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting loadReasonsForSurrendaring...");

        List<AppConfigValues> appConfigValuesList;
        surrendarReasonMap = new LinkedHashMap<>();
        appConfigValuesList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", "Reason For Cheque Surrendaring");
        for (final AppConfigValues app : appConfigValuesList) {
            final String value = app.getValue();
            if (app.getValue().indexOf('|') == -1)
                surrendarReasonMap.put(app.getValue(), app.getValue());
            else
                surrendarReasonMap.put(app.getValue(), value.substring(0, app.getValue().indexOf('|')));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed loadReasonsForSurrendaring.");
    }

    /**
     *
     */
    private void loadBankAndAccounForSurender() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting loadBankAndAccounForSurender...");
        setTypeOfAccount(typeOfAccount);
        addDropdownData(BANKBRANCH_LIST, bankService.getChequeAssignedBankAndBranchName(currentDate));
        if (getBankbranch() != null) {
            addDropdownData(BANKACCOUNT_LIST,
                    bankAccountService.getBankaccountsWithAssignedCheques(getBankbranch(), null, currentDate));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed loadBankAndAccounForSurender.");
    }

    private void loadBankAndAccounForRTGSSurender() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting loadBankAndAccounForSurender...");
        setTypeOfAccount(typeOfAccount);
        addDropdownData(BANKBRANCH_LIST, bankService.getRTGSAssignedBankAndBranchName(currentDate));
        if (getBankbranch() != null) {
            addDropdownData(BANKACCOUNT_LIST, bankAccountService.getBankaccountsWithAssignedRTGS(getBankbranch(), currentDate));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed loadBankAndAccounForSurender.");
    }

    /**
     *
     */
    private void getheader() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getheader...");
        final Bankaccount account = (Bankaccount) persistenceService.find("from Bankaccount where id=?", bankaccount.longValue());
        bank_account_dept = account.getBankbranch().getBank().getName() + "-" + account.getBankbranch().getBranchname()
                + "-" + account.getAccountnumber();
        if (department != null && department != -1) {
            final Department dept = (Department) persistenceService.find("from Department where id=?", department.longValue());
            bank_account_dept = bank_account_dept + "-" + dept.getName();
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getheader.");
    }

    @SkipValidation
    private void validateForSuurenderSearch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateForSuurenderSearch...");

        if (bankaccount == null || bankaccount == -1)
            addFieldError("bankaccount", getMessage("bankaccount.empty"));
        if (bank_branch == null || bank_branch.equals("-1"))
            addFieldError("bankbranch", getMessage("bankbranch.empty"));
        if (isFieldMandatory(DEPARTMENT) && (null == department || department == -1))
            addFieldError(DEPARTMENT, getMessage("validate.department.null"));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateForSuurenderSearch.");
    }

    @ValidationErrorPage(value = "surrendercheques")
    @SkipValidation
    @Action(value = "/payment/chequeAssignment-save")
    public String save() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting surrenderCheques...");
        final StringBuilder reasonMissingRows = new StringBuilder(50);
        prepareInstrumentList();
        loadBankAndAccount();
        loadReasonsForSurrendaring();
        loadChequeSerialNo();

        try {

            if (surrender == null) {
                if (containsRTGS) {
                    addActionError("Please select atleast one Cheque for Surrendering");
                    return searchForRTGSSurrender();

                } else
                    throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SURRENDER_CHEQUE,
                            "Please select atleast one Cheque for Surrendering ")));

            }

            if (department == null || department == -1)
                throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SURRENDER_CHEQUE,
                        "please select department")));
            int index = 0;
            instrumentHeaderList = (List<InstrumentHeader>) getSession().get(INSTRUMENT_HEADER_LIST);
            final String[] newSurrender = new String[instrumentHeaderList.size()];
            for (final InstrumentHeader iheader : instrumentHeaderList) {
                newSurrender[index] = null;

                for (final String ih : surrender)
                    if (ih.equalsIgnoreCase(iheader.getId().toString()))
                        newSurrender[index] = ih;
                index++;
            }
            surrender = newSurrender;
            final List<InstrumentHeader> suurenderChequelist = new ArrayList<>();
            final List<String> chequeNoList = new ArrayList<>();
            final List<String> serialNoList = new ArrayList<>();
            final List<Date> chequeDateList = new ArrayList<>();
            InstrumentHeader instrumentHdr = null;
            if (surrender != null && surrender.length > 0) {
                for (int indx = 0; indx < surrender.length; indx++) {
                    if (surrender[indx] == null)
                        instrumentHdr = null;
                    else
                        instrumentHdr = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?",
                                Long.valueOf(surrender[indx]));
                    if (instrumentHdr != null && (surrendarReasons[indx] == null || surrendarReasons[indx].equalsIgnoreCase("-1"))) {
                        reasonMissingRows.append(indx + 1);
                        reasonMissingRows.append(',');
                    }
                    if (instrumentHdr != null) {
                        instrumentHdr.setSurrendarReason(surrendarReasons[indx]);
                        suurenderChequelist.add(instrumentHdr);
                        if (instrumentHdr.getTransactionNumber() != null)
                            containsRTGS = true;
                    }

                }
                if (!reasonMissingRows.toString().isEmpty())
                    if (containsRTGS) {
                        addActionError("please select the Reason for Surrendering the cheque for selected  rows");
                        return searchForRTGSSurrender();

                    } else
                        throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SURRENDER_CHEQUE,
                                "please select the Reason for Surrendering the cheque for selected  rows")));
                instrumentService.surrenderCheques(suurenderChequelist);
                if (button.equalsIgnoreCase("surrenderAndReassign") && containsRTGS)
                    throw new ValidationException(Arrays.asList(new ValidationError(
                            "Cannot reassign RTGS Numbers. Use RTGS Screen ",
                            "Cannot reassign RTGS Numbers. Use RTGS Screen")));
                else if (button.equalsIgnoreCase("surrenderAndReassign") && !containsRTGS) {

                    for (int indx = 0; indx < surrender.length; indx++)
                        if (surrender[indx] != null)
                            if (!isChequeNoGenerationAuto()) {
                                chequeNoList.add(newInstrumentNumber[indx]);
                                serialNoList.add(newSerialNo[indx]);
                                try {
                                    chequeDateList.add(formatter.parse(newInstrumentDate[indx]));

                                } catch (final ParseException e) {
                                    throw new ValidationException(Arrays.asList(new ValidationError(
                                            "Exception while formatting ChequeDate ", "TRANSACTION_FAILED")));
                                }
                            } else {
                                chequeNoList.add(getNewChequenumbers(instrumentHdr, department));
                                chequeDateList.add(new Date());

                            }

                    if (!isChequeNoGenerationAuto()) {
                        validateNewChequeNumbers(suurenderChequelist, chequeNoList, department, serialNoList);
                        if (getFieldErrors().size() > 0)
                            throw new ValidationException(Arrays.asList(new ValidationError("TRANSACTION FAILED",
                                    "TRANSACTION FAILED")));
                    }
                    paymentMode = "cheque";
                    instHeaderList = addNewInstruments(suurenderChequelist, chequeNoList, chequeDateList, serialNoList);
                    addActionMessage(getMessage("surrender.reassign.succesful"));
                } else {
                    instHeaderList = suurenderChequelist;
                    paymentMode = "cheque";
                    addActionMessage(getMessage("surrender.succesful"));
                }

            } else
                throw new ValidationException(Arrays.asList(new ValidationError("Exception while surrender ChequeDate ",
                        "please select at least one cheque")));
        } catch (final ValidationException e) {
            instrumentVoucherList = (List<InstrumentVoucher>) getSession().get(INSTRUMENT_VOUCHER_LIST);
            instrumentHeaderList = (List<InstrumentHeader>) getSession().get(INSTRUMENT_HEADER_LIST);
            getheader();

            throw e;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed surrenderCheques.");
        return "viewsurrender";
    }

    private List<InstrumentHeader> prepareInstrumentList() {
        String[] selectedArray = selectedRowsId.split(";,");
        if (!selectedRowsId.isEmpty()) {
            instrumentHeaderList = new ArrayList<>();
            int length = selectedArray.length;
            for (int index = 0; index < length; index++) {
                InstrumentHeader instrumentsHeader = new InstrumentHeader();
                String[] items = selectedArray[index].split("\\~");
                if (items[0] != null && !items[0].isEmpty()) {
                    instrumentsHeader.setId(Long.valueOf(items[0]));
                }
                if (items[1] != null && !items[1].isEmpty()) {
                    instrumentsHeader.setInstrumentNumber(items[1]);
                }
                try {
                    if (items[2] != null && !items[2].isEmpty()) {
                        instrumentsHeader.setInstrumentDate(formatter.parse(items[2]));
                    }
                } catch (ParseException e) {
                    LOGGER.error("Error parsing instrument date", e);
                }
                if (items[3] != null && !items[3].isEmpty()) {
                    instrumentsHeader.setSerialNo(financialYearDAO.findById(Long.valueOf(items[3]), false));
                }
                if (items[4] != null && !items[4].isEmpty()) {
                    String item = items[4];
                    if (item.contains(";"))
                        item = items[4].replace(";", EMPTY);
                    instrumentsHeader.setSurrendarReason(item);
                }
                instrumentHeaderList.add(instrumentsHeader);
            }
        }
        return instrumentHeaderList;
    }

    private String getNewChequenumbers(final InstrumentHeader instrumentHeader, final Integer department) {
        return instrumentHeader == null ? EMPTY : chequeService.nextChequeNumber(instrumentHeader.getBankAccountId().getId().toString(), 1, department);

    }

    /**
     * @param suurenderChequelist
     */
    private List<InstrumentHeader> addNewInstruments(final List<InstrumentHeader> suurenderChequelist,
                                                     final List<String> chequeNoList,
                                                     final List<Date> chequeDatelist, final List<String> serialNoList) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting addNewInstruments...");
        final List<Map<String, Object>> instrumentVouchers = new ArrayList<>();
        instHeaderList = new ArrayList<>();

        int index = 0;
        for (final InstrumentHeader instrumentHdr : suurenderChequelist) {
            final InstrumentHeader newInstrumentHeader = instrumentHdr.clone();
            newInstrumentHeader.setInstrumentNumber(chequeNoList.get(index));
            newInstrumentHeader.setSerialNo(financialYearDAO.findById(Long.valueOf(serialNoList.get(index)), false));
            newInstrumentHeader.setStatusId(instrumentService.getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS));
            newInstrumentHeader.setInstrumentDate(chequeDatelist.get(index));
            index++;
            instHeaderList.add(instrumentHeaderService.persist(newInstrumentHeader));
            for (final InstrumentVoucher iv : instrumentHdr.getInstrumentVouchers()) {
                Map<String, Object> instrumentVoucherMap = new HashMap<>();
                instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER, iv.getVoucherHeaderId());
                instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER, newInstrumentHeader);
                instrumentVouchers.add(instrumentVoucherMap);

            }

        }
        instVoucherList = instrumentService.updateInstrumentVoucherReference(instrumentVouchers);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed addNewInstruments.");
        return instHeaderList;
    }

    private void validateNewChequeNumbers(final List<InstrumentHeader> suurenderChequelist, final List<String> chequeNoList,
                                          final Integer department, final List<String> serialNoList) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateNewChequeNumbers...");
        if (newInstrumentNumber != null && newInstrumentNumber.length > 0) {
            InstrumentHeader instrumentHdr;
            for (int index = 0; index < suurenderChequelist.size(); index++) {
                instrumentHdr = suurenderChequelist.get(index);
                if (!instrumentService.isChequeNumberValid(chequeNoList.get(index), instrumentHdr.getBankAccountId().getId(),
                        department, serialNoList.get(index)))
                    addFieldError("newInstrumentNumber[" + index + "]", getMessage(PAYMENT_CHEQUENUMBER_INVALID) + " "
                            + chequeNoList.get(index));
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateNewChequeNumbers.");
    }

    public void validateDataForManual() throws ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateDataForManual...");
        int index = 0;
        final Map<String, String> chqNoMap = new HashMap<>();
        if (paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
            for (final ChequeAssignment assignment : chequeAssignmentList)
                if (assignment.getIsSelected()) {
                    if (assignment.getSerialNo() == null) {

                        addFieldError("Year code should not be empty", getMessage("payment.yearcode.invalid"));
                        break;
                    }
                    if (isBlank(assignment.getChequeNumber())) {
                        addFieldError(CHEQUE_ASSIGNMENT_PREFIX + index + CHEQUE_NUMBER_SUFFIX, getMessage(PAYMENT_CHEQUENO_EMPTY));
                        break;
                    } else if (reassignSurrenderChq) {
                        if (!instrumentService.isReassigningChequeNumberValid(assignment.getChequeNumber(),
                                bankaccount.longValue(), voucherHeader.getVouchermis().getDepartmentid().getId().intValue(),
                                assignment.getSerialNo()))
                            addFieldError(CHEQUE_ASSIGNMENT_PREFIX + index + CHEQUE_NUMBER_SUFFIX,
                                    getMessage(PAYMENT_CHEQUENUMBER_INVALID));
                    } else if (!instrumentService.isChequeNumberValid(assignment.getChequeNumber(), bankaccount.longValue(),
                            voucherHeader.getVouchermis().getDepartmentid().getId().intValue(), assignment.getSerialNo()))
                        addFieldError(CHEQUE_ASSIGNMENT_PREFIX + index + CHEQUE_NUMBER_SUFFIX,
                                getMessage(PAYMENT_CHEQUENUMBER_INVALID));
                    if (null == assignment.getChequeDate()) {
                        addFieldError("Cheque date cannot be empty", getMessage("payment.chequedate.invalid"));
                        break;
                    }
                    if (assignment.getChequeDate().compareTo(assignment.getVoucherDate()) < 0) {

                        addFieldError("Cheque date cannot be less than paymnet date", getMessage("payment.chequedate.invalid"));
                        break;
                    }
                    if (chqNoMap.containsKey(assignment.getChequeNumber())) {
                        if (!chqNoMap.get(assignment.getChequeNumber()).equals(assignment.getPaidTo())) // can't give the same
                            // cheque to different
                            // parties.
                            addFieldError(
                                    CHEQUE_ASSIGNMENT_PREFIX + index + CHEQUE_NUMBER_SUFFIX,
                                    getMessage("payment.duplicate.chequeno", new String[]{assignment.getChequeNumber(),
                                            chqNoMap.get(assignment.getChequeNumber()), assignment.getPaidTo()}));
                    } else
                        chqNoMap.put(assignment.getChequeNumber(), assignment.getPaidTo());
                    index++;
                }
        } else {
            // cash or RTGS
            // / Validations are done for RTGS payment mode
            if (FinancialConstants.MODEOFPAYMENT_RTGS.equalsIgnoreCase(paymentMode))
                return;
            String chequedt = parameters.get("chequeDt")[0];
            if (StringUtils.isEmpty(parameters.get(IN_FAVOUR_OF)[0]))
                addFieldError(IN_FAVOUR_OF, getMessage("inFavourOf.is.empty"));
            if (StringUtils.isEmpty(parameters.get(CHEQUE_NO)[0]))
                addFieldError(CHEQUE_NO, getMessage(PAYMENT_CHEQUENO_EMPTY));
            else {
                for (int indx = 0; indx < chequeAssignmentList.size(); indx++)
                    if (parameters.get(CHEQUE_ASSIGNMENT_PREFIX + indx + "].isSelected") != null
                            && parameters.get(CHEQUE_ASSIGNMENT_PREFIX + indx + "].isSelected")[0].equals("true")) {
                        final String paymentdt = parameters.get(CHEQUE_ASSIGNMENT_PREFIX + indx + "].tempPaymentDate")[0];

                        if (formatter.parse(chequedt).compareTo(formatter.parse(paymentdt)) < 0)
                            addFieldError("Cheque date cannot be less than paymnet date",
                                    getMessage("payment.chequedate.invalid"));
                    }
                if (reassignSurrenderChq) {
                    if (!instrumentService.isReassigningChequeNumberValid(parameters.get(CHEQUE_NO)[0], bankaccount.longValue(),
                            voucherHeader.getVouchermis().getDepartmentid().getId().intValue(), parameters.get("serialNo")[0]))
                        addFieldError(CHEQUE_ASSIGNMENT_PREFIX + index + CHEQUE_NUMBER_SUFFIX, getMessage(PAYMENT_CHEQUENUMBER_INVALID));
                } else if (!instrumentService.isChequeNumberValid(parameters.get(CHEQUE_NO)[0], bankaccount.longValue(),
                        voucherHeader.getVouchermis().getDepartmentid().getId().intValue(), parameters.get("serialNo")[0]))
                    addFieldError("chequeN0", getMessage(PAYMENT_CHEQUENUMBER_INVALID));
            }
            if (null == getChequeDt())
                addFieldError("chequeDt", getMessage("payment.chequedate.empty"));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateDataForManual.");
    }

    @Override
    public void validate() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validate...");
        if (!rtgsContractorAssignment && !containsRTGS) {
            checkMandatory("fundId", Constants.FUND, voucherHeader.getFundId(), "voucher.fund.mandatory");
            checkMandatory("vouchermis.departmentid", Constants.DEPARTMENT, voucherHeader.getVouchermis().getDepartmentid(),
                    "voucher.department.mandatory");
            checkMandatory("voucher.function", Constants.FUNCTION, voucherHeader.getVouchermis().getFunction(),
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
            checkMandatory("Recovery Code", Constants.RECOVERY, recoveryId, "recovery.mandatory");
            loadBankAndAccount();
            loadBillTypeMap();

            if (getBankbranch() == null || getBankbranch() == -1)
                addFieldError("bankbranch", getMessage("bankbranch.empty"));
            if (getBankaccount() == null || getBankaccount() == -1)
                addFieldError("bankaccount", getMessage("bankaccount.empty"));
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validate.");
    }

    private void loadBillTypeMap() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting loadBillTypeMap...");
        billTypeMap = new HashMap<>();
        billTypeMap.put(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,
                FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        billTypeMap.put(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS + "-"
                + FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE, "Contractor/Supplier");
        billTypeMap.put(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK, "No associated bills");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed loadBillTypeMap.");
    }

    private void checkMandatory(final String objectName, final String fieldName, final Object value, final String errorKey) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting checkMandatory...");
        if (mandatoryFields.contains(fieldName) && value == null)
            addFieldError(objectName, getMessage(errorKey));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed checkMandatory.");
    }

    @SkipValidation
    public String ajaxGenerateAdviceHtml() throws ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGenerateAdviceHtml...");
        generateAdvice();
        inputStream = reportHelper.exportHtml(inputStream, JASPER_PATH, getParamMap(), getAdviceList(), "pt");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGenerateAdviceHtml.");
        return "bankAdvice-HTML";
    }

    @SkipValidation
    public String generateAdvicePdf() throws JRException, IOException, ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting generateAdvicePdf...");
        generateAdvice();
        inputStream = reportHelper.exportPdf(inputStream, JASPER_PATH, getParamMap(), getAdviceList());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed generateAdvicePdf.");
        return "bankAdvice-PDF";
    }

    public Map<String, String> getBankBranchMap() {
        return bankBranchMap;
    }

    public void setBankBranchMap(final Map<String, String> bankBranchMap) {
        this.bankBranchMap = bankBranchMap;
    }

    public Map<String, String> getBillTypeMap() {
        return billTypeMap;
    }

    public void setBillTypeMap(final Map<String, String> billTypeMap) {
        this.billTypeMap = billTypeMap;
    }

    public Map<String, String> getBankAccountMap() {
        return bankAccountMap;
    }

    public void setBankAccountMap(final Map<String, String> bankAccountMap) {
        this.bankAccountMap = bankAccountMap;
    }

    @SkipValidation
    public String generateAdviceXls() throws JRException, IOException, ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting generateAdviceXls...");
        generateAdvice();
        inputStream = reportHelper.exportXls(inputStream, JASPER_PATH, getParamMap(), getAdviceList());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed generateAdviceXls.");
        return "bankAdvice-XLS";
    }

    public boolean isChequePrintEnabled() {

        String chequePrintEnabled = null;
        final List<AppConfigValues> enablePrintConfig = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "chequeprintingenabled");
        for (final AppConfigValues appConfigVal : enablePrintConfig)
            chequePrintEnabled = appConfigVal.getValue();
        if (chequePrintEnabled == null)
            return false;

        return "Y".equalsIgnoreCase(chequePrintEnabled);
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignment-bankAdviceExcel")
    public String bankAdviceExcel() throws JRException, IOException {
        BankAdviceReportInfo bankAdvice = new BankAdviceReportInfo();
        final InstrumentVoucher instrumentHdr = instrumentVoucherService.getInstrumentVoucherByVoucherHeader(instHeaderId);
        bankAdvice.setPartyName(instrumentHdr.getInstrumentHeaderId().getPayTo());
        bankAdvice.setAmount(instrumentHdr.getInstrumentHeaderId().getInstrumentAmount());
        final List<Object> data = new ArrayList<>();
        data.add(bankAdvice);

        setFileName(instrumentHdr.getVoucherHeaderId().getVoucherNumber() + "." + ReportFormat.XLS.toString().toLowerCase());
        inputStream = reportHelper.exportXls(getInputStream(), BANK_ADVICE_REPORT_PATH, null, data);
        return "bankAdvice-XLS";
    }

    protected String getMessage(final String key) {
        return getText(key);
    }

    protected String getMessage(final String key, final String[] value) {
        return getText(key, value);
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public void setPaymentService(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Integer getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(final Integer bankaccount) {
        this.bankaccount = bankaccount;
    }

    public List<InstrumentHeader> getInstHeaderList() {
        return instHeaderList;
    }

    public void setInstHeaderList(final List<InstrumentHeader> instHeaderList) {
        this.instHeaderList = instHeaderList;
    }

    public Integer getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(final Integer selectedRows) {
        this.selectedRows = selectedRows;
    }

    public List<ChequeAssignment> getChequeAssignmentList() {
        return chequeAssignmentList;
    }

    public void setChequeAssignmentList(final List<ChequeAssignment> chequeAssignmentList) {
        this.chequeAssignmentList = chequeAssignmentList;
    }

    public boolean isChequeNoGenerationAuto() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info(chequeNoGenerationAuto);
        return chequeNoGenerationAuto;
    }

    public void setChequeNoGenerationAuto(final boolean chequeNoGenerationAuto) {
        this.chequeNoGenerationAuto = chequeNoGenerationAuto;
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public Integer getBankbranch() {
        return bankbranch;
    }

    public void setBankbranch(final Integer bankbranch) {
        this.bankbranch = bankbranch;
    }

    public Date getChequeDt() {
        return chequeDt;
    }

    public void setChequeDt(final Date chequeDt) {
        this.chequeDt = chequeDt;
    }

    public String getInFavourOf() {
        return inFavourOf;
    }

    public void setInFavourOf(final String inFavourOf) {
        this.inFavourOf = inFavourOf;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(final Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public List<Object> getAdviceList() {
        return adviceList;
    }

    public List<InstrumentVoucher> getInstVoucherList() {
        return instVoucherList;
    }

    public void setInstVoucherList(final List<InstrumentVoucher> instVoucherList) {
        this.instVoucherList = instVoucherList;
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

    public List<InstrumentVoucher> getInstrumentVoucherList() {
        return instrumentVoucherList;
    }

    public void setInstrumentVoucherList(final List<InstrumentVoucher> instrumentVoucherList) {
        this.instrumentVoucherList = instrumentVoucherList;
    }

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(final String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public String[] getSurrender() {
        return surrender;
    }

    public void setSurrender(final String[] surrender) {
        this.surrender = surrender;
    }

    public String getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(final String bank_branch) {
        this.bank_branch = bank_branch;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(final String billType) {
        this.billType = billType;
    }

    public String getButton() {
        return button;
    }

    public void setButton(final String button) {
        this.button = button;
    }

    public void setChequeService(final ChequeService chequeService) {
        this.chequeService = chequeService;
    }

    public String[] getNewInstrumentNumber() {
        return newInstrumentNumber;
    }

    public void setNewInstrumentNumber(final String[] newInstrumentNumber) {
        this.newInstrumentNumber = newInstrumentNumber;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(final Integer department) {
        this.department = department;
    }

    public String getBank_account_dept() {
        return bank_account_dept;
    }

    public void setBank_account_dept(final String bank_account_dept) {
        this.bank_account_dept = bank_account_dept;
    }

    public String[] getNewInstrumentDate() {
        return newInstrumentDate;
    }

    public void setNewInstrumentDate(final String[] newInstrumentDate) {
        this.newInstrumentDate = newInstrumentDate;
    }

    public List<InstrumentHeader> getInstrumentHeaderList() {
        return instrumentHeaderList;
    }

    public void setInstrumentHeaderList(final List<InstrumentHeader> instrumentHeaderList) {
        this.instrumentHeaderList = instrumentHeaderList;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(final String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(final Date currentDate) {
        this.currentDate = currentDate;
    }

    public RecoveryService getRecoveryService() {
        return recoveryService;
    }

    public void setRecoveryService(final RecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    public Long getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(final Long recoveryId) {
        this.recoveryId = recoveryId;
    }

    public Map<String, String> getModeOfPaymentMap() {
        return modeOfPaymentMap;
    }

    public void setModeOfPaymentMap(final Map<String, String> modeOfPaymentMap) {
        this.modeOfPaymentMap = modeOfPaymentMap;
    }

    public String[] getSurrendarReasons() {
        return surrendarReasons;
    }

    public void setSurrendarReasons(final String[] surrendarReasons) {
        this.surrendarReasons = surrendarReasons;
    }

    public String[] getNewSerialNo() {
        return newSerialNo;
    }

    public void setNewSerialNo(final String[] newSerialNo) {
        this.newSerialNo = newSerialNo;
    }

    public Boolean getRtgsContractorAssignment() {
        return rtgsContractorAssignment;
    }

    public void setRtgsContractorAssignment(final Boolean rtgsContractorAssignment) {
        this.rtgsContractorAssignment = rtgsContractorAssignment;
    }

    public Map<Bankaccount, List<ChequeAssignment>> getAccountNoAndRtgsEntryMap() {
        return accountNoAndRtgsEntryMap;
    }

    public void setAccountNoAndRtgsEntryMap(
            final Map<Bankaccount, List<ChequeAssignment>> accountNoAndRtgsEntryMap) {
        this.accountNoAndRtgsEntryMap = accountNoAndRtgsEntryMap;
    }

    public boolean isRtgsNoGenerationAuto() {
        return rtgsNoGenerationAuto;
    }

    public void setRtgsNoGenerationAuto(final boolean rtgsNoGenerationAuto) {
        this.rtgsNoGenerationAuto = rtgsNoGenerationAuto;
    }

    public Map<String, String> getRtgsdateMap() {
        return rtgsdateMap;
    }

    public void setRtgsdateMap(final Map<String, String> rtgsdateMap) {
        this.rtgsdateMap = rtgsdateMap;
    }

    public Map<String, String> getRtgsRefNoMap() {
        return rtgsRefNoMap;
    }

    public void setRtgsRefNoMap(final Map<String, String> rtgsRefNoMap) {
        this.rtgsRefNoMap = rtgsRefNoMap;
    }

    public List<ChequeAssignment> getRtgsList() {
        return rtgsList;
    }

    public void setRtgsList(final List<ChequeAssignment> rtgsList) {
        this.rtgsList = rtgsList;
    }

    public Map<String, Boolean> getRtgsSeceltedAccMap() {
        return rtgsSeceltedAccMap;
    }

    public void setRtgsSeceltedAccMap(final Map<String, Boolean> rtgsSeceltedAccMap) {
        this.rtgsSeceltedAccMap = rtgsSeceltedAccMap;
    }

    public List<InstrumentHeader> getInstVoucherDisplayList() {
        return instVoucherDisplayList;
    }

    public void setInstVoucherDisplayList(
            final List<InstrumentHeader> instVoucherDisplayList) {
        this.instVoucherDisplayList = instVoucherDisplayList;
    }

    public Integer getDrawingOfficerId() {
        return drawingOfficerId;
    }

    public void setDrawingOfficerId(final Integer drawingOfficerId) {
        this.drawingOfficerId = drawingOfficerId;
    }

    public String getDrawingOfficerCode() {
        return drawingOfficerCode;
    }

    public void setDrawingOfficerCode(final String drawingOfficerCode) {
        this.drawingOfficerCode = drawingOfficerCode;
    }

    public Map<BankAccountRemittanceCOA, List<ChequeAssignment>> getAccountNoAndRemittanceRtgsEntryMap() {
        return accountNoAndRemittanceRtgsEntryMap;
    }

    public void setAccountNoAndRemittanceRtgsEntryMap(
            final Map<BankAccountRemittanceCOA, List<ChequeAssignment>> accountNoAndRemittanceRtgsEntryMap) {
        this.accountNoAndRemittanceRtgsEntryMap = accountNoAndRemittanceRtgsEntryMap;
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

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(final String bank_account) {
        this.bank_account = bank_account;
    }

    public String getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(final String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final String paymentId) {
        this.paymentId = paymentId;
    }

    public List<ChequeAssignment> getViewReceiptDetailsList() {
        return viewReceiptDetailsList;
    }

    public void setViewReceiptDetailsList(
            final List<ChequeAssignment> viewReceiptDetailsList) {
        this.viewReceiptDetailsList = viewReceiptDetailsList;
    }

    public BigDecimal getTotalDeductedAmount() {
        return totalDeductedAmount;
    }

    public void setTotalDeductedAmount(final BigDecimal totalDeductedAmount) {
        this.totalDeductedAmount = totalDeductedAmount;
    }

    public Boolean getNonSubledger() {
        return nonSubledger;
    }

    public void setNonSubledger(final Boolean nonSubledger) {
        this.nonSubledger = nonSubledger;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public boolean isContainsRTGS() {
        return containsRTGS;
    }

    public void setContainsRTGS(boolean containsRTGS) {
        this.containsRTGS = containsRTGS;
    }

    public Boolean getFunctionNonMandatory() {
        return functionNonMandatory;
    }

    public void setFunctionNonMandatory(Boolean functionNonMandatory) {
        this.functionNonMandatory = functionNonMandatory;
    }

    public Boolean getDeptNonMandatory() {
        return deptNonMandatory;
    }

    public void setDeptNonMandatory(Boolean deptNonMandatory) {
        this.deptNonMandatory = deptNonMandatory;
    }

    public List<CFinancialYear> getYearCodeList() {
        return yearCodeList;
    }

    public void setYearCodeList(List<CFinancialYear> yearCodeList) {
        this.yearCodeList = yearCodeList;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public boolean isChequePrintingEnabled() {
        return chequePrintingEnabled;
    }

    public void setChequePrintingEnabled(boolean chequePrintingEnabled) {
        this.chequePrintingEnabled = chequePrintingEnabled;
    }

    public String getChequePrintAvailableAt() {
        return chequePrintAvailableAt;
    }

    public void setChequePrintAvailableAt(String chequePrintAvailableAt) {
        this.chequePrintAvailableAt = chequePrintAvailableAt;
    }

    public String getInstrumentHeader() {
        return instrumentHeader;
    }

    public void setInstrumentHeader(String instrumentHeader) {
        this.instrumentHeader = instrumentHeader;
    }

    public String getChequeFormat() {
        return chequeFormat;
    }

    public void setChequeFormat(String chequeFormat) {
        this.chequeFormat = chequeFormat;
    }

    public Long getInstHeaderId() {
        return instHeaderId;
    }

    public void setInstHeaderId(Long instHeaderId) {
        this.instHeaderId = instHeaderId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSelectedRowsId() {
        return selectedRowsId;
    }

    public void setSelectedRowsId(String selectedRowsId) {
        this.selectedRowsId = selectedRowsId;
    }

    public List<InstrumentHeader> getTempInstrumentHeaderList() {
        return tempInstrumentHeaderList;
    }

    public void setTempInstrumentHeaderList(List<InstrumentHeader> tempInstrumentHeaderList) {
        this.tempInstrumentHeaderList = tempInstrumentHeaderList;
    }

}