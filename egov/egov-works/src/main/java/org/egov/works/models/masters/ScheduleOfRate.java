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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.egov.common.entity.UOM;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Unique(fields = { "code" }, id = "id", tableName = "EGW_SCHEDULEOFRATE", columnName = {
        "CODE" }, message = "sor.code.isunique")
public class ScheduleOfRate extends BaseModel {
    private static final long serialVersionUID = -7797787370112941401L;
    private static final Logger logger = Logger.getLogger(ScheduleOfRate.class);
    static Integer MAX_DESCRIPTION_LENGTH = 100;

    @NotEmpty(message = "sor.code.not.empty")
    private String code;
    @Required(message = "sor.category.not.null")
    private ScheduleCategory scheduleCategory;

    @NotEmpty(message = "sor.description.not.empty")
    private String description;
    @Required(message = "sor.uom.not.null")
    private UOM uom;

    public ScheduleOfRate() {
    }

    public ScheduleOfRate(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    private List<SORRate> sorRates = new LinkedList<SORRate>();
    private List<MarketRate> marketRates = new LinkedList<MarketRate>();

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Valid
    public ScheduleCategory getScheduleCategory() {
        return scheduleCategory;
    }

    public void setScheduleCategory(final ScheduleCategory scheduleCategory) {
        this.scheduleCategory = scheduleCategory;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionJS() {
        return StringUtils.escapeJavaScript(description);
    }

    public void setDescription(final String description) {
        this.description = StringEscapeUtils.unescapeHtml(description);
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(final UOM uomid) {
        uom = uomid;
    }

    public List<SORRate> getSorRates() {
        return sorRates;
    }

    public void setSorRates(final List<SORRate> sorRates) {
        this.sorRates = sorRates;
    }

    public String getSummary() {
        if (description.length() <= MAX_DESCRIPTION_LENGTH)
            return description;
        return first(MAX_DESCRIPTION_LENGTH / 2, description) + "..." + last(MAX_DESCRIPTION_LENGTH / 2, description);

    }

    public String getSummaryJS() {
        return StringUtils.escapeJavaScript(getSummary());
    }

    public String getScheduleCategorId() {
        return String.valueOf(scheduleCategory.getId());
    }

    protected String first(final int number, final String description) {
        return description.substring(0, number >= description.length() ? description.length() : number);
    }

    protected String last(final int number, final String description) {
        final int begin = description.length() - number;
        return description.substring(begin < 0 ? description.length() : begin, description.length());
    }

    public String getSearchableData() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getCode()).append(" ").append(getDescription());
        return builder.toString();
    }

    public SORRate getRateOn(final Date estimateDate) {
        if (estimateDate == null)
            throw new ApplicationRuntimeException("no.rate.for.date");
        for (final SORRate rate : sorRates)
            if (isWithin(rate.getValidity(), estimateDate))
                return rate;
        throw new ApplicationRuntimeException("no.rate.for.date");
    }

    public boolean isWithin(final Period period, final Date dateTime) {
        final LocalDate start = new LocalDate(period.getStartDate());
        LocalDate end = null;
        if (period.getEndDate() != null)
            end = new LocalDate(period.getEndDate());
        final LocalDate date = new LocalDate(dateTime);

        if (end == null)
            return start.compareTo(date) <= 0;
        else
            return start.compareTo(date) <= 0 && end.compareTo(date) >= 0;

        // return (end!=null)? start.compareTo(date)<=0 &&
        // end.compareTo(date)>=0 : start.compareTo(date)<=0;

    }

    public boolean hasValidRateFor(final Date estimateDate) {
        try {
            final SORRate rate = getRateOn(estimateDate);
            return rate != null;
        } catch (final ApplicationRuntimeException e) {
            logger.error("Rate :" + e.getMessage());
            return false;
        }
    }

    private List<ValidationError> checkForNoRatePresent() {

        if (sorRates != null && sorRates.isEmpty())
            return Arrays.asList(new ValidationError("sorRate", "sor.rate.altleastone_sorRate_needed"));
        else
            return null;

    }

    private void removeEmptyRates() {
        final List<SORRate> emptyRateObjs = new LinkedList<SORRate>();
        for (final SORRate rat : sorRates)
            if ((rat.getRate() == null || rat.getRate().getValue() == 0.0)
                    && (rat.getValidity() == null || rat.getValidity().getStartDate() == null
                            && rat.getValidity().getEndDate() == null))
                emptyRateObjs.add(rat);
        sorRates.removeAll(emptyRateObjs);
    }

    protected List<ValidationError> validateRates() {
        List<ValidationError> errorList = null;
        boolean openEndedRangeFlag = false;

        for (final SORRate rate : sorRates) {
            if (rate.getValidity().getEndDate() == null && openEndedRangeFlag)
                return Arrays.asList(new ValidationError("openendedrange", "sor.rate.multiple.openendedrange"));
            if (rate.getValidity().getEndDate() == null)
                openEndedRangeFlag = true;

            errorList = rate.validate();

            if (errorList != null)
                return errorList;
        }
        return errorList;
    }

    public void setSorRate(final List<SORRate> sorRates) {
        this.sorRates = sorRates;
    }

    public void addSorRate(final SORRate sorRate) {
        sorRates.add(sorRate);
    }

    private List<ValidationError> validateDateRanges() {
        final List<Period> validDates = new ArrayList<Period>();
        validDates.add(0, sorRates.get(0).getValidity());
        Date existingStartDate = null;
        Date existingEndDate = null;
        Date checkStartDate = null;
        Date checkEndDate = null;
        Period existingPeriod = null;
        Period checkPeriod1 = null;
        boolean flag1 = true;
        int k = 1;

        for (int i = 1; i < sorRates.size(); i++) {
            checkStartDate = sorRates.get(i).getValidity().getStartDate();
            checkEndDate = sorRates.get(i).getValidity().getEndDate();
            checkPeriod1 = new Period(checkStartDate, checkEndDate);

            for (int j = 0; j < validDates.size(); j++) {
                existingStartDate = validDates.get(j).getStartDate();
                existingPeriod = validDates.get(j);

                if (validDates.get(j).getEndDate() == null)
                    existingEndDate = null;
                else
                    existingEndDate = validDates.get(j).getEndDate();

                // check if the period to be checked is within any of the
                // existing periods.
                if (isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate)
                        || checkEndDate != null && isWithin(existingPeriod, checkEndDate) || existingEndDate != null
                                && isWithin(checkPeriod1, existingEndDate)) {
                    flag1 = false;
                    break;
                } else if (checkEndDate != null && existingEndDate != null
                        && (isWithin(existingPeriod, checkEndDate) || isWithin(checkPeriod1, existingEndDate))) {
                    flag1 = false;
                    break;
                }
            }

            if (flag1)
                validDates.add(k++, checkPeriod1);
            else
                return Arrays.asList(new ValidationError("dateoverlap", "sor.rate.dates.overlap"));
        }
        return null;
    }

