/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAccountInfoImpl;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfoImpl;
import org.egov.commons.EgwStatus;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.User;
import org.egov.model.instrument.InstrumentHeader;
import org.joda.time.DateTime;

/**
 * The challan information class. Provides details of a challan.
 */
public class ChallanInfo {
	/**
	 * The private receipt header object. This is used by the getters to provide
	 *challan information
	 */
	private ReceiptHeader receiptHeader = null;
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
	public DateTime getChallanDate() {
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
		return receiptHeader.getPayeeName();
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
		return receiptHeader.getPayeeAddress();
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
		return receiptHeader.getService().getName();
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
					null:receipHeaderReferenceObj.getChallan().getService().getName();
		}
		else
		{	
			return receiptHeader.getChallan().getService()==null?
					null:receiptHeader.getChallan().getService().getName();
		}
			
	}
		
}
