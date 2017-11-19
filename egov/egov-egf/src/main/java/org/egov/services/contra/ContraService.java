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
package org.egov.services.contra;

import com.exilant.GLEngine.Transaxtion;
import org.apache.log4j.Logger;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.BankaccountDAO;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.contra.ContraBean;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.voucher.PayInBean;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.services.instrument.BankReconciliationService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.voucher.ContraJournalVoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author msahoo
 *
 */

public class ContraService extends PersistenceService<ContraJournalVoucher, Long>
{
    private static final Logger LOGGER = Logger.getLogger(ContraService.class);
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    @Qualifier("contraJournalVoucherService")
    private ContraJournalVoucherService contraJournalVoucherService;
    @Autowired
    @Qualifier("bankReconciliationService")
    private BankReconciliationService bankReconciliationService;

    @Autowired
    private ChartOfAccountsDAO coaDAO;
    @Autowired
    private BankaccountDAO bankAccountDAO;

    private InstrumentService instrumentService;
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    @Autowired
    protected EisCommonService eisCommonService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    private EmployeeServiceOld employeeServiceOld;
    private int preapprovalStatus = 0;

    private @Autowired EgovCommon egovCommon;

    public ContraService()  {
        super(ContraJournalVoucher.class);
    }

    public ContraService(Class<ContraJournalVoucher> type) {
        super(type);
    }

    public Position getPositionForWfItem(final ContraJournalVoucher rv)
    {
        return eisCommonService.getPositionByUserId(rv.getCreatedBy().getId());
    }

    public Department getDepartmentForUser(final User user)
    {
        return egovCommon.getDepartmentForUser(user, eisCommonService, employeeServiceOld, persistenceService);
    }

