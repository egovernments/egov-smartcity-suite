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

package org.egov.egf.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.egf.web.adaptor.BudgetApprovalAdaptor;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetApproval;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.repository.BudgetDefinitionRepository;
import org.egov.model.service.BudgetApprovalService;
import org.egov.model.service.BudgetDefinitionService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/budgetapproval")
public class BudgetApprovalController {
    private static final String BUDGETAPPROVAL_SEARCH = "budgetapproval-search";
    private static final String BUDGETAPPROVAL_RESULT = "budgetapproval-result";
    @Autowired
    private BudgetApprovalService budgetApprovalService;
    @Autowired
    private BudgetDefinitionService budgetDefinitionService;
    @Autowired
    private BudgetDefinitionRepository budgetDefinitionRepository;
    @Autowired
    private BudgetApprovalAdaptor budgetApprovalAdaptor;
    @Autowired
    private BudgetDetailService budgetDetailService;
    @Autowired
    private MessageSource messageSource;

    private void prepareNewForm(final Model model) {
        model.addAttribute("financialYearList", budgetApprovalService.financialYearList());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        final BudgetDetail budgetDetail = new BudgetDetail();
        prepareNewForm(model);
        model.addAttribute("budgetDetail", budgetDetail);
        return BUDGETAPPROVAL_SEARCH;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String search(final Model model, @ModelAttribute final BudgetDetail budgetDetail) {
        final Long finYearId = budgetDetail.getBudget().getFinancialYear().getId();
        final List<Budget> searchResultList = budgetApprovalService
                .search(finYearId);
        prepareNewForm(model);
        final List<BudgetApproval> budgetApprovalList = new ArrayList<>();
        for (final Budget ba : searchResultList) {
            final BudgetApproval budgetApproval = new BudgetApproval();
            budgetApproval.setId(ba.getId());
            budgetApproval.setDepartment(budgetDetailService.getDeptNameForBudgetId(ba.getId()));
            budgetApproval.setParent(ba.getParent().getName());
            budgetApproval.setReferenceBudget(budgetDetailService.getNextYrBEName(ba));
            budgetApproval.setBeAmount(budgetDetailService.getBEAmount(ba));
            budgetApproval.setReAmount(budgetDetailService.getREAmount(ba));
            budgetApproval.setCount(budgetDetailService.getBudgetDetailCount(ba));

            budgetApprovalList.add(budgetApproval);
        }
        return new StringBuilder("{ \"data\":").append(toSearchResultJson(budgetApprovalList))
                .append("}").toString();

    }

    @RequestMapping(value = "/approve", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String approve(@ModelAttribute final BudgetDetail budgetDetail, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs,
            @RequestParam final List<String> checkedArray) {
        final List<Long> idList = new ArrayList<>();
        for (final String checkListId : checkedArray) {
            if (!checkListId.isEmpty())
                idList.add(Long.parseLong(checkListId));
            final Budget budget = budgetDefinitionService.findOne(Long.valueOf(checkListId));

            budget.setStatus(budgetDefinitionService.getBudgetApprovedStatus());
            budgetDefinitionService.update(budget);
            final Budget bg = budgetDefinitionRepository.findByReferenceBudgetId(Long.valueOf(checkListId));
            bg.setStatus(budgetDefinitionService.getBudgetApprovedStatus());
            budgetDefinitionService.update(bg);
            final Budget parentREBudget = budgetDefinitionService.getParentBudgetForApprovedChildBudgets(budget);
            if (parentREBudget != null) {
                parentREBudget.setStatus(budgetDefinitionService.getBudgetApprovedStatus());
                budgetDefinitionService.update(parentREBudget);
                final Budget referenceParentREBudget = budgetDefinitionRepository.findByReferenceBudgetId(parentREBudget.getId());
                referenceParentREBudget.setStatus(budgetDefinitionService.getBudgetApprovedStatus());
                budgetDefinitionService.update(referenceParentREBudget);
                final Budget rootREBudget = budgetDefinitionService.getParentBudgetForApprovedChildBudgets(parentREBudget);
                if (rootREBudget != null) {
                    rootREBudget.setStatus(budgetDefinitionService.getBudgetApprovedStatus());
                    budgetDefinitionService.update(rootREBudget);
                    final Budget rootBEBudget = budgetDefinitionRepository.findByReferenceBudgetId(rootREBudget.getId());
                    rootBEBudget.setStatus(budgetDefinitionService.getBudgetApprovedStatus());
                    budgetDefinitionService.update(rootBEBudget);
                }

            }
        }

        final List<BudgetDetail> budgetDetailList = budgetDetailService.getBudgetDetails(idList);

        for (final BudgetDetail budgetDetails : budgetDetailList) {
            budgetDetails.setStatus(budgetDetailService.getBudgetDetailStatus(FinancialConstants.WORKFLOW_STATE_APPROVED));
            budgetDetailService.update(budgetDetails);
            final BudgetDetail bg = budgetDetailService.getBudgetDetailByReferencceBudget(budgetDetails.getUniqueNo(),
                    budgetDetails.getBudget().getId());
            bg.setStatus(budgetDetailService.getBudgetDetailStatus(FinancialConstants.WORKFLOW_STATE_APPROVED));
            budgetDetailService.update(bg);
        }

        return messageSource.getMessage("msg.budgetdetail.approve",
                null, Locale.ENGLISH);
    }

    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    public @ResponseBody String reject(@ModelAttribute final BudgetDetail budgetDetail, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs,
            @RequestParam final List<String> checkedArray, @RequestParam final String comments) {
        final List<Long> idList = new ArrayList<>();
        for (final String checkedIdList : checkedArray)
            if (!checkedIdList.isEmpty())
                idList.add(Long.parseLong(checkedIdList));

        final List<BudgetDetail> budgetDetailList = budgetDetailService.getBudgetDetails(idList);
        for (BudgetDetail budgetDetails : budgetDetailList) {
            budgetDetails = budgetDetailService.rejectWorkFlow(budgetDetails, comments);

            budgetDetails.setStatus(budgetDetailService.getBudgetDetailStatus(FinancialConstants.WORKFLOW_STATUS_CODE_REJECTED));
            budgetDetailService.update(budgetDetails);
            final BudgetDetail bg = budgetDetailService.getBudgetDetailByReferencceBudget(budgetDetails.getUniqueNo(),
                    budgetDetails.getBudget().getId());
            bg.setStatus(budgetDetailService.getBudgetDetailStatus(FinancialConstants.WORKFLOW_STATUS_CODE_REJECTED));

            budgetDetailService.update(bg);

        }
        return messageSource.getMessage("msg.budgetdetail.reject", null, Locale.ENGLISH);
    }

    @RequestMapping(value = "/success")
    public String success(@ModelAttribute final Budget budget, final BindingResult errors,
            final Model model, @RequestParam("message") final String message) {
        model.addAttribute("message", message);
        return BUDGETAPPROVAL_RESULT;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(BudgetApproval.class, budgetApprovalAdaptor).create();
        return gson.toJson(object);
    }

}
