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
package org.egov.services.instrument;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.models.ECSType;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.cheque.AccountCheques;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentAccountCodes;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.instrument.InstrumentType;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_ADVICE;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_ATM;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_BANK;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_CARD;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_CASH;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_CHEQUE;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_DD;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_ECS;
import static org.egov.utils.FinancialConstants.INSTRUMENT_TYPE_ONLINE;

@Transactional(readOnly = true)
public class InstrumentService {

    public static final String STATUS_ID = "Status id";
    public static final String RECONCILED_AMOUNT = "Reconciled amount";
    public static final String INSTRUMENT_STATUS_DATE = "Instrument status date";
    public static final String PAYIN_SLIP_ID = "Payin slip id";
    public static final String VOUCHER_HEADER = "Voucher header";
    public static final String INSTRUMENT_HEADER = "Instrument header";
    public static final String BRANCH_NAME = "Bank branch name";
    public static final String PAYEE_NAME = "Payee name";
    public static final String PAY_TO = "Pay to";
    public static final String DETAIL_TYPE_ID = "Detail type id";
    public static final String DETAIL_KEY_ID = "Detail key id";
    public static final String BANK_CODE = "Bank code";
    public static final String INSTRUMENT_TYPE = "Instrument type";
    public static final String INSTRUMENT_AMOUNT = "Instrument amount";
    public static final String INSTRUMENT_DATE = "Instrument date";
    public static final String INSTRUMENT_NUMBER = "Instrument number";
    public static final String INSTRUMENT_SERIALNO = "Instrument serialNo";
    public static final String IS_NULL = "is null";
    public static final String IS_PAYCHECK = "Is pay cheque";
    public static final String BANKACCOUNTID = "Bank account id";
    public static final String ECSTYPE = "ECS Type id";

    public static final String TRANSACTION_NUMBER = "Transaction number";
    public static final String TRANSACTION_DATE = "Transaction date";
    private static final Logger LOGGER = Logger
            .getLogger(InstrumentService.class);
    @Autowired
    @Qualifier("instrumentHeaderService")
    private InstrumentHeaderService instrumentHeaderService;
    @Autowired
    @Qualifier("instrumentVoucherService")
    private InstrumentVoucherService instrumentVoucherService;
    @Autowired
    @Qualifier("instrumentAccountCodesService")
    private InstrumentAccountCodesService instrumentAccountCodesService;
    @Autowired
    @Qualifier("instrumentTypeService")
    private InstrumentTypeService instrumentTypeService;
    @Autowired
    @Qualifier("instrumentOtherDetailsService")
    private InstrumentOtherDetailsService instrumentOtherDetailsService;
    @Autowired
    @Qualifier("bankReconciliationService")
    private BankReconciliationService bankReconciliationService;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    // Business methods
    /**
     * Accepts the list of instruments and save the same to instrument object The values that needs to be passed are:<br>
     * <b> Instrument number, Instrument date, Instrument amount, Instrument type, payee name,Bank code,Bank account,Bank branch
     * name,Is pay cheque </b>
     *
     * @param paramList for <I> receipts Is pay cheque will be '0' and for payments '1' </I>
     * @return List Of Saved Instrument Objects 1.check for receipt or payment with isPaycheque value 2.Find the instrument type
     * and based on type check for mandatory fields 3.set all non mandatory fields if they are provided
     */
    @Transactional
    public List<InstrumentHeader> addToInstrument(
            final List<Map<String, Object>> paramList) {
        final List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
        InstrumentHeader instrHeader;
        InstrumentType instrumentType;
        if (paramList != null)
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("length of paramlist " + paramList.size());
        for (final Map<String, Object> instrMap : paramList) {
            instrHeader = new InstrumentHeader();

            // 1.check for receipt or payment with isPaycheque value
            if (instrMap.get(IS_PAYCHECK) == null)
                throw new IllegalArgumentException(IS_PAYCHECK + IS_NULL);
            else if (!instrMap.get(IS_PAYCHECK).equals(
                    FinancialConstants.IS_PAYCHECK_ZERO)
                    && !instrMap.get(IS_PAYCHECK).equals(
                            FinancialConstants.IS_PAYCHECK_ONE))
                throw new ApplicationRuntimeException("value for "
                        + IS_PAYCHECK + "should be either"
                        + FinancialConstants.IS_PAYCHECK_ZERO + "or "
                        + FinancialConstants.IS_PAYCHECK_ONE);
            else
                instrHeader
                        .setIsPayCheque(instrMap.get(IS_PAYCHECK).toString());

            // 2.Find the instrument type and based on type check for mandatory
            // fields
            if (instrMap.get(INSTRUMENT_TYPE) == null)
                throw new IllegalArgumentException(INSTRUMENT_TYPE + IS_NULL);
            else {
                instrumentType = getInstrumentTypeByType(instrMap.get(
                        INSTRUMENT_TYPE).toString());
                if (instrumentType == null)
                    throw new ApplicationRuntimeException(INSTRUMENT_TYPE + "'"
                            + instrMap.get(INSTRUMENT_TYPE).toString()
                            + "' is not defined in the system ");
                else
                    instrHeader.setInstrumentType(instrumentType);

            }
            if (instrMap.get(INSTRUMENT_AMOUNT) == null)
                throw new IllegalArgumentException(INSTRUMENT_AMOUNT + IS_NULL);
            else
                try {
                    instrHeader.setInstrumentAmount(new BigDecimal(instrMap
                            .get(INSTRUMENT_AMOUNT).toString()));
                } catch (final NumberFormatException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new ApplicationRuntimeException(INSTRUMENT_AMOUNT
                            + "is not numeric");
                }

            switch (instrumentType.getType()) {
            case INSTRUMENT_TYPE_CHEQUE:
                validateAndAssignCheque(instrHeader, instrMap);
                break;
            case INSTRUMENT_TYPE_DD:
                validateAndAssignCheque(instrHeader, instrMap);
                break;
            case INSTRUMENT_TYPE_CARD:
                validateAndAssignCard(instrHeader, instrMap);
                break;
            case INSTRUMENT_TYPE_ONLINE:
                validateAndAssignCard(instrHeader, instrMap);
                break;
            case INSTRUMENT_TYPE_ATM:
                validateAndAssingATM(instrHeader, instrMap);
                break;
            case INSTRUMENT_TYPE_ADVICE:
                validateAndAssingnAdvice(instrHeader, instrMap);
                break;
            case INSTRUMENT_TYPE_BANK:
                validateAndAssingnAdvice(instrHeader, instrMap);
                break;

            case INSTRUMENT_TYPE_CASH:
                validateAndAssignCash(instrHeader, instrMap);
                break;
            case INSTRUMENT_TYPE_BANK_TO_BANK:
                validateAndAssignCash(instrHeader, instrMap);
                break;
            case INSTRUMENT_TYPE_ECS:
                validateAndAssignEcs(instrHeader, instrMap);

            }

            // set all non mandatory fields
            instrHeader.setPayee(instrMap.get(PAYEE_NAME) != null ? instrMap
                    .get(PAYEE_NAME).toString() : null);
            instrHeader.setPayTo(instrMap.get(PAY_TO) != null ? instrMap.get(
                    PAY_TO).toString() : null);
            if (instrMap.get(DETAIL_TYPE_ID) != null) {
                final Accountdetailtype detailType = (Accountdetailtype) persistenceService
                        .find("from Accountdetailtype where id=?", Integer
                                .parseInt(instrMap.get(DETAIL_TYPE_ID)
                                        .toString()));
                instrHeader.setDetailTypeId(detailType);
            }
            instrHeader
                    .setDetailKeyId(instrMap.get(DETAIL_KEY_ID) != null ? (Long) instrMap
                            .get(DETAIL_KEY_ID) : null);
            instrHeader
                    .setBankBranchName(instrMap.get(BRANCH_NAME) != null ? instrMap
                            .get(BRANCH_NAME).toString() : null);
            final EgwStatus status = (EgwStatus) persistenceService
                    .find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper('"
                            + FinancialConstants.INSTRUMENT_CREATED_STATUS
                            + "')");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Created Status of Instrument"
                        + status.getDescription());
            instrHeader.setStatusId(status);
            // in Transaction Number is present then Transaction Date is
            // Mandatory
            instrHeader.setCreatedDate(new Date());

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("saving instrument details as "
                        + instrHeader.toString());
            instrumentHeaderService.persist(instrHeader);
            final InstrumentOtherDetails io = new InstrumentOtherDetails();
            io.setInstrumentHeaderId(instrHeader);
            if (instrHeader.getTransactionDate() != null)
                io.setInstrumentStatusDate(instrHeader.getTransactionDate());
            else
                io.setInstrumentStatusDate(instrHeader.getInstrumentDate());
            instrumentOtherDetailsService.persist(io);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Saved " + io);
            instrList.add(instrHeader);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("returning " + instrList.size() + " instruments");
        return instrList;
    }

