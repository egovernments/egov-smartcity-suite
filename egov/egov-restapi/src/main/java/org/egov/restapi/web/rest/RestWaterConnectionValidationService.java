/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.restapi.web.rest;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.WaterChargesConnectionInfo;
import org.egov.restapi.model.WaterConnectionInfo;
import org.egov.wtms.application.entity.RegularisedConnection;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.rest.WaterChargesRestApiResponse;
import org.egov.wtms.application.service.AdditionalConnectionService;
import org.egov.wtms.application.service.ChangeOfUseService;
import org.egov.wtms.application.service.NewConnectionService;
import org.egov.wtms.application.service.RegularisedConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
import org.egov.wtms.masters.entity.PropertyCategory;
import org.egov.wtms.masters.entity.PropertyPipeSize;
import org.egov.wtms.masters.entity.WaterChargesDetailInfo;
import org.egov.wtms.masters.entity.WaterPropertyUsage;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.PropertyCategoryService;
import org.egov.wtms.masters.service.PropertyPipeSizeService;
import org.egov.wtms.masters.service.WaterPropertyUsageService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;

@RestController
public class RestWaterConnectionValidationService {

    private static final String REGULARISED_CONN_NATUREOFTASK = "Regularised Water Tap Connection";

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
    private PropertyCategoryService propertyCategoryService;

    @Autowired
    private NewConnectionService newConnectionService;

    @Autowired
    private PropertyPipeSizeService propertyPipeSizeService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<RegularisedConnection> waterConnectionWorkflowService;

    @Autowired
    private RegularisedConnectionService regularisedConnectionService;

    public WaterChargesRestApiResponse validatePropertyID(final String propertyid) {
        WaterChargesRestApiResponse errorDetails = null;
        final String errorMessage = newConnectionService.checkValidPropertyAssessmentNumber(propertyid);
        if (errorMessage != null && !errorMessage.equals("")) {
            errorDetails = new WaterChargesRestApiResponse();
            errorDetails.setErrorCode(RestApiConstants.PROPERTYID_IS_VALID_CODE);
            errorDetails.setErrorMessage(errorMessage);

        }
        return errorDetails;
    }

    public WaterChargesRestApiResponse validateWaterConnectionDetails(final String propertyId) {
        String responseMessage = "";
        WaterChargesRestApiResponse errorDetails = null;
        responseMessage = newConnectionService.checkValidPropertyAssessmentNumber(propertyId);
        if (responseMessage.isEmpty()) {
            final String responseMessagedet = newConnectionService.checkConnectionPresentForProperty(propertyId);
            if (isNotBlank(responseMessagedet)) {
                errorDetails = new WaterChargesRestApiResponse();
                errorDetails.setErrorMessage(RestApiConstants.PROPERTYID_IS_VALID_CONNECTION);
                errorDetails.setErrorCode(RestApiConstants.PROPERTYID_IS_VALID_CONNECTION_CODE);
            }
        }
        return errorDetails;
    }

    public ErrorDetails validateAdditionalWaterConnectionDetails(final WaterConnectionInfo connectionInfo) {
        String responseMessage = "";
        ErrorDetails errorDetails = null;
        final WaterConnection connection = waterConnectionService.findByConsumerCode(connectionInfo.getConsumerCode());
        final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                .getParentConnectionDetails(connection.getPropertyIdentifier(), ConnectionStatus.ACTIVE);
        responseMessage = additionalConnectionService.validateAdditionalConnection(parentConnectionDetails);

        if (isNotBlank(responseMessage)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorMessage(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION);
            errorDetails.setErrorCode(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION_CODE);
        }

        return errorDetails;
    }

    public ErrorDetails validateChangOfUsageWaterConnectionDetails(final WaterChargesDetailInfo connectionInfo) {
        String responseMessage = "";
        ErrorDetails errorDetails = null;
        final WaterConnectionDetails connectionUnderChange = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(connectionInfo.getConsumerCode(), ConnectionStatus.ACTIVE);
        responseMessage = changeOfUseService.validateChangeOfUseConnection(connectionUnderChange);

        if (isNotBlank(responseMessage)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorMessage(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION);
            errorDetails.setErrorCode(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION_CODE);
        }

        return errorDetails;
    }

