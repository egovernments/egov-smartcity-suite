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
package org.egov.works.abstractestimate.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.common.entity.UOM;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.revisionestimate.entity.enums.RevisionType;

@Entity
@Table(name = "EGW_ESTIMATE_ACTIVITY")
@SequenceGenerator(name = Activity.SEQ_EGW_ESTIMATEACTIVITY, sequenceName = Activity.SEQ_EGW_ESTIMATEACTIVITY, allocationSize = 1)
public class Activity extends AbstractAuditable {

    private static final long serialVersionUID = 8113772958762752328L;

    public static final String SEQ_EGW_ESTIMATEACTIVITY = "SEQ_EGW_ESTIMATE_ACTIVITY";

    @Id
    @GeneratedValue(generator = SEQ_EGW_ESTIMATEACTIVITY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abstractEstimate")
    private AbstractEstimate abstractEstimate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduleofrate")
    private ScheduleOfRate schedule;

    @Valid
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "nonSor")
    private NonSor nonSor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom")
    private UOM uom;

    @NotNull(message = "activity.rate.not.null")
    @Column(name = "unitrate")
    private double rate = 0.0;

    private double sorRate = 0.0;

    @NotNull(message = "activity.quantity.not.null")
    @GreaterThan(value = 0, message = "activity.quantity.non.negative")
    private double quantity;

    @Min(value = 0, message = "activity.servicetax.non.negative")
    private double serviceTaxPerc;

    @Enumerated(EnumType.STRING)
    private RevisionType revisionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Activity parent;

    @Transient
    private Money sORCurrentRate = new Money(0.0);

    @Transient
    private double amt;

    @Transient
    private Integer srlNo;

    @Transient
    private String signValue;

    public Activity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public AbstractEstimate getAbstractEstimate() {
        return abstractEstimate;
    }

    public void setAbstractEstimate(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    public ScheduleOfRate getSchedule() {
        return schedule;
    }

    public void setSchedule(final ScheduleOfRate schedule) {
        this.schedule = schedule;
    }

    public NonSor getNonSor() {
        return nonSor;
    }

    public void setNonSor(final NonSor nonSor) {
        this.nonSor = nonSor;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(final double quantity) {
        this.quantity = quantity;
    }

    public double getServiceTaxPerc() {
        return serviceTaxPerc;
    }

    public void setServiceTaxPerc(final double serviceTaxPerc) {
        this.serviceTaxPerc = serviceTaxPerc;
    }

    public Money getAmount() {
        final double amt = rate * quantity;
        return new Money(amt);
    }

    public Money getTaxAmount() {
        return new Money(rate * quantity * serviceTaxPerc / 100.0);
    }

    public Money getAmountIncludingTax() {
        return new Money(getAmount().getValue() + getTaxAmount().getValue());
    }

    public Money getSORCurrentRate() {
        Money sorCurrentRate = schedule.getRateOn(abstractEstimate.getEstimateDate()).getRate();
        if (sorCurrentRate != null) {
            BigDecimal currentRate = BigDecimal.valueOf(sorCurrentRate.getValue());
            currentRate = currentRate.setScale(2, BigDecimal.ROUND_UP);
            sorCurrentRate = new Money(currentRate.doubleValue());
        }
        return sorCurrentRate;
    }

    public void setSORCurrentRate(final Money sORCurrentRate) {
        this.sORCurrentRate = sORCurrentRate;
    }

    public Money getSORCurrentMarketRate() {
        Money sorCurrentMarketRate = schedule.getMarketRateOn(abstractEstimate.getEstimateDate()).getMarketRate();
        if (sorCurrentMarketRate != null) {
            BigDecimal marketRate = BigDecimal.valueOf(sorCurrentMarketRate.getValue());
            marketRate = marketRate.setScale(2, BigDecimal.ROUND_UP);
            sorCurrentMarketRate = new Money(marketRate.doubleValue());
        }
        return sorCurrentMarketRate;
    }

    public double getConversionFactorForRE(final Date asOnDate) {
        if (revisionType != null && RevisionType.LUMP_SUM_ITEM.equals(revisionType) && schedule == null)
            return Double.valueOf(1);
        if (revisionType != null
                && (RevisionType.ADDITIONAL_QUANTITY.equals(revisionType) || RevisionType.REDUCED_QUANTITY.equals(revisionType))
                && schedule == null)
            return Double.valueOf(1);
        else if (revisionType != null && (RevisionType.NON_TENDERED_ITEM.equals(revisionType) ||
                RevisionType.ADDITIONAL_QUANTITY.equals(revisionType) || RevisionType.REDUCED_QUANTITY.equals(revisionType))
                && schedule != null) {
            final double masterRate = getSORRateForDate(asOnDate) == null ? Double.valueOf(0)
                    : getSORRateForDate(asOnDate).getValue();
            final double unitRate = rate;
            if (unitRate > 0 && masterRate > 0)
                return unitRate / masterRate;
            else
                return Double.valueOf(1);
        } else
            return Double.valueOf(1);
    }

    public Money getSORRateForDate(final Date asOnDate) {
        Money sorCurrentRate = schedule.getRateOn(asOnDate).getRate();
        if (sorCurrentRate != null) {
            BigDecimal currentRate = BigDecimal.valueOf(sorCurrentRate.getValue());
            currentRate = currentRate.setScale(2, BigDecimal.ROUND_UP);
            sorCurrentRate = new Money(currentRate.doubleValue());
        }
        return sorCurrentRate;
    }

    public double getConversionFactor() {
        if (schedule == null)
            return Double.valueOf(1);
        else {
            final double masterRate = getSORCurrentRate() == null ? Double.valueOf(0) : getSORCurrentRate().getValue();
            final double unitRate = rate;
            if (unitRate > 0 && masterRate > 0)
                return unitRate / masterRate;
            else
                return Double.valueOf(1);
        }
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (rate <= 0.0)
            validationErrors.add(new ValidationError("activity.rate.not.null", "activity.rate.not.null"));
        if (nonSor != null && (nonSor.getUom() == null || nonSor.getUom().getId() == null || nonSor.getUom().getId() == 0))
            validationErrors.add(new ValidationError("activity.nonsor.invalid", "activity.nonsor.invalid"));
        return validationErrors;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(final UOM uom) {
        this.uom = uom;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(final double amt) {
        this.amt = amt;
    }

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(final Integer srlNo) {
        this.srlNo = srlNo;
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(final RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    public Activity getParent() {
        return parent;
    }

    public void setParent(final Activity parent) {
        this.parent = parent;
    }

    public String getSignValue() {
        return signValue;
    }

    public void setSignValue(final String signValue) {
        this.signValue = signValue;
    }

    public double getSorRate() {
        return sorRate;
    }

    public void setSorRate(final double sorRate) {
        this.sorRate = sorRate;
    }

}
