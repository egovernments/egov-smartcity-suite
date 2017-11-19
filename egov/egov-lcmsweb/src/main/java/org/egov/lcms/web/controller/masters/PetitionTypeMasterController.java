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
package org.egov.lcms.web.controller.masters;

import java.util.List;

import javax.validation.Valid;

import org.egov.lcms.masters.entity.PetitionTypeMaster;
import org.egov.lcms.masters.service.CourtTypeMasterService;
import org.egov.lcms.masters.service.PetitionTypeMasterService;
import org.egov.lcms.web.adaptor.PetitionTypeMasterJsonAdaptor;
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
@RequestMapping("/petitiontypemaster")
public class PetitionTypeMasterController {
    private final static String PETITIONTYPEMASTER_NEW = "petitiontypemaster-new";
    private final static String PETITIONTYPEMASTER_RESULT = "petitiontypemaster-result";
    private final static String PETITIONTYPEMASTER_EDIT = "petitiontypemaster-edit";
    private final static String PETITIONTYPEMASTER_VIEW = "petitiontypemaster-view";
    private final static String PETITIONTYPEMASTER_SEARCH = "petitiontypemaster-search";
    @Autowired
    private PetitionTypeMasterService petitionTypeMasterService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CourtTypeMasterService courtTypeMasterService;

    private void prepareNewForm(final Model model) {
        model.addAttribute("courtTypeMasters", courtTypeMasterService.getActiveCourtTypes());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("petitionTypeMaster", new PetitionTypeMaster());
        return PETITIONTYPEMASTER_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final PetitionTypeMaster petitionTypeMaster, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return PETITIONTYPEMASTER_NEW;
        }
        petitionTypeMasterService.create(petitionTypeMaster);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.petitionTypeMaster.success", null, null));
        return "redirect:/petitiontypemaster/result/" + petitionTypeMaster.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final PetitionTypeMaster petitionTypeMaster = petitionTypeMasterService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("petitionTypeMaster", petitionTypeMaster);
        return PETITIONTYPEMASTER_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final PetitionTypeMaster petitionTypeMaster, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return PETITIONTYPEMASTER_EDIT;
        }
        petitionTypeMasterService.update(petitionTypeMaster);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.petitionTypeMaster.update", null, null));
        return "redirect:/petitiontypemaster/result/" + petitionTypeMaster.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final PetitionTypeMaster petitionTypeMaster = petitionTypeMasterService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("petitionTypeMaster", petitionTypeMaster);
        return PETITIONTYPEMASTER_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model) {
        final PetitionTypeMaster petitionTypeMaster = petitionTypeMasterService.findOne(id);
        model.addAttribute("petitionTypeMaster", petitionTypeMaster);
        return PETITIONTYPEMASTER_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final PetitionTypeMaster petitionTypeMaster = new PetitionTypeMaster();
        prepareNewForm(model);
        model.addAttribute("petitionTypeMaster", petitionTypeMaster);
        return PETITIONTYPEMASTER_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final PetitionTypeMaster petitionTypeMaster) {
        final List<PetitionTypeMaster> searchResultList = petitionTypeMasterService.search(petitionTypeMaster);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(PetitionTypeMaster.class, new PetitionTypeMasterJsonAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }
}