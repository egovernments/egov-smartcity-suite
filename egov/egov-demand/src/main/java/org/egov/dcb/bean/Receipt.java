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
package org.egov.dcb.bean;

import org.egov.commons.Installment;
import org.egov.demand.model.EgdmCollectedReceipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Receipt {
    private static final String REASON_CODE_NA = "N/A";
    private static final String TO_STRING_SEP = "#";

    private String receiptNumber = null;
    private Date receiptDate = null;
    private BigDecimal receiptAmt = null;
    private String paymentMode;
    
    private List<Payment> payments;
    private List<ReceiptDetail> receiptDetails = new ArrayList<ReceiptDetail>();
    private Character receiptStatus = null;
    private String consumerCode;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
        .append(receiptNumber).append(TO_STRING_SEP)
        .append(receiptAmt).append(TO_STRING_SEP)
        .append(paymentMode).append(TO_STRING_SEP)
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getConsumerCode() {
		return consumerCode;
	}

	public void setConsumerCode(String consumerCode) {
		this.consumerCode = consumerCode;
	}

}
