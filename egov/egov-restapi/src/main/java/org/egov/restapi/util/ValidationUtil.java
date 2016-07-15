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

package org.egov.restapi.util;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.model.OwnerDetails;
import org.egov.restapi.model.OwnerInformation;
import org.egov.ptis.domain.model.PayPropertyTaxDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.AssessmentRequest;
import org.egov.restapi.model.AssessmentsDetails;
import org.egov.restapi.model.CreatePropertyDetails;
import org.egov.restapi.model.PropertyTransferDetails;
import org.egov.restapi.model.SurroundingBoundaryDetails;
import org.egov.restapi.model.VacantLandDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ValidationUtil {
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    
    @Autowired
    private PropertyExternalService propertyExternalService;
    
    /**
     * Validates Property Transfer request
     * @param propertyTransferDetails
     * @return
     */
    public static ErrorDetails validatePropertyTransferRequest(PropertyTransferDetails propertyTransferDetails){
    	ErrorDetails errorDetails = null;
    	
    	String assessmentNumber = propertyTransferDetails.getAssessmentNo();
    	if(StringUtils.isBlank(assessmentNumber)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.ASSESSMENT_NO_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.ASSESSMENT_NO_REQ_MSG);
            return errorDetails;
    	}
    	
    	String mutationReasonCode = propertyTransferDetails.getMutationReasonCode();
    	if(StringUtils.isBlank(mutationReasonCode)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.MUTATION_REASON_CODE_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.MUTATION_REASON_CODE_REQ_MSG);
            return errorDetails;
    	}
    	
    	if(StringUtils.isNotBlank(mutationReasonCode) 
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_GIFT)
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_WILL)
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_RELINQUISH)
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_PARTITION)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.MUTATION_REASON_INVALID_CODE_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.MUTATION_REASON_INVALID_CODE_REQ_MSG);
            return errorDetails;
    	}
    	
    	if(mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)){
    		if(StringUtils.isBlank(propertyTransferDetails.getSaleDetails())){
    			errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.SALE_DETAILS_REQ_CODE);
                errorDetails.setErrorMessage(RestApiConstants.SALE_DETAILS_REQ_MSG);
                return errorDetails;
    		}
    	}
    	
    	if(!mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)){
    		if(StringUtils.isNotBlank(propertyTransferDetails.getSaleDetails())){
    			errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.OTHER_MUTATION_CODES_SALE_DETAILS_VALIDATION_CODE);
                errorDetails.setErrorMessage(RestApiConstants.OTHER_MUTATION_CODES_SALE_DETAILS_VALIDATION_MSG);
                return errorDetails;
    		}
    	}
    	
    	String deedNo = propertyTransferDetails.getDeedNo();
    	if(StringUtils.isBlank(deedNo)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.DEED_NO_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.DEED_NO_REQ_MSG);
            return errorDetails;
    	}
    	
    	String deedDate = propertyTransferDetails.getDeedDate();
    	if(StringUtils.isBlank(deedDate)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.DEED_DATE_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.DEED_DATE_REQ_MSG);
            return errorDetails;
    	}
    	
    	List<OwnerInformation> ownerDetailsList = propertyTransferDetails.getOwnerDetails();
        if (ownerDetailsList.isEmpty()) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.OWNER_DETAILS_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.OWNER_DETAILS_REQ_MSG);
            return errorDetails;
        } else
            for (final OwnerInformation ownerInfo : ownerDetailsList) {
                if (ownerInfo.getMobileNumber() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.MOBILE_NO_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.MOBILE_NO_REQ_MSG);
                    return errorDetails;
                }
                if (ownerInfo.getName() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.OWNER_NAME_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.OWNER_NAME_REQ_MSG);
                    return errorDetails;
                }
                if (ownerInfo.getGender() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.GENDER_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.GENDER_REQ_MSG);
                    return errorDetails;
                }
                if (ownerInfo.getGuardianRelation() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.GUARDIAN_RELATION_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.GUARDIAN_RELATION_REQ_MSG);
                    return errorDetails;
                }
                if (ownerInfo.getGuardian() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.GUARDIAN_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.GUARDIAN_REQ_MSG);
                    return errorDetails;
                }
            }
    	
    	return errorDetails;
    }
    
    public static ErrorDetails validateCreateRequest(final CreatePropertyDetails createPropDetails) {
        ErrorDetails errorDetails = null;
        final String propertyTypeMasterCode = createPropDetails.getPropertyTypeMasterCode();
        if (propertyTypeMasterCode == null || propertyTypeMasterCode.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.OWNERSHIP_CATEGORY_TYPE_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.OWNERSHIP_CATEGORY_TYPE_REQ_MSG);
            return errorDetails;
        }
        if (propertyTypeMasterCode != null
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_335)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_75)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.OWNERSHIP_CATEGORY_TYPE_INVALID_CODE);
            errorDetails.setErrorMessage(RestApiConstants.OWNERSHIP_CATEGORY_TYPE_INVALID_MSG);
            return errorDetails;
        }
        final String propertyCategoryCode = createPropDetails.getPropertyCategoryCode();
        if (propertyCategoryCode == null || propertyCategoryCode.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.PROPERTY_CATEGORY_TYPE_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.PROPERTY_CATEGORY_TYPE_REQ_MSG);
            return errorDetails;
        }
        if (propertyCategoryCode != null
                && !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_RESIDENTIAL)
                && !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL)
                && !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_MIXED)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.PROPERTY_CATEGORY_TYPE_INVALID_CODE);
            errorDetails.setErrorMessage(RestApiConstants.PROPERTY_CATEGORY_TYPE_INVALID_MSG);
            return errorDetails;
        }

        final List<OwnerDetails> ownerDetailsList = createPropDetails.getOwnerDetails();
        if (ownerDetailsList == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.OWNER_DETAILS_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.OWNER_DETAILS_REQ_MSG);
            return errorDetails;
        } else
            for (final OwnerDetails ownerDetails : ownerDetailsList) {
                if (ownerDetails.getMobileNumber() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.MOBILE_NO_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.MOBILE_NO_REQ_MSG);
                    return errorDetails;
                }
                if (ownerDetails.getName() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.OWNER_NAME_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.OWNER_NAME_REQ_MSG);
                    return errorDetails;
                }
                if (ownerDetails.getGender() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.GENDER_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.GENDER_REQ_MSG);
                    return errorDetails;
                }
                if (ownerDetails.getGuardianRelation() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.GUARDIAN_RELATION_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.GUARDIAN_RELATION_REQ_MSG);
                    return errorDetails;
                }
                if (ownerDetails.getGuardian() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.GUARDIAN_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.GUARDIAN_REQ_MSG);
                    return errorDetails;
                }
            }

        final AssessmentsDetails assessmentsDetails = createPropDetails.getAssessmentDetails();
        if (assessmentsDetails == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.ASSESSMENT_DETAILS_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.ASSESSMENT_DETAILS_REQ_MSG);
            return errorDetails;
        } else {
            if (assessmentsDetails.getMutationReasonCode() == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.REASON_FOR_CREATION_REQ_CODE);
                errorDetails.setErrorMessage(RestApiConstants.REASON_FOR_CREATION_REQ_MSG);
                return errorDetails;
            }
            if (assessmentsDetails.getExtentOfSite() == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.EXTENT_OF_SITE_REQ_CODE);
                errorDetails.setErrorMessage(RestApiConstants.EXTENT_OF_SITE_REQ_MSG);
                return errorDetails;
            }
            if (assessmentsDetails.getRegdDocNo() == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.REG_DOC_NO_REQ_CODE);
                errorDetails.setErrorMessage(RestApiConstants.REG_DOC_NO_REQ_MSG);
                return errorDetails;
            }
            if (assessmentsDetails.getRegdDocDate() == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.REG_DOC_DATE_REQ_CODE);
                errorDetails.setErrorMessage(RestApiConstants.REG_DOC_DATE_REQ_MSG);
                return errorDetails;
            }
            if (assessmentsDetails.getIsExtentAppurtenantLand()) {
                final VacantLandDetails vacantLandDetails = createPropDetails.getVacantLandDetails();
                if (vacantLandDetails == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.VACANT_LAND_DETAILS_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.VACANT_LAND_DETAILS_REQ_MSG);
                    return errorDetails;
                } else {
                    if (vacantLandDetails.getSurveyNumber() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.SURVEY_NO_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.SURVEY_NO_REQ_MSG);
                        return errorDetails;
                    }
                    if (vacantLandDetails.getPattaNumber() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.PATTA_NO_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.PATTA_NO_REQ_MSG);
                        return errorDetails;
                    }
                    if (vacantLandDetails.getVacantLandArea() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.VACANT_LAND_AREA_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.VACANT_LAND_AREA_REQ_MSG);
                        return errorDetails;
                    }
                    if (vacantLandDetails.getMarketValue() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.MARKET_AREA_VALUE_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.MARKET_AREA_VALUE_REQ_MSG);
                        return errorDetails;
                    }
                    if (vacantLandDetails.getCurrentCapitalValue() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.CURRENT_CAPITAL_VALUE_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.CURRENT_CAPITAL_VALUE_REQ_MSG);
                        return errorDetails;
                    }
                    if (vacantLandDetails.getEffectiveDate() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.EFFECTIVE_DATE_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.EFFECTIVE_DATE_REQ_MSG);
                        return errorDetails;
                    }
                }

                final SurroundingBoundaryDetails surBoundaryDetails = createPropDetails.getSurroundingBoundaryDetails();
                if (surBoundaryDetails == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.SURROUNDING_BOUNDARY_DETAILS_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.SURROUNDING_BOUNDARY_DETAILS_REQ_MSG);
                    return errorDetails;
                } else {
                    if (surBoundaryDetails.getNorthBoundary() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.NORTH_BOUNDARY_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.NORTH_BOUNDARY_REQ_MSG);
                        return errorDetails;
                    }
                    if (surBoundaryDetails.getSouthBoundary() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.SOUTH_BOUNDARY_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.SOUTH_BOUNDARY_REQ_MSG);
                        return errorDetails;
                    }
                    if (surBoundaryDetails.getEastBoundary() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.EAST_BOUNDARY_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.EAST_BOUNDARY_REQ_MSG);
                        return errorDetails;
                    }
                    if (surBoundaryDetails.getWestBoundary() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(RestApiConstants.WEST_BOUNDARY_REQ_CODE);
                        errorDetails.setErrorMessage(RestApiConstants.WEST_BOUNDARY_REQ_MSG);
                        return errorDetails;
                    }
                }
            }
        }

        final List<FloorDetails> floorDetailsList = createPropDetails.getFloorDetails();
        if (floorDetailsList == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.FLOOR_DETAILS_REQ_CODE);
            errorDetails.setErrorMessage(RestApiConstants.FLOOR_DETAILS_REQ_MSG);
            return errorDetails;
        } else
            for (final FloorDetails floorDetails : floorDetailsList) {
                if (floorDetails.getFloorNoCode() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.FLOOR_NO_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.FLOOR_NO_REQ_MSG);
                    return errorDetails;
                }
                if (floorDetails.getBuildClassificationCode() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.CLASSIFICATION_OF_BUILDING_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.CLASSIFICATION_OF_BUILDING_REQ_MSG);
                    return errorDetails;
                }
                if (floorDetails.getNatureOfUsageCode() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.NATURE_OF_USAGES_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.NATURE_OF_USAGES_REQ_MSG);
                    return errorDetails;
                }
                if (floorDetails.getOccupancyCode() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.OCCUPANCY_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.OCCUPANCY_REQ_MSG);
                    return errorDetails;
                }
                if (floorDetails.getConstructionDate() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.CONSTRUCTION_DATE_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.CONSTRUCTION_DATE_REQ_MSG);
                    return errorDetails;
                }
                if (floorDetails.getPlinthArea() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.PLINTH_AREA_REQ_CODE);
                    errorDetails.setErrorMessage(RestApiConstants.PLINTH_AREA_REQ_MSG);
                    return errorDetails;
                }
            }
        return errorDetails;
    }
    public  ErrorDetails validatePaymentDetails(final PayPropertyTaxDetails payPropTaxDetails, boolean isMutationFeePayment) {
        ErrorDetails errorDetails = null;
        if (payPropTaxDetails.getAssessmentNo() == null || payPropTaxDetails.getAssessmentNo().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED);
        } else {
            if (payPropTaxDetails.getAssessmentNo().trim().length() > 0 && payPropTaxDetails.getAssessmentNo().trim().length() < 10) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN);
            }
            if (!basicPropertyDAO.isAssessmentNoExist(payPropTaxDetails.getAssessmentNo())) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
            }
            BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(payPropTaxDetails.getAssessmentNo());
            if(basicProperty != null){
            	Property property = basicProperty.getProperty();
            	if(property != null && property.getIsExemptedFromTax()){
            		errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_EXEMPTED_PROPERTY);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_EXEMPTED_PROPERTY);
            	}
            }
        }
        
        if(isMutationFeePayment){
        	if(!propertyExternalService.validateMutationFee(payPropTaxDetails.getAssessmentNo(), payPropTaxDetails.getPaymentAmount())){
        		errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_EXCESS_MUTATION_FEE);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_EXCESS_MUTATION_FEE);
        	}
        	PropertyMutation propertyMutation = propertyExternalService.getLatestPropertyMutationByAssesmentNo(payPropTaxDetails.getAssessmentNo());
        	if(propertyMutation == null){
        		errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_MUTATION_INVALID);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_MUTATION_INVALID);
        	}
        }
        
        if (payPropTaxDetails.getTransactionId() == null || "".equals(payPropTaxDetails.getTransactionId()) ){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_REQUIRED);
        }
        else if(payPropTaxDetails.getTransactionId()!=null || !"".equals(payPropTaxDetails.getTransactionId())){
           BillReceiptInfo billReceiptList=propertyExternalService.validateTransanctionIdPresent(payPropTaxDetails.getTransactionId());
        if(billReceiptList!=null)
        {
             errorDetails = new ErrorDetails();
             errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_VALIDATE);
             errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_VALIDATE);
       
        }
        }
        if (payPropTaxDetails.getPaymentMode() == null || payPropTaxDetails.getPaymentMode().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
        } else if (!PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
                &&  !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
        }

        if(payPropTaxDetails.getPaymentMode() != null
        		&& (
                PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
                || PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim()))) 
          {
            if (payPropTaxDetails.getChqddNo() == null || payPropTaxDetails.getChqddNo().trim().length() == 0) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_NO_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_NO_REQUIRED);
            }else

                if (payPropTaxDetails.getChqddDate() == null ) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_DATE_REQUIRED);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_DATE_REQUIRED);
                }else

                    if (payPropTaxDetails.getBankName() == null || payPropTaxDetails.getBankName().trim().length() == 0) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BANKNAME_REQUIRED);
                        errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BANKNAME_REQUIRED);
                    }else

                        if (payPropTaxDetails.getBranchName() == null ) {
                            errorDetails = new ErrorDetails();
                            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BRANCHNAME_REQUIRED);
                            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BRANCHNAME_REQUIRED);
                        }

          }
        return errorDetails;
    }

    /**
     * Validates Assessment Details request
     * @param assessmentReq
     * @return ErrorDetails
     */
    public ErrorDetails validateAssessmentDetailsRequest(AssessmentRequest assessmentRequest){
    	ErrorDetails errorDetails = null;
    	
    	if (!basicPropertyDAO.isAssessmentNoExist(assessmentRequest.getAssessmentNo())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
        }
    	PropertyMutation propertyMutation = propertyExternalService.getLatestPropertyMutationByAssesmentNo(assessmentRequest.getAssessmentNo());
    	if(propertyMutation == null){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_MUTATION_INVALID);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_MUTATION_INVALID);
    	}
    	return errorDetails;
    }

}
