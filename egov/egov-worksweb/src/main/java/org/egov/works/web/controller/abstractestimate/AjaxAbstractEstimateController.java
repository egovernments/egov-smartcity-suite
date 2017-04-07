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
package org.egov.works.web.controller.abstractestimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.web.support.json.adapter.UserAdaptor;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateForCopyEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateForLoaSearchRequest;
import org.egov.works.abstractestimate.entity.AbstractEstimateForLoaSearchResult;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.EstimateTemplateSearchRequest;
import org.egov.works.abstractestimate.entity.SearchAbstractEstimate;
import org.egov.works.abstractestimate.entity.SearchRequestCancelEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.masters.entity.EstimateTemplate;
import org.egov.works.masters.entity.EstimateTemplateActivity;
import org.egov.works.masters.entity.Overhead;
import org.egov.works.masters.entity.OverheadRate;
import org.egov.works.masters.entity.ScheduleOfRate;
import org.egov.works.masters.service.EstimateTemplateService;
import org.egov.works.masters.service.OverheadService;
import org.egov.works.masters.service.ScheduleOfRateService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.adaptor.AbstractEstimateForLOAJsonAdaptor;
import org.egov.works.web.adaptor.AbstractEstimateForOfflineStatusJsonAdaptor;
import org.egov.works.web.adaptor.AbstractEstimateJsonAdaptor;
import org.egov.works.web.adaptor.CopyEstimateJsonAdaptor;
import org.egov.works.web.adaptor.EstimateActivityJsonAdaptor;
import org.egov.works.web.adaptor.EstimateTemplateActivityJsonAdaptor;
import org.egov.works.web.adaptor.EstimateTemplateJsonAdaptor;
import org.egov.works.web.adaptor.SearchEstimatesToCancelJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping(value = "/abstractestimate")
public class AjaxAbstractEstimateController {

    @Autowired
    private OverheadService overheadService;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @Autowired
    private EstimateTemplateService estimateTemplateService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SearchEstimatesToCancelJson searchEstimatesToCancelJson;

    @Autowired
    private AbstractEstimateForOfflineStatusJsonAdaptor abstractEstimateForOfflineStatusJsonAdaptor;

    @Autowired
    private EstimateTemplateActivityJsonAdaptor estimateTemplateActivityJsonAdaptor;

    @Autowired
    private EstimateActivityJsonAdaptor estimateActivityJsonAdaptor;

    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private CopyEstimateJsonAdaptor copyEstimateJsonAdaptor;

    @Autowired
    private EstimateTemplateJsonAdaptor estimateTemplateJsonAdaptor;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private AbstractEstimateJsonAdaptor abstractEstimateJsonAdaptor;

    public Object toSearchAbstractEstimateForLOAResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder
                .registerTypeAdapter(AbstractEstimate.class, new AbstractEstimateForLOAJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/getpercentageorlumpsumbyoverheadid", method = RequestMethod.GET)
    public @ResponseBody OverheadRate getPercentageOrLumpsumByOverhead(
            @RequestParam("overheadId") final Long overheadId) {

        Overhead overhead = new Overhead();
        OverheadRate overheadRate = new OverheadRate();
        Date startDate, endDate;
        final Date estDate = new Date();
        if (overheadId != null)
            overhead = overheadService.getOverheadById(overheadId);

        if (overhead != null && overhead.getOverheadRates() != null && overhead.getOverheadRates().size() > 0)
            for (final OverheadRate obj : overhead.getOverheadRates()) {
                startDate = obj.getValidity().getStartDate();
                endDate = obj.getValidity().getEndDate();
                if (estDate.compareTo(startDate) >= 0 && (endDate == null || endDate.compareTo(estDate) >= 0))
                    overheadRate = obj;
            }

        return overheadRate;
    }

    @RequestMapping(value = "/ajaxsor-byschedulecategories", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public @ResponseBody List<ScheduleOfRate> findSorByScheduleCategories(@RequestParam("code") final String code,
            @RequestParam("scheduleCategories") final String scheduleCategories,
            @RequestParam("estimateDate") final Date estimateDate) {
        if (!scheduleCategories.equals("null"))
            return scheduleOfRateService.getScheduleOfRatesByCodeAndScheduleOfCategories(code, scheduleCategories,
                    estimateDate);
        return null;
    }

    @RequestMapping(value = "/ajaxsor-byschedulecategoriesandestimateid", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public @ResponseBody List<ScheduleOfRate> findSorByScheduleCategoriesAndEstimateId(
            @RequestParam("code") final String code,
            @RequestParam("scheduleCategories") final String scheduleCategories,
            @RequestParam("estimateDate") final Date estimateDate, @RequestParam("estimateId") final Long estimateId) {
        if (!scheduleCategories.equals("null"))
            return scheduleOfRateService.getScheduleOfRatesByCodeAndScheduleOfCategoriesAndEstimateId(code,
                    scheduleCategories, estimateDate, estimateId);
        return null;
    }

    @RequestMapping(value = "/ajaxestimatetemplatebycode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<EstimateTemplate> getEstimateTemplateByCodeIgnoreCase(@RequestParam final String code) {
        return estimateTemplateService.getEstimateTemplateByCodeIgnoreCase(code);
    }

    @RequestMapping(value = "/ajaxgetestimatetemplatebyid", method = RequestMethod.GET)
    public @ResponseBody String populateMilestoneTemplateActivity(@RequestParam final String id, final Model model,
            @RequestParam final Date estimateDate) throws ApplicationException {
        final List<EstimateTemplateActivity> activities = estimateTemplateService
                .getEstimateTemplateById(Long.valueOf(id)).getEstimateTemplateActivities();
        for (final EstimateTemplateActivity estimateTemplateActivitys : activities)
            estimateTemplateActivitys.setEstimateDate(estimateDate);
        final String result = estimateTemplateToJson(activities);
        return result;
    }

    public String estimateTemplateToJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder
                .registerTypeAdapter(EstimateTemplateActivity.class, estimateTemplateActivityJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/getabstractestimatesbynumber", method = RequestMethod.GET)
    public @ResponseBody List<String> findAbstractEstimateNumbersForAbstractEstimate(
            @RequestParam final String estimateNumber) {
        return estimateService.getAbstractEstimateByEstimateNumberLike(estimateNumber);
    }

    @RequestMapping(value = "/ajaxsearchabstractestimatesforloa", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxSearchAbstractEstimatesForLOA(final Model model,
            @ModelAttribute final AbstractEstimateForLoaSearchRequest abstractEstimateForLoaSearchRequest) {
        final List<AbstractEstimateForLoaSearchResult> searchResultList = estimateService
                .searchAbstractEstimatesForLOA(abstractEstimateForLoaSearchRequest);
        final String result = new StringBuilder("{ \"data\":")
                .append(toSearchAbstractEstimateForLOAResultJson(searchResultList)).append("}").toString();
        return result;
    }

    @RequestMapping(value = "/ajax-assignmentByDesignation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getAssignmentByDesignation(
            @RequestParam("approvalDesignation") final Long approvalDesignation) throws JsonGenerationException,
            JsonMappingException, IOException, NumberFormatException, ApplicationException {
        final List<User> users = new ArrayList<User>();
        List<Assignment> assignments = new ArrayList<Assignment>();
        if (approvalDesignation != null && approvalDesignation != 0 && approvalDesignation != -1)
            assignments = assignmentService.getAllActiveAssignments(approvalDesignation);

        for (final Assignment assignment : assignments)
            users.add(userService.getUserById(assignment.getEmployee().getId()));

        final Gson jsonCreator = new GsonBuilder().registerTypeAdapter(User.class, new UserAdaptor()).create();
        return jsonCreator.toJson(users, new TypeToken<Collection<User>>() {
        }.getType());
    }

    @RequestMapping(value = "/cancel/ajax-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchEstimatesToCancel(final Model model,
            @ModelAttribute final SearchRequestCancelEstimate searchRequestCancelEstimate) {
        final List<AbstractEstimate> abstractEstimates = estimateService
                .searchEstimatesToCancel(searchRequestCancelEstimate);
        final String result = new StringBuilder("{ \"data\":").append(toSearchEstimatesToCancelJson(abstractEstimates))
                .append("}").toString();
        return result;
    }

    public Object toSearchEstimatesToCancelJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(AbstractEstimate.class, searchEstimatesToCancelJson).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxsearchabstractestimatesforofflinestatus", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxSearchAbstractEstimatesForOfflineStatus(final Model model,
            @ModelAttribute final AbstractEstimateForLoaSearchRequest abstractEstimateForLoaSearchRequest) {
        final List<AbstractEstimate> searchResultList = estimateService
                .searchAbstractEstimatesForOfflineStatus(abstractEstimateForLoaSearchRequest);
        final String result = new StringBuilder("{ \"data\":")
                .append(toSearchAbstractEstimateForOfflineStatusJson(searchResultList)).append("}").toString();
        return result;
    }

    public Object toSearchAbstractEstimateForOfflineStatusJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder
                .registerTypeAdapter(AbstractEstimate.class, abstractEstimateForOfflineStatusJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxestimatenumbers-estimatetocancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findEstimateNumbersToCancelEstimate(@RequestParam final String code) {
        return estimateService.findEstimateNumbersToCancelEstimate(code);
    }

    @RequestMapping(value = "/ajaxestimatenumbers-forofflinestatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getAbstractEstimateNumbersToSetOfflineStatus(@RequestParam final String code) {
        return estimateService.getAbstractEstimateNumbersToSetOfflineStatus(code);
    }

    @RequestMapping(value = "/ajaxestimatenumbers-tocreateloa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findApprovedEstimateNumbersForCreateLOA(
            @RequestParam final String estimateNumber) {
        return estimateService.getApprovedEstimateNumbersForCreateLOA(estimateNumber);
    }

    @RequestMapping(value = "/ajaxadminsanctionnumbers-tocreateloa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findApprovedAdminSanctionNumbersForCreateLOA(
            @RequestParam final String adminSanctionNumber) {
        return estimateService.getApprovedAdminSanctionNumbersForCreateLOA(adminSanctionNumber);
    }

    @RequestMapping(value = "/ajaxworkidentificationnumbers-tocreateloa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findApprovedWorkIdentificationNumbersForCreateLOA(
            @RequestParam final String workIdentificationNumber) {
        return estimateService.getApprovedWorkIdentificationNumbersForCreateLOA(workIdentificationNumber);
    }

    @RequestMapping(value = "/ajaxdeduction-coa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<CChartOfAccounts> findDeductionAccountCodesAndAccountHeadByGlcodeLike(
            @RequestParam final String searchQuery) {
        final String[] purposeNames = new String[3];
        purposeNames[0] = WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE;
        return chartOfAccountsService.findOtherDeductionAccountCodesByGlcodeOrNameLike(searchQuery, purposeNames);
    }

    @RequestMapping(value = "/ajaxestimatenumbers-estimatetocopy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<Long, String>> findEstimateNumbersToCopyEstimate(@RequestParam final String code) {
        return estimateService.findEstimateNumbersToCopyEstimate(code);
    }

    @RequestMapping(value = "/ajaxactivities-estimatetocopy", method = RequestMethod.GET)
    public @ResponseBody String populateEstimateActivity(@RequestParam final String id, final Model model,
            @RequestParam final Date estimateDate) throws ApplicationException {
        final List<Activity> activities = estimateService.getActivitiesByEstimate(Long.valueOf(id));
        for (final Activity activity : activities)
            activity.setEstimateDate(estimateDate);
        final String result = estimateActivitiesToJson(activities);
        return result;
    }

    public String estimateActivitiesToJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Activity.class, estimateActivityJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxestimates-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchActivities(
            @ModelAttribute final AbstractEstimateForCopyEstimate abstractEstimateForCopyEstimate,
            final HttpServletRequest request) {
        final List<AbstractEstimate> abstractEstimates = estimateService
                .searchEstimatesToCopy(abstractEstimateForCopyEstimate);
        final String result = new StringBuilder("{ \"data\":").append(toSearchEstimateResultJson(abstractEstimates))
                .append("}").toString();
        return result;
    }

    public Object toSearchEstimateResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(AbstractEstimate.class, copyEstimateJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxestimatetemplates-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchEstimateTemplates(
            @ModelAttribute final EstimateTemplateSearchRequest estimateTemplateSearchRequest,
            final HttpServletRequest request) {
        final List<EstimateTemplate> estimateTemplates = estimateTemplateService
                .searchEstimateTemplates(estimateTemplateSearchRequest);
        return new StringBuilder("{ \"data\":").append(toSearchEstimateTemplatesResultJson(estimateTemplates))
                .append("}").toString();
    }

    public Object toSearchEstimateTemplatesResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(EstimateTemplate.class, estimateTemplateJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/ajax-showhideappravaldetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean findWorkFlowMatrix(@RequestParam final BigDecimal amountRule,
            @RequestParam final String additionalRule) {
        final Map<String, Object> map = new HashMap<String, Object>();

        map.putAll((Map<String, Object>) scriptService.executeScript(WorksConstants.ABSTRACTESTIMATE_APPROVALRULES,
                ScriptService.createContext("estimateValue", amountRule, "cityGrade", additionalRule)));
        return (boolean) map.get("createAndApproveFieldsRequired");
    }

    @RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchAbstractEstimates(
            @ModelAttribute final SearchAbstractEstimate searchAbstractEstimate, final Model model) {
        final List<AbstractEstimate> abstractEstimates = estimateService
                .searchAbstractEstimates(searchAbstractEstimate);
        final String result = new StringBuilder("{ \"data\":").append(toJson(abstractEstimates)).append("}").toString();
        return result;
    }

    public Object toJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(AbstractEstimate.class, abstractEstimateJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

}
