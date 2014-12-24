package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents the information send by the billing system including the payee information, MIS  information, 
 * collection modes allowed for the payment 
 *  
 * @author Rishi
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */


public interface BillInfo {
	
	/**
	 * Enum for Collection Type
	 * C - Counter Collection
	 * F - Field Collection
	 * O - Online Collection
	 * @author rishi
	 *
	 */
	public enum COLLECTIONTYPE {C, F, O};
	
	/**
	 * This method returns the service code
	 * 
	 * @return String as Service Code
	 */
	String getServiceCode();
	
	/**
	 * This method returns the name of the person who 
	 * made the payment
	 * 
	 * @return String representing the  name of the person who 
	 * made the payment
	 */
	String getPaidBy();
	
	/**
	 * This method returns Fund Code
	 * 
	 * @return String as Fund Code
	 */
	String getFundCode(); 
	
	/**
	 * This method returns Functionary Code
	 * 
	 * @return String as Functionary Code
	 */
	BigDecimal getFunctionaryCode() ;

	/**
	 * This method returns Fund Source Code
	 * 
	 * @return String as Fund Source Code
	 */
	String getFundSourceCode() ;

	/**
	 * This method returns Department Code
	 * 
	 * @return String as Department Code
	 */
	String getDepartmentCode() ;

	/**
	 * This method return display message
	 * 
	 * @return String as Display Message 
	 */
	String getDisplayMessage() ;
	

	/**
	 * This method returns True if Part Payment Allowed else return False
	 * 
	 * @return Boolean as Part Payment Allowed
	 */
	Boolean getPartPaymentAllowed() ;

	/**
	 * This method returns True if Account Overriding is allowed else return False 
	 * 
	 * @return Boolean as Override Account Head Allowed
	 */
	Boolean getOverrideAccountHeadsAllowed() ;
	
	/**
	 * This method returns True if the billing system should do the amount apportioning
	 * 
	 * @return Boolean as Call Back For Apportioning
	 */
	Boolean getCallbackForApportioning();

	/**
	 * This method return list of collection modes not allowed for this bill, i.e., cash/cheque/dd/bank/online/card
	 * 
	 * @return List of Collection Modes not allowed for bill
	 */
	List<String> getCollectionModesNotAllowed() ;

	/**
	 * This method return list of bill payee details where in each can have multiple bills associated.  
	 * 
	 * @return List of Bill Payee Details
	 */
	List<BillPayeeDetails> getPayees() ;

	/**
	 * This method sets the given list of Bill Payees
	 * 
	 * @param List containing Bill Payee Details
	 * 				 
	 */
	void setPayees(List<BillPayeeDetails> payees) ;

	/**
	 * This method adds the given bill payee to the list of payees.
	 * 
	 * @param Bill Payee object
	 */
	void addPayees(BillPayeeDetails payee) ;
	
}