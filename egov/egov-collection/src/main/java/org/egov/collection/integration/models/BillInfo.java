/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.collection.integration.models;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents the information send by the billing system including the payee
 * information, MIS information, collection modes allowed for the payment
 *
 * @author Rishi
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */

public interface BillInfo {

    /**
     * Enum for Collection Type C - Counter Collection F - Field Collection O -
     * Online Collection
     *
     * @author rishi
     */
    public enum COLLECTIONTYPE {
        C, F, O
    };

    /**
     * This method returns the service code
     *
     * @return String as Service Code
     */
    String getServiceCode();

    /**
     * This method returns the name of the person who made the payment
     *
     * @return String representing the name of the person who made the payment
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
    BigDecimal getFunctionaryCode();

    /**
     * This method returns Fund Source Code
     *
     * @return String as Fund Source Code
     */
    String getFundSourceCode();

    /**
     * This method returns Department Code
     *
     * @return String as Department Code
     */
    String getDepartmentCode();

    /**
     * This method return display message
     *
     * @return String as Display Message
     */
    String getDisplayMessage();

    /**
     * This method returns True if Part Payment Allowed else return False
     *
     * @return Boolean as Part Payment Allowed
     */
    Boolean getPartPaymentAllowed();

    /**
     * This method returns True if Account Overriding is allowed else return
     * False
     *
     * @return Boolean as Override Account Head Allowed
     */
    Boolean getOverrideAccountHeadsAllowed();

    /**
     * This method returns True if the billing system should do the amount
     * apportioning
     *
     * @return Boolean as Call Back For Apportioning
     */
    Boolean getCallbackForApportioning();

    /**
     * This method return list of collection modes not allowed for this bill,
     * i.e., cash/cheque/dd/bank/online/card
     *
     * @return List of Collection Modes not allowed for bill
     */
    List<String> getCollectionModesNotAllowed();

    /**
     * This method return list of bill payee details where in each can have
     * multiple bills associated.
     *
     * @return List of Bill Payee Details
     */
    List<BillPayeeDetails> getPayees();

    /**
     * This method sets the given list of Bill Payees
     *
     * @param List
     *            containing Bill Payee Details
     */
    void setPayees(List<BillPayeeDetails> payees);

    /**
     * This method adds the given bill payee to the list of payees.
     *
     * @param Bill
     *            Payee object
     */
    void addPayees(BillPayeeDetails payee);
    
    /**
     * This method returns the Collection Type
     * 
     * @return Collection Type as String
     */
    COLLECTIONTYPE getCollectionType();
    
    /**
     * Used Only for Integration. 
     * This method returns the Transaction Reference Number
     *  
     * @return Transaction Number as String
     */
    String getTransactionReferenceNumber();
    
    /**
     * This method returns the source of Receipt
     * Source value is null for the receipts created by ERP.
     * @return
     */
    String getSource();

}