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
package org.egov.collection.web.actions.receipts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ApproverRemitterMapping;
import org.egov.collection.entity.CollectionBankRemittanceReport;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.service.ApproverRemitterMapService;
import org.egov.collection.service.RemittanceServiceImpl;
import org.egov.collection.service.spec.RemittanceSpec;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

@Results({ @Result(name = CashRemittanceAction.NEW, location = "cashRemittance-new.jsp"),
        @Result(name = CashRemittanceAction.PRINT_BANK_CHALLAN, type = "redirectAction", location = "remittanceStatementReport-printCashBankChallan.action", params = {
                "namespace", "/reports", "totalCashAmount", "${totalCashAmount}", "bank", "${bank}", "bankAccount",
                "${bankAccount}", "remittanceDate", "${remittanceDate}" }),
        @Result(name = CashRemittanceAction.INDEX, location = "cashRemittance-index.jsp") })
@ParentPackage("egov")
public class CashRemittanceAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CashRemittanceAction.class);
    private static final String BANK_ACCOUNT_NUMBER_QUERY = "select distinct ba.accountnumber from BANKACCOUNT ba where ba.id =:accountNumberId";
    private static final String SERVICE_FUND_QUERY = new StringBuilder()
            .append("select distinct sd.code as servicecode,fd.code as fundcode from BANKACCOUNT ba,")
            .append("EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd,FUND fd where asm.BANKACCOUNT=ba.ID ")
            .append("and asm.servicedetails=sd.ID and fd.ID=ba.FUNDID and ba.id= :accountNumberId").toString();
    private transient List<HashMap<String, Object>> paramList = null;
    private List<ReceiptHeader> voucherHeaderValues = new ArrayList<>(0);
    private List<CollectionBankRemittanceReport> bankRemittanceList;
    private String[] serviceNameArray;
    private String[] totalCashAmountArray;
    private String[] receiptDateArray;
    private String[] receiptNumberArray;
    private String[] fundCodeArray;
    private String[] departmentCodeArray;
    private String[] approverIdArray;
    private Integer accountNumberId;
    private Integer branchId;
    private Date remittanceDate;
    private Double totalCashAmount;
    private String bank;
    private String bankAccount;
    private Long finYearId;
    private Date fromDate;
    private Date toDate;
    String approverId = "-1";
    private Boolean showRemittanceDate = false;
    private String remittanceAmount;
    private String remitAccountNumber;
    private Integer pageSize;
    private static final String ACCOUNT_NUMBER_LIST = "accountNumberList";
    private static final String REMITTANCE_LIST = "REMITTANCE_LIST";
    private static final String APPROVER_LIST = "approverList";
    protected static final String PRINT_BANK_CHALLAN = "printBankChallan";
    private Boolean isBankCollectionRemitter;
    private final ReceiptHeader receiptHeaderIntsance = new ReceiptHeader();
    @Autowired
    private transient CollectionsUtil collectionsUtil;
    @Autowired
    private transient FinancialYearDAO financialYearDAO;
    @Autowired
    private transient BankaccountHibernateDAO bankaccountHibernateDAO;
    @Autowired
    ApproverRemitterMapService approverRemitterMapService;
    private RemittanceServiceImpl remittanceService;

    @Action(value = "/receipts/cashBankRemittance-newform")
    @SkipValidation
    public String newform() {
        populateRemittanceList();
        return NEW;
    }

    private void populateRemittanceList() {
        final AjaxBankRemittanceAction ajaxBankRemittanceAction = new AjaxBankRemittanceAction();
        ajaxBankRemittanceAction.setPersistenceService(getPersistenceService());
        ajaxBankRemittanceAction.setCollectionsUtil(collectionsUtil);
        ajaxBankRemittanceAction.bankBranchListOfService();
        addDropdownData("bankBranchList", ajaxBankRemittanceAction.getBankBranchArrayList());
        if (collectionsUtil.isBankCollectionRemitter(collectionsUtil.getLoggedInUser()))
            if (ajaxBankRemittanceAction.getBankBranchArrayList().isEmpty())
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "The user is not mapped to any bank branch, please contact system administrator.",
                        "bankremittance.error.bankcollectionoperator")));
            else
                branchId = ((Bankbranch) ajaxBankRemittanceAction.getBankBranchArrayList().get(0)).getId();

        if (branchId != null) {
            ajaxBankRemittanceAction.setBranchId(branchId);
            ajaxBankRemittanceAction.accountListOfService();
            addDropdownData(ACCOUNT_NUMBER_LIST, ajaxBankRemittanceAction.getBankAccountArrayList());
        } else
            addDropdownData(ACCOUNT_NUMBER_LIST, Collections.emptyList());
        addDropdownData("financialYearList", financialYearDAO.getAllActivePostingAndNotClosedFinancialYears());
        addDropdownData(APPROVER_LIST,
                approverRemitterMapService.getApprovers(collectionsUtil.getLoggedInUser()));
    }

    @Action(value = "/receipts/cashBankRemittance-listData")
    @SkipValidation
    public String listData() {
        remitAccountNumber = CollectionConstants.BLANK;
        String approverIdList = CollectionConstants.BLANK;
        if (accountNumberId != null) {
            final Query bankAccountQry = persistenceService.getSession().createNativeQuery(BANK_ACCOUNT_NUMBER_QUERY);
            bankAccountQry.setLong("accountNumberId", Long.valueOf(accountNumberId));
            final Object bankAccountResult = bankAccountQry.uniqueResult();
            remitAccountNumber = (String) bankAccountResult;
        }

        populateRemittanceList();
        if (fromDate != null && toDate != null && toDate.before(fromDate))
            addActionError(getText("bankremittance.before.fromdate"));

        if (!isBankCollectionRemitter) {
            if (getDropdownData().get(APPROVER_LIST).isEmpty())
                addActionError(getText("remittance.noapprovermapped"));
            if (approverId == null || Integer.parseInt(approverId) < 0) {
                approverIdList = ((List<ApproverRemitterMapping>) getDropdownData().get(APPROVER_LIST))
                        .stream()
                        .map(approverRemitterMap -> approverRemitterMap.getApprover().getId().toString())
                        .collect(Collectors.joining(CollectionConstants.SEPARATOR_COMMA));
            } else
                approverIdList = approverId;
        }

        if (!hasErrors() && accountNumberId != null) {
            final Query serviceFundQuery = persistenceService.getSession().createNativeQuery(SERVICE_FUND_QUERY);
            serviceFundQuery.setLong("accountNumberId", accountNumberId);
            final List<Object[]> queryResults = serviceFundQuery.list();

            final List<String> serviceCodeList = new ArrayList<>(0);
            final HashSet<String> fundCodeSet = new HashSet<>(0);
            for (int i = 0; i < queryResults.size(); i++) {
                final Object[] arrayObjectInitialIndex = queryResults.get(i);
                serviceCodeList.add(arrayObjectInitialIndex[0].toString());
                fundCodeSet.add(arrayObjectInitialIndex[1].toString());
            }

            final CFinancialYear financialYear = financialYearDAO.getFinancialYearById(finYearId);
            paramList = remittanceService.findCashRemittanceDetailsForServiceAndFund(approverIdList,
                    collectionsUtil.getJurisdictionBoundary(), "'" + StringUtils.join(serviceCodeList, "','") + "'",
                    "'" + StringUtils.join(fundCodeSet, "','") + "'",
                    fromDate == null ? financialYear.getStartingDate() : fromDate,
                    toDate == null ? financialYear.getEndingDate() : toDate);
            if (fromDate != null && toDate != null)
                pageSize = paramList.size();
            else
                pageSize = CollectionConstants.DEFAULT_PAGE_SIZE;
        }
        return NEW;
    }

    @Action(value = "/receipts/cashBankRemittance-printBankChallan")
    @SkipValidation
    public String printBankChallan() {
        return PRINT_BANK_CHALLAN;
    }

    public String edit() {
        return EDIT;
    }

    public String save() {
        return SUCCESS;
    }

    @Override
    public void prepare() {
        super.prepare();
        final String showRemitDate = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_SHOWREMITDATE);
        if (!showRemitDate.isEmpty() && CollectionConstants.YES.equals(showRemitDate))
            showRemittanceDate = true;

        isBankCollectionRemitter = collectionsUtil.isBankCollectionOperator(collectionsUtil.getLoggedInUser());
        addDropdownData("bankBranchList", Collections.emptyList());
        addDropdownData(ACCOUNT_NUMBER_LIST, Collections.emptyList());
    }

    @ValidationErrorPage(value = ERROR)
    @Action(value = "/receipts/cashBankRemittance-create")
    public String create() {
        final long startTimeMillis = System.currentTimeMillis();
        if (accountNumberId == null || accountNumberId == -1)
            throw new ValidationException(Arrays.asList(new ValidationError("Please select Account number",
                    "bankremittance.error.noaccountNumberselected")));
        voucherHeaderValues = remittanceService
                .createCashBankRemittance(RemittanceSpec.build().withApproverIdArray(getApproverIdArray())
                        .withServiceNameArray(getServiceNameArray()).withTotalAmount(getTotalCashAmountArray())
                        .withReceiptDateArray(getReceiptDateArray()).withFundCodeArray(getFundCodeArray())
                        .withDepartmentCodeArray(getDepartmentCodeArray()).withAccountNumberId(accountNumberId)
                        .withRemittanceDate(remittanceDate).build());
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("$$$$$$ Time taken to persist the remittance list (ms) = " + elapsedTimeMillis);
        bankRemittanceList = remittanceService.prepareCashRemittanceReport(voucherHeaderValues);
        if (getSession().get(REMITTANCE_LIST) != null)
            getSession().remove(REMITTANCE_LIST);
        getSession().put(REMITTANCE_LIST, bankRemittanceList);
        final Bankaccount bankAcc = bankaccountHibernateDAO.findById(Long.valueOf(accountNumberId), false);
        bankAccount = bankAcc.getAccountnumber();
        bank = bankAcc.getBankbranch().getBank().getName();
        totalCashAmount = getSum(getTotalCashAmountArray());
        return INDEX;
    }

    private Double getSum(final String[] array) {
        Double sum = 0.0;
        for (final String num : array)
            if (!num.isEmpty())
                sum = sum + Double.valueOf(num);
        return sum;
    }

    @Override
    public void validate() {
        super.validate();
        populateRemittanceList();
        final SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (receiptDateArray != null) {
            final String[] filterReceiptDateArray = removeNullValue(receiptDateArray);
            final String receiptEndDate = filterReceiptDateArray[filterReceiptDateArray.length - 1];
            try {
                if (!receiptEndDate.isEmpty() && remittanceDate != null
                        && remittanceDate.before(dateFomatter.parse(receiptEndDate)))
                    addActionError(getText("bankremittance.before.receiptdate"));
            } catch (final ParseException e) {
                LOGGER.debug("Exception in parsing date  " + receiptEndDate + " - " + e.getMessage());
                throw new ApplicationRuntimeException("Exception while parsing receiptEndDate date", e);
            }
        }
    }

    private String[] removeNullValue(String[] receiptDateArray) {
        final List<String> list = new ArrayList<>();
        for (final String s : receiptDateArray)
            if (s != null && s.length() > 0)
                list.add(s);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public Object getModel() {
        return receiptHeaderIntsance;
    }

    /**
     * @return the paramList
     */
    public List<HashMap<String, Object>> getParamList() {
        return paramList;
    }

    /**
     * @param paramList the paramList to set
     */
    public void setParamList(final List<HashMap<String, Object>> paramList) {
        this.paramList = paramList;
    }

    /**
     * @return the serviceName
     */
    public String[] getServiceNameArray() {
        return serviceNameArray;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceNameArray(final String[] serviceNameArray) {
        this.serviceNameArray = serviceNameArray;
    }

    /**
     * @return the approverId
     */
    public String[] getApproverIdArray() {
        return approverIdArray;
    }

    /**
     * @param approverId the approverId to set
     */
    public void setApproverIdArray(final String[] approverIdArray) {
        this.approverIdArray = approverIdArray;
    }

    /**
     * @return the totalCashAmount
     */
    public String[] getTotalCashAmountArray() {
        return totalCashAmountArray;
    }

    /**
     * @param totalCashAmount the totalCashAmount to set
     */
    public void setTotalCashAmountArray(final String[] totalCashAmountArray) {
        this.totalCashAmountArray = totalCashAmountArray;
    }

    /**
     * @return the receiptDate
     */
    public String[] getReceiptDateArray() {
        return receiptDateArray;
    }

    /**
     * @param receiptDate the receiptDate to set
     */
    public void setReceiptDateArray(final String[] receiptDateArray) {
        this.receiptDateArray = receiptDateArray;
    }

    /**
     * @return the voucherHeaderValues
     */
    public List<ReceiptHeader> getVoucherHeaderValues() {
        return voucherHeaderValues;
    }

    /**
     * @param voucherHeaderValues the voucherHeaderValues to set
     */
    public void setVoucherHeaderValues(final List<ReceiptHeader> voucherHeaderValues) {
        this.voucherHeaderValues = voucherHeaderValues;
    }

    /**
     * @return the fundCodeArray
     */
    public String[] getFundCodeArray() {
        return fundCodeArray;
    }

    /**
     * @param fundCodeArray the fundCodeArray to set
     */
    public void setFundCodeArray(final String[] fundCodeArray) {
        this.fundCodeArray = fundCodeArray;
    }

    /**
     * @return the departmentCodeArray
     */
    public String[] getDepartmentCodeArray() {
        return departmentCodeArray;
    }

    /**
     * @param departmentCodeArray the departmentCodeArray to set
     */
    public void setDepartmentCodeArray(final String[] departmentCodeArray) {
        this.departmentCodeArray = departmentCodeArray;
    }

    public String[] getReceiptNumberArray() {
        return receiptNumberArray;
    }

    public void setReceiptNumberArray(final String[] receiptNumberArray) {
        this.receiptNumberArray = receiptNumberArray;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(final Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getAccountNumberId() {
        return accountNumberId;
    }

    public void setAccountNumberId(final Integer accountNumberId) {
        this.accountNumberId = accountNumberId;
    }

    public Double getTotalCashAmount() {
        return totalCashAmount;
    }

    public void setTotalCashAmount(final Double totalCashAmount) {
        this.totalCashAmount = totalCashAmount;
    }

    public List<CollectionBankRemittanceReport> getBankRemittanceList() {
        return bankRemittanceList;
    }

    public void setBankRemittanceList(final List<CollectionBankRemittanceReport> bankRemittanceList) {
        this.bankRemittanceList = bankRemittanceList;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(final String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(final String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Boolean getShowRemittanceDate() {
        return showRemittanceDate;
    }

    public void setShowRemittanceDate(final Boolean showRemittanceDate) {
        this.showRemittanceDate = showRemittanceDate;
    }

    public Date getRemittanceDate() {
        return remittanceDate;
    }

    public void setRemittanceDate(final Date remittanceDate) {
        this.remittanceDate = remittanceDate;
    }

    public Long getFinYearId() {
        return finYearId;
    }

    public void setFinYearId(final Long finYearId) {
        this.finYearId = finYearId;
    }

    public void setRemittanceService(final RemittanceServiceImpl remittanceService) {
        this.remittanceService = remittanceService;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getRemittanceAmount() {
        return remittanceAmount;
    }

    public void setRemittanceAmount(String remittanceAmount) {
        this.remittanceAmount = remittanceAmount;
    }

    public Boolean getIsBankCollectionRemitter() {
        return isBankCollectionRemitter;
    }

    public void setIsBankCollectionRemitter(Boolean isBankCollectionRemitter) {
        this.isBankCollectionRemitter = isBankCollectionRemitter;
    }

    public String getRemitAccountNumber() {
        return remitAccountNumber;
    }

    public void setRemitAccountNumber(String remitAccountNumber) {
        this.remitAccountNumber = remitAccountNumber;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public String getApproverId() {
        return approverId;
    }
}
