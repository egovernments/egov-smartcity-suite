package org.egov.dcb.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.commons.Installment;
import org.egov.demand.model.EgdmCollectedReceipt;

public class Receipt {
    private static final String REASON_CODE_NA = "N/A";
    private static final String TO_STRING_SEP = "#";

    private String receiptNumber = null;
    private Date receiptDate = null;
    private BigDecimal receiptAmt = null;
    private List<Payment> payments;
    private List<ReceiptDetail> receiptDetails = new ArrayList<ReceiptDetail>();
    private Character receiptStatus = null;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
        .append(receiptNumber).append(TO_STRING_SEP)
        .append(receiptAmt).append(TO_STRING_SEP)
        .append(receiptDetails).append(TO_STRING_SEP)
        .append(receiptStatus);
        return sb.toString();
    } 
    
    /**
     * Creates a receipt bean - note that the receiptDetails are initialised to a single 
     * ReceiptDetail object based on the demand detail in question. This method is only used by the
     * DCB service when enumerating "tuples", and should not be used by other clients. 
     * @param collReceipt
     * @return
     */
    public static Receipt mapFrom(EgdmCollectedReceipt collReceipt) {
        Receipt r = new Receipt();
        r.setReceiptNumber(collReceipt.getReceiptNumber());
        r.setReceiptAmt(collReceipt.getAmount());
        r.setReceiptDate(collReceipt.getReceiptDate());
        r.setReceiptStatus(collReceipt.getStatus());
        r.addReceiptDetail(new ReceiptDetail(
                collReceipt.getEgdemandDetail().getEgDemandReason().getEgInstallmentMaster(),
                collReceipt.getReasonAmount(),
                collReceipt.getEgdemandDetail().getEgDemandReason().getEgDemandReasonMaster().getCode()));
        return r;
    }

    /**
     * Sums the amounts of every receiptDetail of the given installment.
     * 
     * @param i
     * @return
     */
    public BigDecimal getAmountForInstallment(Installment i) {
        BigDecimal amount = BigDecimal.ZERO;
        for (ReceiptDetail detail : receiptDetails) {
            if (i.equals(detail.getInstallment())) {
                amount = amount.add(detail.getAmount());
            }
        }
        return amount;
    }
    
    /**
     * Takes a number of installment-amount pairs and adds a receiptDetail for each. NOTE: the 
     * "reasonCode" field is set to "N/A" - this method should be used when you're only interested in
     * one amount per installment. If there are multiple reasons per installment, then the input
     * breakup should contain cumulative amounts for each installment.
     * 
     * @param breakup
     */
    public void populateDetails(Map<Installment, BigDecimal> breakup) {
        for (Map.Entry<Installment, BigDecimal> pair : breakup.entrySet()) {
            this.addReceiptDetail(new ReceiptDetail(pair.getKey(), pair.getValue(), REASON_CODE_NA));
        }
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
    public BigDecimal getReceiptAmt() {
        return receiptAmt;
    }
    public void setReceiptAmt(BigDecimal receiptAmt) {
        this.receiptAmt = receiptAmt;
    }
    
    public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	
	public List<ReceiptDetail> getReceiptDetails() {
		return receiptDetails;
	}

	public void addReceiptDetail(ReceiptDetail receiptDetail) {
	    this.receiptDetails.add(receiptDetail);
	}

    public void setReceiptDetails(List<ReceiptDetail> receiptDetails) {
        this.receiptDetails = receiptDetails;
    }

    public boolean equals(Object obj) {
    	 if (this == obj) {
             return true;
         }
         Receipt other = (Receipt) obj;
         if (receiptNumber != null && other != null && receiptNumber.equals(other.receiptNumber)) {
             return true;
         }
         return false;
    }
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((receiptNumber == null) ? 0 : receiptNumber.hashCode());
		return result;
	}

	public Character getReceiptStatus() {
		return receiptStatus;
	}

	public void setReceiptStatus(Character receiptStatus) {
		this.receiptStatus = receiptStatus;
	}

}
