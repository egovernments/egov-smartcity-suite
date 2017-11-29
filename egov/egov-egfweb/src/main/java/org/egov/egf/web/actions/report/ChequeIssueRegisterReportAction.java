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
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.model.BankAdviceReportInfo;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.instrument.InstrumentHeaderService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
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

@Results(value = { @Result(name = "result", location = "chequeIssueRegisterReport-result.jsp"),
        @Result(name = "PDF", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/pdf", Constants.CONTENT_DISPOSITION,
                "no-cache;filename=ChequeIssueRegister.pdf" }),
        @Result(name = "XLS", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/xls", Constants.CONTENT_DISPOSITION,
                "no-cache;filename=ChequeIssueRegister.xls" }) })
@ParentPackage("egov")
public class ChequeIssueRegisterReportAction extends BaseFormAction {
    /**
	 *
	 */
    private static final long serialVersionUID = -5452940328051657821L;
    private static final String MULTIPLE = "Multiple";
    String jasperpath = "/reports/templates/chequeIssueRegisterReport.jasper";
    String bankAdviceJasperPath = "/reports/templates/bankAdviceExcelReport.jasper";
    private List<ChequeIssueRegisterDisplay> chequeIssueRegisterList = new ArrayList<ChequeIssueRegisterDisplay>();
    private Date fromDate;
    private Date toDate;
    private String chequeFromNumber;
    private String chequeToNumber;
    private Department department;
    private Bankaccount accountNumber;
    ReportHelper reportHelper;
    private InputStream inputStream;
    private EgovCommon egovCommon;
    private @Autowired AppConfigValueService appConfigValuesService;
    private String ulbName = "";
    private String bank;
    private static final Logger LOGGER = Logger.getLogger(ChequeIssueRegisterReportAction.class);
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    private boolean chequePrintingEnabled;
    private String chequePrintAvailableAt;
    private boolean chequeFormatExists;
    private String chequeFormat = "";
    private Long instrumentHeaderId;
    private InstrumentHeaderService instrumentHeaderService;

    public ChequeIssueRegisterReportAction() {
        addRelatedEntity(Constants.EXECUTING_DEPARTMENT, Department.class);
    }

