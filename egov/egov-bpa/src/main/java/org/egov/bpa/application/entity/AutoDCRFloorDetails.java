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
    @JoinColumn(name = "AUTODCRID", nullable = false)
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
