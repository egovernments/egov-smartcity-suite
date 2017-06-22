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
package org.egov.works.web.controller.mb;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.script.service.ScriptService;
import org.egov.works.contractorportal.entity.ContractorMBHeader;
import org.egov.works.contractorportal.service.ContractorMBHeaderService;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.entity.SearchRequestCancelMB;
import org.egov.works.mb.entity.SearchRequestMBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.adaptor.SearchMBHeaderJsonAdaptor;
import org.egov.works.web.adaptor.SearchMBToCancelJson;
import org.egov.works.web.adaptor.SearchWorkOrderActivityJsonAdaptor;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@RestController
public class AjaxMBController {

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private MBHeaderService mBHeaderService;

    @Autowired
    private SearchMBHeaderJsonAdaptor searchMBHeaderJsonAdaptor;

    @Autowired
    private WorkOrderActivityService workOrderActivityService;

    @Autowired
    private SearchWorkOrderActivityJsonAdaptor searchWorkOrderActivityJsonAdaptor;

    @Autowired
    private SearchMBToCancelJson searchMBToCancelJson;

    @Autowired
    private ContractorMBHeaderService contractorMBHeaderService;

    @Autowired
    private ScriptService scriptService;

    @RequestMapping(value = "/workorder/validatemb/{workOrderEstimateId}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String validateWorkOrder(@PathVariable final Long workOrderEstimateId,
            final HttpServletRequest request, final HttpServletResponse response) {
        final JsonObject jsonObject = new JsonObject();
        mBHeaderService.validateMBInDrafts(workOrderEstimateId, jsonObject, null);
        mBHeaderService.validateMBInWorkFlow(workOrderEstimateId, jsonObject, null);
        if (jsonObject.toString().length() > 2) {
            sendAJAXResponse(jsonObject.toString(), response);
            return "";
        }
        return null;
    }
    
