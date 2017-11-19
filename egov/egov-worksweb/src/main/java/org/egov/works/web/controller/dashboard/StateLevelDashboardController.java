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

package org.egov.works.web.controller.dashboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.works.elasticsearch.model.WorksIndexsRequest;
import org.egov.works.elasticsearch.model.WorksMilestoneIndexResponse;
import org.egov.works.elasticsearch.service.WorksMilestoneIndexService;
import org.egov.works.elasticsearch.service.WorksTransactionIndexService;
import org.egov.works.web.adaptor.WorksMilestoneIndexJsonAdaptor;
import org.egov.works.web.adaptor.WorksSectorReportJsonAdaptor;
import org.egov.works.web.adaptor.WorksTransactionIndexJsonAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.works.utils.WorksConstants.DISTRICT_COLUMN_HEADER_NAME;
import static org.egov.works.utils.WorksConstants.SECTOR_COLUMN_HEADER_NAME;
import static org.egov.works.utils.WorksConstants.ULB_COLUMN_HEADER_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_DISTNAME_COLUMN_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_TYPEOFWORKNAME_COLUMN_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_ULBNAME_COLUMN_NAME;

/**
 * @author venki
 */

@RestController
@RequestMapping(value = "/worksdashboard")
public class StateLevelDashboardController {

    private static final String DATA = "{ \"data\":";

    private static final Logger LOGGER = LoggerFactory.getLogger(StateLevelDashboardController.class);

    @Autowired
    private WorksMilestoneIndexService worksMilestoneIndexService;

    @Autowired
    private WorksTransactionIndexService worksTransactionIndexService;

    @Autowired
    private WorksMilestoneIndexJsonAdaptor worksMilestoneIndexJsonAdaptor;

    @Autowired
    private WorksTransactionIndexJsonAdaptor worksTransactionIndexJsonAdaptor;

    @Autowired
    private WorksSectorReportJsonAdaptor worksSectorReportJsonAdaptor;

    @RequestMapping(value = "/statewisetypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getStateWiseTypeOfWorkDetails() throws IOException {

        final Long startTime;
        final Long timeTaken;
        final List<WorksMilestoneIndexResponse> resultList;
        final String result;
        final WorksIndexsRequest worksIndexsRequest = new WorksIndexsRequest();

        startTime = System.currentTimeMillis();
        worksIndexsRequest.setReportType(SECTOR_COLUMN_HEADER_NAME);
        resultList = worksMilestoneIndexService.getAggregationResults(worksIndexsRequest,
                WORKSMILESTONE_TYPEOFWORKNAME_COLUMN_NAME);
        result = new StringBuilder(DATA).append(toMilestoneJson(resultList)).append("}").toString();
        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve getStateWiseTypeOfWorkDetails is : " + timeTaken + " (millisecs)");

        return result;

    }

