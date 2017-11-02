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
package org.egov.restapi.web.rest;

import java.io.IOException;
import java.text.ParseException;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.dcb.bean.ChequePayment;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.NewPropertyDetails;
import org.egov.ptis.domain.model.ViewPropertyDetails;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.restapi.model.AmenitiesDetails;
import org.egov.restapi.model.AssessmentsDetails;
import org.egov.restapi.model.ConstructionTypeDetails;
import org.egov.restapi.model.CreatePropertyDetails;
import org.egov.restapi.model.SurroundingBoundaryDetails;
import org.egov.restapi.model.VacantLandDetails;
import org.egov.restapi.util.JsonConvertor;
import org.egov.restapi.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UpdateAssessmentController {
	
	private static final String NO_APPROVAL = "No Approval";

	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
    private PropertyExternalService propertyExternalService;
	
	@Autowired
	private LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepo;
	
	/**
     * This method is used to modify property.
     * 
     * @param createPropertyDetails - Property details request
     * @return
     * @throws IOException
     * @throws ParseException
     */
	@RequestMapping(value = "/property/updateProperty", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public String updateProperty(@RequestBody String createPropertyDetails)
			throws IOException, ParseException {
		String responseJson;
		ApplicationThreadLocals.setUserId(2L);
		CreatePropertyDetails createPropDetails = (CreatePropertyDetails) getObjectFromJSONRequest(createPropertyDetails, CreatePropertyDetails.class);
		
		ErrorDetails errorDetails = validationUtil.validateUpdateRequest(createPropDetails, PropertyTaxConstants.PROPERTY_MODE_MODIFY);
		if (errorDetails != null && errorDetails.getErrorCode() != null) {
			responseJson = JsonConvertor.convert(errorDetails);
	    } else {
	     	ViewPropertyDetails viewPropertyDetails = setRequestParameters(createPropDetails);
	     	NewPropertyDetails newPropertyDetails = propertyExternalService.updateProperty(viewPropertyDetails);
	        responseJson = JsonConvertor.convert(newPropertyDetails);
	    }
        return responseJson;
	}
	
	/**
	 * Prepares the ViewPropertyDetails bean for modification
	 * @param createPropDetails
	 * @return
	 */
	public ViewPropertyDetails setRequestParameters(CreatePropertyDetails createPropDetails){
    	ViewPropertyDetails viewPropertyDetails = new ViewPropertyDetails();
    	viewPropertyDetails.setAssessmentNumber(createPropDetails.getAssessmentNumber());
    	viewPropertyDetails.setPropertyTypeMaster(createPropDetails.getPropertyTypeMasterCode());
    	viewPropertyDetails.setCategory(createPropDetails.getCategoryCode());
    	
    	AssessmentsDetails assessmentDetails = createPropDetails.getAssessmentDetails();
		if (assessmentDetails != null) {
			viewPropertyDetails.setMutationReason(assessmentDetails.getMutationReasonCode());
			viewPropertyDetails.setExtentOfSite(assessmentDetails.getExtentOfSite());
			viewPropertyDetails.setOccupancyCertificationNo(assessmentDetails.getOccupancyCertificationNo());
			viewPropertyDetails.setOccupancyCertificationDate(assessmentDetails.getOccupancyCertificationDate());
			viewPropertyDetails.setExtentAppartenauntLand(assessmentDetails.getExtentAppartenauntLand());
		}
    	//Amenities Details
		AmenitiesDetails amenities = createPropDetails.getAmenitiesDetails();
		if (amenities != null) {
			viewPropertyDetails.setHasLift(amenities.hasLift());
			viewPropertyDetails.setHasToilet(amenities.hasToilet());
			viewPropertyDetails.setHasWaterTap(amenities.hasWaterTap());
			viewPropertyDetails.setHasElectricity(amenities.hasElectricity());
			viewPropertyDetails.setHasAttachedBathroom(amenities.hasAttachedBathroom());
			viewPropertyDetails.setHasWaterHarvesting(amenities.hasWaterHarvesting());
			viewPropertyDetails.setHasCableConnection(amenities.hasCableConnection());
		} else {
			viewPropertyDetails.setHasLift(false);
			viewPropertyDetails.setHasToilet(false);
			viewPropertyDetails.setHasWaterTap(false);
			viewPropertyDetails.setHasElectricity(false);
			viewPropertyDetails.setHasAttachedBathroom(false);
			viewPropertyDetails.setHasWaterHarvesting(false);
			viewPropertyDetails.setHasCableConnection(false);
		}
    	//Construction Type Details
		ConstructionTypeDetails constructionTypeDetails = createPropDetails.getConstructionTypeDetails();
		if (constructionTypeDetails != null) {
			viewPropertyDetails.setFloorType(constructionTypeDetails.getFloorTypeId());
			viewPropertyDetails.setRoofType(constructionTypeDetails.getRoofTypeId());
			viewPropertyDetails.setWallType(constructionTypeDetails.getWallTypeId());
			viewPropertyDetails.setWoodType(constructionTypeDetails.getWoodTypeId());
		} else {
			viewPropertyDetails.setFloorType(null);
			viewPropertyDetails.setRoofType(null);
			viewPropertyDetails.setWallType(null);
			viewPropertyDetails.setWoodType(null);
		}
    	if(createPropDetails.getPropertyTypeMasterCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)){
    		VacantLandDetails vacantLandDetails = createPropDetails.getVacantLandDetails();
    		viewPropertyDetails.setSurveyNumber(vacantLandDetails.getSurveyNumber());
    		viewPropertyDetails.setPattaNumber(vacantLandDetails.getPattaNumber());
    		viewPropertyDetails.setVacantLandArea(vacantLandDetails.getVacantLandArea());
    		viewPropertyDetails.setMarketValue(vacantLandDetails.getMarketValue());
    		viewPropertyDetails.setCurrentCapitalValue(vacantLandDetails.getCurrentCapitalValue());
    		viewPropertyDetails.setEffectiveDate(vacantLandDetails.getEffectiveDate());
    		viewPropertyDetails.setVlPlotArea(vacantLandDetails.getVacantLandPlot());
    		viewPropertyDetails.setLaAuthority(vacantLandDetails.getLayoutApprovalAuthority());
			if (!NO_APPROVAL.equals(
					layoutApprovalAuthorityRepo.findOne(vacantLandDetails.getLayoutApprovalAuthority()).getName())) {
				viewPropertyDetails.setLpNo(vacantLandDetails.getLayoutPermitNumber());
				viewPropertyDetails.setLpDate(vacantLandDetails.getLayoutPermitDate());
			}
    		//Surrounding Boundary Details
    		SurroundingBoundaryDetails surroundingBoundaryDetails = createPropDetails.getSurroundingBoundaryDetails();
    		viewPropertyDetails.setNorthBoundary(surroundingBoundaryDetails.getNorthBoundary());
    		viewPropertyDetails.setSouthBoundary(surroundingBoundaryDetails.getSouthBoundary());
    		viewPropertyDetails.setEastBoundary(surroundingBoundaryDetails.getEastBoundary());
    		viewPropertyDetails.setWestBoundary(surroundingBoundaryDetails.getWestBoundary());
    	} else {
    		viewPropertyDetails.setFloorDetails(createPropDetails.getFloorDetails());
    		viewPropertyDetails.setEffectiveDate(createPropDetails.getFloorDetails().get(0).getOccupancyDate());
    	}
        viewPropertyDetails.setParcelId(createPropDetails.getParcelId());
        viewPropertyDetails.setReferenceId(createPropDetails.getReferenceId());
        viewPropertyDetails.setLatitude(createPropDetails.getLatitude());
        viewPropertyDetails.setLongitude(createPropDetails.getLongitude());
    	return viewPropertyDetails;
    }

	/**
     * This method is used to get POJO object from JSON request.
     * 
     * @param jsonString - request JSON string
     * @return
     * @throws IOException
     */
    private Object getObjectFromJSONRequest(String jsonString, Class cls)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        mapper.setDateFormat(ChequePayment.CHEQUE_DATE_FORMAT);
        return mapper.readValue(jsonString, cls);
    }
    
}
