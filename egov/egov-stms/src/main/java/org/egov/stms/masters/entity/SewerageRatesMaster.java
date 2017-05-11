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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.stms.masters.entity.enums.PropertyType;

@Entity
@Table(name = "egswtax_sewerage_rates_master")
@SequenceGenerator(name = SewerageRatesMaster.SEQ_SEWERAGERATESMASTER, sequenceName = SewerageRatesMaster.SEQ_SEWERAGERATESMASTER, allocationSize = 1)
public class SewerageRatesMaster extends AbstractAuditable {

    private static final long serialVersionUID = -4254428973515064704L;

    public static final String SEQ_SEWERAGERATESMASTER = "SEQ_EGSWTAX_SEWERAGE_RATES_MASTER";

    @Id
    @GeneratedValue(generator = SEQ_SEWERAGERATESMASTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @NotNull
    @Min(value = 1)
    private Double monthlyRate;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date fromDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date toDate;

    private boolean active;
    
    @OneToMany(mappedBy = "sewerageratemaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
    private List<SewerageRatesMasterDetails> sewerageDetailmaster = new ArrayList<SewerageRatesMasterDetails>(0);

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

    public Double getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(final Double monthlyRate) {
        this.monthlyRate = monthlyRate;
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

    public List<SewerageRatesMasterDetails> getSewerageDetailmaster() {
        return sewerageDetailmaster;
    }

    public void setSewerageDetailmaster(List<SewerageRatesMasterDetails> sewerageDetailmaster) {
        this.sewerageDetailmaster = sewerageDetailmaster;
    }
    
    public void deleteSewerageRateDetail(final  SewerageRatesMasterDetails sewerageRateDetail) {
        if(this.sewerageDetailmaster!=null)
                this.sewerageDetailmaster.remove(sewerageRateDetail) ;
    }
    public void addSewerageRateDetail(final  SewerageRatesMasterDetails sewerageRateDetail) {
        if(this.sewerageDetailmaster!=null)
        this.sewerageDetailmaster.add(sewerageRateDetail) ;
    }
}