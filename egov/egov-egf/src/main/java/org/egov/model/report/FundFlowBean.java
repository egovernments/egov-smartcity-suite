/**
 * 
 */
package org.egov.model.report;
  
import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.BaseModel;

/**
 * @author mani
 *
 */
public class FundFlowBean extends BaseModel {
	
	private String bankName;
	private BigDecimal bankAccountId;
	private String glcode;
	private Date reportDate;
	private String accountNumber;
	private String fundName;
	private Boolean walkinPaymentAccount;
	private BigDecimal openingBalance=BigDecimal.ZERO.setScale(2);
	private BigDecimal currentReceipt=BigDecimal.ZERO.setScale(2);
	private BigDecimal FundsAvailable=BigDecimal.ZERO.setScale(2);
	private BigDecimal btbPayment=BigDecimal.ZERO.setScale(2);//this will be on a perticular day
	private BigDecimal btbReceipt=BigDecimal.ZERO.setScale(2);//this will be on a perticular day
	private BigDecimal concurranceBPV=BigDecimal.ZERO.setScale(2);//this will be on a perticular day
	private BigDecimal outStandingBPV=BigDecimal.ZERO.setScale(2);//this will be till date
	private BigDecimal closingBalance=BigDecimal.ZERO.setScale(2);
	private String codeId;   
	
public FundFlowBean(){};
	
	public FundFlowBean( String fundName, String accountNumber,BigDecimal openingBalance,BigDecimal currentReceipt,BigDecimal btbPayment) {
		super();
		this.fundName = fundName;
		this.accountNumber = accountNumber;
		this.currentReceipt=currentReceipt;
		this.btbPayment=btbPayment;
		this.openingBalance=openingBalance;
	}
	public FundFlowBean( String fundName, String accountNumber,BigDecimal openingBalance,BigDecimal currentReceipt,BigDecimal btbPayment,BigDecimal btbReceipt) {
		super();
		this.fundName = fundName;
		this.accountNumber = accountNumber;
		this.currentReceipt=currentReceipt;
		this.btbPayment=btbPayment;
		this.btbReceipt=btbReceipt;
		this.openingBalance=openingBalance;
	}
	public FundFlowBean( String fundName, String accountNumber,BigDecimal openingBalance,BigDecimal currentReceipt,BigDecimal btbPayment,BigDecimal btbReceipt,BigDecimal concurranceBPV,BigDecimal outStandingBPV) {
		super();
		this.fundName = fundName;
		this.accountNumber = accountNumber;
		this.openingBalance=openingBalance;
		this.currentReceipt=currentReceipt;
		this.btbPayment=btbPayment;
		this.btbReceipt = btbReceipt;
		this.concurranceBPV=concurranceBPV;
		this.outStandingBPV=outStandingBPV;
		
	}
	
	public BigDecimal getOutStandingBPV() {
		
		return outStandingBPV;
	}
	
	public void setOutStandingBPV(BigDecimal outStandingBPV) {
		this.outStandingBPV = outStandingBPV==null?BigDecimal.ZERO.setScale(2):outStandingBPV.setScale(2);
	}
	           
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;   
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance =openingBalance==null?BigDecimal.ZERO.setScale(2): openingBalance.setScale(2);
	}
	public BigDecimal getCurrentReceipt() {
		return currentReceipt;
	}
	public void setCurrentReceipt(BigDecimal currentReceipt) {
		this.currentReceipt = currentReceipt==null?BigDecimal.ZERO.setScale(2):currentReceipt.setScale(2);
	}
	public BigDecimal getFundsAvailable() {
		return FundsAvailable;
	}
	public void setFundsAvailable(BigDecimal fundsAvailable) {
		FundsAvailable = fundsAvailable==null?BigDecimal.ZERO.setScale(2):fundsAvailable.setScale(2);
	}
	public BigDecimal getBtbPayment() {
		return btbPayment;
	}
	public void setBtbPayment(BigDecimal btbPayment) {
		this.btbPayment =btbPayment==null?BigDecimal.ZERO.setScale(2): btbPayment.setScale(2);
	}
	public BigDecimal getBtbReceipt() {
		return btbReceipt;
	}
	public void setBtbReceipt(BigDecimal btbReceipt) {
		this.btbReceipt =btbReceipt==null?BigDecimal.ZERO.setScale(2): btbReceipt.setScale(2);
	}
	public BigDecimal getConcurranceBPV() {
		return concurranceBPV;
	}
	public void setConcurranceBPV(BigDecimal concurranceBPV) {
	this.concurranceBPV=concurranceBPV==null?BigDecimal.ZERO.setScale(2):concurranceBPV.setScale(2);
	}
	public BigDecimal getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance==null?BigDecimal.ZERO.setScale(2):closingBalance.setScale(2);
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public BigDecimal getBankAccountId() {
		return bankAccountId;
	}
	public void setBankAccountId(BigDecimal bankAccountId) {
		this.bankAccountId = bankAccountId;
	}                    

	public String getGlcode() {
		return glcode;
	}
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}
	public Boolean getWalkinPaymentAccount() {
		return walkinPaymentAccount;
	}
	public void setWalkinPaymentAccount(Boolean walkinPaymentAccount) {
		this.walkinPaymentAccount = walkinPaymentAccount;
	}
	
	public String toString()
	{

		return "id:"+id+",acccode:"+accountNumber+",createdDate:"+createdDate+",modifiedDate:"+modifiedDate+",opb:"+openingBalance;
	}
}
