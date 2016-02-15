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

import org.egov.tl.entity.PenaltyRates;
import org.egov.tl.service.PenaltyRatesService;
import org.egov.tl.web.adaptor.PenaltyRatesAdaptor;
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
@RequestMapping("/penaltyRates")
public class PenaltyRatesController {
    @Autowired
    private PenaltyRatesService penaltyRatesService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String penaltyRatesForm(@ModelAttribute final PenaltyRates penaltyRates, final Model model) {
        model.addAttribute("licenseAppTypes", penaltyRatesService.findAllLicenseAppType());
        return "penaltyRates-create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String penaltyRatesCreate(@Valid @ModelAttribute final PenaltyRates penaltyRates, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes, final Model model) {
        if (penaltyRatesService.validatePenaltyWithRange(penaltyRates)) {
            model.addAttribute("message", "penaltyrate.already.exist");
            model.addAttribute("licenseAppTypes", penaltyRatesService.findAllLicenseAppType());
            return "penaltyRates-create";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("licenseAppTypes", penaltyRatesService.findAllLicenseAppType());
            return "penaltyRates-create";
        }
        penaltyRatesService.create(penaltyRates);
        redirectAttributes.addFlashAttribute("message", "msg.penaltyRate.created");
        return "redirect:/penaltyRates/success/" + penaltyRates.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") final Long id, final Model model) {
        final PenaltyRates penaltyRates = penaltyRatesService.findOne(id);
        model.addAttribute("licenseAppTypes", penaltyRatesService.findAllLicenseAppType());
        model.addAttribute("penaltyRates", penaltyRates);
        return "penaltyRates-edit";
    }

    @RequestMapping(value = "/success/{id}", method = RequestMethod.GET)
    public String success(@PathVariable("id") final Long id, final Model model) {
        final PenaltyRates penaltyRates = penaltyRatesService.findOne(id);
        model.addAttribute("penaltyRates", penaltyRates);
        return "penaltyRates-success";
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final PenaltyRates penaltyRates = new PenaltyRates();
        model.addAttribute("licenseAppTypes", penaltyRatesService.findAllLicenseAppType());
        model.addAttribute("penaltyRates", penaltyRates);
        return "penaltyRates-search";

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode,
            final Long licenseAppType) {
        final List<PenaltyRates> searchResultList = penaltyRatesService.search(licenseAppType);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final PenaltyRates penaltyRates, final BindingResult bindingResult,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (penaltyRatesService.validatePenaltyWithRate(penaltyRates)) {
            model.addAttribute("message", "penaltyrate.already.exist");
            model.addAttribute("licenseAppTypes", penaltyRatesService.findAllLicenseAppType());
            return "penaltyRates-edit";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("licenseAppTypes", penaltyRatesService.findAllLicenseAppType());
            return "penaltyRates-edit";
        }
        penaltyRatesService.update(penaltyRates);
        redirectAttrs.addFlashAttribute("message", "msg.penaltyrate.success");
        return "redirect:/validity/result/" + penaltyRates.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final PenaltyRates penaltyRates = penaltyRatesService.findOne(id);
        model.addAttribute("licenseAppTypes", penaltyRatesService.findAllLicenseAppType());
        model.addAttribute("penaltyRates", penaltyRates);
        return "penaltyRates-view";
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(PenaltyRates.class, new PenaltyRatesAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}
