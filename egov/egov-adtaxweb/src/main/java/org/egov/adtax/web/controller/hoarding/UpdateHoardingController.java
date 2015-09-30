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
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.HoardingService;
import org.egov.adtax.web.controller.GenericController;
import org.egov.infra.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/hoarding")
public class UpdateHoardingController extends GenericController {

    @Autowired
    private HoardingService hoardingService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @ModelAttribute("hoardingSearch")
    public HoardingSearch hoardingSearch() {
        return new HoardingSearch();
    }

    @ModelAttribute("hoarding")
    public Hoarding hoarding() {
        return new Hoarding();
    }

    @RequestMapping(value = "search-for-update", method = GET)
    public String searchHoardingForm() {
        return "hoarding-search-for-update";
    }

    @RequestMapping(value = "search-for-update", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchHoarding(@ModelAttribute final HoardingSearch hoardingSearch) {
        return "{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(hoardingService.getHoardingSearchResult(hoardingSearch)) + "}";
    }

    @RequestMapping(value = "view/{hoardingNumber}")
    public String viewHoarding(@PathVariable final String hoardingNumber, final Model model) {
        model.addAttribute("hoarding", hoardingService.getHoardingByHoardingNumber(hoardingNumber));
        return "hoarding-view";
    }

    @RequestMapping(value = "update/{hoardingNumber}")
    public String updateHoarding(@PathVariable final String hoardingNumber, final Model model) {
        model.addAttribute("hoarding", hoardingService.getHoardingByHoardingNumber(hoardingNumber));
        return "hoarding-update";
    }

    @RequestMapping(value = "update/{hoardingNumber}", method = POST)
    public String updateHoarding(@PathVariable final String hoardingNumber, @Valid @ModelAttribute final Hoarding hoarding,
            final BindingResult resultBinder) {
        if (resultBinder.hasErrors())
            return "hoarding-update";
        hoardingService.updateHoarding(hoarding);
        return "redirect:/hoarding/view/";
    }
}
