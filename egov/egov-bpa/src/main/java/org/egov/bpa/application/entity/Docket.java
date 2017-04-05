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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_DOCKET")
@SequenceGenerator(name = Docket.SEQ_DOCKET, sequenceName = Docket.SEQ_DOCKET, allocationSize = 1)
public class Docket extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_DOCKET = "SEQ_EGBPA_DOCKET";

    @Id
    @GeneratedValue(generator = SEQ_DOCKET, strategy = GenerationType.SEQUENCE)
    private Long id;
    @OneToOne(mappedBy = "docket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Inspection inspection;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private BpaStatus status;
    @Length(min = 1, max = 64)
    private String existingUsage;
    @Length(min = 1, max = 64)
    private String proposedActivityIsPermissible;
    @Length(min = 1, max = 32)
    private String oldProptaxPaidRecptEnclosed;
    @Length(min = 1, max = 32)
    private String existingSanctionPlanOrPtTaxPaidRecptEnclosed;
    private BigDecimal abuttingRoadWidth;
    @Length(min = 1, max = 32)
    private String abuttingRoadIsPrivateOrPublic;
    @Length(min = 1, max = 32)
    private String abuttingRoadTakenUpForImprovement;
    @Length(min = 1, max = 32)
    private String abuttingRoadgainsAceessThroughPassage;
    private BigDecimal abuttingRoadgainWidth;
    @Length(min = 1, max = 32)
    private String abuttingRoadgainPrivateOrPublic;
    @Length(min = 1, max = 32)
    private String planCompliesWithSideCondition;
    @Length(min = 1, max = 512)
    private String remarks;
    @Length(min = 1, max = 512)
    private String aeeInspectionReport;
    private Integer totalFloorCount;
    private BigDecimal lengthOfCompoundWall;
    private BigDecimal diameterOfWell;
    private BigDecimal seperateLatORTank;
    private BigDecimal terraced;
    private BigDecimal tiledRoof;
    private BigDecimal plotWidthRear = new BigDecimal(0);
    private BigDecimal constructionWidthRear = new BigDecimal(0);
    private BigDecimal constructionHeightRear = new BigDecimal(0);

    @OneToMany(mappedBy = "docket", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocketConstructionStage> docketConstructionStage = new ArrayList<DocketConstructionStage>(0);

    @OneToMany(mappedBy = "docket", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocketDetail> docketDetail = new ArrayList<DocketDetail>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(final Inspection inspection) {
        this.inspection = inspection;
    }

    public String getExistingUsage() {
        return existingUsage;
    }

    public void setExistingUsage(final String existingUsage) {
        this.existingUsage = existingUsage;
    }

    public String getProposedActivityIsPermissible() {
        return proposedActivityIsPermissible;
    }

    public void setProposedActivityIsPermissible(final String proposedActivityIsPermissible) {
        this.proposedActivityIsPermissible = proposedActivityIsPermissible;
    }

    public String getOldProptaxPaidRecptEnclosed() {
        return oldProptaxPaidRecptEnclosed;
    }

    public void setOldProptaxPaidRecptEnclosed(final String oldProptaxPaidRecptEnclosed) {
        this.oldProptaxPaidRecptEnclosed = oldProptaxPaidRecptEnclosed;
    }

    public String getExistingSanctionPlanOrPtTaxPaidRecptEnclosed() {
        return existingSanctionPlanOrPtTaxPaidRecptEnclosed;
    }

    public void setExistingSanctionPlanOrPtTaxPaidRecptEnclosed(final String existingSanctionPlanOrPtTaxPaidRecptEnclosed) {
        this.existingSanctionPlanOrPtTaxPaidRecptEnclosed = existingSanctionPlanOrPtTaxPaidRecptEnclosed;
    }

    public BigDecimal getAbuttingRoadWidth() {
        return abuttingRoadWidth;
    }

    public void setAbuttingRoadWidth(final BigDecimal abuttingRoadWidth) {
        this.abuttingRoadWidth = abuttingRoadWidth;
    }

    public String getAbuttingRoadIsPrivateOrPublic() {
        return abuttingRoadIsPrivateOrPublic;
    }

    public void setAbuttingRoadIsPrivateOrPublic(final String abuttingRoadIsPrivateOrPublic) {
        this.abuttingRoadIsPrivateOrPublic = abuttingRoadIsPrivateOrPublic;
    }

    public String getAbuttingRoadTakenUpForImprovement() {
        return abuttingRoadTakenUpForImprovement;
    }

    public void setAbuttingRoadTakenUpForImprovement(final String abuttingRoadTakenUpForImprovement) {
        this.abuttingRoadTakenUpForImprovement = abuttingRoadTakenUpForImprovement;
    }

    public String getAbuttingRoadgainsAceessThroughPassage() {
        return abuttingRoadgainsAceessThroughPassage;
    }

    public void setAbuttingRoadgainsAceessThroughPassage(final String abuttingRoadgainsAceessThroughPassage) {
        this.abuttingRoadgainsAceessThroughPassage = abuttingRoadgainsAceessThroughPassage;
    }

    public BigDecimal getAbuttingRoadgainWidth() {
        return abuttingRoadgainWidth;
    }

    public void setAbuttingRoadgainWidth(final BigDecimal abuttingRoadgainWidth) {
        this.abuttingRoadgainWidth = abuttingRoadgainWidth;
    }

    public String getAbuttingRoadgainPrivateOrPublic() {
        return abuttingRoadgainPrivateOrPublic;
    }

    public void setAbuttingRoadgainPrivateOrPublic(final String abuttingRoadgainPrivateOrPublic) {
        this.abuttingRoadgainPrivateOrPublic = abuttingRoadgainPrivateOrPublic;
    }

    public String getPlanCompliesWithSideCondition() {
        return planCompliesWithSideCondition;
    }

    public void setPlanCompliesWithSideCondition(final String planCompliesWithSideCondition) {
        this.planCompliesWithSideCondition = planCompliesWithSideCondition;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getAeeInspectionReport() {
        return aeeInspectionReport;
    }

    public void setAeeInspectionReport(final String aeeInspectionReport) {
        this.aeeInspectionReport = aeeInspectionReport;
    }

    public Integer getTotalFloorCount() {
        return totalFloorCount;
    }

    public void setTotalFloorCount(final Integer totalFloorCount) {
        this.totalFloorCount = totalFloorCount;
    }

    public BigDecimal getLengthOfCompoundWall() {
        return lengthOfCompoundWall;
    }

    public void setLengthOfCompoundWall(final BigDecimal lengthOfCompoundWall) {
        this.lengthOfCompoundWall = lengthOfCompoundWall;
    }

    public BigDecimal getDiameterOfWell() {
        return diameterOfWell;
    }

    public void setDiameterOfWell(final BigDecimal diameterOfWell) {
        this.diameterOfWell = diameterOfWell;
    }

    public BigDecimal getSeperateLatORTank() {
        return seperateLatORTank;
    }

    public void setSeperateLatORTank(final BigDecimal seperateLatORTank) {
        this.seperateLatORTank = seperateLatORTank;
    }

    public BigDecimal getTerraced() {
        return terraced;
    }

    public void setTerraced(final BigDecimal terraced) {
        this.terraced = terraced;
    }

    public BigDecimal getTiledRoof() {
        return tiledRoof;
    }

    public void setTiledRoof(final BigDecimal tiledRoof) {
        this.tiledRoof = tiledRoof;
    }

    public BigDecimal getPlotWidthRear() {
        return plotWidthRear;
    }

    public void setPlotWidthRear(final BigDecimal plotWidthRear) {
        this.plotWidthRear = plotWidthRear;
    }

    public BigDecimal getConstructionWidthRear() {
        return constructionWidthRear;
    }

    public void setConstructionWidthRear(final BigDecimal constructionWidthRear) {
        this.constructionWidthRear = constructionWidthRear;
    }

    public BigDecimal getConstructionHeightRear() {
        return constructionHeightRear;
    }

    public void setConstructionHeightRear(final BigDecimal constructionHeightRear) {
        this.constructionHeightRear = constructionHeightRear;
    }

    public BpaStatus getStatus() {
        return status;
    }

    public void setStatus(final BpaStatus status) {
        this.status = status;
    }

    public List<DocketConstructionStage> getDocketConstructionStage() {
        return docketConstructionStage;
    }

    public void setDocketConstructionStage(final List<DocketConstructionStage> docketConstructionStage) {
        this.docketConstructionStage = docketConstructionStage;
    }

    public List<DocketDetail> getDocketDetail() {
        return docketDetail;
    }

    public void setDocketDetail(final List<DocketDetail> docketDetail) {
        this.docketDetail = docketDetail;
    }

}