    /* start market rate */
    /**
     * @return the marketRates
     */
    public List<MarketRate> getMarketRates() {
        return marketRates;
    }

    /**
     * @param marketRates the marketRates to set
     */
    public void setMarketRates(final List<MarketRate> marketRates) {
        this.marketRates = marketRates;
    }

    /* market rate */
    public MarketRate getMarketRateOn(final Date estimateDate) {
        if (estimateDate == null)
            return null;
        for (final MarketRate marketRate : marketRates)
            if (isWithin(marketRate.getValidity(), estimateDate))
                return marketRate;
        return null;
    }

    public boolean hasValidMarketRateFor(final Date estimateDate) {
        final MarketRate marketRate = getMarketRateOn(estimateDate);
        return marketRate != null;
    }

    private void removeEmptyMarketRates() {
        final List<MarketRate> emptyMarketRateObjs = new LinkedList<MarketRate>();
        for (final MarketRate marketRate : marketRates)
            if ((marketRate.getMarketRate() == null || marketRate.getMarketRate().getValue() == 0.0)
                    && (marketRate.getValidity() == null || marketRate.getValidity().getStartDate() == null
                            && marketRate.getValidity().getEndDate() == null))
                emptyMarketRateObjs.add(marketRate);
        marketRates.removeAll(emptyMarketRateObjs);
    }