    public ErrorDetails validateCombinationOfChangOfUsage(final WaterChargesDetailInfo connectionInfo) {
        String responseMessage = "";
        ErrorDetails errorDetails = null;
        final WaterConnectionDetails connectionUnderChange = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(connectionInfo.getConsumerCode(), ConnectionStatus.ACTIVE);
        if (isBlank(connectionInfo.getPropertyID()))
            connectionInfo.setPropertyID(connectionUnderChange.getConnection().getPropertyIdentifier());
        if (isBlank(connectionInfo.getWaterSource()))
            connectionInfo.setWaterSource(connectionUnderChange.getWaterSource().getCode());
        if (isBlank(connectionInfo.getConnectionType()))
            connectionInfo.setConnectionType(connectionUnderChange.getConnectionType().toString());
        if (isBlank(connectionInfo.getPropertyType()))
            connectionInfo.setPropertyType(connectionUnderChange.getPropertyType().getCode());
        if (isBlank(connectionInfo.getCategory()))
            connectionInfo.setCategory(connectionUnderChange.getCategory().getCode());
        if (isBlank(connectionInfo.getPipeSize()))
            connectionInfo.setPipeSize(connectionUnderChange.getPipeSize().getCode());

        if (connectionUnderChange.getCategory().getCode().equals(connectionInfo.getCategory())
                && connectionUnderChange.getUsageType().getCode().equals(connectionInfo.getUsageType())
                && connectionUnderChange.getPropertyType().getCode().equals(connectionInfo.getPropertyType())
                && connectionUnderChange.getPipeSize().getCode().equals(connectionInfo.getPipeSize())
                && connectionUnderChange.getConnectionType().name().equals(connectionInfo.getConnectionType()))
            responseMessage = "Please modify at least one mandatory field";
        if (isNotBlank(responseMessage)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorMessage(RestApiConstants.CONSUMERCODE_IS_NOT_VALID_CONNECTION);
            errorDetails.setErrorCode(responseMessage);
        }

        return errorDetails;
    }

    public WaterChargesRestApiResponse populateAndPersistRegularisedWaterConnection(
            final WaterChargesConnectionInfo waterChargesConnectionInfo) {
        return prepareNewRegularizationConnectionDetails(waterChargesConnectionInfo);
    }

    public WaterChargesRestApiResponse prepareNewRegularizationConnectionDetails(
            final WaterChargesConnectionInfo waterChargesConnectionInfo) {
        final WaterChargesRestApiResponse response = new WaterChargesRestApiResponse();
        final RegularisedConnection regularisedConnection = new RegularisedConnection();
        regularisedConnection.setApplicationNumber(applicationNumberGenerator.generate());
        final WorkFlowMatrix wfmatrix = waterConnectionWorkflowService.getWfMatrix(regularisedConnection.getStateType(), null,
                null, REGULARIZE_CONNECTION, "Created", null);
        final User user = securityUtils.getCurrentUser();
        final Position userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(waterChargesConnectionInfo.getPropertyId());
        regularisedConnection.setUlbCode(waterChargesConnectionInfo.getUlbCode());
        regularisedConnection.setPropertyIdentifier(waterChargesConnectionInfo.getPropertyId());
        regularisedConnection.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                .withOwner(userPosition).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(REGULARISED_CONN_NATUREOFTASK);
        regularisedConnectionService.save(regularisedConnection);
        response.setApplicationNumber(regularisedConnection.getApplicationNumber());
        return response;
    }

    public ErrorDetails validateServiceRequest(final String propertyType, final String usageType, final String pipeSize,
            final String category) {
        ErrorDetails errorDetails = null;
        if (propertyType != null) {
            final WaterPropertyUsage usageTypesList = waterPropertyUsageService.findByPropertyTypecodeAndUsageTypecode(
                    propertyType, usageType);

            if (usageTypesList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_USAGETYPE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_USAGETYPE_COMBINATION_VALID);
                return errorDetails;
            }
            final PropertyPipeSize pipeSizeList = propertyPipeSizeService.findByPropertyTypecodeAndPipeSizecode(
                    propertyType, pipeSize);
            if (pipeSizeList == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_PIPESIZE_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_PIPESIZE_COMBINATION_VALID);
                return errorDetails;
            }

            final PropertyCategory categoryTypes = propertyCategoryService
                    .getAllCategoryTypesByPropertyTypeAndCategory(propertyType,
                            category);

            if (categoryTypes == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.PROPERTY_CATEGORY_COMBINATION_VALID_CODE);
                errorDetails.setErrorMessage(RestApiConstants.PROPERTY_CATEGORY_COMBINATION_VALID);
                return errorDetails;
            }
        }
        return errorDetails;
    }

    public WaterChargesRestApiResponse validatePropertyAssessmentNumber(final String propertyid) {
        WaterChargesRestApiResponse errorDetails = null;
        final String errorMessage = newConnectionService.checkValidPropertyForDataEntry(propertyid);
        if (errorMessage != null && !errorMessage.equals("")) {
            errorDetails = new WaterChargesRestApiResponse();
            errorDetails.setApplicationNumber("-1");
            errorDetails.setErrorCode(RestApiConstants.PROPERTYID_IS_VALID_CODE);
            errorDetails.setErrorMessage(errorMessage);
        }
        return errorDetails;
    }

}