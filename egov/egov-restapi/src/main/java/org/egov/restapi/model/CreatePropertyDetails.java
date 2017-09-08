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

package org.egov.restapi.model;

import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.model.OwnerInformation;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class CreatePropertyDetails implements Serializable {
	
	private String ulbCode;
	private String assessmentNumber;
	private String propertyTypeMasterCode;
	private String propertyDepartment;
	private String categoryCode;
	private String apartmentCmplxCode;
	private Boolean appurtenantLandChecked=false;
	private List<OwnerInformation> ownerDetails;
	private AssessmentsDetails assessmentDetails;
	private PropertyAddressDetails propertyAddressDetails;
	private AmenitiesDetails amenitiesDetails;
	private ConstructionTypeDetails constructionTypeDetails;
	private List<FloorDetails> floorDetails;
	private Boolean floorDetailsEntered=false;
	private VacantLandDetails vacantLandDetails;
	private SurroundingBoundaryDetails surroundingBoundaryDetails;
	private DocumentTypeDetails  documentTypeDetails;
	private String assessmentNo;
	
	@Override
	public String toString() {
		return "CreatePropertyDetails [propertyTypeMasterCode=" + propertyTypeMasterCode + ", categoryCode=" + categoryCode 
				+ ", apartmentCmplxCode=" + apartmentCmplxCode + ", ownerDetails=" + ownerDetails + ", assessmentDetails=" + assessmentDetails
				+ ", propertyAddressDetails=" + propertyAddressDetails + ", amenitiesDetails=" + amenitiesDetails
				+ ", constructionTypeDetails=" + constructionTypeDetails + ", floorDetails=" + floorDetails
				+ ", vacantLandDetails=" + vacantLandDetails + ", surroundingBoundaryDetails="
				+ surroundingBoundaryDetails + ", documentTypeDetails="
						+ documentTypeDetails + "]";
	}
	

	
	public String getPropertyTypeMasterCode() {
		return propertyTypeMasterCode;
	}
	public void setPropertyTypeMasterCode(String propertyTypeMasterCode) {
		this.propertyTypeMasterCode = propertyTypeMasterCode;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getApartmentCmplxCode() {
		return apartmentCmplxCode;
	}
	public void setApartmentCmplxCode(String apartmentCmplxCode) {
		this.apartmentCmplxCode = apartmentCmplxCode;
	}
	public List<OwnerInformation> getOwnerDetails() {
		return ownerDetails;
	}
	public void setOwnerDetails(List<OwnerInformation> ownerDetails) {
		this.ownerDetails = ownerDetails;
	}
	public AssessmentsDetails getAssessmentDetails() {
		return assessmentDetails;
	}
	public void setAssessmentDetails(AssessmentsDetails assessmentDetails) {
		this.assessmentDetails = assessmentDetails;
	}
	public PropertyAddressDetails getPropertyAddressDetails() {
		return propertyAddressDetails;
	}
	public void setPropertyAddressDetails(PropertyAddressDetails propertyAddressDetails) {
		this.propertyAddressDetails = propertyAddressDetails;
	}
	public AmenitiesDetails getAmenitiesDetails() {
		return amenitiesDetails;
	}
	public void setAmenitiesDetails(AmenitiesDetails amenitiesDetails) {
		this.amenitiesDetails = amenitiesDetails;
	}
	public ConstructionTypeDetails getConstructionTypeDetails() {
		return constructionTypeDetails;
	}
	public void setConstructionTypeDetails(ConstructionTypeDetails constructionTypeDetails) {
		this.constructionTypeDetails = constructionTypeDetails;
	}
	public List<FloorDetails> getFloorDetails() {
		return floorDetails;
	}
	public void setFloorDetails(List<FloorDetails> floorDetails) {
		this.floorDetails = floorDetails;
	}
	public VacantLandDetails getVacantLandDetails() {
		return vacantLandDetails;
	}
	public void setVacantLandDetails(VacantLandDetails vacantLandDetails) {
		this.vacantLandDetails = vacantLandDetails;
	}
	public SurroundingBoundaryDetails getSurroundingBoundaryDetails() {
		return surroundingBoundaryDetails;
	}
	public void setSurroundingBoundaryDetails(SurroundingBoundaryDetails surroundingBoundaryDetails) {
		this.surroundingBoundaryDetails = surroundingBoundaryDetails;
	}
	public String getUlbCode() {
		return ulbCode;
	}
	public void setUlbCode(String ulbCode) {
		this.ulbCode = ulbCode;
	}

	public String getAssessmentNo() {
		return assessmentNo;
	}

	public void setAssessmentNo(String assessmentNo) {
		this.assessmentNo = assessmentNo;
	}
	
	public String getPropertyDepartment() {
		return propertyDepartment;
	}


	public void setPropertyDepartment(String propertyDepartment) {
		this.propertyDepartment = propertyDepartment;
	}


	public DocumentTypeDetails getDocumentTypeDetails() {
		return documentTypeDetails;
	}


	public void setDocumentTypeDetails(DocumentTypeDetails documentTypeDetails) {
		this.documentTypeDetails = documentTypeDetails;
	}
	
	public Boolean getFloorDetailsEntered() {
		return floorDetailsEntered;
	}

	public void setFloorDetailsEntered(Boolean floorDetailsEntered) {
		this.floorDetailsEntered = floorDetailsEntered;
	}



	public boolean isAppurtenantLandChecked() {
		return appurtenantLandChecked;
	}



	public void setAppurtenantLandChecked(Boolean appurtenantLandChecked) {
		this.appurtenantLandChecked = appurtenantLandChecked;
	}



	public String getAssessmentNumber() {
		return assessmentNumber;
	}



	public void setAssessmentNumber(String assessmentNumber) {
		this.assessmentNumber = assessmentNumber;
	}

}
