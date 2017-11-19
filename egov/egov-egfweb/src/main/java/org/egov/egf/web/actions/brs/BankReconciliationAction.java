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

import com.exilant.eGov.src.domain.BankReconciliationSummary;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.services.masters.BankService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({ @Result(name = "new", location = "reconciliationSummary-new.jsp"),
		@Result(name = "result", location = "reconciliationSummary-result.jsp") })
public class BankReconciliationAction extends BaseFormAction {
	@Autowired
	FinancialYearHibernateDAO financialYearDAO;
	private EgovCommon egovCommon;
	@Autowired
	BankService bankService;
	@Autowired
	private BankHibernateDAO bankHibernateDAO;
	private List<Bankbranch> branchList = Collections.EMPTY_LIST;
	private final List<Bankaccount> accountList = Collections.EMPTY_LIST;
	Date bankStmtDate = new Date();
	private String accountNumber;
	private Integer branchId;
	private Integer accountId;
	private Integer bankId;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	Bankaccount bankAccount;
	String bankSDate;
	String balanceAsPerStatement;
	String bank;
	String accountNum;
	String branch;

	BigDecimal accountBalance;
	double unReconciledCr;
	double unReconciledCrOthers;
	double unReconciledDr;
	double unReconciledDrOthers;
	double unReconciledCrBrsEntry;
	double unReconciledDrBrsEntry;
	double subTotal;
	double netTotal;
	private static final Logger LOGGER = Logger
			.getLogger(BankReconciliationAction.class);

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

		List<Bank> allBankHavingAccounts = bankHibernateDAO
				.getAllBankHavingBranchAndAccounts();
		dropdownData.put("bankList", allBankHavingAccounts);
		dropdownData.put("branchList", branchList);
		dropdownData.put("accountList", accountList);
		if (branchId != null) {
			branchList = persistenceService
					.findAllBy(
							"select  bb from Bankbranch bb,Bankaccount ba where bb.bank.id=? and ba.bankbranch=bb and bb.isactive=true",
							bankId);
			dropdownData.put("branchList", branchList);

		}
		if (accountId != null) {
			final List<Bankaccount> accountList = getPersistenceService()
					.findAllBy(
							"from Bankaccount ba where ba.bankbranch.id=? and isactive=true order by ba.chartofaccounts.glcode",
							branchId);
			dropdownData.put("accountList", accountList);
		}

	}

	@Action(value = "/brs/bankReconciliation-brsSummary")
	public String brsSummary() {

		bankSDate = parameters.get("bankStmtDate")[0];
		balanceAsPerStatement = parameters.get("bankStBalance")[0];

		bankAccount = (Bankaccount) persistenceService.find(
				"from Bankaccount where id=?", accountId.longValue());
		bank = bankAccount.getBankbranch().getBank().getName();
		accountNum = bankAccount.getAccountnumber();
		branch = bankAccount.getBankbranch().getBranchname();

		Date dt = new Date();

		try {
			dt = sdf.parse(bankSDate);

			accountBalance = egovCommon.getAccountBalance(dt,
					bankAccount.getId());
			CFinancialYear finYearByDate = financialYearDAO
					.getFinYearByDate(dt);
			String recDate = formatter.format(dt);
			Date fromDate = finYearByDate.getStartingDate();

			String unReconciledDrCr = bankReconciliationSummary
					.getUnReconciledDrCr(bankAccount.getId().intValue(),
							fromDate, dt);

			String drcrValues[] = unReconciledDrCr.split("/");
			if (LOGGER.isInfoEnabled())
				LOGGER.info("  unReconciledDrCr   " + unReconciledDrCr);
			if (LOGGER.isInfoEnabled())
				LOGGER.info("  drcrValues[]   " + drcrValues[1]);
			if (LOGGER.isInfoEnabled())
				LOGGER.info("  drcrValues   " + drcrValues.length);
			if (LOGGER.isInfoEnabled())
				LOGGER.info(drcrValues[0] + "  " + drcrValues[1] + "  "
						+ drcrValues[2] + "   " + drcrValues[3]);
			unReconciledCr = Double.parseDouble(drcrValues[0]);
			unReconciledCrOthers = Double.parseDouble(drcrValues[1]);
			unReconciledDr = Double.parseDouble(drcrValues[2]);
			unReconciledDrOthers = Double.parseDouble(drcrValues[3]);
			unReconciledCrBrsEntry = Double.parseDouble(drcrValues[4]);
			unReconciledDrBrsEntry = Double.parseDouble(drcrValues[5]);

			subTotal = unReconciledCr + unReconciledCrOthers
					+ accountBalance.doubleValue();
			netTotal = unReconciledCr + unReconciledCrOthers
					+ accountBalance.doubleValue() + unReconciledDr
					+ unReconciledDrOthers;

		} catch (Exception e) {

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

	public Integer getBranchId() {
		return branchId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

}