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
package org.egov.stms.masters.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "egswtax_sewerageratedetail_master")
@SequenceGenerator(name = SewerageRatesMasterDetails.SEQ_SEWERAGERATEDETAILMASTER, sequenceName = SewerageRatesMasterDetails.SEQ_SEWERAGERATEDETAILMASTER, allocationSize = 1)
public class SewerageRatesMasterDetails {
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
    private SewerageRatesMaster sewerageratemaster;

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

    public SewerageRatesMaster getSewerageratemaster() {
        return sewerageratemaster;
    }

    public void setSewerageratemaster(final SewerageRatesMaster sewerageratemaster) {
        this.sewerageratemaster = sewerageratemaster;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SewerageRatesMasterDetails other = (SewerageRatesMasterDetails) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
