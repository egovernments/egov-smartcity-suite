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

import org.egov.infra.persistence.entity.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static org.egov.stms.masters.entity.SewerageRatesMasterDetails.SEQ_SEWERAGERATEDETAILMASTER;

@Entity
@Table(name = "egswtax_sewerageratedetail_master")
@SequenceGenerator(name = SEQ_SEWERAGERATEDETAILMASTER, sequenceName = SEQ_SEWERAGERATEDETAILMASTER, allocationSize = 1)
public class SewerageRatesMasterDetails extends AbstractPersistable<Long> {
    public static final String SEQ_SEWERAGERATEDETAILMASTER = "SEQ_EGSWTAX_SEWERAGERATEDETAIL_MASTER";

    @Id
    @GeneratedValue(generator = SEQ_SEWERAGERATEDETAILMASTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer noOfClosets;

    @NotNull
    @Min(value = 1)
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "sewerageratemaster", nullable = false)
    private SewerageRatesMaster sewerageRateMaster;

    @Transient
    private boolean markedForRemoval;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getNoOfClosets() {
        return noOfClosets;
    }

    public void setNoOfClosets(final Integer noOfClosets) {
        this.noOfClosets = noOfClosets;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public SewerageRatesMaster getSewerageRateMaster() {
        return sewerageRateMaster;
    }

    public void setSewerageRateMaster(SewerageRatesMaster sewerageRateMaster) {
        this.sewerageRateMaster = sewerageRateMaster;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SewerageRatesMasterDetails that = (SewerageRatesMasterDetails) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sewerageRateMaster, that.sewerageRateMaster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sewerageRateMaster);
    }
}
