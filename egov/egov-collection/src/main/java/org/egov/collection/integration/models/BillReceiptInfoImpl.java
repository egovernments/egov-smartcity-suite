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
package org.egov.collection.integration.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.collection.entity.ChallanInfo;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.collection.web.constants.CollectionConstants;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.security.terminal.model.Location;
import org.joda.time.DateTime;

/**
 * The bill receipt information class. Provides details of a bill receipt.
 */
public class BillReceiptInfoImpl implements BillReceiptInfo {
	/**
	 * The private receipt header object. This is used by the getters to provide
	 * bill receipt information
	 */
	private final ReceiptHeader receiptHeader;

	/**
	 * Indicates the last event that has occurred on this receipt
	 */
	private String event;

	private final Set<ReceiptAccountInfo> accountDetails = new HashSet<ReceiptAccountInfo>();
	private final Set<ReceiptInstrumentInfo> instrumentDetails = new HashSet<ReceiptInstrumentInfo>();

	/**
	 * Set of bounced instruments of this receipt - Will be created only if
	 * event is InstrumentBounced
	 */
	private final Set<ReceiptInstrumentInfo> bouncedInstruments = new HashSet<ReceiptInstrumentInfo>();
	private final Set<ChallanInfo> challanDetails = new HashSet<ChallanInfo>();
	private ChallanInfo challan = null;
	/**
	 * Billing system invokes this URL to view the receipt
	 */
	private final String receiptURL;
	

	/**
	 * Creates bill receipt information object for given receipt header
	 * 
	 * @param receiptHeader
	 *            the receipt header object
	 */
	public BillReceiptInfoImpl(ReceiptHeader receiptHeader) {
		this.receiptHeader = receiptHeader;
		this.receiptURL = CollectionConstants.RECEIPT_VIEW_SOURCEPATH+receiptHeader.getId();
		
		// Populate set of account info objects using receipt details
		for (ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
			accountDetails.add(new ReceiptAccountInfoImpl(receiptDetail));
		}

		// Populate set of instrument headers that belong to this receipt
		/*TODO: uncomment after adding egf
		 * for (InstrumentHeader instrumentHeader : receiptHeader
				.getReceiptInstrument()) {
			instrumentDetails.add(new ReceiptInstrumentInfoImpl(instrumentHeader));
		}*/
		String receiptStatus = receiptHeader.getStatus().getCode();
		
		if (CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED
				.equals(receiptStatus)) {
			this.event = BillingIntegrationService.EVENT_INSTRUMENT_BOUNCED;
			// find all bounced instruments of this receipt
			findBouncedInstruments();
		} 
		else if (CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED
				.equals(receiptStatus) || 
				CollectionConstants.RECEIPT_STATUS_CODE_APPROVED
				.equals(receiptStatus)) {
			this.event = BillingIntegrationService.EVENT_RECEIPT_CREATED;
		} 
		else if (CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED
				.equals(receiptStatus)) {
			this.event = BillingIntegrationService.EVENT_RECEIPT_CANCELLED;
		}
		
	}
	
	/*public BillReceiptInfoImpl(ReceiptHeader receiptHeader,EgovCommon egovCommon,ReceiptHeader receiptHeaderRefObj) {
		this.receiptHeader = receiptHeader;
		this.receiptURL = CollectionConstants.RECEIPT_VIEW_SOURCEPATH+receiptHeader.getId();

		// Populate set of account info objects using receipt details
		for (ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
			accountDetails.add(new ReceiptAccountInfoImpl(receiptDetail));
		}

		// Populate set of instrument headers that belong to this receipt
		for (InstrumentHeader instrumentHeader : receiptHeader
				.getReceiptInstrument()) {
			instrumentDetails.add(new ReceiptInstrumentInfoImpl(instrumentHeader));
		}
		//String receiptStatus = receiptHeader.getEgwStatus().getCode();
		if(receiptHeader.getReceipttype() == CollectionConstants.RECEIPT_TYPE_CHALLAN){
			challan = new ChallanInfo(receiptHeader,egovCommon,receiptHeaderRefObj);
			challanDetails.add(challan);
		}
	}*/
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb
	    .append(getReceiptNum()).append(" ")
	    .append(getAccountDetails());
	    return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getBillReferenceNum()
	 */
	public String getBillReferenceNum() {
		return receiptHeader.getReferencenumber();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getEvent()
	 */
	public String getEvent() {
		return event;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getReceiptNum()
	 */
	public String getReceiptNum() {
		return receiptHeader.getReceiptnumber();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getReceiptDate()
	 */
	public DateTime getReceiptDate() {
		return receiptHeader.getReceiptdate();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getReceiptLocation()
	 */
	public Location getReceiptLocation() {
		return receiptHeader.getLocation();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getReceiptStatus()
	 */
	public EgwStatus getReceiptStatus() {
		return receiptHeader.getStatus();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getPayeeName()
	 */
	public String getPayeeName() {
		return StringEscapeUtils.unescapeJavaScript(receiptHeader.getPayeeName());
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getPayeeAddress()
	 */
	public String getPayeeAddress() {
		return receiptHeader.getPayeeAddress();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getAccountDetails()
	 */
	public Set<ReceiptAccountInfo> getAccountDetails() {
		return accountDetails;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getInstrumentDetails()
	 */
	public Set<ReceiptInstrumentInfo> getInstrumentDetails() {
		return instrumentDetails;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getBouncedInstruments()
	 */
	public Set<ReceiptInstrumentInfo> getBouncedInstruments() {
		return bouncedInstruments;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getServiceName()
	 */
	public String getServiceName() {
		return receiptHeader.getService().getServiceName();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getPaidBy()
	 */
	public String getPaidBy() {
		return receiptHeader.getPaidBy();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getDescription()
	 */
	public String getDescription() {
		return receiptHeader.getReferenceDesc();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getTotalAmount()
	 */
	public BigDecimal getTotalAmount() {
		return receiptHeader.getAmount();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getCreatedBy()
	 */
	public User getCreatedBy() {
		return receiptHeader.getCreatedBy();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getModifiedBy()
	 */
	public User getModifiedBy() {
		return receiptHeader.getLastModifiedBy();
	}

	/**
	 * Finds all instruments of this receipt that are in bounced (dishonored)
	 * status and adds them to the set of bounced instruments.
	 */
	private void findBouncedInstruments() {
		for (ReceiptInstrumentInfo instrumentInfo : instrumentDetails) {
			if (instrumentInfo.isBounced()) {
				bouncedInstruments.add(instrumentInfo);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getReceiptMisc()
	 */
	public ReceiptMisc getReceiptMisc() {
		return receiptHeader.getReceiptMisc();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getChallanDetails()
	 */
	public Set<ChallanInfo> getChallanDetails() {
		return challanDetails;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getChallan()
	 */
	public ChallanInfo getChallan() {
		return challan;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getReceiptURL()
	 */
	public String getReceiptURL() {
		return receiptURL;
	}
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#getCollectionType()
	 */
	public String getCollectionType() {
		return receiptHeader.getCollectiontype().toString();
	}
	
	public String getConsumerCode(){
		return receiptHeader.getConsumerCode();
	}
}
