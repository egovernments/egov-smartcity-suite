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
package org.egov.egf.web.actions.contra;


import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Vouchermis;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.web.actions.voucher.BaseVoucherAction;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.contra.ContraBean;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.services.cheque.ChequeService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Results({
    @Result(name = Constants.VIEW, location = "contraBTC-" + Constants.VIEW + ".jsp"),
    @Result(name = ContraBTCAction.NEW, location = "contraBTC-" + ContraBTCAction.NEW + ".jsp"),
    @Result(name = "availableBalance", location = "contraBTC-availableBalance.jsp")
})
public class ContraBTCAction extends BaseVoucherAction {
    /**
     *
     */
    private static final long serialVersionUID = 8307131392372518372L;
    private static final String SOURCEPATH = "/EGF/contra/contraBTC!view.action?voucherHeader.id=";
    private ContraBean contraBean = new ContraBean();
    private EgovCommon egovCommon;
    private InstrumentService instrumentService;
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving Data";
    private static final String TRANSACTION_FAILED = "Transaction failed";

    @Autowired
    @Qualifier("contraJournalVoucherService")
    private PersistenceService<ContraJournalVoucher, Long> contraJournalVoucherService;
    private static final Logger LOGGER = Logger
            .getLogger(ContraBTCAction.class);
    private BigDecimal availableBalance = BigDecimal.ZERO;
    private boolean close = false;
    private String message = "";
    private Date reverseVoucherDate;
    private String reverseVoucherNumber;
    public PersistenceService<InstrumentHeader, Long> instrumentHeaderService;
    private VoucherService voucherService;
    InstrumentHeader instrumentHeader = null;
    private ChequeService chequeService;
    private String showMode;
    private String saveType;
    
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired CreateVoucher createVoucher;
    @Autowired
	private ChartOfAccounts chartOfAccounts;

    @Override
    public void prepare() {
        super.prepare();
        contraBean.setModeOfCollection("");
        voucherHeader.setName("BankToCash");
        voucherHeader.setType("Contra");
        addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
        addDropdownData("accNumList", Collections.EMPTY_LIST);
        setCashInHandAccount();
        loadSchemeSubscheme();
        loadBankAccountNumber(contraBean);
    }

    private void setCashInHandAccount() {
        final Map<String, Object> boundaryMap = egovCommon
                .getCashChequeInfoForBoundary();
        addDropdownData("boundaryLevelList", (List<Boundary>) boundaryMap
                .get("listBndryLvl"));
        contraBean
        .setCashInHand(boundaryMap.get("cashInHand") != null ? boundaryMap
                .get("cashInHand").toString()
                : null);
    }

    
    @Action(value = "/contra/contraBTC-create")
    public String create() {
        if (validateInputData()) {
            List<InstrumentHeader> instrumentList = new ArrayList<InstrumentHeader>();
            try {
                if (!showChequeNumber()) {
                    final Bankaccount bankAccount = getBank(Integer
                            .valueOf(contraBean.getAccountNumberId()));
                    contraBean.setChequeNumber(chequeService.nextChequeNumber(
                            bankAccount.getId().toString(), 1, getVouchermis()
                            .getDepartmentid().getId().intValue()));
                }
                instrumentList = instrumentService
                        .addToInstrument(createInstruments(contraBean));
                voucherHeader = createVoucher(voucherHeader);
                persistenceService.getSession().flush();
                updateInstrumentVoucherReference(instrumentList);
                saveContraJournalVoucher(instrumentList.get(0), voucherHeader);
                addActionMessage(getText("transaction.success")
                        + voucherHeader.getVoucherNumber());
                if (saveType.equals("saveAndClose"))
                    return saveAndClose();
                else if (saveType.equals("saveAndView"))
                    return saveAndView();
                else if (saveType.equals("saveAndNew"))
                    return saveAndNew();
                else
                    return NEW;

            } catch (final ValidationException e) {
                LOGGER.error("Validation error" + e.getMessage(), e);
                addActionMessage(getText(e.getErrors().get(0).getMessage()));
                return NEW;
            }
            // addActionMessage(getText("transaction.success") +
            // voucherHeader.getVoucherNumber());
            // return true;
        }
        return NEW;
        // return false;
    }

    
    public String saveAndView() {
        // if(create()){
        message = getText("transaction.success")
                + voucherHeader.getVoucherNumber();
        return Constants.VIEW;
    }

    
    public String saveAndClose() {
        // if(create()){
        setClose(true);
        message = getText("transaction.success")
                + voucherHeader.getVoucherNumber();
        return Constants.VIEW;
    }

