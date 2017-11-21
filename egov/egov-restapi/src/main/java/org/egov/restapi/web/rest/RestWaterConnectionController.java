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

import static org.egov.restapi.constants.RestApiConstants.THIRD_PARTY_ERR_CODE_SUCCESS;
import static org.egov.restapi.constants.RestApiConstants.THIRD_PARTY_ERR_MSG_SUCCESS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.model.WaterChargesConnectionInfo;
import org.egov.restapi.model.WaterConnectionInfo;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.rest.WaterChargesDetails;
import org.egov.wtms.application.rest.WaterChargesRestApiResponse;
import org.egov.wtms.application.service.ChangeOfUseService;
import org.egov.wtms.application.service.ConnectionDetailService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
import org.egov.wtms.masters.entity.WaterChargesDetailInfo;
import org.egov.wtms.masters.entity.WaterTaxDetailRequest;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterSourceService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller provides RESTful services to create new/additional/change of use water connection
 */
@RestController
public class RestWaterConnectionController {
    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    private ConnectionCategoryService connectionCategoryService;

    @Autowired
    private RestWaterConnectionValidationService restWaterConnectionValidationService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private PipeSizeService pipeSizeService;

    @Autowired
    private PropertyTypeService propertyTypeService;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private WaterConnectionService waterConnectionService;

    @Autowired
    private WaterSourceService waterSourceService;

    @Autowired
    private ChangeOfUseService changeOfUseService;

    @Autowired
    private ConnectionDetailService connectionDetailService;

    public static final String SUCCESS = "SUCCESS";