    public ContraJournalVoucher updateIntoContraJournal(final CVoucherHeader voucherHeader, final ContraBean contraBean) {
        ContraJournalVoucher existingCJV;
        try {
            existingCJV = contraJournalVoucherService.find("from ContraJournalVoucher where voucherHeaderId=?", voucherHeader);
            existingCJV.setToBankAccountId(bankAccountDAO.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId())));
            contraJournalVoucherService.update(existingCJV);
        } catch (final HibernateException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception occuerd while postiong into contractorJournal");
            throw new HibernateException(e);
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception occuerd while postiong into contractorJournal");
            throw new HibernateException(e);

        }
        return existingCJV;
    }

    public Bankreconciliation updateBankreconciliation(final InstrumentHeader instrHeader, final ContraBean contraBean) {
        Bankreconciliation existingBR;
        try {
            final Long iHeaderId = instrHeader.getId();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("instrHeader.getId() = " + iHeaderId);
            existingBR = bankReconciliationService.find("from Bankreconciliation where instrumentHeaderId=?", iHeaderId);
            existingBR.setAmount(contraBean.getAmount());
            existingBR.setBankaccount(bankAccountDAO.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId())));
            bankReconciliationService.update(existingBR);
        } catch (final HibernateException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception occuerd while updateBankreconciliation" + e);
            throw new HibernateException(e);
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception occuerd while updateBankreconciliation" + e);
            throw new HibernateException(e);

        }

        return existingBR;
    }

    public List<Transaxtion> postInTransaction(final CVoucherHeader voucherHeader, final ContraBean contraBean) {

        final List<Transaxtion> transaxtionList = new ArrayList<Transaxtion>();
        // VoucherDetail vDetailBank = new VoucherDetail();
        final Transaxtion transactionBank = new Transaxtion();
        final CChartOfAccounts bankAccountCode = bankAccountDAO.getBankaccountById(
                Integer.valueOf(contraBean.getAccountNumberId()))
                .getChartofaccounts();

        /*
         * vDetailBank.setLineId(1); vDetailBank.setVoucherHeaderId(voucherHeader);
         * vDetailBank.setGlCode(bankAccountCode.getGlcode()); vDetailBank.setAccountName(bankAccountCode.getName());
         * vDetailBank.setDebitAmount(contraBean.getAmount()); vDetailBank.setCreditAmount(BigDecimal.ZERO);
         * vDetailBank.setNarration(voucherHeader.getDescription());
         */

        transactionBank.setVoucherLineId("1");
        transactionBank.setGlCode(bankAccountCode.getGlcode());
        transactionBank.setGlName(bankAccountCode.getName());
        transactionBank.setVoucherHeaderId(voucherHeader.getId().toString());
        transactionBank.setDrAmount(contraBean.getAmount().toString());
        transactionBank.setCrAmount("0");

        // VoucherDetail vDetailCash = new VoucherDetail();
        final Transaxtion transactionCash = new Transaxtion();
        final CChartOfAccounts cashAccountCode = coaDAO.getCChartOfAccountsByGlCode(contraBean.getCashInHand());
        /*
         * vDetailCash.setLineId(2); vDetailCash.setVoucherHeaderId(voucherHeader);
         * vDetailCash.setGlCode(cashAccountCode.getGlcode()); vDetailCash.setAccountName(cashAccountCode.getName());
         * vDetailCash.setDebitAmount(BigDecimal.ZERO); vDetailCash.setCreditAmount(contraBean.getAmount());
         * vDetailCash.setNarration(voucherHeader.getDescription());
         */

        transactionCash.setVoucherLineId("2");
        transactionCash.setGlCode(cashAccountCode.getGlcode());
        transactionCash.setGlName(cashAccountCode.getName());
        transactionCash.setVoucherHeaderId(voucherHeader.getId().toString());
        transactionCash.setDrAmount("0");
        transactionCash.setCrAmount(contraBean.getAmount().toString());

        /*
         * try { vdPersitSer.persist(vDetailBank); vdPersitSer.persist(vDetailCash); } catch (HibernateException e) {
         * if(LOGGER.isDebugEnabled()) LOGGER.debug("Exception Occured in Contra Service while preparing transaction"+e); throw
         * new HibernateException(e); }
         */

        transaxtionList.add(transactionBank);
        transaxtionList.add(transactionCash);

        return transaxtionList;
    }

    public Map<String, Object> getCTBVoucher(final String voucherId, final ContraBean contraBean) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("ContraService | getCTBVoucher | Start");
        final Map<String, Object> voucherMap = new HashMap<String, Object>();
        final CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?",
                Long.valueOf(voucherId));
        voucherMap.put(Constants.VOUCHERHEADER, voucherHeader);
        final InstrumentVoucher iVoucher = (InstrumentVoucher) persistenceService.find(
                "from InstrumentVoucher where voucherHeaderId=?", voucherHeader);
        final Bankaccount bankAccount = iVoucher.getInstrumentHeaderId().getBankAccountId();
        contraBean.setAccountNumberId(bankAccount.getId().toString());
        contraBean.setAccnumnar(bankAccount.getNarration());
        contraBean.setBankBranchId(bankAccount.getBankbranch().getBank().getId() + "-" +
                bankAccount.getBankbranch().getId().toString());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Cash amount = " + iVoucher.getInstrumentHeaderId().getInstrumentAmount());
        contraBean.setAmount(iVoucher.getInstrumentHeaderId().getInstrumentAmount());
        contraBean.setChequeNumber(iVoucher.getInstrumentHeaderId().getTransactionNumber());
        if (iVoucher.getInstrumentHeaderId().getTransactionDate() != null)
            contraBean.setChequeDate(Constants.DDMMYYYYFORMAT2.format(iVoucher.getInstrumentHeaderId().getTransactionDate()));
        voucherMap.put("contrabean", contraBean);
        return voucherMap;

    }

    public Map<String, Object> getpayInSlipVoucher(final Long voucherId, final ContraBean contraBean, List<PayInBean> iHeaderList) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("ContraService | getpayInSlipVoucher | Start");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        final Map<String, Object> voucherMap = new HashMap<String, Object>();
        iHeaderList = new ArrayList<PayInBean>();
        final CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService
                .find("from CVoucherHeader where id=?", voucherId);
        voucherMap.put(Constants.VOUCHERHEADER, voucherHeader);
        final List<InstrumentOtherDetails> iOther = persistenceService.findAllBy(
                "from InstrumentOtherDetails where payinslipId=?", voucherHeader);
        final Bankaccount bankAccount = iOther.get(0).getInstrumentHeaderId().getBankAccountId();
        contraBean.setAccountNumberId(bankAccount.getId().toString());
        contraBean.setAccnumnar(bankAccount.getNarration());
        contraBean.setBankBranchId(bankAccount.getBankbranch().getBank().getId() + "-" +
                bankAccount.getBankbranch().getId().toString());
        voucherMap.put("contraBean", contraBean);
        PayInBean payInBean;
        BigDecimal totalInstrAmt = BigDecimal.ZERO;
        for (final InstrumentOtherDetails instrumentOtherDetails : iOther) {
            final InstrumentHeader iHeader = instrumentOtherDetails.getInstrumentHeaderId();
            int index = 0;
            payInBean = new PayInBean();
            payInBean.setInstId(Long.valueOf(iHeader.getId().toString()));
            payInBean.setInstrumentNumber(iHeader.getInstrumentNumber());
            try {
                payInBean.setInstrumentDate(formatter.format(formatter1.parse(iHeader.getInstrumentDate().toString())));
                final InstrumentVoucher iVoucher = (InstrumentVoucher) persistenceService.find(
                        "from InstrumentVoucher where instrumentHeaderId=?", iHeader);
                payInBean.setVoucherDate(formatter.format(formatter1.parse(iVoucher.getVoucherHeaderId().getVoucherDate()
                        .toString())));
            } catch (final ParseException e) {
                LOGGER.error("Exception Occured while Parsing instrument date" + e.getMessage());
            }
            payInBean.setInstrumentAmount(iHeader.getInstrumentAmount().toString());
            payInBean.setVoucherNumber(instrumentOtherDetails.getPayinslipId().getVoucherNumber());
            payInBean.setSelectChq(true);
            payInBean.setSerialNo(++index);
            iHeaderList.add(payInBean);
            totalInstrAmt = totalInstrAmt.add(iHeader.getInstrumentAmount());
        }
        voucherMap.put("iHeaderList", iHeaderList);
        voucherMap.put("totalInstrAmt", totalInstrAmt.toString());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("ContraService | getpayInSlipVoucher | End");
        return voucherMap;

    }

    /**
     * This method will be called for remit to bank in case of cheque/dd/card/atm/online deposit where a Contra Voucher is
     * generated
     * @param payInId
     * @param toBankaccountGlcode
     * @param instrumentHeader
     */

    public void updateCheque_DD_Card_Deposit(final Long payInId, final String toBankaccountGlcode,
            final InstrumentHeader instrumentHeader,
            final Map valuesMap)
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" updateCheque_DD_Card_Deposit | Start");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" updateCheque_DD_Card_Deposit for" + instrumentHeader + "and payin id" + payInId);
        final CVoucherHeader payIn = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", payInId);

        updateInstrumentAndPayin(
                payIn,
                (Bankaccount) valuesMap.get("depositedBankAccount"),
                instrumentHeader,
                (EgwStatus) persistenceService.find("from EgwStatus where id = ?",
                        Integer.valueOf(valuesMap.get("instrumentDepositedStatus").toString())));
        final ContraJournalVoucher cjv = addToContra(payIn, (Bankaccount) valuesMap.get("depositedBankAccount"), instrumentHeader);
        addToBankRecon(payIn, instrumentHeader, (EgwStatus) persistenceService.find("from EgwStatus where id = ?",
                Integer.valueOf(valuesMap.get("instrumentReconciledStatus").toString())));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" updateCheque_DD_Card_Deposit | End");
    }

    /**
     * Call this api before calling followin apis and send the map returned by this api into updateCheque_DD_Card_Deposit
     * updateCheque_DD_Card_Deposit_Receipt updateCashDeposit and
     * @param toBankaccountGlcode
     * @return
     */

    public Map prepareForUpdateInstrumentDeposit(final String toBankaccountGlcode)
    {
        final Map<String, Object> valuesMap = new HashMap<String, Object>();
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                "PREAPPROVEDVOUCHERSTATUS");
        preapprovalStatus = Integer.valueOf(configValuesByModuleAndKey.get(0).getValue());
        final EgwStatus instrumentDepositedStatus = (EgwStatus) persistenceService.find(
                "from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
                FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);

        final EgwStatus instrumentReconciledStatus = (EgwStatus) persistenceService.find(
                "from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
                FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
        final Bankaccount depositedBankAccount = (Bankaccount) persistenceService.find(
                "from Bankaccount where chartofaccounts.glcode=?", toBankaccountGlcode);
        valuesMap.put("preapprovalStatus", preapprovalStatus);
        valuesMap.put("instrumentDepositedStatus", instrumentDepositedStatus);
        valuesMap.put("instrumentReconciledStatus", instrumentReconciledStatus);
        valuesMap.put("depositedBankAccount", depositedBankAccount);
        return valuesMap;

    }

    /**
     * This method will be called for remit to bank in case of cheque/dd/card/atm/online deposit where a Receipt Voucher is
     * generated
     * @param payInId
     * @param toBankaccountGlcode
     * @param instrumentHeader
     */
    @Transactional
    public void updateCheque_DD_Card_Deposit_Receipt(final Long receiptId, final String toBankaccountGlcode,
            final InstrumentHeader instrumentHeader, final Map valuesMap)
    {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" updateCheque_DD_Card_Deposit_Receipt | Start");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" updateCheque_DD_Card_Deposit_Receipt for" + instrumentHeader + "and receiptId" + receiptId);
        final CVoucherHeader payIn = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", receiptId);
        // Bankaccount depositedBankAccount=(Bankaccount)
        // persistenceService.find("from Bankaccount where chartofaccounts.glcode=?",toBankaccountGlcode);
        updateInstrumentAndPayin(payIn, (Bankaccount) valuesMap.get("depositedBankAccount"), instrumentHeader,
                (EgwStatus) valuesMap.get("instrumentDepositedStatus"));
        addToBankRecon(payIn, instrumentHeader, (EgwStatus) valuesMap.get("instrumentReconciledStatus"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" updateCheque_DD_Card_Deposit_Receipt | End");
    }

    /**
     * used by modules which are integrating
     * @return
     */

    @Transactional
    public void updateCashDeposit(final Long payInId, final String toBankaccountGlcode, final InstrumentHeader instrumentHeader,
            final Map valuesMap)
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Contra Service | updateCashDeposit | Start");

        final List<AppConfigValues> appConfigList = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,"PREAPPROVEDVOUCHERSTATUS");

        if (null != appConfigList && !appConfigList.isEmpty())
            for (final AppConfigValues appConfigVal : appConfigList)
                preapprovalStatus = Integer.valueOf(appConfigVal.getValue());
        else
            throw new ApplicationRuntimeException("Appconfig value for PREAPPROVEDVOUCHERSTATUS is not defined in the system");

        final CVoucherHeader payIn = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", payInId);
        // Bankaccount depositedBankAccount=(Bankaccount)
        // persistenceService.find("from Bankaccount where chartofaccounts.glcode=?",toBankaccountGlcode);

        updateInstrumentAndPayin(
                payIn,
                (Bankaccount) valuesMap.get("depositedBankAccount"),
                instrumentHeader,
                (EgwStatus) persistenceService.find("from EgwStatus where id = ?",
                        Integer.valueOf(valuesMap.get("instrumentReconciledStatus").toString())));
        final ContraJournalVoucher cjv = addToContra(payIn, (Bankaccount) valuesMap.get("depositedBankAccount"), instrumentHeader);
        addToBankRecon(
                payIn,
                instrumentHeader,
                (EgwStatus) persistenceService.find("from EgwStatus where id = ?",
                        Integer.valueOf(valuesMap.get("instrumentReconciledStatus").toString())));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Contra Service | updateCashDeposit | End");
    }

    public void createVoucherfromPreApprovedVoucher(final ContraJournalVoucher cjv) {
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "APPROVEDVOUCHERSTATUS");
        final String approvedVoucherStatus = appList.get(0).getValue();
        cjv.getVoucherHeaderId().setStatus(Integer.valueOf(approvedVoucherStatus));
    }

    public void cancelVoucher(final ContraJournalVoucher cjv) {
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "cancelledstatus");
        final String approvedVoucherStatus = appList.get(0).getValue();
        cjv.getVoucherHeaderId().setStatus(Integer.valueOf(approvedVoucherStatus));
    }

    public String getDesginationName()
    {
        // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
        final Assignment assignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(ApplicationThreadLocals.getUserId(),
                new Date());
        return assignment.getDesignation().getName();
    }

    public Department getDepartmentForWfItem(final ContraJournalVoucher cjv)
    {
        // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
        final Assignment assignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(cjv.getCreatedBy().getId(),
                new Date());
        return assignment.getDepartment();
    }

    public Boundary getBoundaryForUser(final ContraJournalVoucher rv)
    {
        return egovCommon.getBoundaryForUser(rv.getCreatedBy());
    }

    public Position getPositionForEmployee(final Employee emp) throws ApplicationRuntimeException
    {
        return eisCommonService.getPrimaryAssignmentPositionForEmp(emp.getId());
    }

    @Transactional
    public void addToBankRecon(final CVoucherHeader payIn, final InstrumentHeader instrumentHeader,
            final EgwStatus instrumentReconciledStatus) {
        instrumentService.addToBankReconcilationWithLoop(payIn, instrumentHeader, instrumentReconciledStatus);

    }

    @Transactional
    public ContraJournalVoucher addToContra(final CVoucherHeader payIn, final Bankaccount depositedBank,
            final InstrumentHeader instrumentHeader) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Adding to contra");
        final ContraJournalVoucher cjv = new ContraJournalVoucher();
        cjv.setToBankAccountId(depositedBank);
        cjv.setInstrumentHeaderId(instrumentHeader);
        cjv.setVoucherHeaderId(payIn);
        contraJournalVoucherService.applyAuditing(cjv);
        contraJournalVoucherService.persist(cjv);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Adding to contra completed");
        return cjv;
    }

    @Transactional
    private void updateInstrumentAndPayin(final CVoucherHeader payIn, final Bankaccount account,
            final InstrumentHeader instrumentHeader,
            final EgwStatus status)
    {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateInstrumentAndPayin | Start");
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        // List<InstrumentHeader> iHeaderList = createInstruements();
        iMap.put("Instrument header", instrumentHeader);
        iMap.put("Payin slip id", payIn);
        iMap.put("Instrument status date", payIn.getVoucherDate());
        iMap.put("Status id", status);
        iMap.put("Bank account id", account);
        iList.add(iMap);
        instrumentService.updateInstrumentOtherDetails(iList);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateInstrumentAndPayin | End");
    }

    public Map prepareForUpdateInstrumentDepositSQL()
    {
        final Map<String, Object> valuesMap = new HashMap<String, Object>();
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                "PREAPPROVEDVOUCHERSTATUS");
        preapprovalStatus = Integer.valueOf(configValuesByModuleAndKey.get(0).getValue());
        final Integer instrumentDepositedStatusId = (Integer) persistenceService.find(
                "select id from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
                FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);

        final Integer instrumentReconciledStatusId = (Integer) persistenceService.find(
                "select id from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
                FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
        valuesMap.put("preapprovalStatus", preapprovalStatus);
        valuesMap.put("instrumentDepositedStatus", instrumentDepositedStatusId.longValue());
        valuesMap.put("instrumentReconciledStatus", instrumentReconciledStatusId.longValue());
        return valuesMap;

    }

    /**
     * This method will be called for remit to bank in case of cheque/dd/card/atm/online deposit where a Receipt Voucher is
     * generated This api will be used for only when it is called in loop
     *
     * @param isntrumentDetailsMap Map should contain with following No. key value 1. "instrumentheader" InstrumentHeader -
     * org.egov.model.instrument.InstrumentHeader 2. "bankaccountid" Integer - id of the BankAccount 3. "instrumentamount"
     * BigDecimal - instrumentamount 4. "instrumenttype" String - type of instrument (eg. Cheque,DD....) 5. "depositdate" Date -
     * Date of remittance 6. "createdby" Integer - userid who is depositing 7. "ispaycheque" boolean - saying whether it is
     * paymentcheque or receipt cheque 8. "payinid" Long - Voucher header id Map will also contain data returned by
     * prepareForUpdateInstrumentDepositSQL which should be called only once
     */

    public void updateCheque_DD_Card_Deposit_Receipt(final Map instrumentDetailsMap)
    {
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("Starting updateCheque_DD_Card_Deposit_ReceiptSql ");
        updateInstrumentAndPayinSql(instrumentDetailsMap);
        addToBankReconcilationSQL(instrumentDetailsMap);
    }

    /**
     *
     * @param isntrumentDetailsMap
     * @see public void updateCheque_DD_Card_Deposit_Receipt(Map isntrumentDetailsMap) fordetails
     */
    public void updateCheque_DD_Card_Deposit(final Map instrumentDetailsMap)
    {
        // if(LOGGER.isDebugEnabled()) LOGGER.debug(" updateCheque_DD_Card_Deposit | start");
        updateInstrumentAndPayinSql(instrumentDetailsMap);
        addToBankReconcilationSQL(instrumentDetailsMap);
        addToContraSql(instrumentDetailsMap);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug(" updateCheque_DD_Card_Deposit | End"+instrumentCount);
    }
    
    public void updateCheque_DD_Card_Deposit(final Map instrumentDetailsMap, CVoucherHeader cVoucherHeader, InstrumentHeader instrumentHeader, Bankaccount bankaccount)
    {
        updateInstrumentAndPayinSql(instrumentDetailsMap);
        addToBankReconcilationSQL(instrumentDetailsMap);
        addToContraJournal(cVoucherHeader,instrumentHeader,bankaccount);
    }

    /**
     * @see public void updateCheque_DD_Card_Deposit_Receipt(Map isntrumentDetailsMap) fordetails
     * @param isntrumentDetailsMap
     */
    public void updateCashDeposit(final Map instrumentDetailsMap)
    {
        updateInstrumentAndPayinSql(instrumentDetailsMap);
        addToBankReconcilationSQL(instrumentDetailsMap);
        addToContraSql(instrumentDetailsMap);
    }
    
    @Transactional
    public void updateCashDeposit(final Map instrumentDetailsMap, CVoucherHeader cVoucherHeader, InstrumentHeader instrumentHeader, Bankaccount bankaccount)
    {
        updateInstrumentAndPayinSql(instrumentDetailsMap);
        addToBankReconcilationSQL(instrumentDetailsMap);
        addToContraJournal(cVoucherHeader,instrumentHeader,bankaccount);
    }

    private void updateInstrumentAndPayinSql(final Map instrumentDetailsMap) {
        final String ioSql = "update EGF_INSTRUMENTOTHERDETAILS set PAYINSLIPID=:payinId,INSTRUMENTSTATUSDATE=:ihStatusDate," +
                " LASTMODIFIEDBY=:modifiedBy, LASTMODIFIEDDATE =:modifiedDate where INSTRUMENTHEADERID=:ihId";

        final SQLQuery ioSQLQuery = getSession().createSQLQuery(ioSql);
        ioSQLQuery.setLong("payinId", (Long) instrumentDetailsMap.get("payinid"))
                .setLong("ihId", (Long) instrumentDetailsMap.get("instrumentheader"))
                .setDate("ihStatusDate", (Date) instrumentDetailsMap.get("depositdate"))
                .setDate("modifiedDate", new Date())
                .setLong("modifiedBy", (Long) instrumentDetailsMap.get("createdby"));
        ioSQLQuery.executeUpdate();

        final String ihSql = "update EGF_instrumentheader  set ID_STATUS=:statusId,BANKACCOUNTID=:bankAccId,LASTMODIFIEDBY=:modifiedBy,"
                + " LASTMODIFIEDDATE =:modifiedDate where id=:ihId";

        final SQLQuery ihSQLQuery = getSession().createSQLQuery(ihSql);
        ihSQLQuery.setLong("statusId", (Long) instrumentDetailsMap.get("instrumentDepositedStatus"))
                .setLong("ihId", (Long) instrumentDetailsMap.get("instrumentheader"))
                .setLong("bankAccId", (Long) instrumentDetailsMap.get("bankaccountid"))
                .setDate("modifiedDate", new Date())
                .setLong("modifiedBy", (Long) instrumentDetailsMap.get("createdby"));
        ihSQLQuery.executeUpdate();

    }
    
    /**
     *
     * @param instrumentDetailsMap
     * @throws ApplicationRuntimeException
     *
     * Will update bank reconcilation and set isreconciled to true for the type 1. cash 2.ECS 3. bank challan 4. bank
     */
    public void addToBankReconcilationSQL(final Map instrumentDetailsMap)
            throws ApplicationRuntimeException {
        final String brsSql = "Insert into bankreconciliation (ID,BANKACCOUNTID,AMOUNT,TRANSACTIONTYPE,INSTRUMENTHEADERID) values "
                +
                " (nextVal('seq_bankreconciliation'),:bankAccId,:amount,:trType,:ihId)";
        final SQLQuery brsSQLQuery = getSession().createSQLQuery(brsSql);

        brsSQLQuery.setLong("bankAccId", (Long) instrumentDetailsMap.get("bankaccountid"))
                .setBigDecimal("amount", (BigDecimal) instrumentDetailsMap.get("instrumentamount"))
                .setString("trType", "1".equalsIgnoreCase((String) instrumentDetailsMap.get("ispaycheque")) ? "Cr" : "Dr")
                .setLong("ihId", (Long) instrumentDetailsMap.get("instrumentheader"));
        brsSQLQuery.executeUpdate();

        if (FinancialConstants.INSTRUMENT_TYPE_CASH.equalsIgnoreCase((String) instrumentDetailsMap.get("instrumenttype"))
                ||
                FinancialConstants.INSTRUMENT_TYPE_BANK.equalsIgnoreCase((String) instrumentDetailsMap.get("instrumenttype"))
                ||
                FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK.equalsIgnoreCase((String) instrumentDetailsMap
                        .get("instrumenttype")) ||
                FinancialConstants.INSTRUMENT_TYPE_ECS.equalsIgnoreCase((String) instrumentDetailsMap.get("instrumenttype")))
        {
            final String ioSql = "update EGF_instrumentOtherdetails set reconciledamount=:reconciledAmt,INSTRUMENTSTATUSDATE=:ihStatusDate,LASTMODIFIEDBY=:modifiedBy,"
                    +
                    " LASTMODIFIEDDATE =:modifiedDate where INSTRUMENTHEADERID=:ihId";

            final SQLQuery ioSQLQuery = getSession().createSQLQuery(ioSql);
            ioSQLQuery.setLong("ihId", (Long) instrumentDetailsMap.get("instrumentheader"))
                    .setBigDecimal("reconciledAmt", (BigDecimal) instrumentDetailsMap.get("instrumentamount"))
                    .setDate("ihStatusDate", (Date) instrumentDetailsMap.get("depositdate"))
                    .setDate("modifiedDate", new Date())
                    .setLong("modifiedBy", (Long) instrumentDetailsMap.get("createdby"));
            ioSQLQuery.executeUpdate();

            final String ihSql = "update EGF_instrumentheader  set ID_STATUS=:statusId,LASTMODIFIEDBY=:modifiedBy," +
                    " LASTMODIFIEDDATE =:modifiedDate where id=:ihId";
            final SQLQuery ihSQLQuery = getSession().createSQLQuery(ihSql);
            ihSQLQuery.setLong("statusId", (Long) instrumentDetailsMap.get("instrumentReconciledStatus"))
                    .setLong("ihId", (Long) instrumentDetailsMap.get("instrumentheader"))
                    .setDate("modifiedDate", new Date())
                    .setLong("modifiedBy", (Long) instrumentDetailsMap.get("createdby"));
            ihSQLQuery.executeUpdate();

        }

    }

    private void addToContraSql(final Map instrumentDetailsMap) {

        final String ioSql = "Insert into contrajournalvoucher (ID,VOUCHERHEADERID,FROMBANKACCOUNTID,TOBANKACCOUNTID,INSTRUMENTHEADERID"
                +
                " ,STATE_ID,CREATEDBY,LASTMODIFIEDBY) values " +
                " (nextVal('seq_contrajournalvoucher'),:vhId,null,:depositedBankId,:ihId,null,:createdBy,:createdBy)";
        final SQLQuery ioSQLQuery = getSession().createSQLQuery(ioSql);
        ioSQLQuery.setLong("vhId", (Long) instrumentDetailsMap.get("payinid"))
                .setLong("ihId", (Long) instrumentDetailsMap.get("instrumentheader"))
                .setLong("depositedBankId", (Long) instrumentDetailsMap.get("bankaccountid"))
                .setLong("createdBy", (Long) instrumentDetailsMap.get("createdby"));
        ioSQLQuery.executeUpdate();

    }
    
    @Transactional
    public void addToContraJournal(CVoucherHeader cVoucherHeader, InstrumentHeader instrumentHeader, Bankaccount bankaccount) {
        ContraJournalVoucher contraJournalVoucher = new ContraJournalVoucher();
        contraJournalVoucher.setVoucherHeaderId(cVoucherHeader);
        contraJournalVoucher.setInstrumentHeaderId(instrumentHeader);
        contraJournalVoucher.setToBankAccountId(bankaccount);
        persistenceService.persist(contraJournalVoucher);
    }

    @SuppressWarnings("unchecked")
    public void editInstruments(final Long voucherId) {

        final List<InstrumentOtherDetails> iOtherdetails = persistenceService.findAllBy(
                "from InstrumentOtherDetails  io where payinslipId.id=?", voucherId);

        for (final InstrumentOtherDetails instrumentOtherDetails : iOtherdetails) {
            instrumentService.editInstruments(instrumentOtherDetails);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Modifying " + instrumentOtherDetails);
        }
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /*
     * public PersistenceService<VoucherDetail, Long> getVdPersitSer() { return vdPersitSer; } public void
     * setVdPersitSer(PersistenceService<VoucherDetail, Long> vdPersitSer) { this.vdPersitSer = vdPersitSer; }
     */
    public InstrumentService getInstrumentService() {
        return instrumentService;
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public void setEmployeeServiceOld(final EmployeeServiceOld employeeServiceOld) {
        this.employeeServiceOld = employeeServiceOld;
    }
}