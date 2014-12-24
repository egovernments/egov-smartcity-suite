/**
 * 
 */
package org.egov.erpcollection.integration.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.erpcollection.integration.models.BillInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.PaymentInfo;

/**
 * Interface exposed by collections system to other systems (typically billing
 * systems)
 */
public interface CollectionIntegrationService {
	/**
	 * Returns the list of bill receipt information objects for given reference
	 * number (typically bill number). One bill can be associated with multiple
	 * receipts in case of part payments.
	 * 
	 * @param serviceCode
	 *            The service code of the billing system
	 * @param refNum 
	 *            The bill reference number
	 * @return The bill receipt information
	 */
	public List<BillReceiptInfo> getBillReceiptInfo(String serviceCode,String refNum);

	/**
	 * Returns the bill receipt information for given set of reference numbers
	 * (typically bill numbers)
	 * 
	 * @param serviceCode
	 *            The service code of the billing system
	 * 
	 * @param refNums
	 *            Set of bill reference numbers
	 * @return Map of bill receipt information objects. Key = bill reference
	 *         number, value = List of Bill receipt information objects
	 */
	public Map<String, List<BillReceiptInfo>> getBillReceiptInfo(
			String serviceCode,Set<String> refNums);

	/**
	 * Returns the bill receipt information for given receipt number
	 * 
	 * @param serviceCode
	 *            The service code of the billing system
	 * @param receiptNum
	 *            The Collections or Manual Receipt Number
	 * @return The bill receipt information
	 */
	public BillReceiptInfo getReceiptInfo(String serviceCode,String receiptNum);

	/**
	 * Returns HashMap of the bill receipt information for given set of receipt
	 * numbers
	 * 
	 * @param serviceCode
	 *            The service code of the billing system
	 * @param receiptNums
	 *            Set of Collections or Manual Receipt Number
	 * @return Map of bill receipt information objects. Key = bill reference
	 *         number, value = Bill receipt information object
	 */
	public Map<String, BillReceiptInfo> getReceiptInfo(
			String serviceCode,Set<String> receiptNums);

	/**
	 * Returns set of bill receipt information for given instrument number. One
	 * instrument might have been used to pay for more than one bills, hence may
	 * belong to more than one receipts.
	 * 
	 * @param serviceCode
	 *            The service code of the billing system
	 * @param instrumentNum
	 *            The instrument number
	 * @return The set of bill receipt information objects for given instrument
	 *         number
	 */
	public List<BillReceiptInfo> getInstrumentReceiptInfo(
			String serviceCode,String instrumentNum);

	/**
	 * Returns the bill receipt information for given set of instrument numbers.
	 * Since one instrument may belong to more than one receipts, this method
	 * will return a map of instrument number to set of bill receipt information
	 * objects.
	 * 
	 * @param serviceCode
	 *            The service code of the billing system
	 * @param instrumentNums
	 *            Set of instrument numbers
	 * @return Map of bill receipt information objects. Key = instrument number,
	 *         value = Set of Bill receipt information objects
	 */
	public Map<String, List<BillReceiptInfo>> getInstrumentReceiptInfo(
			String serviceCode,Set<String> instrumentNums);
	
	/**
	 * This method creates the receipt for the given bill and payment information
	 *  
	 * @param bill an instance of <code>BillInfo</code> containing the bill information.
	 * 
	 * @param paymentInfoList a <code>List</code> of <code>PaymentInfo</code> containing
	 * the payment information. A List is used in order to accommodate payment using 
	 * multiple cheques/DDs for a single receipt. In all other cases, it should be taken 
	 * care that only a single mode of payment is permitted, i.e., the List should contain
	 * a single entry.
	 * 
	 * @return an instance of <code>BillReceiptInfo</code> containing all details of the
	 * created receipt. 
	 */
	public BillReceiptInfo createReceipt(BillInfo bill, List<PaymentInfo> paymentInfoList);
	
	/**
	 * Returns the online pending bill receipt information for given service and consumer code
	 *  
	 * @param serviceCode
	 *            The service code of the billing system
	 * 
	 * @param consumerCode
	 *            The consumer code of the billing system
	 * @return List of Pending Bill receipt information objects
	 */
	public List<BillReceiptInfo> getOnlinePendingReceipts(String serviceCode,String consumerCode);
}
