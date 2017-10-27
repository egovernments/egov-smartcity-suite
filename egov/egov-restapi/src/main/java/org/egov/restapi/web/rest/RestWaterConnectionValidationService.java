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

import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.WaterConnectionInfo;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.AdditionalConnectionService;
import org.egov.wtms.application.service.ChangeOfUseService;
import org.egov.wtms.application.service.NewConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
import org.egov.wtms.masters.entity.PropertyCategory;
import org.egov.wtms.masters.entity.PropertyPipeSize;
import org.egov.wtms.masters.entity.WaterChargesDetailInfo;
import org.egov.wtms.masters.entity.WaterPropertyUsage;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyCategoryService;
import org.egov.wtms.masters.service.PropertyPipeSizeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterPropertyUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RestWaterConnectionValidationService {

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private PipeSizeService pipeSizeService;

    @Autowired
    private UsageTypeService usageTypeService;
    
    @Autowired
    private WaterPropertyUsageService waterPropertyUsageService;

    @Autowired
    private ChangeOfUseService changeOfUseService;

    @Autowired
    private WaterConnectionService waterConnectionService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private AdditionalConnectionService additionalConnectionService;
    
    @Autowired
    private ConnectionCategoryService connectionCategoryService;
    
    @Autowired
    private PropertyCategoryService propertyCategoryService;
 
    @Autowired
    private NewConnectionService newConnectionService;
    
    @Autowired
    private PropertyPipeSizeService propertyPipeSizeService;

    public ErrorDetails validateCreateRequest(final WaterConnectionInfo connectionInfo) {
        ErrorDetails errorDetails = null;
        if (connectionInfo.getPropertyType() != null) {
            final WaterPropertyUsage usageTypesList = waterPropertyUsageService.findByPropertyTypecodeAndUsageTypecode(
                    connectionInfo.getPropertyType(), connectionInfo.getUsageType());

            if (usageTypesList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_USAGETYPE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_USAGETYPE_COMBINATION_VALID);
                return errorDetails;
            }
            final PropertyPipeSize pipeSizeList = propertyPipeSizeService.findByPropertyTypecodeAndPipeSizecode(
                    connectionInfo.getPropertyType(), connectionInfo.getPipeSize());
            if (pipeSizeList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_PIPESIZE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_PIPESIZE_COMBINATION_VALID);
                return errorDetails;
            }

            final PropertyCategory categoryTypes = propertyCategoryService
                    .getAllCategoryTypesByPropertyTypeAndCategory(connectionInfo.getPropertyType(),
                            connectionInfo.getCategory());

            if (categoryTypes == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_CATEGORY_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_CATEGORY_COMBINATION_VALID);
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

    public ErrorDetails validateWaterConnectionDetails(final WaterConnectionInfo connectionInfo) throws IOException {
        String responseMessage = "";
        ErrorDetails errorDetails = null;
        responseMessage = newConnectionService.checkValidPropertyAssessmentNumber(connectionInfo.getPropertyID());
        if (responseMessage.isEmpty()) {
            final String responseMessagedet = newConnectionService.checkConnectionPresentForProperty(connectionInfo
                    .getPropertyID());
            if (responseMessagedet != "" && responseMessagedet != null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorMessage(RestApiConstants.PROPERTYID_IS_VALID_CONNECTION);
                errorDetails.setErrorCode(RestApiConstants.PROPERTYID_IS_VALID_CONNECTION_CODE);
            }
        }
        return errorDetails;
    }

    public ErrorDetails validateAdditionalWaterConnectionDetails(final WaterConnectionInfo connectionInfo)
            throws IOException {
        String responseMessage = "";
        ErrorDetails errorDetails = null;
        final WaterConnection connection = waterConnectionService.findByConsumerCode(connectionInfo.getConsumerCode());
        final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                .getParentConnectionDetails(connection.getPropertyIdentifier(), ConnectionStatus.ACTIVE);
        responseMessage = additionalConnectionService.validateAdditionalConnection(parentConnectionDetails);

        if (responseMessage != "" && responseMessage != null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorMessage(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION);
            errorDetails.setErrorCode(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION_CODE);
        }

        return errorDetails;
    }

    public ErrorDetails validateChangOfUsageWaterConnectionDetails(final WaterChargesDetailInfo connectionInfo)
            throws IOException {
        String responseMessage = "";
        ErrorDetails errorDetails = null;
        final WaterConnectionDetails connectionUnderChange = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(connectionInfo.getConsumerCode(), ConnectionStatus.ACTIVE);
        responseMessage = changeOfUseService.validateChangeOfUseConnection(connectionUnderChange);

        if (responseMessage != "" && responseMessage != null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorMessage(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION);
            errorDetails.setErrorCode(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION_CODE);
        }

        return errorDetails;
    }
    
    public ErrorDetails validateCombinationOfChangOfUsage(final WaterChargesDetailInfo connectionInfo)
            throws IOException {
        String responseMessage = "";
        ErrorDetails errorDetails = null;
        final WaterConnectionDetails connectionUnderChange = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(connectionInfo.getConsumerCode(), ConnectionStatus.ACTIVE);
        if(connectionUnderChange.getCategory().getCode().equals(connectionInfo.getCategory()) &&
                connectionUnderChange.getUsageType().getCode().equals(connectionInfo.getUsageType()) &&
                connectionUnderChange.getPropertyType().getCode().equals(connectionInfo.getPropertyType()) &&
                connectionUnderChange.getPipeSize().getCode().equals(connectionInfo.getPipeSize())&&
                connectionUnderChange.getConnectionType().name().equals(connectionInfo.getConnectionType())){
        responseMessage = "Please modify at least one mandatory field";
        }
        if (responseMessage != "" && responseMessage != null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorMessage(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION);
            errorDetails.setErrorCode(responseMessage);
        }

        return errorDetails;
    }
    
    public ErrorDetails validateRequest(final WaterChargesDetailInfo connectionInfo) {
        ErrorDetails errorDetails = null;
        if (connectionInfo.getPropertyType() != null) {
            final WaterPropertyUsage usageTypesList = waterPropertyUsageService.findByPropertyTypecodeAndUsageTypecode(
                    connectionInfo.getPropertyType(), connectionInfo.getUsageType());

            if (usageTypesList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_USAGETYPE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_USAGETYPE_COMBINATION_VALID);
                return errorDetails;
            }
            final PropertyPipeSize pipeSizeList = propertyPipeSizeService.findByPropertyTypecodeAndPipeSizecode(
                    connectionInfo.getPropertyType(), connectionInfo.getPipeSize());
            if (pipeSizeList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_PIPESIZE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_PIPESIZE_COMBINATION_VALID);
                return errorDetails;
            }

            final PropertyCategory categoryTypes = propertyCategoryService
                    .getAllCategoryTypesByPropertyTypeAndCategory(connectionInfo.getPropertyType(),
                            connectionInfo.getCategory());

            if (categoryTypes == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_CATEGORY_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_CATEGORY_COMBINATION_VALID);
                return errorDetails;
            }
        }
        return errorDetails;
    }

}