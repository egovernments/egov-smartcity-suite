package org.egov.restapi.web.rest;

import java.io.IOException;

import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.ConnectionInfo;
import org.egov.wtms.application.service.NewConnectionService;
import org.egov.wtms.masters.entity.PropertyCategory;
import org.egov.wtms.masters.entity.PropertyPipeSize;
import org.egov.wtms.masters.entity.WaterPropertyUsage;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestWaterConnectionValidationService {

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private PipeSizeService pipeSizeService;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private ConnectionCategoryService connectionCategoryService;

    @Autowired
    private NewConnectionService newConnectionService;

    public ErrorDetails validateCreateRequest(final ConnectionInfo connectionInfo) {
        ErrorDetails errorDetails = null;
        if (connectionInfo.getPropertyType() != null) {
            final WaterPropertyUsage usageTypesList = usageTypeService.getAllUsageTypesByPropertyTypeAndUsageType(
                    connectionInfo.getPropertyType(), connectionInfo.getUsageType());

            if (connectionInfo.getPropertyType().equals("RESIDENTIAL") && usageTypesList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_RESIDENTIAL_USAGETYPE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_RESIDENTIAL_USAGETYPE_COMBINATION_VALID);
                return errorDetails;

            } else if (connectionInfo.getPropertyType().equals("NON-RESIDENTIAL") && usageTypesList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_NONRESIDENTIAL_USAGETYPE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_NONRESIDENTIAL_USAGETYPE_COMBINATION_VALID);
                return errorDetails;

            }
            final PropertyPipeSize pipeSizeList = pipeSizeService.getAllPipeSizesByPropertyTypeAnPipeSize(
                    connectionInfo.getPropertyType(), connectionInfo.getPipeSize());
            if (connectionInfo.getPropertyType().equals("RESIDENTIAL") && pipeSizeList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_RESIDENTIAL_PIPESIZE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_RESIDENTIAL_PIPESIZE_COMBINATION_VALID);
                return errorDetails;
            } else if (connectionInfo.getPropertyType().equals("NON-RESIDENTIAL") && pipeSizeList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_NON_RESIDENTIAL_PIPESIZE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_NON_RESIDENTIAL_PIPESIZE_COMBINATION_VALID);
                return errorDetails;

            }

            final PropertyCategory categoryTypes = connectionCategoryService
                    .getAllCategoryTypesByPropertyTypeAndCategory(connectionInfo.getPropertyType(),
                            connectionInfo.getCategory());

            if (connectionInfo.getPropertyType().equals("RESIDENTIAL") && categoryTypes == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_RESIDENTIAL_CATEGORY_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_RESIDENTIAL_CATEGORY_COMBINATION_VALID);
                return errorDetails;
            } else if (connectionInfo.getPropertyType().equals("NON-RESIDENTIAL") && categoryTypes == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_NON_RESIDENTIAL_CATEGORY_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_NON_RESIDENTIAL_CATEGORY_COMBINATION_VALID);
                return errorDetails;

            }
        }
        return errorDetails;
    }

    public ErrorDetails validatePropertyID(final String propertyid) throws IOException {
        ErrorDetails errorDetails = null;
        final String errorMessage = newConnectionService.checkValidPropertyAssessmentNumber(propertyid);
        if (errorMessage != null && !errorMessage.equals("")) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.PROPERTYID_IS_VALID_CODE);
            errorDetails.setErrorMessage(RestApiConstants.PROPERTYID_IS_VALID);

        }
        return errorDetails;
    }

    public ErrorDetails validateWaterConnectionDetails(final ConnectionInfo connectionInfo) throws IOException {
        String responseMessage = "";
        ErrorDetails errorDetails = null;
        responseMessage = newConnectionService.checkValidPropertyAssessmentNumber(connectionInfo.getPropertyID());
        if (responseMessage.isEmpty()){
           String responseMessagedet = newConnectionService.checkConnectionPresentForProperty(connectionInfo.getPropertyID());
        if (responseMessagedet != "" && responseMessagedet != null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorMessage(RestApiConstants.PROPERTYID_IS_VALID_CONNECTION);
            errorDetails.setErrorCode(RestApiConstants.PROPERTYID_IS_VALID_CONNECTION_CODE);
        }
        }
        return errorDetails;
    }
}
