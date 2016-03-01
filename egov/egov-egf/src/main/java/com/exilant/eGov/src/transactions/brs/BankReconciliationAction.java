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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * BankReconciliationAction.java Created on Aug 7, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

/**
 *
 * @author Tilak
 * @version 1.00
 */

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.services.instrument.InstrumentHeaderService;
import org.egov.services.instrument.InstrumentOtherDetailsService;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BankBranch;
import com.exilant.eGov.src.domain.BankReconciliation;

public class BankReconciliationAction extends DispatchAction {
    public BankReconciliationAction() {
        super();

    }

    private static final Logger LOGGER = Logger.getLogger(BankReconciliationAction.class);
    String target = "";
    String alertMessage = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    Date dt;
    EGovernCommon cm = new EGovernCommon();
    @Autowired
    @Qualifier("instrumentService")
    private InstrumentService instrumentService;
    PersistenceService persistenceService;
    @Autowired
    @Qualifier("instrumentHeaderService")
    private InstrumentHeaderService instrumentHeaderService;
    @Autowired
    @Qualifier("instrumentOtherDetailsService")
    private InstrumentOtherDetailsService instrumentOtherDetailsService;

    /**
     * @param persistenceService the persistenceService to set
     */
    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * @param instrumentService the instrumentService to set
     */
    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    /*
     * get list of bankbranches
     */
    public ActionForward toLoad(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws Exception
    {

        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside toLoad");
            final BankBranch bb = new BankBranch();
            final Map hm = bb.getBankBranch();
            req.getSession().setAttribute("bankBranchList", hm);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> before ending BankReconciliationAction");
            target = "success";

        } catch (final Exception ex)
        {
            target = "error";
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);

        }
        return mapping.findForward(target);

    }

    /*
     * get list of accountnumber for given bankbranch
     */
    public ActionForward getAccountNumbers(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws Exception
    {

        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside getAccountNumbers");
            final org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm) form;
            final BankBranch bb = new BankBranch();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("bank id  " + bankRecForm.get("bankId"));
            final HashMap hm = bb.getAccNumber((String) bankRecForm.get("bankId"));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> after list creation BankReconciliationAction");
            req.getSession().setAttribute("accNumberList1", hm);
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> before ending BankReconciliationAction");
            target = "success";

        } catch (final Exception ex)
        {
            target = "error";
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);

        }
        return mapping.findForward(target);

    }

    /*
     * get list of accountnumber for given bankbranch(used in brs report screen)
     */
    public ActionForward getAccountNumbersForRS(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws Exception
    {

        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside getAccountNumbers");
            final org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm) form;
            final BankBranch bb = new BankBranch();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("bank id  " + bankRecForm.get("bankId"));
            final HashMap hm = bb.getAccNumber((String) bankRecForm.get("bankId"));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> after list creation BankReconciliationAction");
            req.getSession().setAttribute("accNumberList3", hm);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> before ending BankReconciliationAction");
            target = "success";

        } catch (final Exception ex)
        {
            target = "error";
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);

        }
        return mapping.findForward(target);

    }

    /*
     * get list of unreconciled cheques, bankbranch balance and sum of unreconciled receipts&payments amount
     */
    public ActionForward showBrsDetails(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws Exception
    {

        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside showBrsDetails");
            final org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm) form;
            // BigDecimal accountBalance=new BigDecimal("0.00");
            final BankReconciliation br = new BankReconciliation();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("bankacc id  " + bankRecForm.get("accId"));
            dt = new Date();
            dt = sdf.parse((String) bankRecForm.get("bankStatementDate"));
            final String recToDate = formatter.format(dt);
            final Date toDate = sdf.parse((String) bankRecForm.get("recToDate"));
            if (LOGGER.isInfoEnabled())
                LOGGER.info("recToDate  " + recToDate);
            String recFromDate = (String) bankRecForm.get("recFromDate");
            final Date fromDate = sdf.parse((String) bankRecForm.get("recFromDate"));
            recFromDate = StringUtils.isNotBlank(recFromDate) ? formatter.format(sdf.parse((String) bankRecForm
                    .get("recFromDate"))) : "";
            if (LOGGER.isInfoEnabled())
                LOGGER.info("recFromDate  " + recFromDate);
            final ArrayList al = br.getRecordsToReconcile((String) bankRecForm.get("accId"), recFromDate, recToDate);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> after list creation BankReconciliationAction");
            req.setAttribute("brsDetails", al);
            final BigDecimal accountBalance = cm.getAccountBalance(recToDate, (String) bankRecForm.get("accId")).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            req.setAttribute("accountBalance", "" + accountBalance);

            final String bankAccIdStr = (String) bankRecForm.get("accId");
            Integer bankAccountId = 0;
            if (bankAccIdStr != null)
                bankAccountId = Integer.parseInt(bankAccIdStr);
            // String unReconciledDrCr=br.getUnReconciledDrCr((String)bankRecForm.get("accId"),recFromDate,recToDate,null);
            final String unReconciledDrCr = br.getUnReconciledDrCr(bankAccountId, fromDate, toDate);
            final String drcrValues[] = unReconciledDrCr.split("/");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("  unReconciledDrCr   " + unReconciledDrCr);
            if (LOGGER.isInfoEnabled())
                LOGGER.info(drcrValues[0] + "  " + drcrValues[3] + "  " + drcrValues[1] + "   " + drcrValues[2]);
            // double unReconciledCr=(Double.parseDouble(drcrValues[0]))+(Double.parseDouble(drcrValues[3]));
            // double unReconciledDr=(Double.parseDouble(drcrValues[1]))+(Double.parseDouble(drcrValues[2]));
            final double unReconciledCr = Double.parseDouble(drcrValues[0]);
            final double unReconciledCrOthers = Double.parseDouble(drcrValues[1]);
            final double unReconciledDr = Double.parseDouble(drcrValues[2]);
            final double unReconciledDrOthers = Double.parseDouble(drcrValues[3]);
            final double unReconciledCrBrsEntry = Double.parseDouble(drcrValues[4]);
            final double unReconciledDrBrsEntry = Double.parseDouble(drcrValues[5]);
            req.setAttribute("unReconciledCr", "" + unReconciledCr);
            req.setAttribute("unReconciledCrOthers", "" + unReconciledCrOthers);
            req.setAttribute("unReconciledDr", "" + unReconciledDr);
            req.setAttribute("unReconciledDrOthers", "" + unReconciledDrOthers);
            req.setAttribute("unReconciledCrBrsEntries", "" + unReconciledCrBrsEntry);
            req.setAttribute("unReconciledDrBrsEntries", "" + unReconciledDrBrsEntry);

            req.setAttribute("balanceAsPerStatement", "" + Double.parseDouble((String) bankRecForm.get("balAsPerStatement")));

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> before ending BankReconciliationAction");
            target = "success";

        } catch (final Exception ex)
        {
            target = "error";
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);

        }
        return mapping.findForward(target);

    }

    /*
     * get list of unreconciled cheques, bankbranch balance(used in Brs report screen)
     */
    public ActionForward brsSummary(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws Exception
    {

        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside brsSummary");
            final org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm) form;
            // accountBalance=new BigDecimal("0.00");
            final BankReconciliation br = new BankReconciliation();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("bankacc id  " + bankRecForm.get("accId"));
            dt = new Date();
            dt = sdf.parse((String) bankRecForm.get("bankStatementDate"));
            final FinancialYearHibernateDAO financialYearDAO = new FinancialYearHibernateDAO(CFinancialYear.class,
                    HibernateUtil.getCurrentSession());

            final CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dt);
            final String recDate = formatter.format(dt);
            final Date fromDate = finYearByDate.getStartingDate();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("recDate  " + recDate);
            final BigDecimal accountBalance = cm.getAccountBalance(recDate, (String) bankRecForm.get("accId")).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            req.setAttribute("accountBalance", "" + accountBalance);

            final String bankAccIdStr = (String) bankRecForm.get("accId");
            Integer bankAccountId = 0;
            if (bankAccIdStr != null)
                bankAccountId = Integer.parseInt(bankAccIdStr);
            final String unReconciledDrCr = br.getUnReconciledDrCr(bankAccountId, fromDate, dt);
            final String drcrValues[] = unReconciledDrCr.split("/");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("  unReconciledDrCr   " + unReconciledDrCr);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("  drcrValues[]   " + drcrValues[1]);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("  drcrValues   " + drcrValues.length);
            if (LOGGER.isInfoEnabled())
                LOGGER.info(drcrValues[0] + "  " + drcrValues[1] + "  " + drcrValues[2] + "   " + drcrValues[3]);
            final double unReconciledCr = Double.parseDouble(drcrValues[0]);
            final double unReconciledCrOthers = Double.parseDouble(drcrValues[1]);
            final double unReconciledDr = Double.parseDouble(drcrValues[2]);
            final double unReconciledDrOthers = Double.parseDouble(drcrValues[3]);
            final double unReconciledCrBrsEntry = Double.parseDouble(drcrValues[4]);
            final double unReconciledDrBrsEntry = Double.parseDouble(drcrValues[5]);
            req.setAttribute("unReconciledCr", "" + unReconciledCr);
            req.setAttribute("unReconciledCrOthers", "" + unReconciledCrOthers);
            req.setAttribute("unReconciledDr", "" + unReconciledDr);
            req.setAttribute("unReconciledDrOthers", "" + unReconciledDrOthers);
            req.setAttribute("unReconciledCrBrsEntries", "" + unReconciledCrBrsEntry);
            req.setAttribute("unReconciledDrBrsEntries", "" + unReconciledDrBrsEntry);
            // commenting for time being to load system faster
            // ArrayList al=br.getUnReconciledCheques((String)bankRecForm.get("accId"),recDate,null);

            final ArrayList al = new ArrayList();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>>>>>>>>>  al   ....." + al);
            req.setAttribute("unreconciledCheques", al);
            req.setAttribute("balanceAsPerStatement", "" + Double.parseDouble((String) bankRecForm.get("balAsPerStatement")));

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> before ending ReconciliationSummaryAction");
            target = "success";

        } catch (final Exception ex)
        {
            target = "error";
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);
            //
        }
        return mapping.findForward(target);

    }

    /*
     * To reconcile cheques
     */
    public ActionForward reconcile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req,
            final HttpServletResponse res)
            throws Exception
    {

        try {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(">>> inside reconcile");
            final org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm) form;
            bankRecForm.get("recID");
            final String[] instrumentId = (String[]) bankRecForm.get("ihId");
            final String[] chqDate = (String[]) bankRecForm.get("bankStatementChqDate");
            dt = new Date();
            dt = sdf.parse((String) bankRecForm.get("recToDate"));
            final Date reconcileDate = dt;
            formatter.format(dt);
            new BankReconciliation();

            for (int i = 0; i < instrumentId.length; i++)
            {
                if (chqDate[i] == null || chqDate[i].equalsIgnoreCase(""))
                    continue;
                dt = new Date();
                dt = sdf.parse(chqDate[i]);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("formatter.format(dt)   " + formatter.format(dt));

                final InstrumentHeader ih = instrumentHeaderService.find("from InstrumentHeader where id=?",
                        Long.valueOf(instrumentId[i]));
                ih.setStatusId(instrumentService.getStatusId(FinancialConstants.INSTRUMENT_RECONCILED_STATUS));
                instrumentHeaderService.persist(ih);
                InstrumentOtherDetails io = instrumentOtherDetailsService.find(
                        "from InstrumentOtherDetails where instrumentHeaderId=?", ih);
                if (io == null)
                {
                    io = new InstrumentOtherDetails();
                    io.setInstrumentStatusDate(dt);
                    io.setReconciledAmount(ih.getInstrumentAmount());
                    io.setInstrumentHeaderId(ih);

                }
                io.setInstrumentStatusDate(dt);
                io.setReconciledAmount(ih.getInstrumentAmount());
                io.setReconciledOn(reconcileDate);
                instrumentOtherDetailsService.persist(io);

            }

            /*
             * for(int i=0; i < recId.length; i++) { if(chqDate[i] == null || chqDate[i].equalsIgnoreCase("")) continue;
             * if(LOGGER.isInfoEnabled()) LOGGER.info("chqDate[i]  "+chqDate[i]+"   recId[i]   "+ recId[i]); dt=new Date(); dt =
             * sdf.parse(chqDate[i]); if(LOGGER.isInfoEnabled()) LOGGER.info("formatter.format(dt)   "+ formatter.format(dt));
             * br.setId(recId[i]); br.setIsReconciled("1"); br.setReconciliationDate(reconcileDate);
             * br.setRecChequeDate(formatter.format(dt)); br.update(null); }
             */
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(">>> before ending reconcile");
            alertMessage = "Executed successfully";
            req.setAttribute("alertMessage", alertMessage);
            target = "success";

        } catch (final Exception ex)
        {
            target = "error";
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);
            //
        }
        return mapping.findForward(target);
    }
}
