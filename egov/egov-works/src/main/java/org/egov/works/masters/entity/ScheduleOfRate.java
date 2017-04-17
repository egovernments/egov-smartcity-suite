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
package org.egov.works.masters.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.common.entity.UOM;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.utils.StringUtils;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_SCHEDULEOFRATE")
@Unique(fields = { "code" }, enableDfltMsg = true)
@SequenceGenerator(name = ScheduleOfRate.SEQ_EGW_SCHEDULEOFRATE, sequenceName = ScheduleOfRate.SEQ_EGW_SCHEDULEOFRATE, allocationSize = 1)
public class ScheduleOfRate extends AbstractAuditable {
    private static final long serialVersionUID = -7797787370112941401L;

    public static final String SEQ_EGW_SCHEDULEOFRATE = "SEQ_EGW_SCHEDULEOFRATE";
    public static final Integer MAX_DESCRIPTION_LENGTH = 100;

    @Id
    @GeneratedValue(generator = SEQ_EGW_SCHEDULEOFRATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHARWITHOUTSPACE, message = "sor.code.alphaNumeric")
    private String code;

    @Required(message = "sor.category.not.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOR_CATEGORY_ID")
    private ScheduleCategory scheduleCategory;

    @NotNull
    @SafeHtml
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "sor.description.alphaNumeric")
    private String description;

    @Required(message = "sor.uom.not.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UOM_ID")
    private UOM uom;
    
    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "scheduleOfRate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = SORRate.class)
    private List<SORRate> sorRates = new ArrayList<SORRate>(0);

    @OrderBy("id")
    @OneToMany(mappedBy = "scheduleOfRate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MarketRate.class)
    private List<MarketRate> marketRates = new ArrayList<MarketRate>(0);

    @Transient
    private Double sorRateValue;

    @Transient
    private List<SORRate> tempSorRates = new ArrayList<SORRate>(0);

    @Transient
    private List<MarketRate> tempMarketRates = new ArrayList<MarketRate>(0);

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

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

    @JsonIgnore
    public List<SORRate> getSorRatesOrderById() {
        Collections.sort(sorRates, (c1, c2) -> c1.getId().compareTo(c2.getId()));
        Collections.reverse(sorRates);
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

    public String getSorRate() {
        return sorRateValue != null ? sorRateValue.toString() : "";
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
            if (rate != null && isWithin(rate.getValidity(), estimateDate))
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

    }

    public boolean hasValidRateFor(final Date estimateDate) {
        try {
            final SORRate rate = getRateOn(estimateDate);
            return rate != null;
        } catch (final ApplicationRuntimeException e) {
            return false;
        }
    }

    public void setSorRate(final List<SORRate> sorRates) {
        this.sorRates = sorRates;
    }

    public void addSorRate(final SORRate sorRate) {
        sorRates.add(sorRate);
    }

    public List<MarketRate> getMarketRates() {
        return marketRates;
    }

    public void setMarketRates(final List<MarketRate> marketRates) {
        this.marketRates = marketRates;
    }

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

    public void setMarketRate(final List<MarketRate> marketRates) {
        this.marketRates = marketRates;
    }

    public void addMarketRate(final MarketRate marketRate) {
        marketRates.add(marketRate);
    }

    public Double getSorRateValue() {
        return sorRateValue;
    }

    public void setSorRateValue(final Double sorRateValue) {
        this.sorRateValue = sorRateValue;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;

    }

    public List<SORRate> getTempSorRates() {
        return tempSorRates;
    }

    public void setTempSorRates(final List<SORRate> tempSorRates) {
        this.tempSorRates = tempSorRates;
    }

    public List<MarketRate> getTempMarketRates() {
        return tempMarketRates;
    }

    public void setTempMarketRates(final List<MarketRate> tempMarketRates) {
        this.tempMarketRates = tempMarketRates;
    }

}