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

package org.egov.mrs.web.controller.masters;

import org.egov.mrs.masters.entity.MarriageReligion;
import org.egov.mrs.masters.service.ReligionService;
import org.egov.mrs.web.adaptor.ReligionJsonAdaptor;
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

import javax.validation.Valid;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping(value = "/masters")
public class ReligionController {

    private static final String MARRIAGE_RELIGION = "marriageReligion";
    private static final String MRG_RELIGION_CREATE = "religion-create";
    private static final String MRG_RELIGION_UPDATE = "religion-update";
    private static final String MRG_RELIGION_VIEW = "religion-view";
    private static final String MRG_RELIGION_SEARCH = "religion-search";
    private static final String MRG_RELIGION_SUCCESS = "religion-success";

    @Autowired
    private ReligionService religionService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/religion/create", method = RequestMethod.GET)
    public String loadCreateForm(final Model model) {
        model.addAttribute(MARRIAGE_RELIGION, new MarriageReligion());
        return MRG_RELIGION_CREATE;
    }

    @RequestMapping(value = "/religion/create", method = RequestMethod.POST)
    public String createReligion(
            @Valid @ModelAttribute final MarriageReligion marriageReligion,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttributes) {

        if (errors.hasErrors())
            return MRG_RELIGION_CREATE;
        religionService.createReligion(marriageReligion);
        redirectAttributes.addFlashAttribute("message", messageSource
                .getMessage("msg.religion.create.success", null, null));
        return "redirect:/masters/religion/success/" + marriageReligion.getId();
    }

    @RequestMapping(value = "/religion/success/{id}", method = RequestMethod.GET)
    public String viewReligion(@PathVariable final Long id, final Model model) {
        model.addAttribute(MARRIAGE_RELIGION, religionService.findById(id));
        return MRG_RELIGION_SUCCESS;
    }

    @RequestMapping(value = "/religion/search/{mode}", method = RequestMethod.GET)
    public String getSearchPage(@PathVariable("mode") final String mode,
            final Model model) {
        model.addAttribute(MARRIAGE_RELIGION, new MarriageReligion());
        return MRG_RELIGION_SEARCH;
    }

    @RequestMapping(value = "/religion/view/{id}", method = RequestMethod.GET)
    public String viewRegistrationunit(@PathVariable("id") final Long id,
            final Model model) {
        final MarriageReligion marriageReligion = religionService.findById(id);
        model.addAttribute(MARRIAGE_RELIGION, marriageReligion);
        return MRG_RELIGION_VIEW;
    }

    @RequestMapping(value = "/religion/searchResult", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchReligionResult(final Model model,
            @ModelAttribute final MarriageReligion religion) {
        final List<MarriageReligion> searchResultList = religionService
                .searchReligions(religion);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, MarriageReligion.class, ReligionJsonAdaptor.class)).append("}").toString();
    }

    @RequestMapping(value = "/religion/edit/{id}", method = RequestMethod.GET)
    public String editReligion(@PathVariable("id") final Long id, final Model model) {
        model.addAttribute(MARRIAGE_RELIGION, religionService.findById(id));
        return MRG_RELIGION_UPDATE;
    }

    @RequestMapping(value = "/religion/update", method = RequestMethod.POST)
    public String updateReligion(
            @Valid @ModelAttribute final MarriageReligion marriageReligion,
            final BindingResult errors,
            final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors())
            return MRG_RELIGION_UPDATE;
        religionService.updateReligion(marriageReligion);
        redirectAttributes.addFlashAttribute("message", messageSource
                .getMessage("msg.religion.update.success", null, null));
        return "redirect:/masters/religion/success/" + marriageReligion.getId();
    }
}
