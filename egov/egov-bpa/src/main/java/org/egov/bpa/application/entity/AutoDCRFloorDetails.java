/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.bpa.application.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_AUTODCR_FLOORDETAILS")
@SequenceGenerator(name = AutoDCRFloorDetails.SEQ_EGBPA_AUTODCR_FLD, sequenceName = AutoDCRFloorDetails.SEQ_EGBPA_AUTODCR_FLD, allocationSize = 1)
public class AutoDCRFloorDetails extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_EGBPA_AUTODCR_FLD = "SEQ_EGBPA_AUTODCR_FloorDetails";

    @Id
    @GeneratedValue(generator = SEQ_EGBPA_AUTODCR_FLD, strategy = GenerationType.SEQUENCE)
    private Long id;
    @Length(min = 1, max = 128)
    private String floorName;
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "autoDcr", nullable = false)
    private AutoDCR autoDcr;
    private BigDecimal totalCarpetArea;
    private BigDecimal totalBuildUpArea;
    private Long totalSlab; // LetterToParty

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(final String floorName) {
        this.floorName = floorName;
    }

    public BigDecimal getTotalCarpetArea() {
        return totalCarpetArea;
    }

    public void setTotalCarpetArea(final BigDecimal totalCarpetArea) {
        this.totalCarpetArea = totalCarpetArea;
    }

    public BigDecimal getTotalBuildUpArea() {
        return totalBuildUpArea;
    }

    public void setTotalBuildUpArea(final BigDecimal totalBuildUpArea) {
        this.totalBuildUpArea = totalBuildUpArea;
    }

    public Long getTotalSlab() {
        return totalSlab;
    }

    public void setTotalSlab(final Long totalSlab) {
        this.totalSlab = totalSlab;
    }

}
