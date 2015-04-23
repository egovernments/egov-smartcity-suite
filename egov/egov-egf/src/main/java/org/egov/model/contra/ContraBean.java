package org.egov.model.contra;

import java.math.BigDecimal;

public class ContraBean {
	private String chequeInHand;
	private String cashInHand;
	private String bankBranchId;
	private String accountNumberId;
	private String fromBankAccountId;
	private String functionId;
	private String chequeNumber;
	private String chequeDate;
	private String fromBankId;
	private String bankId;
	private String toBankId;
	private BigDecimal amount; 
	private String boundaryLevel;
	private String accnumnar;
	private BigDecimal availableBalance;
	private String fromBankBranchId;
	private String toBankBranchId;
	private String result;
	private String mode;;
	private BigDecimal accountBalance;
	private String saveMode;
	private String modeOfCollection;
	private String fromBankBalance;
	private String toBankBalance;
	private Integer fromFundId;
	private Integer toFundId;
	private String 	sourceGlcode;
	private String 	destinationGlcode;
	private Integer toDepartment;

	/**
	 * @return the fromBankBalance
	 */
	public String getFromBankBalance() {
		return fromBankBalance;
	}
	/**
	 * @param fromBankBalance the fromBankBalance to set
	 */
	public void setFromBankBalance(String fromBankBalance) {
		this.fromBankBalance = fromBankBalance;
	}
	/**
	 * @return the toBankBalance
	 */
	public String getToBankBalance() {
		return toBankBalance;
	}
	/**
	 * @param toBankBalance the toBankBalance to set
	 */
	public void setToBankBalance(String toBankBalance) {
		this.toBankBalance = toBankBalance;
	}
	public String getSaveMode() {
		return saveMode;
	}
	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getFromBankId() {
		return fromBankId;
	}
	public void setFromBankId(String fromBankId) {
		this.fromBankId = fromBankId;
	}
	public String getSourceGlcode() {
		return sourceGlcode;
	}
	public void setSourceGlcode(String sourceGlcode) {
		this.sourceGlcode = sourceGlcode;
	}
	public String getDestinationGlcode() {
		return destinationGlcode;
	}
	public void setDestinationGlcode(String destinationGlcode) {
		this.destinationGlcode = destinationGlcode;
	}
	public String getToBankId() {
		return toBankId;
	}
	public void setToBankId(String toBankId) {
		this.toBankId = toBankId;
	}
	public String getFromBankBranchId() {
		return fromBankBranchId;
	}
	public void setFromBankBranchId(String fromBankBranchId) {
		this.fromBankBranchId = fromBankBranchId;
	}
	public String getToBankBranchId() {
		return toBankBranchId;
	}
	public void setToBankBranchId(String toBankBranchId) {
		this.toBankBranchId = toBankBranchId;
	}
	
	
	public String getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	private String toBankAccountId;
	public String getFromBankAccountId() {
		return fromBankAccountId;
	}
	public void setFromBankAccountId(String fromBankAccountId) {
		this.fromBankAccountId = fromBankAccountId;
	}
	public String getToBankAccountId() {
		return toBankAccountId;
	}
	public void setToBankAccountId(String toBankAccountId) {
		this.toBankAccountId = toBankAccountId;
	}
	
	public String getChequeInHand() {
		return chequeInHand;
	}
	public void setChequeInHand(String chequeInHand) {
		this.chequeInHand = chequeInHand;
	}
	public String getCashInHand() {
		return cashInHand;
	}
	public void setCashInHand(String cashInHand) {
		this.cashInHand = cashInHand;
	}
	public String getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(String bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
	public String getAccountNumberId() {
		return accountNumberId;
	}
	public void setAccountNumberId(String accountNumberId) {
		this.accountNumberId = accountNumberId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getBoundaryLevel() {
		return boundaryLevel;
	}
	public void setBoundaryLevel(String boundaryLevel) {
		this.boundaryLevel = boundaryLevel;
	}
	public String getAccnumnar() {
		return accnumnar;
	}
	public void setAccnumnar(String accnumnar) {
		this.accnumnar = accnumnar;
	}
	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getModeOfCollection() {
		return modeOfCollection;
	}
	public void setModeOfCollection(String modeOfCollection) {
		this.modeOfCollection = modeOfCollection;
	}
	
	public Integer getFromFundId() {
		return fromFundId;
	}
	public void setFromFundId(Integer fromFundId) {
		this.fromFundId = fromFundId;
	}
	public Integer getToFundId() {
		return toFundId;
	}
	public void setToFundId(Integer toFundId) {
		this.toFundId = toFundId;
	}
	public Integer getToDepartment() {
		return toDepartment;
	}
	public void setToDepartment(Integer toDepartment) {
		this.toDepartment = toDepartment;
	}
	public  void reset() {
		this.chequeInHand = null;
		this.cashInHand = null;
		this.bankBranchId = null;
		this.accountNumberId = null;
		this.fromBankAccountId = null;
		this.chequeNumber = null;
		this.chequeDate = null;
		this.fromBankId = null;
		this.bankId = null;
		this.toBankId = null;
		this.amount = null;
		this.boundaryLevel = null;
		this.accnumnar = null;
		this.availableBalance = null;
		this.fromBankBranchId = null;
		this.toBankBranchId = null;
		this.toBankAccountId = null;
		this.modeOfCollection="cheque";
		this.fromBankBalance=null;
		this.toBankBalance=null;
		this.fromFundId=null;
		this.toFundId=null;
		this.toDepartment=null;
		
		
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
}
