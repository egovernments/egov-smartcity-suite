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
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "EGBPA_APPLICATION_FLOORDETAILS")
@SequenceGenerator(name = FloorDetail.SEQ_EGBPA_REGN_FLOORDETAILS, sequenceName = FloorDetail.SEQ_EGBPA_REGN_FLOORDETAILS, allocationSize = 1)
public class FloorDetail extends AbstractAuditable {

    private static final long serialVersionUID = 1L;
    public static final String SEQ_EGBPA_REGN_FLOORDETAILS = "SEQ_EGBPA_APPLICATION_FLOORDETAILS";
    @Id
    @GeneratedValue(generator = SEQ_EGBPA_REGN_FLOORDETAILS, strategy = GenerationType.SEQUENCE)

    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private BuildingDetail buildingDetailid;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inspection")
    private Inspection inspection; // need to refer InspectionObject
    private BigDecimal existingbuildingArea;
    private BigDecimal proposedBuildingArea;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "existingbuildingUsage")
    private BuildingUsage existingbuildingUsage;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proposedbuilingusage")
    private BuildingUsage proposedbuilingusage;
    private Integer floorNumber;
    private BigDecimal carpetArea;
    private BigDecimal plinthArea;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public BuildingDetail getBuildingDetailid() {
        return buildingDetailid;
    }

    public void setBuildingDetailid(final BuildingDetail buildingDetailid) {
        this.buildingDetailid = buildingDetailid;
    }

    public Inspection getInspectionid() {
        return inspection;
    }

    public void setInspectionid(final Inspection inspectionid) {
        this.inspection = inspectionid;
    }

    public BigDecimal getExistingbuildingArea() {
        return existingbuildingArea;
    }

    public void setExistingbuildingArea(final BigDecimal existingbuildingArea) {
        this.existingbuildingArea = existingbuildingArea;
    }

    public BigDecimal getProposedBuildingArea() {
        return proposedBuildingArea;
    }

    public void setProposedBuildingArea(final BigDecimal proposedBuildingArea) {
        this.proposedBuildingArea = proposedBuildingArea;
    }

    public BuildingUsage getExistingbuildingUsage() {
        return existingbuildingUsage;
    }

    public void setExistingbuildingUsage(final BuildingUsage existingbuildingUsage) {
        this.existingbuildingUsage = existingbuildingUsage;
    }

    public BuildingUsage getProposedbuilingusage() {
        return proposedbuilingusage;
    }

    public void setProposedbuilingusage(final BuildingUsage proposedbuilingusage) {
        this.proposedbuilingusage = proposedbuilingusage;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(final Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public BigDecimal getCarpetArea() {
        return carpetArea;
    }

    public void setCarpetArea(final BigDecimal carpetArea) {
        this.carpetArea = carpetArea;
    }

    public BigDecimal getPlinthArea() {
        return plinthArea;
    }

    public void setPlinthArea(final BigDecimal plinthArea) {
        this.plinthArea = plinthArea;
    }

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

}
