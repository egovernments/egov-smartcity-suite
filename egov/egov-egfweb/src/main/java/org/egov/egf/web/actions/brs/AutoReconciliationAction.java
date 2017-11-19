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
package org.egov.egf.web.actions.brs;

import net.sf.jasperreports.engine.JRException;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.model.brs.AutoReconcileBean;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
    @Result(name = AutoReconciliationAction.NEW, location = "autoReconciliation-" + AutoReconciliationAction.NEW + ".jsp"),
    @Result(name = "result", location = "autoReconciliation-" + "result" + ".jsp"),
    @Result(name = "report", location = "autoReconciliation-" + "report" + ".jsp"),
    @Result(name = "upload", location = "autoReconciliation-upload.jsp"),
    @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
            "application/pdf", "contentDisposition", "no-cache;filename=AutoReconcileReport.pdf" }),
            @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                    "application/xls", "contentDisposition", "no-cache;filename=AutoReconcileReport.xls" })
})
public class AutoReconciliationAction extends BaseFormAction {

   
    private static final long serialVersionUID = -4207341983597707193L;
    private static final Logger LOGGER = Logger.getLogger(AutoReconciliationAction.class);
   
    private List<Bankbranch> branchList = Collections.EMPTY_LIST;
    private final List<Bankaccount> accountList = Collections.EMPTY_LIST;
    private Integer accountId;
    private Integer bankId;
    private Integer branchId;
    private Date reconciliationDate;
    private Date fromDate;
    private Date toDate;
    private String accNo;
    private File bankStatmentInXls;
    private String bankStatmentInXlsFileName;
    private String failureMessage = "Invalid data in  the  following row(s), please correct and upload again\n";
    private final String successMessage = "BankStatement upload completed Successfully # rows processed";
    private boolean isFailed;
   

    private final String jasperpath = "/reports/templates/AutoReconcileReport.jasper";
    private ReportHelper reportHelper;
    private InputStream inputStream;
    private final String BRS_MESSAGE_MORE_THAN_ONE_MATCH = "found more than one match in instruments";
    private final String BRS_MESSAGE_DUPPLICATE_IN_BANKSTATEMENT = "duplicate instrument number within the bankstament";
   
  
    private List<AutoReconcileBean> statementsNotInBankBookList;
    private List<AutoReconcileBean> statementsFoundButNotProcessed;
    private FinancialYearDAO financialYearDAO;
    private Date finYearStartDate;
    private List<AutoReconcileBean> entriesNotInBankStament;
    private Bankaccount bankAccount;
    private BigDecimal notInBooktotalDebit;
    private BigDecimal notInBooktotalCredit;
    private BigDecimal notprocessedCredit;
    
    private BigDecimal notprocessedDebit;
    private BigDecimal notprocessedNet;
    private BigDecimal notInBookNet;
    private String notInBookNetBal;
    private BigDecimal notInStatementTotalDebit;
    private BigDecimal notInStatementTotalCredit;
    private BigDecimal notInStatementNet;
    private BigDecimal bankBookBalance;
    @Autowired
    private AutoReconcileHelper autoReconcileHelper;
    
    @Autowired
    private BankHibernateDAO bankHibernateDAO;
    
    

    public BigDecimal getBankBookBalance() {
        return autoReconcileHelper.getBankBookBalance();
    }

    public void setBankBookBalance(final BigDecimal bankBookBalance) {
        this.bankBookBalance = bankBookBalance;
    }

    private BigDecimal brsBalance;
    private BigDecimal totalNotReconciledAmount;
    private Integer statusId;

    public BigDecimal getBrsBalance() {
        return autoReconcileHelper.getBrsBalance();
    }

    public void setBrsBalance(final BigDecimal brsBalance) {
        this.brsBalance = brsBalance;
    }

    public Bankaccount getBankAccount() {
        return autoReconcileHelper.getBankAccount();
    }

