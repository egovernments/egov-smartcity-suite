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

package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.egf.web.adaptor.BudgetApprovalAdaptor;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetApproval;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.service.BudgetApprovalService;
import org.egov.model.service.BudgetDefinitionService;
import org.egov.services.budget.BudgetDetailService;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/budgetapproval")
public class BudgetApprovalController {
    private final static String BUDGETAPPROVAL_SEARCH = "budgetapproval-search";
    private final static String BUDGETAPPROVAL_RESULT = "budgetapproval-result";
    @Autowired
    private BudgetApprovalService budgetApprovalService;
    @Autowired
    private BudgetDefinitionService budgetDefinitionService;
    @Autowired
    private BudgetApprovalAdaptor budgetApprovalAdaptor;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernate;
    @Autowired
    private BudgetDetailService budgetDetailService;
    @Autowired
    private MessageSource messageSource;

    private void prepareNewForm(Model model) {
        model.addAttribute("financialYearList", budgetApprovalService.financialYearList());
    };

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        BudgetDetail budgetDetail = new BudgetDetail();
        prepareNewForm(model);
        model.addAttribute("budgetDetail", budgetDetail);
        return BUDGETAPPROVAL_SEARCH;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String search(Model model, @ModelAttribute final BudgetDetail budgetDetail) {
        List<BudgetDetail> searchResultList = budgetApprovalService
                .search(budgetDetail.getBudget().getFinancialYear().getId());
        prepareNewForm(model);
        final List<BudgetApproval> budgetApprovalList = new ArrayList<BudgetApproval>();
        for (BudgetDetail ba : searchResultList) {
            final BudgetApproval budgetApproval = new BudgetApproval();
            budgetApproval.setId(ba.getId());
            budgetApproval.setDepartment(ba.getExecutingDepartment().getName());
            budgetApproval.setParent(ba.getBudget().getParent().getName());
            budgetApproval.setReferenceBudget(ba.getBudget().getReferenceBudget().getName());
            budgetApproval.setBeAmount(ba.getOriginalAmount());
            budgetApproval.setReAmount(ba.getApprovedAmount());

            budgetApprovalList.add(budgetApproval);
        }

        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(budgetApprovalList))
                .append("}").toString();
        return result;
    }

    @RequestMapping(value = "/approve", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String approve(@ModelAttribute final BudgetDetail budgetDetail, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs,
            @RequestParam(value = "checkedArray") List<String> checkedArray,
            @RequestParam("comments") String comments) {
        final List<String> sp = checkedArray;

        List<Long> idList = new ArrayList<Long>();

        for (String checkListId : sp) {
            if (!checkListId.isEmpty())
                idList.add(Long.parseLong(checkListId));
        }

        List<BudgetDetail> budgetDetailList = budgetDetailService.getBudgets(idList);
        Budget budget = new Budget();
        for (BudgetDetail budgetDetails : budgetDetailList) {
            budgetDetails.setStatus(egwStatusHibernate.getStatusByModuleAndCode("BUDGETDETAIL", "Approved"));
            budget = budgetDefinitionService.findOne(budgetDetails.getBudget().getId());
            budget.getParent().setStatus(egwStatusHibernate.getStatusByModuleAndCode("BUDGET", "Approved"));
            budget.getReferenceBudget().setStatus(egwStatusHibernate.getStatusByModuleAndCode("BUDGET", "Approved"));
            budgetDefinitionService.update(budget);
            budgetDetailService.update(budgetDetails);
        }
        return "success";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String success(@ModelAttribute final Budget budget, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        model.addAttribute("message",
                messageSource.getMessage("msg.budgetdetail.approve", null, Locale.ENGLISH));
        return BUDGETAPPROVAL_RESULT;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(BudgetApproval.class, budgetApprovalAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

}
