/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.tl.entity.Validity;
import org.egov.tl.service.NatureOfBusinessService;
import org.egov.tl.service.ValidityService;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.egov.tl.web.adaptor.ValidityJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/validity")
public class ValidityController {
    private final static String VALIDITY_NEW = "validity-new";
    private final static String VALIDITY_RESULT = "validity-result";
    private final static String VALIDITY_EDIT = "validity-edit";
    private final static String VALIDITY_VIEW = "validity-view";
    private final static String VALIDITY_SEARCH = "validity-search";
    @Autowired
    private ValidityService validityService;
    @Autowired
    private NatureOfBusinessService natureOfBusinessService;
    @Autowired
    private LicenseCategoryService licenseCategoryService;

    private void prepareNewForm(final Model model) {
        model.addAttribute("natureOfBusinesss", natureOfBusinessService.findAll());
        model.addAttribute("licenseCategorys", licenseCategoryService.findAll());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(@ModelAttribute final Validity validity, final Model model) {
        prepareNewForm(model);
       return VALIDITY_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final Validity validity, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return VALIDITY_NEW;
        }
        validityService.create(validity);
        redirectAttrs.addFlashAttribute("message", "msg.validity.success");
        return "redirect:/validity/result/" + validity.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final Validity validity = validityService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("validity", validity);
        return VALIDITY_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final Validity validity, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return VALIDITY_EDIT;
        }
        validityService.update(validity);
        redirectAttrs.addFlashAttribute("message", "msg.validity.success");
        return "redirect:/validity/result/" + validity.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final Validity validity = validityService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("validity", validity);
        return VALIDITY_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model) {
        final Validity validity = validityService.findOne(id);
        model.addAttribute("validity", validity);
        return VALIDITY_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final Validity validity = new Validity();
        prepareNewForm(model);
        model.addAttribute("validity", validity);
        return VALIDITY_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final Validity validity) {
        final List<Validity> searchResultList = validityService.search(validity);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Validity.class, new ValidityJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}