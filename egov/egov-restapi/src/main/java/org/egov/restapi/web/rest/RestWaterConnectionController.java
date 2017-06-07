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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.model.WaterConnectionInfo;
import org.egov.restapi.util.JsonConvertor;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.rest.WaterChargesDetails;
import org.egov.wtms.application.service.ChangeOfUseService;
import org.egov.wtms.application.service.ConnectionDetailService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
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
import org.springframework.http.MediaType;
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

    /*
     * @Autowired private TokenService tokenService;
     */

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

    /*
     * @PersistenceContext private EntityManager entityManager;
     */

    public static final String SUCCESS = "SUCCESS";

    /*
     * public Session getCurrentSession() { return entityManager.unwrap(Session.class); }
     */

    @RequestMapping(value = "/watercharges/newconnection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createNewConnection(@Valid @RequestBody final WaterConnectionInfo WaterConnectionInfo)
            throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        final ErrorDetails response = restWaterConnectionValidationService.validatePropertyID(WaterConnectionInfo
                .getPropertyID());
        if (response != null)
            return getJSONResponse(response);
        final ErrorDetails errorMessage = restWaterConnectionValidationService
                .validateWaterConnectionDetails(WaterConnectionInfo);
        if (errorMessage != null)
            return getJSONResponse(errorMessage);
        final ErrorDetails errorDetails = restWaterConnectionValidationService.validateCreateRequest(WaterConnectionInfo);
        if (errorDetails != null)
            return getJSONResponse(errorDetails);
        final String applicationCode = WaterTaxConstants.NEWCONNECTION;
        final WaterConnectionDetails waterConnectionDetails = populateAndPersistWaterConnectionDetails(WaterConnectionInfo,
                applicationCode);
        return waterConnectionDetails.getApplicationNumber();
        /*
         * String httpStatus = HttpStatus.OK.getReasonPhrase(); try { final Boolean isAuthenticatedUser =
         * authenticateUser("mahesh", "demo"); final Token tokenObj = validateToken(token); if (tokenObj != null) { if
         * (isAuthenticatedUser) { tokenService.redeem(tokenObj); } else httpStatus = HttpStatus.UNAUTHORIZED.getReasonPhrase(); }
         * else httpStatus = HttpStatus.PRECONDITION_FAILED.getReasonPhrase(); } catch (final Exception exp) { } return
         * httpStatus;
         */
    }

    @RequestMapping(value = "/watercharges/additionalconnection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createAddtionalConnection(@Valid @RequestBody final WaterConnectionInfo waterConnectionInfo)
            throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        final ErrorDetails response = restWaterConnectionValidationService
                .validateAdditionalWaterConnectionDetails(waterConnectionInfo);
        final String applicationCode = WaterTaxConstants.ADDNLCONNECTION;
        if (response != null)
            return getJSONResponse(response);
        final ErrorDetails errorDetails = restWaterConnectionValidationService.validateCreateRequest(waterConnectionInfo);
        if (errorDetails != null)
            return getJSONResponse(errorDetails);
        final WaterConnectionDetails waterConnectionDetails = populateAndPersistWaterConnectionDetails(waterConnectionInfo,
                applicationCode);
        return waterConnectionDetails.getApplicationNumber();

    }

    /*
     * @RequestMapping(value = "/watercharges/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     * public String getToken() throws JsonGenerationException, JsonMappingException, IOException { final String strQuery =
     * "select md from EgModules md where md.name=:name"; final Query hql = getCurrentSession().createQuery(strQuery);
     * hql.setParameter("name", WaterTaxConstants.EGMODULES_NAME); final Map<String, String> tokenMap = new HashMap<String,
     * String>(0); tokenMap.put("tokennumber", tokenService.generate(TTL_FOR_TOKEN_SECS, ((EgModules)
     * hql.uniqueResult()).getName()).getTokenNumber()); return getJSONResponse(tokenMap); }
     */

    @RequestMapping(value = "/watercharges/changeofuse", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createChangeOfUsageConnection(@Valid @RequestBody final WaterConnectionInfo waterConnectionInfo)
            throws JsonGenerationException, JsonMappingException, IOException, ParseException {

        final ErrorDetails response = restWaterConnectionValidationService
                .validateChangOfUsageWaterConnectionDetails(waterConnectionInfo);

        final String applicationCode = WaterTaxConstants.CHANGEOFUSE;
        if (response != null)
            return getJSONResponse(response);
        final ErrorDetails errorMessage = restWaterConnectionValidationService
                .validateCombinationOfChangOfUsage(waterConnectionInfo);
        if (errorMessage != null)
            return getJSONResponse(response);
        final ErrorDetails errorDetails = restWaterConnectionValidationService.validateCreateRequest(waterConnectionInfo);
        if (errorDetails != null)
            return getJSONResponse(errorDetails);
        final WaterConnectionDetails waterConnectionDetails = populateAndPersistWaterConnectionDetails(waterConnectionInfo,
                applicationCode);
        return waterConnectionDetails.getApplicationNumber();

    }

    private WaterConnectionDetails populateAndPersistWaterConnectionDetails(final WaterConnectionInfo WaterConnectionInfo,
            final String applicationCode) {
        WaterConnectionDetails waterConnectionDetails = null;
        Long approvalPosition = 0l;

        Position userPosition = null;
        if (!applicationCode.equals(WaterTaxConstants.CHANGEOFUSE)) {
            waterConnectionDetails = prepareNewAndAdditionalConnectionDetails(WaterConnectionInfo, applicationCode);
            userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(waterConnectionDetails.getConnection()
                    .getPropertyIdentifier());
        } else {
            final WaterConnectionDetails connectionUnderChange = waterConnectionDetailsService
                    .findByConsumerCodeAndConnectionStatus(WaterConnectionInfo.getConsumerCode(), ConnectionStatus.ACTIVE);
            waterConnectionDetails = prepareChangeOfUsageWaterConnectionDetails(WaterConnectionInfo, connectionUnderChange);
            waterConnectionDetails.setConnection(connectionUnderChange.getConnection());
            userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(connectionUnderChange.getConnection()
                    .getPropertyIdentifier());
        }

        if (userPosition != null)
            approvalPosition = userPosition.getId();

        if (applicationCode.equals(WaterTaxConstants.CHANGEOFUSE))
            waterConnectionDetails = changeOfUseService.createChangeOfUseApplication(waterConnectionDetails,
                    approvalPosition, "Rest Api", waterConnectionDetails.getApplicationType().getCode(), null, null);
        else
            waterConnectionDetails = waterConnectionDetailsService.createNewWaterConnection(waterConnectionDetails,
                    approvalPosition, "Rest Api", waterConnectionDetails.getApplicationType().getCode(), null, null);
        return waterConnectionDetails;

    }

    private WaterConnectionDetails prepareNewAndAdditionalConnectionDetails(final WaterConnectionInfo WaterConnectionInfo,
            final String applicationCode) {

        final WaterConnection waterConnection = new WaterConnection();

        final WaterConnectionDetails waterConnectionDetails = new WaterConnectionDetails();

        waterConnection.setPropertyIdentifier(WaterConnectionInfo.getPropertyID());
        waterConnectionDetails.setConnection(waterConnection);
        waterConnectionDetails.setApplicationDate(new Date());
        if (applicationCode.equals(WaterTaxConstants.NEWCONNECTION))
            waterConnectionDetails.setApplicationType(applicationTypeService
                    .findByCode(WaterTaxConstants.NEWCONNECTION));
        else {
            final WaterConnection connection = waterConnectionService.findByConsumerCode(WaterConnectionInfo
                    .getConsumerCode());
            waterConnectionDetails.setApplicationType(applicationTypeService
                    .findByCode(WaterTaxConstants.ADDNLCONNECTION));
            waterConnection.setParentConnection(connection);
        }
        waterConnectionDetails.setCategory(connectionCategoryService.findByCode(WaterConnectionInfo.getCategory()));
        return prepareWaterConnectionDetails(WaterConnectionInfo, waterConnectionDetails);
    }

    private WaterConnectionDetails prepareChangeOfUsageWaterConnectionDetails(final WaterConnectionInfo waterConnectionInfo,
            final WaterConnectionDetails waterConnectionDetails) {
        final WaterConnectionDetails changeOfUse = new WaterConnectionDetails();
        changeOfUse.setApplicationDate(new Date());
        changeOfUse.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.CHANGEOFUSE));
        changeOfUse.setCategory(connectionCategoryService.findByCode(waterConnectionInfo.getCategory()));
        changeOfUse.setConnectionReason(waterConnectionInfo.getReasonforChangeOfUsage());
        return prepareWaterConnectionDetails(waterConnectionInfo, changeOfUse);
    }

    private WaterConnectionDetails prepareWaterConnectionDetails(final WaterConnectionInfo WaterConnectionInfo,
            final WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
        waterConnectionDetails.setConnectionType(WaterConnectionInfo.getConnectionType().equals(
                ConnectionType.METERED.toString()) ? ConnectionType.METERED : ConnectionType.NON_METERED);

        final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
        if (appProcessTime != null)
            waterConnectionDetails.setDisposalDate(waterConnectionDetailsService.getDisposalDate(
                    waterConnectionDetails, appProcessTime));
        waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                WaterTaxConstants.APPLICATION_STATUS_CREATED, WaterTaxConstants.MODULETYPE));
        waterConnectionDetails.setPipeSize(pipeSizeService.findByCode(WaterConnectionInfo.getPipeSize()));
        waterConnectionDetails.setPropertyType(propertyTypeService.findByCode(WaterConnectionInfo.getPropertyType()));
        waterConnectionDetails.setUsageType(usageTypeService.findByCode(WaterConnectionInfo.getUsageType()));
        waterConnectionDetails.setWaterSource(waterSourceService.findByCode(WaterConnectionInfo.getWaterSource()));
        if (waterConnectionDetails.getUsageType().getCode().equals("LODGES"))
            waterConnectionDetails.setNumberOfRooms(WaterConnectionInfo.getNumberOfRooms());
        else
            waterConnectionDetails.setNumberOfPerson(WaterConnectionInfo.getNumberOfPersons());

        return waterConnectionDetails;
    }

    @RequestMapping(value = "/watertax/connectiondetails", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWaterConnectionDetailsByPropertyId(@Valid @RequestBody final WaterTaxDetailRequest waterTaxDetailRequest)
            throws JsonGenerationException, JsonMappingException, IOException {

        List<WaterChargesDetails> waterChargesDetailsList;
        waterChargesDetailsList = connectionDetailService.getWaterTaxDetailsByPropertyId(
                waterTaxDetailRequest.getAssessmentNumber(),
                waterTaxDetailRequest.getUlbCode(), waterTaxDetailRequest.getConsumerNumber());

        return JsonConvertor.convert(waterChargesDetailsList);
    }

    private String getJSONResponse(final Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        final String jsonResponse = objectMapper.writeValueAsString(obj);
        return jsonResponse;
    }

    public Boolean authenticateUser(final String username, final String password) {
        Boolean isAuthenticated = false;
        if (username.equals("mahesh") && password.equals("demo"))
            isAuthenticated = true;
        ApplicationThreadLocals.setUserId(Long.valueOf("2"));
        return isAuthenticated;
    }
}