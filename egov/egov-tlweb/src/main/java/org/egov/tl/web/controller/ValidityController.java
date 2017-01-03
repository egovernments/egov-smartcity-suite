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
package org.egov.tl.web.controller;

import org.egov.tl.entity.Validity;
import org.egov.tl.service.NatureOfBusinessService;
import org.egov.tl.service.ValidityService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.web.response.adaptor.ValidityResponseAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/validity")
public class ValidityController {
    private static final String VALIDITY_NEW = "validity-new";
    private static final String VALIDITY_RESULT = "validity-result";
    private static final String VALIDITY_EDIT = "validity-edit";
    private static final String VALIDITY_VIEW = "validity-view";
    private static final String VALIDITY_SEARCH = "validity-search";
    private static final String VALIDITY_MODEL_ATTRIB_NAME = "validity";

    @Autowired
    private ValidityService validityService;

    @Autowired
    private NatureOfBusinessService natureOfBusinessService;

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    private void prepareNewForm(Model model) {
        model.addAttribute("natureOfBusinesss", natureOfBusinessService.findAll());
        model.addAttribute("licenseCategorys", licenseCategoryService.getCategories());
    }

    @RequestMapping(value = "/new", method = GET)
    public String newForm(@ModelAttribute Validity validity, Model model) {
        prepareNewForm(model);
        return VALIDITY_NEW;
    }

    @RequestMapping(value = "/create", method = POST)
    public String create(@Valid @ModelAttribute Validity validity, BindingResult errors,
                         RedirectAttributes redirectAttrs, Model model) {
        if (!validity.hasValidValues())
            errors.rejectValue("basedOnFinancialYear", "validity.value.notset");
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return VALIDITY_NEW;
        }
        validityService.create(validity);
        redirectAttrs.addFlashAttribute("message", "msg.validity.success");
        return "redirect:/validity/result/" + validity.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = GET)
    public String edit(@PathVariable("id") Long id, Model model) {
        Validity validity = validityService.findOne(id);
        prepareNewForm(model);
        model.addAttribute(VALIDITY_MODEL_ATTRIB_NAME, validity);
        return VALIDITY_EDIT;
    }

    @RequestMapping(value = "/update", method = POST)
    public String update(@Valid @ModelAttribute Validity validity, BindingResult errors,
                         RedirectAttributes redirectAttrs, Model model) {
        if (!validity.hasValidValues())
            errors.rejectValue("basedOnFinancialYear", "validity.value.notset");
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return VALIDITY_EDIT;
        }
        validityService.update(validity);
        redirectAttrs.addFlashAttribute("message", "msg.validity.success");
        return "redirect:/validity/result/" + validity.getId();
    }

    @RequestMapping(value = "/view/{id}", method = GET)
    public String view(@PathVariable("id") Long id, Model model) {
        Validity validity = validityService.findOne(id);
        prepareNewForm(model);
        model.addAttribute(VALIDITY_MODEL_ATTRIB_NAME, validity);
        return VALIDITY_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = GET)
    public String result(@PathVariable("id") Long id, Model model) {
        Validity validity = validityService.findOne(id);
        model.addAttribute(VALIDITY_MODEL_ATTRIB_NAME, validity);
        return VALIDITY_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = GET)
    public String search(@PathVariable("mode") String mode, Model model) {
        Validity validity = new Validity();
        prepareNewForm(model);
        model.addAttribute(VALIDITY_MODEL_ATTRIB_NAME, validity);
        return VALIDITY_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = POST, produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") String mode,
                             @RequestParam(required = false) Long natureOfBusiness,
                             @RequestParam(required = false) Long licenseCategory) {
        return new StringBuilder("{ \"data\":").
                append(toJSON(validityService.search(natureOfBusiness, licenseCategory), Validity.class,
                        ValidityResponseAdaptor.class)).append("}").toString();
    }
}