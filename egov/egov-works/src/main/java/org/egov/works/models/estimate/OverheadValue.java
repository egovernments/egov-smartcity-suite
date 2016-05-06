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
package org.egov.works.models.estimate;

import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.masters.OverheadRate;
import org.joda.time.LocalDate;

import java.util.Date;

public class OverheadValue extends BaseModel {

    private static final long serialVersionUID = -2562352896664615339L;
    private Overhead overhead;
    private Money amount;
    private AbstractEstimate abstractEstimate;

    public AbstractEstimate getAbstractEstimate() {
        return abstractEstimate;
    }

    public void setAbstractEstimate(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    public OverheadValue() {
    }

    public OverheadValue(final Money amount, final Overhead overhead) {
        super();
        this.amount = amount;
        this.overhead = overhead;
    }

    public Overhead getOverhead() {
        return overhead;
    }

    public void setOverhead(final Overhead overhead) {
        this.overhead = overhead;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(final Money amount) {
        this.amount = amount;
    }

    public OverheadRate getOverheadRateOn(final Date estimateDate) {

        for (final OverheadRate overheadRate : overhead.getOverheadRates())
            if (overheadRate != null && isWithin(overheadRate.getValidity(), estimateDate))
                return overheadRate;

        return null;
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

}
