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
package org.egov.works.web.controller.lineestimate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.commons.service.FinancialYearService;
import org.egov.commons.service.FunctionService;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.support.json.adapter.UserAdaptor;
import org.egov.model.budget.BudgetGroup;
import org.egov.services.masters.SchemeService;
import org.egov.utils.BudgetAccountType;
import org.egov.works.abstractestimate.entity.EstimatePhotographSearchRequest;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.LineEstimateForLoaSearchRequest;
import org.egov.works.lineestimate.entity.LineEstimateForLoaSearchResult;
import org.egov.works.lineestimate.entity.LineEstimateSearchRequest;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.master.service.NatureOfWorkService;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.adaptor.LineEstimateForEstimatePhotographJsonAdaptor;
import org.egov.works.web.adaptor.LineEstimateForLOAJsonAdaptor;
import org.egov.works.web.adaptor.LineEstimateJsonAdaptor;
import org.egov.works.web.adaptor.SearchLineEstimateToCancelJSONAdaptor;
import org.egov.works.web.adaptor.SubSchemeAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping(value = "/lineestimate")
public class AjaxLineEstimateController {
    @Autowired
    private SchemeService schemeService;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private LineEstimateJsonAdaptor lineEstimateJsonAdaptor;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SearchLineEstimateToCancelJSONAdaptor searchLineEstimateToCancelJSONAdaptor;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private FinancialYearService financialYearService;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private BudgetDetailsHibernateDAO budgetDetailsHibernateDAO;

    @Autowired
    private NatureOfWorkService natureOfWorkService;
    
    @Autowired
    private LineEstimateForEstimatePhotographJsonAdaptor lineEstimateForEstimatePhotographJsonAdaptor;

