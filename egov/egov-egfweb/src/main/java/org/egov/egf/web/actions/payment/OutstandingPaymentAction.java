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
package org.egov.egf.web.actions.payment;


import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bankaccount;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=OutstandingPaymentReport.pdf" }),
                @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "application/xls", "contentDisposition", "no-cache;filename=OutstandingPaymentReport.xls" })
})
@ParentPackage("egov")

public class OutstandingPaymentAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 3437296032021248608L;
    private static final Logger LOGGER = Logger.getLogger(OutstandingPaymentAction.class);
    private List<Paymentheader> paymentHeaderList = new ArrayList<Paymentheader>();
    private final Map<Long, String> voucherHeaderMap = new HashMap<Long, String>();
    private Date asOnDate = new Date();
    private BigDecimal bankBalance = BigDecimal.ZERO;
    private EgovCommon egovCommon;
    private BigDecimal currentReceiptsAmount = BigDecimal.ZERO;
    private BigDecimal runningBalance = BigDecimal.ZERO;
    
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired AppConfigValueService appConfigValuesService;
    private Bankaccount bankAccount;
    private String voucherStatusKey = "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE";
    private final String jasperpath = "/reports/templates/OutstandingPaymentReport.jasper";
    private ReportHelper reportHelper;
    private InputStream inputStream;
    private String selectedVhs;
    private Long[] selectdVhs;
    private BigDecimal rBalance = BigDecimal.ZERO;
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    @Override
    public String execute() throws Exception {
        return "form";
    }

    public Long[] getSelectdVhs() {
        return selectdVhs;
    }

    public void setSelectdVhs(final Long[] selectdVhs) {
        this.selectdVhs = selectdVhs;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (!parameters.containsKey("skipPrepare")) {
            addDropdownData("bankList", Collections.EMPTY_LIST);
            addDropdownData("accNumList", Collections.EMPTY_LIST);
            addDropdownData("fundList", masterDataCache.get("egi-fund"));
        }
    }

    @Action(value = "/payment/outstandingPayment-ajaxLoadPaymentHeader")
    public String ajaxLoadPaymentHeader() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadPaymentHeader...");
        if (parameters.containsKey("bankAccount.id") && parameters.get("bankAccount.id")[0] != null) {
            if (parameters.containsKey("asOnDate") && parameters.get("asOnDate")[0] != null)
                try {
                    setAsOnDate(Constants.DDMMYYYYFORMAT2.parse(parameters.get("asOnDate")[0]));
                } catch (final ParseException e) {
                    throw new ValidationException("Invalid date", "Invalid date");
                }
            if (parameters.containsKey("asOnDate") && parameters.get("asOnDate")[0] != null)
                setSelectedVhs("selectedVhs");
            final Integer id = Integer.valueOf(parameters.get("bankAccount.id")[0]);
            bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?", id);
            // this is actually approval status
            final List<AppConfigValues> appConfig = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                    "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE");
            if (appConfig == null || appConfig.isEmpty())
                throw new ValidationException("", "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");

            String appConfigValue = "";
            boolean condtitionalAppConfigIsPresent = false;
            String designationName = null;
            String functionaryName = null;
            String stateWithoutCondition = "";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Beginning app config check...");
            for (final AppConfigValues app : appConfig)
            {
                appConfigValue = app.getValue();
                if (appConfigValue.contains(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE))
                {
                    condtitionalAppConfigIsPresent = true;
                    final String[] array = appConfigValue
                            .split(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE);
                    if (array.length != 2)
                        throw new ValidationException("", "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is invalid");
                    // Order assumed is first is designation Name, second functionary Name
                    designationName = array[0];
                    functionaryName = array[1];
                } else
                    stateWithoutCondition = appConfigValue;
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Ending app config check...");
            final StringBuffer query = new StringBuffer();
            query.append("from Paymentheader where voucherheader.voucherDate<=? and voucherheader.status in ( " +
                    +FinancialConstants.CREATEDVOUCHERSTATUS + "," + FinancialConstants.PREAPPROVEDVOUCHERSTATUS
                    + ") and bankaccount.id=? and" +
                    " state.type='Paymentheader'");
            if (condtitionalAppConfigIsPresent)
            {
                final String ownerIdList = getCommaSeperatedListForDesignationNameAndFunctionaryName(designationName,
                        functionaryName);
                query.append(" and state.owner in (" + ownerIdList + ") order by state.createdDate desc ");
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("In condtitionalAppConfigIsPresent - qry" + query.toString());
                paymentHeaderList.addAll(persistenceService.findPageBy(query.toString(), 1, 100, getAsOnDate(), id).getList());
            }
            else
            {
                query.append(" and state.value like '" + stateWithoutCondition + "' order by state.createdDate desc ");
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("In ELSE - qry" + query.toString());
                paymentHeaderList.addAll(persistenceService.findPageBy(query.toString(), 1, 100, getAsOnDate(), id).getList());
            }
            bankBalance = egovCommon.getBankBalanceAvailableforPayment(getAsOnDate(), id);

        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Ending ajaxLoadPaymentHeader...");
        return "results";
    }

    private String getCommaSeperatedListForDesignationNameAndFunctionaryName(final String designationName,
            final String functionaryName)
    {
        final String qrySQL = "select pos_id from eg_eis_employeeinfo empinfo, eg_designation desg, functionary func   " +
                " where empinfo.functionary_id=func.id and empinfo.DESIGNATIONID=desg.DESIGNATIONID " +
                " and empinfo.isactive=true   " +
                " and desg.DESIGNATION_NAME like '" + designationName + "' and func.NAME like '" + functionaryName + "' ";
        final Query query = persistenceService.getSession().createSQLQuery(qrySQL);
        final List<BigDecimal> result = query.list();
        if (result == null || result.isEmpty())
            throw new ValidationException("", "No employee with functionary -" + functionaryName + " and designation - "
                    + designationName);
        final StringBuffer returnListSB = new StringBuffer();
        String commaSeperatedList = "";
        for (final BigDecimal posId : result)
            returnListSB.append(posId.toString() + ",");
        commaSeperatedList = returnListSB.substring(0, returnListSB.length() - 1);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Commo seperated  list - " + commaSeperatedList);
        return commaSeperatedList;
    }

    public String getUlbName() {
        final Query query = persistenceService.getSession().createSQLQuery("select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    @Action(value = "/payment/outstandingPayment-exportPdf")
    public String exportPdf() throws JRException, IOException {
        final List<Object> dataSource = generateReportData();
        setInputStream(reportHelper.exportPdf(getInputStream(), jasperpath, getParamMap(), dataSource));
        return "PDF";
    }

    public void setPaymentHeaderList(final List<Paymentheader> paymentHeaderList) {
        this.paymentHeaderList = paymentHeaderList;
    }

    @Action(value = "/payment/outstandingPayment-exportXls")
    public String exportXls() throws JRException, IOException {
        final List<Object> dataSource = generateReportData();
        setInputStream(reportHelper.exportXls(getInputStream(), jasperpath, getParamMap(), dataSource));
        return "XLS";
    }

    private List<Object> generateReportData() {
        String[] splitVh = null;
        if (parameters.containsKey("selectedVhs") && parameters.get("selectedVhs")[0] != null) {
            final String[] vh_ids = parameters.get("selectedVhs");
            splitVh = vh_ids[0].split(",");
            for (int i = 0; i < splitVh.length; i++)
                voucherHeaderMap.put(Long.parseLong(splitVh[i]), "Selected");

        }

        ajaxLoadPaymentHeader();
        final List<Object> dataSource = new ArrayList<Object>();
        for (final Paymentheader row : paymentHeaderList) {
            final String chkSelected = voucherHeaderMap.get(row.getVoucherheader().getId());
            if ("Selected".equals(chkSelected))
                row.setIsSelected("Selected");
            else
                row.setIsSelected(null);
            dataSource.add(row);
        }
        return dataSource;
    }

    public String getSelectedVhs() {
        return selectedVhs;
    }

    public BigDecimal getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(final BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    public void setSelectedVhs(final String selectedVhs) {
        this.selectedVhs = selectedVhs;
    }

    public BigDecimal getRBalance() {
        return rBalance;
    }

    public void setRBalance(final BigDecimal balance) {
        rBalance = balance;
    }

    Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ulbName", getUlbName());
        bankAccount.getBankbranch().getBank().getName().concat("-")
        .concat(bankAccount.getBankbranch().getBranchname()).concat("-")
        .concat(bankAccount.getAccountnumber());
        final String heading = "Outstanding Payment Report as on " + Constants.DDMMYYYYFORMAT2.format(asOnDate);
        final String bankDetail = "Bank Balance Details as on " + Constants.DDMMYYYYFORMAT2.format(asOnDate);
        paramMap.put("heading", heading);
        paramMap.put("bankDetail", bankDetail);
        paramMap.put("bankName", bankAccount.getBankbranch().getBank().getName().toString());
        paramMap.put("bankBranchName", bankAccount.getBankbranch().getBranchname());
        paramMap.put("bankAccountNumber", bankAccount.getAccountnumber().toString());
        paramMap.put("chartOfAccount", bankAccount.getChartofaccounts().getGlcode());
        paramMap.put("currentBalance", bankBalance.toString());
        paramMap.put("runningBalance", runningBalance.toString());
        return paramMap;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public List<Paymentheader> getPaymentHeaderList() {
        return paymentHeaderList;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public BigDecimal getBankBalance() {
        return bankBalance;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT2.format(date);
    }

    @Override
    public Object getModel() {

        return null;
    }

    public void setVoucherStatusKey(final String voucherStatus) {
        voucherStatusKey = voucherStatus;
    }

    public String getVoucherStatusKey() {
        return voucherStatusKey;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    public void setCurrentReceiptsAmount(final BigDecimal currentReceiptsAmount) {
        this.currentReceiptsAmount = currentReceiptsAmount;
    }

    public BigDecimal getCurrentReceiptsAmount() {
        return currentReceiptsAmount;
    }

    public void setBankAccount(final Bankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Bankaccount getBankAccount() {
        return bankAccount;
    }
}