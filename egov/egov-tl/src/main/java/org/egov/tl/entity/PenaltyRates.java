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
package org.egov.tl.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static org.egov.tl.entity.PenaltyRates.SEQ_PENALTYRATES;

@Entity
@Table(name = "egtl_penaltyrates")
@SequenceGenerator(name = SEQ_PENALTYRATES, sequenceName = SEQ_PENALTYRATES, allocationSize = 1)
public class PenaltyRates extends AbstractAuditable {

    protected static final String SEQ_PENALTYRATES = "SEQ_EGTL_PENALTYRATES";
    private static final long serialVersionUID = 1329581042965327280L;

    @Id
    @GeneratedValue(generator = SEQ_PENALTYRATES, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Long fromRange;

    @NotNull
    private Long toRange;

    @Min(0)
    private Double rate;

    @ManyToOne
    @JoinColumn(name = "licenseAppType", updatable = false)
    @NotNull
    private LicenseAppType licenseAppType;

    @Transient
    private boolean markedForRemoval;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromRange() {
        return fromRange;
    }

    public void setFromRange(Long fromRange) {
        this.fromRange = fromRange;
    }

    public Long getToRange() {
        return toRange;
    }

    public void setToRange(Long toRange) {
        this.toRange = toRange;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public LicenseAppType getLicenseAppType() {
        return licenseAppType;
    }

    public void setLicenseAppType(LicenseAppType licenseAppType) {
        this.licenseAppType = licenseAppType;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }
}
