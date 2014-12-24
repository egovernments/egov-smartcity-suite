package org.egov.erpcollection.integration.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.models.ReceiptDetail;

/**
 * This interface needs to be implemented by any billing application that
 * integrates with the eGov collection system. <br>
 * <br>
 * 
 * For internal applications, the methods can use direct API calls. <br>
 * 
 * For external applications, the integration can be through web-service/REST
 * calls.<br>
 * <br>
 * 
 * The convention to be followed: a bean named
 * "&lt;servicename&gt;CollectionsInterface" needs to be available in the
 * application context. Service name is the 'code' provided for the billing
 * service in <code>ServiceDetails</code> class. <br>
 * <br>
 * 
 * The method <code>updateReceiptDetails</code> will be called by collections
 * system whenever an event occurs that needs to be communicated to the billing
 * system e.g. receipt creation, receipt cancellation or instrument (related to
 * a receipt) bounced. <br>
 * <br>
 * 
 * The <code>BillReceiptInfo</code> object, apart from the receipt information,
 * also provides this event code.
 */
public interface BillingIntegrationService {

	/**
	 * A <code>String</code> indicating the event when the instrument for this
	 * receipt has bounced
	 */
	String EVENT_INSTRUMENT_BOUNCED = "INSTRUMENT_BOUNCED";

	/**
	 * A <code>String</code> indicating the event that the receipt has been
	 * created at the Collections end
	 */
	String EVENT_RECEIPT_CREATED = "RECEIPT_CREATED";

	/**
	 * A <code>String</code> indicating the event that the receipt has been
	 * cancelled at the Collections end
	 */
	String EVENT_RECEIPT_CANCELLED = "RECEIPT_CANCELLED";

	/**
	 * This method needs to be implemented in order to update the billing system
	 * with the bill receipt information
	 * 
	 * @param billReceipts
	 *            a <code>Set</code> of <code>BillReceiptInfo</code> containing
	 *            the bill receipt, receipt account and receipt instrument
	 *            information. The information also includes the event for which
	 *            the billing system needs to be updated.
	 * 
	 * @return a <code>Boolean</code> indicating if the update has taken place
	 *         successfully
	 */
	public Boolean updateReceiptDetails(Set<BillReceiptInfo> billReceipts);
	
	/**
	 * Collection system will invoke this method when the billing system send 
	 * "&lt;enablebillapportioning&gt; as true in the bill-xml.
	 * In case of any exception occurred while apportioning, 
	 * billing system will throw ValidationException.
	 *   
	 * @param billReferenceNumber
	 * 		  Bill Reference Number of the bill send by billing system 	         
	 * @param actualAmountPaid
	 *        Acutal amount paid at the counter
	 * @param receiptDetailsArray
	 * 		  List of ReceiptDetails object associated with this bill
	 * @return void  	       
	 */
	
	public void apportionPaidAmount(String billReferenceNumber, BigDecimal actualAmountPaid, ArrayList<ReceiptDetail> receiptDetailsArray);
	
}
