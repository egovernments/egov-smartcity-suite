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

package org.egov.wtms.masters.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "egwtr_metered_rates_detail")
@SequenceGenerator(name = MeteredRatesDetail.SEQ_METERED_RATES_DETAIL, sequenceName = MeteredRatesDetail.SEQ_METERED_RATES_DETAIL, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate")

})
public class MeteredRatesDetail extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String SEQ_METERED_RATES_DETAIL = "SEQ_EGWTR_METERED_RATES_DETAIL";

    @Id
    @GeneratedValue(generator = SEQ_METERED_RATES_DETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meteredrate", nullable = false)
    @Audited
    private MeteredRates meteredRate;

    @Audited
    private Double rateAmount;

    @Audited
    private Double flatAmount;

    @Audited
    private boolean recursive;

    @Audited
    private Double recursiveFactor;

    @Audited
    private Double recursiveAmount;

    @Temporal(value = TemporalType.DATE)
    @Audited
    private Date fromDate;

    @Temporal(value = TemporalType.DATE)
    @Audited
    private Date toDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Double getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(final Double rateAmount) {
        this.rateAmount = rateAmount;
    }

    public Double getFlatAmount() {
        return flatAmount;
    }

    public void setFlatAmount(final Double flatAmount) {
        this.flatAmount = flatAmount;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(final boolean recursive) {
        this.recursive = recursive;
    }

    public Double getRecursiveFactor() {
        return recursiveFactor;
    }

    public void setRecursiveFactor(final Double recursiveFactor) {
        this.recursiveFactor = recursiveFactor;
    }

    public Double getRecursiveAmount() {
        return recursiveAmount;
    }

    public void setRecursiveAmount(final Double recursiveAmount) {
        this.recursiveAmount = recursiveAmount;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public MeteredRates getMeteredRate() {
        return meteredRate;
    }

    public void setMeteredRate(final MeteredRates meteredRate) {
        this.meteredRate = meteredRate;
    }

}
