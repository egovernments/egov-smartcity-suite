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

package org.egov.works.web.controller.dashboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.egov.works.elasticsearch.model.WorksMilestoneIndexRequest;
import org.egov.works.elasticsearch.model.WorksMilestoneIndexResponse;
import org.egov.works.elasticsearch.model.WorksTransactionIndexRequest;
import org.egov.works.elasticsearch.service.WorksMilestoneIndexService;
import org.egov.works.elasticsearch.service.WorksTransactionIndexService;
import org.egov.works.web.adaptor.WorksMilestoneIndexJsonAdaptor;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author venki
 */
@RestController
@RequestMapping(value = "/public/worksdashboard")
public class StateLevelDashboardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateLevelDashboardController.class);

    @Autowired
    private WorksMilestoneIndexService worksMilestoneIndexService;

    @Autowired
    private WorksTransactionIndexService worksTransactionIndexService;

    @Autowired
    private WorksMilestoneIndexJsonAdaptor worksMilestoneIndexJsonAdaptor;

    @Autowired
    private WorksTransactionIndexJsonAdaptor worksTransactionIndexJsonAdaptor;

    @RequestMapping(value = "/statewisetypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getStateWiseTypeOfWorkDetails() throws IOException {
        final Long startTime = System.currentTimeMillis();
        final WorksMilestoneIndexRequest worksMilestoneIndexRequest = new WorksMilestoneIndexRequest();
        worksMilestoneIndexRequest.setReportType("SECTOR");
        final List<WorksMilestoneIndexResponse> resultList = worksMilestoneIndexService
                .returnAggregationResults(worksMilestoneIndexRequest, true, "lineestimatetypeofworkname");
        final String result = new StringBuilder("{ \"data\":").append(toMilestoneJson(resultList))
                .append("}").toString();
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve getStateWiseTypeOfWorkDetails is : " + timeTaken + " (millisecs)");
        return result;
    }

    @RequestMapping(value = "/districtwise-bytypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getDistrictWiseByTypeOfWork(@RequestParam("typeofwork") final String typeofwork)
            throws IOException {
        final WorksMilestoneIndexRequest worksMilestoneIndexRequest = new WorksMilestoneIndexRequest();
        worksMilestoneIndexRequest.setTypeofwork(typeofwork);
        worksMilestoneIndexRequest.setReportType("District Name");
        final Long startTime = System.currentTimeMillis();
        final List<WorksMilestoneIndexResponse> resultList = worksMilestoneIndexService
                .returnAggregationResults(worksMilestoneIndexRequest, true, "distname");
        final String result = new StringBuilder("{ \"data\":").append(toMilestoneJson(resultList))
                .append("}").toString();
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve getDistrictWiseDistrictDetails is : " + timeTaken + " (millisecs)");
        return result;
    }

    @RequestMapping(value = "/ulbwise-bytypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getUlbWiseByTypeOfWork(@RequestParam("typeofwork") final String typeofwork)
            throws IOException {
        final WorksMilestoneIndexRequest worksMilestoneIndexRequest = new WorksMilestoneIndexRequest();
        worksMilestoneIndexRequest.setTypeofwork(typeofwork);
        worksMilestoneIndexRequest.setReportType("ULB Name");
        final Long startTime = System.currentTimeMillis();
        final List<WorksMilestoneIndexResponse> resultList = worksMilestoneIndexService
                .returnAggregationResults(worksMilestoneIndexRequest, true, "ulbname");
        final String result = new StringBuilder("{ \"data\":").append(toMilestoneJson(resultList))
                .append("}").toString();
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve getUlbWiseDistrictDetails is : " + timeTaken + " (millisecs)");
        return result;
    }

    @RequestMapping(value = "/ulbwise-bydistrictandtypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getUlbWiseByDistrictAndTypeOfWork(@RequestParam("typeofwork") final String typeofwork,
            @RequestParam("districtname") final String districtname) throws IOException {
        final WorksMilestoneIndexRequest worksMilestoneIndexRequest = new WorksMilestoneIndexRequest();
        worksMilestoneIndexRequest.setTypeofwork(typeofwork);
        worksMilestoneIndexRequest.setDistname(districtname);
        worksMilestoneIndexRequest.setReportType("ULB Name");
        final Long startTime = System.currentTimeMillis();
        final List<WorksMilestoneIndexResponse> resultList = worksMilestoneIndexService
                .returnAggregationResults(worksMilestoneIndexRequest, true, "ulbname");
        final String result = new StringBuilder("{ \"data\":").append(toMilestoneJson(resultList))
                .append("}").toString();
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve getUlbWiseByDistrictAndTypeOfWork is : " + timeTaken + " (millisecs)");
        return result;
    }

    @RequestMapping(value = "/ulbwise-bytypeofworkandulbs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getUlbWiseByTypeOfWorkAndUlbs(@RequestParam("typeofwork") final String typeofwork,
            @RequestParam("ulbcodes") final String ulbcodes) throws IOException {
        final WorksMilestoneIndexRequest worksMilestoneIndexRequest = new WorksMilestoneIndexRequest();
        worksMilestoneIndexRequest.setTypeofwork(typeofwork);
        worksMilestoneIndexRequest.setUlbcodes(Arrays.asList(ulbcodes.split(",")));
        worksMilestoneIndexRequest.setReportType("ULB Name");
        final Long startTime = System.currentTimeMillis();
        final List<WorksMilestoneIndexResponse> resultList = worksMilestoneIndexService
                .returnAggregationResults(worksMilestoneIndexRequest, true, "ulbname");
        final String result = new StringBuilder("{ \"data\":").append(toMilestoneJson(resultList))
                .append("}").toString();
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve getUlbWiseByTypeOfWorkAndUlbs is : " + timeTaken + " (millisecs)");
        return result;
    }

    @RequestMapping(value = "/statewiseulb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getStateWiseULBDetails(@RequestParam("typeofwork") final String typeofwork,
            @RequestParam("ulbname") final String ulbname) throws IOException {
        final Long startTime = System.currentTimeMillis();
        final WorksMilestoneIndexRequest worksMilestoneIndexRequest = new WorksMilestoneIndexRequest();
        worksMilestoneIndexRequest.setTypeofwork(typeofwork);
        worksMilestoneIndexRequest.setUlbname(ulbname);
        final List<WorksMilestoneIndexResponse> resultList = worksMilestoneIndexService
                .returnAggregationResults(worksMilestoneIndexRequest, true, "lineestimatedetailid");
        WorksTransactionIndexRequest worksTransactionIndexRequest;
        for (final WorksMilestoneIndexResponse response : resultList) {
            worksTransactionIndexRequest = new WorksTransactionIndexRequest();
            worksTransactionIndexRequest.setUlbname(ulbname);
            worksTransactionIndexRequest.setLineestimatedetailid(Integer.valueOf(response.getName()));
            worksTransactionIndexService.getWorksTransactionDetails(worksTransactionIndexRequest, response);
        }
        final String result = new StringBuilder("{ \"data\":").append(toTransactionJson(resultList))
                .append("}").toString();
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve getStateWiseULBDetails is : " + timeTaken + " (millisecs)");
        return result;
    }

    public Object toMilestoneJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorksMilestoneIndexResponse.class, worksMilestoneIndexJsonAdaptor)
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    public Object toTransactionJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorksMilestoneIndexResponse.class, worksTransactionIndexJsonAdaptor)
                .create();
        final String json = gson.toJson(object);
        return json;
    }

}