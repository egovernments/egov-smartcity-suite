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
import org.egov.council.entity.CouncilParty;
import org.egov.council.service.CouncilPartyService;
import org.egov.council.web.adaptor.CouncilPartyJsonAdaptor;
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
@RequestMapping("/councilparty")
public class CouncilPartyController {
    private static final String COUNCIL_PARTY = "councilParty";
    private static final String COUNCILPARTY_NEW = "councilparty-new";
    private static final String COUNCILPARTY_RESULT = "councilparty-result";
    private static final String COUNCILPARTY_EDIT = "councilparty-edit";
    private static final String COUNCILPARTY_VIEW = "councilparty-view";
    private static final String COUNCILPARTY_SEARCH = "councilparty-search";
    @Autowired
    private CouncilPartyService councilPartyService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        CouncilParty councilParty = new CouncilParty();
        councilParty.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());
        model.addAttribute(COUNCIL_PARTY, councilParty);

        return COUNCILPARTY_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilParty councilParty, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "notempty.cncl.partyAffiliation");
        if (councilParty != null && councilParty.getCode() == null)
            councilParty.setCode(RandomStringUtils.random(4, Boolean.TRUE, Boolean.TRUE).toUpperCase());

        if (errors.hasErrors()) {
            return COUNCILPARTY_NEW;
        }
        councilPartyService.create(councilParty);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilParty.success", null, null));
        return "redirect:/councilparty/result/" + councilParty.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, Model model) {
        CouncilParty councilParty = councilPartyService.findOne(id);
        model.addAttribute(COUNCIL_PARTY, councilParty);
        return COUNCILPARTY_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilParty councilParty, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "notempty.cncl.partyAffiliation");
        if (errors.hasErrors()) {
            return COUNCILPARTY_EDIT;
        }
        councilPartyService.update(councilParty);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilParty.success", null, null));
        return "redirect:/councilparty/result/" + councilParty.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilParty councilParty = councilPartyService.findOne(id);
        model.addAttribute(COUNCIL_PARTY, councilParty);
        return COUNCILPARTY_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilParty councilParty = councilPartyService.findOne(id);
        model.addAttribute(COUNCIL_PARTY, councilParty);
        return COUNCILPARTY_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilParty councilParty = new CouncilParty();
        model.addAttribute(COUNCIL_PARTY, councilParty);
        return COUNCILPARTY_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilParty councilParty) {
        List<CouncilParty> searchResultList = councilPartyService.search(councilParty);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, CouncilParty.class, CouncilPartyJsonAdaptor.class)).append("}")
                .toString();
    }
}