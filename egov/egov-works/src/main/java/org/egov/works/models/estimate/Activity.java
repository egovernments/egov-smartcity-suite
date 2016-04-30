/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.models.estimate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.egov.common.entity.UOM;
import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.revisionEstimate.RevisionType;

public class Activity extends BaseModel {

    private static final long serialVersionUID = 1276738003131328718L;

    private AbstractEstimate abstractEstimate;

    private ScheduleOfRate schedule;

    @Valid
    private NonSor nonSor;

    private UOM uom;

    @Required(message = "activity.rate.not.null")
    private Money rate = new Money(0.0);

    private Money sorRate = new Money(0.0);

    @Required(message = "activity.quantity.not.null")
    @GreaterThan(value = 0, message = "activity.quantity.non.negative")
    private double quantity;

    @Min(value = 0, message = "activity.servicetax.non.negative")
    private double serviceTaxPerc;

    private double amt;

    private Integer srlNo;

    private RevisionType revisionType;

    private Money sORCurrentRate = new Money(0.0);

    private Activity parent;

    private String signValue;

    public Activity() {
    };

    public Activity(final AbstractEstimate abstractEstimate, final ScheduleOfRate schedule, final Money rate,
            final Double quantity,
            final Double serviceTaxPerc, final NonSor nonSor) {
        super();
        this.abstractEstimate = abstractEstimate;
        this.schedule = schedule;
        this.nonSor = nonSor;
        this.rate = rate;
        this.quantity = quantity;
        this.serviceTaxPerc = serviceTaxPerc;
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

    public Money getRate() {
        return rate;
    }

    public void setRate(final Money rate) {
        this.rate = rate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(final Double quantity) {
        this.quantity = quantity;
    }

    public Double getServiceTaxPerc() {
        return serviceTaxPerc;
    }

    public void setServiceTaxPerc(final Double serviceTaxPerc) {
        this.serviceTaxPerc = serviceTaxPerc;
    }

    public Money getAmount() {
        final double amt = rate.getValue() * quantity;
        return new Money(amt);
    }

    public Money getTaxAmount() {
        return new Money(rate.getValue() * quantity * serviceTaxPerc / 100.0);
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
            final double unitRate = rate == null ? Double.valueOf(0) : rate.getValue();
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
            final double unitRate = rate == null ? Double.valueOf(0) : rate.getValue();
            if (unitRate > 0 && masterRate > 0)
                return unitRate / masterRate;
            else
                return Double.valueOf(1);
        }
    }

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (rate.getValue() <= 0.0)
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

    public Money getSorRate() {
        return sorRate;
    }

    public void setSorRate(final Money sorRate) {
        this.sorRate = sorRate;
    }

}
