/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.demand.interfaces;

/**
 * @author satyam
 */

import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Module;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    Long getBoundaryNum();

    String getBoundaryType();

    String getDepartmentCode();

    BigDecimal getFunctionaryCode();

    String getFundCode();

    String getFundSourceCode();

    String getEmailId();

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

    String getTransanctionReferenceNumber();

    /**
     * Comma separated list of payment modes not allowed for the Demand.
     *
     * @return
     */
    String getCollModesNotAllowed();

    /**
     * The "consumer code" of the entity being billed - e.g. a property ID in case of property tax.
     */
    String getConsumerId();

    String getConsumerType();

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