package org.egov.ptis.client.integration.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.collection.integration.models.PaymentInfo;
import org.egov.collection.integration.models.PaymentInfo.TYPE;
import org.egov.dcb.bean.ChequePayment;
import org.egov.dcb.bean.CreditCardPayment;
import org.egov.dcb.bean.DDPayment;
import org.junit.Test;

public class CollectionHelperTest {
	public final static String INSTRUMENTNUMBER = "instrumentNumber";
	public final static String INSTRUMENTDATE = "instrumentDate";
	public final static String BANKNAME = "bankName";
	public final static String BRANCHNAME = "branchName";
	public final static String BANKID = "bankId";
	public final static String TRANSACTIONNUMBER = "transactionNumber";
	public final static String CREDITCARDNO = "creditCardNo";
	
	@Test
	public void testChequePaymentObject() {
		CollectionHelper collectionHelper = new CollectionHelper();
		Map<String, String> paymentInfo = buildPaymentInfoObject("1", "01-09-2016", "1", "HSR LAYOUT");

		ChequePayment payment = ChequePayment.create(paymentInfo);

		List<PaymentInfo> paymentInfoList = collectionHelper.preparePaymentInfo(payment);
		assertEquals(paymentInfoList.size(), 1);
	}
	
	@Test
	public void testChequePaymentObjectWithTypeCheque() {
		CollectionHelper collectionHelper = new CollectionHelper();
		Map<String, String> paymentInfo = buildPaymentInfoObject("1", "01-09-2016", "1", "HSR LAYOUT");

		ChequePayment payment = ChequePayment.create(paymentInfo);

		List<PaymentInfo> paymentInfoList = collectionHelper.preparePaymentInfo(payment);
		assertEquals(paymentInfoList.get(0).getInstrumentType(), TYPE.cheque);
	
	}
	
	@Test
	public void testDDPaymentObjectWithTypeDD() {
		CollectionHelper collectionHelper = new CollectionHelper();
		Map<String, String> paymentInfo = buildPaymentInfoObject("1", "01-09-2016", "1", "HSR LAYOUT");

		DDPayment dDPayment = DDPayment.create(paymentInfo);

		List<PaymentInfo> paymentInfoList = collectionHelper.preparePaymentInfo(dDPayment);
		assertEquals(paymentInfoList.get(0).getInstrumentType(), TYPE.dd);
	
	}
/*	@Test
	public void testCreditCardPayment() {
		CollectionHelper collectionHelper = new CollectionHelper();
		Map<String, String> paymentInfo = buildPaymentInfoObject("1", "01-09-2016", "1", "HSR LAYOUT");

		CreditCardPayment dDPayment = CreditCardPayment.create(paymentInfo);

		List<PaymentInfo> paymentInfoList = collectionHelper.preparePaymentInfo(dDPayment);
		assertTrue(paymentInfoList.get(0) instanceof PaymentInfo);		
	}*/
	

	private Map<String, String> buildPaymentInfoObject(String instructementNumber, String instrumentDate, String bankId,
			String branchName) {
		Map<String, String> paymentInfo = new HashMap<String, String>();
		paymentInfo.put(INSTRUMENTNUMBER, instructementNumber);
		paymentInfo.put(INSTRUMENTDATE, instrumentDate);
		paymentInfo.put(BANKID, bankId);
		paymentInfo.put(BRANCHNAME, branchName);
		return paymentInfo;
	}
}
