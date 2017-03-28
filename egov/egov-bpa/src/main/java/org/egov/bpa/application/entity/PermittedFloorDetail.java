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

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_PERMITTED_FLOORDETAILS")
@SequenceGenerator(name = PermittedFloorDetail.SEQ_PERMITTEDFLOORDETAILS, sequenceName = PermittedFloorDetail.SEQ_PERMITTEDFLOORDETAILS, allocationSize = 1)
public class PermittedFloorDetail extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_PERMITTEDFLOORDETAILS = "SEQ_EGBPA_PERMITTED_FLOORDETAILS";

    @Id
    @GeneratedValue(generator = SEQ_PERMITTEDFLOORDETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;
    private Integer floorNumber;
    @Length(min = 1, max = 128)
    private String floorType;
    private Integer block;
    private BigDecimal area;
    private Integer noOfUnits;
    private BigDecimal parking;
    private BigDecimal usagePermitted;
    @ManyToOne(fetch = FetchType.LAZY)
    private ChangeOfUsage usageFrom;
    @ManyToOne(fetch = FetchType.LAZY)
    private ChangeOfUsage usageTo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Integer noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public BigDecimal getParking() {
        return parking;
    }

    public void setParking(BigDecimal parking) {
        this.parking = parking;
    }

    public BigDecimal getUsagePermitted() {
        return usagePermitted;
    }

    public void setUsagePermitted(BigDecimal usagePermitted) {
        this.usagePermitted = usagePermitted;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public ChangeOfUsage getUsageFrom() {
        return usageFrom;
    }

    public void setUsageFrom(ChangeOfUsage usageFrom) {
        this.usageFrom = usageFrom;
    }

    public ChangeOfUsage getUsageTo() {
        return usageTo;
    }

    public void setUsageTo(ChangeOfUsage usageTo) {
        this.usageTo = usageTo;
    }

}