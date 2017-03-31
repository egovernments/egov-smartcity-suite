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
package org.egov.bpa.application.entity;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_MSTR_BPAFEEDETAIL")
@SequenceGenerator(name = BpaFeeDetail.SEQ_BPAFEEDETAIL, sequenceName = BpaFeeDetail.SEQ_BPAFEEDETAIL, allocationSize = 1)
public class BpaFeeDetail extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_BPAFEEDETAIL = "SEQ_EGBPA_MSTR_BPAFEEDETAIL";
    @Id
    @GeneratedValue(generator = SEQ_BPAFEEDETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "bpafee")
    private BpaFee bpafee;
    private double fromAreasqmt;
    private double toAreasqmt;
    @NotNull
    private double amount;
    @Transient
    private Long srlNo;
    @Length(min = 1, max = 128)
    private String subType;
    @Length(min = 1, max = 128)
    private String landUseZone;
    private Long floorNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usageType")
    private LandBuildingTypes usageType;
    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    @Length(min = 1, max = 128)
    private String additionalType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public BpaFee getBpafee() {
        return bpafee;
    }

    public double getFromAreasqmt() {
        return fromAreasqmt;
    }

    public double getToAreasqmt() {
        return toAreasqmt;
    }

    public double getAmount() {
        return amount;
    }

    public Long getSrlNo() {
        return srlNo;
    }

    public String getSubType() {
        return subType;
    }

    public String getLandUseZone() {
        return landUseZone;
    }

    public Long getFloorNumber() {
        return floorNumber;
    }

    public LandBuildingTypes getUsageType() {
        return usageType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getAdditionalType() {
        return additionalType;
    }

    public void setBpafee(final BpaFee bpafee) {
        this.bpafee = bpafee;
    }

    public void setFromAreasqmt(final double fromAreasqmt) {
        this.fromAreasqmt = fromAreasqmt;
    }

    public void setToAreasqmt(final double toAreasqmt) {
        this.toAreasqmt = toAreasqmt;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public void setSrlNo(final Long srlNo) {
        this.srlNo = srlNo;
    }

    public void setSubType(final String subType) {
        this.subType = subType;
    }

    public void setLandUseZone(final String landUseZone) {
        this.landUseZone = landUseZone;
    }

    public void setFloorNumber(final Long floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setUsageType(final LandBuildingTypes usageType) {
        this.usageType = usageType;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public void setAdditionalType(final String additionalType) {
        this.additionalType = additionalType;
    }
}