package org.egov.dao.billpayment;

import java.util.List;

import org.egov.egf.model.BillPayment.BillPaymentDetails;


public interface BillAndPaymentDetailsDAO {

	BillPaymentDetails getBillAndPaymentDetails(String billNo) throws Exception;

}
