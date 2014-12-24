package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;

public class EgdmCollectedReceipt implements Cloneable { 
	public static final Character RCPT_CANCEL_STATUS = 'C';
	private Long id;
	private String receiptNumber;
	private Date receiptDate;
	private BigDecimal amount;
	private BigDecimal reasonAmount;
	private Character status;
	private Date updatedTime; 
	private EgDemandDetails egdemandDetail;

	public String toString() {
		return receiptNumber;
	}
	
	/**
	 * Returns a copy that can be associated with another EgDemandDetails. The copy has the same 
	 * receipt number, date, amount, status and time stamp. (Note: making it public instead of
     * protected to allow any class to use it.)
	 */
	@Override
	public Object clone() {
	    EgdmCollectedReceipt clone = null;
	    try {
	        clone = (EgdmCollectedReceipt) super.clone();
	    } catch (CloneNotSupportedException e) {
            // this should never happen
            throw new InternalError(e.toString());
        }
	    clone.setId(null);
	    clone.setEgdemandDetail(null);
	    return clone;
	}
	
	public EgDemandDetails getEgdemandDetail() {
		return egdemandDetail;
	}
	public void setEgdemandDetail(EgDemandDetails egdemandDetail) {
		this.egdemandDetail = egdemandDetail;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
	public Date getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

    public BigDecimal getReasonAmount() {
        return reasonAmount;
    }

    public void setReasonAmount(BigDecimal reasonAmount) {
        this.reasonAmount = reasonAmount;
    }

	public Boolean isCancelled() {
		Boolean cancelStatus = Boolean.FALSE;
		if (getStatus().equals(RCPT_CANCEL_STATUS)) {
			cancelStatus = Boolean.TRUE;
		}
		return cancelStatus;
	}
	
}
