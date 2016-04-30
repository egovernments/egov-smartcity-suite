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
package org.egov.bpa.models.extd;

import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Docket extends BaseModel {
	
	private InspectionExtn inspection;
	private ApplicantStatus statusOfApplicant;
	private String existingUsage;
	private String proposedActivityIsPermissible;
	private String existingSanctionPlanOrPtTaxPaidRecptEnclosed;
	private BigDecimal abuttingRoad_Width;
	private String abuttingRoad_IsPrivateOrPublic;
	private String abuttingRoad_TakenUpForImprovement;
	private String abuttingRoad_gainsAceessThroughPassage;
	private BigDecimal abuttingRoad_gainWidth;
	private String abuttingRoad_gainPrivateOrPublic;
	private String  planCompliesWithSideCondition;
	private String remarks;
	private String aeeInspectionReport;
	private Integer floorCount;
	private BigDecimal lengthOfCompoundWall;
	private BigDecimal diameterOfWell;
	private BigDecimal seperateLatORTank;
	private BigDecimal terraced;
	private BigDecimal tiledRoof;
	private BigDecimal plotWidthRear=new BigDecimal(0);
	private BigDecimal constructionWidthRear=new BigDecimal(0);
	private BigDecimal constructionHeightRear=new BigDecimal(0);
	
	public String getAeeInspectionReport() {
		return aeeInspectionReport;
	}
	public void setAeeInspectionReport(String aeeInspectionReport) {
		this.aeeInspectionReport = aeeInspectionReport;
	}


	private Set <DocketConstructionStage> 	constructionStagesSet = new HashSet<DocketConstructionStage>();
	private Set <DocketViolations> 	violationSet = new HashSet<DocketViolations>();
	private Set <DocketDocumentDetails> 	documentEnclosedSet = new HashSet<DocketDocumentDetails>();
	private Set<DocketFloorDetails>docketFlrDtlsSet = new HashSet<DocketFloorDetails>();
	
	public Set<DocketDocumentDetails> getDocumentEnclosedSet() {
		return documentEnclosedSet;
	}
	public void setDocumentEnclosedSet(
			Set<DocketDocumentDetails> documentEnclosedSet) {
		this.documentEnclosedSet = documentEnclosedSet;
	}
	public String getAbuttingRoad_gainPrivateOrPublic() {
		return abuttingRoad_gainPrivateOrPublic;
	}
	public void setAbuttingRoad_gainPrivateOrPublic(
			String abuttingRoad_gainPrivateOrPublic) {
		this.abuttingRoad_gainPrivateOrPublic = abuttingRoad_gainPrivateOrPublic;
	}
	public Set<DocketConstructionStage> getConstructionStagesSet() {
		return constructionStagesSet;
	}
	public void setConstructionStagesSet(
			Set<DocketConstructionStage> constructionStagesSet) {
		this.constructionStagesSet = constructionStagesSet;
	}
	public Set<DocketViolations> getViolationSet() {
		return violationSet;
	}
	public void setViolationSet(Set<DocketViolations> violationSet) {
		this.violationSet = violationSet;
	}
	public InspectionExtn getInspection() {
		return inspection;
	}
	public void setInspection(InspectionExtn inspection) {
		this.inspection = inspection;
	}
	public ApplicantStatus getStatusOfApplicant() {
		return statusOfApplicant;
	}
	public void setStatusOfApplicant(ApplicantStatus statusOfApplicant) {
		this.statusOfApplicant = statusOfApplicant;
	}
	public String getExistingUsage() {
		return existingUsage;
	}
	public void setExistingUsage(String existingUsage) {
		this.existingUsage = existingUsage;
	}
	public String getProposedActivityIsPermissible() {
		return proposedActivityIsPermissible;
	}
	public void setProposedActivityIsPermissible(
			String proposedActivityIsPermissible) {
		this.proposedActivityIsPermissible = proposedActivityIsPermissible;
	}
	public String getExistingSanctionPlanOrPtTaxPaidRecptEnclosed() {
		return existingSanctionPlanOrPtTaxPaidRecptEnclosed;
	}
	public void setExistingSanctionPlanOrPtTaxPaidRecptEnclosed(
			String existingSanctionPlanOrPtTaxPaidRecptEnclosed) {
		this.existingSanctionPlanOrPtTaxPaidRecptEnclosed = existingSanctionPlanOrPtTaxPaidRecptEnclosed;
	}
	public BigDecimal getAbuttingRoad_Width() {
		return abuttingRoad_Width;
	}
	public void setAbuttingRoad_Width(BigDecimal abuttingRoad_Width) {
		this.abuttingRoad_Width = abuttingRoad_Width;
	}
	public String getAbuttingRoad_IsPrivateOrPublic() {
		return abuttingRoad_IsPrivateOrPublic;
	}
	public void setAbuttingRoad_IsPrivateOrPublic(
			String abuttingRoad_IsPrivateOrPublic) {
		this.abuttingRoad_IsPrivateOrPublic = abuttingRoad_IsPrivateOrPublic;
	}
	public String getAbuttingRoad_TakenUpForImprovement() {
		return abuttingRoad_TakenUpForImprovement;
	}
	public void setAbuttingRoad_TakenUpForImprovement(
			String abuttingRoad_TakenUpForImprovement) {
		this.abuttingRoad_TakenUpForImprovement = abuttingRoad_TakenUpForImprovement;
	}
	public String getAbuttingRoad_gainsAceessThroughPassage() {
		return abuttingRoad_gainsAceessThroughPassage;
	}
	public void setAbuttingRoad_gainsAceessThroughPassage(
			String abuttingRoad_gainsAceessThroughPassage) {
		this.abuttingRoad_gainsAceessThroughPassage = abuttingRoad_gainsAceessThroughPassage;
	}
	public BigDecimal getAbuttingRoad_gainWidth() {
		return abuttingRoad_gainWidth;
	}
	public void setAbuttingRoad_gainWidth(BigDecimal abuttingRoad_gainWidth) {
		this.abuttingRoad_gainWidth = abuttingRoad_gainWidth;
	}
	public String getPlanCompliesWithSideCondition() {
		return planCompliesWithSideCondition;
	}
	public void setPlanCompliesWithSideCondition(
			String planCompliesWithSideCondition) {
		this.planCompliesWithSideCondition = planCompliesWithSideCondition;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	public Integer getFloorCount() {
		return floorCount;
	}
	public void setFloorCount(Integer floorCount) {
		this.floorCount = floorCount;
	}
	public Set<DocketFloorDetails> getDocketFlrDtlsSet() {
		return docketFlrDtlsSet;
	}
	public void setDocketFlrDtlsSet(Set<DocketFloorDetails> docketFlrDtlsSet) {
		this.docketFlrDtlsSet = docketFlrDtlsSet;
	}
	public void addDocketFlrDtls(DocketFloorDetails docketFlrDtls) {
		getDocketFlrDtlsSet().add(docketFlrDtls);
	}

	public void removeDocketFlrDtls(DocketFloorDetails docketFlrDtls) {
		getDocketFlrDtlsSet().remove(docketFlrDtls);
	}
	public BigDecimal getLengthOfCompoundWall() {
		return lengthOfCompoundWall;
	}
	public void setLengthOfCompoundWall(BigDecimal lengthOfCompoundWall) {
		this.lengthOfCompoundWall = lengthOfCompoundWall;
	}
	public BigDecimal getDiameterOfWell() {
		return diameterOfWell;
	}
	public void setDiameterOfWell(BigDecimal diameterOfWell) {
		this.diameterOfWell = diameterOfWell;
	}
	public BigDecimal getSeperateLatORTank() {
		return seperateLatORTank;
	}
	public void setSeperateLatORTank(BigDecimal seperateLatORTank) {
		this.seperateLatORTank = seperateLatORTank;
	}
	
	public BigDecimal getTerraced() {
		return terraced;
	}
	public void setTerraced(BigDecimal terraced) {
		this.terraced = terraced;
	}
	public BigDecimal getTiledRoof() {
		return tiledRoof;
	}
	public void setTiledRoof(BigDecimal tiledRoof) {
		this.tiledRoof = tiledRoof;
	}
	@Override
	public String toString() {
		return "Docket [inspection=" + inspection + ", statusOfApplicant="
				+ statusOfApplicant + ", existingUsage=" + existingUsage
				+ ", proposedActivityIsPermissible="
				+ proposedActivityIsPermissible
				+ ", existingSanctionPlanOrPtTaxPaidRecptEnclosed="
				+ existingSanctionPlanOrPtTaxPaidRecptEnclosed
				+ ", abuttingRoad_Width=" + abuttingRoad_Width
				+ ", abuttingRoad_IsPrivateOrPublic="
				+ abuttingRoad_IsPrivateOrPublic
				+ ", abuttingRoad_TakenUpForImprovement="
				+ abuttingRoad_TakenUpForImprovement
				+ ", abuttingRoad_gainsAceessThroughPassage="
				+ abuttingRoad_gainsAceessThroughPassage
				+ ", abuttingRoad_gainWidth=" + abuttingRoad_gainWidth
				+ ", planCompliesWithSideCondition="
				+ planCompliesWithSideCondition + ", remarks=" + remarks + ",floorCount=" + floorCount + ",lengthOfCompoundWall=" + lengthOfCompoundWall + ",diameterOfWell=" + diameterOfWell + ",seperateLatORTank=" + seperateLatORTank + ",terraced=" + terraced + ",tiledRoof=" + tiledRoof + "]";
	}
	public BigDecimal getPlotWidthRear() {
		return plotWidthRear;
	}
	public void setPlotWidthRear(BigDecimal plotWidthRear) {
		this.plotWidthRear = plotWidthRear;
	}
	public BigDecimal getConstructionWidthRear() {
		return constructionWidthRear;
	}
	public void setConstructionWidthRear(BigDecimal constructionWidthRear) {
		this.constructionWidthRear = constructionWidthRear;
	}
	public BigDecimal getConstructionHeightRear() {
		return constructionHeightRear;
	}
	public void setConstructionHeightRear(BigDecimal constructionHeightRear) {
		this.constructionHeightRear = constructionHeightRear;
	}
	
		
}