    @Override
    public void prepare() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        if (!parameters.containsKey("showDropDown")) {
            addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
            addDropdownData("bankAccountList", Collections.EMPTY_LIST);
            dropdownData.put("executingDepartmentList", masterDataCache.get("egi-department"));
        }
        populateUlbName();
    }

    @Override
    public String execute() throws Exception {
        return "form";
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public void generateReport() throws JRException, IOException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("----Inside generateReport---- ");

        accountNumber = (Bankaccount) persistenceService.find("from Bankaccount where id=?", accountNumber.getId());
        if (accountNumber.getChequeformat() != null && !accountNumber.getChequeformat().equals("")) {
            chequeFormat = accountNumber.getChequeformat().getId().toString();
        }

        validateDates(fromDate, toDate);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Querying to date range " + getFormattedDate(fromDate) + "to date "
                    + getFormattedDate(getNextDate(toDate)));
        // persistenceService.setType(InstrumentHeader.class);
        final List<AppConfigValues> printAvailConfig = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "chequeprintavailableat");

        chequePrintingEnabled = isChequePrintEnabled();

        for (final AppConfigValues appConfigVal : printAvailConfig)
            chequePrintAvailableAt = appConfigVal.getValue();

        final Query query = persistenceService
                .getSession()
                .createSQLQuery(
                        "select ih.instrumentnumber as chequeNumber,ih.instrumentdate as chequeDate,"
                                + "ih.instrumentamount as chequeAmount,vh.vouchernumber as voucherNumber,vh.id as vhId,ih.serialno as serialNo,vh.voucherdate as voucherDate,vh.name as voucherName,ih.payto as payTo,mbd.billnumber as billNumber,"
                                + "mbd.billDate as billDate,vh.type as type,es.DESCRIPTION as chequeStatus,ih.id as instrumentheaderid from egf_instrumentHeader ih,egf_instrumentvoucher iv,EGW_STATUS es,"
                                + "voucherheader vh left outer join miscbilldetail mbd on  vh.id=mbd.PAYVHID ,vouchermis vmis where ih.instrumentDate <'"
                                + getFormattedDate(getNextDate(toDate))
                                + "' and ih.instrumentDate>='"
                                + getFormattedDate(fromDate)
                                + "' and ih.isPayCheque='1' "
                                + "and ih.INSTRUMENTTYPE=(select id from egf_instrumenttype where TYPE='cheque' ) and vh.status not in ("
                                + getExcludeVoucherStatues() + ") and vh.id=iv.voucherheaderid and  bankAccountId="
                                + accountNumber.getId() + " and ih.id=iv.instrumentheaderid and ih.id_status=es.id "
                                + " and vmis.voucherheaderid=vh.id " + createQuery()
                                + " order by ih.instrumentDate,ih.instrumentNumber ")
                .addScalar("chequeNumber").addScalar("chequeDate", StandardBasicTypes.DATE)
                .addScalar("chequeAmount", BigDecimalType.INSTANCE).addScalar("voucherNumber")
                .addScalar("voucherDate", StandardBasicTypes.DATE).addScalar("voucherName").addScalar("payTo")
                .addScalar("billNumber").addScalar("billDate", StandardBasicTypes.DATE).addScalar("type")
                .addScalar("vhId", BigDecimalType.INSTANCE).addScalar("serialNo", LongType.INSTANCE)
                .addScalar("chequeStatus").addScalar("instrumentHeaderId", LongType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ChequeIssueRegisterDisplay.class));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Search query" + query.getQueryString());
        chequeIssueRegisterList = query.list();
        if (chequeIssueRegisterList == null)
            chequeIssueRegisterList = new ArrayList<ChequeIssueRegisterDisplay>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Got Cheque list| Size of list is" + chequeIssueRegisterList.size());
        updateBillNumber();
        updateVoucherNumber();
        removeDuplicates();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("--End  generateReport--");
    }

    public boolean isChequePrintEnabled() {

        String chequePrintEnabled = null;
        final List<AppConfigValues> enablePrintConfig = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "chequeprintingenabled");
        if (enablePrintConfig != null)
            for (final AppConfigValues appConfigVal : enablePrintConfig)
                chequePrintEnabled = appConfigVal.getValue();

        if (chequePrintEnabled != null && chequePrintEnabled.equalsIgnoreCase("Y"))
            return true;
        else
            return false;
    }

    private void removeDuplicates() {
        final Map<String, ChequeIssueRegisterDisplay> map = new HashMap<String, ChequeIssueRegisterDisplay>();
        for (final Iterator<ChequeIssueRegisterDisplay> row = chequeIssueRegisterList.iterator(); row.hasNext();) {
            final ChequeIssueRegisterDisplay next = row.next();
            if (map.get(next.getChequeNumber() + "-" + next.getSerialNo()) == null)
                map.put(next.getChequeNumber() + "-" + next.getSerialNo(), next);
            else
                row.remove();
        }
    }

    String createQuery() {
        String query = "";
        if (department != null && department.getId() != 0)
            query = query.concat(" and vmis.departmentid=" + department.getId());
        return query;
    }

    private void updateBillNumber() {
        final Map<String, ChequeIssueRegisterDisplay> map = new HashMap<String, ChequeIssueRegisterDisplay>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside updateBillNumber ");
        for (final ChequeIssueRegisterDisplay row : chequeIssueRegisterList)
            if (map.get(row.getChequeNumber()) == null)
                map.put(row.getChequeNumber(), row);
            else if (row.getBillNumber() != null
                    && row.getBillNumber().equalsIgnoreCase(map.get(row.getChequeNumber()).getBillNumber()))
                continue;
            else {
                map.get(row.getChequeNumber()).setBillNumber("MULTIPLE");
                row.setBillNumber(MULTIPLE);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End updateBillNumber ");
    }

    private void updateVoucherNumber() {
        final Map<String, ChequeIssueRegisterDisplay> map = new HashMap<String, ChequeIssueRegisterDisplay>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End updateVoucherNumber ");
        for (final ChequeIssueRegisterDisplay row : chequeIssueRegisterList)
            if (map.get(row.getChequeNumber()) == null)
                map.put(row.getChequeNumber(), row);
            else if (row.getVoucherNumber() != null
                    && row.getVoucherNumber().equalsIgnoreCase(map.get(row.getChequeNumber()).getVoucherNumber()))
                continue;
            else if (map.get(row.getChequeNumber()).getChequeStatus()
                    .equalsIgnoreCase(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS)
                    || map.get(row.getChequeNumber()).getChequeStatus()
                            .equalsIgnoreCase(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS)
                    || map.get(row.getChequeNumber()).getChequeStatus()
                            .equalsIgnoreCase(FinancialConstants.INSTRUMENT_CANCELLED_STATUS)
                    || row.getChequeStatus()
                            .equalsIgnoreCase(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS)
                    || row.getChequeStatus().equalsIgnoreCase(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS)
                    || row.getChequeStatus().equalsIgnoreCase(FinancialConstants.INSTRUMENT_CANCELLED_STATUS))
                continue;
            else {
                map.get(row.getChequeNumber()).setVoucherNumber("MULTIPLE");
                row.setVoucherNumber(MULTIPLE);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End updateVoucherNumber ");
    }

    private void validateDates(final Date fromDate, final Date toDate) {
        if (fromDate.compareTo(toDate) == 1)
            throw new ValidationException(Arrays.asList(new ValidationError("invalid.from.date", "invalid.from.date")));
    }

    @Action(value = "/report/chequeIssueRegisterReport-generatePdf")
    public String generatePdf() throws JRException, IOException {
        generateReport();
        final List<Object> data = new ArrayList<Object>();
        data.addAll(getChequeIssueRegisterList());
        inputStream = reportHelper.exportPdf(getInputStream(), jasperpath, getParamMap(), data);
        return "PDF";
    }

    @Action(value = "/report/chequeIssueRegisterReport-generateXls")
    public String generateXls() throws JRException,
            IOException {
        generateReport();
        final List<Object> data = new ArrayList<Object>();
        data.addAll(getChequeIssueRegisterList());
        inputStream = reportHelper.exportXls(getInputStream(), jasperpath,
                getParamMap(), data);
        return "XLS";
    }

    @Action(value = "/report/chequeIssueRegisterReport-bankAdviceExcel")
    public String bankAdviceExcel() throws JRException, IOException {
        BankAdviceReportInfo bankAdvice = new BankAdviceReportInfo();
        final InstrumentHeader instrumentHeader = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?",
                instrumentHeaderId);
        bankAdvice.setPartyName(instrumentHeader.getPayTo());
        bankAdvice.setAmount(instrumentHeader.getInstrumentAmount());
        final List<Object> data = new ArrayList<Object>();
        data.add(bankAdvice);
        inputStream = reportHelper.exportXls(getInputStream(), bankAdviceJasperPath, null, data);
        return "XLS";
    }

    @ReadOnly
    @Action(value = "/report/chequeIssueRegisterReport-ajaxPrint")
    public String ajaxPrint() throws JRException, IOException {
        generateReport();
        return "result";
    }

    @Override
    public Object getModel() {
        return null;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public String getFormattedDate(final Date date) {
        final SimpleDateFormat formatter = Constants.DDMMYYYYFORMAT1;
        return formatter.format(date);
    }

    private Date getNextDate(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    protected Map<String, Object> getParamMap() {
        accountNumber = (Bankaccount) persistenceService.find("from Bankaccount where id=?", accountNumber.getId());
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("bank", getFormattedBankName());
        paramMap.put("accountNumber", accountNumber.getAccountnumber());
        paramMap.put("fromDate", Constants.DDMMYYYYFORMAT1.format(fromDate));
        paramMap.put("toDate", Constants.DDMMYYYYFORMAT1.format(toDate));
        paramMap.put("ulbName", ulbName);
        if (department != null && department.getId() != null && department.getId() != 0) {
            final Department dept = (Department) persistenceService.find("from Department where id=?",
                    department.getId());
            paramMap.put("departmentName", dept.getName());
        }
        return paramMap;
    }

    public String getFormattedBankName() {
        final String[] bankData = bank.split("-");
        final Bank bank = (Bank) persistenceService.find("from Bank where id=?", Integer.valueOf(bankData[0]));
        final Bankbranch bankBranch = (Bankbranch) persistenceService.find("from Bankbranch where id=?",
                Integer.valueOf(bankData[1]));
        String name = "";
        if (bank != null && bankBranch != null)
            name = bank.getName().concat(" - ").concat(bankBranch.getBranchname());
        return name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public List<ChequeIssueRegisterDisplay> getChequeIssueRegisterList() {
        return chequeIssueRegisterList;
    }

    public void setChequeIssueRegisterList(final List<ChequeIssueRegisterDisplay> chequeIssueRegisterList) {
        this.chequeIssueRegisterList = chequeIssueRegisterList;
    }

    public void setAccountNumber(final Bankaccount bankAccount) {
        accountNumber = bankAccount;
    }

    public Bankaccount getAccountNumber() {
        return accountNumber;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    private String getExcludeVoucherStatues() {
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "statusexcludeReport");
        String statusExclude = "-1";
        statusExclude = appList.get(0).getValue();
        return statusExclude;
    }

    private void populateUlbName() {

        setUlbName(ReportUtil.getCityName());
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setBank(final String bank) {
        this.bank = bank;
    }

    public String getBank() {
        return bank;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setChequeToNumber(final String chequeToNumber) {
        this.chequeToNumber = chequeToNumber;
    }

    public String getChequeToNumber() {
        return chequeToNumber;
    }

    public void setChequeFromNumber(final String chequeFromNumber) {
        this.chequeFromNumber = chequeFromNumber;
    }

    public String getChequeFromNumber() {
        return chequeFromNumber;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public boolean isChequePrintingEnabled() {
        return chequePrintingEnabled;
    }

    public String getChequePrintAvailableAt() {
        return chequePrintAvailableAt;
    }

    public boolean isChequeFormatExists() {
        return chequeFormatExists;
    }

    public void setChequePrintingEnabled(boolean chequePrintingEnabled) {
        this.chequePrintingEnabled = chequePrintingEnabled;
    }

    public void setChequePrintAvailableAt(String chequePrintAvailableAt) {
        this.chequePrintAvailableAt = chequePrintAvailableAt;
    }

    public void setChequeFormatExists(boolean chequeFormatExists) {
        this.chequeFormatExists = chequeFormatExists;
    }

    public String getChequeFormat() {
        return chequeFormat;
    }

    public void setChequeFormat(String chequeFormat) {
        this.chequeFormat = chequeFormat;
    }

    public Long getInstrumentHeaderId() {
        return instrumentHeaderId;
    }

    public void setInstrumentHeaderId(Long instrumentHeaderId) {
        this.instrumentHeaderId = instrumentHeaderId;
    }

}