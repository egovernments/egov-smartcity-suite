package org.egov.dao.billpayment;

import org.egov.egf.model.BillPaymentDetails;

import java.util.List;


public interface BillAndPaymentDetailsDAO {
	List<BillPaymentDetails> getBillAndPaymentDetails(String billNo);
}
