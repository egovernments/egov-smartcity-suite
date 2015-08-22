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

import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.DrainageEnum;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.model.LocalityDetails;
import org.egov.ptis.domain.model.MasterCodeNamePairDetails;
import org.egov.ptis.domain.model.NewPropertyDetails;
import org.egov.ptis.domain.model.OwnerDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.model.ReceiptDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

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
				PropertyExternalService.FLAG_FULL_DETAILS);

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
	@Path("/property/getPropertyTaxDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyTaxDetails(@FormParam("assessmentNo") String assessmentNo,
			@FormParam("username") String username, @FormParam("password") String password)
					throws JsonGenerationException, JsonMappingException, IOException {
		PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(assessmentNo);
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
	@Path("/property/getPropertyTaxDetailsByBoundary")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyTaxDetails(@FormParam("circleName") String circleName,
			@FormParam("zoneName") String zoneName, @FormParam("wardName") String wardName,
			@FormParam("blockName") String blockName, @FormParam("ownerName") String ownerName,
			@FormParam("doorNo") String doorNo, @FormParam("username") String username,
			@FormParam("password") String password) throws JsonGenerationException, JsonMappingException, IOException {

		PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
		List<PropertyTaxDetails> propertyTaxDetailsList = new ArrayList<PropertyTaxDetails>();
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			propertyTaxDetailsList = propertyExternalService.getPropertyTaxDetails(circleName, zoneName, wardName,
					blockName, ownerName, doorNo);
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
				ReceiptDetails receiptDetails = propertyExternalService.payPropertyTax(assessmentNo, paymentMode,
						totalAmount, paidBy, username, password);
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
			errorDetails = propertyExternalService.payWaterTax(consumerNo, paymentMode, totalAmount, paidBy, username,
					password);
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
	@POST
	@Path("/property/getPropertyTypeMasterDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyTypeMasterDetails(@FormParam("username") String username,
			@FormParam("password") String password) throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> propTypeMasterDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			propTypeMasterDetailsList = propertyExternalService.getPropertyTypeMasterDetails();
			responseJson = getJSONResponse(propTypeMasterDetailsList);
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
	@POST
	@Path("/property/getPropertyTypeCategoryDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPropertyTypeCategoryDetails(@FormParam("categoryCode") String categoryCode,
			@FormParam("username") String username, @FormParam("password") String password)
					throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getPropertyTypeCategoryDetails(categoryCode);
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
	@POST
	@Path("/property/getApartmentsAndComplexes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApartmentsAndComplexes(@FormParam("username") String username,
			@FormParam("password") String password) throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getReasonsForCreateProperty")
	@Produces(MediaType.APPLICATION_JSON)
	public String getReasonsToCreateProperty(@FormParam("username") String username,
			@FormParam("password") String password) throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getReasonsForCreateProperty();
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
	@Path("/property/getLocalities")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLocalities(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = null;
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getLocalities();
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
	@Path("/property/getBoundaryByLocalityCode")
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
	 * This method is used to get all list of all the election wards.
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
	@POST
	@Path("/property/getElectionWards")
	@Produces(MediaType.APPLICATION_JSON)
	public String getElectionWards(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			mstrCodeNamePairDetailsList = propertyExternalService.getElectionBoundaries();
			responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
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
	@POST
	@Path("/property/getEnumerationBlocks")
	@Produces(MediaType.APPLICATION_JSON)
	public String getEnumerationBlocks(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getFloorTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFloorTypes(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getRoofTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRoofTypes(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getWallTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getWallTypes(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getWoodTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getWoodTypes(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getFloorNos")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFloorNos(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			TreeMap<Integer, String> floorMap = FLOOR_MAP;
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
	@POST
	@Path("/property/getBuildingClassifications")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBuildingClassifications(@FormParam("username") String username,
			@FormParam("password") String password) throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getNatureOfUsages")
	@Produces(MediaType.APPLICATION_JSON)
	public String getNatureOfUsages(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getOccupancies")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOccupancies(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getExemptionCategories")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTaxExemptionCategories(@FormParam("username") String username,
			@FormParam("password") String password) throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getDrainages")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDrainages(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	@POST
	@Path("/property/getApproverDepartments")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApproverDepartments(@FormParam("username") String username, @FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
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
	 * This method is used to create property.
	 * 
	 * @param propertyTypeMasterCode
	 *            - category of ownership code
	 * @param propertyCategoryCode
	 *            - property type code
	 * @param apartmentCmplxCode
	 *            - apartment/complex code
	 * @param ownerDetails
	 *            - a list of owner details
	 * @param mutationReasonCode
	 *            - reason for creation or mutation code
	 * @param extentOfSite
	 *            - extent of site
	 * @param isExtentAppurtenantLand
	 *            - extent appurtenant land
	 * @param occupancyCertificationNo
	 *            - occupancy certification number
	 * @param isSuperStructure
	 *            - super structure
	 * @param isBuildingPlanDetails
	 *            - building plan details
	 * @param regdDocNo
	 *            - registration document number
	 * @param regdDocDate
	 *            - registration document date
	 * @param localityCode
	 *            - locality code
	 * @param street
	 *            - street name
	 * @param electionWardCode
	 *            - election ward code
	 * @param doorNo
	 *            - door number
	 * @param enumerationBlockCode
	 *            - enumeration block code
	 * @param pinCode
	 *            - pin code
	 * @param isCorrAddrDiff
	 *            - is correspondence address different
	 * @param corrAddr1
	 *            - correspondence address 1
	 * @param corrAddr2
	 *            - correspondence address 2
	 * @param corrPinCode
	 *            - correspondence address pin code
	 * @param hasLift
	 *            - has lift
	 * @param hasToilet
	 *            - has toilet
	 * @param hasWaterTap
	 *            - has water tap
	 * @param hasElectricity
	 *            - has electricity
	 * @param hasAttachedBathroom
	 *            - has attached bathroom
	 * @param hasWaterHarvesting
	 *            - has water harvesting
	 * @param floorTypeCode
	 *            - floor type code
	 * @param roofTypeCode
	 *            - roof type code
	 * @param wallTypeCode
	 *            - wall type code
	 * @param woodTypeCode
	 *            - wood type code
	 * @param floorDetails
	 *            - a list of floor details
	 * @param surveyNumber
	 *            - survey number
	 * @param pattaNumber
	 *            - patta number
	 * @param vacantLandArea
	 *            - vacant land area value
	 * @param marketValue
	 *            - market value
	 * @param currentCapitalValue
	 *            - current capital value
	 * @param completionDate
	 *            - date of completion
	 * @param northBoundary
	 *            - north boundary
	 * @param southBoundary
	 *            - south boundary
	 * @param eastBoundary
	 *            - east boundary
	 * @param westBoundary
	 *            - west boundary
	 * @param photoAsmntStream-
	 *            photo of assessment input stream object
	 * @param photoAsmntDisp-
	 *            photo of assessment content disposition object
	 * @param bldgPermCopyStream-
	 *            building permission copy input stream object
	 * @param bldgPermCopyDisp-
	 *            building permission copy content disposition object
	 * @param atstdCopyPropDocStream-
	 *            attested copy of property document input stream object
	 * @param atstdCopyPropDocDisp-
	 *            attested copy of property document content disposition
	 * @param nonJudcStampStream
	 *            - non judicial stamp input stream object
	 * @param nonJudcStampDisp
	 *            - non judicial stamp content disposition object
	 * @param afdvtBondStream
	 *            - affidavit bond paper input stream object
	 * @param afdvtBondDisp
	 *            - affidavit bond paper content disposition object
	 * @param deathCertCopyStream
	 *            - death certificate copy input stream object
	 * @param deathCertCopyDisp
	 *            - death certificate copy content disposition object
	 * @param username
	 *            - username credential
	 * @param password-
	 *            password credential
	 * @return - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ParseException
	 */
	@POST
	@Path("/property/createProperty")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces(MediaType.APPLICATION_JSON)
	public String createProperty(@FormDataParam("propertyTypeMasterCode") String propertyTypeMasterCode,
			@FormDataParam("propertyCategoryCode") String propertyCategoryCode,
			@FormDataParam("apartmentCmplxCode") String apartmentCmplxCode,
			@FormDataParam("ownerDetails") String ownerDetails,
			@FormDataParam("mutationReasonCode") String mutationReasonCode,
			@FormDataParam("extentOfSite") String extentOfSite,
			@FormDataParam("isExtentAppurtenantLand") String isExtentAppurtenantLand,
			@FormDataParam("occupancyCertificationNo") String occupancyCertificationNo,
			@FormDataParam("isSuperStructure") Boolean isSuperStructure,
			@FormDataParam("isBuildingPlanDetails") Boolean isBuildingPlanDetails,
			@FormDataParam("regdDocNo") String regdDocNo, @FormDataParam("regdDocDate") String regdDocDate,
			@FormDataParam("localityCode") String localityCode, @FormDataParam("street") String street,
			@FormDataParam("electionWardCode") String electionWardCode, @FormDataParam("doorNo") String doorNo,
			@FormDataParam("enumerationBlockCode") String enumerationBlockCode,
			@FormDataParam("pinCode") String pinCode, @FormDataParam("isCorrAddrDiff") Boolean isCorrAddrDiff,
			@FormDataParam("corrAddr1") String corrAddr1, @FormDataParam("corrAddr2") String corrAddr2,
			@FormDataParam("corrPinCode") String corrPinCode, @FormDataParam("hasLift") Boolean hasLift,
			@FormDataParam("hasToilet") Boolean hasToilet, @FormDataParam("hasWaterTap") Boolean hasWaterTap,
			@FormDataParam("hasElectricity") Boolean hasElectricity,
			@FormDataParam("hasAttachedBathroom") String hasAttachedBathroom,
			@FormDataParam("hasWaterHarvesting") String hasWaterHarvesting,
			@FormDataParam("floorTypeCode") String floorTypeCode, @FormDataParam("roofTypeCode") String roofTypeCode,
			@FormDataParam("wallTypeCode") String wallTypeCode, @FormDataParam("woodTypeCode") String woodTypeCode,
			@FormDataParam("floorDetails") String floorDetails, @FormDataParam("surveyNumber") String surveyNumber,
			@FormDataParam("pattaNumber") String pattaNumber, @FormDataParam("vacantLandArea") Double vacantLandArea,
			@FormDataParam("marketValue") Double marketValue,
			@FormDataParam("currentCapitalValue") Double currentCapitalValue,
			@FormDataParam("completionDate") String completionDate,
			@FormDataParam("northBoundary") String northBoundary, @FormDataParam("southBoundary") String southBoundary,
			@FormDataParam("eastBoundary") String eastBoundary, @FormDataParam("westBoundary") String westBoundary,
			@FormDataParam("photoAsmnt") InputStream photoAsmntStream,
			@FormDataParam("photoAsmnt") FormDataContentDisposition photoAsmntDisp,
			@FormDataParam("bldgPermCopy") InputStream bldgPermCopyStream,
			@FormDataParam("bldgPermCopy") FormDataContentDisposition bldgPermCopyDisp,
			@FormDataParam("atstdCopyPropDoc") InputStream atstdCopyPropDocStream,
			@FormDataParam("atstdCopyPropDoc") FormDataContentDisposition atstdCopyPropDocDisp,
			@FormDataParam("nonJudcStamp") InputStream nonJudcStampStream,
			@FormDataParam("nonJudcStamp") FormDataContentDisposition nonJudcStampDisp,
			@FormDataParam("afdvtBond") InputStream afdvtBondStream,
			@FormDataParam("afdvtBond") FormDataContentDisposition afdvtBondDisp,
			@FormDataParam("deathCertCopy") InputStream deathCertCopyStream,
			@FormDataParam("deathCertCopy") FormDataContentDisposition deathCertCopyDisp,
			@FormDataParam("username") String username, @FormDataParam("password") String password)
					throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		ErrorDetails errorDetails = null;
		String responseJson = null;
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			EgovThreadLocals.setUserId(Long.valueOf("16"));
			List<FloorDetails> floorDetailsList = new ObjectMapper().readValue(floorDetails.toString(),
					new TypeReference<Collection<FloorDetails>>() {
					});
			List<OwnerDetails> ownerDetailsList = new ObjectMapper().readValue(ownerDetails.toString(),
					new TypeReference<Collection<OwnerDetails>>() {
					});
			List<Document> documents = propertyExternalService.getDocuments(photoAsmntStream, photoAsmntDisp, bldgPermCopyStream,
					bldgPermCopyDisp, atstdCopyPropDocStream, atstdCopyPropDocDisp, nonJudcStampStream,
					nonJudcStampDisp, afdvtBondStream, afdvtBondDisp, deathCertCopyStream, deathCertCopyDisp);
			NewPropertyDetails newPropertyDetails = propertyExternalService.createNewProperty(propertyTypeMasterCode,
					propertyCategoryCode, apartmentCmplxCode, ownerDetailsList, mutationReasonCode, extentOfSite,
					isExtentAppurtenantLand, occupancyCertificationNo, isSuperStructure, isBuildingPlanDetails,
					regdDocNo, regdDocDate, localityCode, street, electionWardCode, doorNo, enumerationBlockCode,
					pinCode, isCorrAddrDiff, corrAddr1, corrAddr2, corrPinCode, hasLift, hasToilet, hasWaterTap,
					hasElectricity, hasAttachedBathroom, hasWaterHarvesting, floorTypeCode, roofTypeCode, wallTypeCode,
					woodTypeCode, floorDetailsList, surveyNumber, pattaNumber, vacantLandArea, marketValue,
					currentCapitalValue, completionDate, northBoundary, southBoundary, eastBoundary, westBoundary, documents);
			if (null != newPropertyDetails) {
				responseJson = getJSONResponse(newPropertyDetails);
			} else {
				errorDetails = getRequestFailedErrorDetails();
				responseJson = getJSONResponse(errorDetails);
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