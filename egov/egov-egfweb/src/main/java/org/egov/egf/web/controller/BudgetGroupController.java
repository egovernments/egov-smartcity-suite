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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.egf.web.adaptor.BudgetGroupJsonAdaptor;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.service.BudgetingGroupService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.utils.BudgetAccountType;
import org.egov.utils.BudgetingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/budgetgroup")
public class BudgetGroupController {
    private final static String BUDGETGROUP_NEW = "budgetgroup-new";
    private final static String BUDGETGROUP_RESULT = "budgetgroup-result";
    private final static String BUDGETGROUP_EDIT = "budgetgroup-edit";
    private final static String BUDGETGROUP_VIEW = "budgetgroup-view";
    private final static String BUDGETGROUP_SEARCH = "budgetgroup-search";
    @Autowired
    private BudgetingGroupService budgetGroupService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private BudgetDetailService budgetDetailService;

    private void prepareNewForm(final Model model) {
        model.addAttribute("majorCodeList", budgetGroupService.getMajorCodeList());
        model.addAttribute("minCodeList", budgetGroupService.getMinCodeList());
        model.addAttribute("budgetAccountTypes", Arrays.asList(BudgetAccountType.values()));
        model.addAttribute("budgetingTypes", Arrays.asList(BudgetingType.values()));

    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("budgetGroup", new BudgetGroup());
        return BUDGETGROUP_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final BudgetGroup budgetGroup, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {

        if (budgetGroup.getMajorCode() == null && budgetGroup.getMinCode() == null && budgetGroup.getMaxCode() == null)
            errors.rejectValue("majorCode", "budgetgroup.select.accountcode");
        if (budgetGroup.getMajorCode() != null
                && (budgetGroup.getMinCode() != null || budgetGroup.getMaxCode() != null))
            errors.rejectValue("majorCode", "budgetgroup.invalid.mapping");
        if (budgetGroup.getMajorCode() == null
                && (budgetGroup.getMinCode() != null || budgetGroup.getMaxCode() != null)) {
            if (budgetGroup.getMinCode() == null)
                errors.rejectValue("minCode", "budgetgroup.invalidminorcode.mapping");
            else if (budgetGroup.getMaxCode() == null)
                errors.rejectValue("maxCode", "budgetgroup.invalidmajorcode.mapping");
        }
        String validationMessage = budgetGroupService.validate(budgetGroup, errors);
        if (errors.hasErrors() || !StringUtils.isEmpty(validationMessage)) {
            prepareNewForm(model);
            model.addAttribute("majorCode", validationMessage);
            return BUDGETGROUP_NEW;
        }
        budgetGroupService.create(budgetGroup);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.budgetGroup.success", null, Locale.ENGLISH));
        return "redirect:/budgetgroup/result/" + budgetGroup.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final BudgetGroup budgetGroup = budgetGroupService.findOne(id);
        List<BudgetDetail> bd = budgetDetailService.getBudgetDetailsByBudgetGroupId(budgetGroup.getId());
        if (!bd.isEmpty()) {
            model.addAttribute("mode", "edit");
        }
        prepareNewForm(model);
        model.addAttribute("budgetGroup", budgetGroup);
        return BUDGETGROUP_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final BudgetGroup budgetGroup, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        String validationMessage = budgetGroupService.validate(budgetGroup, errors);
        if (errors.hasErrors() || !StringUtils.isEmpty(validationMessage)) {
            model.addAttribute("majorCode", validationMessage);
            prepareNewForm(model);
            return BUDGETGROUP_EDIT;
        }
        budgetGroupService.update(budgetGroup);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.budgetGroup.update", null, Locale.ENGLISH));
        return "redirect:/budgetgroup/result/" + budgetGroup.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final BudgetGroup budgetGroup = budgetGroupService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("budgetGroup", budgetGroup);
        return BUDGETGROUP_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model) {
        final BudgetGroup budgetGroup = budgetGroupService.findOne(id);
        model.addAttribute("budgetGroup", budgetGroup);
        return BUDGETGROUP_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final BudgetGroup budgetGroup = new BudgetGroup();
        prepareNewForm(model);
        model.addAttribute("budgetGroup", budgetGroup);
        return BUDGETGROUP_SEARCH;
    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final BudgetGroup budgetGroup) {
        final List<BudgetGroup> searchResultList = budgetGroupService.search(budgetGroup);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(BudgetGroup.class, new BudgetGroupJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}