    public String saveAndNew() {
        // if(create()){
        contraBean = new ContraBean();
        setCashInHandAccount();
        voucherHeader = new CVoucherHeader();
        return NEW;
    }

    @Action(value = "/contra/contraBTC-view")
    public String view() {
        if (voucherHeader != null && voucherHeader.getId() != null) {
            populateData();
            loadSchemeSubscheme();
        }
        return Constants.VIEW;
    }

    private void populateData() {
        voucherHeader = (CVoucherHeader) persistenceService.find(
                "from CVoucherHeader where id=?", voucherHeader.getId());
        final ContraJournalVoucher contraVoucher = (ContraJournalVoucher) persistenceService
                .find("from ContraJournalVoucher where voucherHeaderId.id=?",
                        voucherHeader.getId());
        contraBean.setAccountNumberId(contraVoucher.getFromBankAccountId()
                .getId().toString());
        final String fromBankAndBranchId = contraVoucher.getFromBankAccountId()
                .getBankbranch().getBank().getId().toString()
                + "-"
                + contraVoucher.getFromBankAccountId().getBankbranch().getId()
                .toString();
        contraBean.setBankBranchId(fromBankAndBranchId);
        loadBankAccountNumber(contraBean);
        final InstrumentHeader instrumentHeader = contraVoucher
                .getInstrumentHeaderId();
        contraBean.setChequeNumber(instrumentHeader.getInstrumentNumber());
        final String chequeDate = Constants.DDMMYYYYFORMAT2.format(instrumentHeader
                .getInstrumentDate());
        contraBean.setChequeDate(chequeDate);
        final BigDecimal instrumentAmount = instrumentHeader.getInstrumentAmount();
        contraBean.setAmount(instrumentAmount == null ? BigDecimal.ZERO
                .setScale(2) : instrumentAmount.setScale(2));
    }