    protected List<ValidationError> validateMarketRates() {
        List<ValidationError> errorList = null;
        boolean openEndedRangeFlag = false;

        for (final MarketRate marketRate : marketRates)
            if (marketRate != null) {
                if (marketRate.getValidity().getEndDate() == null && openEndedRangeFlag)
                    return Arrays
                            .asList(new ValidationError("openendedrange", "sor.marketrate.multiple.openendedrange"));
                if (marketRate.getValidity().getEndDate() == null)
                    openEndedRangeFlag = true;
                errorList = marketRate.validate();
                if (errorList != null)
                    return errorList;
            }
        return errorList;
    }

    public void setMarketRate(final List<MarketRate> marketRates) {
        this.marketRates = marketRates;
    }

    public void addMarketRate(final MarketRate marketRate) {
        marketRates.add(marketRate);
    }

    private List<ValidationError> validateDateRangesForMarketRate() {
        final List<Period> validDates = new ArrayList<Period>();
        validDates.add(0, marketRates.get(0).getValidity());
        Date existingStartDate = null;
        Date existingEndDate = null;
        Date checkStartDate = null;
        Date checkEndDate = null;
        Period existingPeriod = null;
        Period checkPeriod1 = null;
        boolean flag1 = true;
        int k = 1;

        for (int i = 1; i < marketRates.size(); i++) {
            checkStartDate = marketRates.get(i).getValidity().getStartDate();
            checkEndDate = marketRates.get(i).getValidity().getEndDate();
            checkPeriod1 = new Period(checkStartDate, checkEndDate);

            for (int j = 0; j < validDates.size(); j++) {
                existingStartDate = validDates.get(j).getStartDate();
                existingPeriod = validDates.get(j);
                if (validDates.get(j).getEndDate() == null)
                    existingEndDate = null;
                else
                    existingEndDate = validDates.get(j).getEndDate();

                // check if the period to be checked is within any of the
                // existing periods.
                if (isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate)
                        || checkEndDate != null && isWithin(existingPeriod, checkEndDate) || existingEndDate != null
                                && isWithin(checkPeriod1, existingEndDate)) {
                    flag1 = false;
                    break;
                } else if (checkEndDate != null && existingEndDate != null
                        && (isWithin(existingPeriod, checkEndDate) || isWithin(checkPeriod1, existingEndDate))) {
                    flag1 = false;
                    break;
                }
            }
            if (flag1)
                validDates.add(k++, checkPeriod1);
            else
                return Arrays.asList(new ValidationError("dateoverlap", "sor.marketrate.dates.overlap"));
        }

        return null;
    }

    /* ends market rate */

    @Override
    public List<ValidationError> validate() {
        List<ValidationError> errorList = null;
        removeEmptyRates();
        if (marketRates != null && !marketRates.isEmpty())
            removeEmptyMarketRates();

        if ((errorList = checkForNoRatePresent()) != null)
            return errorList;

        if ((errorList = validateDateRanges()) != null)
            return errorList;

        if ((errorList = validateRates()) != null)
            return errorList;

        /* for market rate */
        if (marketRates != null && !marketRates.isEmpty()) {
            if ((errorList = validateDateRangesForMarketRate()) != null)
                return errorList;
            if ((errorList = validateMarketRates()) != null)
                return errorList;
        }

        return errorList;

    }

}