    @RequestMapping(value = "/watercharges/newconnection", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public String createNewConnection(@Valid @RequestBody final WaterConnectionInfo waterConnectionInfo)
            throws IOException {
        final WaterChargesRestApiResponse response = restWaterConnectionValidationService.validatePropertyID(waterConnectionInfo
                .getPropertyID());
        if (response != null)
            return getJSONResponse(response);
        final WaterChargesRestApiResponse errorMessage = restWaterConnectionValidationService
                .validateWaterConnectionDetails(waterConnectionInfo.getPropertyID());
        if (errorMessage != null)
            return getJSONResponse(errorMessage);
        final ErrorDetails errorDetails = restWaterConnectionValidationService.validateServiceRequest(
                waterConnectionInfo.getPropertyType(),
                waterConnectionInfo.getUsageType(), waterConnectionInfo.getPipeSize(), waterConnectionInfo.getCategory());
        if (errorDetails != null)
            return getJSONResponse(errorDetails);
        final String applicationCode = WaterTaxConstants.NEWCONNECTION;
        final WaterConnectionDetails waterConnectionDetails = populateAndPersistWaterConnectionDetails(waterConnectionInfo,
                applicationCode);
        return waterConnectionDetails.getApplicationNumber();
    }

    @RequestMapping(value = "/watercharges/regulariseconnection", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String createRegularisedConnection(@Valid @RequestBody final WaterChargesConnectionInfo waterChargesConnectionInfo)
            throws IOException {
        final WaterChargesRestApiResponse errorDetails = restWaterConnectionValidationService
                .validatePropertyAssessmentNumber(waterChargesConnectionInfo.getPropertyId());
        if (errorDetails != null) {
            errorDetails.setReferenceId(waterChargesConnectionInfo.getReferenceId());
            return getJSONResponse(errorDetails);
        }
        final WaterChargesRestApiResponse response = restWaterConnectionValidationService
                .populateAndPersistRegularisedWaterConnection(waterChargesConnectionInfo);
        if (response != null) {
            response.setReferenceId(waterChargesConnectionInfo.getReferenceId());
            response.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
            response.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);

        }

        return getJSONResponse(response);
    }

    @RequestMapping(value = "/watercharges/additionalconnection", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public String createAddtionalConnection(@Valid @RequestBody final WaterConnectionInfo waterConnectionInfo)
            throws IOException {
        final ErrorDetails response = restWaterConnectionValidationService
                .validateAdditionalWaterConnectionDetails(waterConnectionInfo);
        final String applicationCode = WaterTaxConstants.ADDNLCONNECTION;
        if (response != null)
            return getJSONResponse(response);
        final ErrorDetails errorDetails = restWaterConnectionValidationService.validateServiceRequest(
                waterConnectionInfo.getPropertyType(),
                waterConnectionInfo.getUsageType(), waterConnectionInfo.getPipeSize(), waterConnectionInfo.getCategory());
        if (errorDetails != null)
            return getJSONResponse(errorDetails);
        final WaterConnectionDetails waterConnectionDetails = populateAndPersistWaterConnectionDetails(waterConnectionInfo,
                applicationCode);
        return waterConnectionDetails.getApplicationNumber();

    }

    @RequestMapping(value = "/watercharges/changeofuse", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public String createChangeOfUsageConnection(@Valid @RequestBody final WaterChargesDetailInfo waterConnectionInfo)
            throws IOException {
        WaterConnectionDetails waterConnectionDetails = null;
        Long approvalPosition = 0l;
        Position userPosition = null;
        final ErrorDetails response = restWaterConnectionValidationService
                .validateChangOfUsageWaterConnectionDetails(waterConnectionInfo);
        if (response != null)
            return getJSONResponse(response);
        final ErrorDetails errorMessage = restWaterConnectionValidationService
                .validateCombinationOfChangOfUsage(waterConnectionInfo);
        if (errorMessage != null)
            return getJSONResponse(response);
        final ErrorDetails errorDetails = restWaterConnectionValidationService.validateServiceRequest(
                waterConnectionInfo.getPropertyType(),
                waterConnectionInfo.getUsageType(), waterConnectionInfo.getPipeSize(), waterConnectionInfo.getCategory());
        if (errorDetails != null)
            return getJSONResponse(errorDetails);
        final WaterConnectionDetails connectionUnderChange = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(waterConnectionInfo.getConsumerCode(), ConnectionStatus.ACTIVE);
        waterConnectionDetails = prepareChangeOfUsageWaterConnectionDetails(waterConnectionInfo, connectionUnderChange);
        if (connectionUnderChange != null) {
            waterConnectionDetails.setConnection(connectionUnderChange.getConnection());
            userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(connectionUnderChange.getConnection()
                    .getPropertyIdentifier());
            if (userPosition != null)
                approvalPosition = userPosition.getId();
        }
        waterConnectionDetails = changeOfUseService.createChangeOfUseApplication(waterConnectionDetails,
                approvalPosition, WaterTaxConstants.APPLICATION_GIS_SYSTEM, waterConnectionDetails.getApplicationType().getCode(),
                null,
                WaterTaxConstants.SURVEY);
        return waterConnectionDetails.getApplicationNumber();

    }

    private WaterConnectionDetails populateAndPersistWaterConnectionDetails(final WaterConnectionInfo waterConnectionInfo,
            final String applicationCode) {
        WaterConnectionDetails waterConnectionDetails = null;
        Long approvalPosition = 0l;

        Position userPosition = null;
        if (!applicationCode.equals(WaterTaxConstants.CHANGEOFUSE)) {
            waterConnectionDetails = prepareNewAndAdditionalConnectionDetails(waterConnectionInfo, applicationCode);
            userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(waterConnectionDetails.getConnection()
                    .getPropertyIdentifier());
        }

        if (userPosition != null)
            approvalPosition = userPosition.getId();
        if (!applicationCode.equals(WaterTaxConstants.CHANGEOFUSE))
            waterConnectionDetails = waterConnectionDetailsService.createNewWaterConnection(waterConnectionDetails,
                    approvalPosition, "Rest Api", waterConnectionDetails.getApplicationType().getCode(), null, null);
        return waterConnectionDetails;

    }

    private WaterConnectionDetails prepareNewAndAdditionalConnectionDetails(final WaterConnectionInfo waterConnectionInfo,
            final String applicationCode) {

        final WaterConnection waterConnection = new WaterConnection();

        final WaterConnectionDetails waterConnectionDetails = new WaterConnectionDetails();

        waterConnection.setPropertyIdentifier(waterConnectionInfo.getPropertyID());
        waterConnectionDetails.setConnection(waterConnection);
        waterConnectionDetails.setApplicationDate(new Date());
        if (applicationCode.equals(WaterTaxConstants.NEWCONNECTION))
            waterConnectionDetails.setApplicationType(applicationTypeService
                    .findByCode(WaterTaxConstants.NEWCONNECTION));
        else {
            final WaterConnection connection = waterConnectionService.findByConsumerCode(waterConnectionInfo
                    .getConsumerCode());
            waterConnectionDetails.setApplicationType(applicationTypeService
                    .findByCode(WaterTaxConstants.ADDNLCONNECTION));
            waterConnection.setParentConnection(connection);
        }
        waterConnectionDetails.setCategory(connectionCategoryService.findByCode(waterConnectionInfo.getCategory()));
        return prepareWaterConnectionDetails(waterConnectionInfo, waterConnectionDetails);
    }

    private WaterConnectionDetails prepareChangeOfUsageWaterConnectionDetails(final WaterChargesDetailInfo waterConnectionInfo,
            final WaterConnectionDetails waterConnectionDetails) {
        final WaterConnectionDetails changeOfUse = new WaterConnectionDetails();
        changeOfUse.setApplicationDate(new Date());
        changeOfUse.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.CHANGEOFUSE));
        changeOfUse.setCategory(connectionCategoryService.findByCode(waterConnectionInfo.getCategory()));
        changeOfUse.setConnectionReason(waterConnectionInfo.getReasonforChangeOfUsage());
        return prepareConnectionDetailsForChangeOfUse(waterConnectionInfo, changeOfUse);
    }

    private WaterConnectionDetails prepareWaterConnectionDetails(final WaterConnectionInfo waterConnectionInfo,
            final WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
        waterConnectionDetails.setConnectionType(waterConnectionInfo.getConnectionType().equals(
                ConnectionType.METERED.toString()) ? ConnectionType.METERED : ConnectionType.NON_METERED);

        final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
        if (appProcessTime != null)
            waterConnectionDetails.setDisposalDate(waterConnectionDetailsService.getDisposalDate(
                    waterConnectionDetails, appProcessTime));
        waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                WaterTaxConstants.APPLICATION_STATUS_CREATED, WaterTaxConstants.MODULETYPE));
        waterConnectionDetails.setPipeSize(pipeSizeService.findByCode(waterConnectionInfo.getPipeSize()));
        waterConnectionDetails.setPropertyType(propertyTypeService.findByCode(waterConnectionInfo.getPropertyType()));
        waterConnectionDetails.setUsageType(usageTypeService.findByCode(waterConnectionInfo.getUsageType()));
        waterConnectionDetails.setWaterSource(waterSourceService.findByCode(waterConnectionInfo.getWaterSource()));
        if (waterConnectionDetails.getUsageType().getCode().equals("LODGES"))
            waterConnectionDetails.setNumberOfRooms(waterConnectionInfo.getNumberOfRooms());
        else
            waterConnectionDetails.setNumberOfPerson(waterConnectionInfo.getNumberOfPersons());

