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
package org.egov.works.models.measurementbook;

import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.regex.Constants;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MBDetails extends BaseModel {

    private static final long serialVersionUID = -5088074625605584344L;
    @Required(message = "mbdetails.mbheader.null")
    private MBHeader mbHeader;
    @Required(message = "mbdetails.activity.null")
    private WorkOrderActivity workOrderActivity;
    @GreaterThan(value = 0, message = "mbdetails.quantity.non.negative")
    private double quantity;
    private double rate;
    @Length(max = 400, message = "mbdetails.remark.length")
    private String remarks;

    // ------------------------Fields for calculations---------------------
    private double prevCumlvQuantity;
    private double currCumlvQuantity;
    private double amtForCurrQuantity;
    private double cumlvAmtForCurrCumlvQuantity;
    private Date mbdetailsDate;
    @OptionalPattern(regex = Constants.ALPHANUMERIC_WITHSLASHES, message = "mbdetails.ordernumber")
    private String OrderNumber;
    // -------------------------------------------------------------------
    private double totalEstQuantity; // Added for RE
    private double amount = 0.0;

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (mbHeader != null && (mbHeader.getId() == null || mbHeader.getId() == 0 || mbHeader.getId() == -1))
            validationErrors.add(new ValidationError("mbHeader", "mbdetails.mbheader.null"));
        if (workOrderActivity != null
                && (workOrderActivity.getId() == null || workOrderActivity.getId() == 0 || workOrderActivity.getId() == -1))
            validationErrors.add(new ValidationError("workOrderActivity", "mbdetails.activity.null"));
        return validationErrors;
    }

    public void setMbHeader(final MBHeader mbHeader) {
        this.mbHeader = mbHeader;
    }

    public MBHeader getMbHeader() {
        return mbHeader;
    }

    public void setQuantity(final double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public WorkOrderActivity getWorkOrderActivity() {
        return workOrderActivity;
    }

    public void setWorkOrderActivity(final WorkOrderActivity workOrderActivity) {
        this.workOrderActivity = workOrderActivity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    /**
     * Get Cumulative quantity upto pervious entry
     */
    public double getPrevCumlvQuantity() {
        return prevCumlvQuantity;
    }

    public void setPrevCumlvQuantity(final double prevCumlvQuantity) {
        this.prevCumlvQuantity = prevCumlvQuantity;
    }

    /**
     * Get Cumulative quantity including current entry
     */
    public double getCurrCumlvQuantity() {
        return currCumlvQuantity;
    }

    public void setCurrCumlvQuantity(final double currCumlvQuantity) {
        this.currCumlvQuantity = currCumlvQuantity;
    }

    /**
     * Get Amount for current entry
     */
    public double getAmtForCurrQuantity() {
        return amtForCurrQuantity;
    }

    public void setAmtForCurrQuantity(final double amtForCurrQuantity) {
        this.amtForCurrQuantity = amtForCurrQuantity;
    }

    /**
     * Get Cumulative amount including current entry
     */
    public double getCumlvAmtForCurrCumlvQuantity() {
        return cumlvAmtForCurrCumlvQuantity;
    }

    public void setCumlvAmtForCurrCumlvQuantity(final double cumlvAmtForCurrCumlvQuantity) {
        this.cumlvAmtForCurrCumlvQuantity = cumlvAmtForCurrCumlvQuantity;
    }

    public Date getMbdetailsDate() {
        return mbdetailsDate;
    }

    public void setMbdetailsDate(final Date mbdetailsDate) {
        this.mbdetailsDate = mbdetailsDate;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        OrderNumber = orderNumber;
    }

    public double getTotalEstQuantity() {
        return totalEstQuantity;
    }

    public void setTotalEstQuantity(final double totalEstQuantity) {
        this.totalEstQuantity = totalEstQuantity;
    }

    public double getAmount() {
        if (workOrderActivity.getActivity().getNonSor() == null)
            amount = workOrderActivity.getApprovedRate() * quantity * workOrderActivity.getConversionFactor();
        else
            amount = workOrderActivity.getApprovedRate() * quantity;
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }
}
