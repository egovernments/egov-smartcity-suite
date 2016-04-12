package org.egov.web.actions.brs;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.services.masters.BankService;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.eGov.src.domain.BankReconciliationSummary;

@ParentPackage("egov")
@Results({ @Result(name = "new", location = "reconciliationSummary-new.jsp"),
	       @Result(name = "result", location = "reconciliationSummary-result.jsp")	
})
public class BankReconciliationAction extends BaseFormAction {
	@Autowired
	FinancialYearHibernateDAO financialYearDAO;
	private EgovCommon egovCommon;
	@Autowired
	BankService bankService;
	Date bankStmtDate = new Date();
	private String accountNumber;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	Bankaccount bankAccount;
	String bankSDate ;
	String balanceAsPerStatement;
	String bank;
	String accountNum;
	
	BigDecimal accountBalance;
	double unReconciledCr;
	double unReconciledCrOthers;
	double unReconciledDr;
	double unReconciledDrOthers;
	double unReconciledCrBrsEntry;
	double unReconciledDrBrsEntry;
	double subTotal;
	double netTotal;
	private static final Logger LOGGER = Logger.getLogger(BankReconciliationAction.class);
	

	@Autowired
	BankReconciliationSummary bankReconciliationSummary;
	@Override
	public Object getModel() {
		return null;
	}

	@Action(value = "/brs/bankReconciliation-newForm")
	public String newForm() {
		return "new";
	}

	public void prepare() {

		addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
		addDropdownData("accNumList", Collections.EMPTY_LIST);

	}

	@Action(value = "/brs/bankReconciliation-brsSummary")
	public String brsSummary() {
		
		 bankSDate = parameters.get("bankStmtDate")[0];
		 balanceAsPerStatement=parameters.get("bankStBalance")[0];
		
		
		 bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                 Long.valueOf(parameters.get("bankAccount.id")[0]));
		 bank=bankAccount.getBankbranch().getBank().getName();
		 accountNum=bankAccount.getAccountnumber();
		 
		Date dt = new Date();

		try {
			dt = sdf.parse(bankSDate);
		
		 accountBalance = egovCommon.getAccountBalance(dt,bankAccount.getId());
		CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dt);
		String recDate = formatter.format(dt);
		Date fromDate = finYearByDate.getStartingDate();
		
			String unReconciledDrCr=bankReconciliationSummary.getUnReconciledDrCr(bankAccount.getId().intValue(),fromDate, dt);
			
			String drcrValues[]=unReconciledDrCr.split("/");
			if(LOGGER.isInfoEnabled())     LOGGER.info("  unReconciledDrCr   "+unReconciledDrCr);
			if(LOGGER.isInfoEnabled())     LOGGER.info("  drcrValues[]   "+drcrValues[1]);
			if(LOGGER.isInfoEnabled())     LOGGER.info("  drcrValues   "+drcrValues.length);
			if(LOGGER.isInfoEnabled())     LOGGER.info(drcrValues[0]+"  "+drcrValues[1]+"  "+drcrValues[2]+"   "+drcrValues[3]);
			 unReconciledCr=Double.parseDouble(drcrValues[0]);
			 unReconciledCrOthers=Double.parseDouble(drcrValues[1]);
			 unReconciledDr=Double.parseDouble(drcrValues[2]);
			 unReconciledDrOthers=Double.parseDouble(drcrValues[3]);
			 unReconciledCrBrsEntry=Double.parseDouble(drcrValues[4]);
			 unReconciledDrBrsEntry=Double.parseDouble(drcrValues[5]);
			
			
			
			 subTotal=unReconciledCr+unReconciledCrOthers+accountBalance.doubleValue();
			 netTotal=unReconciledCr+unReconciledCrOthers+accountBalance.doubleValue()+unReconciledDr+unReconciledDrOthers;
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return "result";
	}

	public void setEgovCommon(final EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getBankStmtDate() {
		return bankStmtDate;
	}

	public String getBankSDate() {
		return bankSDate;
	}

	public String getBalanceAsPerStatement() {
		return balanceAsPerStatement;
	}

	public String getBank() {
		return bank;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setBankStmtDate(Date bankStmtDate) {
		this.bankStmtDate = bankStmtDate;
	}

	public void setBankSDate(String bankSDate) {
		this.bankSDate = bankSDate;
	}

	public void setBalanceAsPerStatement(String balanceAsPerStatement) {
		this.balanceAsPerStatement = balanceAsPerStatement;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public double getUnReconciledCr() {
		return unReconciledCr;
	}

	public double getUnReconciledCrOthers() {
		return unReconciledCrOthers;
	}

	public double getUnReconciledDr() {
		return unReconciledDr;
	}

	public double getUnReconciledDrOthers() {
		return unReconciledDrOthers;
	}

	public double getUnReconciledCrBrsEntry() {
		return unReconciledCrBrsEntry;
	}

	public double getUnReconciledDrBrsEntry() {
		return unReconciledDrBrsEntry;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public double getNetTotal() {
		return netTotal;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public void setUnReconciledCr(double unReconciledCr) {
		this.unReconciledCr = unReconciledCr;
	}

	public void setUnReconciledCrOthers(double unReconciledCrOthers) {
		this.unReconciledCrOthers = unReconciledCrOthers;
	}

	public void setUnReconciledDr(double unReconciledDr) {
		this.unReconciledDr = unReconciledDr;
	}

	public void setUnReconciledDrOthers(double unReconciledDrOthers) {
		this.unReconciledDrOthers = unReconciledDrOthers;
	}

	public void setUnReconciledCrBrsEntry(double unReconciledCrBrsEntry) {
		this.unReconciledCrBrsEntry = unReconciledCrBrsEntry;
	}

	public void setUnReconciledDrBrsEntry(double unReconciledDrBrsEntry) {
		this.unReconciledDrBrsEntry = unReconciledDrBrsEntry;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public void setNetTotal(double netTotal) {
		this.netTotal = netTotal;
	}
	

}