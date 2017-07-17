/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.web.controller.rest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The AssessmentService class is used as the RESTFul service to handle user
 * request and response.
 * 
 * @author ranjit
 *
 */
@RestController
public class AssessmentServiceController {

	@Autowired
	private PropertyExternalService propertyExternalService;

	/**
	 * This method is used for handling user request for assessment details.
	 * 
	 * @param assessmentNumber
	 *            - assessment number i.e. property id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value ="/rest/property/{assessmentNumber}", produces = APPLICATION_JSON_VALUE)
	public AssessmentDetails getAssessmentDetails(@PathVariable String assessmentNumber) throws IOException {
		return propertyExternalService.loadAssessmentDetails(assessmentNumber,
                        PropertyExternalService.FLAG_FULL_DETAILS,BasicPropertyStatus.ALL);
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
	 * @throws IOException
	 */
	@RequestMapping(value = "/property/propertyTaxDetails", method = POST, produces = APPLICATION_JSON_VALUE)
	public String getPropertyTaxDetails(@RequestParam String assessmentNo, @RequestParam String username, @RequestParam String password)
					throws IOException {
		PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
		Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
			propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(assessmentNo, null, null);
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/propertyTaxDetailsByBoundary", method = POST, produces = APPLICATION_JSON_VALUE)
	public String getPropertyTaxDetails(@RequestParam String circleName, @RequestParam String zoneName, @RequestParam String wardName,
			@RequestParam String blockName, @RequestParam String ownerName, @RequestParam String doorNo, @RequestParam String aadhaarNumber,
			@RequestParam String mobileNumber, @RequestParam String username, @RequestParam String password) throws IOException {
		
        Boolean isAuthenticatedUser = propertyExternalService.authenticateUser(username, password);
		if (isAuthenticatedUser) {
            return getJSONResponse(propertyExternalService.getPropertyTaxDetails(circleName, zoneName, wardName,
                    blockName, ownerName, doorNo, aadhaarNumber, mobileNumber));
		} else {
            PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/payPropertyTax", method = POST, consumes = APPLICATION_FORM_URLENCODED_VALUE, produces = APPLICATION_JSON_VALUE)
	public String payPropertyTax(@RequestParam String assessmentNo, @RequestParam String paymentMode, @RequestParam BigDecimal totalAmount,
			@RequestParam String paidBy, @RequestParam String username, @RequestParam String password) throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/payWaterTax", method = POST, consumes = APPLICATION_FORM_URLENCODED_VALUE, produces = APPLICATION_JSON_VALUE)
	public String payWateTax(@RequestParam String consumerNo, @RequestParam String paymentMode,
			@RequestParam BigDecimal totalAmount, @RequestParam String paidBy)
					throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/ownershipCategories", produces = APPLICATION_JSON_VALUE)
	public String getOwnershipCategories() throws IOException {
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
    @RequestMapping(value = "/property/ownershipCategoryByCode/{ownershipCategoryCode}", produces = APPLICATION_JSON_VALUE)
	public String getOwnershipCategoryByCode(@PathVariable String ownershipCategoryCode)
					throws IOException {
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
	 * @return
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/propertyTypes/{ownershipCategoryCode}", produces = APPLICATION_JSON_VALUE)
	public String getPropertyTypeCategoryDetails(@PathVariable String ownershipCategoryCode)
					throws IOException {
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
	
    @RequestMapping(value = "/property/propertyTypes", produces = APPLICATION_JSON_VALUE)
	public String getPropertyTypes()
					throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/apartments", produces = APPLICATION_JSON_VALUE)
	public String getApartmentsAndComplexes() throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/createPropertyReasons", produces = APPLICATION_JSON_VALUE)
	public String getCreatePropertyReasons() throws IOException {
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/localities", produces = APPLICATION_JSON_VALUE)
	public String getLocalities()
			throws IOException {
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/zones", produces = APPLICATION_JSON_VALUE)
	public String getZones()
			throws IOException {
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/electionWards", produces = APPLICATION_JSON_VALUE)
	public String getElectionWards()
			throws IOException {
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/boundaryByLocalityCode", method = POST, produces = APPLICATION_JSON_VALUE)
	public String getBoundaryByLocalityCode(@RequestParam String localityCode,
			@RequestParam String username, @RequestParam String password)
					throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/enumerationBlocks", produces = APPLICATION_JSON_VALUE)
	public String getEnumerationBlocks()
			throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/floorTypes", produces = APPLICATION_JSON_VALUE)
	public String getFloorTypes()
			throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/roofTypes", produces = APPLICATION_JSON_VALUE)
	public String getRoofTypes()
			throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/wallTypes", produces = APPLICATION_JSON_VALUE)
	public String getWallTypes()
			throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/woodTypes", produces = APPLICATION_JSON_VALUE)
	public String getWoodTypes()
			throws IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList;
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/floors", produces = APPLICATION_JSON_VALUE)
	public String getFloors()
			throws IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<>();
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/propertyClassifications", produces = APPLICATION_JSON_VALUE)
	public String getPropertyClassifications() throws IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList;
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/propertyUsages", produces = APPLICATION_JSON_VALUE)
	public String getPropertUsages()
			throws IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList;
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/occupancyTypes", produces = APPLICATION_JSON_VALUE)
	public String getOccupancyTypes()
			throws IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList;
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/exemptionCategories", produces = APPLICATION_JSON_VALUE)
	public String getTaxExemptionCategories() throws IOException {
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/drainages", produces = APPLICATION_JSON_VALUE)
	public String getDrainages() throws IOException {

		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<>();
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/documentTypes", produces = APPLICATION_JSON_VALUE)
	public String getDocumentTypes()
			throws IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList;
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/mutationReasons", produces = APPLICATION_JSON_VALUE)
	public String getMutationReasons()
			throws IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList;
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
	 * @return responseJson - server response in JSON format
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/approverDepartments", produces = APPLICATION_JSON_VALUE)
	public String getApproverDepartments() throws IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList;
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
	 * @throws IOException
	 */
    @RequestMapping(value = "/wardBlockLocalityMapping", produces = APPLICATION_JSON_VALUE)
	public String getWardBlockLocalityMappings()
			throws IOException {
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
	 * @throws IOException
	 */
	private String getJSONResponse(Object obj) throws IOException {
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

	
}