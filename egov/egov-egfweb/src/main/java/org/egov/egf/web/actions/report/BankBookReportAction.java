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
package org.egov.egf.web.actions.report;

import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.model.BankBookEntry;
import org.egov.egf.model.BankBookViewEntry;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
        @Result(name = "results", location = "bankBookReport-results.jsp"),
        @Result(name = "chequeDetails", location = "bankBookReport-chequeDetails.jsp"),
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=BankBookReport.pdf" }),
        @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/xls", "contentDisposition", "no-cache;filename=BankBookReport.xls" })
})
public class BankBookReportAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = -4641317233825371935L;
    private static final String EMPTY_STRING = "";
    private static final String PAYMENT = "Payment";
    private static final String RECEIPT = "Receipt";
    private static final String SURRENDERED = "Surrendered";
    private static final Logger LOGGER = Logger.getLogger(BankBookReportAction.class);
    String jasperpath = "/reports/templates/bankBookReport.jasper";
    List<Paymentheader> paymentHeaderList = new ArrayList<Paymentheader>();
    private List<BankBookEntry> bankBookEntries = new ArrayList<BankBookEntry>();
    private List<BankBookViewEntry> bankBookViewEntries = new ArrayList<BankBookViewEntry>();
    private Date startDate = new Date();
    private Date endDate = new Date();
    private BigDecimal bankBalance = BigDecimal.ZERO;
    private Bankaccount bankAccount;
    private InputStream inputStream;
    ReportHelper reportHelper;
    private EgovCommon egovCommon;
    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> mandatoryFields = new ArrayList<String>();
    private Fund fundId = new Fund();
    private CFunction function = new CFunction();
    private Vouchermis vouchermis = new Vouchermis();
    private Long voucherId;
    private List<InstrumentHeader> chequeDetails = new ArrayList<InstrumentHeader>();
    private String chequeStatus = EMPTY_STRING;
    private String voucherStr = "";
    private StringBuffer header = new StringBuffer();
    private Date todayDate;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private AppConfigValueService appConfigValuesService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final List<String> voucherNo = new ArrayList<String>();
    private boolean isCreditOpeningBalance = false;
    private String queryFrom = "";
    private String getInstrumentsByVoucherIdsQuery = "";
    private Map<Long, List<Object[]>> voucherIdAndInstrumentMap = new HashMap<Long, List<Object[]>>();
    private Map<Long, List<Object[]>> InstrumentHeaderIdsAndInstrumentVouchersMap = new HashMap<Long, List<Object[]>>();

    @Autowired
    private FinancialYearDAO financialYearDAO;

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    @Override
    public String execute() throws Exception {
        finYearDate();
        return "form";
    }

    public void finYearDate() {

        final String financialYearId = financialYearDAO.getCurrYearFiscalId();
        if (financialYearId == null || financialYearId.equals(""))
            startDate = new Date();
        else
            startDate = (Date) persistenceService.find("select startingDate  from CFinancialYear where id=?",
                    Long.parseLong(financialYearId));
        endDate = null;

    }

    public BankBookReportAction() {
        addRelatedEntity("vouchermis.departmentid", Department.class);
        addRelatedEntity("vouchermis.fundId", Fund.class);
        addRelatedEntity("vouchermis.schemeid", Scheme.class);
        addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
        addRelatedEntity("vouchermis.functionary", Functionary.class);
        addRelatedEntity("vouchermis.fundsource", Fundsource.class);
        addRelatedEntity("vouchermis.divisionid", Boundary.class);
    }

    @Override
    public void prepare() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        if (!parameters.containsKey("skipPrepare")) {
            addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
            addDropdownData("accNumList", Collections.EMPTY_LIST);

            getHeaderFields();
            if (headerFields.contains(Constants.DEPARTMENT))
                addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
            if (headerFields.contains(Constants.FUNCTION))
                addDropdownData("functionList",
                        persistenceService.findAllBy("from CFunction where isactive=true and isnotleaf=false  order by name"));
            if (headerFields.contains(Constants.FUNCTIONARY))
                addDropdownData("functionaryList",
                        persistenceService.findAllBy(" from Functionary where isactive=true order by name"));
            if (headerFields.contains(Constants.FUND))
                addDropdownData("fundList",
                        persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
            if (headerFields.contains(Constants.FUNDSOURCE))
                addDropdownData("fundsourceList",
                        persistenceService.findAllBy(" from Fundsource where isactive=true order by name"));
            if (headerFields.contains(Constants.FIELD))
                addDropdownData("fieldList",
                        persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
            if (headerFields.contains(Constants.SCHEME))
                addDropdownData("schemeList", Collections.EMPTY_LIST);
            if (headerFields.contains(Constants.SUBSCHEME))
                addDropdownData("subschemeList", Collections.EMPTY_LIST);
        }
    }

    protected void getHeaderFields() {
        final List<AppConfigValues> appConfigList =appConfigValuesService.getConfigValuesByModuleAndKey("EGF", "REPORT_SEARCH_MISATTRRIBUTES");
            for (final AppConfigValues appConfigVal : appConfigList) {

                final String value = appConfigVal.getValue();
                final String header = value.substring(0, value.indexOf('|'));
                headerFields.add(header);
                final String mandate = value.substring(value.indexOf('|') + 1);
                if (mandate.equalsIgnoreCase("M"))
                    mandatoryFields.add(header);
            }
    }

    public boolean shouldShowHeaderField(final String fieldName) {
        return headerFields.contains(fieldName);
    }

    @ReadOnly
    @Action(value = "/report/bankBookReport-ajaxLoadBankBook")
    public String ajaxLoadBankBook() {
        if (parameters.containsKey("bankAccount.id") && parameters.get("bankAccount.id")[0] != null) {
            startDate = parseDate("startDate");
            endDate = parseDate("endDate");

            CFinancialYear financialYear = financialYearDAO.getFinYearByDate(startDate);
            Date endingDate = financialYear.getEndingDate();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String endFormat = formatter.format(endDate);
            String endFormat1 = formatter.format(endingDate);
            if (endFormat.compareTo(endFormat1) > 0)
            {
                addActionError(getText("End date should be within a financial year"));
                return "results";
            }
            setTodayDate(new Date());
            bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                    Long.valueOf(parameters.get("bankAccount.id")[0]));
            final List<BankBookEntry> results = getResults(bankAccount.getChartofaccounts().getGlcode());
            final Map<String, BankBookEntry> voucherNumberAndEntryMap = new HashMap<String, BankBookEntry>();
            final List<String> multipleChequeVoucherNumber = new ArrayList<String>();
            final List<BankBookEntry> rowsToBeRemoved = new ArrayList<BankBookEntry>();
            for (final BankBookEntry row : results) {
                if (row.getType().equalsIgnoreCase(RECEIPT))
                    row.setType(RECEIPT);
                else
                    row.setType(PAYMENT);
                boolean shouldAddRow = true;
                if (voucherNumberAndEntryMap.containsKey(row.getVoucherNumber())) {
                    if (SURRENDERED.equalsIgnoreCase(row.getInstrumentStatus())
                            || FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS.equalsIgnoreCase(row
                                    .getInstrumentStatus()))
                        shouldAddRow = false;
                    else {
                        final BankBookEntry entryInMap = voucherNumberAndEntryMap.get(row.getVoucherNumber());
                        if ((SURRENDERED.equalsIgnoreCase(entryInMap.getInstrumentStatus()) || FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS
                                .equalsIgnoreCase(entryInMap.getInstrumentStatus()))
                                && (!SURRENDERED.equalsIgnoreCase(row.getInstrumentStatus()) || !FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS
                                        .equalsIgnoreCase(row.getInstrumentStatus()))) {
                            rowsToBeRemoved.add(entryInMap);
                            voucherNumberAndEntryMap.put(row.getVoucherNumber(), row);
                        } else if (row.getVoucherDate().compareTo(entryInMap.getVoucherDate()) == 0
                                && row.getParticulars().equalsIgnoreCase(entryInMap.getParticulars())
                                && row.getAmount().equals(entryInMap.getAmount())
                                && !SURRENDERED.equalsIgnoreCase(entryInMap.getInstrumentStatus())) {
                            multipleChequeVoucherNumber.add(row.getVoucherNumber());
                            shouldAddRow = false;
                        }
                        else
                            shouldAddRow = true;
                    }
                } else
                    voucherNumberAndEntryMap.put(row.getVoucherNumber(), row);
                if (shouldAddRow)
                    bankBookEntries.add(row);
            }
            if (!bankBookEntries.isEmpty()) {
                computeTotals(bankAccount.getChartofaccounts().getGlcode(), bankAccount.getFund().getCode(),
                        multipleChequeVoucherNumber, rowsToBeRemoved);
                prepareViewObject();
            }
        }
        return "results";
    }

    private void prepareViewObject() {
        for (final BankBookEntry row : bankBookEntries) {
            BankBookViewEntry bankBookViewEntry = new BankBookViewEntry();
            if ("Total".equalsIgnoreCase(row.getParticulars())) {
                bankBookViewEntry.setReceiptAmount(row.getReceiptAmount());
                bankBookViewEntry.setReceiptParticulars(row.getParticulars());
                bankBookViewEntry.setPaymentAmount(row.getReceiptAmount());
                bankBookViewEntry.setPaymentParticulars(row.getParticulars());
            } else if ("To Opening Balance".equalsIgnoreCase(row.getParticulars())) {
                final BigDecimal amt = row.getAmount();
                if (amt.longValue() < 0)
                {
                    bankBookViewEntry.setPaymentAmount(amt.abs());
                    bankBookViewEntry.setPaymentParticulars(row.getParticulars());
                }
                else
                {
                    bankBookViewEntry.setReceiptAmount(amt.abs());
                    bankBookViewEntry.setReceiptParticulars(row.getParticulars());
                }
            } else if ("Closing:By Balance c/d".equalsIgnoreCase(row.getParticulars())) {
                final BigDecimal amt = row.getAmount();
                if (amt.longValue() < 0)
                {
                    bankBookViewEntry.setReceiptAmount(amt.abs());
                    bankBookViewEntry.setReceiptParticulars(row.getParticulars());
                }
                else
                {
                    bankBookViewEntry.setPaymentAmount(amt.abs());
                    bankBookViewEntry.setPaymentParticulars(row.getParticulars());
                }

            } else {
                final String voucherDate = row.getVoucherDate() == null ? "" : Constants.DDMMYYYYFORMAT2.format(row
                        .getVoucherDate());
                if (row.getType().equalsIgnoreCase(RECEIPT)) {
                    bankBookViewEntry = new BankBookViewEntry(row.getVoucherNumber(), voucherDate, row.getParticulars(),
                            row.getAmount(), row.getChequeDetail(), RECEIPT, row.getChequeNumber());
                    bankBookViewEntry.setVoucherId(row.getVoucherId().longValue());
                } else {
                    bankBookViewEntry = new BankBookViewEntry(row.getVoucherNumber(), voucherDate, row.getParticulars(),
                            row.getAmount(), row.getChequeDetail(), PAYMENT, row.getChequeNumber());
                    bankBookViewEntry.setVoucherId(row.getVoucherId().longValue());
                }
            }
            bankBookViewEntries.add(bankBookViewEntry);
        }
    }

    private void computeTotals(final String glCode, final String fundCode, final List<String> multipleChequeVoucherNumber,
            final List<BankBookEntry> rowsToBeRemoved) {
        final List<BankBookEntry> entries = new ArrayList<BankBookEntry>();
        getInstrumentsByVoucherIds();
        getInstrumentVouchersByInstrumentHeaderIds();
        Integer deptId = null;
        if (getVouchermis() != null && getVouchermis().getDepartmentid() != null
                && getVouchermis().getDepartmentid().getId() != null && getVouchermis().getDepartmentid().getId() != -1)
            deptId = getVouchermis().getDepartmentid().getId().intValue();
        final BankBookEntry initialOpeningBalance = getInitialAccountBalance(glCode, fundCode, deptId);
        entries.add(initialOpeningBalance);
        Date date = bankBookEntries.get(0).getVoucherDate();
        String voucherNumber = EMPTY_STRING;
        String chequeNumber = "";
        BigDecimal receiptTotal = BigDecimal.ZERO;
        BigDecimal paymentTotal = BigDecimal.ZERO;
        BigDecimal initialBalance = initialOpeningBalance.getAmount();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside computeTotals()");
        for (final BankBookEntry bankBookEntry : bankBookEntries) {
            if (initialBalance.longValue() < 0)
                isCreditOpeningBalance = true;
            if (!rowsToBeRemoved.contains(bankBookEntry)) {// for a voucher there could be multiple surrendered cheques associated
                // with it. remove the dupicate rows
                if (bankBookEntry.voucherDate.compareTo(date) != 0) {
                    date = bankBookEntry.getVoucherDate();
                    final BigDecimal closingBalance = initialBalance.add(receiptTotal).subtract(paymentTotal);
                    if (closingBalance.longValue() < 0)
                    {
                        entries.add(new BankBookEntry("Closing:By Balance c/d", closingBalance, PAYMENT, BigDecimal.ZERO,
                                BigDecimal.ZERO));
                        if (isCreditOpeningBalance)
                            entries.add(new BankBookEntry("Total", BigDecimal.ZERO, RECEIPT, closingBalance.abs().add(
                                    receiptTotal), initialBalance.abs().add(paymentTotal)));
                        else
                            entries.add(new BankBookEntry("Total", BigDecimal.ZERO, RECEIPT, initialBalance.abs()
                                    .add(receiptTotal).add(closingBalance.abs()), paymentTotal));
                        entries.add(new BankBookEntry("To Opening Balance", closingBalance, RECEIPT, BigDecimal.ZERO,
                                BigDecimal.ZERO));
                    }
                    else
                    {
                        entries.add(new BankBookEntry("Closing:By Balance c/d", closingBalance, RECEIPT, BigDecimal.ZERO,
                                BigDecimal.ZERO));
                        if (isCreditOpeningBalance)
                            entries.add(new BankBookEntry("Total", BigDecimal.ZERO, RECEIPT, receiptTotal, closingBalance.abs()
                                    .add(paymentTotal).add(initialBalance.abs())));
                        else
                            entries.add(new BankBookEntry("Total", BigDecimal.ZERO, RECEIPT, initialBalance.abs().add(
                                    receiptTotal), closingBalance.abs().add(paymentTotal)));
                        entries.add(new BankBookEntry("To Opening Balance", closingBalance, PAYMENT, BigDecimal.ZERO,
                                BigDecimal.ZERO));
                    }

                    receiptTotal = BigDecimal.ZERO;
                    paymentTotal = BigDecimal.ZERO;
                    initialBalance = closingBalance;
                    isCreditOpeningBalance = false;
                }
                if (RECEIPT.equalsIgnoreCase(bankBookEntry.getType())
                        && !voucherNumber.equalsIgnoreCase(bankBookEntry.getVoucherNumber()))
                    receiptTotal = receiptTotal.add(bankBookEntry.getAmount());
                else if (!voucherNumber.equalsIgnoreCase(bankBookEntry.getVoucherNumber()))
                    paymentTotal = paymentTotal.add(bankBookEntry.getAmount());
                if (SURRENDERED.equalsIgnoreCase(bankBookEntry.getInstrumentStatus()))
                    bankBookEntry.setChequeDetail(EMPTY_STRING);
                if (multipleChequeVoucherNumber.contains(bankBookEntry.getVoucherNumber())) {
                    bankBookEntry.setChequeDetail("MULTIPLE");// Set the cheque details to MULTIPLE if the voucher has multiple
                    // cheques assigned to it
                    final List<Object[]> chequeDetails = voucherIdAndInstrumentMap.get(bankBookEntry.getVoucherId().longValue());
                    final StringBuffer listofcheque = new StringBuffer(100);
                    String chequeNos = " ";
                    String chequeComp = " ";

                    if (!voucherNo.contains(bankBookEntry.getVoucherNumber())) {
                        for (final Object[] iv : chequeDetails) {
                            chequeNumber = getStringValue(iv[1]);
                            chequeStatus = " ";
                            chequeStatus = getStringValue(iv[2]);
                            if (!(SURRENDERED.equalsIgnoreCase(chequeStatus) || FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS
                                    .equalsIgnoreCase(chequeStatus))) {

                                if (isInstrumentMultiVoucherMapped(getLongValue(iv[3]))) {
                                    final String chqDate = sdf.format(getDateValue(iv[4]));
                                    chequeComp = chequeNumber + " " + chqDate + "-MULTIPLE";
                                }

                                listofcheque.append(getStringValue(iv[1])).append(" ")
                                        .append(getDateValue(iv[4]) != null ? sdf.format(getDateValue(iv[4])) : "");
                                // String chqDate=sdf.format(iv.getInstrumentHeaderId().getInstrumentDate());
                                if (chequeComp.contains("-MULTIPLE"))
                                {
                                    listofcheque.append(" ").append("-MULTIPLE,");
                                    chequeComp = " ";
                                }
                                else
                                    listofcheque.append(" ").append(",");
                            }
                        }
                        chequeNos = listofcheque.toString();
                        if (chequeNos.length() > 1)
                            chequeNos = chequeNos.substring(0, chequeNos.length() - 1);

                        bankBookEntry.setChequeNumber(chequeNos);
                        voucherNumber = bankBookEntry.getVoucherNumber();
                        entries.add(bankBookEntry);
                        voucherNo.add(bankBookEntry.getVoucherNumber());
                    }
                }
                else {
                    voucherStr = " ";
                    List<Object[]> instrumentVoucherList = new ArrayList<Object[]>();
                    instrumentVoucherList = voucherIdAndInstrumentMap.get(bankBookEntry.getVoucherId().longValue());
                    if (instrumentVoucherList != null)
                        for (final Object[] instrumentVoucher : instrumentVoucherList)
                            try {
                                chequeNumber = getStringValue(instrumentVoucher[1]);
                                chequeStatus = " ";
                                chequeStatus = getStringValue(instrumentVoucher[2]);
                                if (!(SURRENDERED.equalsIgnoreCase(chequeStatus) || FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS
                                        .equalsIgnoreCase(chequeStatus)))
                                    if (isInstrumentMultiVoucherMapped(getLongValue(instrumentVoucher[3]))) {
                                        if (chequeNumber != null && !chequeNumber.equalsIgnoreCase("")) {
                                            final String chqDate = getDateValue(instrumentVoucher[4]) != null ? sdf
                                                    .format(getDateValue(instrumentVoucher[4])) : "";
                                            voucherStr = chequeNumber + " " + chqDate + "-MULTIPLE";
                                        }
                                        else {
                                            chequeNumber = getStringValue(instrumentVoucher[5]);
                                            final String chqDate = getDateValue(instrumentVoucher[6]) != null ? sdf
                                                    .format(getDateValue(instrumentVoucher[6])) : "";
                                            voucherStr = chequeNumber + " " + chqDate + "-MULTIPLE";
                                        }
                                    }
                                    else if (chequeNumber != null && !chequeNumber.equalsIgnoreCase("")) {
                                        final String chqDate = getDateValue(instrumentVoucher[4]) != null ? sdf
                                                .format(getDateValue(instrumentVoucher[4])) : "";
                                        voucherStr = chequeNumber + " " + chqDate;
                                    }
                                    else {  // voucherStr=" ";
                                        chequeNumber = getStringValue(instrumentVoucher[5]);
                                        final String chqDate = sdf.format(getDateValue(instrumentVoucher[6]));
                                        voucherStr = chequeNumber + " " + chqDate;
                                        // }
                                    }

                            } catch (final NumberFormatException ex) {
                            }
                    bankBookEntry.setChequeDetail(voucherStr);
                    entries.add(bankBookEntry);
                    voucherNo.add(bankBookEntry.getVoucherNumber());
                }
                voucherNumber = bankBookEntry.getVoucherNumber();
            }

        }
        String vhNum = EMPTY_STRING;
        for (final BankBookEntry bankBookEntry : bankBookEntries)
            if (bankBookEntry.voucherNumber.equalsIgnoreCase(vhNum)) { // this is to handle multiple debits or credits for a
                // single voucher.
                bankBookEntry.setVoucherDate(null);
                bankBookEntry.setAmount(null);
                bankBookEntry.setVoucherNumber(EMPTY_STRING);
            } else
                vhNum = bankBookEntry.getVoucherNumber();
        // adding total,closing and opening balance to the last group
        addTotalsSection(initialBalance, paymentTotal, receiptTotal, entries);
        bankBookEntries = entries;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End of computeTotals()");
    }

    private void getInstrumentsByVoucherIds() {
        String mainQuery = "";
        mainQuery = "SELECT vh2.id,ih2.instrumentnumber,es2.code,ih2.id as instrumentHeaderId ,ih2.instrumentdate, ih2.transactionnumber, ih2.transactiondate";
        getInstrumentsByVoucherIdsQuery = " FROM VOUCHERHEADER vh2,egf_instrumentvoucher iv2 ,egf_instrumentheader ih2 ,egw_status es2 WHERE vh2.id = iv2.voucherheaderid AND iv2.instrumentheaderid=ih2.id"
                +
                " AND ih2.id_status = es2.id AND vh2.id in (select vh.id as vhId" + queryFrom + ")";
        mainQuery = mainQuery + getInstrumentsByVoucherIdsQuery;

        final List<Object[]> objs = persistenceService.getSession().createSQLQuery(mainQuery).list();

        for (final Object[] obj : objs)
            if (voucherIdAndInstrumentMap.containsKey(getLongValue(obj[0])))
                voucherIdAndInstrumentMap.get(getLongValue(obj[0])).add(obj);
            else {
                final List<Object[]> instrumentVouchers = new ArrayList<Object[]>();
                instrumentVouchers.add(obj);
                voucherIdAndInstrumentMap.put(getLongValue(obj[0]), instrumentVouchers);
            }
    }

    private void getInstrumentVouchersByInstrumentHeaderIds() {

        final List<Object[]> objs = persistenceService
                .getSession()
                .createSQLQuery(
                        "SELECT ih.id,vh1.id as voucherHeaderId"
                                +
                                " FROM VOUCHERHEADER vh1,egf_instrumentvoucher iv ,egf_instrumentheader ih,egw_status es1 WHERE vh1.id = iv.voucherheaderid AND iv.instrumentheaderid=ih.id"
                                +
                                " AND ih.id_status = es1.id AND ih.id in (select ih2.id as instrHeaderId "
                                + getInstrumentsByVoucherIdsQuery + ")")
                .list();

        for (final Object[] obj : objs)
            if (InstrumentHeaderIdsAndInstrumentVouchersMap.containsKey(getLongValue(obj[0])))
                InstrumentHeaderIdsAndInstrumentVouchersMap.get(getLongValue(obj[0])).add(obj);
            else {
                final List<Object[]> instrumentVouchers = new ArrayList<Object[]>();
                instrumentVouchers.add(obj);
                InstrumentHeaderIdsAndInstrumentVouchersMap.put(getLongValue(obj[0]), instrumentVouchers);
            }
    }

    private boolean isInstrumentMultiVoucherMapped(final Long instrumentHeaderId) {
        final List<Object[]> instrumentVoucherList = InstrumentHeaderIdsAndInstrumentVouchersMap.get(instrumentHeaderId);
        boolean rep = false;
        if (instrumentVoucherList != null && instrumentVoucherList.size() != 0) {

            final Object[] obj = instrumentVoucherList.get(0);
            final Long voucherId = getLongValue(obj[1]);
            for (final Object[] instrumentVoucher : instrumentVoucherList)
                if (voucherId != getLongValue(instrumentVoucher[1]))
                {
                    rep = true;
                    break;
                }
        }
        return rep;
    }

    private BankBookEntry getInitialAccountBalance(final String glCode, final String fundCode, final Integer deptId) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        final BankBookEntry initialOpeningBalance = new BankBookEntry("To Opening Balance", egovCommon.getAccountBalanceforDate(
                calendar.getTime(), glCode, fundCode, null, null, deptId), RECEIPT, BigDecimal.ZERO, BigDecimal.ZERO);
        return initialOpeningBalance;
    }

    private void addTotalsSection(BigDecimal initialBalance, BigDecimal paymentTotal, BigDecimal receiptTotal,
            final List<BankBookEntry> entries) {
        final BigDecimal closingBalance = initialBalance.add(receiptTotal).subtract(paymentTotal);
        entries.add(new BankBookEntry("Closing:By Balance c/d", closingBalance, PAYMENT, BigDecimal.ZERO, BigDecimal.ZERO));
        // Obtain the total accordingly. Similar to how it is done in computeTotals().
        if (initialBalance.longValue() < 0)
            isCreditOpeningBalance = true;
        if (closingBalance.longValue() < 0)
        {
            if (isCreditOpeningBalance)
                entries.add(new BankBookEntry("Total", BigDecimal.ZERO, RECEIPT, closingBalance.abs().add(receiptTotal),
                        initialBalance.abs().add(paymentTotal)));
            else
                entries.add(new BankBookEntry("Total", BigDecimal.ZERO, RECEIPT, initialBalance.abs().add(receiptTotal)
                        .add(closingBalance.abs()), paymentTotal));
        } else if (isCreditOpeningBalance)
            entries.add(new BankBookEntry("Total", BigDecimal.ZERO, RECEIPT, receiptTotal, closingBalance.abs()
                    .add(paymentTotal).add(initialBalance.abs())));
        else
            entries.add(new BankBookEntry("Total", BigDecimal.ZERO, RECEIPT, initialBalance.abs().add(receiptTotal),
                    closingBalance.abs().add(paymentTotal)));
        isCreditOpeningBalance = false;
        receiptTotal = BigDecimal.ZERO;
        paymentTotal = BigDecimal.ZERO;
        initialBalance = closingBalance;
    }

    private String getAppConfigValueFor(final String module, final String key) {
        try {
            return appConfigValuesService.getConfigValuesByModuleAndKey(module, key).get(0).getValue();
        } catch (final Exception e) {
            throw new ValidationException(EMPTY_STRING, "The key '" + key + "' is not defined in appconfig");
        }
    }

    private List<BankBookEntry> getResults(final String glCode1) {
        final String miscQuery = getMiscQuery();
        String OrderBy = "";
        final String voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        final String query1 = "SELECT distinct vh.id as voucherId,vh.voucherDate AS voucherDate, vh.voucherNumber AS voucherNumber,"
                +
                " gl.glcode||' - '||case when gl.debitAmount  = 0 then (case (gl.creditamount) when 0 then gl.creditAmount||'.00cr' when floor(gl.creditamount) then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end ) else (case (gl.debitamount) when 0 then gl.debitamount||'.00dr' when floor(gl.debitamount)  then gl.debitamount||'.00dr' else  gl.debitamount||'dr' 	 end ) end"
                +
                " AS particulars,case when gl1.debitAmount = 0 then gl1.creditamount else gl1.debitAmount end AS amount, case when gl1.debitAmount = 0 then 'Payment' else 'Receipt' end AS type,"
                +
                " case when (case when ch.instrumentnumber is NULL then ch.transactionnumber else ch.instrumentnumber  ||' , ' ||TO_CHAR(case when ch.instrumentdate is NULL THEN ch.transactiondate else ch.instrumentdate end,'dd/mm/yyyy') end )  is NULL then case when ch.instrumentnumber is NULL then ch.transactionnumber else ch.instrumentnumber end ||' , ' ||TO_CHAR(case when ch.instrumentdate is NULL then ch.transactiondate else ch.instrumentdate end,'dd/mm/yyyy') end"
                +
                " AS chequeDetail,gl.glcode as glCode,ch.description as instrumentStatus  ";
        queryFrom = " FROM generalLedger gl,generalLedger gl1"
                +
                ",vouchermis vmis, VOUCHERHEADER vh left outer join (select iv.voucherheaderid,ih.instrumentnumber,ih.instrumentdate,"
                +
                "es.description,ih.transactionnumber,ih.transactiondate from egf_instrumentheader ih,egw_status es,egf_instrumentvoucher iv where iv.instrumentheaderid=ih.id and "
                +
                "ih.id_status=es.id) ch on ch.voucherheaderid=vh.id  WHERE  gl.voucherHeaderId = vh.id  AND vmis.VOUCHERHEADERID=vh.id  "
                +
                "and gl.voucherheaderid  IN (SELECT voucherheaderid FROM generalledger gl WHERE glcode='" + glCode1
                + "') AND gl.voucherheaderid = gl1.voucherheaderid AND gl.glcode <> '" + glCode1 + "' AND gl1.glcode = '"
                + glCode1 + "' and vh.voucherDate>='" + Constants.DDMMYYYYFORMAT1.format(startDate) + "' " +
                "and vh.voucherDate<='" + Constants.DDMMYYYYFORMAT1.format(endDate) + "' and vh.status not in("
                + voucherStatusToExclude + ") " + miscQuery + " ";
        OrderBy = "order by voucherdate,vouchernumber";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Main query :" + query1 + queryFrom + OrderBy);

        final Query query = persistenceService.getSession().createSQLQuery(query1 + queryFrom + OrderBy)
                .addScalar("voucherId", new BigDecimalType())
                .addScalar("voucherDate")
                .addScalar("voucherNumber")
                .addScalar("particulars")
                .addScalar("amount", new BigDecimalType())
                .addScalar("type")
                .addScalar("chequeDetail")
                .addScalar("glCode")
                .addScalar("instrumentStatus")
                .setResultTransformer(Transformers.aliasToBean(BankBookEntry.class));
        final List<BankBookEntry> results = query.list();
        return results;
    }

    String getMiscQuery() {
        final StringBuffer query = new StringBuffer();

        if (fundId != null && fundId.getId() != null && fundId.getId() != -1) {
            query.append(" and vh.fundId=").append(fundId.getId().toString());
            final Fund fnd = (Fund) persistenceService.find("from Fund where id=?", fundId.getId());
            header.append(" for " + fnd.getName());
        }

        if (getVouchermis() != null && getVouchermis().getDepartmentid() != null
                && getVouchermis().getDepartmentid().getId() != null && getVouchermis().getDepartmentid().getId() != -1) {
            query.append(" and vmis.DEPARTMENTID=").append(getVouchermis().getDepartmentid().getId().toString());
            final Department dept = (Department) persistenceService.find("from Department where id=?", getVouchermis()
                    .getDepartmentid().getId());
            header.append(" in " + dept.getName() + " ");
        }
        if (getVouchermis() != null && getVouchermis().getFunctionary() != null
                && getVouchermis().getFunctionary().getId() != null && getVouchermis().getFunctionary().getId() != -1)
            query.append(" and vmis.FUNCTIONARYID=").append(getVouchermis().getFunctionary().getId().toString());
        if (getVouchermis() != null && getVouchermis().getFundsource() != null && getVouchermis().getFundsource().getId() != null
                && getVouchermis().getFundsource().getId() != -1)
            query.append(" and vmis.FUNDSOURCEID =").append(getVouchermis().getFundsource().getId().toString());
        if (getVouchermis() != null && getVouchermis().getSchemeid() != null && getVouchermis().getSchemeid().getId() != null
                && getVouchermis().getSchemeid().getId() != -1)
            query.append(" and vmis.SCHEMEID =").append(getVouchermis().getSchemeid().getId().toString());
        if (getVouchermis() != null && getVouchermis().getSubschemeid() != null
                && getVouchermis().getSubschemeid().getId() != null && getVouchermis().getSubschemeid().getId() != -1)
            query.append(" and vmis.SUBSCHEMEID =").append(getVouchermis().getSubschemeid().getId().toString());
        if (getVouchermis() != null && getVouchermis().getDivisionid() != null && getVouchermis().getDivisionid().getId() != null
                && getVouchermis().getDivisionid().getId() != -1)
            query.append(" and vmis.DIVISIONID =").append(getVouchermis().getDivisionid().getId().toString());
        /*
         * if (function != null && function.getId() != null && function.getId() != -1) {
         * query.append(" and vmis.FUNCTIONID=").append(function.getId().toString()); final CFunction func = (CFunction)
         * persistenceService.find("from CFunction where id=?", function.getId()); header.append(" in " + func.getName() + " "); }
         */
        if (getVouchermis() != null && getVouchermis().getFunction() != null
                && getVouchermis().getFunction().getId() != null && getVouchermis().getFunction().getId() != -1) {
            query.append(" and vmis.functionid=").append(getVouchermis().getFunction().getId());
            final CFunction func = (CFunction) persistenceService.find("from CFunction where id=?", getVouchermis()
                    .getFunction().getId());
            header.append(" in " + func.getName() + " ");
        }

        return query.toString();
    }

    /*
     * public String getUlbName() { final Query query =
     * persistenceService.getSession().createSQLQuery("select name from companydetail"); final List<String> result = query.list();
     * if (result != null) return result.get(0); return EMPTY_STRING; }
     */

    Date parseDate(final String stringDate) {
        if (parameters.containsKey(stringDate) && parameters.get(stringDate)[0] != null)
            try {
                return Constants.DDMMYYYYFORMAT2.parse(parameters.get(stringDate)[0]);
            } catch (final ParseException e) {
                throw new ValidationException("Invalid date", "Invalid date");
            }
        return new Date();
    }

    public List<Paymentheader> getPaymentHeaderList() {
        return paymentHeaderList;
    }

    private String getStringValue(final Object object) {
        return object != null ? object.toString() : "";
    }

    private Date getDateValue(final Object object) {
        return object != null ? (Date) object : null;
    }

    private Long getLongValue(final Object object) {
        return object != null ? new Long(object.toString()) : 0;
    }

    public BigDecimal getBankBalance() {
        return bankBalance;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT2.format(date);
    }

    public void setBankBalance(final BigDecimal bankBalance) {
        this.bankBalance = bankBalance;
    }

    public void setBankAccount(final Bankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Bankaccount getBankAccount() {
        return bankAccount;
    }

    @Action(value = "/report/bankBookReport-exportPdf")
    public String exportPdf() throws JRException, IOException {
        ajaxLoadBankBook();
        final List<Object> dataSource = new ArrayList<Object>();
        for (final BankBookViewEntry row : bankBookViewEntries)
            dataSource.add(row);
        setInputStream(reportHelper.exportPdf(getInputStream(), jasperpath, getParamMap(), dataSource));
        return "PDF";
    }

    @Action(value = "/report/bankBookReport-exportXls")
    public String exportXls() throws JRException, IOException {
        ajaxLoadBankBook();
        final List<Object> dataSource = new ArrayList<Object>();
        for (final BankBookViewEntry row : bankBookViewEntries)
            dataSource.add(row);
        setInputStream(reportHelper.exportXls(getInputStream(), jasperpath, getParamMap(), dataSource));
        return "XLS";
    }

    Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ulbName", ReportUtil.getCityName());
        final String name = bankAccount.getBankbranch().getBank().getName().concat("-")
                .concat(bankAccount.getBankbranch().getBranchname()).concat("-")
                .concat(bankAccount.getAccountnumber());
        paramMap.put(
                "heading",
                getText("bank.book.heading", new String[] { name, header.toString(), Constants.DDMMYYYYFORMAT2.format(startDate),
                        Constants.DDMMYYYYFORMAT2.format(endDate) }));
        // paramMap.put("today", Constants.DDMMYYYYFORMAT2.format(new Date()));
        return paramMap;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public void setBankBookViewEntries(final List<BankBookViewEntry> bankBookViewEntries) {
        this.bankBookViewEntries = bankBookViewEntries;
    }

    public List<BankBookViewEntry> getBankBookViewEntries() {
        return bankBookViewEntries;
    }

    public void setFundId(final Fund fundId) {
        this.fundId = fundId;
    }

    public Fund getFundId() {
        return fundId;
    }

    public Vouchermis getVouchermis() {
        return vouchermis;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public void setVoucherId(final Long voucherId) {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    @Action(value = "/report/bankBookReport-showChequeDetails")
    public String showChequeDetails() {
        if (voucherId != null)
            chequeDetails = persistenceService.findAllBy(
                    "select iv.instrumentHeaderId from InstrumentVoucher iv where iv.voucherHeaderId.id=?", voucherId);
        return "chequeDetails";
    }

    public void setChequeDetails(final List<InstrumentHeader> chequeDetails) {
        this.chequeDetails = chequeDetails;
    }

    public List<InstrumentHeader> getChequeDetails() {
        return chequeDetails;
    }

    public void setVouchermis(final Vouchermis vouchermis) {
        this.vouchermis = vouchermis;
    }

    public StringBuffer getHeader() {
        return header;
    }

    public void setHeader(final StringBuffer header) {
        this.header = header;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    private void setTodayDate(final Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getQueryFrom() {
        return queryFrom;
    }

    public void setQueryFrom(final String queryFrom) {
        this.queryFrom = queryFrom;
    }

    public String getGetInstrumentsByVoucherIdsQuery() {
        return getInstrumentsByVoucherIdsQuery;
    }

    public void setGetInstrumentsByVoucherIdsQuery(
            final String getInstrumentsByVoucherIdsQuery) {
        this.getInstrumentsByVoucherIdsQuery = getInstrumentsByVoucherIdsQuery;
    }

    public Map<Long, List<Object[]>> getVoucherIdAndInstrumentMap() {
        return voucherIdAndInstrumentMap;
    }

    public void setVoucherIdAndInstrumentMap(
            final Map<Long, List<Object[]>> voucherIdAndInstrumentMap) {
        this.voucherIdAndInstrumentMap = voucherIdAndInstrumentMap;
    }

    public Map<Long, List<Object[]>> getInstrumentHeaderIdsAndInstrumentVouchersMap() {
        return InstrumentHeaderIdsAndInstrumentVouchersMap;
    }

    public void setInstrumentHeaderIdsAndInstrumentVouchersMap(
            final Map<Long, List<Object[]>> instrumentHeaderIdsAndInstrumentVouchersMap) {
        InstrumentHeaderIdsAndInstrumentVouchersMap = instrumentHeaderIdsAndInstrumentVouchersMap;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(
            AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(CFunction function) {
        this.function = function;
    }

    public FinancialYearDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

}