    public void setBankAccount(final Bankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public Object getModel() {
        return new Bankreconciliation();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare()
    {
    	List<Bank> allBankHavingAccounts = bankHibernateDAO.getAllBankHavingBranchAndAccounts(); 
        dropdownData.put("bankList", allBankHavingAccounts);  
        dropdownData.put("branchList", branchList);
        dropdownData.put("accountList", accountList);
        if (branchId != null)
        {
            branchList = persistenceService
                    .findAllBy(
                            "select  bb from Bankbranch bb,Bankaccount ba where bb.bank.id=? and ba.bankbranch=bb and bb.isactive=true",
                            bankId);
            dropdownData.put("branchList", branchList);

        }
        if (accountId != null)
        {
            final List<Bankaccount> accountList = getPersistenceService().findAllBy(
                    "from Bankaccount ba where ba.bankbranch.id=? and isactive=true order by ba.chartofaccounts.glcode", branchId);
            dropdownData.put("accountList", accountList);
        }
      
    }

    private void setup() {
		autoReconcileHelper.setAccountId(accountId);
		autoReconcileHelper.setReconciliationDate(reconciliationDate);
		autoReconcileHelper.setFromDate(fromDate);
		autoReconcileHelper.setToDate(toDate);
		autoReconcileHelper.setBankStatmentInXls(bankStatmentInXls);
		autoReconcileHelper.setBankStatmentInXlsFileName(bankStatmentInXlsFileName);
		 
		
	}

	@Action(value = "/brs/autoReconciliation-newForm")
    public String newForm()
    {
        return NEW;
    }

    @Action(value = "/brs/autoReconciliation-beforeUpload")
    public String beforeUpload()
    {
    	
        return "upload";
    }

    @Action(value = "/brs/autoReconciliation-upload")
    @ValidationErrorPage("upload")
    public String upload()
    {
    	setup() ;
    	autoReconcileHelper.upload();
         return "upload";
    }

   

    @Override
    public void validate()
    {

    }

    public String getMessage() {
        return autoReconcileHelper.getMessage();
    }

   

    public String getFailureMessage() {
        return autoReconcileHelper.getFailureMessage();
    }

    

   

    /**
     * Step1: mark which are all we are going to process step2 :find duplicate and mark to be processed manually step3: process
     * non duplicates
     * @return
     */
    /**
     * @return
     */
    @Action(value = "/brs/autoReconciliation-schedule")
    public String schedule()
    {
    	setup() ;
    	autoReconcileHelper.schedule();  
        return "result";
    }

     
    public int getRowCount() {
        return autoReconcileHelper.getRowCount();
    }
    
    public int getCount() {
        return autoReconcileHelper.getCount();
    }
   

     

    
    @Action(value = "/brs/autoReconciliation-generateReport")
    @SuppressWarnings({ "unchecked", "deprecation" })
    public String generateReport() {
    	setup() ;
        autoReconcileHelper.generateReport();
        return "report";

    }

    public BigDecimal getTotalNotReconciledAmount() {
        return autoReconcileHelper.getTotalNotReconciledAmount();
    }

    

    public BigDecimal getNotInBooktotalDebit() {
        return autoReconcileHelper.getNotInBooktotalDebit();
    }

    public BigDecimal getNotInBooktotalCredit() {
        return autoReconcileHelper.getNotInBooktotalCredit();
    }

    public BigDecimal getNotInBookNet() {
        return autoReconcileHelper.getNotInBookNet();
    }

    public BigDecimal getNotInStatementTotalDebit() {
        return autoReconcileHelper.getNotInStatementTotalDebit();
    }

    public BigDecimal getNotInStatementTotalCredit() {
        return autoReconcileHelper.getNotInStatementTotalCredit();
    }

    public BigDecimal getNotInStatementNet() {
        return autoReconcileHelper.getNotInStatementNet();
    }

    

    

     

    

    public Date getReconciliationDate() {
        return reconciliationDate;
    }

    public void setReconciliationDate(final Date reconciliationDate) {
        this.reconciliationDate = reconciliationDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(final int accountId) {
        this.accountId = accountId;
    }

    public File getBankStatmentInXls() {
        return bankStatmentInXls;
    }

    public void setBankStatmentInXls(final File bankStatmentInXls) {
        this.bankStatmentInXls = bankStatmentInXls;
    }

    public void setBankStatmentInXlsContentType(final String bankStatmentInXlsContentType) {
    }

    public void setBankStatmentInXlsFileName(final String bankStatmentInXlsFileName) {
        this.bankStatmentInXlsFileName = bankStatmentInXlsFileName;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(final String accNo) {
        this.accNo = accNo;
    }

    public int getBankId() {
        return bankId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBankId(final int bankId) {
        this.bankId = bankId;
    }

    public void setBranchId(final int branchId) {
        this.branchId = branchId;
    }

     

    public List<AutoReconcileBean> getStatementsNotInBankBookList() {
        return autoReconcileHelper.getStatementsNotInBankBookList();
    }

    public void setStatementsNotInBankBookList(final List<AutoReconcileBean> statementsNotInBankBookList) {
        this.statementsNotInBankBookList = statementsNotInBankBookList;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public List<AutoReconcileBean> getEntriesNotInBankStament() {
        return autoReconcileHelper.getEntriesNotInBankStament();
    }

    public void setEntriesNotInBankStament(final List<AutoReconcileBean> entriesNotInBankStament) {
        this.entriesNotInBankStament = entriesNotInBankStament;
    }

    public List<AutoReconcileBean> getStatementsFoundButNotProcessed() {
        return autoReconcileHelper.getStatementsFoundButNotProcessed();
    }

    public BigDecimal getNotprocessedNet() {
        return autoReconcileHelper.getNotprocessedNet();
    }

    public void setStatementsFoundButNotProcessed(final List<AutoReconcileBean> statementsFoundButNotProcessed) {
        this.statementsFoundButNotProcessed = statementsFoundButNotProcessed;
    }

    public void setNotprocessedNet(final BigDecimal notprocessedNet) {
        this.notprocessedNet = notprocessedNet;
    }
    
    @Action(value = "/brs/autoReconciliation-generatePDF")
    public String generatePDF() throws Exception {
        final List<Object> dataSource = new ArrayList<Object>();
        final AutoReconcileBean AutoReconcileObj = new AutoReconcileBean();

        generateReport();
        if (getStatementsNotInBankBookList().size() == 0) {
            AutoReconcileObj.setNoDetailsFound("No Dteails Found");
            getStatementsNotInBankBookList().add(AutoReconcileObj);
        }
        for (final AutoReconcileBean row : getStatementsNotInBankBookList())
            dataSource.add(row);
        inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), dataSource);
        return "PDF";
    }
    @Action(value = "/brs/autoReconciliation-generateXLS")
    public String generateXLS() throws JRException, IOException {
        final List<Object> dataSource = new ArrayList<Object>();
        final AutoReconcileBean AutoReconcileObj = new AutoReconcileBean();
        generateReport();

        if (getStatementsNotInBankBookList().size() == 0) {
            AutoReconcileObj.setNoDetailsFound("No Details Found");
            getStatementsNotInBankBookList().add(AutoReconcileObj);
        }
        for (final AutoReconcileBean row : getStatementsNotInBankBookList())
            dataSource.add(row);
        inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), dataSource);
        return "XLS";
    }

    protected Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        final AutoReconcileBean AutoReconcileObj = new AutoReconcileBean();
        paramMap.put("heading", "Bank reconcilation statement from " + Constants.DDMMYYYYFORMAT2.format(fromDate) + " to "
                + Constants.DDMMYYYYFORMAT2.format(toDate));
        paramMap.put("bankName", autoReconcileHelper.getBankAccount().getBankbranch().getBank().getName());
        paramMap.put("accountNumber", autoReconcileHelper.getBankAccount().getAccountnumber());
        paramMap.put("accountCode", autoReconcileHelper.getBankAccount().getChartofaccounts().getGlcode());
        paramMap.put("accountDescription", autoReconcileHelper.getBankAccount().getChartofaccounts().getName());
        paramMap.put("bankBookBalance", autoReconcileHelper.getBankBookBalance());
        paramMap.put("notInBookNet", autoReconcileHelper.getNotInBookNetBal());
        paramMap.put("notprocessedNet",autoReconcileHelper.getNotprocessedNet());
        paramMap.put("notInStatementNet", autoReconcileHelper.getNotInStatementNet());
        paramMap.put("totalNotReconciledAmount", autoReconcileHelper.getTotalNotReconciledAmount());
        paramMap.put("brsBalance", autoReconcileHelper.getBrsBalance());

        final List<Object> statementDataSource = new ArrayList<Object>();
        final List<Object> entriesNotInBankStamentDataSource = new ArrayList<Object>();
        paramMap.put("BankStatement", reportHelper.getClass().getResourceAsStream("/reports/templates/BankStatement.jasper"));
        if (getStatementsFoundButNotProcessed().size() == 0) {
            AutoReconcileObj.setNoDetailsFound("No Details Found");
            autoReconcileHelper.getStatementsFoundButNotProcessed().add(AutoReconcileObj);
        }
        for (final AutoReconcileBean row : getStatementsFoundButNotProcessed())
            statementDataSource.add(row);

        paramMap.put("statementsFoundButNotProcessedList", statementDataSource);

        paramMap.put("EntriesNotinBankStatement",
                reportHelper.getClass().getResourceAsStream("/reports/templates/BankBookEntriesNotinBankStatement.jasper"));
        /*
         * To print the subreport if no entires found for EntriesNotinBankStatement added nodetailFound Object
         */
        if (getEntriesNotInBankStament().size() == 0) {
            AutoReconcileObj.setNoDetailsFound("No Details Found");
            getEntriesNotInBankStament().add(AutoReconcileObj);
        }
        for (final AutoReconcileBean row :  getEntriesNotInBankStament())
            entriesNotInBankStamentDataSource.add(row);
        paramMap.put("BankBookEntriesNotinBankStatementList", entriesNotInBankStamentDataSource);

        return paramMap;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getNotInBookNetBal() {
        return autoReconcileHelper.getNotInBookNetBal();
    }

    public void setNotInBookNetBal(final String notInBookNetBal) {
        this.notInBookNetBal = notInBookNetBal;
    }

}