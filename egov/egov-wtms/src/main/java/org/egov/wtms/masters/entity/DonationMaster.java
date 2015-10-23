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
package org.egov.wtms.masters.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;

/**
 * @author sushant
 */
@Entity
@Table(name = "egwtr_donation_master")
@SequenceGenerator(name = DonationMaster.SEQ_DONATIONMASTER, sequenceName = DonationMaster.SEQ_DONATIONMASTER, allocationSize = 1)
public class DonationMaster extends AbstractAuditable {

    private static final long serialVersionUID = -2596548687171468023L;
    public static final String SEQ_DONATIONMASTER = "SEQ_EGWTR_DONATION_MASTER";

    @Id
    @GeneratedValue(generator = SEQ_DONATIONMASTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "propertytype", nullable = false)
    private PropertyType propertyType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "usagetype", nullable = false)
    private UsageType usageType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "categorytype", nullable = false)
    private ConnectionCategory categoryType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "maxpipesize", nullable = false)
    private PipeSize maxPipeSize;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "minpipesize", nullable = false)
    private PipeSize minPipeSize;

    @JoinColumn(name = "donationamount", nullable = false)
    private Double donationAmount;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date effectiveDate;

    private boolean active;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(final UsageType usageType) {
        this.usageType = usageType;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public ConnectionCategory getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(final ConnectionCategory categoryType) {
        this.categoryType = categoryType;
    }

    public PipeSize getMaxPipeSize() {
        return maxPipeSize;
    }

    public void setMaxPipeSize(final PipeSize maxPipeSize) {
        this.maxPipeSize = maxPipeSize;
    }

    public PipeSize getMinPipeSize() {
        return minPipeSize;
    }

    public void setMinPipeSize(final PipeSize minPipeSize) {
        this.minPipeSize = minPipeSize;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Double getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(final Double donationAmount) {
        this.donationAmount = donationAmount;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}