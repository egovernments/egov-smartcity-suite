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
package org.egov.egf.web.actions.report;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.deduction.model.EgRemittance;
import org.egov.egf.model.BillRegisterReportBean;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.FinancialConstants;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author manoranjan
 *
 */
@SuppressWarnings("unchecked")
@ParentPackage("egov")
@Results({
    @Result(name = BillRegisterReportAction.NEW, location = "billRegisterReport-" + BillRegisterReportAction.NEW + ".jsp"),
    @Result(name = "completeBill", location = "billRegisterReport-completeBill.jsp")
})
public class BillRegisterReportAction extends SearchFormAction {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BillRegisterReportAction.class);
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
    private final List<String> headerFields = new ArrayList<String>();
    private final List<String> mandatoryFields = new ArrayList<String>();
    private CVoucherHeader voucherHeader = new CVoucherHeader();
    private static Map<String, List<String>> netAccountCode = new HashMap<String, List<String>>(); // have list of all net payable
    // accounts codes based on the
    // expenditure type.
    private Date fromDate;
    
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired	
    private  AppConfigValueService appConfigValueService;
    
   

	private Date toDate;
    private String exptype;
    private Long preVoucherId;
    private String billType;
    private String billNumber;
    private boolean isCompleteBillRegisterReport = false;
    private final String chqdelimitSP = "/";
    private final String chqdelimitDP = "//";
    private List<BillRegisterReportBean> billRegReportList;
    private List<String> chequeStatusCheckList = new ArrayList<String>();
    StringBuffer getRemiitPaymentVoucherQry = new StringBuffer("");
    List<Integer> cancelledChequeStatus = new ArrayList<Integer>();

    private static boolean errorState = false;

    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public BillRegisterReportAction() {
        voucherHeader.setVouchermis(new Vouchermis());
        addRelatedEntity("vouchermis.departmentid", Department.class);
        addRelatedEntity("fundId", Fund.class);
        addRelatedEntity("vouchermis.schemeid", Scheme.class);
        addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
        addRelatedEntity("vouchermis.functionary", Functionary.class);
        addRelatedEntity("vouchermis.divisionid", Boundary.class);
        addRelatedEntity("fundsourceId", Fundsource.class);
        chequeStatusCheckList.add(FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
        chequeStatusCheckList.add(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS);
        chequeStatusCheckList.add(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS);
        chequeStatusCheckList.add(FinancialConstants.INSTRUMENT_CANCELLED_STATUS);

        

        getRemiitPaymentVoucherQry.append("select  distinct rm from EgRemittance rm join rm.egRemittanceDetail rdtl  " +
                "where rdtl.egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.voucherNumber =?" +
                "and rdtl.egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.status!=?" +
                " and rm.voucherheader.status!=?")
                .append(" order by rm.voucherheader.id");

    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterReportAction | prepare | start");
        String query = getQuery();
        if (null != sortField)
            query = query + " order by " + sortField + " " + sortOrder;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterReportAction | prepare | query >> " + query);
        return new SearchQuerySQL(query, "select count(*) from ( " + query + " ) as count", null);
    }

    @Override
    public Object getModel() {

        return voucherHeader;
    }

    @Action(value = "/report/billRegisterReport-newform")
    public String newform() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        isCompleteBillRegisterReport = false;
        loadDropdownData();
        toDate = fromDate = null;
        voucherHeader.reset();
        exptype = billType = null;
        if (errorState)
            addActionError(getText("bill.register.report.system.error"));
        return NEW;
    }

    @Action(value = "/report/billRegisterReport-searchform")
    public String searchform() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        isCompleteBillRegisterReport = true;
        loadDropdownData();
        toDate = fromDate = null;
        voucherHeader.reset();
        exptype = billType = null;
        if (errorState)
            addActionError(getText("bill.register.report.system.error"));
        return "completeBill";
    }

    @ValidationErrorPage(value = "new")
    public String list() throws Exception {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterReportAction | list | start");
        setPageSize(50);
        loadDropdownData();
        validateBeforeSearch();
        search();
        formatSearchResult();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterReportAction | list | End");

        return NEW;
    }

    @ReadOnly
    @ValidationErrorPage(value = "completeBill")
    @Action(value = "/report/billRegisterReport-billSearch")
    public String billSearch() throws Exception {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterReportAction | completeBill | start");
        isCompleteBillRegisterReport = true;
        setPageSize(50);
        loadDropdownData();
        validateBeforeSearch();
        search();
        formatSearchResult();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterReportAction | list | End");

        return "completeBill";

    }

    public void validateBeforeSearch() {
        if (null != fromDate && null != toDate && fromDate.after(toDate))
            throw new ValidationException(Arrays.asList(new ValidationError("date",
                    "from date can not be greater than to date")));
        else if (dropdownData.get("expenditureList").size() == 0)
            throw new ValidationException(Arrays.asList(new ValidationError("date",
                    "There is no Bill present in the system")));
        if (isCompleteBillRegisterReport)
            if (null != billNumber && !billNumber.equals("")
            && null != voucherHeader.getVoucherNumber() && !voucherHeader.equals("") && !StringUtils
            .isEmpty(voucherHeader.getVoucherNumber()))
                throw new ValidationException(Arrays.asList(new ValidationError("VoucherNumber",
                        "Enter either Voucher number or Bill number")));
    }

    public boolean shouldShowHeaderField(final String field) {
        return headerFields.contains(field);
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    @SuppressWarnings("unused")
    protected void formatSearchResult() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterReportAction | formatSearchResult | start");
        billRegReportList = new ArrayList<BillRegisterReportBean>();
        final EgovPaginatedList egovPaginatedList = (EgovPaginatedList) searchResult;
        final List<Object[]> list = egovPaginatedList.getList();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterReportAction | formatSearchResult | list size : " + list.size());

        for (final Object[] object : list)
            try {

                final BillRegisterReportBean billRegReport = new BillRegisterReportBean();
                billRegReport.setBillNumber(object[0].toString());
                billRegReport.setVoucherNumber(object[1] != null ? object[1].toString() : "");
                billRegReport.setPartyName(object[2] != null ? object[2].toString() : "");
                billRegReport.setGrossAmount(null != object[3] ? new BigDecimal(object[3].toString()).setScale(2,BigDecimal.ROUND_HALF_EVEN)
                        : BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_EVEN));
                billRegReport.setNetAmount(null != object[4] ? new BigDecimal(object[4].toString()).setScale(2,BigDecimal.ROUND_HALF_EVEN) : BigDecimal.ZERO
                        .setScale(2,BigDecimal.ROUND_HALF_EVEN));
                billRegReport.setDeductionAmount(billRegReport.getGrossAmount().subtract(billRegReport.getNetAmount()));
                billRegReport.setStatus(null != object[5] ? object[5].toString().toUpperCase() : "");
                billRegReport.setBillDate(DDMMYYYYFORMATS.format((Date) object[6]));
                if (!StringUtils.isEmpty(billRegReport.getVoucherNumber())) {
                    final List<Miscbilldetail> miscBillList = persistenceService.findAllBy(
                            " from Miscbilldetail mis where mis.billnumber=? " +
                                    " and mis.billVoucherHeader.voucherNumber=?", billRegReport.getBillNumber(),
                                    billRegReport.getVoucherNumber());
                    if (null != miscBillList && miscBillList.size() > 0) {
                        BigDecimal paidAmount = null;
                        final StringBuffer payMentVoucherNumber = new StringBuffer("");
                        final StringBuffer chequeNoAndDate = new StringBuffer("");
                        preVoucherId = miscBillList.get(0).getPayVoucherHeader().getId();
                        for (final Miscbilldetail miscbilldetail : miscBillList) {
                            if (null != miscbilldetail.getPayVoucherHeader()
                                    && (miscbilldetail.getPayVoucherHeader().getStatus().
                                            equals(Integer.valueOf(FinancialConstants.CREATEDVOUCHERSTATUS)) || miscbilldetail
                                            .getPayVoucherHeader().getStatus().
                                            equals(Integer.valueOf(FinancialConstants.PREAPPROVEDVOUCHERSTATUS))))
                                if (!StringUtils.isEmpty(payMentVoucherNumber.toString())) {
                                    payMentVoucherNumber.append("|").append(
                                            miscbilldetail.getPayVoucherHeader().getVoucherNumber());
                                    paidAmount = paidAmount.add(miscbilldetail.getPaidamount()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                                    final Paymentheader paymentMode = (Paymentheader) persistenceService.find(
                                            "from Paymentheader where voucherheader=?", miscbilldetail.getPayVoucherHeader());
                                    if (!paymentMode.getType().equals(FinancialConstants.MODEOFPAYMENT_RTGS)) {
                                        final Query qry = persistenceService.getSession().createQuery(
                                                "from InstrumentVoucher iv where iv.voucherHeaderId.id=:vhId and" +
                                                " iv.instrumentHeaderId.statusId.id not in(:cancelledChequeList)");
                                        qry.setLong("vhId", miscbilldetail.getPayVoucherHeader().getId());
                                        qry.setParameterList("cancelledChequeList", cancelledChequeStatus);
                                        final List<InstrumentVoucher> instrumentVoucherList = qry.list();
                                        if (instrumentVoucherList.size() > 0)
                                            for (final InstrumentVoucher inst : instrumentVoucherList)
                                                // chequeStatus=inst.getInstrumentHeaderId().getStatusId().getId();
                                                // if(!cancelledChequeStatus.contains(chequeStatus)){
                                                if (!StringUtils.isEmpty(chequeNoAndDate.toString())) {
                                                    if (preVoucherId.equals(inst.getVoucherHeaderId().getId()))
                                                        chequeNoAndDate
                                                        .append(chqdelimitSP)
                                                        .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                                        .append(" ")
                                                        .append(DDMMYYYYFORMATS.format(inst.getInstrumentHeaderId()
                                                                .getInstrumentDate()));
                                                    else
                                                        chequeNoAndDate
                                                        .append(chqdelimitDP)
                                                        .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                                        .append(" ")
                                                        .append(DDMMYYYYFORMATS.format(inst.getInstrumentHeaderId()
                                                                .getInstrumentDate()));
                                                }
                                                else
                                                    chequeNoAndDate
                                                .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                                .append(" ")
                                                .append(DDMMYYYYFORMATS.format(inst.getInstrumentHeaderId()
                                                        .getInstrumentDate()));
                                    } else {
                                        final Query qry = persistenceService.getSession().createQuery(
                                                "from InstrumentVoucher iv where iv.voucherHeaderId.id=:vhId and" +
                                                " iv.instrumentHeaderId.statusId.id not in(:cancelledChequeList)");
                                        qry.setLong("vhId", miscbilldetail.getPayVoucherHeader().getId());
                                        qry.setParameterList("cancelledChequeList", cancelledChequeStatus);
                                        final List<InstrumentVoucher> instrumentVoucherList = qry.list();
                                        if (instrumentVoucherList.size() > 0)
                                            for (final InstrumentVoucher inst : instrumentVoucherList)
                                                if (!StringUtils.isEmpty(chequeNoAndDate.toString())) {
                                                    if (preVoucherId.equals(inst.getVoucherHeaderId().getId()))
                                                        chequeNoAndDate
                                                        .append(chqdelimitSP)
                                                        .append(inst.getInstrumentHeaderId().getTransactionNumber() != null ? inst
                                                                .getInstrumentHeaderId().getTransactionNumber()
                                                                        : "")
                                                                .append(" ")
                                                                .append(inst.getInstrumentHeaderId().getTransactionDate() != null ? DDMMYYYYFORMATS
                                                                        .format(inst.getInstrumentHeaderId().getTransactionDate())
                                                                        : "");
                                                    else
                                                        chequeNoAndDate
                                                        .append(chqdelimitDP)
                                                        .append(inst.getInstrumentHeaderId().getTransactionNumber() != null ? inst
                                                                .getInstrumentHeaderId().getTransactionNumber()
                                                                        : "")
                                                                .append(" ")
                                                                .append(inst.getInstrumentHeaderId().getTransactionDate() != null ? DDMMYYYYFORMATS
                                                                        .format(inst.getInstrumentHeaderId().getTransactionDate())
                                                                        : "");
                                                } else
                                                    chequeNoAndDate
                                                    .append(inst.getInstrumentHeaderId().getTransactionNumber() != null ? inst
                                                            .getInstrumentHeaderId().getTransactionNumber()
                                                                    : "")
                                                            .append(" ")
                                                            .append(inst.getInstrumentHeaderId().getTransactionDate() != null ? DDMMYYYYFORMATS
                                                                    .format(inst.getInstrumentHeaderId().getTransactionDate())
                                                                    : "");
                                    }
                                } else {
                                    paidAmount = miscbilldetail.getPaidamount().setScale(2,BigDecimal.ROUND_HALF_EVEN);
                                    payMentVoucherNumber.append(miscbilldetail.getPayVoucherHeader().getVoucherNumber());
                                    final Paymentheader paymentMode = (Paymentheader) persistenceService.find(
                                            "from Paymentheader where voucherheader=?", miscbilldetail.getPayVoucherHeader());
                                    if (!paymentMode.getType().equals(FinancialConstants.MODEOFPAYMENT_RTGS)) {
                                        // List<InstrumentVoucher>
                                        // instrumentVoucherList=(List<InstrumentVoucher>)persistenceService.findAllBy(" from InstrumentVoucher where voucherHeaderId=?",
                                        // miscbilldetail.getPayVoucherHeader());
                                        final Query qry = persistenceService.getSession().createQuery(
                                                "from InstrumentVoucher iv where iv.voucherHeaderId.id=:vhId and" +
                                                " iv.instrumentHeaderId.statusId.id not in(:cancelledChequeList)");
                                        qry.setLong("vhId", miscbilldetail.getPayVoucherHeader().getId());
                                        qry.setParameterList("cancelledChequeList", cancelledChequeStatus);
                                        final List<InstrumentVoucher> instrumentVoucherList = qry.list();
                                        if (instrumentVoucherList.size() > 0)
                                            for (final InstrumentVoucher inst : instrumentVoucherList)
                                                if (!StringUtils.isEmpty(chequeNoAndDate.toString())) {
                                                    if (preVoucherId.equals(inst.getVoucherHeaderId().getId()))
                                                        chequeNoAndDate
                                                        .append(chqdelimitSP)
                                                        .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                                        .append(" ")
                                                        .append(DDMMYYYYFORMATS.format(inst.getInstrumentHeaderId()
                                                                .getInstrumentDate()));
                                                    else
                                                        chequeNoAndDate
                                                        .append(chqdelimitDP)
                                                        .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                                        .append(" ")
                                                        .append(inst.getInstrumentHeaderId().getInstrumentDate() != null ? DDMMYYYYFORMATS
                                                                .format(inst.getInstrumentHeaderId().getInstrumentDate())
                                                                : "");
                                                } else
                                                    chequeNoAndDate
                                                    .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                                    .append(" ")
                                                    .append(inst.getInstrumentHeaderId().getInstrumentDate() != null ? DDMMYYYYFORMATS
                                                            .format(inst.getInstrumentHeaderId().getInstrumentDate())
                                                            : "");
                                    } else {
                                        final Query qry = persistenceService.getSession().createQuery(
                                                "from InstrumentVoucher iv where iv.voucherHeaderId.id=:vhId and" +
                                                " iv.instrumentHeaderId.statusId.id not in(:cancelledChequeList)");
                                        qry.setLong("vhId", miscbilldetail.getPayVoucherHeader().getId());
                                        qry.setParameterList("cancelledChequeList", cancelledChequeStatus);
                                        final List<InstrumentVoucher> instrumentVoucherList = qry.list();
                                        if (instrumentVoucherList.size() > 0)
                                            for (final InstrumentVoucher inst : instrumentVoucherList)
                                                if (!StringUtils.isEmpty(chequeNoAndDate.toString())) {
                                                    if (preVoucherId.equals(inst.getVoucherHeaderId().getId()))
                                                        chequeNoAndDate
                                                        .append(chqdelimitSP)
                                                        .append(inst.getInstrumentHeaderId().getTransactionNumber() != null ? inst
                                                                .getInstrumentHeaderId().getTransactionNumber()
                                                                        : ""
                                                                )
                                                                .append(" ")
                                                                .append(inst.getInstrumentHeaderId().getTransactionDate() != null ? DDMMYYYYFORMATS
                                                                        .format
                                                                        (inst.getInstrumentHeaderId().getTransactionDate())
                                                                        : "");
                                                    else
                                                        chequeNoAndDate
                                                        .append(chqdelimitDP)
                                                        .append(inst.getInstrumentHeaderId().getTransactionNumber() != null ? inst
                                                                .getInstrumentHeaderId().getTransactionNumber()
                                                                        : "")
                                                                .append(" ")
                                                                .append(inst.getInstrumentHeaderId().getTransactionDate() != null ? DDMMYYYYFORMATS
                                                                        .format(inst.getInstrumentHeaderId().getTransactionDate())
                                                                        : "");
                                                } else
                                                    chequeNoAndDate
                                                    .append(inst.getInstrumentHeaderId().getTransactionNumber() != null ? inst
                                                            .getInstrumentHeaderId().getTransactionNumber()
                                                                    : "")
                                                            .append(" ")
                                                            .append(inst.getInstrumentHeaderId().getTransactionDate() != null ? DDMMYYYYFORMATS
                                                                    .format(inst.getInstrumentHeaderId().getTransactionDate())
                                                                    : "");
                                    }
                                }

                            preVoucherId = miscbilldetail.getPayVoucherHeader().getId();
                            if (isCompleteBillRegisterReport)
                                getRemittancePaymentDetail(billRegReport);
                        }
                        billRegReport.setPaidAmount(paidAmount);
                        billRegReport.setPaymentVoucherNumber(payMentVoucherNumber.toString());
                        billRegReport.setChequeNumAndDate(chequeNoAndDate.toString());

                    } else if (isCompleteBillRegisterReport)
                        getRemittancePaymentDetail(billRegReport);

                }

                billRegReportList.add(billRegReport);
            } catch (final Exception e) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Failed while processing bill number :" + object[0].toString());
                throw e;
            }
        egovPaginatedList.setList(billRegReportList);
    }

    /*
     * Get remittance payment detail for the voucher Below lines to get the cheque and cheque date for the voucher /* In case
     * where for single payment multiple cheque are assigned we use chqdelimitSP / single slash separate cheque nos In case where
     * for a voucher multiple BPVs are issued and for the BPVs different cheques are issued we seperate them with chqdelimitDP //
     * double slash
     */
    private void getRemittancePaymentDetail(final BillRegisterReportBean billRegReport) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("...........Getting Remitance Payment details........");
        List<EgRemittance> remittancePaymentItem = new ArrayList<EgRemittance>();
        final StringBuffer remmitPaymentVoucherNumber = new StringBuffer("");

        StringBuffer remittanceChequeNoAndDate = new StringBuffer("");
        Long paymentVhId = null;
        List<InstrumentVoucher> instrumentVoucherList = new ArrayList<InstrumentVoucher>();

        if (billRegReport.getVoucherNumber() != null) {

            remittancePaymentItem = persistenceService.findAllBy(getRemiitPaymentVoucherQry.toString()
                    , billRegReport.getVoucherNumber(), FinancialConstants.CANCELLEDVOUCHERSTATUS,
                    FinancialConstants.CANCELLEDVOUCHERSTATUS);

            if (remittancePaymentItem.size() > 0) {
                paymentVhId = remittancePaymentItem.get(0).getVoucherheader().getId();
                remittanceChequeNoAndDate = new StringBuffer("");
                for (int i = 0; i < remittancePaymentItem.size(); i++) {

                    // if(remittancePaymentItem.get(i).getVoucherheader().getStatus())
                    remmitPaymentVoucherNumber.append(remittancePaymentItem.get(i).getVoucherheader().getVoucherNumber() + "|");
                    final Query qry = persistenceService.getSession().createQuery(
                            "from InstrumentVoucher iv where iv.voucherHeaderId.id=:vhId and" +
                            " iv.instrumentHeaderId.statusId.id not in(:cancelledChequeList)");
                    qry.setLong("vhId", remittancePaymentItem.get(i).getVoucherheader().getId());
                    qry.setParameterList("cancelledChequeList", cancelledChequeStatus);
                    instrumentVoucherList = qry.list();

                    if (instrumentVoucherList.size() > 0)
                        for (final InstrumentVoucher inst : instrumentVoucherList)
                            if (!StringUtils.isEmpty(remittanceChequeNoAndDate.toString())) {
                                if (paymentVhId != null && paymentVhId.equals(inst.getVoucherHeaderId().getId()))
                                    remittanceChequeNoAndDate
                                    .append(chqdelimitSP)
                                    .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                    .append(" ")
                                    .append(inst.getInstrumentHeaderId().getInstrumentDate() != null ? DDMMYYYYFORMATS
                                            .format(inst.getInstrumentHeaderId().getInstrumentDate()) : "");
                                else
                                    remittanceChequeNoAndDate
                                    .append(chqdelimitDP)
                                    .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                    .append(" ")
                                    .append(inst.getInstrumentHeaderId().getInstrumentDate() != null ? DDMMYYYYFORMATS
                                            .format(inst.getInstrumentHeaderId().getInstrumentDate()) : "");
                            } else
                                remittanceChequeNoAndDate
                                .append(inst.getInstrumentHeaderId().getInstrumentNumber())
                                .append(" ")
                                .append(inst.getInstrumentHeaderId().getInstrumentDate() != null ? DDMMYYYYFORMATS
                                        .format(inst.getInstrumentHeaderId().getInstrumentDate()) : "");
                    paymentVhId = remittancePaymentItem.get(i).getVoucherheader().getId();
                }
            }
            billRegReport.setRemittanceVoucherNumber(remmitPaymentVoucherNumber.length() > 0 ? remmitPaymentVoucherNumber
                    .substring(0, remmitPaymentVoucherNumber.length() - 1) : " ");
            billRegReport.setRemittanceChequeNumberAndDate(remittanceChequeNoAndDate.toString());
        }
    }

    public void netAccountCodeValue() {
        
        final Session session = persistenceService.getSession();
        try {

        	final List<AppConfigValues> cBillNetPurpose = appConfigValueService.
                    getConfigValuesByModuleAndKey("EGF", "contingencyBillPurposeIds");
            
        	
        	final List<String> cBillNetPayCodeList = new ArrayList<String>();
            String coaQuery;
            for (final AppConfigValues appConfigValues : cBillNetPurpose) {
                coaQuery = "from CChartOfAccounts where purposeId in ( " + appConfigValues.getValue() + " )";
                final List<CChartOfAccounts> coaList = session.createQuery(coaQuery).list();
                for (final CChartOfAccounts chartOfAccounts : coaList)
                    cBillNetPayCodeList.add(chartOfAccounts.getId().toString());
            }
            netAccountCode.put("Expense", cBillNetPayCodeList);

            // setting net pay account codes for purchase type.
            final List<String> pBillNetPayCodeList = new ArrayList<String>();
            final List<AppConfigValues> purchBillNetPurpose = appConfigValueService.
                    getConfigValuesByModuleAndKey("EGF", "purchaseBillPurposeIds");
            
            for (final AppConfigValues appConfigValues : purchBillNetPurpose) {
                coaQuery = "from CChartOfAccounts where purposeId in ( " + appConfigValues.getValue() + " )";
                final List<CChartOfAccounts> coaList = session.createQuery(coaQuery).list();
                for (final CChartOfAccounts chartOfAccounts : coaList)
                    pBillNetPayCodeList.add(chartOfAccounts.getId().toString());
            }
            netAccountCode.put("Purchase", pBillNetPayCodeList);

            // setting net pay account codes for salary type.
            final List<String> sBillNetPayCodeList = new ArrayList<String>();
            final List<AppConfigValues> sBillNetPurpose = appConfigValueService.
                    getConfigValuesByModuleAndKey("EGF", "salaryBillPurposeIds");
            
           if (LOGGER.isDebugEnabled())
                LOGGER.debug("Number of salary purpose ids - " + sBillNetPurpose.size());
            for (final AppConfigValues appConfigValues : sBillNetPurpose) {
                coaQuery = "from CChartOfAccounts where purposeId in ( " + appConfigValues.getValue() + " )";
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Querying CChartOfAccounts -  " + coaQuery);
                final List<CChartOfAccounts> coaList = session.createQuery(coaQuery).list();
                for (final CChartOfAccounts chartOfAccounts : coaList)
                    sBillNetPayCodeList.add(chartOfAccounts.getId().toString());

            }
            netAccountCode.put("Salary", sBillNetPayCodeList);

            // setting net pay account codes for works type.

            final List<String> wBillNetPayCodeList = new ArrayList<String>();
            
            final List<AppConfigValues> wBillNetPurpose = appConfigValueService.
                    getConfigValuesByModuleAndKey("EGF", "worksBillPurposeIds");

            for (final AppConfigValues appConfigValues : wBillNetPurpose) {
                coaQuery = "from CChartOfAccounts where purposeId in ( " + appConfigValues.getValue() + " )";
                final List<CChartOfAccounts> coaList = session.createQuery(coaQuery).list();
                for (final CChartOfAccounts chartOfAccounts : coaList)
                    wBillNetPayCodeList.add(chartOfAccounts.getId().toString());
            }
            netAccountCode.put("Works", wBillNetPayCodeList);

            // setting the netpayable code for pension type
            final List<String> penBillNetPayCodeList = new ArrayList<String>();
            final List<AppConfigValues> pensionBillNetPurpose = appConfigValueService.
                    getConfigValuesByModuleAndKey("EGF", "pensionBillPurposeIds");
            
            for (final AppConfigValues appConfigValues : pensionBillNetPurpose) {
                coaQuery = "from CChartOfAccounts where purposeId in ( " + appConfigValues.getValue() + " )";
                final List<CChartOfAccounts> coaList = session.createQuery(coaQuery).list();
                for (final CChartOfAccounts chartOfAccounts : coaList)
                    penBillNetPayCodeList.add(chartOfAccounts.getId().toString());
            }

            netAccountCode.put("Pension", penBillNetPayCodeList);

        } catch (final Exception e)
        {
            errorState = true;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("EXCEPTION IN STATIC BLOCK OF BillRegisterReportAction ");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(e.getMessage());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(e);
        }
    }

    protected String getQuery() {
        final StringBuffer query = new StringBuffer(1000);
        final StringBuffer whereQuery = new StringBuffer(200);
        new StringBuffer(50);

        /*
         * if(null != voucherHeader.getVoucherNumber() && !StringUtils.isEmpty(voucherHeader.getVoucherNumber())){
         * whereQuery.append(" and vh.vouchernumber like '%"+voucherHeader.getVoucherNumber()+"%'"); }
         */

        if (null != voucherHeader.getFundId())
            whereQuery.append(" and mis.fundid=" + voucherHeader.getFundId().getId());
        if (null != voucherHeader.getVouchermis().getDepartmentid())
            whereQuery.append(" and mis.departmentid=" + voucherHeader.getVouchermis().getDepartmentid().getId());
        if (null != voucherHeader.getVouchermis().getSchemeid())
            whereQuery.append(" and mis.schemeid=" + voucherHeader.getVouchermis().getSchemeid().getId());
        if (null != voucherHeader.getVouchermis().getSubschemeid())
            whereQuery.append(" and mis.subschemeid=" + voucherHeader.getVouchermis().getSubschemeid().getId());
        if (null != voucherHeader.getVouchermis().getFunctionary())
            whereQuery.append(" and mis.functionaryid=" + voucherHeader.getVouchermis().getFunctionary().getId());
        if (null != voucherHeader.getVouchermis().getFundsource())
            whereQuery.append(" and mis.fundsourceid=" + voucherHeader.getVouchermis().getFundsource().getId());
        if (null != voucherHeader.getVouchermis().getDivisionid())
            whereQuery.append(" and mis.fieldid=" + voucherHeader.getVouchermis().getDivisionid().getId());
        if (!StringUtils.isEmpty(billType))
            whereQuery.append(" and  b.billtype='" + billType + "'");
        if (null != fromDate)
            whereQuery.append(" and b.billdate >= to_date('" + DDMMYYYYFORMATS.format(fromDate) + "','dd/MM/yyyy')");
        if (null != toDate)
            whereQuery.append(" and b.billdate <= to_date('" + DDMMYYYYFORMATS.format(toDate) + "','dd/MM/yyyy')");
        if (null != billNumber && !StringUtils.isEmpty(billNumber))
            whereQuery.append(" and b.billnumber like '%" + billNumber + "%'");

        if (StringUtils.isEmpty(exptype)) {
            final List<String> expndtrList = dropdownData.get("expenditureList");
            for (final String expenditure : expndtrList) {
                if (!StringUtils.isEmpty(query.toString()))
                    query.append(" UNION ");
                query.append(getQueryByExpndType(expenditure, whereQuery.toString()));

            }
        } else
            query.append(getQueryByExpndType(exptype, whereQuery.toString()));

        return query.toString();
    }

    protected String getQueryByExpndType(final String expndType, final String whereQuery) {

    	netAccountCodeValue();
        final List<String> listOfNetPayGlIds = netAccountCode.get(expndType);
        final StringBuffer netPayCodes = new StringBuffer(30);
        String voucherQry = "";
        for (final String netCode : listOfNetPayGlIds)
            if (!StringUtils.isEmpty(netPayCodes.toString()))
                netPayCodes.append(",").append(netCode);
            else
                netPayCodes.append(netCode);
        // voucher header condition for complete bill register report
        if (voucherHeader.getVoucherNumber() != null && !StringUtils.isEmpty(voucherHeader.getVoucherNumber()))
            voucherQry = " and vh.vouchernumber like '%" + voucherHeader.getVoucherNumber() + "%'";
        final StringBuffer query = new StringBuffer(500);
        // query to get bills for which vouchers are approved.
        query.append(
                " select b.billnumber ,vh.vouchernumber as vouchernumber, mis.payto,b.passedamount, sum(bd.creditamount) as netpay, s.description,b.billdate as billdate")
                .
                append(" from eg_billregister b, eg_billdetails bd, voucherheader vh,eg_billregistermis mis , egw_status s ")
                .
                append(" where b.id= bd.billid and b.id=mis.billid and mis.voucherheaderid =vh.id  and s.id= b.statusid and bd.creditamount > 0")
                .
                append(voucherQry).
                append("  and bd.glcodeid in(").append(netPayCodes.toString()).append(")").append(" and b.expendituretype='")
                .append(expndType).append("'").
                append("  and vh.status IN (0,5) ").append(whereQuery)
                .append(" group by b.billnumber, vh.vouchernumber,mis.payto, b.passedamount, s.description,b.billdate");

        query.append(" UNION ");

        // query to get bills for which vouchers are Cancelled.
        query.append(
                " select b.billnumber ,'' as vouchernumber, mis.payto,b.passedamount, sum(bd.creditamount) as netpay, s.description,b.billdate as billdate")
                .
                append(" from eg_billregister b, eg_billdetails bd, voucherheader vh,eg_billregistermis mis , egw_status s ")
                .
                append(" where b.id= bd.billid and b.id=mis.billid and mis.voucherheaderid =vh.id  and s.id= b.statusid and bd.creditamount > 0")
                .
                append(voucherQry).
                append("  and bd.glcodeid in(").append(netPayCodes.toString()).append(")").append(" and b.expendituretype='")
                .append(expndType).append("'").
                append("  and vh.status = 4").append(whereQuery)
                .append(" group by b.billnumber,vouchernumber, mis.payto, b.passedamount, s.description,b.billdate");

        if (voucherHeader.getVoucherNumber() == null || StringUtils.isEmpty(voucherHeader.getVoucherNumber())) {
            query.append(" UNION ");

            // query to get bills for voucher is not created
            query.append(
                    " select b.billnumber ,'' as vouchernumber, mis.payto,b.passedamount, sum(bd.creditamount) as netpay, s.description,b.billdate as billdate")
                    .
                    append(" from eg_billregister b, eg_billdetails bd,eg_billregistermis mis , egw_status s ")
                    .
                    append(" where b.id= bd.billid and b.id=mis.billid  and s.id= b.statusid and  mis.voucherheaderid is null and bd.creditamount > 0")
                    .
                    append("  and bd.glcodeid in(").append(netPayCodes.toString()).append(")").append(" and b.expendituretype='")
                    .append(expndType).append("'").
                    append(whereQuery).append(" group by b.billnumber, vouchernumber,mis.payto, b.passedamount, s.description,b.billdate");
        }

        return query.toString();
    }

    protected void loadDropdownData() {
        final Query query = persistenceService.getSession().createQuery("select status.id from EgwStatus status where " +
                "status.description in (:surrenderedList) and status.moduletype='Instrument'");
        query.setParameterList("surrenderedList", chequeStatusCheckList);
        cancelledChequeStatus = query.list();
        getHeaderFields();
        if (headerFields.contains("department"))
            addDropdownData("departmentList", masterDataCache.get("egi-department"));
        if (headerFields.contains("functionary"))
            addDropdownData("functionaryList", masterDataCache.get("egi-functionary"));
        if (headerFields.contains("fund"))
            addDropdownData("fundList", masterDataCache.get("egi-fund"));
        if (headerFields.contains("fundsource"))
            addDropdownData("fundsourceList", masterDataCache.get("egi-fundSource"));
        if (headerFields.contains("field"))
            addDropdownData("fieldList", masterDataCache.get("egi-ward"));
        if (headerFields.contains("scheme"))
            addDropdownData("schemeList", Collections.EMPTY_LIST);
        if (headerFields.contains("subscheme"))
            addDropdownData("subschemeList", Collections.EMPTY_LIST);
        addDropdownData(
                "expenditureList",
                persistenceService
                .findAllBy(" select distinct bill.expendituretype from EgBillregister bill  order by bill.expendituretype"));
        addDropdownData(
                "billTypeList",
                persistenceService
                .findAllBy(" select distinct bill.billtype from EgBillregister bill where  bill.billtype is not null order by bill.billtype"));

    }

    protected void getHeaderFields()
    {
         final List<AppConfigValues> appConfigList =appConfigValueService.getConfigValuesByModuleAndKey("EGF", "DEFAULT_SEARCH_MISATTRRIBUTES");
            for (final AppConfigValues appConfigVal :appConfigList)
            {
                final String value = appConfigVal.getValue();
                final String header = value.substring(0, value.indexOf('|'));
                headerFields.add(header);
                final String mandate = value.substring(value.indexOf('|') + 1);
                if (mandate.equalsIgnoreCase("M"))
                    mandatoryFields.add(header);
            }

    }

    public EgwStatus getStatusId(final String statusString) {
        final String statusQury = "from EgwStatus where upper(moduletype)=upper('instrument') and  upper(description)=upper('"
                + statusString + "')";
        final EgwStatus egwStatus = (EgwStatus) persistenceService.find(statusQury);
        return egwStatus;

    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public String getExptype() {
        return exptype;
    }

    public String getBillType() {
        return billType;
    }

    public void setExptype(final String exptype) {
        this.exptype = exptype;
    }

    public void setBillType(final String billType) {
        this.billType = billType;
    }

    public List<BillRegisterReportBean> getBillRegReportList() {
        return billRegReportList;
    }

    public void setBillRegReportList(final List<BillRegisterReportBean> billRegReportList) {
        this.billRegReportList = billRegReportList;
    }

   

    public List<String> getChequeStatusCheckList() {
        return chequeStatusCheckList;
    }

    public void setChequeStatusCheckList(final List<String> chequeStatusCheckList) {
        this.chequeStatusCheckList = chequeStatusCheckList;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public boolean isCompleteBillRegisterReport() {
        return isCompleteBillRegisterReport;
    }

    public void setCompleteBillRegisterReport(final boolean isCompleteBillRegisterReport) {
        this.isCompleteBillRegisterReport = isCompleteBillRegisterReport;
    }

    public static Map<String, List<String>> getNetAccountCode() {
		return netAccountCode;
	}

	public static void setNetAccountCode(Map<String, List<String>> netAccountCode) {
		BillRegisterReportAction.netAccountCode = netAccountCode;
	}

	public AppConfigValueService getAppConfigValueService() {
		return appConfigValueService;
	}

	public void setAppConfigValueService(AppConfigValueService appConfigValueService) {
		this.appConfigValueService = appConfigValueService;
	}

	
	
	
    /*
     * protected String getQuery(){ StringBuffer query = new StringBuffer();
     * query.append(" from EgBillregister egBill where egBill.egBillregistermis.fund.id="+voucherHeader.getFundId().getId());
     * if(null!=voucherHeader.getVouchermis().getDepartmentid()){
     * query.append(" and egBill.egBillregistermis.egDepartment.id="+voucherHeader.getVouchermis().getDepartmentid().getId()); }
     * if(!StringUtils.isEmpty(exptype) ){ query.append(" and egBill.expendituretype='"+exptype+"'"); }
     * if(!StringUtils.isEmpty(billType)){ query.append(" and egBill.billtype='"+billType+"'"); } if(null!=fromDate){
     * query.append(" and egBill.billdate >= to_date('"+DDMMYYYYFORMATS.format(fromDate)+"','dd/MM/yyyy')"); } if(null!=toDate){
     * query.append(" and egBill.billdate <= to_date('"+DDMMYYYYFORMATS.format(toDate)+"','dd/MM/yyyy')"); } return
     * query.toString(); }
     */

    /*
     * @SuppressWarnings("unchecked") protected void formatSearchResult() throws Exception{ if(LOGGER.isDebugEnabled())
     * LOGGER.debug("BillRegisterReportAction | formatSearchResult | start"); billRegReportList = new
     * ArrayList<BillRegisterReportBean>(); EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
     * List<EgBillregister> list = egovPaginatedList.getList(); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("BillRegisterReportAction | formatSearchResult | list size : "+list.size() ); for(EgBillregister billRegister
     * : list) { try { BigDecimal netAmount = getNetAmount(billRegister); if(null != netAmount ){ // for a bill if wrong net
     * payable code is used , then filter out that bill. BillRegisterReportBean billRegReport = new BillRegisterReportBean();
     * billRegReport.setBillNumber(billRegister.getBillnumber());
     * billRegReport.setGrossAmount(billRegister.getPassedamount().setScale(2));
     * billRegReport.setPartyName(null!=billRegister.getEgBillregistermis()?billRegister.getEgBillregistermis().getPayto():"");
     * billRegReport.setNetAmount(netAmount);
     * billRegReport.setDeductionAmount(billRegReport.getGrossAmount().subtract(billRegReport.getNetAmount()));
     * if(null!=billRegister.getEgBillregistermis().getVoucherHeader() && billRegister.getEgBillregistermis().getVoucherHeader().
     * getStatus().equals(Integer.valueOf(FinancialConstants.CREATEDVOUCHERSTATUS))){
     * billRegReport.setVoucherNumber(billRegister.getEgBillregistermis().getVoucherHeader().getVoucherNumber());
     * List<Miscbilldetail> miscBillList
     * =(List<Miscbilldetail>)persistenceService.findAllBy(" from Miscbilldetail where billnumber=?",
     * billRegister.getBillnumber()); if(null!= miscBillList && miscBillList.size()>0){ StringBuffer paidAmount=new
     * StringBuffer(""); StringBuffer payMentVoucherNumber=new StringBuffer(""); for (Miscbilldetail miscbilldetail :
     * miscBillList) { if(null != miscbilldetail.getPayVoucherHeader() && miscbilldetail.getPayVoucherHeader().getStatus().
     * equals(Integer.valueOf(FinancialConstants.CREATEDVOUCHERSTATUS))){ if(!StringUtils.isEmpty(paidAmount.toString())){
     * paidAmount.append("|"); payMentVoucherNumber.append("|"); } paidAmount.append(miscbilldetail.getPaidamount());
     * payMentVoucherNumber.append(miscbilldetail.getPayVoucherHeader().getVoucherNumber()); } }
     * billRegReport.setPaidAmount(paidAmount.toString()); billRegReport.setPaymentVoucherNumber(payMentVoucherNumber.toString());
     * } } billRegReport.setStatus(billRegister.getStatus().getDescription()); billRegReportList.add(billRegReport); } } catch
     * (Exception e) { if(LOGGER.isDebugEnabled()) LOGGER.debug( "Failed while processing bill number :"+
     * billRegister.getBillnumber()); throw e; } } egovPaginatedList.setList(billRegReportList); }
     */

    /**
     * @description - Return the net payable amount for the bill, return null if the net payable code is wrong.
     * @param billRegister
     * @return
     */
    /*
     * protected BigDecimal getNetAmount(EgBillregister billRegister){ try { Set<EgBilldetails> billDetails =
     * billRegister.getEgBilldetailes(); List<String> listOfNetPayGlIds = netAccountCode.get(billRegister.getExpendituretype());
     * for (EgBilldetails egBilldetails : billDetails) { if(null != egBilldetails.getCreditamount() &&
     * egBilldetails.getCreditamount().compareTo(BigDecimal.ZERO)!=0 ){
     * if(listOfNetPayGlIds.contains(egBilldetails.getGlcodeid().toString())) return egBilldetails.getCreditamount().setScale(2);
     * else continue; } } } catch (Exception e) {
     * LOGGER.error("Expecetion Occured while getting Net Amount for bill :"+billRegister.getBillnumber() , e); } return null; }
     */

}