    private void updateInstrumentVoucherReference(
            final List<InstrumentHeader> instrumentList) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        iMap.put("Instrument header", instrumentList.get(0));
        iMap.put("Voucher header", voucherHeader);
        iList.add(iMap);
        instrumentService.updateInstrumentVoucherReference(iList);
    }

    boolean validateInputData() {
        if (null == contraBean.getBankBranchId()
                || contraBean.getBankBranchId().equalsIgnoreCase("-1")) {
            addActionError(getText("contra.validate.bank"));
            return false;
        }
        if (null == contraBean.getAccountNumberId()
                || contraBean.getAccountNumberId().equalsIgnoreCase("-1")) {
            addActionError(getText("contra.validate.accnum"));
            return false;
        }
        if (null == contraBean.getAmount()
                || contraBean.getAmount().compareTo(BigDecimal.ZERO)==0) {
            addActionError(getText("contra.validate.amt"));
            return false;
        }
        if (showChequeNumber()
                && (null == contraBean.getChequeDate()
                || contraBean.getChequeNumber() == null || !validateChequeNumber())) {
            addActionError(getText("contra.invalid.cheque.number"));
            return false;
        }
        if (shouldShowHeaderField("vouchernumber")
                && StringUtils.isBlank(voucherHeader.getVoucherNumber())) {
            addActionError(getText("contra.invalid.voucher.number"));
            return false;
        }
        if (shouldShowHeaderField("vouchernumber")
                && voucherHeader.getVoucherDate().compareTo(new Date()) >= 1) {
            addActionError(getText("contra.invalid.voucher.date"));
            return false;
        }
        final BigDecimal cashBalance = getCashBalance();
        if (cashBalance.compareTo(contraBean.getAmount()) == -1
                && isBankBalanceMandatory()) {
            addActionError(getText("contra.insufficient.bankbalance",
                    new String[] { "" + cashBalance }));
            return false;
        }
        contraBean.setAccountBalance(cashBalance);
        return true;
    }

    protected BigDecimal getCashBalance() {
        BigDecimal accountBalance;
        try {
            accountBalance = egovCommon.getAccountBalance(voucherHeader
                    .getVoucherDate(), Long.valueOf(contraBean
                            .getAccountNumberId()));
        } catch (final ValidationException e) {
            LOGGER.error("Error in retriving" + e.getMessage(), e);
            accountBalance = BigDecimal.valueOf(-1);
        }
        return accountBalance;

    }

    private boolean validateChequeNumber() {
        if (instrumentHeader == null
                && !instrumentService.isChequeNumberValid(contraBean
                        .getChequeNumber(), Long.parseLong(contraBean
                                .getAccountNumberId()), voucherHeader.getVouchermis()
                                .getDepartmentid().getId().intValue(), null))
            return false;
        else if (instrumentHeader != null
                && !contraBean.getChequeNumber().equalsIgnoreCase(
                        instrumentHeader.getInstrumentNumber())
                        && !instrumentService.isChequeNumberValid(contraBean
                                .getChequeNumber(), Long.parseLong(contraBean
                                        .getAccountNumberId()), voucherHeader.getVouchermis()
                                        .getDepartmentid().getId().intValue(), null))
            return false;
        return true;
    }

    @Override
    public String execute() throws Exception {
        return NEW;
    }

    @Action(value = "/contra/contraBTC-newForm")
    public String newForm() throws Exception {
        return NEW;
    }

    public Vouchermis getVouchermis() {
        return voucherHeader.getVouchermis();
    }

    public void setVouchermis(final Vouchermis vouchermis) {
        voucherHeader.setVouchermis(vouchermis);
    }

    public void setContraBean(final ContraBean contraBean) {
        this.contraBean = contraBean;
    }

    public ContraBean getContraBean() {
        return contraBean;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    private List<Map<String, Object>> createInstruments(final ContraBean cBean) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        if (!showChequeNumber()) {
            final Bankaccount bankAccount = getBank(Integer.valueOf(contraBean
                    .getAccountNumberId()));
            cBean.setChequeNumber(chequeService.nextChequeNumber(bankAccount
                    .getId().toString(), 1, getVouchermis().getDepartmentid()
                    .getId().intValue()));
        }
        iMap.put("Instrument number", cBean.getChequeNumber());
        Date dt = null;
        try {
            dt = Constants.DDMMYYYYFORMAT2.parse(contraBean.getChequeDate());
        } catch (final ParseException e) {
            LOGGER.error("Parse Error" + e.getMessage(), e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
        iMap.put("Instrument date", dt);
        iMap.put("Instrument amount", Double.valueOf(cBean.getAmount()
                .toString()));
        iMap.put("Instrument type", FinancialConstants.INSTRUMENT_TYPE_CHEQUE);
        final Bankaccount bankAccount = getBank(Integer.valueOf(cBean
                .getAccountNumberId()));
        iMap.put("Bank code", bankAccount.getBankbranch().getBank().getCode());
        iMap.put("Bank branch name", bankAccount.getBankbranch()
                .getBranchaddress1());
        iMap.put("Bank account id", bankAccount.getId());
        iMap.put("Is pay cheque", "1");
        iList.add(iMap);
        return iList;
    }

    private Bankaccount getBank(final Integer id) {
        return (Bankaccount) persistenceService.find(
                "from Bankaccount where id=?", id);
    }

    ContraJournalVoucher saveContraJournalVoucher(
            final InstrumentHeader instrumentHeader, final CVoucherHeader vh) {
        final ContraJournalVoucher cjv = new ContraJournalVoucher();
        cjv.setInstrumentHeaderId(instrumentHeader);
        cjv.setVoucherHeaderId(vh);
        final Bankaccount bankAccount = getBank(Integer.valueOf(contraBean
                .getAccountNumberId()));
        cjv.setFromBankAccountId(bankAccount);
        cjv.setToBankAccountId(getCashBankAccount());
        contraJournalVoucherService.persist(cjv);
        return cjv;
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    Bankaccount getCashBankAccount() {
        return (Bankaccount) persistenceService.find(
                "from Bankaccount where chartofaccounts.glcode=?", contraBean
                .getCashInHand());
    }

    @Action(value = "/contra/contraBTC-ajaxAvailableBalance")
    public String ajaxAvailableBalance() {
        if (parameters.get("accountNumberId")[0] != null
                && !"-1".equals(parameters.get("accountNumberId")[0])
                && voucherHeader.getVoucherDate() != null)
            try {
                availableBalance = egovCommon.getAccountBalance(voucherHeader
                        .getVoucherDate(), Long.valueOf(parameters
                                .get("accountNumberId")[0]));
            } catch (final Exception e) {
                LOGGER.error("Error in retriving balance" + e.getMessage(), e);
                availableBalance = BigDecimal.valueOf(-1);
            }
        return "availableBalance";
    }

    public void setAvailableBalance(final BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    private CVoucherHeader createVoucher(final CVoucherHeader voucher) {
        try {
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
            headerDetails.put(VoucherConstant.SOURCEPATH, SOURCEPATH);
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
            final List<HashMap<String, Object>> accountdetails = populateAccountDetails();
            voucherHeader = createVoucher(headerDetails, subledgerDetails,
                    accountdetails);
            voucherHeader.getVouchermis().setSourcePath(
                    SOURCEPATH + voucherHeader.getId());
        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
        } catch (final ValidationException e) {
            throw e;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        }
        return voucherHeader;
    }

    CVoucherHeader createVoucher(final HashMap<String, Object> headerDetails,
            final List<HashMap<String, Object>> subledgerDetails,
            final List<HashMap<String, Object>> accountdetails) {
         
        return createVoucher.createVoucher(headerDetails, accountdetails, subledgerDetails);
    }

    private List<HashMap<String, Object>> populateAccountDetails() {
        final Bankaccount bankAccount = getBank(Integer.valueOf(contraBean
                .getAccountNumberId()));
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        accountdetails.add(populateDetailMap(contraBean.getCashInHand(),
                BigDecimal.ZERO, contraBean.getAmount()));
        accountdetails.add(populateDetailMap(bankAccount.getChartofaccounts()
                .getGlcode(), contraBean.getAmount(), BigDecimal.ZERO));
        return accountdetails;
    }

    HashMap<String, Object> populateDetailMap(final String glCode,
            final BigDecimal creditAmount, final BigDecimal debitAmount) {
        final HashMap<String, Object> detailMap = new HashMap<String, Object>();
        detailMap.put(VoucherConstant.CREDITAMOUNT, creditAmount.toString());
        detailMap.put(VoucherConstant.DEBITAMOUNT, debitAmount.toString());
        detailMap.put(VoucherConstant.GLCODE, glCode);
        return detailMap;
    }

    @Override
    public boolean shouldShowHeaderField(final String field) {
        return super.shouldShowHeaderField(field);
    }

    public void setClose(final boolean close) {
        this.close = close;
    }

    public boolean isClose() {
        return close;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String reverseAndView() {
        saveReverse();
        message = getText("transaction.success")
                + voucherHeader.getVoucherNumber();
        return Constants.VIEW;
    }

    public String reverseAndClose() {
        saveReverse();
        close = true;
        message = getText("transaction.success")
                + voucherHeader.getVoucherNumber();
        return Constants.VIEW;
    }

    public String reverse() {
        if (voucherHeader != null && voucherHeader.getId() != null)
            populateData();
        return "reverse";
    }

    public String saveReverse() {
        CVoucherHeader reversalVoucher = null;
        final HashMap<String, Object> reversalVoucherMap = new HashMap<String, Object>();
        reversalVoucherMap.put("Original voucher header id", voucherHeader
                .getId());
        reversalVoucherMap.put("Reversal voucher type", "Contra");
        reversalVoucherMap.put("Reversal voucher name", "BankToCash");
        reversalVoucherMap.put("Reversal voucher date", reverseVoucherDate);
        reversalVoucherMap.put("Reversal voucher number", reverseVoucherNumber);
        final List<HashMap<String, Object>> reversalList = new ArrayList<HashMap<String, Object>>();
        reversalList.add(reversalVoucherMap);
        try {
            reversalVoucher =  createVoucher.reverseVoucher(reversalList);
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            addActionError(getText(e.getErrors().get(0).getMessage()));
            return "reverse";
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            addActionError(getText(e.getMessage()));
            return "reverse";
        } catch (final ParseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "Date is not in proper Format",
                    "Date is not in proper Format")));
        }
        addActionMessage(getText("transaction.success")
                + reversalVoucher.getVoucherNumber());
        voucherHeader = reversalVoucher;
        return Constants.VIEW;
    }

    public void setReverseVoucherDate(final Date reversalVoucherDate) {
        reverseVoucherDate = reversalVoucherDate;
    }

    public Date getReverseVoucherDate() {
        return reverseVoucherDate;
    }

    public void setReverseVoucherNumber(final String reverseVoucherNumber) {
        this.reverseVoucherNumber = reverseVoucherNumber;
    }

    public String getReverseVoucherNumber() {
        return reverseVoucherNumber;
    }

    @Action(value = "/contra/contraBTC-redirect")
    public String redirect() {
        showMode = parameters.get("showMode")[0];
        if (showMode == null || StringUtils.isBlank(showMode)
                || voucherHeader == null || voucherHeader.getId() == null)
            return Constants.VIEW;
        populateData();
        return showMode;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @Action(value = "/contra/contraBTC-edit")
    public String edit() {
        final ContraJournalVoucher contraVoucher = (ContraJournalVoucher) persistenceService
                .find("from ContraJournalVoucher where voucherHeaderId.id=?",
                        voucherHeader.getId());
        instrumentHeader = contraVoucher.getInstrumentHeaderId();
        if (validateInputData())
            try {
                final CVoucherHeader oldVoucher = voucherService.updateVoucherHeader(
                        voucherHeader, "Contra");
                final InstrumentVoucher instrumentVoucher = (InstrumentVoucher) persistenceService
                        .find(
                                "from InstrumentVoucher where voucherHeaderId.id=?",
                                oldVoucher.getId());
                if (instrumentVoucher == null)
                    throw new ValidationException(
                            Arrays
                            .asList(new ValidationError(
                                    " System Error :Instrument is not linked with voucher",
                                    "")));
                final InstrumentHeader oldInstrumentHeader = instrumentVoucher
                        .getInstrumentHeaderId();
                instrumentService.cancelInstrument(oldInstrumentHeader);
                persistenceService.getSession().flush();
                final List<InstrumentHeader> instrument = instrumentService
                        .addToInstrument(createInstruments(contraBean));
                persistenceService.getSession().flush();
                contraVoucher.setFromBankAccountId(getBank(Integer
                        .valueOf(contraBean.getAccountNumberId())));
                contraVoucher.setInstrumentHeaderId(instrument.get(0));
                contraJournalVoucherService.persist(contraVoucher);
                createLedgerAndPost(oldVoucher);
                addActionMessage(getText("transaction.success")
                        + oldVoucher.getVoucherNumber());
                return Constants.VIEW;
            } catch (final Exception e) {
                LOGGER.error(e.getMessage(), e);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exp", e.getMessage()));
                throw new ValidationException(errors);
            }
        return "edit";
    }

    void createLedgerAndPost(final CVoucherHeader voucher) {
        try {
            createVoucher.deleteVoucherdetailAndGL(voucher);
            persistenceService.getSession().flush();
            final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
            final Bankaccount bankAccount = getBank(Integer.valueOf(contraBean
                    .getAccountNumberId()));
            accountdetails.add(populateDetailMap(contraBean.getCashInHand(),
                    contraBean.getAmount(), BigDecimal.ZERO));
            accountdetails.add(populateDetailMap(bankAccount
                    .getChartofaccounts().getGlcode(), BigDecimal.ZERO,
                    contraBean.getAmount()));
             
            final List<Transaxtion> transactions = createVoucher.createTransaction(null,
                    accountdetails, subledgerDetails, voucher);
            persistenceService.getSession().flush();
            Transaxtion txnList[] = new Transaxtion[transactions.size()];
            txnList = transactions.toArray(txnList);
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            if (!chartOfAccounts.postTransaxtions(txnList, formatter.format(voucherHeader
                    .getVoucherDate())))
                throw new ValidationException(
                        Arrays
                        .asList(new ValidationError(
                                EXCEPTION_WHILE_SAVING_DATA,
                                TRANSACTION_FAILED)));
        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        }
    }

    public void modifyInstrument(final InstrumentHeader ih,
            final CVoucherHeader vh) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        iMap.put("Instrument header", ih);
        iMap.put("Voucher header", vh);
        iList.add(iMap);
        instrumentService.modifyInstrumentVoucher(iList);
    }

    public boolean showChequeNumber() {
        return egovCommon.isShowChequeNumber();
    }

    public void setChequeService(final ChequeService chequeService) {
        this.chequeService = chequeService;
    }

    public String getShowMode() {
        return showMode;
    }

    public void setShowMode(final String showMode) {
        this.showMode = showMode;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(final String saveType) {
        this.saveType = saveType;
    }
}