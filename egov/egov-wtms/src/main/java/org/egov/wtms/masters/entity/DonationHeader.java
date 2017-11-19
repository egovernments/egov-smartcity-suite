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

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "egwtr_donation_header")
@SequenceGenerator(name = DonationHeader.SEQ_DONATIONHEADER, sequenceName = DonationHeader.SEQ_DONATIONHEADER, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class DonationHeader extends AbstractAuditable {

    private static final long serialVersionUID = 4583091947098722880L;
    public static final String SEQ_DONATIONHEADER = "SEQ_EGWTR_DONATION_HEADER";

    @Id
    @GeneratedValue(generator = SEQ_DONATIONHEADER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "category", nullable = false)
    @Audited
    private ConnectionCategory category;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "usagetype", nullable = false)
    @Audited
    private UsageType usageType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "propertytype", nullable = false)
    @Audited
    private PropertyType propertyType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "minpipesize", nullable = false)
    @Audited
    private PipeSize minPipeSize;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "maxpipesize", nullable = false)
    @Audited
    private PipeSize maxPipeSize;

    @Audited
    private boolean active;

    @OneToMany(mappedBy = "donationHeader", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DonationDetails> donationDetails = new HashSet<DonationDetails>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public ConnectionCategory getCategory() {
        return category;
    }

    public void setCategory(final ConnectionCategory category) {
        this.category = category;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(final UsageType usageType) {
        this.usageType = usageType;
    }

    public PipeSize getMinPipeSize() {
        return minPipeSize;
    }

    public void setMinPipeSize(final PipeSize minPipeSize) {
        this.minPipeSize = minPipeSize;
    }

    public PipeSize getMaxPipeSize() {
        return maxPipeSize;
    }

    public void setMaxPipeSize(final PipeSize maxPipeSize) {
        this.maxPipeSize = maxPipeSize;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Set<DonationDetails> getDonationDetails() {
        return donationDetails;
    }

    public void setDonationDetails(final Set<DonationDetails> donationDetails) {
        this.donationDetails = donationDetails;
    }

    public void addDonationDetails(final DonationDetails donationDetail) {
        donationDetails.add(donationDetail);
    }

}