    @RequestMapping(value = "/workorder/validatemb/byworkordernumber", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String validateWorkOrderByWorkOrderNumber(@RequestParam final String workOrderNumber,
            final HttpServletRequest request, final HttpServletResponse response) {
        final JsonObject jsonObject = new JsonObject();
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateByWorkOrderNumber(workOrderNumber);
        mBHeaderService.validateMBInDrafts(workOrderEstimate.getId(), jsonObject, null);
        mBHeaderService.validateMBInWorkFlow(workOrderEstimate.getId(), jsonObject, null);
        if (jsonObject.toString().length() > 2) {
            sendAJAXResponse(jsonObject.toString(), response);
            return "";
        }
        return null;
    }

    protected void sendAJAXResponse(final String msg, final HttpServletResponse response) {
        try {
            final Writer httpResponseWriter = response.getWriter();
            IOUtils.write(msg, httpResponseWriter);
            IOUtils.closeQuietly(httpResponseWriter);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/mbheader/ajaxworkordernumbers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findWorkOrderNumber(@RequestParam final String code) {
        return workOrderEstimateService.getApprovedAndWorkCommencedWorkOrderNumbers(code);
    }

    @RequestMapping(value = "/mbheader/ajaxestimateNumbers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findEstimateNumbersForWorkOrder(@RequestParam final String code) {
        return workOrderEstimateService.getEstimateNumbersByApprovedAndWorkCommencedWorkOrders(code);
    }

    @RequestMapping(value = "/mbheader/ajaxcontractors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Contractor> findContractorsForWorkOrder(@RequestParam final String code) {
        return workOrderEstimateService.getContractorsByWorkOrderStatus(code);
    }

    @RequestMapping(value = "/mbheader/ajaxcontractormbheaders", method = RequestMethod.GET)
    public @ResponseBody List<ContractorMBHeader> findContractorMBHeadersForWorkOrderEstimateId(
            @RequestParam final String workOrderEstimateId) {
        return contractorMBHeaderService.getContractorMBHeaderByWorkOrderEstimateId(Long.valueOf(workOrderEstimateId));
    }

    @RequestMapping(value = "/mbheader/ajax-searchmbheader", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchMBHeaders(@ModelAttribute final SearchRequestMBHeader searchRequestMBHeader) {
        final List<MBHeader> mBHeaderList = mBHeaderService.searchMBHeader(searchRequestMBHeader);
        final String result = new StringBuilder("{ \"data\":").append(searchMBHeader(mBHeaderList)).append("}")
                .toString();
        return result;
    }

    public Object searchMBHeader(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(MBHeader.class, searchMBHeaderJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/measurementbook/ajax-searchactivities", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchWorkOrderActivities(final HttpServletRequest request) {
        final Long workOrderEstimateId = request.getParameter("workOrderEstimateId") != null
                ? Long.parseLong(request.getParameter("workOrderEstimateId")) : null;
        final Long mbHeaderId = request.getParameter("id") != null ? Long.parseLong(request.getParameter("id")) : -1;
        final String description = request.getParameter("description");
        final String itemCode = request.getParameter("itemCode");
        final String sorType = request.getParameter("sorType");
        final String workOrderNumber = request.getParameter("workOrderNumber");
        final List<WorkOrderActivity> workOrderActivities = workOrderActivityService
                .searchActivities(workOrderEstimateId, description, itemCode, sorType, workOrderNumber);
        final List<WorkOrderActivity> activities = new ArrayList<WorkOrderActivity>();
        // TODO re factor this code to handle via criteria
        if (description != null && !description.equals(""))
            for (final WorkOrderActivity woa : workOrderActivities)
                if (woa.getActivity().getSchedule() != null
                        && woa.getActivity().getSchedule().getDescription().toLowerCase()
                                .contains(description.toLowerCase())
                        || woa.getActivity().getNonSor() != null && woa.getActivity().getNonSor().getDescription()
                                .toLowerCase().contains(description.toLowerCase()))
                    activities.add(woa);

        if (!activities.isEmpty()) {
            workOrderActivities.clear();
            workOrderActivities.addAll(activities);
        }

        if (itemCode != null && !itemCode.equals("")) {
            activities.clear();
            for (final WorkOrderActivity woa : workOrderActivities)
                if (woa.getActivity().getSchedule() != null
                        && woa.getActivity().getSchedule().getCode().toLowerCase().contains(itemCode.toLowerCase()))
                    activities.add(woa);
        }

        if (!activities.isEmpty()) {
            workOrderActivities.clear();
            workOrderActivities.addAll(activities);
        }

        for (final WorkOrderActivity woa : workOrderActivities)
            woa.setMbHeaderId(mbHeaderId);

        final String result = new StringBuilder("{ \"data\":")
                .append(toSearchWorkOrderActivityResultJson(workOrderActivities)).append("}").toString();
        return result;
    }

    @RequestMapping(value = "/measurementbook/ajax-searchreactivities", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchREWorkOrderActivities(final HttpServletRequest request) {
        final Long workOrderEstimateId = request.getParameter("workOrderEstimateId") != null
                ? Long.parseLong(request.getParameter("workOrderEstimateId")) : null;;
        final Long mbHeaderId = request.getParameter("id") != null ? Long.parseLong(request.getParameter("id")) : -1;
        final String description = request.getParameter("description");
        final String itemCode = request.getParameter("itemCode");
        final String nonTenderedType = request.getParameter("nonTenderedType");
        final String mbDate = request.getParameter("mbDate");
        final String workOrderNumber = request.getParameter("workOrderNumber");
        final List<WorkOrderActivity> workOrderActivities = workOrderActivityService
                .searchREActivities(workOrderEstimateId, description, itemCode, nonTenderedType, mbDate, workOrderNumber);

        for (final WorkOrderActivity woa : workOrderActivities)
            woa.setMbHeaderId(mbHeaderId);

        final String result = new StringBuilder("{ \"data\":")
                .append(toSearchWorkOrderActivityResultJson(workOrderActivities)).append("}").toString();
        return result;
    }

    public Object toSearchWorkOrderActivityResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorkOrderActivity.class, searchWorkOrderActivityJsonAdaptor)
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/measurementbook/cancel/ajax-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchMBsToCancel(final Model model,
            @ModelAttribute final SearchRequestCancelMB searchRequestCancelMB) {
        final List<MBHeader> mbHeaders = mBHeaderService
                .searchMBsToCancel(searchRequestCancelMB);
        final String result = new StringBuilder("{ \"data\":").append(toSearchMBsToCancelJson(mbHeaders)).append("}")
                .toString();
        return result;
    }

    public Object toSearchMBsToCancelJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(MBHeader.class, searchMBToCancelJson)
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/measurementbook/ajaxloanumbers-mbtocancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findLOAsToCancelMB(@RequestParam final String code) {
        return mBHeaderService.findLoaNumbersToCancelMB(code);
    }

    @RequestMapping(value = "/measurementbook/ajaxcontractors-mbtocancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findContractorsToCancelMB(@RequestParam final String code) {
        return mBHeaderService.findContractorsToCancelMB(code);
    }

    @RequestMapping(value = "/measurementbook/ajaxworkidentificationnumbers-mbtocancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findWorkIdNumbersToCancelMB(@RequestParam final String code) {
        return mBHeaderService.findWorkIdentificationNumbersToCancelMB(code);
    }

    @RequestMapping(value = "/measurementbook/ajaxvalidatelatestmb-mbtocancel/{mbHeaderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String validateIsLatestMB(@PathVariable final Long mbHeaderId) {
        final MBHeader mbHeader = mBHeaderService.getMBHeaderById(mbHeaderId);
        final MBHeader latestMBHeader = mBHeaderService.getLatestMBHeader(mbHeader.getWorkOrderEstimate().getId());
        if (!mbHeader.getId().equals(latestMBHeader.getId()))
            return latestMBHeader.getMbRefNo();
        return "";
    }

    @RequestMapping(value = "/measurementbook/ajax-loadmbbasedonbilldate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String loadMBBasedOnBillDate(@RequestParam final Long workOrderEstimateId,
            @RequestParam final Date billDate) {
        final List<MBHeader> mBHeaderList = mBHeaderService.getMBHeaderBasedOnBillDate(workOrderEstimateId, billDate);
        final StringBuilder result = new StringBuilder(searchMBHeader(mBHeaderList).toString());
        if (!mBHeaderList.isEmpty())
            return result.toString();
        else
            return "";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/mbheader/ajax-showhidembappravaldetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean findWorkFlowMatrix(
            @RequestParam final BigDecimal amountRule, @RequestParam final String additionalRule) {
        final Map<String, Object> map = new HashMap<String, Object>();

        map.putAll((Map<String, Object>) scriptService.executeScript(WorksConstants.MB_APPROVALRULES,
                ScriptService.createContext("mbAmount", amountRule,
                        "cityGrade", additionalRule)));
        return (boolean) map.get("createAndApproveFieldsRequired");
    }

}
