	
package org.egov.erpcollection.integration.pgi;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.egov.EGOVRuntimeException;
import org.junit.Before;
import org.junit.Test;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment service.
 * 
 */

public class BillDeskAdaptorTest {
	
	BillDeskAdaptor billDeskAdaptor;
	
	@Before
	public void setupAction(){
		billDeskAdaptor= new BillDeskAdaptor();
	}
	
	private Date convertToDate(String strDate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
		Date date = null;
			date = sdf.parse(strDate);
		return date;
	}
	
	@Test
	public void parsePaymentResponse() throws ParseException{
		
		String response="MerchantID|CustomerID|TxnReferenceNo|BankReferenceNo|1000.0|BankID|"+
		"BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"+
		"21-09-2009|0300|SettlementType|1001|AdditionalInfo2-ReceiptAmt|AdditionalInfo3-ReceiptRefNo|AdditionalInfo4-PaytServiceCode|"+
		"AdditionalInfo5-OnlinePaytServiceCode|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription|2954574939";
		
		PaymentResponse paytResponse = billDeskAdaptor.parsePaymentResponse(response);
		
		assertEquals(paytResponse.getTxnReferenceNo(),"TxnReferenceNo");
		assertEquals(paytResponse.getTxnAmount(),Double.valueOf(1000.0));
		assertEquals(paytResponse.getAuthStatus(),"0300");
		assertEquals(paytResponse.getReceiptId(),"1001");
		assertEquals(paytResponse.getChecksum(),"2954574939");
		assertEquals(paytResponse.getTxnDate(),convertToDate("21-09-2009"));
	}
	
	@Test
	public void parsePaymentResponseWithInvalidDate() {
		
		String response="MerchantID|CustomerID|TxnReferenceNo|BankReferenceNo|1000.0|BankID|"+
		"BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"+
		"21/09/2009|0300|SettlementType|1001|AdditionalInfo2-ReceiptAmt|AdditionalInfo3-ReceiptRefNo|AdditionalInfo4-PaytServiceCode|"+
		"AdditionalInfo5-OnlinePaytServiceCode|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription|1773210064";
		
		try {
			billDeskAdaptor.parsePaymentResponse(response);
		} catch (EGOVRuntimeException e) {
			assertEquals(e.getMessage(),"AdditionalInfo5-OnlinePaytServiceCode.pgi.AdditionalInfo4-PaytServiceCode.transactiondate.parse.error");
		}
	}
	
	@Test
	public void parsePaymentResponseWithInvalidChecksum() {
		
		String response="MerchantID|CustomerID|TxnReferenceNo|BankReferenceNo|1000.0|BankID|"+
		"BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"+
		"21-09-2009|0300|SettlementType|1001|AdditionalInfo2-ReceiptAmt|AdditionalInfo3-ReceiptRefNo|AdditionalInfo4-PaytServiceCode|"+
		"AdditionalInfo5-OnlinePaytServiceCode|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription|1134208796";
		
		try {
			billDeskAdaptor.parsePaymentResponse(response);
		} catch (EGOVRuntimeException e) {
			assertEquals(e.getMessage(),"AdditionalInfo5-OnlinePaytServiceCode.pgi.AdditionalInfo4-PaytServiceCode.checksum.mismatch");
		}
	}
	
}