    @RequestMapping(value = "/districtwise-bytypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getDistrictWiseByTypeOfWork(@RequestParam("typeofwork") final String typeofwork)
            throws IOException {

        final Long startTime;
        final Long timeTaken;
        final List<WorksMilestoneIndexResponse> resultList;
        final String result;
        final WorksIndexsRequest worksIndexsRequest = new WorksIndexsRequest();

        worksIndexsRequest.setTypeofwork(typeofwork);
        worksIndexsRequest.setReportType(DISTRICT_COLUMN_HEADER_NAME);
        startTime = System.currentTimeMillis();
        resultList = worksMilestoneIndexService.getAggregationResults(worksIndexsRequest,
                WORKSMILESTONE_DISTNAME_COLUMN_NAME);
        result = new StringBuilder(DATA).append(toMilestoneJson(resultList)).append("}").toString();
        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve getDistrictWiseDistrictDetails is : " + timeTaken + " (millisecs)");

        return result;
    }

    @RequestMapping(value = "/ulbwise-bytypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getUlbWiseByTypeOfWork(@RequestParam("typeofwork") final String typeofwork)
            throws IOException {

        final Long startTime;
        final Long timeTaken;
        final List<WorksMilestoneIndexResponse> resultList;
        final String result;
        final WorksIndexsRequest worksIndexsRequest = new WorksIndexsRequest();

        worksIndexsRequest.setTypeofwork(typeofwork);
        worksIndexsRequest.setReportType(ULB_COLUMN_HEADER_NAME);
        startTime = System.currentTimeMillis();
        resultList = worksMilestoneIndexService.getAggregationResults(worksIndexsRequest,
                WORKSMILESTONE_ULBNAME_COLUMN_NAME);
        result = new StringBuilder(DATA).append(toMilestoneJson(resultList)).append("}").toString();
        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve getUlbWiseDistrictDetails is : " + timeTaken + " (millisecs)");

        return result;
    }

    @RequestMapping(value = "/ulbwise-bydistrictandtypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getUlbWiseByDistrictAndTypeOfWork(@RequestParam("typeofwork") final String typeofwork,
            @RequestParam("districtname") final String districtname) throws IOException {

        final Long startTime;
        final Long timeTaken;
        final List<WorksMilestoneIndexResponse> resultList;
        final String result;
        final WorksIndexsRequest worksIndexsRequest = new WorksIndexsRequest();

        worksIndexsRequest.setTypeofwork(typeofwork);
        worksIndexsRequest.setDistname(districtname);
        worksIndexsRequest.setReportType(ULB_COLUMN_HEADER_NAME);
        startTime = System.currentTimeMillis();
        resultList = worksMilestoneIndexService.getAggregationResults(worksIndexsRequest,
                WORKSMILESTONE_ULBNAME_COLUMN_NAME);
        result = new StringBuilder(DATA).append(toMilestoneJson(resultList)).append("}").toString();
        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve getUlbWiseByDistrictAndTypeOfWork is : " + timeTaken + " (millisecs)");

        return result;
    }

    @RequestMapping(value = "/ulbwise-bytypeofworkandulbs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getUlbWiseByTypeOfWorkAndUlbs(@RequestParam("typeofwork") final String typeofwork,
            @RequestParam("ulbcodes") final String ulbcodes) throws IOException {

        final Long startTime;
        final Long timeTaken;
        final List<WorksMilestoneIndexResponse> resultList;
        final String result;
        final WorksIndexsRequest worksIndexsRequest = new WorksIndexsRequest();

        worksIndexsRequest.setTypeofwork(typeofwork);
        worksIndexsRequest.setUlbcodes(Arrays.asList(ulbcodes.split(",")));
        worksIndexsRequest.setReportType(ULB_COLUMN_HEADER_NAME);
        startTime = System.currentTimeMillis();
        resultList = worksMilestoneIndexService.getAggregationResults(worksIndexsRequest,
                WORKSMILESTONE_ULBNAME_COLUMN_NAME);
        result = new StringBuilder(DATA).append(toMilestoneJson(resultList)).append("}").toString();
        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve getUlbWiseByTypeOfWorkAndUlbs is : " + timeTaken + " (millisecs)");

        return result;
    }

    @RequestMapping(value = "/statewiseulb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getStateWiseULBDetails(@RequestParam("typeofwork") final String typeofwork,
            @RequestParam("ulbname") final String ulbname) throws IOException {

        final Long startTime;
        final Long timeTaken;
        List<WorksMilestoneIndexResponse> resultList;
        final String result;
        final WorksIndexsRequest worksIndexsRequest = new WorksIndexsRequest();

        startTime = System.currentTimeMillis();
        worksIndexsRequest.setTypeofwork(typeofwork);
        worksIndexsRequest.setUlbname(ulbname);
        resultList = worksTransactionIndexService.getWorksTransactionDetails(worksIndexsRequest);
        final List<Integer> lineestimatedetailids = new ArrayList<>();
        for (final WorksMilestoneIndexResponse response : resultList)
            lineestimatedetailids.add(response.getLineestimatedetailid());
        worksIndexsRequest.setUlbname(ulbname);
        worksIndexsRequest.setLineestimatedetailids(lineestimatedetailids);
        resultList = worksMilestoneIndexService.getAggregationResultsForUlb(worksIndexsRequest, resultList);

        result = new StringBuilder(DATA).append(toTransactionJson(resultList)).append("}").toString();
        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve getStateWiseULBDetails is : " + timeTaken + " (millisecs)");

        return result;
    }

    @RequestMapping(value = "/sectorwisereport", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSectorWiseULBDetails() throws IOException {

        final Long startTime;
        final Long timeTaken;
        List<WorksMilestoneIndexResponse> resultList;
        final List<WorksMilestoneIndexResponse> finalResultList = new ArrayList<>();
        final String result;
        final WorksIndexsRequest worksIndexsRequest = new WorksIndexsRequest();

        startTime = System.currentTimeMillis();
        resultList = worksTransactionIndexService.getWorksTransactionDetails(worksIndexsRequest);

        final Map<String, List<WorksMilestoneIndexResponse>> ulbNameWiseMap = new HashMap<>();
        for (final WorksMilestoneIndexResponse response : resultList)
            if (ulbNameWiseMap.get(response.getUlbname()) != null)
                ulbNameWiseMap.get(response.getUlbname()).add(response);
            else {
                ulbNameWiseMap.put(response.getUlbname(), new ArrayList<>());
                ulbNameWiseMap.get(response.getUlbname()).add(response);
            }
        List<Integer> lineestimatedetailids;

        for (final String key : ulbNameWiseMap.keySet()) {
            lineestimatedetailids = new ArrayList<>();
            for (final WorksMilestoneIndexResponse response : ulbNameWiseMap.get(key))
                lineestimatedetailids.add(response.getLineestimatedetailid());

            worksIndexsRequest.setUlbname(key);
            worksIndexsRequest.setLineestimatedetailids(lineestimatedetailids);
            resultList = worksMilestoneIndexService.getAggregationResultsForUlb(worksIndexsRequest, ulbNameWiseMap.get(key));
            finalResultList.addAll(resultList);

        }

        result = new StringBuilder(DATA).append(toSectorReportJson(finalResultList)).append("}").toString();
        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve getSectorWiseULBDetails is : " + timeTaken + " (millisecs)");

        return result;
    }

    public Object toMilestoneJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorksMilestoneIndexResponse.class, worksMilestoneIndexJsonAdaptor)
                .create();
        return gson.toJson(object);
    }

    public Object toTransactionJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorksMilestoneIndexResponse.class, worksTransactionIndexJsonAdaptor)
                .create();
        return gson.toJson(object);
    }

    public Object toSectorReportJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorksMilestoneIndexResponse.class, worksSectorReportJsonAdaptor)
                .create();
        return gson.toJson(object);
    }

}