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
package org.egov.ptis.web.rest;

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.DrainageEnum;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.LocalityDetails;
import org.egov.ptis.domain.model.MasterCodeNamePairDetails;
import org.egov.ptis.domain.model.PayPropertyTaxDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.model.ReceiptDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

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
	private PropertyExternalService propertyExternalService;

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
	public String getAssessmentDetails(@PathParam("assessmentNumber") String assessmentNumber)
			throws JsonGenerationException, JsonMappingException, IOException {
		AssessmentDetails assessmentDetail = propertyExternalService.loadAssessmentDetails(assessmentNumber,
				PropertyExternalService.FLAG_FULL_DETAILS,BasicPropertyStatus.ACTIVE);

		return getJSONResponse(assessmentDetail);
	}

	/**
	 * This method is used get the property tax details.
	 * 
	 * @param assessmentNo
	 *            - assessment no
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@POST
	@Path("/property/propertyTaxDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyTaxDetails(@FormParam("assessmentNo") String assessmentNo,
			@FormParam("username") String username, @FormParam("password") String password)
					throws JsonGenerationException, JsonMappingException, IOException {
		PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(assessmentNo, null);
		} else {
			ErrorDetails errorDetails = getInvalidCredentialsErrorDetails();
			propertyTaxDetails.setErrorDetails(errorDetails);
		}
		return getJSONResponse(propertyTaxDetails);
	}

	/**
	 * This method id used to search the property based on boundary details.
	 * 
	 * @param circleName
	 *            - boundary as circle name
	 * @param zoneName
	 *            - boundary as zone name
	 * @param wardName
	 *            - boundary as ward name
	 * @param blockName
	 *            - boundary as block name
	 * @param ownerName
	 *            - owner name
	 * @param doorNo
	 *            - door number
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@POST
	@Path("/property/propertyTaxDetailsByBoundary")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyTaxDetails(@FormParam("circleName") String circleName,
			@FormParam("zoneName") String zoneName, @FormParam("wardName") String wardName,
			@FormParam("blockName") String blockName, @FormParam("ownerName") String ownerName,
			@FormParam("doorNo") String doorNo, @FormParam("aadhaarNumber") String aadhaarNumber,
			@FormParam("mobileNumber") String mobileNumber, @FormParam("username") String username,
			@FormParam("password") String password) throws JsonGenerationException, JsonMappingException, IOException {

		PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
		List<PropertyTaxDetails> propertyTaxDetailsList = new ArrayList<PropertyTaxDetails>();
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			propertyTaxDetailsList = propertyExternalService.getPropertyTaxDetails(circleName, zoneName, wardName,
					blockName, ownerName, doorNo, aadhaarNumber, mobileNumber);
			return getJSONResponse(propertyTaxDetailsList);
		} else {
			ErrorDetails errorDetails = getInvalidCredentialsErrorDetails();
			propertyTaxDetails.setErrorDetails(errorDetails);
			return getJSONResponse(propertyTaxDetails);
		}
	}

	/**
	 * This method is used to pay the property tax.
	 * 
	 * @param assessmentNo
	 *            - assessment number
	 * @param paymentMode
	 *            - mode of payment
	 * @param totalAmount
	 *            - total amount paid
	 * @param paidBy
	 *            - payer name
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@POST
	@Path("/property/payPropertyTax")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String payPropertyTax(@FormParam("assessmentNo") String assessmentNo,
			@FormParam("paymentMode") String paymentMode, @FormParam("totalAmount") BigDecimal totalAmount,
			@FormParam("paidBy") String paidBy, @FormParam("username") String username,
			@FormParam("password") String password) throws JsonGenerationException, JsonMappingException, IOException {
		String responseJson = new String();
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			ErrorDetails errorDetails = propertyExternalService.validatePaymentDetails(assessmentNo, paymentMode,
					totalAmount, paidBy);
			if (null != errorDetails) {
				responseJson = getJSONResponse(errorDetails);
			} else {
				PayPropertyTaxDetails pt=new PayPropertyTaxDetails();
				pt.setAssessmentNo(assessmentNo);
				pt.setPaymentMode(paymentMode);
				pt.setPaymentAmount(totalAmount);
				pt.setPaidBy(paidBy);
				
				ReceiptDetails receiptDetails = propertyExternalService.payPropertyTax(pt, "");
				responseJson = getJSONResponse(receiptDetails);
			}
		}
		return responseJson;
	}

	/**
	 * This method is used to pay the water tax.
	 * 
	 * @param consumerNo
	 *            - consumer number
	 * @param paymentMode
	 *            - mode of payment
	 * @param totalAmount
	 *            - total amount paid
	 * @param paidBy
	 *            - payer's name
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@POST
	@Path("/property/payWaterTax")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String payWateTax(@FormParam("consumerNo") String consumerNo, @FormParam("paymentMode") String paymentMode,
			@FormParam("totalAmount") BigDecimal totalAmount, @FormParam("paidBy") String paidBy,
			@FormParam("username") String username, @FormParam("password") String password)
					throws JsonGenerationException, JsonMappingException, IOException {
		ErrorDetails errorDetails = propertyExternalService.validatePaymentDetails(consumerNo, paymentMode, totalAmount,
				paidBy);
		if (null != errorDetails) {
			return getJSONResponse(errorDetails);
		} else {
			errorDetails = propertyExternalService.payWaterTax(consumerNo, paymentMode, totalAmount, paidBy);
			return getJSONResponse(errorDetails);
		}
	}

	/**
	 * This method is used to get the property type master details
	 * 
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/ownershipCategories")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOwnershipCategories() throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> propTypeMasterDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			propTypeMasterDetailsList = propertyExternalService.getPropertyTypeMasterDetails();
			responseJson = getJSONResponse(propTypeMasterDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}
	
	//TODO : Need to check again
	@GET
	@Path("/property/ownershipCategoryByCode/{ownershipCategoryCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOwnershipCategoryByCode(@PathParam("ownershipCategoryCode") String ownershipCategoryCode)
					throws JsonGenerationException, JsonMappingException, IOException {
		PropertyTypeMaster propertyTypeMaster = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			propertyTypeMaster = propertyExternalService.getPropertyTypeMasterByCode(ownershipCategoryCode);
			responseJson = getJSONResponse(propertyTypeMaster);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get the property type based one category
	 * 
	 * @param categoryCode
	 *            - property category code
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/propertyTypes/{ownershipCategoryCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyTypeCategoryDetails(@PathParam("ownershipCategoryCode") String ownershipCategoryCode)
					throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getPropertyTypeCategoryDetails(ownershipCategoryCode);
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}
	
	@GET
	@Path("/property/propertyTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyTypes()
					throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getPropertyTypes();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all the apartments and complexes.
	 * 
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/apartments")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApartmentsAndComplexes() throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getApartmentsAndComplexes();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get reasons for create the property.
	 * 
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/createPropertyReasons")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCreatePropertyReasons() throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getReasonsForChangeProperty(PropertyTaxConstants.PROP_CREATE_RSN);
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all localities.
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/localities")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLocalities()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getBoundariesByBoundaryTypeAndHierarchyType(PropertyTaxConstants.LOCALITY, PropertyTaxConstants.LOCATION_HIERARCHY_TYPE);
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}
	
	/**
	 * This method is used to get all zones.
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/zones")
	@Produces(MediaType.APPLICATION_JSON)
	public String getZones()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		//Authentication may be added later
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getBoundariesByBoundaryTypeAndHierarchyType(ZONE,REVENUE_HIERARCHY_TYPE);
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}
	
	/**
	 * This method is used to get all election wards.
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/electionWards")
	@Produces(MediaType.APPLICATION_JSON)
	public String getElectionWards()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		//Authentication may be added later
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getBoundariesByBoundaryTypeAndHierarchyType(WARD, ADMIN_HIERARCHY_TYPE);
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all localities.
	 * 
	 * @param username
	 *            - username credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@POST
	@Path("/property/boundaryByLocalityCode")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBoundaryByLocalityCode(@FormParam("localityCode") String localityCode,
			@FormParam("username") String username, @FormParam("password") String password)
					throws JsonGenerationException, JsonMappingException, IOException {
		LocalityDetails localityDetails = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			localityDetails = propertyExternalService.getLocalityDetailsByLocalityCode(localityCode);
			responseJson = getJSONResponse(localityDetails);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all list of all the enumeration blocks.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/enumerationBlocks")
	@Produces(MediaType.APPLICATION_JSON)
	public String getEnumerationBlocks()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getEnumerationBlocks();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all types of floors.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/floorTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFloorTypes()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getFloorTypes();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all type of roofs.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/roofTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRoofTypes()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getRoofTypes();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all list of all type of walls.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/wallTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getWallTypes()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getWallTypes();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all list of all type of woods
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/woodTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getWoodTypes()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getWoodTypes();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all list of floor numbers.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/floors")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFloors()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			TreeMap<Integer, String> floorMap = PropertyTaxConstants.FLOOR_MAP;
			
			Set<Integer> keys = floorMap.keySet();
			for (Integer key : keys) {
				MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
				mstrCodeNamePairDetails.setCode(key.toString());
				mstrCodeNamePairDetails.setName(floorMap.get(key));
				mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
			}
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all classifications of the property
	 * structutres.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/propertyClassifications")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyClassifications() throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getBuildingClassifications();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get nature of usages of the property.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/propertyUsages")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertUsages()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getNatureOfUsages();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all list of occupancies.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/occupancyTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOccupancyTypes()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getOccupancies();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get all the tax exemption categories.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/exemptionCategories")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTaxExemptionCategories() throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getExemptionCategories();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get drainages.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/drainages")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDrainages()
			throws JsonGenerationException, JsonMappingException, IOException {

		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			for (DrainageEnum drngEnum : DrainageEnum.values()) {
				MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
				mstrCodeNamePairDetails.setCode(drngEnum.getCode());
				mstrCodeNamePairDetails.setName(drngEnum.name());
				mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
			}
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}

	/**
	 * This method is used to get list of all documents 
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/documentTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDocumentTypes()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		//Authentication may be added later 
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getDocumentTypes();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}
	
	/**
	 * This method is used to get list of all mutation reasons 
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/mutationReasons")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMutationReasons()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		//Authentication may be added later
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService
	                .getReasonsForChangeProperty(PropertyTaxConstants.PROP_MUTATION_RSN);
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}
	
	/**
	 * This method is used to get all approver departments.
	 * 
	 * @param username
	 *            - usernam credential
	 * @param password
	 *            - password credential
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/property/approverDepartments")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApproverDepartments()
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getApproverDepartments();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
	}


	/**
	 * This method is used to get list of all Ward-Block-Locality Mappings 
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Path("/wardBlockLocalityMapping")
	@Produces(MediaType.APPLICATION_JSON)
	public String getWardBlockLocalityMappings()
			throws JsonGenerationException, JsonMappingException, IOException {
		String responseJson = StringUtils.EMPTY;
		ErrorDetails errorDetails = null;
		//Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		//Authentication may be added later
		Boolean isAuthenticatedUser = true;
		if (isAuthenticatedUser) {
			List<Object[]> boundaryDetailsList = propertyExternalService.getWardBlockLocalityMapping();
			List<JSONObject> boundaryJsonObjs = new ArrayList<JSONObject>();
			if(!boundaryDetailsList.isEmpty()){
				for(Object[] obj : boundaryDetailsList){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("wardNum", obj[0]);
					jsonObject.put("wardName", obj[1]);
					jsonObject.put("blockNum", obj[2]);
					jsonObject.put("blockName", obj[3]);
					jsonObject.put("localityNum", obj[4]);
					jsonObject.put("localityName", obj[5]);
					
					boundaryJsonObjs.add(jsonObject);
				}
				responseJson = getJSONResponse(boundaryJsonObjs);
			}
			
		} else {
			errorDetails = getInvalidCredentialsErrorDetails();
			responseJson = getJSONResponse(errorDetails);
		}
		return responseJson;
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
		String jsonResponse = objectMapper.writeValueAsString(obj);
		return jsonResponse;
	}

	/**
	 * This method is used to get the error details for invalid credentials.
	 * 
	 * @return
	 */
	private ErrorDetails getInvalidCredentialsErrorDetails() {
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_INVALIDCREDENTIALS);
		errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_INVALIDCREDENTIALS);
		return errorDetails;
	}
	/**
	 * This method is used to get the error details for communication failure.
	 * @return
	 */
	private ErrorDetails getRequestFailedErrorDetails() {
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_COMMUNICATION_FAILURE);
		errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_COMMUNICATION_FAILURE);
		return errorDetails;
	}

}