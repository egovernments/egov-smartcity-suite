/*******************************************************************************
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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
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
import org.egov.collection.entity.DishonoredChequeForm;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.DishonorCheque;
import org.egov.model.instrument.DishonorChequeDetails;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.instrument.InstrumentHeaderService;
import org.egov.services.voucher.GeneralLedgerDetailService;
import org.egov.services.voucher.GeneralLedgerService;
import org.egov.services.voucher.VoucherHeaderService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DishonorChequeActionHelper {
    final private static Logger LOGGER = Logger.getLogger(DishonorChequeActionHelper.class);
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
    private DishonorChequeService dishonorChequeService;

    @Autowired
    private FunctionDAO functionDAO;

    @Autowired
    @Qualifier("createVoucher")
    private CreateVoucher createVoucher;

    @Transactional
    public DishonorCheque createDishonorCheque(DishonoredChequeForm chequeForm) throws Exception {
        DishonorCheque dishonorChq = new DishonorCheque();

        try {
            CVoucherHeader originalVoucher = voucherHeaderService.findById(
                    Long.valueOf(chequeForm.getVoucherHeaderIds().split(",")[0]), false);
            InstrumentHeader instrumentHeader = instrumentHeaderService.findById(
                    Long.valueOf(chequeForm.getInstHeaderIds().split(",")[0]), false);

            DishonorChequeDetails dishonourChqDetails = new DishonorChequeDetails();
            dishonorChq.setStatus(egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.STATUS_MODULE_DISHONORCHEQUE,
                    FinancialConstants.DISHONORCHEQUE_APPROVED_STATUS));
            dishonorChq.setTransactionDate(chequeForm.getTransactionDate());
            dishonorChq.setBankReferenceNumber(chequeForm.getReferenceNo());
            dishonorChq.setInstrumentDishonorReason(chequeForm.getRemarks());
            dishonorChq.setOriginalVoucherHeader(originalVoucher);
            dishonorChq.setInstrumentHeader(instrumentHeader);
            String[] receiptGeneralLedger = chequeForm.getReceiptGLDetails().split(",");
            String[] remittanceGeneralLedger = chequeForm.getRemittanceGLDetails().split(",");
            CGeneralLedger ledger = new CGeneralLedger();
            for (String gl : receiptGeneralLedger) {
                ledger = generalLedgerService.find("from CGeneralLedger where voucherHeaderId.id = ? and glcode = ?",
                        originalVoucher.getId(), gl.split("-")[0]);

                dishonourChqDetails = new DishonorChequeDetails();
                dishonourChqDetails.setHeader(dishonorChq);
                CChartOfAccounts glCode = chartOfAccountsService.find("from CChartOfAccounts where glcode=?", ledger.getGlcode());
                dishonourChqDetails.setGlcodeId(glCode);
                if (ledger.getFunctionId() != null) {
                    dishonourChqDetails.setFunctionId(ledger.getFunctionId());
                }
                dishonourChqDetails.setDebitAmt(BigDecimal.valueOf(Long.valueOf(gl.split("-")[1])));
                dishonourChqDetails.setCreditAmount(BigDecimal.valueOf(Long.valueOf(gl.split("-")[2])));
                dishonorChq.getDetails().add(dishonourChqDetails);
            }

            for (String gl : remittanceGeneralLedger) {
                CVoucherHeader remittanceVoucher = voucherHeaderService
                        .find("select gl.voucherHeaderId from CGeneralLedger gl ,InstrumentOtherDetails iod where gl.voucherHeaderId.id = iod.payinslipId.id and iod.instrumentHeaderId.id   in ("
                                + chequeForm.getInstHeaderIds()
                                + ") ");
                ledger = generalLedgerService.find("from CGeneralLedger where voucherHeaderId.id = ? and glcode = ?",
                        remittanceVoucher.getId(), gl.split("-")[0]);

                dishonourChqDetails = new DishonorChequeDetails();
                dishonourChqDetails.setHeader(dishonorChq);
                CChartOfAccounts glCode = chartOfAccountsService.find("from CChartOfAccounts where glcode=?", ledger.getGlcode());
                dishonourChqDetails.setGlcodeId(glCode);
                if (ledger.getFunctionId() != null) {
                    dishonourChqDetails.setFunctionId(ledger.getFunctionId());
                }
                dishonourChqDetails.setDebitAmt(BigDecimal.valueOf(Long.valueOf(gl.split("-")[1])));
                dishonourChqDetails.setCreditAmount(BigDecimal.valueOf(Long.valueOf(gl.split("-")[2])));
                dishonorChq.getDetails().add(dishonourChqDetails);
            }
            // dishonorChq.getDetails().addAll(dishonorChequeDetails);
            persistenceService.applyAuditing(dishonorChq);
            persistenceService.persist(dishonorChq);
            approve(chequeForm, dishonorChq, originalVoucher, instrumentHeader);
        } catch (final ValidationException e) {
            e.printStackTrace();
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            e.printStackTrace();
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return dishonorChq;
    }

    @Transactional
    public void approve(DishonoredChequeForm chequeForm, DishonorCheque dishonorChq, CVoucherHeader originalVoucher,
            InstrumentHeader instrumentHeader) {
        final String instrumentHeaderIds[] = chequeForm.getInstHeaderIds().split(",");
        createReversalVoucher(chequeForm, dishonorChq, originalVoucher, instrumentHeader);
        for (final String instHeadId : instrumentHeaderIds)
            dishonorChequeService.updateCollectionsOnInstrumentDishonor(Long.valueOf(instHeadId));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public CVoucherHeader createReversalVoucher(DishonoredChequeForm chequeForm, DishonorCheque dishonorChq,
            CVoucherHeader originalVoucher, InstrumentHeader instrumentHeader) {
        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(originalVoucher, instrumentHeader);
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        final List<HashMap<String, Object>> subledgerdetails = new ArrayList<HashMap<String, Object>>();

        List<DishonorChequeDetails> dishonorChequeDetails = new ArrayList<DishonorChequeDetails>();
        dishonorChequeDetails.addAll(dishonorChq.getDetails());
        HashMap<String, Object> detailMap = null;
        HashMap<String, Object> subledgerMap = null;
        CFunction function = null;
        CGeneralLedger ledger = new CGeneralLedger();
        for (DishonorChequeDetails gl : dishonorChequeDetails) {
            if (gl.getCreditAmount() != null && gl.getCreditAmount().compareTo(BigDecimal.ZERO) != 0) {
                CVoucherHeader remittanceVoucher = voucherHeaderService
                        .find("select gl.voucherHeaderId from CGeneralLedger gl ,InstrumentOtherDetails iod where gl.voucherHeaderId.id = iod.payinslipId.id and iod.instrumentHeaderId.id   in ("
                                + chequeForm.getInstHeaderIds()
                                + ") ");
                ledger = generalLedgerService.find("from CGeneralLedger where voucherHeaderId.id = ? and glcode = ?",
                        remittanceVoucher.getId(), gl.getGlcodeId().getGlcode());
            } else {
                ledger = generalLedgerService.find("from CGeneralLedger where voucherHeaderId.id = ? and glcode = ?",
                        originalVoucher.getId(), gl.getGlcodeId().getGlcode());
            }
            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.GLCODE, ledger.getGlcode());
            // debit becomes credit ,credit becomes debit
            detailMap.put(VoucherConstant.DEBITAMOUNT, gl.getDebitAmt());
            detailMap.put(VoucherConstant.CREDITAMOUNT, gl.getCreditAmount());
            accountdetails.add(detailMap);
            List<CGeneralLedgerDetail> ledgerDetailSet = generalLedgerDetailService
                    .findAllBy(
                            "from CGeneralLedgerDetail where generalLedgerId.id=?",
                            ledger.getId());
            for (final CGeneralLedgerDetail ledgerDetail : ledgerDetailSet) {
                subledgerMap = new HashMap<String, Object>();
                subledgerMap.put(VoucherConstant.GLCODE, ledger.getGlcode());
                subledgerMap.put(VoucherConstant.DETAILTYPEID, ledgerDetail.getDetailTypeId().getId());
                subledgerMap.put(VoucherConstant.DETAILKEYID, ledgerDetail.getDetailKeyId());
                // even for subledger debit becomes credit ,credit becomes debit
                if (gl.getDebitAmt().compareTo(BigDecimal.ZERO) != 0)
                    subledgerMap.put(VoucherConstant.CREDITAMOUNT, gl.getCreditAmount());
                else
                    subledgerMap.put(VoucherConstant.DEBITAMOUNT, gl.getDebitAmt());

                subledgerdetails.add(subledgerMap);
            }

        }

        CVoucherHeader reversalVoucher = createVoucher.createVoucher(headerDetails, accountdetails, subledgerdetails);

        reversalVoucher.setStatus(0);
        voucherHeaderService.applyAuditing(reversalVoucher);
        voucherHeaderService.persist(reversalVoucher);
        return reversalVoucher;
    }

    private HashMap<String, Object> createHeaderAndMisDetails(
            CVoucherHeader voucherHeader, InstrumentHeader instrumentHeader) throws ValidationException {
        final HashMap<String, Object> headerdetails = new HashMap<String, Object>();
        // All reversal will be GJV
        headerdetails.put(VoucherConstant.VOUCHERNAME,
                FinancialConstants.JOURNALVOUCHER_NAME_RECEIPT_REVERSAL);
        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
        headerdetails.put((String) VoucherConstant.VOUCHERSUBTYPE,
                voucherHeader.getVoucherSubType());
        headerdetails.put(VoucherConstant.VOUCHERNUMBER,
                null);
        headerdetails.put(VoucherConstant.VOUCHERDATE,
                voucherHeader.getVoucherDate());
        headerdetails
                .put(VoucherConstant.DESCRIPTION,
                        " Reversal Voucher Entry for receipt number " + voucherHeader.getVoucherNumber() + ", Cheque Number "
                                + instrumentHeader.getInstrumentNumber() + " Cheque Dated :"
                                + instrumentHeader.getInstrumentDate());

        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherHeader
                    .getVouchermis().getDepartmentid().getCode());
        if (voucherHeader.getFundId() != null)
            headerdetails.put(VoucherConstant.FUNDCODE, voucherHeader
                    .getFundId().getCode());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            headerdetails.put(VoucherConstant.SCHEMECODE, voucherHeader
                    .getVouchermis().getSchemeid().getCode());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            headerdetails.put(VoucherConstant.SUBSCHEMECODE, voucherHeader
                    .getVouchermis().getSubschemeid().getCode());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeader
                    .getVouchermis().getFundsource().getCode());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            headerdetails.put(VoucherConstant.DIVISIONID, voucherHeader
                    .getVouchermis().getDivisionid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            headerdetails.put(VoucherConstant.FUNCTIONARYCODE, voucherHeader
                    .getVouchermis().getFunctionary().getCode());
        if (voucherHeader.getVouchermis().getFunction() != null)
            headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeader
                    .getVouchermis().getFunction().getCode());
        return headerdetails;
    }
}