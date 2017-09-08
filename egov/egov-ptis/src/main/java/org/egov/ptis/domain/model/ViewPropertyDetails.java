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
package org.egov.ptis.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class ViewPropertyDetails {

    private String ulbCode;
    private String oldAssessmentNumber;
    private String assessmentNumber;
    private String propertyTypeMaster;
    private String category;
    private String exemption;
    private String apartmentCmplx;
    private BigDecimal arv;
    private List<OwnerInformation> ownerDetails;
    private String mutationReason;
    private String parentPropertyAssessmentNo;
    private String extentOfSite;
    private Boolean isExtentAppurtenantLand;
    private String occupancyCertificationNo;
    private String extentAppartenauntLand;
    private String regdDocNo;
    private String regdDocDate;
    private String propertyAddress;
    private String corrAddress;
    private String localityName;
    private String streetName;
    private String electionWardName;
    private String doorNo;
    private String enumerationBlockName;
    private String pinCode;
    private String zoneName;
    private String wardName;
    private String blockName;
    private Boolean isCorrAddrDiff = false;
    private String corrAddr1;
    private String corrAddr2;
    private String corrPinCode;
    private Boolean hasLift;
    private Boolean hasToilet;
    private Boolean hasWaterTap;
    private Boolean hasElectricity;
    private Boolean hasAttachedBathroom;
    private Boolean hasWaterHarvesting;
    private Boolean hasCableConnection;
    private String floorType;
    private String roofType;
    private String wallType;
    private String woodType;
    private List<FloorDetails> floorDetails;
    private String surveyNumber;
    private String pattaNumber;
    private Float vacantLandArea;
    private Double marketValue;
    private Double currentCapitalValue;
    private String effectiveDate;
    private String northBoundary;
    private String southBoundary;
    private String eastBoundary;
    private String westBoundary;
    private String vlPlotArea;
    private String laAuthority;
    private String lpNo;
    private String lpDate;
    private String docType;
    private String mroProcNo;
    private String mroProcDate;
    private String courtName;
    private boolean twSigned;
    
    private Boolean floorDetailsEntered = false;
    private String propertyDepartment;
    

	public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getOldAssessmentNumber() {
        return oldAssessmentNumber;
    }

    public void setOldAssessmentNumber(String oldAssessmentNumber) {
        this.oldAssessmentNumber = oldAssessmentNumber;
    }

    public String getAssessmentNumber() {
        return assessmentNumber;
    }

    public void setAssessmentNumber(String assessmentNumber) {
        this.assessmentNumber = assessmentNumber;
    }

    public String getPropertyTypeMaster() {
        return propertyTypeMaster;
    }

    public void setPropertyTypeMaster(String propertyTypeMaster) {
        this.propertyTypeMaster = propertyTypeMaster;
    }

    public String getExemption() {
        return exemption;
    }

    public void setExemption(String exemption) {
        this.exemption = exemption;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getApartmentCmplx() {
        return apartmentCmplx;
    }

    public void setApartmentCmplx(String apartmentCmplx) {
        this.apartmentCmplx = apartmentCmplx;
    }

    public BigDecimal getArv() {
        return arv;
    }

    public void setArv(BigDecimal arv) {
        this.arv = arv;
    }

    public List<OwnerInformation> getOwnerDetails() {
        return ownerDetails;
    }

    public void setOwnerDetails(List<OwnerInformation> ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

    public String getMutationReason() {
        return mutationReason;
    }

    public void setMutationReason(String mutationReason) {
        this.mutationReason = mutationReason;
    }

    public String getParentPropertyAssessmentNo() {
        return parentPropertyAssessmentNo;
    }

    public void setParentPropertyAssessmentNo(String parentPropertyAssessmentNo) {
        this.parentPropertyAssessmentNo = parentPropertyAssessmentNo;
    }

    public String getExtentOfSite() {
        return extentOfSite;
    }

    public void setExtentOfSite(String extentOfSite) {
        this.extentOfSite = extentOfSite;
    }

    public Boolean getIsExtentAppurtenantLand() {
        return isExtentAppurtenantLand;
    }

    public void setIsExtentAppurtenantLand(Boolean isExtentAppurtenantLand) {
        this.isExtentAppurtenantLand = isExtentAppurtenantLand;
    }

    public String getOccupancyCertificationNo() {
        return occupancyCertificationNo;
    }

    public void setOccupancyCertificationNo(String occupancyCertificationNo) {
        this.occupancyCertificationNo = occupancyCertificationNo;
    }

    public String getExtentAppartenauntLand() {
        return extentAppartenauntLand;
    }

    public void setExtentAppartenauntLand(String extentAppartenauntLand) {
        this.extentAppartenauntLand = extentAppartenauntLand;
    }

    public String getRegdDocNo() {
        return regdDocNo;
    }

    public void setRegdDocNo(String regdDocNo) {
        this.regdDocNo = regdDocNo;
    }

    public String getRegdDocDate() {
        return regdDocDate;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public void setRegdDocDate(String regdDocDate) {
        this.regdDocDate = regdDocDate;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getElectionWardName() {
        return electionWardName;
    }

    public void setElectionWardName(String electionWardName) {
        this.electionWardName = electionWardName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getEnumerationBlockName() {
        return enumerationBlockName;
    }

    public void setEnumerationBlockName(String enumerationBlockName) {
        this.enumerationBlockName = enumerationBlockName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Boolean getIsCorrAddrDiff() {
        return isCorrAddrDiff;
    }

    public void setIsCorrAddrDiff(Boolean isCorrAddrDiff) {
        this.isCorrAddrDiff = isCorrAddrDiff;
    }

    public String getCorrAddr1() {
        return corrAddr1;
    }

    public void setCorrAddr1(String corrAddr1) {
        this.corrAddr1 = corrAddr1;
    }

    public String getCorrAddr2() {
        return corrAddr2;
    }

    public void setCorrAddr2(String corrAddr2) {
        this.corrAddr2 = corrAddr2;
    }

    public String getCorrPinCode() {
        return corrPinCode;
    }

    public void setCorrPinCode(String corrPinCode) {
        this.corrPinCode = corrPinCode;
    }

    public Boolean getHasLift() {
        return hasLift;
    }

    public void setHasLift(Boolean hasLift) {
        this.hasLift = hasLift;
    }

    public Boolean getHasToilet() {
        return hasToilet;
    }

    public void setHasToilet(Boolean hasToilet) {
        this.hasToilet = hasToilet;
    }

    public Boolean getHasWaterTap() {
        return hasWaterTap;
    }

    public void setHasWaterTap(Boolean hasWaterTap) {
        this.hasWaterTap = hasWaterTap;
    }

    public Boolean getHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(Boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public Boolean getHasAttachedBathroom() {
        return hasAttachedBathroom;
    }

    public void setHasAttachedBathroom(Boolean hasAttachedBathroom) {
        this.hasAttachedBathroom = hasAttachedBathroom;
    }

    public Boolean getHasWaterHarvesting() {
        return hasWaterHarvesting;
    }

    public void setHasWaterHarvesting(Boolean hasWaterHarvesting) {
        this.hasWaterHarvesting = hasWaterHarvesting;
    }

    public Boolean getHasCableConnection() {
        return hasCableConnection;
    }

    public void setHasCableConnection(Boolean hasCableConnection) {
        this.hasCableConnection = hasCableConnection;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
    }

    public String getRoofType() {
        return roofType;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }

    public String getWallType() {
        return wallType;
    }

    public void setWallType(String wallType) {
        this.wallType = wallType;
    }

    public String getWoodType() {
        return woodType;
    }

    public void setWoodType(String woodType) {
        this.woodType = woodType;
    }

    public List<FloorDetails> getFloorDetails() {
        return floorDetails;
    }

    public void setFloorDetails(List<FloorDetails> floorDetails) {
        this.floorDetails = floorDetails;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getCorrAddress() {
        return corrAddress;
    }

    public void setCorrAddress(String corrAddress) {
        this.corrAddress = corrAddress;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getMroProcNo() {
        return mroProcNo;
    }

    public void setMroProcNo(String mroProcNo) {
        this.mroProcNo = mroProcNo;
    }

    public String getMroProcDate() {
        return mroProcDate;
    }

    public void setMroProcDate(String mroProcDate) {
        this.mroProcDate = mroProcDate;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public boolean getTwSigned() {
        return twSigned;
    }

    public void setTwSigned(boolean twSigned) {
        this.twSigned = twSigned;
    }

    public String getSurveyNumber() {
        return surveyNumber;
    }

    public void setSurveyNumber(String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    public String getPattaNumber() {
        return pattaNumber;
    }

    public void setPattaNumber(String pattaNumber) {
        this.pattaNumber = pattaNumber;
    }

    public Float getVacantLandArea() {
        return vacantLandArea;
    }

    public void setVacantLandArea(Float vacantLandArea) {
        this.vacantLandArea = vacantLandArea;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    public Double getCurrentCapitalValue() {
        return currentCapitalValue;
    }

    public void setCurrentCapitalValue(Double currentCapitalValue) {
        this.currentCapitalValue = currentCapitalValue;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(String northBoundary) {
        this.northBoundary = northBoundary;
    }

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(String southBoundary) {
        this.southBoundary = southBoundary;
    }

    public String getEastBoundary() {
        return eastBoundary;
    }

    public void setEastBoundary(String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }

    public String getWestBoundary() {
        return westBoundary;
    }

    public void setWestBoundary(String westBoundary) {
        this.westBoundary = westBoundary;
    }

    public String getVlPlotArea() {
        return vlPlotArea;
    }

    public void setVlPlotArea(String vlPlotArea) {
        this.vlPlotArea = vlPlotArea;
    }

    public String getLaAuthority() {
        return laAuthority;
    }

    public void setLaAuthority(String laAuthority) {
        this.laAuthority = laAuthority;
    }

    public String getLpNo() {
        return lpNo;
    }

    public void setLpNo(String lpNo) {
        this.lpNo = lpNo;
    }

    public String getLpDate() {
        return lpDate;
    }

    public void setLpDate(String lpDate) {
        this.lpDate = lpDate;
    }
    
    public Boolean getFloorDetailsEntered() {
		return floorDetailsEntered;
	}

	public void setFloorDetailsEntered(Boolean floorDetailsEntered) {
		this.floorDetailsEntered = floorDetailsEntered;
	}

	public String getPropertyDepartment() {
		return propertyDepartment;
	}

	public void setPropertyDepartment(String propertyDepartment) {
		this.propertyDepartment = propertyDepartment;
	}

}
