/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.ptis.bean;

import org.apache.commons.lang3.StringUtils;
import org.egov.ptis.domain.model.OwnerInformation;

import java.util.List;
public class AssessmentInfo {

    private String ulbCode=StringUtils.EMPTY;
    private String oldAssessmentNumber=StringUtils.EMPTY;
    private String assessmentNumber=StringUtils.EMPTY;
    private String propertyType=StringUtils.EMPTY;
    private String category=StringUtils.EMPTY;
    private String exemption=StringUtils.EMPTY;
    private String apartmentCmplx=StringUtils.EMPTY;
    private List<OwnerInformation> ownerDetails;
    private String extentOfSite=StringUtils.EMPTY;
    private String occupancyCertificationNo=StringUtils.EMPTY;
    private String regdDocNo=StringUtils.EMPTY;
    private String regdDocDate=StringUtils.EMPTY;
    private String propertyAddress=StringUtils.EMPTY;
    private String corrAddress=StringUtils.EMPTY;
    private String localityName=StringUtils.EMPTY;
    private String streetName=StringUtils.EMPTY;
    private String electionWardName=StringUtils.EMPTY;
    private String doorNo=StringUtils.EMPTY;
    private String pinCode=StringUtils.EMPTY;
    private String revZone=StringUtils.EMPTY;
    private String revWard=StringUtils.EMPTY;
    private String revBlock=StringUtils.EMPTY;
    private boolean hasLift;
    private boolean hasToilet;
    private boolean hasWaterTap;
    private boolean hasElectricity;
    private boolean hasAttachedBathroom;
    private boolean hasWaterHarvesting;
    private boolean hasCableConnection;
    private String floorType=StringUtils.EMPTY;
    private String roofType=StringUtils.EMPTY;
    private String wallType;
    private String woodType;
    private List<FloorInfo> floorInfo;
    private String surveyNumber=StringUtils.EMPTY;
    private String pattaNumber=StringUtils.EMPTY;
    private Float vacantLandArea=0.0f;
    private Double marketValue=0.0d;
    private Double currentCapitalValue=0.0d;
    private String effectiveDate=StringUtils.EMPTY;
    private String northBoundary=StringUtils.EMPTY;
    private String southBoundary=StringUtils.EMPTY;
    private String eastBoundary=StringUtils.EMPTY;
    private String westBoundary=StringUtils.EMPTY;
    private String vlPlotArea=StringUtils.EMPTY;
    private String laAuthority=StringUtils.EMPTY;
    private String lpNo=StringUtils.EMPTY;
    private String lpDate=StringUtils.EMPTY;
    private String docType=StringUtils.EMPTY;
    private String mroProcNo=StringUtils.EMPTY;
    private String mroProcDate=StringUtils.EMPTY;
    private String courtName=StringUtils.EMPTY;
    private boolean twSigned;

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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExemption() {
        return exemption;
    }

    public void setExemption(String exemption) {
        this.exemption = exemption;
    }

    public String getApartmentCmplx() {
        return apartmentCmplx;
    }

    public void setApartmentCmplx(String apartmentCmplx) {
        this.apartmentCmplx = apartmentCmplx;
    }

    public List<OwnerInformation> getOwnerDetails() {
        return ownerDetails;
    }

    public void setOwnerDetails(List<OwnerInformation> ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

    public String getExtentOfSite() {
        return extentOfSite;
    }

    public void setExtentOfSite(String extentOfSite) {
        this.extentOfSite = extentOfSite;
    }

    public String getOccupancyCertificationNo() {
        return occupancyCertificationNo;
    }

    public void setOccupancyCertificationNo(String occupancyCertificationNo) {
        this.occupancyCertificationNo = occupancyCertificationNo;
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

    public void setRegdDocDate(String regdDocDate) {
        this.regdDocDate = regdDocDate;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getCorrAddress() {
        return corrAddress;
    }

    public void setCorrAddress(String corrAddress) {
        this.corrAddress = corrAddress;
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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getRevZone() {
        return revZone;
    }

    public void setRevZone(String revZone) {
        this.revZone = revZone;
    }

    public String getRevWard() {
        return revWard;
    }

    public void setRevWard(String revWard) {
        this.revWard = revWard;
    }

    public String getRevBlock() {
        return revBlock;
    }

    public void setRevBlock(String revBlock) {
        this.revBlock = revBlock;
    }

    public boolean getHasLift() {
        return hasLift;
    }

    public void setHasLift(boolean hasLift) {
        this.hasLift = hasLift;
    }

    public boolean getHasToilet() {
        return hasToilet;
    }

    public void setHasToilet(boolean hasToilet) {
        this.hasToilet = hasToilet;
    }

    public boolean getHasWaterTap() {
        return hasWaterTap;
    }

    public void setHasWaterTap(boolean hasWaterTap) {
        this.hasWaterTap = hasWaterTap;
    }

    public boolean getHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public boolean getHasAttachedBathroom() {
        return hasAttachedBathroom;
    }

    public void setHasAttachedBathroom(boolean hasAttachedBathroom) {
        this.hasAttachedBathroom = hasAttachedBathroom;
    }

    public boolean getHasWaterHarvesting() {
        return hasWaterHarvesting;
    }

    public void setHasWaterHarvesting(boolean hasWaterHarvesting) {
        this.hasWaterHarvesting = hasWaterHarvesting;
    }

    public boolean getHasCableConnection() {
        return hasCableConnection;
    }

    public void setHasCableConnection(boolean hasCableConnection) {
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

    public List<FloorInfo> getFloorInfo() {
        return floorInfo;
    }

    public void setFloorInfo(List<FloorInfo> floorInfo) {
        this.floorInfo = floorInfo;
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

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
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

    public boolean isTwSigned() {
        return twSigned;
    }

    public void setTwSigned(boolean twSigned) {
        this.twSigned = twSigned;
    }

}
