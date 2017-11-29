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

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.egf.model.BankAdviceReportInfo;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@ParentPackage("egov")
@Results({
        @Result(name = BankAdviceReportAction.NEW, location = "bankAdviceReport-" + BankAdviceReportAction.NEW + ".jsp"),
        @Result(name = "downloadText", location = "bankAdviceReport-downloadText.jsp"),
        @Result(name = "reportview", type = "stream", location = "inputStream", params = { "contentType", "${contentType}",
                "contentDisposition", "attachment; filename=${fileName}" }),
        @Result(name = "txtresult", type = "stream", location = "inStream", params = { "contentType", "${contentType}",
                "contentDisposition", "attachment; filename=${textFileName}" })
})
public class BankAdviceReportAction extends BaseFormAction {

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BankAdviceReportAction.class);
    private Bank bank;
    private Bankbranch bankbranch;
    private String bankName;
    private String branchName;
    private String accountNumber;
    private String instrumentNumber;
    private String instrumentDate;
    private Bankaccount bankaccount;
    private InstrumentHeader instrumentnumber;
    private InputStream inputStream;
    private InputStream inStream;
    private Map<Integer, String> monthMap = new LinkedHashMap<Integer, String>();
    public List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();
    private Map<Integer, String> fullNameMonthMap = new TreeMap<Integer, String>();
    private FinancialYearHibernateDAO financialYearDAO;
    private Integer month;
    private Long financialYearId;
    private String mode;
    private String heading;

    public InputStream getInStream() {
        return inStream;
    }

    public void setInStream(final InputStream inStream) {
        this.inStream = inStream;
    }

    private String contentType;
    private String fileName;
    private String textFileName;
    private ReportService reportService;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    String countQuery = null;
    public List<BankAdviceReportInfo> bankAdviseResultList = new ArrayList<BankAdviceReportInfo>();

    @Override
    public void prepare() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        addDropdownData(
                "bankList",
                persistenceService
                        .findAllBy("select distinct b from Bank b , Bankbranch bb , Bankaccount ba WHERE bb.bank=b and ba.bankbranch=bb and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and b.isactive=true order by b.name"));
        if (bankbranch == null)
            addDropdownData("bankBranchList", Collections.EMPTY_LIST);
        else
            addDropdownData(
                    "bankBranchList",
                    persistenceService
                            .findAllBy(
                                    "select distinct bb from Bankbranch bb,Bankaccount ba where bb.bank.id=? and ba.bankbranch=bb and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bb.isactive=true",
                                    bank.getId()));
        if (bankaccount == null)
            addDropdownData("bankAccountList", Collections.EMPTY_LIST);
        else
            addDropdownData("bankAccountList",
                    persistenceService.findAllBy("from Bankaccount where bankbranch.id=? and isactive=true", bankbranch.getId()));
        if (instrumentnumber == null)
            addDropdownData("chequeNumberList", Collections.EMPTY_LIST);
        else {
            List<Object[]> resultList = new ArrayList<Object[]>();
            final List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();
            resultList = getPersistenceService()
                    .findAllBy(
                            ""
                                    +
                                    "SELECT ih.id, ih.instrumentNumber FROM InstrumentHeader ih, InstrumentVoucher iv, Paymentheader ph "
                                    +
                                    "WHERE ih.isPayCheque ='1' AND ih.bankAccountId.id = ? AND ih.statusId.description in ('New')"
                                    +
                                    " AND ih.statusId.moduletype='Instrument' AND iv.instrumentHeaderId = ih.id and ih.bankAccountId is not null "
                                    +
                                    "AND iv.voucherHeaderId     = ph.voucherheader AND ph.bankaccount = ih.bankAccountId AND ph.type = '"
                                    + FinancialConstants.MODEOFPAYMENT_RTGS + "' " +
                                    "GROUP BY ih.instrumentNumber,ih.id", bankaccount.getId());
            for (final Object[] obj : resultList) {
                InstrumentHeader ih = new InstrumentHeader();
                ih = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?", (Long) obj[0]);

                instrumentHeaderList.add(ih);
            }
            addDropdownData("chequeNumberList", instrumentHeaderList);
        }
        fullNameMonthMap = DateUtils.getAllMonthsWithFullNames();
        final List<CFinancialYear> financialYears = financialYearDAO.getAllActiveFinancialYearList();
        addDropdownData("financialYearsList", financialYears);
    }

    @Action(value = "/report/bankAdviceReport-newForm")
    public String newForm() {
        return NEW;
    }

    @Action(value = "/report/bankAdviceReport-tnebnewForm")
    public String tnebnewForm() {
        return "downloadText";
    }

    public List getSubLedgerDetailQueryAndParams(final InstrumentHeader instrumentHeader) {
        final HashMap<Object, Map<Object, BigDecimal>> detailTypeMap = new HashMap<Object, Map<Object, BigDecimal>>();
        HashMap<Object, BigDecimal> detailKeyMap = new HashMap<Object, BigDecimal>();
        Map<Object, BigDecimal> tempMap = new HashMap<Object, BigDecimal>();
        BigDecimal detailKeyAmt = BigDecimal.ZERO;
        // Getting net payable
        final String query = " SELECT gld.detailtypeid, gld.detailkeyid, sum(gld.amount) " +
                " FROM egf_instrumentvoucher ivh, generalledger gl, generalledgerdetail gld " +
                " WHERE ivh.instrumentheaderid = ? AND ivh.voucherheaderid = gl.voucherheaderid " +
                " AND gl.debitamount != 0 AND gl.id = gld.generalledgerid " +
                " group by gld.detailkeyid, gld.detailtypeid ";

        // Get without subledger one
        final String withNoSubledgerQry = " SELECT gld.DETAILTYPEID,gld.DETAILKEYID , sum(gld.amount) FROM   ( (SELECT voucherheaderid "
                +
                "  FROM egf_instrumentvoucher   WHERE instrumentheaderid =?   ) except   (SELECT DISTINCT payvhid  FROM miscbilldetail mb,"
                +
                " voucherheader vh ,    generalledger gl  LEFT JOIN chartofaccountdetail dtl  ON gl.glcodeid    =dtl.glcodeid  "
                +
                " WHERE mb.payvhid  =vh.id "
                +
                " AND vh.id =gl.voucherheaderid  AND dtl.glcodeid IS NOT NULL  AND vh.id  IN (SELECT voucherheaderid FROM egf_instrumentvoucher "
                +
                " WHERE instrumentheaderid =? ))) p ,  miscbilldetail m, generalledger gl, generalledgerdetail gld WHERE p.voucherheaderid=m.payvhid "
                +
                " AND gl.voucherheaderid =m.billvhid AND gl.id=gld.generalledgerid AND gl.debitamount!=0 " +
                " group by gld.detailtypeid ,gld.detailkeyid  ";

        final Query WithNetPayableSubledgerQuery = persistenceService.getSession().createSQLQuery(query);
        WithNetPayableSubledgerQuery.setParameter(0, instrumentHeader.getId());

        // Get without subledger one
        final Query getDebitsideSubledgerQuery = persistenceService.getSession().createSQLQuery(withNoSubledgerQry);
        getDebitsideSubledgerQuery.setParameter(0, instrumentHeader.getId());
        getDebitsideSubledgerQuery.setParameter(1, instrumentHeader.getId());

        final List<Object[]> retList = WithNetPayableSubledgerQuery.list();
        retList.addAll(getDebitsideSubledgerQuery.list());

        for (final Object[] obj : retList)
            if (detailTypeMap.isEmpty()) {
                detailKeyMap = new HashMap<Object, BigDecimal>();
                detailKeyMap.put(obj[1], ((BigDecimal) obj[2]).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                detailTypeMap.put(obj[0], detailKeyMap);
            }
            else {
                tempMap = new HashMap<Object, BigDecimal>();
                detailKeyAmt = BigDecimal.ZERO;
                if (null != detailTypeMap.get(obj[0])) {
                    tempMap = detailTypeMap.get(obj[0]);
                    // detailKey=tempMap.get((Integer)obj[1]);
                    if (null != tempMap && tempMap.containsKey(obj[1])) {
                        detailKeyAmt = tempMap.get(obj[1]).add(
                                (((BigDecimal) obj[2]).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                        tempMap.put(obj[1], detailKeyAmt);
                    } else
                        tempMap.put(obj[1], (((BigDecimal) obj[2]).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                } else {
                    detailKeyMap = new HashMap<Object, BigDecimal>();
                    detailKeyMap.put(obj[1], (((BigDecimal) obj[2]).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                    detailTypeMap.put(obj[0], detailKeyMap);
                }
            }

        return retList;
    }

    @ValidationErrorPage(NEW)
    @Action(value = "/report/bankAdviceReport-search")
    public String search() {

        if (instrumentnumber.getId() == -1) {
            addFieldError("searchCriteria", "Please select all search criteria");
            return NEW;
        }
        bankAdviseResultList = getBankAdviceReportList();
        return NEW;
    }

    @SkipValidation
    public String TNEBsearch() {

        final String query = "select distinct ih " +
                "from EBDetails d, EgBillregister br, Miscbilldetail mbd, " +
                "InstrumentVoucher iv inner join iv.instrumentHeaderId ih " +
                "where d.receiptNo is null " +
                "and d.egBillregister = br " +
                "and br.billnumber = mbd.billnumber " +
                "and mbd.payVoucherHeader = iv.voucherHeaderId " +
                "and ih.statusId.code in ('"
                + FinancialConstants.INSTRUMENT_CREATED_STATUS + "', '"
                + FinancialConstants.INSTRUMENT_RECONCILED_STATUS + "') " +
                "and month(ih.transactionDate) = ? " +
                "and year(ih.transactionDate) between  ? and ?";

        final CFinancialYear financialYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id = ?",
                financialYearId);

        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(financialYear.getStartingDate());
        final Integer startingYear = calendar.get(Calendar.YEAR);

        calendar.setTime(financialYear.getEndingDate());
        final Integer endingYear = calendar.get(Calendar.YEAR);

        instrumentHeaderList = persistenceService.findAllBy(query, month, startingYear, endingYear);
        mode = "search";
        monthMap = DateUtils.getAllMonths();
        heading = "List of RTGS Bank advice generated for ";
        heading = heading + monthMap.get(month) + "-" + financialYear.getFinYearRange();
        return "downloadText";
    }

    @ReadOnly
    private List populateSubLedgerDetails(final List subList) {

        final List<Object[]> retList = subList;
        final List<BankAdviceReportInfo> subLedgerList = new ArrayList<BankAdviceReportInfo>();

        for (final Object[] obj : retList) {
            final Accountdetailtype adt = (Accountdetailtype) persistenceService.find("from Accountdetailtype where id=?",
                    ((BigInteger) obj[0]).intValue());

            EntityType subDetail = null;
            try
            {
                final Class aClass = Class.forName(adt.getFullQualifiedName());
                final java.lang.reflect.Method method = aClass.getMethod("getId");
                final String dataType = method.getReturnType().getSimpleName();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("data Type = " + dataType);
                if (dataType.equals("Long"))
                    subDetail = (EntityType) persistenceService.find("from " + adt.getFullQualifiedName() + " where id=?",
                            ((BigInteger) obj[1]).longValue());
                else
                    subDetail = (EntityType) persistenceService.find("from " + adt.getFullQualifiedName() + " where id=?",
                            ((BigInteger) obj[1]).intValue());

            } catch (final ClassCastException e) {
                LOGGER.error(e);
            } catch (final Exception e)
            {
                LOGGER.error("Exception to get EntityType=" + e.getMessage());
            }
            final BankAdviceReportInfo bankAdviceReportInfo = new BankAdviceReportInfo();
            bankAdviceReportInfo.setPartyName(subDetail.getName().toUpperCase());
            bankAdviceReportInfo.setAccountNumber(subDetail.getBankaccount());
            bankAdviceReportInfo.setBank(subDetail.getBankname());
            // bankAdviceReportInfo.setBankBranch(subDetail.getBankaccount());
            bankAdviceReportInfo.setIfscCode(subDetail.getIfsccode());
            bankAdviceReportInfo.setAmount(((BigDecimal) obj[2]).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            totalAmount = totalAmount.add(bankAdviceReportInfo.getAmount());
            subLedgerList.add(bankAdviceReportInfo);
        }

        return subLedgerList;
    }

    /*
     * @ValidationErrorPage(NEW)
     * @Action(value = "/report/bankAdviceReport-exportExcel") public String exportExcel() { final Map<String, Object>
     * reportParams = new HashMap<String, Object>(); final StringBuffer letterContext = new StringBuffer(); letterContext
     * .append("             I request you to transfer the amount indicated below through RTGS duly debiting from the")
     * .append("  Current Account No: ") .append(getBankAccountNumber(bankaccount.getId()) != null ?
     * getBankAccountNumber(bankaccount.getId()) : "") .append("  under your bank to the following bank accounts:");
     * reportParams.put("bankName", getBankName(bank.getId())); reportParams.put("letterContext", letterContext.toString());
     * reportParams.put("branchName", getBankBranchName(bankbranch.getId())); reportParams.put("accountNumber",
     * getBankAccountNumber(bankaccount.getId())); reportParams.put("chequeNumber", "RTGS Ref. No: " +
     * getInstrumentNumber(instrumentnumber.getId())); reportParams.put("chequeDate",
     * getInstrumentDate(instrumentnumber.getId())); final List<BankAdviceReportInfo> subLedgerList = getBankAdviceReportList();
     * final ReportRequest reportInput = new ReportRequest("bankAdviceReport", subLedgerList, reportParams);
     * reportInput.setReportFormat(FileFormat.XLS); contentType = ReportViewerUtil.getContentType(FileFormat.XLS); fileName =
     * "BankAdviceReport." + FileFormat.XLS.toString().toLowerCase(); final ReportOutput reportOutput =
     * reportService.createReport(reportInput); if (reportOutput != null && reportOutput.getReportOutputData() != null)
     * inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); return "reportview"; }
     */

    @ValidationErrorPage(NEW)
    @Action(value = "/report/bankAdviceReport-exportExcel")
    public String exportExcel() {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("bankName", getBankName(bank.getId()));
        reportParams.put("branchName", getBankBranchName(bankbranch.getId()));
        reportParams.put("accountNumber", getBankAccountNumber(bankaccount.getId()));
        final List<BankAdviceReportInfo> subLedgerList = getBankAdviceReportList();
        final ReportRequest reportInput = new ReportRequest("bankAdviceExcelReport", subLedgerList, reportParams);
        reportInput.setReportFormat(ReportFormat.XLS);
        contentType = ReportViewerUtil.getContentType(ReportFormat.XLS);
        fileName = "BankAdviceReport." + ReportFormat.XLS.toString().toLowerCase();
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

        return "reportview";
    }

    @ValidationErrorPage(NEW)
    @Action(value = "/report/bankAdviceReport-exportHtml")
    public String exportHtml() {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final StringBuffer letterContext = new StringBuffer();
        letterContext
                .append("             I request you to transfer the amount indicated below through RTGS duly debiting from the")
                .append("  Current Account No: ")
                .append(getBankAccountNumber(bankaccount.getId()) != null ? getBankAccountNumber(bankaccount.getId()) : "")
                .append("  under your bank to the following bank accounts:");
        reportParams.put("bankName", getBankName(bank.getId()));
        reportParams.put("letterContext", letterContext.toString());
        reportParams.put("branchName", getBankBranchName(bankbranch.getId()));
        reportParams.put("accountNumber", getBankAccountNumber(bankaccount.getId()));
        reportParams.put("chequeNumber", "RTGS Ref. No: " + getInstrumentNumber(instrumentnumber.getId()));
        reportParams.put("chequeDate", getInstrumentDate(instrumentnumber.getId()));
        final List<BankAdviceReportInfo> subLedgerList = getBankAdviceReportList();
        final ReportRequest reportInput = new ReportRequest("bankAdviceReport", subLedgerList, reportParams);
        reportInput.setReportFormat(ReportFormat.HTM);
        contentType = ReportViewerUtil.getContentType(ReportFormat.HTM);
        fileName = "BankAdviceReport." + ReportFormat.HTM.toString().toLowerCase();
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

        return "reportview";
    }

    private String getBankName(final Integer bankId) {
        final Bank bank = (Bank) persistenceService.find("from Bank where id=?", bankId);
        return bank.getName();
    }

    private String getBankBranchName(final Integer bankBranchId) {
        final Bankbranch bankBranch = (Bankbranch) persistenceService.find("from Bankbranch where id=?", bankBranchId);
        return bankBranch.getBranchname();
    }

    private String getBankAccountNumber(final Long bankAccountId) {
        final Bankaccount bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?", bankAccountId);
        return bankAccount.getAccountnumber();
    }

    private String getInstrumentNumber(final Long instrumentHeaderId) {
        final InstrumentHeader instrumentHeader = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?",
                instrumentHeaderId);
        return instrumentHeader.getTransactionNumber();
    }

    private String getInstrumentDate(final Long instrumentHeaderId) {
        final InstrumentHeader instrumentHeader = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?",
                instrumentHeaderId);
        return Constants.DDMMYYYYFORMAT2.format(instrumentHeader.getTransactionDate());
    }

    @ValidationErrorPage(NEW)
    @Action(value = "/report/bankAdviceReport-exportPDF")
    public String exportPDF() {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final StringBuffer letterContext = new StringBuffer();
        letterContext
                .append("             I request you to transfer the amount indicated below through RTGS duly debiting from the")
                .append("  Current Account No: ")
                .append(getBankAccountNumber(bankaccount.getId()) != null ? getBankAccountNumber(bankaccount.getId()) : " ")
                .append("  under your bank to the following bank accounts:");
        reportParams.put("bankName", getBankName(bank.getId()));
        reportParams.put("branchName", getBankBranchName(bankbranch.getId()));
        reportParams.put("letterContext", letterContext.toString());
        reportParams.put("accountNumber", getBankAccountNumber(bankaccount.getId()));
        reportParams.put("chequeNumber", "RTGS Ref. No: " + getInstrumentNumber(instrumentnumber.getId()));
        reportParams.put("chequeDate", getInstrumentDate(instrumentnumber.getId()));
        final List<BankAdviceReportInfo> subLedgerList = getBankAdviceReportList();
        reportParams.put("totalAmount", totalAmount);
        final ReportRequest reportInput = new ReportRequest("bankAdviceReport", subLedgerList, reportParams);
        reportInput.setReportFormat(ReportFormat.PDF);
        contentType = ReportViewerUtil.getContentType(ReportFormat.PDF);
        fileName = "BankAdviceReport." + ReportFormat.PDF.toString().toLowerCase();
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

        return "reportview";
    }

    /*
     * @ValidationErrorPage(NEW)
     * @Action(value="/report/bankAdviceReport-exportText") public String exportText() { try { String
     * rtgsNumber=getInstrumentNumber(instrumentnumber.getId()); String rtgsDate=getInstrumentDate(instrumentnumber.getId());
     * rtgsDate = rtgsDate.replace("/",""); textFileName =rtgsNumber+".txt"; FileOutputStream fos=new FileOutputStream
     * (textFileName,false); List<BankAdviceReportInfo> subLedgerList =(List<BankAdviceReportInfo>) getBankAdviceReportList();
     * StringBuffer fb; monthMap = DateUtils.getAllMonths(); this.contentType = ReportViewerUtil.getContentType(FileFormat.TXT);
     * for (BankAdviceReportInfo bankAdviceReportInfo :subLedgerList ) { fb=new StringBuffer(300); if(rtgsNumber!=null &&
     * rtgsDate!=null) { EBDetails ebDetails = (EBDetails) persistenceService.find(
     * "select ebd from EBDetails ebd,InstrumentHeader ih ,InstrumentVoucher iv ,Miscbilldetail miscBill where ih.id=? and ih.id = iv.instrumentHeaderId.id and iv.voucherHeaderId.id = miscBill.payVoucherHeader.id and miscBill.billnumber = ebd.egBillregister.billnumber and ebd.ebConsumer.name = ? "
     * ,instrumentnumber.getId(),bankAdviceReportInfo.getPartyName()); fb.append(rtgsNumber) .append(seperatorForIOB)
     * .append(rtgsDate) .append(seperatorForIOB) .append(bankAdviceReportInfo.getPartyName()) .append(seperatorForIOB)
     * .append(monthMap.get(ebDetails.getMonth())+" "+EBUtils.getYear(ebDetails.getDueDate())) .append(seperatorForIOB)
     * .append(bankAdviceReportInfo.getAmount()) .append("\n"); fos.write (fb.toString().getBytes()); } } fos.flush();
     * fos.close(); inStream=new FileInputStream(textFileName); }catch (FileNotFoundException e) {  }catch
     * (IOException ioe){  } return "txtresult"; }
     */

    private List getBankAdviceReportList() {

        final List retList = getSubLedgerDetailQueryAndParams(instrumentnumber);
        if (retList != null && !retList.isEmpty())
            return populateSubLedgerDetails(retList);
        else
            return Collections.EMPTY_LIST;
    }

    @Override
    public Object getModel() {

        return null;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(final Bank bank) {
        this.bank = bank;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public Bankbranch getBankbranch() {
        return bankbranch;
    }

    public void setBankbranch(final Bankbranch bankbranch) {
        this.bankbranch = bankbranch;
    }

    public Bankaccount getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(final Bankaccount bankaccount) {
        this.bankaccount = bankaccount;
    }

    public InstrumentHeader getInstrumentnumber() {
        return instrumentnumber;
    }

    public void setInstrumentnumber(final InstrumentHeader instrumentnumber) {
        this.instrumentnumber = instrumentnumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(final String branchName) {
        this.branchName = branchName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(final String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public String getInstrumentDate() {
        return instrumentDate;
    }

    public void setInstrumentDate(final String instrumentDate) {
        this.instrumentDate = instrumentDate;
    }

    public void setBankAdviseResultList(
            final List<BankAdviceReportInfo> bankAdviseResultList) {
        this.bankAdviseResultList = bankAdviseResultList;
    }

    public List<BankAdviceReportInfo> getBankAdviseResultList() {
        return bankAdviseResultList;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTextFileName() {
        return textFileName;
    }

    public void setTextFileName(final String textFileName) {
        this.textFileName = textFileName;
    }

    public Map<Integer, String> getMonthMap() {
        return monthMap;
    }

    public void setMonthMap(final Map<Integer, String> monthMap) {
        this.monthMap = monthMap;
    }

    public Map<Integer, String> getFullNameMonthMap() {
        return fullNameMonthMap;
    }

    public void setFullNameMonthMap(final Map<Integer, String> fullNameMonthMap) {
        this.fullNameMonthMap = fullNameMonthMap;
    }

    public FinancialYearHibernateDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(final Integer month) {
        this.month = month;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(final Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

}