/**
 * 
 */
package org.egov.works.web.actions.report;

import java.math.BigDecimal;

/**
 * @author manoranjan
 *
 */
public class TaxDeductionStmntBean {
	
	private String name;
	
	private BigDecimal grossValue;
	
	private BigDecimal netPayble;
	
	private String paymentDate;
	
	private String voucherDate;
	
	private BigDecimal taxDeductedAmt;
	
	private String remittedDate;

	public String getName() {
		return name;
	}

	public BigDecimal getGrossValue() {
		return grossValue;
	}

	public BigDecimal getNetPayble() {
		return netPayble;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public String getVoucherDate() {
		return voucherDate;
	}

	public BigDecimal getTaxDeductedAmt() {
		return taxDeductedAmt;
	}

	public String getRemittedDate() {
		return remittedDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGrossValue(BigDecimal grossValue) {
		this.grossValue = grossValue;
	}

	public void setNetPayble(BigDecimal netPayble) {
		this.netPayble = netPayble;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}

	public void setTaxDeductedAmt(BigDecimal taxDeductedAmt) {
		this.taxDeductedAmt = taxDeductedAmt;
	}

	public void setRemittedDate(String remittedDate) {
		this.remittedDate = remittedDate;
	}
	
	
	
	

}
