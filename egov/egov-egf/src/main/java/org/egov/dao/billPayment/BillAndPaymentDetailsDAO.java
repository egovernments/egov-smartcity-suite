package org.egov.dao.billPayment;

import org.egov.egf.model.BillPaymentDetails;

import java.util.List;


public interface BillAndPaymentDetailsDAO {
	List<BillPaymentDetails> getBillAndPaymentDetails(String billNo);
}
