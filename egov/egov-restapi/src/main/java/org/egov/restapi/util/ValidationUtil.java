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

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.BLOCK;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_USAGE_RESIDENCE;
import static org.egov.restapi.constants.RestApiConstants.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.ptis.client.service.calculator.APTaxCalculator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.model.OwnerInformation;
import org.egov.ptis.domain.model.PayPropertyTaxDetails;
import org.egov.ptis.domain.model.TaxCalculatorRequest;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.master.service.StructureClassificationService;
import org.egov.restapi.model.AssessmentRequest;
import org.egov.restapi.model.AssessmentsDetails;
import org.egov.restapi.model.CreatePropertyDetails;
import org.egov.restapi.model.DocumentTypeDetails;
import org.egov.restapi.model.PropertyAddressDetails;
import org.egov.restapi.model.PropertyTransferDetails;
import org.egov.restapi.model.SurroundingBoundaryDetails;
import org.egov.restapi.model.VacantLandDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationUtil {
	private static final String NO_APPROVAL = "No Approval";

	@Autowired
	private BasicPropertyDAO basicPropertyDAO;

	@Autowired
	private PropertyExternalService propertyExternalService;

	@Autowired
	PropertyUsageService propertyUsageService;

	@Autowired
	StructureClassificationService structureClassificationService;

	@Autowired
	PropertyTaxUtil propertyTaxUtil;

	@Autowired
	APTaxCalculator aPTaxCalculator;
	
	@Autowired
    private LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepo;
	
	@Autowired
	private VacantLandPlotAreaRepository vacantLandPlotAreaRepository;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PINCODE_PATTERN = "^[1-9][0-9]{5}$";
	private static final String GUARDIAN_PATTERN = "^[\\p{L} .'-]+$";
	private static final String DIGITS_ONLY = "^(0|[1-9][0-9]*)$";
	private static final String CAMEL_CASE_PATTERN = "([A-Z]+[a-z]+\\w+)+";
	private static final String DIGITS_FLOAT_INT_DBL = "[-+]?[0-9]*\\.?[0-9]+";
	private static final String UPPERCASE_PATTERN = "([A-Z])";

	/**
	 * Validates Property Transfer request
	 * 
	 * @param propertyTransferDetails
	 * @return
	 */
	public static ErrorDetails validatePropertyTransferRequest(final PropertyTransferDetails propertyTransferDetails) {
		ErrorDetails errorDetails = null;

		final String assessmentNumber = propertyTransferDetails.getAssessmentNo();
		if (StringUtils.isBlank(assessmentNumber)) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(ASSESSMENT_NO_REQ_CODE);
			errorDetails.setErrorMessage(ASSESSMENT_NO_REQ_MSG);
			return errorDetails;
		}

		final String mutationReasonCode = propertyTransferDetails.getMutationReasonCode();
		if (StringUtils.isBlank(mutationReasonCode)) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(MUTATION_REASON_CODE_REQ_CODE);
			errorDetails.setErrorMessage(MUTATION_REASON_CODE_REQ_MSG);
			return errorDetails;
		}

		if (StringUtils.isNotBlank(mutationReasonCode)
				&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_GIFT)
				&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_WILL)
				&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)
				&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_RELINQUISH)
				&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_PARTITION)) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(MUTATION_REASON_INVALID_CODE_REQ_CODE);
			errorDetails.setErrorMessage(MUTATION_REASON_INVALID_CODE_REQ_MSG);
			return errorDetails;
		}

		if (mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)
				&& StringUtils.isBlank(propertyTransferDetails.getSaleDetails())) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(SALE_DETAILS_REQ_CODE);
			errorDetails.setErrorMessage(SALE_DETAILS_REQ_MSG);
			return errorDetails;
		}

		if (!mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)
				&& StringUtils.isNotBlank(propertyTransferDetails.getSaleDetails())) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(OTHER_MUTATION_CODES_SALE_DETAILS_VALIDATION_CODE);
			errorDetails.setErrorMessage(OTHER_MUTATION_CODES_SALE_DETAILS_VALIDATION_MSG);
			return errorDetails;
		}

		final String deedNo = propertyTransferDetails.getDeedNo();
		if (StringUtils.isBlank(deedNo)) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(DEED_NO_REQ_CODE);
			errorDetails.setErrorMessage(DEED_NO_REQ_MSG);
			return errorDetails;
		}

		final String deedDate = propertyTransferDetails.getDeedDate();
		if (StringUtils.isBlank(deedDate)) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(DEED_DATE_REQ_CODE);
			errorDetails.setErrorMessage(DEED_DATE_REQ_MSG);
			return errorDetails;
		}

		final List<OwnerInformation> ownerDetailsList = propertyTransferDetails.getOwnerDetails();
		if (ownerDetailsList.isEmpty()) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(OWNER_DETAILS_REQ_CODE);
			errorDetails.setErrorMessage(OWNER_DETAILS_REQ_MSG);
			return errorDetails;
		} else
			for (final OwnerInformation ownerInfo : ownerDetailsList) {
				if (ownerInfo.getMobileNumber() == null) {
					errorDetails = new ErrorDetails();
					errorDetails.setErrorCode(MOBILE_NO_REQ_CODE);
					errorDetails.setErrorMessage(MOBILE_NO_REQ_MSG);
					return errorDetails;
				}
				if (ownerInfo.getName() == null) {
					errorDetails = new ErrorDetails();
					errorDetails.setErrorCode(OWNER_NAME_REQ_CODE);
					errorDetails.setErrorMessage(OWNER_NAME_REQ_MSG);
					return errorDetails;
				}
				if (ownerInfo.getGender() == null) {
					errorDetails = new ErrorDetails();
					errorDetails.setErrorCode(GENDER_REQ_CODE);
					errorDetails.setErrorMessage(GENDER_REQ_MSG);
					return errorDetails;
				}
				if (ownerInfo.getGuardianRelation() == null) {
					errorDetails = new ErrorDetails();
					errorDetails.setErrorCode(GUARDIAN_RELATION_REQ_CODE);
					errorDetails.setErrorMessage(GUARDIAN_RELATION_REQ_MSG);
					return errorDetails;
				}
				if (ownerInfo.getGuardian() == null) {
					errorDetails = new ErrorDetails();
					errorDetails.setErrorCode(GUARDIAN_REQ_CODE);
					errorDetails.setErrorMessage(GUARDIAN_REQ_MSG);
					return errorDetails;
				}
			}
		return errorDetails;
	}

	public ErrorDetails validateCreateRequest(final CreatePropertyDetails createPropDetails, final String mode)
			throws ParseException {
		ErrorDetails errorDetails = new ErrorDetails();
		final String propertyTypeMasterCode = createPropDetails.getPropertyTypeMasterCode();

		final DocumentTypeDetails documentTypeDetails = createPropDetails.getDocumentTypeDetails();
		if (StringUtils.isBlank(propertyTypeMasterCode)) {
			errorDetails.setErrorCode(OWNERSHIP_CATEGORY_TYPE_REQ_CODE);
			errorDetails.setErrorMessage(OWNERSHIP_CATEGORY_TYPE_REQ_MSG);
			return errorDetails;
		} else if (propertyTypeMasterCode != null
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_335)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_75)) {
			errorDetails.setErrorCode(OWNERSHIP_CATEGORY_TYPE_INVALID_CODE);
			errorDetails.setErrorMessage(OWNERSHIP_CATEGORY_TYPE_INVALID_MSG);
			return errorDetails;
		}
		final String propertyCategoryCode = createPropDetails.getCategoryCode();
		if (StringUtils.isBlank(propertyCategoryCode)) {
			errorDetails.setErrorCode(PROPERTY_CATEGORY_TYPE_REQ_CODE);
			errorDetails.setErrorMessage(PROPERTY_CATEGORY_TYPE_REQ_MSG);
			return errorDetails;
		} else if (propertyCategoryCode != null
				&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_VACANT_LAND)
				&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_STATE_GOVT)
				&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_CENTRAL_GOVT)
				&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_RESIDENTIAL)
				&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL)
				&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_MIXED)) {
			errorDetails.setErrorCode(PROPERTY_CATEGORY_TYPE_INVALID_CODE);
			errorDetails.setErrorMessage(PROPERTY_CATEGORY_TYPE_INVALID_MSG);
			return errorDetails;
		}
		if (PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND.equalsIgnoreCase(propertyTypeMasterCode)
				&& (PropertyTaxConstants.CATEGORY_RESIDENTIAL.equalsIgnoreCase(propertyCategoryCode)
						|| PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL.equalsIgnoreCase(propertyCategoryCode)
						|| PropertyTaxConstants.CATEGORY_MIXED.equalsIgnoreCase(propertyCategoryCode))) {
			errorDetails.setErrorCode(CATEGORY_CANT_BE_RESIDENTIAL);
			errorDetails.setErrorMessage(CATEGORY_CANT_BE_RESIDENTIAL_MSG);
			return errorDetails;
		}
		Double areaOfPlot = 0.0;
		if (mode.equals(PropertyTaxConstants.PROPERTY_MODE_CREATE)) {
			// Owner details validations
			final List<OwnerInformation> ownerDetailsList = createPropDetails.getOwnerDetails();
			if(documentTypeDetails != null && !DOCUMENT_NAME_NOTARY_DOCUMENT.equals(documentTypeDetails.getDocumentName()))
				errorDetails = validateOwnerDetails(errorDetails, ownerDetailsList);
				if (errorDetails != null && errorDetails.getErrorCode() != null)
					return errorDetails;
		}
		// Assessment level validations
		final AssessmentsDetails assessmentsDetails = createPropDetails.getAssessmentDetails();
		if (assessmentsDetails == null && !PropertyTaxConstants.PROPERTY_MODE_MODIFY.equalsIgnoreCase(mode)) {
			errorDetails.setErrorCode(ASSESSMENT_DETAILS_REQ_CODE);
			errorDetails.setErrorMessage(ASSESSMENT_DETAILS_REQ_MSG);
			return errorDetails;
		} else {
			if (assessmentsDetails != null && StringUtils.isBlank(assessmentsDetails.getMutationReasonCode())
					&& !PropertyTaxConstants.PROPERTY_MODE_MODIFY.equalsIgnoreCase(mode)) {
				errorDetails.setErrorCode(REASON_FOR_CREATION_REQ_CODE);
				errorDetails.setErrorMessage(REASON_FOR_CREATION_REQ_MSG);
				return errorDetails;

			}
			if (assessmentsDetails != null
					&& (!propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND))) {
				if (StringUtils.isBlank(assessmentsDetails.getExtentOfSite())) {
					errorDetails.setErrorCode(EXTENT_OF_SITE_REQ_CODE);
					errorDetails.setErrorMessage(EXTENT_OF_SITE_REQ_MSG);
					return errorDetails;
				} else if (Double.valueOf(assessmentsDetails.getExtentOfSite()) == 0) {
					errorDetails.setErrorCode(AREA_GREATER_THAN_ZERO_CODE);
					errorDetails.setErrorMessage(AREA_GREATER_THAN_ZERO_MSG);
					return errorDetails;
				}
				areaOfPlot = Double.valueOf(assessmentsDetails.getExtentOfSite());
			}
			// Vacant Land validations
			if (propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)) {
				errorDetails = validateVacantLandDetails(createPropDetails, errorDetails);
				if (errorDetails != null && errorDetails.getErrorCode() != null)
					return errorDetails;
			}
		}

		// Property Address validations
		final PropertyAddressDetails propertyAddressDetails = createPropDetails.getPropertyAddressDetails();
		if (!mode.equals(PropertyTaxConstants.PROPERTY_MODE_MODIFY))
			if (propertyAddressDetails == null) {
				errorDetails.setErrorCode(ADDRESS_DETAILS_REQ_CODE);
				errorDetails.setErrorMessage(ADDRESS_DETAILS_REQ_MSG);
				return errorDetails;
			} else {
				errorDetails = validateBoundaries(propertyAddressDetails, errorDetails, mode);
				if (errorDetails != null && errorDetails.getErrorCode() != null)
					return errorDetails;
				else {
					final Boundary locality = propertyExternalService.getBoundarybyboundaryNumberTypeHierarchy(
							propertyAddressDetails.getLocalityNum(), propertyExternalService
									.getBoundaryTypeByNameandHierarchy(LOCALITY_BNDRY_TYPE, LOCATION_HIERARCHY_TYPE));
					final Boundary block = propertyExternalService.getBoundarybyboundaryNumberTypeHierarchy(
							propertyAddressDetails.getBlockNum(),
							propertyExternalService.getBoundaryTypeByNameandHierarchy(BLOCK, REVENUE_HIERARCHY_TYPE));
					final Boundary ward = propertyExternalService.getBoundarybyboundaryNumberTypeHierarchy(
							propertyAddressDetails.getWardNum(),
							propertyExternalService.getBoundaryTypeByNameandHierarchy(WARD, REVENUE_HIERARCHY_TYPE));
					errorDetails = validateCrossHierarchyMapping(locality, ward, block, errorDetails);
					if (errorDetails != null && errorDetails.getErrorCode() != null)
						return errorDetails;
					else if (StringUtils.isBlank(propertyAddressDetails.getPinCode())) {
						errorDetails.setErrorCode(PIN_CODE_REQ_CODE);
						errorDetails.setErrorMessage(PIN_CODE_REQ_MSG);
						return errorDetails;
					} else {
						final Pattern pattern = Pattern.compile(PINCODE_PATTERN);
						final Matcher matcher = pattern.matcher(propertyAddressDetails.getPinCode());
						if (!matcher.matches()) {
							errorDetails.setErrorCode(PIN_CODE_ALPHASPL_ERROR_CODE);
							errorDetails.setErrorMessage(PIN_CODE_ALPHASPL_ERROR_MSG);
							return errorDetails;
						}
					}
				}
			}

		if (!propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)) {
			final List<FloorDetails> floorDetailsList = createPropDetails.getFloorDetails();
			if (floorDetailsList == null || floorDetailsList.isEmpty()) {
				errorDetails.setErrorCode(FLOOR_DETAILS_REQ_CODE);
				errorDetails.setErrorMessage(FLOOR_DETAILS_REQ_MSG);
				return errorDetails;
			} else
				for (final FloorDetails floorDetails : floorDetailsList) {
					if (StringUtils.isBlank(floorDetails.getFloorNoCode())) {
						errorDetails.setErrorCode(FLOOR_NO_REQ_CODE);
						errorDetails.setErrorMessage(FLOOR_NO_REQ_MSG);
						return errorDetails;
					}
					if (StringUtils.isBlank(floorDetails.getBuildClassificationCode())) {
						errorDetails.setErrorCode(CLASSIFICATION_OF_BUILDING_REQ_CODE);
						errorDetails.setErrorMessage(CLASSIFICATION_OF_BUILDING_REQ_MSG);
						return errorDetails;
					}
					final Boolean classification = structureClassificationService
							.isActiveClassification(floorDetails.getBuildClassificationCode());
					if (classification == null) {
						errorDetails.setErrorCode(CLASSIFICATION_CODE_DOESNT_EXIST);
						errorDetails.setErrorMessage(CLASSIFICATION_CODE_DOESNT_EXIST_MSG);
						return errorDetails;
					}
					if (!classification) {
						errorDetails.setErrorCode(INACTIVE_CLASSIFICATION_CODE);
						errorDetails.setErrorMessage(INACTIVE_CLASSIFICATION_REQ_MSG);
						return errorDetails;
					}
					if (StringUtils.isBlank(floorDetails.getNatureOfUsageCode())) {
						errorDetails.setErrorCode(NATURE_OF_USAGES_REQ_CODE);
						errorDetails.setErrorMessage(NATURE_OF_USAGES_REQ_MSG);
						return errorDetails;
					}
					if (propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL)
							&& propertyUsageService.getUsageByCode(floorDetails.getNatureOfUsageCode()).getUsageName()
									.equalsIgnoreCase(NATURE_OF_USAGE_RESIDENCE)) {
						errorDetails.setErrorCode(NATUREOFUSAGE_CANT_BE_RESIDENTIAL);
						errorDetails.setErrorMessage(NATUREOFUSAGE_CANT_BE_RESIDENTIAL_MSG);
						return errorDetails;
					}
					final Boolean usage = propertyUsageService.isActiveUsage(floorDetails.getNatureOfUsageCode());
					if (usage == null) {
						errorDetails.setErrorCode(USAGE_CODE_DOESNT_EXIST);
						errorDetails.setErrorMessage(USAGE_CODE_DOESNT_EXIST_MSG);
						return errorDetails;
					}
					if (!usage) {
						errorDetails.setErrorCode(INACTIVE_USAGE_CODE);
						errorDetails.setErrorMessage(INACTIVE_USAGE_REQ_MSG);
						return errorDetails;
					}
					if (!floorDetails.getNatureOfUsageCode().equalsIgnoreCase(PropertyTaxConstants.PROPTYPE_RESD)
							&& StringUtils.isBlank(floorDetails.getFirmName())) {
						errorDetails.setErrorCode(FIRMNAME_REQ_CODE);
						errorDetails.setErrorMessage(FIRMNAME_REQ_MSG);
						return errorDetails;
					}
					if (StringUtils.isBlank(floorDetails.getOccupancyCode())) {
						errorDetails.setErrorCode(OCCUPANCY_REQ_CODE);
						errorDetails.setErrorMessage(OCCUPANCY_REQ_MSG);
						return errorDetails;
					}
					if (StringUtils.isBlank(floorDetails.getConstructionDate())) {
						errorDetails.setErrorCode(CONSTRUCTION_DATE_REQ_CODE);
						errorDetails.setErrorMessage(CONSTRUCTION_DATE_REQ_MSG);
						return errorDetails;
					}
					if (StringUtils.isBlank(floorDetails.getOccupancyDate())) {
						errorDetails.setErrorCode(OCCUPANCY_DATE_REQ_CODE);
						errorDetails.setErrorMessage(OCCUPANCY_DATE_REQ_MSG);
						return errorDetails;
					}
					String zoneNo = null;
					if (!mode.equals(PropertyTaxConstants.PROPERTY_MODE_MODIFY))
						zoneNo = propertyAddressDetails.getZoneNum();
					else {
						final BasicProperty basicProperty = basicPropertyDAO
								.getBasicPropertyByPropertyID(createPropDetails.getAssessmentNumber());
						zoneNo = basicProperty.getPropertyID().getZone().getBoundaryNum().toString();
					}
					if (propertyExternalService.isActiveUnitRateExists(floorDetails, zoneNo,
							floorDetails.getNatureOfUsageCode(), floorDetails.getBuildClassificationCode())) {
						errorDetails.setErrorCode(INACTIVE_UNIT_RATES_CODE);
						errorDetails.setErrorMessage(INACTIVE_UNIT_RATES_REQ_MSG);
						return errorDetails;
					}

					final Date constructionDate = propertyExternalService
							.convertStringToDate(floorDetails.getConstructionDate());
					final Date occupancyDate = propertyExternalService
							.convertStringToDate(floorDetails.getOccupancyDate());
					if (constructionDate.after(new Date()) || occupancyDate.after(new Date())) {
						errorDetails.setErrorCode(FUTURE_DATES_NOT_ALLOWED_CODE);
						errorDetails.setErrorMessage(FUTURE_DATES_NOT_ALLOWED_MSG);
						return errorDetails;
					}
					if (occupancyDate.before(constructionDate)) {
						errorDetails.setErrorCode(OCCUPANCY_DATE_BEFORE_CONSTRUCTION_DATE_CODE);
						errorDetails.setErrorMessage(OCCUPANCY_DATE_BEFORE_CONSTRUCTION_DATE_MSG);
						return errorDetails;
					}
					if (floorDetails.getUnstructuredLand() == null) {
						errorDetails.setErrorCode(UNSTRUCTURED_LAND_REQ_CODE);
						errorDetails.setErrorMessage(UNSTRUCTURED_LAND_REQ_MSG);
						return errorDetails;
					} else if (floorDetails.getUnstructuredLand()) {

						if (floorDetails.getPlinthArea() == null) {
							errorDetails.setErrorCode(PLINTH_AREA_REQ_CODE);
							errorDetails.setErrorMessage(PLINTH_AREA_REQ_MSG);
							return errorDetails;
						}
						if (Double.valueOf(floorDetails.getPlinthArea()) == 0.0) {
							errorDetails.setErrorCode(PLINTH_AREA_GREATER_THAN_ZERO_CODE);
							errorDetails.setErrorMessage(PLINTH_AREA_GREATER_THAN_ZERO_MSG);
							return errorDetails;
						}
						if (Double.valueOf(floorDetails.getPlinthArea()) > areaOfPlot) {
							errorDetails.setErrorCode(PLINTH_AREA_GREATER_THAN_PLOT_AREA_CODE);
							errorDetails.setErrorMessage(PLINTH_AREA_GREATER_THAN_PLOT_AREA_MSG);
							return errorDetails;
						}
					} else {

						if (floorDetails.getPlinthLength() == null) {
							errorDetails.setErrorCode(PLINTH_LENGTH_REQ_CODE);
							errorDetails.setErrorMessage(PLINTH_LENGTH_REQ_MSG);
							return errorDetails;
						}
						if (Float.valueOf(floorDetails.getPlinthLength()) == 0.0) {
							errorDetails.setErrorCode(PLINTH_LENGTH_GREATER_THAN_ZERO_CODE);
							errorDetails.setErrorMessage(PLINTH_LENGTH_GREATER_THAN_ZERO_MSG);
							return errorDetails;
						}
						if (floorDetails.getPlinthBreadth() == null) {
							errorDetails.setErrorCode(PLINTH_BREADTH_REQ_CODE);
							errorDetails.setErrorMessage(PLINTH_BREADTH_REQ_MSG);
							return errorDetails;
						}
						if (Float.valueOf(floorDetails.getPlinthBreadth()) == 0.0) {
							errorDetails.setErrorCode(PLINTH_AREA_GREATER_THAN_ZERO_CODE);
							errorDetails.setErrorMessage(PLINTH_AREA_GREATER_THAN_ZERO_MSG);
							return errorDetails;
						}
					}
					if (StringUtils.isNotBlank(documentTypeDetails.getDocumentDate()) &&
							!mode.equals(PropertyTaxConstants.PROPERTY_MODE_MODIFY)
							&& propertyExternalService.convertStringToDate(documentTypeDetails.getDocumentDate()).after(
									propertyExternalService.convertStringToDate(floorDetails.getOccupancyDate()))) {
						errorDetails.setErrorCode(DOCUMENT_DATE_GREATER_CONSTRUCTION_DATE_CODE);
						errorDetails.setErrorMessage(DOCUMENT_DATE_GREATER_CONSTRUCTION_DATE_REQ_MSG);
						return errorDetails;
					}
					if (StringUtils.isNotBlank(floorDetails.getBuildingPermissionNo())) {
						if (StringUtils.isBlank(floorDetails.getBuildingPermissionDate())) {
							errorDetails.setErrorCode(BLD_PERMISSION_DATE_REQ);
							errorDetails.setErrorMessage(BLD_PERMISSION_DATE_REQ_MSG);
							return errorDetails;
						}
						if (floorDetails.getBuildingPlanPlinthArea() == null) {
							errorDetails.setErrorCode(BLD_PLAN_PLINTHAREA_REQ);
							errorDetails.setErrorMessage(BLD_PLAN_PLINTHAREA_REQ_MSG);
							return errorDetails;
						}
					}
				}
		}

		// Document Type level validations
		if (!mode.equals(PropertyTaxConstants.PROPERTY_MODE_MODIFY)) {
			final VacantLandDetails vlt = createPropDetails.getVacantLandDetails();
			if (documentTypeDetails == null) {
				errorDetails.setErrorCode(DOCUMENT_TYPE_DETAILS_REQ_CODE);
				errorDetails.setErrorMessage(DOCUMENT_TYPE_DETAILS_REQ_MSG);
				return errorDetails;
			} else if (StringUtils.isBlank(documentTypeDetails.getDocumentName())) {
				errorDetails.setErrorCode(DOCUMENT_TYPE_DETAILS_NAME_REQ_CODE);
				errorDetails.setErrorMessage(DOCUMENT_TYPE_DETAILS_NAME_REQ_MSG);
				return errorDetails;
			} else {
				if (!documentTypeDetails.getDocumentName().equals(DOCUMENT_NAME_NOTARY_DOCUMENT)) {
					if (StringUtils.isBlank(documentTypeDetails.getDocumentNumber())) {
						errorDetails.setErrorCode(REG_DOC_NO_REQ_CODE);
						errorDetails.setErrorMessage(REG_DOC_NO_REQ_MSG);
						return errorDetails;
					}

					if (StringUtils.isBlank(documentTypeDetails.getDocumentDate())) {
						errorDetails.setErrorCode(REG_DOC_DATE_REQ_CODE);
						errorDetails.setErrorMessage(REG_DOC_DATE_REQ_MSG);
						return errorDetails;
					} else if (propertyExternalService.convertStringToDate(documentTypeDetails.getDocumentDate())
							.after(new Date())) {
						errorDetails.setErrorCode(FUTURE_DATES_NOT_ALLOWED_CODE);
						errorDetails.setErrorMessage(FUTURE_DATES_NOT_ALLOWED_MSG);
						return errorDetails;
					}
					if (propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)
							&& propertyExternalService.convertStringToDate(documentTypeDetails.getDocumentDate())
									.before(propertyExternalService.convertStringToDate(vlt.getEffectiveDate()))) {
						errorDetails.setErrorCode(DOCUMENT_DATE_LESS_EFFECTIVE_DATE_CODE);
						errorDetails.setErrorMessage(DOCUMENT_DATE_LESS_EFFECTIVE_DATE_REQ_MSG);
						return errorDetails;
					}
				}
				if (documentTypeDetails.getDocumentName().equals(DOCUMENT_NAME_PATTA_CERTIFICATE))
					if (StringUtils.isBlank(documentTypeDetails.getMroProceedingNumber())) {
						errorDetails.setErrorCode(MRO_PROCC_NO_REQ_CODE);
						errorDetails.setErrorMessage(MRO_PROCC_NO_REQ_MSG);
						return errorDetails;
					} else if (StringUtils.isBlank(documentTypeDetails.getMroProceedingDate())) {
						errorDetails.setErrorCode(MRO_PROCC_DATE_REQ_CODE);
						errorDetails.setErrorMessage(MRO_PROCC_DATE_REQ_MSG);
						return errorDetails;
					} else if (documentTypeDetails.getDocumentName().equals(DOCUMENT_NAME_DECREE_BY_CIVILCOURT)
							&& StringUtils.isBlank(documentTypeDetails.getCourtName())) {
						errorDetails.setErrorCode(COURT_NAME_REQ_CODE);
						errorDetails.setErrorMessage(COURT_NAME_REQ_MSG);
						return errorDetails;
					}
			}
		}
		return errorDetails;
	}


	public Date convertStringToDate(final String dateInString) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.parse(dateInString);
	}

	/**
	 * Validates Vacant Land details
	 *
	 * @param createPropDetails
	 * @param errorDetails
	 * @return ErrorDetails
	 * @throws ParseException
	 */
	public ErrorDetails validateVacantLandDetails(final CreatePropertyDetails createPropDetails,
			ErrorDetails errorDetails) {
		final VacantLandDetails vacantLandDetails = createPropDetails.getVacantLandDetails();
		if (vacantLandDetails == null) {
			errorDetails.setErrorCode(VACANT_LAND_DETAILS_REQ_CODE);
			errorDetails.setErrorMessage(VACANT_LAND_DETAILS_REQ_MSG);
			return errorDetails;
		} else {

			if (StringUtils.isBlank(vacantLandDetails.getSurveyNumber())) {
				errorDetails.setErrorCode(SURVEY_NO_REQ_CODE);
				errorDetails.setErrorMessage(SURVEY_NO_REQ_MSG);
				return errorDetails;
			} else if (StringUtils.isBlank(vacantLandDetails.getPattaNumber())) {
				errorDetails.setErrorCode(PATTA_NO_REQ_CODE);
				errorDetails.setErrorMessage(PATTA_NO_REQ_MSG);
				return errorDetails;
			}
			if (vacantLandDetails.getVacantLandArea() == null) {
				errorDetails.setErrorCode(VACANT_LAND_AREA_REQ_CODE);
				errorDetails.setErrorMessage(VACANT_LAND_AREA_REQ_MSG);
				return errorDetails;
			} else if (Float.valueOf(vacantLandDetails.getVacantLandArea()) == 0.0) {
				errorDetails.setErrorCode(AREA_GREATER_THAN_ZERO_CODE);
				errorDetails.setErrorMessage(AREA_GREATER_THAN_ZERO_MSG);
				return errorDetails;
			} else {
				final Pattern pattern = Pattern.compile(DIGITS_FLOAT_INT_DBL);
				final Matcher matcher = pattern.matcher(Float.toString(vacantLandDetails.getVacantLandArea()));
				if (!matcher.matches()) {
					errorDetails.setErrorCode(VL_AREA_NUMBER_REQ_CODE);
					errorDetails.setErrorMessage(VL_AREA_NUMBER_REQ_MSG);
					return errorDetails;
				}
			}
			if (vacantLandDetails.getMarketValue() == null) {
				errorDetails.setErrorCode(MARKET_AREA_VALUE_REQ_CODE);
				errorDetails.setErrorMessage(MARKET_AREA_VALUE_REQ_MSG);
				return errorDetails;
			} else if (Double.valueOf(vacantLandDetails.getMarketValue()) == 0) {
				errorDetails.setErrorCode(MARKET_VALUE_GREATER_THAN_ZERO_CODE);
				errorDetails.setErrorMessage(MARKET_VALUE_GREATER_THAN_ZERO_MSG);
				return errorDetails;
			} else {
				final Pattern pattern = Pattern.compile(DIGITS_FLOAT_INT_DBL);
				final Matcher matcher = pattern.matcher(Double.toString(vacantLandDetails.getMarketValue()));
				if (!matcher.matches()) {
					errorDetails.setErrorCode(MKT_VAL_NUMBER_REQ_CODE);
					errorDetails.setErrorMessage(MKT_VAL_NUMBER_REQ_MSG);
					return errorDetails;
				}
			}
			if (vacantLandDetails.getCurrentCapitalValue() == null) {
				errorDetails.setErrorCode(CURRENT_CAPITAL_VALUE_REQ_CODE);
				errorDetails.setErrorMessage(CURRENT_CAPITAL_VALUE_REQ_MSG);
				return errorDetails;
			} else if (Double.valueOf(vacantLandDetails.getCurrentCapitalValue()) == 0.0) {
				errorDetails.setErrorCode(CURRENT_CAPITAL_VALUE_GREATER_THAN_ZERO_CODE);
				errorDetails.setErrorMessage(CURRENT_CAPITAL_VALUE_GREATER_THAN_ZERO_MSG);
				return errorDetails;
			} else if (StringUtils.isBlank(vacantLandDetails.getEffectiveDate())) {
				errorDetails.setErrorCode(EFFECTIVE_DATE_REQ_CODE);
				errorDetails.setErrorMessage(EFFECTIVE_DATE_REQ_MSG);
				return errorDetails;
			}
			if (vacantLandDetails.getVacantLandPlot() == null) {
				errorDetails.setErrorCode(VACANTLAND_AREA_REQ);
				errorDetails.setErrorMessage(VACANTLAND_AREA_REQ_MSG);
				return errorDetails;
			}
			if (vacantLandPlotAreaRepository.findOne(vacantLandDetails.getVacantLandPlot()) == null) {
				errorDetails.setErrorCode(VACANT_PLOT_AREA_TYPE_DOESNT_EXIST);
				errorDetails.setErrorMessage(VACANT_PLOT_AREA_TYPE_DOESNT_EXIST_MSG);
				return errorDetails;
			}
			if (vacantLandDetails.getLayoutApprovalAuthority() == null) {
				errorDetails.setErrorCode(LAYOUT_AUTHORITY_REQ);
				errorDetails.setErrorMessage(LAYOUT_AUTHORITY_REQ_MSG);
				return errorDetails;
			}
			if (layoutApprovalAuthorityRepo.findOne(vacantLandDetails.getLayoutApprovalAuthority()) == null) {
				errorDetails.setErrorCode(LAYOUT_AUTH_DOESNT_EXIST);
				errorDetails.setErrorMessage(LAYOUT_AUTH_DOESNT_EXIST_MSG);
				return errorDetails;
			}
			if (vacantLandDetails.getLayoutPermitNumber().isEmpty() && !NO_APPROVAL.equals(
					layoutApprovalAuthorityRepo.findOne(vacantLandDetails.getLayoutApprovalAuthority()).getName())) {
				errorDetails.setErrorCode(LAYOUT_AUTHORITY_NUM);
				errorDetails.setErrorMessage(LAYOUT_AUTHORITY_NUM_MSG);
				return errorDetails;
			}
			if (vacantLandDetails.getLayoutPermitDate().isEmpty() && !NO_APPROVAL.equals(
					layoutApprovalAuthorityRepo.findOne(vacantLandDetails.getLayoutApprovalAuthority()).getName())) {
				errorDetails.setErrorCode(LAYOUT_AUTHORITY_DATE);
				errorDetails.setErrorMessage(LAYOUT_AUTHORITY_DATE_MSG);
				return errorDetails;
			}
			final SurroundingBoundaryDetails surBoundaryDetails = createPropDetails.getSurroundingBoundaryDetails();
			if (surBoundaryDetails == null) {
				errorDetails.setErrorCode(SURROUNDING_BOUNDARY_DETAILS_REQ_CODE);
				errorDetails.setErrorMessage(SURROUNDING_BOUNDARY_DETAILS_REQ_MSG);
				return errorDetails;
			} else if (StringUtils.isBlank(surBoundaryDetails.getNorthBoundary())) {
				errorDetails.setErrorCode(NORTH_BOUNDARY_REQ_CODE);
				errorDetails.setErrorMessage(NORTH_BOUNDARY_REQ_MSG);
				return errorDetails;
			} else if (StringUtils.isBlank(surBoundaryDetails.getSouthBoundary())) {
				errorDetails.setErrorCode(SOUTH_BOUNDARY_REQ_CODE);
				errorDetails.setErrorMessage(SOUTH_BOUNDARY_REQ_MSG);
				return errorDetails;
			} else if (StringUtils.isBlank(surBoundaryDetails.getEastBoundary())) {
				errorDetails.setErrorCode(EAST_BOUNDARY_REQ_CODE);
				errorDetails.setErrorMessage(EAST_BOUNDARY_REQ_MSG);
				return errorDetails;
			} else if (StringUtils.isBlank(surBoundaryDetails.getWestBoundary())) {
				errorDetails.setErrorCode(WEST_BOUNDARY_REQ_CODE);
				errorDetails.setErrorMessage(WEST_BOUNDARY_REQ_MSG);
				return errorDetails;
			}
		}
		return errorDetails;
	}

	/**
	 * Validates owner details
	 *
	 * @param errorDetails
	 * @param ownerDetailsList
	 * @return ErrorDetails
	 */
	public ErrorDetails validateOwnerDetails(ErrorDetails errorDetails, final List<OwnerInformation> ownerDetailsList) {
		if (ownerDetailsList == null) {
			errorDetails.setErrorCode(OWNER_DETAILS_REQ_CODE);
			errorDetails.setErrorMessage(OWNER_DETAILS_REQ_MSG);
			return errorDetails;
		} else
			for (final OwnerInformation ownerDetails : ownerDetailsList) {
				if (StringUtils.isBlank(ownerDetails.getMobileNumber())) {
					errorDetails.setErrorCode(MOBILE_NO_REQ_CODE);
					errorDetails.setErrorMessage(MOBILE_NO_REQ_MSG);
					return errorDetails;
				} else if (ownerDetails.getMobileNumber().trim().length() != 10) {
					errorDetails.setErrorCode(MOBILENO_MAX_LENGTH_ERROR_CODE);
					errorDetails.setErrorMessage(MOBILENO_MAX_LENGTH_ERROR_MSG);
					return errorDetails;
				} else {
					final Pattern pattern = Pattern.compile("\\d{10}");
					final Matcher matcher = pattern.matcher(ownerDetails.getMobileNumber());
					if (!matcher.matches()) {
						errorDetails.setErrorCode(MOBILENO_ALPHANUMERIC_ERROR_CODE);
						errorDetails.setErrorMessage(MOBILENO_ALPHANUMERIC_ERROR_MSG);
						return errorDetails;
					}
				}
				if (StringUtils.isNotBlank(ownerDetails.getEmailId())) {
					final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
					final Matcher matcher = pattern.matcher(ownerDetails.getEmailId());
					if (!matcher.matches()) {
						errorDetails.setErrorCode(EMAIL_ERROR_CODE);
						errorDetails.setErrorMessage(EMAIL_INVALID_MSG);
						return errorDetails;
					}
				}
				if (StringUtils.isBlank(ownerDetails.getName())) {
					errorDetails.setErrorCode(OWNER_NAME_REQ_CODE);
					errorDetails.setErrorMessage(OWNER_NAME_REQ_MSG);
					return errorDetails;
				} else {
					final Pattern pattern = Pattern.compile("\\w+\\.?");
					final Matcher matcher = pattern.matcher(ownerDetails.getMobileNumber());
					if (!matcher.matches()) {
						errorDetails.setErrorCode(OWNER_NAME_ALPHANUMERIC_ERROR_CODE);
						errorDetails.setErrorMessage(OWNER_NAME_ALPHANUMERIC_ERROR_MSG);
						return errorDetails;
					}
				}

				if (StringUtils.isBlank(ownerDetails.getGender())) {
					errorDetails.setErrorCode(GENDER_REQ_CODE);
					errorDetails.setErrorMessage(GENDER_REQ_MSG);
					return errorDetails;
				}
				if (StringUtils.isBlank(ownerDetails.getGuardianRelation())) {
					errorDetails.setErrorCode(GUARDIAN_RELATION_REQ_CODE);
					errorDetails.setErrorMessage(GUARDIAN_RELATION_REQ_MSG);
					return errorDetails;
				} else {
					final Pattern pattern = Pattern.compile(CAMEL_CASE_PATTERN);
					final Matcher matcher = pattern.matcher(ownerDetails.getGuardianRelation());
					if (!matcher.matches()) {
						errorDetails.setErrorCode(INVALID_GUARDIAN_RELATION_CODE);
						errorDetails.setErrorMessage(INVALID_GUARDIAN_RELATION_REQ_MSG);
						return errorDetails;
					}
				}
				if (StringUtils.isBlank(ownerDetails.getGuardian())) {
					errorDetails.setErrorCode(GUARDIAN_REQ_CODE);
					errorDetails.setErrorMessage(GUARDIAN_REQ_MSG);
					return errorDetails;
				} else {

					final Pattern pattern = Pattern.compile(GUARDIAN_PATTERN);
					final Matcher matcher = pattern.matcher(ownerDetails.getGuardian());
					if (!matcher.matches()) {
						errorDetails.setErrorCode(GUARDIAN_NAME_NUMERICSPL_ERROR_CODE);
						errorDetails.setErrorMessage(GUARDIANNAME_NUMERICSPL_ERROR_MSG);
						return errorDetails;
					}
				}
			}
		return errorDetails;

	}

	public ErrorDetails validatePaymentDetails(final PayPropertyTaxDetails payPropTaxDetails,
			final boolean isMutationFeePayment, final String propertyType) {
		ErrorDetails errorDetails = null;
		if (payPropTaxDetails.getAssessmentNo() == null || payPropTaxDetails.getAssessmentNo().trim().length() == 0) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED);
		} else {
			if (payPropTaxDetails.getAssessmentNo().trim().length() > 0
					&& payPropTaxDetails.getAssessmentNo().trim().length() < 10) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN);
			}
			if (!basicPropertyDAO.isAssessmentNoExist(payPropTaxDetails.getAssessmentNo())) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
			}
			final BasicProperty basicProperty = basicPropertyDAO
					.getBasicPropertyByPropertyID(payPropTaxDetails.getAssessmentNo());
			if (basicProperty != null) {
				final Property property = basicProperty.getProperty();
				if (property != null && property.getIsExemptedFromTax()) {
					errorDetails = new ErrorDetails();
					errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_EXEMPTED_PROPERTY);
					errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_EXEMPTED_PROPERTY);
				}
			}
		}

		if (isMutationFeePayment) {
			if (!propertyExternalService.validateMutationFee(payPropTaxDetails.getAssessmentNo(),
					payPropTaxDetails.getPaymentAmount())) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_EXCESS_MUTATION_FEE);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_EXCESS_MUTATION_FEE);
			}
			final PropertyMutation propertyMutation = propertyExternalService
					.getLatestPropertyMutationByAssesmentNo(payPropTaxDetails.getAssessmentNo());
			if (propertyMutation == null) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_MUTATION_INVALID);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_MUTATION_INVALID);
			}
		}

		if (payPropTaxDetails.getTransactionId() == null || "".equals(payPropTaxDetails.getTransactionId())) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_REQUIRED);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_REQUIRED);
		} else if (payPropTaxDetails.getTransactionId() != null || !"".equals(payPropTaxDetails.getTransactionId())) {
			final BillReceiptInfo billReceiptList = propertyExternalService
					.validateTransanctionIdPresent(payPropTaxDetails.getTransactionId(), propertyType);
			if (billReceiptList != null) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_VALIDATE);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_VALIDATE);

			}
		}
		if (payPropTaxDetails.getPaymentMode() == null || payPropTaxDetails.getPaymentMode().trim().length() == 0) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
		} else if (!PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH
				.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
				&& !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE
						.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
				&& !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD
						.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
		}

		if (payPropTaxDetails.getPaymentMode() != null && (PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE
				.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
				|| PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD
						.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())))
			if (payPropTaxDetails.getChqddNo() == null || payPropTaxDetails.getChqddNo().trim().length() == 0) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_NO_REQUIRED);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_NO_REQUIRED);
			} else

			if (payPropTaxDetails.getChqddDate() == null) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_DATE_REQUIRED);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_DATE_REQUIRED);
			} else

			if (payPropTaxDetails.getBankName() == null || payPropTaxDetails.getBankName().trim().length() == 0) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BANKNAME_REQUIRED);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BANKNAME_REQUIRED);
			} else

			if (payPropTaxDetails.getBranchName() == null) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BRANCHNAME_REQUIRED);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BRANCHNAME_REQUIRED);
			}
		return errorDetails;
	}

	/**
	 * Validates Assessment Details request
	 *
	 * @param assessmentRequest
	 * @return ErrorDetails
	 */
	public ErrorDetails validateAssessmentDetailsRequest(final AssessmentRequest assessmentRequest) {
		ErrorDetails errorDetails = null;

		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyForUpicNoOrOldUpicNo(assessmentRequest.getAssessmentNo(),
				assessmentRequest.getOldAssessmentNo());
		if (basicProperty == null) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
		}
		return errorDetails;
	}

	public ErrorDetails validateSurveyRequest(final AssessmentRequest assessmentRequest) throws ParseException {
		ErrorDetails errorDetails = null;
		if (StringUtils.isBlank(assessmentRequest.getTransactionType())) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(TRANSACTION_TYPE_REQUIRED_CODE);
			errorDetails.setErrorMessage(TRANSACTION_TYPE_REQUIRED_MSG);
		} else if (StringUtils.isBlank(assessmentRequest.getFromDate())) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(FROM_DATE_REQUIRED_CODE);
			errorDetails.setErrorMessage(FROM_DATE_REQUIRED_MSG);
		} else if (StringUtils.isBlank(assessmentRequest.getToDate())) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(TO_DATE_REQUIRED_CODE);
			errorDetails.setErrorMessage(TO_DATE_REQUIRED_MSG);
		}
		if (StringUtils.isNotBlank(assessmentRequest.getTransactionType())
				&& StringUtils.isNotBlank(assessmentRequest.getFromDate())
				&& StringUtils.isNotBlank(assessmentRequest.getToDate())) {
			final Long propertiesCount = propertyExternalService.getPropertiesCount(
					assessmentRequest.getTransactionType(), assessmentRequest.getFromDate(),
					assessmentRequest.getToDate());
			if (propertiesCount > 100) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PROPERTIES_LIST_EXCEED_LIMIT_CODE);
				errorDetails.setErrorMessage(PROPERTIES_LIST_EXCEED_LIMIT_MSG);
			}
		}

		return errorDetails;
	}

	public ErrorDetails validateUpdateRequest(final CreatePropertyDetails createPropDetails, final String mode)
			throws ParseException {
		ErrorDetails errorDetails = new ErrorDetails();
		final BasicProperty bp = propertyExternalService
				.getBasicPropertyByPropertyID(createPropDetails.getAssessmentNumber());
		if (bp.isUnderWorkflow()) {
			errorDetails.setErrorCode(PROPERTY_UNDERWORKFLOW_CODE);
			errorDetails.setErrorMessage(PROPERTY_UNDERWORKFLOW_REQ_MSG);
			return errorDetails;
		} else {
			final Property prop = propertyExternalService.getPropertyByBasicPropertyID(bp);
			if (prop.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
				errorDetails.setErrorCode(DEMAND_INACTIVE_CODE);
				errorDetails.setErrorMessage(DEMAND_INACTIVE_REQ_MSG);
				return errorDetails;
			} else {
				errorDetails = validateCreateRequest(createPropDetails, mode);
				if (errorDetails != null && errorDetails.getErrorCode() != null)
					return errorDetails;
			}
		}
		if (!bp.getActiveProperty().getPropertyDetail().getCategoryType()
				.equals(PropertyTaxConstants.CATEGORY_VACANT_LAND)
				&& createPropDetails.getPropertyTypeMasterCode()
						.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)) {
			errorDetails.setErrorCode(NON_VACANT_TO_VACANT);
			errorDetails.setErrorMessage(NON_VACANT_TO_VACANT_MSG);
			return errorDetails;
		}
			
		return errorDetails;

	}

	public ErrorDetails validateBoundaries(final PropertyAddressDetails propertyAddressDetails,
			ErrorDetails errorDetails,  final String mode) {
		if (StringUtils.isBlank(propertyAddressDetails.getElectionWardNum())) {
			errorDetails.setErrorCode(ELECTION_WARD_REQ_CODE);
			errorDetails.setErrorMessage(ELECTION_WARD_REQ_MSG);
			return errorDetails;
		} else if (!propertyExternalService.isBoundaryActive(propertyAddressDetails.getElectionWardNum(), WARD,
				ADMIN_HIERARCHY_TYPE)) {
			errorDetails.setErrorCode(INACTIVE_ELECTION_WARD_CODE);
			errorDetails.setErrorMessage(INACTIVE_ELECTION_WARD_REQ_MSG);
			return errorDetails;
		}
		
		if (StringUtils.isBlank(propertyAddressDetails.getLocalityNum())) {
			errorDetails.setErrorCode(LOCALITY_REQ_CODE);
			errorDetails.setErrorMessage(LOCALITY_REQ_MSG);
			return errorDetails;
		} else if (!propertyExternalService.isBoundaryActive(propertyAddressDetails.getLocalityNum(),
				LOCALITY_BNDRY_TYPE, LOCATION_HIERARCHY_TYPE)) {
			errorDetails.setErrorCode(INACTIVE_LOCALITY_CODE);
			errorDetails.setErrorMessage(INACTIVE_LOCALITY_REQ_MSG);
			return errorDetails;
		}
		
		if (StringUtils.isBlank(propertyAddressDetails.getBlockNum())) {
			errorDetails.setErrorCode(BLOCK_NO_REQ_CODE);
			errorDetails.setErrorMessage(BLOCK_NO_REQ_MSG);
			return errorDetails;
		} else if (!propertyExternalService.isBoundaryActive(propertyAddressDetails.getBlockNum(), BLOCK,
				REVENUE_HIERARCHY_TYPE)) {
			errorDetails.setErrorCode(INACTIVE_BLOCK_CODE);
			errorDetails.setErrorMessage(INACTIVE_BLOCK_REQ_MSG);
		}
		
		if (StringUtils.isBlank(propertyAddressDetails.getZoneNum())
				&& !PropertyTaxConstants.PROPERTY_MODE_MODIFY.equalsIgnoreCase(mode)) {
			errorDetails.setErrorCode(ZONE_NO_REQ_CODE);
			errorDetails.setErrorMessage(ZONE_NO_REQ_MSG);
			return errorDetails;
		} else if (!propertyExternalService.isBoundaryActive(propertyAddressDetails.getZoneNum(), ZONE,
				REVENUE_HIERARCHY_TYPE)) {
			errorDetails.setErrorCode(INACTIVE_ZONE_CODE);
			errorDetails.setErrorMessage(INACTIVE_ZONE_REQ_MSG);
		}
		if (StringUtils.isBlank(propertyAddressDetails.getWardNum())) {
			errorDetails.setErrorCode(WARD_NO_REQ_CODE);
			errorDetails.setErrorMessage(WARD_NO_REQ_MSG);
			return errorDetails;
		} else if (!propertyExternalService.isBoundaryActive(propertyAddressDetails.getWardNum(), WARD,
				REVENUE_HIERARCHY_TYPE)) {
			errorDetails.setErrorCode(INACTIVE_WARD_CODE);
			errorDetails.setErrorMessage(INACTIVE_WARD_REQ_MSG);
		}
		return errorDetails;
	}

	public ErrorDetails validateCrossHierarchyMapping(final Boundary locality, final Boundary ward,
			final Boundary block, ErrorDetails errorDetails) {
		final BoundaryType wardBoundaryType = propertyExternalService.getBoundaryTypeByNameandHierarchy(WARD,
				REVENUE_HIERARCHY_TYPE);
		final Boolean isMapping = propertyExternalService.isCrossHierarchyMappingExist(locality.getId(), ward.getId(),
				block.getId(), wardBoundaryType.getId());
		if (!isMapping) {
			errorDetails.setErrorCode(CROSS_MAPPING_FOR_LOCALITY_WARD_BLOCK_CODE);
			errorDetails.setErrorMessage(CROSS_MAPPING_FOR_LOCALITY_WARD_BLOCK_REQ_MSG);
		}

        return errorDetails;
    }
	
    public ErrorDetails validateTaxCalculatorRequest(TaxCalculatorRequest taxCalculatorRequest) {
        ErrorDetails errorDetails = null;
        BasicProperty basicProperty = basicPropertyDAO
                .getBasicPropertyForUpicNoOrOldUpicNo(taxCalculatorRequest.getAssessmentNo(), null);
        if (basicProperty == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
            return errorDetails;
        } else {
            Property property = basicProperty.getProperty();
            if (property != null) {
                if (PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND
                        .equalsIgnoreCase(property.getPropertyDetail().getPropertyTypeMaster().getCode())) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(VACANT_LAND_PROPERTY_ERROR_CODE);
                    errorDetails.setErrorMessage(VACANT_LAND_PROPERTY_ERROR_MSG);
                    return errorDetails;
                }
                errorDetails = validateFloorDetailsForTaxCalculation(taxCalculatorRequest.getFloorDetails());
                if (errorDetails != null && StringUtils.isNotBlank(errorDetails.getErrorCode()))
                    return errorDetails;
            }
        }
        return errorDetails;
    }

    private ErrorDetails validateFloorDetailsForTaxCalculation(List<FloorDetails> floorDetailsList) {
        ErrorDetails errorDetails = new ErrorDetails();
        if (floorDetailsList.isEmpty()) {
            errorDetails.setErrorCode(FLOOR_DETAILS_REQ_CODE);
            errorDetails.setErrorMessage(FLOOR_DETAILS_REQ_MSG);
        } else {
            for (final FloorDetails floorDetails : floorDetailsList) {
                errorDetails = validateStructureClassification(floorDetails);
                if (StringUtils.isNotBlank(errorDetails.getErrorCode()))
                    return errorDetails;

                errorDetails = validateFloorUsage(floorDetails);
                if (StringUtils.isNotBlank(errorDetails.getErrorCode()))
                    return errorDetails;

                if (StringUtils.isBlank(floorDetails.getOccupancyCode())) {
                    errorDetails.setErrorCode(OCCUPANCY_REQ_CODE);
                    errorDetails.setErrorMessage(OCCUPANCY_REQ_MSG);
                    return errorDetails;
                }
                if (StringUtils.isBlank(floorDetails.getConstructionDate())) {
                    errorDetails.setErrorCode(CONSTRUCTION_DATE_REQ_CODE);
                    errorDetails.setErrorMessage(CONSTRUCTION_DATE_REQ_MSG);
                    return errorDetails;
                }
            }
        }
        return errorDetails;
    }

    private ErrorDetails validateStructureClassification(FloorDetails floorDetails) {
        ErrorDetails errorDetails = new ErrorDetails();
        if (StringUtils.isBlank(floorDetails.getBuildClassificationCode())) {
            errorDetails.setErrorCode(CLASSIFICATION_OF_BUILDING_REQ_CODE);
            errorDetails.setErrorMessage(CLASSIFICATION_OF_BUILDING_REQ_MSG);
            return errorDetails;
        } else {
            final Boolean classification = structureClassificationService
                    .isActiveClassification(floorDetails.getBuildClassificationCode());
            if (classification == null) {
                errorDetails.setErrorCode(CLASSIFICATION_CODE_DOESNT_EXIST);
                errorDetails.setErrorMessage(CLASSIFICATION_CODE_DOESNT_EXIST_MSG);
                return errorDetails;
            }
            if (!classification) {
                errorDetails.setErrorCode(INACTIVE_CLASSIFICATION_CODE);
                errorDetails.setErrorMessage(INACTIVE_CLASSIFICATION_REQ_MSG);
                return errorDetails;
            }
        }
        return errorDetails;
    }

    private ErrorDetails validateFloorUsage(FloorDetails floorDetails) {
        ErrorDetails errorDetails = new ErrorDetails();
        if (StringUtils.isBlank(floorDetails.getNatureOfUsageCode())) {
            errorDetails.setErrorCode(NATURE_OF_USAGES_REQ_CODE);
            errorDetails.setErrorMessage(NATURE_OF_USAGES_REQ_MSG);
            return errorDetails;
        } else {
            Boolean usage = propertyUsageService.isActiveUsage(floorDetails.getNatureOfUsageCode());
            if (usage == null) {
                errorDetails.setErrorCode(USAGE_CODE_DOESNT_EXIST);
                errorDetails.setErrorMessage(USAGE_CODE_DOESNT_EXIST_MSG);
                return errorDetails;
            }
            if (!usage) {
                errorDetails.setErrorCode(INACTIVE_USAGE_CODE);
                errorDetails.setErrorMessage(INACTIVE_USAGE_REQ_MSG);
                return errorDetails;
            }
        }
        return errorDetails;
    }

}