        return waterConnectionDetails;
    }

    private WaterConnectionDetails prepareConnectionDetailsForChangeOfUse(final WaterChargesDetailInfo waterConnectionInfo,
            final WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
        waterConnectionDetails.setConnectionType(waterConnectionInfo.getConnectionType().equals(
                ConnectionType.METERED.toString()) ? ConnectionType.METERED : ConnectionType.NON_METERED);

        final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
        if (appProcessTime != null)
            waterConnectionDetails.setDisposalDate(waterConnectionDetailsService.getDisposalDate(
                    waterConnectionDetails, appProcessTime));
        waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                WaterTaxConstants.APPLICATION_STATUS_CREATED, WaterTaxConstants.MODULETYPE));
        waterConnectionDetails.setPipeSize(pipeSizeService.findByCode(waterConnectionInfo.getPipeSize()));
        waterConnectionDetails.setPropertyType(propertyTypeService.findByCode(waterConnectionInfo.getPropertyType()));
        waterConnectionDetails.setUsageType(usageTypeService.findByCode(waterConnectionInfo.getUsageType()));
        waterConnectionDetails.setWaterSource(waterSourceService.findByCode(waterConnectionInfo.getWaterSource()));
        if (waterConnectionDetails.getUsageType().getCode().equals("LODGES"))
            waterConnectionDetails.setNumberOfRooms(waterConnectionInfo.getNumberOfRooms());
        else
            waterConnectionDetails.setNumberOfPerson(waterConnectionInfo.getNumberOfPersons());

        return waterConnectionDetails;
    }

    @RequestMapping(value = "/watertax/connectiondetails", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<WaterChargesDetails> getWaterConnectionDetailsByPropertyId(final WaterTaxDetailRequest waterTaxDetailRequest) {

        List<WaterChargesDetails> waterChargesDetailsList;
        waterChargesDetailsList = connectionDetailService.getWaterTaxDetailsByPropertyId(
                waterTaxDetailRequest.getAssessmentNumber(),
                waterTaxDetailRequest.getUlbCode(), waterTaxDetailRequest.getConsumerNumber());

        return waterChargesDetailsList;
    }

    private String getJSONResponse(final Object obj) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        return objectMapper.writeValueAsString(obj);
    }

    public Boolean authenticateUser(final String username, final String password) {
        Boolean isAuthenticated = false;
        if (username.equals("mahesh") && password.equals("demo"))
            isAuthenticated = true;
        ApplicationThreadLocals.setUserId(Long.valueOf("2"));
        return isAuthenticated;
    }
}