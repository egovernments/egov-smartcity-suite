package org.egov.restapi.util;

import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.CreatePropertyDetails;

public class ValidationUtil {
	public static ErrorDetails validateCreateRequest(CreatePropertyDetails createPropDetails) {
		ErrorDetails errorDetails = null;
		String propertyTypeMasterCode = createPropDetails.getPropertyTypeMasterCode();
		if (propertyTypeMasterCode == null || propertyTypeMasterCode.trim().length() == 0) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(RestApiConstants.OWNERSHIP_CATEGORY_TYPE_REQ_CODE);
			errorDetails.setErrorMessage(RestApiConstants.OWNERSHIP_CATEGORY_TYPE_REQ_MSG);
		}
		if (!propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_335)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50)
				&& !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_75)) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(RestApiConstants.OWNERSHIP_CATEGORY_TYPE_INVALID_CODE);
			errorDetails.setErrorMessage(RestApiConstants.OWNERSHIP_CATEGORY_TYPE_INVALID_MSG);
		}

		String propertyCategoryCode = createPropDetails.getPropertyCategoryCode();
		if (propertyCategoryCode == null || propertyCategoryCode.trim().length() == 0) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(RestApiConstants.PROPERTY_CATEGORY_TYPE_REQ_CODE);
			errorDetails.setErrorMessage(RestApiConstants.PROPERTY_CATEGORY_TYPE_REQ_MSG);
		} 
		if (!propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_RESIDENTIAL)
				&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL)
				&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_MIXED)) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(RestApiConstants.PROPERTY_CATEGORY_TYPE_INVALID_CODE);
			errorDetails.setErrorMessage(RestApiConstants.PROPERTY_CATEGORY_TYPE_INVALID_MSG);
		}
		return errorDetails;
	}
}
