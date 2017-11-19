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

package org.egov.council.web.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.council.entity.CouncilDesignation;
import org.egov.council.service.CouncilDesignationService;
import org.egov.council.web.adaptor.CouncilDesignationJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping("/councildesignation")
public class CouncilDesignationController {
    private static final String COUNCIL_DESIGNATION = "councilDesignation";
    private static final String COUNCILDESIGNATION_NEW = "councildesignation-new";
    private static final String COUNCILDESIGNATION_RESULT = "councildesignation-result";
    private static final String COUNCILDESIGNATION_EDIT = "councildesignation-edit";
    private static final String COUNCILDESIGNATION_VIEW = "councildesignation-view";
    private static final String COUNCILDESIGNATION_SEARCH = "councildesignation-search";
    @Autowired
    private CouncilDesignationService councilDesignationService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        CouncilDesignation councilDesignation = new CouncilDesignation();
        councilDesignation.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());

        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);

        return COUNCILDESIGNATION_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilDesignation councilDesignation,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {

        if (councilDesignation != null && councilDesignation.getCode() == null)
            councilDesignation.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "notempty.cncl.designation");
        if (errors.hasErrors()) {
            return COUNCILDESIGNATION_NEW;
        }

        councilDesignationService.create(councilDesignation);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.councilDesignation.success", null, null));
        return "redirect:/councildesignation/result/" + councilDesignation.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        CouncilDesignation councilDesignation = councilDesignationService.findOne(id);
        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);
        return COUNCILDESIGNATION_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilDesignation councilDesignation,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "notempty.cncl.designation");
        if (errors.hasErrors()) {
            return COUNCILDESIGNATION_EDIT;
        }
        councilDesignationService.update(councilDesignation);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.councilDesignation.success", null, null));
        return "redirect:/councildesignation/result/" + councilDesignation.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilDesignation councilDesignation = councilDesignationService.findOne(id);
        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);
        return COUNCILDESIGNATION_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilDesignation councilDesignation = councilDesignationService.findOne(id);
        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);
        return COUNCILDESIGNATION_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilDesignation councilDesignation = new CouncilDesignation();
        model.addAttribute(COUNCIL_DESIGNATION, councilDesignation);
        return COUNCILDESIGNATION_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilDesignation councilDesignation) {
        List<CouncilDesignation> searchResultList = councilDesignationService.search(councilDesignation);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, CouncilDesignation.class, CouncilDesignationJsonAdaptor.class)).append("}")
                .toString();
    }

}