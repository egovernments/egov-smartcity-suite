package org.egov.demand.interfaces;

/**
 * @author satyam
 *
 */

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infstr.commons.Module;

/**
 * Any class that needs to generate a Bill must implement the Billable
 * Interface.
 * 
 */
public interface Billable {

	String getBillPayee();

	String getBillAddress();

	String getReferenceNumber();

	EgDemand getCurrentDemand();

	List<EgDemand> getAllDemands();

	EgBillType getBillType();

	Date getBillLastDueDate();

	Integer getBoundaryNum();

	String getBoundaryType();

	String getDepartmentCode();

	BigDecimal getFunctionaryCode();

	String getFundCode();

	String getFundSourceCode();

	Date getIssueDate();

	Date getLastDate();

	Module getModule();

	Boolean getOverrideAccountHeadsAllowed();

	Boolean getPartPaymentAllowed();

	String getServiceCode();

	BigDecimal getTotalAmount();

	Long getUserId();

	String getDescription();

	String getDisplayMessage();

	/**
	 * Comma separated list of payment modes not allowed for the Demand.
	 * 
	 * @return
	 */
	String getCollModesNotAllowed();

	/**
	 * The "consumer code" of the entity being billed - e.g. a property ID in case of property tax.
	 */
	String getPropertyId();

	/**
	 * If apportioning of a payment into the various account heads is to be done by Collections,
	 * this should be FALSE. If the apportioning is done by the billing system (via the 
	 * TaxCollection.apportionPaidAmount() interface), then this should return TRUE.  
	 */
	Boolean isCallbackForApportion();
	
	/**
	 * Typically a billing system will always have one setting for "isCallbackForApportion". In some
	 * cases however, the same billing system needs to have the choice of whether to apportion or
	 * not, depending on the use case. In such cases, this method may be used to alter the value.  
	 */
	void setCallbackForApportion(Boolean b);

}