    private void validateAndAssignEcs(InstrumentHeader instrHeader,
            final Map<String, Object> instrMap) {
        if (instrMap.get(ECSTYPE) != null) {
            final ECSType ecsType = getECSType(instrMap.get(ECSTYPE).toString());
            if (ecsType == null)
                throw new ApplicationRuntimeException(ECSTYPE + "'"
                        + instrMap.get(ECSTYPE).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setECSType(ecsType);
        }

        if (instrMap.get(TRANSACTION_NUMBER) != null)
            instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER)
                    .toString());
        else
            throw new IllegalArgumentException(TRANSACTION_NUMBER + IS_NULL);
        if (instrMap.get(TRANSACTION_DATE) == null)
            throw new IllegalArgumentException(TRANSACTION_DATE + IS_NULL);
        else if (new Date().compareTo((Date) instrMap.get(TRANSACTION_DATE)) == -1)
            throw new IllegalArgumentException(TRANSACTION_DATE
                    + "cannot be future Date");
        else
            instrHeader.setTransactionDate((Date) instrMap
                    .get(TRANSACTION_DATE));
    }

    private void validateAndAssignCash(InstrumentHeader instrHeader,
            final Map<String, Object> instrMap) {
        if (instrMap.get(BANKACCOUNTID) != null) {
            final Bankaccount bankaccount = getBankaccount(instrMap.get(
                    BANKACCOUNTID).toString());
            if (bankaccount == null)
                throw new ApplicationRuntimeException(BANKACCOUNTID + "'"
                        + instrMap.get(BANKACCOUNTID).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setBankAccountId(bankaccount);
        }
        if (instrMap.get(BANK_CODE) != null) {
            final Bank bank = getBank(instrMap.get(BANK_CODE).toString());
            if (bank == null)
                throw new ApplicationRuntimeException(BANK_CODE + "'"
                        + instrMap.get(BANK_CODE).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setBankId(bank);
        }
        if (instrMap.get(TRANSACTION_NUMBER) != null)
            instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER)
                    .toString());

        if (instrMap.get(TRANSACTION_DATE) != null)
            if (new Date().compareTo((Date) instrMap.get(TRANSACTION_DATE)) == -1)
                throw new IllegalArgumentException(TRANSACTION_DATE
                        + "cannot be future Date");
            else
                instrHeader.setTransactionDate((Date) instrMap
                        .get(TRANSACTION_DATE));
    }

    private void validateAndAssingnAdvice(InstrumentHeader instrHeader,
            final Map<String, Object> instrMap) {
        if (instrMap.get(TRANSACTION_NUMBER) != null)
            instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER)
                    .toString());
        else
            throw new IllegalArgumentException(TRANSACTION_NUMBER + IS_NULL);
        if (instrMap.get(TRANSACTION_DATE) == null)
            throw new IllegalArgumentException(TRANSACTION_DATE + IS_NULL);
        else if (new Date().compareTo((Date) instrMap.get(TRANSACTION_DATE)) == -1)
            throw new IllegalArgumentException(TRANSACTION_DATE
                    + "cannot be future Date");
        else
            instrHeader.setTransactionDate((Date) instrMap
                    .get(TRANSACTION_DATE));
        if (instrMap.get(BANKACCOUNTID) == null)
            throw new IllegalArgumentException(BANKACCOUNTID + IS_NULL);
        if (instrMap.get(BANKACCOUNTID) != null) {
            final Bankaccount bankaccount = getBankaccount(instrMap.get(
                    BANKACCOUNTID).toString());
            if (bankaccount == null)
                throw new ApplicationRuntimeException(BANKACCOUNTID + "'"
                        + instrMap.get(BANKACCOUNTID).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setBankAccountId(bankaccount);
        }
    }

    private void validateAndAssingATM(InstrumentHeader instrHeader,
            final Map<String, Object> instrMap) {
        if (instrMap.get(TRANSACTION_NUMBER) != null)
            instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER)
                    .toString());
        else
            throw new IllegalArgumentException(TRANSACTION_NUMBER + IS_NULL);
        if (instrMap.get(TRANSACTION_DATE) == null)
            throw new IllegalArgumentException(TRANSACTION_DATE + IS_NULL);
        else if (new Date().compareTo((Date) instrMap.get(TRANSACTION_DATE)) == -1)
            throw new IllegalArgumentException(TRANSACTION_DATE
                    + "cannot be future Date");
        else
            instrHeader.setTransactionDate((Date) instrMap
                    .get(TRANSACTION_DATE));
        if (instrMap.get(BANK_CODE) != null) {
            final Bank bank = getBank(instrMap.get(BANK_CODE).toString());
            if (bank == null)
                throw new ApplicationRuntimeException(BANK_CODE + "'"
                        + instrMap.get(BANK_CODE).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setBankId(bank);
        }
    }

    private void validateAndAssignCard(InstrumentHeader instrHeader,
            final Map<String, Object> instrMap) {
        if (instrMap.get(TRANSACTION_NUMBER) != null)
            instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER)
                    .toString());
        else
            throw new IllegalArgumentException(TRANSACTION_NUMBER + IS_NULL);
        if (instrMap.get(TRANSACTION_DATE) == null)
            throw new IllegalArgumentException(TRANSACTION_DATE + IS_NULL);
        else if (new Date().compareTo((Date) instrMap.get(TRANSACTION_DATE)) == -1)
            throw new IllegalArgumentException(TRANSACTION_DATE
                    + "cannot be future Date");
        else
            instrHeader.setTransactionDate((Date) instrMap
                    .get(TRANSACTION_DATE));
        if (instrMap.get(INSTRUMENT_NUMBER) != null)
            instrHeader.setInstrumentNumber((String) instrMap
                    .get(INSTRUMENT_NUMBER));
        if (instrMap.get(BANK_CODE) != null) {
            final Bank bank = getBank(instrMap.get(BANK_CODE).toString());
            if (bank == null)
                throw new ApplicationRuntimeException(BANK_CODE + "'"
                        + instrMap.get(BANK_CODE).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setBankId(bank);
        } 

        // applicable for payment
        if (instrMap.get(BANKACCOUNTID) != null) {
            final Bankaccount bankaccount = getBankaccount(instrMap.get(
                    BANKACCOUNTID).toString());
            if (bankaccount == null)
                throw new ApplicationRuntimeException(BANKACCOUNTID + "'"
                        + instrMap.get(BANKACCOUNTID).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setBankAccountId(bankaccount);
        }
    }

    private void validateAndAssignCheque(InstrumentHeader instrHeader,
            final Map<String, Object> instrMap) {
        if (instrMap.get(INSTRUMENT_NUMBER) == null)
            throw new IllegalArgumentException(INSTRUMENT_NUMBER + IS_NULL);
        else
            instrHeader.setInstrumentNumber(instrMap.get(INSTRUMENT_NUMBER)
                    .toString());
        if (instrMap.get(INSTRUMENT_SERIALNO) == null)
            instrHeader.setSerialNo(null);
        else
            instrHeader.setSerialNo(financialYearDAO.findById(Long.valueOf(instrMap.get(INSTRUMENT_SERIALNO)
                    .toString()),false));
        if (instrMap.get(INSTRUMENT_DATE) == null)
            throw new IllegalArgumentException(INSTRUMENT_DATE + IS_NULL);
        else if (new Date().compareTo((Date) instrMap.get(INSTRUMENT_DATE)) == -1)
            throw new IllegalArgumentException(INSTRUMENT_DATE
                    + "cannot be future Date");
        else
            instrHeader.setInstrumentDate((Date) instrMap.get(INSTRUMENT_DATE));
        // if it is type of cheque And DD add bank code
        if (instrMap.get(BANK_CODE) != null) {
            final Bank bank = getBank(instrMap.get(BANK_CODE).toString());
            if (bank == null)
                throw new ApplicationRuntimeException(BANK_CODE + "'"
                        + instrMap.get(BANK_CODE).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setBankId(bank);
        } else
            throw new ApplicationRuntimeException(BANK_CODE + IS_NULL);

        // applicable for payment
        if (instrMap.get(BANKACCOUNTID) != null) {
            final Bankaccount bankaccount = getBankaccount(instrMap.get(
                    BANKACCOUNTID).toString());
            if (bankaccount == null)
                throw new ApplicationRuntimeException(BANKACCOUNTID + "'"
                        + instrMap.get(BANKACCOUNTID).toString()
                        + "' is not defined in the system ");
            else
                instrHeader.setBankAccountId(bankaccount);
        }
    }

    /**
     * Accepts List of Instrument and Voucher objects and updates the same in InstrumentVoucher Object .<br>
     * List of Values that can be Passed are<br>
     * <B>Instrument header -InstrumentHeader Object(Mandatory), Voucher header - Voucher Header Object (Mandatory) </B><br>
     *
     * @param paramList
     * @return List<InstrumentVoucher>
     */
    @Transactional
    public List<InstrumentVoucher> updateInstrumentVoucherReference(
            final List<Map<String, Object>> paramList) {
        final List<InstrumentVoucher> iVouherList = new ArrayList<InstrumentVoucher>();
        for (final Map<String, Object> iVoucherMap : paramList) {
            final InstrumentVoucher iVoucher = new InstrumentVoucher();
            if (iVoucherMap.get(INSTRUMENT_HEADER) == null)
                throw new ApplicationRuntimeException(INSTRUMENT_HEADER
                        + IS_NULL);
            else
                iVoucher.setInstrumentHeaderId((InstrumentHeader) iVoucherMap
                        .get(INSTRUMENT_HEADER));
            if (iVoucherMap.get(VOUCHER_HEADER) == null)
                throw new ApplicationRuntimeException(VOUCHER_HEADER + IS_NULL);
            else
                iVoucher.setVoucherHeaderId((CVoucherHeader) iVoucherMap
                        .get(VOUCHER_HEADER));

            instrumentVoucherService.persist(iVoucher);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Saved cheque and voucher Link" + iVoucher);
            /**
             * all payments should be enterd into BankReconcialation table also if it is type bank and receipt also should be
             * reconciled ---|| iVoucher .getInstrumentHeaderId().getInstrumentType().getType().equals
             * (FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK)
             */
            if (iVoucher.getInstrumentHeaderId().getIsPayCheque()
                    .equals(FinancialConstants.IS_PAYCHECK_ONE)
                    || iVoucher.getInstrumentHeaderId().getIsPayCheque()
                            .equals(FinancialConstants.IS_PAYCHECK_ZERO)
                    && iVoucher.getInstrumentHeaderId().getInstrumentType()
                            .getType()
                            .equals(FinancialConstants.INSTRUMENT_TYPE_BANK)) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Adding to Bank Reconcialation");
                addToBankReconcilation(iVoucher.getVoucherHeaderId(),
                        iVoucher.getInstrumentHeaderId());
            }

            iVouherList.add(iVoucher);
        }

        return iVouherList;

    }

    @Transactional
    public List<InstrumentVoucher> modifyInstrumentVoucher(
            final List<Map<String, Object>> paramList) {
        final List<InstrumentVoucher> iVouherList = new ArrayList<InstrumentVoucher>();
        for (final Map<String, Object> iVoucherMap : paramList) {
            final InstrumentVoucher iVoucher = new InstrumentVoucher();
            if (iVoucherMap.get(INSTRUMENT_HEADER) == null)
                throw new ApplicationRuntimeException(INSTRUMENT_HEADER
                        + IS_NULL);
            else
                iVoucher.setInstrumentHeaderId((InstrumentHeader) iVoucherMap
                        .get(INSTRUMENT_HEADER));
            if (iVoucherMap.get(VOUCHER_HEADER) == null)
                throw new ApplicationRuntimeException(VOUCHER_HEADER + IS_NULL);
            else
                iVoucher.setVoucherHeaderId((CVoucherHeader) iVoucherMap
                        .get(VOUCHER_HEADER));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("before Modification by modifyInstrumentVoucher: "
                        + iVoucher);
            instrumentVoucherService.update(iVoucher);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("After Modification by modifyInstrumentVoucher: "
                        + iVoucher);
            iVouherList.add(iVoucher);
        }

        return iVouherList;

    }

    /**
     * this api is called for every instrument type but amount is reconciled in this step only for type BANK else only entry is
     * made in bankreconcialtion table. populates following fields in Bankreconciliation<br>
     * <B>bankaccount,chequenumber,chequedate,amount,voucherheader,isreconciled, transactiontype,type<B><br>
     *
     * @param vouherHeader
     * @param instrumentHeader
     * @return Bankreconciliation
     * @throws ApplicationRuntimeException
     */
    @Transactional
    public Bankreconciliation addToBankReconcilation(
            final CVoucherHeader vouherHeader,
            final InstrumentHeader instrumentHeader)
            throws ApplicationRuntimeException {
        final EgwStatus instrumentReconciledStatus = (EgwStatus) persistenceService
                .find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
                        FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
        return addToBankReconcilationWithLoop(vouherHeader, instrumentHeader,
                instrumentReconciledStatus);
    }

    /**
     *
     * @param vouherHeader
     * @param instrumentHeader
     * @param instrumentReconciledStatus
     * @return
     * @throws ApplicationRuntimeException instrumentReconciledStatus is used for INSTRUMENT_TYPE_BANK and
     * INSTRUMENT_TYPE_BANK_TO_BANK since they are reconciled on voucher creation itself . Others will be in deposited status
     */
    @Transactional
    public Bankreconciliation addToBankReconcilationWithLoop(
            final CVoucherHeader vouherHeader,
            final InstrumentHeader instrumentHeader,
            final EgwStatus instrumentReconciledStatus)
            throws ApplicationRuntimeException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("addToBankReconcilation | Start");
        if (vouherHeader == null)
            throw new IllegalArgumentException("voucherHeader" + IS_NULL);
        if (instrumentHeader == null)
            throw new IllegalArgumentException("instrumentHeader" + IS_NULL);
        final Bankreconciliation bankreconciliation = new Bankreconciliation();
        InstrumentOtherDetails iOtherdetails;
        iOtherdetails = instrumentOtherDetailsService.find(
                "from InstrumentOtherDetails where instrumentHeaderId=?",
                instrumentHeader);
        if (iOtherdetails == null)
            iOtherdetails = new InstrumentOtherDetails();

        if (instrumentHeader.getInstrumentType().getType()
                .equalsIgnoreCase(FinancialConstants.INSTRUMENT_TYPE_BANK)) {
            iOtherdetails.setReconciledAmount(instrumentHeader
                    .getInstrumentAmount());
            iOtherdetails.setInstrumentStatusDate(instrumentHeader
                    .getInstrumentDate());
            instrumentHeader.setStatusId(instrumentReconciledStatus);
        } else if (instrumentHeader
                .getInstrumentType()
                .getType()
                .equalsIgnoreCase(
                        FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK)) {
            iOtherdetails.setReconciledAmount(instrumentHeader
                    .getInstrumentAmount());
            iOtherdetails.setInstrumentStatusDate(instrumentHeader
                    .getInstrumentDate());
            instrumentHeader.setStatusId(instrumentReconciledStatus);
        } else {
            bankreconciliation.setBankaccount(instrumentHeader
                    .getBankAccountId());
            iOtherdetails.setInstrumentStatusDate(instrumentHeader
                    .getInstrumentDate());
            // EgwStatus status =
            // (EgwStatus)persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
            // instrumentHeader.setStatusId(status);
        }
        if (instrumentHeader.getIsPayCheque() == null)
            throw new ApplicationRuntimeException(
                    IS_PAYCHECK
                            + IS_NULL
                            + " in Instrument Header cannot update Transactiontype in BankReconciliation ");
        else if (instrumentHeader.getIsPayCheque().equals(
                FinancialConstants.IS_PAYCHECK_ONE))
            bankreconciliation.setTransactiontype("Cr");
        else if (instrumentHeader.getIsPayCheque().equals(
                FinancialConstants.IS_PAYCHECK_ZERO))
            bankreconciliation.setTransactiontype("Dr");
        bankreconciliation.setInstrumentHeaderId(instrumentHeader.getId());
        bankreconciliation.setAmount(instrumentHeader.getInstrumentAmount());
        instrumentOtherDetailsService.persist(iOtherdetails);
        bankReconciliationService.persist(bankreconciliation);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("addToBankReconcilation | End");
        return bankreconciliation;

    }

    /**
     * Accepts list of following Objects in a Map and updates them into InstrumnetOtherdetails and statusid in InstrumentHeader.
     * accepted values are<br>
     * <b> Instrument Header,payinSlipId,instrumentStatusDate,reconciledAmount,statusId ,BankAccount</b><br>
     * status values are<br>
     * <b>New,Deposited,Reconciled,Dishonored,Cancelled</b><br>
     * Based on status instrumentStatusDate will have deposited date,reconciled date and dishonored date.
     *
     * @param paramList
     * @return
     */
    @Transactional
    public List<InstrumentOtherDetails> updateInstrumentOtherDetails(
            final List<Map<String, Object>> paramList)
            throws ApplicationRuntimeException {
        InstrumentHeader iHeader = null;
        InstrumentOtherDetails iOtherDetails = null;
        final List<InstrumentOtherDetails> iOtherDetailsList = new ArrayList<InstrumentOtherDetails>();
        for (final Map<String, Object> iOtherDetailsMap : paramList) {

            if (iOtherDetailsMap.get(INSTRUMENT_HEADER) == null)
                throw new ApplicationRuntimeException(INSTRUMENT_HEADER
                        + IS_NULL);
            else {
                iHeader = (InstrumentHeader) iOtherDetailsMap
                        .get(INSTRUMENT_HEADER);
                iOtherDetails = instrumentOtherDetailsService
                        .find("from InstrumentOtherDetails where instrumentHeaderId=?",
                                iHeader);
                if (iOtherDetails == null) {
                    iOtherDetails = new InstrumentOtherDetails();
                    iOtherDetails.setInstrumentHeaderId(iHeader);
                }
                if (iOtherDetailsMap.get(PAYIN_SLIP_ID) == null) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("PayinSlip Id is null");
                    // throw new
                    // ApplicationRuntimeException(PAYIN_SLIP_ID+IS_NULL);
                } else
                    iOtherDetails
                            .setPayinslipId((CVoucherHeader) iOtherDetailsMap
                                    .get(PAYIN_SLIP_ID));

                if (iOtherDetailsMap.get(INSTRUMENT_STATUS_DATE) != null) {
                    new java.sql.Date(
                            ((Date) iOtherDetailsMap
                                    .get(INSTRUMENT_STATUS_DATE)).getTime());
                    iOtherDetails.setInstrumentStatusDate(new java.sql.Date(
                            ((Date) iOtherDetailsMap
                                    .get(INSTRUMENT_STATUS_DATE)).getTime()));
                }
                if (iOtherDetailsMap.get(RECONCILED_AMOUNT) != null)
                    iOtherDetails
                            .setReconciledAmount((BigDecimal) iOtherDetailsMap
                                    .get(RECONCILED_AMOUNT));
                if (iOtherDetailsMap.get(STATUS_ID) == null)
                    throw new ApplicationRuntimeException(
                            "Required Object Status is not Found in the Map ");
                else
                    iHeader.setStatusId((EgwStatus) iOtherDetailsMap
                            .get(STATUS_ID));
                if (iOtherDetailsMap.get(TRANSACTION_NUMBER) != null)
                    iHeader.setTransactionNumber((String) iOtherDetailsMap
                            .get(TRANSACTION_NUMBER));
                if (iOtherDetailsMap.get(TRANSACTION_DATE) != null)
                    iHeader.setTransactionDate((Date) iOtherDetailsMap
                            .get(TRANSACTION_DATE));
                if (iOtherDetailsMap.get(BANKACCOUNTID) != null)
                    iHeader.setBankAccountId((Bankaccount) iOtherDetailsMap
                            .get(BANKACCOUNTID));
            }
            instrumentOtherDetailsService.persist(iOtherDetails);
            instrumentHeaderService.persist(iHeader);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("updated  otherdetails as " + iOtherDetails);
            iOtherDetailsList.add(iOtherDetails);
        }

        return iOtherDetailsList;
    }

    @Transactional
    public boolean cancelInstrument(final InstrumentHeader ih)
            throws ApplicationRuntimeException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Cancelling " + ih);
        boolean result = false;
        try {
            final String cancelStatusQuiery = "from EgwStatus where upper(moduletype)=upper('instrument') and  upper(description)=upper('"
                    + FinancialConstants.INSTRUMENT_CANCELLED_STATUS + "')";
            final EgwStatus cancelStatus = (EgwStatus) persistenceService
                    .find(cancelStatusQuiery);
            ih.setStatusId(cancelStatus);
            instrumentHeaderService.update(ih);
            result = true;
        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ApplicationRuntimeException(e.getMessage());
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
        return result;
    }

    private Bank getBank(final String bankCode) {
        return (Bank) persistenceService.find("from Bank where code=?",
                bankCode);
    }

    private Bankaccount getBankaccount(final String bankAccountId) {
        return (Bankaccount) persistenceService.find(
                "from Bankaccount where id=?", Long.valueOf(bankAccountId));

    }

    private ECSType getECSType(final String ecsTypeId) {
        return (ECSType) persistenceService.find("from ECSType where id=?",
                Long.valueOf(ecsTypeId));

    }

    /**
     * returns list of InstrumentVouchers reconciled from reconcilationFromDate to reconcilationToDate to get list of
     * InstrumentVouchers reconciled on a perticular date pass both as same Date 'dd/MM/yyyy'),reconcilationToDate('dd/MM/yyyy')
     *
     * @return List of InstrumentVouchers
     */
    public List<InstrumentVoucher> getReconciledCheques(
            final Date reconcilationFromDate, final Date reconcilationToDate)
            throws ApplicationRuntimeException {
        if (reconcilationFromDate == null || reconcilationToDate == null)
            throw new ApplicationRuntimeException(
                    "reconcilationFromDate and reconcilationToDate should not be null");
        final Query qry = persistenceService.getSession()
                .createQuery(
                        "select iv from InstrumentVoucher iv inner join iv.instrumentHeaderId as  ih   where ih.statusId.description=:status"
                                + " and ih in (select iih from InstrumentOtherDetails io inner join io.instrumentHeaderId as iih where io.instrumentStatusDate>=:startDate and io.instrumentStatusDate<=:endDate )");
        qry.setString("status", FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
        qry.setDate("startDate", reconcilationFromDate);
        qry.setDate("endDate", reconcilationToDate);
        return qry.list();
    }

    /**
     * returns List of InstrumentVouhcers dishonored from dishonoredFromDate to dishonoredToDate to get list of InstrumentVouchers
     * dishonored on a perticular date pass both as same Date
     *
     * @param dishonoredFromDate ,dishonoredToDate
     * @return List of InstrumentVouchers
     */
    public List<InstrumentVoucher> getBouncedCheques(
            final Date dishonoredFromDate, final Date dishonoredToDate)
            throws ApplicationRuntimeException {
        if (dishonoredFromDate == null || dishonoredToDate == null)
            throw new ApplicationRuntimeException(
                    "dishonoredFromDate and dishonoredToDate should not be null");
        final Query qry = persistenceService.getSession()
                .createQuery(
                        "select iv from InstrumentVoucher iv inner join iv.instrumentHeaderId as  ih   where ih.statusId.description=:status"
                                + " and ih in (select iih from InstrumentOtherDetails io inner join io.instrumentHeaderId as iih where io.modifiedDate>=:startDate and io.modifiedDate<=:endDate ) order by iv.instrumentHeaderId desc");
        qry.setString("status", FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
        qry.setDate("startDate", dishonoredFromDate);
        qry.setDate("endDate", dishonoredToDate);
        return qry.list();
    }

    public InstrumentType getInstrumentTypeById(final Long id)
            throws ApplicationRuntimeException {
        InstrumentType iType = null;
        if (id == null)
            throw new ApplicationRuntimeException(INSTRUMENT_TYPE + " id "
                    + IS_NULL);
        else
            iType = instrumentTypeService.findById(id, false);
        return iType;
    }

    public InstrumentType getInstrumentTypeByType(final String type)
            throws ApplicationRuntimeException {
        InstrumentType iType = null;
        if (type == null)
            throw new ApplicationRuntimeException(INSTRUMENT_TYPE + IS_NULL);
        else if (type.isEmpty())
            throw new ApplicationRuntimeException(INSTRUMENT_TYPE + "is empty");
        else {
            String qry = "";
            try {
                qry = "from InstrumentType  where type=? and isActive=true";
                iType = instrumentTypeService.find(qry, type);
            } catch (final Exception e) {
                LOGGER.error("Error while getting InstrumentType from database"
                        + e.getMessage(), e);
            }

        }
        return iType;
    }

    public EgwStatus getStatusId(final String statusString) {
        final String statusQury = "from EgwStatus where upper(moduletype)=upper('instrument') and  upper(description)=upper('"
                + statusString + "')";
        final EgwStatus egwStatus = (EgwStatus) persistenceService
                .find(statusQury);
        return egwStatus;

    }

    public InstrumentHeader getInstrumentHeader(final Long bankaccountId,
            final String instrumentNo, final String payTo) {
        return instrumentHeaderService
                .find(" from InstrumentHeader where bankAccountId.id=? and instrumentNumber=? and payTo=? ",
                        bankaccountId, instrumentNo, payTo);
    }

    public InstrumentHeader getInstrumentHeader(final Long bankaccountId,
            final String instrumentNo, final String payTo, final String serialNo) {
        return instrumentHeaderService
                .find(" from InstrumentHeader where bankAccountId.id=? and instrumentNumber=? and payTo=? and serialNo.id=? ",
                        bankaccountId, instrumentNo, payTo, Long.valueOf(serialNo));
    }

    public InstrumentHeader getInstrumentHeaderById(final Long id) {
        return instrumentHeaderService
                .find(" from InstrumentHeader where id=?",id);
    }
    
    @Transactional
    public InstrumentType createInstrumentType(final InstrumentType iType) {
        instrumentTypeService.persist(iType);
        return iType;
    }

    @Transactional
    public InstrumentAccountCodes createInstrumentAccountCodes(
            final InstrumentAccountCodes iAccCodes) {
        instrumentAccountCodesService.persist(iAccCodes);
        return iAccCodes;
    }

    // setters for Spring injection

    public boolean isChequeNumberWithinRange(final String chequeNumber,
            final Long bankAccountId, final Integer departmentId,
            final String serialNo) {
        AccountCheques accountCheques = new AccountCheques();
        if (serialNo != null)
            accountCheques = (AccountCheques) persistenceService
                    .find("select ac from AccountCheques ac, ChequeDeptMapping cd where ac.id = cd.accountCheque.id and "
                            + " ac.bankAccountId.id=? and cd.allotedTo.id=? and ? between ac.fromChequeNumber and ac.toChequeNumber and ac.serialNo=? ",
                            bankAccountId, departmentId.longValue(), chequeNumber, Long.valueOf(serialNo));
        else
            accountCheques = (AccountCheques) persistenceService
                    .find("select ac from AccountCheques ac, ChequeDeptMapping cd where ac.id = cd.accountCheque.id and "
                            + " ac.bankAccountId.id=? and cd.allotedTo.id=? and ? between ac.fromChequeNumber and ac.toChequeNumber ",
                            bankAccountId, departmentId.longValue(), chequeNumber);
        if (accountCheques == null)
            return false;
        return true;
    }

   public boolean isChequeNumberUnique(final String chequeNumber,
            final Long bankAccountId, final String serialNo) {
        final InstrumentType instrumentType = getInstrumentTypeByType("cheque");
        List<InstrumentHeader> list = new ArrayList<InstrumentHeader>();
        if (serialNo != null)
            list = instrumentHeaderService
                    .findAllBy(
                            "from InstrumentHeader where instrumentNumber=? and instrumentType.id=? and bankAccountId.id=? and isPayCheque='1' and "
                                    + "serialNo.id=?", chequeNumber,
                            instrumentType.getId(), bankAccountId, Long.valueOf(serialNo));
        else
            list = instrumentHeaderService
                    .findAllBy(
                            "from InstrumentHeader where instrumentNumber=? and instrumentType.id=? and bankAccountId.id=? and isPayCheque='1' ",
                            chequeNumber,
                            instrumentType.getId(), bankAccountId);
        if (list != null && list.size() > 0)
            return false;
        return true;
    }

   public  boolean isRtgsNumberUnique(final String chequeNumber,
            final Long bankAccountId) {
        final InstrumentType instrumentType = getInstrumentTypeByType("advice");
        final List<InstrumentHeader> list = instrumentHeaderService
                .findAllBy(
                        "from InstrumentHeader where transactionNumber=? and instrumentType.id=? and bankAccountId.id=? and isPayCheque='1' ",
                        chequeNumber, instrumentType.getId(), bankAccountId);
        if (list != null && list.size() > 0)
            return false;
        return true;
    }

   public  boolean isChequeIsSurrenderdForReassign(final String chequeNumber,
            final Long bankAccountId, final String serialNo) {
        final InstrumentType instrumentType = getInstrumentTypeByType("cheque");
        final List<InstrumentHeader> list = instrumentHeaderService
                .findAllBy(
                        "from InstrumentHeader where instrumentNumber=? and instrumentType.id=? and bankAccountId.id=? and statusId in (?) "
                                + "and serialNo.id=?",
                        chequeNumber,
                        instrumentType.getId(),
                        bankAccountId,
                        getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS),
                        Long.valueOf(serialNo));
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    public boolean isChequeNumberValid(final String chequeNumber,
            final Long bankAccountId, final Integer departmentId,
            final String serialNo) {
        if (!isChequeNumberWithinRange(chequeNumber, bankAccountId,
                departmentId, serialNo))
            return false;
        if (!isChequeNumberUnique(chequeNumber, bankAccountId, serialNo))
            return false;
        return true;
    }

    public boolean isRtgsNumberValid(final String chequeNumber,
            final Long bankAccountId) {
        if (!isRtgsNumberUnique(chequeNumber, bankAccountId))
            return false;
        return true;
    }

    /**
     * @param vhId
     */
    public void unDeposit(final Long payinslipId) {
        final InstrumentOtherDetails iOtherdetails = instrumentOtherDetailsService
                .find("from InstrumentOtherDetails  io where payinslipId.id=?",
                        payinslipId);
        final InstrumentHeader iHeader = iOtherdetails.getInstrumentHeaderId();
        iHeader.setStatusId(getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS));
        instrumentHeaderService.persist(iHeader);
        iOtherdetails.setPayinslipId(null);
        instrumentOtherDetailsService.persist(iOtherdetails);
    }

    /**
     * @param suurenderChequelist
     */
    @Transactional
    public void surrenderCheques(
            final List<InstrumentHeader> suurenderChequelist) {

        for (final InstrumentHeader instrumentHeader : suurenderChequelist) {
            final String reason = instrumentHeader.getSurrendarReason();
            // when reason is there
            if (reason != null) {
                // if it contain reassign=Y OR N remove it and save
                if (reason.indexOf('|') != -1) {
                    if (reason.substring(reason.indexOf('|') + 1,
                            reason.length()).equalsIgnoreCase("Y")) {
                        instrumentHeader
                                .setStatusId(getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS));
                        instrumentHeader.setSurrendarReason(reason.substring(0,
                                reason.indexOf('|')));
                    } else {
                        instrumentHeader.setSurrendarReason(reason.substring(0,
                                reason.indexOf('|')));
                        instrumentHeader
                                .setStatusId(getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS));
                    }
                } else
                    instrumentHeader
                            .setStatusId(getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS));
            } else
                instrumentHeader
                        .setStatusId(getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS));
            InstrumentOtherDetails instrumentOtherDetails = instrumentOtherDetailsService
                    .find("from InstrumentOtherDetails where instrumentHeaderId=?",
                            instrumentHeader);
            if (instrumentOtherDetails != null)
                instrumentOtherDetails.setInstrumentStatusDate(new Date());
            else {
                instrumentOtherDetails = new InstrumentOtherDetails();
                instrumentOtherDetails.setInstrumentHeaderId(instrumentHeader);
                instrumentOtherDetails.setInstrumentStatusDate(new Date());
            }
            instrumentHeaderService.persist(instrumentHeader);
            instrumentOtherDetailsService.persist(instrumentOtherDetails);
        }

    }

    @Transactional
    public void updateInstrumentOtherDetailsStatus(
            final InstrumentHeader instrumentHeader, final Date statusDate,
            final BigDecimal reconciledAmount) {
        final InstrumentOtherDetails instrumentOtherDetails = instrumentOtherDetailsService
                .find("from InstrumentOtherDetails where instrumentHeaderId.id=?",
                        instrumentHeader.getId());
        instrumentOtherDetails.setInstrumentStatusDate(statusDate);
        instrumentOtherDetails.setReconciledAmount(reconciledAmount);
        instrumentOtherDetailsService.persist(instrumentOtherDetails);
    }

    @Transactional
    public void editInstruments(
            final InstrumentOtherDetails instrumentOtherDetails) {

        final InstrumentHeader instrumentHeader = instrumentOtherDetails
                .getInstrumentHeaderId();
        instrumentHeader
                .setStatusId(getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS));
        instrumentHeaderService.update(instrumentHeader);
        instrumentOtherDetailsService.delete(instrumentOtherDetails);
        final Bankreconciliation bankreconciliation = (Bankreconciliation) persistenceService
                .find("from Bankreconciliation where instrumentHeaderId=?",
                        instrumentHeader.getId());
        bankReconciliationService.delete(bankreconciliation);
        final ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService
                .find("from ContraJournalVoucher where instrumentHeaderId=?",
                        instrumentHeader);
        persistenceService.delete(contraJournalVoucher);

    }

    /**
     * @param chequeNumber
     * @param bankaccountId
     * @param departmentId
     * @return
     */
    public boolean isReassigningChequeNumberValid(final String chequeNumber,
            final Long bankAccountId, final Integer departmentId,
            final String serialNo) {
        if (!isChequeNumberWithinRange(chequeNumber, bankAccountId,
                departmentId, serialNo))
            return false;
        if (!isChequeIsSurrenderdForReassign(chequeNumber, bankAccountId,
                serialNo))
            return false;
        return true;
    }

}