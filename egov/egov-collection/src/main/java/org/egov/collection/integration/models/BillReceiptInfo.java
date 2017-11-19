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
import java.util.Date;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Location;
import org.egov.infra.admin.master.entity.User;

public interface BillReceiptInfo {

    /**
     * @return the reference number
     */
    public abstract String getBillReferenceNum();

    /**
     * @return the event for this bill receipt info
     */
    public abstract String getEvent();

    /**
     * @return the receipt number
     */
    public abstract String getReceiptNum();

    /**
     * @return the receipt date
     */
    public abstract Date getReceiptDate();

    /**
     * @return the receipt location
     */
    public abstract Location getReceiptLocation();

    /**
     * @return the receipt status
     */
    public abstract EgwStatus getReceiptStatus();

    /**
     * @return the payee name
     */
    public abstract String getPayeeName();

    /**
     * @return the payee address
     */
    public abstract String getPayeeAddress();

    /**
     * @return the account details for the bill receipt
     */
    public abstract Set<ReceiptAccountInfo> getAccountDetails();

    /**
     * @return the instrument details for the receipt
     */
    public abstract Set<ReceiptInstrumentInfo> getInstrumentDetails();

    /**
     * @return set of all Bounced Instruments belonging to this receipt
     */
    public abstract Set<ReceiptInstrumentInfo> getBouncedInstruments();

    /**
     * @return The service name for this receipt
     */
    public abstract String getServiceName();

    /**
     * @return Name of person who has paid for this receipt
     */
    public abstract String getPaidBy();

    /**
     * @return The receipt description
     */
    public abstract String getDescription();

    /**
     * @return Total amount of the receipt
     */
    public abstract BigDecimal getTotalAmount();

    /**
     * @return User who has created the receipt
     */
    public abstract User getCreatedBy();

    /**
     * @return User who has last modified the receipt
     */
    public abstract User getModifiedBy();

    /**
     * @return URL to view the receipt
     */
    public abstract String getReceiptURL();

    /**
     * @return the collectiontype
     */
    public abstract String getCollectionType();

    /**
     * @return manual receipt number for back dated receipt
     */
    public abstract String getManualReceiptNumber();

    /**
     * @return manual receipt date for back dated receipt
     */
    public abstract Date getManualReceiptDate();

    /**
     * @return legacy check for billing system
     */
    public abstract Boolean getLegacy();

    /**
     * @return Any additional information to be printed in receipt
     */
    public abstract String getAdditionalInfo();

    /**
     * @return receipt created at source
     */
    public abstract String getSource();

    /**
     * @return receipt instrument type
     */
    public abstract String getReceiptInstrumentType();
}