    @RequestMapping(value = "/getsubschemesbyschemeid/{schemeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getAllSubSchemesBySchemeId(final Model model, @PathVariable final String schemeId)
            throws IOException, NumberFormatException, ApplicationException {
        final Scheme scheme = schemeService.findById(Integer.parseInt(schemeId), false);
        final Set<SubScheme> subSchemes = scheme.getSubSchemes();
        final String jsonResponse = toJSON(subSchemes, SubScheme.class, SubSchemeAdaptor.class);
        return jsonResponse;
    }

    @RequestMapping(value = "/getfinancilyearbyid", method = RequestMethod.GET)
    public @ResponseBody CFinancialYear getFinancilYearById(@RequestParam("fyId") Long fyId) {

        CFinancialYear financialYear = new CFinancialYear();
        if (fyId != null) {
            financialYear = financialYearService.findById(Long.valueOf(fyId), false);
        }

        return financialYear;
    }

    @RequestMapping(value = "/ajax-getlocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Boundary> getChildBoundariesById(@RequestParam final Long id) {
        return crossHierarchyService.getActiveChildBoundariesByParentId(id);
    }

    @RequestMapping(value = "/getsubtypeofwork", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<EgwTypeOfWork> getSubTypeOfWork(@RequestParam("id") final Long id) {
        return egwTypeOfWorkHibernateDAO.getSubTypeOfWorkByParentId(id);
    }

    @RequestMapping(value = "/ajax-getward", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Boundary> findWard(@RequestParam("name") final String name) {
        final List<Boundary> boundaries = boundaryService.getBondariesByNameAndBndryTypeAndHierarchyType(
                WorksConstants.BOUNDARY_TYPE_WARD, WorksConstants.HIERARCHY_TYPE_ADMINISTRATION, "%" + name);
        final List<Boundary> cityBoundary = boundaryService.getBondariesByNameAndBndryTypeAndHierarchyType(
                WorksConstants.BOUNDARY_TYPE_CITY, WorksConstants.HIERARCHY_TYPE_ADMINISTRATION, "%" + name + "%");
        boundaries.addAll(cityBoundary);
        return boundaries;
    }

    @RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(final Model model,
            @ModelAttribute final LineEstimateSearchRequest lineEstimateSearchRequest) {
        final List<LineEstimate> searchResultList = lineEstimateService.searchLineEstimates(lineEstimateSearchRequest);
        final String result = new StringBuilder("{ \"data\":").append(toSearchLineEstimateResultJson(searchResultList))
                .append("}").toString();
        return result;
    }

    @RequestMapping(value = "/ajaxsearchlineestimatesforloa", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxSearchLineEstimatesForLOA(final Model model,
            @ModelAttribute final LineEstimateForLoaSearchRequest lineEstimateForLoaSearchRequest) {
        final List<LineEstimateForLoaSearchResult> searchResultList = lineEstimateService
                .searchLineEstimatesForLOA(lineEstimateForLoaSearchRequest);
        final String result = new StringBuilder("{ \"data\":").append(toSearchLineEstimateForLOAResultJson(searchResultList))
                .append("}").toString();
        return result;
    }

    public Object toSearchLineEstimateResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(LineEstimate.class, lineEstimateJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    public Object toSearchLineEstimateForLOAResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(LineEstimate.class, new LineEstimateForLOAJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/lineEstimateNumbers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findLineEstimateNumbers(@RequestParam final String name) {
        return lineEstimateService.findLineEstimateNumbers(name);
    }

    @RequestMapping(value = "/lineEstimateNumbersForLoa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findEstimateNumbersForLoa(@RequestParam final String name) {
        return lineEstimateService.findEstimateNumbersForLoa(name);
    }

    @RequestMapping(value = "/adminSanctionNumbers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findAdminSanctionNumbers(@RequestParam final String name) {
        return lineEstimateService.findAdminSanctionNumbers(name);
    }

    @RequestMapping(value = "/adminSanctionNumbersForLoa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findAdminSanctionNumbersForLoa(@RequestParam final String name) {
        return lineEstimateService.findAdminSanctionNumbersForLoa(name);
    }

    @RequestMapping(value = "/workIdNumbersForLoa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findworkIdNumbersForLoa(@RequestParam final String name) {
        return lineEstimateService.findWorkIdentificationNumbersToSearchLineEstimatesForLoa(name);
    }

    @RequestMapping(value = "/ajax-assignmentByDepartmentAndDesignation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getAssignmentByDepartmentAndDesignation(
            @RequestParam("approvalDesignation") final Long approvalDesignation,
            @RequestParam("approvalDepartment") final Long approvalDepartment)
            throws JsonGenerationException, JsonMappingException, IOException, NumberFormatException, ApplicationException {
        final List<User> users = new ArrayList<User>();
        List<Assignment> assignments = new ArrayList<Assignment>();
        if (approvalDepartment != null && approvalDepartment != 0 && approvalDepartment != -1
                && approvalDesignation != null && approvalDesignation != 0 && approvalDesignation != -1)
            assignments = assignmentService.findAllAssignmentsByDeptDesigAndDates(approvalDepartment,
                    approvalDesignation, new Date());

        for (final Assignment assignment : assignments)
            users.add(userService.getUserById(assignment.getEmployee().getId()));
        final Gson jsonCreator = new GsonBuilder().registerTypeAdapter(User.class, new UserAdaptor()).create();
        return jsonCreator.toJson(users, new TypeToken<Collection<User>>() {
        }.getType());
    }

    @RequestMapping(value = "/ajaxsearchcreatedby", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getcreateByDepartment(
            @RequestParam("department") final Long department)
            throws JsonGenerationException, JsonMappingException, IOException, NumberFormatException, ApplicationException {
        final List<User> users = lineEstimateService.getCreatedByUsersForCancelLineEstimateByDepartment(department);
        final Gson jsonCreator = new GsonBuilder().registerTypeAdapter(User.class, new UserAdaptor()).create();
        return jsonCreator.toJson(users, new TypeToken<Collection<User>>() {
        }.getType());
    }

    @RequestMapping(value = "/cancel/ajax-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchLineEstimatesToCancel(final Model model,
            @ModelAttribute final LineEstimateSearchRequest lineEstimateSearchRequest) {
        final List<LineEstimate> lineestimates = lineEstimateService
                .searchLineEstimatesToCancel(lineEstimateSearchRequest);
        final String result = new StringBuilder("{ \"data\":")
                .append(toSearchLineEstimatesToCancelJson(lineestimates))
                .append("}").toString();
        return result;
    }

    public Object toSearchLineEstimatesToCancelJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(LineEstimate.class, searchLineEstimateToCancelJSONAdaptor)
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajax-checkifloascreated", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String checkIfLOAsCreated(@RequestParam final Long lineEstimateId) {
        final String estimateNumbers = lineEstimateService.checkIfLOAsCreated(lineEstimateId);
        String message = messageSource.getMessage("error.lineestimate.loa.created", new String[] { estimateNumbers }, null);
        if (estimateNumbers.equals(""))
            return "";
        return message;
    }

    @RequestMapping(value = "/getbudgethead", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<BudgetGroup> getBudgetHeadByFunction(@RequestParam("fundId") final Integer fundId,
            @RequestParam("departmentId") final Long departmentId, @RequestParam("functionId") final Long functionId,
            @RequestParam("natureOfWorkId") final Long natureOfWorkId) {
        List<BudgetGroup> budgetGroups = new ArrayList<BudgetGroup>();
        try {
            NatureOfWork natureOfWork = null;
            if (natureOfWorkId != null)
                natureOfWork = natureOfWorkService.findById(natureOfWorkId);
            String accountType = null;
            if (natureOfWork != null
                    && natureOfWork.getExpenditureType().getValue()
                            .equalsIgnoreCase(WorksConstants.NATUREOFWORK_EXPENDITURETYPE_CAPITAL))
                accountType = BudgetAccountType.CAPITAL_EXPENDITURE.toString();
            else if (natureOfWork != null
                    && natureOfWork.getExpenditureType().getValue()
                            .equalsIgnoreCase(WorksConstants.NATUREOFWORK_EXPENDITURETYPE_REVENUE))
                accountType = BudgetAccountType.REVENUE_EXPENDITURE.toString();
            budgetGroups = budgetGroupDAO.getBudgetGroupsByFundFunctionDeptAndAccountType(fundId, departmentId, functionId,
                    accountType);
            return budgetGroups;
        } catch (final ValidationException v) {
            return budgetGroups;
        }
    }

    @RequestMapping(value = "/getbudgetheadbyfunction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<BudgetGroup> getBudgetHeadByFunction(@RequestParam("functionId") final Long functionId) {
        List<BudgetGroup> budgetGroups = new ArrayList<BudgetGroup>();
        final CFunction function = functionService.findOne(functionId);
        try {
            budgetGroups = budgetGroupDAO.getBudgetHeadByFunction(function.getCode());
            return budgetGroups;
        } catch (final ValidationException v) {
            return budgetGroups;
        }
    }

    @RequestMapping(value = "/getfunctionsbyfundidanddepartmentid", method = RequestMethod.GET)
    public @ResponseBody List<CFunction> getAllFunctionsByFundIdAndDepartmentId(final Model model,
            @RequestParam("fundId") final Integer fundId, @RequestParam("departmentId") final Long departmentId)
            throws JsonGenerationException, JsonMappingException, IOException, NumberFormatException, ApplicationException {
        final List<CFunction> functions = budgetDetailsHibernateDAO.getFunctionsByFundAndDepartment(fundId, departmentId);
        return functions;
    }
    
    @RequestMapping(value = "/getestimatenumbers-uploadphotographs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findEstimateNumbersForEstimatePhotograph(@RequestParam final String estimateNumber) {
        return lineEstimateService.getEstimateNumbersForEstimatePhotograph(estimateNumber);
    }
    
    @RequestMapping(value = "/getwin-uploadphotographs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findWinForEstimatePhotograph(@RequestParam final String workIdentificationNumber) {
        return lineEstimateService.getWinForEstimatePhotograph(workIdentificationNumber);
    }
    
    @RequestMapping(value = "/searchlineestimateforestimatephotograph", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxSearchLEForEstimatePhotograph(final Model model,
            @ModelAttribute final EstimatePhotographSearchRequest estimatePhotographSearchRequest) {
        final List<LineEstimateDetails> searchResultList = lineEstimateService.searchLineEstimatesForEstimatePhotograph(estimatePhotographSearchRequest);
        final String result = new StringBuilder("{ \"data\":").append(toSearchLineEstimateForEstimatePhotograph(searchResultList))
                .append("}").toString();
        return result;
    }
    
    public Object toSearchLineEstimateForEstimatePhotograph(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(LineEstimateDetails.class, lineEstimateForEstimatePhotographJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }
    
    
}
