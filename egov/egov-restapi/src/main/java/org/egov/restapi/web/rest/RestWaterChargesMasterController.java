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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.DocumentNamesService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RestWaterChargesMasterController {

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private ConnectionCategoryService connectionCategoryService;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private DocumentNamesService documentNamesService;

    @Autowired
    private PipeSizeService pipeSizeService;

    @Autowired
    private WaterSourceService waterSourceService;

    @Autowired
    private PropertyTypeService propertyTypeService;

    /*@RequestMapping(value = "/watercharges/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConnectionCategoryList() throws JsonGenerationException, JsonMappingException, IOException {
        final List<ConnectionCategory> connectionCategoryList = connectionCategoryService.getConnectionCategoryListForRest();
        return getJSONResponse(connectionCategoryList);
    }

    @RequestMapping(value = "/watercharges/usagetypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsageTypeList() throws JsonGenerationException, JsonMappingException, IOException {
        final List<UsageType> usageTypeList = usageTypeService.getUsageTypeListForRest();
        return getJSONResponse(usageTypeList);
    }

    @RequestMapping(value = "/watercharges/documentnames", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDocumentNameList() throws JsonGenerationException, JsonMappingException, IOException {
        final List<DocumentNames> documentNamesList = documentNamesService.getDocumentNamesListForRest();
        return getJSONResponse(documentNamesList);
    }

    @RequestMapping(value = "/watercharges/pipesizes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPipeSizeList() throws JsonGenerationException, JsonMappingException, IOException {
        final List<PipeSize> pipeSizeList = pipeSizeService.getPipeSizeListForRest();
        return getJSONResponse(pipeSizeList);
    }

    @RequestMapping(value = "/watercharges/watersourcetypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWaterSourceTypes() throws JsonGenerationException, JsonMappingException, IOException {
        final List<WaterSource> waterSourceList = waterSourceService.getWaterSourceListForRest();
        return getJSONResponse(waterSourceList);
    }

    @RequestMapping(value = "/watercharges/waterconnectiontypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWaterConnectionTypes() throws JsonGenerationException, JsonMappingException, IOException {
        final List<Map<String, String>> connectionTypeList = new ArrayList<Map<String, String>>(0);
        final Map<String, String> meteredTypeMap = new HashMap<String, String>(0);
        meteredTypeMap.put("code", ConnectionType.METERED.toString());
        final Map<String, String> nonMeteredTypeMap = new HashMap<String, String>(0);
        nonMeteredTypeMap.put("code", ConnectionType.NON_METERED.toString());
        connectionTypeList.add(meteredTypeMap);
        connectionTypeList.add(nonMeteredTypeMap);
        return getJSONResponse(connectionTypeList);
    }

    @RequestMapping(value = "/watercharges/propertytypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPropertyTypes() throws JsonGenerationException, JsonMappingException, IOException {
        final List<PropertyType> propertyTypeList = propertyTypeService.getPropertyTypeListForRest();
        return getJSONResponse(propertyTypeList);
    }*/

    /**
     * This method is used to prepare jSON response.
     *
     * @param obj - a POJO object
     * @return jsonResponse - JSON response string
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     *//*
    private String getJSONResponse(final Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        final Gson jsonCreator = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .disableHtmlEscaping().create();
        return jsonCreator.toJson(obj, new TypeToken<Collection<Document>>() {
        }.getType());
    }*/

}
