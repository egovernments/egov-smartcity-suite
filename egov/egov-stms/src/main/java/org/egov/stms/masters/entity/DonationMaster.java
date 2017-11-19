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
package org.egov.stms.masters.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.stms.masters.entity.enums.PropertyType;

@Entity
@Table(name = "egswtax_donation_master")
@SequenceGenerator(name = DonationMaster.SEQ_DONATIONMASTER, sequenceName = DonationMaster.SEQ_DONATIONMASTER, allocationSize = 1)
public class DonationMaster extends AbstractAuditable {

    private static final long serialVersionUID = 7892490025155232973L;

    public static final String SEQ_DONATIONMASTER = "SEQ_EGSWTAX_DONATION_MASTER";

    @Id
    @GeneratedValue(generator = SEQ_DONATIONMASTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date fromDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date toDate;

    private boolean active;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
    private List<DonationDetailMaster> donationDetail = new ArrayList<DonationDetailMaster>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final PropertyType propertyType) {
        this.propertyType = propertyType;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public List<DonationDetailMaster> getDonationDetail() {
        return donationDetail;
    }

    public void setDonationDetail(final List<DonationDetailMaster> donationDetail) {
        this.donationDetail = donationDetail;
    }
    
    public void deleteDonationDetail(final  DonationDetailMaster donationDetail) {
        if(this.donationDetail!=null)
                this.donationDetail.remove(donationDetail) ;
    }
    public void addDonationDetail(final  DonationDetailMaster donationDetail) {
        if(this.donationDetail!=null)
        this.donationDetail.add(donationDetail) ;
    }
}