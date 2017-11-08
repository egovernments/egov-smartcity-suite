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

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.dcb.bean.ChequePayment;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.bean.AssessmentInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.DrainageEnum;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.LocalityDetails;
import org.egov.ptis.domain.model.MasterCodeNamePairDetails;
import org.egov.ptis.domain.model.OwnerDetails;
import org.egov.ptis.domain.model.PayPropertyTaxDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.model.ReceiptDetails;
import org.egov.ptis.domain.model.RestPropertyTaxDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.restapi.model.AssessmentRequest;
import org.egov.restapi.model.LocalityCodeDetails;
import org.egov.restapi.model.OwnershipCategoryDetails;
import org.egov.restapi.model.PropertyTaxBoundaryDetails;
import org.egov.restapi.util.JsonConvertor;
import org.egov.restapi.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AssessmentService class is used as the RESTFul service to handle user request and response.
 * 
 * @author ranjit
 *
 */
@RestController
@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AssessmentService {

    @Autowired
    private PropertyExternalService propertyExternalService;
    @Autowired
    private ValidationUtil validationUtil;

    /**
     * This method is used for handling user request for assessment details.
     * 
     * @param assessmentRequest
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/property/assessmentDetails", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getAssessmentDetails(@RequestBody String assessmentRequest)
            throws IOException {
        AssessmentRequest assessmentReq = (AssessmentRequest) getObjectFromJSONRequest(assessmentRequest,
                AssessmentRequest.class);
        AssessmentDetails assessmentDetail = propertyExternalService
                .loadAssessmentDetails(assessmentReq.getAssessmentNo(), PropertyExternalService.FLAG_FULL_DETAILS,
                        BasicPropertyStatus.ACTIVE);
        return getJSONResponse(assessmentDetail);
    }

    /**
     * This method is used get the property tax details.
     * 
     * @param assessmentRequest
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertytaxdetails", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getPropertyTaxDetails(@RequestBody String assessmentRequest)
            throws IOException {
        PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
        AssessmentRequest assessmentReq = (AssessmentRequest) getObjectFromJSONRequest(assessmentRequest,
                AssessmentRequest.class);
        try {
            String assessmentNo = assessmentReq.getAssessmentNo();
            String oldAssessmentNo = assessmentReq.getOldAssessmentNo();
            String category = assessmentReq.getCategory();
            if (StringUtils.isNotBlank(assessmentNo) || StringUtils.isNotBlank(oldAssessmentNo)) {
                propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(assessmentNo, oldAssessmentNo, category);
            } else {
                ErrorDetails errorDetails = getInvalidCredentialsErrorDetails();
                propertyTaxDetails.setErrorDetails(errorDetails);
            }
            if (propertyTaxDetails.getOwnerDetails() == null)
            {
                propertyTaxDetails.setOwnerDetails(new ArrayList<OwnerDetails>(0));
            }
            if (propertyTaxDetails.getLocalityName() == null)
                propertyTaxDetails.setLocalityName("");
            if (propertyTaxDetails.getPropertyAddress() == null)
                propertyTaxDetails.setPropertyAddress("");
            if (propertyTaxDetails.getTaxDetails() == null)
            {
                RestPropertyTaxDetails ar = new RestPropertyTaxDetails();
                List taxDetails = new ArrayList<RestPropertyTaxDetails>(0);
                taxDetails.add(ar);
                propertyTaxDetails.setTaxDetails(taxDetails);
            }
        } catch (Exception e) {
            List<ErrorDetails> errorList = new ArrayList<ErrorDetails>(0);
            ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            errorList.add(er);
            return JsonConvertor.convert(errorList);
        }
        return JsonConvertor.convert(propertyTaxDetails);
    }

    /**
     * This method is used get the property tax details.
     * 
     * @param assessmentNo - assessment no
     * @param ownerName - Owner Name
     * @param mobileNumber - Mobile Number
     * @return
     * @throws IOException
     */
    /**
     */
    @RequestMapping(value = "/property/propertytaxdetailsByOwnerDetails", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getPropertyTaxDetailsByOwnerDetails(@RequestBody String assessmentRequest)
            throws IOException {

        List<PropertyTaxDetails> propertyTaxDetailsList = new ArrayList<PropertyTaxDetails>();
        AssessmentRequest assessmentReq = (AssessmentRequest) getObjectFromJSONRequest(assessmentRequest,
                AssessmentRequest.class);
        try {
            String assessmentNo = assessmentReq.getAssessmentNo();
            String ownerName = assessmentReq.getOwnerName();
            String mobileNumber = assessmentReq.getMobileNumber();
            String category = assessmentReq.getCategory();
            String doorNo = assessmentReq.getDoorNo();
            if (!StringUtils.isBlank(category)) {
                if (!(PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX.equals(category)
                        || PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX.equals(category))) {
                    List<ErrorDetails> errors = new ArrayList<ErrorDetails>(0);
                    ErrorDetails er = new ErrorDetails();
                    er.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_WRONG_CATEGORY);
                    er.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_WRONG_CATEGORY);
                    errors.add(er);
                    return JsonConvertor.convert(errors);
                }
            }
            if (!StringUtils.isBlank(assessmentNo) || !StringUtils.isBlank(ownerName) || !StringUtils.isBlank(mobileNumber) || !StringUtils.isBlank(doorNo)) {
                propertyTaxDetailsList = propertyExternalService.getPropertyTaxDetails(assessmentNo, ownerName, mobileNumber, category, doorNo);
            } else {
                ErrorDetails errorDetails = getInvalidCredentialsErrorDetails();
                PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
                propertyTaxDetails.setErrorDetails(errorDetails);
                propertyTaxDetailsList.add(propertyTaxDetails);
            }

            for (PropertyTaxDetails propertyTaxDetails : propertyTaxDetailsList) {
                if (propertyTaxDetails.getOwnerDetails() == null) {
                    propertyTaxDetails.setOwnerDetails(new ArrayList<OwnerDetails>(0));
                }
                if (propertyTaxDetails.getLocalityName() == null)
                    propertyTaxDetails.setLocalityName("");
                if (propertyTaxDetails.getPropertyAddress() == null)
                    propertyTaxDetails.setPropertyAddress("");
                if (propertyTaxDetails.getTaxDetails() == null) {
                    RestPropertyTaxDetails ar = new RestPropertyTaxDetails();
                    List<RestPropertyTaxDetails> taxDetails = new ArrayList<RestPropertyTaxDetails>(0);
                    taxDetails.add(ar);
                    propertyTaxDetails.setTaxDetails(taxDetails);
                }
                if(propertyTaxDetails.getErrorDetails() == null){
                	ErrorDetails errorDetails = new ErrorDetails();
                	errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
                	propertyTaxDetails.setErrorDetails(errorDetails);
                }
            }
        } catch (Exception e) {
            List<ErrorDetails> errorList = new ArrayList<ErrorDetails>(0);
            ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            errorList.add(er);
            return JsonConvertor.convert(errorList);
        }
        return JsonConvertor.convert(propertyTaxDetailsList);
    }

    /**
     * This method is used to search the property based on boundary details.
     * 
     * @param propertyTaxBoundaryDetails - boundary details request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyTaxDetailsByBoundary", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getPropertyTaxDetailsByBoundary(@RequestBody String propertyTaxBoundaryDetails)
            throws IOException {
        PropertyTaxBoundaryDetails propTaxBoundaryDetails = (PropertyTaxBoundaryDetails) getObjectFromJSONRequest(
                propertyTaxBoundaryDetails, PropertyTaxBoundaryDetails.class);
        String circleName = propTaxBoundaryDetails.getCircleName();
        String zoneName = propTaxBoundaryDetails.getZoneName();
        String wardName = propTaxBoundaryDetails.getWardName();
        String blockName = propTaxBoundaryDetails.getBlockName();
        String ownerName = propTaxBoundaryDetails.getOwnerName();
        String doorNo = propTaxBoundaryDetails.getDoorNo();
        String aadhaarNumber = propTaxBoundaryDetails.getAadhaarNumber();
        String mobileNumber = propTaxBoundaryDetails.getMobileNumber();
        List<PropertyTaxDetails> propertyTaxDetailsList = propertyExternalService.getPropertyTaxDetails(circleName,
                zoneName, wardName, blockName, ownerName, doorNo, aadhaarNumber, mobileNumber);
        return getJSONResponse(propertyTaxDetailsList);
    }

    /**
     * This method is used to pay the property tax.
     * 
     * @param payPropertyTaxDetails
     * @param request
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/paypropertytax", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String payPropertyTax(@RequestBody String payPropertyTaxDetails, final HttpServletRequest request)
            throws IOException {
        String responseJson;
        try {
            responseJson = new String();
            PayPropertyTaxDetails payPropTaxDetails = (PayPropertyTaxDetails) getObjectFromJSONRequest(
                    payPropertyTaxDetails, PayPropertyTaxDetails.class);
			String propertyType = propertyExternalService.getPropertyType(payPropTaxDetails.getAssessmentNo());
			ErrorDetails errorDetails = validationUtil.validatePaymentDetails(payPropTaxDetails, false, propertyType);
            if (null != errorDetails) {
                responseJson = JsonConvertor.convert(errorDetails);
            } else {
                payPropTaxDetails.setSource(request.getSession().getAttribute("source") != null ? request.getSession()
                        .getAttribute("source").toString()
                        : "");
                ReceiptDetails receiptDetails = propertyExternalService.payPropertyTax(payPropTaxDetails, propertyType);
                responseJson = JsonConvertor.convert(receiptDetails);
            }
        } catch (ValidationException e) {

            List<ErrorDetails> errorList = new ArrayList<ErrorDetails>(0);

            List<ValidationError> errors = e.getErrors();
            for (ValidationError ve : errors)
            {
                ErrorDetails er = new ErrorDetails();
                er.setErrorCode(ve.getKey());
                er.setErrorMessage(ve.getMessage());
                errorList.add(er);
            }
            responseJson = JsonConvertor.convert(errorList);
        } catch (Exception e) {

            List<ErrorDetails> errorList = new ArrayList<ErrorDetails>(0);
            ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            errorList.add(er);
            responseJson = JsonConvertor.convert(errorList);
        }
        return responseJson;
    }

    /**
     * This method is used to pay the water tax.
     * 
     * @param consumerNo - consumer number
     * @param paymentMode - mode of payment
     * @param totalAmount - total amount paid
     * @param paidBy - payer's name
     * @return responseJson - server response in JSON format
     * @throws IOException
     */

    /**
     * This method is used to get the property type master details
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/ownershipCategories", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getOwnershipCategories() throws IOException {
        List<MasterCodeNamePairDetails> propTypeMasterDetailsList = propertyExternalService
                .getPropertyTypeMasterDetails();
        return getJSONResponse(propTypeMasterDetailsList);
    }

    /**
     * This method returns Ownership Category for the given code.
     * 
     * @param ownershipCategoryCode
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/property/ownershipCategoryByCode", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getOwnershipCategoryByCode(@RequestBody String ownershipCategoryDetails)
            throws IOException {
        OwnershipCategoryDetails ownershipCategory = (OwnershipCategoryDetails) getObjectFromJSONRequest(
                ownershipCategoryDetails, OwnershipCategoryDetails.class);
        PropertyTypeMaster propertyTypeMaster = propertyExternalService
                .getPropertyTypeMasterByCode(ownershipCategory.getOwnershipCategoryCode());
        return getJSONResponse(propertyTypeMaster);
    }

    /**
     * This method is used to get the property type based on ownership category
     * 
     * @param categoryCode - property category code
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyTypesByOwnership", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getPropertyTypeCategoryDetails(@RequestBody String ownershipCategoryDetails)
            throws IOException {
        OwnershipCategoryDetails ownershipCategory = (OwnershipCategoryDetails) getObjectFromJSONRequest(
                ownershipCategoryDetails, OwnershipCategoryDetails.class);
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getPropertyTypeCategoryDetails(ownershipCategory.getOwnershipCategoryCode());
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get the property type based one category
     * 
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyTypes", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getPropertyTypes() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getPropertyTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all the apartments and complexes.
     * 
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/apartments", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getApartmentsAndComplexes() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getApartmentsAndComplexes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get reasons for create the property.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/createPropertyReasons", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getCreatePropertyReasons() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getReasonsForChangeProperty(PropertyTaxConstants.PROP_CREATE_RSN);
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all localities.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/localities", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getLocalities() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getBoundariesByBoundaryTypeAndHierarchyType(
        		PropertyTaxConstants.LOCALITY, PropertyTaxConstants.LOCATION_HIERARCHY_TYPE);
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all localities.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/boundaryByLocalityCode", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getBoundaryByLocalityCode(@RequestBody String localityCodeDetails)
            throws IOException {
        LocalityCodeDetails locCodeDetails = (LocalityCodeDetails) getObjectFromJSONRequest(localityCodeDetails,
                LocalityCodeDetails.class);
        LocalityDetails localityDetails = propertyExternalService
                .getLocalityDetailsByLocalityCode(locCodeDetails.getLocalityCode());
        return getJSONResponse(localityDetails);
    }

    /**
     * This method is used to get all list of all the election wards.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/electionWards", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getElectionWards() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getBoundariesByBoundaryTypeAndHierarchyType(
        		WARD, ADMIN_HIERARCHY_TYPE);
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of all the enumeration blocks.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/enumerationBlocks", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getEnumerationBlocks() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getEnumerationBlocks();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all types of floors.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/floorTypes", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getFloorTypes() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getFloorTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all type of roofs.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/roofTypes", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getRoofTypes() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getRoofTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of all type of walls.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/wallTypes", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getWallTypes() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getWallTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of all type of woods
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/woodTypes", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getWoodTypes() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getWoodTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of floor numbers.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/floors", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getFloors() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        TreeMap<Integer, String> floorMap = PropertyTaxConstants.FLOOR_MAP;
        Set<Integer> keys = floorMap.keySet();
        for (Integer key : keys) {
            MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(key.toString());
            mstrCodeNamePairDetails.setName(floorMap.get(key));
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all classifications of the property structures.
     * 
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyClassifications", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getPropertyClassifications() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getBuildingClassifications();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get nature of usages of the property.
     * 
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyUsages", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getPropertUsages() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getNatureOfUsages();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of occupancies.
     * 
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/occupancyTypes", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getOccupancyTypes() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getOccupancies();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all the tax exemption categories.
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/property/exemptionCategories", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getTaxExemptionCategories() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getExemptionCategories();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get drainages.
     * 
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/drainages", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getDrainages() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        for (DrainageEnum drngEnum : DrainageEnum.values()) {
            MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(drngEnum.getCode());
            mstrCodeNamePairDetails.setName(drngEnum.name());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all approver departments.
     * 
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/approverDepartments", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getApproverDepartments() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getApproverDepartments();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method provides ward-wise property details
     * @throws IOException 
     */
    @RequestMapping(value = "/property/wardWisePropertyDetails", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public List<AssessmentInfo> getWardWisePropertyDetails(AssessmentRequest assessmentRequest) throws IOException {
        return propertyExternalService.getPropertyDetailsForWard(assessmentRequest.getUlbCode(), assessmentRequest.getWardNum(),
                assessmentRequest.getAssessmentNo(), assessmentRequest.getDoorNo(), assessmentRequest.getOldAssessmentNo());
    }
    
    /**
     * This method is used to prepare jSON response.
     * 
     * @param obj - a POJO object
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

    /**
     * This method is used to get the error details for communication failure.
     * 
     * @return
     */
    private ErrorDetails getRequestFailedErrorDetails() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_COMMUNICATION_FAILURE);
        errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_COMMUNICATION_FAILURE);
        return errorDetails;
    }

    /**
     * This method is used to get all the different types of documents.
     * 
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/documentTypes", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getDocumentTypes() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getDocumentTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get reasons for mutation.
     *
     * @return responseJson - server response in JSON format
     * @throws IOException
     */
    @RequestMapping(value = "/property/mutationReasons", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getMutatioReasons() throws IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getReasonsForChangeProperty(PropertyTaxConstants.PROP_MUTATION_RSN);
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method gives the various dues related to a property - property dues, water dues, sewerage dues
     * @param assessmentRequest
     * @param request
     * @return AssessmentDetails
     * @throws IOException
     */
    @RequestMapping(value = "/property/taxDues", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public AssessmentDetails getDCBDetails(@RequestBody AssessmentRequest assessmentRequest, final HttpServletRequest request){
        AssessmentDetails assessmentDetails = null;
        ErrorDetails errorDetails = validationUtil.validateAssessmentDetailsRequest(assessmentRequest);
        if(errorDetails != null){
            assessmentDetails = new AssessmentDetails();
            assessmentDetails.setErrorDetails(errorDetails);
        } else {
            assessmentDetails = propertyExternalService.getDuesForProperty(request, assessmentRequest.getAssessmentNo(),
                    assessmentRequest.getOldAssessmentNo());
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
            assessmentDetails.setErrorDetails(errorDetails);
        }
        return assessmentDetails;
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