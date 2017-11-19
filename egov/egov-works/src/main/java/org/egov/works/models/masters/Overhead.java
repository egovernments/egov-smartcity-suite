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

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Unique(fields = "name", id = "id", tableName = "EGW_OVERHEAD", columnName = "NAME", message = "overhead.name.isunique")
public class Overhead extends BaseModel {

    private static final long serialVersionUID = 985152668665306509L;
    public static final String BY_DATE_AND_TYPE = "BY_DATE_AND_TYPE";
    public static final String OVERHEADS_BY_DATE = "OVERHEADS_BY_DATE";
    private String name;
    private String description;

    private CChartOfAccounts accountCode;

    private ExpenditureType expenditureType;

    private List<OverheadRate> overheadRates = new LinkedList<OverheadRate>();

    public Overhead() {
    }

    public Overhead(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    @NotEmpty(message = "overhead.name.not.empty")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @NotEmpty(message = "overhead.description.not.empty")
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @NotNull(message = "overhead.account.not.empty")
    public CChartOfAccounts getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(final CChartOfAccounts accountCode) {
        this.accountCode = accountCode;
    }

    @NotNull(message = "overhead.expenditure.not.empty")
    public ExpenditureType getExpenditureType() {
        return expenditureType;
    }

    public void setExpenditureType(final ExpenditureType expenditureType) {
        this.expenditureType = expenditureType;
    }

    public List<OverheadRate> getOverheadRates() {
        return overheadRates;
    }

    public void setOverheadRates(final List<OverheadRate> overheadRates) {
        this.overheadRates = overheadRates;
    }

    /**
     * This method checks if no overhead rates have been entered for the Overhead.
     *
     * @return list of <code>ValidationError</code> indicating that at least one Overhead Rate should be entered for teh Overhead
     */
    private List<ValidationError> checkForNoRatesPresent() {
        if (overheadRates != null && overheadRates.isEmpty())
            return Arrays.asList(new ValidationError("overheadrate",
                    "estimate.overhead.altleastone_overheadrate_needed"));
        else
            return null;
    }

    private List<ValidationError> validateOverheadRates() {
        List<ValidationError> errorList = null;
        boolean openEndedRangeFlag = false;

        for (final OverheadRate overheadRate : overheadRates) {
            // check if multiple open ended date ranges are present
            if (overheadRate.getValidity().getEndDate() == null && openEndedRangeFlag)
                return Arrays.asList(new ValidationError("openendedrange",
                        "estimate.overheadrate.multiple.openendedrange"));
            if (overheadRate.getValidity().getEndDate() == null)
                openEndedRangeFlag = true;

            // validation for percentage-lumpsum amount and invalid date ranges
            errorList = overheadRate.validate();
            if (errorList != null)
                return errorList;
        }
        return errorList;
    }

    private List<ValidationError> validateDateRanges() {
        final List<Period> validDates = new ArrayList<Period>();
        // check for date range over lap
        validDates.add(0, overheadRates.get(0).getValidity());
        Date existingStartDate = null;
        Date checkStartDate = null;
        Date checkEndDate = null;
        Period existingPeriod = null;
        Period checkPeriod1 = null;
        boolean flag1 = true;
        int k = 1;
        for (int i = 1; i < overheadRates.size(); i++) {
            checkStartDate = overheadRates.get(i).getValidity().getStartDate();
            checkEndDate = overheadRates.get(i).getValidity().getEndDate();
            checkPeriod1 = new Period(checkStartDate, checkEndDate);
            for (int j = 0; j < validDates.size(); j++) {
                existingStartDate = validDates.get(j).getStartDate();
                existingPeriod = validDates.get(0);

                // check if the period to be checked is within any of the
                // existing periods.
                if (isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate)) {
                    flag1 = false;
                    break;
                } else if (checkEndDate != null && isWithin(existingPeriod, checkEndDate)) {
                    flag1 = false;
                    break;
                }
            }
            if (flag1)
                validDates.add(k++, checkPeriod1);
            else
                return Arrays.asList(new ValidationError("dateoverlap", "estimate.overhead.dates.overlap"));
        }

        return null;
    }

    /**
     * This method removes any empty over head rate from the list of over head rates.
     */
    private void removeEmptyRates() {
        final List<OverheadRate> emptyRateObjs = new LinkedList<OverheadRate>();

        for (final OverheadRate overheadRate : overheadRates)
            if (overheadRate.getPercentage() == 0.0
                    && (overheadRate.getLumpsumAmount() == null || overheadRate.getLumpsumAmount().getValue() == 0.0)
                    && (overheadRate.getValidity() == null || overheadRate.getValidity().getStartDate() == null || overheadRate
                            .getValidity().getEndDate() == null))
                emptyRateObjs.add(overheadRate);

        overheadRates.removeAll(emptyRateObjs);
    }

    /**
     * This method performs the validations for the over head rates entered by the user.
     */
    @Override
    public List<ValidationError> validate() {
        List<ValidationError> errorList = new ArrayList<ValidationError>();

        removeEmptyRates();
        if ((errorList = checkForNoRatesPresent()) != null)
            return errorList;

        if ((errorList = validateOverheadRates()) != null)
            return errorList;

        if ((errorList = validateDateRanges()) != null)
            return errorList;

        return errorList;

    }

    public boolean isWithin(final Period period, final Date dateTime) {
        final LocalDate start = new LocalDate(period.getStartDate());
        final LocalDate end = new LocalDate(period.getEndDate());
        final LocalDate date = new LocalDate(dateTime);
        if (period.getEndDate() == null)
            return start.compareTo(date) <= 0;
        else
            return start.compareTo(date) <= 0 && end.compareTo(date) >= 0;
    }

    public void setOverheadRate(final List<OverheadRate> overheadRates) {
        this.overheadRates = overheadRates;
    }

    public void addOverheadRate(final OverheadRate overheadRate) {
        overheadRates.add(overheadRate);
    }

    public String getValidPercentage(final Date estimateDate) {
        for (final OverheadRate overheadRate : overheadRates)
            if (overheadRate != null && isWithin(overheadRate.getValidity(), estimateDate)
                    && overheadRate.getPercentage() > 0.0)
                return String.valueOf(overheadRate.getPercentage());

        return "";
    }

    public OverheadRate getOverheadRateOn(final Date estimateDate) {
        if (estimateDate == null)
            throw new ApplicationRuntimeException("no.rate.for.date");

        for (final OverheadRate overheadRate : overheadRates)
            if (overheadRate != null && isWithin(overheadRate.getValidity(), estimateDate))
                return overheadRate;

        return null;
    }
}
