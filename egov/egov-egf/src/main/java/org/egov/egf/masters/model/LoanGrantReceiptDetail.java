/**
 * 
 */
package org.egov.egf.masters.model;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;
import org.egov.model.instrument.InstrumentHeader;

/**
 * @author mani
 */
public class LoanGrantReceiptDetail extends BaseModel {
	private static final long	serialVersionUID	= -4039208357937783248L;
	final static Logger LOGGER=Logger.getLogger(LoanGrantReceiptDetail.class);
	private Bankaccount			bankaccount;
	private LoanGrantHeader		loanGrantHeader;
	private FundingAgency		fundingAgency;
	private CVoucherHeader		voucherHeader;
	private InstrumentHeader	instrumentHeader;
	private BigDecimal			amount;
	private String				description;
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public Bankaccount getBankaccount() {
		return bankaccount;
	}
	
	public String getDescription() {
		return description;
	}
	
	public FundingAgency getFundingAgency() {
		return fundingAgency;
	}
	
	public InstrumentHeader getInstrumentHeader() {
		return instrumentHeader;
	}
	
	public LoanGrantHeader getLoanGrantHeader() {
		return loanGrantHeader;
	}
	
	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public void setBankaccount(Bankaccount bankaccount) {
		this.bankaccount = bankaccount;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setFundingAgency(FundingAgency fundingAgency) {
		this.fundingAgency = fundingAgency;
	}
	
	public void setInstrumentHeader(InstrumentHeader instrumentHeader) {
		this.instrumentHeader = instrumentHeader;
	}
	
	public void setLoanGrantHeader(LoanGrantHeader loanGrantHeader) {
		this.loanGrantHeader = loanGrantHeader;
	}
	
	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}
	
}
