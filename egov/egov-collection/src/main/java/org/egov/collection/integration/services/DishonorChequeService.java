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
package org.egov.collection.integration.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.CollectionDishonorCheque;
import org.egov.collection.entity.CollectionDishonorChequeDetails;
import org.egov.collection.entity.CollectionDishonorChequeSubLedgerDetails;
import org.egov.collection.entity.DishonoredChequeBean;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.DishonorCheque;
import org.egov.model.instrument.DishonorChequeDetails;
import org.egov.model.instrument.DishonorChequeSubLedgerDetails;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.instrument.FinancialIntegrationService;
import org.egov.services.instrument.InstrumentHeaderService;
import org.egov.services.voucher.GeneralLedgerDetailService;
import org.egov.services.voucher.GeneralLedgerService;
import org.egov.services.voucher.VoucherHeaderService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DishonorChequeService implements FinancialIntegrationService {

    private static final Logger LOGGER = Logger.getLogger(DishonorChequeService.class);

    @Autowired
    private FinancialsUtil financialsUtil;

    @Autowired
    @Qualifier("instrumentHeaderService")
    public InstrumentHeaderService instrumentHeaderService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    @Qualifier("voucherHeaderService")
    private VoucherHeaderService voucherHeaderService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;

    @Autowired
    @Qualifier("generalLedgerService")
    private GeneralLedgerService generalLedgerService;

    @Autowired
    @Qualifier("generalLedgerDetailService")
    private GeneralLedgerDetailService generalLedgerDetailService;

    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    @Qualifier("createVoucher")
    private CreateVoucher createVoucher;

    @Autowired
    @Qualifier("receiptHeaderService")
    public ReceiptHeaderService receiptHeaderService;

    @Autowired
    @Qualifier("collectionsUtil")
    public CollectionsUtil collectionsUtil;

    @Transactional
    public DishonorCheque createDishonorCheque(final DishonoredChequeBean chequeForm) throws Exception {
        final DishonorCheque dishonorChq = new DishonorCheque();

        try {
            CVoucherHeader originalVoucher = null;
            if (!chequeForm.getVoucherHeaderIds().isEmpty())
                originalVoucher = voucherHeaderService.findById(
                        Long.valueOf(chequeForm.getVoucherHeaderIds().split(",")[0]), false);
            final InstrumentHeader instrumentHeader = instrumentHeaderService.findById(
                    Long.valueOf(chequeForm.getInstHeaderIds().split(",")[0]), false);
            if (originalVoucher != null)
                createDishonorChequeForVoucher(chequeForm, dishonorChq, originalVoucher, instrumentHeader);
            else
                createDishonorChequeWithoutVoucher(chequeForm, instrumentHeader);
        } catch (final ValidationException e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e);
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e);
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return dishonorChq;
    }

    @Transactional
    public void createDishonorChequeForVoucher(final DishonoredChequeBean chequeForm, final DishonorCheque dishonorChq,
            final CVoucherHeader originalVoucher, final InstrumentHeader instrumentHeader) throws Exception {
        DishonorChequeSubLedgerDetails dishonourChqSLDetails = new DishonorChequeSubLedgerDetails();
        dishonorChq.setStatus(egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.STATUS_MODULE_DISHONORCHEQUE,
                FinancialConstants.DISHONORCHEQUE_APPROVED_STATUS));
        dishonorChq.setTransactionDate(chequeForm.getTransactionDate());
        dishonorChq.setBankReferenceNumber(chequeForm.getReferenceNo());
        dishonorChq.setInstrumentDishonorReason(chequeForm.getRemarks());
        dishonorChq.setOriginalVoucherHeader(originalVoucher);
        instrumentHeader.setSurrendarReason(chequeForm.getDishonorReason());
        instrumentHeaderService.update(instrumentHeader);
        dishonorChq.setInstrumentHeader(instrumentHeader);
        final String[] receiptGeneralLedger = chequeForm.getReceiptGLDetails().split(",");
        final String[] remittanceGeneralLedger = chequeForm.getRemittanceGLDetails().split(",");
        final Set<DishonorChequeDetails> dishonorChequeDetailsSet = new HashSet<>();
        CGeneralLedger ledger = new CGeneralLedger();
        for (final String gl : receiptGeneralLedger) {
            ledger = generalLedgerService.find("from CGeneralLedger where voucherHeaderId.id = ? and glcode = ?",
                    originalVoucher.getId(), gl.split("-")[0].trim());
            final List<CGeneralLedgerDetail> ledgerDetailSet = generalLedgerDetailService.findAllBy(
                    "from CGeneralLedgerDetail where generalLedgerId.id=?", ledger.getId());
            final DishonorChequeDetails dishonourChqDetails = new DishonorChequeDetails();
            dishonourChqDetails.setHeader(dishonorChq);
            final CChartOfAccounts glCode = chartOfAccountsService.find("from CChartOfAccounts where glcode=?",
                    ledger.getGlcode());
            dishonourChqDetails.setGlcodeId(glCode);
            if (ledger.getFunctionId() != null)
                dishonourChqDetails.setFunctionId(ledger.getFunctionId());
            dishonourChqDetails.setDebitAmt(BigDecimal.valueOf(Double.valueOf(gl.split("-")[1].trim())));
            dishonourChqDetails.setCreditAmount(BigDecimal.valueOf(Double.valueOf(gl.split("-")[2].trim())));
            for (final CGeneralLedgerDetail ledgerDetail : ledgerDetailSet) {
                dishonourChqSLDetails = new DishonorChequeSubLedgerDetails();
                dishonourChqSLDetails.setDetails(dishonourChqDetails);
                dishonourChqSLDetails
                        .setAmount(dishonourChqDetails.getDebitAmt().compareTo(BigDecimal.ZERO) == 0 ? dishonourChqDetails
                                .getCreditAmount() : dishonourChqDetails.getDebitAmt());
                dishonourChqSLDetails.setDetailTypeId(ledgerDetail.getDetailTypeId().getId());
                dishonourChqSLDetails.setDetailKeyId(ledgerDetail.getDetailKeyId());
                dishonourChqDetails.getSubLedgerDetails().add(dishonourChqSLDetails);
            }

            dishonorChequeDetailsSet.add(dishonourChqDetails);
            LOGGER.info("dishonorChq Details " + dishonorChequeDetailsSet.size());
        }

        for (final String gl : remittanceGeneralLedger) {
            final CVoucherHeader remittanceVoucher = voucherHeaderService
                    .find("select gl.voucherHeaderId from CGeneralLedger gl ,InstrumentOtherDetails iod where gl.voucherHeaderId.id = iod.payinslipId.id and iod.instrumentHeaderId.id   in ("
                            + chequeForm.getInstHeaderIds() + ") ");
            ledger = generalLedgerService.find("from CGeneralLedger where voucherHeaderId.id = ? and glcode = ?",
                    remittanceVoucher.getId(), gl.split("-")[0].trim());
            final List<CGeneralLedgerDetail> ledgerDetailSet = generalLedgerDetailService.findAllBy(
                    "from CGeneralLedgerDetail where generalLedgerId.id=?", ledger.getId());
            final DishonorChequeDetails dishonourChqDetails = new DishonorChequeDetails();
            dishonourChqDetails.setHeader(dishonorChq);
            final CChartOfAccounts glCode = chartOfAccountsService.find("from CChartOfAccounts where glcode=?",
                    ledger.getGlcode());
            dishonourChqDetails.setGlcodeId(glCode);
            if (ledger.getFunctionId() != null)
                dishonourChqDetails.setFunctionId(ledger.getFunctionId());
            dishonourChqDetails.setDebitAmt(BigDecimal.valueOf(Double.valueOf(gl.split("-")[1].trim())));
            dishonourChqDetails.setCreditAmount(BigDecimal.valueOf(Double.valueOf(gl.split("-")[2].trim())));
            for (final CGeneralLedgerDetail ledgerDetail : ledgerDetailSet) {
                dishonourChqSLDetails = new DishonorChequeSubLedgerDetails();
                dishonourChqSLDetails.setDetails(dishonourChqDetails);
                dishonourChqSLDetails
                        .setAmount(dishonourChqDetails.getDebitAmt().compareTo(BigDecimal.ZERO) == 0 ? dishonourChqDetails
                                .getCreditAmount() : dishonourChqDetails.getDebitAmt());
                dishonourChqSLDetails.setDetailTypeId(ledgerDetail.getDetailTypeId().getId());
                dishonourChqSLDetails.setDetailKeyId(ledgerDetail.getDetailKeyId());
                dishonourChqDetails.getSubLedgerDetails().add(dishonourChqSLDetails);
                // Need to handle multiple sub ledgers
                break;
            }
            dishonorChequeDetailsSet.add(dishonourChqDetails);
        }
        dishonorChq.getDetails().addAll(dishonorChequeDetailsSet);
        persistenceService.applyAuditing(dishonorChq);
        persistenceService.persist(dishonorChq);
        approve(chequeForm, dishonorChq, originalVoucher, instrumentHeader);
    }

    @Transactional
    public CollectionDishonorCheque populateAndPersistDishonorCheque(final DishonoredChequeBean chequeForm)
            throws Exception {
        final CollectionDishonorCheque dishonorChq = new CollectionDishonorCheque();

        try {
            final InstrumentHeader instrumentHeader = instrumentHeaderService.findById(
                    Long.valueOf(chequeForm.getInstHeaderIds().split(",")[0]), false);
            final ReceiptHeader collectionHeader = receiptHeaderService.findById(
                    Long.valueOf(chequeForm.getReceiptHeaderIds().split(",")[0]), false);
            dishonorChq.setStatus(egwStatusDAO.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_DISHONORCHEQUE,
                    CollectionConstants.DISHONORCHEQUE_STATUS_CODE_APPROVED));
            dishonorChq.setTransactionDate(chequeForm.getTransactionDate());
            dishonorChq.setCollectionHeader(collectionHeader);
            dishonorChq.setBankReferenceNumber(chequeForm.getReferenceNo());
            dishonorChq.setInstrumentDishonorReason(chequeForm.getRemarks());
            instrumentHeader.setSurrendarReason(chequeForm.getDishonorReason());
            instrumentHeaderService.update(instrumentHeader);
            dishonorChq.setInstrumentHeader(instrumentHeader);
            final String[] receiptGeneralLedger = chequeForm.getReceiptGLDetails().split(",");
            final String[] remittanceGeneralLedger = chequeForm.getRemittanceGLDetails().split(",");
            dishonorChq.getDetails()
                    .add(populateDischonourChequedetails(chequeForm, dishonorChq, receiptGeneralLedger));
            dishonorChq.getDetails().add(
                    populateDischonourChequedetails(chequeForm, dishonorChq, remittanceGeneralLedger));
            persistenceService.applyAuditing(dishonorChq);
            persistenceService.persist(dishonorChq);
        } catch (final ValidationException e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e.getMessage());
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e.getMessage());
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return dishonorChq;
    }

    public CollectionDishonorChequeDetails populateDischonourChequedetails(final DishonoredChequeBean chequeForm,
            final CollectionDishonorCheque dishonorChq, final String[] receiptGeneralLedger)
                    throws NumberFormatException {
        CollectionDishonorChequeDetails dishonourChqDetails = new CollectionDishonorChequeDetails();
        CollectionDishonorChequeSubLedgerDetails dishonourChqSLDetails;
        ReceiptDetail ledger = new ReceiptDetail();
        for (final String gl : receiptGeneralLedger) {
            ledger = (ReceiptDetail) persistenceService.find(
                    "from ReceiptDetail where collectionheader = ? and accounthead.glcode = ?",
                    Long.valueOf(chequeForm.getReceiptHeaderIds().split(",")[0]), gl.split("-")[0].trim());
            final Set<AccountPayeeDetail> ledgerDetailSet = ledger.getAccountPayeeDetails();
            dishonourChqDetails = new CollectionDishonorChequeDetails();
            dishonourChqDetails.setDishonorcheque(dishonorChq);
            dishonourChqDetails.setChartofaccounts(ledger.getAccounthead());
            if (ledger.getFunction() != null)
                dishonourChqDetails.setFunction(ledger.getFunction());
            dishonourChqDetails.setDebitAmount(BigDecimal.valueOf(Double.valueOf(gl.split("-")[1].trim())));
            dishonourChqDetails.setCreditAmount(BigDecimal.valueOf(Double.valueOf(gl.split("-")[2].trim())));
            for (final AccountPayeeDetail ledgerDetail : ledgerDetailSet) {
                dishonourChqSLDetails = new CollectionDishonorChequeSubLedgerDetails();
                dishonourChqSLDetails.setDishonorchequedetail(dishonourChqDetails);
                dishonourChqSLDetails
                        .setAmount(dishonourChqDetails.getDebitAmount().compareTo(BigDecimal.ZERO) == 0 ? dishonourChqDetails
                                .getCreditAmount() : dishonourChqDetails.getDebitAmount());
                dishonourChqSLDetails.setDetailType(ledgerDetail.getAccountDetailType().getId());
                dishonourChqSLDetails.setDetailKey(ledgerDetail.getAccountDetailKey().getId());
                dishonourChqDetails.getSubLedgerDetails().add(dishonourChqSLDetails);
            }
        }
        return dishonourChqDetails;
    }

    @Transactional
    public void createDishonorChequeWithoutVoucher(final DishonoredChequeBean chequeForm,
            final InstrumentHeader instrumentHeader) throws Exception {
        instrumentHeader.setSurrendarReason(chequeForm.getDishonorReason());
        persistenceService.update(instrumentHeader);
        populateAndPersistDishonorCheque(chequeForm);
        updateCollectionsOnInstrumentDishonor(instrumentHeader.getId());
    }

    @Transactional
    public void approve(final DishonoredChequeBean chequeForm, final DishonorCheque dishonorChq,
            final CVoucherHeader originalVoucher, final InstrumentHeader instrumentHeader) {
        try {
            final String instrumentHeaderIds[] = chequeForm.getInstHeaderIds().split(",");
            final CVoucherHeader reversalVoucher = createReversalVoucher(chequeForm, dishonorChq, originalVoucher,
                    instrumentHeader);

            dishonorChq.setReversalVoucherHeader(reversalVoucher);
            persistenceService.update(dishonorChq);
            final CollectionDishonorCheque collDisCheque = populateAndPersistDishonorCheque(chequeForm);
            collDisCheque.setReversalVoucherHeader(reversalVoucher);
            persistenceService.update(collDisCheque);
            for (final String instHeadId : instrumentHeaderIds)
                updateCollectionsOnInstrumentDishonor(Long.valueOf(instHeadId));

        } catch (final ValidationException e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
    }

    @Transactional
    public CVoucherHeader createReversalVoucher(final DishonoredChequeBean chequeForm,
            final DishonorCheque dishonorChq, final CVoucherHeader originalVoucher,
            final InstrumentHeader instrumentHeader) {
        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(originalVoucher, instrumentHeader);
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        final List<HashMap<String, Object>> subledgerdetails = new ArrayList<HashMap<String, Object>>();
        CVoucherHeader reversalVoucher = new CVoucherHeader();
        try {
            final List<DishonorChequeDetails> dishonorChequeDetails = new ArrayList<DishonorChequeDetails>();
            dishonorChequeDetails.addAll(dishonorChq.getDetails());
            HashMap<String, Object> detailMap = null;
            HashMap<String, Object> subledgerMap = null;
            for (final DishonorChequeDetails gl : dishonorChequeDetails) {
                detailMap = new HashMap<String, Object>();
                detailMap.put(VoucherConstant.GLCODE, gl.getGlcodeId().getGlcode());
                // debit becomes credit ,credit becomes debit
                detailMap.put(VoucherConstant.DEBITAMOUNT, gl.getDebitAmt());
                detailMap.put(VoucherConstant.CREDITAMOUNT, gl.getCreditAmount());
                accountdetails.add(detailMap);
                final Set<DishonorChequeSubLedgerDetails> dishonorChequeSubLedgerDetails = gl.getSubLedgerDetails();
                for (final DishonorChequeSubLedgerDetails slLedgerDetail : dishonorChequeSubLedgerDetails) {
                    subledgerMap = new HashMap<String, Object>();
                    subledgerMap.put(VoucherConstant.GLCODE, gl.getGlcodeId().getGlcode());
                    subledgerMap.put(VoucherConstant.DETAILTYPEID, slLedgerDetail.getDetailTypeId());
                    subledgerMap.put(VoucherConstant.DETAILKEYID, slLedgerDetail.getDetailKeyId());
                    subledgerMap.put(VoucherConstant.DEBITAMOUNT, slLedgerDetail.getAmount());
                    subledgerdetails.add(subledgerMap);
                    // Need to handle multiple sub ledgers
                    break;
                }
            }
            reversalVoucher = createVoucher.createVoucher(headerDetails, accountdetails, subledgerdetails);
            reversalVoucher.setStatus(0);
            voucherHeaderService.applyAuditing(reversalVoucher);
            voucherHeaderService.persist(reversalVoucher);
        } catch (final ValidationException e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return reversalVoucher;
    }

    private HashMap<String, Object> createHeaderAndMisDetails(final CVoucherHeader voucherHeader,
            final InstrumentHeader instrumentHeader) throws ValidationException {
        final HashMap<String, Object> headerdetails = new HashMap<String, Object>();
        // All reversal will be GJV
        headerdetails.put(VoucherConstant.VOUCHERNAME, FinancialConstants.JOURNALVOUCHER_NAME_RECEIPT_REVERSAL);
        headerdetails.put(VoucherConstant.VOUCHERTYPE, FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
        headerdetails.put((String) VoucherConstant.VOUCHERSUBTYPE, voucherHeader.getVoucherSubType());
        headerdetails.put(VoucherConstant.VOUCHERNUMBER, null);
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
        headerdetails.put(
                VoucherConstant.DESCRIPTION,
                " Reversal Voucher Entry for receipt number " + voucherHeader.getVoucherNumber() + ", Cheque Number "
                        + instrumentHeader.getInstrumentNumber() + " Cheque Dated :"
                        + instrumentHeader.getInstrumentDate());

        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            headerdetails
                    .put(VoucherConstant.DEPARTMENTCODE, voucherHeader.getVouchermis().getDepartmentid().getCode());
        if (voucherHeader.getFundId() != null)
            headerdetails.put(VoucherConstant.FUNDCODE, voucherHeader.getFundId().getCode());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            headerdetails.put(VoucherConstant.SCHEMECODE, voucherHeader.getVouchermis().getSchemeid().getCode());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            headerdetails.put(VoucherConstant.SUBSCHEMECODE, voucherHeader.getVouchermis().getSubschemeid().getCode());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeader.getVouchermis().getFundsource().getCode());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            headerdetails.put(VoucherConstant.DIVISIONID, voucherHeader.getVouchermis().getDivisionid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            headerdetails
                    .put(VoucherConstant.FUNCTIONARYCODE, voucherHeader.getVouchermis().getFunctionary().getCode());
        if (voucherHeader.getVouchermis().getFunction() != null)
            headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
        return headerdetails;
    }

    @Override
    @Transactional
    public void updateCollectionsOnInstrumentDishonor(final Long instrumentHeaderId) {
        LOGGER.debug("Update Collection and Billing system for dishonored instrument id: " + instrumentHeaderId);
        final EgwStatus receiptInstrumentBounceStatus = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED);
        final EgwStatus receiptCancellationStatus = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
        final ReceiptHeader receiptHeader = (ReceiptHeader) persistenceService
                .find("select DISTINCT (receipt) from ReceiptHeader receipt "
                        + "join receipt.receiptInstrument as instruments where instruments.id=? and instruments.statusId.code not in (?,?)",
                        Long.valueOf(instrumentHeaderId), receiptInstrumentBounceStatus.getCode(),
                        receiptCancellationStatus.getCode());
        final InstrumentHeader instrumentHeader = (InstrumentHeader) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_GET_INSTRUMENTHEADER_BY_ID, instrumentHeaderId);
        instrumentHeader.setStatusId(getDishonoredStatus());
        financialsUtil.updateInstrumentHeader(instrumentHeader);
        // update receipts - set status to INSTR_BOUNCED and recon flag to false
        receiptHeaderService.updateDishonoredInstrumentStatus(receiptHeader, instrumentHeader,
                receiptInstrumentBounceStatus, false);
        LOGGER.debug("Updated receipt status to " + receiptInstrumentBounceStatus.getCode()
                + " set reconcilation to false");

    }

    private EgwStatus getDishonoredStatus() {
        return collectionsUtil.getStatusForModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT,
                FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setFinancialsUtil(final FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    @Override
    public void updateSourceInstrumentVoucher(final String event, final Long instrumentHeaderId) {

    }

}
