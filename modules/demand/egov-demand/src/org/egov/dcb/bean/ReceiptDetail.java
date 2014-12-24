package org.egov.dcb.bean;

import java.math.BigDecimal;

import org.egov.commons.Installment;

public class ReceiptDetail {
	private static final String TO_STRING_SEP = ";";
	
	private Installment installment=null;
	private BigDecimal amount;
	private String reasonCode;
	
    public ReceiptDetail(Installment installment, BigDecimal amount, String reasonCode) {
        this.installment = installment;
        this.amount = amount;
        this.reasonCode = reasonCode;
    }
    
    @Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb
        .append(amount).append(TO_STRING_SEP)
        .append(installment).append(TO_STRING_SEP)
        .append(reasonCode);
	    return sb.toString();
	}
	
	public Installment getInstallment() {
		return installment;
	}
	public void setInstallment(Installment installment) {
		this.installment = installment;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReasonCode() {
        return reasonCode;
    }
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }


}
