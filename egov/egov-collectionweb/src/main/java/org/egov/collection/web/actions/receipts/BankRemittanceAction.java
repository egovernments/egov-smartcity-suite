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
package org.egov.collection.web.actions.receipts;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.entity.CollectionBankRemittanceReport;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.service.BankAccountService;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.model.instrument.InstrumentHeader;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Results({
        @Result(name = BankRemittanceAction.NEW, location = "bankRemittance-new.jsp"),
        @Result(name = BankRemittanceAction.PRINT_BANK_CHALLAN, type = "redirectAction", location = "remittanceStatementReport-printBankChallan.action", params = {
                "namespace", "/reports", "totalCashAmount",
                "${totalCashAmount}", "totalChequeAmount", "${totalChequeAmount}", "totalOnlineAmount",
                "${totalOnlineAmount}", "bank",
                "${bank}", "bankAccount",
                "${bankAccount}" }),
        @Result(name = BankRemittanceAction.INDEX, location = "bankRemittance-index.jsp") })
@ParentPackage("egov")
public class BankRemittanceAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BankRemittanceAction.class);
    private List<HashMap<String, Object>> paramList = null;
    private ReceiptHeaderService receiptHeaderService;
    private final ReceiptHeader receiptHeaderIntsance = new ReceiptHeader();
    private List<ReceiptHeader> voucherHeaderValues = new ArrayList(0);
    private String[] serviceNameArray;
    private String[] totalCashAmountArray;
    private String[] totalChequeAmountArray;
    private String[] totalCardAmountArray;
    private String[] receiptDateArray;
    private String[] receiptNumberArray;
    private String[] totalOnlineAmountArray;
    private String[] fundCodeArray;
    private String[] departmentCodeArray;
    private Integer accountNumberId;
    private CollectionsUtil collectionsUtil;
    private Integer branchId;
    private static final String ACCOUNT_NUMBER_LIST = "accountNumberList";
    private Boolean isListData = false;

    // Added for Manual Work Flow
    private Integer positionUser;
    private Integer designationId;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private BankAccountService bankAccountSer;

    protected static final String PRINT_BANK_CHALLAN = "printBankChallan";
    private Double totalCashAmount;
    private Double totalChequeAmount;
    private Double totalOnlineAmount;
    private List<CollectionBankRemittanceReport> bankRemittanceList;
    private String bank;
    private String bankAccount;
    

    /**
     * @param collectionsUtil
     *            the collectionsUtil to set
     */
    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    @Action(value = "/receipts/bankRemittance-newform")
    public String newform() {
        populateBankAccountList();
        return NEW;
    }

    public void populateBankAccountList() {
        final AjaxBankRemittanceAction ajaxBankRemittanceAction = new AjaxBankRemittanceAction();
        ajaxBankRemittanceAction.setPersistenceService(getPersistenceService());
        ajaxBankRemittanceAction.bankBranchListOfService();
        addDropdownData("bankBranchList", ajaxBankRemittanceAction.getBankBranchArrayList());
        if (branchId != null) {
            ajaxBankRemittanceAction.setBranchId(branchId);
            ajaxBankRemittanceAction.accountListOfService();
            addDropdownData(ACCOUNT_NUMBER_LIST, ajaxBankRemittanceAction.getBankAccountArrayList());
        } else
            addDropdownData(ACCOUNT_NUMBER_LIST, Collections.EMPTY_LIST);
    }

    public String getJurisdictionBoundary() {
        final User user = collectionsUtil.getLoggedInUser();
        final Employee employee = employeeService.getEmployeeById(user.getId());
        final StringBuilder jurValuesId = new StringBuilder();
        for (final Jurisdiction element : employee.getJurisdictions()) {
            if (jurValuesId.length() > 0)
                jurValuesId.append(',');
            jurValuesId.append(element.getBoundary().getId());

            for (final Boundary boundary : element.getBoundary().getChildren()) {
                jurValuesId.append(',');
                jurValuesId.append(boundary.getId());
            }
        }
        return jurValuesId.toString();
    }

    @Action(value = "/receipts/bankRemittance-listData")
    public String listData() {
        isListData = true;
        populateBankAccountList();
        final String serviceFundQueryStr = "select distinct sd.code as servicecode,fd.code as fundcode from BANKACCOUNT ba,"
                + "EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd,FUND fd where asm.BANKACCOUNT=ba.ID and asm.servicedetails=sd.ID and fd.ID=ba.FUNDID and "
                + "ba.id=" + accountNumberId;

        final Query serviceFundQuery = persistenceService.getSession().createSQLQuery(serviceFundQueryStr);
        final List<Object[]> queryResults = serviceFundQuery.list();

        final List serviceCodeList = new ArrayList<String>();
        final List fundCodeList = new ArrayList<String>();
        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            serviceCodeList.add(arrayObjectInitialIndex[0].toString());
            fundCodeList.add(arrayObjectInitialIndex[1].toString());
        }
        paramList = receiptHeaderService.findAllRemittanceDetailsForServiceAndFund(getJurisdictionBoundary(), "'"
                + StringUtils.join(serviceCodeList, "','") + "'", "'" + StringUtils.join(fundCodeList, "','") + "'");
        return NEW;
    }

    @Action(value = "/receipts/bankRemittance-printBankChallan")
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
        addDropdownData("bankBranchList", Collections.EMPTY_LIST);
        addDropdownData("accountNumberList", Collections.EMPTY_LIST);
    }

    @Action(value = "/receipts/bankRemittance-create")
    @ValidationErrorPage(value = NEW)
    public String create() {
        final long startTimeMillis = System.currentTimeMillis();
        BigInteger accountNumber = null;
        String serviceName = "";
        String fundCode = "";

        for (int i = 0; i < getServiceNameArray().length; i++)
            if (getServiceNameArray() != null && !getServiceNameArray()[i].isEmpty()) {
                serviceName = getServiceNameArray()[i];
                fundCode = getFundCodeArray()[i];
                break;
            }
        final String bankAccountStr = "select distinct asm.BANKACCOUNT from BANKACCOUNT ba,"
                + "EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd,FUND fd where asm.BANKACCOUNT=ba.ID and asm.servicedetails=sd.ID and fd.ID=ba.FUNDID and "
                + "sd.name= '" + serviceName + "'  and fd.code='" + fundCode + "'";

        final Query bankAccountQry = persistenceService.getSession().createSQLQuery(bankAccountStr);
        if (bankAccountQry.list().size() > 1)
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "Mulitple Bank Accounts for the same Service and Fund is mapped. Please correct the data",
                    "bankremittance.error.multiplebankaccounterror")));

        final Object queryResults = bankAccountQry.uniqueResult();
        accountNumber = (BigInteger) queryResults;
        accountNumberId = accountNumber != null ? accountNumber.intValue() : accountNumberId;
        if (accountNumber == null || accountNumber.equals(-1) || accountNumber != null
                && accountNumber.intValue() != accountNumberId)
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "Bank Account for the Service and Fund is not mapped", "bankremittance.error.bankaccounterror")));
        voucherHeaderValues = receiptHeaderService.createBankRemittance(getServiceNameArray(),
                getTotalCashAmountArray(), getTotalChequeAmountArray(), getTotalCardAmountArray(),
                getTotalOnlineAmountArray(), getReceiptDateArray(), getFundCodeArray(), getDepartmentCodeArray(),
                accountNumberId, positionUser, getReceiptNumberArray());
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("$$$$$$ Time taken to persist the remittance list (ms) = " + elapsedTimeMillis);
        bankRemittanceList = prepareBankRemittanceReport(voucherHeaderValues);
        if(getSession().get("REMITTANCE_LIST")!=null)
            getSession().remove("REMITTANCE_LIST");
        getSession().put("REMITTANCE_LIST", bankRemittanceList);
        Bankaccount bankAcc = bankAccountSer.findById(Long.valueOf(accountNumberId), false);
        bankAccount = bankAcc.getAccountnumber();
        bank = bankAcc.getBankbranch().getBank().getName();
        totalCashAmount = getSum(getTotalCashAmountArray());
        totalChequeAmount = getSum(getTotalChequeAmountArray());
        totalOnlineAmount = getSum(getTotalOnlineAmountArray());
        return INDEX;
    }

    private List<CollectionBankRemittanceReport> prepareBankRemittanceReport(final List<ReceiptHeader> receiptHeaders) {
        final List<CollectionBankRemittanceReport> reportList = new ArrayList<CollectionBankRemittanceReport>();
        for (ReceiptHeader receiptHead : receiptHeaders) {
            final CollectionBankRemittanceReport collBankRemitReport = new CollectionBankRemittanceReport();
            collBankRemitReport.setReceiptNumber(receiptHead.getReceiptnumber());
                collBankRemitReport.setReceiptDate(receiptHead.getReceiptDate());
            collBankRemitReport.setVoucherNumber(receiptHead.getRemittanceVoucher());
            Iterator itr = receiptHead.getReceiptInstrument().iterator();
            while(itr.hasNext()) {
                InstrumentHeader instHead = (InstrumentHeader) itr.next();
            collBankRemitReport.setChequeNo(instHead.getInstrumentNumber());
            collBankRemitReport.setBranchName(instHead.getBankBranchName());
            collBankRemitReport.setBankName(instHead.getBankId()!=null?instHead.getBankId().getName():"");
            collBankRemitReport.setChequeDate(instHead.getInstrumentDate());
            collBankRemitReport.setPaymentMode(instHead.getInstrumentType().getType());
            collBankRemitReport.setAmount(instHead.getInstrumentAmount().doubleValue());
            }
            reportList.add(collBankRemitReport);
        }
        return reportList;
    }

    private Double getSum(final String[] array) {
        Double sum = 0.0;
        for (final String num : array)
            if (!num.isEmpty())
                sum = sum + Double.valueOf(num);
        return sum;
    }

    @Override
    public Object getModel() {
        return receiptHeaderIntsance;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    /**
     * @return the paramList
     */
    public List<HashMap<String, Object>> getParamList() {
        return paramList;
    }

    /**
     * @param paramList
     *            the paramList to set
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
     * @param serviceName
     *            the serviceName to set
     */
    public void setServiceNameArray(final String[] serviceNameArray) {
        this.serviceNameArray = serviceNameArray;
    }

    /**
     * @return the totalCashAmount
     */
    public String[] getTotalCashAmountArray() {
        return totalCashAmountArray;
    }

    /**
     * @param totalCashAmount
     *            the totalCashAmount to set
     */
    public void setTotalCashAmountArray(final String[] totalCashAmountArray) {
        this.totalCashAmountArray = totalCashAmountArray;
    }

    /**
     * @return the totalChequeAmount
     */
    public String[] getTotalChequeAmountArray() {
        return totalChequeAmountArray;
    }

    /**
     * @param totalChequeAmount
     *            the totalChequeAmount to set
     */
    public void setTotalChequeAmountArray(final String[] totalChequeAmountArray) {
        this.totalChequeAmountArray = totalChequeAmountArray;
    }

    /**
     * @return the receiptDate
     */
    public String[] getReceiptDateArray() {
        return receiptDateArray;
    }

    /**
     * @param receiptDate
     *            the receiptDate to set
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
     * @param voucherHeaderValues
     *            the voucherHeaderValues to set
     */
    public void setVoucherHeaderValues(final List<ReceiptHeader> voucherHeaderValues) {
        this.voucherHeaderValues = voucherHeaderValues;
    }

    /**
     * @return the totalOnlineAmountArray
     */
    public String[] getTotalOnlineAmountArray() {
        return totalOnlineAmountArray;
    }

    /**
     * @param totalOnlineAmountArray
     *            the totalOnlineAmountArray to set
     */
    public void setTotalOnlineAmountArray(final String[] totalOnlineAmountArray) {
        this.totalOnlineAmountArray = totalOnlineAmountArray;
    }

    /**
     * @return the fundCodeArray
     */
    public String[] getFundCodeArray() {
        return fundCodeArray;
    }

    /**
     * @param fundCodeArray
     *            the fundCodeArray to set
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
     * @param departmentCodeArray
     *            the departmentCodeArray to set
     */
    public void setDepartmentCodeArray(final String[] departmentCodeArray) {
        this.departmentCodeArray = departmentCodeArray;
    }

    /**
     * @return the totalCardAmountArray
     */
    public String[] getTotalCardAmountArray() {
        return totalCardAmountArray;
    }

    /**
     * @param totalCardAmountArray
     *            the totalCardAmountArray to set
     */
    public void setTotalCardAmountArray(final String[] totalCardAmountArray) {
        this.totalCardAmountArray = totalCardAmountArray;
    }

    /**
     * @return the positionUser
     */
    public Integer getPositionUser() {
        return positionUser;
    }

    /**
     * @param positionUser
     *            the positionUser to set
     */
    public void setPositionUser(final Integer positionUser) {
        this.positionUser = positionUser;
    }

    /**
     * @return the designationId
     */
    public Integer getDesignationId() {
        return designationId;
    }

    /**
     * @param designationId
     *            the designationId to set
     */
    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
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

    public Boolean getIsListData() {
        return isListData;
    }

    public void setIsListData(final Boolean isListData) {
        this.isListData = isListData;
    }

    public Double getTotalCashAmount() {
        return totalCashAmount;
    }

    public void setTotalCashAmount(final Double totalCashAmount) {
        this.totalCashAmount = totalCashAmount;
    }

    public Double getTotalChequeAmount() {
        return totalChequeAmount;
    }

    public void setTotalChequeAmount(final Double totalChequeAmount) {
        this.totalChequeAmount = totalChequeAmount;
    }

    public Double getTotalOnlineAmount() {
        return totalOnlineAmount;
    }

    public void setTotalOnlineAmount(final Double totalOnlineAmount) {
        this.totalOnlineAmount = totalOnlineAmount;
    }

    public List<CollectionBankRemittanceReport> getBankRemittanceList() {
        return bankRemittanceList;
    }

    public void setBankRemittanceList(List<CollectionBankRemittanceReport> bankRemittanceList) {
        this.bankRemittanceList = bankRemittanceList;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
}