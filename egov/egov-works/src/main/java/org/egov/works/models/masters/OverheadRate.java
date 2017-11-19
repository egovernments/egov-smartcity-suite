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
package org.egov.works.models.masters;

import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

public class OverheadRate extends BaseModel {

    private static final long serialVersionUID = 5980950787039146268L;

    @Valid
    @Required(message = "validity.period.not.null")
    private Period validity;

    private Overhead overhead;

    @Min(value = 0, message = "overhead.percentage.not.negative")
    private double percentage;

    @Valid
    private Money lumpsumAmount;

    private Long overheadId;

    public Long getOverheadId() {
        return overheadId;
    }

    public void setOverheadId(final Long overheadId) {
        this.overheadId = overheadId;
    }

    public OverheadRate() {

    }

    public OverheadRate(final double percentage, final Money lumpsum) {
        this.percentage = percentage;
        lumpsumAmount = lumpsum;
    }

    public Overhead getOverhead() {
        return overhead;
    }

    public void setOverhead(final Overhead overhead) {
        this.overhead = overhead;
    }

    public Money getLumpsumAmount() {
        return lumpsumAmount;
    }

    public void setLumpsumAmount(final Money lumpsumAmount) {
        this.lumpsumAmount = lumpsumAmount;
    }

    public Period getValidity() {
        return validity;
    }

    public void setValidity(final Period validity) {
        this.validity = validity;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(final double percentage) {
        this.percentage = percentage;
    }

    /**
     * This method validates the overhead rate values. Appropriate validation error is returned in each of the following
     * scenarios:
     * <ol>
     * <li>If percentage is less than zero or greater than 100.</li>
     * <li>If neither of percentage or lump sum amount is present.</li>
     * <li>If start date is not present, or start date date falls after the end date.</li>
     * </ol>
     *
     * @return a list of <code>ValidationError</code> containing the appropriate error messages or null in case of no errors.
     */
    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        if (percentage < 0.0 || percentage > 100)
            validationErrors.add(new ValidationError("percentage", "estimate.overhead.percentage.lessthan.100"));

        if (percentage == 0.0 && (lumpsumAmount == null || lumpsumAmount.getValue() == 0.0))
            validationErrors.add(new ValidationError("percentage", "estimate.overhead.percentage_or_lumpsum_needed"));

        if (percentage > 0.0 && lumpsumAmount != null && lumpsumAmount.getValue() > 0.0)
            validationErrors.add(new ValidationError("percentage",
                    "estimate.overhead.only_one_of_percentage_or_lumpsum_needed"));

        if (validity == null
                || validity != null && !compareDates(validity.getStartDate(), validity.getEndDate()))
            validationErrors.add(new ValidationError("validity", "estimate.overhead.invalid_date_range"));

        if (!validationErrors.isEmpty())
            return validationErrors;
        return null;
    }

    /**
     * compares two date object return type boolean
     */
    public static boolean compareDates(final java.util.Date startDate, final java.util.Date endDate) {
        if (startDate == null)
            return false;

        if (endDate == null)
            return true;

        if (endDate.before(startDate))
            return false;

        return true;
    }
}
