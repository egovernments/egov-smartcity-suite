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
package org.egov.api.web.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.ws.rs.FormParam;
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
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



/**
 * The AssessmentService class is used as the RESTFul service to handle user request and response.
 * 
 * @author ranjit
 *
 */
@RestController
public class AssessmentService {

    @Autowired
    private PropertyExternalService propertyExternalService;

    /**
     * This method is used for handling user request for assessment details.
     * 
     * @param assessmentNumber - assessment number i.e. property id
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/assessmentDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String getAssessmentDetails(@FormParam("assessmentNo") String assessmentNo)
            throws JsonGenerationException, JsonMappingException, IOException {
        AssessmentDetails assessmentDetail = propertyExternalService.loadAssessmentDetails(assessmentNo,
                PropertyExternalService.FLAG_FULL_DETAILS);
        return getJSONResponse(assessmentDetail);
    }

    /**
     * This method is used get the property tax details.
     * 
     * @param assessmentNo - assessment no
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyTaxDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String getPropertyTaxDetails(@FormParam("assessmentNo") String assessmentNo)
            throws JsonGenerationException, JsonMappingException, IOException {
        PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
        if (null != assessmentNo) {
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
     * @param circleName - boundary as circle name
     * @param zoneName - boundary as zone name
     * @param wardName - boundary as ward name
     * @param blockName - boundary as block name
     * @param ownerName - owner name
     * @param doorNo - door number
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyTaxDetailsByBoundary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String getPropertyTaxDetails(@FormParam("circleName") String circleName,
            @FormParam("zoneName") String zoneName, @FormParam("wardName") String wardName,
            @FormParam("blockName") String blockName, @FormParam("ownerName") String ownerName,
            @FormParam("doorNo") String doorNo, @FormParam("aadhaarNumber") String aadhaarNumber,
            @FormParam("mobileNumber") String mobileNumber)
            throws JsonGenerationException, JsonMappingException, IOException {
        List<PropertyTaxDetails> propertyTaxDetailsList = propertyExternalService.getPropertyTaxDetails(circleName,
                zoneName, wardName, blockName, ownerName, doorNo, aadhaarNumber, mobileNumber);
        return getJSONResponse(propertyTaxDetailsList);
    }

    /**
     * This method is used to pay the property tax.
     * 
     * @param assessmentNo - assessment number
     * @param paymentMode - mode of payment
     * @param totalAmount - total amount paid
     * @param paidBy - payer name
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/payPropertyTax", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED, produces = MediaType.APPLICATION_JSON)
    public String payPropertyTax(@FormParam("assessmentNo") String assessmentNo,
            @FormParam("paymentMode") String paymentMode, @FormParam("totalAmount") BigDecimal totalAmount,
            @FormParam("paidBy") String paidBy) throws JsonGenerationException, JsonMappingException, IOException {
        String responseJson = new String();

        ErrorDetails errorDetails = propertyExternalService.validatePaymentDetails(assessmentNo, paymentMode,
                totalAmount, paidBy);
        if (null != errorDetails) {
            responseJson = getJSONResponse(errorDetails);
        } else {
            ReceiptDetails receiptDetails = propertyExternalService.payPropertyTax(assessmentNo, paymentMode,
                    totalAmount, paidBy);
            responseJson = getJSONResponse(receiptDetails);
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
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/payWaterTax", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED, produces = MediaType.APPLICATION_JSON)
    public String payWateTax(@FormParam("consumerNo") String consumerNo, @FormParam("paymentMode") String paymentMode,
            @FormParam("totalAmount") BigDecimal totalAmount, @FormParam("paidBy") String paidBy)
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
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/ownershipCategories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getOwnershipCategories() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> propTypeMasterDetailsList = propertyExternalService
                .getPropertyTypeMasterDetails();
        return getJSONResponse(propTypeMasterDetailsList);
    }

    /**
     * This method returns Ownership Category for the given code.
     * @param ownershipCategoryCode
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/ownershipCategoryByCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String getOwnershipCategoryByCode(@FormParam("ownershipCategoryCode") String ownershipCategoryCode)
            throws JsonGenerationException, JsonMappingException, IOException {
        PropertyTypeMaster propertyTypeMaster = propertyExternalService
                .getPropertyTypeMasterByCode(ownershipCategoryCode);
        return getJSONResponse(propertyTypeMaster);
    }

    /**
     * This method is used to get the property type based on ownership category
     * 
     * @param categoryCode - property category code
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyTypesByOwnership", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String getPropertyTypeCategoryDetails(@FormParam("ownershipCategoryCode") String ownershipCategoryCode)
            throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getPropertyTypeCategoryDetails(ownershipCategoryCode);
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get the property type based one category
     * 
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getPropertyTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getPropertyTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all the apartments and complexes.
     * 
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/apartments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getApartmentsAndComplexes() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getApartmentsAndComplexes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get reasons for create the property.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/createPropertyReasons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getCreatePropertyReasons() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getReasonsForChangeProperty(PropertyTaxConstants.PROP_CREATE_RSN);
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all localities.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/localities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getLocalities() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getLocalities();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all localities.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/boundaryByLocalityCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String getBoundaryByLocalityCode(@FormParam("localityCode") String localityCode)
            throws JsonGenerationException, JsonMappingException, IOException {
        LocalityDetails localityDetails = propertyExternalService.getLocalityDetailsByLocalityCode(localityCode);
        return getJSONResponse(localityDetails);
    }

    /**
     * This method is used to get all list of all the election wards.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/electionWards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getElectionWards() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getElectionBoundaries();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of all the enumeration blocks.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/enumerationBlocks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getEnumerationBlocks() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getEnumerationBlocks();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all types of floors.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/floorTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getFloorTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getFloorTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all type of roofs.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/roofTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getRoofTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getRoofTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of all type of walls.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/wallTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getWallTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getWallTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of all type of woods
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/woodTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getWoodTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getWoodTypes();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of floor numbers.
     *
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/floors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getFloors() throws JsonGenerationException, JsonMappingException, IOException {
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
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyClassifications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getPropertyClassifications() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
                .getBuildingClassifications();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get nature of usages of the property.
     * 
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/propertyUsages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getPropertUsages() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getNatureOfUsages();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all list of occupancies.
     * 
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/occupancyTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getOccupancyTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getOccupancies();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get all the tax exemption categories.
     * 
     * @param username - usernam credential
     * @param password - password credential
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/exemptionCategories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getTaxExemptionCategories() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getExemptionCategories();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to get drainages.
     * 
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/drainages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getDrainages() throws JsonGenerationException, JsonMappingException, IOException {
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
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/property/approverDepartments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public String getApproverDepartments() throws JsonGenerationException, JsonMappingException, IOException {
        List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getApproverDepartments();
        return getJSONResponse(mstrCodeNamePairDetailsList);
    }

    /**
     * This method is used to create property.
     * 
     * @param propertyTypeMasterCode - category of ownership code
     * @param propertyCategoryCode - property type code
     * @param apartmentCmplxCode - apartment/complex code
     * @param ownerDetails - a list of owner details
     * @param mutationReasonCode - reason for creation or mutation code
     * @param extentOfSite - extent of site
     * @param isExtentAppurtenantLand - extent appurtenant land
     * @param occupancyCertificationNo - occupancy certification number
     * @param isSuperStructure - super structure
     * @param isBuildingPlanDetails - building plan details
     * @param regdDocNo - registration document number
     * @param regdDocDate - registration document date
     * @param localityCode - locality code
     * @param street - street name
     * @param electionWardCode - election ward code
     * @param doorNo - door number
     * @param enumerationBlockCode - enumeration block code
     * @param pinCode - pin code
     * @param isCorrAddrDiff - is correspondence address different
     * @param corrAddr1 - correspondence address 1
     * @param corrAddr2 - correspondence address 2
     * @param corrPinCode - correspondence address pin code
     * @param hasLift - has lift
     * @param hasToilet - has toilet
     * @param hasWaterTap - has water tap
     * @param hasElectricity - has electricity
     * @param hasAttachedBathroom - has attached bathroom
     * @param hasWaterHarvesting - has water harvesting
     * @param floorTypeCode - floor type code
     * @param roofTypeCode - roof type code
     * @param wallTypeCode - wall type code
     * @param woodTypeCode - wood type code
     * @param floorDetails - a list of floor details
     * @param surveyNumber - survey number
     * @param pattaNumber - patta number
     * @param vacantLandArea - vacant land area value
     * @param marketValue - market value
     * @param currentCapitalValue - current capital value
     * @param completionDate - date of completion
     * @param northBoundary - north boundary
     * @param southBoundary - south boundary
     * @param eastBoundary - east boundary
     * @param westBoundary - west boundary
     * @param photoAsmntStream- photo of assessment input stream object
     * @param photoAsmntDisp- photo of assessment content disposition object
     * @param bldgPermCopyStream- building permission copy input stream object
     * @param bldgPermCopyDisp- building permission copy content disposition object
     * @param atstdCopyPropDocStream- attested copy of property document input stream object
     * @param atstdCopyPropDocDisp- attested copy of property document content disposition
     * @param nonJudcStampStream - non judicial stamp input stream object
     * @param nonJudcStampDisp - non judicial stamp content disposition object
     * @param afdvtBondStream - affidavit bond paper input stream object
     * @param afdvtBondDisp - affidavit bond paper content disposition object
     * @param deathCertCopyStream - death certificate copy input stream object
     * @param deathCertCopyDisp - death certificate copy content disposition object
     * 
     * @return - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/property/createProperty", method = RequestMethod.POST, consumes=MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
    public String createProperty(@RequestParam("propertyTypeMasterCode") String propertyTypeMasterCode,
            @RequestParam("propertyCategoryCode") String propertyCategoryCode,
            @RequestParam("apartmentCmplxCode") String apartmentCmplxCode,
            @RequestParam(value="exemptionCategoryCode", defaultValue="") String exemptionCategoryCode,
            @RequestParam("ownerDetails") String ownerDetails,
            @RequestParam("mutationReasonCode") String mutationReasonCode,
            @RequestParam("extentOfSite") String extentOfSite,
            @RequestParam("isExtentAppurtenantLand") String isExtentAppurtenantLand,
            @RequestParam("occupancyCertificationNo") String occupancyCertificationNo,
            @RequestParam("isSuperStructure") Boolean isSuperStructure,
            @RequestParam("isBuildingPlanDetails") Boolean isBuildingPlanDetails,
            @RequestParam("regdDocNo") String regdDocNo, @RequestParam("regdDocDate") String regdDocDate,
            @RequestParam("localityCode") String localityCode, @RequestParam("street") String street,
            @RequestParam("electionWardCode") String electionWardCode, @RequestParam("doorNo") String doorNo,
            @RequestParam("enumerationBlockCode") String enumerationBlockCode,
            @RequestParam("pinCode") String pinCode, @RequestParam("isCorrAddrDiff") Boolean isCorrAddrDiff,
            @RequestParam("corrAddr1") String corrAddr1, @RequestParam("corrAddr2") String corrAddr2,
            @RequestParam("corrPinCode") String corrPinCode, @RequestParam("hasLift") Boolean hasLift,
            @RequestParam("hasToilet") Boolean hasToilet, @RequestParam("hasWaterTap") Boolean hasWaterTap,
            @RequestParam("hasElectricity") Boolean hasElectricity,
            @RequestParam("hasAttachedBathroom") String hasAttachedBathroom,
            @RequestParam("hasWaterHarvesting") String hasWaterHarvesting,
            @RequestParam("floorTypeCode") String floorTypeCode, @RequestParam("roofTypeCode") String roofTypeCode,
            @RequestParam("wallTypeCode") String wallTypeCode, @RequestParam("woodTypeCode") String woodTypeCode,
            @RequestParam("floorDetails") String floorDetails, @RequestParam("surveyNumber") String surveyNumber,
            @RequestParam("pattaNumber") String pattaNumber, @RequestParam("vacantLandArea") Double vacantLandArea,
            @RequestParam("marketValue") Double marketValue,
            @RequestParam("currentCapitalValue") Double currentCapitalValue,
            @RequestParam("completionDate") String completionDate,
            @RequestParam("northBoundary") String northBoundary, @RequestParam("southBoundary") String southBoundary,
            @RequestParam("eastBoundary") String eastBoundary, @RequestParam("westBoundary") String westBoundary,
            @RequestParam(value = "photoAsmnt", required=false) MultipartFile photoAsmntDisp,
            @RequestParam(value = "bldgPermCopy", required=false) MultipartFile bldgPermCopyDisp,
            @RequestParam(value = "atstdCopyPropDoc", required=false) MultipartFile atstdCopyPropDocDisp,
            @RequestParam(value = "nonJudcStamp", required=false) MultipartFile nonJudcStampDisp,
            @RequestParam(value = "afdvtBond", required=false) MultipartFile afdvtBondDisp,
            @RequestParam(value = "deathCertCopy", required=false) MultipartFile deathCertCopyDisp)
            throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        EgovThreadLocals.setUserId(Long.valueOf("40"));
        List<FloorDetails> floorDetailsList = new ObjectMapper().readValue(floorDetails.toString(),
                new TypeReference<Collection<FloorDetails>>() {
                });
        List<OwnerDetails> ownerDetailsList = new ObjectMapper().readValue(ownerDetails.toString(),
                new TypeReference<Collection<OwnerDetails>>() {
                });
        List<Document> documents = propertyExternalService.getDocuments(photoAsmntDisp,
                bldgPermCopyDisp, atstdCopyPropDocDisp, nonJudcStampDisp, afdvtBondDisp, deathCertCopyDisp);
        NewPropertyDetails newPropertyDetails = propertyExternalService.createNewProperty(propertyTypeMasterCode,
                propertyCategoryCode, apartmentCmplxCode, ownerDetailsList, mutationReasonCode, extentOfSite,
                isExtentAppurtenantLand, occupancyCertificationNo, isSuperStructure, isBuildingPlanDetails, regdDocNo,
                regdDocDate, localityCode, street, electionWardCode, doorNo, enumerationBlockCode, pinCode,
                isCorrAddrDiff, corrAddr1, corrAddr2, corrPinCode, hasLift, hasToilet, hasWaterTap, hasElectricity,
                hasAttachedBathroom, hasWaterHarvesting, floorTypeCode, roofTypeCode, wallTypeCode, woodTypeCode,
                floorDetailsList, surveyNumber, pattaNumber, vacantLandArea, marketValue, currentCapitalValue,
                completionDate, northBoundary, southBoundary, eastBoundary, westBoundary, documents);
        return getJSONResponse(newPropertyDetails);
    }

    /**
     * This method is used to prepare jSON response.
     * 
     * @param obj - a POJO object
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
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
    @RequestMapping(value = "/property/documentTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public String getDocumentTypes() throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService.getDocumentTypes();
		return getJSONResponse(mstrCodeNamePairDetailsList);
	}
	
	/**
	 * This method is used to get reasons for mutation.
	 *
	 * @return responseJson - server response in JSON format
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/property/mutationReasons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public String getMutatioReasons() throws JsonGenerationException, JsonMappingException, IOException {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = propertyExternalService
				.getReasonsForChangeProperty(PropertyTaxConstants.PROP_MUTATION_RSN);
		return getJSONResponse(mstrCodeNamePairDetailsList);
	}
}