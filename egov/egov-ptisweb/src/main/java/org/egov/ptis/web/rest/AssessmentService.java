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
package org.egov.ptis.web.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.ptis.bean.rest.AssessmentDetails;
import org.egov.ptis.bean.rest.BoundaryDetails;
import org.egov.ptis.bean.rest.ErrorDetails;
import org.egov.ptis.bean.rest.OwnerName;
import org.egov.ptis.bean.rest.PropertyDetails;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The AssessmentService class is used as the RESTFul service to handle user
 * request and response.
 * 
 * @author ranjit
 *
 */
@Component
@Path("/")
public class AssessmentService {
	
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	
	@Autowired
	private PtDemandDao ptDemandDao;
	
	/**
	 * This method is used for handling user request for assessment details.
	 * 
	 * @param assessmentNumber
	 *            - assessment number i.e. property id
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/{assessmentNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAssessmentDetails(@PathParam("assessmentNumber") String assessmentNumber) throws JsonGenerationException, JsonMappingException, IOException{
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNumber);
		AssessmentDetails assessmentDetail = new AssessmentDetails();
		BoundaryDetails boundaryDetails = new BoundaryDetails();
		PropertyDetails propertyDetails = new PropertyDetails();
		Set<OwnerName> ownerNames = new HashSet<OwnerName>();
		ErrorDetails errorDetails = new ErrorDetails();

		if(null != basicProperty) {
			//Error Code
			if(!basicProperty.isActive()) {
				errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_CODE);
				errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_MSG);
			} else {
				Set<PropertyStatusValues> statusValues = basicProperty.getPropertyStatusValuesSet();
				if(null != statusValues && !statusValues.isEmpty()) {
					for (PropertyStatusValues statusValue : statusValues){
						if(statusValue.getPropertyStatus().getStatusCode() == PropertyTaxConstants.MARK_DEACTIVE) {
							errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_CODE);
							errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_MSG);
						}
					}
				}
			}

			//Owner Details
			Property property = basicProperty.getProperty();
			if(null != property) {
				Set<PropertyOwner> propertyOwners = property.getPropertyOwnerSet();
				if(propertyOwners != null && !propertyOwners.isEmpty()) {
					for(PropertyOwner propertyOwner : propertyOwners) {
						OwnerName ownerName = new OwnerName();
						ownerName.setAadhaarNumber(propertyOwner.getAadhaarNumber());
						ownerName.setOwnerName(propertyOwner.getName());
						ownerName.setMobileNumber(propertyOwner.getMobileNumber());
						ownerNames.add(ownerName);
					}
				}

				//Property Details
				PropertyDetail propertyDetail = property.getPropertyDetail();
				if(null != propertyDetail) {
					propertyDetails.setPropertyType(propertyDetail.getPropertyType());
					if(propertyDetail.getPropertyUsage() != null) {
						propertyDetails.setPropertyUsage(propertyDetail.getPropertyUsage().getUsageName());
					}
				}

				Map<String, BigDecimal> resultmap = ptDemandDao.getDemandCollMap(property);
				if(null != resultmap && !resultmap.isEmpty()) {
					BigDecimal currDmd = resultmap.get(PropertyTaxConstants.CURR_DMD_STR);
					BigDecimal arrDmd = resultmap.get(PropertyTaxConstants.ARR_DMD_STR);
					BigDecimal currCollection = resultmap.get(PropertyTaxConstants.CURR_COLL_STR);
					BigDecimal arrColelection = resultmap.get(PropertyTaxConstants.ARR_COLL_STR);
					
					//Calculating tax dues
					BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrColelection);
					propertyDetails.setTaxDue(taxDue);
				}
			}

			//Boundary Details
			PropertyID propertyID = basicProperty.getPropertyID();
			if(null != propertyID) {
				if(null != propertyID.getZone()) {
					boundaryDetails.setZoneNumber(propertyID.getZone().getBoundaryNum());
					boundaryDetails.setZoneName(propertyID.getZone().getName());
				}
				if(null != propertyID.getWard()) {
					boundaryDetails.setWardNumber(propertyID.getWard().getBoundaryNum());
					boundaryDetails.setWardName(propertyID.getWard().getName());
				}
				if(null != propertyID.getArea()) {
					boundaryDetails.setBlockNumber(propertyID.getArea().getBoundaryNum());
					boundaryDetails.setBlockName(propertyID.getArea().getName());
				}
				if(null != propertyID.getLocality()) {
					boundaryDetails.setLocalityName(propertyID.getLocality().getName());
				}
				if(null != propertyID.getStreet()) {
					boundaryDetails.setStreetName(propertyID.getStreet().getName());
				}
			}
			
		} else {
			errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_CODE);
			errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG);
		}

		//Assessment Details
		assessmentDetail.setPropertyID(assessmentNumber);
		assessmentDetail.setOwnerNames(ownerNames);
		assessmentDetail.setBoundaryDetails(boundaryDetails);
		assessmentDetail.setPropertyDetails(propertyDetails);
		assessmentDetail.setErrorDetails(errorDetails);
		
		return getJSONResponse(assessmentDetail);
	}

	/**
	 * This method is used to prepare jSON response.
	 * 
	 * @param obj
	 *            - a POJO object
	 * @return jsonResponse - JSON response string
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private String getJSONResponse(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		String jsonResponse  = objectMapper.writeValueAsString(obj);
		return jsonResponse;
	}
}