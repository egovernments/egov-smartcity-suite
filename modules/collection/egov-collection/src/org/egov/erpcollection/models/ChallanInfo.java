package org.egov.erpcollection.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.egf.commons.EgovCommon;
import org.egov.erpcollection.integration.models.ReceiptAccountInfo;
import org.egov.erpcollection.integration.models.ReceiptAccountInfoImpl;
import org.egov.erpcollection.integration.models.ReceiptInstrumentInfo;
import org.egov.erpcollection.integration.models.ReceiptInstrumentInfoImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.model.instrument.InstrumentHeader;

/**
 * The challan information class. Provides details of a challan.
 */
public class ChallanInfo {
	/**
	 * The private receipt header object. This is used by the getters to provide
	 *challan information
	 */
	private final ReceiptHeader receiptHeader;
	private final Set<ReceiptAccountInfo> accountDetails = new HashSet<ReceiptAccountInfo>();
	private final Set<AccountPayeeDetailInfo> accountPayeeDetails = new HashSet<AccountPayeeDetailInfo>();
	private final Set<ReceiptInstrumentInfo> instrumentDetails = new HashSet<ReceiptInstrumentInfo>();
	private ReceiptHeader receipHeaderReferenceObj = new ReceiptHeader();
	
	
	/**
	 * Creates challan information object for given receipt header
	 * 
	 * @param receiptHeader
	 *            the receipt header object
	 */
	public ChallanInfo(ReceiptHeader receiptHeader,EgovCommon egovCommon,ReceiptHeader receiptHeaderRefObj) {
		this.receiptHeader = receiptHeader;
		if(receiptHeaderRefObj!=null && receiptHeaderRefObj.getChallan()!=null)
		{	
				this.receipHeaderReferenceObj = receiptHeaderRefObj;
		}	
		// Populate set of account info objects using receipt details
		for (ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
			
			accountDetails.add(new ReceiptAccountInfoImpl(receiptDetail));
			for (AccountPayeeDetail accountPayeeDetail : receiptDetail.getAccountPayeeDetails()) {
				accountPayeeDetails.add(new AccountPayeeDetailInfo(accountPayeeDetail,egovCommon));
			}
		}
		// Populate set of instrument headers that belong to this receipt
		for (InstrumentHeader instrumentHeader : receiptHeader
				.getReceiptInstrument()) {
			instrumentDetails.add(new ReceiptInstrumentInfoImpl(instrumentHeader));
		}
		
	}
	
	/**
	 * @return the challan number
	 */
	public String getChallanNumber() {
		String challanNumberStr="";
		if(receiptHeader.getChallan()==null)
		{	
			challanNumberStr = receipHeaderReferenceObj.getChallan().getChallanNumber();
		}
		else
		{	
			challanNumberStr = receiptHeader.getChallan().getChallanNumber();
		}
		return challanNumberStr;
	}
	
	/**
	 * @return the challan date
	 */
	public Date getChallanDate() {
		if(receiptHeader.getChallan()==null)
		{	
			return receipHeaderReferenceObj.getChallan().getChallanDate();
		}
		else
		{	
			return receiptHeader.getChallan().getChallanDate();
		}
	}
	
	/**
	 * @return the challan status
	 */
	public EgwStatus getChallanStatus() {
		if(receiptHeader.getChallan()==null)
		{	
			return receipHeaderReferenceObj.getChallan().getStatus();
		}
		else
		{	
			return receiptHeader.getChallan().getStatus();
		}
	}
	
	/**
	 * @return the reference number
	 */
	public String getBillReferenceNum() {
		return receiptHeader.getReferencenumber();
	}
	
	/**
	 * @return the payee name
	 */
	public String getPayeeName() {
		return receiptHeader.getReceiptPayeeDetails().getPayeename();
	}
	
	/**
	 * @return the payee address
	 */
	public String getDescription() {
		return receiptHeader.getReferenceDesc();
	}
	
	/**
	 * @return the narration
	 */
	public String getPayeeAddress() {
		return receiptHeader.getReceiptPayeeDetails().getPayeeAddress();
	}
	/**
	 * @return the account details for the challan
	 */
	public Set<ReceiptAccountInfo> getAccountDetails() {
		return accountDetails;
	}
	
	/**
	 * @return  ReceiptMisc data for the challan
	 */
	public ReceiptMisc getReceiptMisc() {
		return receiptHeader.getReceiptMisc();
	}
	
	/**
	 * @return User who has created the challan
	 */
	public User getCreatedBy() {
		if(receiptHeader.getChallan()==null)
		{	
			return receipHeaderReferenceObj.getChallan().getCreatedBy();
		}
		else
		{	
			return receiptHeader.getChallan().getCreatedBy();
		}
	}

	/**
	 * @return Total amount of the receipt
	 */
	public BigDecimal getTotalAmount() {
		return receiptHeader.getTotalAmount();
	}
	
	/**
	 * @return The service name for this receipt
	 */
	public String getServiceName() {
		return receiptHeader.getService().getServiceName();
	}
	
	/**
	 * @return The function name for this receipt
	 */
	public String getFunctionName() {
		String functionName=null;
		for (ReceiptDetail rDetails : receiptHeader.getReceiptDetails()) {
			if(rDetails.getFunction()!=null){
				functionName= rDetails.getFunction().getName();
			}
		}
		return functionName;
	}
	
	/**
	 * @return the account details for the challan
	 */
	public Set<AccountPayeeDetailInfo> getAccountPayeeDetails() {
		return accountPayeeDetails;
	}
	
	/**
	 * @return the instrument details for the receipt
	 */
	public Set<ReceiptInstrumentInfo> getInstrumentDetails() {
		return instrumentDetails;
	}
	
	public String getChallanVoucherNum(){
		if(receiptHeader.getChallan()==null)
		{	
			return receipHeaderReferenceObj.getChallan().getVoucherHeader()==null?
					null:receipHeaderReferenceObj.getChallan().getVoucherHeader().getVoucherNumber();
		}
		else
		{	
			return receiptHeader.getChallan().getVoucherHeader()==null?
					null:receiptHeader.getChallan().getVoucherHeader().getVoucherNumber();
		}
			
	}
	
	/**
	 * @return The service name for this receipt
	 */
	public String getChallanServiceName() {
		if(receiptHeader.getChallan()==null)
		{	
			return receipHeaderReferenceObj.getChallan().getService()==null?
					null:receipHeaderReferenceObj.getChallan().getService().getServiceName();
		}
		else
		{	
			return receiptHeader.getChallan().getService()==null?
					null:receiptHeader.getChallan().getService().getServiceName();
		}
			
	}
		
}
