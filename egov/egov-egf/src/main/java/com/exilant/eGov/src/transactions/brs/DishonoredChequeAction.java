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
/*
 * DishonoredChequeAction.java Created on May 17, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.instrument.DishonorCheque;
import org.egov.model.instrument.DishonorChequeDetails;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.recoveries.Recovery;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.services.instrument.DishonorChequeService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BankBranch;
import com.exilant.eGov.src.domain.BankEntries;

public class DishonoredChequeAction extends DispatchAction {
    private static final String PAYMENT = "Payment";
    private static final String RECEIPT = "Receipt";
    private static final String JOURNAL_VOUCHER = "Journal Voucher";
    private static final String ERROR = "error";
    private static final String SUCCESS = "success";
    private InstrumentService instrumentService;
    protected @Autowired AppConfigValueService appConfigValuesService;
    private boolean isRestrictedtoOneFunctionCenter;
    public PersistenceService<InstrumentHeader, Long> instrumentHeaderService;
    PersistenceService persistenceService;
    private EisUtilService eisService;
    private DishonorChequeService dishonorChqService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;
    private EisCommonService eisCommonService;
    private PersistenceService<CChartOfAccounts, Long> chartOfAccountService;
    private VoucherService voucherService;
    private static final Logger LOGGER = Logger.getLogger(DishonoredChequeAction.class);
    String target = "";
    String mode = "";
    String reversalVhIdValue = "";
    String bankChargesVhIdValue = "";
    String txnDateChq;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    Date dt;
    EGovernCommon cm = new EGovernCommon();
    String alertMessage = null;
    List<Designation> designationList = Collections.EMPTY_LIST;
    List<String> dishonorReasonsList = Collections.EMPTY_LIST;

    public ActionForward toLoad(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws IOException, ServletException {
        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside toLoad");
            final String todayDt = cm.getCurrentDate();
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside toLoad ********** getCurrentDate-->TODAY DATE IS" + todayDt);
            req.getSession().setAttribute("todayDate", todayDt);

            final BankBranch bb = new BankBranch();
            // use persistence service
            final Map hm = bb.getBankBranch();
            req.getSession().setAttribute("bankBranchList", hm);
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> before ending DishonoredChequeAction");
            target = SUCCESS;
        } catch (final Exception ex) {
            target = ERROR;
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);
        }
        return mapping.findForward(target);

    }

    /*
     * get list of accountnumber for given bankbranch
     */
    public ActionForward getAccountNumbers(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws IOException, ServletException
    {
        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside getAccountNumbers");
            final DishonoredChequeForm dishonCheqForm = (DishonoredChequeForm) form;
            final BankBranch bb = new BankBranch();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("bank id  " + dishonCheqForm.getBankId());
            final HashMap hm = bb.getAccNumber(dishonCheqForm.getBankId());
            req.getSession().setAttribute("accNumberList2", hm);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> before ending DishonoredChequeAction");
            target = SUCCESS;
        } catch (final Exception ex) {
            target = ERROR;
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);

        }
        return mapping.findForward(target);
    }

    /*
     * get list of cheques which have not been reconciled
     */

    public ActionForward getDetails(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws IOException, ServletException {
        try {
            // if(LOGGER.isInfoEnabled()) LOGGER.info(">>> inside getDetails------>SEARCH QUERY");

            final DishonoredChequeForm dishonCheqForm = (DishonoredChequeForm) form;
            List<BrsEntries> dishonCheqDetails = new ArrayList<BrsEntries>();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("bankacc id (accId)-->from drop down list " + dishonCheqForm.getAccId());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("CHEQUE NO IS  " + dishonCheqForm.getChequeNo());

            final BankEntries be = new BankEntries();
            String chqFromDate = "";
            String chqToDate = "";
            String vhid = null;
            String bkchrage = "";
            // dishonorReasonsList = (List<String>)
            // HibernateUtil.getCurrentSession().createSQLQuery("select * from egf_instrument_dishonor_reason").list();
            final HashMap hm = new HashMap<String, String>();
            for (final String reason : dishonorReasonsList)
                hm.put(reason, reason);
            req.getSession().setAttribute("dishonorReasonsList", hm);
            dishonCheqForm.getVoucherId();
            dishonCheqForm.getPostTxn();
            dishonCheqForm.getBankChargeAmt();

            vhid = dishonCheqForm.getSelectedVhid();
            final String ref = dishonCheqForm.getSelectedRefNo();
            bkchrage = dishonCheqForm.getSelectedBankCharges();
            req.setAttribute("bkchrage", bkchrage);
            req.setAttribute("ref", ref);
            /*
             * if(marked!=null) { for(String selected:marked) { if(selected.equalsIgnoreCase("on")) {
             * dishonCheqForm.setSelected("on"); vhid=passVoucher[index]; bkchrage=bankChargeAmt[index];
             * req.setAttribute("bkchrage", bkchrage); String ref=dishonCheqForm.getRefNo(); req.setAttribute("ref", ref); }
             * index++; } }
             */

            dt = new Date();
            saveToken(req);
            if (!dishonCheqForm.getBankFromDate().equalsIgnoreCase("")) {
                dt = sdf.parse(dishonCheqForm.getBankFromDate());
                chqFromDate = formatter.format(getNextDate(dt, -1));
            }
            chqToDate = formatter.format(getNextDate(dt, 1));
            Long bankId = Long.valueOf(0);

            if (!dishonCheqForm.getInstrumentMode().equalsIgnoreCase(""))
                if (dishonCheqForm.getInstrumentMode().equals("1"))
                    mode = FinancialConstants.INSTRUMENT_TYPE_DD;
                else
                    mode = FinancialConstants.INSTRUMENT_TYPE_CHEQUE;

            if (dishonCheqForm.getBankId() != null)
                bankId = Long.valueOf(dishonCheqForm.getBankId().toString().split("-")[0]);
            else
            {

            }
            List<BrsEntries> tempdishonCheqDetails = null;
            if (vhid != null)
                tempdishonCheqDetails = be.getChequeDetails(mode, Long.parseLong(dishonCheqForm.getAccId().toString()), bankId,
                        dishonCheqForm.getChequeNo(), chqFromDate, chqToDate, vhid);
            else
                tempdishonCheqDetails = be.getChequeDetails(mode, Long.parseLong(dishonCheqForm.getAccId().toString()), bankId,
                        dishonCheqForm.getChequeNo(), chqFromDate, chqToDate, null);

            if (tempdishonCheqDetails != null && !tempdishonCheqDetails.isEmpty() && tempdishonCheqDetails.size() > 1) {
                final List chequeDetails = new ArrayList();
                for (final BrsEntries brsEntry : tempdishonCheqDetails) {
                    // BrsEntries brs = dishonCheqDetails.get(0);
                    // brs.setVoucherNumber("MULTIPLE");
                    if (!brsEntry.getVoucherType().equalsIgnoreCase("Payment"))
                        chequeDetails.add(brsEntry);
                    // dishonCheqDetails .addAll(chequeDetails);
                    req.setAttribute("resubmit", true);

                }

            } else if (tempdishonCheqDetails != null && tempdishonCheqDetails.size() == 1)
            {
                dishonCheqDetails.add(tempdishonCheqDetails.get(0));
                getreversalGlCodes(tempdishonCheqDetails, dishonCheqForm);
            }
            dishonCheqDetails = tempdishonCheqDetails;

            req.setAttribute("dishonCheqDetails", dishonCheqDetails);
            req.setAttribute("buttonType", req.getParameter("buttonValue"));
            // For workflow details

            // CChartOfAccounts coa =
            // (CChartOfAccounts)persistenceService.find("select ba.chartofaccounts from Bankaccount ba where ba.id= "+Long.parseLong(accIdParam));
            // dishonCheqForm.setGlcodeBkId(coa.getGlcode());
            // dishonCheqForm.setGlcodeForBank(coa.getGlcode());
            req.setAttribute("IntiateReceiptWkfl", true);
            if (dishonCheqForm.isReceiptInstrument())
                req.setAttribute("IntiateReceiptWkfl", true);
            // populateWorkflowEntities(req);
            // getValidActions(null,req);
            // getNextAction(null,req) ;
            target = SUCCESS;
        } catch (final Exception ex) {
            target = ERROR;
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);
        }
        return mapping.findForward(target);
    }

    public ActionForward prepareReversalVoucher(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
                    throws IOException, ServletException {
        // getreversalGlCodes(dishonCheqDetails,dishonCheqForm);

        return mapping.findForward(target);
    }

    // public ajaxGet

    /**
     * Populates all the glcodes for which reversal gl entries have to be made fetches all glcodes with creditamount > 0 for
     * receipt and fetches all glcodes with debitamount > 0 for all others(payment,contra)
     */
    @SuppressWarnings("unchecked")
    private void getreversalGlCodes(final List al, final DishonoredChequeForm dishonCheqForm) {
        if (al == null || al.size() == 0)
            return;
        final String voucherNumber = getVoucherNumbers(al);
        List<Object[]> glCodes = new ArrayList<Object[]>();
        List<Object[]> glCodescredit = new ArrayList<Object[]>();
        new ArrayList<Object[]>();
        List<Object[]> slDetailsCredit = new ArrayList<Object[]>();
        List<Object[]> slDetailsDebit = new ArrayList<Object[]>();
        new ArrayList<Object[]>();
        CFunction glFunctionObj = new CFunction();

        StringBuffer reversalGlCodes = new StringBuffer();
        new StringBuffer();
        if (RECEIPT.equalsIgnoreCase(((BrsEntries) al.get(0)).getVoucherType())
                || JOURNAL_VOUCHER.equalsIgnoreCase(((BrsEntries) al.get(0)).getVoucherType()))
        {
            glCodescredit = persistenceService
                    .findAllBy("select gl.glcode,gl.glcodeId.name, sum(gl.creditAmount),sum(gl.debitAmount),gl.functionId from CGeneralLedger gl where gl.voucherHeaderId in("
                            + voucherNumber
                            + ") and gl.debitAmount<>0 and gl.creditAmount=0 and gl.glcode not in (select glcode from CChartOfAccounts where purposeId in (select id from AccountCodePurpose where name='Cheque In Hand')) group by gl.glcode,gl.glcodeId.name,gl.functionId order by gl.glcode");
            glCodes = persistenceService
                    .findAllBy("select gl.glcode,gl.glcodeId.name,sum(gl.creditAmount),sum(gl.debitAmount),gl.functionId  from CGeneralLedger gl where gl.voucherHeaderId in("
                            + voucherNumber
                            + ") and gl.creditAmount<>0 and gl.debitAmount=0 group by gl.glcode,gl.glcodeId.name,gl.functionId order by gl.glcode");
            glCodes.addAll(glCodescredit);
        } else
            glCodes = persistenceService
            .findAllBy("select distinct gl.glcode,gl.glcodeId.name, sum(gl.creditAmount) ,sum(gl.debitAmount),gl.functionId from CGeneralLedger gl where gl.voucherHeaderId in("
                    + voucherNumber
                    + ") and gl.creditAmount=0 and gl.debitAmount<>0 group by gl.glcode,gl.glcodeId.name,gl.functionId  order by gl.glcode");
        String glCode = "";

        for (final Object[] generalLedger : glCodes) {
            if (generalLedger[4] != null && !generalLedger[4].toString().equals("")) {
                // reversalfunctionId=reversalfunctionId.append(generalLedger[4].toString()).append(',');
                glFunctionObj = (CFunction) persistenceService.find(" from CFunction fn where id=?",
                        Long.parseLong(generalLedger[4].toString()));
                // glFunction=
                // glFunctiontt.getId();
                // glFunctiontt.getCode();
                // glFunctionObj=glFunction.get(0).toString().split(",");
                glCode = glCode.concat(generalLedger[0].toString()).concat("~").concat(generalLedger[1].toString()).concat("~")
                        .concat(generalLedger[2].toString()).concat("~").concat(generalLedger[3].toString()).concat("~")
                        .concat(glFunctionObj.getId().toString()).concat("~").concat(glFunctionObj.getCode().toString())
                        .concat(",");
            } else
                glCode = glCode.concat(generalLedger[0].toString()).concat("~").concat(generalLedger[1].toString()).concat("~")
                .concat(generalLedger[2].toString()).concat("~").concat(generalLedger[3].toString()).concat("~")
                .concat("").concat(",");
            reversalGlCodes = reversalGlCodes.append(generalLedger[0].toString()).append(',');

            // glCode =
            // glCode.concat(generalLedger[0].toString()).concat("~").concat(generalLedger[1].toString()).concat("~").concat(generalLedger[2].toString()).concat("~").concat(generalLedger[3].toString()).concat("~").concat(generalLedger[4].toString()).concat(",");
            // reversalGlCodes = reversalGlCodes .append(generalLedger[0].toString()).append(',');
            // reversalGlCodes = reversalGlCodes .append(generalLedger[0].toString()).append(',');
        }
        final String reversalGlCodesStr = reversalGlCodes.substring(0, reversalGlCodes.length() - 1);
        final StringBuffer slCodes = new StringBuffer();
        dishonCheqForm.setGlcodeChList(glCode);
        slDetailsCredit = persistenceService
                .findAllBy("select distinct gl.glcode, gd.detailTypeId, gd.detailKeyId,SUM(gd.amount)" +
                        " from CGeneralLedger gl, CGeneralLedgerDetail gd where gl.voucherHeaderId in(" + voucherNumber + ")" +
                        " and gl.id = gd.generalLedgerId and gl.debitAmount >0 and gl.glcode in (" + reversalGlCodesStr
                        + ") group by gl.glcode, gd.detailTypeId, gd.detailKeyId");
        for (final Object[] objects : slDetailsCredit)
            if (slCodes.length() > 0)
                slCodes.append("~").append(objects[0]).append("-").append(objects[1]).append("-").append(objects[2]).append("-")
                .append("credit")
                .append("-").append(objects[3]);
            else
                slCodes.append(objects[0]).append("-").append(objects[1]).append("-").append(objects[2]).append("-")
                .append("credit")
                .append("-").append(objects[3]);

        slDetailsDebit = persistenceService.findAllBy("select distinct gl.glcode, gd.detailTypeId, gd.detailKeyId,SUM(gd.amount)"
                +
                " from CGeneralLedger gl, CGeneralLedgerDetail gd where gl.voucherHeaderId in(" + voucherNumber + ")" +
                " and gl.id = gd.generalLedgerId and gl.creditAmount >0 and gl.glcode in (" + reversalGlCodesStr
                + ") group by gl.glcode, gd.detailTypeId, gd.detailKeyId");
        for (final Object[] objects : slDetailsDebit)
            if (slCodes.length() > 0)
                slCodes.append("~").append(objects[0]).append("-").append(objects[1]).append("-").append(objects[2]).append("-")
                .append("debit")
                .append("-").append(objects[3]);
            else
                slCodes.append(objects[0]).append("-").append(objects[1]).append("-").append(objects[2]).append("-")
                .append("debit")
                .append("-").append(objects[3]);

        dishonCheqForm.setSubledgerDetails(slCodes.toString());
    }

    private String getVoucherNumbers(final List al) {
        String voucherNumber = "-1";
        for (final Object object : al)
            voucherNumber = voucherNumber.concat(",").concat(((BrsEntries) object).getVoucherHeaderId());
        return voucherNumber;
    }

    /*
     * pass voucher for bankentry
     */
    public void setOneFunctionCenterValue() {
        final AppConfigValues appConfigValues = (AppConfigValues) persistenceService.find("from AppConfigValues where key in " +
                "(select id from AppConfig where key_name='ifRestrictedToOneFunctionCenter' and module='EGF' )");
        if (appConfigValues == null)
            throw new ValidationException("Error", "ifRestrictedToOneFunctionCenter is not defined");
        else
            setRestrictedtoOneFunctionCenter(appConfigValues.getValue().equalsIgnoreCase("yes") ? true : false);
    }

    public ActionForward createDishonourCheque(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
                    throws IOException, ServletException {

        User nextUser = null;
        Position approvePos = null;
        final DishonoredChequeForm dishonCheqForm = (DishonoredChequeForm) form;
        final String vTypeParam = dishonCheqForm.getVoucherTypeParam();
        if (vTypeParam.equalsIgnoreCase("Receipt"))
            try {

                if (LOGGER.isInfoEnabled())
                    LOGGER.info("vTypeParam --->" + vTypeParam);
                final DishonorCheque dishonorChq = new DishonorCheque();
                DishonorChequeDetails dishonourChqDetails = null;
                EgwStatus dishonorWkflowStatus = null;
                Date transactionDate = new Date();
                setOneFunctionCenterValue();
                if (isTokenValid(req)) {
                    // reset token
                    resetToken(req);

                    final int vHeadId = Integer.parseInt(dishonCheqForm.getPassVoucherId());
                    final int instrumentHeadId = Integer.parseInt(dishonCheqForm.getInstrumentHeaderId());

                    final int debitAmtSize = dishonCheqForm.getDebitAmount().length;
                    final int creditAmtSize = dishonCheqForm.getCreditAmount().length;
                    final String debitAmount[] = new String[debitAmtSize];
                    final String[] creditAmount = new String[creditAmtSize];

                    final String debitFunctions[] = new String[debitAmtSize];
                    final String[] creditFunctions = new String[creditAmtSize];

                    final String debitGlcodes[] = new String[debitAmtSize];
                    final String[] creditGlCodes = new String[creditAmtSize];
                    dishonCheqForm.getPassChqDate();
                    dishonCheqForm.getPassedAmount();

                    // TODO for receipt and payment debit and credit amounts need to be taken from respective values
                    // String debitAmount[] = new String[dishonCheqForm.getDebitAmount().length] ;
                    int debitCount = 0;

                    for (int i = 0; i < dishonCheqForm.getDebitAmount().length; i++)
                        if (!StringUtils.isEmpty(dishonCheqForm.getDebitAmount()[i]))
                            if (Double.parseDouble(dishonCheqForm.getDebitAmount()[i]) != 0)
                            {
                                debitAmount[debitCount] = dishonCheqForm.getDebitAmount()[i];
                                debitGlcodes[debitCount] = dishonCheqForm.getGlcodeChId()[i];
                                debitFunctions[debitCount] = dishonCheqForm.getFunctionChId()[i];
                                debitCount++;
                            }
                    // String[] creditAmount = new String[dishonCheqForm.getCreditAmount().length] ;
                    int creditCount = 0;
                    for (int i = 0; i < dishonCheqForm.getCreditAmount().length; i++)
                        if (!StringUtils.isEmpty(dishonCheqForm.getCreditAmount()[i]))
                            if (Double.parseDouble(dishonCheqForm.getCreditAmount()[i]) != 0)
                            {
                                creditAmount[creditCount] = dishonCheqForm.getCreditAmount()[i];
                                creditGlCodes[creditCount] = dishonCheqForm.getGlcodeChId()[i];
                                creditFunctions[creditCount] = dishonCheqForm.getFunctionChId()[i];
                                creditCount++;
                            }

                    final String bankTotalAmt = dishonCheqForm.getBankTotalAmt();
                    final Double bankChargesAmt = Double.parseDouble(bankTotalAmt);

                    dishonCheqForm.getGlcodeChId();
                    dishonCheqForm.getFunctionChId();
                    dishonCheqForm.getFunctionCode();

                    // if(LOGGER.isInfoEnabled())
                    // LOGGER.info(">>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<"+dishonCheqForm.getFunctionChId());
                    if (dishonCheqForm.getGlcodeChIdP() != null)
                        dishonCheqForm.getGlcodeChIdP();

                    final String glcodeBkId = dishonCheqForm.getGlcodeBkId();

                    final String bankChReason = dishonCheqForm.getBankChReason();
                    final String chqReason = dishonCheqForm.getChqReason();
                    if (!"".equals(dishonCheqForm.getVoucherTxnDate()))
                        transactionDate = sdf.parse(dishonCheqForm.getVoucherTxnDate());

                    dishonorWkflowStatus = egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT,
                            FinancialConstants.INSTRUMENT_INWORKFLOW_STATUS);
                    dishonorChq.setStatus(dishonorWkflowStatus);// make a staus underworkflow
                    dishonorChq.setBankChargesAmt(BigDecimal.valueOf(bankChargesAmt));
                    dishonorChq.setTransactionDate(transactionDate);
                    dishonorChq.setBankReferenceNumber(dishonCheqForm.getPassRefNo());
                    dishonorChq.setBankreason(bankChReason);
                    dishonorChq.setInstrumentDishonorReason(chqReason);

                    if (null != glcodeBkId && !glcodeBkId.trim().isEmpty())
                        dishonorChq.setBankchargeGlCodeId(chartOfAccountService.findByNamedQuery("GLCODEBYID",
                                Long.valueOf(String.valueOf(glcodeBkId))));
                    final CVoucherHeader originalVoucher = voucherService.findByNamedQuery("VOUCHERHEADERBYID",
                            Long.valueOf(String.valueOf(vHeadId)));
                    dishonorChq.setOriginalVoucherHeader(originalVoucher);
                    final InstrumentHeader instHeader = instrumentHeaderService.findByNamedQuery("INSTRUMENTHEADERBYID",
                            Long.valueOf(String.valueOf(instrumentHeadId)));

                    dishonorChq.setInstrumentHeader(instHeader);
                    // set the instrument status to instrument in dishonor workflow
                    instHeader.setStatusId(dishonorWkflowStatus);
                    instHeader.setSurrendarReason(dishonCheqForm.getDishonorReasons());
                    instrumentHeaderService.persist(instHeader);
                    // narration- reversal entry for receipt "voucher No"-- with "cheque No" dated :"Cheque date"
                    // Get iod created by user
                    final InstrumentOtherDetails iob = (InstrumentOtherDetails) persistenceService.find(
                            "from InstrumentOtherDetails where instrumentHeaderId.id=?", instHeader.getId());
                    eisCommonService.getEmployeeByUserId(iob.getCreatedBy().getId());
                    nextUser = iob.getPayinslipId().getCreatedBy();
                    dishonorChq.setPayinSlipCreatorUser(nextUser);
                    dishonorChq.setPayinSlipCreator(nextUser.getId().intValue());
                    approvePos = eisService.getPrimaryPositionForUser(nextUser.getId(), new Date());
                    if (null != approvePos) {
                        /* dishonorChq.setApproverPositionId(approvePos.getId().int); */
                    }
                    int deailIndex = 0;
                    if (vTypeParam.equalsIgnoreCase(RECEIPT) || JOURNAL_VOUCHER.equalsIgnoreCase(vTypeParam))
                        for (final String detailGlCodeId : debitGlcodes)
                            if (detailGlCodeId != null)
                            {
                                dishonourChqDetails = new DishonorChequeDetails();
                                dishonourChqDetails.setHeader(dishonorChq);
                                final CChartOfAccounts glCode = (CChartOfAccounts) persistenceService.find(
                                        "from CChartOfAccounts where glcode=?", detailGlCodeId);
                                dishonourChqDetails.setGlcodeId(glCode);
                                if (debitFunctions[deailIndex] != null && debitFunctions[deailIndex] != "")
                                    dishonourChqDetails.setFunctionId(Integer.parseInt(debitFunctions[deailIndex]));
                                if (null != debitAmount[deailIndex])
                                    dishonourChqDetails.setDebitAmt(new BigDecimal(debitAmount[deailIndex]));
                                else
                                    dishonourChqDetails.setDebitAmt(BigDecimal.ZERO);
                                if (null != creditAmount[deailIndex]) {
                                    dishonourChqDetails.setCreditAmount(new BigDecimal(creditAmount[deailIndex]));
                                    if (creditFunctions[deailIndex] != null && creditFunctions[deailIndex] != "")
                                        dishonourChqDetails.setFunctionId(Integer.parseInt(creditFunctions[deailIndex]));
                                } else
                                    dishonourChqDetails.setCreditAmount(BigDecimal.ZERO);
                                deailIndex = deailIndex + 1;
                                dishonorChq.getDetails().add(dishonourChqDetails);
                            }
                    populateWorkflowEntities(req);
                    getValidActions(dishonorChq, req);
                    getNextAction(dishonorChq, req);
                    final User logginUser = (User) persistenceService.find("from User where id=?", EgovThreadLocals.getUserId());
                    dishonorChq.setCreatedBy(nextUser);
                    dishonorChq.setCreatedDate(new Date());
                    dishonorChq.setLastModifiedBy(logginUser);
                    dishonorChq.setLastModifiedDate(new Date());
                    // dishonourChqDetails.setHeader(dishonorChq);
                    // dishonorChq.addDishonorChqDetails(dishonourChqDetails);
                    dishonorChqService.approve(dishonorChq, null, null);
                    // dishonorChq.getState().setCreatedBy(nextUser);
                    // .getState().getCreatedBy().setId(approvePos.intValue());
                    HibernateUtil.getCurrentSession().flush();
                    req.setAttribute("alertMessage", "Initated Dishonor and Forwarded successfully to " + nextUser.getUsername());
                    target = SUCCESS;
                }
                else
                    target = "tokenerror";

            } catch (final ValidationException ex) {
                target = SUCCESS;
                final List<ValidationError> errors = ex.getErrors();
                errors.get(0).getMessage();
                alertMessage = errors.get(0).getMessage();
                LOGGER.error("Exception Encountered!!!" + errors.get(0).getMessage(), ex);
                throw ex;

            }

        catch (final Exception ex) {
            target = ERROR;
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);
            throw new ValidationException(Arrays.asList(new ValidationError("engine.validation.failed", "Validation Faild")));

        }
        else {
            req.setAttribute("alertMessage", "Dishonor Workflow is Applicable to only Receipt Cheques");
            target = SUCCESS;
        }
        // alertMessage="Executed successfully";
        // req.setAttribute("alertMessage", alertMessage);
        return mapping.findForward(target);

    }

    public ActionForward processInbox(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws IOException, ServletException {
        try {
            if (req.getParameter("dishonourChqId") != null)
            {
                final String chqId = req.getParameter("dishonourChqId");
                final DishonorCheque cheque = dishonorChqService.find(" from DishonorCheque where id=?", Long.valueOf(chqId));
                req.setAttribute("DishonorCheque", cheque);
                populateWorkflowEntities(req);
                getValidActions(cheque, req);
                getNextAction(cheque, req);
            }
        } catch (final ValidationException ex) {
            target = SUCCESS;
            final List<ValidationError> errors = ex.getErrors();
            errors.get(0).getMessage();
            alertMessage = errors.get(0).getMessage();
            LOGGER.error("Exception Encountered!!!" + errors.get(0).getMessage(), ex);

        }

        catch (final Exception ex) {
            target = ERROR;
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);

        }
        return mapping.findForward("inbox");
    }

    private void getValidActions(final DishonorCheque cheque, final HttpServletRequest req)
    {
        List<String> validActions = Collections.emptyList();
        List<String> validActionsList = Collections.emptyList();
        new ArrayList<String>();
        String tempValidAction = null;
        // the action list is based on "NEXT_ACTION" value of the pensionSlip
        // All pensionSlip in the inbox will have the same next action.
        if (null == cheque || cheque.getId() == null)
            validActions = Arrays.asList("forward");
        else {
            /*
             * String validAction=(String)persistenceService.find("select validActions from WorkFlowMatrix where objectType=? " +
             * "and currentState in(select nextState from WorkFlowMatrix where objectType=? and currentState=?)",
             * cheque.getStateType(),cheque.getStateType(),cheque.getCurrentState().getValue());
             */
            final String validAction = (String) persistenceService.find(
                    "select validActions from WorkFlowMatrix where objectType=? " +
                            "and currentState =?",
                    cheque.getStateType(), cheque.getCurrentState().getValue());
            if (null != validAction)
            {
                final StringTokenizer strToken = new StringTokenizer(validAction, ",");
                tempValidAction = null;
                validActionsList = new ArrayList<String>();
                while (strToken.hasMoreElements()) {
                    tempValidAction = strToken.nextToken();
                    validActionsList.add(tempValidAction);
                }
            }

        }
        req.setAttribute("validActions", validActions);

    }

    private void getNextAction(final DishonorCheque cheque, final HttpServletRequest req) {
        String nextAction = "";
        if (null != cheque && null != cheque.getId())
            nextAction = (String) persistenceService.find(
                    "select nextAction from WorkFlowMatrix where objectType=? and currentState=?",
                    cheque.getStateType(), cheque.getCurrentState().getValue());

        req.setAttribute("nextAction", nextAction);
    }

    private void populateWorkflowEntities(final HttpServletRequest req)
            throws Exception {
        req.setAttribute("departmentList", persistenceService.findAllBy("from Department order by deptName"));
        // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
        final Assignment asignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(EgovThreadLocals.getUserId(),
                DateUtils.today());
        if (asignment.getDesignation().getName().equalsIgnoreCase("SECTION MANAGER"))
            ;
        {
            designationList = persistenceService.findAllBy("from Designation where name=?", "ACCOUNTS OFFICER");
        }
        req.setAttribute("designationList", designationList);
    }

    public ActionForward saveDetails(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws IOException, ServletException {
        int fundId = 0;
        int functionId = 0;
        int fundSourceId = 0;
        final int fieldId = 0;
        // String functionId=null;
        String departmentId = null;
        final String appConfigKey = "GJV_FOR_RCPT_CHQ_DISHON";
        final AppConfigValues appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                FinancialConstants.MODULE_NAME_APPCONFIG, appConfigKey).get(0);
        final String gjvForRcpt = appConfigValues.getValue();
        // DishonoredEntriesDelegate delegate = new DishonoredEntriesDelegate();
        try {
            // if(LOGGER.isInfoEnabled()) LOGGER.info(">>> inside saveDetails");
            setOneFunctionCenterValue();
            final DishonoredChequeForm dishonCheqForm = (DishonoredChequeForm) form;
            final String vTypeParam = dishonCheqForm.getVoucherTypeParam();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("vTypeParam --->" + vTypeParam);
            String pTxnDate = "";
            if (LOGGER.isInfoEnabled())
                LOGGER.info("pTxnDate BEFORE CONVERT" + dishonCheqForm.getVoucherTxnDate());
            Date dt = new Date();

            if (isTokenValid(req)) {
                // reset token
                resetToken(req);
                if (!"".equals(dishonCheqForm.getVoucherTxnDate())) {
                    dt = sdf.parse(dishonCheqForm.getVoucherTxnDate());
                    pTxnDate = formatter.format(dt);
                }
                final int passAccId = Integer.parseInt(dishonCheqForm.getPassAccId()); // 215
                if (dishonCheqForm.getPassFundId() != null && !dishonCheqForm.getPassFundId().equals(""))
                    fundId = Integer.parseInt(dishonCheqForm.getPassFundId()); // 22
                if (dishonCheqForm.getPassFundSrcId() != null && !dishonCheqForm.getPassFundSrcId().equals(""))
                    fundSourceId = Integer.parseInt(dishonCheqForm.getPassFundSrcId());
                if (null != dishonCheqForm.getDepartmentId() && dishonCheqForm.getDepartmentId().trim().length() != 0)
                    departmentId = dishonCheqForm.getDepartmentId(); // 16
                if (isRestrictedtoOneFunctionCenter) {
                    if (null != dishonCheqForm.getFunctionChId() && dishonCheqForm.getFunctionChId()[0].trim().length() != 0)
                        functionId = Integer.parseInt(dishonCheqForm.getFunctionChId()[0]);
                } else
                    functionId = 0;
                final int vHeadId = Integer.parseInt(dishonCheqForm.getPassVoucherId()); // 3121600
                Integer.parseInt(dishonCheqForm.getInstrumentHeaderId());

                final String passRefNo = dishonCheqForm.getPassRefNo(); // 876
                final int payinVHeadId = Integer.parseInt(dishonCheqForm.getPassPayinVHId()); // 3121600
                final String passVoucher[] = dishonCheqForm.getPassVoucher(); // [yes]
                // String passChqNo[]=(String[])dishonCheqForm.getPassChqNo();
                final String chqDate[] = dishonCheqForm.getPassChqDate(); // 01/01/2013
                final String passedAmount = dishonCheqForm.getPassedAmount(); // 914
                final String debitAmount[] = new String[dishonCheqForm.getDebitAmount().length];
                int debitCount = 0;
                for (int i = 0; i < dishonCheqForm.getDebitAmount().length; i++)
                    if (!StringUtils.isEmpty(dishonCheqForm.getDebitAmount()[i])) {
                        debitAmount[debitCount] = dishonCheqForm.getDebitAmount()[i];
                        debitCount++;
                    }

                final String[] creditAmount = new String[dishonCheqForm.getCreditAmount().length];
                final String[] functions = new String[dishonCheqForm.getCreditAmount().length];
                int creditCount = 0;
                for (int i = 0; i < dishonCheqForm.getCreditAmount().length; i++)
                    if (!StringUtils.isEmpty(dishonCheqForm.getCreditAmount()[i])) {
                        creditAmount[creditCount] = dishonCheqForm.getCreditAmount()[i];
                        functions[creditCount] = dishonCheqForm.getFunctionChId()[i];
                        creditCount++;
                    }

                final String bankTotalAmt = dishonCheqForm.getBankTotalAmt();
                final Double bankChargesAmt = Double.parseDouble(bankTotalAmt);

                final String glcodeChId[] = dishonCheqForm.getGlcodeChId();  // 01/01/2013
                String glcodeChIdP[] = { "" };
                dishonCheqForm.getFunctionChId();
                final String functionCd[] = dishonCheqForm.getFunctionCode();  // [909100, 909100]

                // if(LOGGER.isInfoEnabled())
                // LOGGER.info(">>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<"+dishonCheqForm.getFunctionChId());
                if (dishonCheqForm.getGlcodeChIdP() != null)
                    glcodeChIdP = dishonCheqForm.getGlcodeChIdP();
                /*
                 * for (int i = 0; i < glcodeChIdP.length; i++) { glcodeChIdP[i] = glcodeChIdP[i].split("~")[0]; }
                 */

                final String glcodeBkId = dishonCheqForm.getGlcodeBkId();
                final String chqReason = dishonCheqForm.getChqReason();
                final String chqReasonP = dishonCheqForm.getChqReasonP();

                final String bankChReason = dishonCheqForm.getBankChReason();
                BankEntries be;

                /* Create Payment Voucher for Bank Charges */
                final Bankaccount bankAccount = getBankAccount(passAccId);

                // final level approval
                // bankcharges VH
                if (bankChargesAmt > 0 && !bankTotalAmt.equals("") && !glcodeBkId.equals("")) {
                    CVoucherHeader voucherHeader = null;
                    final String voucherNumber = dishonCheqForm.getBankChargesVoucherNumber();
                    final String[] glCode = new String[] { bankAccount.getChartofaccounts().getGlcode() };
                    be = createBankEntry(pTxnDate, passAccId, passRefNo, bankTotalAmt, glcodeBkId, bankChReason);

                    voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT, pTxnDate, chqReason,
                            voucherNumber);

                    // bankChargesVhIdValue=bankEntriesDelegate.createPaymtVouForDishonBankCharges(vHeadId,passRefNo,bankTotalAmt,narration,pTxnDate,glcodeBkId,passAccId,userId,fundId,fundSourceId,fieldId,departmentId,functionaryId,null);
                    final InstrumentHeader instrument = instrumentService.addToInstrument(
                            createInstruments(voucherNumber, formatter.parse(pTxnDate), new BigDecimal(passedAmount),
                                    bankAccount, "1", FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK)).get(0);
                    instrumentService.updateInstrumentOtherDetailsStatus(instrument, formatter.parse(pTxnDate), BigDecimal.ZERO);
                    voucherHeader.setName("Bank Entry");
                    final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader, vHeadId, departmentId,
                            fundId, fundSourceId, fieldId, functionId);
                    // here is subledger
                    voucherHeader = createVoucher(voucherHeader, headerDetails, new String[] {}, glCode,
                            getGlCodeForId(glcodeBkId), "", new String[] { bankTotalAmt }, null,
                            dishonCheqForm.getSubledgerDetails());

                    updateInstrumentVoucherReference(Arrays.asList(instrument), voucherHeader);
                    be.setVoucherheaderId(voucherHeader.getId().toString());
                    be.setInstrumentHeaderId(instrument.getId());
                    be.insert();
                    bankChargesVhIdValue = voucherHeader.getId().toString();
                }

                /* Create Payment Voucher for -------------->Receipt Reversal */

                final Date chequeDate = sdf.parse(chqDate[0]);
                if (vTypeParam.equalsIgnoreCase(RECEIPT) || JOURNAL_VOUCHER.equalsIgnoreCase(vTypeParam)) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("CREATING RECEIPT Reversal  >>>>>>>>>>");
                    if (debitAmount != null) {
                        CVoucherHeader voucherHeader = null;
                        final String voucherNumber = dishonCheqForm.getReversalVoucherNumber();
                        // If reversal for receipt, then according to appconfig value get the prefix for voucher.
                        if (vTypeParam.equalsIgnoreCase(RECEIPT) && gjvForRcpt.equalsIgnoreCase("Y"))
                            voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL, pTxnDate,
                                    chqReason, voucherNumber);
                        else
                            voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT, pTxnDate,
                                    chqReason, voucherNumber);
                        final List<InstrumentHeader> instrument = instrumentService.addToInstrument(createInstruments(
                                voucherNumber,
                                chequeDate, new BigDecimal(passedAmount), bankAccount, "1",
                                FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK));
                        instrument.get(0).setStatusId(getReconciledStatus());
                        instrumentHeaderService.persist(instrument.get(0));
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("---------------------------" + debitAmount.toString());
                        instrumentService.updateInstrumentOtherDetailsStatus(instrument.get(0), formatter.parse(pTxnDate),
                                new BigDecimal(getTotalAmount(debitAmount)));
                        voucherHeader.setName("Receipt Reversal");
                        voucherHeader.setDescription(chqReason);
                        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader, vHeadId,
                                departmentId,
                                fundId, fundSourceId, fieldId, functionId);
                        final CVoucherHeader paymentVoucher = createVoucher(voucherHeader, headerDetails, glcodeChId,
                                glcodeChIdP,
                                bankAccount.getChartofaccounts().getGlcode(), vTypeParam, debitAmount, creditAmount,
                                dishonCheqForm.getSubledgerDetails(), functionCd);
                        reversalVhIdValue = paymentVoucher.getId().toString();
                        // instrumentService.addToBankReconcilation(voucherHeader,instrument.get(0));
                        updateInstrumentVoucherReference(instrument, paymentVoucher);
                        // reversalVhIdValue=dishonoredEntriesDelegate.createPaymtVouForReversalDishonChq(passedAmount,vHeadId,passRefNo,chqTotalAmt,chqReason,pTxnDate,glcodeChId,passAccId,userId,fundId,fundSourceId,fieldId,departmentId,functionaryId,null);
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("After calling createPaymtVouForReversalDishonChq VoucherHeaderId is:"
                                    + reversalVhIdValue);
                    }
                }
                /* Create Receipt Voucher for ---------------->Payment Reversal */
                if (vTypeParam.equalsIgnoreCase(PAYMENT))
                    if (creditAmount != null) {
                        final String voucherNumber = dishonCheqForm.getReversalVoucherNumber();
                        final CVoucherHeader voucherHeader = createVoucherHeader(
                                FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT,
                                pTxnDate, chqReason, voucherNumber);
                        final List<InstrumentHeader> instrument = instrumentService.addToInstrument(createInstruments(
                                voucherNumber,
                                chequeDate, new BigDecimal(passedAmount), bankAccount, "0",
                                FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK));
                        instrument.get(0).setStatusId(getReconciledStatus());
                        instrumentHeaderService.persist(instrument.get(0));
                        instrumentService.updateInstrumentOtherDetailsStatus(instrument.get(0), formatter.parse(pTxnDate),
                                new BigDecimal(getTotalAmount(creditAmount)));
                        voucherHeader.setName("Payment Reversal");
                        voucherHeader.setDescription(chqReasonP);
                        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader, payinVHeadId,
                                departmentId, fundId, fundSourceId, fieldId, functionId);
                        final CVoucherHeader receiptVoucher = createVoucher(voucherHeader, headerDetails, glcodeChId,
                                glcodeChIdP,
                                bankAccount.getChartofaccounts().getGlcode(), vTypeParam, creditAmount, null,
                                dishonCheqForm.getSubledgerDetails());
                        reversalVhIdValue = receiptVoucher.getId().toString();
                        updateInstrumentVoucherReference(instrument, receiptVoucher);
                        // reversalVhIdValue=dishonoredEntriesDelegate.createReceiptVoucherForPaymentReversal(passedAmount,vHeadId,passRefNo,creditAmount,chqReasonP,pTxnDate,glcodeChIdP,passAccId,userId,fundId,fundSourceId,fieldId,departmentId,functionaryId,null);
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("After calling createReceiptVoucherForPaymentReversal VoucherHeaderId is:"
                                    + reversalVhIdValue);
                    }

                for (final String element : passVoucher)
                    if (element.equalsIgnoreCase("yes")) {
                        // This fix is for Phoenix Migration.
                        // /delegate.updateInstrumentHeader(instrumentHeadId,persistenceService,passRefNo);
                    }
                target = SUCCESS;
                alertMessage = "Executed successfully";
                req.setAttribute("alertMessage", alertMessage);
                req.setAttribute("buttonType", req.getParameter("buttonValue"));
                req.setAttribute("reversalVhId", reversalVhIdValue);
                req.setAttribute("bankChargesVhId", bankChargesVhIdValue);
                req.setAttribute("reversalAmount", passedAmount);
                req.setAttribute("bankChargesAmount", bankTotalAmt);
            } else
                target = "tokenerror";
        }/*
         * catch(InsufficientAmountException ex){ target = SUCCESS; alertMessage=ex.getMessage();
         * LOGGER.error("Exception Encountered!!!"+ex.getMessage(),ex); }
         */
        catch (final ValidationException ex) {
            target = SUCCESS;
            final List<ValidationError> errors = ex.getErrors();
            errors.get(0).getMessage();
            alertMessage = errors.get(0).getMessage();
            LOGGER.error("Exception Encountered!!!" + errors.get(0).getMessage(), ex);

        }

        catch (final Exception ex) {
            target = ERROR;
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);

        }
        req.setAttribute("alertMessage", alertMessage);
        return mapping.findForward(target);
    }

    private Double getTotalAmount(final String[] chqTotalAmt) {
        Double total = Double.valueOf("0");
        for (final String amt : chqTotalAmt)
            if (!StringUtils.isEmpty(amt))
                total = total + Double.valueOf(amt);
        return total;
    }

    private EgwStatus getReconciledStatus() {
        return egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT,
                FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
    }

    BankEntries createBankEntry(final String pTxnDate, final int passAccId, final String passRefNo, final String bankTotalAmt,
            final String glcodeBkId,
            final String bankChReason) {
        final BankEntries be = new BankEntries();
        be.setBankAccountId(passAccId);
        be.setRefNo(passRefNo);
        be.setTxnAmount(bankTotalAmt);
        be.setType("P");
        be.setRemarks(bankChReason);
        be.setTxnDate(pTxnDate);
        be.setGlcodeId(glcodeBkId);
        return be;
    }

    private String getGlCodeForId(final String id) {
        String glCode = "";
        if (id != null && !id.isEmpty())
            glCode = ((CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?", Long.valueOf(id)))
            .getGlcode();
        return glCode;
    }

    private CVoucherHeader createVoucherHeader(final String type, final String voucherDate, final String description,
            final String voucherNumber)
            throws ParseException {
        final CVoucherHeader voucherHeader = new CVoucherHeader();
        voucherHeader.setType(type);
        if (voucherNumber != null && !voucherNumber.isEmpty())
            voucherHeader.setVoucherNumber(voucherNumber);
        voucherHeader.setVoucherDate(formatter.parse(voucherDate));
        voucherHeader.setDescription(description);
        return voucherHeader;
    }

    public ActionForward beforePrintDishonoredCheque(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest req,
            final HttpServletResponse res)
                    throws IOException, ServletException {
        String target = "";
        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Inside beforePrintDishonoredCheque");
            final String reversalVhId = req.getParameter("reversalVhId");
            final String bankChargesVhId = req.getParameter("bankChargesVhId");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("reversalVhId>>" + reversalVhId);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("bankChargesVhId>>" + bankChargesVhId);
            final String reversalAmount = req.getParameter("reversalAmount");
            final String bankChargesAmount = req.getParameter("bankChargesAmount");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("reversalAmount>>" + reversalAmount);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("bankChargesAmount>>" + bankChargesAmount);
            final DishonoredChequeForm disForm = (DishonoredChequeForm) form;
            // DishonoredEntriesDelegate dised=new DishonoredEntriesDelegate();
            // This fix is for Phoenix Migration.
            if (!reversalVhId.equals("") && reversalVhId != null && reversalVhId.length() > 0) {
                final ArrayList alAccDetailsForRevVoucher = null;// dised.getDishonoredRevVoucherAccCodeDetails(Long.parseLong(reversalVhId),null);
                if (!alAccDetailsForRevVoucher.isEmpty()) {
                    final String[] reversalAccCode = new String[alAccDetailsForRevVoucher.size()];
                    final String[] reversalDescn = new String[alAccDetailsForRevVoucher.size()];
                    final String[] reversalDebitAmount = new String[alAccDetailsForRevVoucher.size()];
                    final String[] reversalCreditAmount = new String[alAccDetailsForRevVoucher.size()];

                    for (int n = 0; n < alAccDetailsForRevVoucher.size(); n++) {
                        final DishonoredViewEntries dve = (DishonoredViewEntries) alAccDetailsForRevVoucher.get(n);
                        reversalAccCode[n] = dve.getReversalAccCode();
                        reversalDescn[n] = dve.getReversalDescn();
                        reversalDebitAmount[n] = dve.getReversalDebitAmount();
                        reversalCreditAmount[n] = dve.getReversalCreditAmount();
                    }// for
                    disForm.setReversalAccCode(reversalAccCode);
                    disForm.setReversalDescn(reversalDescn);
                    disForm.setReversalDebitAmount(reversalDebitAmount);
                    disForm.setReversalCreditAmount(reversalCreditAmount);
                }// if
                final ArrayList alVHeaderDetailsForRev = null;// dised.getDishonoredRevVoucherHeaderDetails(Long.parseLong(reversalVhId),null);
                for (final Iterator itr = alVHeaderDetailsForRev.iterator(); itr.hasNext();)
                {
                    final DishonoredViewEntries dve = (DishonoredViewEntries) itr.next();
                    disForm.setVouHName(dve.getVouHName());
                    disForm.setReason(StringUtils.isEmpty(dve.getReason()) ? "" : dve.getReason());
                    disForm.setVoucherNumber(dve.getVoucherNumber());
                    disForm.setVouDate(dve.getVouDate());

                    disForm.setFund(dve.getFund());
                }
                disForm.setPassedAmount(reversalAmount);
            }
            if (!bankChargesVhId.equals("") && bankChargesVhId != null && bankChargesVhId.length() > 0) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Inside Bank charges entry details -getting query");
                final ArrayList alAccDetailsForBkChgs = null;// dised.getDishonoredRevVoucherAccCodeDetails(Long.parseLong(bankChargesVhId),null);
                if (!alAccDetailsForBkChgs.isEmpty()) {
                    final String[] reversalAccCode = new String[alAccDetailsForBkChgs.size()];
                    final String[] reversalDescn = new String[alAccDetailsForBkChgs.size()];
                    final String[] reversalDebitAmount = new String[alAccDetailsForBkChgs.size()];
                    final String[] reversalCreditAmount = new String[alAccDetailsForBkChgs.size()];
                    for (int n = 0; n < alAccDetailsForBkChgs.size(); n++) {
                        final DishonoredViewEntries dve = (DishonoredViewEntries) alAccDetailsForBkChgs.get(n);
                        reversalAccCode[n] = dve.getReversalAccCode();
                        reversalDescn[n] = dve.getReversalDescn();
                        reversalDebitAmount[n] = dve.getReversalDebitAmount();
                        reversalCreditAmount[n] = dve.getReversalCreditAmount();
                    }
                    final List<Object[]> list = getBankChargesDetails(bankChargesVhId);
                    disForm.setReversalAccCodeBC(reversalAccCode);
                    disForm.setReversalDescnBC(reversalDescn);
                    disForm.setReversalDebitAmountBC(reversalDebitAmount);
                    disForm.setReversalCreditAmountBC(reversalCreditAmount);
                    if (list != null) {
                        disForm.setRefNo(list.get(0)[0] == null ? "" : list.get(0)[0].toString());
                        disForm.setRefDate(list.get(0)[1] == null ? "" : list.get(0)[1].toString());
                    }
                }
                final ArrayList alVHeaderDetailsForBkChgs = null;// dised.getDishonoredRevVoucherHeaderDetails(Long.parseLong(bankChargesVhId),null);

                for (final Iterator itr = alVHeaderDetailsForBkChgs.iterator(); itr.hasNext();) {
                    final DishonoredViewEntries dve = (DishonoredViewEntries) itr.next();
                    disForm.setVouHNameBC(dve.getVouHName());
                    disForm.setReasonBC(dve.getReason());
                    disForm.setVoucherNumberBC(dve.getVoucherNumber());
                    disForm.setVouDateBC(dve.getVouDate());
                }
                disForm.setBankTotalAmt(bankChargesAmount);
            }
            target = "printDishonoredChequeDetail";
            req.setAttribute("DishonoredChequeForm", disForm);
        } catch (final Exception ex) {
            target = ERROR;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception Encountered!!!" + ex.getMessage(), ex);
        }
        return mapping.findForward(target);
    }

    private List<Object[]> getBankChargesDetails(final String bankChargesVhId) {
        // SQLQuery query =
        // HibernateUtil.getCurrentSession().createSQLQuery("select refno,to_char(txndate, 'dd/mm/yyyy') from bankentries where voucherheaderid="+bankChargesVhId);
        return null;
    }

    List<Map<String, Object>> createInstruments(final String instrumentNumber, final Date instrumentDate,
            final BigDecimal instrumentAmount,
            final Bankaccount bankAccount, final String isPayCheque, final String instrumentType) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        iMap.put("Transaction number", instrumentNumber);
        iMap.put("Transaction date", instrumentDate);
        iMap.put("Instrument amount", instrumentAmount.doubleValue());
        iMap.put("Instrument type", instrumentType);
        iMap.put("Bank code", bankAccount.getBankbranch().getBank().getCode());
        iMap.put("Bank branch name", bankAccount.getBankbranch().getBranchaddress1());
        iMap.put("Bank account id", bankAccount.getId());
        iMap.put("Is pay cheque", isPayCheque);
        iList.add(iMap);
        return iList;
    }

    private Bankaccount getBankAccount(final Integer id) {
        return (Bankaccount) persistenceService.find("from Bankaccount where id=?", id);
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    /*
     * Fuction code is required for reversing vouchers
     */

    @Deprecated
    CVoucherHeader createVoucher(final CVoucherHeader voucher, final HashMap<String, Object> headerDetails,
            final String[] glCode,
            final String[] glCodeP, final String bankAccountGlCode, final String voucherType,
            final String[] debitAmount, final String[] creditAmount, final String subLedger) {
        CVoucherHeader voucherHeader = null;
        try {
            headerDetails.put(VoucherConstant.SOURCEPATH, "");
            List<HashMap<String, Object>> subledgerDetails;
            final List<HashMap<String, Object>> accountdetails = populateAccountDetails(glCode, glCodeP, bankAccountGlCode,
                    voucherType, debitAmount, creditAmount);
            if (null != subLedger && !StringUtils.isEmpty(subLedger))
                subledgerDetails = populateSubledgerDetails(subLedger);
            else
                subledgerDetails = new ArrayList<HashMap<String, Object>>();

            final CreateVoucher cv = new CreateVoucher();
            // TODO from headerDetails accountdetails subledgerDetails from these 3 populate intermediate objects and create
            // voucher at final aproval.
            voucherHeader = cv.createVoucher(headerDetails, accountdetails, subledgerDetails);
            voucherHeader.getVouchermis().setSourcePath("");
        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError("", "")));
        } catch (final ValidationException e) {
            throw e;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        return voucherHeader;
    }

    CVoucherHeader createVoucher(final CVoucherHeader voucher, final HashMap<String, Object> headerDetails,
            final String[] glCode,
            final String[] glCodeP, final String bankAccountGlCode, final String voucherType,
            final String[] debitAmount, final String[] creditAmount, final String subLedger, final String[] functionCd) {
        CVoucherHeader voucherHeader = null;
        try {
            headerDetails.put(VoucherConstant.SOURCEPATH, "");
            List<HashMap<String, Object>> subledgerDetails;
            final List<HashMap<String, Object>> accountdetails = populateAccountDetails(glCode, glCodeP, bankAccountGlCode,
                    voucherType, debitAmount, creditAmount, functionCd);
            if (null != subLedger && !StringUtils.isEmpty(subLedger))
                subledgerDetails = populateSubledgerDetails(subLedger, accountdetails);
            else
                subledgerDetails = new ArrayList<HashMap<String, Object>>();
            final CreateVoucher cv = new CreateVoucher();
            voucherHeader = cv.createVoucher(headerDetails, accountdetails, subledgerDetails);
            voucherHeader.getVouchermis().setSourcePath("");
        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError("", "")));
        } catch (final ValidationException e) {
            throw e;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        return voucherHeader;
    }

    HashMap<String, Object> createHeaderAndMisDetails(final CVoucherHeader voucherHeader, final int originalVoucherId,
            final String departmentId,
            final int fundId, final int fundSourceId, final int fieldId, final int functionId) throws ValidationException {
        final HashMap<String, Object> headerdetails = new HashMap<String, Object>();
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
        headerdetails.put(VoucherConstant.VOUCHERNUMBER, voucherHeader.getVoucherNumber());
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
        headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());
        headerdetails.put(VoucherConstant.ORIGIONALVOUCHER, String.valueOf(originalVoucherId));
        headerdetails.put(VoucherConstant.STATUS, 2);
        if (departmentId != null && !departmentId.isEmpty())
            headerdetails.put(VoucherConstant.DEPARTMENTCODE,
                    ((Department) persistenceService.find("from Department where id=?", new Integer(departmentId))).getCode());
        if (fundId > 0)
            headerdetails.put(VoucherConstant.FUNDCODE,
                    ((Fund) persistenceService.find("from Fund where id=?", new Integer(fundId))).getCode());
        if (fundSourceId > 0)
            headerdetails.put(VoucherConstant.FUNDSOURCECODE,
                    ((Fundsource) persistenceService.find("from Fundsource where id=?", new Integer(fundSourceId))).getCode());
        if (functionId > 0)
            headerdetails.put(VoucherConstant.FUNCTIONCODE,
                    ((CFunction) persistenceService.find("from CFunction where id=?", new Long(functionId))).getCode());
        if (fieldId > 0)
            headerdetails.put(VoucherConstant.DIVISIONID, new Integer(fieldId));
        return headerdetails;
    }

    List<HashMap<String, Object>> populateSubledgerDetails(final String subLedgerDetails,
            final List<HashMap<String, Object>> accountdetails) {
        final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(subLedgerDetails);

        final String[] subledgerToken = subLedgerDetails.split("~");
        for (final HashMap<String, Object> chk : accountdetails)
            for (final String element : subledgerToken) {
                final HashMap<String, Object> subledgerMap = new HashMap<String, Object>();
                final String[] token = element.split("-");
                if (chk.get(VoucherConstant.GLCODE).equals(token[0].toString())) {
                    subledgerMap.put(VoucherConstant.GLCODE, token[0].toString());
                    subledgerMap.put(VoucherConstant.DETAILTYPEID, token[1].toString());
                    subledgerMap.put(VoucherConstant.DETAILKEYID, token[2].toString());

                    if ("debit".equalsIgnoreCase(token[3].toString()))
                        subledgerMap.put(VoucherConstant.DEBITAMOUNT, chk.get(VoucherConstant.DEBITAMOUNT));
                    else if ("credit".equalsIgnoreCase(token[3].toString())) {
                        subledgerMap.put(VoucherConstant.CREDITAMOUNT, chk.get(VoucherConstant.CREDITAMOUNT));
                        List<Recovery> tdslist = new ArrayList<Recovery>();
                        // persistenceService.findAllBy(query, params)
                        // persistenceService.find(query)
                        tdslist = persistenceService.findAllBy(" from Recovery where chartofaccounts.glcode="
                                + token[0].toString());
                        if (!tdslist.isEmpty())
                            for (final Recovery tds : tdslist)
                                if (tds.getType().equals(token[0].toString()))
                                    subledgerMap.put(VoucherConstant.TDSID, tds.getId());
                    } else
                        throw new ApplicationRuntimeException(
                                "DishonoredChequeAction |  populatesubledgerDetails | not able to find either debit or " +
                                "credit amount");

                    subledgerDetails.add(subledgerMap);
                }
            }

        return subledgerDetails;
    }

    List<HashMap<String, Object>> populateSubledgerDetails(final String subLedgerDetails) {
        final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(subLedgerDetails);

        final String[] subledgerToken = subLedgerDetails.split("~");
        for (final String element : subledgerToken) {
            final HashMap<String, Object> subledgerMap = new HashMap<String, Object>();
            final String[] token = element.split("-");
            subledgerMap.put(VoucherConstant.GLCODE, token[0].toString());
            subledgerMap.put(VoucherConstant.DETAILTYPEID, token[1].toString());
            subledgerMap.put(VoucherConstant.DETAILKEYID, token[2].toString());
            if ("debit".equalsIgnoreCase(token[3].toString()))
                subledgerMap.put(VoucherConstant.DEBITAMOUNT, token[4].toString());
            else if ("credit".equalsIgnoreCase(token[3].toString())) {
                subledgerMap.put(VoucherConstant.CREDITAMOUNT, token[4].toString());
                List<Recovery> tdslist = new ArrayList<Recovery>();
                // persistenceService.findAllBy(query, params)
                // persistenceService.find(query)
                tdslist = persistenceService.findAllBy(" from Recovery where chartofaccounts.glcode=" + token[0].toString());
                if (!tdslist.isEmpty())
                    for (final Recovery tds : tdslist)
                        if (tds.getType().equals(token[0].toString()))
                            subledgerMap.put(VoucherConstant.TDSID, tds.getId());
            } else
                throw new ApplicationRuntimeException(
                        "DishonoredChequeAction |  populatesubledgerDetails | not able to find either debit or " +
                        "credit amount");

            subledgerDetails.add(subledgerMap);
        }

        return subledgerDetails;
    }

    @Deprecated
    List<HashMap<String, Object>> populateAccountDetails(final String[] coaId, final String[] coaPId,
            final String bankAccountGlCode,
            final String voucherType, final String[] debitAmount, final String[] creditAmount) {
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        BigDecimal totalAmountDbt = BigDecimal.ZERO;
        BigDecimal totalAmountCrd = BigDecimal.ZERO;
        if (RECEIPT.equals(voucherType) || JOURNAL_VOUCHER.equals(voucherType)) {
            for (int i = 0; i < debitAmount.length; i++)
                if (!StringUtils.isEmpty(debitAmount[i])) {
                    final BigDecimal amount = new BigDecimal(debitAmount[i]);
                    final String code = coaId[i];
                    if (amount.compareTo(BigDecimal.ZERO) != 0)
                        accountdetails.add(populateDetailMap(code, BigDecimal.ZERO, amount));

                    totalAmountDbt = totalAmountDbt.add(amount);
                }
            for (int i = 0; i < creditAmount.length; i++)
                if (!StringUtils.isEmpty(creditAmount[i])) {
                    final BigDecimal amount = new BigDecimal(creditAmount[i]);
                    final String code = coaId[i];
                    if (amount.compareTo(BigDecimal.ZERO) != 0)
                        accountdetails.add(populateDetailMap(code, amount, BigDecimal.ZERO));
                    totalAmountCrd = totalAmountCrd.add(amount);
                }
            accountdetails.add(populateDetailMap(bankAccountGlCode, totalAmountDbt.subtract(totalAmountCrd), BigDecimal.ZERO));
        } else {
            for (int i = 0; i < debitAmount.length; i++)
                if (!StringUtils.isEmpty(debitAmount[i])) {
                    final String code = coaPId[i];
                    final BigDecimal amount = new BigDecimal(debitAmount[i]);
                    accountdetails.add(populateDetailMap(code, amount, BigDecimal.ZERO));
                    totalAmountDbt = totalAmountDbt.add(amount);
                }
            accountdetails.add(populateDetailMap(bankAccountGlCode, BigDecimal.ZERO, totalAmountDbt));
        }
        return accountdetails;
    }

    List<HashMap<String, Object>> populateAccountDetails(final String[] coaId, final String[] coaPId,
            final String bankAccountGlCode,
            final String voucherType, final String[] debitAmount, final String[] creditAmount, final String[] functionCode) {
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        BigDecimal totalAmountDbt = BigDecimal.ZERO;
        BigDecimal totalAmountCrd = BigDecimal.ZERO;
        if (RECEIPT.equals(voucherType) || JOURNAL_VOUCHER.equals(voucherType)) {
            for (int i = 0; i < debitAmount.length; i++)
                if (!StringUtils.isEmpty(debitAmount[i])) {
                    final BigDecimal amount = new BigDecimal(debitAmount[i]);
                    final String code = coaId[i];
                    final String funct = functionCode[i];
                    if (amount.compareTo(BigDecimal.ZERO) != 0)
                        accountdetails.add(populateDetailMap(code, BigDecimal.ZERO, amount, funct));

                    totalAmountDbt = totalAmountDbt.add(amount);
                }
            for (int i = 0; i < creditAmount.length; i++)
                if (!StringUtils.isEmpty(creditAmount[i])) {
                    final BigDecimal amount = new BigDecimal(creditAmount[i]);
                    final String code = coaId[i];
                    if (amount.compareTo(BigDecimal.ZERO) != 0)
                        accountdetails.add(populateDetailMap(code, amount, BigDecimal.ZERO));
                    totalAmountCrd = totalAmountCrd.add(amount);
                }
            accountdetails.add(populateDetailMap(bankAccountGlCode, totalAmountDbt.subtract(totalAmountCrd), BigDecimal.ZERO));
        } else {
            for (int i = 0; i < debitAmount.length; i++)
                if (!StringUtils.isEmpty(debitAmount[i])) {
                    final String code = coaPId[i];
                    final BigDecimal amount = new BigDecimal(debitAmount[i]);
                    accountdetails.add(populateDetailMap(code, amount, BigDecimal.ZERO));
                    totalAmountDbt = totalAmountDbt.add(amount);
                }
            accountdetails.add(populateDetailMap(bankAccountGlCode, BigDecimal.ZERO, totalAmountDbt));
        }
        return accountdetails;
    }

    HashMap<String, Object> populateDetailMap(final String glCode, final BigDecimal creditAmount, final BigDecimal debitAmount) {
        final HashMap<String, Object> detailMap = new HashMap<String, Object>();
        detailMap.put(VoucherConstant.CREDITAMOUNT, creditAmount.toString());
        detailMap.put(VoucherConstant.DEBITAMOUNT, debitAmount.toString());
        detailMap.put(VoucherConstant.GLCODE, glCode);
        // detailMap.put(VoucherConstant.FUNCTIONCODE, glCode);
        return detailMap;
    }

    HashMap<String, Object> populateDetailMap(final String glCode, final BigDecimal creditAmount, final BigDecimal debitAmount,
            final String function) {
        final HashMap<String, Object> detailMap = new HashMap<String, Object>();
        detailMap.put(VoucherConstant.CREDITAMOUNT, creditAmount.toString());
        detailMap.put(VoucherConstant.DEBITAMOUNT, debitAmount.toString());
        detailMap.put(VoucherConstant.GLCODE, glCode);
        detailMap.put(VoucherConstant.FUNCTIONCODE, function);
        return detailMap;
    }

    void updateInstrumentVoucherReference(final List<InstrumentHeader> instrumentList, final CVoucherHeader voucherHeader) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        iMap.put("Instrument header", instrumentList.get(0));
        iMap.put("Voucher header", voucherHeader);
        iList.add(iMap);
        instrumentService.updateInstrumentVoucherReference(iList);
    }

    private Date getNextDate(final Date date, final int amount) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, amount);
        return calendar.getTime();
    }

    public void setInstrumentHeaderService(final PersistenceService<InstrumentHeader, Long> instrumentHeaderService) {
        this.instrumentHeaderService = instrumentHeaderService;
    }

    public boolean isRestrictedtoOneFunctionCenter() {
        return isRestrictedtoOneFunctionCenter;
    }

    public void setRestrictedtoOneFunctionCenter(
            final boolean isRestrictedtoOneFunctionCenter) {

        this.isRestrictedtoOneFunctionCenter = isRestrictedtoOneFunctionCenter;
    }

    public void setChartOfAccountService(final PersistenceService chartOfAccountService) {
        this.chartOfAccountService = chartOfAccountService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public EisCommonService getEisCommonService() {
        return eisCommonService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public DishonorChequeService getDishonorChqService() {
        return dishonorChqService;
    }

    public void setDishonorChqService(final DishonorChequeService dishonorChqService) {
        this.dishonorChqService = dishonorChqService;
    }

    public EisUtilService getEisService() {
        return eisService;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public List<String> getDishonorReasonsList() {
        return dishonorReasonsList;
    }

    public void setDishonorReasonsList(final List<String> dishonorReasonsList) {
        this.dishonorReasonsList = dishonorReasonsList;
    }

}
