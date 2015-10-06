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
package org.egov.restapi.web.rest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.infra.security.token.service.TokenService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.restapi.model.ConnectionInfo;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.NewConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterSourceService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Connection Service class provided RESTful services to create water connection
 */
@RestController
public class RestNewConnectionController {
    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private NewConnectionService newConnectionService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    private ConnectionCategoryService connectionCategoryService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private PipeSizeService pipeSizeService;

    @Autowired
    private PropertyTypeService propertyTypeService;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private WaterSourceService waterSourceService;

    @PersistenceContext
    private EntityManager entityManager;

    public static final String SUCCESS = "SUCCESS";

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @RequestMapping(value = "/watercharges/newconnection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createNewConnection(
            @Valid @RequestBody final ConnectionInfo connectionInfo) throws IOException {
        final WaterConnectionDetails waterConnectionDetails = populateAndPersistWaterConnectionDetails(connectionInfo);
        return waterConnectionDetails.getConnection().getConsumerCode();
        /*
         * String httpStatus = HttpStatus.OK.getReasonPhrase(); try { System.out.println("newConnection :  " +
         * connectionInfo.toString()); System.out.println("token : " + token); final Boolean isAuthenticatedUser =
         * authenticateUser("mahesh", "demo"); final Token tokenObj = validateToken(token); if (tokenObj != null) { if
         * (isAuthenticatedUser) { tokenService.redeem(tokenObj); } else httpStatus = HttpStatus.UNAUTHORIZED.getReasonPhrase(); }
         * else httpStatus = HttpStatus.PRECONDITION_FAILED.getReasonPhrase(); } catch (final Exception exp) {
         * exp.printStackTrace(); } return httpStatus;
         */
    }

    /*
     * @RequestMapping(value = "/watercharges/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     * public String getToken() throws JsonGenerationException, JsonMappingException, IOException { final String strQuery =
     * "select md from EgModules md where md.name=:name"; final Query hql = getCurrentSession().createQuery(strQuery);
     * hql.setParameter("name", WaterTaxConstants.EGMODULES_NAME); final Map<String, String> tokenMap = new HashMap<String,
     * String>(0); tokenMap.put("tokennumber", tokenService.generate(TTL_FOR_TOKEN_SECS, ((EgModules)
     * hql.uniqueResult()).getName()).getTokenNumber()); return getJSONResponse(tokenMap); }
     */

    private WaterConnectionDetails populateAndPersistWaterConnectionDetails(final ConnectionInfo connectionInfo)
            throws IOException {
        // TODO : Persist WaterConnectionDetails
        WaterConnectionDetails waterConnectionDetails = prepareWaterConnectionDetails(connectionInfo);
        String errorMessage = validateWaterConnectionDetails(waterConnectionDetails);
        if (errorMessage.isEmpty()) {
            waterConnectionDetails = waterConnectionDetailsService.createNewWaterConnection(waterConnectionDetails, null, null,
                    null,
                    null);
        }
        return waterConnectionDetails;

    }

    private String validateWaterConnectionDetails(WaterConnectionDetails waterConnectionDetails) {
        String responseMessage = "";
        responseMessage = newConnectionService.checkValidPropertyAssessmentNumber(waterConnectionDetails.getConnection()
                .getPropertyIdentifier());
        if (responseMessage.isEmpty()) {
            responseMessage = newConnectionService.checkConnectionPresentForProperty(waterConnectionDetails.getConnection()
                    .getPropertyIdentifier());
        }
        return responseMessage;
    }

    private WaterConnectionDetails prepareWaterConnectionDetails(final ConnectionInfo connectionInfo)
            throws IOException {
        final String response = validatePropertyID(connectionInfo.getPropertyID());
        final WaterConnection waterConnection = new WaterConnection();
        final WaterConnectionDetails waterConnectionDetails = new WaterConnectionDetails();
        if (response.equals(SUCCESS)) {
            waterConnection.setPropertyIdentifier(connectionInfo.getPropertyID());
            waterConnectionDetails.setConnection(waterConnection);
            waterConnectionDetails.setApplicationDate(new Date());
            waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.NEWCONNECTION));
            waterConnectionDetails.setCategory(connectionCategoryService.findByCode(connectionInfo.getCategory()));
            waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
            waterConnectionDetails
                    .setConnectionType(connectionInfo.getConnectionType().equals(ConnectionType.METERED.toString()) ? ConnectionType.METERED
                            : ConnectionType.NON_METERED);
            final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                    waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
            if (appProcessTime != null)
                waterConnectionDetails.setDisposalDate(waterConnectionDetailsService.getDisposalDate(waterConnectionDetails,
                        appProcessTime));
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    WaterTaxConstants.APPLICATION_STATUS_CREATED, WaterTaxConstants.MODULETYPE));
            waterConnectionDetails.setPipeSize(pipeSizeService.findByCode(connectionInfo.getPipeSize()));
            waterConnectionDetails.setPropertyType(propertyTypeService.findByCode(connectionInfo.getPropertyType()
                    ));
            waterConnectionDetails.setUsageType(usageTypeService.findByCode(connectionInfo.getUsageType()));
            waterConnectionDetails.setWaterSource(waterSourceService
                    .findByCode(connectionInfo.getWaterSource()));
            if (waterConnectionDetails.getUsageType().getCode().equals("Lodges"))
                waterConnectionDetails.setNumberOfRooms(connectionInfo.getNumberOfRooms());
            else
                waterConnectionDetails.setNumberOfPerson(connectionInfo.getNumberOfPersons());
        }
        return waterConnectionDetails;
    }

    private String validatePropertyID(final String propertyid) throws IOException {
        final String errorMessage = newConnectionService.checkValidPropertyAssessmentNumber(propertyid);
        if (errorMessage != null && !errorMessage.equals(""))
            return getJSONResponse(getErrorDetails("3", "InValid Property Id"));
        return SUCCESS;
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
        EgovThreadLocals.setUserId(Long.valueOf("2"));
        return isAuthenticated;
    }

    private Map<String, String> getErrorDetails(final String errorCode, final String errorMessage) {
        final Map<String, String> errorDetailsMap = new HashMap<String, String>(0);
        errorDetailsMap.put("errorcode", errorCode);
        errorDetailsMap.put("errormessage", errorMessage);
        return errorDetailsMap;
    }
}